package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillMaturityProcessDTO;
import com.global.treasurer.dto.BillMaturityQueryDTO;
import com.global.treasurer.entity.TblBillMaturity;
import com.global.treasurer.mapper.BillMaturityMapper;
import com.global.treasurer.service.IBillMaturityService;
import com.global.treasurer.vo.BillMaturityVO;
import com.hbfk.util.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 票据到期Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Service
public class BillMaturityServiceImpl extends ServiceImpl<BillMaturityMapper, TblBillMaturity>
        implements IBillMaturityService {

    private static final Logger log = LoggerFactory.getLogger(BillMaturityServiceImpl.class);

    @Override
    public PageInfo<BillMaturityVO> selectBillMaturityList(BillMaturityQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<BillMaturityVO> list = baseMapper.selectBillMaturityList(queryDTO);
        return new PageInfo<>(list);
    }

    @Override
    public BillMaturityVO selectBillMaturityById(Long maturityId) {
        if (maturityId == null) {
            throw new BizException("到期记录ID不能为空");
        }
        return baseMapper.selectBillMaturityById(maturityId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processBillMaturity(BillMaturityProcessDTO dto) {
        if (dto.getMaturityIds() == null || dto.getMaturityIds().isEmpty()) {
            throw new BizException("请选择要处理的到期记录");
        }
        Date processDate = dto.getProcessDate() != null ? dto.getProcessDate() : new Date();
        int result = baseMapper.batchUpdateProcessStatus(
                dto.getMaturityIds(),
                dto.getProcessType(),
                processDate,
                dto.getProcessDescription()
        );
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendMaturityReminder(List<Long> maturityIds) {
        if (maturityIds == null || maturityIds.isEmpty()) {
            throw new BizException("请选择要发送提醒的到期记录");
        }
        // 更新提醒状态
        int result = baseMapper.batchUpdateReminderStatus(maturityIds, "SENT");
        // TODO: 实际发送提醒通知(邮件、短信、站内信等)
        log.info("发送到期提醒通知，记录数: {}", maturityIds.size());
        return result > 0;
    }

    @Override
    public List<Map<String, Object>> getMaturityCalendarData(Map<String, Object> params) {
        Date startDate = parseDate(params.get("startDate"));
        Date endDate = parseDate(params.get("endDate"));
        Long companyId = params.get("companyId") != null ? Long.valueOf(params.get("companyId").toString()) : null;
        
        if (startDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            startDate = cal.getTime();
        }
        if (endDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 0);
            endDate = cal.getTime();
        }
        return baseMapper.selectMaturityCalendarData(startDate, endDate, companyId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRemainingDays() {
        baseMapper.updateAllRemainingDays();
        log.info("更新票据剩余天数完成");
    }

    @Override
    public Map<String, Object> getBillMaturityStatistics(BillMaturityQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new BillMaturityQueryDTO();
        }
        Map<String, Object> dbResult = baseMapper.selectBillMaturityStatistics(queryDTO);

        // 转换字段名：达梦数据库返回大写字段名，需要转换为前端期望的驼峰命名
        Map<String, Object> statistics = new HashMap<>();
        if (dbResult != null) {
            // 兼容大写和驼峰两种格式
            statistics.put("totalBills", getValueIgnoreCase(dbResult, "totalBills", "TOTALBILLS", 0));
            statistics.put("expiringBills", getValueIgnoreCase(dbResult, "expiringBills", "EXPIRINGBILLS", 0));
            statistics.put("maturedBills", getValueIgnoreCase(dbResult, "maturedBills", "MATUREDBILLS", 0));
            statistics.put("totalAmount", getValueIgnoreCase(dbResult, "totalAmount", "TOTALAMOUNT", 0));
        } else {
            statistics.put("totalBills", 0);
            statistics.put("expiringBills", 0);
            statistics.put("maturedBills", 0);
            statistics.put("totalAmount", 0);
        }
        return statistics;
    }

    /**
     * 从Map中获取值，兼容大小写
     */
    private Object getValueIgnoreCase(Map<String, Object> map, String camelKey, String upperKey, Object defaultValue) {
        if (map.containsKey(camelKey)) {
            return map.get(camelKey) != null ? map.get(camelKey) : defaultValue;
        }
        if (map.containsKey(upperKey)) {
            return map.get(upperKey) != null ? map.get(upperKey) : defaultValue;
        }
        return defaultValue;
    }

    @Override
    public Map<String, Object> getMaturityTrendData(Map<String, Object> params) {
        // 获取天数参数，默认30天
        int days = 30;
        if (params != null && params.get("days") != null) {
            days = Integer.parseInt(params.get("days").toString());
        }

        // 生成日期标签
        List<String> labels = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("M/d");
        Calendar cal = Calendar.getInstance();

        for (int i = days - 1; i >= 0; i--) {
            Calendar tempCal = Calendar.getInstance();
            tempCal.add(Calendar.DAY_OF_MONTH, -i);
            labels.add(sdf.format(tempCal.getTime()));
        }

        // 查询趋势数据
        cal.add(Calendar.DAY_OF_MONTH, -(days - 1));
        Date startDate = cal.getTime();
        Date endDate = new Date();

        List<Map<String, Object>> trendList = baseMapper.selectMaturityTrendData(startDate, endDate);

        // 构建日期到数据的映射
        Map<String, Map<String, Object>> dateDataMap = new HashMap<>();
        SimpleDateFormat queryDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (trendList != null) {
            for (Map<String, Object> item : trendList) {
                // 兼容大小写：达梦数据库返回大写字段名
                Object dateObj = getValueIgnoreCase(item, "maturityDate", "MATURITYDATE", null);
                if (dateObj == null) {
                    dateObj = getValueIgnoreCase(item, "maturity_date", "MATURITY_DATE", null);
                }
                if (dateObj != null) {
                    String dateKey;
                    if (dateObj instanceof Date) {
                        dateKey = queryDateFormat.format((Date) dateObj);
                    } else {
                        dateKey = dateObj.toString().substring(0, 10);
                    }
                    dateDataMap.put(dateKey, item);
                }
            }
        }

        // 填充数据数组
        List<Integer> countData = new ArrayList<>();
        List<Double> amountData = new ArrayList<>();

        for (int i = days - 1; i >= 0; i--) {
            Calendar tempCal = Calendar.getInstance();
            tempCal.add(Calendar.DAY_OF_MONTH, -i);
            String dateKey = queryDateFormat.format(tempCal.getTime());

            Map<String, Object> dayData = dateDataMap.get(dateKey);
            if (dayData != null) {
                // 兼容大小写
                Object countObj = getValueIgnoreCase(dayData, "count", "COUNT", 0);
                Object amountObj = getValueIgnoreCase(dayData, "totalAmount", "TOTALAMOUNT", 0);
                countData.add(countObj != null ? ((Number) countObj).intValue() : 0);
                amountData.add(amountObj != null ? ((Number) amountObj).doubleValue() / 10000 : 0.0);
            } else {
                countData.add(0);
                amountData.add(0.0);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("countData", countData);
        result.put("amountData", amountData);

        return result;
    }

    private Date parseDate(Object dateObj) {
        if (dateObj == null) return null;
        if (dateObj instanceof Date) return (Date) dateObj;
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateObj.toString());
        } catch (ParseException e) {
            return null;
        }
    }
}

