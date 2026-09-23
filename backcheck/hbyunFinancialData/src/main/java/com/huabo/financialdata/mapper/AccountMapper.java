package com.huabo.financialdata.mapper;

import com.hbfk.entity.TblAttachment;
import com.huabo.financialdata.entity.entity.Account;
import com.huabo.financialdata.entity.entity.AccountQuery;
import com.huabo.financialdata.entity.vo.diaryBook.DiaryBookRequestVo;
import com.huabo.financialdata.entity.vo.export.ExportRequestVo;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 会计科目表
 *
 * @author lee
 * @version 1.0.0
 **/
public interface AccountMapper{

    /**
     * 定制sql多条件查询
     *
     * @param example 查询条件
     * @return 返回结果
     */
    List<Account> selectByCondition(AccountQuery example);

    //获取accName
    String selectAccName(DiaryBookRequestVo diaryBookRequestVo);

	List<Account> selectListByExport(ExportRequestVo exportRequestVo) ;

	BigDecimal selectNextPrimaryKey();

	@Insert("INSERT INTO TBL_ATTACHMENT(ATTID, ATTNAME, ATTPATH, ATTSIZE, MEMO, UPLOADTIME, UPLOADER,JMURL,ISENCRYPTED)"
	       + " VALUES (#{att.attid}, #{att.attname}, #{att.attpath}, #{att.attsize}, NULL, #{att.uploadtime}, #{att.uploader},#{att.jmurl},#{att.isencrypted})")
	void insertAttchment(@Param("att")TblAttachment att);
	
	@Select("select * from TBL_ATTACHMENT where attid=#{id}")
	TblAttachment getOne(String attid);

}
