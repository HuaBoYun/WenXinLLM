package com.huabo.monitor.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_VERSION")
@Schema(name="TblVersionMySql")
public class TblVersionMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("FID")
    private BigDecimal fid;
    @TableField("FVENDOR")
    private String fvendor;//厂商:1.金蝶    2.用友
    @TableField("DATABASETYPE")
    private String databaseType;//Bathdata数据库类型
    @TableField("FATHERID")
    private BigDecimal fatherid;

    private List<TblVersionMySql> chiVerList = new ArrayList<TblVersionMySql>();//

}
