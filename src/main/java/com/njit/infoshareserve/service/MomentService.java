package com.njit.infoshareserve.service;

import com.njit.infoshareserve.bean.Moment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86187
* @description 针对表【moment】的数据库操作Service
* @createDate 2023-04-22 21:07:50
*/
public interface MomentService extends IService<Moment> {

    //通过momentIdList和status更新动态状态
    public Boolean updateMomStatusByMomIds(Integer status, List<Integer> momentIdList);

    //根据keyword查找动态，涉及内容和话题。status = 1表示已通过审核的
    public List<Moment> searchMomentListByKey(String keyword);

}
