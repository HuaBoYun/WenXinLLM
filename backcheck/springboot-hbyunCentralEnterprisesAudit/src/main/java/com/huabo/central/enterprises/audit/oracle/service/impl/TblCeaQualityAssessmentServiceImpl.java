package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaQualityAssessment;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaQualityAssessmentMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaQualityAssessmentService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaQualityAssessmentQueryParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblCeaQualityAssessmentServiceImpl implements TblCeaQualityAssessmentService {

	@Resource
	private TblCeaQualityAssessmentMapper tblCeaQualityAssessmentMapper;

	@Override
	public PageInfo<TblCeaQualityAssessment> getList(TblCeaQualityAssessmentQueryParam param) {
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize()).doSelectPageInfo(() -> tblCeaQualityAssessmentMapper.getList(param));
	}

	@Override
	public TblCeaQualityAssessment saveOrUpdate(TblCeaQualityAssessment param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaQualityAssessmentMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaQualityAssessmentMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaQualityAssessmentMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaQualityAssessment findById(Long id) {
		TblCeaQualityAssessment model = tblCeaQualityAssessmentMapper.selectByPrimaryKey(id);
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
		int count = tblCeaQualityAssessmentMapper.selectCount(TblCeaQualityAssessment.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
