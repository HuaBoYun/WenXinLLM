package com.financial.sharing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.entity.TblTravelStandard;
import com.financial.sharing.util.PageResult;
import java.util.List;
import java.util.Map;

/**
 * 差旅标准Service接口
 */
public interface TblTravelStandardService extends IService<TblTravelStandard> {
    
    /**
     * 分页查询差旅标准
     */
    PageResult<TblTravelStandard> getTravelStandardPage(Map<String, Object> params);
    
    /**
     * 根据标准编码查询
     */
    TblTravelStandard getByStandardCode(String standardCode);
    
    /**
     * 根据城市级别和职位级别查询
     */
    List<TblTravelStandard> getByCityAndPosition(String cityLevel, String positionLevel);
    
    /**
     * 更新标准状态
     */
    boolean updateStandardStatus(String standardId, Integer isEnabled);
    
    /**
     * 复制标准
     */
    String copyStandard(String standardId, String newStandardCode, String newStandardName);
}

