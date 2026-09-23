package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.BugCriterion;
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
 * @since 2022-08-05
 */
@Mapper
public interface BugCriterionMapper extends BaseMapper<BugCriterion> {

    @Select("SELECT * FROM TBL_BUGCRITERION where 1=1 and ORGID=#{param1}")
    List<BugCriterion> findAll(String orgid);


}
