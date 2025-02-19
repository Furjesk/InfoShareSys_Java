package com.njit.infoshareserve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njit.infoshareserve.bean.Momcomment;
import com.njit.infoshareserve.service.MomcommentService;
import com.njit.infoshareserve.mapper.MomcommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86187
* @description 针对表【momcomment】的数据库操作Service实现
* @createDate 2023-04-24 11:05:52
*/
@Service
public class MomcommentServiceImpl extends ServiceImpl<MomcommentMapper, Momcomment>
    implements MomcommentService{

    @Autowired
    MomcommentMapper momcommentMapper;

    @Override
    public List<Momcomment> selectCommentListByMomId(Integer momentId) {
        return momcommentMapper.selectCommentListByMomId(momentId);
    }
}




