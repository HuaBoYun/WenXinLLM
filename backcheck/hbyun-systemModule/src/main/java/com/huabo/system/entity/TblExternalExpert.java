package com.huabo.system.entity;

import java.math.BigDecimal;

import org.springframework.data.annotation.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_EXTERNAL_EXPERT")
@Schema(name="TblExternalExpert")
public class TblExternalExpert {

    @TableId(value="EXTERID",type = IdType.INPUT)
    @Schema(name="主键")
    private BigDecimal exterid;
    @TableField("COMPANY")
    @Schema(name="单位")
    private String company;
    @TableField("EXPERTISE")
    @Schema(name="专长")
    private String expertise;
    @TableField("QUALIFICATION")
    @Schema(name="资格证书")
    private String qualification;
    @TableField("STAFFID")
    @Schema(name="关联用户ID")
    private BigDecimal staffid;

    @Transient
    @Schema(name="隶属流程")
    private TblStaff staff;

    public TblExternalExpert() {
    }
}
