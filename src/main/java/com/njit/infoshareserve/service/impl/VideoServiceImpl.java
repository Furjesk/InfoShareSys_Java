package com.njit.infoshareserve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njit.infoshareserve.bean.Video;
import com.njit.infoshareserve.service.VideoService;
import com.njit.infoshareserve.mapper.VideoMapper;
import org.springframework.stereotype.Service;

/**
* @author 86187
* @description 针对表【video】的数据库操作Service实现
* @createDate 2023-04-11 14:39:02
*/
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{

}




