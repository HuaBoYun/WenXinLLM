package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.ThreeTableCompare;
import com.huabo.cybermonitor.entity.ThreeTableDispatch;
import com.huabo.cybermonitor.mapper.ThreeTableCompareMapper;
import com.huabo.cybermonitor.mapper.ThreeTableDispatchMapper;
import com.huabo.cybermonitor.service.IThreeTableCompareService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.ThreeTableCompareDetailVO;
import com.huabo.cybermonitor.vo.ThreeTableCompareQueryVO;
import com.huabo.cybermonitor.vo.ThreeTableDispatchVO;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 三表比对 Service 实现
 * @author system
 * @date 2025-01-01
 */
@Service
public class ThreeTableCompareServiceImpl
        extends ServiceImpl<ThreeTableCompareMapper, ThreeTableCompare>
        implements IThreeTableCompareService {

    private static final Logger log = LoggerFactory.getLogger(ThreeTableCompareServiceImpl.class);

    @Autowired
    private ThreeTableDispatchMapper dispatchMapper;

    @Override
    public PageResult<ThreeTableCompare> getList(ThreeTableCompareQueryVO queryVO) {
        LambdaQueryWrapper<ThreeTableCompare> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryVO.getCompanyName())) {
            wrapper.like(ThreeTableCompare::getCompanyName, queryVO.getCompanyName());
        }
        if (StringUtils.hasText(queryVO.getDiffStatus())) {
            wrapper.eq(ThreeTableCompare::getDiffStatus, queryVO.getDiffStatus());
        }
        if (StringUtils.hasText(queryVO.getShareholderType())) {
            wrapper.eq(ThreeTableCompare::getShareholderType, queryVO.getShareholderType());
        }
        wrapper.orderByDesc(ThreeTableCompare::getCreateTime);

        int pageNumber = queryVO.getPageNumber() != null ? queryVO.getPageNumber() : 1;
        int pageSize   = queryVO.getPageSize()   != null ? queryVO.getPageSize()   : 20;
        Page<ThreeTableCompare> page = new Page<>(pageNumber, pageSize);
        Page<ThreeTableCompare> result = this.page(page, wrapper);
        return PageResult.of(result);
    }

    @Override
    public ThreeTableCompareDetailVO getDetail(String id) {
        ThreeTableCompare record = this.getById(id);
        if (record == null) {
            throw new RuntimeException("记录不存在，id=" + id);
        }
        ThreeTableCompareDetailVO vo = new ThreeTableCompareDetailVO();
        vo.setId(record.getId());
        vo.setCompanyName(record.getCompanyName());
        vo.setDiffStatus(record.getDiffStatus());
        vo.setSuggestion(record.getSuggestion());
        vo.setDiffDetails(buildDiffDetails(record));
        return vo;
    }

    /**
     * 构建差异分析明细
     */
    private List<Map<String, Object>> buildDiffDetails(ThreeTableCompare r) {
        List<Map<String, Object>> details = new ArrayList<>();
        // 持股比例差异
        if (r.getRatioDiff() != null && r.getRatioDiff() == 1) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("dimension", "持股比例");
            item.put("propertyVal", r.getPropertyRatio() != null ? r.getPropertyRatio() + "%" : "-");
            item.put("bizVal",      r.getBizRatio()      != null ? r.getBizRatio()      + "%" : "-");
            item.put("financeVal",  r.getFinanceRatio()  != null ? r.getFinanceRatio()  + "%" : "-");
            item.put("desc", "三表持股比例不一致，请核查变更记录");
            details.add(item);
        }
        // 注册资本差异
        if (r.getCapitalDiff() != null && r.getCapitalDiff() == 1) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("dimension", "注册资本(万)");
            item.put("propertyVal", r.getPropertyCapital() != null ? r.getPropertyCapital().toString() : "-");
            item.put("bizVal",      r.getBizCapital()      != null ? r.getBizCapital().toString()      : "-");
            item.put("financeVal",  "-");
            item.put("desc", "产权登记与工商登记注册资本不一致，请核查增资情况");
            details.add(item);
        }
        // 登记状态差异
        if (r.getStatusDiff() != null && r.getStatusDiff() == 1) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("dimension", "登记/并表状态");
            item.put("propertyVal", r.getPropertyStatus() != null ? r.getPropertyStatus() : "-");
            item.put("bizVal",      r.getBizStatus()      != null ? r.getBizStatus()      : "-");
            item.put("financeVal",  r.getFinanceStatus()  != null ? r.getFinanceStatus()  : "-");
            item.put("desc", "三表登记状态不一致，请核查相关手续");
            details.add(item);
        }
        if (details.isEmpty()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("dimension", "综合比对");
            item.put("propertyVal", "一致");
            item.put("bizVal",      "一致");
            item.put("financeVal",  "一致");
            item.put("desc", "三表数据完全一致");
            details.add(item);
        }
        return details;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitDispatch(ThreeTableDispatchVO dispatchVO) {
        ThreeTableDispatch dispatch = new ThreeTableDispatch();
        dispatch.setCompareId(dispatchVO.getCompareId());
        dispatch.setCompanyName(dispatchVO.getCompanyName());
        dispatch.setOwner(dispatchVO.getOwner());
        dispatch.setRequirement(dispatchVO.getRequirement());
        dispatch.setStatus("PENDING");
        dispatch.setCreateTime(new Date());
        dispatch.setUpdateTime(new Date());
        // 解析日期字符串
        if (StringUtils.hasText(dispatchVO.getDeadline())) {
            try {
                dispatch.setDeadline(new SimpleDateFormat("yyyy-MM-dd").parse(dispatchVO.getDeadline()));
            } catch (ParseException e) {
                throw new RuntimeException("核查期限格式错误，请使用 yyyy-MM-dd 格式");
            }
        }
        dispatchMapper.insert(dispatch);
    }

    @Override
    public void exportDiffReport(ThreeTableCompareQueryVO queryVO, HttpServletResponse response) {
        // 查询全量数据（不分页）
        LambdaQueryWrapper<ThreeTableCompare> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryVO.getCompanyName())) {
            wrapper.like(ThreeTableCompare::getCompanyName, queryVO.getCompanyName());
        }
        if (StringUtils.hasText(queryVO.getDiffStatus())) {
            wrapper.eq(ThreeTableCompare::getDiffStatus, queryVO.getDiffStatus());
        }
        if (StringUtils.hasText(queryVO.getShareholderType())) {
            wrapper.eq(ThreeTableCompare::getShareholderType, queryVO.getShareholderType());
        }
        wrapper.orderByDesc(ThreeTableCompare::getCreateTime);
        List<ThreeTableCompare> list = this.list(wrapper);

        try (SXSSFWorkbook wb = new SXSSFWorkbook(100)) {
            SXSSFSheet sheet = wb.createSheet("三表比对差异报告");
            String[] headers = {
                "企业名称", "股东类型",
                "产权登记状态", "产权持股比例(%)", "产权注册资本(万)",
                "工商登记状态", "工商持股比例(%)", "工商注册资本(万)",
                "财务并表状态", "财务并表比例(%)",
                "差异状态", "处置建议", "创建时间"
            };
            SXSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < list.size(); i++) {
                ThreeTableCompare r = list.get(i);
                SXSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(nvl(r.getCompanyName()));
                row.createCell(1).setCellValue(mapShareholderType(r.getShareholderType()));
                row.createCell(2).setCellValue(nvl(r.getPropertyStatus()));
                row.createCell(3).setCellValue(r.getPropertyRatio() != null ? r.getPropertyRatio().toString() : "");
                row.createCell(4).setCellValue(r.getPropertyCapital() != null ? r.getPropertyCapital().toString() : "");
                row.createCell(5).setCellValue(nvl(r.getBizStatus()));
                row.createCell(6).setCellValue(r.getBizRatio() != null ? r.getBizRatio().toString() : "");
                row.createCell(7).setCellValue(r.getBizCapital() != null ? r.getBizCapital().toString() : "");
                row.createCell(8).setCellValue(nvl(r.getFinanceStatus()));
                row.createCell(9).setCellValue(r.getFinanceRatio() != null ? r.getFinanceRatio().toString() : "");
                row.createCell(10).setCellValue(mapDiffStatus(r.getDiffStatus()));
                row.createCell(11).setCellValue(nvl(r.getSuggestion()));
                row.createCell(12).setCellValue(r.getCreateTime() != null ? sdf.format(r.getCreateTime()) : "");
            }
            String filename = "三表比对差异报告_" + System.currentTimeMillis() + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("导出三表比对差异报告失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"result\":500,\"msg\":\"导出失败: " + e.getMessage() + "\"}");
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    private String nvl(String s) { return s != null ? s : ""; }

    private String mapShareholderType(String type) {
        if (type == null) return "";
        switch (type) {
            case "ENTERPRISE":  return "企业法人";
            case "INDIVIDUAL":  return "自然人";
            case "GOVERNMENT":  return "国有机构";
            case "INSTITUTION": return "事业单位";
            case "FUND":        return "基金";
            default:            return type;
        }
    }

    private String mapDiffStatus(String status) {
        if (status == null) return "";
        switch (status) {
            case "NONE":           return "无差异";
            case "DIFF":           return "有差异";
            case "ONLY_PROPERTY":  return "仅产权登记";
            case "ONLY_BUSI":      return "仅工商登记";
            default:               return status;
        }
    }
}
