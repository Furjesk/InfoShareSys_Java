package com.njit.infoshareserve.bean;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName label
 */
@Data
@TableName("label")
public class Label implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer labelid;

    /**
     * 
     */
    private String labelname;

    /**
     * 外键
     */
    private Integer category2id;

    private static final long serialVersionUID = 1L;
}