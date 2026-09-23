package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.YyTeam;
import com.huabo.fxgl.mapper.YyTeamMapper;
import com.huabo.fxgl.service.IYyCompanyService;
import com.huabo.fxgl.service.IYyTeamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@Service
public class YyTeamServiceImpl extends ServiceImpl<YyTeamMapper, YyTeam> implements IYyTeamService {
    @Autowired
    private IYyCompanyService yyCompanyService;
    @Override
    public List<YyTeam> getByOrgidAndStaffid(BigDecimal orgid, BigDecimal staffid,String fxjktype) {
        // String sql="select * from TBL_YY_TEAM where STAFFID="+userid+" and COMPANYID="+orgid;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("STAFFID", staffid);
        queryWrapper.eq("COMPANYID", orgid);
        queryWrapper.eq("fxjktype", fxjktype);
        List<YyTeam> teamList = list(queryWrapper);
        for (YyTeam team : teamList) {
        	QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("teamid", team.getTeamid());
            queryWrapper2.eq("fxjktype",fxjktype );
            long count=yyCompanyService.count(queryWrapper2);
            team.setCompanyCount(count);
            
        }
        return teamList;
    }
}
