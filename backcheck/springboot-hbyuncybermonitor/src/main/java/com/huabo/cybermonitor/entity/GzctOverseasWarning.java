package com.huabo.cybermonitor.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.extension.activerecord.Model;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("GZCT_OVERSEAS_WARNING")
public class GzctOverseasWarning extends Model<GzctOverseasWarning> {
    @TableId(value = "WARNING_ID", type = IdType.ASSIGN_UUID) private String warningId;
    @TableField("COMPANY_ID") private String companyId;
    // 前端用 unitName，对应 companyName
    @TableField("COMPANY_NAME") private String companyName;
    @TableField("UNIT_NAME") private String unitName;
    @TableField("COUNTRY") private String country;
    @TableField("WARNING_TYPE") private String warningType;
    // 前端用 description，对应 warningContent
    @TableField("WARNING_CONTENT") private String warningContent;
    @TableField("DESCRIPTION") private String description;
    @TableField("LEVEL") private String level;
    @TableField("STATUS") private String status;
    @TableField("HANDLE_RESULT") private String handleResult;
    // 前端用 warningTime，对应 createTime
    @TableField("WARNING_TIME") private LocalDateTime warningTime;
    @TableField("HANDLER") private String handler;
    @TableField("PROCESS_REMARK") private String processRemark;
    @TableField("PROCESS_TIME") private LocalDateTime processTime;
    @TableField("CREATE_TIME") private LocalDateTime createTime;
    @TableField("UPDATE_TIME") private LocalDateTime updateTime;
}
