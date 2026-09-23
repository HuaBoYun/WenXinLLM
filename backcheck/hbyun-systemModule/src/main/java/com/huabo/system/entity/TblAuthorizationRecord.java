package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="TBL_AUTHORIZATION_RECORD对象",description="操作确认表")
@Table(name = "TBL_AUTHORIZATION_RECORD")
public class TblAuthorizationRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int OPERATIONINSERT = 1;//操作-新增
	public static final int OPERATIONMODIFY = 2;//操作-修改
	public static final int OPERATIONREMOVE = 3;//操作-删除
	public static final int OPERATIONENABLE = 4;//操作-启用
	public static final int OPERATIONDEPRECATED = 5;//操作-弃用
	public static final int OPERATIONRESETPWD = 6;//操作-重置密码
	public static final int OPERATIONGRANTUSER = 7;//操作-用户授权
	public static final int OPERATIONGRANTORG = 8;//操作-公司授权
	public static final int OPERATIONUNUSER = 9;//操作-用户取消授权
	public static final int OPERATIONUNORG = 10;//操作-公司取消授权
	public static final int OPERATIONGRANTMENU = 11;//操作-菜单授权
	public static final int OPERATIONGRANTDATA = 12;//操作-数据授权
	public static final int OPERATIONGUNRANTDATA = 13;//操作-取消数据授权
	
	
	
	public static final String TARGETTYPECOMPANY = "company";  //公司
	public static final String TARGETTYPEDEPT = "dept";        //部门
	public static final String TARGETTYPEUSER = "user";       //用户
	public static final String TARGETTYPEROLE = "role";       //角色
	public static final String TARGETTYPERIGHT = "right";     //菜单
	public static final String TARGETTYPEGRANT = "grant";     //授权
	public static final String TARGETTYPESECRECT = "secrect"; //密级
	public static final String TARGETTYPEFLOW = "commonflow";       //通用流程
	public static final String TARGETTYPECONTRACTFLOW = "contractflow";       //合同类型流程
	

	@TableId(value="RECORDID",type = IdType.INPUT)
    @Schema(name= "操作记录主键")
    private String recordId;

    @TableField("RECORDTEXT")
    @Schema(name= "操作记录标题")
    private String recordText;

    @TableField("OPERATIONTYPE")
    @Schema(name= "操作类型 1-新增，2-修改，3-删除，4-启用，5-弃用，6-重置密码 ，7-用户授权， 8-公司授权， 9-用户取消授权，10-公司取消授权，11-菜单授权，12-数据授权")
    private int operationType;

    @TableField("TARGETTYPE")
    @Schema(name= "操作数据类型")
    private String targetType;

    @TableField("TARGETID")
    @Schema(name= "操作数据主键")
    private String targetId;

    @TableField("OPERATIONDATA")
    @Schema(name= "操作数据")
    private String operationData;

    @TableField("OPERATIONMEMO")
    @Schema(name= "描述")
    private String operationMemo;
    
    
    @TableField("STATUS")
    @Schema(name= "审批状态 1-审批中、2-需调整、3-已撤销、4-已终止、5-已跟踪、6-已完成")
    private Integer status;

    @TableField("CREATOR")
    @Schema(name= "创建人")
    private BigDecimal creator;

    @TableField("MODIFIER")
    @Schema(name= "修改人")
    private BigDecimal modifier;
    
    @TableField("CREATORNAME")
    @Schema(name= "创建人姓名")
    private String creatorName;
    
    @TableField("MODIFYERNAME")
    @Schema(name= "修改姓名")
    private String modifyerName;
    
    @Schema(name= "创建时间")
	@TableField("CREATIONTIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date creationTime;
	
	  @Schema(name= "修改时间")
	  @TableField("MODIFIEDTIME")
	  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date modifiedTime;
    
}
