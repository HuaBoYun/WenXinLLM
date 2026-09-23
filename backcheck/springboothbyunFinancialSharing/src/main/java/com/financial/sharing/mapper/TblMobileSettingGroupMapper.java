package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblMobileSettingGroup;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 设置分组Mapper
 */
public interface TblMobileSettingGroupMapper extends BaseMapper<TblMobileSettingGroup> {

    /**
     * 根据分组编码查询
     */
    TblMobileSettingGroup selectByGroupCode(@Param("groupCode") String groupCode);

    /**
     * 根据分组类型查询
     */
    List<TblMobileSettingGroup> selectByGroupType(@Param("groupType") String groupType);

    /**
     * 根据是否启用查询
     */
    List<TblMobileSettingGroup> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);
}
