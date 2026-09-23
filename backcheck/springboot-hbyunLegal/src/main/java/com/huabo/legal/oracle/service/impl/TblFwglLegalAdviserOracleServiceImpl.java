package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglLegalAdviserOracle;
import com.huabo.legal.oracle.mapper.TblFwglLegalAdviserOracleMapper;
import com.huabo.legal.oracle.service.TblFwglLegalAdviserOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglLegalAdviserQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglLegalAdviserOracleServiceImpl implements TblFwglLegalAdviserOracleService {

	@Resource
	private TblFwglLegalAdviserOracleMapper tblFwglLegalAdviserOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 总法律顾问列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglLegalAdviserOracle> getList(TblFwglLegalAdviserQueryParam param) {
		Example example = new Example(TblFwglLegalAdviserOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getAdviserName())) {
			criteria.andLike("adviserName", "%" + param.getAdviserName() + "%");
		}
		if (StringUtils.isNotEmpty(param.getPosition())) {
			criteria.andLike("position", "%" + param.getPosition() + "%");
		}
		if (StringUtils.isNotBlank(param.getCreator())) {
			criteria.andEqualTo("creator", param.getCreator());
		}
		if (StringUtils.isNotBlank(param.getBelongGroup())) {
			List<Long> tblOrganizationAll = tblStaffOracleService.getTblOrganizationAll(Long.valueOf(param.getBelongGroup()));
			criteria.andIn("belongGroup", tblOrganizationAll);
			//			criteria.andCondition(" ( belongGroup in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID= " + param.getBelongGroup()
			//					+ " and ORGTYPE!=0 AND " + "ORGTYPE<100  connect by prior orgid= FATHERORGID) or belongGroup = " + param.getBelongGroup()+") ");
		}
		if (StringUtils.isNotBlank(param.getWorkUnit())) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		example.setOrderByClause(" ADVISERID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglLegalAdviserOracleMapper.selectByExample(example));
	}

	/**
	 * 总法律顾问 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLegalAdviserOracle saveOrUpdate(TblFwglLegalAdviserOracle param) {
		Date now = new Date();
		if (param.getAdviserId() == null) {
			param.setAdviserId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);//新增为0状态为未审批
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLegalAdviserOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getAdviserId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLegalAdviserOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getAdviserId());
	}

	/**
	 * 总法律顾问详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLegalAdviserOracle findById(Long id) {
		TblFwglLegalAdviserOracle legalAdviser = tblFwglLegalAdviserOracleMapper.selectByPrimaryKey(id);
		if (legalAdviser == null) {
			throw new ServiceException(400, 50001);
		}
		return legalAdviser;
	}

	/**
	 * 总法律顾问 刪除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglLegalAdviserOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 法务机构及负责人 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglLegalAdviserOracleMapper.selectCount(TblFwglLegalAdviserOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
