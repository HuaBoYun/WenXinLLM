package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblAccountingRuleCategory;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 会计规则分类Mapper
 */
public interface TblAccountingRuleCategoryMapper extends BaseMapper<TblAccountingRuleCategory> {

    /**
     * 根据分类编码查询
     */
    TblAccountingRuleCategory selectByCategoryCode(@Param("categoryCode") String categoryCode);

    /**
     * 根据父级ID查询
     */
    List<TblAccountingRuleCategory> selectByParentId(@Param("parentId") String parentId);

    /**
     * 根据是否启用查询
     */
    List<TblAccountingRuleCategory> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);

    /**
     * 查询所有顶级分类
     */
    List<TblAccountingRuleCategory> selectTopLevelCategories();
}
