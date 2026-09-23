package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblSystemModelFlow;

import org.apache.ibatis.annotations.Insert;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
public interface TblSystemModelFlowMapper extends BaseMapper<TblSystemModelFlow> {

    @Insert("INSERT INTO TBL_SYSTEM_MODELFLOW(MODELID,FLOWID) VALUES(#{modelId},#{flowId})")
    void insertFlow(TblSystemModelFlow flow);


}
