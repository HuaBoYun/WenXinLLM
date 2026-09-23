package com.huabo.system.vo.result;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblSystemCustomizeShowExtResult implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name="字段")
	private String field;

	@Schema(name="名称")
	private String name;

	@Schema(name="排序")
	private Integer sort;

	@Schema(name="排序(列表)")
	private Integer listSort;

	@Schema(name="是否必填 0-不必填 1-必填")
	private Integer isRequired;

	@Schema(name="是否系统必填 0-不必填 1-必填")
	private Integer isSystemRequired;

	@Schema(name="组件是否可编辑 1-可编辑 0-不可编辑")
	private Integer isEdit;

	@Schema(name="是否联动 1-是 0-否")
	private Integer isAssociate;

	@Schema(name="联动JSON")
	private String associateInfoJson;

	@Schema(name="组件类型")
	private String componentType;

	@Schema(name="组件宽度")
	private String componentWidth;

	@Schema(name="组名称")
	private String groupName;

	@Schema(name="附属字段")
	private String attachedField;

	@Schema(name="扩展存储")
	private String extJson;

	@Schema(name="关联ID-自定义场景关联ID")
	private Integer sceneId;

	@Schema(name="状态")
	private Integer state;

	@Schema(name="创建人ID")
	private Integer creator;

	@Schema(name="工作单位ID")
	private Integer workUnit;

	@Schema(name="所属集团ID")
	private Integer belongGroup;

	@Schema(name="创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	@Schema(name="更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	@Schema(name="是否展示详情页 0-不展示 1-展示")
	private Integer isDetails;

	@Schema(name="是否展示列表页 0-不展示 1-展示")
	private Integer isList;
}
