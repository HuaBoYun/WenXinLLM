package com.huabo.finance.mapper;

import com.huabo.finance.entity.caiji.BdAccount;
import com.huabo.finance.mappersql.BdAccountMapperSqlConfig;
import com.huabo.finance.vo.BdAccountVo;
import com.huabo.finance.vo.ExportRequestVo;
import com.huabo.finance.vr.BdAccountVr;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.entity.TblAttachment;

/**
 * <p>
 * 会计科目基本信息 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-03-24
 */
public interface BdAccountMapper extends BaseMapper<BdAccount> {

	@SelectProvider(type = BdAccountMapperSqlConfig.class , method = "selectAllList")
	List<BdAccountVr> selectAllList(BdAccountVo vo, FaAccbookinfoUtil bookInfo) throws Exception;

	@SelectProvider(type = BdAccountMapperSqlConfig.class , method = "selectListByExport")
	List<BdAccountVr> selectListByExport(ExportRequestVo exportRequestVo) throws Exception;

	@Insert("INSERT INTO TBL_ATTACHMENT(ATTID, ATTNAME, ATTPATH, ATTSIZE, MEMO, UPLOADTIME, UPLOADER,JMURL,ISENCRYPTED)"
		       + " VALUES (#{attid}, #{attname}, #{attpath}, #{attsize}, NULL, #{uploadtime}, #{uploader}, #{jmurl},1)")
	void insertAttchment(TblAttachment attachment);

	@SelectProvider(type = BdAccountMapperSqlConfig.class , method = "selectFatherPkAccount")
	String selectFatherPkAccount(String pid, BdAccountVo vo, FaAccbookinfoUtil bookInfo) throws Exception;

}
