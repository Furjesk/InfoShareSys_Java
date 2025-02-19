package com.njit.infoshareserve.service;

import com.njit.infoshareserve.bean.Label;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86187
* @description 针对表【label】的数据库操作Service
* @createDate 2023-04-12 19:07:25
*/
public interface LabelService extends IService<Label> {

    List<Label> selectLabelsByIds(List<Integer> idList);
    List<Label> selectLabelsByC2Id(Integer category2Id);

}
