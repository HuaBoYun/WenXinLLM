package com.huabo.know.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ZSGX_LIBRARY")
@Schema(name="知识库对象")
public class Tblzsgxlibrary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "LIBRARYID", type= IdType.INPUT)
   @Schema(name = "主键ID")
	private Long libraryid;
	
	@Schema(name = "名称")
    @TableField("WKNANE")
	private String wkname;
	
	@Schema(name = "文号")
    @TableField("WKCODE")
	private String wkcode;
	
	@Schema(name = "效力位阶")
    @TableField("XLLEVEL")
	private String xllevel;
	
	
	@Schema(name = "专题分类")
    @TableField("TOPLICCLASS")
	private String toplicclass;
	
	@Schema(name = "制定机关")
    @TableField("ZDORGAN") 
	private String zdorgan;
	
	@Schema(name = "时效性")
    @TableField("TIMELINESS")
	private String timeliness;
	
	@Schema(name = "法规类别/文件夹名称")
    @TableField("FGCATEGORY")
	private String fgcategory;
	
	@Schema(name = "公布年份")
    @TableField("GBYEAR")
	private String gbyear;
	
	@Schema(name = "文本内容")
    @TableField("FGCONTENT")
	private String fgcontent;
	
	@Schema(name = "页面展示图片")
    @TableField("FGIMAGE")
	private String fgimage;
	
	@Schema(name = "创建时间")
    @TableField("CRETETIME")
	private Date cretetime;
	
	@Schema(name = "类别：1、法律知识 2、合同文本3、知识文库")
    @TableField("LRTYPE")
	private String lrtype;
	
	@Schema(name = "所属公司ID")
    @TableField("CREATEORGANID")
	private Integer createorganid;
	
	@Schema(name = "创建人ID")
    @TableField("CREATESTAFFID")
	private Integer createstaffid;
	
	@Schema(name = "查看次数")
    @TableField("CKCOUNT")
	private Integer ckcount;
	
	@Schema(name = "下载次数")
    @TableField("XZCOUNT")
	private Integer xzcount;
	
	
	@Schema(name="统计汇总数量",hidden=true)
	private Integer hzcount;
	
}
