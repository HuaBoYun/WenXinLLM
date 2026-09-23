package com.huabo.system.vo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.huabo.system.vo.param.PageableParam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 系统业务单据下发通知表
 * </p>
 *
 * @author LHP
 * @since 2023-11-24
 */
@Data
@Schema(name="TblAuthorizationRecord返回对象", description="确认信息")
public class TblAuthorizationRecordVo extends PageableParam implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Schema(name="操作记录标题")
    private String recordText;

    @Schema(name="操作类型 1-新增，2-修改，3-删除，4-启用，5-弃用，6-重置密码")
    private int operationType;

    @Schema(name="操作数据类型")
    private String targetType;

    @Schema(name="操作数据主键")
    private String targetId;

    @TableField("STATUS")
    @Schema(name="审批状态 1-审批中、2-需调整、3-已撤销、4-已终止、5-已跟踪、6-已完成")
    private Integer status;

}
