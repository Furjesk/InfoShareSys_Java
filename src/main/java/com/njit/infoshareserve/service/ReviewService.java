package com.njit.infoshareserve.service;

import com.njit.infoshareserve.bean.Moment;
import com.njit.infoshareserve.bean.Review;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86187
* @description 针对表【review】的数据库操作Service
* @createDate 2023-04-21 10:47:03
*/
public interface ReviewService extends IService<Review> {

    public List<Review> selectReviewByMId(Integer managerId);

    public List<Review> selectReviewByUId(Integer userId);

    public List<Integer> selectMomIdListByMId(Integer managerId);

    public List<Moment> selectReviewByMomIds(List<Integer> momIds);

}
