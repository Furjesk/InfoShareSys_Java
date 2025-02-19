package com.njit.infoshareserve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njit.infoshareserve.bean.PredictSetRate;
import com.njit.infoshareserve.bean.VideoSet;
import com.njit.infoshareserve.service.PredictSetRateService;
import com.njit.infoshareserve.mapper.PredictSetRateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86187
* @description 针对表【predict_set_rate】的数据库操作Service实现
* @createDate 2023-05-11 18:36:05
*/
@Service
public class PredictSetRateServiceImpl extends ServiceImpl<PredictSetRateMapper, PredictSetRate>
    implements PredictSetRateService{

    @Autowired
    PredictSetRateMapper predictSetRateMapper;

    @Override
    public void truncateTable() {
        predictSetRateMapper.truncateTable();
    }

    //根据userid获取预测高评分视频，20条
    @Override
    public List<VideoSet> getRecommendSetList(Integer userId) {
        return predictSetRateMapper.getRecommendSetList(userId);
    }
}




