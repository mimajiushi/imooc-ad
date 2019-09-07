package com.imooc.ad.search;

import com.imooc.ad.search.vo.SearchRequest;
import com.imooc.ad.search.vo.SearchResponse;

/**
 * Created by Qinyi.
 */
public interface ISearch {

    /**
     * 用于广告检索请求
     */
    SearchResponse fetchAds(SearchRequest request);
}
