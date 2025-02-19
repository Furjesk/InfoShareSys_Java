package com.njit.infoshareserve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njit.infoshareserve.bean.Moment;
import com.njit.infoshareserve.service.MomentService;
import com.njit.infoshareserve.mapper.MomentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86187
* @description 针对表【moment】的数据库操作Service实现
* @createDate 2023-04-22 21:07:50
*/
@Service
public class MomentServiceImpl extends ServiceImpl<MomentMapper, Moment>
    implements MomentService{

    @Autowired
    MomentMapper momentMapper;

    @Override
    public Boolean updateMomStatusByMomIds(Integer status, List<Integer> momentIdList) {
        return momentMapper.updateMomStatusByMomIds(status, momentIdList);
    }

    //根据keyword查找动态，涉及内容和话题。status = 1表示已通过审核的
    @Override
    public List<Moment> searchMomentListByKey(String keyword) {
        return momentMapper.searchMomentListByKey(keyword);
    }
}




