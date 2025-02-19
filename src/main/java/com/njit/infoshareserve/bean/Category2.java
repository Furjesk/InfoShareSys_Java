package com.njit.infoshareserve.bean;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName category2
 */
@Data
@TableName("category2")
public class Category2 implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer category2id;

    /**
     * 
     */
    private String category2name;

    /**
     * 
     */
    private String category2brief;

    /**
     * 
     */
    private Integer category1id;

    private Integer userid;

    private static final long serialVersionUID = 1L;
}