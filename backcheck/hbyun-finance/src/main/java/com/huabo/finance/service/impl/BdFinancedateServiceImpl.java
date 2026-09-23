package com.huabo.finance.service.impl;

import com.huabo.finance.entity.BdFinancedate;
import com.huabo.finance.mapper.BdFinancedateMapper;
import com.huabo.finance.service.BdFinancedateService;
import com.huabo.finance.unit.BaseDao;
import com.huabo.finance.vo.BdFinancedateVo;
import com.huabo.finance.vr.FaAccbookinfoVr;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.BaseDaoSqlServer;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;

import java.sql.Connection;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 财务数据采集配置信息表 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-03-11
 */
@Service
public class BdFinancedateServiceImpl extends ServiceImpl<BdFinancedateMapper, BdFinancedate> implements BdFinancedateService {

	@Resource
	private BdFinancedateMapper bdFinancedateMapper;
	
	
	@Override
	public JsonBean findAllList(TblStaffUtil staff, BdFinancedateVo vo) throws Exception {
		
		
		QueryWrapper<BdFinancedate> wrapper = new QueryWrapper<BdFinancedate>();
		
		if(StringUtils.isNotBlank(vo.getFintext())) {
			wrapper.like("FINTEXT", vo.getFintext());
		}
		if(StringUtils.isNotBlank(vo.getFinmemo())) {
			wrapper.like("FINMEMO", vo.getFinmemo());
		}
		if(StringUtils.isNotBlank(vo.getFinancedbtype())) {
			wrapper.eq("FINANCEDBTYPE", vo.getFinancedbtype());
		}
		
		if(StringUtils.isNotBlank(vo.getPlanid())) {
			wrapper.inSql("FID", "SELECT DATACONFIG FROM BD_FINANCEPLAN_DATACONFIG WHERE PLANID = '"+vo.getPlanid()+"'");
		}
		
		if(vo.getStatus() != null) {
			wrapper.eq("STATUS", vo.getStatus());
		}
		wrapper.select("FID","FINTEXT","FINMEMO","FINANCEDBTYPE","FINANCEDBEXPM","FINANCECONN","FINANCEUSER","FINANCEPORT","STATUS","CREATIONTIME","MODIFIEDTIME","CREATOR","MODIFIER");
		
		Page<BdFinancedate> page = new Page<>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		page = bdFinancedateMapper.selectPage(page, wrapper);
		
		return ResponseFormat.retParam(1, 200, page);
	}


	@Override
	public JsonBean save(TblStaffUtil staff, BdFinancedate fd) throws Exception {
		
		if(StringUtils.isNotBlank(fd.getFid())) {
			fd.setModifiedtime(new Date());
			fd.setModifier(staff.getStaffid());
			this.bdFinancedateMapper.updateById(fd);
		}else {
			fd.setFid(RandomUtil.uuStringId());
			fd.setCreationtime(new Date());
			fd.setCreator(staff.getStaffid());
			fd.setLinkdetpid(staff.getLinkDetp().getOrgid());
			fd.setLinkorgid(staff.getCurrentOrg().getOrgid());
			this.bdFinancedateMapper.insert(fd);
			
		}
		return ResponseFormat.retParam(1, 200, fd);
	}


	@Override
	public JsonBean detail(String fid) throws Exception {
		BdFinancedate fd = this.bdFinancedateMapper.selectById(fid);
		fd.setFinancepwd(null);//不暴露密码
		return ResponseFormat.retParam(1, 200, fd);
	}


	@Override
	public JsonBean remove(String fid) throws Exception {
		Integer count = this.bdFinancedateMapper.selectUseCountById(fid);
		if(count > 0 ) {
			return ResponseFormat.retParam(0, "数据源使用，无法删除！", null);
		}
		this.bdFinancedateMapper.deleteById(fid);
		return ResponseFormat.retParam(1, 200, null);
	}


	@Override
	public JsonBean testCon(BdFinancedate fd) throws Exception {
		Connection conn = null;
		
		try {
			if(StringUtils.isBlank(fd.getFinanceconn())) {
				return ResponseFormat.retParam(0, "数据库连接地址为空", null);
			}
			if(StringUtils.isBlank(fd.getFinancedbexpm())) {
				return ResponseFormat.retParam(0, "数据库名称为空", null);
			}
			if(StringUtils.isBlank(fd.getFinancedbtype())) {
				return ResponseFormat.retParam(0, "数据库类型为空", null);
			}
			if(StringUtils.isBlank(fd.getFinanceport())) {
				return ResponseFormat.retParam(0, "数据库端口为空", null);
			}
			if(StringUtils.isBlank(fd.getFinancepwd())) {
				return ResponseFormat.retParam(0, "数据库密码为空", null);
			}
			if(StringUtils.isBlank(fd.getFinanceuser())) {
				return ResponseFormat.retParam(0, "数据库用户名为空", null);
			}
			
			conn = BaseDao.getConnection(fd.getFinancedbtype(), fd.getFinanceconn(), fd.getFinanceport(), fd.getFinanceuser(),fd.getFinancepwd(),fd.getFinancedbexpm());
			
			if(conn == null) {
				return ResponseFormat.retParam(0, "数据库连接失败！", null);
			}
			
			if(!conn.isValid(5)) {
				return ResponseFormat.retParam(0, "数据库连接超时！", null);
			}
			
			return ResponseFormat.retParam(1, "连接成功", null);
			
		}finally {
			BaseDao.close(fd.getFinancedbtype(), conn, null, null);
		}
		
	}

}
