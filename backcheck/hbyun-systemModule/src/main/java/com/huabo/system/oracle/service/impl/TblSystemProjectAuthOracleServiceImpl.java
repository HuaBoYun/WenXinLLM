package com.huabo.system.oracle.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.constant.YesNo;
import com.huabo.system.entity.TblSystemLoginPageOracle;
import com.huabo.system.entity.TblSystemProjectAuthOracle;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.mapper.TblSystemProjectAuthOracleMapper;
import com.huabo.system.oracle.service.TblSystemProjectAuthOracleService;
import com.huabo.system.vo.param.TblSystemProjectAuthParam;
import com.huabo.system.vo.param.TblSystemProjectAuthQueryParam;
import com.sun.xml.messaging.saaj.packaging.mime.util.QEncoderStream;

import tk.mybatis.mapper.entity.Example;

@Service
public class TblSystemProjectAuthOracleServiceImpl implements TblSystemProjectAuthOracleService {

	@Resource
	private TblSystemProjectAuthOracleMapper tblSystemProjectAuthOracleMapper;

	/**
	 * 系统项目授权列表 查询
	 * @return
	 */
	@Override
	public List<TblSystemProjectAuthOracle> getList(TblSystemProjectAuthQueryParam param) {
		QueryWrapper<TblSystemProjectAuthOracle> wrapper = new QueryWrapper<TblSystemProjectAuthOracle>();
		wrapper.eq("projectId", param.getProjectId());
		wrapper.orderByDesc("id");
		return tblSystemProjectAuthOracleMapper.selectList(wrapper);
	}

	/**
	 * 系统项目授权 新增
	 * @param param
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdate(TblSystemProjectAuthParam param) {
		//再次加授权
		Date now = new Date();
		//先删 对应项目下集团的所有授权
		QueryWrapper<TblSystemProjectAuthOracle> wrapper = new QueryWrapper<TblSystemProjectAuthOracle>();
		wrapper.eq("PROJECTID", param.getProjectId());
		tblSystemProjectAuthOracleMapper.delete(wrapper);
		param.getList().forEach(item -> {
			item.setState(YesNo.YES);
			item.setCreatedTime(now);
			item.setUpdatedTime(now);
			item.setId(RandomUtil.uuBigDecimalId());
			tblSystemProjectAuthOracleMapper.insert(item);
		});
	}

	/**
	 * 系统项目授权 详情 查询
	 * @param id
	 * @return
	 */
	@Override
	public TblSystemProjectAuthOracle findById(BigDecimal id) {
		TblSystemProjectAuthOracle systemProjectAuth = tblSystemProjectAuthOracleMapper.selectById(id);
		if (systemProjectAuth == null) {
			throw new ServiceException(400, 50001);
		}
		return systemProjectAuth;
	}

	/**
	 * 系统项目授权 刪除
	 * @param id
	 */
	@Override
	public void delete(BigDecimal id) {
		tblSystemProjectAuthOracleMapper.deleteById(id);
	}

	/**
	 * 删除系统项目相关授权
	 * @param projectId
	 */
	@Override
	public void deleteProjectAuth(BigDecimal projectId) {
		QueryWrapper<TblSystemProjectAuthOracle> wapper = new QueryWrapper<TblSystemProjectAuthOracle>();
		wapper.eq("PROJECTID", projectId);
		tblSystemProjectAuthOracleMapper.delete(wapper);
	}

	/**
	 * 根据id查询 系统登录页 是存在
	 * @param id
	 * @return
	 */
	private Boolean idById(Integer id) {
		QueryWrapper<TblSystemProjectAuthOracle> wrapper = new QueryWrapper<TblSystemProjectAuthOracle>();
		wrapper.eq("ID", id);
		int count = tblSystemProjectAuthOracleMapper.selectCount(wrapper).intValue();
		if (count == 0) {
			return true;
		}
		return false;
	}
}
