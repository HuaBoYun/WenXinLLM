package com.huabo.financialdata.entity.vo.gbi;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * GBI导入批次列表返回实体
 * </p>
 *
 * @author 
 * @since 2024-09-24
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name="批次列表返回实体", description="批次列表返回实体")
public class GbiBatchListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name="主键id")
    private String id;

    @Schema(name="appId")
    private String appId;

    @Schema(name="批次编号，目前暂时和id一样，之后可支持批次多文件")
    private String batchNo;

    @Schema(name="原文件名称")
    private String attname;
//
//    @Schema(name="文件ftp地址")
//    private String attpath;

    @Schema(name="文件大小")
    private BigDecimal attsize;

    @Schema(name="用户id")
    private BigDecimal userId;

    @Schema(name="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(name="表信息")
    List<GbiTableListVO> tableList;
}
