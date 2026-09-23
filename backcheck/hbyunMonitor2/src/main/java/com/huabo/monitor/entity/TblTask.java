package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-08-30
 */
@TableName("TBL_TASK")
@Schema(name="TblTask对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TblTask implements Serializable {
    public static final String START = "0";
    public static final String END = "1";
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.INPUT)
    private BigDecimal taskid;

    private String taskname;

    private String taskcode;

    private String url;
    //
    private BigDecimal staffid;

    private LocalDateTime tasktime;

    private String status;

}
