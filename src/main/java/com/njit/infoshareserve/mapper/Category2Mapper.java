package com.njit.infoshareserve.mapper;

import com.njit.infoshareserve.bean.Category2;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 86187
* @description 针对表【category2】的数据库操作Mapper
* @createDate 2023-04-12 10:22:08
* @Entity com.njit.infoshareserve.bean.Category2
*/
@Mapper
public interface Category2Mapper extends BaseMapper<Category2> {

    @Select("select * from category2 where category1Id = #{category1id}")
    List<Category2> selectByCategory1Id(Integer category1id);
}




