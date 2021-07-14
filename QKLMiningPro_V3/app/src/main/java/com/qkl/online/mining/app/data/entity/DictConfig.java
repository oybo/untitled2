package com.qkl.online.mining.app.data.entity;

/**
 *  *  author : oyb
 *  *  date : 2018/9/27 19:02
 *  *  description : 
 *  
 */
public class DictConfig extends BaseBean {


    /**
     * startBanners :
     * startCover : http://yun-planet.oss-cn-shenzhen.aliyuncs.com/upload/20180916/8232c401292144b9a4978e8eeace9029.jpg
     * iosVersion : 2.1.2
     * updateUrl : http://47.75.190.211/index.html
     * reservationUrl : http://www.yunpanet.com:8081/Reservation
     * version : 2.0.4
     * appName : 雲石星球
     * appSummary : 快來雲石星球玩游戲賺雲石
     * drawMinAmount : 5000
     * drawMinFeeRate : 0.02
     * drawMaxFeeRate : 0.05
     * yuntListUrl : http://www.yunpanet.com:8081/yuntlist
     * tokenHolderUrl : http://www.yunpanet.com:8081/yunlist
     * minerDefaultImg : http://yun-planet.oss-cn-shenzhen.aliyuncs.com/upload/20181014/d11469edfea54075990d8b3dca6295c4.png
     * serviceMail : 12311@qq.com
     * servicePhone : 0755-12345312
     */

    private String startBanners;
    private String startCover;
    private String iosVersion;
    private String updateUrl;
    private String reservationUrl;
    private String version;
    private String appName;
    private String appSummary;
    private int drawMinAmount;
    private double drawMinFeeRate;
    private double drawMaxFeeRate;
    private String yuntListUrl;
    private String tokenHolderUrl;
    private String minerDefaultImg;
    private String serviceMail;
    private String servicePhone;

    public String getStartBanners() {
        return startBanners;
    }

    public void setStartBanners(String startBanners) {
        this.startBanners = startBanners;
    }

    public String getStartCover() {
        return startCover;
    }

    public void setStartCover(String startCover) {
        this.startCover = startCover;
    }

    public String getIosVersion() {
        return iosVersion;
    }

    public void setIosVersion(String iosVersion) {
        this.iosVersion = iosVersion;
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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppSummary() {
        return appSummary;
    }

    public void setAppSummary(String appSummary) {
        this.appSummary = appSummary;
    }

    public int getDrawMinAmount() {
        return drawMinAmount;
    }

    public void setDrawMinAmount(int drawMinAmount) {
        this.drawMinAmount = drawMinAmount;
    }

    public double getDrawMinFeeRate() {
        return drawMinFeeRate;
    }

    public void setDrawMinFeeRate(double drawMinFeeRate) {
        this.drawMinFeeRate = drawMinFeeRate;
    }

    public double getDrawMaxFeeRate() {
        return drawMaxFeeRate;
    }

    public void setDrawMaxFeeRate(double drawMaxFeeRate) {
        this.drawMaxFeeRate = drawMaxFeeRate;
    }

    public String getYuntListUrl() {
        return yuntListUrl;
    }

    public void setYuntListUrl(String yuntListUrl) {
        this.yuntListUrl = yuntListUrl;
    }

    public String getTokenHolderUrl() {
        return tokenHolderUrl;
    }

    public void setTokenHolderUrl(String tokenHolderUrl) {
        this.tokenHolderUrl = tokenHolderUrl;
    }

    public String getMinerDefaultImg() {
        return minerDefaultImg;
    }

    public void setMinerDefaultImg(String minerDefaultImg) {
        this.minerDefaultImg = minerDefaultImg;
    }

    public String getServiceMail() {
        return serviceMail;
    }

    public void setServiceMail(String serviceMail) {
        this.serviceMail = serviceMail;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }
}
