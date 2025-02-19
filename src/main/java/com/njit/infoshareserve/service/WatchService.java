package com.njit.infoshareserve.service;

import com.njit.infoshareserve.bean.Watch;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86187
* @description 针对表【watch】的数据库操作Service
* @createDate 2023-05-26 13:39:13
*/
public interface WatchService extends IService<Watch> {

    //获取用户视频的浏览历史
    public List<Watch> getSetHistory(Integer userId);

}
