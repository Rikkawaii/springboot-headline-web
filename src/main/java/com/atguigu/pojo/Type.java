package com.atguigu.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;

/**
 * @TableName news_type
 */
//@TableName(value ="news_type"),已在application.yml全局配置
@Data
public class Type implements Serializable {
    @TableId
    private Integer tid;

    private String tname;
    @Version
    private Integer version;

    private Integer isDeleted;

    private static final long serialVersionUID = 1L;
}