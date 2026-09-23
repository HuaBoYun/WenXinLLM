package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglPracticeApplyMySql;
import com.huabo.legal.mysql.mapper.TblFwglPracticeApplyMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglPracticeApplyMySqlService;
import com.huabo.legal.vo.param.TblFwglParam;
import com.huabo.legal.vo.param.TblFwglPracticeApplyQueryParam;
import com.huabo.legal.vo.result.LegalPersonnelCardEmploymentRateResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglPracticeApplyMySqlServiceImpl implements TblFwglPracticeApplyMySqlService {

	@Resource
	private TblFwglPracticeApplyMySqlMapper tblFwglPracticeApplyMySqlMapper;

	/**
	 * 执业申请列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglPracticeApplyMySql> getList(TblFwglPracticeApplyQueryParam param) {
		Example example = new Example(TblFwglPracticeApplyMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getPracticeApplyName())) {
			criteria.andLike("practiceApplyName", "%" + param.getPracticeApplyName() + "%");
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
		if (StringUtils.isNotBlank(param.getStaffId())) {
			criteria.andEqualTo("staffId", param.getStaffId());
		}
		example.setOrderByClause(" PRACTICEAPPLYID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglPracticeApplyMySqlMapper.selectByExample(example));
	}

	/**
	 * 执业申请 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglPracticeApplyMySql saveOrUpdate(TblFwglPracticeApplyMySql param) {
		Date now = new Date();
		if (param.getPracticeApplyId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglPracticeApplyMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getPracticeApplyId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglPracticeApplyMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getPracticeApplyId());
	}

	/**
	 * 执业申请 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglPracticeApplyMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 执业申请详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglPracticeApplyMySql findById(Integer id) {
		TblFwglPracticeApplyMySql practiceApply = tblFwglPracticeApplyMySqlMapper.selectByPrimaryKey(id);
		if (practiceApply == null) {
			throw new ServiceException(400, 50001);
		}
		return practiceApply;
	}

	/**
	 * 法务人员持证上岗率
	 * @param param
	 */
	@Override
	public LegalPersonnelCardEmploymentRateResult getLegalPersonnelCardEmploymentRate(TblFwglParam param) {
		TblFwglPracticeApplyMySql practiceApplyMySql = new TblFwglPracticeApplyMySql();
		practiceApplyMySql.setBelongGroup(param.getBelongGroup());
		//法务人员持证上岗率 该集团 人数总数
		int total = tblFwglPracticeApplyMySqlMapper.selectCount(practiceApplyMySql);
		//法务人员持证上岗率 该集团 持证人数
		Integer count = tblFwglPracticeApplyMySqlMapper.findLegalPersonnelCardEmploymentRate(param);
		return new LegalPersonnelCardEmploymentRateResult(count, total - count);
	}

	/**
	 * 公司律师人数占比
	 * @param param
	 * @return
	 */
	@Override
	public LegalPersonnelCardEmploymentRateResult getFirmLegalProportion(TblFwglParam param) {
		TblFwglPracticeApplyMySql practiceApplyMySql = new TblFwglPracticeApplyMySql();
		practiceApplyMySql.setBelongGroup(param.getBelongGroup());
		//公司律师人数占比 该集团 人数总数
		int total = tblFwglPracticeApplyMySqlMapper.selectCount(practiceApplyMySql);
		//公司律师人数占比 该集团 公司律师
		Integer count = tblFwglPracticeApplyMySqlMapper.findFirmLegalProportion(param);
		return new LegalPersonnelCardEmploymentRateResult(count, total - count);
	}

	/**
	 * 根据id查询 执业申请 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglPracticeApplyMySqlMapper.selectCount(TblFwglPracticeApplyMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
