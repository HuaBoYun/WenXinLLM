package com.huabo.audit.oracle.vo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import com.huabo.audit.oracle.entity.TblAttachment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 整改评价信息表
 * </p>
 *
 * @author LHP
 * @since 2023-11-28
 */
@Data
public class TblZgzzRctevaluationVo implements Serializable {

	 private static final long serialVersionUID = 1L;

     @Schema(name = "主键")
   private String evalId;

     @Schema(name = "创建人主键")
   private BigDecimal createStaff;

     @Schema(name = "创建时间")
     @JSONField(format = "yyyy-MM-dd HH:mm:ss")
     @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
   private Date createTime;

     @Schema(name = "修改人主键")
   private BigDecimal updateStaff;

     @Schema(name = "修改时间")
     @JSONField(format = "yyyy-MM-dd HH:mm:ss")
     @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
   private Date updateTime;

     @Schema(name = "所属部门主键")
   private BigDecimal linkDept;

     @Schema(name = "所属公司主键")
   private BigDecimal linkOrg;

     @Schema(name = "审批状态 0-未评价 1-审批中 2-已退回 3-已撤销 6-已完成")
   private Integer status;

     @Schema(name = "检查过程")
   private String inspectionProcess;

     @Schema(name = "评价人")
   private BigDecimal evaluator;

     @Schema(name = "评价时间")
     @JSONField(format = "yyyy-MM-dd HH:mm:ss")
     @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
   private Date evaluaTime;

     @Schema(name = "整改结果状态 1 or null-未整改，2-已整改未到位、3-已整改到位、4-关闭")
   private Integer resultStatus;

     @Schema(name = "整改落实信息主键")
   private String implId;
     
     @Schema(name = "整改截止时间")
     @JSONField(format = "yyyy-MM-dd HH:mm:ss")
     @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
   private Date rectendTime;

     @Schema(name = "附件列表")
     @IgnoreSwaggerParameter
    @Transient
     private List<TblAttachment> attList = new ArrayList<TblAttachment>(0);

     public String getResultStatusStr(Integer resultStatus) {
	   	if(resultStatus == null) {
	   		return "未整改";
	   	}
   	
 		String statusStr = "";
 		//审批状态 0-未整改 1-审批中 ，2-已退回 3-已撤销 6-已完成 ，7-整改中 ，8-整改完成，9-未销号问题
 		switch (resultStatus) {
 			case 2:
 				statusStr = "已整改未到位";
 				break;
 			case 3:
 				statusStr = "已整改到位";
 				break;
 			case 4:
 				statusStr = "关闭";
 				break;
 			default:
 				statusStr = "未整改";
 				break;
 		}
 		return statusStr;
 	}

}
