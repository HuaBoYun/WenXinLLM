package com.huabo.compliance.vo;

import lombok.Data;

@Data
public class CopyVo {

    private String faid;
    private String pid;
    private String faflowid;
    private String flownumber;
    private String flowname;
    private String stutes;
    private String desc;
    private Integer pageNumber = 1;
    private Integer pageSize = 15;

}
