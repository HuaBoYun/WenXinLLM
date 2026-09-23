package com.global.treasurer.dto;

// import lombok.Data; // 已移除

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 票据到期处理DTO
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
// @Data // 已移除,使用手动编写的getter/setter
public class BillMaturityProcessDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 到期记录ID列表 */
    @NotNull(message = "到期记录ID不能为空")
    private List<Long> maturityIds;

    /** 处理类型: COLLECT-托收, DISCOUNT-贴现, ENDORSE-背书, EXTEND-展期 */
    @NotBlank(message = "处理类型不能为空")
    private String processType;

    /** 处理日期 */
    private Date processDate;

    /** 处理说明 */
    private String processDescription;


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public List<Long> getMaturityIds() { return maturityIds; }
    public void setMaturityIds(List<Long> maturityIds) { this.maturityIds = maturityIds; }
    public String getProcessType() { return processType; }
    public void setProcessType(String processType) { this.processType = processType; }
    public Date getProcessDate() { return processDate; }
    public void setProcessDate(Date processDate) { this.processDate = processDate; }
    public String getProcessDescription() { return processDescription; }
    public void setProcessDescription(String processDescription) { this.processDescription = processDescription; }

}
