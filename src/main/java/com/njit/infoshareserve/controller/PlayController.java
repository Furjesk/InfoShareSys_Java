package com.njit.infoshareserve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.njit.infoshareserve.bean.*;
import com.njit.infoshareserve.recommend.OnlineRecommend;
import com.njit.infoshareserve.recommend.StatisticalRecommend;
import com.njit.infoshareserve.service.*;
import com.njit.infoshareserve.utils.TimeLongUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RequestMapping("/play")
@RestController
public class PlayController {

    @Autowired
    VideoService videoService;
    @Autowired
    VideoSetService videoSetService;
    @Autowired
    SetCommentService setcommentService;
    @Autowired
    HaslabelService haslabelService;
    @Autowired
    LabelService labelService;
    @Autowired
    SetCommentService setCommentService;
    @Autowired
    LikesetService likesetService;
    @Autowired
    RatesetService ratesetService;
    @Autowired
    OnlineRecommend onlineRecommend;
    @Autowired
    SetSimilarService setSimilarService;
    @Autowired
    StatisticalRecommend statisticalRecommend;
    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;
    @Autowired
    WatchService watchService;

    /**
     * 通过setId、userId获取所有相关的信息，包括评论、videoList、videoSet等【这里的评论列表或许不需要三层遍历？将连接查询字段写全起别名可以吗？只需二层循环】
     * 还得获取初始推荐列表，从相似度列表获取
     * @rqmap:  {userId=1, setId=11}
     * @return
     */
    @PostMapping("/getSetInfoBySetId")
    public ResultData<Map<String,Object>> getSetInfoBySetId(@RequestBody Map<String,Integer> rqMap)  {
        /** 得到videoSet【要传给前端】+【加上up主信息，自己写连接查询】 */
        VideoSet videoSet = videoSetService.selectVideoSetBySetId(rqMap.get("setId"));

        /** 得到评论列表【要 处理一下 传给前端】+【加上评论者信息，自己写连接查询】 */
        List<SetComment> originList = setcommentService.selectCommentListBySetId(rqMap.get("setId"));
        /** 处理评论数据【如果评论为空，循环不执行，返回评论列表就是空数组，不会报错】 */
        /** 这边可以用CommentDealUtil里的方法吧 */
        List<SetComment> commentList = new ArrayList<>();
        List<SetComment> tempSubList;
        SetComment tempTopComment, tempSubComment;
        Integer fatherId = -1;
        for (int i = 0; i < originList.size(); i++) {
            if(originList.get(i).getCommentlevel()==1) {
                //是顶级评论，直接加入commentList
                commentList.add(originList.get(i));
            } else {
                //不是顶级评论，则在commentList遍历找它的顶级评论，并加入
                for (int j = 0; j < commentList.size(); j++) {
                    if(commentList.get(j).getCommentid()==originList.get(i).getTopcommentid()){
                        //用tempComment暂存顶级评论
                        tempTopComment = commentList.get(j);

                        if(tempTopComment.getSubcommentlist()!=null)
                            tempSubList = tempTopComment.getSubcommentlist();
                        else
                            tempSubList = new ArrayList<>();

                        //用tempSubComment暂存子级评论
                        tempSubComment = originList.get(i);
                        if(tempSubComment.getCommentlevel()==2) {
                            //如果是二级评论，直接加入子评论列表
                            tempSubList.add(tempSubComment);
                        } else {
                            //如果是三级 三级以上子评论，通过父评论id找到父评论者的username【而父评论者一定在该顶级评论的子评论列表中】
                            fatherId = tempSubComment.getFatherid();
                            for (int k = 0; k < tempSubList.size(); k++) {
                                if(tempSubList.get(k).getCommentid() == fatherId) {
                                    tempSubComment.setFatherusername(tempSubList.get(k).getUsername());//父评论用户名
                                    tempSubComment.setFatheruserid(tempSubList.get(k).getUserid());//父评论用户ID
                                    break;
                                }
                            }
                            tempSubList.add(tempSubComment);
                        }
//                        tempSubList.add(originList.get(i));

                        //顶级评论tempComment更新
                        tempTopComment.setSubcommentlist(tempSubList);
                        //commentList更新
                        commentList.set(j,tempTopComment);
                        break;
                    }
                }
            }
        }

        /** 得到标签联系列表 */
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("setId",rqMap.get("setId"));
        queryWrapper.select("labelId");
        List<Haslabel> hasLabelList = haslabelService.getBaseMapper().selectList(queryWrapper);
        /** 得到标签列表【要传给前端】 */
        List<Integer> labelIdList = new ArrayList<>();
        for (int i = 0; i < hasLabelList.size(); i++) {
            labelIdList.add(hasLabelList.get(i).getLabelid());
        }
        queryWrapper.clear();
        queryWrapper.in("labelId",labelIdList);
        List<Label> labelList = labelService.getBaseMapper().selectList(queryWrapper);

        /** 得到视频列表【要传给前端】 */
        queryWrapper.clear();
        queryWrapper.eq("setId",rqMap.get("setId"));
        List<Video> videoList = videoService.getBaseMapper().selectList(queryWrapper);

        /** 得到推荐列表 */
//        queryWrapper.clear();
//        queryWrapper.eq("setId",rqMap.get("setId"));
        List<VideoSet> recommendSetList = new ArrayList<>();
        // 如果set_similar没有该setId，那么说明没有LFM，采用统计推荐
        if(setSimilarService.getOne(queryWrapper) == null) {
            VideoSet set = videoSetService.getById(rqMap.get("setId"));
            // 根据category2Id查找该分类点赞前十名
            recommendSetList = statisticalRecommend.getCategory2Top10(set.getCategory2id());
        } else {
            // 否则采用离线推荐
            List<Integer> recSetIdList = onlineRecommend.getCandidateSetIdList(rqMap.get("setId"),null);
            VideoSet videoSet1;
            for (int i = 0; i < recSetIdList.size(); i++) {
                videoSet1 = videoSetService.getSetAndUsernameBySetId(recSetIdList.get(i));
                recommendSetList.add(videoSet1);
            }
//            recommendSetList = videoSetService.selectSetsByIds(recSetIdList);
        }

        /** 得到关注情况 */
        queryWrapper.clear();
        queryWrapper.eq("followId",videoSet.getUserid()); //UP主ID
        queryWrapper.eq("followerId",rqMap.get("userId")); //粉丝ID
        Follow follow = followService.getOne(queryWrapper);

        /** 得到当前用户对当前视频集的点赞情况 */
        Likeset likeset;
        queryWrapper.clear();
        queryWrapper.eq("userId",rqMap.get("userId"));
        queryWrapper.eq("setId",rqMap.get("setId"));
        likeset = likesetService.getOne(queryWrapper);

        /** 得到评分数据 */
        Rateset rateset;
        rateset = ratesetService.getOne(queryWrapper);

        /** 得到历史记录 */
        Watch watchHistory = watchService.getOne(queryWrapper);

        Map<String,Object> map = new HashMap<>();
        map.put("videoSet",videoSet);
        map.put("commentList",commentList);
        map.put("labelList",labelList);
        map.put("videoList",videoList);
        map.put("recommendSetList",recommendSetList);
        //点赞数据
        if(likeset == null) {
            //之前从未点赞过
            map.put("isLike",false);
        } else if(likeset.getStatus()==0) {
            //取消点赞
            map.put("isLike",false);
        } else {
            map.put("isLike",true);
        }
        //评分数据
        if(rateset == null) {
            map.put("rate",0);
        } else {
            map.put("rate",rateset.getRate());
        }
        //关注情况
        if(follow == null || follow.getStatus() == 0) {
            map.put("followStatus",0);
        } else if(follow.getStatus() == 1) {
            queryWrapper.clear();
            queryWrapper.eq("followerId",videoSet.getUserid()); //UP主ID
            queryWrapper.eq("followId",rqMap.get("userId")); //粉丝ID
            follow = followService.getOne(queryWrapper);
            if(follow != null && follow.getStatus() == 1)
                map.put("followStatus",2);
            else
                map.put("followStatus",1);
        }
        //观看历史
        if(watchHistory != null)
            map.put("watchLocation",watchHistory.getWatchlocation());
        else
            map.put("watchLocation",null);

        return ResultData.success(map);
    }

