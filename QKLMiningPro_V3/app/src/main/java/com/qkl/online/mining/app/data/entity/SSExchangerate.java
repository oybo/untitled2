package com.qkl.online.mining.app.data.entity;

import java.text.DecimalFormat;

/**
 *  *  author : oyb
 *  *  date : 2018/10/16 18:10
 *  *  description : 
 * 实时汇率
 *  
 */
public class SSExchangerate extends BaseBean {

    /**
     * yunToUsdt : 0.1
     * yunToYunt : 10
     */

    private double exchangeRate;
    private double yunToYuntRate;
    private double yunToYuntPrice;

    public String getExchangeRate() {
        DecimalFormat df = new DecimalFormat("0.0000");
        return df.format(exchangeRate);
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getYunToYuntRate() {
        if (yunToYuntRate > 0) {
            DecimalFormat df = new DecimalFormat("0.0000");
            return df.format(yunToYuntRate);
        } else {
            return "0";
        }
    }

    public void setYunToYuntRate(double yunToYuntRate) {
        this.yunToYuntRate = yunToYuntRate;
    }

    public String getYunToYuntPrice() {
        if (yunToYuntPrice > 0) {
            DecimalFormat df = new DecimalFormat("0.0000");
            return df.format(yunToYuntPrice);
        } else {
            return "0";
        }
    }

    public void setYunToYuntPrice(double yunToYuntPrice) {
        this.yunToYuntPrice = yunToYuntPrice;
    }
}
