package com.njit.infoshareserve.recommend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.njit.infoshareserve.bean.Category2;
import com.njit.infoshareserve.bean.VideoSet;
import com.njit.infoshareserve.service.Category2Service;
import com.njit.infoshareserve.service.VideoSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计推荐模块
 */
@Component
public class StatisticalRecommend {

    @Autowired
    VideoSetService videoSetService;
    @Autowired
    Category2Service category2Service;

    /**
     * 1.历史热门视频统计——统计所有历史数据中每个视频的点赞数、评论数
     */
    public List<VideoSet> HistoryHotSet() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("likeNum");
        queryWrapper.orderByDesc("commentNum");
        return videoSetService.getBaseMapper().selectList(queryWrapper);
    }

    /**
     * 2.近期热门视频统计——统计每月的视频点赞个数、评论数，就代表了视频近期的热门度
     * 【这个可以做到搜索排行榜】
     */
    public List<VideoSet> RecentHotSet() {

        return videoSetService.selectRecentHotSet();
    }

    /**
     * 3.电影平均评分统计
     * 视频没有评分，不做
     */

    /**
     * 4.各类别Top8优质电影统计
     * 根据1级分类 查找每个2级分类的电影中计算用户历史点赞总数排个序，取前8个
     * 首页上方一级分类，点进去后按二级分类展示8条推荐数据，右侧排行榜8个热门就是统计推荐模块
     */
    public Map<Integer,List<VideoSet>> Category1All2Top8(Integer category1Id) {
        Map map = new HashMap<Integer,List<VideoSet>>();

        //根据一级分类id获得所有二级分类id
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("category1Id",category1Id);
        List<Category2> category2List = category2Service.getBaseMapper().selectList(queryWrapper);
        //封装各个二级分类的排行榜
        for (int i = 0; i < category2List.size(); i++) {
            queryWrapper.clear();
            queryWrapper.eq("category2Id",category2List.get(i).getCategory2id());
            queryWrapper.orderByDesc("likeNum");
            queryWrapper.last("limit 8");
            List<VideoSet> videoSetList = videoSetService.getBaseMapper().selectList(queryWrapper);
            map.put(category2List.get(i).getCategory2id(),videoSetList);
        }

        return map;
    }

    /**
     * 通过category2Id获取前十个热门视频
     * 用于视频播放页面 且 LFM没有数据时（新增视频都没有）
     * @return
     */
    public List<VideoSet> getCategory2Top10(Integer category2Id) {
        return videoSetService.selectTop10SetsByCategory2Id(category2Id);
    }

}