    /**
     * 获取实时推荐列表
     * @return
     */
//    @PostMapping("/getOnlineRecSets")
//    public ResultData<List<VideoSet>> getOnlineRecSets(@RequestBody Map<String,Integer> rqMap) {
    public List<VideoSet> getOnlineRecSets(Integer userId, Integer setId, SetSimilar setSimilar) {
        List<Integer> recSetIdList = onlineRecommend.getRecommendIdList(userId, setId, setSimilar);
        List<VideoSet> recSetList = new ArrayList<>();
        VideoSet videoSet1;
        for (int i = 0; i < recSetIdList.size(); i++) {
            videoSet1 = videoSetService.getSetAndUsernameBySetId(recSetIdList.get(i));
            recSetList.add(videoSet1);
        }
        return recSetList;
    }

    @PostMapping("/sendSetComment")
    public ResultData<String> sendSetComment(@RequestBody SetComment setComment){
        Boolean success = setCommentService.save(setComment);
        if(success) {
            return ResultData.success("发布评论成功");
        } else {
            return ResultData.fail(500,"发布评论失败");
        }
    }

    /**
     * 点赞视频集
     * 修改/插入likeset表；videoset表的likeNum由触发器完成，还是代码实现吧，触发器不好搞，因为一开始没有likeset
     * 每次修改createtime，近期热门需要用
     */
    @PostMapping("/likeSet")
    public ResultData<String> likeSet(@RequestBody Map<String,Integer> map){
        Likeset likeset;
        Date date = new Date();
        UpdateWrapper updateWrapper = new UpdateWrapper();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userId",map.get("userId"));
        queryWrapper.eq("setId",map.get("setId"));
        likeset = likesetService.getOne(queryWrapper);
        if(likeset==null){
            //之前从未点过赞
            likeset = new Likeset();
            likeset.setUserid(map.get("userId"));
            likeset.setSetid(map.get("setId"));
            likeset.setCreatetime(date);
            //1表示点赞，0表示取消点赞
            likeset.setStatus(1);
            likesetService.save(likeset);
            //videoset的likeNum+1
            updateWrapper.eq("setId",map.get("setId"));
            updateWrapper.setSql("likeNum = likeNum + 1");
            videoSetService.update(updateWrapper);
        } else {
            //之前点过赞
            updateWrapper.clear();
            updateWrapper.eq("userId",map.get("userId"));
            updateWrapper.eq("setId",map.get("setId"));
            updateWrapper.set("createtime",date);
            if(likeset.getStatus()==1) {
                updateWrapper.set("status",0);
                likesetService.update(updateWrapper);
                //videoset的likeNum-1
                updateWrapper.clear();
                updateWrapper.eq("setId",map.get("setId"));
                updateWrapper.setSql("likeNum = likeNum - 1");
            } else {
                updateWrapper.set("status",1);
                likesetService.update(updateWrapper);
                //videoset的likeNum+1
                updateWrapper.clear();
                updateWrapper.eq("setId",map.get("setId"));
                updateWrapper.setSql("likeNum = likeNum + 1");
            }
            videoSetService.update(updateWrapper);
        }
        return ResultData.success("点赞操作成功");
    }

