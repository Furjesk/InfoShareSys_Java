package com.njit.infoshareserve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njit.infoshareserve.bean.Label;
import com.njit.infoshareserve.service.LabelService;
import com.njit.infoshareserve.mapper.LabelMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86187
* @description 针对表【label】的数据库操作Service实现
* @createDate 2023-04-12 19:07:25
*/
@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label>
    implements LabelService{

    @Autowired
    LabelMapper labelMapper;

    @Override
    public List<Label> selectLabelsByIds(List<Integer> idList) {
        return labelMapper.selectLabelsByIds(idList);
    }

    @Override
    public List<Label> selectLabelsByC2Id(Integer category2Id) {
        return labelMapper.selectLabelsByC2Id(category2Id);
    }
}




