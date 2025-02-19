package com.njit.infoshareserve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.njit.infoshareserve.bean.Category1;
import com.njit.infoshareserve.bean.ResultData;
import com.njit.infoshareserve.bean.VideoSet;
import com.njit.infoshareserve.service.Category1Service;
import com.njit.infoshareserve.service.PredictSetRateService;
import com.njit.infoshareserve.service.VideoSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/home")
@RestController
public class HomeController {

    @Autowired
    PredictSetRateService predictSetRateService;
    @Autowired
    VideoSetService videoSetService;
    @Autowired
    Category1Service category1Service;

    /**
     * 通过userId获取推荐videoSetList【通过离线推荐算法实现】
     * @return
     */
    @GetMapping("/getHomeSetList/{userId}")
    public ResultData< Map<String, List<VideoSet>> > getHomeSetList(@PathVariable("userId") Integer userId){

        // 如果预测评分表没有该用户的数据，也即没有计算LFM，那么返回前20个视频集，或者根据用户标签推荐【冷启动】
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userId",userId);
        List list = predictSetRateService.list(queryWrapper);

        //1.获取top10热门视频
        List<VideoSet> top10SetList = videoSetService.getTop10SetList();
        //2.获取最新10视频
        List<VideoSet> recent10SetList = videoSetService.getRecent10SetList();
        //3.推荐视频
        List<VideoSet> recommendSetList;
        if(list.size() == 0) {
            //可以根据兴趣标签推荐【未实现收集兴趣标签
//            recommendSetList = videoSetService.list();
            recommendSetList = null;
        } else {
            //根据userid获取 离线推荐算法算好的 预测高评分视频，20条
            recommendSetList = predictSetRateService.getRecommendSetList(userId);
        }

        Map<String, List<VideoSet>> resMap = new HashMap<>();
        resMap.put("top10SetList",top10SetList);
        resMap.put("recent10SetList",recent10SetList);
        resMap.put("recommendSetList",recommendSetList);

        return ResultData.success(resMap);
    }

    @GetMapping("/getCategory1List")
    public ResultData<List<Category1>> getCategory1List() {
        List<Category1> list = category1Service.list();
        return ResultData.success(list);
    }

}
