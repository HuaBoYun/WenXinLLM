package com.huabo.monitor.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_EXTERNAL_EXPERT")
@ApiModel(value="TblExternalExpert")
public class TblExternalExpert {

    @TableId("EXTERID")
    private BigDecimal exterid;
    @TableField("COMPANY")
    @Column(name = "COMPANY")
    private String company;
    @TableField("EXPERTISE")
    private String expertise;
    @TableField("QUALIFICATION")
    private String qualification;
    @TableField("STAFFID")
    private BigDecimal staffid;

    @Transient
    @ApiModelProperty(value = "隶属流程")
    private TblStaff staff;

    public TblExternalExpert() {
    }
}
