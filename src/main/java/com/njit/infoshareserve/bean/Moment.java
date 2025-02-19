package com.njit.infoshareserve.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName moment
 */
@Data
@TableName("moment")
public class Moment implements Serializable {

    @TableField(exist = false)
    private List<Momentimg> momentimgList;
    @TableField(exist = false)
    private List<Momcomment> momcommentList;

    @TableField(exist = false)
    private String subjectname;
    //动态发布者头像和昵称
    @TableField(exist = false)
    private String imgurl;
    @TableField(exist = false)
    private String username;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer momentid;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private Integer likenum;

    /**
     * 
     */
    private Integer commentnum;
    private Integer transmitnum;

    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private Integer userid;

    /**
     * 0表示待审核；1表示通过审核；2表示退回；3表示用户删除/撤销
     */
    private Integer status;

    /**
     * 0表示不允许评论；1表示允许评论
     */
    private Integer cancomment;
    private Integer subjectid;

    private static final long serialVersionUID = 1L;
}