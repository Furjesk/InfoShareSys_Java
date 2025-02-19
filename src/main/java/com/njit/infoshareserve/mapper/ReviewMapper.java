package com.njit.infoshareserve.mapper;

import com.njit.infoshareserve.bean.Moment;
import com.njit.infoshareserve.bean.Review;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author 86187
* @description 针对表【review】的数据库操作Mapper
* @createDate 2023-04-21 10:47:03
* @Entity com.njit.infoshareserve.bean.Review
*/
@Mapper
public interface ReviewMapper extends BaseMapper<Review> {

    @Select("select videoset.setname, videoset.setimg, videoset.setlong, review.* from videoset,review where videoset.setid = review.setid and managerId = #{managerId}")
    public List<Review> selectReviewByMId(Integer managerId);

    @Select("select videoset.setname, videoset.setimg, videoset.setlong, review.* from videoset,review where videoset.setid = review.setid and userId = #{userId}")
    public List<Review> selectReviewByUId(Integer userId);

    @Select("select momentId from review where managerId = #{managerId} and targetType = 2")
    public List<Integer> selectMomIdListByMId(Integer managerId);

    public List<Moment> selectReviewByMomIds(List<Integer> momIds);

}




