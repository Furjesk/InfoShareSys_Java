package com.njit.infoshareserve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.njit.infoshareserve.bean.*;
import com.njit.infoshareserve.service.*;
import com.njit.infoshareserve.utils.NonStaticResourceHttpRequestHandler;
import com.njit.infoshareserve.utils.TimeLongUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@RequestMapping("/upload")
@RestController
//@AllArgsConstructor
@RequiredArgsConstructor
public class UploadController {

    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;
    /**
     * 指定是哪个管理员审核【待优化，随机或平均分配等】
     */
    private final Integer MANAGERID = 1;
    /**
     * 资源服务器地址
     */
    @Value("${resource-server-url}")
    private String sourcePath = "E:\\IdealWorkspace\\InfoShareResourceServer\\";
    @Value("${resource-server-request-address}")
    private String ip_addr = "http://127.0.0.1:8888";

    @Autowired
    Category1Service category1Service;
    @Autowired
    Category2Service category2Service;
    @Autowired
    LabelService labelService;
    @Autowired
    HaslabelService haslabelService;
    @Autowired
    VideoSetService videoSetService;
    @Autowired
    VideoService videoService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    MomentService momentService;
    @Autowired
    MomentimgService momentimgService;
    @Autowired
    SubjectService subjectService;

    /**
     * 通过url实现资源预览
     * http://localhost:8888/api/file/videoPreview/20210716_210945.mp4
     *
     * @param path 为 文件名.扩展名
     */
    @GetMapping("/videoPreview/{path}")
    public void videoPreview(HttpServletRequest request, HttpServletResponse response, @PathVariable String path) throws IOException, ServletException {
        path = URLDecoder.decode(path,"UTF-8");
        //假如我把视频1.mp4放在了resources下的videos文件夹里面
        //sourcePath 是获取resources文件夹的绝对地址 E:/IdealWorkspace/InfoShareServe/target/classes/
        //realPath 即是视频所在的磁盘地址
//        String sourcePath = "E:\\IdealWorkspace\\InfoShareResourceServer\\";
        String realPath = sourcePath + "resources/videos/" + path;

        Path filePath = Paths.get(realPath);
        if (Files.exists(filePath)) {
            String mimeType = Files.probeContentType((filePath)); // 获取文件类型
            if (!StringUtils.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

    @GetMapping("/setImgPreview/{path}")
    public void setImgPreview(HttpServletRequest request, HttpServletResponse response, @PathVariable String path) throws IOException, ServletException {
        path = URLDecoder.decode(path,"UTF-8");
//        String sourcePath = "E:\\IdealWorkspace\\InfoShareResourceServer\\";
        String realPath = sourcePath + "resources/setImg/" + path;

        Path filePath = Paths.get(realPath);
        if (Files.exists(filePath)) {
            String mimeType = Files.probeContentType((filePath));
            if (!StringUtils.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

    @GetMapping("/momentImgPreview/{path}")
    public void momentImgPreview(HttpServletRequest request, HttpServletResponse response, @PathVariable String path) throws IOException, ServletException {
        path = URLDecoder.decode(path,"UTF-8");
//        String sourcePath = "E:\\IdealWorkspace\\InfoShareResourceServer\\";
        String realPath = sourcePath + "resources/momentImg/" + path;

        Path filePath = Paths.get(realPath);
        if (Files.exists(filePath)) {
            String mimeType = Files.probeContentType((filePath));
            if (!StringUtils.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

    /**
     * 【上传视频的标签 和 动态的话题 都没有判断名字重复，只是根据前端传来的也没用id简答判断以下】
     */

    /**
     * userId 区分是谁上传的视频
     * 用uuid拼上去来使视频名唯一
     *
     * @return
     */
    @PostMapping("/upload-video")
    public ResultData<String> uploadVideo(@RequestParam("file") MultipartFile[] files,
                                          @RequestParam(value = "userid", required = false) Integer userid,
                                          @RequestParam(value = "existedSetId", required = false) Integer existedSetId,
                                          @RequestParam(value = "setname", required = false) String setname,
                                          @RequestParam(value = "setbrief", required = false) String setbrief,
                                          @RequestParam(value = "category1id", required = false) Integer category1id,
                                          @RequestParam(value = "category2id", required = false) Integer category2id,
                                          @RequestParam(value = "videotype", required = false) String videotype,
                                          @RequestParam(value = "cannotforward", required = false) Integer cannotforward,
                                          @RequestParam(value = "labellist", required = false) String labellist
    ) throws UnsupportedEncodingException {
//        String sourcePath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1);
//        String sourcePath = "E:\\IdealWorkspace\\InfoShareResourceServer\\";
        String realPath, contentType, typePrefix, videoLong, videoTitle;
        int i = 0;
        Integer setId = -1;
        File tempFile = null;
        Boolean success;

        File dir = new File(sourcePath + "resources/videos/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        dir = new File(sourcePath + "resources/setImg/");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        contentType = files[0].getContentType();
        // substring(0,2) ==> [0,2)
        typePrefix = contentType.substring(0, contentType.indexOf("/"));
        UUID uuid;

        // 获得当地时间
        Date date = new Date();

        /** 上传视频 */
        if (typePrefix.equals("video")) {

            Long setLong = 0L;

            /** videoset */
            VideoSet videoSet = new VideoSet();
            videoSet.setCreatetime(date);
            if (userid != null)
                videoSet.setUserid(userid);
            success = videoSetService.save(videoSet);
            if (success) {
                setId = videoSet.getSetid();
            } else {
                return ResultData.fail(500, "服务器出错:插入videoSet失败");
            }

            try {
                for (i = 0; i < files.length; i++) {
                    //uuid拼接文件名,防止文件重名(36位)(每轮生产新的uuid)
                    uuid = UUID.randomUUID();
                    realPath = sourcePath + "resources/videos/" + uuid + files[i].getOriginalFilename();

                    //存储到该路径 服务器(磁盘)的一个文件夹下
                    tempFile = new File(realPath);
                    files[i].transferTo(tempFile);

                    /** Video */
                    Video video = new Video();
                    //视频url，编码一下，否则有的特殊字符会404
                    String videoUrl = ip_addr + "/upload/videoPreview/" + URLEncoder.encode(uuid + files[i].getOriginalFilename(),"UTF-8");
                    video.setVideourl(videoUrl);
                    //原始文件名,可用于检测是否上传过一样的视频,询问一下是否继续上传(可以不用)
                    //现作为视频选集标题substring(0,contentType.indexOf("/"))
                    videoTitle = files[i].getOriginalFilename().substring(0, files[i].getOriginalFilename().lastIndexOf("."));
                    video.setVideoname(videoTitle);
                    //获取视频时长
                    if (tempFile != null) {
                        MultimediaObject multimediaObject = new MultimediaObject(tempFile);
                        MultimediaInfo info = multimediaObject.getInfo();
                        setLong += info.getDuration(); //视频集时长
                        videoLong = TimeLongUtil.millisecondToTimeString(info.getDuration());
                        video.setVideolong(videoLong);
                    }
                    //视频所属视频集id
                    video.setSetid(setId);
                    videoService.save(video);
                }

                //for循环完了,再更新videoset的setlong
                String setLongStr = TimeLongUtil.millisecondToTimeString(setLong);
                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.eq("setId", setId);
                updateWrapper.set("setLong", setLongStr);
                videoSetService.update(videoSet, updateWrapper);

            } catch (IOException | EncoderException e) {
                e.printStackTrace();
            }
        } else if (typePrefix.equals("image")) {
            //上传图片 以及 视频集属性
            uuid = UUID.randomUUID();
            setId = existedSetId;
            realPath = sourcePath + "resources/setImg/" + uuid + files[i].getOriginalFilename();

            //存储到该路径 服务器(磁盘)的一个文件夹下
            try {
                files[0].transferTo(new File(realPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            /** videoset */
            //这里再更新一下videset的setImg的url
            String setImgUrl = ip_addr + "/upload/setImgPreview/" + URLEncoder.encode(uuid + files[i].getOriginalFilename(),"UTF-8");
            VideoSet videoSet = videoSetService.getById(setId);
            videoSet.setSetimg(setImgUrl);
            //解码
            if (setname != null)
                videoSet.setSetname(setname);
            if (setbrief != null)
                videoSet.setSetbrief(setbrief);
            if (category1id != null)
                videoSet.setCategory1id(category1id);
            if (category2id != null)
                videoSet.setCategory2id(category2id);
            if (videotype != null)
                videoSet.setVideotype(videotype);
            if (cannotforward != null)
                videoSet.setCannotforward(cannotforward);

            videoSetService.updateById(videoSet);

            /** label */
            Label label;
            List<Integer> labelIds = new ArrayList<>();
            if (labellist != null) {
                String[] labels = labellist.split(",");

                for (i = 0; i < labels.length; i++) {
                    String[] split2 = labels[i].split("-");
                    //用户输入的，数据库不一定没有
                    if (split2[2].equals("null")) {
                        QueryWrapper queryWrapper = new QueryWrapper();
                        queryWrapper.eq("labelName",split2[1]);
                        label = labelService.getOne(queryWrapper);
                        if(label == null) {
                            //数据库是真没有
                            label = new Label();
                            label.setLabelname(split2[1]);
                            label.setCategory2id(Integer.parseInt(split2[0]));
                            success = labelService.save(label);
                            if (success) {
                                labelIds.add(label.getLabelid());
                            } else {
                                return ResultData.fail(500, "服务器出错:插入label失败");
                            }
                        }else {
                            labelIds.add(label.getLabelid());
                        }
                    }
                    //数据库有，不用插入，直接获取labelid
                    else {
                        labelIds.add(Integer.parseInt(split2[2]));
                    }

                }
            }

            /** haslabel */
            Haslabel haslabel = new Haslabel();

            for (i = 0; i < labelIds.size(); i++) {
                haslabel.setSetid(setId);
                haslabel.setLabelid(labelIds.get(i));
                //1表示set；2表示moment；3表示group
                haslabel.setTargettype(1);
                haslabelService.save(haslabel);
            }

            /** review表 */
            Review review = new Review();
            //默认全给1
            review.setManagerid(MANAGERID);
            review.setSetid(setId);
            //表示是setid
            review.setTargettype(1);
            //表示待审核
            review.setStatus(0);
            review.setCreatetime(date);
            reviewService.save(review);

        } else {
            //格式错误
            return ResultData.fail(406, "上传格式错误");
        }

        return ResultData.success(setId.toString());
    }

    /**
     * 上传动态
     * @RequestParam(value = "labellist", required = false) String labellist
     * @return
     */
    @PostMapping("/upload-moment")
    public ResultData<Integer> uploadMoment(@RequestParam(value = "file", required = false) MultipartFile[] files,
                                            @RequestParam("textarea") String textarea,
                                            @RequestParam("userId") Integer userId,
                                            @RequestParam("canComment") Integer canComment,
                                            @RequestParam(value = "subjectId",required = false) Integer subjectId,
                                            @RequestParam(value = "subjectName",required = false) String subjectName
                                            ) {
//        String sourcePath = "E:\\IdealWorkspace\\InfoShareResourceServer\\";
        String realPath;
        int i = 0;
        Integer momentId = -1;
        File tempFile = null;
        Boolean success;

        File dir = new File(sourcePath + "resources/momentImg/");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        Date date = new Date();
        UUID uuid = UUID.randomUUID();
        /** moment */
        Moment moment = new Moment();
        moment.setContent(textarea);
        moment.setCreatetime(date);
        moment.setUserid(userId);
        moment.setCancomment(canComment); //0表示不允许评论；1表示允许评论
        moment.setStatus(0); //0表示待审核；1表示通过审核；2表示退回；3表示用户删除/撤销
        //直接选的数据库有的subject，插入即可
        if(subjectId!=null) {
            moment.setSubjectid(subjectId);
        }
        else if(subjectName!=null) {
            //先插入subject
            Subject subject;
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("subjectName",subjectName);
            subject = subjectService.getOne(queryWrapper);
            if(subject==null) {
                //数据库没有该话题，则插入
                subject = new Subject();
                subject.setSubjectname(subjectName);
                subject.setUserid(userId);
                subject.setCreatetime(date);
                if(subjectService.save(subject)) {
                    //再插入moment
                    moment.setSubjectid(subject.getSubjectid());
                }
            } else {
                //数据库有该话题，直接插入moment
                moment.setSubjectid(subject.getSubjectid());
            }
        }
        success = momentService.save(moment);
        if (success) {
            momentId = moment.getMomentid();
        } else {
            return ResultData.fail(500, "上传动态失败");
        }

        /** review审核表 */
        Review review = new Review();
        review.setManagerid(MANAGERID);
        review.setMomentid(momentId);
        review.setTargettype(2); //1表示setid；2表示momentid
        review.setStatus(0);
        review.setCreatetime(date);
        reviewService.save(review);

        /** momentImg */
        if (files != null) {

            String momentImgUrl;
            for (i = 0; i < files.length; i++) {
                realPath = sourcePath + "resources/momentImg/" + uuid + files[i].getOriginalFilename();

                //存储到该路径 服务器(磁盘)的一个文件夹下
                tempFile = new File(realPath);
                try {
                    files[i].transferTo(tempFile);
                    /** momentImg */
                    Momentimg momentimg = new Momentimg();
                    momentimg.setMomentid(momentId);
                    momentImgUrl = ip_addr + "/upload/momentImgPreview/" + URLEncoder.encode(uuid + files[i].getOriginalFilename(),"UTF-8");
                    momentimg.setMomimgurl(momentImgUrl);
                    momentimgService.save(momentimg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return ResultData.success(momentId);
    }

    /**
     * 获取分类列表，封装好格式
     */
    @GetMapping("/getCategoryList")
    public ResultData<List<Category1>> getCategoryList() {
        List<Category1> category1List = category1Service.getBaseMapper().selectList(null);
        for (int i = 0; i < category1List.size(); i++) {
            List<Category2> category2List = category2Service.selectByCategory1Id(category1List.get(i).getCategory1id());
            category1List.get(i).setCategory2List(category2List);
        }
        return ResultData.success(category1List);
    }

    /**
     * 根据分类2id 获取标签列表数据【可优化：后端根据 视频内容 来推荐一些标签，这个太难了，可选】
     */
    @GetMapping("/getLabelListByC2Id/{category2Id}")
    public ResultData<List<Label>> getLabelList(@PathVariable("category2Id") Integer category2Id) {
//        List<Integer> idList;
        /**
         * 这边优化为总label个数划分10个区域，每个区域随机挑选一个labelid
         * 或者更难的：根据视频内容推荐
         */
//        idList = Arrays.asList(1,2,3,4,5,6,8,9,10,12);
//        List<Label> labelList = labelService.selectLabelsByIds(idList);
        //只获取前10个
        List<Label> labelList = labelService.selectLabelsByC2Id(category2Id);

        return ResultData.success(labelList);
    }

    /**
     * 获取话题列表
     * 可改进为向下滑动，多次获取
     */
    @GetMapping("/getSubjectList")
    public ResultData<List<Subject>> getSubjectList() {

        List<Subject> subjectList = subjectService.getBaseMapper().selectList(null);

        return ResultData.success(subjectList);
    }

}
