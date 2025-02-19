package com.njit.infoshareserve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.njit.infoshareserve.bean.Category2;
import com.njit.infoshareserve.bean.VideoSet;
import com.njit.infoshareserve.service.VideoSetService;
import com.njit.infoshareserve.mapper.VideoSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author 86187
* @description 针对表【videoset】的数据库操作Service实现
* @createDate 2023-04-11 14:39:02
*/
@Service
public class VideoSetServiceImpl extends ServiceImpl<VideoSetMapper, VideoSet>
    implements VideoSetService {

    @Autowired
    VideoSetMapper videoSetMapper;

    @Override
    public VideoSet selectVideoSetBySetId(Integer setId) {
        return videoSetMapper.selectVideoSetBySetId(setId);
    }

    @Override
    public List<VideoSet> searchAllByKeyword(String keyword) {
        return videoSetMapper.searchAllByKeyword(keyword);
    }

    @Override
    public Boolean updateSetStatusByReviewIds(Integer status, List<Integer> reviewIdList) {
        return videoSetMapper.updateSetStatusByReviewIds(status,reviewIdList);
    }

    @Override
    public List<VideoSet> getVideoSetListByUId(Integer userId) {
        return videoSetMapper.getVideoSetListByUId(userId);
    }

    @Override
    public List<VideoSet> getSetListByCategory2Id(Integer category2Id) {
        return videoSetMapper.getSetListByCategory2Id(category2Id);
    }

    @Override
    public List<VideoSet> getTop10SetList() {
        return videoSetMapper.getTop10SetList();
    }

    @Override
    public List<VideoSet> getRecent10SetList() {
        return videoSetMapper.getRecent10SetList();
    }

    /**
     * 推荐模块
     * @return
     */
    @Override
    public List<VideoSet> selectRecentHotSet() {
        return videoSetMapper.selectRecentHotSet();
    }

    @Override
    public List<VideoSet> selectSetsByIds(List<Integer> serIdList) {
        return videoSetMapper.selectSetsByIds(serIdList);
    }

    @Override
    public List<VideoSet> selectTop10SetsByCategory2Id(Integer category2Id) {
        return videoSetMapper.selectTop10SetsByCategory2Id(category2Id);
    }

    @Override
    public VideoSet getSetAndUsernameBySetId(Integer setId) {
        return videoSetMapper.getSetAndUsernameBySetId(setId);
    }

    @Override
    public List<VideoSet> select8SetsByCategory2Id(Integer category2Id) {
        return videoSetMapper.select8SetsByCategory2Id(category2Id);
    }

}




