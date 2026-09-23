package com.huabo.know.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2024-04-04
 */
@Getter
@Setter
  @TableName("TBL_ZSGX_LABEL_TYPE")
@Schema(name="TblZsgxLabelType对象")
public class TblZsgxLabelType implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId("ID")
      private String id;

    @TableField("TYPE_VALUE")
    private String typeValue;

    @TableField("TYPE_NAME")
    private String typeName;

    @TableField("SORT")
    private BigDecimal sort;

    @TableField("SELECTED")
    private BigDecimal selected;

    @TableField("CREATE_COMPANY")
    private String createCompany;

    @TableField("CREATE_DEPT")
    private String createDept;

    @TableField("CREATE_BY")
    private String createBy;

    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    @TableField("UPDATE_BY")
    private String updateBy;

    @TableField("DELETED")
    private BigDecimal deleted;


}
