package com.njit.infoshareserve.service;

import com.njit.infoshareserve.bean.SetSimilar;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86187
* @description 针对表【set_similar】的数据库操作Service
* @createDate 2023-05-12 19:53:50
*/
public interface SetSimilarService extends IService<SetSimilar> {

    //截断表
    public void truncateTable();
}
