package com.huabo.monitor.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Id;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author yhr
 * @since 2022-09-13
 */
@Data
@TableName("TBL_TESTTEMPL_TYPE")
@Schema(name="TblTesttemplType对象")
@KeySequence(value="HIBERNATE_SEQUENCE")
public class TblTesttemplType implements Serializable {


    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.INPUT)
    @Id
    @Column(name = "TYPEID")
	@Schema(name = "类型主键")
    private BigDecimal typeid;

    @TableField("TYPENAME")
   	@Column(name = "TYPENAME")
   	@Schema(name = "类型名称")
    private String typename;

    @TableField("TYPEDESC")
   	@Column(name = "TYPEDESC")
   	@Schema(name = "类型描述")
    private String typedesc;

    @TableField("PARENTID")
   	@Column(name = "PARENTID")
   	@Schema(name = "父级类型主键")
    private BigDecimal parentid;

    @TableField("TESTTEMPLETAID")
   	@Column(name = "TESTTEMPLETAID")
   	@Schema(name = "测试模板主键")
    private BigDecimal testtempletaid;

    @TableField("CREATETIME")
   	@Column(name = "CREATETIME")
   	@Schema(name = "创建时间")
    private LocalDateTime createtime;

    @TableField("TYPECODE")
   	@Column(name = "TYPECODE")
   	@Schema(name = "类型编码")
    private String typecode;

}
