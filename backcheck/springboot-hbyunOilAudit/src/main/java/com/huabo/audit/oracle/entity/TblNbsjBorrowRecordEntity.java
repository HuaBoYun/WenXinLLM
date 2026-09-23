package com.huabo.audit.oracle.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_BORROWRECORD")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjBorrowRecordEntity {
	
	public final static String TYPE_DAJY = "档案借阅";
	public final static String URL_JYSP = "/nbsj/sjgd/toBorrowinfo?borrowid=";//档案借阅详情路径
	/**
	 * 0未审批 
	 */
	public final static Integer SPNO=0;
	/**
	 * 1 审批中
	 */
	public final static Integer SPKA=1;
	/**
	 * 2 需调整  
	 */
	public final static Integer SPTZ=2;
	/**
	 * 3审批完
	 */
	public final static Integer SPZZ=4;
//	private static final long serialVersionUID = -1355695941497663207L;
	
	@TableId(value = "BORROWID", type= IdType.AUTO)
	@Schema
	private Integer borrowid; //借阅id
	
	@TableField(value = "CREATEDATE")
	@Schema
	private Date createDate; //借阅时间
	
	@TableField(value = "RETURNDATE")
	@Schema
	private Date returnDate;//归还时间
	
	@TableField(value = "STATUS")
	@Schema
	private Integer status;//状态
	
	@TableField(value = "MEMO")
	@Schema
	private String memo; //借阅事由
	
	@TableField(value = "tblstaff")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblStaff tblstaff;
	
	@TableField(value = "tblproject")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblNbsjProject tblproject;
	
	@TableField(value = "STAFFID")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Integer staffid;//申请人id
	
	@Schema(hidden=true)
	private String staffname;

	@TableField(value = "PROJECTID")
	@Schema
	private Integer projectid;
	
	//项目名称
	private String prjoectname;
	
	//项目编码
	private String projectcode;
//	
//	
//	public TblNbsjBorrowRecordEntity(BigDecimal borrowid, TblNbsjProject tblproject,TblStaff tblstaff,  Date createDate,
//			Date returnDate, String status, String memo) {
//		super();
//		this.borrowid = borrowid;
//		this.tblproject = tblproject;
//		this.tblstaff = tblstaff;
//		this.createDate = createDate;
//		this.returnDate = returnDate;
//		this.status = status;
//		this.memo = memo;
//	}
	
	@TableField(value = "borrowDate")
	@Schema(hidden=true)
	private String borrowDate;//借阅日期
	
	@TableField(value = "backDate")
	@Schema(hidden=true)
	private String backDate;//归还日期
	

}
