package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.BugInnerrule;
import com.huabo.fxgl.entity.Innerrule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

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
public interface InnerruleMapper extends BaseMapper<Innerrule> {



    @Select("SELECT INNRULID, RULENAME, RULENUMBER, ORG.ORGNAME AS PUBLISHORGNAME, PUBLISHDATE  from TBL_INNERRULE inn LEFT JOIN TBL_ORGANIZATION org on INN.PUBLISHORG=ORG.ORGID " +
            "WHERE inn.INNRULID in( SELECT tb.INNRULID from TBL_FLOW_INNERRULE tb where tb.flowid=#{flowid})")
    IPage<Innerrule> selectInnerRuleByFlowId(IPage<Innerrule> page, @Param("flowid") String flowid);

    @Select("SELECT * from TBL_INNERRULE where INNRULID not in (SELECT DISTINCT INNRULID from TBL_BUG_INNERRULE where bugid=#{bugid}) and PUBLISHORG in ( select to_char(ORGID) from TBL_ORGANIZATION where (1 = 1  AND orgtype=0 and (STATUS != 1 or STATUS IS NULL) ) or  ORGID =#{orgid} start with  ORGID=#{orgid} connect by prior ORGID= fatherorgid) ORDER BY INNRULID ASC")
    IPage<Innerrule> findInnerRuleByBugPageBean(@Param("bugid") String bugid, @Param("orgid")String orgid, IPage page);

}
