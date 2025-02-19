package com.njit.infoshareserve.service;

import com.njit.infoshareserve.bean.PredictSetRate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.njit.infoshareserve.bean.VideoSet;

import java.util.List;

/**
* @author 86187
* @description 针对表【predict_set_rate】的数据库操作Service
* @createDate 2023-05-11 18:36:05
*/
public interface PredictSetRateService extends IService<PredictSetRate> {

    //截断表
    public void truncateTable();

    //根据userid获取预测高评分视频，20条
    public List<VideoSet> getRecommendSetList(Integer userId);

}
