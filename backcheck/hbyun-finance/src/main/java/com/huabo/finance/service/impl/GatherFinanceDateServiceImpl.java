package com.huabo.finance.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.IpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.database.DataBaseSqlGetStr;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.finance.config.SystemStaticConfig;
import com.huabo.finance.entity.BdFinanceIncrementinfo;
import com.huabo.finance.entity.BdFinancedate;
import com.huabo.finance.entity.BdFinancedateRecord;
import com.huabo.finance.entity.BdFinanceplan;
import com.huabo.finance.entity.BdInitSqlconfig;
import com.huabo.finance.entity.BdPlanSqlconfig;
import com.huabo.finance.entity.TblConfigColumnInfo;
import com.huabo.finance.entity.TblConfigTableInfo;
import com.huabo.finance.entity.TblSysScheduledTask;
import com.huabo.finance.entity.caiji.OrgOrgs;
import com.huabo.finance.mapper.BdFinanceIncrementinfoMapper;
import com.huabo.finance.mapper.BdFinancedateMapper;
import com.huabo.finance.mapper.BdFinancedateRecordMapper;
import com.huabo.finance.mapper.BdFinanceplanMapper;
import com.huabo.finance.mapper.BdInitSqlconfigMapper;
import com.huabo.finance.mapper.BdPlanSqlconfigMapper;
import com.huabo.finance.mapper.GatherFinanceDataMapper;
import com.huabo.finance.mapper.OrgOrgsMapper;
import com.huabo.finance.mapper.TblConfigColumnInfoMapper;
import com.huabo.finance.mapper.TblConfigTableInfoMapper;
import com.huabo.finance.service.GatherFinanceDateService;
import com.huabo.finance.thread.BussinessGatherDataTask;
import com.huabo.finance.thread.GatherDataTask;
import com.huabo.finance.thread.SampleTask;
import com.huabo.finance.thread.ThreadManagementService;
import com.huabo.finance.unit.BaseDao;
import com.huabo.finance.unit.BussinessDataSqlFunc;
import com.huabo.finance.unit.SqlValidatorUtil;
import com.huabo.finance.vr.BdFinanceplanVr;
import com.huabo.finance.vr.BdPlanSqlconfigVr;
import com.huabo.finance.vr.ColumnType;

/**
 * <p>
 * 数据采集执行
 * </p>
 *
 * @author L
 * @since 2025-03-12
 */
@Service
public class GatherFinanceDateServiceImpl implements GatherFinanceDateService {

	@Resource
	private BdPlanSqlconfigMapper bdPlanSqlconfigMapper;
	
	@Resource
	private BdInitSqlconfigMapper bdInitSqlconfigMapper;
	
	@Resource
	private BdFinanceplanMapper bdFinanceplanMapper;
	
	@Resource
	private BdFinancedateMapper bdFinancedateMapper;
	
	@Resource
	private GatherFinanceDataMapper gatherFinanceDataMapper;
	
	@Resource
	private OrgOrgsMapper orgOrgsMapper;
	
	@Resource
	private BdFinancedateRecordMapper bdFinancedateRecordMapper;
	
	@Resource
	private BdFinanceIncrementinfoMapper bdFinanceIncrementinfoMapper;
	
	@Resource(name = "synExecutor")
    private ThreadPoolTaskExecutor taskExecutor;
	
    private final Map<String, Future<?>> taskFutures = new ConcurrentHashMap<>();
    
    
    private final ThreadManagementService threadManagementService;
    
    @Resource
    private TblConfigTableInfoMapper tblConfigTableInfoMapper;
    
    @Resource
    private TblConfigColumnInfoMapper tblConfigColumnInfoMapper;
    
