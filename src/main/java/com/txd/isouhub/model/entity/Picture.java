package com.txd.isouhub.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 图片
 */
@Data
public class Picture implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * url
     */
    private String url;

    private static final long serialVersionUID = 1L;
}