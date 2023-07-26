package com.txd.isouhub.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txd.isouhub.common.BaseResponse;
import com.txd.isouhub.common.ErrorCode;
import com.txd.isouhub.common.ResultUtils;
import com.txd.isouhub.datasource.DataSource;
import com.txd.isouhub.datasource.DataSourceRegistry;
import com.txd.isouhub.exception.ThrowUtils;
import com.txd.isouhub.model.dto.picture.PictureQueryRequest;
import com.txd.isouhub.model.entity.Picture;
import com.txd.isouhub.model.enums.SearchTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图片接口
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private DataSourceRegistry dataSourceRegistry;

    /**
     * @param pictureQueryRequest
     * @param request
     * @return
     */

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> searchPostVOByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                          HttpServletRequest request) {
        long pageNum = pictureQueryRequest.getCurrent();
        long pageSize = pictureQueryRequest.getPageSize();
        String searchText = pictureQueryRequest.getSearchText();
        log.info("pageNum:{},pageSize:{},searchText:{}", pageNum, pageSize, searchText);
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 35, ErrorCode.PARAMS_ERROR);
        DataSource<Picture> pictureService = dataSourceRegistry.getDataSourceByType(SearchTypeEnum.PICTURE.getValue());
        Page<Picture> picturePage = pictureService.doSearch(searchText, pageNum, pageSize);
        return ResultUtils.success(picturePage);
    }
}
