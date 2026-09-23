package com.huabo.system.mapper;


import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblMyTask;
	
/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-28
 */
@Mapper
public interface TblMyTaskMapper extends BaseMapper<TblMyTask> {

	@InsertProvider(method="insertMyTaskSetting",type=TblMyTaskMapperSqlConfig.class)
	void insertMyTaskSetting(TblMyTask task) throws Exception;

	@Select("SELECT * FROM TBL_MY_TASK WHERE FROMID = #{formId} order by ID")
	List<TblMyTask> selectByFormId(@Param("formId")String formId) throws Exception;

	@Select("SELECT * FROM TBL_MY_TASK WHERE FROMID = #{fromid} AND ROWNUM = 1 ORDER BY TASKID desc")
	TblMyTask selectByFormIdReturnUnique(@Param("fromid") String fromid) throws Exception;

	@UpdateProvider(method = "updateSetting",type = TblMyTaskMapperSqlConfig.class)
    void updateSetting(TblMyTask task);

	@Select("SELECT * FROM TBL_MY_TASK TMT LEFT JOIN TBL_CONTRACT_COLLECTION TCC ON TMT.FROMID = TCC.COLLECTIONID WHERE TCC.COLLECTIONID = #{processName}  order by ID")
    List<TblMyTask> getByFromid(String processName);

	@Select("SELECT * from TBL_MY_TASK t where t.FROMID = #{lendid}  order by t.id")
	List<TblMyTask> findByLendid(String lendid);

	@Select("SELECT * FROM TBL_MY_TASK WHERE FROMID=#{fromid} ORDER BY TASKID desc")
	List<TblMyTask> findOndbyFrom(String fromid);
	
	@SelectProvider(method="findByObj",type=TblMyTaskMapperSqlConfig.class)
	List<TblMyTask> findByObj(String fromid, String status, String staffid, String type) ;
}
