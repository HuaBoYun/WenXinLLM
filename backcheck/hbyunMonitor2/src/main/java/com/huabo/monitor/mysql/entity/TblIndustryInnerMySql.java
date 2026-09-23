package com.huabo.monitor.mysql.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_INDUSTRY_INNER")
@Schema(name="TblIndustryInnerMySql")
public class TblIndustryInnerMySql {

    @TableField("ORGID")
    private Long orgid;
    @TableField("INDUSTRYID")
    private Long industryid;
}
