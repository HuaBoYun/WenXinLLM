package com.huabo.audit.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqk;
import com.huabo.audit.oracle.mapper.TblYqnsGcxmzjZjbMapper;
import com.huabo.audit.oracle.mapper.TblYqnsJsxmTzwcqkMapper;
import com.huabo.audit.service.business.ProjectSettlementService;
import com.huabo.audit.util.MyJsonBean;
import com.huabo.audit.util.MyResponseFormat;
import com.huabo.audit.util.PageResult;
import com.huabo.audit.vo.param.ProjectSettlementCompletionQueryParam;
import com.huabo.audit.vo.param.ProjectSettlementCostIntermediateQueryParam;
import com.huabo.audit.vo.result.ProjectSettlementCompletionResult;
import com.huabo.audit.vo.result.ProjectSettlementCostIntermediateResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProjectSettlementServiceImpl implements ProjectSettlementService {

	@Resource
	private TblYqnsGcxmzjZjbMapper tblYqnsGcxmzjZjbMapper;
	@Resource
	private TblYqnsJsxmTzwcqkMapper tblYqnsJsxmTzwcqkMapper;

	/**
	 * 工程结算审计项目汇总 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<ProjectSettlementCostIntermediateResult> getProjectSettlementCostIntermediateList(
			ProjectSettlementCostIntermediateQueryParam param) {
		PageInfo<ProjectSettlementCostIntermediateResult> pageInfo = PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblYqnsGcxmzjZjbMapper.findProjectSettlementCostIntermediateList(param));
		if (CollectionUtil.isEmpty(pageInfo.getList())) {
			return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
		}
		PageResult<ProjectSettlementCostIntermediateResult> build = new PageResult<ProjectSettlementCostIntermediateResult>().build(pageInfo);
		return MyResponseFormat.retParam(200, 200, build);
	}

	/**
	 * 工程结算审计项目汇总 详情查询
	 * @param gcxmzjzjbid
	 * @return
	 */
	@Override
	public MyJsonBean<ProjectSettlementCostIntermediateResult> getProjectSettlementCostIntermediate(BigDecimal gcxmzjzjbid) {
		ProjectSettlementCostIntermediateQueryParam queryParam = new ProjectSettlementCostIntermediateQueryParam();
		queryParam.setGcxmzjzjbid(gcxmzjzjbid);
		List<ProjectSettlementCostIntermediateResult> list = tblYqnsGcxmzjZjbMapper.findProjectSettlementCostIntermediateList(queryParam);
		if (CollectionUtil.isEmpty(list)) {
			return MyResponseFormat.retParam(200, 200, null);
		}
		return MyResponseFormat.retParam(200, 200, list.get(0));
	}

	/**
	 * 竣工决算审计项目汇总 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public MyJsonBean<ProjectSettlementCompletionResult> getProjectSettlementCompletionList(ProjectSettlementCompletionQueryParam param) {
		QueryWrapper<TblYqnsJsxmTzwcqk> wrapper = new QueryWrapper<>();
		//合同编号模糊查询
		if (StringUtils.isNotBlank(param.getHtbh())) {
			wrapper.lambda().like(TblYqnsJsxmTzwcqk::getHtbh, param.getHtbh());
		}
		//主键排序倒序
		wrapper.orderByDesc(true, "JSXMTZWCQKID");
		PageInfo<TblYqnsJsxmTzwcqk> pageInfo = PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblYqnsJsxmTzwcqkMapper.selectList(wrapper));
		if (CollectionUtil.isEmpty(pageInfo.getList())) {
			return MyResponseFormat.retParam(200, 200, PageResult.buildNoData());
		}
		PageInfo<ProjectSettlementCompletionResult> pageInfoCope = new PageInfo<>();
		BeanUtils.copyProperties(pageInfo, pageInfoCope);
		//复制对象 简化实体类的繁琐输出
		pageInfoCope.setList(pageInfo.getList().stream().map(item -> {
			ProjectSettlementCompletionResult result = new ProjectSettlementCompletionResult();
			BeanUtils.copyProperties(item, result);
			return result;
		}).collect(Collectors.toList()));
		PageResult<ProjectSettlementCompletionResult> build = new PageResult<ProjectSettlementCompletionResult>().build(pageInfoCope);
		return MyResponseFormat.retParam(200, 200, build);
	}

	/**
	 * 竣工决算审计项目汇总 详情查询
	 * @param jsxmtzwcqkid
	 * @return
	 */
	@Override
	public MyJsonBean<ProjectSettlementCompletionResult> getProjectSettlementCompletion(Long jsxmtzwcqkid) {
		QueryWrapper<TblYqnsJsxmTzwcqk> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(TblYqnsJsxmTzwcqk::getJsxmtzwcqkid, jsxmtzwcqkid);
		TblYqnsJsxmTzwcqk model = tblYqnsJsxmTzwcqkMapper.selectOne(wrapper);
		if (Objects.isNull(model)) {
			return MyResponseFormat.retParam(200, 200, null);
		}
		ProjectSettlementCompletionResult result = new ProjectSettlementCompletionResult();
		BeanUtils.copyProperties(model, result);
		return MyResponseFormat.retParam(200, 200, result);
	}
}
