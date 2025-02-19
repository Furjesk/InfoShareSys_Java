package com.njit.infoshareserve.mapper;

import com.njit.infoshareserve.bean.PredictSetRate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njit.infoshareserve.bean.VideoSet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author 86187
* @description 针对表【predict_set_rate】的数据库操作Mapper
* @createDate 2023-05-11 18:36:05
* @Entity com.njit.infoshareserve.bean.PredictSetRate
*/
@Mapper
public interface PredictSetRateMapper extends BaseMapper<PredictSetRate> {

    //截断表
    @Update("truncate table predict_set_rate")
    public void truncateTable();

    //根据userid获取预测高评分视频，20条
    @Select("select username,videoset.* from videoset,predict_set_rate,users " +
            "where videoset.setId = predict_set_rate.setId and predict_set_rate.userId = #{userId} and videoset.userid = users.userid " +
            "and rate >= 3 and status = 1 " +
            "order by rate desc limit 20;")
    public List<VideoSet> getRecommendSetList(Integer userId);

}




