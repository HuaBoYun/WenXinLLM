package com.global.treasurer.entity;
// import lombok.Data; // 已移除
// @Data // 已移除,使用手动编写的getter/setter
public class FundMonitoringDashboard {
    private Long id;
    private String dashboardName;

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDashboardName() { return dashboardName; }
    public void setDashboardName(String dashboardName) { this.dashboardName = dashboardName; }

}
