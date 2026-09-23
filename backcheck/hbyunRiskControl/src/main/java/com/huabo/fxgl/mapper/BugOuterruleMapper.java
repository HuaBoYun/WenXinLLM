package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.BugOuterrule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-15
 */
@Repository
public interface BugOuterruleMapper extends BaseMapper<BugOuterrule> {
    @Select("SELECT * FROM TBL_BUG_OUTERRULE fi where fi.BUGID=#{flowid}and fi.OUTRULID=#{innerid}")
    List<BugOuterrule> isIfFlowInner(String flowid, String innerid);

}
