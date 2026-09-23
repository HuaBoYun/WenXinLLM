package com.huabo.finance.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.IpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.finance.entity.BdImportBatchsRecord;
import com.huabo.finance.entity.TblConfigColumnInfo;
import com.huabo.finance.mapper.BdImportBatchsRecordMapper;
import com.huabo.finance.mapper.TblConfigColumnInfoMapper;
import com.huabo.finance.mapper.TblConfigTableInfoMapper;
import com.huabo.finance.service.ExcelImportService;
import com.huabo.finance.thread.DynamicDataImportListener;
import com.huabo.finance.vr.TblConfigTableInfoVr;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * excel 导入服务实现类
 * </p>
 *
 * @author L
 * @since 2025-06-24
 */
@Service
@RequiredArgsConstructor
public class ExcelImportServiceImpl implements ExcelImportService {

	@Resource
	private UserProvider userProvider;
	
	@Resource
	private TblConfigTableInfoMapper tblConfigTableInfoMapper;
	
	@Resource
	private TblConfigColumnInfoMapper tblConfigColumnInfoMapper;
	
	@Resource
	private BdImportBatchsRecordMapper bdImportBatchsRecordMapper;
	
	private final JdbcTemplate jdbcTemplate;
	
	@Override
	@Async("excelImportExecutor") // 使用异步线程池
	public JsonBean importTemplateData(String tableId, MultipartFile file, HttpServletRequest request) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		String recordMemo = "开始导入：\n";
		
		TblConfigTableInfoVr info = this.tblConfigTableInfoMapper.selectEntityById(tableId);
		List<TblConfigColumnInfo> colList = this.tblConfigColumnInfoMapper.selectListByTableId(tableId);
		
		recordMemo += "1.获取业务数据"+info.getFname()+"的库表配置信息...\n";
		
		String ip = IpUtil.getIpAddr(request);
		
		QueryWrapper<BdImportBatchsRecord> wrapper = new QueryWrapper<BdImportBatchsRecord>();
		wrapper.eq("TABLEID", tableId);
		Integer batchNum = this.bdImportBatchsRecordMapper.selectCount(wrapper);
		
		BdImportBatchsRecord record = new BdImportBatchsRecord();
		record.setRecordip(ip);
		record.setRecordname(info.getFname()+"导入记录");
		record.setCreator(staff.getStaffid());
		record.setCreatname(staff.getRealname());
		record.setStartdate(new Date());
		record.setCreatetime(new Date());
		record.setImportId(RandomUtil.uuStringId());
		record.setLinkdept(staff.getLinkDetp().getOrgid());
		record.setLinkorg(staff.getCurrentOrg().getOrgid());
		record.setIscompleted(1);
		record.setTableId(tableId);
		record.setBatchNum(batchNum+1);
		this.bdImportBatchsRecordMapper.insert(record);
		List<TblConfigColumnInfo> primaryCol = colList.stream().filter(item -> 0 == item.getIsPrimaryKey()).collect(Collectors.toList());
		  // 2. 配置监听器（批次大小5000）
		DynamicDataImportListener listener = new DynamicDataImportListener(
				info, 
				colList,
				primaryCol,
				record.getImportId(),
				recordMemo,
				bdImportBatchsRecordMapper,
                jdbcTemplate,
                5000
        );
        
		
		
        // 3. 使用EasyExcel流式读取
        try (InputStream is = file.getInputStream()) {
            EasyExcel.read(is)
                    .registerReadListener(listener)
                    .sheet()
                    .doRead();
        } catch (IOException e) {
        	record.setRecordmemo("文件读取失败! ");
        	record.setIscompleted(2);
        	record.setIsresult(0);
        	record.setEnddate(new Date());
        	this.bdImportBatchsRecordMapper.updateById(record);
        }
		return ResponseFormat.retParam(1, 200, null);
	}

}
