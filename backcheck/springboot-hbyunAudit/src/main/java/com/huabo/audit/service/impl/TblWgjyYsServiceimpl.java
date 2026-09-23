package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblWgzzWghcBg;
import com.huabo.audit.oracle.entity.TblWgzzWgjyYs;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.service.TblWghcBgService;
import com.huabo.audit.service.TblWgjyYsService;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.service.impl
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:10:38
 */
@Service
public class TblWgjyYsServiceimpl implements TblWgjyYsService {

    @Resource
    TblWgzzWgjyYsMapper tblWgzzWgjyYsMapper;

    @Resource
    TblWgzzWghcMapper tblWgzzWghcMapper;
	@Resource
	TblWghsMapper tblWghsMapper;
	
	@Resource
    private UserProvider userProvider;

    //违规经营列表查询
    @Override
    public JsonBean getByWgjyYsList(String token, Integer pageNumber, Integer pageSize, String clueNaber) throws Exception {
        /*TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }*/
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblWgzzWgjyYs> pageInfo = new PageInfo<TblWgzzWgjyYs>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblWgzzWgjyYsMapper.getWgjyYsList(pageInfo, clueNaber));
        pageInfo.setTotalRecord(this.tblWgzzWgjyYsMapper.getWgjyYsContList(pageInfo, clueNaber));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

	@Override
	public List<TblWgzzWgjyYs> getByWgjyYsExtList(Integer pageNumber, Integer pageSize) {
		PageInfo<TblWgzzWgjyYs> pageInfo = new PageInfo<TblWgzzWgjyYs>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		return this.tblWgzzWgjyYsMapper.getWgjyYsList(pageInfo, null);
	}

	//违规经营移送新增/修改
    @Override
    public JsonBean addlist(String token, TblWgzzWgjyYs tblWgzzWgjyYs) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<>();
        tblWgzzWgjyYs.setImpcreateusername(new Date());
        if (tblWgzzWgjyYs.getId() != null) {
            //修改
            tblWgzzWgjyYsMapper.updateByPrimaryKeySelective(tblWgzzWgjyYs);

            //修改
//            tblWgzzWghcMapper.updateWghc(tblWgzzWgjyYs.getWghcid(),tblWgzzWgjyYs.getCluenaber(),tblWgzzWgjyYs.getHscontent());

			tblWghsMapper.deletefile(tblWgzzWgjyYs.getId());
			if (tblWgzzWgjyYs.getFileids() != null && !"".equals(tblWgzzWgjyYs.getFileids())) {
				String[] attId = tblWgzzWgjyYs.getFileids().split(",");
				for (String attid : attId) {
					this.tblWghsMapper.insertAttInfoForPlan(tblWgzzWgjyYs.getId(), attid);
				}
				return ResponseFormat.retParam(1, 200, null);
			}
        } else {
        	 tblWgzzWgjyYs.setCreator(loginStaff.getStaffid());
            //新增
            tblWgzzWgjyYsMapper.insertSelective(tblWgzzWgjyYs);

            //修改
//            tblWgzzWghcMapper.updateWghc(tblWgzzWgjyYs.getWghcid(),tblWgzzWgjyYs.getCluenaber(),tblWgzzWgjyYs.getHscontent());
			if (tblWgzzWgjyYs.getFileids() != null && !"".equals(tblWgzzWgjyYs.getFileids())) {
				String[] attId = tblWgzzWgjyYs.getFileids().split(",");
				for (String attid : attId) {
					this.tblWghsMapper.insertAttInfoForPlan(tblWgzzWgjyYs.getId(), attid);
				}
				return ResponseFormat.retParam(1, 200, null);
			}
        }
        resultMap.put("tblWgzzWgjyYs", tblWgzzWgjyYs);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    //违规经营移送删除
    @Override
    public JsonBean removeList(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.tblWgzzWgjyYsMapper.deleteWgjyYs(id);
        return ResponseFormat.retParam(1, 200, null);
    }

    //违规核查报告详情
    @Override
    public JsonBean detail(String token, BigDecimal wghcid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblWgzzWgjyYs tblWgzzWgjyYs = tblWgzzWgjyYsMapper.selectBywghcId(wghcid);
        return ResponseFormat.retParam(1, 200, tblWgzzWgjyYs);
    }

    
    @Override
    public JsonBean getbyiddetail(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblWgzzWgjyYs tblWgzzWgjyYs = tblWgzzWgjyYsMapper.selectById(id);
        return ResponseFormat.retParam(1, 200, tblWgzzWgjyYs);
    }

    
    
	@Override
	public R deleteAttInfoByAttId(String attId) throws Exception{
		boolean b = false;
		TblAttachment att = this.tblWghsMapper.selectEntityById(attId);
		if(att!=null){
			this.tblWghsMapper.deleteFileInfoByAttId(att.getAttid());
			this.tblWghsMapper.deleteEntity(att.getAttid());
		}
		return R.success();
	}
}
