package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_AUTHORIZATION")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjAuthorizationEntity {
	
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@TableId(value = "AUTHID", type= IdType.INPUT)
    @Schema
    private BigDecimal authId;

    @TableField(exist = false)
    @Schema
    private TblStaff authStaff;

    @TableField(exist = false)
    @Schema
    private TblNbsjTeamstaffEntity teamStaff;
    
    @TableField(value = "authTime")
    @Schema
    private Date authTime;

    @TableField(exist = false)
    @Schema
    private TblNbsjProject project;

    @TableField(exist = false)
    @Schema
    private TblAduitProGramEntity aduitProGram;

    @TableField(exist = false)
    @Schema
    private Set<TblNbsjOperateEntity> nbsjOperate;

}
