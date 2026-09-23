package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.RepAtt;
import com.huabo.fxgl.mapper.RepAttMapper;
import com.huabo.fxgl.service.IRepAttService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-18
 */
@Service
public class RepAttServiceImpl extends ServiceImpl<RepAttMapper, RepAtt> implements IRepAttService {

    /*
    * @author zuoshun
    * @version v1.0.1
    * @Description 通过reportId查询中间表获得attrId集合
    * @Date 2022/8/18
    * @param id
    * @return java.util.List<java.math.BigDecimal>
    * @url:
    **/
    @Override
    public List<BigDecimal> getAttIdsByReportId(String id) {
        QueryWrapper<RepAtt> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("REPORTID",id);
        List<RepAtt> list = this.list(queryWrapper);
        if (list!=null&&list.size()>0){
          return list.stream().map(RepAtt::getAttid).collect(Collectors.toList());
        }
        return null;
    }
}
