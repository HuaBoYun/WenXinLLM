package com.huabo.legal.test.vo.param;

import com.huabo.legal.util.PageableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestQueryParam extends PageableParam {

	@Schema(name="主键ID")
	private Integer id;

	@Schema(name="名称")
	private String name;

	@Schema(name="主键IDS")
	private List<Integer> ids;

	@Schema(name="当前操作人的所属集团以及子集团所有 集团IDS",hidden=true)
	private List<Long> belongGroupList;

	@Schema(name="开始时间")
	private Date beginTime;

	@Schema(name="结束时间")
	private Date endTime;
}
