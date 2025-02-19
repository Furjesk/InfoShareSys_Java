package com.njit.infoshareserve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.njit.infoshareserve.bean.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<Users> {

}
