package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaQualityAssessmentExt;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaQualityAssessmentExtMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaQualityAssessmentExtService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaQualityAssessmentExtQueryParam;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TblCeaQualityAssessmentExtServiceImpl implements TblCeaQualityAssessmentExtService {

	@Resource
	private TblCeaQualityAssessmentExtMapper tblCeaQualityAssessmentExtMapper;

	@Override
	public List<TblCeaQualityAssessmentExt> getList(TblCeaQualityAssessmentExtQueryParam param) {
		Example example = new Example(TblCeaQualityAssessmentExt.class);
		Example.Criteria criteria = example.createCriteria();
		if (Objects.nonNull(param.getQualityAssessmentId())) {
			criteria.andEqualTo("qualityAssessmentId", param.getQualityAssessmentId());
		}
		example.setOrderByClause(" ID desc ");
		return tblCeaQualityAssessmentExtMapper.selectByExample(example);
	}

	@Override
	public TblCeaQualityAssessmentExt saveOrUpdate(TblCeaQualityAssessmentExt param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaQualityAssessmentExtMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaQualityAssessmentExtMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaQualityAssessmentExtMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaQualityAssessmentExt findById(Long id) {
		TblCeaQualityAssessmentExt model = tblCeaQualityAssessmentExtMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaQualityAssessmentExtMapper.selectCount(TblCeaQualityAssessmentExt.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