    @Resource
	private UserProvider userProvider;
    
    
    public GatherFinanceDateServiceImpl(ThreadManagementService threadManagementService) {
        this.threadManagementService = threadManagementService;
    }
    
	
	@Override
	public JsonBean exeUnique(String fid , HttpServletRequest request, TblStaffUtil staff, String finitsqlid, String finitPlanid) throws Exception {
		String ip = IpUtil.getIpAddr(request);
		GatherDataTask task = new GatherDataTask(fid,staff,ip,this,finitsqlid,finitPlanid);
        Future<?> future = taskExecutor.submit(task);
        String taskId = StringUtils.isNotBlank(fid)?fid+":"+finitPlanid:finitsqlid+":"+finitPlanid;
        taskFutures.put(taskId, future);
        return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public void gatherFinanceDataExecuteSql(String fid, String ip, TblStaffUtil staff, String finitsqlid, String finitPlanid) throws Exception {
		
		JsonBean result = null;
		BdFinancedateRecord fr = new BdFinancedateRecord();
		String frId = null;
		String sqlid = null;
		String fname = null;
		String increment = null;
		String sql = null;
		String resultTip="";
		try{
			
			fr.setRecordid(RandomUtil.uuStringId());
			fr.setRecordip(ip);
			fr.setCreatetime(new Date());
			fr.setCreator(staff.getStaffid());
			fr.setCreatname(staff.getRealname());
			fr.setLinkdept(staff.getLinkDetp().getOrgid());
			fr.setLinkorg(staff.getCurrentOrg().getOrgid());
			fr.setIscompleted(1);
			fr.setRecordtype(1);
			frId = fr.getRecordid();
			
			BdInitSqlconfig initConfig = this.bdInitSqlconfigMapper.selectById(finitsqlid);
			
			BdFinanceplan plan = this.bdFinanceplanMapper.selectById(finitPlanid);
			
			resultTip = "1.获取财务数据采集配置信息："+initConfig.getFname()+"\n";
			
			sqlid = initConfig.getFid()+":"+finitPlanid;
			fname = initConfig.getFname();
			increment = initConfig.getIncrementcol();
			
			if(StringUtils.isNotBlank(fid)) {
				BdPlanSqlconfig config = this.bdPlanSqlconfigMapper.selectById(fid);
				sqlid = config.getFid()+":"+finitPlanid;
				fname = config.getFname();
				increment = config.getIncrementcol();
				initConfig.setFinSpecificityCol(config.getSpecificityCol());
				sql = StringUtils.isNotBlank(config.getFsql())?config.getFsql():initConfig.getFinitsql();
			}else {
				sql = initConfig.getFinitsql();
			}
			
			
			fr.setStartdate(new Date());
			fr.setSqlid(sqlid);
			fr.setPlanid(finitPlanid);
			fr.setRecordname(fname);
			this.bdFinancedateRecordMapper.insert(fr);
			//Thread.sleep(6000000);
			BdFinancedate dataConfig = this.bdFinancedateMapper.selectByPlanId(plan.getFid());
			resultTip+="2.获取数据源配置信息。\n";
			
			if("ORG_ORGS".equals(initConfig.getFtable())) {
				//采集公司信息
				result = this.gatherCompanyInfo(sql,initConfig,plan,dataConfig);
			}else {
				
				if(plan.getFinancetype() == 2) {
					//增量采集
					result = this.gatherFinanceDataInfoBatch(sql,initConfig,plan,dataConfig,fname,increment);
				}else {
					//全量采集
					result = this.gatherFinanceDataInfo(sql,initConfig,plan,dataConfig,fname);
				}
			}
			resultTip = "采集完成！\n"+resultTip+result.getData().toString();
		}catch(Exception e){
			resultTip = "采集失败！\n"+resultTip+result.getData().toString()+e.getMessage();
		}finally {
			fr = new BdFinancedateRecord();
			fr.setRecordid(frId);
			fr.setIscompleted(2);
			fr.setEnddate(new Date());
			fr.setRecordmemo(resultTip);
			fr.setIsresult(result.getCode());
			this.bdFinancedateRecordMapper.updateById(fr);
		}
	}


	@Override
	public JsonBean stopGather(String fid, String finitsqlid, String finitPlanid, String recordSqlid) throws Exception {
		String taskId = StringUtils.isNotBlank(recordSqlid)?recordSqlid:StringUtils.isNotBlank(fid)?fid+":"+finitPlanid:StringUtils.isNotBlank(recordSqlid)?recordSqlid:finitsqlid+":"+finitPlanid;
		
		Future<?> future = taskFutures.get(taskId);
        if (future != null) {
            future.cancel(true); // 中断线程
            taskFutures.remove(taskId);
        }
        
        QueryWrapper<BdFinancedateRecord> wrapper = new QueryWrapper<BdFinancedateRecord>();
        wrapper.eq("SQLID", taskId);
        wrapper.eq("ISCOMPLETED", 1);
        
        List<BdFinancedateRecord> brList = this.bdFinancedateRecordMapper.selectList(wrapper);
        for (BdFinancedateRecord br : brList) {
			br.setEnddate(new Date());
			br.setIscompleted(2);
			br.setRecordmemo("用户手动取消执行");
			br.setIsresult(0);
			this.bdFinancedateRecordMapper.updateById(br);
		}
        
        return ResponseFormat.retParam(1, 200, null);
	}
	
	private JsonBean gatherFinanceDataInfoBatch(String sql, BdInitSqlconfig initConfig, BdFinanceplan plan,
			BdFinancedate dataConfig, String fname, String increment) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ColumnType ct = null;
		List<ColumnType> ctList = new ArrayList<ColumnType>(0);
		Map<String,List<ColumnType>> ectMap = new HashMap<String, List<ColumnType>>(0);
		String result = "";
		try {
			if(StringUtils.isBlank(increment)) {
				//没有增量配置  无法进行增量采集，直接调用全量采集
				result = "无增量配置信息，使用全量采集方式。\n";
				return this.gatherFinanceDataInfo(sql, initConfig, plan, dataConfig, fname);
			}
			
			BdFinanceIncrementinfo cii = new BdFinanceIncrementinfo();
			cii.setCreatetime(new Date());
			cii.setIncrementid(RandomUtil.uuStringId());
			cii.setInitsqlid(initConfig.getFid());
			cii.setPlanid(plan.getFid());
			cii.setLastindex(DateUtil.getIntDateStr(new Date(), DateUtil.DATE_FULL_STR));
			
			String upSql = sql.toUpperCase();
			
			String whereClause = DataBaseSqlGetStr.extractWhereClause(sql);
			
			//查找上次采集的最后一次下标
			String lastIndex = this.bdFinanceIncrementinfoMapper.selectLastIndex(initConfig.getFid(),plan.getFid());
			result = "3.获取"+fname+"最后一次采集增量采集信息！\n";
			
			if(StringUtils.isBlank(lastIndex)) {
				//没有上次采集记录  默认为第一次采集  使用全量采集采集所有数据
				return this.gatherFinanceDataInfo(sql,initConfig,plan,dataConfig,fname);
			}else {
				//有采集记录，从采集记录开始采集；
				String[] lastIndexs = increment.split(",");
				
				String increSql = "( ";
				
				for (String ls : lastIndexs) {
					increSql += ls+" >= "+DataBaseSqlConfig.getDateHmsStrFormat(lastIndex, dataConfig.getFinancedbtype())+" OR ";
				}
				
				increSql = increSql.substring(0,increSql.length() - 3) + ")";
				
				if(StringUtils.isNotBlank(whereClause)) {
					//拼接了where条件
					Integer whereIndex = sql.indexOf(whereClause);
					String selectSql = sql.substring(0, whereIndex);
					String whereSql = sql.substring(whereIndex);
					
					sql = selectSql + increSql + " AND " + whereSql;
				}else {
					//sql语句没有where条件做额外处理
					sql = this.dealNoWhereSqlConcatWhere(sql,upSql,increSql);
				}
			}
			result = "4.生成"+fname+"增量采集条件"+sql+"！\n";
			//1.BASEDAO获取需要同步的数据信息
			con = BaseDao.getConnection(dataConfig.getFinancedbtype(), dataConfig.getFinanceconn(), dataConfig.getFinanceport(), dataConfig.getFinanceuser(), dataConfig.getFinancepwd(), dataConfig.getFinancedbexpm());
			result = "5.获取"+fname+"数据源配置信息"+dataConfig.getFintext()+"！\n";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			//1.1获取 列的数量
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			StringBuffer pkVal = new StringBuffer();
			
			//1.3 循环获取需要插入的数据，顺序保持与列一致 ,循环得到返回的列名，并作为数据库新增数据的列名，
			String pkv = "";
			while (rs.next()) {
				ctList = new ArrayList<ColumnType>(0);
				for (int i = 0 ; i < columnCount ; i++) {
					ct = new ColumnType();
					ct.setCdata(rs.getObject(i+1));
					ct.setName(rsmd.getColumnName(i+1));
					ct.setCtype(rsmd.getColumnType(i+1));
					if(initConfig.getPrimarycol().equals(ct.getName())) {
						pkVal.append("'").append(ct.getCdata()).append("',");
						pkv = ct.getCdata().toString();
					}
					ctList.add(ct);
				}
				ectMap.put(pkv, ctList);
			}
			
			if(ectMap.size() == 0) {
				return ResponseFormat.retParam(1, 200,result+fname+"采集无数据。\n");
			}
			result = "6."+fname+"共获取"+ectMap.size()+"条数据！\n";
			//处理主键列的值（StringBuffer） ，查找已采集需要修改的数据主键
			pkVal.deleteCharAt(pkVal.length()-1);
			String pkValStr = pkVal.toString();
			List<String> existIdList = this.gatherFinanceDataMapper.selectExistPrimaryKey(initConfig.getPrimarycol(),pkValStr,initConfig.getFtable(),plan.getFid());
			
			//循环过滤出需要新增和修改的数据
			Map<String,List<ColumnType>> modifyCtMap = new HashMap<String,List<ColumnType>>(0);
			Map<String,List<ColumnType>> insertCtMap = new HashMap<String,List<ColumnType>>(0);
			
			for (Map.Entry<String, List<ColumnType>> entry : ectMap.entrySet()) {
				if(existIdList.contains(entry.getKey())) {
					modifyCtMap.put(entry.getKey(),entry.getValue());
				}else {
					insertCtMap.put(entry.getKey(),entry.getValue());
				}
	        }
			
			//生成 update语句
			StringBuffer updateSb = new StringBuffer();
			for (Map.Entry<String, List<ColumnType>> entry : modifyCtMap.entrySet()){
				updateSb = new StringBuffer("UPDATE ").append(initConfig.getFtable()).append(" SET ");
				
				for (ColumnType mdct : entry.getValue()) {
					updateSb.append(mdct.getName()).append(" = ").append(ColumnType.getInsertDataStr(mdct)).append(",");
				}
				updateSb.deleteCharAt(updateSb.length() - 1);
				updateSb.append(" WHERE ").append(initConfig.getPrimarycol()).append(" = '").append(entry.getKey()).append("'");
				this.gatherFinanceDataMapper.executeUpdateSql(updateSb.toString());
			}
			
			
			//生成 Insert 语句
			StringBuffer insertColSb = new StringBuffer();
			StringBuffer insertValSb = new StringBuffer();
			StringBuffer insertSb = new StringBuffer("");
			for (Map.Entry<String, List<ColumnType>> entry : insertCtMap.entrySet()) {
				insertColSb = new StringBuffer("INSERT INTO ").append(initConfig.getFtable()).append("(DATAORIGINFLAG,FPLANID");
				insertValSb = new StringBuffer("VALUES (-2,'").append(plan.getFid()).append("'");
				for (ColumnType cti : entry.getValue()) {
					insertColSb.append(",").append(cti.getName());
					insertValSb.append(",").append(ColumnType.getInsertDataStr(cti));
				}
				insertSb = insertColSb.append(")").append(insertValSb.toString()).append(")");
				this.gatherFinanceDataMapper.executeInsertSql(insertSb.toString());
			}
			result = "7."+fname+"本次采集新增"+insertCtMap.size()+"条数据，修改"+modifyCtMap.size()+"条数据！\n";
			this.bdFinanceIncrementinfoMapper.insert(cii);
			result = "8."+fname+"更新最近一次增量采集信息！\n";
		}finally {
			BaseDao.close(dataConfig.getFinancedbtype(), con, rs, ps);
		}
		return ResponseFormat.retParam(1, 200,result);
	}

