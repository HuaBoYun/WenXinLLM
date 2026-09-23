package com.huabo.es.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * tbl_accbook
 *
 * @author : caozhibo
 * @date : 2023-3-22
 */
@Schema
@TableName("tbl_accbook")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TblAccbook implements Serializable, Cloneable {
    /**
     *
     */
    @Schema
    @TableId
    private String bookid;
    /**
     *
     */
    @Schema
    private String bookname;
    /**
     *
     */
    @Schema
    private Long orgid;
    /**
     *
     */
    @Schema
    private String orgname;
    /**
     *
     */
    @Schema
    private String acctid;
    /**
     *
     */
    @Schema
    private String bookdesc;
    /**
     *
     */
    @Schema
    private String bookyear;
    /**
     *
     */
    @Schema
    private String balancesheeturl;
    /**
     *
     */
    @Schema
    private String incomestatementsurl;
    /**
     *
     */
    @Schema
    private String cashflowstatementsurl;

}