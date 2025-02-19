package com.njit.infoshareserve.mapper;

import com.njit.infoshareserve.bean.Category2;
import com.njit.infoshareserve.bean.VideoSet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
* @author 86187
* @description 针对表【videoset】的数据库操作Mapper
* @createDate 2023-04-11 14:39:02
* @Entity com.njit.infoshareserve.bean.Videoset
*/
@Mapper
public interface VideoSetMapper extends BaseMapper<VideoSet> {

    @Select("select username,imgurl,videoset.* from videoset,users where videoset.userid = users.userid and setid = #{setId}")
    public VideoSet selectVideoSetBySetId(Integer setId);

    //根据keyword查找视频集，附带一级分类，由后端处理。status = 1表示已通过审核的
    @Select("select username,videoset.* from videoset,users where videoset.userid = users.userid and status = 1 and (setName like #{keyword} or setBrief like #{keyword})")
    public List<VideoSet> searchAllByKeyword(String keyword);

    //通过reviewIdList和status更新视频集状态
    public Boolean updateSetStatusByReviewIds(Integer status, List<Integer> reviewIdList);

    //通过userId获取他的所有视频集列表，按时间降序
    @Select("select username,videoset.* from users,videoset " +
            "where users.userId = videoset.userId and videoset.status = 1 and users.userId = #{userId} " +
            "order by videoset.createtime desc")
    public List<VideoSet> getVideoSetListByUId(Integer userId);

    // 通过category2Id随机获取8个视频
    @Select("select username,videoset.* from users,videoset " +
            "where users.userId = videoset.userId and videoset.status = 1 and category2Id = #{category2Id} limit 8")
    public List<VideoSet> select8SetsByCategory2Id(Integer category2Id);

    //获取二级分类下所有视频（频道详情页用）
    @Select("select username,videoset.* from users,videoset " +
            "where users.userId = videoset.userId and videoset.status = 1 and category2Id = #{category2Id}")
    public List<VideoSet> getSetListByCategory2Id(Integer category2Id);

    //获取top10热门视频（首页用）
    @Select("select username,videoset.* from users,videoset " +
            "where users.userId = videoset.userId and videoset.status = 1 " +
            "order by likeNum desc limit 10")
    public List<VideoSet> getTop10SetList();

    //获取最新10视频（首页用）
    @Select("select username,videoset.* from users,videoset " +
            "where users.userId = videoset.userId and videoset.status = 1 " +
            "order by createtime desc limit 10")
    public List<VideoSet> getRecent10SetList();

    /**
     * 推荐模块
     * @return
     */
    //统计推荐——近期热门视频统计
    public List<VideoSet> selectRecentHotSet();

    // 通过推荐id列表获得视频集和username
    public List<VideoSet> selectSetsByIds(List<Integer> serIdList);

    // 通过category2Id获取前十个热门视频
    @Select("select username, videoset.* from users,videoset " +
            "where users.userId = videoset.userId and category2Id = #{category2Id} " +
            "order by likenum desc limit 10")
    public List<VideoSet> selectTop10SetsByCategory2Id(Integer category2Id);

    @Select("select username, videoset.* from users,videoset " +
            "where users.userId = videoset.userId and setId = #{setId}")
    public VideoSet getSetAndUsernameBySetId(Integer setId);
}




