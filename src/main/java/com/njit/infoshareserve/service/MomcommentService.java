package com.njit.infoshareserve.service;

import com.njit.infoshareserve.bean.Momcomment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86187
* @description 针对表【momcomment】的数据库操作Service
* @createDate 2023-04-24 11:05:52
*/
public interface MomcommentService extends IService<Momcomment> {

    public List<Momcomment> selectCommentListByMomId(Integer momentId);

}
