package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name="TblExternalExpert")
public class TblExternalExpertMySql {

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
    @Schema(name = "隶属流程")
    private TblStaffMySql staffMySql;

    public TblExternalExpertMySql() {
    }
}
