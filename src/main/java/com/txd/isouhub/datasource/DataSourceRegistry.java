package com.txd.isouhub.datasource;

import com.txd.isouhub.model.enums.SearchTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源注册器
 */
@Component
public class DataSourceRegistry {

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private VideoDataSource videoDataSource;

    private Map<String, DataSource> dataSourceMap;

    @PostConstruct
    public void doInit() {
        dataSourceMap = new HashMap<>();
        dataSourceMap.put(SearchTypeEnum.POST.getValue(), postDataSource);
        dataSourceMap.put(SearchTypeEnum.USER.getValue(), userDataSource);
        dataSourceMap.put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
        dataSourceMap.put(SearchTypeEnum.VIDEO.getValue(), videoDataSource);
    }

    public <T> DataSource<T> getDataSourceByType(String type) {
        if (dataSourceMap == null) {
            return null;
        }
        return (DataSource<T>) dataSourceMap.get(type);
    }

}