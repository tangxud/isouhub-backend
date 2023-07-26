package com.txd.isouhub.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 视频
 */
@Data
public class Video implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 视频地址
     */
    private String arcurl;

    /**
     * 视频封面图片地址
     */
    private String pic;

    private static final long serialVersionUID = 1L;
}
