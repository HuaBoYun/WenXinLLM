package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.Criterion;
import com.huabo.fxgl.mapper.CriterionMapper;
import com.huabo.fxgl.service.ICriterionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-12
 */
@Service
public class CriterionServiceImpl extends ServiceImpl<CriterionMapper, Criterion> implements ICriterionService {
    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 通过orgId查询 Criterion
     * @Date 2022/8/18
     * @param id
     * @return java.util.List<com.huabo.fxgl.entity.Criterion>
     * @url:
     **/
    @Override
    public List<Criterion> getByOrgId(BigDecimal orgId) {
        if (orgId==null) {
            return new LinkedList<>();
        }
        QueryWrapper<Criterion> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("ORGID",orgId);
        return this.list(queryWrapper);
    }
}
