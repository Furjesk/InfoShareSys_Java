package com.njit.infoshareserve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njit.infoshareserve.bean.Category2;
import com.njit.infoshareserve.service.Category2Service;
import com.njit.infoshareserve.mapper.Category2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86187
* @description 针对表【category2】的数据库操作Service实现
* @createDate 2023-04-12 10:22:08
*/
@Service
public class Category2ServiceImpl extends ServiceImpl<Category2Mapper, Category2>
    implements Category2Service{

    @Autowired
    Category2Mapper category2Mapper;

    @Override
    public List<Category2> selectByCategory1Id(Integer category1id) {
        return category2Mapper.selectByCategory1Id(category1id);
    }
}




