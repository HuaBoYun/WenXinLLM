package com.huabo.audit.oracle.mapper;

import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblYqnsAuditMyManuVerifyEntity;

/**
 * @Classname TblYqnsAuditMyManuscriptMapper
 * @Description 央企内审-审计实施-我的底稿-下方审计查证事实
 * @Date 2023/10/18 10:16
 * @Created by GJ.C
 */
public interface TblYqnsAuditMyManuVerifyMapper extends BaseMapper<TblYqnsAuditMyManuVerifyEntity> {
	
	 @Update("UPDATE TBL_YQNS_AUDIT_MY_MANU_VERIFY SET  STATUS=0  WHERE MYMANUSCRIPTID=#{dgid} AND STATUS=1 ")
	 void updateByxmner(String dgid) throws Exception;

}