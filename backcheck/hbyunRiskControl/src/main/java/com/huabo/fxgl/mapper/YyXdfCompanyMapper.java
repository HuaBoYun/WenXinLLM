package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.YyXdfCompany;
import org.apache.ibatis.annotations.Mapper;

/**
 * 企业监控信息Mapper接口
 */
@Mapper
public interface YyXdfCompanyMapper extends BaseMapper<YyXdfCompany> {
    
    /**
     * 根据企业ID（统一社会信用代码）查询企业
     * @param companyid 企业ID
     * @return 企业信息
     */
    default YyXdfCompany selectByCompanyId(String companyid) {
        return selectById(companyid);
    }
    
    /**
     * 根据企业名称查询企业
     * @param companyname 企业名称
     * @return 企业信息
     */
    default YyXdfCompany selectByCompanyName(String companyname) {
        return null; // 需要自定义SQL实现
    }
}
