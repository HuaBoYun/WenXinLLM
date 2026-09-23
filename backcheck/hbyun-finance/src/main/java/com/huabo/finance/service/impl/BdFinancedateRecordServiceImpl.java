package com.huabo.finance.service.impl;

import com.huabo.finance.entity.BdFinancedateRecord;
import com.huabo.finance.mapper.BdFinancedateRecordMapper;
import com.huabo.finance.service.BdFinancedateRecordService;
import com.huabo.finance.vo.BdFinancedateRecordVo;
import com.huabo.finance.vr.BdFinanceplanVr;
import com.huabo.finance.vr.FaAccbookinfoVr;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 财务数据采集记录表 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-03-17
 */
@Service
public class BdFinancedateRecordServiceImpl extends ServiceImpl<BdFinancedateRecordMapper, BdFinancedateRecord> implements BdFinancedateRecordService {

	@Resource
	private BdFinancedateRecordMapper bdFinancedateRecordMapper;
	
	@Override
	public JsonBean getGoonAcInfoList(BdFinancedateRecordVo vo) throws Exception {
		vo.setIscompleted(1);
		Page<BdFinanceplanVr> page = new Page<>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
	    IPage<BdFinanceplanVr> pageInfo  = this.bdFinancedateRecordMapper.selectGoonInfoPageInfo(page, vo);
		return ResponseFormat.retParam(1, 200, pageInfo);
	}

	@Override
	public JsonBean getAllAcInfoList(BdFinancedateRecordVo vo) throws Exception {
		Page<BdFinanceplanVr> page = new Page<>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
	    IPage<BdFinanceplanVr> pageInfo  = this.bdFinancedateRecordMapper.selectGoonInfoPageInfo(page, vo);
		return ResponseFormat.retParam(1, 200, pageInfo);
	}

	@Override
	public JsonBean getFinanceRecordPageList(TblStaffUtil staff, BdFinancedateRecordVo vo, String tableId,
			String sqlconfigid, String planid, String sqlfinid) throws Exception {
		if(StringUtils.isNotBlank(tableId)) {
			vo.setSqlid(tableId);
		}else {
			String taskId = StringUtils.isNotBlank(sqlfinid)?sqlfinid+":"+planid:sqlconfigid+":"+planid;
			vo.setSqlid(taskId);
		}
		
		QueryWrapper<BdFinancedateRecord> wrapper = new QueryWrapper<BdFinancedateRecord>();
		wrapper.eq("SQLID", vo.getSqlid());
		
		if(vo.getRecordtype() != null) {
			wrapper.eq("RECORDTYPE", vo.getRecordtype());
		}
		wrapper.orderByDesc("CREATETIME");
		wrapper.select("RECORDID","RECORDNAME","SQLID","RECORDTYPE","ISCOMPLETED","ISRESULT","CREATETIME","RECORDIP","CREATNAME","STARTDATE","ENDDATE");
		Page<BdFinancedateRecord> page = new Page<BdFinancedateRecord>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<BdFinancedateRecord> pageInfo  = this.bdFinancedateRecordMapper.selectPage(page, wrapper);
		return ResponseFormat.retParam(1, 200, pageInfo);
	}

	@Override
	public JsonBean getRecordDetail(TblStaffUtil staff, String recordid) throws Exception {
		BdFinancedateRecord record = this.bdFinancedateRecordMapper.selectById(recordid);
		return ResponseFormat.retParam(1, 200, record);
	}

}
