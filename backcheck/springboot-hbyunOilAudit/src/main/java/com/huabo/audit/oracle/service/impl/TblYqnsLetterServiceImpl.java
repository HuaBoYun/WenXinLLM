package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.service.impl.ReservePropertyService;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsLetter;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffSelectMapper;
import com.huabo.audit.oracle.mapper.TblYqnsLetterMapper;
import com.huabo.audit.oracle.service.TblYqnsLetterService;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;

@Service
public class TblYqnsLetterServiceImpl implements TblYqnsLetterService {
	
	@Resource
	private TblYqnsLetterMapper tblYqnsLetterMapper;
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
    private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;

    @Resource
    private ImplementPlanMapper implementPlanMapper;

	@Resource
	private ReservePropertyService reservePropertyService;
	
	@Resource
    private UserProvider userProvider;

	@Override
	public JsonBean saveOrupdate(String token, TblYqnsLetter sheet,String attids) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        
        //==查询当前实施的项目！
        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
        if(tnp == null) {
            return ResponseFormat.retParam(0,30003,null);
        }
        sheet.setProjectid(tnp.getId());
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
        if(sheet!=null && sheet.getLetterid()!=null) {
        	tblYqnsLetterMapper.updateByPrimaryKeySelective(sheet);
        	if(attids!=null && attids.length()>0) {
        		tblYqnsLetterMapper.delFileRelation(sheet.getLetterid());
        		String[] stins = attids.split(",");
        		for (String attid : stins) {
        			tblYqnsLetterMapper.insetFileRelation(attid, sheet.getLetterid());
				} 
        		
        	}
        }else {
        	sheet.setLetterid(RandomUtil.uuBigDecimalId());
			sheet.setStatus(0);
			sheet.setCreatestaff(staff.getStaffid());
			sheet.setCreatetime(new Date());
        	tblYqnsLetterMapper.insertSelective(sheet);
        	if(attids!=null && attids.length()>0) {
        		String[] stins = attids.split(",");
        		for (String attid : stins) {
        			tblYqnsLetterMapper.insetFileRelation(attid, sheet.getLetterid());
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
        TblYqnsLetter sheet = tblYqnsLetterMapper.selectById(sheetid);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(sheet);

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
        
        //==查询当前实施的项目！ 
        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
        if(tnp == null && tBlNbsjSheetVo.getProjectId()==null) {
            return ResponseFormat.retParam(0,30003,null);
        }
        if(tBlNbsjSheetVo.getProjectId() ==null) {
        	  BigDecimal projectId = tnp.getId();
        	tBlNbsjSheetVo.setProjectId(projectId);
        }
        
		 PageInfo<TblYqnsLetter> pageInfo = new PageInfo<TblYqnsLetter>();
         pageInfo.setCurrentPage(pageNumber);
         pageInfo.setPageSize(pageSize);
         pageInfo.setTlist(tblYqnsLetterMapper.selectListByPageInfo(pageInfo, tBlNbsjSheetVo));
         pageInfo.setTotalRecord(tblYqnsLetterMapper.selectCountByPageInfo(pageInfo, tBlNbsjSheetVo));

		 //构建预留字段返回
		reservePropertyService.buildReserveProperty(pageInfo.getTlist());

         resultMap.put("data", pageInfo);
         return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deleteone(String token, BigDecimal sheetid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsLetterMapper.delFileRelation(sheetid);
        tblYqnsLetterMapper.deleteById(sheetid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findattlistByid(String token, BigDecimal sheetid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        List<TblAttachment> list = tblAttachmentMapper.selectAttListByletterid(sheetid);
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
	
	//获取当前实施项目；
    public ImplementPlanEntity getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception {
    	BigDecimal projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(staffid);
        if (projectId == null) {
            return null;
        }
        return implementPlanMapper.selectById(projectId.toString());
    }
	
}
