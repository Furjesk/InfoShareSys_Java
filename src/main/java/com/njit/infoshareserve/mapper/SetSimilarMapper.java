package com.njit.infoshareserve.mapper;

import com.njit.infoshareserve.bean.SetSimilar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
* @author 86187
* @description 针对表【set_similar】的数据库操作Mapper
* @createDate 2023-05-12 19:53:50
* @Entity com.njit.infoshareserve.bean.SetSimilar
*/
@Mapper
public interface SetSimilarMapper extends BaseMapper<SetSimilar> {

    //截断表
    @Update("truncate table set_similar")
    void truncateTable();
}




