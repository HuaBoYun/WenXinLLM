package com.huabo.audit.oracle.mapper;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblWgzzEntity;
import com.huabo.audit.oracle.vo.TblWgzzVo;
import com.huabo.audit.util.PageInfo;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.oracle.mapper
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/26
 * @Time:10:50
 */

public interface TblWgzzMapper extends tk.mybatis.mapper.common.Mapper<TblWgzzEntity> {
    @SelectProvider(method="getwgzzList",type=TblwgzzMapperSqlConfig.class)
    @Results({
            @Result(column="CLUEID",property="clueid"),
            @Result(column="CLUENABER",property="cluenaber"),
            @Result(column="CLUEUNIT",property="clueunit"),
            @Result(column="CLUEHANDLING",property="cluehandling"),
            @Result(column="CREATOR",property="creator"),
            @Result(column="IMPCREATEUSERNAME",property="impcreateusername"),
			@Result(column="VERIFYCONTENTNEW",property="verifycontentnew"),
    })
    List<TblWgzzEntity> getwgzzList(PageInfo<TblWgzzEntity> pageInfo,TblWgzzEntity param);

    @Select("select * from TBL_WGZZ_WGZZ where CLUEID = #{clueid}")
    @Results({
            @Result(column="CLUEID",property="clueid"),
            @Result(column="CLUENABER",property="cluenaber"),
            @Result(column="CLUEUNIT",property="clueunit"),
            @Result(column="CLUEHANDLING",property="cluehandling"),
            @Result(column="CREATOR",property="creator"),
            @Result(column="IMPCREATEUSERNAME",property="impcreateusername"),
    })
   TblWgzzEntity getwgzzXQList(@Param("clueid") BigDecimal clueid);

    @SelectProvider(method="getwgzzContList",type=TblwgzzMapperSqlConfig.class)
    Integer getwgzzContList(PageInfo<TblWgzzEntity> pageInfo,String clueNaber);


    //删除
    @Delete("delete from TBL_WGZZ_WGZZ t where t.CLUEID = #{clueid}")
    Integer deletewgzz(@Param("clueid") BigDecimal clueid);

    //==违规追责-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_WGZZ_ATT WHERE ID = #{clueid})")
    List<TblAttachment> findAttachmentListByWgzz(BigDecimal clueid);
    
    
    
    @SelectProvider(method="getwgzzYsList",type=TblwgzzMapperSqlConfig.class)
    @Results({
            @Result(column="CLUEID",property="clueid"),
            @Result(column="CLUENABER",property="cluenaber"),
            @Result(column="CLUEUNIT",property="clueunit"),
            @Result(column="CLUEHANDLING",property="cluehandling"),
            @Result(column="CREATOR",property="creator"),
            @Result(column="IMPCREATEUSERNAME",property="impcreateusername"),
			@Result(column="VERIFYCONTENTNEW",property="verifycontentnew"),
    })
    List<TblWgzzEntity> getwgzzYsList(PageInfo<TblWgzzEntity> pageInfo,TblWgzzEntity param,Integer type);

}
