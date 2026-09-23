package com.huabo.audit.oracle.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 描述: 
 * author: ziyao
 * date: 2022-04-21
 */
@TableName("TBL_NBSJ_TARGETTYPE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjTargettypeEntity {
	public static final Integer TEMP_NUMBER=0;//审计模板
	public static final Integer ZY_NUMBER=1;//指引模板
	public static final Integer COPY_TYPE=2; // 审计经验库
	
    @TableId(value = "targetid", type= IdType.INPUT)
    @Schema(name = "审计模板目录id")
    private BigDecimal targetId;

    @TableField(value = "targetname")
    @Schema(name = "审计目标名称", required = true)
    private String targetName;

    @TableField(value = "targetdesc")
    @Schema(name = "描述", required = false)
    private String targetDesc;

    @TableField(value = "parentid")
    @Schema(name = "父id", required = true)
    private BigDecimal parentId;

    @TableField(value = "createtime")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(name="创建日期",hidden=true)
    private Date createTime;

    @TableField(value = "updatetime")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(name="修改时间",hidden=true)
    private Date updateTime;

    @TableField(value = "TEMPID")
    @Schema(name = "模板id")
    private BigDecimal tempId;

    @TableField(value = "status")
    @Schema(name = "类型：审计模板 0、指引模板 1")
    private Integer status;

    @TableField(exist = false)
    @Schema(name = "子集节点")
    @IgnoreSwaggerParameter
    private List<TblNbsjTargettypeEntity> childrenList;

}
