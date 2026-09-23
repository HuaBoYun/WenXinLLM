package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.NbsjBugcriterion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.Risk;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 */
@Repository
public interface NbsjBugcriterionMapper extends BaseMapper<NbsjBugcriterion> {
    @Select("SELECT * FROM TBL_NBSJ_BUGCRITERION where BUGCRIID = ( SELECT max(BUGCRIID) FROM TBL_NBSJ_BUG_CRITERION where BUGID= #{param1}")
    List<NbsjBugcriterion> findByTblBugCriterion(String bugid);


}
