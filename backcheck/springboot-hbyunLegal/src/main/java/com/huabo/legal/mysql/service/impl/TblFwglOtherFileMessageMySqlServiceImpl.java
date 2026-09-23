package com.huabo.legal.mysql.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.huabo.legal.constant.YesNo;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglOtherFileMessageMySql;
import com.huabo.legal.mysql.mapper.TblFwglOtherFileMessageMySqlMapper;
import com.huabo.legal.mysql.service.TblFwglOtherFileMessageMySqlService;
import com.huabo.legal.vo.param.TblFwglOtherFileMessageQueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TblFwglOtherFileMessageMySqlServiceImpl implements TblFwglOtherFileMessageMySqlService {

	@Resource
	private TblFwglOtherFileMessageMySqlMapper tblFwglOtherFileMessageMySqlMapper;

	/**
	 * 其他文件报送列表 查询
	 * @param param
	 * @return
	 */
	@Override
	public PageInfo<TblFwglOtherFileMessageMySql> getList(TblFwglOtherFileMessageQueryParam param) {
		Example example = new Example(TblFwglOtherFileMessageMySql.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(param.getFileName())) {
			criteria.andLike("fileName", "%" + param.getFileName() + "%");
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
		example.setOrderByClause(" OTHERFILEMESSAGEID desc ");
		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
				.doSelectPageInfo(() -> tblFwglOtherFileMessageMySqlMapper.selectByExample(example));
	}

	/**
	 * 其他文件报送 新增/更新
	 * @param param
	 * @return
	 */
	@Override
	public TblFwglOtherFileMessageMySql saveOrUpdate(TblFwglOtherFileMessageMySql param) {
		Date now = new Date();
		if (param.getOtherFileMessageId() == null) {
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			tblFwglOtherFileMessageMySqlMapper.insertSelective(param);
		} else {
			if (idById(param.getOtherFileMessageId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblFwglOtherFileMessageMySqlMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getOtherFileMessageId());
	}

	/**
	 * 其他文件报送 删除
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		tblFwglOtherFileMessageMySqlMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 其他文件报送详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblFwglOtherFileMessageMySql findById(Integer id) {
		TblFwglOtherFileMessageMySql otherFileMessage = tblFwglOtherFileMessageMySqlMapper.selectByPrimaryKey(id);
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
	private Boolean idById(Integer id) {
		int count = tblFwglOtherFileMessageMySqlMapper.selectCount(TblFwglOtherFileMessageMySql.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
}