    /**
     * 评分视频集，然后返回 实时推荐列表
     */
    @PostMapping("/rateSet")
    public ResultData<List<VideoSet>> rateSet(@RequestBody Map<String,Integer> map){
        Rateset rateset;
        Date date = new Date();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userId",map.get("userId"));
        queryWrapper.eq("setId",map.get("setId"));
        rateset = ratesetService.getOne(queryWrapper);
        if(rateset==null){
            //之前从未评分
            rateset = new Rateset();
            rateset.setUserid(map.get("userId"));
            rateset.setSetid(map.get("setId"));
            rateset.setCreatetime(date);
            //评分
            rateset.setRate(map.get("rate"));
            ratesetService.save(rateset);
        } else {
            //之前评过分
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("userId",map.get("userId"));
            updateWrapper.eq("setId",map.get("setId"));
            updateWrapper.set("createtime",date);
            updateWrapper.set("rate",map.get("rate"));
            ratesetService.update(updateWrapper);
        }

        queryWrapper.clear();
        queryWrapper.eq("setId",map.get("setId"));
        List<VideoSet> onlineRecSets = null;
        // 如果set_similar没有该setId，那么说明没有LFM，就返回空的，前端判断一下，空的就不更新
        SetSimilar setSimilar = setSimilarService.getOne(queryWrapper);
        if(setSimilar != null) {
            // 评分成功后，获取实时推荐视频列表
            // 传入setSimilar是为了防止多次重复查询
            onlineRecSets = getOnlineRecSets(map.get("userId"), map.get("setId"), setSimilar);
            //这里再根据分类和up主排序（因为实时推荐算法一点都不明显，数据都不变化）

        }

        return ResultData.success(onlineRecSets);
    }

