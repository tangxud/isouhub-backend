package com.txd.isouhub.datasource;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txd.isouhub.common.ErrorCode;
import com.txd.isouhub.exception.BusinessException;
import com.txd.isouhub.model.entity.Picture;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PictureDataSource implements DataSource<Picture> {

    @Override
    public Page<Picture> doSearch(String searchText, long pageNum, long pageSize) {
        long current = (pageNum - 1) * pageSize;
        if (pageSize > 30) {
            pageSize = 30;
        }
        String queryUrl;
        if (searchText == null || "".equals(searchText))
            searchText = "探索未知";
        queryUrl = String.format("https://www.bing.com/images/search?q=%s&first=%d", searchText, current);
        log.info("queryUrl:{}", queryUrl);
        Document doc;
        try {
            doc = Jsoup.connect(queryUrl).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "图片数据获取异常");
        }
        Elements newsHeadlines = doc.select(".iuscp.isv");
        List<Picture> pictures = new ArrayList<>();
        for (Element headline : newsHeadlines) {
            // 取图片地址
            String m = headline.select(".iusc").get(0).attr("m");
            Map map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            // 取图片标题
            String title = headline.select(".inflnk").get(0).attr("aria-label");
            Picture picture = new Picture();
            picture.setUrl(murl);
            picture.setTitle(title);
            pictures.add(picture);
            if (pictures.size() >= pageSize) {
                break;
            }
        }
        Page<Picture> page = new Page<>(current, pageSize);
        page.setRecords(pictures);
        return page;
    }
}
