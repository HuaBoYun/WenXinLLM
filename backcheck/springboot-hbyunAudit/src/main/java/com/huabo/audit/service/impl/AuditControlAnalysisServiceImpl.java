package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblAuditModelDataSourceOracle;
import com.huabo.audit.oracle.entity.TblNbsjAuditStepEntity;
import com.huabo.audit.oracle.mapper.TblAuditModelDataSourceOracleMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAuditStepMapper;
import com.huabo.audit.service.AuditControlAnalysisService;

@Service
public class AuditControlAnalysisServiceImpl implements AuditControlAnalysisService {
	
	@Resource
	private TblNbsjAuditStepMapper tblNbsjAuditStepMapper;
	
	@Resource
	private TblAuditModelDataSourceOracleMapper tblAuditModelDataSourceOracleMapper;

	@Override
	public JsonBean sumIndicatorAnalysisSummary(TblStaffUtil staff, String limitStr) throws Exception {
		BigDecimal rednum = BigDecimal.ZERO;
		BigDecimal yelnum = BigDecimal.ZERO;
		BigDecimal grenum = BigDecimal.ZERO;
		
		
		//1.获取审计模块下所有启用的指标
		TblNbsjAuditStepEntity step = new TblNbsjAuditStepEntity();
		step.setMpdeltype("ZNSJ");
		step.setQystatus(0);
		List<TblNbsjAuditStepEntity> stepList = this.tblNbsjAuditStepMapper.selectListBySummary(step,limitStr);
		
		if(stepList == null || stepList.size() == 0) {
			return ResponseFormat.retParam(0, "没有指标数据", null);
		}
		
		//2.查询所有审计指标引用数据源信息，并根据id生成map
		List<TblAuditModelDataSourceOracle> dataList = tblAuditModelDataSourceOracleMapper.selectAuditUseInfoList(step);
		if(dataList == null || dataList.size() == 0) {
			return ResponseFormat.retParam(0, "没有数据源信息", null);
		}
		Map<BigDecimal, TblAuditModelDataSourceOracle> dsMap = dataList.stream().collect(Collectors.toMap(TblAuditModelDataSourceOracle::getId, ds -> ds));
		
		TblAuditModelDataSourceOracle dsu = null;
		Connection connGRC = null;
		Statement stmtGRC = null;
		int columncount = 0;
		ResultSetMetaData meta = null;
		ResultSet rsOracle = null;
		//3.循环启用的指标进行查询
		for (TblNbsjAuditStepEntity s : stepList) {
			//3.1查询整个实体获取sql等信息
			s = this.tblNbsjAuditStepMapper.selectByPrimaryKey(s.getStepid());
			
			//3.2获取数据源
			dsu = dsMap.get(s.getBookid());
			if(dsu == null || StringUtils.isBlank(s.getSqlstr())) {
				continue;
			}
			
			//3.3获取JDBC
			connGRC = DriverManager.getConnection(dsu.getDataBaseConnectionAddress(), dsu.getDataBaseUsers(), dsu.getDataBasePassWord());
		    stmtGRC = connGRC.createStatement();
		    rsOracle = stmtGRC.executeQuery(s.getSqlstr());
		    meta = rsOracle.getMetaData();
			columncount = meta.getColumnCount();
		    while (rsOracle.next()) {
				for (int i = 1; i <= columncount; i++) {
					if("红".equals(rsOracle.getObject(i))) {
						rednum = rednum.add(BigDecimal.ONE);
						break;
					}else if("黄".equals(rsOracle.getObject(i))) {
						yelnum = yelnum.add(BigDecimal.ONE);
						break;
					}else if("绿".equals(rsOracle.getObject(i))) {
						grenum = grenum.add(BigDecimal.ONE);
						break;
					}
				}
			}
		    rsOracle.close();
		}
		
		BigDecimal total = rednum.add(yelnum).add(grenum);
		
		Map<String, BigDecimal> resultMap = new HashMap<String, BigDecimal>(0);
		resultMap.put("total", total);
		resultMap.put("redNum", rednum);
		resultMap.put("yellowNum", yelnum);
		resultMap.put("greenNum", grenum);
        return ResponseFormat.retParam(1, 200, resultMap);
	}


	
}
