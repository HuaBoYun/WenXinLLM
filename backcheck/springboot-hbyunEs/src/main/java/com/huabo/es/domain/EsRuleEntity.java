package com.huabo.es.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName : EsRuleEntity
 * @Description : TODO
 * @Author : zhibo.cao
 * @Date: 2023-03-08 19:40:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsRuleEntity {
    public Long rulrId;
    public Long attId;
    public String ruleName;
    public String publishOrg;
    public Date publishDate;
    public String ruleCode;
    public String ruleNumber;
    public String bodyInfo;
    public String status;
    public String table;
}
