package com.huabo.central.enterprises.audit.oracle.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.central.enterprises.audit.constant.YesNo;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaProjectNoticeExt;
import com.huabo.central.enterprises.audit.oracle.mapper.TblCeaProjectNoticeExtMapper;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaProjectNoticeExtService;
import com.huabo.central.enterprises.audit.vo.param.TblCeaProjectNoticeExtQueryParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class TblCeaProjectNoticeExtServiceImpl implements TblCeaProjectNoticeExtService {

	@Resource
	private TblCeaProjectNoticeExtMapper tblCeaProjectNoticeExtMapper;

	@Override
	public List<TblCeaProjectNoticeExt> getList(Long projectNoticeId) {
		Example example = new Example(TblCeaProjectNoticeExt.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("projectNoticeId", projectNoticeId);
		example.setOrderByClause(" ID desc ");
		return tblCeaProjectNoticeExtMapper.selectByExample(example);
	}

	@Override
	public TblCeaProjectNoticeExt saveOrUpdate(TblCeaProjectNoticeExt param) {
		Date now = new Date();
		if (param.getId() == null) {
			param.setId(RandomUtil.uuLongId());
			param.setState(YesNo.NO);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblCeaProjectNoticeExtMapper.insertSelective(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreator(null);
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblCeaProjectNoticeExtMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Long id) {
		tblCeaProjectNoticeExtMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblCeaProjectNoticeExt findById(Long id) {
		TblCeaProjectNoticeExt model = tblCeaProjectNoticeExtMapper.selectByPrimaryKey(id);
		if (model == null) {
			throw new ServiceException(400, 50001);
		}
		return model;
	}

	/**
	 * 项目评优-通知-下发-批量新增
	 * @param projectNoticeId
	 * @param param
	 */
	@Override
	public void updatesTblCeaProjectNoticeExt(Long projectNoticeId, List<TblCeaProjectNoticeExt> param) {
		if (CollectionUtil.isEmpty(param)) {
			return;
		}
		param.forEach(item -> {
			TblCeaProjectNoticeExt tblCeaProjectNoticeExt = new TblCeaProjectNoticeExt();
			tblCeaProjectNoticeExt.setProjectNoticeId(projectNoticeId);
			tblCeaProjectNoticeExt.setDistributeId(item.getDistributeId());
			int count1 = tblCeaProjectNoticeExtMapper.selectCount(tblCeaProjectNoticeExt);
			if (count1 > 0) {
				return;
			}
			saveOrUpdate(tblCeaProjectNoticeExt);
		});

	}

	/**
	 * 项目评优-通知-首页展示 列表查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblCeaProjectNoticeExt> getTblCeaProjectNoticeExtHomeList(TblCeaProjectNoticeExtQueryParam param) {
		Example example = new Example(TblCeaProjectNoticeExt.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("distributeId", param.getCreator());
		criteria.andEqualTo("state", 0);
		example.setOrderByClause(" ID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblCeaProjectNoticeExtMapper.selectByExample(example));
	}

	/**
	 * 项目评优-通知-首页展示-同意
	 * @param id
	 */
	@Override
	public void submitTblCeaProjectNoticeExtHomeList(Long id) {
		TblCeaProjectNoticeExt tblCeaProjectNoticeExtUpdate = new TblCeaProjectNoticeExt();
		tblCeaProjectNoticeExtUpdate.setId(id);
		tblCeaProjectNoticeExtUpdate.setState(1);
		saveOrUpdate(tblCeaProjectNoticeExtUpdate);
	}

	/**
	 * 根据id查询 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Long id) {
		int count = tblCeaProjectNoticeExtMapper.selectCount(TblCeaProjectNoticeExt.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
