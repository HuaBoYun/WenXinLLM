package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.oracle.entity.base.ReservedEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * @author Rui
 * @ClassName PlanDataEntity
 * @Description
 * @DATE 2023/10/01
 */

@Data
@TableName("TBL_YQNS_PLAN_DATA")
@Schema(name="提交资料表")
@Accessors(chain = true)
public class PlanDataEntity  extends ReservedEntity {

    @TableId(value="ID", type= IdType.AUTO)
    @Schema(name="ID")
    private BigDecimal id;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    @TableField(value="NAME")
    @Schema(name="名称")
    private String name;

    @TableField(value="CREATE_USER")
    @Schema(name="创建人")
    private TblStaff createUser;

    @TableField(value="CREATE_TIME")
    @Schema(name="创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    @Schema(name = "附件主键集合")
    @TableField(exist = false)
    private String attIds;

    @Schema(name = "附件集合")
    @TableField(exist = false)
    private List<TblAttachment> attachments;

}
