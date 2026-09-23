package com.huabo.legal.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglOtherFileMessageOracle;
import com.huabo.legal.oracle.mapper.TblFwglOtherFileMessageOracleMapper;
import com.huabo.legal.oracle.service.TblFwglOtherFileMessageOracleService;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.vo.param.TblFwglOtherFileMessageQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblFwglOtherFileMessageOracleServiceImpl implements TblFwglOtherFileMessageOracleService {

	@Resource
	private TblFwglOtherFileMessageOracleMapper tblFwglOtherFileMessageOracleMapper;
	@Resource
	private TblStaffOracleService tblStaffOracleService;

	/**
	 * 其他文件报送列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglOtherFileMessageOracle> getList(TblFwglOtherFileMessageQueryParam param) {
		Example example = new Example(TblFwglOtherFileMessageOracle.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getFileName())) {
			criteria.andLike("fileName", "%" + param.getFileName() + "%");
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
		example.setOrderByClause(" OTHERFILEMESSAGEID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglOtherFileMessageOracleMapper.selectByExample(example));
	}

	/**
	 * 其他文件报送 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglOtherFileMessageOracle saveOrUpdate(TblFwglOtherFileMessageOracle param) {
		Date now = new Date();
		if (param.getOtherFileMessageId() == null) {
			param.setOtherFileMessageId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);//新增初始状态为0
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglOtherFileMessageOracleMapper.insertSelective(param);
		} else {
			if (idById(param.getOtherFileMessageId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglOtherFileMessageOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getOtherFileMessageId());
	}

	/**
	 * 其他文件报送 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblFwglOtherFileMessageOracleMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 其他文件报送详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglOtherFileMessageOracle findById(Long id) {
		TblFwglOtherFileMessageOracle otherFileMessage = tblFwglOtherFileMessageOracleMapper.selectByPrimaryKey(id);
		if (otherFileMessage == null) {
			throw new ServiceException(400, 50001);
		}
		return otherFileMessage;
	}

	/**
	 * 根据id查询 其他文件报送 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblFwglOtherFileMessageOracleMapper.selectCount(TblFwglOtherFileMessageOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
