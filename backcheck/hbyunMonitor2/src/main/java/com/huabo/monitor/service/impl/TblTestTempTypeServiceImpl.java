package com.huabo.monitor.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblTesttemplType;
import com.huabo.monitor.mapper.TblTestelementMapper;
import com.huabo.monitor.mapper.TblTesttemplTypeMapper;
import com.huabo.monitor.service.TblTestTempTypeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Service
public class TblTestTempTypeServiceImpl implements TblTestTempTypeService {
	
	@Resource
	private TblTesttemplTypeMapper tblTesttemplTypeMapper;
	
	@Resource
    private UserProvider userProvider;
	
    @Override
    public void deleteBytemplId(BigDecimal testtemid) {

    }

	@Override
	public JsonBean save(String token, TblTesttemplType type) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		this.tblTesttemplTypeMapper.insert(type);
		return ResponseFormat.retParam(1, 200, type);
	}

	@Override
	public JsonBean modify(String token, TblTesttemplType type) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		this.tblTesttemplTypeMapper.updateById(type);
		return ResponseFormat.retParam(1, 200, type);
	}

	@Override
	public JsonBean remove(String token, BigDecimal typeid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        Integer count = this.tblTesttemplTypeMapper.selectChidrenCount(typeid);
        
        if(count > 0 ) {
        	return ResponseFormat.retParam(1, "存在自己分类无法删除", null);
        }
        String sql = "DELETE FROM TBL_TESTELEMENT WHERE TYPEID = "+typeid;
		this.tblTesttemplTypeMapper.executeDelSql(sql);
        sql = "DELETE FROM TBL_TESTTEMPL_TYPE WHERE TYPEID = "+typeid;
		this.tblTesttemplTypeMapper.executeDelSql(sql);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean getInfo(String token, BigDecimal typeid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        TblTesttemplType type = this.tblTesttemplTypeMapper.selectEntityById(typeid);
        
        return ResponseFormat.retParam(1, 200, type);
	}

	@Override
	public JsonBean getTreeList(String token, BigDecimal parentId, BigDecimal testtempletaid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        List<TblTesttemplType> typeList = this.tblTesttemplTypeMapper.selectTreeListInfo(parentId,testtempletaid);
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("typeList", typeList);
        return ResponseFormat.retParam(1, 200, typeList);
	}
}
