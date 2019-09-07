package com.imooc.ad.search.vo;

import com.imooc.ad.index.creative.CreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Qinyi.
 * 广告检索响应信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    public Map<String, List<Creative>> adSlot2Ads = new HashMap<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Creative {

        private Long adId;
        private String adUrl;
        private Integer width;
        private Integer height;
        private Integer type;
        private Integer materialType;

        // fixme 这个应该放到数据库方便热更新
        // 展示监测 url 就是卖广告的人请求后台返回的广告url
        private List<String> showMonitorUrl = Arrays.asList("www.imooc.com", "www.imooc.com");
        // 点击监测 url 就是卖广告的人点击之后返回的广告url
        private List<String> clickMonitorUrl = Arrays.asList("www.imooc.com", "www.imooc.com");
    }

    public static Creative convert(CreativeObject object) {

        Creative creative = new Creative();
        // fixme 用工具类替代
//        creative.setAdId(object.getAdId());
//        creative.setAdUrl(object.getAdUrl());
//        creative.setWidth(object.getWidth());
//        creative.setHeight(object.getHeight());
//        creative.setType(object.getType());
//        creative.setMaterialType(object.getMaterialType());
        BeanUtils.copyProperties(creative, object);

        return creative;
    }
}
