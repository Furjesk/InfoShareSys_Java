package com.njit.infoshareserve.bean;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName watch
 */
@Data
@TableName("watch")
public class Watch implements Serializable {
    @TableField(exist = false)
    private Integer UPId;
    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String imgurl;
    @TableField(exist = false)
    private String setimg;
    @TableField(exist = false)
    private String setname;
    @TableField(exist = false)
    private String setlong;
    @TableField(exist = false)
    private String category1name;
    /**
     *
     */
    private Integer userid;

    /**
     * 
     */
    private Integer setid;

    /**
     * 上一次的定位
     */
    private String watchlocation;

    /**
     * 观看时间
     */
    private Date watchtime;

    private static final long serialVersionUID = 1L;
}