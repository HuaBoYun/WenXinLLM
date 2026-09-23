package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.YyTeam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
public interface YyTeamMapper extends BaseMapper<YyTeam> {
    /*未找到Oracle数据库中对应的序列，顾手动查询 teamid的最大值 + 1用作下一个team的主键*/
    /*@Select("select max(TEAMID) from TBL_YY_TEAM")
    Integer selectMaxId();*/
}
