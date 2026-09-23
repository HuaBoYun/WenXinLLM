package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsMyTaskReviewEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * @Classname TblYqnsMyTaskReviewMapper
 * @Description TODO 央企内审-审计实施-我的任务-审查
 * @Date  2023/10/28 14:40
 * @Created by GJ.C
 */
public interface TblYqnsMyTaskReviewMapper extends BaseMapper<TblYqnsMyTaskReviewEntity> {

//    @Select(" SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER " +
//            " FROM TBL_ATTACHMENT WHERE" +
//            " ATTID IN (SELECT ATTID FROM TBL_YQNS_MY_TASK_REVIEW_ATTACH " +
//            " WHERE REVIEWID = #{id})")
    @SelectProvider(type = TblYqnsMyTaskReviewMapperSqlConfig.class,method="selectAttachmentListByPk")
    List<TblAttachment> selectAttachmentListByPk(String id,String attids);

    @Delete("DELETE FROM TBL_YQNS_MY_TASK_REVIEW_ATTACH WHERE REVIEWID = #{id}") 
    void deleteAttByPk(String id);

    @Delete("DELETE FROM TBL_YQNS_MY_TASK_REVIEW_ATTACH WHERE ATTID = #{attid}")
    void deleteAttById(String attid);

    @Insert("INSERT INTO TBL_YQNS_MY_TASK_REVIEW_ATTACH(REVIEWID,ATTID) VALUES(#{id},#{attid})")
    void saveAtt(String id, String attid);
    
    @SelectProvider(type = TblYqnsMyTaskReviewMapperSqlConfig.class,method="selectListByVo")
	List<TblYqnsMyTaskReviewEntity> selectListByVo(TblYqnsMyTaskReviewEntity vo, TblStaffUtil user, boolean totalFlag) throws Exception;
}