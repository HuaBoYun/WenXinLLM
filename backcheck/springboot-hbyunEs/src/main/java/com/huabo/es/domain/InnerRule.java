package com.huabo.es.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.frameworkset.elasticsearch.entity.ESBaseData;

import java.util.Date;

/**
 * @ClassName : InnerRule
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2023-03-13 14:20:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InnerRule extends ESBaseData {
    private Long attid;

    private String innruletype;

    private Long innrulid;

    private Date publishdate;

    private String publishorg;

    private String rulecode;

    private String rulename;

    private String rulenumber;

    private String status;

    private String timeliness;

    private String bodyinfo;

    private String tablel;


}
