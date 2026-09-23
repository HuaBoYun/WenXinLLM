package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglFirmLawyerOracle;
import com.huabo.legal.oracle.mapper.TblFwglFirmLawyerOracleMapper;
import com.huabo.legal.oracle.service.TblFwglFirmLawyerOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglFirmLawyerQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TblFwglFirmLawyerOracleServiceImpl implements TblFwglFirmLawyerOracleService {

	@Autowired
	private TblFwglFirmLawyerOracleMapper tblFwglFirmLawyerOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 查询公司律师列表
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglFirmLawyerOracle> getList(TblFwglFirmLawyerQueryParam param) {

		Example example = new Example(TblFwglFirmLawyerOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getLawyerName())) {
			criteria.andLike("lawyerName", "%" + param.getLawyerName() + "%");
		}
		if (StringUtils.isNotBlank(param.getCategory())) {
			criteria.andEqualTo("category", param.getCategory());
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
		example.setOrderByClause(" LAWYERID desc");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglFirmLawyerOracleMapper.selectByExample(example));
	}

	/**
	 * 公司律师 新增/更新
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Override
	public TblFwglFirmLawyerOracle saveOrUpdate(TblFwglFirmLawyerOracle param) {
		Date now = new Date();
		if (param.getLawyerId() == null) {
			param.setLawyerId(RandomUtil.uuLongId());
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglFirmLawyerOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getLawyerId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglFirmLawyerOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getLawyerId());
	}

	/**
	 * 公司律师 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglFirmLawyerOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 查询公司律师
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglFirmLawyerOracle findById(Long id) {
		TblFwglFirmLawyerOracle firmLawyer = tblFwglFirmLawyerOracleMapper.selectByPrimaryKey(id);
		if (firmLawyer == null) {
			throw new ServiceException(400, 50001);
		}
		return firmLawyer;
	}

	/**
	 * 根据id查询公司律师是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglFirmLawyerOracleMapper.selectCount(TblFwglFirmLawyerOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}


}
