package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.RiskeventAtt;
import com.huabo.fxgl.mapper.RiskeventAttMapper;
import com.huabo.fxgl.service.IRiskeventAttService;
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
 * @since 2022-08-01
 */
@Service
public class RiskeventAttServiceImpl extends ServiceImpl<RiskeventAttMapper, RiskeventAtt> implements IRiskeventAttService {
    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description
     * @Date 2022/8/11
     * @param eventid
     * @return java.util.List<java.math.BigDecimal>
     * @url:
     **/
    @Override
    public List<BigDecimal> getAttIdsByEventId(String eventid) {
        QueryWrapper<RiskeventAtt> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("RISEVEID",eventid);
        List<RiskeventAtt> attList = this.list(queryWrapper);
        if (attList!=null&&attList.size()>0){
            return attList.stream().map(RiskeventAtt::getAttid).collect(Collectors.toList());
        }
        return null;
    }
}
