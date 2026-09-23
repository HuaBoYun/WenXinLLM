package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsAuditProjectEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-08 09:30
 **/
@Mapper
public interface AuditProjectMapper extends BaseMapper<TblYqnsAuditProjectEntity> {

    @Select("SELECT AUDIT_PROJECT_SEQUENCE.NEXTVAL FROM DUAL")
    Long getNextSequenceValue();

}
