package com.njit.infoshareserve.bean;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName subject
 */
@Data
@TableName("subject")
public class Subject implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer subjectid;

    /**
     * 
     */
    private String subjectname;
    private Integer userid;
    private Date createtime;

    private static final long serialVersionUID = 1L;
}