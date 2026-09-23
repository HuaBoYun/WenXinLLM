package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblMyTask;

import io.lettuce.core.dynamic.annotation.Param;

public interface TblMyTaskMapper extends tk.mybatis.mapper.common.Mapper<TblMyTask> {

	@InsertProvider(method="insertMyTaskSetting",type=TblMyTaskMapperSqlConfig.class)
	void insertMyTaskSetting(TblMyTask task) throws Exception;

	@Select("SELECT * FROM TBL_MY_TASK WHERE FROMID = #{formId}  order by ID")
	List<TblMyTask> selectByFormId(@Param("formId")String formId) throws Exception;

	@Select("SELECT * FROM TBL_MY_TASK WHERE FROMID = #{fromid} AND ROWNUM = 1 and ANALID IS NOT NULL ORDER BY TASKID desc")
	TblMyTask selectByFormIdReturnUnique(@Param("fromid") String fromid) throws Exception;

	@UpdateProvider(method = "updateSetting",type = TblMyTaskMapperSqlConfig.class)
    void updateSetting(TblMyTask task);

	@Select("SELECT * FROM TBL_MY_TASK TMT LEFT JOIN TBL_CONTRACT_COLLECTION TCC ON TMT.FROMID = TCC.COLLECTIONID WHERE TCC.COLLECTIONID = #{processName}  order by ID")
    List<TblMyTask> getByFromid(String processName);

	@Select("SELECT * from TBL_MY_TASK t where t.FROMID = #{lendid} order by t.id")
	List<TblMyTask> findByLendid(String lendid);

	@Select("SELECT * FROM TBL_MY_TASK WHERE FROMID=#{fromid} and ANALID IS NOT NULL ORDER BY TASKID desc")
	List<TblMyTask> findOndbyFrom(String fromid);
	
	@SelectProvider(method="findByObj",type=TblMyTaskMapperSqlConfig.class)
	List<TblMyTask> findByObj(String fromid,String status,String staffid,String type) ;
	
	@Insert("INSERT INTO TBL_MY_TASK_ATT(ID,ATTID) VALUES(#{id},#{attId})")
	void insertAttmentRelation(@Param("attId")String attId, @Param("id")Integer id);
	
	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_MY_TASK_ATT WHERE ID = #{id})")
    List<TblAttachment> findAttachmentListByID(Integer id);
    
    @Delete("DELETE FROM TBL_MY_TASK_ATT WHERE ID=#{id}")
	void deleteAttmentRelationByBizId(@Param("id")Integer id);
    
    @Delete("DELETE FROM TBL_MY_TASK_ATT WHERE ATTID=#{attId}")
	void deleteAttmentRelationByAttid(@Param("attId")Integer attId);
}
