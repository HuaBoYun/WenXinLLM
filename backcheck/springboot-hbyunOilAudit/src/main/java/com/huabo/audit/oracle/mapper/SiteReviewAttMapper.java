package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsSiteReviewAttEntity;
import org.apache.ibatis.annotations.Select;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-09 14:14
 **/
public interface SiteReviewAttMapper extends BaseMapper<TblYqnsSiteReviewAttEntity> {

    @Select("SELECT SITE_REVIEW_ATT_SEQUENCE.NEXTVAL FROM DUAL")
    Long getNextSequenceValue();

}
