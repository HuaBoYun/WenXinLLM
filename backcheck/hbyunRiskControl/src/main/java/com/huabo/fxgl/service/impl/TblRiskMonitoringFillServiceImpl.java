package com.huabo.fxgl.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.TblFillIssued;
import com.huabo.fxgl.entity.TblRiskMonDictonary;
import com.huabo.fxgl.entity.TblRiskMonitoringFill;
import com.huabo.fxgl.mapper.OrganizationMapper;
import com.huabo.fxgl.mapper.StaffMapper;
import com.huabo.fxgl.mapper.TblFillIssuedMapper;
import com.huabo.fxgl.mapper.TblRiskMonDictonaryMapper;
import com.huabo.fxgl.mapper.TblRiskMonitoringFillMapper;
import com.huabo.fxgl.service.TblRiskMonitoringFillService;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.util.PageResult;
import com.huabo.fxgl.util.RiskDataMerger;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

@Service
public class TblRiskMonitoringFillServiceImpl extends ServiceImpl<TblRiskMonitoringFillMapper, TblRiskMonitoringFill> implements TblRiskMonitoringFillService {

    @Resource
    private TblRiskMonitoringFillMapper tblRiskMonitoringFillMapper;
    

    @Resource
    private StaffMapper staffMapper;

    @Resource
    private OrganizationMapper organizationMapper;
    
    @Resource
    private TblFillIssuedMapper tblFillIssuedMapper;
    @Resource
    private  TblRiskMonDictonaryMapper tblRiskMonDictonaryMapper;
    
 

    @Resource
    private UserProvider userProvider;
    
    //审计法务部人员
    @Value("${application.auditlegaldepartment:}")
	private String auditlegaldepartment;
    
	@Override
	public JsonBean saveOrUpdate(TblRiskMonitoringFill tblRiskMonitoringFill, String token) throws Exception {
		  Map<String, Object> hashMap = null;
	        try {
	            hashMap = new HashMap<>();
	            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
	            if (staffUtil == null){
	                return  ResponseFormat.retParam(0, 20006, hashMap);
	            }
	            if(tblRiskMonitoringFill.getId()!=null&&tblRiskMonitoringFill.getId().compareTo(new BigDecimal(0))==1){
	            	TblFillIssued iss=tblFillIssuedMapper.selectByFillId(tblRiskMonitoringFill.getId());
	            	iss.setDeptNotes(tblRiskMonitoringFill.getDeptNotes());
	            	iss.setDeptDate(new Date());
	            	tblFillIssuedMapper.updateById(iss);
	            	tblRiskMonitoringFillMapper.updateById(tblRiskMonitoringFill);
	            }
//	            else{
//	            	tblRiskMonitoringFill.setCreatestaffid(staffUtil.getStaffid());//创建人名称
//	 	            tblRiskMonitoringFill.setCreatetime(new Date());//创建时间
//	 	            tblRiskMonitoringFill.setLinkOrgId(staffUtil.getLinkOrg().getOrgid());//创建单位
//	 	            tblRiskMonitoringFill.setLinkDeptId(staffUtil.getLinkDetp().getOrgid());
//	 	            tblRiskMonitoringFill.setId(RandomUtil.uuBigDecimalId());
//	 	            tblRiskMonitoringFillMapper.insert(tblRiskMonitoringFill);
//	 	        	TblFillIssued iss=tblFillIssuedMapper.selectByFillId(tblRiskMonitoringFill.getMonitorId().toString(),tblRiskMonitoringFill.getLinkDeptId());
//	 	        	iss.setDeptNotes(tblRiskMonitoringFill.getDeptNotes());
//	            	iss.setDeptDate(new Date());
//	            	tblFillIssuedMapper.updateById(iss);
//	            }
	            hashMap.put("pageInfo",tblRiskMonitoringFill);
	        } catch (Exception e) {
	          e.printStackTrace();
	        }
	        return ResponseFormat.retParam(1, 200, hashMap);
	}
    
