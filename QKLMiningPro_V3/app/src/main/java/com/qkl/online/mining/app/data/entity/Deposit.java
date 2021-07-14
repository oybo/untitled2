package com.qkl.online.mining.app.data.entity;


public class Deposit {

    /**
     * memberId : 221575525259415552
     * email : zm91881@126.com
     * isMust : 1
     * goUrl : http://www.baidu.com
     * taskType : 0
     * taskName : 测试任务1
     * taskPriority : 1
     * treatAmount : -20
     */

    private long memberId;
    private String email;
    private int isMust;
    private String goUrl;
    private int taskType;
    private String taskName;
    private int taskPriority;
    private int treatAmount;

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsMust() {
        return isMust;
    }

    public void setIsMust(int isMust) {
        this.isMust = isMust;
    }

    public String getGoUrl() {
        return goUrl;
    }

    public void setGoUrl(String goUrl) {
        this.goUrl = goUrl;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(int taskPriority) {
        this.taskPriority = taskPriority;
    }

    public int getTreatAmount() {
        return treatAmount;
    }

    public void setTreatAmount(int treatAmount) {
        this.treatAmount = treatAmount;
    }

}
