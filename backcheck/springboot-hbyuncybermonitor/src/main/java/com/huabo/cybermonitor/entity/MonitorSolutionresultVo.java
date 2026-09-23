package com.huabo.cybermonitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author：yhr
 * @date:2022-09-06 10:00
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitorSolutionresultVo {


    private static final long serialVersionUID = 1L;
    public static final Integer YJYJ=1;
    /**
     * 监控执行类型
     */
    public static final Integer ZKZX=3;
    public static final Integer ZX=2;

    private BigDecimal solutionresultid;

    private LocalDateTime savetime;

    private String memo;

    private BigDecimal staffid;

    private String realname;

    private String username;


    private BigDecimal solutionid;

    private BigDecimal source;

}
