package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.oracle.dto.EntermeetingListSearchDto;
import com.huabo.audit.oracle.entity.TblNbsjEntermeetingEntity;
import com.huabo.audit.oracle.vo.EntermeetingListSearchVO;
import com.huabo.audit.oracle.vo.PlanManageVO;
import com.huabo.audit.oracle.vo.TblNbsjEntermeetingVo;
import com.huabo.audit.util.PageInfo;

public interface TblNbsjEntermeetingMapper extends BaseMapper<TblNbsjEntermeetingEntity>{

//	List<TblNbsjEntermeetingEntity> approachList(ExecPhraseVo vo);
	
	@SelectProvider(method="planManagePageList",type=TblNbsjEntermeetingMapperSqlConfig.class)
    List<TblNbsjEntermeetingEntity> planManagePageList(PageInfo<TblNbsjEntermeetingEntity> pageInfo, PlanManageVO planManageVO);
    
    @Select("SELECT COUNT(*) FROM  TBL_NBSJ_ENTERMEETING WHERE AUDITORGID = #{origid}")
    Integer planManagePageListCount(String orgid);
    
    @Select("SELECT * from TBL_NBSJ_ENTERMEETING WHERE ENTERID= #{enterid} ")
    TblNbsjEntermeetingEntity getById(String enterid);
    
    @Select("SELECT * FROM TBL_NBSJ_ENTERMEETING WHERE  PLANCODE  = #{plan.plancode} and  AUDITORGID = #{plan.orgid}")
    TblNbsjEntermeetingEntity  validPlanCode(@Param("plan") PlanManageVO planManageVO);
    
    @SelectProvider(method="auditPlanListSearch",type=TblNbsjEntermeetingMapperSqlConfig.class)
    List<TblNbsjEntermeetingEntity> auditPlanListSearch(PageInfo<EntermeetingListSearchVO> pageInfo, TblStaffUtil tblStaffUtil, EntermeetingListSearchDto auditPlanListSearchDTO);

    @SelectProvider(method="selectNbsjEntermeetingCountByPageInfo",type=TblNbsjEntermeetingMapperSqlConfig.class)
   	Integer selectNbsjEntermeetingCountByPageInfo(PageInfo<TblNbsjEntermeetingEntity> pageInfo,TblNbsjEntermeetingVo tblNbsjEntermeetingVo) throws Exception;

    @Select("SELECT TNA.* FROM TBL_NBSJ_ENTERMEETING TNA  WHERE TNA.ENTERID = #{enterid}")
    @Results({
    	@Result(column="ENTERID",property="enterid"),
    	@Result(column="ENTERCOED",property="entercoed"),
    	@Result(column="ENTERNAME",property="entername"),
    	@Result(column="CREATRTIME",property="creatrtime"),
    	@Result(column="CREATESTAFFID",property="createstaffid"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="CONTENT",property="content"),
    })
   	TblNbsjEntermeetingEntity selectNbsjEntermeetingEntityById(@Param("enterid") Integer enterid) throws Exception;
    
    @SelectProvider(method="selectNbsjEntermeetingListByPageInfo",type=TblNbsjEntermeetingMapperSqlConfig.class)
    @Results({
    	@Result(column="ENTERID",property="enterid"),
    	@Result(column="ENTERCOED",property="entercoed"),
    	@Result(column="ENTERNAME",property="entername"),
    	@Result(column="CREATRTIME",property="creatrtime"),
    	@Result(column="CREATESTAFFID",property="createstaffid"),
    	@Result(column="STATUS",property="status"),
    	@Result(column="CONTENT",property="content"),
    })
	List<TblNbsjEntermeetingEntity> selectNbsjEntermeetingListByPageInfo(PageInfo<TblNbsjEntermeetingEntity> pageInfo,TblNbsjEntermeetingVo tblNbsjEntermeetingVo) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_ENTERMEETING WHERE ENTERID = #{enterid}")
    void deleteEntermeetingEntityById(Integer enterid) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjEntermeetingMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblNbsjEntermeetingEntity plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblNbsjEntermeetingMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="enterid", keyColumn="ENTERID")
	void insertEntity(TblNbsjEntermeetingEntity plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblNbsjEntermeetingMapperSqlConfig.class)
	void updateEntity(TblNbsjEntermeetingEntity plan) throws Exception;
    
    @Update("UPDATE TBL_NBSJ_ENTERMEETING SET STATUS='1' WHERE ENTERID = #{enterid}")
    void calcelEntermeetingEntityById(Integer enterid) throws Exception;

	
    @Delete("DELETE FROM TBL_LEGAL_ENTERMEETING_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(Integer attid);

}
