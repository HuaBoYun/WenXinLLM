package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsSheet;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSheetMapper;
import com.huabo.audit.oracle.service.TblYqnsSheetService;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;

@Service
public class TblYqnsSheetServiceImpl implements TblYqnsSheetService {
	
	@Resource
	private TblYqnsSheetMapper tblYqnsSheetMapper;
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;

	@Resource
    private UserProvider userProvider;

	@Override
	public JsonBean saveOrupdate(String token, TblYqnsSheet sheet,String attids) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        if(sheet!=null && sheet.getSheetid()!=null) {
        	tblYqnsSheetMapper.updateByPrimaryKeySelective(sheet);
        	if(attids!=null && attids.length()>0) {
        		tblYqnsSheetMapper.delFileRelation(sheet.getSheetid());
        		String[] stins = attids.split(",");
        		for (String attid : stins) {
        			tblYqnsSheetMapper.insetFileRelation(attid, sheet.getSheetid());
				}
        		
        	}
        }else {
        	sheet.setStatus(0);
        	sheet.setSheetid(RandomUtil.uuBigDecimalId());
        	tblYqnsSheetMapper.insertSelective(sheet);
        	if(attids!=null && attids.length()>0) {
        		String[] stins = attids.split(",");
        		for (String attid : stins) {
        			tblYqnsSheetMapper.insetFileRelation(attid, sheet.getSheetid());
				}
        		
        	}
        }
        resultMap.put("data", sheet);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findByid(String token, BigDecimal sheetid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsSheet sheet = tblYqnsSheetMapper.selectById(sheetid);
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("data", sheet);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findAllList(String token, Integer pageNumber, Integer pageSize, TBlNbsjSheetVo tBlNbsjSheetVo)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
		 PageInfo<TblYqnsSheet> pageInfo = new PageInfo<TblYqnsSheet>();
         pageInfo.setCurrentPage(pageNumber);
         pageInfo.setPageSize(pageSize);
         pageInfo.setTlist(tblYqnsSheetMapper.selectListByPageInfo(pageInfo, tBlNbsjSheetVo));
         pageInfo.setTotalRecord(tblYqnsSheetMapper.selectCountByPageInfo(pageInfo, tBlNbsjSheetVo));
         resultMap.put("data", pageInfo);
         return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deleteone(String token, BigDecimal sheetid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsSheetMapper.delFileRelation(sheetid);
        tblYqnsSheetMapper.deleteById(sheetid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findattlistByid(String token, BigDecimal sheetid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        List<TblAttachment> list = tblAttachmentMapper.selectAttListBysheetId(sheetid);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("data", list);
        return ResponseFormat.retParam(1,200,list);
	}

	@Override
	public JsonBean deleteatt(String token, BigDecimal attid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblAttachmentMapper.deleteEntity(attid);
        return ResponseFormat.retParam(1,200,null);
	}
	
}
