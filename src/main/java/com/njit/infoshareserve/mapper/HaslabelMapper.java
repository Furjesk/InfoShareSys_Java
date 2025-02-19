package com.njit.infoshareserve.mapper;

import com.njit.infoshareserve.bean.Haslabel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njit.infoshareserve.bean.Label;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 86187
* @description 针对表【haslabel】的数据库操作Mapper
* @createDate 2023-04-12 19:06:55
* @Entity com.njit.infoshareserve.bean.Haslabel
*/
@Mapper
public interface HaslabelMapper extends BaseMapper<Haslabel> {

}




