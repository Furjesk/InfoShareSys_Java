package com.njit.infoshareserve.bean;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName haslabel
 */
@Data
@TableName("haslabel")
public class Haslabel implements Serializable {
    /**
     * 
     */
    private Integer setid;

    /**
     * 
     */
    private Integer groupid;

    /**
     * 
     */
    private Integer labelid;

    /**
     * 1表示set；2表示group
     */
    private Integer targettype;

    private static final long serialVersionUID = 1L;
}