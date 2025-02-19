package com.njit.infoshareserve.recommend;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.njit.infoshareserve.bean.Rateset;
import com.njit.infoshareserve.bean.SetSimilar;
import com.njit.infoshareserve.service.RatesetService;
import com.njit.infoshareserve.service.SetSimilarService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OnlineRecommend {

    private Integer K = 10;
    private Integer N = 10;

    @Autowired
    RatesetService ratesetService;
    @Autowired
    SetSimilarService setSimilarService;

    /**
     * 传入setSimilar是为了防止多次重复查询
     * 1. 获取当前用户最近的 k 次评分，保存成Array[(mid, score)]
     * 2. 从相似度矩阵取出当前电影最相似的 N 个电影，作为备选列表，Array[mid]，可以过滤掉以及看过的视频
     * 3. 对每个备选列表，计算推荐优先级，得到当前用户的实时推荐列表，Array[(mid,score)]
     * 4. 把推荐数据 ( uid,Array[(mid,score)] ) 保存到mongodb，这里只有一个用户，因为传进来就一个【我就不存了，直接返回给前端】
     */
    public List<Integer> getRecommendIdList(Integer curUserId, Integer curSetId, SetSimilar setSimilar) {

        // 1. 获取当前用户最近的 k 次评分，保存成Array[(mid, score)]
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userId",curUserId);
        queryWrapper.orderByDesc("createtime");
        queryWrapper.last("limit " + K);
        List<Rateset> recentRateList = ratesetService.list(queryWrapper);
        // 统计高低分个数，定义>=3分为高分
        Integer h_count = 0, l_count = 0;
        for (int i = 0; i < recentRateList.size(); i++) {
            if(recentRateList.get(i).getRate() >= 3)
                h_count++;
            else
                l_count++;
        }

        // 2. 从相似度矩阵取出当前电影最相似的 N 个电影，作为备选列表，Array[mid]
        //传入setSimilar是为了防止多次重复查询
        List<Integer> candidateSetIds = getCandidateSetIdList(curSetId, setSimilar);

        // 3. 对每个备选列表，计算推荐优先级，得到当前用户的实时推荐列表，Array[(mid,score)]
        Map<Integer,Double> recommendSetPriority = new HashMap<>();
        Double priority;
        for (int j = 0; j < candidateSetIds.size(); j++) {
            priority = recommendPriority(candidateSetIds.get(j), recentRateList, h_count, l_count);
            // 优先级大于3才推荐
            if(priority > 3) {
                recommendSetPriority.put(candidateSetIds.get(j), priority);
            }
        }
        // 对推荐列表按照优先级降序排序
        List<Map.Entry<Integer, Double>> list = new LinkedList<>(recommendSetPriority.entrySet());
        // lambda表达式形式
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        // 只需要Id列表
        List<Integer> recIdList = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : list) {
            recIdList.add(entry.getKey());
        }

//        Map<Integer, Double> result = new LinkedHashMap<>();
//        for (Map.Entry<Integer, Double> entry : list) {
//            result.put(entry.getKey(), entry.getValue());
//        }
//        log.info("推荐优先级recommendSetPriority:{}",result);

        // 4. 把推荐数据 我就不存了，直接返回
        return recIdList;
    }

    public List<Integer> getCandidateSetIdList(Integer setId, SetSimilar setSimilar) {
        //传入setSimilar是为了防止多次重复查询
        if(setSimilar == null) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("setId",setId);
            setSimilar = setSimilarService.getOne(queryWrapper);
        }
        String simJsonStr = setSimilar.getSimilarSets();
        JSONObject simJson = JSONObject.fromObject(simJsonStr);
        Iterator<String> keys = simJson.keys();
        Integer i = 0;
        List<Integer> candidateSetIds = new ArrayList<>();
        while(keys.hasNext() && i < N) {
            candidateSetIds.add(Integer.parseInt(keys.next()));
            i++;
        }
        return candidateSetIds;
    }

    /**
     * 某个备选视频，计算推荐优先级
     * @return
     */
    public Double recommendPriority(Integer candidateId, List<Rateset> recentRateList, Integer h_count, Integer l_count) {

        QueryWrapper queryWrapper  = new QueryWrapper();
        queryWrapper.eq("setId",candidateId);
        SetSimilar setSimilar = setSimilarService.getOne(queryWrapper);
        JSONObject sim_Json = JSONObject.fromObject(setSimilar.getSimilarSets());

        Float weight = 0f; // 加权、分子
        Integer sim_sum = 0;
        // 相似度和评分 加权运算
        for (int i = 0; i < recentRateList.size(); i++) {
            // 相似度，由于存入数据库的相似度全是>0.6的，所以可能找不到
            Object sim = sim_Json.get(recentRateList.get(i).getSetid().toString());
            if(sim!=null) {
                sim_sum++;
                weight += Float.parseFloat(sim.toString()) * recentRateList.get(i).getRate();
            }
        }
        Double priority = weight / sim_sum + Math.log( Math.max(h_count,1) ) - Math.log( Math.max(l_count,1) );
        return priority;
    }

}
