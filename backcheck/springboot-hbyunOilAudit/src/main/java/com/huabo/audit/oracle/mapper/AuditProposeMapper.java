package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsProposeEntity;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @ Author: Striker dev@example.com
 * @ Date: 2023-09-07 19:30
 * @ TODO:
 **/
public interface AuditProposeMapper extends BaseMapper<TblYqnsProposeEntity> {
    @Select("SELECT PROPOSE_SEQUENCE.NEXTVAL FROM DUAL")
    Long getNextSequenceValue();

    @Select("SELECT TYP.ID,TYP.HIERARCHYTYPE,TYP.BUSINESSTYPE,TYP.TITLE,TYP.DRAFTIDSTRS,TYP.DETAILS,TYPA.ADOPTID,TYPA.WTZGID,TYPA.ISADOPT,TYPA.ZJJJCGLX,TYPA.ZJJJCGJE,TYPA.QTJJCGJE,TYPA.QTJJCGJE,TYPA.NOTREASON,TYPA.WTZGID " + 
    		"FROM TBL_YQNS_PROPOSE TYP LEFT JOIN TBL_YQNS_PROPOSE_ADOPT TYPA ON TYP.ID = TYPA.ID AND WTZGID = #{wtzgid} WHERE SJBGDGID = #{sjbgdgid}")
	List<TblYqnsProposeEntity> selectProposeAdoptList(@Param("sjbgdgid")Long sjbgdgid,@Param("wtzgid") BigDecimal wtzgid);

}
