package com.qkl.online.mining.app.data.entity;

/**
 *  *  author : oyb
 *  *  date : 2018/12/27 11:58
 *  *  description : 
 *  
 */
public class IncomeBean extends BaseBean {

    /**
     * tradeDate : 20180502
     * value : 0.03676598
     */
    private String tradeDate;
    private double value;

    public IncomeBean(String tradeDate, double value) {
        this.tradeDate = tradeDate;
        this.value = value;
    }

    public String getTradeDate() {
        return tradeDate == null ? "" : tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
