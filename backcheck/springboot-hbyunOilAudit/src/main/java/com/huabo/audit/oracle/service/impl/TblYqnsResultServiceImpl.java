package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffSelectMapper;
import com.huabo.audit.service.impl.ReservePropertyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsResult;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsResultMapper;
import com.huabo.audit.oracle.service.TblYqnsResultService;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;

@Service
public class TblYqnsResultServiceImpl implements TblYqnsResultService {
	
	@Resource
	private TblYqnsResultMapper tblYqnsResultMapper;
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;

	@Resource
	private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;

	@Autowired
	private ImplementPlanMapper implementPlanMapper;

	@Autowired
	private ReservePropertyService reservePropertyService;
	
	@Resource
    private UserProvider userProvider;

	 

	@Override
	public JsonBean saveOrupdate(String token, TblYqnsResult sheet,String attids,String JsonObject) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
		//==查询当前实施的项目！
		ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,null);
		}
		BigDecimal projectId = tnp.getId();
		if(null == projectId) {
			return ResponseFormat.retParam(0,30003,null);
		}
		sheet.setProjectid(projectId);
		sheet.setCreatestaff(staff.getStaffid());
		
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        if(sheet!=null && sheet.getResultid()!=null) {
        	tblYqnsResultMapper.updateByPrimaryKeySelective(sheet);
        	if(attids!=null && attids.length()>0) {
        		tblYqnsResultMapper.delFileRelation(sheet.getResultid());
        		String[] stins = attids.split(",");
        		for (String attid : stins) {
        			tblYqnsResultMapper.insetFileRelation(attid, sheet.getResultid());
				}
        		
        	}
        }else {
        	sheet.setStatus(0);
        	sheet.setResultid(RandomUtil.uuBigDecimalId());
        	tblYqnsResultMapper.insertSelective(sheet);
        	if(attids!=null && attids.length()>0) {
        		String[] stins = attids.split(",");
        		for (String attid : stins) {
        			tblYqnsResultMapper.insetFileRelation(attid, sheet.getResultid());
				}
        		
        	}
        }
        
        if(StringUtils.isNotBlank(sheet.getZixbids()) ) {
        	String[] zixbids = sheet.getZixbids().split(",");
        	for (String zixbid : zixbids) {
        		TblYqnsResult zixb = new TblYqnsResult();
        		zixb.setResultid(new BigDecimal(zixbid));
        		zixb.setResultids(sheet.getResultid());
        		tblYqnsResultMapper.updateByPrimaryKeySelective(zixb);
			}
        }
