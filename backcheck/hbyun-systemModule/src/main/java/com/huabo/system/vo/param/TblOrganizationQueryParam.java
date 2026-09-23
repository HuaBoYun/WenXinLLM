package com.huabo.system.vo.param;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
@Data
@Schema(name="组织信息查询参数", description="组织信息查询参数")
public class TblOrganizationQueryParam implements Serializable {

	private static final long serialVersionUID = -5027488243662163691L;

	@Schema(name="公司主键")
    private BigDecimal orgid;
	
	@Schema(name="父级公司主键，为空默认查询所有")
	private BigDecimal fatherorgid;

    @Schema(name="公司名称")
    private String orgname;

    @Schema(name="公司编号")
    private String orgnumber;

    @Schema(name="公司简介")
    private String orgmeno;

    @Schema(name="备注")
    private String memo;

    @Schema(name="行业编号")
    private String icode;///行业架构ID（在哪个行业下创建行业知识库/行业缺陷库/行业问题库/行业数据库/行业指标库/行业规则库/行业模型库，该字段为哪个行业ID）

    @Schema(name="默认查询所有 状态 1弃用 ，0-启用")
    private Integer status;

    @Schema(name="创建时间")
    private Date orgcreate;

    @Schema(name="职务")
    private String duties;

    @Schema(name="行业编号")
    private Integer industryid;

    @Schema(name="新增来源于微信 1为微信 0为pc")
    private String bywx;

    @Schema(name="历史ID")
    private String historycode;
    
    @Schema(name="公司类型 0-部门，1-100 公司，100+ 行业架构")
    private Integer orgtype;

    @Schema(name="历史部门ID")
    private String historydepartmentid;
    
    @Schema(name="发文代字")
    private String writtenByDept;
    
    @Schema(name="父级公司名称")
    private String fahterOrgName;
    
    @Schema(name="公司Id主键 逗号拼接")
    private String orgIdStrs;
    
    @Schema(name="公司集合主键")
    private List<String> orgIdList;
}
