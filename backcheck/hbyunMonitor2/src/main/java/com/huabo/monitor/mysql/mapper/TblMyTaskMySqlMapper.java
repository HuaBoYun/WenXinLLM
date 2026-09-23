package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.mysql.entity.TblMyTaskMySql;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-28
 */
@Mapper
public interface TblMyTaskMySqlMapper extends BaseMapper<TblMyTaskMySql> {

    @InsertProvider(method = "insertMyTaskSetting", type = TblMyTaskMapperSqlMySqlConfig.class)
    void insertMyTaskSetting(TblMyTaskMySql task) throws Exception;

    @Select("SELECT * FROM TBL_MY_TASK WHERE FROMID = #{formId} order by ID")
    List<TblMyTaskMySql> selectByFormId(@Param("formId") String formId) throws Exception;

    @Select("SELECT * FROM TBL_MY_TASK WHERE FROMID = #{fromid} AND ROWNUM = 1 ORDER BY TASKID desc")
    TblMyTaskMySql selectByFormIdReturnUnique(@Param("fromid") String fromid) throws Exception;

    @UpdateProvider(method = "updateSetting", type = TblMyTaskMapperSqlMySqlConfig.class)
    void updateSetting(TblMyTaskMySql task);

    @Select("SELECT * FROM TBL_MY_TASK TMT LEFT JOIN TBL_CONTRACT_COLLECTION TCC ON TMT.FROMID = TCC.COLLECTIONID WHERE TCC.COLLECTIONID = #{processName}  order by ID")
    List<TblMyTaskMySql> getByFromid(String processName);

    @Select("SELECT * from TBL_MY_TASK t where t.FROMID = #{lendid}  order by t.id")
    List<TblMyTaskMySql> findByLendid(String lendid);

    @Select("SELECT * FROM TBL_MY_TASK WHERE FROMID=#{fromid} ORDER BY TASKID desc")
    List<TblMyTaskMySql> findOndbyFrom(String fromid);

    @SelectProvider(method = "findByObj", type = TblMyTaskMapperSqlMySqlConfig.class)
    List<TblMyTaskMySql> findByObj(String fromid, String status, String staffid, String type);
}