    /**
     * 关注/取消关注用户
     * @param map followId被关注者，followerId粉丝，status关注状态
     */
    @PostMapping("/followUP")
    public ResultData<String> followUP(@RequestBody Map<String,Integer> map) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("followId",map.get("followId"));
        queryWrapper.eq("followerId",map.get("followerId"));
        Follow follow = followService.getOne(queryWrapper);
        Date date = new Date();
        if(follow == null) { // 之前没有关注过
            follow = new Follow();
            follow.setFollowid(map.get("followId"));
            follow.setFollowerid(map.get("followerId"));
            follow.setStatus(map.get("status")); //status=1
            follow.setFollowtime(date);
            followService.save(follow);
            // 更新两个用户
            UpdateWrapper updateWrapper = new UpdateWrapper();
            // 被关注者
            updateWrapper.eq("userId",map.get("followId"));
            updateWrapper.setSql("followerNum = followerNum + 1"); // 粉丝数+1
            userService.update(updateWrapper);
            // 关注者or粉丝
            updateWrapper.clear();
            updateWrapper.eq("userId",map.get("followerId"));
            updateWrapper.setSql("followNum = followNum + 1"); // 关注数+1
            userService.update(updateWrapper);
        } else { // 之前关注过，数据库存在，直接修改status
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("followId",map.get("followId"));
            updateWrapper.eq("followerId",map.get("followerId"));
            updateWrapper.set("status",map.get("status"));
            updateWrapper.set("followtime",date);
            followService.update(updateWrapper);

            // 更新两个用户
            // 被关注者
            updateWrapper.clear();
            updateWrapper.eq("userId",map.get("followId"));
            if(map.get("status") == 1)
                updateWrapper.setSql("followerNum = followerNum + 1"); // 粉丝数+1
            else
                updateWrapper.setSql("followerNum = followerNum - 1"); // 粉丝数-1
            userService.update(updateWrapper);
            // 关注者or粉丝
            updateWrapper.clear();
            updateWrapper.eq("userId",map.get("followerId"));
            if(map.get("status") == 1)
                updateWrapper.setSql("followNum = followNum + 1"); // 关注数+1
            else
                updateWrapper.setSql("followNum = followNum - 1"); // 关注数-1
            userService.update(updateWrapper);
        }
        return ResultData.success("操作成功");
    }

    @PostMapping("/getSaveSetHistory")
    public ResultData<String> getSaveSetHistory(@RequestBody Map<String,Object> map) {
        Watch watch;
        String location = map.get("location").toString();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userId",Integer.parseInt(map.get("userId").toString()) );
        queryWrapper.eq("setId",Integer.parseInt(map.get("setId").toString()) );
        watch = watchService.getOne(queryWrapper);
        if(watch == null) { // 没有历史记录
            watch = new Watch();
            watch.setUserid(Integer.parseInt(map.get("userId").toString()));
            watch.setSetid(Integer.parseInt(map.get("setId").toString()));

            if(location.indexOf('.') != -1)
                watch.setWatchlocation( TimeLongUtil.secondToTimeString(Long.parseLong(location.substring(0, location.indexOf('.')))) );
            else
                watch.setWatchlocation( TimeLongUtil.secondToTimeString(Long.parseLong(location)) );
            watch.setWatchtime(new Date());
            watchService.save(watch);
        } else { //有历史记录，更新观看时长
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("userId",Integer.parseInt(map.get("userId").toString()) );
            updateWrapper.eq("setId",Integer.parseInt(map.get("setId").toString()) );

            if(location.indexOf('.') != -1)
                updateWrapper.set("watchLocation",TimeLongUtil.secondToTimeString(Long.parseLong(location.substring(0, location.indexOf('.')))) );
            else
                updateWrapper.set("watchLocation",TimeLongUtil.secondToTimeString(Long.parseLong(location)) );
            updateWrapper.set("watchtime",new Date());
            watchService.update(updateWrapper);
        }
        return ResultData.success("历史记录保存成功");
    }

}
