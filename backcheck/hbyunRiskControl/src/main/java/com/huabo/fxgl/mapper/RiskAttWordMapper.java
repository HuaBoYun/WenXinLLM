package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.RiskAttWord;
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
 * @since 2022-08-19
 */
@Repository
public interface RiskAttWordMapper extends BaseMapper<RiskAttWord> {
    List<RiskAttWord> getFile(@Param("type") String type, @Param("orgid") String orgid, @Param("id") String id);
}
