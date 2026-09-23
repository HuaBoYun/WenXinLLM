package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.system.vo.param.ShowPullDownExtListResult;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义展示表
 */
@Schema(name = "TblSystemCustomizeShow")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "TBL_SYSTEM_CUSTOMIZE_SHOW")
public class TblSystemCustomizeShow implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Schema(name="主键ID")
	@TableId(value = "ID",type = IdType.INPUT)
	private BigDecimal id;

	@NotBlank(message = "field 字段 不能为空")
	@Schema(name="字段")
	@TableField(value = "FIELD")
	private String field;

	@Schema(name="原名称")
	@TableField(value = "ORLNAME")
	private String orlName;

	@Schema(name="变更名称")
	@TableField(value = "CHANGENAME")
	private String changeName;

	
	@Schema(name="是否使用变更名称 0-不使用 1-使用")
	@TableField(value = "ISCHANGE")
	private Integer isChange;

	@Schema(name="是否展示详情页 0-不展示 1-展示")
	@TableField(value = "ISDETAILS")
	private Integer isDetails;

	@TableField(value = "ISLIST")
	@Schema(name="是否展示列表页 0-不展示 1-展示")
	private Integer isList;

	@TableField(value = "SORT")
	@NotNull(message = "sort 排序(详细) 不能为空")
	@Schema(name="排序(详细)")
	private Integer sort;

	@TableField(value = "LISTSORT")
	@NotNull(message = "listSort 排序(列表) 不能为空")
	@Schema(name="排序(列表)")
	private Integer listSort;

	@TableField(value = "ISREQUIRED")
	@Schema(name="是否必填 0-不必填 1-必填")
	private Integer isRequired;

	@NotNull(message = "isSystemRequired 是否系统必填 0-不必填 1-必填 不能为空")
	@TableField(value = "ISSYSTEMREQUIRED")
	@Schema(name="是否系统必填 0-不必填 1-必填")
	private Integer isSystemRequired;

	@TableField(value = "COMPONENTTYPE")
	@Schema(name="组件类型")
	private String componentType;

	@TableField(value = "COMPONENTWIDTH")
	@Schema(name="组件宽度")
	private String componentWidth;

	@TableField(value = "GROUPNAME")
	@Schema(name="组名称")
	private String groupName;

	@TableField(value = "ATTACHEDFIELD")
	@Schema(name="附属字段")
	private String attachedField;

	@TableField(value = "EXTJSON")
	@Schema(name="扩展存储 前端自行extJson进行渲染", hidden = true)
	private String extJson;

	@Transient
	@TableField(exist = false)
	@Schema(name="下拉存储列表 只进行录入操作")
	private List<ShowPullDownExtListResult> pullDownExtList;

	@NotNull(message = "sceneId 关联ID-自定义场景关联ID 不能为空")
	@TableField(value = "SCENEID")
	@Schema(name="关联ID-自定义场景关联ID")
	private BigDecimal sceneId;

	@TableField(value = "ISEDIT")
	@Schema(name="组件是否可编辑 1-可编辑 0-不可编辑")
	private Integer isEdit;

	@TableField(value = "ISASSOCIATE")
	@Schema(name="是否联动 1-是 0-否")
	private Integer isAssociate;

	@Schema(name="联动JSON")
	@TableField(value = "ASSOCIATEINFOJSON")
	private String associateInfoJson;

	@NotNull(message = "sceneCode 场景唯一编码 不能为空")
	@Schema(name="场景唯一编码", required = true)
	@TableField(value = "SCENECODE")
	private String sceneCode;

	@Schema(name="关联ID-展示（编辑模块）ID", hidden = true)
	@TableField(value = "SHOWEXTID")
	private BigDecimal showExtId;

	@Schema(name="状态", hidden = true)
	@TableField(value = "STATE")
	private Integer state;
	
	@Schema(name="创建人ID", hidden = true)
	@TableField(value = "CREATOR")
	private BigDecimal creator;

	@Schema(name="工作单位ID", hidden = true)
	@TableField(value = "WORKUNIT")
	private BigDecimal workUnit;

	@Schema(name="所属集团ID", hidden = true)
	@TableField(value = "BELONGGROUP")
	private BigDecimal belongGroup;

	@Schema(name="创建时间", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(value = "CREATEDTIME")
	private Date createdTime;

	@Schema(name="更新时间", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(value = "UPDATEDTIME")
	private Date updatedTime;

	@TableField(value = "CONTENTJSON")
	@Schema(name="扩展字段")
	private String contentJson;

}