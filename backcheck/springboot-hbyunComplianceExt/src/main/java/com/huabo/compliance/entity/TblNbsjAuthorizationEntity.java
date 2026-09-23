package com.huabo.compliance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.Set;

@TableName("TBL_NBSJ_AUTHORIZATION")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjAuthorizationEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")  
	@TableId(value = "AUTHID", type= IdType.AUTO)
    @Schema
    private Integer authId;

    @TableField(value = "authStaff")
    @Schema
    private TblStaff authStaff;
    
    @TableField(value = "teamStaff")
    @Schema
    private TblNbsjTeamstaffEntity teamStaff;
    
    @TableField(value = "authTime")
    @Schema
    private Date authTime;
    
    @TableField(value = "project")
    @Schema
    private TblNbsjProject project;
    
    @TableField(value = "aduitProGram")
    @Schema
    private TblAduitProGramEntity aduitProGram;
    
    @TableField(value = "nbsjOperate")
    @Schema
    private Set<TblNbsjOperateEntity> nbsjOperate;

}
