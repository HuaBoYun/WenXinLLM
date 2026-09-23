package com.huabo.es.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.frameworkset.elasticsearch.entity.ESBaseData;

import java.util.Date;

/**
 * @ClassName : OuterRule
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2023-03-13 14:20:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OuterRule extends ESBaseData {

    private String applyarea;

    private Long attid;

    private String bodyinfo;

    private Long createorgid;

    private String effectivelevel;

    private String enteringperson;

    private Date enteringtime;

    private String ispythonflag;

    private Long outrulid;

    private Date publishdate;

    private String publishorg;

    private String rulecode;

    private String rulename;

    private String rulenumber;

    private String status;

    private String summaryinfo;

    private Date takeeffecttime;

    private String timeliness;

    private String table;

}
