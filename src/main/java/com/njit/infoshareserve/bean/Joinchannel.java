package com.njit.infoshareserve.bean;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName joinchannel
 */
@Data
@TableName("joinchannel")
public class Joinchannel implements Serializable {

    @TableField(exist = false)
    private String category2name;
    /**
     * 
     */
    private Integer userid;

    /**
     * 
     */
    private Integer category2id;

    /**
     * 
     */
    private Date jointime;

    private static final long serialVersionUID = 1L;
}