//		if(StringUtils.isNotBlank(JsonObject) ) {
//			List<TblYqnsResult> sheets = JSONObject.parseArray(JsonObject, TblYqnsResult.class);
//			for (TblYqnsResult result:sheets) {
//				result.setResultids(sheet.getResultid());
//				result.setProjectid(sheet.getProjectid());
//				result.setStatus(0);
//				tblYqnsResultMapper.insertSelective(result);
//			}
//		}
        resultMap.put("data", sheet);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findByid(String token, BigDecimal sheetid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsResult sheet = tblYqnsResultMapper.selectById(sheetid);
        if (sheet != null && sheet.getResultid() != null){
			List<TblYqnsResult> yqnsResults = tblYqnsResultMapper.selectByFahterResultIds(sheet.getResultid());
			sheet.setZixbs(yqnsResults);
		}
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
		//==查询当前实施的项目！
        if(tBlNbsjSheetVo.getProjectId()==null) {
        	 if(tBlNbsjSheetVo.getProjectId()==null) {
             	ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
         		if(tnp == null) {
         			return ResponseFormat.retParam(0,30003,null);
         		}
         		BigDecimal projectId = tnp.getId();
         		if(null == projectId) {
         			return ResponseFormat.retParam(0,30003,null);
         		}
         		tBlNbsjSheetVo.setProjectId(projectId);
         		if(!staff.getStaffid().toString().equals(tnp.getZsstaffid().toString())) {
         			tBlNbsjSheetVo.setCreatestaff(staff.getStaffid());
         		}
             }
        	 
        }
       
		
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
		 PageInfo<TblYqnsResult> pageInfo = new PageInfo<TblYqnsResult>();
         pageInfo.setCurrentPage(pageNumber);
         pageInfo.setPageSize(pageSize);
         pageInfo.setTlist(tblYqnsResultMapper.selectListByPageInfo(pageInfo, tBlNbsjSheetVo, null));
         pageInfo.setTotalRecord(tblYqnsResultMapper.selectCountByPageInfo(pageInfo, tBlNbsjSheetVo));

		 //构建预留字段返回
		reservePropertyService.buildReserveProperty(pageInfo.getTlist());

         resultMap.put("data", pageInfo);
         return ResponseFormat.retParam(1,200,resultMap);
	}

	//获取当前实施项目；
	public ImplementPlanEntity getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception {
		BigDecimal projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(staffid);
		if (projectId == null) {
			return null;
		}
		return implementPlanMapper.selectById(projectId.toString());
	}

	@Override
	public JsonBean deleteone(String token, BigDecimal sheetid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsResultMapper.delFileRelation(sheetid);
        tblYqnsResultMapper.deleteById(sheetid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findattlistByid(String token, BigDecimal sheetid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        List<TblAttachment> list = tblAttachmentMapper.selectAttListByresultid(sheetid);
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
	
	@Override
	public JsonBean findSjxmgkList(String token, Integer pageNumber, Integer pageSize, TBlNbsjSheetVo tBlNbsjSheetVo)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
		//==查询当前实施的项目！
		ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,null);
		}
		BigDecimal projectId = tnp.getId();
		if(null == projectId) {
			return ResponseFormat.retParam(0,30003,null);
		}
		tBlNbsjSheetVo.setProjectId(projectId);
		tBlNbsjSheetVo.setStatus("6");
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
		 PageInfo<TblYqnsResult> pageInfo = new PageInfo<TblYqnsResult>();
         pageInfo.setCurrentPage(pageNumber);
         pageInfo.setPageSize(pageSize);
         pageInfo.setTlist(tblYqnsResultMapper.selectListByPageInfo(pageInfo, tBlNbsjSheetVo, null));
         pageInfo.setTotalRecord(tblYqnsResultMapper.selectCountByPageInfo(pageInfo, tBlNbsjSheetVo));
         resultMap.put("data", pageInfo);
         return ResponseFormat.retParam(1,200,resultMap);
	}
	
	/**
     * 审计项目追款-导出
     */
    @Override
    public JsonBean sjxmzkExport(HttpServletResponse response, String token, TBlNbsjSheetVo tBlNbsjSheetVo, List<String> idList) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
      //==查询当前实施的项目！
  		ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
  		if(tnp == null) {
  			return ResponseFormat.retParam(0,30003,null);
  		}
  		BigDecimal projectId = tnp.getId();
  		if(null == projectId) {
  			return ResponseFormat.retParam(0,30003,null);
  		}
  		tBlNbsjSheetVo.setProjectId(projectId);
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
  		PageInfo<TblYqnsResult> pageInfo = new PageInfo<TblYqnsResult>();
        pageInfo.setCurrentPage(1);
        pageInfo.setPageSize(9999);
        List<TblYqnsResult> listData = tblYqnsResultMapper.selectListByPageInfo(pageInfo, tBlNbsjSheetVo, idList);

        String[] titles = {"序号", "审计项目名称", "被审计单位","追款事由","追款金额(万元)","追款相对方"};
        
        TblYqnsResult entity = new TblYqnsResult();
        List<Object[]> objs = new ArrayList<>();
        for (TblYqnsResult bean : listData) {
            Object[] obj = new Object[titles.length];
            obj[0] = bean.getResultcode();
            obj[1] = bean.getProjectname();
            obj[2] = bean.getOrgidnames(); 
            obj[3] = bean.getOverview();
            obj[4] = bean.getSdmoney();
            obj[5] = bean.getSgorgname();
            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("审计项目追款".getBytes(), "UTF-8") + ".xlsx");
        ImportOrExportExcelUtil.exportExcel(titles, objs, response.getOutputStream(), null);
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }
    
    
    
    @Override
	public JsonBean findbyhj(String token,TBlNbsjSheetVo tBlNbsjSheetVo)throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
		
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
         resultMap.put("data", tblYqnsResultMapper.findbyhj(tBlNbsjSheetVo, null));
         return ResponseFormat.retParam(1,200,resultMap);
	}
	
    
    
    
    @Override
   	public JsonBean exportData(HttpServletResponse response, String token, TBlNbsjSheetVo tBlNbsjSheetVo, List<String> idList)throws Exception {
   		TblStaffUtil staff = userProvider.get();
           if (staff == null) {
           	return ResponseFormat.retParam(0, 20006, null);
           }
           String[] titles = {"序号", "合同编号", "工程名称", "施工单位", "报审金额", "审定金额", "审减金额", "审减率", "工程量计算", "定额套用", "现场实例", "物资价格", "其它审减", "审计人员"};
          
           List<Object[]> objs = new ArrayList<>();
           AtomicLong xh = new AtomicLong(1);
           List<TblYqnsResult> list = tblYqnsResultMapper.findbyhj(tBlNbsjSheetVo, idList);
           if(list!=null && list.size()>0) {
        	   for (TblYqnsResult rs : list) {
        		   Object[] obj = new Object[titles.length];
        		   obj[0] = xh.getAndIncrement();
        		   obj[1] = rs.getContractcode();
        		   obj[2] = rs.getContractname();
        		   obj[3] = rs.getSgorgname();
        		   obj[4] = rs.getContractmoney();
        		   obj[5] = rs.getSdmoney();
        		   obj[6] = rs.getHjmoney();
        		   obj[7] = rs.getHjl();
        		   obj[8] = rs.getGcljs();
        		   obj[9] = rs.getDety();
        		   obj[10] = rs.getXcsc();
        		   obj[11] = rs.getWzjg();
        		   obj[12] = rs.getQtsj();
        		   obj[13] = rs.getRwnames();
        		   objs.add(obj);
			}
           }
           response.setContentType("application/binary;charset=UTF-8");
           response.setHeader("Content-Disposition", "attachment; filename=" + new String("审计审减内容".getBytes(), "UTF-8") + ".xlsx");
           ImportOrExportExcelUtil.exportExcel(titles, objs, response.getOutputStream(), null);
           return ResponseFormat.retParam(1, 200, Boolean.TRUE);
   	}
   	
    
}
