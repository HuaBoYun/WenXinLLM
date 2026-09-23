package com.huabo.know.vo.param;

import com.huabo.know.page.BasePageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class TermsListParam extends BasePageParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name = "标题")
	private String title;

	@Schema(name = "合同类型code")
	private String contractTypeCode;

	@Schema(name = "条款利益倾向方code")
	private String interestPartyCode;

	@Schema(name = "条款类型code")
	private String termsTypeCode;

	@Schema(name = "排序字段-发布时间(issueDate, 正序传asc,倒序传desc)")
	private String issueDateSort;

	@Schema(name = "发布年份")
	private String issueYear;

}
