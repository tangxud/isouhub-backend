package com.txd.isouhub;

import com.txd.isouhub.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 主类测试
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private PostService postService;

    @Test
    void testMain() {
        System.out.println(postService.count());
    }

}
