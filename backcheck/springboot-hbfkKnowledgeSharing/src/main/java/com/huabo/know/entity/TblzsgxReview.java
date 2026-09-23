package com.huabo.know.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ZSGX_REVIEW")
@Schema(name="知识共享-检查清单对象")
public class TblzsgxReview implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@TableId(value = "ID", type= IdType.INPUT)
    @Schema(name = "主键ID")
	private String id;
	
	@Schema(name = "名称")
    @TableField("NAME")
	private String name;
	
	@Schema(name = "编号")
    @TableField("CODE")
	private String code;
	
	@Schema(name = "类型")
    @TableField("TYPE")
	private String type;
	
	@Schema(name = "排序")
    @TableField("SORT")
	private Integer sort;
	
	@Schema(name = "选中")
    @TableField("SELECTED")
	private Integer selected;
	
	@Schema(name = "创建公司")
    @TableField("CREATE_COMPANY")
	private String createCompany;

	@Schema(name = "创建部门")
	@TableField("CREATE_DEPT")
	private String createDept;

	@Schema(name = "创建人")
	@TableField("CREATE_BY")
	private String createBy;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Schema(name = "创建时间")
	@TableField("CREATE_TIME")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Schema(name = "修改时间")
    @TableField("UPDATE_TIME")
	private Date updateTime;
	
	@Schema(name = "是否删除")
    @TableField("DELETED")
	private Integer deleted;
	
}
