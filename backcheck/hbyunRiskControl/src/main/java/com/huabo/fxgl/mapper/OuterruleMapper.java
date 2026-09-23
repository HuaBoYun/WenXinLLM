package com.huabo.fxgl.mapper;

import cn.hutool.db.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.BugOuterrule;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.Outerrule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Repository
public interface OuterruleMapper extends BaseMapper<Outerrule> {
    @Select( "SELECT * from TBL_OUTERRULE where OUTRULID not in (SELECT DISTINCT OUTRULID from TBL_BUG_OUTERRULE where bugid=#{bugid}) and CREATEORGID in  ( select to_char(ORGID) from TBL_ORGANIZATION where (1 = 1  AND orgtype=0 and (STATUS != 1 or STATUS IS NULL) ) or  ORGID = #{orgid} start with  ORGID=#{orgid} connect by prior ORGID= fatherorgid) ORDER BY OUTRULID ASC")
    IPage<Outerrule> findOuterRuleByBugPageBean(@Param("bugid") String bugid, @Param("orgid")String orgid, IPage page);

//

    @Select("SELECT * from TBL_OUTERRULE  o where o.OUTRULID in (SELECT fo.OUTRULID from TBL_FLOW_OUTERRULE fo where fo.flowid=#{flowid})")
    IPage<Outerrule> findOuterRuleByFlowId(IPage page, @Param("flowid") String flowid);



}
