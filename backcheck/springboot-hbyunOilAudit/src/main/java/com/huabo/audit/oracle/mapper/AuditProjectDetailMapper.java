package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsAuditProjectDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-08 09:32
 **/
@Mapper
public interface AuditProjectDetailMapper extends BaseMapper<TblYqnsAuditProjectDetailEntity> {

    @Select("SELECT AUDIT_PROJECT_DETAIL_SEQUENCE.NEXTVAL FROM DUAL")
    Long getNextSequenceValue();

}
