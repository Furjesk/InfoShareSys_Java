package com.njit.infoshareserve.mapper;

import com.njit.infoshareserve.bean.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 86187
* @description 针对表【video】的数据库操作Mapper
* @createDate 2023-04-11 14:39:02
* @Entity com.njit.infoshareserve.bean.Video
*/
@Mapper
public interface VideoMapper extends BaseMapper<Video> {

}




