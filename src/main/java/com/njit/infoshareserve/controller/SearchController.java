package com.njit.infoshareserve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.njit.infoshareserve.bean.*;
import com.njit.infoshareserve.service.MomentService;
import com.njit.infoshareserve.service.MomentimgService;
import com.njit.infoshareserve.service.UserService;
import com.njit.infoshareserve.service.VideoSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/search")
@RestController
public class SearchController {

    @Autowired
    VideoSetService videoSetService;
    @Autowired
    UserService userService;
    @Autowired
    MomentService momentService;
    @Autowired
    MomentimgService momentimgService;

    /**
     * 根据搜索的keyword查询videoSet列表、用户列表等，处理后返回给前端
     * 请求路径示例：http://localhost:8888/search/searchByKeyword?keyword=标题
     * @return
     */
    @GetMapping("/searchByKeyword")
    public ResultData<Map<String,Object>> searchByKeyword(String keyword) {
        Map<String,Object> map = new HashMap<>();

        //获取视频集列表
        //设计连接查询，需自己写SQL
        List<VideoSet> videoSetList = videoSetService.searchAllByKeyword("%"+keyword+"%");

        //获取用户列表
        QueryWrapper queryWrapper = new QueryWrapper();
        //like查询是使用的默认方式，也就是说在查询条件的左右两边都有%：NAME = ‘%王%'；
        queryWrapper.like("userName",keyword);
        List<Users> userList = userService.getBaseMapper().selectList(queryWrapper);

        //获取动态列表，涉及连接查询
        List<Moment> momentList = momentService.searchMomentListByKey("%"+keyword+"%");
        //放入动态的第一张图片
        Moment moment;
        List<Momentimg> momentimgList;
        Momentimg momentimg;
        for (int i = 0; i < momentList.size(); i++) {
            moment = momentList.get(i);
            queryWrapper.clear();
            queryWrapper.eq("momentId", moment.getMomentid());
            queryWrapper.last("limit 1");
            momentimg = momentimgService.getOne(queryWrapper);
            if(momentimg != null) {
                // momentimgList.clear();不行，会所有的moment的momentimgList都指向同一个，所以所有图片都一样
                momentimgList = new ArrayList<>();
                momentimgList.add(momentimg);
                moment.setMomentimgList(momentimgList);
                momentList.set(i, moment);
            }
        }

        map.put("videoSetList",videoSetList);
        map.put("userList",userList);
        map.put("momentList",momentList);

        return ResultData.success(map);
    }
}
