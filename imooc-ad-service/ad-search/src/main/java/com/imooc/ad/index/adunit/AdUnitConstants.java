package com.imooc.ad.index.adunit;

/**
 * Created by Qinyi.
 * 广告/流量类型
 */
public class AdUnitConstants {

    public static class POSITION_TYPE {

        /**
         * 开屏
         */
        public static final int KAIPING = 1;

        /**
         * 贴片（视频播放前的开始之前的）
         */
        public static final int TIEPIAN = 2;

        /**
         * 中贴（视频播放中间插入的）
         */
        public static final int TIEPIAN_MIDDLE = 4;

        /**
         * 暂停时的广告
         */
        public static final int TIEPIAN_PAUSE = 8;

        /**
         * 后贴（视频播放完的广告）
         */
        public static final int TIEPIAN_POST = 16;
    }
}
