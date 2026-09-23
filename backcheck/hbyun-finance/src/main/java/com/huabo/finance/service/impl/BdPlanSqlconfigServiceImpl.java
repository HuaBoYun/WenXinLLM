package com.huabo.finance.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.finance.entity.BdFinancedate;
import com.huabo.finance.entity.BdFinancedateRecord;
import com.huabo.finance.entity.BdFinanceplan;
import com.huabo.finance.entity.BdInitSqlconfig;
import com.huabo.finance.entity.BdPlanSqlconfig;
import com.huabo.finance.mapper.BdFinancedateMapper;
import com.huabo.finance.mapper.BdFinancedateRecordMapper;
import com.huabo.finance.mapper.BdFinanceplanMapper;
import com.huabo.finance.mapper.BdInitSqlconfigMapper;
import com.huabo.finance.mapper.BdPlanSqlconfigMapper;
import com.huabo.finance.service.BdPlanSqlconfigService;
import com.huabo.finance.unit.BaseDao;
import com.huabo.finance.vo.BdPlanSqlconfigVo;
import com.huabo.finance.vr.BdFinancedateRecordVr;
import com.huabo.finance.vr.BdPlanSqlconfigVr;

/**
 * <p>
 * 财务采方案配置sql语句 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-03-13
 */
@Service
public class BdPlanSqlconfigServiceImpl extends ServiceImpl<BdPlanSqlconfigMapper, BdPlanSqlconfig> implements BdPlanSqlconfigService {
	@Resource
	private BdPlanSqlconfigMapper bdPlanSqlconfigMapper;
	
	@Resource
	private BdFinanceplanMapper bdFinanceplanMapper;
	
	@Resource
	private BdFinancedateMapper bdFinancedateMapper;
	
	@Resource
	private BdInitSqlconfigMapper bdInitSqlconfigMapper;
	
	@Resource
	private BdFinancedateRecordMapper bdFinancedateRecordMapper;
	
	@Override
	public JsonBean save(TblStaffUtil staff, BdPlanSqlconfig ps) throws Exception {
		if(StringUtils.isBlank(ps.getFsql())) {
			return ResponseFormat.retParam(0, 10004, null);
		}
		
		String sql = ps.getFsql().toUpperCase();
		
		if(sql.indexOf("CREATE") != -1 || sql.indexOf("DELETE") != -1 || sql.indexOf("UPDATE") != -1 || sql.indexOf("DROP") != -1 ) {
			return ResponseFormat.retParam(0, "sql语句参数不符合规范", null);
		}
		
		if(StringUtils.isNotBlank(ps.getFid())) {
			ps.setModifiedtime(new Date());
			ps.setModifier(staff.getStaffid());
			this.baseMapper.updateById(ps);
		}else {
			ps.setFid(RandomUtil.uuStringId());
			ps.setCreationtime(new Date());
			ps.setCreator(staff.getStaffid());
			ps.setLinkdetpid(staff.getLinkDetp().getOrgid());
			ps.setLinkorgid(staff.getCurrentOrg().getOrgid());
			this.baseMapper.insert(ps);
		}
		
		return ResponseFormat.retParam(1, 200, ps);
	}

