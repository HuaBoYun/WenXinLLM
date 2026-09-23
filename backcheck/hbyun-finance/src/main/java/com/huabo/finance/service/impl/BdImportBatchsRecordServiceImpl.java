package com.huabo.finance.service.impl;

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
import com.hbfk.util.user.UserProvider;
import com.huabo.finance.entity.BdImportBatchsRecord;
import com.huabo.finance.entity.TblConfigTableInfo;
import com.huabo.finance.mapper.BdImportBatchsRecordMapper;
import com.huabo.finance.mapper.TblConfigTableInfoMapper;
import com.huabo.finance.service.BdImportBatchsRecordService;
import com.huabo.finance.vo.BdImportBatchsRecordVo;

/**
 * <p>
 * 账簿角色授权 服务实现类
 * </p>
 *
 * @author L
 * @since 2025-04-01
 */
@Service
public class BdImportBatchsRecordServiceImpl extends ServiceImpl<BdImportBatchsRecordMapper, BdImportBatchsRecord> implements BdImportBatchsRecordService {
	@Resource
	private UserProvider userProvider;
	
	@Resource
	private BdImportBatchsRecordMapper bdImportBatchsRecordMapper;
	
	@Resource
    private TblConfigTableInfoMapper tblConfigTableInfoMapper;
	
	@Override
	public JsonBean getImportRecordList(BdImportBatchsRecordVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		QueryWrapper<BdImportBatchsRecord> wrapper = new QueryWrapper<BdImportBatchsRecord>();
		if(StringUtils.isNotBlank(vo.getCreatname())) {
			wrapper.eq("CREATNAME", vo.getCreatname());
		}
		wrapper.eq("TABLEID", vo.getTableId());
		wrapper.select("IMPORTID,RECORDNAME,STARTDATE,ENDDATE,ISCOMPLETED,ISRESULT,BATCHNUM,RECORDIP,CREATNAME");
		wrapper.orderByDesc("BATCHNUM");
		Page<BdImportBatchsRecord> page = new Page<BdImportBatchsRecord>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
	    IPage<BdImportBatchsRecord> pageList  = this.bdImportBatchsRecordMapper.selectPage(page, wrapper);
	    return ResponseFormat.retParam(1, 200, pageList);
	}

	@Override
	public JsonBean getImportRecordDetail(String importId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		BdImportBatchsRecord record = this.bdImportBatchsRecordMapper.selectById(importId);
		return ResponseFormat.retParam(1, 200, record);
	}

	@Override
	public JsonBean removeImportData(String importId, String tableId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblConfigTableInfo table = this.tblConfigTableInfoMapper.selectById(tableId);
		
		String sql = "DELETE FROM "+table.getOursTableName().toUpperCase()+" WHERE F_IMPORTBATCHES = '"+importId+"' AND F_DATASOURCETYPE = 2";
		this.tblConfigTableInfoMapper.executeDeleteSql(sql);
		
		return ResponseFormat.retParam(1, 200, null);
	}

}
