package com.txd.isouhub.manager;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txd.isouhub.common.ErrorCode;
import com.txd.isouhub.datasource.*;
import com.txd.isouhub.exception.BusinessException;
import com.txd.isouhub.exception.ThrowUtils;
import com.txd.isouhub.model.dto.search.SearchRequest;
import com.txd.isouhub.model.dto.user.UserQueryRequest;
import com.txd.isouhub.model.entity.Picture;
import com.txd.isouhub.model.entity.Video;
import com.txd.isouhub.model.enums.SearchTypeEnum;
import com.txd.isouhub.model.vo.PostVO;
import com.txd.isouhub.model.vo.SearchVO;
import com.txd.isouhub.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * 搜索门面
 */
@Component
@Slf4j
public class SearchFacade {

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private VideoDataSource videoDataSource;

    @Resource
    private DataSourceRegistry dataSourceRegistry;


    public SearchVO searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String type = searchRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        String searchText = searchRequest.getSearchText();
        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
        long current = searchRequest.getCurrent();
        long pageSize = searchRequest.getPageSize();
        // 搜索出所有数据
        if (searchTypeEnum == SearchTypeEnum.ALL) {
            CompletableFuture<Page<Video>> videoTask = CompletableFuture.supplyAsync(() -> videoDataSource.doSearch(searchText, current, pageSize));

            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> pictureDataSource.doSearch(searchText, current, pageSize));

            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
                UserQueryRequest userQueryRequest = new UserQueryRequest();
                userQueryRequest.setUserName(searchText);
                return userDataSource.doSearch(searchText, current, pageSize);
            });

            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
                // 异步线程超出了spring上下文，所以需要手动将request存储到ThreadLocal中
                RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
                try {
                    return postDataSource.doSearch(searchText, current, pageSize);
                } finally {
                    // 清空ThreadLocal中存储的HttpServletRequest对象
                    RequestContextHolder.resetRequestAttributes();
                }
            });

            CompletableFuture.allOf(userTask, postTask, pictureTask).join();
            try {
                Page<UserVO> userVOPage = userTask.get();
                Page<PostVO> postVOPage = postTask.get();
                Page<Picture> picturePage = pictureTask.get();
                Page<Video> videoPage = videoTask.get();
                SearchVO searchVO = new SearchVO();
                searchVO.setUserList(userVOPage.getRecords());
                searchVO.setPostList(postVOPage.getRecords());
                searchVO.setPictureList(picturePage.getRecords());
                searchVO.setVideoList(videoPage.getRecords());
                return searchVO;
            } catch (Exception e) {
                log.error("查询异常", e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
            }
        } else {
            SearchVO searchVO = new SearchVO();
            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
            Page<?> page = dataSource.doSearch(searchText, current, pageSize);
            searchVO.setDataList(page.getRecords());
            return searchVO;
        }
    }
}
