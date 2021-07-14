package com.qkl.online.mining.app.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 *  *  author : oyb
 *  *  date : 2018/11/19 21:24
 *  *  description : 
 *  
 */
public class AppCommonsConfig {


    /**
     * inviteStartListUrl : http://h5.hjkk66.com/invitelist
     * appSummary : 快來雲石星球玩遊戲賺雲石，
     * servicePhone :
     * appName : 雲石星球
     * serviceMail : yunplanet9@gmail.com
     * tokenHolderUrl : http://h5.hjkk66.com/yunlist
     * yuntListUrl : http://h5.hjkk66.com/yuntlist
     * startCover : http://yunplanet.net/app/appstart.png
     * drawMinAmount : 5000
     * drawMaxFeeRate : 0.05
     * drawMinFeeRate : 0.02
     * minerDefaultImg : http://yun-planet.oss-cn-shenzhen.aliyuncs.com/upload/20181014/d11469edfea54075990d8b3dca6295c4.png
     * updateUrl : http://down.yunplanet.net
     * reservationUrl : http://h5.yunplanet.net/reservationyunt
     * version : 2.1.7
     * iosVersion : 2.1.11
     * regprotocol : http://h6.yunplanet.net/protocol
     * startBanners : http://yunplanet.net/app/appstart.png,http://yunplanet.net/app/appstart1.jpg,http://yunplanet.net/app/appstart2.jpg
     */

    private String inviteStartListUrl;
    private String appSummary;
    private String servicePhone;
    private String appName;
    private String serviceMail;
    private String tokenHolderUrl;
    private String yuntListUrl;
    private String startCover;
    private int drawMinAmount;
    private double drawMaxFeeRate;
    private double drawMinFeeRate;
    private String minerDefaultImg;
    private String updateUrl;
    private String reservationUrl;
    private String version;
    private String iosVersion;
    private String regprotocol;
    private String startBanners;
    /**
     * drawMaxFeeRate : 0.05
     * drawMinFeeRate : 1.0E-4
     * msg : {"cacheDuration":"60","zh-TW":"http://h5wallet.yunplanet.net/msg.zh-TW.json","en-US":"http://h5wallet.yunplanet.net/msg.en-US.json"}
     */

    private MsgBean msg;

    public String getInviteStartListUrl() {
        return inviteStartListUrl;
    }

    public void setInviteStartListUrl(String inviteStartListUrl) {
        this.inviteStartListUrl = inviteStartListUrl;
    }

    public String getAppSummary() {
        return appSummary;
    }

    public void setAppSummary(String appSummary) {
        this.appSummary = appSummary;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getServiceMail() {
        return serviceMail;
    }

    public void setServiceMail(String serviceMail) {
        this.serviceMail = serviceMail;
    }

    public String getTokenHolderUrl() {
        return tokenHolderUrl;
    }

    public void setTokenHolderUrl(String tokenHolderUrl) {
        this.tokenHolderUrl = tokenHolderUrl;
    }

    public String getYuntListUrl() {
        return yuntListUrl;
    }

    public void setYuntListUrl(String yuntListUrl) {
        this.yuntListUrl = yuntListUrl;
    }

    public String getStartCover() {
        return startCover;
    }

    public void setStartCover(String startCover) {
        this.startCover = startCover;
    }

    public int getDrawMinAmount() {
        return drawMinAmount;
    }

    public void setDrawMinAmount(int drawMinAmount) {
        this.drawMinAmount = drawMinAmount;
    }

    public double getDrawMaxFeeRate() {
        return drawMaxFeeRate;
    }

    public void setDrawMaxFeeRate(double drawMaxFeeRate) {
        this.drawMaxFeeRate = drawMaxFeeRate;
    }

    public double getDrawMinFeeRate() {
        return drawMinFeeRate;
    }

    public void setDrawMinFeeRate(double drawMinFeeRate) {
        this.drawMinFeeRate = drawMinFeeRate;
    }

    public String getMinerDefaultImg() {
        return minerDefaultImg;
    }

    public void setMinerDefaultImg(String minerDefaultImg) {
        this.minerDefaultImg = minerDefaultImg;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getReservationUrl() {
        return reservationUrl;
    }

    public void setReservationUrl(String reservationUrl) {
        this.reservationUrl = reservationUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIosVersion() {
        return iosVersion;
    }

    public void setIosVersion(String iosVersion) {
        this.iosVersion = iosVersion;
    }

    public String getRegprotocol() {
        return regprotocol;
    }

    public void setRegprotocol(String regprotocol) {
        this.regprotocol = regprotocol;
    }

    public String getStartBanners() {
        return startBanners;
    }

    public void setStartBanners(String startBanners) {
        this.startBanners = startBanners;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        /**
         * cacheDuration : 60
         * zh-TW : http://h5wallet.yunplanet.net/msg.zh-TW.json
         * en-US : http://h5wallet.yunplanet.net/msg.en-US.json
         */

        private String cacheDuration;
        @SerializedName("zh-TW")
        private String zhTW;
        @SerializedName("en-US")
        private String enUS;

        public String getCacheDuration() {
            return cacheDuration;
        }

        public void setCacheDuration(String cacheDuration) {
            this.cacheDuration = cacheDuration;
        }

        public String getZhTW() {
            return zhTW;
        }

        public void setZhTW(String zhTW) {
            this.zhTW = zhTW;
        }

        public String getEnUS() {
            return enUS;
        }

        public void setEnUS(String enUS) {
            this.enUS = enUS;
        }
    }
}
