package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglLegalAdviserMySql;
import com.huabo.legal.mysql.mapper.TblFwglLegalAdviserMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglLegalAdviserMySqlService;
import com.huabo.legal.vo.param.TblFwglLegalAdviserQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglLegalAdviserMySqlServiceImpl implements TblFwglLegalAdviserMySqlService {

	@Resource
	private TblFwglLegalAdviserMySqlMapper tblFwglLegalAdviserMySqlMapper;

	/**
	 * 总法律顾问列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglLegalAdviserMySql> getList(TblFwglLegalAdviserQueryParam param) {
		Example example = new Example(TblFwglLegalAdviserMySql.class);
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
			criteria.andEqualTo("belongGroup", param.getBelongGroup());
		}
		if (StringUtils.isNotBlank(param.getWorkUnit())) {
			criteria.andEqualTo("workUnit", param.getWorkUnit());
		}
		example.setOrderByClause(" ADVISERID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglLegalAdviserMySqlMapper.selectByExample(example));
	}

	/**
	 * 总法律顾问 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglLegalAdviserMySql saveOrUpdate(TblFwglLegalAdviserMySql param) {
		Date now = new Date();
		if (param.getAdviserId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglLegalAdviserMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getAdviserId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglLegalAdviserMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getAdviserId());
	}

	/**
	 * 总法律顾问详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglLegalAdviserMySql findById(Integer id) {
		TblFwglLegalAdviserMySql legalAdviser = tblFwglLegalAdviserMySqlMapper.selectByPrimaryKey(id);
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
	public void delete(Integer id) {
		tblFwglLegalAdviserMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 根据id查询 法务机构及负责人 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		int count = tblFwglLegalAdviserMySqlMapper.selectCount(TblFwglLegalAdviserMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
