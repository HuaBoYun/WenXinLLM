package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.compliance.mysql.entity.TblSystemModelFlowMySql;
import org.apache.ibatis.annotations.Insert;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
public interface TblSystemModelFlowMySqlMapper extends BaseMapper<TblSystemModelFlowMySql> {

    @Insert("INSERT INTO TBL_SYSTEM_MODELFLOW(MODELID,FLOWID) VALUES(#{modelId},#{flowId})")
    void insertFlow(TblSystemModelFlowMySql flow);


}
