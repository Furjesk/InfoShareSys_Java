package com.njit.infoshareserve.service;

import com.njit.infoshareserve.bean.Category2;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86187
* @description 针对表【category2】的数据库操作Service
* @createDate 2023-04-12 10:22:13
*/
public interface Category2Service extends IService<Category2> {

    List<Category2> selectByCategory1Id(Integer category1id);
}
