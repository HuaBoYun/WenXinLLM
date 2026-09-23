package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsSiteReviewContentEntity;
import org.apache.ibatis.annotations.Select;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-09 14:13
 **/
public interface SiteReviewContentMapper extends BaseMapper<TblYqnsSiteReviewContentEntity> {

    @Select("SELECT SITE_REVIEW_CONTENT_SEQUENCE.NEXTVAL FROM DUAL")
    Long getNextSequenceValue();

}
