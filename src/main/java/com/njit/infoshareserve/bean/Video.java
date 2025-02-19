package com.njit.infoshareserve.bean;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName video
 */
@Data
@TableName("video")
public class Video implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer videoid;

    /**
     * 
     */
    private String videourl;

    /**
     * 
     */
    private String videoname;

    /**
     * 
     */
    private String videolong;

    /**
     * 
     */
    private Integer setid;

    private static final long serialVersionUID = 1L;
}