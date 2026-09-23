package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.entity.TblTravelStandard;
import com.financial.sharing.entity.TblTravelStandardDetail;
import com.financial.sharing.mapper.TblTravelStandardMapper;
import com.financial.sharing.mapper.TblTravelStandardDetailMapper;
import com.financial.sharing.service.TblTravelStandardService;
import com.financial.sharing.service.TblTravelStandardDetailService;
import com.financial.sharing.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 差旅标准Service实现
 */
@Slf4j
@Service
public class TblTravelStandardServiceImpl extends ServiceImpl<TblTravelStandardMapper, TblTravelStandard> 
        implements TblTravelStandardService {

    @Autowired
    private TblTravelStandardDetailMapper detailMapper;
    
    @Autowired
    private TblTravelStandardDetailService detailService;

    @Override
    public PageResult<TblTravelStandard> getTravelStandardPage(Map<String, Object> params) {
        try {
            int pageNum = params.containsKey("pageNum") ? (int) params.get("pageNum") : 1;
            int pageSize = params.containsKey("pageSize") ? (int) params.get("pageSize") : 15;

            QueryWrapper<TblTravelStandard> queryWrapper = new QueryWrapper<>();

            // 只在参数不为空且不为空字符串时添加查询条件
            if (params.containsKey("standardName") && params.get("standardName") != null
                && !params.get("standardName").toString().trim().isEmpty()) {
                queryWrapper.like("STANDARD_NAME", params.get("standardName"));
            }
            if (params.containsKey("cityLevel") && params.get("cityLevel") != null
                && !params.get("cityLevel").toString().trim().isEmpty()) {
                queryWrapper.eq("CITY_LEVEL", params.get("cityLevel"));
            }
            if (params.containsKey("positionLevel") && params.get("positionLevel") != null
                && !params.get("positionLevel").toString().trim().isEmpty()) {
                queryWrapper.eq("POSITION_LEVEL", params.get("positionLevel"));
            }
            if (params.containsKey("isEnabled") && params.get("isEnabled") != null) {
                queryWrapper.eq("IS_ENABLED", params.get("isEnabled"));
            }

            queryWrapper.orderByDesc("CREATE_TIME");

            long total = this.count(queryWrapper);
            List<TblTravelStandard> list = this.page(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize),
                    queryWrapper
            ).getRecords();

            PageResult<TblTravelStandard> result = new PageResult<>();
            result.setTlist(list);
            result.setTotalRecord((int) total);
            result.setCurrentPage(pageNum);
            result.setPageSize(pageSize);

            return result;
        } catch (Exception e) {
            log.error("查询差旅标准分页数据失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage());
        }
    }

    @Override
    public TblTravelStandard getByStandardCode(String standardCode) {
        return this.baseMapper.selectByStandardCode(standardCode);
    }

    @Override
    public List<TblTravelStandard> getByCityAndPosition(String cityLevel, String positionLevel) {
        return this.baseMapper.selectByCityAndPosition(cityLevel, positionLevel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStandardStatus(String standardId, Integer isEnabled) {
        try {
            TblTravelStandard standard = this.getById(standardId);
            if (standard == null) {
                throw new RuntimeException("标准不存在");
            }
            standard.setIsEnabled(isEnabled);
            standard.setUpdateTime(LocalDateTime.now());
            return this.updateById(standard);
        } catch (Exception e) {
            log.error("更新标准状态失败", e);
            throw new RuntimeException("更新失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String copyStandard(String standardId, String newStandardCode, String newStandardName) {
        try {
            TblTravelStandard original = this.getById(standardId);
            if (original == null) {
                throw new RuntimeException("原标准不存在");
            }
            
            // 创建新标准
            TblTravelStandard newStandard = new TblTravelStandard();
            newStandard.setStandardId(UUID.randomUUID().toString());
            newStandard.setStandardCode(newStandardCode);
            newStandard.setStandardName(newStandardName);
            newStandard.setCityLevel(original.getCityLevel());
            newStandard.setPositionLevel(original.getPositionLevel());
            newStandard.setStandardType(original.getStandardType());
            newStandard.setEffectiveDate(original.getEffectiveDate());
            newStandard.setExpiryDate(original.getExpiryDate());
            newStandard.setIsEnabled(1);
            newStandard.setCreateTime(LocalDateTime.now());
            
            this.save(newStandard);
            
            // 复制明细
            List<TblTravelStandardDetail> details = detailMapper.selectByStandardId(standardId);
            for (TblTravelStandardDetail detail : details) {
                detail.setDetailId(UUID.randomUUID().toString());
                detail.setStandardId(newStandard.getStandardId());
                detail.setCreateTime(LocalDateTime.now());
            }
            detailService.saveBatchDetails(details);
            
            return newStandard.getStandardId();
        } catch (Exception e) {
            log.error("复制标准失败", e);
            throw new RuntimeException("复制失败: " + e.getMessage());
        }
    }
}

