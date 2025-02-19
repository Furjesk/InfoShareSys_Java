package com.njit.infoshareserve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njit.infoshareserve.bean.Moment;
import com.njit.infoshareserve.bean.Review;
import com.njit.infoshareserve.service.ReviewService;
import com.njit.infoshareserve.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86187
* @description 针对表【review】的数据库操作Service实现
* @createDate 2023-04-21 10:47:03
*/
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review>
    implements ReviewService{

    @Autowired
    ReviewMapper reviewMapper;

    @Override
    public List<Review> selectReviewByMId(Integer managerId) {
        return reviewMapper.selectReviewByMId(managerId);
    }

    @Override
    public List<Review> selectReviewByUId(Integer userId) {
        return reviewMapper.selectReviewByUId(userId);
    }

    @Override
    public List<Integer> selectMomIdListByMId(Integer managerId) {
        return reviewMapper.selectMomIdListByMId(managerId);
    }

    @Override
    public List<Moment> selectReviewByMomIds(List<Integer> momIds) {
        return reviewMapper.selectReviewByMomIds(momIds);
    }

}




