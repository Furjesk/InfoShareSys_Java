package com.njit.infoshareserve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.njit.infoshareserve.bean.*;
import com.njit.infoshareserve.service.*;
import com.njit.infoshareserve.utils.CommentDealUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/manage")
@RestController
public class ManageController {

    @Autowired
    ReviewService reviewService;
    @Autowired
    VideoSetService videoSetService;
    @Autowired
    MomentService momentService;
    @Autowired
    MomentimgService momentimgService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    MomcommentService momcommentService;
    @Autowired
    UserService userService;
    @Autowired
    Category2Service category2Service;

    /**
     * 通过userId获取 视频集 列表
     *
     * @return
     */
    @GetMapping("/getReviewSetListByUId")
    public ResultData<List<Review>> getReviewSetListByMId(Integer userId) {
        List<Review> reviewList = reviewService.selectReviewByUId(userId);
        return ResultData.success(reviewList);
    }

    /**
     * 通过userId获取 动态 列表
     *
     * @return
     */
    @GetMapping("/getReviewMomentListByUId")
    public ResultData<List<Moment>> getReviewMomentListByUId(Integer userId) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userId", userId);
        queryWrapper.orderByDesc("createtime"); //查用户自己的用降序
        List<Moment> momentList = momentService.getBaseMapper().selectList(queryWrapper);
        Users user = userService.getOne(queryWrapper);
        //遍历，自己填充数据
        Moment moment;
        Subject subject;
        List<Momentimg> momentimgList;

        for (int i = 0; i < momentList.size(); i++) {
            moment = momentList.get(i);
            //设置动态发布者头像和用户昵称
            moment.setImgurl(user.getImgurl());
            moment.setUsername(user.getUsername());
            //根据moment的外键subjectId获取subjectName
            if (moment.getSubjectid() != null) {
                queryWrapper.clear();
                queryWrapper.eq("subjectId", moment.getSubjectid());
                subject = subjectService.getOne(queryWrapper);
                moment.setSubjectname(subject.getSubjectname());
            }
            //根据momentId获取momentimgList
            queryWrapper.clear();
            queryWrapper.eq("momentId", moment.getMomentid());
            momentimgList = momentimgService.getBaseMapper().selectList(queryWrapper);
            if (momentimgList != null) {
                moment.setMomentimgList(momentimgList);
            }
            //获取评论列表并处理数据
            /** 得到评论列表【要 处理一下 传给前端】+【加上评论者信息，自己写连接查询】 */
            List<Momcomment> originList = momcommentService.selectCommentListByMomId(moment.getMomentid());
            moment.setCommentnum(originList.size());
//            if (originList != null) { //这边判不判断无所谓，最多返回空数组，为了评论组件复用，防止出问题，就不判断了
            List<Momcomment> momcommentList = CommentDealUtil.dealMomCommentList(originList); //T = O(n)~O(n^3)
            moment.setMomcommentList(momcommentList);
//            }

        }
        return ResultData.success(momentList);
    }

    /**
     * 删除视频集
     * @param params
     * @return
     */
    @PostMapping("/deleteVideoSetBySetId")
    public ResultData<String> deleteVideoSetBySetId(@RequestBody Map<String, Integer> params) {

        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("setId", params.get("setId"));
        updateWrapper.set("status", 3); //3代表用户删除/撤销
        //修改review表
        Boolean r1 = reviewService.update(updateWrapper);
        Boolean r2 = videoSetService.update(updateWrapper);
        if (r1 && r2) {
            return ResultData.success("删除成功");
        }
        return ResultData.fail(500, "删除失败");
    }

    /**
     * 删除动态
     */
    @PostMapping("/deleteMomentByMomId")
    public ResultData<String> deleteMomentByMomId(@RequestBody Map<String, Integer> params) {
        log.info("params.momentId:{}", params.get("momentId"));

        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("momentId", params.get("momentId"));
        updateWrapper.set("status", 3); //3代表用户删除/撤销
        //修改review表
        Boolean r1 = reviewService.update(updateWrapper);
        Boolean r2 = momentService.update(updateWrapper);
        if (r1 && r2) {
            return ResultData.success("删除成功");
        }
        return ResultData.fail(500, "删除失败");
    }

    /**
     * 发布动态评论
     * @return
     */
    @PostMapping("/sendMomComment")
    public ResultData<String> sendMomComment(@RequestBody Momcomment momcomment) {
        Boolean success = momcommentService.save(momcomment);
        if(success) {
            return ResultData.success("发布评论成功");
        } else {
            return ResultData.fail(500,"发布评论失败");
        }
    }

    /**
     * 查看是否已存在该频道
     * @param map category1id,category2name
     * @return
     */
    @PostMapping("/hasCategory2")
    public ResultData<String> hasCategory2(@RequestBody Map<String, String> map) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("category1id",Integer.parseInt(map.get("category1id")));
        queryWrapper.eq("category2name",map.get("category2name"));
        Category2 category2 = category2Service.getOne(queryWrapper);
        if(category2 != null) {
            return ResultData.fail(406,"该分类已存在该频道");
        }
        return ResultData.success("可以创建");
    }

    /**
     * 创建频道
     * @param map category1id,category2name,category2brief
     * @return
     */
    @PostMapping("/createChannel")
    public ResultData<String> createChannel(@RequestBody Map<String, String> map) {

        Category2 category2 = new Category2();
        category2.setCategory1id(Integer.parseInt(map.get("category1id")));
        category2.setCategory2name(map.get("category2name"));
        category2.setCategory2brief(map.get("category2brief"));
        category2.setUserid(Integer.parseInt(map.get("userid")));
        Boolean success = category2Service.save(category2);
        if(success)
            return ResultData.success("创建成功");
        else
            return ResultData.fail(500,"创建失败");
    }

}
