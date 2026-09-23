package com.huabo.legal.test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.test.mybatis.entity.TestMyBatisEntity;
import com.huabo.legal.test.mybatisplus.entity.TestMyBatisPlusEntity;
import com.huabo.legal.test.mybatis.mapper.TestMyBatisMapper;
import com.huabo.legal.test.mybatisplus.mapper.TestMyBatisPlusMapper;
import com.huabo.legal.test.service.TestService;
import com.huabo.legal.test.vo.param.TestQueryParam;
import com.huabo.legal.util.PageResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import com.vip.vjtools.vjkit.time.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class TestServiceImpl implements TestService {

	@Resource
	private TestMyBatisMapper testMyBatisMapper;
	@Resource
	private TestMyBatisPlusMapper testMyBatisPlusMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;


	/**
	 * mybatis 单表查询分页
	 * @param queryParam
	 * @return
	 */
	@Override
	public JsonBean getMybatisSinglePageList(TestQueryParam queryParam) {

		//假设 当前操作人所属集团ID = 1
		Long belongGroup = 1L;

		//查询 当前操作人的所属集团ID以及子集团所有 集团IDS
		//这个集团数组就是替代原sql
		//belongGroup in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID= " + param.getBelongGroup()
		//					+ " and ORGTYPE!=0 AND " + "ORGTYPE<100  connect by prior orgid= FATHERORGID) or belongGroup = " + param.getBelongGroup()+")
		List<Long> belongGroupList = tblStaffOracleService.getTblOrganizationAll(belongGroup);
		queryParam.setBelongGroupList(belongGroupList);

		//分页查询
		Example example = new Example(TestMyBatisEntity.class);
		Example.Criteria criteria = example.createCriteria();
		//查询 需要判空 在查询 精准查询
		if (Objects.nonNull(queryParam.getId())) {
			criteria.andEqualTo("id", queryParam.getId());
		}
		//查询 需要判空 在查询 数组查询
		if (CollectionUtil.isNotEmpty(queryParam.getIds())) {
			criteria.andIn("id", queryParam.getIds());
		}
		//查询 需要判空 在查询 模糊查询
		if (StringUtils.isNotBlank(queryParam.getName())) {
			criteria.andLike("name", "%" + queryParam.getName() + "%");
		}
		//查询 需要判空 在查询 某个字段 大于且等于 某个时间
		if (queryParam.getBeginTime() != null) {
			criteria.andGreaterThanOrEqualTo("createTime", queryParam.getBeginTime());
		}
		//查询 需要判空 在查询 某个字段 小于 某个时间
		if (queryParam.getEndTime() != null) {
			//addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
			criteria.andLessThan("createTime", DateUtil.addDays(queryParam.getEndTime(), 1));
		}
		//查询 需要判空 在查询 数组查询
		if (CollectionUtil.isNotEmpty(queryParam.getBelongGroupList())) {
			criteria.andIn("belongGroup", queryParam.getBelongGroupList());
		}
		//创建时间倒序
		example.setOrderByClause(" createTime desc");

		//单表分页
		PageInfo<TestMyBatisEntity> pageInfo = PageMethod.startPage(queryParam.getPageNumber(), queryParam.getPageSize())
				.doSelectPageInfo(() -> testMyBatisMapper.selectByExample(example));

		//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
		PageResult<TestMyBatisEntity> build = new PageResult<TestMyBatisEntity>().build(pageInfo);
		return ResponseFormat.retParam(200, 200, build);
	}

	/**
	 * mybatis 链表分页查询
	 * @param queryParam
	 * @return
	 */
	@Override
	public JsonBean getMybatisDoublePageList(TestQueryParam queryParam) {

		//假设 当前操作人所属集团ID = 1
		Long belongGroup = 1L;

		//查询 当前操作人的所属集团ID以及子集团所有 集团IDS
		//这个集团数组就是替代原sql
		//belongGroup in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID= " + param.getBelongGroup()
		//					+ " and ORGTYPE!=0 AND " + "ORGTYPE<100  connect by prior orgid= FATHERORGID) or belongGroup = " + param.getBelongGroup()+")
		List<Long> belongGroupList = tblStaffOracleService.getTblOrganizationAll(belongGroup);
		queryParam.setBelongGroupList(belongGroupList);

		//链表分页  xml 写法
		PageInfo<TestMyBatisEntity> pageInfo = PageMethod.startPage(queryParam.getPageNumber(), queryParam.getPageSize())
				.doSelectPageInfo(() -> testMyBatisMapper.findList(queryParam));

		//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
		PageResult<TestMyBatisEntity> build = new PageResult<TestMyBatisEntity>().build(pageInfo);
		return ResponseFormat.retParam(200, 200, build);
	}

	/**
	 * mybatis-plus 单表分页查询
	 * @param queryParam
	 * @return
	 */
	@Override
	public JsonBean getMybatisPlusSinglePageList(TestQueryParam queryParam) {
		//假设 当前操作人所属集团ID = 1
		Long belongGroup = 1L;

		//查询 当前操作人的所属集团ID以及子集团所有 集团IDS
		//这个集团数组就是替代原sql
		//belongGroup in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID= " + param.getBelongGroup()
		//					+ " and ORGTYPE!=0 AND " + "ORGTYPE<100  connect by prior orgid= FATHERORGID) or belongGroup = " + param.getBelongGroup()+")
		List<Long> belongGroupList = tblStaffOracleService.getTblOrganizationAll(belongGroup);
		queryParam.setBelongGroupList(belongGroupList);

		//分页查询
		//查询条件
		QueryWrapper<TestMyBatisPlusEntity> wrapper = new QueryWrapper<>();
		//查询 需要判空 在查询 精准查询
		if (Objects.nonNull(queryParam.getId())) {
			wrapper.lambda().eq(TestMyBatisPlusEntity::getId, queryParam.getId());
		}
		//查询 需要判空 在查询 数组查询
		if (CollectionUtil.isNotEmpty(queryParam.getIds())) {
			wrapper.lambda().in(TestMyBatisPlusEntity::getId, queryParam.getIds());
		}
		//查询 需要判空 在查询 模糊查询
		if (StringUtils.isNotBlank(queryParam.getName())) {
			wrapper.lambda().like(TestMyBatisPlusEntity::getName, queryParam.getName());
		}
		//查询 需要判空 在查询 某个字段 大于且等于 某个时间
		if (queryParam.getBeginTime() != null) {
			wrapper.lambda().ge(TestMyBatisPlusEntity::getCreateTime, queryParam.getBeginTime());
		}
		//查询 需要判空 在查询 某个字段 小于 某个时间
		if (queryParam.getEndTime() != null) {
			//addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
			wrapper.lambda().lt(TestMyBatisPlusEntity::getCreateTime, DateUtil.addDays(queryParam.getEndTime(), 1));
		}
		//查询 需要判空 在查询 数组查询
		if (CollectionUtil.isNotEmpty(queryParam.getBelongGroupList())) {
			wrapper.lambda().in(TestMyBatisPlusEntity::getBelongGroup, queryParam.getBelongGroupList());
		}
		//倒序
		wrapper.orderByDesc(true, "createTime");
		//升序 wrapper.orderByAsc(true,"createTime");
		//获得数据
		PageInfo<TestMyBatisPlusEntity> pageInfo = PageMethod.startPage(queryParam.getPageNumber(), queryParam.getPageSize())
				.doSelectPageInfo(() -> testMyBatisPlusMapper.selectList(wrapper));
		PageResult<TestMyBatisPlusEntity> build = new PageResult<TestMyBatisPlusEntity>().build(pageInfo);
		//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
		return ResponseFormat.retParam(200, 200, build);
	}

	/**
	 * mybatis-plus 链表分页查询
	 * @param queryParam
	 * @return
	 */
	@Override
	public JsonBean getMybatisPlusDoublePageList(TestQueryParam queryParam) {
		//假设 当前操作人所属集团ID = 1
		Long belongGroup = 1L;

		//查询 当前操作人的所属集团ID以及子集团所有 集团IDS
		//这个集团数组就是替代原sql
		//belongGroup in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID= " + param.getBelongGroup()
		//					+ " and ORGTYPE!=0 AND " + "ORGTYPE<100  connect by prior orgid= FATHERORGID) or belongGroup = " + param.getBelongGroup()+")
		List<Long> belongGroupList = tblStaffOracleService.getTblOrganizationAll(belongGroup);
		queryParam.setBelongGroupList(belongGroupList);

		//链表分页  xml 写法
		PageInfo<TestMyBatisEntity> pageInfo = PageMethod.startPage(queryParam.getPageNumber(), queryParam.getPageSize())
				.doSelectPageInfo(() -> testMyBatisPlusMapper.findList(queryParam));

		//分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
		PageResult<TestMyBatisEntity> build = new PageResult<TestMyBatisEntity>().build(pageInfo);
		return ResponseFormat.retParam(200, 200, build);
	}
}
