package com.njit.infoshareserve.bean;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName follow
 */
@Data
@TableName("follow")
public class Follow implements Serializable {
    /**
     * 被关注者ID
     */
    private Integer followid;

    /**
     * 粉丝ID/关注者ID
     */
    private Integer followerid;

    /**
     * 1关注；0取消关注；默认0
     */
    private Integer status;
    private Date followtime;

    private static final long serialVersionUID = 1L;
}