	private JsonBean gatherFinanceDataInfo(String sql, BdInitSqlconfig initConfig, BdFinanceplan plan,
			BdFinancedate dataConfig, String fname) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = "";
		ColumnType ct = null;
		List<ColumnType> ctList = new ArrayList<ColumnType>(0);
		List<List<ColumnType>> ectList = new ArrayList<List<ColumnType>>(0);
		List<String> sqlList = new ArrayList<String>(0);
		
		String exeSql = sql;
		
		try {
			
			if(StringUtils.isNotBlank(initConfig.getFinSpecificityCol())) {
				exeSql += " "+initConfig.getFinSpecificityCol();
			}
			
			//1.BASEDAO获取需要同步的数据信息
			con = BaseDao.getConnection(dataConfig.getFinancedbtype(), dataConfig.getFinanceconn(), dataConfig.getFinanceport(), dataConfig.getFinanceuser(), dataConfig.getFinancepwd(), dataConfig.getFinancedbexpm());
			result = "3."+fname+"获取数据源配置信息"+dataConfig.getFintext()+"！\n";
			ps = con.prepareStatement(exeSql);
			rs = ps.executeQuery();
			
			//1.1获取 列的数量
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			//1.3 循环获取需要插入的数据，顺序保持与列一致 ,循环得到返回的列名，并作为数据库新增数据的列名，
			while (rs.next()) {
				ctList = new ArrayList<ColumnType>(0);
				for (int i = 0 ; i < columnCount ; i++) {
					ct = new ColumnType();
					ct.setCdata(rs.getObject(i+1));
					ct.setName(rsmd.getColumnName(i+1));
					ct.setCtype( rsmd.getColumnType(i+1));
					ctList.add(ct);
				}
				ectList.add(ctList);
				
			}
			
			if(ectList.size() == 0) {
				return ResponseFormat.retParam(1, 200,result+fname+"采集无数据");
			}
			result += "4."+fname+"获取获取"+ectList.size()+"条数据！\n";
			String whereClause = DataBaseSqlGetStr.extractWhereClause(sql);
			List<String> electList = DataBaseSqlGetStr.extractSelectItems(sql);
			sql = sql.toUpperCase();
			result += "5.获取select查询列："+String.join(",", electList)+"，和where条件："+whereClause+"\n";
			//获取第一个列信息数据  生成列类型map 用于where判断
			/*List<ColumnType> ctuList = ectList.get(0);
			Map<String,Integer> ctwMap = ctuList.stream().collect(Collectors.toMap(ColumnType::getName, ColumnType::getCtype));*/
			
			
			String[] cols = null;
			
			for (String colStr : electList) {
				if(colStr.contains(" AS ")) {
					cols = colStr.split(" AS ");
				}else {
					cols = colStr.split(" ");
				}
				if(StringUtils.isNotBlank(whereClause)) {
					whereClause = whereClause.replace(cols[0].trim(), cols[1].trim());
				}
			}
			result += "6.获取和where替换后的条件："+whereClause+"\n";
			//有数据 生成 Insert语句 插入
			//先删除之前存在的采集数据
			String delSql = "DELETE FROM "+initConfig.getFtable()+" WHERE FPLANID = '"+plan.getFid()+"' AND DATAORIGINFLAG = -2 ";
			if(StringUtils.isNotBlank(whereClause)) {
				delSql = delSql + " AND "+ whereClause;
			}
			
			this.gatherFinanceDataMapper.executeDeleteSql(delSql);
			result += "7."+fname+"清除之前的采集数据！执行sql："+delSql+"\n";
			StringBuffer insertColSb = new StringBuffer();
			StringBuffer insertValSb = new StringBuffer();
			StringBuffer insertSb = new StringBuffer("");
			result += "8.执行插入操作：\n";
			for (List<ColumnType> list : ectList) {
				insertColSb = new StringBuffer("INSERT INTO ").append(initConfig.getFtable()).append("(DATAORIGINFLAG,FPLANID");
				insertValSb = new StringBuffer("VALUES (-2,'").append(plan.getFid()).append("'");
				for (ColumnType cti : list) {
					insertColSb.append(",").append(cti.getName());
					insertValSb.append(",").append(ColumnType.getInsertDataStr(cti));
				}
				insertSb = insertColSb.append(")").append(insertValSb.toString()).append(")");
				result += "\t "+insertSb.toString() +";\n";
				sqlList.add(insertSb.toString());
			}
			
			for (String s : sqlList) {
				this.gatherFinanceDataMapper.executeInsertSql(s);
			}
			result += "9."+fname+"本次共采集"+sqlList.size()+"条数据！\n";
		}catch(Exception e){
			e.getMessage();
			return ResponseFormat.retParam(0, "采集失败",result);
		}finally {
			BaseDao.close(dataConfig.getFinancedbtype(), con, rs, ps);
		}
		
