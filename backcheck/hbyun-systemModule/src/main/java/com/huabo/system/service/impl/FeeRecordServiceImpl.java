package com.huabo.system.service.impl;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.dto.FeeRecordQueryDTO;
import com.huabo.system.entity.TblFeeRecord;
import com.huabo.system.mapper.TblFeeRecordMapper;
import com.huabo.system.service.FeeRecordService;
import com.hbfk.util.user.UserProvider;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FeeRecordServiceImpl implements FeeRecordService {

    @Resource
    private TblFeeRecordMapper feeRecordMapper;
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean getPersonalRecords(String token, FeeRecordQueryDTO dto) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        String staffId = loginStaff.getStaffid().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = (dto.getStartTime() != null && !dto.getStartTime().isEmpty()) ? sdf.parse(dto.getStartTime()) : null;
        Date endTime = (dto.getEndTime() != null && !dto.getEndTime().isEmpty()) ? sdf.parse(dto.getEndTime()) : null;

        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<TblFeeRecord> records = feeRecordMapper.findPersonalRecords(
                staffId, startTime, endTime, dto.getModuleType(), dto.getRightId(), dto.getSubModuleName());
        PageInfo<TblFeeRecord> pageInfo = new PageInfo<>(records);

        BigDecimal totalFee = feeRecordMapper.sumFeeByStaff(staffId, startTime, endTime);

        // 按小模块聚合统计（给统计图用，同时受时间和模块筛选）
        List<Map<String, Object>> chartData = feeRecordMapper.statsBySubModuleForPerson(staffId, startTime, endTime, dto.getModuleType());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", pageInfo.getList());
        resultMap.put("total", pageInfo.getTotal());
        resultMap.put("pageNum", pageInfo.getPageNum());
        resultMap.put("pageSize", pageInfo.getPageSize());
        resultMap.put("totalFee", totalFee);
        resultMap.put("chartData", chartData);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public void exportRecords(String token, FeeRecordQueryDTO dto, HttpServletResponse response) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return;
        }

        String staffId = loginStaff.getStaffid().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = (dto.getStartTime() != null && !dto.getStartTime().isEmpty()) ? sdf.parse(dto.getStartTime()) : null;
        Date endTime = (dto.getEndTime() != null && !dto.getEndTime().isEmpty()) ? sdf.parse(dto.getEndTime()) : null;
        List<TblFeeRecord> records = feeRecordMapper.findPersonalRecords(
                staffId, startTime, endTime, dto.getModuleType(), dto.getRightId(), dto.getSubModuleName());

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("费用明细");

        XSSFRow header = sheet.createRow(0);
        String[] headers = {"大模块", "小模块", "页面", "接口描述", "费用(元)", "页面路由", "调用时间"};
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < records.size(); i++) {
            TblFeeRecord r = records.get(i);
            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(r.getModuleName() != null ? r.getModuleName() : "");
            row.createCell(1).setCellValue(r.getSubModuleName() != null ? r.getSubModuleName() : "");
            row.createCell(2).setCellValue(r.getPageName() != null ? r.getPageName() : "");
            row.createCell(3).setCellValue(r.getApiSummary() != null ? r.getApiSummary() : "");
            row.createCell(4).setCellValue(r.getFeeAmount() != null ? r.getFeeAmount().doubleValue() : 0);
            row.createCell(5).setCellValue(r.getPageRoute() != null ? r.getPageRoute() : "");
            row.createCell(6).setCellValue(r.getCreateTime() != null ? dtf.format(r.getCreateTime()) : "");
        }

        // Write response
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("费用明细.xlsx", "UTF-8"));
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
