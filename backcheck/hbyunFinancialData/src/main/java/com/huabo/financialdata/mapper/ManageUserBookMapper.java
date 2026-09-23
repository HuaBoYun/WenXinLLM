package com.huabo.financialdata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.financialdata.entity.entity.ManageUserBook;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * @author lee
 * @version 1.0.0
 **/
public interface ManageUserBookMapper extends BaseMapper<ManageUserBook> {


	@Update("UPDATE TBL_MANAGE_USER_BOOK SET  STATUS=null WHERE bookid = #{bookid} and staffid=#{staffid}")
    void updatestatus(BigDecimal staffid, String bookid, Integer status);

    @Update("UPDATE TBL_MANAGE_USER_BOOK SET  STATUS=#{status} WHERE bookid = #{bookid} and staffid=#{staffid}")
    void updatestatusone(BigDecimal staffid, String bookid, Integer status);

    @Update("UPDATE TBL_MANAGE_USER_BOOK SET STATUS = NULL WHERE staffid=#{staffid}")
	void updateBookStatusNoCheck(@Param("staffid") BigDecimal staffid, @Param("status") Integer status);
}
