package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglSpecialLawServiceExamineOracle;
import com.huabo.legal.oracle.mapper.TblFwglSpecialLawServiceExamineOracleMapper;
import com.huabo.legal.oracle.service.TblFwglSpecialLawServiceExamineOracleService;
import com.huabo.legal.vo.param.TblFwglSpecialLawServiceExamineQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglSpecialLawServiceExamineOracleServiceImpl implements TblFwglSpecialLawServiceExamineOracleService {

	@Resource
	private TblFwglSpecialLawServiceExamineOracleMapper tblFwglSpecialLawServiceExamineOracleMapper;

	/**
	 * 根据 专项法律服务-考核列表 查询
	 * @param examineId
	 * @return
	 */
	@Override
	public List<TblFwglSpecialLawServiceExamineOracle> getList(String examineId) {
		Example example = new Example(TblFwglSpecialLawServiceExamineOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(examineId)) {
			criteria.andIn("examineId", Arrays.asList(examineId.split(",")));
		}
		example.setOrderByClause(" EXAMINEID desc ");
		return tblFwglSpecialLawServiceExamineOracleMapper.selectByExample(example);
	}

	/**
	 * 专项法律服务-考核 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglSpecialLawServiceExamineOracle saveOrUpdate(TblFwglSpecialLawServiceExamineOracle param) {
		Date now = new Date();
		if (param.getExamineId() == null) {
			param.setExamineId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglSpecialLawServiceExamineOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getExamineId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglSpecialLawServiceExamineOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getExamineId());
	}

	/**
	 * 专项法律服务-考核详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglSpecialLawServiceExamineOracle findById(Long id) {
		TblFwglSpecialLawServiceExamineOracle specialLawServiceExamine = tblFwglSpecialLawServiceExamineOracleMapper.selectByPrimaryKey(id);
		if (specialLawServiceExamine == null) {
			throw new ServiceException(400, 50001);
		}
		return specialLawServiceExamine;
	}

	/**
	 * 专项法律服务-考核 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglSpecialLawServiceExamineOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 考核台账列表
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglSpecialLawServiceExamineOracle> getList(TblFwglSpecialLawServiceExamineQueryParam param) {
		Example example = new Example(TblFwglSpecialLawServiceExamineOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getIsCollaboration())) {
			criteria.andEqualTo("isCollaboration", param.getIsCollaboration());
		}
		if (StringUtils.isNotBlank(param.getServiceProject())) {
			criteria.andLike("serviceProject", "%" + param.getServiceProject() + "%");
		}
		if (StringUtils.isNotBlank(param.getTeamName())) {
			criteria.andLike("teamName", "%" + param.getTeamName() + "%");
		}
		if (StringUtils.isNotBlank(param.getCreator())) {
			criteria.andEqualTo("creator", param.getCreator());
		}
		if (StringUtils.isNotBlank(param.getBelongGroup())) {
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		if (StringUtils.isNotBlank(param.getWorkUnit())) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		example.setOrderByClause(" EXAMINEID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglSpecialLawServiceExamineOracleMapper.selectByExample(example));
	}

	/**
	 * 根据id查询 专项法律服务-考核 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglSpecialLawServiceExamineOracleMapper.selectCount(TblFwglSpecialLawServiceExamineOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