	@Override
	public JsonBean detail(String fid) throws Exception {
		Map<String, Object> resultMap = new HashMap<String,Object>(0);
		
		BdPlanSqlconfig ps = this.baseMapper.selectById(fid);
		resultMap.put("ps", ps);
		if(StringUtils.isNotBlank(ps.getFinitsqlid())) {
			BdInitSqlconfig bsc = this.bdInitSqlconfigMapper.selectById(ps.getFinitsqlid());
			resultMap.put("bsc", bsc);
		}
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean remove(String fid) throws Exception {
		if(StringUtils.isBlank(fid)) {
			return ResponseFormat.retParam(1, 200, null);
		}
		this.baseMapper.deleteById(fid);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean findAllList(TblStaffUtil staff, BdPlanSqlconfigVo vo) throws Exception {
		if(StringUtils.isBlank(vo.getFplanid())){
			return ResponseFormat.retParam(0, 10004, null);
		}
		
		Page<BdPlanSqlconfigVr> page = new Page<BdPlanSqlconfigVr>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
	    IPage<BdPlanSqlconfigVr> pageList  = this.bdPlanSqlconfigMapper.selectPageInfo(page, vo);
	    
	    
	    QueryWrapper<BdFinancedateRecord> wrapper = new QueryWrapper<BdFinancedateRecord>();
	    wrapper.eq("ISCOMPLETED", 1);
	    wrapper.eq("PLANID", vo.getFplanid());
	    List<BdFinancedateRecord> recordList = this.bdFinancedateRecordMapper.selectList(wrapper);
	    
	    BdFinancedateRecordVr fr = null;
	    for (BdPlanSqlconfigVr bsf : pageList.getRecords()) {
			for (BdFinancedateRecord br : recordList) {
				if(StringUtils.isBlank(br.getSqlid())) {
					continue;
				}
				if(br.getSqlid().equals(bsf.getFid()+":"+bsf.getFinitPlanid()) || br.getSqlid().equals(bsf.getSqlconfigid()+":"+bsf.getFinitPlanid())  ) {
					fr = new BdFinancedateRecordVr();
					fr.setSqlid(br.getSqlid());
					fr.setRecordid(br.getRecordid());
					bsf.setBfrv(fr);
					continue;
				}
			}
		}
	    
	    
		return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean testSql(TblStaffUtil staff, BdPlanSqlconfig ps) throws Exception {
		Connection con = null;
		PreparedStatement cps = null;
		if(StringUtils.isBlank(ps.getFplanid())) {
			return ResponseFormat.retParam(0, "采集方案信息入参缺失！", null);
		}
		
		if(StringUtils.isBlank(ps.getFsql())) {
			return ResponseFormat.retParam(0, "sql语句入参缺失！", null);
		}
		
		String sql = ps.getFsql().toUpperCase();
		
		if(sql.indexOf("CREATE") != -1 || sql.indexOf("DELETE") != -1 || sql.indexOf("UPDATE") != -1 || sql.indexOf("DROP") != -1 ) {
			return ResponseFormat.retParam(0, "sql语句参数不符合规范", null);
		}
		
		BdFinanceplan bp = this.bdFinanceplanMapper.selectById(ps.getFplanid());
		if(bp == null) {
			return ResponseFormat.retParam(0, "采集方案数据丢失！", null);
		}
		
		BdFinancedate bd = this.bdFinancedateMapper.selectByPlanId(bp.getFid());
		if(bd == null) {
			return ResponseFormat.retParam(0, "当前采集方案未配置数据源信息", null);
		}
		
		try {
			con = BaseDao.getConnection(bd.getFinancedbtype(), bd.getFinanceconn(), bd.getFinanceport(), bd.getFinanceuser(), bd.getFinancepwd(), bd.getFinancedbexpm());
			cps = con.prepareStatement(ps.getFsql());
			boolean flag = cps.execute();
			if(!flag){
				return ResponseFormat.retParam(0, "sql执行失败", null);
			}
		}catch (Exception e) {
			return ResponseFormat.retParam(0, e.getMessage(), null);
		}finally {
			BaseDao.close(bd.getFinanceconn(), con, null, cps);
		}
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean excuteSql(TblStaffUtil staff, BdPlanSqlconfig ps) throws Exception {
		Connection con = null;
		PreparedStatement cps = null;
		ResultSet rs = null;
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		if(StringUtils.isBlank(ps.getFplanid())) {
			return ResponseFormat.retParam(0, "采集方案信息入参缺失！", null);
		}
		
		if(StringUtils.isBlank(ps.getFsql())) {
			return ResponseFormat.retParam(0, "sql语句入参缺失！", null);
		}
		
		String sql = ps.getFsql().toUpperCase();
		
		if(sql.indexOf("CREATE") != -1 || sql.indexOf("DELETE") != -1 || sql.indexOf("UPDATE") != -1 || sql.indexOf("DROP") != -1 ) {
			return ResponseFormat.retParam(0, "sql语句参数不符合规范", null);
		}
		
		BdFinanceplan bp = this.bdFinanceplanMapper.selectById(ps.getFplanid());
		if(bp == null) {
			return ResponseFormat.retParam(0, "采集方案数据丢失！", null);
		}
		
		BdFinancedate bd = this.bdFinancedateMapper.selectByPlanId(bp.getFid());
		if(bd == null) {
			return ResponseFormat.retParam(0, "当前采集方案未配置数据源信息", null);
		}
		
		try {
			con = BaseDao.getConnection(bd.getFinancedbtype(), bd.getFinanceconn(), bd.getFinanceport(), bd.getFinanceuser(), bd.getFinancepwd(), bd.getFinancedbexpm());
			cps = con.prepareStatement(ps.getFsql());
			rs = cps.executeQuery();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			List<String> columnNameList = new ArrayList<String>(0);
			for (int i = 1 ; i <= columnCount ; i++) {
				columnNameList.add(rsmd.getColumnName(i));
			}
			resultMap.put("colName", columnNameList);
			
			List<Object[]> dataList = new ArrayList<Object[]>(0);
			Object[] objs = null;
			
			while (rs.next()) {
				objs = new Object[columnCount];
				
				for (int i = 0 ; i < columnCount ; i++) {
					objs[i] = rs.getObject(i+1);
				}
				dataList.add(objs);
			}
			
			resultMap.put("dataList", dataList);
		}catch (Exception e) {
			return ResponseFormat.retParam(0, e.getMessage(), null);
		}finally {
			BaseDao.close(bd.getFinancedbtype(), con, rs, cps);
		}
		return ResponseFormat.retParam(1, 200, resultMap);
	}
}
