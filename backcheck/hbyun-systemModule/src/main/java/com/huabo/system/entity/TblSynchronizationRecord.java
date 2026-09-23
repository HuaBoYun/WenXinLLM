package com.huabo.system.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 同步信息记录表
 * </p>
 *
 * @author lhp
 * @since 2025-08-14
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_SYNCHRONIZATION_RECORD")
@Schema(name="TblSynchronizationRecord对象", description="同步信息记录表")
public class TblSynchronizationRecord implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name="主键")
      @TableField("RECORDID")
    private String recordid;

      @Schema(name="同步时间")
      @TableField("CREATETIME")
    private Date createtime;

      @Schema(name="同步类型：org-组织，staff-人员")
      @TableField("RECORDTYPE")
    private String recordtype;

      @Schema(name="响应文本")
      @TableField("RESPONSETEXT")
    private String responsetext;

      @Schema(name="同步结果")
      @TableField("RESULTTEXT")
    private String resulttext;


}
