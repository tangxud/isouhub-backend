package com.txd.isouhub.service;

import cn.hutool.json.JSONUtil;
import com.txd.isouhub.model.entity.Picture;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

/**
 * 图片服务测试
 */
@SpringBootTest
class PictureServiceImplTest {

    private static final Picture picture = new Picture();

//    @Autowired
//    private PictureServiceImpl pictureService;
//
//    @Test
//    void listPictureByPageTest() throws IOException {
//        Page<Picture> picturePage = pictureService.listPictureByPage("小黑子", 1, 20);
//        System.out.println(picturePage.getRecords());
//    }

    @Test
    void getPictureTest() throws IOException {
        String queryParams = "小黑子";
        String queryUrl = "https://www.bing.com/images/search?q=" + queryParams;
        Document doc = Jsoup.connect(queryUrl).get();
        Elements newsHeadlines = doc.select(".iuscp.isv");
        for (Element headline : newsHeadlines) {
            // 取图片地址
            String m = headline.select(".iusc").get(0).attr("m");
            Map map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            // 取图片标题
            String title = headline.select(".inflnk").get(0).attr("aria-label");
            picture.setUrl(murl);
            picture.setTitle(title);
        }
        System.out.println(picture);
        Assertions.assertTrue(true);
    }
}
