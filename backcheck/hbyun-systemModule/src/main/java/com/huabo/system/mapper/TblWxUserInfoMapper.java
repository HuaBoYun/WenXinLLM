package com.huabo.system.mapper;


import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblWxUserInfo;
	
/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-28
 */
@Mapper
public interface TblWxUserInfoMapper extends BaseMapper<TblWxUserInfo> {

	@Select("SELECT TWI.USERID,TWI.AVATARURL,TWI.GENDER,TWI.NICKNAME,TWI.XCXOPENID,TWI.UNIONID,TWI.STAFFID,TSF.REALNAME,TSF.EMAIL,TSF.MIBLEPHONE,TSF.ORGID FROM TBL_WXUSER_INFO TWI LEFT JOIN TBL_STAFF TSF ON TSF.STAFFID = TWI.STAFFID WHERE TWI.UNIONID = #{unionId}")
	@Results({
		@Result(column="USERID",property="userId"),
		@Result(column="AVATARURL",property="avatarUrl"),
		@Result(column="GENDER",property="gender"),
		@Result(column="NICKNAME",property="nickName"),
		@Result(column="XCXOPENID",property="xcxOpenId"),
		@Result(column="UNIONID",property="unionId"),
		@Result(column="STAFFID",property="staffId"),
		@Result(column="REALNAME",property="realName"),
		@Result(column="EMAIL",property="email"),
		@Result(column="MIBLEPHONE",property="miblePhone"),
		@Result(column="ORGID",property="orgId"),
	})
	TblWxUserInfo getuser(@Param("unionId")String unionId) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_WXUSER_INFO WHERE UNIONID = #{unionId}")
	Integer findCountByUserUnionId(@Param("unionId")String unionId);

	@InsertProvider(type = TblWxUserInfoSqlConfig.class,method = "insertWxUserInfoReturnUserId")
	@Options(useGeneratedKeys=true, keyProperty="userId", keyColumn="USERID")
	void insertWxUserInfoReturnUserId(TblWxUserInfo userInfo);
	
	@Update("UPDATE TBL_WXUSER_INFO SET AVATARURL = #{avatarUrl} WHERE UNIONID = #{unionId}")
	void updateAvatarUrl(TblWxUserInfo userInfo) throws Exception;

	@InsertProvider(type = TblWxUserInfoSqlConfig.class,method = "updateWxUserInfoReturnUserId")
	void updateWxUserInfoReturnUserId(TblWxUserInfo userInfo) throws Exception;
}
