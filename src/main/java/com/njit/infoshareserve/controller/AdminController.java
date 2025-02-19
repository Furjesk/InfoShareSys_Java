package com.njit.infoshareserve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.njit.infoshareserve.bean.*;
import com.njit.infoshareserve.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/admin")
@RestController
public class AdminController {

    @Autowired
    ReviewService reviewService;
    @Autowired
    VideoSetService videoSetService;
    @Autowired
    ManagersService managersService;
    @Autowired
    MomentimgService momentimgService;
    @Autowired
    MomentService momentService;

    @PostMapping("/login")
    public ResultData<Managers> userLogin(@RequestBody Managers manager){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userName",manager.getUsername());
        // 数据库中的user
        Managers dbManager = managersService.getOne(queryWrapper);

        if(dbManager != null && manager.getUserpwd().equals(dbManager.getUserpwd())){
            dbManager.setUserpwd("");
//            dbManager.setToken("this_is_a_token");
            return ResultData.success(dbManager);
        }
        return ResultData.fail(401,"后端：用户名或密码错误！");
    }

    /**
     * 通过managerId获取待该管理员审核的视频集列表
     * @return
     */
    @GetMapping("/getReviewSetListByMId")
    public ResultData<List<Review>> getReviewSetListByMId(Integer managerId){
        List<Review> reviewList = reviewService.selectReviewByMId(managerId);
        return ResultData.success(reviewList);
    }

    /**
     * 通过managerId获取 审核动态 列表
     * 动态发布后不可编辑，直接删除重发，因此审核表不会出现reviewid不同而momentid相同【圈起来，要考的，可直接通过momentid来更新review表】
     * @return
     */
    @GetMapping("/getReviewMomentListByMId")
    public ResultData<List<Moment>> getReviewMomentListByMId(Integer managerId) {

        QueryWrapper queryWrapper = new QueryWrapper();

        List<Integer> momentIdList = reviewService.selectMomIdListByMId(managerId);
        List<Moment> momentList = reviewService.selectReviewByMomIds(momentIdList);

        //遍历，自己填充图片数据
        Moment moment;
        List<Momentimg> momentimgList;
        for (int i = 0; i < momentList.size(); i++) {
            moment = momentList.get(i);
            //根据momentId获取momentimgList
            queryWrapper.clear();
            queryWrapper.eq("momentId", moment.getMomentid());
            momentimgList = momentimgService.getBaseMapper().selectList(queryWrapper);
            if (momentimgList != null) {
                moment.setMomentimgList(momentimgList);
            }
        }
        return ResultData.success(momentList);
    }

    /**
     * 传入params包括：审核视频的ID List，以及要进行的操作（通过/退回）
     */
    @PostMapping("/videoSetOperation")
    public ResultData<String> videoSetOperation(@RequestBody Map<String,Object> params){
        List<Integer> reviewIdList = (List<Integer>) params.get("reviewIdList");
        if(params.get("operation").equals("pass")){
            //审核通过
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.in("reviewId",reviewIdList);
            //0表示待审核；1表示通过审核；2表示退回（3表示用户删除）
            updateWrapper.set("status",1);
            updateWrapper.set("dealTime",new Date());
            //更新审核表和视频集表
            reviewService.update(updateWrapper);
            videoSetService.updateSetStatusByReviewIds(1,reviewIdList);

        } else if(params.get("operation").equals("deny")) {
            //退回
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.in("reviewId",reviewIdList);
            //0表示待审核；1表示通过审核；2表示退回（3表示用户删除）
            updateWrapper.set("status",2);
            updateWrapper.set("dealTime",new Date());
            reviewService.update(updateWrapper);
            videoSetService.updateSetStatusByReviewIds(2,reviewIdList);

        } else {
            return ResultData.fail(400,"非法操作");
        }
        return ResultData.success("操作成功");
    }

    /**
     * 传入params包括：动态的ID List，以及要进行的操作（通过/退回）
     */
    @PostMapping("/momentOperation")
    public ResultData<String> momentOperation(@RequestBody Map<String,Object> params){
        List<Integer> momentIdList = (List<Integer>) params.get("momentIdList");
        if(params.get("operation").equals("pass")){
            //审核通过
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.in("momentId",momentIdList);
            //0表示待审核；1表示通过审核；2表示退回（3表示用户删除）
            updateWrapper.set("status",1);
            updateWrapper.set("dealTime",new Date());
            //更新审核表和视频集表
            reviewService.update(updateWrapper);
            momentService.updateMomStatusByMomIds(1,momentIdList);

        } else if(params.get("operation").equals("deny")) {
            //退回
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.in("momentId",momentIdList);
            //0表示待审核；1表示通过审核；2表示退回（3表示用户删除）
            updateWrapper.set("status",2);
            updateWrapper.set("dealTime",new Date());
            reviewService.update(updateWrapper);
            momentService.updateMomStatusByMomIds(2,momentIdList);

        } else {
            return ResultData.fail(400,"非法操作");
        }
        return ResultData.success("操作成功");
    }
}
