package com.txd.isouhub.model.vo;

import com.txd.isouhub.model.entity.Picture;
import com.txd.isouhub.model.entity.Video;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SearchVO implements Serializable {

    private List<PostVO> postList;

    private List<Picture> pictureList;

    private List<UserVO> userList;

    private List<Video> videoList;

    private List<?> dataList;

    private static final long serialVersionUID = 1L;
}
