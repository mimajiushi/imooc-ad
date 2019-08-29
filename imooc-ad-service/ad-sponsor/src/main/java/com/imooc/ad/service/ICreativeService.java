package com.imooc.ad.service;

import com.imooc.ad.vo.CreativeRequest;
import com.imooc.ad.vo.CreativeResponse;

/**
 * Created by Qinyi.
 * 广告创意（其实就相当于载体，如视频，文字，图片等）
 */
public interface ICreativeService {

    CreativeResponse createCreative(CreativeRequest request);
}
