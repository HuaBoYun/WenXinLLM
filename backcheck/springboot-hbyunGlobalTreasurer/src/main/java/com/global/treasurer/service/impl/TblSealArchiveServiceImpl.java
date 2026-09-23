package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TcSealArchive;
import com.global.treasurer.entity.TblSealUsageRecord;
import com.global.treasurer.mapper.TcSealArchiveMapper;
import com.global.treasurer.service.TblSealArchiveService;
import com.global.treasurer.service.TblSealUsageRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 印鉴档案Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-25
 */
@Service
public class TblSealArchiveServiceImpl extends ServiceImpl<TcSealArchiveMapper, TcSealArchive> implements TblSealArchiveService {
    private static final Logger log = LoggerFactory.getLogger(TblSealArchiveServiceImpl.class);

    @Autowired
    private TcSealArchiveMapper tcSealArchiveMapper;

    @Autowired
    private TblSealUsageRecordService tblSealUsageRecordService;

    /**
     * 分页查询印鉴档案 - 使用PageHelper分页
     */
    @Override
    public IPage<TcSealArchive> getSealArchivePage(Integer page, Integer limit,
            String sealCode, String sealName, Long sealTypeId, String ownerName, Integer isActive) {

        // 使用PageHelper分页
        PageHelper.startPage(page, limit);

        // 构建动态SQL条件
        StringBuilder condition = new StringBuilder();
        java.util.List<Object> params = new java.util.ArrayList<>();

        if (StringUtils.hasText(sealCode)) {
            condition.append(" AND SEAL_CODE LIKE ?");
            params.add("%" + sealCode + "%");
        }
        if (StringUtils.hasText(sealName)) {
            condition.append(" AND SEAL_NAME LIKE ?");
            params.add("%" + sealName + "%");
        }
        if (sealTypeId != null) {
            condition.append(" AND SEAL_TYPE_ID = ?");
            params.add(sealTypeId);
        }
        if (StringUtils.hasText(ownerName)) {
            condition.append(" AND OWNER_NAME LIKE ?");
            params.add("%" + ownerName + "%");
        }
        if (isActive != null) {
            condition.append(" AND STATUS = ?");
            params.add(isActive == 1 ? "1" : "0");
        }

        // 添加排序
        condition.append(" ORDER BY CREATE_TIME DESC");

        // 使用Mapper中已有的selectListWithCondition方法
        List<TcSealArchive> list = tcSealArchiveMapper.selectListWithCondition(
            condition.toString(),
            params.toArray()
        );

        // 使用PageInfo获取分页信息
        PageInfo<TcSealArchive> pageInfo = new PageInfo<>(list);

        // 转换为MyBatis-Plus的IPage对象返回
        Page<TcSealArchive> result = new Page<>(page, limit);
        result.setRecords(pageInfo.getList());
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        result.setCurrent(pageInfo.getPageNum());
        result.setSize(pageInfo.getPageSize());

        return result;
    }

    /**
     * 获取印鉴统计数据
     */
    @Override
    public Map<String, Object> getSealStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        try {
            // 总印鉴数
            long totalSeals = this.count();
            statistics.put("totalSeals", totalSeals);

            // 在用印鉴数
            QueryWrapper<TcSealArchive> activeWrapper = new QueryWrapper<>();
            activeWrapper.eq("STATUS", "1");
            long activeSeals = this.count(activeWrapper);
            statistics.put("activeSeals", activeSeals);

            // 锁定印鉴数
            QueryWrapper<TcSealArchive> lockedWrapper = new QueryWrapper<>();
            lockedWrapper.eq("STATUS", "0");
            long lockedSeals = this.count(lockedWrapper);
            statistics.put("lockedSeals", lockedSeals);

            // 今日使用次数 - 使用达梦数据库兼容的SQL
            try {
                QueryWrapper<TblSealUsageRecord> todayWrapper = new QueryWrapper<>();
                // 使用TRUNC函数，兼容达梦/Oracle数据库
                todayWrapper.apply("TRUNC(USAGE_TIME) = TRUNC(SYSDATE)");
                long todayUsages = tblSealUsageRecordService.count(todayWrapper);
                statistics.put("todayUsage", todayUsages);
            } catch (Exception e) {
                log.warn("查询今日使用次数失败", e);
                statistics.put("todayUsage", 0L);
            }
        } catch (Exception e) {
            log.error("获取印鉴统计数据失败", e);
            // 返回默认值
            statistics.put("totalSeals", 0L);
            statistics.put("activeSeals", 0L);
            statistics.put("lockedSeals", 0L);
            statistics.put("todayUsage", 0L);
        }

        return statistics;
    }
}
