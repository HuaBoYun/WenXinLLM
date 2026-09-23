package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblExpenseItem;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 费用项目Mapper
 */
public interface TblExpenseItemMapper extends BaseMapper<TblExpenseItem> {

    /**
     * 根据费用项目编码查询
     */
    TblExpenseItem selectByItemCode(@Param("itemCode") String itemCode);

    /**
     * 根据父级ID查询
     */
    List<TblExpenseItem> selectByParentId(@Param("parentId") String parentId);

    /**
     * 根据层级查询
     */
    List<TblExpenseItem> selectByItemLevel(@Param("itemLevel") Integer itemLevel);

    /**
     * 根据是否启用查询
     */
    List<TblExpenseItem> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);

    /**
     * 查询所有顶级费用项目
     */
    List<TblExpenseItem> selectTopLevelItems();
}
