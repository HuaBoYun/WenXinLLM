package com.huabo.compliance.service.impl;

import com.huabo.compliance.entity.TblTestelement;
import com.huabo.compliance.mapper.TblTestelementMapper;
import com.huabo.compliance.service.TblTestElementService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-09-08
 */
@Service
public class TblTestelementServiceImpl extends ServiceImpl<TblTestelementMapper, TblTestelement> implements TblTestElementService {

	@Resource
	private TblTestelementMapper tblTestelementMapper;
	
	@Resource
	private UserProvider userProvider;
	
	@Override
	public JsonBean save(String token, TblTestelement newEle) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        this.tblTestelementMapper.insertEntity(newEle);
        
        return ResponseFormat.retParam(1, 200, newEle);
	}

	@Override
	public JsonBean modify(String token, TblTestelement newEle) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        this.tblTestelementMapper.updateEntity(newEle);
        
        return ResponseFormat.retParam(1, 200, newEle);
	}

	@Override
	public JsonBean remvoe(String token, BigDecimal elementid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        this.tblTestelementMapper.removeEntity(elementid);
        
        return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean getPageInfo(String token, Integer pageNumber, Integer pageSize, BigDecimal typeid,
			BigDecimal templid, String elementcode, String businessdesc) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblTestelement ele = new TblTestelement();
        ele.setTypeid(typeid);
        ele.setTemplid(templid);
        ele.setElementcode(elementcode);
        ele.setBusinessdesc(businessdesc);
		
        PageInfo<TblTestelement> pageInfo = new PageInfo<TblTestelement>();
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setPageSize(pageSize);
        pageInfo.setCondition(ele);
        
        pageInfo.setTlist(this.tblTestelementMapper.selectPageList(pageInfo));
        pageInfo.setTotalRecord(this.tblTestelementMapper.selectPageCount(pageInfo));
        
        
        return ResponseFormat.retParam(1, 200, pageInfo);
	}

	@Override
	public JsonBean getInfo(String token, BigDecimal elementid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        TblTestelement ele = this.tblTestelementMapper.selectEntityById(elementid);
        
        return ResponseFormat.retParam(1, 200, ele);
	}

	@Override
	public List<TblTestelement> findByPlanidAll(String planid) throws Exception {
		// TODO Auto-generated method stub
		return tblTestelementMapper.findByPlanidAll(planid);
	}

}
