package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.BugInnerrule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-15
 *
 */
@Repository
public interface BugInnerruleMapper extends BaseMapper<BugInnerrule> {
    @Select("SELECT * FROM TBL_BUG_INNERRULE fi where fi.BUGID=#{flowid} and fi.INNRULID=#{innerid}")
    List<BugInnerrule> isIfFlowInner(@Param("flowid")String flowid, @Param("innerid")String innerid);
}
