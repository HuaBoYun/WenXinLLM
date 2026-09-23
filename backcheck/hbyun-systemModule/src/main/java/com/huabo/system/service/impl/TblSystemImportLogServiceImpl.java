package com.huabo.system.service.impl;


import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblSystemImportLog;
import com.huabo.system.mapper.TblSystemImportLogMapper;
import com.huabo.system.service.TblSystemImportLogService;

@Service
@Transactional
public class TblSystemImportLogServiceImpl implements TblSystemImportLogService {
    @Resource
    private TblSystemImportLogMapper tblSystemImportLogMapper;
    
    @Resource
    private UserProvider userProvider;

	@Override
	public void save(TblSystemImportLog imlog) throws Exception {
		this.tblSystemImportLogMapper.insert(imlog);
	}

	@Override
	public JsonBean getSystemImportLogList(Integer pageNumber, Integer pageSize, String token, String createTime, Integer importType)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        Page<TblSystemImportLog> page = new Page<TblSystemImportLog>(pageNumber,pageSize);
		page.setOptimizeCountSql(false); // 禁用自动优化
        IPage<TblSystemImportLog> pageList = this.tblSystemImportLogMapper.selectImportLogPage(page, createTime,importType); 
        return ResponseFormat.retParam(1, 200, pageList);
	}

    

}