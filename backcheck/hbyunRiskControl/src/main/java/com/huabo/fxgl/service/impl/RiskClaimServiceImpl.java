package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.RiskClaim;
import com.huabo.fxgl.mapper.RiskClaimMapper;
import com.huabo.fxgl.service.IRiskClaimService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Service
public class RiskClaimServiceImpl extends ServiceImpl<RiskClaimMapper, RiskClaim> implements IRiskClaimService {
    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 通过eventId查询索赔信息
     * @Date 2022/8/11
     * @param evenid
     * @return com.hbfk.util.JsonBean
     * @url:
     **/
    @Override
    public JsonBean getByEventId(String riseveid) {
        if (riseveid==null){
            return new JsonBean(0,"参数异常",null);
        }
        QueryWrapper<RiskClaim> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("CLAIMNUMBER",riseveid);
        List<RiskClaim> riskClaimList = this.list(queryWrapper);
        return new JsonBean(1,"",riskClaimList);
    }
}
