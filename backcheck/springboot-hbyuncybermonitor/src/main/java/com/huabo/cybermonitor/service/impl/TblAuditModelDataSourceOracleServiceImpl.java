package com.huabo.cybermonitor.service.impl;

import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.cybermonitor.entity.MonitorRule;
import com.huabo.cybermonitor.entity.StepResult;
import com.huabo.cybermonitor.entity.TblAuditModelDataSourceOracle;
import com.huabo.cybermonitor.exception.ServiceException;
import com.huabo.cybermonitor.mapper.TblAuditModelDataSourceOracleMapper;
import com.huabo.cybermonitor.service.TblAuditModelDataSourceOracleService;
import com.huabo.cybermonitor.util.JDBCProperties;
import com.huabo.cybermonitor.util.YesNo;
import com.huabo.cybermonitor.vo.TblAuditModelDataSourceQueryParam;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TblAuditModelDataSourceOracleServiceImpl implements TblAuditModelDataSourceOracleService {

	@Resource
	private TblAuditModelDataSourceOracleMapper tblAuditModelDataSourceOracleMapper;

	@Resource
	private UserProvider userProvider;

	@Override
	public PageInfo<TblAuditModelDataSourceOracle> getList(TblAuditModelDataSourceQueryParam param) {
//		Example example = new Example(TblAuditModelDataSourceOracle.class);
//		Example.Criteria criteria = example.createCriteria();
//		if (param.getCreateType() != null) {
//			criteria.andEqualTo("createType", param.getCreateType());
//		}
//		if (param.getBelongGroup() != null) {
//			criteria.andEqualTo("belongGroup", param.getBelongGroup());
//		}
//		example.setOrderByClause(" id desc ");
//		return PageMethod.startPage(param.getPageNumber(), param.getPageSize())
//				.doSelectPageInfo(() -> tblAuditModelDataSourceOracleMapper.selectByExample(example));
		
		PageInfo<TblAuditModelDataSourceOracle> pageInfo = new PageInfo<TblAuditModelDataSourceOracle>();
    	pageInfo.setPageSize(param.getPageSize());
    	pageInfo.setCurrentPage(param.getPageNumber());
		try {
			pageInfo.setTlist(tblAuditModelDataSourceOracleMapper.selectByPageInfo(pageInfo, param));
			pageInfo.setTotalRecord(tblAuditModelDataSourceOracleMapper.selectByPageInfocount(param));
			pageInfo.getTotalPage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
    	
    	return pageInfo;
	}

	@Override
	public TblAuditModelDataSourceOracle saveOrUpdate(TblAuditModelDataSourceOracle param) {
		Date now = new Date();
		//excel类型手动给与默认值
//		if (param.getCreateType() == 2) {
//			param.setDataBaseType(DateBaseConfig.DATABASETYPE);
//			param.setDataBaseConnectionAddress(dataBaseConnectionAddress);
//		}
		if (param.getId() == null) {
			int count = tblAuditModelDataSourceOracleMapper.selectCount(TblAuditModelDataSourceOracle.ofDataBaseUsers(param.getDataBaseUsers()));
			if (count > 0) {
				throw new ServiceException(400, "数据库用户已存在");
			}
			param.setState(YesNo.YES);
			param.setCreatedTime(now);
			param.setUpdatedTime(now);
			//tblAuditModelDataSourceOracleMapper.insertSelective(param);
			tblAuditModelDataSourceOracleMapper.insertEntityparem(param);
		} else {
			if (idById(param.getId())) {
				throw new ServiceException(400, 50001);
			}
			param.setCreatedTime(null);
			param.setUpdatedTime(now);
			tblAuditModelDataSourceOracleMapper.updateByPrimaryKeySelective(param);
		}
		return findById(param.getId());
	}

	@Override
	public void delete(Integer id) {
		tblAuditModelDataSourceOracleMapper.deleteByPrimaryKey(id);
	}

	@Override
	public TblAuditModelDataSourceOracle findById(Integer id) {
		TblAuditModelDataSourceOracle auditModelDataSource = tblAuditModelDataSourceOracleMapper.selectByPrimaryKey(id);
		if (auditModelDataSource == null) {
			throw new ServiceException(400, 50001);
		}
		return auditModelDataSource;
	}

	
	private Boolean idById(Integer id) {
		int count = tblAuditModelDataSourceOracleMapper.selectCount(TblAuditModelDataSourceOracle.ofId(id));
		if (count == 0) {
			return true;
		}
		return false;
	}
	
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public JsonBean zxsql(String token,  MonitorRule rule) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(rule!=null  ) {
			Long time = System.currentTimeMillis();
			StepResult result=new StepResult();
			result.setMemo(time+"");
			result.setSavetime(new Date());
			result.setStaffid(loginStaff.getStaffid());
			result.setStepid(rule.getRuleid());
			result.setRealname(loginStaff.getRealname());
			tblAuditModelDataSourceOracleMapper.insertEntity(result);

			//查询数据源
			if (StringUtils.isBlank(rule.getConnectionstrings())) {
				throw new ServiceException("规则信息未获取到数据源ID！");
			}

			TblAuditModelDataSourceOracle dataSource = tblAuditModelDataSourceOracleMapper.selectByPrimaryKey(Integer.valueOf(rule.getConnectionstrings()));
			if (Objects.isNull(dataSource)) {
				throw new ServiceException("未获取到数据源信息！");
			}
			List<String> params = JDBCProperties.getTbable(rule.getRulesql(),dataSource);
			if(!JDBCProperties.isNotExistsTable("ZNJK_GZ_"+rule.getRuleid(),rule.getDataname(),dataSource)) {
				JDBCProperties.executeSql("ZNJK_GZ_"+rule.getRuleid(),dataSource, params);
			}
			List<Map<String, Object>> list = JDBCProperties.getData(rule.getRulesql(),dataSource);
			JDBCProperties.insertData("ZNJK_GZ_"+rule.getRuleid(), list, dataSource, result.getResultid()+"");
			
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data",rule);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	
	
	@Override
	public JsonBean getxjjgList(String token, BigDecimal stepId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	List<StepResult> list = tblAuditModelDataSourceOracleMapper.sjmxReulst(stepId);
    	resultMap.put("data", list);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	
	@Override
	public JsonBean getDatelistt(String token, BigDecimal resultid,MonitorRule rule,Integer pageNumber,Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		//校验resultId 并且使用result信息中的ruleId
		StepResult result = tblAuditModelDataSourceOracleMapper.onesjmxReulst(resultid);
		if (Objects.isNull(result)) {
			throw new ServiceException("执行过程不存在！");
		}
		if (Objects.isNull(result.getStepid())) {
			throw new ServiceException("执行信息未获取到规则ID！");
		}

		TblAuditModelDataSourceOracle dataSource = tblAuditModelDataSourceOracleMapper.selectByPrimaryKey(Integer.valueOf(rule.getConnectionstrings()));
		if (Objects.isNull(dataSource)) {
			throw new ServiceException("未获取到数据源信息！");
		}


		String tablename="ZNJK_GZ_"+rule.getRuleid();
		String sql="select * from "+tablename+" where EXECTIME='"+resultid+"'";
		PageInfo<Map<String,Object>> pageInfo = new PageInfo<Map<String,Object>>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	return  JDBCProperties.GetGather(dataSource, sql, pageInfo);
	}
}
