package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectNotice;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaProjectNoticeMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaProjectNoticeService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectNoticeQueryParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
public class TblCeaProjectNoticeServiceImpl implements TblCeaProjectNoticeService {

	@Resource
	private TblCeaProjectNoticeMapper tblCeaProjectNoticeMapper;

	/**
	 * 项目评优-通知 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblCeaProjectNotice> getList(TblCeaProjectNoticeQueryParam param) {
		Example example = new Example(TblCeaProjectNotice.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(param.getNoticeTitle())) {
			criteria.andLike("noticeTitle", "%" + param.getNoticeTitle() + "%");
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(param.getDeptIds())) {
			criteria.andCondition(
					" ( creator = " + param.getCreator() + " or creator in (SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (" + param
							.getDeptIds() + ")))");
		} else {
			criteria.andEqualTo("creator", param.getCreator());
		}
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaProjectNoticeMapper.selectByExample(example));
	}

	/**
	 * 项目评优-通知 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblCeaProjectNotice saveOrUpdate(TblCeaProjectNotice param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaProjectNoticeMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaProjectNoticeMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	/**
	 * 项目评优-通知 删除
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		tblCeaProjectNoticeMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 项目评优-通知 详情查询
	 * @param id
	 * @return
	 */
	@Override
	public TblCeaProjectNotice findById(Long id) {
		TblCeaProjectNotice model = tblCeaProjectNoticeMapper.selectByPrimaryKey(id);
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
		int count = tblCeaProjectNoticeMapper.selectCount(TblCeaProjectNotice.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
