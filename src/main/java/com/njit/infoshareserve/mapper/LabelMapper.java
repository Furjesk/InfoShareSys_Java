package com.njit.infoshareserve.mapper;

import com.njit.infoshareserve.bean.Label;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 86187
* @description 针对表【label】的数据库操作Mapper
* @createDate 2023-04-12 19:07:25
* @Entity com.njit.infoshareserve.bean.Label
*/
@Mapper
public interface LabelMapper extends BaseMapper<Label> {

    List<Label> selectLabelsByIds(List<Integer> idList);

    @Select("select * from label where category2Id = #{category2Id} limit 10")
    List<Label> selectLabelsByC2Id(Integer category2Id);
}




