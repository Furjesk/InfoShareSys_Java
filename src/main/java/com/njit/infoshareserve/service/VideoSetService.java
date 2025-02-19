package com.njit.infoshareserve.service;

import com.njit.infoshareserve.bean.VideoSet;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86187
* @description 针对表【videoset】的数据库操作Service
* @createDate 2023-04-11 14:39:02
*/
public interface VideoSetService extends IService<VideoSet> {

    public VideoSet selectVideoSetBySetId(Integer setId);

    //根据keyword查找视频集，附带一级分类和标签，由后端处理
    public List<VideoSet> searchAllByKeyword(String keyword);

    //通过reviewIdList和status更新视频集状态
    public Boolean updateSetStatusByReviewIds(Integer status, List<Integer> reviewIdList);

    //通过userId获取他的所有视频集列表，按时间降序
    public List<VideoSet> getVideoSetListByUId(Integer userId);

    //获取二级分类下所有视频（频道详情页用）
    public List<VideoSet> getSetListByCategory2Id(Integer category2Id);

    //获取top10热门视频（首页用）
    public List<VideoSet> getTop10SetList();

    //获取最新10视频（首页用）
    public List<VideoSet> getRecent10SetList();

    /**
     * 推荐模块
     * @return
     */
    //统计推荐——近期热门视频统计
    public List<VideoSet> selectRecentHotSet();

    // 通过推荐id列表获得视频集和username【废弃】
    public List<VideoSet> selectSetsByIds(List<Integer> serIdList);

    // 通过category2Id获取前十个热门视频
    public List<VideoSet> selectTop10SetsByCategory2Id(Integer category2Id);

    public VideoSet getSetAndUsernameBySetId(Integer setId);

    // 通过category2Id随机获取8个视频
    public List<VideoSet> select8SetsByCategory2Id(Integer category2Id);

}
