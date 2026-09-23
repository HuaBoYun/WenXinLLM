package com.huabo.system.flow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name="审批操作", description="审批操作")
public class FlowModel {

	@Schema(name="批量审批类型 0.通过 1.拒绝 2.转办")
    private Integer batchType = 0;
	
	@Schema(name="选择分支")
	private List<String> branchList = new ArrayList<String>();
	 
	@Schema(name="候选人candidateList	:{H4VYCH2:{a,b,c},B:{....}}")
	private Map<String, List<String>> candidateList = new HashMap<>();
	
	@Schema(name="1.有分支  2.没有分支有候选人  3.没有分支也没有候选人")
	private Integer candidateType;
	
	@Schema(name="自定义抄送人")
    private String copyIds;
	
	@Schema(name="流程编号flowCode")
    private String enCode;
	
	@Schema(name="加签人业务中台主键")
    private String pkYmStaffId;
	
	@Schema(name="加签人星光系统主键")
    private BigDecimal freeApproverStaffId;
	
	@Schema(name="审批意见")
	private String handleOpinion;
	
	@Schema(name="批量审批id")
    private List<String> ids = new ArrayList<>();
	
	@Schema(name="签名")
	private String signImg;
	
	@Schema(name="流程引擎主键")
	private String flowId;
	
	private List<FlowTaskOperator> taskList = new ArrayList<FlowTaskOperator>(0);
}