	@Override
	public void delete(String id)
			throws Exception {
		// TODO Auto-generated method stub
	      try {
	    	  tblRiskMonitoringFillMapper.deleteById(id);
//	            //删除所有附件
//	            List<BigDecimal> attIdList = this.tblRiskReportingMapper.findAttIdListByReporting(id);
//	            for (BigDecimal attId : attIdList) {
//	                this.deleteRealtionAttInfo(attId);
//	            }
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	}
    
  @Override
  public JsonBean getList(String token, Integer pageNumber, Integer pageSize, TblRiskMonitoringFill tblRiskMonitoringFill) throws Exception {
      Map<String, Object> hashMap = null;
      try {
          hashMap = new HashMap<>();
          //得到了当前登录的用户信息
          TblStaffUtil staffUtil = userProvider.get();
          if (staffUtil == null){
              return  ResponseFormat.retParam(0, 20006, hashMap);
          }
          tblRiskMonitoringFill.setCreatestaffid(staffUtil.getStaffid());
          tblRiskMonitoringFill.setLinkOrgId(staffUtil.getLinkOrg().getOrgid());
 		//  String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), true, "m.LINKORGID", "m.LINKDEPTID", "m.createstaffid", "m.SECRECTLEVELID", "m.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
          com.github.pagehelper.PageInfo<TblRiskMonitoringFill> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                  .doSelectPageInfo(() -> tblRiskMonitoringFillMapper.getList(tblRiskMonitoringFill,null));
          FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
          //分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
          PageResult<TblRiskMonitoringFill> build = new PageResult<TblRiskMonitoringFill>().build(pageInfo);

          hashMap.put("pageInfo",build);
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
      return ResponseFormat.retParam(1, 200, hashMap);
  }
  
  
  @Override
	public JsonBean details(String id,String token) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> hashMap = new HashMap<>();
		  FiexibleNameAssignment ment=new FiexibleNameAssignment();
		  TblRiskMonitoringFill tbl=null;
		try {
			  tbl=tblRiskMonitoringFillMapper.getEntityById(id);
			  if(tbl!=null){
		        	//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(tbl,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,tbl ); 
			  }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		  return ResponseFormat.retParam(1, 200, tbl);

	}
    
  
	@Override
	public JsonBean reportToLeader(String token, String id) throws Exception {
		// TODO Auto-generated method stub
		  Map<String, Object> hashMap = null;
	        try {
	       hashMap = new HashMap<>();
		   TblStaffUtil staffUtil = userProvider.get();
          if (staffUtil == null){
              return  ResponseFormat.retParam(0, 20006, hashMap);
          }
        for(String s:id.split(",")){
		TblRiskMonitoringFill entity=tblRiskMonitoringFillMapper.selectById(s);
		entity.setReportstatus(1);//修改上报状态为已经上报
		entity.setToReportDate(new Date());
		entity.setReportStaffid(staffUtil.getStaffid().toString());
		entity.setReportStaffName(staffUtil.getRealname());
		tblRiskMonitoringFillMapper.updateById(entity);
        }
	  }catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	    JsonBean jsonBean = new JsonBean(1, "success", "操作成功");
	    return jsonBean;
	}

	
	@Override
	public JsonBean getHzList(String token, String orgid, Integer year, String jd) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> hashMap=new HashMap<String, Object>();
		try {
		 	 TblStaffUtil tblStaffUtil = userProvider.get();
			Calendar calendar = Calendar.getInstance();
			if (year == null) {
				year = calendar.get(Calendar.YEAR);
			}
			if (StringUtils.isBlank(jd)) {
				jd = getCurrentQuarter();
			}
			if(StringUtils.isBlank(orgid)){
				orgid=tblStaffUtil.getCurrentOrg().getOrgid().toString();
			}
			 List<Map<String, Object>> datas = setRefactoringForms(year,orgid,jd);
			  Map<String, Object> d=new HashMap<String, Object>();
				if (datas != null && datas.size() > 0) {
				//	d=datas.get(0);
					//if(d.get("LINKORGID")!=null){
					 Organization org=organizationMapper.selectById(orgid);
				   //  Staff staff=staffMapper.selectById(d.get("CREATESTAFFID").toString());
					hashMap.put("orgName", org.getOrgname());
					//}
					 hashMap.put("staffName",""); //staff.getRealname()
					 hashMap.put("time", ""); //d.get("CREATETIME").toString().substring(0, 10)
				}
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
				//同比增长率
			List<Map<String, Object>> list1 = setOneYear(year,orgid,jd);
			//环比
			List<Map<String, Object>> list2=setLinkRelativeRatio(year,orgid,jd);
			String[] title1=getTitle1(year,orgid,jd);
			Map<String, String> title2=getTitle2(year,orgid,jd);
			list.addAll(list1);
			list.addAll(list2);
			   //具体情况说明
			 List<Map<String, Object>> desList = setRefactoringFormsDes(year,orgid,jd);
			 List<Map<String, Object>> desNewList = new ArrayList<Map<String,Object>>();
			 Map<String, Object> map2=new HashMap<String, Object>();
			 for (Map<String, Object> map : desList) {
				    Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
				    while (iterator.hasNext()) {
				        Map.Entry<String, Object> entry = iterator.next();
				        String newKey =  entry.getKey().replace("DES", "");
				        map2.put(newKey, entry.getValue());
				    }
			 }
			 desNewList.add(map2);
			 list.addAll(desNewList);
			//备注
			 List<TblRiskMonitoringFill> fillList=tblRiskMonitoringFillMapper.getListByYear(year,jd,orgid);
			 Map<BigDecimal, String> notesmap = new HashMap<>();
			 for(TblRiskMonitoringFill b:fillList){
				 notesmap.put(b.getLinkDeptId(), b.getDeptNotes());
 			 }
			 //责任部门
			 BigDecimal versionId=fillList.size()>0?fillList.get(0).getVersionId():new BigDecimal("0");
 			 List<TblRiskMonDictonary> dicList=tblRiskMonDictonaryMapper.getMonDicCol(new BigDecimal(orgid),versionId);
 			 List<Map<String, Object>> deptList=new ArrayList<Map<String,Object>>();
 			 List<Map<String, Object>> notesList=new ArrayList<Map<String,Object>>();
 			 Map<String, Object> map1 = new HashMap<>();
 			 Map<String, Object> noteMap = new HashMap<>();
 			 for(TblRiskMonDictonary b:dicList){
 				 map1.put(b.getCode().toUpperCase(), b.getDeptName());
 				 if(b.getDeptId()!=null){
 					noteMap.put(b.getCode().toUpperCase(),notesmap.get(new BigDecimal(b.getDeptId())));
 				 }else{
 					noteMap.put(b.getCode().toUpperCase(),null);
 				 }
 			 }
 			 deptList.add(map1);
 			 notesList.add(noteMap);
 			 list.addAll(notesList);
 			 list.addAll(deptList);
			hashMap.put("result", list);
			hashMap.put("title1", title1);
			hashMap.put("title2", title2);
   
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		JsonBean jsonBean = new JsonBean(1, "success", hashMap);
	    return jsonBean;
	}
  
	@Override
	public Map<String, Object>  getReportHzList(String token, String orgid, Integer year, String jd) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> hashMap=new HashMap<String, Object>();
		try {
			 TblStaffUtil tblStaffUtil = userProvider.get();
			Calendar calendar = Calendar.getInstance();
			if (year == null) {
				year = calendar.get(Calendar.YEAR);
			}
			if (StringUtils.isBlank(jd)) {
				jd = getCurrentQuarter();
			}
			if(StringUtils.isBlank(orgid)){
				orgid=tblStaffUtil.getCurrentOrg().getOrgid().toString();
			}
			List<Map<String, Object>> datas = setRefactoringForms(year,orgid,jd);
			  Map<String, Object> d=new HashMap<String, Object>();
				if (datas != null && datas.size() > 0) {
					//d=datas.get(0);
					//if(d.get("LINKORGID")!=null){
						 Organization org=organizationMapper.selectById(orgid);
					   //  Staff staff=staffMapper.selectById(d.get("CREATESTAFFID").toString());
						hashMap.put("orgName", org.getOrgname());
					//	}
					 hashMap.put("staffName",""); //staff.getRealname()
					 hashMap.put("time", ""); //d.get("CREATETIME").toString().substring(0, 10)
				}
			List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
				//同比增长率
			List<Map<String, Object>> list1 = setOneYear(year,orgid,jd);
			//环比
			List<Map<String, Object>> list2=setLinkRelativeRatio(year,orgid,jd);
			String[] title1=getTitle1(year,orgid,jd);
			Map<String, String> title2=getTitle2(year,orgid,jd);
			list.addAll(list1);
			list.addAll(list2);
			   //具体情况说明
			 List<Map<String, Object>> desList = setRefactoringFormsDes(year,orgid,jd);
			 List<Map<String, Object>> desNewList = new ArrayList<Map<String,Object>>();
			 Map<String, Object> map2=new HashMap<String, Object>();
			 for (Map<String, Object> map : desList) {
				    Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
				    while (iterator.hasNext()) {
				        Map.Entry<String, Object> entry = iterator.next();
				        String newKey =  entry.getKey().replace("DES", "");
				        map2.put(newKey, entry.getValue());
				    }
			 }
			 desNewList.add(map2);
			 list.addAll(desNewList);
			//备注
			 List<TblRiskMonitoringFill> fillList=tblRiskMonitoringFillMapper.getListByYear(year,jd,orgid);
			 Map<BigDecimal, String> notesmap = new HashMap<>();
			 for(TblRiskMonitoringFill b:fillList){
				 notesmap.put(b.getLinkDeptId(), b.getDeptNotes());
			 }
			 //责任部门
			 BigDecimal versionId=fillList.size()>0?fillList.get(0).getVersionId():new BigDecimal("0");
			 List<TblRiskMonDictonary> dicList=tblRiskMonDictonaryMapper.getMonDicCol(new BigDecimal(orgid),versionId);
			 List<Map<String, Object>> deptList=new ArrayList<Map<String,Object>>();
			 List<Map<String, Object>> notesList=new ArrayList<Map<String,Object>>();
			 Map<String, Object> map1 = new HashMap<>();
			 Map<String, Object> noteMap = new HashMap<>();
			 for(TblRiskMonDictonary b:dicList){
				 map1.put(b.getCode().toUpperCase(), b.getDeptName());
				 if(b.getDeptId()!=null){
					noteMap.put(b.getCode().toUpperCase(),notesmap.get(new BigDecimal(b.getDeptId())));
				 }else{
					noteMap.put(b.getCode().toUpperCase(),null);
				 }
			 }
			 deptList.add(map1);
			 notesList.add(noteMap);
			 list.addAll(notesList);
			 list.addAll(deptList);
			hashMap.put("list", list);
			hashMap.put("title1", title1);
			hashMap.put("title2", title2);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return hashMap;
	}
  
	
	@Override
	public JsonBean monitoringCancel(String token, String id) throws Exception {
		// TODO Auto-generated method stub
		for(String s :id.split(",")){
			TblRiskMonitoringFill fil=tblRiskMonitoringFillMapper.selectById(s);
			fil.setReportstatus(2);
			tblRiskMonitoringFillMapper.updateById(fil);
		}
		 JsonBean jsonBean = new JsonBean(1, "success", "操作成功");
		    return jsonBean;
	}
	
	
	// 获取当前季度
    public static String getCurrentQuarter() {
        Month currentMonth = LocalDate.now().getMonth();
        int monthValue = currentMonth.getValue();
        if (monthValue >= 1 && monthValue <= 3) {
            return "一季度";
        } else if (monthValue >= 4 && monthValue <= 6) {
            return "二季度";
        } else if (monthValue >= 7 && monthValue <= 9) {
            return "三季度";
        } else {
            return "四季度";
        }
    }
    
    
    //集团同比增减率
    List<Map<String, Object>>  setOneYear(Integer year,String orgid,String jd) throws Exception{
		List<Map<String, Object>> fillList=new ArrayList<Map<String,Object>>();
		try {
			//当年E5
			List<Map<String, Object>>  fil= setRefactoringForms(year,orgid,jd);
			 if(fil!=null&&fil.size()>0){
				   fillList.add(fil.get(0));
			 }else{
				 fillList.add(null);
			 }
			//F5 查询年度的前一年
			 fil= setRefactoringForms((year-1),orgid,jd);
			 if(fil!=null&&fil.size()>0){
			   fillList.add(fil.get(0));
			 }else{
				 fillList.add(null);
			 }
			 //2024年三季度同比增减率G5G5=(E5-F5)/F5
			    String fields="zlInteger1,zlInteger2,zlInteger3,zlInteger4,zlInteger5,zlBigdecimal1,zlBigdecimal2,zlBigdecimal3,zlBigdecimal4,zlBigdecimal5,zlBigdecimal6,zlBigdecimal7,zlBigdecimal8,zlBigdecimal9,cwInteger1,cwInteger2,cwInteger3,cwInteger4,cwInteger5,cwBigdecimal1,cwBigdecimal2,cwBigdecimal3,cwBigdecimal4,cwBigdecimal5,cwBigdecimal6,cwBigdecimal7,cwBigdecimal8,cwBigdecimal9,cwBigdecimal10,cwBigdecimal11,scInteger1,scInteger2,scBigdecimal1,scBigdecimal2,scBigdecimal3,scBigdecimal4,scBigdecimal5,scBigdecimal6,yyInteger1,yyInteger2,yyInteger3,yyInteger4,yyInteger5,yyInteger6,yyBigdecimal1,yyBigdecimal2,yyBigdecimal3,yyBigdecimal4,flInteger1,flInteger2,flBigdecimal1";
			    Map<String, Object> fill=new HashMap<String, Object>();
			    if(fillList.get(0)!=null&&fillList.get(1)!=null){
			    for(String s:fields.split(",")){
			    	if(!Objects.isNull(fillList.get(1).get(s.toUpperCase()))&&!isZero(fillList.get(1).get(s.toUpperCase()))){
			    		 fill.put(s.toUpperCase(),onYear(fillList.get(0).get(s.toUpperCase()),fillList.get(1).get(s.toUpperCase()))) ;
			    	}
			     }
			    fillList.add(fill);
			    }else{
			    	fillList.add(null);
			    }
			  
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return fillList;
	}
    
    //重大经营同比增减率
    List<Map<String, Object>>  setZdOneYear(Integer year,String orgid,String jd) throws Exception{
		List<Map<String, Object>> fillList=new ArrayList<Map<String,Object>>();
		try {
			//当年E5
			List<Map<String, Object>>  fil= setZdRefactoringForms(year,orgid,jd);
			 if(fil!=null&&fil.size()>0){
				   fillList.add(fil.get(0));
			 }else{
				 fillList.add(null);
			 }
			//F5 查询年度的前一年
			 fil= setZdRefactoringForms((year-1),orgid,jd);
//			 if(fil!=null&&fil.size()>0){
//			   fillList.add(fil.get(0));
//			 }else{
//				 fillList.add(null);
//			 }
			 //2024年三季度同比增减率G5G5=(E5-F5)/F5
			    String fields="aqBigdecimal1,aqBigdecimal2,aqBigdecimal3,aqBigdecimal4,aqBigdecimal5,aqBigdecimal6,aqBigdecimal11,aqBigdecimal12,aqBigdecimal13,aqBigdecimal14,aqBigdecimal7,aqBigdecimal8,aqBigdecimal9,aqBigdecimal10,cwBigdecimal8,cwBigdecimal12";
			    Map<String, Object> fill=new HashMap<String, Object>();
			    if(fillList.get(0)!=null&&fil.get(0)!=null){
			    for(String s:fields.split(",")){
			    	if(!Objects.isNull(fil.get(0).get(s.toUpperCase()))&&!isZero(fil.get(0).get(s.toUpperCase()))){
			    		 fill.put(s.toUpperCase(),onYear(fillList.get(0).get(s.toUpperCase()),fil.get(0).get(s.toUpperCase()))+"/"+onQoQ(fillList.get(0).get(s.toUpperCase()),fil.get(0).get(s.toUpperCase()))) ;
			    	}else{
			    		 fill.put(s.toUpperCase(),"/"+onQoQ(fillList.get(0).get(s.toUpperCase()),fil.get(0).get(s.toUpperCase()))) ;

			    	}
			     }
			    fillList.add(fill);
			    }else{
			    	fillList.add(null);
			    }
			  
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return fillList;
	}
    
    //集团环比增减率
    List<Map<String, Object>>  setLinkRelativeRatio(Integer year,String orgid,String jd) throws Exception{
		List<Map<String, Object>> fillList=new ArrayList<Map<String,Object>>();
	    String fields="zlInteger1,zlInteger2,zlInteger3,zlInteger4,zlInteger5,zlBigdecimal1,zlBigdecimal2,zlBigdecimal3,zlBigdecimal4,zlBigdecimal5,zlBigdecimal6,zlBigdecimal7,zlBigdecimal8,zlBigdecimal9,cwInteger1,cwInteger2,cwInteger3,cwInteger4,cwInteger5,cwBigdecimal1,cwBigdecimal2,cwBigdecimal3,cwBigdecimal4,cwBigdecimal5,cwBigdecimal6,cwBigdecimal7,cwBigdecimal8,cwBigdecimal9,cwBigdecimal10,cwBigdecimal11,scInteger1,scInteger2,scBigdecimal1,scBigdecimal2,scBigdecimal3,scBigdecimal4,scBigdecimal5,scBigdecimal6,yyInteger1,yyInteger2,yyInteger3,yyInteger4,yyInteger5,yyInteger6,yyBigdecimal1,yyBigdecimal2,yyBigdecimal3,yyBigdecimal4,flInteger1,flInteger2,flBigdecimal1";
		String field2s="zlBigdecimal3,zlBigdecimal6,zlBigdecimal7,zlBigdecimal8,zlBigdecimal9,cwBigdecimal1,cwBigdecimal9,scInteger1,scBigdecimal1,scInteger2,scBigdecimal2,yyBigdecimal2,yyBigdecimal3,yyBigdecimal4,yyInteger2,yyInteger3,yyInteger4,flInteger1";
	    try {
			if(jd.equals("一季度")){
				//一季度
				List<Map<String, Object>> firstQuarter =setRefactoringForms(year, orgid,jd);
				Map<String, Object> first=new HashMap<String, Object>();
				if (firstQuarter != null && firstQuarter.size() > 0) {
					first=firstQuarter.get(0);
				} 
				//#{year-1}年四季度末值
				List<Map<String, Object>>  fil = setRefactoringForms((year-1), orgid, "四季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年一季度 产生额
				Map<String, Object> fill = new HashMap<String, Object>();
				if(first!=null&&fillList.get(0)!=null){
				for (String s : fields.split(",")) {
//					if(field2s.indexOf(s)>-1){
//						fill.put(s.toUpperCase(),first.get(s.toUpperCase()));
//					}else{
						fill.put(s.toUpperCase(),
								onQoQ(first.get(s.toUpperCase()), fillList.get(0).get(s.toUpperCase())));
					//}
				}
				fillList.add(fill);
				}else{
			    fillList.add(null);
				}
				//#{year-1}年三季度末值
				  fil = setRefactoringForms((year-1), orgid, "三季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year-1}年四季度产生额
				  fill = new HashMap<String, Object>();
				  if(fillList.get(0)!=null&&fillList.get(2)!=null){
				    for (String s : fields.split(",")) {
//				    	if(field2s.indexOf(s)>-1){
//				    		fill.put(s.toUpperCase(),fillList.get(0).get(s.toUpperCase()));
//				    	}else{
						fill.put(s.toUpperCase(),
								onQoQ(fillList.get(0).get(s.toUpperCase()), fillList.get(2).get(s.toUpperCase())));
					 //}
				     }
				 fillList.add(fill);
				 }else{
					 fillList.add(null);
				  }
				//#{year}年一季度 环比增减率
				fill = new HashMap<String, Object>();
				if(fillList.get(1)!=null&&fillList.get(3)!=null){
				   for (String s : fields.split(",")) {
					if (!Objects.isNull(fillList.get(3).get(s.toUpperCase()))&&!isZero(fillList.get(3).get(s.toUpperCase()))) {
						fill.put(s.toUpperCase(),
								onYear(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase())));
					}
			 	   }
			   	fillList.add(fill);
				}else{
					fillList.add(null);
				}
			}else if(jd.equals("二季度")){ //截至2024年 二季度末 H5
				//#{year}二季度末数据
				List<Map<String, Object>> secondQuarter = setRefactoringForms(year, orgid,jd);
				Map<String, Object> second=new HashMap<String, Object>();
				if (secondQuarter != null && secondQuarter.size() > 0) {
					second=secondQuarter.get(0);
				}
				//#{year}年一季度末值
				List<Map<String, Object>>  fil = setRefactoringForms(year, orgid, "一季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年二季度 产生额
				Map<String, Object> fill = new HashMap<String, Object>();
				if(second!=null&&fillList.get(0)!=null){
				for (String s : fields.split(",")) {
//					if(field2s.indexOf(s)>-1){
//						fill.put(s.toUpperCase(),second.get(s.toUpperCase()));
//					}else{
						fill.put(s.toUpperCase(),
								onQoQ(second.get(s.toUpperCase()), fillList.get(0).get(s.toUpperCase())));
					//}
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
				//#{year-1}年四季度末值
				  fil = setRefactoringForms((year-1), orgid, "四季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年一季度 产生额
				  fill = new HashMap<String, Object>();
				if(fillList.get(0)!=null&&fillList.get(2)!=null){
				  for (String s : fields.split(",")) {
//					  if(field2s.indexOf(s)>-1){
//							fill.put(s.toUpperCase(),fillList.get(0).get(s.toUpperCase()));
//						}else{
						fill.put(s.toUpperCase(),
								onQoQ(fillList.get(0).get(s.toUpperCase()), fillList.get(2).get(s.toUpperCase())));
					//}
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
				//#{year}年一季度 环比增减率
				fill = new HashMap<String, Object>();
				if(fillList.get(1)!=null&&fillList.get(3)!=null){
				for (String s : fields.split(",")) {
					if (!Objects.isNull(fillList.get(3).get(s.toUpperCase()))&&!isZero(fillList.get(3).get(s.toUpperCase()))) {
						fill.put(s.toUpperCase(),
								onYear(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase())));
					}
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
			}else if(jd.equals("三季度")){ 
				//#{year}三季度末数据
				List<Map<String, Object>> thirdQuarter = setRefactoringForms(year, orgid,jd);
				Map<String, Object> third=new HashMap<String, Object>();
				if (thirdQuarter != null && thirdQuarter.size() > 0) {
					third=thirdQuarter.get(0);
				}
				//#{year}年二季度末值
				List<Map<String, Object>>  fil = setRefactoringForms(year, orgid, "二季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年三季度 产生额
				Map<String, Object> fill = new HashMap<String, Object>();
			if(third!=null&&fillList.get(0)!=null){
				for (String s : fields.split(",")) {
//						  if(field2s.indexOf(s)>-1){
//								fill.put(s.toUpperCase(),third.get(s.toUpperCase()));
//							}else{					
								fill.put(s.toUpperCase(),
								onQoQ(third.get(s.toUpperCase()), fillList.get(0).get(s.toUpperCase())));
					// }
				}
				fillList.add(fill);
			}else{
				fillList.add(null);
			}
				//#{year}年一季度末值
				  fil = setRefactoringForms(year, orgid, "一季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年二季度 产生额
				  fill = new HashMap<String, Object>();
				if(fillList.get(0)!=null&&fillList.get(2)!=null){
				  for (String s : fields.split(",")) {
//					  if(field2s.indexOf(s)>-1){
//							fill.put(s.toUpperCase(),fillList.get(0).get(s.toUpperCase()));
//						}else{
						fill.put(s.toUpperCase(),
								onQoQ(fillList.get(0).get(s.toUpperCase()), fillList.get(2).get(s.toUpperCase())));
					// }
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
				//#{year}年三季度 环比增减率
				fill = new HashMap<String, Object>();
				if(fillList.get(1)!=null&&fillList.get(3)!=null){
				for (String s : fields.split(",")) {
					if (!Objects.isNull(fillList.get(3).get(s.toUpperCase()))&&!isZero(fillList.get(3).get(s.toUpperCase()))) {
						fill.put(s.toUpperCase(),
								onYear(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase())));
					}
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
				
			}else if(jd.equals("四季度")){
				//#{year}四季度末数据
				List<Map<String, Object>> fourthQuarter = setRefactoringForms(year, orgid,jd);
				Map<String, Object> fourth=new HashMap<String, Object>();
				if (fourthQuarter != null && fourthQuarter.size() > 0) {
					fourth=fourthQuarter.get(0);
				}
				//#{year}年三季度末值
				List<Map<String, Object>>  fil = setRefactoringForms(year, orgid, "三季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年四季度 产生额
				Map<String, Object> fill = new HashMap<String, Object>();
				if(fourth!=null&& fillList.get(0)!=null){
				for (String s : fields.split(",")) {
//					  if(field2s.indexOf(s)>-1){
//							fill.put(s.toUpperCase(),fourth.get(s.toUpperCase()));
//						}else{					
							fill.put(s.toUpperCase(),
								onQoQ(fourth.get(s.toUpperCase()), fillList.get(0).get(s.toUpperCase())));
					// }
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
				//#{year}年二季度末值
				  fil = setRefactoringForms(year, orgid, "二季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年三季度 产生额
				  fill = new HashMap<String, Object>();
				if(fillList.get(0)!=null&&fillList.get(2)!=null){
				  for (String s : fields.split(",")) {
//					  if(field2s.indexOf(s)>-1){
//							fill.put(s.toUpperCase(),fillList.get(0).get(s.toUpperCase()));
//						}else{							
							fill.put(s.toUpperCase(),
								onQoQ(fillList.get(0).get(s.toUpperCase()), fillList.get(2).get(s.toUpperCase())));
					// }
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
				//#{year}年四季度 环比增减率
				fill = new HashMap<String, Object>();
				if(fillList.get(1)!=null&&fillList.get(3)!=null){
				for (String s : fields.split(",")) {
					if (!Objects.isNull(fillList.get(3).get(s.toUpperCase()))&&!isZero(fillList.get(3).get(s.toUpperCase()))) {
						fill.put(s.toUpperCase(),
								onYear(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase())));
					}
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return fillList;
	}
	
	public boolean isZero(Object value) {
	    if (value == null) {
	        return false;
	    }
	    
	    if (value instanceof Integer) {
	        return ((Integer) value) == 0;
	    }
	    
	    if (value instanceof BigDecimal) {
	        return ((BigDecimal) value).compareTo(BigDecimal.ZERO) == 0;
	    }
	    if (value instanceof String &&!((String) value).isEmpty()) {
	        return Double.parseDouble(value.toString())==0.00;
	    }
	    return false;
	}
	
	public String onYear(Object value,Object value2) {
	    if (value instanceof Integer) {
	        return (double)((Integer) value-(Integer) value2)/(Integer) value2*100+"%";
	    }else if (value instanceof BigDecimal) {
	    	BigDecimal value1=(BigDecimal)value;
	    	BigDecimal value3=(BigDecimal)value2;
	    	if(!value.equals("")&&value2!=null&&!Objects.isNull(value3)){
	         return  value1.subtract(value3)
	        	    .divide(value3, 4, RoundingMode.HALF_UP)
	        	    .multiply(new BigDecimal("100"))
	        	    .setScale(2, RoundingMode.HALF_UP)+"%";
	    	}else{
	    		return "";
	    	}
	    }else if(value instanceof String){
	    	 String str1 = (String) value;
	         String str2 = (String) value2;
	         if (str1.trim().isEmpty() || str2.trim().isEmpty()) {
	             return "";
	         }
	             // 数值转换
	             double num1 = Double.parseDouble(str1.trim());
	             double num2 = Double.parseDouble(str2.trim());
	             
	             // 除数零检查
	             if (num2 == 0) {
	                 return "";
	             }
	             // 计算并格式化
	             double ratio = (num1 - num2) / num2;
	             DecimalFormat df = new DecimalFormat("#0.00");
	             return df.format(ratio)+"%";
	       
	    	}else{
	    		return "";
	    	}
	}
	
	public String onQoQ(Object value,Object value2) {
	    
	    if (value instanceof Integer) {
	        return ((Integer) value-(Integer) value2)+"";
	    }
	    
	    if (value instanceof BigDecimal) {
	    	BigDecimal value1=(BigDecimal)value;
	    	if(value==null){
	    		value1=new BigDecimal(0);
	    	}
	    	BigDecimal value3=(BigDecimal)value2;
	    	if(value2==null){
	    		value3=new BigDecimal(0); 
	    	}
	    	System.out.println(value1.subtract(value3).setScale(2, RoundingMode.HALF_UP)+"");
	         return  value1.subtract(value3).setScale(2, RoundingMode.HALF_UP)+"";
	    }
	    
	      if(value instanceof String){
	    	 String str1 = (String) value;
	         String str2 = (String) value2;
	         if (str1.trim().isEmpty() || str2.trim().isEmpty()) {
	             return "";
	         }
	             // 数值转换
	             double num1 = Double.parseDouble(str1.trim());
	             double num2 = Double.parseDouble(str2.trim());
	             // 计算并格式化
	             double ratio = (num1 - num2);
	             return ratio+"";
	       
	    	}
	    return "";
	}
	public static String sumObjects(Object obj1, Object obj2, Object obj3) {
        BigDecimal sum = BigDecimal.ZERO;
        
        Object[] objects = {obj1, obj2, obj3};
        
        for (Object obj : objects) {
            if (obj == null) {
                continue;
            }
            
            if (obj instanceof Integer) {
                sum = sum.add(new BigDecimal((Integer) obj));
            } else if (obj instanceof Long) {
                sum = sum.add(new BigDecimal((Long) obj));
            } else if (obj instanceof Double) {
                sum = sum.add(BigDecimal.valueOf((Double) obj));
            } else if (obj instanceof Float) {
                sum = sum.add(BigDecimal.valueOf((Float) obj));
            } else if (obj instanceof BigDecimal) {
                sum = sum.add((BigDecimal) obj);
            } else if (obj instanceof String) {
            	 String str = (String) obj;
                 if (str.trim().isEmpty()) {
                     continue; // 空字符串或空白字符串当作0处理
                 }
            	try {
                    sum = sum.add(new BigDecimal((String) obj));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("字符串无法转换为数字: " + obj);
                }
            } else {
                throw new IllegalArgumentException("不支持的类型: " + obj.getClass().getName());
            }
        }
        
        // 保留两位小数，四舍五入
        return sum.setScale(2, RoundingMode.HALF_UP).toString();
    }
	
	
	
	Map<String, Object>  setSecondQuarter(Integer year,String orgid) throws Exception{
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return map;
	}
	
	Map<String, Object>  setThirdQuarter(Integer year,String orgid) throws Exception{
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return map;
	}
	
	Map<String, Object>  setFourthQuarter(Integer year,String orgid) throws Exception{
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return map;
	}
	
	
	@Override
    public   void exportByTemplate(String templatePath,  Map<String, Object> dataList, ServletOutputStream outputStream,String jd,String orgid,Integer year) throws IOException {
		 FileOutputStream stream=null;
		try (InputStream templateStream =   TblRiskMonitoringFillServiceImpl.class.getClassLoader().getResourceAsStream(templatePath)) {
        	if (templateStream == null) {
                throw new IllegalArgumentException("模板文件不存在：" + templatePath);
            }
          Workbook  workbook = new HSSFWorkbook(templateStream);
          workbook.setSheetName(0, "第"+jd);
          Sheet sheet = workbook.getSheetAt(0);
          CellStyle cellStyle = workbook.createCellStyle();
          // 创建边框对象
          CreationHelper creationHelper = workbook.getCreationHelper();
          // 设置边框样式为细边框
          cellStyle.setBorderBottom(new Short("1"));
          cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
          cellStyle.setBorderLeft(new Short("1"));
          cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
          cellStyle.setBorderRight(new Short("1"));
          cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
          cellStyle.setBorderTop(new Short("1"));
          cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
          CellStyle cellStyle2 = workbook.createCellStyle();
          cellStyle2.setWrapText(true); // 启用自动换行
          cellStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中（可选）
          cellStyle2.setAlignment(CellStyle.ALIGN_GENERAL);
          Row row = sheet.getRow(1);  
          if (row == null) {
              row = sheet.createRow(1);  
          }
          Cell cell0 = row.getCell(0);  
          if (cell0 == null) {
              cell0 = row.createCell(0);  
          }
          cell0.setCellValue(dataList.get("orgName").toString()+"第"+year+"年第"+jd+"风险分类监测指标体系表（责任部门填报）"); 
           row = sheet.getRow(2);  
             cell0 = row.getCell(0);  
           if (cell0 == null) {
               cell0 = row.createCell(0);  
           }
           cell0.setCellValue("填报单位："+dataList.get("orgName").toString()); 
           cell0 = row.getCell(3);  
           if (cell0 == null) {
               cell0 = row.createCell(0);  
           }
           cell0.setCellValue("填报时间："+dataList.get("time").toString());
           cell0 = row.getCell(8);  
           if (cell0 == null) {
               cell0 = row.createCell(0);  
           }
           cell0.setCellValue("填报人："+dataList.get("staffName").toString());
           int cellLength=15;   
           int data=0;
           int title=0;
           Font font1 = workbook.createFont();
           font1.setColor(IndexedColors.RED.getIndex()); // 设置第一种颜色为红色
           Font font2 = workbook.createFont();
           font2.setColor(IndexedColors.BLUE.getIndex()); // 设置第二种
			List<Map<String, Object>> list=(List<Map<String, Object>>) dataList.get("list");
	    //标题1循环
			String[] title1=(String[])dataList.get("title1");
			for(int i=0;i<8;i++){
				row = sheet.getRow(4);
				Cell cell = row.createCell(4+i);
				cell.setCellValue(title1[i]);
				cell.setCellStyle(cellStyle2);
			}
		//标题2循环
			Map<String, String> title2 = (Map<String, String>) dataList.get("title2");
			int r = 0;
			StringBuffer buff = new StringBuffer();
			row = sheet.getRow(5);
			Cell cell = row.getCell(4);
			String text = cell.getStringCellValue();
			buff.append(text.replace("${key1}${key2}", title2.get("key1") + title2.get("key2")));
			HSSFRichTextString richString = new HSSFRichTextString(buff.toString());
			richString.applyFont(0, title2.get("key1").length(), font1);
			richString.applyFont(title2.get("key1").length(),(title2.get("key1").length()+title2.get("key2").length()), font2);
			cell.setCellValue(richString);
			buff = new StringBuffer();
			cell = row.getCell(5);
			text = cell.getStringCellValue();
			buff.append(text.replace("${key3}${key4}", title2.get("key3") + title2.get("key4")));
			richString = new HSSFRichTextString(buff.toString());
			richString.applyFont(0, (title2.get("key3").length()), font1);
			richString.applyFont(title2.get("key3").length(),(title2.get("key3").length() + title2.get("key4").length()+1), font2);
			cell.setCellValue(richString);
			buff = new StringBuffer();
			cell = row.getCell(7);
			text = cell.getStringCellValue();
			buff.append(text.replace("${key5}${key6}", title2.get("key5") + title2.get("key6")));
			richString = new HSSFRichTextString(buff.toString());
			richString.applyFont(0, (title2.get("key5").length()), font1);
			richString.applyFont(title2.get("key5").length(),
					(title2.get("key5").length() + title2.get("key6").length()), font2);
			cell.setCellValue(richString);
			buff = new StringBuffer();
			cell = row.getCell(9);
			text = cell.getStringCellValue();
			buff.append(text.replace("${key7}${key8}", title2.get("key7") + title2.get("key8")));
			richString = new HSSFRichTextString(buff.toString());
			richString.applyFont(0, title2.get("key7").length(), font1);
			richString.applyFont(title2.get("key7").length(),
					(title2.get("key7").length() + title2.get("key8").length()), font2);
			cell.setCellValue(richString);
        //数据内容循环
			for(int c=4;c<cellLength;c++){
				 Map<String, Object> map = list.get(data);
				 if(map!=null){
				row = sheet.getRow(6);
				 cell = row.createCell(c);
				cell.setCellValue(map.get("ZLSTRING1")!=null?map.get("ZLSTRING1").toString():"");
				cell.setCellStyle(cellStyle);
				row = sheet.getRow(7);
				cell = row.createCell(c);
				cell.setCellValue(map.get("ZLSTRING2")!=null?map.get("ZLSTRING2").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(8);
				cell = row.createCell(c);
				cell.setCellValue(map.get("ZLINTEGER1")!=null?map.get("ZLINTEGER1").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(9);
				cell = row.createCell(c);
				cell.setCellValue(map.get("ZLBIGDECIMAL1")!=null?map.get("ZLBIGDECIMAL1").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(10);
				cell = row.createCell(c);
				cell.setCellValue(map.get("ZLBIGDECIMAL2")!=null?map.get("ZLBIGDECIMAL2").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(11);
				cell = row.createCell(c);
				cell.setCellValue(map.get("ZLINTEGER2")!=null?map.get("ZLINTEGER2").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(12);
				cell = row.createCell(c);
				cell.setCellValue(map.get("ZLBIGDECIMAL3")!=null?map.get("ZLBIGDECIMAL3").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(13);
				cell = row.createCell(c);
				cell.setCellValue(map.get("ZLINTEGER3")!=null?map.get("ZLINTEGER3").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(14);
				cell = row.createCell(c);
				cell.setCellValue(map.get("ZLBIGDECIMAL4")!=null?map.get("ZLBIGDECIMAL4").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(15);
				cell = row.createCell(c);
				cell.setCellValue(map.get("ZLINTEGER4")!=null?map.get("ZLINTEGER4").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(16);
				cell = row.createCell(c);
				cell.setCellValue(map.get("ZLBIGDECIMAL5")!=null?map.get("ZLBIGDECIMAL5").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(17);
				cell = row.createCell(c);
				cell.setCellValue(map.get("ZLBIGDECIMAL6")!=null?map.get("ZLBIGDECIMAL6").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(18);
				cell = row.createCell(c);
				cell.setCellValue(map.get("ZLBIGDECIMAL7")!=null?map.get("ZLBIGDECIMAL7").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(19);
				cell = row.createCell(c);
				cell.setCellValue(map.get("ZLBIGDECIMAL8")!=null?map.get("ZLBIGDECIMAL8").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(20);
				cell = row.createCell(c);
				cell.setCellValue(map.get("ZLBIGDECIMAL9")!=null?map.get("ZLBIGDECIMAL9").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(21);
				cell = row.createCell(c);
				cell.setCellValue(map.get("ZLINTEGER5")!=null?map.get("ZLINTEGER5").toString():"");
				cell.setCellStyle(cellStyle);

				//以上战略
				row = sheet.getRow(22);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWBIGDECIMAL1")!=null?map.get("CWBIGDECIMAL1").toString():""); 
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(23);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWINTEGER1")!=null?map.get("CWINTEGER1").toString():""); 
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(24);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWBIGDECIMAL2")!=null?map.get("CWBIGDECIMAL2").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(25);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWINTEGER2")!=null?map.get("CWINTEGER2").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(26);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWBIGDECIMAL3")!=null?map.get("CWBIGDECIMAL3").toString():""); 
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(27);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWINTEGER3")!=null?map.get("CWINTEGER3").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(28);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWBIGDECIMAL4")!=null?map.get("CWBIGDECIMAL4").toString():""); 
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(29);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWINTEGER4")!=null?map.get("CWINTEGER4").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(30);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWBIGDECIMAL5")!=null?map.get("CWBIGDECIMAL5").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(31);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWINTEGER5")!=null?map.get("CWINTEGER5").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(32);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWBIGDECIMAL6")!=null?map.get("CWBIGDECIMAL6").toString():""); 
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(33);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWBIGDECIMAL7")!=null?map.get("CWBIGDECIMAL7").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(34);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWBIGDECIMAL8")!=null?map.get("CWBIGDECIMAL8").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(35);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWBIGDECIMAL9")!=null?map.get("CWBIGDECIMAL9").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(36);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWBIGDECIMAL10")!=null?map.get("CWBIGDECIMAL10").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(37);
				cell = row.createCell(c);
				cell.setCellValue(map.get("CWBIGDECIMAL11")!=null?map.get("CWBIGDECIMAL11").toString():"");
				cell.setCellStyle(cellStyle);

				//以上财务
				row = sheet.getRow(38);
				cell = row.createCell(c);
				cell.setCellValue(map.get("SCINTEGER1")!=null?map.get("SCINTEGER1").toString():""); 
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(39);
				cell = row.createCell(c);
				cell.setCellValue(map.get("SCBIGDECIMAL1")!=null?map.get("SCBIGDECIMAL1").toString():""); 
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(40);
				cell = row.createCell(c);
				cell.setCellValue(map.get("SCINTEGER2")!=null?map.get("SCINTEGER2").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(41);
				cell = row.createCell(c);
				cell.setCellValue(map.get("SCBIGDECIMAL2")!=null?map.get("SCBIGDECIMAL2").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(42);
				cell = row.createCell(c);
				cell.setCellValue(map.get("SCBIGDECIMAL3")!=null?map.get("SCBIGDECIMAL3").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(43);
				cell = row.createCell(c);
				cell.setCellValue(map.get("SCBIGDECIMAL4")!=null?map.get("SCBIGDECIMAL4").toString():""); 
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(44);
				cell = row.createCell(c);
				cell.setCellValue(map.get("SCBIGDECIMAL5")!=null?map.get("SCBIGDECIMAL5").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(45);
				cell = row.createCell(c);
				cell.setCellValue(map.get("SCBIGDECIMAL6")!=null?map.get("SCBIGDECIMAL6").toString():""); 
				cell.setCellStyle(cellStyle);

				//以上市场风险
				row = sheet.getRow(46);
				cell = row.createCell(c);
				cell.setCellValue(map.get("YYINTEGER1")!=null?map.get("YYINTEGER1").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(47);
				cell = row.createCell(c);
				cell.setCellValue(map.get("YYBIGDECIMAL1")!=null?map.get("YYBIGDECIMAL1").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(48);
				cell = row.createCell(c);
				cell.setCellValue(map.get("YYBIGDECIMAL2")!=null?map.get("YYBIGDECIMAL2").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(49);
				cell = row.createCell(c);
				cell.setCellValue(map.get("YYBIGDECIMAL3")!=null?map.get("YYBIGDECIMAL3").toString():""); 
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(50);
				cell = row.createCell(c);
				cell.setCellValue(map.get("YYBIGDECIMAL4")!=null?map.get("YYBIGDECIMAL4").toString():""); 
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(51);
				cell = row.createCell(c);
				cell.setCellValue(map.get("YYINTEGER2")!=null?map.get("YYINTEGER2").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(52);
				cell = row.createCell(c);
				cell.setCellValue(map.get("YYINTEGER3")!=null?map.get("YYINTEGER3").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(53);
				cell = row.createCell(c);
				cell.setCellValue(map.get("YYINTEGER4")!=null?map.get("YYINTEGER4").toString():""); 
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(54);
				cell = row.createCell(c);
				cell.setCellValue(map.get("YYINTEGER5")!=null?map.get("YYINTEGER5").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(55);
				cell = row.createCell(c);
				cell.setCellValue(map.get("YYINTEGER6")!=null?map.get("YYINTEGER6").toString():"");
				cell.setCellStyle(cellStyle);

				//以上运营
				row = sheet.getRow(56);
				cell = row.createCell(c);
				cell.setCellValue(map.get("FLINTEGER1")!=null?map.get("FLINTEGER1").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(57);
				cell = row.createCell(c);
				cell.setCellValue(map.get("FLINTEGER2")!=null?map.get("FLINTEGER2").toString():"");
				cell.setCellStyle(cellStyle);

				row = sheet.getRow(58);
				cell = row.createCell(c);
				cell.setCellValue(map.get("FLBIGDECIMAL1")!=null?map.get("FLBIGDECIMAL1").toString():"");
				cell.setCellStyle(cellStyle);

				//以上法律
				row = sheet.getRow(59);
				cell = row.createCell(c);
				cell.setCellValue(map.get("QTSTRING1")!=null?map.get("QTSTRING1").toString():"");
				cell.setCellStyle(cellStyle);


				//以上其他
				 }
				data++;
           }
//				File file = new File("C://Users//cj//Desktop//aaaaaaa1.xls");
//				file.createNewFile();
//				  stream    = FileUtils.openOutputStream(file);
				workbook.write(outputStream);
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}finally{
			try {
				outputStream.flush();
				outputStream.close();
				//stream.flush();
				//stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
	
	public String getTitle3(Integer year,String orgid,String jd) throws Exception{
		String title="";
		try {
			if(jd.equals("一季度")){
				title=year+".1.1-"+year+".3.31";
			}else if(jd.equals("二季度")){
				title=year+".1.1-"+year+".6.30";
			}else if(jd.equals("三季度")){
				title=year+".1.1-"+year+".9.30";
			}else if(jd.equals("四季度")){
				title=year+".1.1-"+year+".12.31";
              }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return title;
	}
	public Map<String, String> getTitle2(Integer year,String orgid,String jd) throws Exception{
		Map<String, String> title=new HashMap<String, String>();
		try {
			if(jd.equals("一季度")){
				title.put("key1", "①标红指标按区间值填列，填报区间为：("+year+".1.1-"+year+".3.31)");
				title.put("key2", "②未标色指标按时点值填列，填截至"+year+".3.31数据");
				title.put("key3", "①标红指标按区间值填列，填报区间为：("+(year-1)+".1.1-"+(year-1)+".3.31)");
				title.put("key4", "②未标色指标按时点值填列，填截至"+(year-1)+".3.31数据");
				title.put("key5", "①标红指标按区间值填列，填报区间为：("+(year-1)+".1.1-"+(year-1)+".12.31)");
				title.put("key6", "②未标色指标按时点值填列，填截至"+(year-1)+".12.31数据");
				title.put("key7", "①标红指标按区间值填列，填报区间为：("+(year-1)+".1.1-"+(year-1)+".9.30)");
				title.put("key8", "②未标色指标按时点值填列，填截至"+(year-1)+".9.30数据");
			}else if(jd.equals("二季度")){
				title.put("key1", "①标红指标按区间值填列，填报区间为：("+year+".1.1-"+year+".6.30)");
				title.put("key2", "②未标色指标按时点值填列，填截至"+year+".6.30数据");
				title.put("key3", "①标红指标按区间值填列，填报区间为：("+(year-1)+".1.1-"+(year-1)+".6.30)");
				title.put("key4", "②未标色指标按时点值填列，填截至"+(year-1)+".6.30数据");
				title.put("key5", "①标红指标按区间值填列，填报区间为：("+year+".1.1-"+year+".3.31)");
				title.put("key6", "②未标色指标按时点值填列，填截至"+year+".3.31)");
				title.put("key7","①标红指标按区间值填列，填报区间为：("+(year-1)+".1.1-"+(year-1)+".12.31)");
				title.put("key8", "②未标色指标按时点值填列，填截至"+(year-1)+".12.31数据");
			}else if(jd.equals("三季度")){
				title.put("key1", "①标红指标按区间值填列，填报区间为：("+year+".1.1-"+year+".9.30)");
				title.put("key2", "②未标色指标按时点值填列，填截至"+year+".9.30数据");
				title.put("key3", "①标红指标按区间值填列，填报区间为：("+(year-1)+".1.1-"+(year-1)+".9.30)");
				title.put("key4", "②未标色指标按时点值填列，填截至"+(year-1)+".9.30数据");
				title.put("key5", "①标红指标按区间值填列，填报区间为：("+year+".1.1-"+year+".6.30)");
				title.put("key6", "②未标色指标按时点值填列，填截至"+year+".6.30数据");
				title.put("key7","①标红指标按区间值填列，填报区间为：("+year+".1.1-"+year+".3.31)");
				title.put("key8", "②未标色指标按时点值填列，填截至"+year+".3.31数据");
			}else if(jd.equals("四季度")){
				title.put("key1", "①标红指标按区间值填列，填报区间为：("+year+".1.1-"+year+".12.31)");
				title.put("key2", "②未标色指标按时点值填列，填截至"+year+".12.31数据");
				title.put("key3", "①标红指标按区间值填列，填报区间为：("+(year-1)+".1.1-"+(year-1)+".12.31)");
				title.put("key4", "②未标色指标按时点值填列，填截至"+(year-1)+".12.31数据");
				title.put("key5", "①标红指标按区间值填列，填报区间为：("+year+".1.1-"+year+".9.30)");
				title.put("key6", "②未标色指标按时点值填列，填截至"+year+".9.30数据");
				title.put("key7","①标红指标按区间值填列，填报区间为：("+year+".1.1-"+year+".6.30)");
				title.put("key8", "②未标色指标按时点值填列，填截至"+year+".6.30数据");
              }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return title;
	}
	public String[] getTitle1(Integer year,String orgid,String jd) throws Exception{
		String[] title=null;
		try {
			if(jd.equals("一季度")){
				title=new String[]{"截至"+year+"年一季度末E5","截至"+(year-1)+"年一季度末F5",year+"年一季度同比增减率G5","截至"+(year-1)+"年四季度末H5",year+"年一季度产生额I5","截至"+(year-1)+"年三季度末J5",year+"年四季度产生额K5",year+"年一季度环比增减率L5"};
			}else if(jd.equals("二季度")){
				title=new String[]{"截至"+year+"年二季度末E5","截至"+(year-1)+"年二季度末F5",year+"年二季度同比增减率G5","截至"+year+"年一季度末H5",year+"年二季度产生额I5","截至"+(year-1)+"年四季度末J5",year+"年一季度产生额K5",year+"年二季度环比增减率L5"};
			}else if(jd.equals("三季度")){
				title=new String[]{"截至"+year+"年三季度末E5","截至"+(year-1)+"年三季度末F5",year+"年三季度同比增减率G5","截至"+year+"年二季度末H5",year+"年三季度产生额I5","截至"+year+"年一季度末J5",year+"年二季度产生额K5",year+"年三季度环比增减率L5"};
			}else if(jd.equals("四季度")){
				title=new String[]{"截至"+year+"年四季度末E5","截至"+(year-1)+"年四季度末F5",year+"年四季度同比增减率G5","截至"+year+"年三季度末H5",year+"年四季度产生额I5","截至"+year+"年二季度末J5",year+"年三季度产生额K5",year+"年四季度环比增减率L5"};
              }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return title;
	}
	public String getQuarterDates(int year, int quarter) {
	    // 检查季度是否有效
	    if (quarter < 1 || quarter > 4) {
	        throw new IllegalArgumentException("Quarter must be between 1 and 4");
	    }
	    // 计算季度的开始月份和结束月份
	    int startMonth = (quarter - 1) * 3 + 1; // 1, 4, 7, 10
	    int endMonth = startMonth + 2; // 2, 5, 8, 11
	    // 获取季度的开始和结束日期
	    YearMonth startYearMonth = YearMonth.of(year, startMonth);
	    LocalDate startDate = startYearMonth.atDay(1); // 该月的第一天
	    LocalDate endDate = startYearMonth.plusMonths(3).atEndOfMonth(); // 下一个季度的开始月份前一天
	    return startDate+"-"+endDate;
	}
	
	
	@Override
	public JsonBean getRiskMonDeptList(String token, String id) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> hashMap = new HashMap<>();
		try {
			//先根据填报ID获取对应的部门设置，这是为了查询到字典及部门匹配之前的关联;提供前端铺设
			TblRiskMonitoringFill fill=tblRiskMonitoringFillMapper.selectById(id);
			if(fill!=null){
			List<TblRiskMonDictonary> list=tblRiskMonDictonaryMapper.getDicByDept(fill.getLinkDeptId(), fill.getLinkOrgId(),fill.getVersionId());
			hashMap.put("data", list);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1, 200, hashMap);
	}
	
	
	
	public List<Map<String, Object>> setRefactoringForms(Integer year,String orgid,String jd) throws Exception{
		List<Map<String, Object>> fill=new ArrayList<Map<String,Object>>();
		try {
			List<TblRiskMonitoringFill> fillList=tblRiskMonitoringFillMapper.getListByYear(year,jd,orgid);
			fill = RiskDataMerger.mergeFillData(fillList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return  fill;
	}
	
	
	public List<Map<String, Object>> setZdRefactoringForms(Integer year,String orgid,String jd) throws Exception{
		List<Map<String, Object>> fill=new ArrayList<Map<String,Object>>();
		try {
			List<TblRiskMonitoringFill> fillList=tblRiskMonitoringFillMapper.getListByYear(year,jd,orgid);
			fill = RiskDataMerger.mergeZdFillData(fillList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return  fill;
	}
	
	
	public List<Map<String, Object>> setRefactoringFormsDes(Integer year,String orgid,String jd) throws Exception{
		List<Map<String, Object>> fill=new ArrayList<Map<String,Object>>();
		try {
			List<TblRiskMonitoringFill> fillList=tblRiskMonitoringFillMapper.getListByYear(year,jd,orgid);
			fill = RiskDataMerger.mergeFillDataDes(fillList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return  fill;
	}
	
	//重大经营类
	public List<Map<String, Object>> setZdRefactoringFormsDes(Integer year,String orgid,String jd) throws Exception{
		List<Map<String, Object>> fill=new ArrayList<Map<String,Object>>();
		try {
			List<TblRiskMonitoringFill> fillList=tblRiskMonitoringFillMapper.getListByYear(year,jd,orgid);
			fill = RiskDataMerger.mergeFillDataZdDes(fillList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return  fill;
	}

	@Override
	public JsonBean getZdHzList(String token, String orgid, Integer year, String jd) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> hashMap=new HashMap<String, Object>();
		try {
		 	 TblStaffUtil tblStaffUtil = userProvider.get();
			Calendar calendar = Calendar.getInstance();
			if (year == null) {
				year = calendar.get(Calendar.YEAR);
			}
			if (StringUtils.isBlank(jd)) {
				jd = getCurrentQuarter();
			}
			if(StringUtils.isBlank(orgid)){
				orgid=tblStaffUtil.getCurrentOrg().getOrgid().toString();
			}
			 List<Map<String, Object>> datas = setZdRefactoringForms(year,orgid,jd);
			  Map<String, Object> d=new HashMap<String, Object>();
				if (datas != null && datas.size() > 0) {
//					d=datas.get(0);
//					if(d.get("LINKORGID")!=null){
					 Organization org=organizationMapper.selectById(orgid);
					hashMap.put("orgName", org.getOrgname());
					//}
					 hashMap.put("staffName",""); //staff.getRealname()
					 hashMap.put("time", ""); //d.get("CREATETIME").toString().substring(0, 10)
				}
			List<Map<String, Object>> list=new  LinkedList<>();
			 //同比增长率
			List<Map<String, Object>> list1 = setZdOneYear(year,orgid,jd);
			//环比
			List<Map<String, Object>> list2=setZdLinkRelativeRatio(year,orgid,jd);
			list.addAll(list1);
			list.addAll(list2);
			 //变化原因说明
			 List<Map<String, Object>> desList = setZdRefactoringFormsDes(year,orgid,jd);
			 List<Map<String, Object>> desNewList = new ArrayList<Map<String,Object>>();
			 List<Map<String, Object>> qjList = new ArrayList<Map<String,Object>>();
			 Map<String, Object> map2=new HashMap<String, Object>();
			 for (Map<String, Object> map : desList) {
				    Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
				    while (iterator.hasNext()) {
				        Map.Entry<String, Object> entry = iterator.next();
				        String newKey =  entry.getKey().replace("DES", "");
				        map2.put(newKey, entry.getValue());
				    }
			 }
			 desNewList.add(map2);
			 list.addAll(desNewList);
			 Map<String, Object> map3=new HashMap<String, Object>();
			 for (Map<String, Object> map : desList) {
				    Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
				    while (iterator.hasNext()) {
				        Map.Entry<String, Object> entry = iterator.next();
				        String newKey =  entry.getKey().replace("DES", "");
				        map3.put(newKey, getTitle3(year,orgid,jd));
				    }
			 }
			 qjList.add(map3);
			 list.addAll(qjList);
			 List<TblRiskMonitoringFill> fillList=tblRiskMonitoringFillMapper.getListByYear(year,jd,orgid);
			 Map<BigDecimal, String> notesmap = new HashMap<>();
			 for(TblRiskMonitoringFill b:fillList){
				 notesmap.put(b.getLinkDeptId(), b.getDeptNotes());
 			 }
			 //责任部门
			 BigDecimal versionId=fillList.size()>0?fillList.get(0).getVersionId():new BigDecimal("0");
 			 List<TblRiskMonDictonary> dicList=tblRiskMonDictonaryMapper.getMonDicCol(new BigDecimal(orgid),versionId);
 			 List<Map<String, Object>> deptList=new ArrayList<Map<String,Object>>();
 			 Map<String, Object> map1 = new HashMap<>();
 			 Map<String, Object> noteMap = new HashMap<>();
 			 for(TblRiskMonDictonary b:dicList){
 				 map1.put(b.getCode().toUpperCase(), b.getDeptName());
 			 }
 			 deptList.add(map1);
 			 list.addAll(deptList);
			hashMap.put("result", list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		JsonBean jsonBean = new JsonBean(1, "success", hashMap);
	    return jsonBean;
	}
	
	
	 //重大经营环比增减率
    List<Map<String, Object>>  setZdLinkRelativeRatio(Integer year,String orgid,String jd) throws Exception{
		List<Map<String, Object>> fillList=new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> zdFillList=new ArrayList<Map<String,Object>>();
	    String fields="aqBigdecimal1,aqBigdecimal2,aqBigdecimal3,aqBigdecimal4,aqBigdecimal5,aqBigdecimal6,aqBigdecimal11,aqBigdecimal12,aqBigdecimal13,aqBigdecimal14,aqBigdecimal7,aqBigdecimal8,aqBigdecimal9,aqBigdecimal10,cwBigdecimal8,cwBigdecimal12";
		//String field2s="zlBigdecimal3,zlBigdecimal6,zlBigdecimal7,zlBigdecimal8,zlBigdecimal9,cwBigdecimal1,cwBigdecimal9,scInteger1,scBigdecimal1,scInteger2,scBigdecimal2,yyBigdecimal2,yyBigdecimal3,yyBigdecimal4,yyInteger2,yyInteger3,yyInteger4,flInteger1";
	    try {
			if(jd.equals("一季度")){
				//一季度
				List<Map<String, Object>> firstQuarter =setZdRefactoringForms(year, orgid,jd);
				Map<String, Object> first=new HashMap<String, Object>();
				if (firstQuarter != null && firstQuarter.size() > 0) {
					first=firstQuarter.get(0);
				} 
				//#{year-1}年四季度末值
				List<Map<String, Object>>  fil = setZdRefactoringForms((year-1), orgid, "四季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年一季度 产生额
				Map<String, Object> fill = new HashMap<String, Object>();
				if(first!=null&&fillList.get(0)!=null){
				for (String s : fields.split(",")) {
//					if(field2s.indexOf(s)>-1){
//						fill.put(s.toUpperCase(),first.get(s.toUpperCase()));
//					}else{
						fill.put(s.toUpperCase(),
								onQoQ(first.get(s.toUpperCase()), fillList.get(0).get(s.toUpperCase())));
					//}
				}
				fillList.add(fill);
				}else{
			    fillList.add(null);
				}
				//#{year-1}年三季度末值
				  fil = setZdRefactoringForms((year-1), orgid, "三季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year-1}年四季度产生额
				  fill = new HashMap<String, Object>();
				  if(fillList.get(0)!=null&&fillList.get(2)!=null){
				    for (String s : fields.split(",")) {
//				    	if(field2s.indexOf(s)>-1){
//				    		fill.put(s.toUpperCase(),fillList.get(0).get(s.toUpperCase()));
//				    	}else{
						fill.put(s.toUpperCase(),
								onQoQ(fillList.get(0).get(s.toUpperCase()), fillList.get(2).get(s.toUpperCase())));
					 //}
				     }
				 fillList.add(fill);
				 }else{
					 fillList.add(null);
				  }
				//#{year}年一季度 环比增减率
				fill = new HashMap<String, Object>();
				if(fillList.get(1)!=null&&fillList.get(3)!=null){
				   for (String s : fields.split(",")) {
					if (!Objects.isNull(fillList.get(3).get(s.toUpperCase()))&&!isZero(fillList.get(3).get(s.toUpperCase()))) {
						fill.put(s.toUpperCase(),
								onYear(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase()))+"/"+onQoQ(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase())));
					}else{
						fill.put(s.toUpperCase(),
								"/"+onQoQ(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase())));
						
					}
			 	   }
			   	fillList.add(fill);
			   	zdFillList.add(fill);
				}else{
					fillList.add(null);
					zdFillList.add(null);
				}
			}else if(jd.equals("二季度")){ //截至2024年 二季度末 H5
				//#{year}二季度末数据
				List<Map<String, Object>> secondQuarter = setZdRefactoringForms(year, orgid,jd);
				Map<String, Object> second=new HashMap<String, Object>();
				if (secondQuarter != null && secondQuarter.size() > 0) {
					second=secondQuarter.get(0);
				}
				//#{year}年一季度末值
				List<Map<String, Object>>  fil = setZdRefactoringForms(year, orgid, "一季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年二季度 产生额
				Map<String, Object> fill = new HashMap<String, Object>();
				if(second!=null&&fillList.get(0)!=null){
				for (String s : fields.split(",")) {
//					if(field2s.indexOf(s)>-1){
//						fill.put(s.toUpperCase(),second.get(s.toUpperCase()));
//					}else{
						fill.put(s.toUpperCase(),
								onQoQ(second.get(s.toUpperCase()), fillList.get(0).get(s.toUpperCase())));
					//}
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
				//#{year-1}年四季度末值
				  fil = setZdRefactoringForms((year-1), orgid, "四季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年一季度 产生额
				  fill = new HashMap<String, Object>();
				if(fillList.get(0)!=null&&fillList.get(2)!=null){
				  for (String s : fields.split(",")) {
//					  if(field2s.indexOf(s)>-1){
//							fill.put(s.toUpperCase(),fillList.get(0).get(s.toUpperCase()));
//						}else{
						fill.put(s.toUpperCase(),
								onQoQ(fillList.get(0).get(s.toUpperCase()), fillList.get(2).get(s.toUpperCase())));
					//}
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
				//#{year}年一季度 环比增减率
				fill = new HashMap<String, Object>();
				if(fillList.get(1)!=null&&fillList.get(3)!=null){
				for (String s : fields.split(",")) {
					if (!Objects.isNull(fillList.get(3).get(s.toUpperCase()))&&!isZero(fillList.get(3).get(s.toUpperCase()))) {
						fill.put(s.toUpperCase(),
								onYear(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase()))+"/"+onQoQ(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase())));
					}else{
						fill.put(s.toUpperCase(),
								"/"+onQoQ(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase())));
					
					}
				}
				fillList.add(fill);
				zdFillList.add(fill);
				}else{
					fillList.add(null);
					fillList.add(null);
				}
			}else if(jd.equals("三季度")){ 
				//#{year}三季度末数据
				List<Map<String, Object>> thirdQuarter = setZdRefactoringForms(year, orgid,jd);
				Map<String, Object> third=new HashMap<String, Object>();
				if (thirdQuarter != null && thirdQuarter.size() > 0) {
					third=thirdQuarter.get(0);
				}
				//#{year}年二季度末值
				List<Map<String, Object>>  fil = setZdRefactoringForms(year, orgid, "二季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年三季度 产生额
				Map<String, Object> fill = new HashMap<String, Object>();
			if(third!=null&&fillList.get(0)!=null){
				for (String s : fields.split(",")) {
//						  if(field2s.indexOf(s)>-1){
//								fill.put(s.toUpperCase(),third.get(s.toUpperCase()));
//							}else{					
								fill.put(s.toUpperCase(),
								onQoQ(third.get(s.toUpperCase()), fillList.get(0).get(s.toUpperCase())));
					// }
				}
				fillList.add(fill);
			}else{
				fillList.add(null);
			}
				//#{year}年一季度末值
				  fil = setZdRefactoringForms(year, orgid, "一季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年二季度 产生额
				  fill = new HashMap<String, Object>();
				if(fillList.get(0)!=null&&fillList.get(2)!=null){
				  for (String s : fields.split(",")) {
//					  if(field2s.indexOf(s)>-1){
//							fill.put(s.toUpperCase(),fillList.get(0).get(s.toUpperCase()));
//						}else{
						fill.put(s.toUpperCase(),
								onQoQ(fillList.get(0).get(s.toUpperCase()), fillList.get(2).get(s.toUpperCase())));
					// }
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
				//#{year}年三季度 环比增减率
				fill = new HashMap<String, Object>();
				if(fillList.get(1)!=null&&fillList.get(3)!=null){
				for (String s : fields.split(",")) {
					if (!Objects.isNull(fillList.get(3).get(s.toUpperCase()))&&!isZero(fillList.get(3).get(s.toUpperCase()))) {
						fill.put(s.toUpperCase(),
								onYear(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase()))+"/"+onQoQ(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase())));
					}else{
						fill.put(s.toUpperCase(),
								"/"+onQoQ(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase())));
					
					}
				}
				fillList.add(fill);
				zdFillList.add(fill);
				}else{
					fillList.add(null);
					zdFillList.add(null);

				}
				
			}else if(jd.equals("四季度")){
				//#{year}四季度末数据
				List<Map<String, Object>> fourthQuarter = setZdRefactoringForms(year, orgid,jd);
				Map<String, Object> fourth=new HashMap<String, Object>();
				if (fourthQuarter != null && fourthQuarter.size() > 0) {
					fourth=fourthQuarter.get(0);
				}
				//#{year}年三季度末值
				List<Map<String, Object>>  fil = setZdRefactoringForms(year, orgid, "三季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年四季度 产生额
				Map<String, Object> fill = new HashMap<String, Object>();
				if(fourth!=null&& fillList.get(0)!=null){
				for (String s : fields.split(",")) {
//					  if(field2s.indexOf(s)>-1){
//							fill.put(s.toUpperCase(),fourth.get(s.toUpperCase()));
//						}else{					
							fill.put(s.toUpperCase(),
								onQoQ(fourth.get(s.toUpperCase()), fillList.get(0).get(s.toUpperCase())));
					// }
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
				//#{year}年二季度末值
				  fil = setZdRefactoringForms(year, orgid, "二季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年三季度 产生额
				  fill = new HashMap<String, Object>();
				if(fillList.get(0)!=null&&fillList.get(2)!=null){
				  for (String s : fields.split(",")) {
//					  if(field2s.indexOf(s)>-1){
//							fill.put(s.toUpperCase(),fillList.get(0).get(s.toUpperCase()));
//						}else{							
							fill.put(s.toUpperCase(),
								onQoQ(fillList.get(0).get(s.toUpperCase()), fillList.get(2).get(s.toUpperCase())));
					// }
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
				//#{year}年四季度 环比增减率
				fill = new HashMap<String, Object>();
				if(fillList.get(1)!=null&&fillList.get(3)!=null){
				for (String s : fields.split(",")) {
					if (!Objects.isNull(fillList.get(3).get(s.toUpperCase()))&&!isZero(fillList.get(3).get(s.toUpperCase()))) {
						fill.put(s.toUpperCase(),
								onYear(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase()))+"/"+onQoQ(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase())));
					}else{
						fill.put(s.toUpperCase(),
								"/"+onQoQ(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase())));
						
					}
				}
				fillList.add(fill);
				zdFillList.add(fill);
				}else{
					fillList.add(null);
					zdFillList.add(null);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return zdFillList;
	}

	@Override
	public Map<String, Object> getReportZdHzList(String token, String orgid, Integer year, String jd) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> hashMap=new HashMap<String, Object>();
		try {
		 	 TblStaffUtil tblStaffUtil = userProvider.get();
			Calendar calendar = Calendar.getInstance();
			if (year == null) {
				year = calendar.get(Calendar.YEAR);
			}
			if (StringUtils.isBlank(jd)) {
				jd = getCurrentQuarter();
			}
			if(StringUtils.isBlank(orgid)){
				orgid=tblStaffUtil.getCurrentOrg().getOrgid().toString();
			}
			 List<Map<String, Object>> datas = setZdRefactoringForms(year,orgid,jd);
			  Map<String, Object> d=new HashMap<String, Object>();
				if (datas != null && datas.size() > 0) {
					 Organization org=organizationMapper.selectById(orgid);
					 hashMap.put("orgName", org.getOrgname());
					 hashMap.put("staffName",""); //staff.getRealname()
					 hashMap.put("time", ""); //d.get("CREATETIME").toString().substring(0, 10)
				}
			List<Map<String, Object>> list=new  LinkedList<>();
			 //同比增长率
			List<Map<String, Object>> list1 = setZdOneYear(year,orgid,jd);
			//环比
			List<Map<String, Object>> list2=setZdLinkRelativeRatio(year,orgid,jd);
			list.addAll(list1);
			list.addAll(list2);
			 //变化原因说明
			 List<Map<String, Object>> desList = setZdRefactoringFormsDes(year,orgid,jd);
			 List<Map<String, Object>> desNewList = new ArrayList<Map<String,Object>>();
			 List<Map<String, Object>> qjList = new ArrayList<Map<String,Object>>();
			 Map<String, Object> map2=new HashMap<String, Object>();
			 for (Map<String, Object> map : desList) {
				    Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
				    while (iterator.hasNext()) {
				        Map.Entry<String, Object> entry = iterator.next();
				        String newKey =  entry.getKey().replace("DES", "");
				        map2.put(newKey, entry.getValue());
				    }
			 }
			 desNewList.add(map2);
			 list.addAll(desNewList);
			 Map<String, Object> map3=new HashMap<String, Object>();
			 for (Map<String, Object> map : desList) {
				    Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
				    while (iterator.hasNext()) {
				        Map.Entry<String, Object> entry = iterator.next();
				        String newKey =  entry.getKey().replace("DES", "");
				        map3.put(newKey, getTitle3(year,orgid,jd));
				    }
			 }
			 qjList.add(map3);
			 list.addAll(qjList);
			 List<TblRiskMonitoringFill> fillList=tblRiskMonitoringFillMapper.getListByYear(year,jd,orgid);
			 Map<BigDecimal, String> notesmap = new HashMap<>();
			 for(TblRiskMonitoringFill b:fillList){
				 notesmap.put(b.getLinkDeptId(), b.getDeptNotes());
 			 }
			 //责任部门
			 BigDecimal versionId=fillList.size()>0?fillList.get(0).getVersionId():new BigDecimal("0");
 			 List<TblRiskMonDictonary> dicList=tblRiskMonDictonaryMapper.getMonDicCol(new BigDecimal(orgid),versionId);
 			 List<Map<String, Object>> deptList=new ArrayList<Map<String,Object>>();
 			 Map<String, Object> map1 = new HashMap<>();
 			 Map<String, Object> noteMap = new HashMap<>();
 			 for(TblRiskMonDictonary b:dicList){
 				 map1.put(b.getCode().toUpperCase(), b.getDeptName());
 			 }
 			 deptList.add(map1);
 			 list.addAll(deptList);
			hashMap.put("result", list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return hashMap;
	}

	@Override
	public void exportByZdTemplate(String templatePath, Map<String, Object> dataList, ServletOutputStream outputStream,
			String jd, String orgid, Integer year) throws IOException {
		// TODO Auto-generated method stub
		 FileOutputStream stream=null;
			try (InputStream templateStream =   TblRiskMonitoringFillServiceImpl.class.getClassLoader().getResourceAsStream(templatePath)) {
	        	if (templateStream == null) {
	                throw new IllegalArgumentException("模板文件不存在：" + templatePath);
	            }
	          Workbook  workbook = new HSSFWorkbook(templateStream);
	          workbook.setSheetName(0, "第"+jd);
	          Sheet sheet = workbook.getSheetAt(0);
	          CellStyle cellStyle = workbook.createCellStyle();
	          // 创建边框对象
	          CreationHelper creationHelper = workbook.getCreationHelper();
	          // 设置边框样式为细边框
	          cellStyle.setBorderBottom(new Short("1"));
	          cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	          cellStyle.setBorderLeft(new Short("1"));
	          cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	          cellStyle.setBorderRight(new Short("1"));
	          cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
	          cellStyle.setBorderTop(new Short("1"));
	          cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
	          CellStyle cellStyle2 = workbook.createCellStyle();
	          cellStyle2.setWrapText(true); // 启用自动换行
	          cellStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中（可选）
	          cellStyle2.setAlignment(CellStyle.ALIGN_GENERAL);
	          Row row = sheet.getRow(2);  
	          if (row == null) {
	              row = sheet.createRow(1);  
	          }
	          Cell cell0 = row.getCell(0);  
	          if (cell0 == null) {
	              cell0 = row.createCell(0);  
	          }
	          cell0.setCellValue(dataList.get("orgName").toString()+"第"+year+"年第"+jd+"重大经营风险监测预警指标体系表"); 
	           row = sheet.getRow(3);  
	             cell0 = row.getCell(0);  
	           if (cell0 == null) {
	               cell0 = row.createCell(0);  
	           }
	           cell0.setCellValue("填报单位："+dataList.get("orgName").toString()+"       "+"填报日期："+dataList.get("time").toString()+"        "
	           +"填报人："+dataList.get("staffName").toString()+"       "+"联系电话："); 
	           int cellLength=15;   
	           int data=0;
	           int title=0;
	           Font font1 = workbook.createFont();
	           font1.setColor(IndexedColors.RED.getIndex()); // 设置第一种颜色为红色
	           Font font2 = workbook.createFont();
	           font2.setColor(IndexedColors.BLUE.getIndex()); // 设置第二种
				List<Map<String, Object>> list=(List<Map<String, Object>>) dataList.get("result");
	        //数据内容循环
				for(int c=6;c<12;c++){
					 Map<String, Object> map = list.get(data);
					 if(map!=null){
					row = sheet.getRow(6);
					Cell cell = row.createCell(c);
					cell.setCellValue(map.get("AQBIGDECIMAL1")!=null?map.get("AQBIGDECIMAL1").toString():"");
					cell.setCellStyle(cellStyle);
					row = sheet.getRow(7);
					cell = row.createCell(c);
					cell.setCellValue(map.get("AQBIGDECIMAL2")!=null?map.get("AQBIGDECIMAL2").toString():"");
					cell.setCellStyle(cellStyle);
					row = sheet.getRow(8);
					cell = row.createCell(c);
					cell.setCellValue(map.get("AQBIGDECIMAL3")!=null?map.get("AQBIGDECIMAL3").toString():"");
					cell.setCellStyle(cellStyle);

					row = sheet.getRow(9);
					cell = row.createCell(c);
					cell.setCellValue(map.get("AQBIGDECIMAL4")!=null?map.get("AQBIGDECIMAL4").toString():"");
					cell.setCellStyle(cellStyle);

					row = sheet.getRow(10);
					cell = row.createCell(c);
					cell.setCellValue(map.get("AQBIGDECIMAL5")!=null?map.get("AQBIGDECIMAL5").toString():"");
					cell.setCellStyle(cellStyle);

					row = sheet.getRow(11);
					cell = row.createCell(c);
					cell.setCellValue(map.get("AQBIGDECIMAL6")!=null?map.get("AQBIGDECIMAL6").toString():"");
					cell.setCellStyle(cellStyle);

					row = sheet.getRow(12);
					cell = row.createCell(c);
					cell.setCellValue(map.get("AQBIGDECIMAL11")!=null?map.get("AQBIGDECIMAL11").toString():"");
					cell.setCellStyle(cellStyle);

					row = sheet.getRow(13);
					cell = row.createCell(c);
					cell.setCellValue(map.get("AQBIGDECIMAL12")!=null?map.get("AQBIGDECIMAL12").toString():"");
					cell.setCellStyle(cellStyle);

					row = sheet.getRow(14);
					cell = row.createCell(c);
					cell.setCellValue(map.get("AQBIGDECIMAL13")!=null?map.get("AQBIGDECIMAL13").toString():"");
					cell.setCellStyle(cellStyle);

					row = sheet.getRow(15);
					cell = row.createCell(c);
					cell.setCellValue(map.get("AQBIGDECIMAL14")!=null?map.get("AQBIGDECIMAL14").toString():"");
					cell.setCellStyle(cellStyle);

					row = sheet.getRow(16);
					cell = row.createCell(c);
					cell.setCellValue(map.get("AQBIGDECIMAL7")!=null?map.get("AQBIGDECIMAL7").toString():"");
					cell.setCellStyle(cellStyle);

					row = sheet.getRow(17);
					cell = row.createCell(c);
					cell.setCellValue(map.get("AQBIGDECIMAL8")!=null?map.get("AQBIGDECIMAL8").toString():"");
					cell.setCellStyle(cellStyle);
					
					row = sheet.getRow(18);
					cell = row.createCell(c);
					cell.setCellValue(map.get("AQBIGDECIMAL9")!=null?map.get("AQBIGDECIMAL9").toString():"");
					cell.setCellStyle(cellStyle);
					
					
					row = sheet.getRow(19);
					cell = row.createCell(c);
					cell.setCellValue(map.get("AQBIGDECIMAL10")!=null?map.get("AQBIGDECIMAL10").toString():"");
					cell.setCellStyle(cellStyle);
					
					row = sheet.getRow(20);
					cell = row.createCell(c);
					cell.setCellValue(map.get("CWBIGDECIMAL8")!=null?map.get("CWBIGDECIMAL8").toString():"");
					cell.setCellStyle(cellStyle);
					
					row = sheet.getRow(21);
					cell = row.createCell(c);
					cell.setCellValue(map.get("CWBIGDECIMAL12")!=null?map.get("CWBIGDECIMAL12").toString():"");
					cell.setCellStyle(cellStyle);
					 }
					data++;
	           }
//					File file = new File("C://Users//cj//Desktop//aaaaaaa1.xls");
//					file.createNewFile();
//					  stream  = FileUtils.openOutputStream(file);
					workbook.write(outputStream);
	        }catch (Exception e) {
				// TODO: handle exception
	        	e.printStackTrace();
			}finally{
				try {
					outputStream.flush();
					outputStream.close();
//					stream.flush();
//					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
	
	
	@Override
	public JsonBean getZdHzListNew(String token, String orgid, Integer year, String jd) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> hashMap=new HashMap<String, Object>();
		try {
		 	 TblStaffUtil tblStaffUtil = userProvider.get();
			Calendar calendar = Calendar.getInstance();
			if (year == null) {
				year = calendar.get(Calendar.YEAR);
			}
			if (StringUtils.isBlank(jd)) {
				jd = getCurrentQuarter();
			}
			if(StringUtils.isBlank(orgid)){
				orgid=tblStaffUtil.getCurrentOrg().getOrgid().toString();
			}
			 List<Map<String, Object>> datas = setZdRefactoringForms(year,orgid,jd);
			  Map<String, Object> d=new HashMap<String, Object>();
				if (datas != null && datas.size() > 0) {
					 Organization org=organizationMapper.selectById(orgid);
					 hashMap.put("orgName", org.getOrgname());
					 hashMap.put("staffName",""); //staff.getRealname()
					 hashMap.put("time", ""); //d.get("CREATETIME").toString().substring(0, 10)
				}
			List<Map<String, Object>> list=new  LinkedList<>();
			 //同比增长率
			List<Map<String, Object>> list1 =setZdOneYearNew(year,orgid,jd);
			//环比
			List<Map<String, Object>> list2=setZdLinkRelativeRatioNew(year,orgid,jd);
			list.addAll(list1);
			list.addAll(list2);
			 //变化原因说明
			 List<Map<String, Object>> desList = setZdRefactoringFormsDes(year,orgid,jd);
			 List<Map<String, Object>> desNewList = new ArrayList<Map<String,Object>>();
			 List<Map<String, Object>> qjList = new ArrayList<Map<String,Object>>();
			 Map<String, Object> map2=new HashMap<String, Object>();
			 for (Map<String, Object> map : desList) {
				 map2.put("FSSG",new StringBuffer().append("一般事故次数:").append(map.get("AQBIGDECIMAL1DES")==null?"":map.get("AQBIGDECIMAL1DES").toString())
						 .append(" 具有潜在事故风险的违规事件:").append(map.get("AQBIGDECIMAL1DES")==null?"":map.get("AQBIGDECIMAL1DES").toString()));
				 map2.put("SCSG",new StringBuffer().append("一般事故次数:").append(map.get("AQBIGDECIMAL6DES")==null?"":map.get("AQBIGDECIMAL6DES").toString())
						 .append("较大事故次数:").append(map.get("AQBIGDECIMAL5DES")==null?"":map.get("AQBIGDECIMAL5DES").toString())
						 .append("重大事故次数:").append(map.get("AQBIGDECIMAL4DES")==null?"":map.get("AQBIGDECIMAL4DES").toString())
						 .append("特别重大事故次数:").append(map.get("AQBIGDECIMAL3DES")==null?"":map.get("AQBIGDECIMAL3DES").toString()));
				 map2.put("HJSJ",new StringBuffer().append("一般事故次数:").append(map.get("AQBIGDECIMAL14DES")==null?"":map.get("AQBIGDECIMAL14DES").toString())
						 .append("较大事故次数:").append(map.get("AQBIGDECIMAL13DES")==null?"":map.get("AQBIGDECIMAL13DES").toString())
						 .append("重大事故次数:").append(map.get("AQBIGDECIMAL12DES")==null?"":map.get("AQBIGDECIMAL12DES").toString())
						 .append("特别重大事故次数:").append(map.get("AQBIGDECIMAL11DES")==null?"":map.get("AQBIGDECIMAL11DES").toString()));
				 map2.put("ZYB",new StringBuffer().append("一般事故次数:").append(map.get("AQBIGDECIMAL10DES")==null?"":map.get("AQBIGDECIMAL10DES").toString())
				        .append("较大事故次数:").append(map.get("AQBIGDECIMAL9DES")==null?"":map.get("AQBIGDECIMAL9DES").toString())
				        .append("重大事故次数:").append(map.get("AQBIGDECIMAL8DES")==null?"":map.get("AQBIGDECIMAL8DES").toString())
				        .append("特别重大事故次数:").append(map.get("AQBIGDECIMAL7DES")==null?"":map.get("AQBIGDECIMAL7DES").toString()));
				 map2.put("ZCFZ",new StringBuffer().append("资产负债率:").append(map.get("CWBIGDECIMAL8DES")==null?"":map.get("CWBIGDECIMAL8DES").toString())
						 .append(" 经营性现金流:").append(map.get("CWBIGDECIMAL12DES")==null?"":map.get("CWBIGDECIMAL12DES").toString()));
			 }
			 desNewList.add(map2);
			 list.addAll(desNewList);
			 Map<String, Object> map3=new HashMap<String, Object>();
			 for (Map<String, Object> map : desList) {
				 map3.put("FSSG",getTitle3(year,orgid,jd));
				 map3.put("SCSG",getTitle3(year,orgid,jd));
				 map3.put("HJSJ",getTitle3(year,orgid,jd));
				 map3.put("ZYB",getTitle3(year,orgid,jd));
				 map3.put("ZCFZ",getTitle3(year,orgid,jd));
			 }
			 qjList.add(map3);
			 list.addAll(qjList);
			 List<TblRiskMonitoringFill> fillList=tblRiskMonitoringFillMapper.getListByYear(year,jd,orgid);
			 Map<BigDecimal, String> notesmap = new HashMap<>();
			 for(TblRiskMonitoringFill b:fillList){
				 notesmap.put(b.getLinkDeptId(), b.getDeptNotes());
 			 }
			 //责任部门
			 BigDecimal versionId=fillList.size()>0?fillList.get(0).getVersionId():new BigDecimal("0");
 			 List<TblRiskMonDictonary> dicList=tblRiskMonDictonaryMapper.getMonDicColZd(new BigDecimal(orgid),versionId);
 			 List<Map<String, Object>> deptList=new ArrayList<Map<String,Object>>();
 			 Map<String, Object> map1 = new HashMap<>();
 			 Map<String, Object> noteMap = new HashMap<>();
 			 for(TblRiskMonDictonary b:dicList){
 				 if(b.getCode().toUpperCase().equals("AQBIGDECIMAL1")){
 	 				 map1.put("FSSG",b.getDeptName());
 				 }else if(b.getCode().toUpperCase().equals("AQBIGDECIMAL3")){
 					 map1.put("SCSG",b.getDeptName());
 				 }else if(b.getCode().toUpperCase().equals("AQBIGDECIMAL11")){
 					 map1.put("HJSJ",b.getDeptName());
 				 }else if(b.getCode().toUpperCase().equals("AQBIGDECIMAL7")){
 					 map1.put("ZYB",b.getDeptName());
 				 }else if(b.getCode().toUpperCase().equals("CWBIGDECIMAL8")){
 					 map1.put("ZCFZ",b.getDeptName());
 				 }
 			 }
 			 deptList.add(map1);
 			 list.addAll(deptList);
			hashMap.put("result", list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		JsonBean jsonBean = new JsonBean(1, "success", hashMap);
	    return jsonBean;
	}
	 

	
	//重大经营同比增减率
    List<Map<String, Object>>  setZdOneYearNew(Integer year,String orgid,String jd) throws Exception{
		List<Map<String, Object>> fillList=new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> returnList=new ArrayList<Map<String,Object>>();
		try {
			//当年E5
			List<Map<String, Object>>  fil= setZdRefactoringForms(year,orgid,jd);
			 if(fil!=null&&fil.size()>0){
				 Map<String, Object> map=fil.get(0);
				 Map<String, Object> newMap=new HashMap<String, Object>();
				 newMap.put("FSSG",new StringBuffer().append("一般事故次数:").append(map.get("AQBIGDECIMAL1")==null?"":map.get("AQBIGDECIMAL1").toString()).append(" 具有潜在事故风险的违规事件:").append(map.get("AQBIGDECIMAL1")==null?"":map.get("AQBIGDECIMAL1").toString()));
				 newMap.put("SCSG",new StringBuffer().append("一般事故次数:").append(map.get("AQBIGDECIMAL6")==null?"":map.get("AQBIGDECIMAL6").toString()).append(" 较大及以上事故次数:").append(sumObjects(map.get("AQBIGDECIMAL5"),map.get("AQBIGDECIMAL4"),map.get("AQBIGDECIMAL3"))));
				 newMap.put("HJSJ",new StringBuffer().append("一般事故次数:").append(map.get("AQBIGDECIMAL14")==null?"":map.get("AQBIGDECIMAL14").toString()).append(" 较大及以上事故次数:").append(sumObjects(map.get("AQBIGDECIMAL13"),map.get("AQBIGDECIMAL12"),map.get("AQBIGDECIMAL11"))));
				 newMap.put("ZYB",new StringBuffer().append("一般事故次数:").append(map.get("AQBIGDECIMAL10")==null?"":map.get("AQBIGDECIMAL10").toString()).append(" 较大及以上事故次数:").append(sumObjects(map.get("AQBIGDECIMAL7"),map.get("AQBIGDECIMAL8"),map.get("AQBIGDECIMAL9"))));
				 newMap.put("ZCFZ",new StringBuffer().append("资产负债率:").append(map.get("CWBIGDECIMAL8")==null?"":map.get("CWBIGDECIMAL8").toString()).append(" 经营性现金流:").append(map.get("CWBIGDECIMAL12")==null?"":map.get("CWBIGDECIMAL12").toString()));
				 fillList.add(map);
				 returnList.add(newMap);
			 }else{
				 returnList.add(null);
			 }
			//F5 查询年度的前一年
			 fil= setZdRefactoringForms((year-1),orgid,jd);
			 //2024年三季度同比增减率G5G5=(E5-F5)/F5
			    String fields="aqBigdecimal1,aqBigdecimal2,aqBigdecimal3,aqBigdecimal4,aqBigdecimal5,aqBigdecimal6,aqBigdecimal11,aqBigdecimal12,aqBigdecimal13,aqBigdecimal14,aqBigdecimal7,aqBigdecimal8,aqBigdecimal9,aqBigdecimal10,cwBigdecimal8,cwBigdecimal12";
			    Map<String, Object> fill=new HashMap<String, Object>();
			    if(fillList.get(0)!=null&&fil.get(0)!=null){
			    	Map<String, Object> newMap=new HashMap<String, Object>();
					 newMap.put("FSSG","一般事故次数："+onYear(fillList.get(0).get("AQBIGDECIMAL1"),fil.get(0).get("AQBIGDECIMAL1"))+"/"+onQoQ(fillList.get(0).get("AQBIGDECIMAL1"),fil.get(0).get("AQBIGDECIMAL1"))+" 具有潜在事故风险的违规事件:"+onYear(fillList.get(0).get("AQBIGDECIMAL1"),fil.get(0).get("AQBIGDECIMAL1"))+"/"+onQoQ(fillList.get(0).get("AQBIGDECIMAL1"),fil.get(0).get("AQBIGDECIMAL1")));
					 newMap.put("SCSG","一般事故次数："+onYear(fillList.get(0).get("AQBIGDECIMAL6"),fil.get(0).get("AQBIGDECIMAL6"))+"/"+onQoQ(fillList.get(0).get("AQBIGDECIMAL6"),fil.get(0).get("AQBIGDECIMAL6"))+" 较大及以上事故次数:"+
					 onYear(sumObjects(fillList.get(0).get("AQBIGDECIMAL5"),fillList.get(0).get("AQBIGDECIMAL4"),fillList.get(0).get("AQBIGDECIMAL3")) 
						    	,sumObjects(fil.get(0).get("AQBIGDECIMAL5"),fil.get(0).get("AQBIGDECIMAL4"),fil.get(0).get("AQBIGDECIMAL3")) )+"/"
					 +onQoQ(sumObjects(fillList.get(0).get("AQBIGDECIMAL5"),fillList.get(0).get("AQBIGDECIMAL4"),fillList.get(0).get("AQBIGDECIMAL3")) 
								,sumObjects(fil.get(0).get("AQBIGDECIMAL5"),fil.get(0).get("AQBIGDECIMAL4"),fil.get(0).get("AQBIGDECIMAL3"))));
					 newMap.put("HJSJ","一般事故次数："+onYear(fillList.get(0).get("AQBIGDECIMAL14"),fil.get(0).get("AQBIGDECIMAL14"))+"/"+onQoQ(fillList.get(0).get("AQBIGDECIMAL14"),fil.get(0).get("AQBIGDECIMAL14"))+" 较大及以上事故次数:"+
							 onYear(sumObjects(fillList.get(0).get("AQBIGDECIMAL13"),fillList.get(0).get("AQBIGDECIMAL12"),fillList.get(0).get("AQBIGDECIMAL11")) 
								    	,sumObjects(fil.get(0).get("AQBIGDECIMAL13"),fil.get(0).get("AQBIGDECIMAL12"),fil.get(0).get("AQBIGDECIMAL11")) )+"/"
							 +onQoQ(sumObjects(fillList.get(0).get("AQBIGDECIMAL13"),fillList.get(0).get("AQBIGDECIMAL12"),fillList.get(0).get("AQBIGDECIMAL11")) 
										,sumObjects(fil.get(0).get("AQBIGDECIMAL13"),fil.get(0).get("AQBIGDECIMAL12"),fil.get(0).get("AQBIGDECIMAL11"))));
					 newMap.put("ZYB","一般事故次数："+onYear(fillList.get(0).get("AQBIGDECIMAL10"),fil.get(0).get("AQBIGDECIMAL10"))+"/"+onQoQ(fillList.get(0).get("AQBIGDECIMAL10"),fil.get(0).get("AQBIGDECIMAL10"))+" 较大及以上事故次数:"+
							 onYear(sumObjects(fillList.get(0).get("AQBIGDECIMAL7"),fillList.get(0).get("AQBIGDECIMAL8"),fillList.get(0).get("AQBIGDECIMAL9")) 
								    	,sumObjects(fil.get(0).get("AQBIGDECIMAL7"),fil.get(0).get("AQBIGDECIMAL8"),fil.get(0).get("AQBIGDECIMAL9")) )+"/"
							 +onQoQ(sumObjects(fillList.get(0).get("AQBIGDECIMAL7"),fillList.get(0).get("AQBIGDECIMAL8"),fillList.get(0).get("AQBIGDECIMAL9")) 
										,sumObjects(fil.get(0).get("AQBIGDECIMAL7"),fil.get(0).get("AQBIGDECIMAL8"),fil.get(0).get("AQBIGDECIMAL9"))));
					 newMap.put("ZCFZ","资产负债率："+onYear(fillList.get(0).get("CWBIGDECIMAL8"),fil.get(0).get("CWBIGDECIMAL8"))+"/"+onQoQ(fillList.get(0).get("CWBIGDECIMAL8"),fil.get(0).get("CWBIGDECIMAL8"))+" 经营性现金流:"+onYear(fillList.get(0).get("CWBIGDECIMAL12"),fil.get(0).get("CWBIGDECIMAL12"))+"/"+onQoQ(fillList.get(0).get("CWBIGDECIMAL12"),fil.get(0).get("CWBIGDECIMAL12")));
			         returnList.add(newMap);
			    }else{
			    	returnList.add(null);
			    }
			  
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return returnList;
	}

	@Override
	public Map<String, Object> getReportZdHzListNew(String token, String orgid, Integer year, String jd)
			throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> hashMap=new HashMap<String, Object>();
		try {
		 	 TblStaffUtil tblStaffUtil = userProvider.get();
			Calendar calendar = Calendar.getInstance();
			if (year == null) {
				year = calendar.get(Calendar.YEAR);
			}
			if (StringUtils.isBlank(jd)) {
				jd = getCurrentQuarter();
			}
			if(StringUtils.isBlank(orgid)){
				orgid=tblStaffUtil.getCurrentOrg().getOrgid().toString();
			}
			 List<Map<String, Object>> datas = setZdRefactoringForms(year,orgid,jd);
			  Map<String, Object> d=new HashMap<String, Object>();
				if (datas != null && datas.size() > 0) {
					 Organization org=organizationMapper.selectById(orgid);
					 hashMap.put("orgName", org.getOrgname());
					 hashMap.put("staffName",""); //staff.getRealname()
					 hashMap.put("time", ""); //d.get("CREATETIME").toString().substring(0, 10)
				}
			List<Map<String, Object>> list=new  LinkedList<>();
			 //同比增长率
			List<Map<String, Object>> list1 =setZdOneYearNew(year,orgid,jd);
			//环比
			List<Map<String, Object>> list2=setZdLinkRelativeRatioNew(year,orgid,jd);
			list.addAll(list1);
			list.addAll(list2);
			 //变化原因说明
			 List<Map<String, Object>> desList = setZdRefactoringFormsDes(year,orgid,jd);
			 List<Map<String, Object>> desNewList = new ArrayList<Map<String,Object>>();
			 List<Map<String, Object>> qjList = new ArrayList<Map<String,Object>>();
			 Map<String, Object> map2=new HashMap<String, Object>();
			 for (Map<String, Object> map : desList) {
				 map2.put("FSSG",new StringBuffer().append("一般事故次数:").append(map.get("AQBIGDECIMAL1DES")==null?"":map.get("AQBIGDECIMAL1DES").toString())
						 .append(" 具有潜在事故风险的违规事件:").append(map.get("AQBIGDECIMAL1DES")==null?"":map.get("AQBIGDECIMAL1DES").toString()));
				 map2.put("SCSG",new StringBuffer().append("一般事故次数:").append(map.get("AQBIGDECIMAL6DES")==null?"":map.get("AQBIGDECIMAL6DES").toString())
						 .append("较大事故次数:").append(map.get("AQBIGDECIMAL5DES")==null?"":map.get("AQBIGDECIMAL5DES").toString())
						 .append("重大事故次数:").append(map.get("AQBIGDECIMAL4DES")==null?"":map.get("AQBIGDECIMAL4DES").toString())
						 .append("特别重大事故次数:").append(map.get("AQBIGDECIMAL3DES")==null?"":map.get("AQBIGDECIMAL3DES").toString()));
				 map2.put("HJSJ",new StringBuffer().append("一般事故次数:").append(map.get("AQBIGDECIMAL14DES")==null?"":map.get("AQBIGDECIMAL14DES").toString())
						 .append("较大事故次数:").append(map.get("AQBIGDECIMAL13DES")==null?"":map.get("AQBIGDECIMAL13DES").toString())
						 .append("重大事故次数:").append(map.get("AQBIGDECIMAL12DES")==null?"":map.get("AQBIGDECIMAL12DES").toString())
						 .append("特别重大事故次数:").append(map.get("AQBIGDECIMAL11DES")==null?"":map.get("AQBIGDECIMAL11DES").toString()));
				 map2.put("ZYB",new StringBuffer().append("一般事故次数:").append(map.get("AQBIGDECIMAL10DES")==null?"":map.get("AQBIGDECIMAL10DES").toString())
				        .append("较大事故次数:").append(map.get("AQBIGDECIMAL9DES")==null?"":map.get("AQBIGDECIMAL9DES").toString())
				        .append("重大事故次数:").append(map.get("AQBIGDECIMAL8DES")==null?"":map.get("AQBIGDECIMAL8DES").toString())
				        .append("特别重大事故次数:").append(map.get("AQBIGDECIMAL7DES")==null?"":map.get("AQBIGDECIMAL7DES").toString()));
				 map2.put("ZCFZ",new StringBuffer().append("资产负债率:").append(map.get("CWBIGDECIMAL8DES")==null?"":map.get("CWBIGDECIMAL8DES").toString())
						 .append(" 经营性现金流:").append(map.get("CWBIGDECIMAL12DES")==null?"":map.get("CWBIGDECIMAL12DES").toString()));
			 }
			 desNewList.add(map2);
			 list.addAll(desNewList);
			 Map<String, Object> map3=new HashMap<String, Object>();
			 for (Map<String, Object> map : desList) {
				 map3.put("FSSG",getTitle3(year,orgid,jd));
				 map3.put("SCSG",getTitle3(year,orgid,jd));
				 map3.put("HJSJ",getTitle3(year,orgid,jd));
				 map3.put("ZYB",getTitle3(year,orgid,jd));
				 map3.put("ZCFZ",getTitle3(year,orgid,jd));
			 }
			 qjList.add(map3);
			 list.addAll(qjList);
			 List<TblRiskMonitoringFill> fillList=tblRiskMonitoringFillMapper.getListByYear(year,jd,orgid);
			 Map<BigDecimal, String> notesmap = new HashMap<>();
			 for(TblRiskMonitoringFill b:fillList){
				 notesmap.put(b.getLinkDeptId(), b.getDeptNotes());
 			 }
			 //责任部门
			 BigDecimal versionId=fillList.size()>0?fillList.get(0).getVersionId():new BigDecimal("0");
 			 List<TblRiskMonDictonary> dicList=tblRiskMonDictonaryMapper.getMonDicColZd(new BigDecimal(orgid),versionId);
 			 List<Map<String, Object>> deptList=new ArrayList<Map<String,Object>>();
 			 Map<String, Object> map1 = new HashMap<>();
 			 Map<String, Object> noteMap = new HashMap<>();
 			 for(TblRiskMonDictonary b:dicList){
 				 if(b.getCode().toUpperCase().equals("AQBIGDECIMAL1")){
 	 				 map1.put("FSSG",b.getDeptName());
 				 }else if(b.getCode().toUpperCase().equals("AQBIGDECIMAL3")){
 					 map1.put("SCSG",b.getDeptName());
 				 }else if(b.getCode().toUpperCase().equals("AQBIGDECIMAL11")){
 					 map1.put("HJSJ",b.getDeptName());
 				 }else if(b.getCode().toUpperCase().equals("AQBIGDECIMAL7")){
 					 map1.put("ZYB",b.getDeptName());
 				 }else if(b.getCode().toUpperCase().equals("CWBIGDECIMAL8")){
 					 map1.put("ZCFZ",b.getDeptName());
 				 }
 			 }
 			 deptList.add(map1);
 			 list.addAll(deptList);
			hashMap.put("result", list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return hashMap;
	}

	@Override
	public void exportByZdTemplateNew(String templatePath, Map<String, Object> dataList,
			ServletOutputStream outputStream, String jd, String orgid, Integer year) throws IOException {
		 FileOutputStream stream=null;
			try (InputStream templateStream =   TblRiskMonitoringFillServiceImpl.class.getClassLoader().getResourceAsStream(templatePath)) {
	        	if (templateStream == null) {
	                throw new IllegalArgumentException("模板文件不存在：" + templatePath);
	            }
	          Workbook  workbook = new HSSFWorkbook(templateStream);
	          workbook.setSheetName(0, "第"+jd);
	          Sheet sheet = workbook.getSheetAt(0);
	          CellStyle cellStyle = workbook.createCellStyle();
	          // 创建边框对象
	          CreationHelper creationHelper = workbook.getCreationHelper();
	          // 设置边框样式为细边框
	          cellStyle.setBorderBottom(new Short("1"));
	          cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	          cellStyle.setBorderLeft(new Short("1"));
	          cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	          cellStyle.setBorderRight(new Short("1"));
	          cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
	          cellStyle.setBorderTop(new Short("1"));
	          cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
	          CellStyle cellStyle2 = workbook.createCellStyle();
	          cellStyle2.setWrapText(true); // 启用自动换行
	          cellStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中（可选）
	          cellStyle2.setAlignment(CellStyle.ALIGN_GENERAL);
	          Row row = sheet.getRow(2);  
	          if (row == null) {
	              row = sheet.createRow(1);  
	          }
	          Cell cell0 = row.getCell(0);  
	          if (cell0 == null) {
	              cell0 = row.createCell(0);  
	          }
	          cell0.setCellValue(dataList.get("orgName").toString()+"第"+year+"年第"+jd+"重大经营风险监测预警指标体系表"); 
	           row = sheet.getRow(3);  
	             cell0 = row.getCell(0);  
	           if (cell0 == null) {
	               cell0 = row.createCell(0);  
	           }
	           cell0.setCellValue("填报单位："+dataList.get("orgName").toString()+"       "+"填报日期："+dataList.get("time").toString()+"        "
	           +"填报人："+dataList.get("staffName").toString()+"       "+"联系电话："); 
	           int cellLength=15;   
	           int data=0;
	           int title=0;
	           Font font1 = workbook.createFont();
	           font1.setColor(IndexedColors.RED.getIndex()); // 设置第一种颜色为红色
	           Font font2 = workbook.createFont();
	           font2.setColor(IndexedColors.BLUE.getIndex()); // 设置第二种
				List<Map<String, Object>> list=(List<Map<String, Object>>) dataList.get("result");
	        //数据内容循环
				for(int c=5;c<11;c++){
					 Map<String, Object> map = list.get(data);
					 if(map!=null){
					row = sheet.getRow(6);
					Cell cell = row.createCell(c);
					cell.setCellValue(map.get("FSSG")!=null?map.get("FSSG").toString():"");
					cell.setCellStyle(cellStyle);
					row = sheet.getRow(7);
					cell = row.createCell(c);
					cell.setCellValue(map.get("SCSG")!=null?map.get("SCSG").toString():"");
					cell.setCellStyle(cellStyle);
					row = sheet.getRow(8);
					cell = row.createCell(c);
					cell.setCellValue(map.get("HJSJ")!=null?map.get("HJSJ").toString():"");
					cell.setCellStyle(cellStyle);

					row = sheet.getRow(9);
					cell = row.createCell(c);
					cell.setCellValue(map.get("ZYB")!=null?map.get("ZYB").toString():"");
					cell.setCellStyle(cellStyle);

					row = sheet.getRow(10);
					cell = row.createCell(c);
					cell.setCellValue(map.get("ZCFZ")!=null?map.get("ZCFZ").toString():"");
					cell.setCellStyle(cellStyle);
					 }
					data++;
	           }
//					File file = new File("C://Users//cj//Desktop//aaaaaaa1.xls");
//					file.createNewFile();
//					  stream  = FileUtils.openOutputStream(file);
					workbook.write(outputStream);
	        }catch (Exception e) {
				// TODO: handle exception
	        	e.printStackTrace();
			}finally{
				try {
					outputStream.flush();
					outputStream.close();
//					stream.flush();
//					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
	}
    
	
	
	 //重大经营环比增减率
    List<Map<String, Object>>  setZdLinkRelativeRatioNew(Integer year,String orgid,String jd) throws Exception{
		List<Map<String, Object>> fillList=new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> zdFillList=new ArrayList<Map<String,Object>>();
	    String fields="aqBigdecimal1,aqBigdecimal2,aqBigdecimal3,aqBigdecimal4,aqBigdecimal5,aqBigdecimal6,aqBigdecimal11,aqBigdecimal12,aqBigdecimal13,aqBigdecimal14,aqBigdecimal7,aqBigdecimal8,aqBigdecimal9,aqBigdecimal10,cwBigdecimal8,cwBigdecimal12";
	    try {
			if(jd.equals("一季度")){
				List<Map<String, Object>> firstQuarter =setZdRefactoringForms(year, orgid,jd);
				Map<String, Object> first=new HashMap<String, Object>();
				if (firstQuarter != null && firstQuarter.size() > 0) {
					first=firstQuarter.get(0);
				} 
				//#{year-1}年四季度末值
				List<Map<String, Object>>  fil = setZdRefactoringForms((year-1), orgid, "四季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年一季度 产生额
				Map<String, Object> fill = new HashMap<String, Object>();
				if(first!=null&&fillList.get(0)!=null){
				for (String s : fields.split(",")) {
						fill.put(s.toUpperCase(),onQoQ(first.get(s.toUpperCase()), fillList.get(0).get(s.toUpperCase())));
				}
				fillList.add(fill);
				}else{
			    fillList.add(null);
				}
				//#{year-1}年三季度末值
				  fil = setZdRefactoringForms((year-1), orgid, "三季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year-1}年四季度产生额
				  fill = new HashMap<String, Object>();
				  if(fillList.get(0)!=null&&fillList.get(2)!=null){
				    for (String s : fields.split(",")) {
						fill.put(s.toUpperCase(),onQoQ(fillList.get(0).get(s.toUpperCase()), fillList.get(2).get(s.toUpperCase())));
				     }
				 fillList.add(fill);
				 }else{
					 fillList.add(null);
				  }
				//#{year}年一季度 环比增减率
				fill = new HashMap<String, Object>();
				if(fillList.get(1)!=null&&fillList.get(3)!=null){
					 	Map<String, Object> newMap=new HashMap<String, Object>();
						 newMap.put("FSSG","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1"))+" 具有潜在事故风险的违规事件:"+onYear(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1")));
						 newMap.put("SCSG","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL6"),fillList.get(3).get("AQBIGDECIMAL6"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL6"),fillList.get(3).get("AQBIGDECIMAL6"))+" 较大及以上事故次数:"+
						 onYear(sumObjects(fillList.get(1).get("AQBIGDECIMAL5"),fillList.get(1).get("AQBIGDECIMAL4"),fillList.get(1).get("AQBIGDECIMAL3")) 
							    	,sumObjects(fillList.get(3).get("AQBIGDECIMAL5"),fillList.get(3).get("AQBIGDECIMAL4"),fillList.get(3).get("AQBIGDECIMAL3")) )+"/"
						 +onQoQ(sumObjects(fillList.get(1).get("AQBIGDECIMAL5"),fillList.get(1).get("AQBIGDECIMAL4"),fillList.get(1).get("AQBIGDECIMAL3")) 
									,sumObjects(fillList.get(3).get("AQBIGDECIMAL5"),fillList.get(3).get("AQBIGDECIMAL4"),fillList.get(3).get("AQBIGDECIMAL3"))));
						 newMap.put("HJSJ","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL14"),fillList.get(3).get("AQBIGDECIMAL14"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL14"),fillList.get(3).get("AQBIGDECIMAL14"))+" 较大及以上事故次数:"+
								 onYear(sumObjects(fillList.get(1).get("AQBIGDECIMAL13"),fillList.get(1).get("AQBIGDECIMAL12"),fillList.get(1).get("AQBIGDECIMAL11")) 
									    	,sumObjects(fillList.get(3).get("AQBIGDECIMAL13"),fillList.get(3).get("AQBIGDECIMAL12"),fillList.get(3).get("AQBIGDECIMAL11")) )+"/"
								 +onQoQ(sumObjects(fillList.get(1).get("AQBIGDECIMAL13"),fillList.get(1).get("AQBIGDECIMAL12"),fillList.get(1).get("AQBIGDECIMAL11")) 
											,sumObjects(fillList.get(3).get("AQBIGDECIMAL13"),fillList.get(3).get("AQBIGDECIMAL12"),fillList.get(3).get("AQBIGDECIMAL11"))));
						 newMap.put("ZYB","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL10"),fillList.get(3).get("AQBIGDECIMAL10"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL10"),fillList.get(3).get("AQBIGDECIMAL10"))+" 较大及以上事故次数:"+
								 onYear(sumObjects(fillList.get(1).get("AQBIGDECIMAL7"),fillList.get(1).get("AQBIGDECIMAL8"),fillList.get(1).get("AQBIGDECIMAL9")) 
									    	,sumObjects(fillList.get(3).get("AQBIGDECIMAL7"),fillList.get(3).get("AQBIGDECIMAL8"),fillList.get(3).get("AQBIGDECIMAL9")) )+"/"
								 +onQoQ(sumObjects(fillList.get(1).get("AQBIGDECIMAL7"),fillList.get(1).get("AQBIGDECIMAL8"),fillList.get(1).get("AQBIGDECIMAL9")) 
											,sumObjects(fillList.get(3).get("AQBIGDECIMAL7"),fillList.get(3).get("AQBIGDECIMAL8"),fillList.get(3).get("AQBIGDECIMAL9"))));
						 newMap.put("ZCFZ","资产负债率："+onYear(fillList.get(1).get("CWBIGDECIMAL8"),fillList.get(3).get("CWBIGDECIMAL8"))+"/"+onQoQ(fillList.get(1).get("CWBIGDECIMAL8"),fillList.get(3).get("CWBIGDECIMAL8"))+" 经营性现金流:"+onYear(fillList.get(1).get("CWBIGDECIMAL12"),fillList.get(3).get("CWBIGDECIMAL12"))+"/"+onQoQ(fillList.get(1).get("CWBIGDECIMAL12"),fillList.get(3).get("CWBIGDECIMAL12")));
				zdFillList.add(newMap);
				}else{
				 zdFillList.add(null);
				}
			}else if(jd.equals("二季度")){ //截至2024年 二季度末 H5
				//#{year}二季度末数据
				List<Map<String, Object>> secondQuarter = setZdRefactoringForms(year, orgid,jd);
				Map<String, Object> second=new HashMap<String, Object>();
				if (secondQuarter != null && secondQuarter.size() > 0) {
					second=secondQuarter.get(0);
				}
				//#{year}年一季度末值
				List<Map<String, Object>>  fil = setZdRefactoringForms(year, orgid, "一季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年二季度 产生额
				Map<String, Object> fill = new HashMap<String, Object>();
				if(second!=null&&fillList.get(0)!=null){
				for (String s : fields.split(",")) {
						fill.put(s.toUpperCase(),onQoQ(second.get(s.toUpperCase()), fillList.get(0).get(s.toUpperCase())));
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
				//#{year-1}年四季度末值
				  fil = setZdRefactoringForms((year-1), orgid, "四季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年一季度 产生额
				  fill = new HashMap<String, Object>();
				if(fillList.get(0)!=null&&fillList.get(2)!=null){
				  for (String s : fields.split(",")) {
						fill.put(s.toUpperCase(), onQoQ(fillList.get(0).get(s.toUpperCase()), fillList.get(2).get(s.toUpperCase())));
				}
				    fillList.add(fill);
				}else{
					fillList.add(null);
				}
				//#{year}年一季度 环比增减率
				fill = new HashMap<String, Object>();
				if(fillList.get(1)!=null&&fillList.get(3)!=null){
					Map<String, Object> newMap=new HashMap<String, Object>();
					 newMap.put("FSSG","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1"))+" 具有潜在事故风险的违规事件:"+onYear(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1")));
					 newMap.put("SCSG","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL6"),fillList.get(3).get("AQBIGDECIMAL6"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL6"),fillList.get(3).get("AQBIGDECIMAL6"))+" 较大及以上事故次数:"+
					 onYear(sumObjects(fillList.get(1).get("AQBIGDECIMAL5"),fillList.get(1).get("AQBIGDECIMAL4"),fillList.get(1).get("AQBIGDECIMAL3")) 
						    	,sumObjects(fillList.get(3).get("AQBIGDECIMAL5"),fillList.get(3).get("AQBIGDECIMAL4"),fillList.get(3).get("AQBIGDECIMAL3")) )+"/"
					 +onQoQ(sumObjects(fillList.get(1).get("AQBIGDECIMAL5"),fillList.get(1).get("AQBIGDECIMAL4"),fillList.get(1).get("AQBIGDECIMAL3")) 
								,sumObjects(fillList.get(3).get("AQBIGDECIMAL5"),fillList.get(3).get("AQBIGDECIMAL4"),fillList.get(3).get("AQBIGDECIMAL3"))));
					 newMap.put("HJSJ","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL14"),fillList.get(3).get("AQBIGDECIMAL14"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL14"),fillList.get(3).get("AQBIGDECIMAL14"))+" 较大及以上事故次数:"+
							 onYear(sumObjects(fillList.get(1).get("AQBIGDECIMAL13"),fillList.get(1).get("AQBIGDECIMAL12"),fillList.get(1).get("AQBIGDECIMAL11")) 
								    	,sumObjects(fillList.get(3).get("AQBIGDECIMAL13"),fillList.get(3).get("AQBIGDECIMAL12"),fillList.get(3).get("AQBIGDECIMAL11")) )+"/"
							 +onQoQ(sumObjects(fillList.get(1).get("AQBIGDECIMAL13"),fillList.get(1).get("AQBIGDECIMAL12"),fillList.get(1).get("AQBIGDECIMAL11")) 
										,sumObjects(fillList.get(3).get("AQBIGDECIMAL13"),fillList.get(3).get("AQBIGDECIMAL12"),fillList.get(3).get("AQBIGDECIMAL11"))));
					 newMap.put("ZYB","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL10"),fillList.get(3).get("AQBIGDECIMAL10"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL10"),fillList.get(3).get("AQBIGDECIMAL10"))+" 较大及以上事故次数:"+
							 onYear(sumObjects(fillList.get(1).get("AQBIGDECIMAL7"),fillList.get(1).get("AQBIGDECIMAL8"),fillList.get(1).get("AQBIGDECIMAL9")) 
								    	,sumObjects(fillList.get(3).get("AQBIGDECIMAL7"),fillList.get(3).get("AQBIGDECIMAL8"),fillList.get(3).get("AQBIGDECIMAL9")) )+"/"
							 +onQoQ(sumObjects(fillList.get(1).get("AQBIGDECIMAL7"),fillList.get(1).get("AQBIGDECIMAL8"),fillList.get(1).get("AQBIGDECIMAL9")) 
										,sumObjects(fillList.get(3).get("AQBIGDECIMAL7"),fillList.get(3).get("AQBIGDECIMAL8"),fillList.get(3).get("AQBIGDECIMAL9"))));
					 newMap.put("ZCFZ","资产负债率："+onYear(fillList.get(1).get("CWBIGDECIMAL8"),fillList.get(3).get("CWBIGDECIMAL8"))+"/"+onQoQ(fillList.get(1).get("CWBIGDECIMAL8"),fillList.get(3).get("CWBIGDECIMAL8"))+" 经营性现金流:"+onYear(fillList.get(1).get("CWBIGDECIMAL12"),fillList.get(3).get("CWBIGDECIMAL12"))+"/"+onQoQ(fillList.get(1).get("CWBIGDECIMAL12"),fillList.get(3).get("CWBIGDECIMAL12")));
			         zdFillList.add(newMap);
				}else{
					zdFillList.add(null);
				}
			}else if(jd.equals("三季度")){ 
				//#{year}三季度末数据
				List<Map<String, Object>> thirdQuarter = setZdRefactoringForms(year, orgid,jd);
				Map<String, Object> third=new HashMap<String, Object>();
				if (thirdQuarter != null && thirdQuarter.size() > 0) {
					third=thirdQuarter.get(0);
				}
				//#{year}年二季度末值
				List<Map<String, Object>>  fil = setZdRefactoringForms(year, orgid, "二季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年三季度 产生额
				Map<String, Object> fill = new HashMap<String, Object>();
			if(third!=null&&fillList.get(0)!=null){
				for (String s : fields.split(",")) {
								fill.put(s.toUpperCase(),
								onQoQ(third.get(s.toUpperCase()), fillList.get(0).get(s.toUpperCase())));
				}
				fillList.add(fill);
			}else{
				fillList.add(null);
			}
				//#{year}年一季度末值
				  fil = setZdRefactoringForms(year, orgid, "一季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年二季度 产生额
				  fill = new HashMap<String, Object>();
				if(fillList.get(0)!=null&&fillList.get(2)!=null){
				  for (String s : fields.split(",")) {
						fill.put(s.toUpperCase(),
								onQoQ(fillList.get(0).get(s.toUpperCase()), fillList.get(2).get(s.toUpperCase())));
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
				//#{year}年三季度 环比增减率
				fill = new HashMap<String, Object>();
				if(fillList.get(1)!=null&&fillList.get(3)!=null){
//				for (String s : fields.split(",")) {
//					if (!Objects.isNull(fillList.get(3).get(s.toUpperCase()))&&!isZero(fillList.get(3).get(s.toUpperCase()))) {
//						fill.put(s.toUpperCase(),
//								onYear(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase()))+"/"+onQoQ(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase())));
//					}else{
//						fill.put(s.toUpperCase(),
//								"/"+onQoQ(fillList.get(1).get(s.toUpperCase()),fillList.get(3).get(s.toUpperCase())));
//					
//					}
//				}
//				fillList.add(fill);
//				zdFillList.add(fill);
					Map<String, Object> newMap=new HashMap<String, Object>();
					 newMap.put("FSSG","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1"))+" 具有潜在事故风险的违规事件:"+onYear(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1")));
					 newMap.put("SCSG","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL6"),fillList.get(3).get("AQBIGDECIMAL6"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL6"),fillList.get(3).get("AQBIGDECIMAL6"))+" 较大及以上事故次数:"+
					 onYear(sumObjects(fillList.get(1).get("AQBIGDECIMAL5"),fillList.get(1).get("AQBIGDECIMAL4"),fillList.get(1).get("AQBIGDECIMAL3")) 
						    	,sumObjects(fillList.get(3).get("AQBIGDECIMAL5"),fillList.get(3).get("AQBIGDECIMAL4"),fillList.get(3).get("AQBIGDECIMAL3")) )+"/"
					 +onQoQ(sumObjects(fillList.get(1).get("AQBIGDECIMAL5"),fillList.get(1).get("AQBIGDECIMAL4"),fillList.get(1).get("AQBIGDECIMAL3")) 
								,sumObjects(fillList.get(3).get("AQBIGDECIMAL5"),fillList.get(3).get("AQBIGDECIMAL4"),fillList.get(3).get("AQBIGDECIMAL3"))));
					 newMap.put("HJSJ","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL14"),fillList.get(3).get("AQBIGDECIMAL14"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL14"),fillList.get(3).get("AQBIGDECIMAL14"))+" 较大及以上事故次数:"+
							 onYear(sumObjects(fillList.get(1).get("AQBIGDECIMAL13"),fillList.get(1).get("AQBIGDECIMAL12"),fillList.get(1).get("AQBIGDECIMAL11")) 
								    	,sumObjects(fillList.get(3).get("AQBIGDECIMAL13"),fillList.get(3).get("AQBIGDECIMAL12"),fillList.get(3).get("AQBIGDECIMAL11")) )+"/"
							 +onQoQ(sumObjects(fillList.get(1).get("AQBIGDECIMAL13"),fillList.get(1).get("AQBIGDECIMAL12"),fillList.get(1).get("AQBIGDECIMAL11")) 
										,sumObjects(fillList.get(3).get("AQBIGDECIMAL13"),fillList.get(3).get("AQBIGDECIMAL12"),fillList.get(3).get("AQBIGDECIMAL11"))));
					 newMap.put("ZYB","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL10"),fillList.get(3).get("AQBIGDECIMAL10"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL10"),fillList.get(3).get("AQBIGDECIMAL10"))+" 较大及以上事故次数:"+
							 onYear(sumObjects(fillList.get(1).get("AQBIGDECIMAL7"),fillList.get(1).get("AQBIGDECIMAL8"),fillList.get(1).get("AQBIGDECIMAL9")) 
								    	,sumObjects(fillList.get(3).get("AQBIGDECIMAL7"),fillList.get(3).get("AQBIGDECIMAL8"),fillList.get(3).get("AQBIGDECIMAL9")) )+"/"
							 +onQoQ(sumObjects(fillList.get(1).get("AQBIGDECIMAL7"),fillList.get(1).get("AQBIGDECIMAL8"),fillList.get(1).get("AQBIGDECIMAL9")) 
										,sumObjects(fillList.get(3).get("AQBIGDECIMAL7"),fillList.get(3).get("AQBIGDECIMAL8"),fillList.get(3).get("AQBIGDECIMAL9"))));
					 newMap.put("ZCFZ","资产负债率："+onYear(fillList.get(1).get("CWBIGDECIMAL8"),fillList.get(3).get("CWBIGDECIMAL8"))+"/"+onQoQ(fillList.get(1).get("CWBIGDECIMAL8"),fillList.get(3).get("CWBIGDECIMAL8"))+" 经营性现金流:"+onYear(fillList.get(1).get("CWBIGDECIMAL12"),fillList.get(3).get("CWBIGDECIMAL12"))+"/"+onQoQ(fillList.get(1).get("CWBIGDECIMAL12"),fillList.get(3).get("CWBIGDECIMAL12")));
			         zdFillList.add(newMap);
				}else{
					zdFillList.add(null);
				}
			}else if(jd.equals("四季度")){
				//#{year}四季度末数据
				List<Map<String, Object>> fourthQuarter = setZdRefactoringForms(year, orgid,jd);
				Map<String, Object> fourth=new HashMap<String, Object>();
				if (fourthQuarter != null && fourthQuarter.size() > 0) {
					fourth=fourthQuarter.get(0);
				}
				//#{year}年三季度末值
				List<Map<String, Object>>  fil = setZdRefactoringForms(year, orgid, "三季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年四季度 产生额
				Map<String, Object> fill = new HashMap<String, Object>();
				if(fourth!=null&& fillList.get(0)!=null){
				for (String s : fields.split(",")) {
							fill.put(s.toUpperCase(),onQoQ(fourth.get(s.toUpperCase()), fillList.get(0).get(s.toUpperCase())));
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
				//#{year}年二季度末值
				  fil = setZdRefactoringForms(year, orgid, "二季度");
				if (fil != null && fil.size() > 0) {
					fillList.add(fil.get(0));
				}else{
					fillList.add(null);
				}
				//#{year}年三季度 产生额
				  fill = new HashMap<String, Object>();
				if(fillList.get(0)!=null&&fillList.get(2)!=null){
				  for (String s : fields.split(",")) {
							fill.put(s.toUpperCase(),onQoQ(fillList.get(0).get(s.toUpperCase()), fillList.get(2).get(s.toUpperCase())));
				}
				fillList.add(fill);
				}else{
					fillList.add(null);
				}
				//#{year}年四季度 环比增减率
				fill = new HashMap<String, Object>();
				if(fillList.get(1)!=null&&fillList.get(3)!=null){
					Map<String, Object> newMap=new HashMap<String, Object>();
					 newMap.put("FSSG","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1"))+" 具有潜在事故风险的违规事件:"+onYear(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL1"),fillList.get(3).get("AQBIGDECIMAL1")));
					 newMap.put("SCSG","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL6"),fillList.get(3).get("AQBIGDECIMAL6"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL6"),fillList.get(3).get("AQBIGDECIMAL6"))+" 较大及以上事故次数:"+
					 onYear(sumObjects(fillList.get(1).get("AQBIGDECIMAL5"),fillList.get(1).get("AQBIGDECIMAL4"),fillList.get(1).get("AQBIGDECIMAL3")) 
						    	,sumObjects(fillList.get(3).get("AQBIGDECIMAL5"),fillList.get(3).get("AQBIGDECIMAL4"),fillList.get(3).get("AQBIGDECIMAL3")) )+"/"
					 +onQoQ(sumObjects(fillList.get(1).get("AQBIGDECIMAL5"),fillList.get(1).get("AQBIGDECIMAL4"),fillList.get(1).get("AQBIGDECIMAL3")) 
								,sumObjects(fillList.get(3).get("AQBIGDECIMAL5"),fillList.get(3).get("AQBIGDECIMAL4"),fillList.get(3).get("AQBIGDECIMAL3"))));
					 newMap.put("HJSJ","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL14"),fillList.get(3).get("AQBIGDECIMAL14"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL14"),fillList.get(3).get("AQBIGDECIMAL14"))+" 较大及以上事故次数:"+
							 onYear(sumObjects(fillList.get(1).get("AQBIGDECIMAL13"),fillList.get(1).get("AQBIGDECIMAL12"),fillList.get(1).get("AQBIGDECIMAL11")) 
								    	,sumObjects(fillList.get(3).get("AQBIGDECIMAL13"),fillList.get(3).get("AQBIGDECIMAL12"),fillList.get(3).get("AQBIGDECIMAL11")) )+"/"
							 +onQoQ(sumObjects(fillList.get(1).get("AQBIGDECIMAL13"),fillList.get(1).get("AQBIGDECIMAL12"),fillList.get(1).get("AQBIGDECIMAL11")) 
										,sumObjects(fillList.get(3).get("AQBIGDECIMAL13"),fillList.get(3).get("AQBIGDECIMAL12"),fillList.get(3).get("AQBIGDECIMAL11"))));
					 newMap.put("ZYB","一般事故次数："+onYear(fillList.get(1).get("AQBIGDECIMAL10"),fillList.get(3).get("AQBIGDECIMAL10"))+"/"+onQoQ(fillList.get(1).get("AQBIGDECIMAL10"),fillList.get(3).get("AQBIGDECIMAL10"))+" 较大及以上事故次数:"+
							 onYear(sumObjects(fillList.get(1).get("AQBIGDECIMAL7"),fillList.get(1).get("AQBIGDECIMAL8"),fillList.get(1).get("AQBIGDECIMAL9")) 
								    	,sumObjects(fillList.get(3).get("AQBIGDECIMAL7"),fillList.get(3).get("AQBIGDECIMAL8"),fillList.get(3).get("AQBIGDECIMAL9")) )+"/"
							 +onQoQ(sumObjects(fillList.get(1).get("AQBIGDECIMAL7"),fillList.get(1).get("AQBIGDECIMAL8"),fillList.get(1).get("AQBIGDECIMAL9")) 
										,sumObjects(fillList.get(3).get("AQBIGDECIMAL7"),fillList.get(3).get("AQBIGDECIMAL8"),fillList.get(3).get("AQBIGDECIMAL9"))));
					 newMap.put("ZCFZ","资产负债率："+onYear(fillList.get(1).get("CWBIGDECIMAL8"),fillList.get(3).get("CWBIGDECIMAL8"))+"/"+onQoQ(fillList.get(1).get("CWBIGDECIMAL8"),fillList.get(3).get("CWBIGDECIMAL8"))+" 经营性现金流:"+onYear(fillList.get(1).get("CWBIGDECIMAL12"),fillList.get(3).get("CWBIGDECIMAL12"))+"/"+onQoQ(fillList.get(1).get("CWBIGDECIMAL12"),fillList.get(3).get("CWBIGDECIMAL12")));
			         zdFillList.add(newMap);
				}else{
					zdFillList.add(null);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return zdFillList;
	}
	
	
}
 


