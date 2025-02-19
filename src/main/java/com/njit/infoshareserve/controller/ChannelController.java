package com.njit.infoshareserve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.njit.infoshareserve.bean.Category2;
import com.njit.infoshareserve.bean.Joinchannel;
import com.njit.infoshareserve.bean.ResultData;
import com.njit.infoshareserve.bean.VideoSet;
import com.njit.infoshareserve.recommend.StatisticalRecommend;
import com.njit.infoshareserve.service.Category2Service;
import com.njit.infoshareserve.service.JoinchannelService;
import com.njit.infoshareserve.service.VideoSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    Category2Service category2Service;
    @Autowired
    StatisticalRecommend statisticalRecommend;
    @Autowired
    VideoSetService videoSetService;
    @Autowired
    JoinchannelService joinchannelService;

    /**
     * 根据一级分类id获取二级分类列表
     * @param category1id
     * @return
     */
    @GetMapping("/getCategory2ListBy1id/{category1id}")
    public ResultData<List<Category2>> getCategory2ListBy1id(@PathVariable("category1id") Integer category1id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("category1Id",category1id);
        List<Category2> category2List = category2Service.list(queryWrapper);
        return ResultData.success(category2List);

    }

    /**
     * 一级分类下所有二级分类对应的热门和推荐/随机 8个视频
     * @param map category1id,userid(用于推荐，【未使用】)
     */
    @PostMapping("/getCategory1PerCategory2")
    public ResultData< Map<Integer, Map<String,Object>> > getCategory1PerCategory2(@RequestBody Map<String, Integer> map) {
        //排行榜
        Map<Integer, List<VideoSet>> top8Map = statisticalRecommend.Category1All2Top8(map.get("category1id"));
        //随便8个视频
        Map< Integer, Map<String,Object> > resMap = new HashMap<>();
        List<VideoSet> videoSets;
        QueryWrapper queryWrapper = new QueryWrapper();
        Category2 category2;
        for(Map.Entry<Integer, List<VideoSet>> entry : top8Map.entrySet()) {
            //二级分类名
            queryWrapper.clear();
            queryWrapper.eq("category2Id",entry.getKey());
            category2 = category2Service.getOne(queryWrapper);
            Map<String,Object> videoSetMap = new HashMap<>();
            videoSetMap.put("category2name",category2.getCategory2name());
            //热门8个
            videoSetMap.put("hot8",entry.getValue());
            //随便获取二级分类的8个视频
            videoSets = videoSetService.select8SetsByCategory2Id(entry.getKey());
            videoSetMap.put("recs8",videoSets);
            resMap.put(entry.getKey(), videoSetMap);
        }
        return ResultData.success(resMap);
    }

    /**
     * 获取二级分类所有视频/获取频道详情数据
     * @param map category2id,userid
     * @return
     */
    @PostMapping("/getChannelDetail")
    public ResultData< Map<String, Object> > getChannelDetail(@RequestBody Map<String, Integer> map) {

        QueryWrapper queryWrapper = new QueryWrapper();
        //用户是否加入频道
        queryWrapper.eq("userId",map.get("userid"));
        queryWrapper.eq("category2Id",map.get("category2id"));
        Joinchannel joinchannel = joinchannelService.getOne(queryWrapper);
        //排行榜8个
        queryWrapper.clear();
        queryWrapper.eq("category2Id",map.get("category2id"));
        queryWrapper.orderByDesc("likeNum");
        queryWrapper.last("limit 8");
        List<VideoSet> hot8List = videoSetService.list(queryWrapper);
        //所有视频
        List<VideoSet> allVideoSets = videoSetService.getSetListByCategory2Id(map.get("category2id"));

        Map<String, Object> resMap = new HashMap<>();
        if(joinchannel == null)
            resMap.put("isJoin",false);
        else
            resMap.put("isJoin",true);
        resMap.put("hot8List",hot8List);
        resMap.put("allVideoSets",allVideoSets);

        return ResultData.success(resMap);
    }

    /**
     * 加入频道
     * @param map category2id,userid,action(bool)
     * @return
     */
    @PostMapping("/JoinChannel")
    public ResultData<String> JoinChannel(@RequestBody Map<String, Integer> map) {

        Boolean success = true;
        if(map.get("action") == 1) { //加入
            Joinchannel joinchannel = new Joinchannel();
            joinchannel.setUserid(map.get("userid"));
            joinchannel.setCategory2id(map.get("category2id"));
            joinchannel.setJointime(new Date());
            success = joinchannelService.save(joinchannel);
        } else if(map.get("action") == 0) { //取消加入
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("userId",map.get("userid"));
            queryWrapper.eq("category2Id",map.get("category2id"));
            success = joinchannelService.remove(queryWrapper);
        }
        if(success)
            return ResultData.success("操作成功");
        else
            return ResultData.fail(500,"操作失败");
    }

    /**
     * 获取加入频道的top8视频 及加入频道列表
     * @param userId
     * @return
     */
    @GetMapping("/getJoinChannelInfo/{userId}")
    public ResultData<Map<String,Object>> getJoinChannelInfo(@PathVariable("userId") Integer userId) {
        //获取用户加入频道列表
        List<Joinchannel> joinchannelList = joinchannelService.getJoinListByUserid(userId);
        //获取每个频道top8视频
        Map<Integer,Object> allTop8Map = new HashMap<>();

        List<VideoSet> videoSets;
        for (int i = 0; i < joinchannelList.size(); i++) {
            videoSets = joinchannelService.getChannelTop8(joinchannelList.get(i).getCategory2id());

            Map<String,Object> top8Map = new HashMap<>();
            top8Map.put("category2Name",joinchannelList.get(i).getCategory2name());
            top8Map.put("category2Top8", videoSets);
            allTop8Map.put(joinchannelList.get(i).getCategory2id(),top8Map);
        }
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("channelList",joinchannelList);
        resMap.put("allTop8Map",allTop8Map);
        return ResultData.success(resMap);
    }
}
