package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.system.vo.param.ShowPullDownExtListResult;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义展示-编辑表
 */
@Schema(name = "TblSystemCustomizeShowExt")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "TBL_SYSTEM_CUSTOMIZE_SHOW_EXT")
public class TblSystemCustomizeShowExt implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键ID
	 */
	@Schema(name="主键ID")
	@TableId(value = "ID", type = IdType.INPUT)
	private BigDecimal id;

	/**
	 * 字段
	 */
	@TableField(value = "FIELD")
	@Schema(name="字段")
	private String field;

	/**
	 * 原名称
	 */
	@TableField(value = "ORLNAME")
	@Schema(name="原名称")
	private String orlName;

	/**
	 * 变更名称
	 */
	@TableField(value = "CHANGENAME")
	@Schema(name="变更名称")
	private String changeName;

	/**
	 * 是否使用变更名称 0-不使用 1-使用
	 */
	@TableField(value = "ISCHANGE")
	@Schema(name="是否使用变更名称 0-不使用 1-使用")
	private Integer isChange;

	/**
	 * 是否展示详情页 0-不展示 1-展示
	 */
	@TableField(value = "ISDETAILS")
	@Schema(name="是否展示详情页 0-不展示 1-展示")
	private Integer isDetails;

	/**
	 * 是否展示列表页 0-不展示 1-展示
	 */
	@TableField(value = "ISLIST")
	@Schema(name="是否展示列表页 0-不展示 1-展示")
	private Integer isList;

	/**
	 * 排序
	 */
	@TableField(value = "SORT")
	@Schema(name="排序(详细)")
	private Integer sort;

	@TableField(value = "LISTSORT")
	@Schema(name="排序(列表)")
	private Integer listSort;


	/**
	 * 是否必填 0-不必填 1-必填
	 */
	@TableField(value = "ISREQUIRED")
	@Schema(name="是否必填 0-不必填 1-必填")
	private Integer isRequired;

	@TableField(value = "ISSYSTEMREQUIRED")
	@Schema(name="是否系统必填 0-不必填 1-必填")
	private Integer isSystemRequired;

	/**
	 * 组件类型
	 */
	@TableField(value = "COMPONENTTYPE")
	@Schema(name="组件类型")
	private String componentType;

	/**
	 * 组件宽度
	 */
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
	@Schema(name="下拉存储列表 只进行录入操作")
	@TableField(exist = false)
	private List<ShowPullDownExtListResult> pullDownExtList;

	/**
	 * 关联ID-自定义场景关联ID
	 */
	@TableField(value = "SCENEID")
	@Schema(name="关联ID-自定义场景关联ID")
	private BigDecimal sceneId;

	@TableField(value = "ISASSOCIATE")
	@Schema(name="是否联动 1-是 0-否")
	private Integer isAssociate;

	@TableField(value = "ASSOCIATEINFOJSON")
	@Schema(name="联动JSON")
	private String associateInfoJson;

	@TableField(value = "ISEDIT")
	@Schema(name="组件是否可编辑 1-可编辑 0-不可编辑")
	private Integer isEdit;

	@TableField(value = "SCENECODE")
	@Schema(name="场景唯一编码", required = true)
	private String sceneCode;

	/**
	 * 状态
	 */
	@TableField(value = "STATE")
	@Schema(name="状态", hidden = true)
	private Integer state;

	/**
	 * 创建人ID
	 */
	@TableField(value = "CREATOR")
	@Schema(name="创建人ID", hidden = true)
	private BigDecimal creator;

	/**
	 * 工作单位ID
	 */
	@TableField(value = "WORKUNIT")
	@Schema(name="工作单位ID", hidden = true)
	private BigDecimal workUnit;

	/**
	 * 所属集团ID
	 */
	@TableField(value = "BELONGGROUP")
	@Schema(name="所属集团ID", hidden = true)
	private BigDecimal belongGroup;

	/**
	 * 创建时间
	 */
	@TableField(value = "CREATEDTIME")
	@Schema(name="创建时间", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	/**
	 * 更新时间
	 */
	@TableField(value = "UPDATEDTIME")
	@Schema(name="更新时间", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	@TableField(value = "CONTENTJSON")
	@Schema(name="扩展字段")
	private String contentJson;

	public static TblSystemCustomizeShowExt ofId(BigDecimal id) {
		TblSystemCustomizeShowExt tblSystemCustomizeShowExt = new TblSystemCustomizeShowExt();
		tblSystemCustomizeShowExt.setId(id);
		return tblSystemCustomizeShowExt;
	}

	public static TblSystemCustomizeShowExt ofState(BigDecimal id, Integer state) {
		TblSystemCustomizeShowExt tblSystemCustomizeShowExt = new TblSystemCustomizeShowExt();
		tblSystemCustomizeShowExt.setId(id);
		tblSystemCustomizeShowExt.setState(state);
		return tblSystemCustomizeShowExt;
	}

	public static TblSystemCustomizeShowExt ofState(Integer state) {
		TblSystemCustomizeShowExt tblSystemCustomizeShowExt = new TblSystemCustomizeShowExt();
		tblSystemCustomizeShowExt.setState(state);
		return tblSystemCustomizeShowExt;
	}
}