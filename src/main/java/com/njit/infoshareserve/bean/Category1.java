package com.njit.infoshareserve.bean;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName category1
 */
@Data
@TableName("category1")
public class Category1 implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer category1id;

    /**
     * 
     */
    private String category1name;

    /**
     * 二级分类
     */
    @TableField(exist = false)
    private List<Category2> category2List;

    private static final long serialVersionUID = 1L;
}