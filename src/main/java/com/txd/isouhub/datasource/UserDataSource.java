package com.txd.isouhub.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txd.isouhub.model.dto.user.UserQueryRequest;
import com.txd.isouhub.model.vo.UserVO;
import com.txd.isouhub.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserDataSource implements DataSource<UserVO> {
    @Resource
    private UserService userService;

    @Override
    public Page<UserVO> doSearch(String searchText, long current, long pageSize) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userQueryRequest.setCurrent(current);
        userQueryRequest.setPageSize(pageSize);
        return userService.listUserVOPage(userQueryRequest);
    }
}
