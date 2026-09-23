package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsEnginProjectAttEntity;
import org.apache.ibatis.annotations.Select;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-14 10:44
 **/
public interface EnginProjectAttMapper extends BaseMapper<TblYqnsEnginProjectAttEntity> {

    @Select("SELECT ENGIN_PROJECT_ATT_SEQUENCE.NEXTVAL FROM DUAL")
    Long getNextSequenceValue();

}
