package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExamineTopicFirst;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaExamineTopicFirstMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaExamineTopicFirstService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExamineTopicFirstQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TblCeaExamineTopicFirstServiceImpl implements TblCeaExamineTopicFirstService {

	@Resource
	private TblCeaExamineTopicFirstMapper tblCeaExamineTopicFirstMapper;

	/**
	 * 考核题目-首部分 列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblCeaExamineTopicFirst> getList(TblCeaExamineTopicFirstQueryParam param) {
		Example example = new Example(TblCeaExamineTopicFirst.class);
		Example.Criteria criteria = example.createCriteria();
		if (param.getExamineType() != null) {
			criteria.andEqualTo("examineType", param.getExamineType());
		}
		if (Objects.nonNull(param.getExamineEmphasis())) {
			criteria.andLike("examineEmphasis", "%" + param.getExamineEmphasis() + "%");
		}
		
		example.setOrderByClause(" id desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaExamineTopicFirstMapper.selectByExample(example));
	}

	/**
	 * 考核题目-首部分 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblCeaExamineTopicFirst saveOrUpdate(TblCeaExamineTopicFirst param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaExamineTopicFirstMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaExamineTopicFirstMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	/**
	 * 考核题目-首部分 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblCeaExamineTopicFirstMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 考核题目-首部分 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblCeaExamineTopicFirst findById(Long id) {
		TblCeaExamineTopicFirst examineTopicFirst = tblCeaExamineTopicFirstMapper.selectByPrimaryKey(id);
		if (examineTopicFirst == null) {
			throw new ServiceException(400, 50001);
		}
		return examineTopicFirst;
	}

	/**
	 * 考核题目-首部分Map 列表 查询
	 * @return
	 */
	@Override
	public Map<Long, TblCeaExamineTopicFirst> getMap() {
		List<TblCeaExamineTopicFirst> examineTopicFirst = tblCeaExamineTopicFirstMapper.selectAll();
		if (CollectionUtil.isEmpty(examineTopicFirst)) {
			return Collections.emptyMap();
		}
		return examineTopicFirst.stream().collect(Collectors.toMap(TblCeaExamineTopicFirst::getId, x -> x));
	}

	/**
	 * 根据id查询 计划 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaExamineTopicFirstMapper.selectCount(TblCeaExamineTopicFirst.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
