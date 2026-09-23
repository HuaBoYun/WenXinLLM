package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblWgzzWghcBg;
import com.huabo.audit.oracle.mapper.TblWgzzWghcBgMapper;
import com.huabo.audit.oracle.mapper.TblWgzzWghcMapper;
import com.huabo.audit.service.TblWghcBgService;
import com.huabo.audit.util.PageInfo;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.service.impl
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:10:38
 */
@Service
public class TblWghcBgServiceimpl implements TblWghcBgService {

    @Resource
    TblWgzzWghcBgMapper tblWgzzWghcBgMapper;
    @Resource
    TblWgzzWghcMapper tblWgzzWghcMapper;
    
    @Resource
    private UserProvider userProvider;

    //违规核查报告列表查询
    @Override
    public JsonBean getByWghcBgList(String token, Integer pageNumber, Integer pageSize, String clueNaber) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblWgzzWghcBg> pageInfo = new PageInfo<TblWgzzWghcBg>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblWgzzWghcBgMapper.getWghcBgList(pageInfo, clueNaber));
        pageInfo.setTotalRecord(this.tblWgzzWghcBgMapper.getWghcBgContList(pageInfo, clueNaber));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    //违规核查报告新增/修改
    @Override
    public JsonBean addlist(String token, TblWgzzWghcBg tblWgzzWghcBg) throws Exception {
        /*TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }*/
        Map<String, Object> resultMap = new HashMap<>();
        if (tblWgzzWghcBg.getId() != null) {
            //修改
            tblWgzzWghcBgMapper.updateByPrimaryKeySelective(tblWgzzWghcBg);
            //修改
            tblWgzzWghcMapper.updateWghc(tblWgzzWghcBg.getWghcid(),tblWgzzWghcBg.getCluenaber(),tblWgzzWghcBg.getHscontent());
        } else {
            //新增
            tblWgzzWghcBgMapper.insertSelective(tblWgzzWghcBg);
            //修改
            tblWgzzWghcMapper.updateWghc(tblWgzzWghcBg.getWghcid(),tblWgzzWghcBg.getCluenaber(),tblWgzzWghcBg.getHscontent());
        }
        resultMap.put("tblWgzzWghcBg", tblWgzzWghcBg);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    //违规核查报告删除
    @Override
    public JsonBean removeList(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.tblWgzzWghcBgMapper.deleteWghcBg(id);
        return ResponseFormat.retParam(1, 200, null);
    }

    //违规核查报告详情
    @Override
    public JsonBean detail(String token, BigDecimal wghcid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblWgzzWghcBg tblWgzzWghcBg = tblWgzzWghcBgMapper.selectBywghcId(wghcid);
        return ResponseFormat.retParam(1, 200, tblWgzzWghcBg);
    }
}
