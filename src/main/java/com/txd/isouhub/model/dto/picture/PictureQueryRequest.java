package com.txd.isouhub.model.dto.picture;

import com.txd.isouhub.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 创建请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PictureQueryRequest extends PageRequest implements Serializable {

    /**
     * 查询搜索词
     */
    private String searchText;

    private static final long serialVersionUID = 1L;
}