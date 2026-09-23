package com.huabo.legal.oracle.mapper;

import com.hbfk.entity.TblAttachment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface TblAttachmentMapper {

	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_FWGL_LEARRNING_ATT  WHERE LINGID = #{lingid})")
	List<TblAttachment> selectAttListBylingid(Long lingid);

	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_FWGL_NOTICE_ATT  WHERE NOTICEID = #{noticeid})")
	List<TblAttachment> selectAttListBynoticeid(Long noticeid);

	@Insert("INSERT INTO TBL_FWGL_LEARRNING_ATT(LINGID,ATTID) VALUES(#{lingid},#{attid})")
	void insertAttmentling(Long lingid, String attid);

	@Insert("INSERT INTO TBL_FWGL_NOTICE_ATT(NOTICEID,ATTID) VALUES(#{noticeid},#{attid})")
	void insertAttmentnotice(Long noticeid, String attid);

	@Delete("DELETE FROM TBL_FWGL_LEARRNING_ATT WHERE LINGID=#{lingid}")
	void deleteAttmentling(Long lingid);

	@Delete("DELETE FROM TBL_FWGL_NOTICE_ATT WHERE NOTICEID=#{noticeid}")
	void deleteAttmennotice(Long noticeid);

	@Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID=#{attid}")
	void deleteAttmentattid(Long attid);


}
