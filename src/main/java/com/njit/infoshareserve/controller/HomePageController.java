package com.njit.infoshareserve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.njit.infoshareserve.bean.*;
import com.njit.infoshareserve.service.*;
import com.njit.infoshareserve.utils.CommentDealUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/homePage")
public class HomePageController {

    @Autowired
    UserService userService;
    @Autowired
    MomentService momentService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    MomcommentService momcommentService;
    @Autowired
    MomentimgService momentimgService;
    @Autowired
    VideoSetService videoSetService;
    @Autowired
    FollowService followService;

    /** 获取UP主页的已通过动态列表 */
    @GetMapping("/getUserMoment/{userId}")
    public ResultData<List<Moment>> getUserMoment(@PathVariable(value = "userId") Integer userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userId", userId);
        Users user = userService.getOne(queryWrapper);

        queryWrapper.eq("status",1);
        queryWrapper.orderByDesc("createtime"); //查用户自己的用降序
        List<Moment> momentList = momentService.getBaseMapper().selectList(queryWrapper);

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

            List<Momcomment> momcommentList = CommentDealUtil.dealMomCommentList(originList); //T = O(n)~O(n^3)
            moment.setMomcommentList(momcommentList);

        }
        return ResultData.success(momentList);
    }

    /** 获取UP主页的已通过视频集列表 */
    @GetMapping("/getUserVideoSet/{userId}")
    public ResultData<List<VideoSet>> getUserVideoSet(@PathVariable(value = "userId") Integer userId) {
        List<VideoSet> videoSetList = videoSetService.getVideoSetListByUId(userId);
        return ResultData.success(videoSetList);
    }

    /** 获取UP主页的关注列表，并且获得当前用户是否关注这些人 */
    @PostMapping("/getFollowList")
    public ResultData<List<Users>> getFollowList(@RequestBody Map<String,Integer> map) {
        //获取该UP的关注列表
        List<Users> followList = followService.getFollowList(map.get("UPId"));
        //如果当前UP是自己，看看是否是相互关注。否则遍历列表，判断当前用户是否关注了这些人
        if(map.get("UPId") != map.get("userId")) {
            Follow follow;
            Users user;
            QueryWrapper queryWrapper = new QueryWrapper();
            for (int i = 0; i < followList.size(); i++) {
                user = followList.get(i);
                queryWrapper.clear();
                queryWrapper.eq("followerId",map.get("userId")); // 当前用户
                queryWrapper.eq("followId",user.getUserid()); // UP关注列表的人
                follow = followService.getOne(queryWrapper);
                if(follow == null || follow.getStatus() == 0) {
                    user.setIsFollow(0);
                    followList.set(i,user);
                } else if(follow.getStatus() == 1) {
                    //说明当前用户关注了他
                    //他关注当前用户了吗？
                    queryWrapper.clear();
                    queryWrapper.eq("followId",map.get("userId")); // 当前用户
                    queryWrapper.eq("followerId",user.getUserid()); // UP关注列表的人
                    follow = followService.getOne(queryWrapper);
                    if(follow != null && follow.getStatus() == 1) {
                        user.setIsFollow(2);
                        followList.set(i,user);
                    }
                }
            }
        } else {
            //看看是否是相互关注
            Follow follow;
            Users user;
            QueryWrapper queryWrapper = new QueryWrapper();
            for (int i = 0; i < followList.size(); i++) {
                user = followList.get(i);
                queryWrapper.clear();
                queryWrapper.eq("followId",map.get("userId")); // 当前用户==UP
                queryWrapper.eq("followerId",user.getUserid()); // UP关注列表的人
                follow = followService.getOne(queryWrapper);
                if(follow != null) {
                    user.setIsFollow(2);
                    followList.set(i,user);
                } //否则就是1，不变
            }
        }
        return ResultData.success(followList);
    }

    /** 获取UP主页的关注列表，并且获得当前用户是否关注这些人 */
    @PostMapping("/getFollowerList")
    public ResultData<List<Users>> getFollowerList(@RequestBody Map<String,Integer> map) {
        //获取该UP的粉丝列表
        List<Users> followList = followService.getFollowerList(map.get("UPId"));
        //如果当前UP是自己，直接返回。否则遍历列表，判断当前用户是否关注了这些人
            Follow follow;
            Users user;
            QueryWrapper queryWrapper = new QueryWrapper();
            for (int i = 0; i < followList.size(); i++) {
                user = followList.get(i);
                queryWrapper.clear();
                queryWrapper.eq("followerId",map.get("userId")); // 当前用户
                queryWrapper.eq("followId",user.getUserid()); // UP粉丝列表的人
                follow = followService.getOne(queryWrapper);
                if(follow == null || follow.getStatus() == 0) {
                    user.setIsFollow(0);
                } else if(follow.getStatus() == 1) {
                    //说明当前用户关注了他
                    //他关注当前用户了吗？
                    queryWrapper.clear();
                    queryWrapper.eq("followId",map.get("userId")); // 当前用户
                    queryWrapper.eq("followerId",user.getUserid()); // UP粉丝列表的人
                    follow = followService.getOne(queryWrapper);
                    if(follow != null && follow.getStatus() == 1) {
                        user.setIsFollow(2);
                    } else {
                        user.setIsFollow(1);
                    }
                }
                followList.set(i,user);
            }
        return ResultData.success(followList);
    }

    /** 获取UP信息 */
    @PostMapping("/getUpUserInfo")
    public ResultData<Users> getUpUserInfo(@RequestBody Map<String,Integer> map) {
        Users UP = userService.getById(map.get("UPId"));
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("followId",map.get("UPId"));
        queryWrapper.eq("followerId",map.get("userId"));
        Follow follow = followService.getOne(queryWrapper);
        if(follow == null || follow.getStatus() == 0) {
            //当前用户没有关注UP
            UP.setIsFollow(0);
        } else if(follow.getStatus() == 1) {
            //当前用户关注了UP
            queryWrapper.clear();
            queryWrapper.eq("followerId",map.get("UPId"));
            queryWrapper.eq("followId",map.get("userId"));
            follow = followService.getOne(queryWrapper);
            if(follow != null && follow.getStatus() == 1) {
                //UP也关注了当前用户
                UP.setIsFollow(2);
            } else {
                UP.setIsFollow(1);
            }
        }
        return ResultData.success(UP);
    }

}