		return ResponseFormat.retParam(1, 200,result);
	}
	
	
	
	private JsonBean gatherCompanyInfo(String sql, BdInitSqlconfig initConfig, BdFinanceplan plan,
			BdFinancedate dataConfig) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = "";
		Integer updateCount = 0;
		Integer insertCount = 0;
		try {
			List<OrgOrgs> gatherOrgList = new ArrayList<OrgOrgs>(0);
			OrgOrgs gaOrg = null;
			
			if(StringUtils.isNotBlank(initConfig.getFinSpecificityCol())) {
				sql += " "+initConfig.getFinSpecificityCol();
			}
			
			con = BaseDao.getConnection(dataConfig.getFinancedbtype(), dataConfig.getFinanceconn(), dataConfig.getFinanceport(), dataConfig.getFinanceuser(), dataConfig.getFinancepwd(), dataConfig.getFinancedbexpm());
			result = "3.获取数据源连接信息成功！\n";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				gaOrg = new OrgOrgs();
				gaOrg.setCode(rs.getString("CODE"));
				gaOrg.setPkOrg(rs.getString("PK_ORG"));
				gaOrg.setName(rs.getString("NAME"));
				gaOrg.setInnercode(rs.getString("INNERCODE"));
				gaOrg.setShortname(rs.getString("SHORTNAME"));
				gaOrg.setMnecode(rs.getString("MNECODE"));
				gaOrg.setPkFatherorg(rs.getString("PK_FATHERORG"));
				gaOrg.setEnablestate(rs.getInt("ENABLESTATE"));
				gaOrg.setFplanid(plan.getFid());
				gaOrg.setDataoriginflag(-2);
				gatherOrgList.add(gaOrg);
			}
	
			List<String> gaOrgidList = gatherOrgList.stream().map(OrgOrgs::getPkOrg).collect(Collectors.toList());
			result += "4.本次获取共"+gaOrgidList.size()+"条数据。\n";
			List<OrgOrgs> exList = this.orgOrgsMapper.selectBatchIds(gaOrgidList);
			
			Map<String, OrgOrgs> exMap = exList.stream().collect(Collectors.toMap(OrgOrgs::getPkOrg, org -> org));
			
			for (OrgOrgs orgA : gatherOrgList) {
	            if (exMap.containsKey(orgA.getPkOrg())) {
	            	this.orgOrgsMapper.updateById(orgA);
	            	updateCount ++;
	            } else {
	            	this.orgOrgsMapper.insert(orgA);
	            	insertCount ++;
	            }
	        }
			result += "5.本次同步组织表，新增："+insertCount+"条，修改："+updateCount+"条。\n";
		}finally {
			BaseDao.close(dataConfig.getFinancedbtype(), con, rs, ps);
		}
		
		return ResponseFormat.retParam(1, 200, result);
	}
	
	

	private String dealNoWhereSqlConcatWhere(String sql, String upSql, String increSql) throws Exception {
		if(upSql.contains(" ORDER ")) {
			List<String> orderSqlList = DataBaseSqlGetStr.extractOrderByClause(sql);
			Integer orderIndx = upSql.lastIndexOf(orderSqlList.get(0));
			String preSqlUp = upSql.substring(0, orderIndx);
			String preSql = sql.substring(0, orderIndx);
			String forSql = sql.substring(orderIndx);
			Integer oIndex = preSqlUp.lastIndexOf(" ORDER ");
			String noOrderSql = preSql.substring(0,oIndex);
			 
			sql = noOrderSql + " WHERE "+increSql + " ORDER BY " + forSql;
		}else {
			sql = sql+ " WHERE "+increSql;
		}
		return sql;
	}

	@Override
	public JsonBean getSqlGatherInfo(String recordid) throws Exception {
		BdFinancedateRecord record = this.bdFinancedateRecordMapper.selectById(recordid);
		return ResponseFormat.retParam(1, 200, record);
	}

	@Override
	public JsonBean beginFinancePlan(TblStaffUtil staff,String ip, String fid) throws Exception {
		BdFinancedateRecord fr = new BdFinancedateRecord();
		//获取 采集方案  数据源 和 采集sql配置
		BdFinanceplan plan = this.bdFinanceplanMapper.selectById(fid);
		
		if(plan.getFstatus() != 1 ) {
			return ResponseFormat.retParam(0, "方案未启用，无法采集", null);
		}
		
		BdFinancedate dataConfig = this.bdFinancedateMapper.selectByPlanId(fid);
		
		fr = new BdFinancedateRecord();
		fr.setRecordid(RandomUtil.uuStringId());
		fr.setRecordip(ip);
		fr.setCreatetime(new Date());
		fr.setCreator(staff.getStaffid());
		fr.setCreatname(staff.getRealname());
		fr.setLinkdept(staff.getLinkDetp().getOrgid());
		fr.setLinkorg(staff.getCurrentOrg().getOrgid());
		fr.setIscompleted(1);
		fr.setRecordtype(1);
		fr.setStartdate(new Date());
		fr.setPlanid(fid);
		fr.setRecordname(plan.getFname());
		this.bdFinancedateRecordMapper.insert(fr);
		String pid = fr.getRecordid();
		List<BdPlanSqlconfigVr> sqlList = new ArrayList<BdPlanSqlconfigVr>(0);
		
		if(plan.getFinanceRange() == 1 || plan.getFinanceRange() == 3) {
			sqlList = this.bdPlanSqlconfigMapper.selectAllListByPlan(plan.getFid(),plan.getFversionid());
			for (BdPlanSqlconfigVr config : sqlList) {
				fr = new BdFinancedateRecord();
				fr.setRecordid(RandomUtil.uuStringId());
				fr.setRecordip(ip);
				fr.setCreatetime(new Date());
				fr.setCreator(staff.getStaffid());
				fr.setCreatname(staff.getRealname());
				fr.setLinkdept(staff.getLinkDetp().getOrgid());
				fr.setLinkorg(staff.getCurrentOrg().getOrgid());
				fr.setIscompleted(0);
				fr.setRecordtype(1);
				fr.setPrecordId(pid);
				fr.setSqlid(StringUtils.isNotBlank(config.getFid())?config.getFid()+":"+fid:config.getSqlconfigid()+":"+fid);
				fr.setPlanid(fid);
				fr.setRecordname(StringUtils.isNotBlank(config.getFname())?config.getFname():config.getSqlconfigname());
				this.bdFinancedateRecordMapper.insert(fr);
				config.setFr(fr);
			}
		}
		
		List<TblConfigTableInfo> tableList = new ArrayList<TblConfigTableInfo>(0);
		if(plan.getFinanceRange() == 2 || plan.getFinanceRange() == 3) {
			tableList = this.tblConfigTableInfoMapper.selectFinanceDataListByPlanId(fid);
			for (TblConfigTableInfo tbInfo : tableList) {
				if(StringUtils.isBlank(tbInfo.getDataConfig())) {
					continue;
				}
				fr = new BdFinancedateRecord();
				fr.setRecordid(RandomUtil.uuStringId());
				fr.setRecordip(ip);
				fr.setCreatetime(new Date());
				fr.setCreator(staff.getStaffid());
				fr.setCreatname(staff.getRealname());
				fr.setLinkdept(staff.getLinkDetp().getOrgid());
				fr.setLinkorg(staff.getCurrentOrg().getOrgid());
				fr.setIscompleted(0);
				fr.setRecordtype(1);
				fr.setPrecordId(pid);
				fr.setSqlid(tbInfo.getFid());
				fr.setPlanid(fid);
				fr.setRecordname(tbInfo.getFname());
				this.bdFinancedateRecordMapper.insert(fr);
				tbInfo.setFr(fr);
			}
		}
		threadManagementService.initTasks(dataConfig,plan,sqlList,tableList,this);
		threadManagementService.startExecution(dataConfig,plan,sqlList,tableList,this);
        System.out.println("已启动 " + sqlList.size() + " 个任务");
		
		return ResponseFormat.retParam(1, 200, sqlList);
	}


	public void executeBatchFinance(BdFinancedateRecord fr, BdPlanSqlconfigVr config, BdFinancedate dataConfig, BdFinanceplan plan) throws Exception {
		JsonBean result = null;
		String sql = StringUtils.isNotBlank(config.getFsql())?config.getFsql():config.getInitsql();
		String fname = StringUtils.isNotBlank(config.getFname())?config.getFname():config.getSqlconfigname();
		BdInitSqlconfig initConfig = new BdInitSqlconfig();
		initConfig.setFid(config.getSqlconfigid());
		initConfig.setFtable(config.getConfigTableName());
		initConfig.setPrimarycol(config.getInitPrimaryCol());
		
		fr.setStartdate(new Date());
		fr.setIscompleted(1);
		this.bdFinancedateRecordMapper.updateById(fr);
		
		if("ORG_ORGS".equals(config.getConfigTableName())) {
			//采集公司信息
			result = this.gatherCompanyInfo(sql,initConfig,plan,dataConfig);
		}else {
			
			if(plan.getFinancetype() == 2) {
				//增量采集
				result = this.gatherFinanceDataInfoBatch(sql,initConfig,plan,dataConfig,fname,config.getFinitincrementcol());
			}else {
				//全量采集
				result = this.gatherFinanceDataInfo(sql,initConfig,plan,dataConfig,fname);
			}
		}
		
		fr = new BdFinancedateRecord();
		fr.setRecordid(fr.getRecordid());
		fr.setIscompleted(2);
		fr.setEnddate(new Date());
		fr.setRecordmemo("采集完成！\n"+result);
		fr.setIsresult(result.getCode());
		this.bdFinancedateRecordMapper.updateById(fr);
		
		//查找所有当前方案未完成采集的数量；
		Integer noCount = this.bdFinancedateRecordMapper.selectNoDealCountByPlanId(plan.getFid(),fr.getPrecordId());
		if(noCount == 0) {
			//全部执行完成
			//将采集方案的采集记录状态改为已完成
			this.bdFinancedateRecordMapper.updatePlanRecordStatus(plan.getFid(),fr.getPrecordId());
		}
	}


	@Override
	public JsonBean getFinancePlanStatus(TblStaffUtil staff, String fid) throws Exception {
		//查找当前整改方案的最近一次采集记录信息
		BdFinancedateRecord planLastRecord = this.bdFinancedateRecordMapper.selectPlanLastRecordEntity(fid);
		
		//获取整改方案下 所有sql的 最新采集记录
		QueryWrapper<BdFinancedateRecord> wrapper = new QueryWrapper<BdFinancedateRecord>();
		wrapper.eq("PRECORDID", planLastRecord.getRecordid());
		wrapper.eq("ISCOMPLETED", 1);
		wrapper.orderByAsc("STARTDATE");
		List<BdFinancedateRecord> recordList = this.bdFinancedateRecordMapper.selectList(wrapper);
		wrapper.clear();
		wrapper.eq("PRECORDID", planLastRecord.getRecordid());
		wrapper.ne("ISCOMPLETED", 1);
		wrapper.orderByAsc("ISCOMPLETED");
		List<BdFinancedateRecord> recordList1 = this.bdFinancedateRecordMapper.selectList(wrapper);
		
		recordList.addAll(recordList1);
		return ResponseFormat.retParam(1, 200, recordList);
	}

	@Override
	public JsonBean stopFinanceProcess(TblStaffUtil staff, String recordid) throws Exception {
		BdFinancedateRecord record = this.bdFinancedateRecordMapper.selectById(recordid);
		record.setEnddate(new Date());
		record.setIscompleted(2);
		record.setIsresult(1);
		record.setRecordmemo("用户取消执行");
		this.bdFinancedateRecordMapper.updateById(record);
		boolean stopped = threadManagementService.stopTask(record.getSqlid());
		
		String tip = stopped ? "任务 " + record.getRecordname() + " 已停止" : "任务不存在";
		Integer noCount = this.bdFinancedateRecordMapper.selectNoDealCountByPlanId(record.getPlanid(),record.getPrecordId());
		if(noCount == 0) {
			//全部执行完成
			//将采集方案的采集记录状态改为已完成
			this.bdFinancedateRecordMapper.updatePlanRecordStatus(record.getPlanid(),record.getPrecordId());
		}
		return ResponseFormat.retParam(1, tip, null);
	}


	@Override
	public JsonBean stopFinancePlanProcess(TblStaffUtil staff, String planid) throws Exception {
		//根据方案查找最近一次的 采集记录信息
		BdFinancedateRecord planRecord = this.bdFinancedateRecordMapper.selectPlanLastRecordEntity(planid);
		
		//查找所有未完成的采集记录信息
		List<BdFinancedateRecord> noDealList = this.bdFinancedateRecordMapper.selectNoDealListByPlanId(planid, planRecord.getRecordid());
		
		//循环关闭线程
		for (BdFinancedateRecord record : noDealList) {
			record.setEnddate(new Date());
			record.setIscompleted(2);
			record.setIsresult(1);
			record.setRecordmemo("用户取消执行");
			this.bdFinancedateRecordMapper.updateById(record);
			threadManagementService.stopTask(record.getSqlid());
		}
		
		this.bdFinancedateRecordMapper.updatePlanRecordStatus(planid, planRecord.getRecordid());
		return ResponseFormat.retParam(1, 200, null);
	}


	@Override
	public JsonBean getFinancePlanTreeList(TblStaffUtil staff, String fname, Integer fstatus) throws Exception {
		List<BdFinanceplanVr> pageList  = this.bdFinanceplanMapper.selectListForFinance(staff.getCurrentOrg().getOrgid(),fname,fstatus);
		return ResponseFormat.retParam(1, 200, pageList);
	}


	@Override
	public void beginCornFinanceTask(TblSysScheduledTask task) throws Exception {
		BdFinancedateRecord fr = new BdFinancedateRecord();
		//获取 采集方案  数据源 和 采集sql配置
		BdFinanceplan plan = this.bdFinanceplanMapper.selectById(task.getPlanId());
		BdFinancedate dataConfig = this.bdFinancedateMapper.selectById(plan.getDbconfigid());
		
		
		fr = new BdFinancedateRecord();
		fr.setRecordid(RandomUtil.uuStringId());
		fr.setRecordip("本机");
		fr.setCreatetime(new Date());
		fr.setIscompleted(1);
		fr.setRecordtype(2);
		fr.setCreatname("系统自动采集");
		fr.setStartdate(new Date());
		fr.setPlanid(plan.getFid());
		fr.setRecordname(plan.getFname());
		this.bdFinancedateRecordMapper.insert(fr);
		String pid = fr.getRecordid();
		
		
		List<BdPlanSqlconfigVr> sqlList = this.bdPlanSqlconfigMapper.selectAllListByPlan(plan.getFid(),plan.getFversionid());
		
		for (BdPlanSqlconfigVr config : sqlList) {
			fr = new BdFinancedateRecord();
			fr.setRecordid(RandomUtil.uuStringId());
			fr.setRecordip("本机");
			fr.setCreatetime(new Date());
			fr.setCreatname("系统自动采集");
			fr.setIscompleted(0);
			fr.setRecordtype(1);
			fr.setPrecordId(pid);
			fr.setSqlid(StringUtils.isNotBlank(config.getFid())?config.getFid()+":"+plan.getFid():config.getSqlconfigid()+":"+plan.getFid());
			fr.setPlanid(plan.getFid());
			fr.setRecordname(StringUtils.isNotBlank(config.getFname())?config.getFname():config.getSqlconfigname());
			this.bdFinancedateRecordMapper.insert(fr);
			config.setFr(fr);
		}
		
		List<TblConfigTableInfo> tableList = this.tblConfigTableInfoMapper.selectListByPlanId(plan.getFid());
		for (TblConfigTableInfo tbInfo : tableList) {
			fr = new BdFinancedateRecord();
			fr.setRecordid(RandomUtil.uuStringId());
			fr.setRecordip("本机");
			fr.setCreatetime(new Date());
			fr.setCreatname("系统自动采集");
			fr.setIscompleted(0);
			fr.setRecordtype(1);
			fr.setPrecordId(pid);
			fr.setSqlid(tbInfo.getFid());
			fr.setPlanid(plan.getFid());
			fr.setRecordname(tbInfo.getFname());
			this.bdFinancedateRecordMapper.insert(fr);
			tbInfo.setFr(fr);
		}
		threadManagementService.initTasks(dataConfig,plan,sqlList,tableList,this);
		threadManagementService.startExecution(dataConfig,plan,sqlList,tableList,this);
        System.out.println("已启动 " + sqlList.size() + " 个任务");
	}


	@Override
	public JsonBean gatherBussinessData(String fid, HttpServletRequest request, HttpServletResponse response) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		String ip = IpUtil.getIpAddr(request);
		TblConfigTableInfo table = this.tblConfigTableInfoMapper.selectById(fid);
		
		if(StringUtils.isBlank(table.getDataConfig())) {
			return ResponseFormat.retParam(0, "未配置数据源，无法采集", null);
		}
		
		if(table.getFinanceType() == 2) {
			return ResponseFormat.retParam(0, "仅支持excel导入，无法采集", null);
		}
		
		String sql = table.getSqlText();
		
		if(StringUtils.isBlank(sql)) {
			return ResponseFormat.retParam(0, "sql语句为空", null);
		}
		
		sql = sql.trim().toUpperCase();
		
		if(!SqlValidatorUtil.validateSql(sql)) {
			return ResponseFormat.retParam(0, "sql语句参数不符合规范", null);
		}
		
		BussinessGatherDataTask task = new BussinessGatherDataTask(fid,staff,ip,this,fid);
        Future<?> future = taskExecutor.submit(task);
        taskFutures.put(fid, future);
        return ResponseFormat.retParam(1, 200, null);
	}


	
	/**
	 * 业务数据采集定时任务
	 */
	@Override
	public void gatherBussinessDataExecuteSql(String taskName, String ip, TblStaffUtil staff, String tableId)
			throws Exception {
		JsonBean result = null;
		BdFinancedateRecord fr = new BdFinancedateRecord();
		TblConfigTableInfo table = this.tblConfigTableInfoMapper.selectById(tableId);
		String frId = RandomUtil.uuStringId();
		try {
			fr.setRecordid(frId);
			fr.setRecordip(ip);
			fr.setCreatetime(new Date());
			fr.setCreator(staff.getStaffid());
			fr.setCreatname(staff.getRealname());
			fr.setLinkdept(staff.getLinkDetp().getOrgid());
			fr.setLinkorg(staff.getCurrentOrg().getOrgid());
			fr.setIscompleted(1);
			fr.setRecordtype(1);
			fr.setStartdate(new Date());
			fr.setSqlid(table.getFid());
			fr.setPlanid(table.getPlanId());
			fr.setRecordname(table.getFname());
			this.bdFinancedateRecordMapper.insert(fr);
			
			BdFinanceplan plan = this.bdFinanceplanMapper.selectById(table.getPlanId());
			BdFinancedate dataConfig = this.bdFinancedateMapper.selectById(table.getDataConfig());
			
			//全量采集
			result = this.gatherBussinessDataInfo(table,plan,dataConfig);
		}finally {
			fr = new BdFinancedateRecord();
			fr.setRecordid(frId);
			fr.setIscompleted(2);
			fr.setEnddate(new Date());
			fr.setRecordmemo(result.getData().toString());
			fr.setIsresult(result.getCode());
			this.bdFinancedateRecordMapper.updateById(fr);
		}
	}

	/**
	 * 业务数据采集接口方法
	 * @param table
	 * @param plan
	 * @param dataConfig
	 * @return
	 */
	private JsonBean gatherBussinessDataInfo(TblConfigTableInfo table, BdFinanceplan plan, BdFinancedate dataConfig) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = "";
		try {
			List<TblConfigColumnInfo> colList = this.tblConfigColumnInfoMapper.selectListByTableId(table.getFid());
			result += "1.获取业务数据采集库表信息。 \n";
			List<List<TblConfigColumnInfo>> queryDataList = new ArrayList<List<TblConfigColumnInfo>>(0);
			List<TblConfigColumnInfo> colDataList = null;
			TblConfigColumnInfo colDate = null;
			//获取采集数据
			String sql = table.getSqlText();
			con = BaseDao.getConnection(dataConfig.getFinancedbtype(), dataConfig.getFinanceconn(), dataConfig.getFinanceport(), dataConfig.getFinanceuser(), dataConfig.getFinancepwd(), dataConfig.getFinancedbexpm());
			result +="2.获取"+dataConfig.getFintext()+"数据源连接信息。\n";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				colDataList = new ArrayList<TblConfigColumnInfo>(0);
				for (TblConfigColumnInfo col : colList) {
					colDate = new TblConfigColumnInfo();
					colDate.setQueryData(rs.getObject(col.getOutsColname()));
					colDate.setFid(col.getFid());
					colDate.setFname(col.getFname());
					colDate.setOursColname(col.getOursColname());
					colDate.setOutsColname(col.getOutsColname());
					colDate.setColType(col.getColType());
					colDate.setColLength(col.getColLength());
					colDate.setIsAutoIncrement(col.getIsAutoIncrement());
					colDate.setIsShowList(col.getIsShowList());
					colDate.setIsFilter(col.getIsFilter());
					colDate.setIsPrimaryKey(col.getIsPrimaryKey());
					colDate.setFtableId(col.getFtableId());
					colDataList.add(colDate);
				}
				queryDataList.add(colDataList);
			}
			result += "3.获取采集数据共"+queryDataList.size()+"条。\n";
			//判断是否已经存在，生成count 语句
			String countSql = "SELECT COUNT(0) FROM "+table.getOursTableName()+" WHERE ";
			String primarySql = "";
			Integer count = 0;
			String ddlSql;
			String colSql="";
			String valueSql="";
			BigDecimal insertCount = BigDecimal.ZERO;
			BigDecimal updateCount = BigDecimal.ZERO;
			for (List<TblConfigColumnInfo> cl : queryDataList) {
				//生成主键条件语句 用于修改，和判断是否已存在数据
				primarySql = this.generalPrimaryWhereSql(cl);
				count = this.tblConfigTableInfoMapper.selectTableCount(countSql+primarySql);
				if(count > 0) {
					//生成修改语句；
					ddlSql = "UPDATE "+table.getOursTableName()+" SET ";
					for (TblConfigColumnInfo col : cl) {
						ddlSql += col.getOursColname()+" = "+BussinessDataSqlFunc.formatQueryDate(col)+",";
					}
					ddlSql += " F_DATASOURCETYPE = 1";
					ddlSql +=" WHERE "+ primarySql;
					this.tblConfigTableInfoMapper.executeSql(ddlSql);
					updateCount = updateCount.add(BigDecimal.ONE);
				}else {
					//生成新增语句；
					colSql="";
					valueSql="";
					ddlSql = "INSERT INTO "+table.getOursTableName()+"(";
					for (TblConfigColumnInfo col : cl) {
						colSql += col.getOursColname()+",";
						valueSql += BussinessDataSqlFunc.formatQueryDate(col)+",";
					}
					colSql += "F_DATASOURCETYPE";
					valueSql += "1";
					ddlSql += colSql+") VALUES("+valueSql+")";
					this.tblConfigTableInfoMapper.executeInsertSql(ddlSql);
					insertCount = insertCount.add(BigDecimal.ONE);
				}
			}
			result = table.getFname()+"采集完成! \n "+result+"4.本次采集共新增"+insertCount+"条数据，修改"+updateCount+"条数据。\n";
		}catch (Exception e) {
			return ResponseFormat.retParam(0, table.getFname()+"采集失败","采集失败! \n"+result+e.getMessage());
		}finally {
			BaseDao.close(dataConfig.getFinancedbtype(), con, rs, ps);
		}
		return ResponseFormat.retParam(1, 200,result);
	}


	private String generalPrimaryWhereSql(List<TblConfigColumnInfo> list) throws Exception {
		String primarySql = "";
		for (TblConfigColumnInfo col : list) {
			if(col.getIsPrimaryKey() == 0) {
				primarySql += col.getOursColname()+" = "+BussinessDataSqlFunc.formatQueryDate(col)+" AND ";
			}
		}
		if(StringUtils.isNotBlank(primarySql)) {
			primarySql = primarySql.substring(0,primarySql.length() - 5);
		}
		return primarySql;
	}


	public void gatherBussinessDataBatch(TblConfigTableInfo tblInfo) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = "";
		BdFinancedate dataConfig = this.bdFinancedateMapper.selectById(tblInfo.getDataConfig());
		
		try {
			result += "1.获取业务数据采集库表信息。 \n";
			List<TblConfigColumnInfo> colList = this.tblConfigColumnInfoMapper.selectListByTableId(tblInfo.getFid());
			List<List<TblConfigColumnInfo>> queryDataList = new ArrayList<List<TblConfigColumnInfo>>(0);
			
			//获取采集数据
			String sql = tblInfo.getSqlText();
			con = BaseDao.getConnection(dataConfig.getFinancedbtype(), dataConfig.getFinanceconn(), dataConfig.getFinanceport(), dataConfig.getFinanceuser(), dataConfig.getFinancepwd(), dataConfig.getFinancedbexpm());
			result +="2.获取"+dataConfig.getFintext()+"数据源连接信息。\n";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				for (TblConfigColumnInfo col : colList) {
					col.setQueryData(rs.getObject(col.getOutsColname()));
				}
				queryDataList.add(colList);
			}
			result += "3.获取采集数据共"+queryDataList.size()+"条。\n";
			//判断是否已经存在，生成count 语句
			String countSql = "SELECT COUNT(0) FROM "+tblInfo.getOursTableName()+" WHERE ";
			String primarySql = "";
			Integer count = 0;
			String ddlSql;
			String colSql="";
			String valueSql="";
			BigDecimal insertCount = BigDecimal.ZERO;
			BigDecimal updateCount = BigDecimal.ZERO;
			for (List<TblConfigColumnInfo> cl : queryDataList) {
				//生成主键条件语句 用于修改，和判断是否已存在数据
				primarySql = this.generalPrimaryWhereSql(cl);
				countSql += primarySql;
				count = this.tblConfigTableInfoMapper.selectTableCount(countSql);
				if(count > 0) {
					//生成修改语句；
					ddlSql = "UPDATE "+tblInfo.getOursTableName()+" SET ";
					for (TblConfigColumnInfo col : cl) {
						ddlSql += col.getOursColname()+" = "+BussinessDataSqlFunc.formatQueryDate(col)+",";
					}
					ddlSql +=" WHERE "+ primarySql;
					this.tblConfigTableInfoMapper.executeSql(ddlSql);
					updateCount = updateCount.add(BigDecimal.ONE);
				}else {
					//生成新增语句；
					ddlSql = "INSERT INTO "+tblInfo.getOursTableName()+"(";
					for (TblConfigColumnInfo col : cl) {
						colSql += col.getOursColname()+",";
						valueSql += BussinessDataSqlFunc.formatQueryDate(col)+",";
					}
					ddlSql += colSql.substring(0,colSql.length()-1)+") VALUES("+valueSql.substring(0,valueSql.length()-1)+")";
					this.tblConfigTableInfoMapper.executeInsertSql(ddlSql);
					insertCount = insertCount.add(BigDecimal.ONE);
				}
			}
			result = tblInfo.getFname()+"采集完成! \n "+result+"4.本次采集共新增"+insertCount+"条数据，修改"+updateCount+"条数据。\n";
		}catch(Exception e){
			result = tblInfo.getFname()+"采集失败! \n"+result+e.getMessage();
		}finally {
			BdFinancedateRecord fr = tblInfo.getFr();
			fr.setRecordid(fr.getRecordid());
			fr.setIscompleted(2);
			fr.setEnddate(new Date());
			fr.setRecordmemo(result);
			fr.setIsresult(1);
			this.bdFinancedateRecordMapper.updateById(fr);
			
			BaseDao.close(dataConfig.getFinancedbtype(), con, rs, ps);
			
			//查找所有当前方案未完成采集的数量；
			Integer noCount = this.bdFinancedateRecordMapper.selectNoDealCountByPlanId(tblInfo.getPlanId(),fr.getPrecordId());
			if(noCount == 0) {
				//全部执行完成
				//将采集方案的采集记录状态改为已完成
				this.bdFinancedateRecordMapper.updatePlanRecordStatus(tblInfo.getPlanId(),fr.getPrecordId());
			}
		}
	}


	@Override
	public JsonBean stopGatherData(String fid) throws Exception {
		Future<?> future = taskFutures.get(fid);
        if (future != null) {
            future.cancel(true); // 中断线程
            taskFutures.remove(fid);
        }
        
        QueryWrapper<BdFinancedateRecord> wrapper = new QueryWrapper<BdFinancedateRecord>();
        wrapper.eq("SQLID", fid);
        wrapper.eq("ISCOMPLETED", 1);
        
        List<BdFinancedateRecord> brList = this.bdFinancedateRecordMapper.selectList(wrapper);
        for (BdFinancedateRecord br : brList) {
			br.setEnddate(new Date());
			br.setIscompleted(2);
			br.setRecordmemo("用户手动取消执行");
			br.setIsresult(0);
			this.bdFinancedateRecordMapper.updateById(br);
		}
        
        return ResponseFormat.retParam(1, 200, null);
	}
	
	
}
