package com.njit.infoshareserve.mapper;

import com.njit.infoshareserve.bean.Subject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86187
* @description 针对表【subject】的数据库操作Mapper
* @createDate 2023-04-23 21:26:51
* @Entity com.njit.infoshareserve.bean.Subject
*/
@Mapper
public interface SubjectMapper extends BaseMapper<Subject> {

}




