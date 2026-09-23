package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.config.SysConfig;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.RiskRectifysolution;
import com.huabo.fxgl.entity.Riskevent;
import com.huabo.fxgl.entity.YyCompany;
import com.huabo.fxgl.mapper.RiskRectifysolutionMapper;
import com.huabo.fxgl.service.IRiskRectifysolutionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Queue;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Service
public class RiskRectifysolutionServiceImpl extends ServiceImpl<RiskRectifysolutionMapper, RiskRectifysolution> implements IRiskRectifysolutionService {

    @Autowired
    private RiskRectifysolutionMapper riskRectifysolutionMapper;

    @Override
    public Integer findSolutioncode(String solutioncode) {
        return riskRectifysolutionMapper.findSolutioncode(solutioncode);
    }

//    @Override
//    public boolean findTblRiskRectifysolutionByRisEventForTomcat(String eventid, Find find, Integer pageNo, int pageSize) {
//            // TODO Auto-generated method stub
//        QueryWrapper<RiskRectifysolution> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("pageNo", pageNo);
//        queryWrapper.eq("pageSize", pageSize);
//            //String sql = "select * from TBL_RISK_RECTIFYSOLUTION where RISEVEID = "+eventid;
//            if(StringUtils.isNotEmpty(find.getCode())) {
//                //sql += " and SOLUTIONCODE like '%"+find.getCode()+"%'";
//                queryWrapper.like("Code", find.getCode());
//
//            }
//            if(StringUtils.isNotEmpty(find.getName())) {
//                //sql += " and SOLUTIONNAME like '%"+find.getName()+"%'";
//                queryWrapper.like("Name", find.getName());
//            }
//            if(StringUtils.isNotEmpty(find.getUserName())) {
//                //sql += " and RECTIFYHEAD like '%"+find.getUserName()+"%'";
//                queryWrapper.like("UserName", find.getUserName);
//
//            }
//            return riskRectifysolutionMapper.findByPageBean(pageNo, pageSize);
//        }

    @Override
    public RiskRectifysolution getRiskRectifysolution(String rectsolid) {
        System.out.println("rectsolid: " + rectsolid);
        String sql = "select * from TBL_RISK_RECTIFYSOLUTION where RECTSOLID = "+rectsolid;
        List<RiskRectifysolution> list = riskRectifysolutionMapper.findBysql(sql);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public IPage<RiskRectifysolution> getRiskRectifysolutionPage(IPage page, String eventid, String code, String name, String userName) {
        QueryWrapper<RiskRectifysolution> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("RISEVEID", eventid);
        String sql = "select * from TBL_RISK_RECTIFYSOLUTION where RISEVEID = "+eventid;
        if(StringUtils.isNotEmpty(code)) {
            queryWrapper.like("SOLUTIONCODE", code);
        }
        if(StringUtils.isNotEmpty(name)) {
            queryWrapper.like("SOLUTIONNAME", name);
        }
        if(StringUtils.isNotEmpty(userName)) {
            queryWrapper.like("RECTIFYHEAD", userName);
        }
        return page(page, queryWrapper);
    }
}



