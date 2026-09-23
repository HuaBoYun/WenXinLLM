package com.huabo.fxgl.vo;

import java.util.Date;

import lombok.Data;

@Data
public class RiskPgVo {
    private String RISKNUMBER;
    private String RISKNAME;
    private String RISKDES;
    private String FREQUENCY;
    private String SEVERITY;
    private String RISKLEVEL;
    private Date ASSDATE;
    private String REALNAME;
}
