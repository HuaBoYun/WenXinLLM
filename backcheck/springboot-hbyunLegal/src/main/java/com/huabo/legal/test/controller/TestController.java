package com.huabo.legal.test.controller;

import com.hbfk.util.JsonBean;
import com.huabo.legal.test.service.TestService;
import com.huabo.legal.test.vo.param.TestQueryParam;
import com.vip.vjtools.vjkit.collection.ListUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Tag(name="实例",description="实例")
@RequestMapping(value = "/api-auth/legal/test")
@Slf4j
public class TestController {

	@Resource
	private TestService testService;

	@Operation(summary="mybatis 单表分页查询")
	@PostMapping("/mybatis/example/1")
	public JsonBean test1(@RequestBody TestQueryParam testQueryParam) {
		TestQueryParam queryParam = new TestQueryParam();
		queryParam.setPageNumber(1); //当前页数
		queryParam.setPageSize(20); //一页多少条

		return testService.getMybatisSinglePageList(testQueryParam);
	}

	@Operation(summary="mybatis 链表分页查询")
	@PostMapping("/mybatis/example/2")
	public JsonBean test2(@RequestBody TestQueryParam testQueryParam) {
		TestQueryParam queryParam = new TestQueryParam();
		queryParam.setPageNumber(1); //当前页数
		queryParam.setPageSize(20); //一页多少条

		return testService.getMybatisDoublePageList(testQueryParam);
	}

	@Operation(summary="mybatis-plus 单表分页查询")
	@PostMapping("/mybatis-plus/example/1")
	public JsonBean test3(@RequestBody TestQueryParam testQueryParam) {
		TestQueryParam queryParam = new TestQueryParam();
		queryParam.setPageNumber(1); //当前页数
		queryParam.setPageSize(20); //一页多少条

		return testService.getMybatisPlusSinglePageList(testQueryParam);
	}

	@Operation(summary="mybatis-plus 链表分页查询")
	@PostMapping("/mybatis-plus/example/2")
	public JsonBean test4(@RequestBody TestQueryParam testQueryParam) {
		TestQueryParam queryParam = new TestQueryParam();
		queryParam.setPageNumber(1); //当前页数
		queryParam.setPageSize(20); //一页多少条

		return testService.getMybatisPlusDoublePageList(testQueryParam);
	}
}
