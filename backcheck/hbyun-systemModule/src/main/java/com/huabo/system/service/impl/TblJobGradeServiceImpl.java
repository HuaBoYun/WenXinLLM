package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.TblJobGrade;
import com.huabo.system.mapper.TblJobGradeDao;
import com.huabo.system.service.TblJobGradeService;

@Service("TblJobGradeService")
public class TblJobGradeServiceImpl implements TblJobGradeService {

    @Resource
    public TblJobGradeDao tblJobGradeDao;
    
	@Override
	public List<TblJobGrade> findAll(BigDecimal gradeid) {
		// TODO Auto-generated method stub
		return this.tblJobGradeDao.listBySql(gradeid);
	}
	@Override
	public void saveJobGrade(TblJobGrade grade) {
		// TODO Auto-generated method stub
		this.tblJobGradeDao.saveJobGrade(grade);
	}
	
	
	@Override
	public TblJobGrade findByid(String historycode) {
		// TODO Auto-generated method stub
		return tblJobGradeDao.findJobGradeByHis(historycode);
	}
	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		  tblJobGradeDao.deleteGrade(id);
	}
	 
	@Override
	public void updateJob(TblJobGrade grade) {
		// TODO Auto-generated method stub
		this.tblJobGradeDao.updateJobGrade(grade);
	}
	//职位表同步
	@Override
	public void syncJobGrade(String data) throws Exception {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if (!StringUtils.isEmpty(data)) {
			try {
			net.sf.json.JSONObject paramStr = net.sf.json.JSONObject.fromObject(data);
			if(paramStr.get("success").toString()=="true"){
				net.sf.json.JSONObject msg = net.sf.json.JSONObject.fromObject(paramStr.get("msg").toString());
			net.sf.json.JSONArray jobGrades = net.sf.json.JSONArray.fromObject(msg.get("jobGrades"));
			for (Object o : jobGrades) {
				net.sf.json.JSONObject obj = (net.sf.json.JSONObject) o;
				TblJobGrade grade = tblJobGradeDao.findJobGradeByHis(obj.getString("id"));
				if (grade==null) {
					TblJobGrade job = new TblJobGrade();
					job.setHistoryCode(obj.getString("id"));
					job.setDataSource("zz");
					job.setAliasName(obj.get("aliasName")!=null?obj.getString("aliasName"):"");
					job.setCategoryName(obj.getString("categoryName"));
					job.setCategory(obj.getString("category"));
					job.setGradeCode(obj.getString("name"));
					job.setDescription(obj.get("description")!=null?obj.getString("description"):"");
					job.setGradeid(RandomUtil.uuBigDecimalId());
					tblJobGradeDao.saveJobGrade(job);
				} else {
					TblJobGrade job = grade;
					job.setHistoryCode(obj.getString("id"));
					job.setDataSource("zz");
					job.setAliasName(obj.getString("aliasName"));
					job.setCategoryName(obj.getString("categoryName"));
					job.setCategory(obj.getString("category"));
					job.setGradeCode(obj.getString("name"));
					job.setDescription(obj.getString("description"));
					tblJobGradeDao.updateJobGrade(job);
				}
			}
			}
			System.out.println("========职级同步已完成!===========");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
			
	}

//
//    @Override
//    public List<TblJob> findAll(BigDecimal companyid) {
//        //String sql = "select * from TBL_JOB where COMPANYID='" + companyid + "'";
//        return this.tblJobDao.listBySql(companyid);
//    }
//
//    @Override
//    public List<TblJobMySql> findAllMySql(BigDecimal companyid) {
//        //String sql = "select * from TBL_JOB where COMPANYID='" + companyid + "'";
//        return this.tblJobMySqlDao.listBySql(companyid);
//    }
//
//    @Override
//    public void saveJob(TblJob job) {
//        this.tblJobDao.saveJob(job);
//    }
//
//    @Override
//    public void saveMySqlJob(TblJobMySql jobMySql) {
//        this.tblJobMySqlDao.saveJob(jobMySql);
//    }
//
//    @Override
//    public TblJob findByid(String jid) {
//        return tblJobDao.selectJid(jid);
//    }
//
//    @Override
//    public TblJobMySql findByMySqlid(String jid) {
//        return tblJobMySqlDao.selectJid(jid);
//    }
//
//    @Override
//    public void delete(TblJob selectedId) {
//        tblJobDao.deleteSelectedId(selectedId);
//    }
//
//    @Override
//    public void deleteMySql(TblJobMySql MySqlselectedId) {
//        tblJobMySqlDao.deleteSelectedId(MySqlselectedId);
//    }
//
//    @Override
//    public Map<String, Object> list(Integer pageNumber, Integer pageSize, String token, String staffId) {
//        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
//            Map<String, Object> resultMap = new HashMap<String, Object>(0);
//            Jedis jedis = null;
//            if (token == null && staffId == null) {
//                resultMap.put("code", "0");
//                resultMap.put("msg", "用户已失效！");
//                return resultMap;
//            }
//            if (token == null && staffId != null) {
//                jedis = JedisUtil.getJedis();
//                token = jedis.get(staffId);
//            }
//            Claims claims = JwtUtils.parseJwt(token);
//            Object object = claims.get("staffInfo");
//            JSONObject objJson = JSONObject.fromObject(object);
//            TblStaff user = (TblStaff) JSONObject.toBean(objJson, TblStaff.class);
//            BigDecimal companyId = user.getCurrentOrg().getOrgid();
//            PageInfo<TblJob> pageInfo = new PageInfo<TblJob>();
//            pageInfo.setCurrentPage(pageNumber);
//            pageInfo.setPageSize(pageSize);
//            pageInfo.setTlist(tblJobDao.selectListByPageInfo(pageInfo, companyId));
//            pageInfo.setTotalRecord(tblJobDao.selectListByPageInfoCount(pageInfo, companyId));
//            resultMap.put("code", "1");
//            resultMap.put("msg", "访问接口成功");
//            resultMap.put("pageInfo", pageInfo);
//            return resultMap;
//        } else {
//            Map<String, Object> resultMap = new HashMap<String, Object>(0);
//            Jedis jedis = null;
//            if (token == null && staffId == null) {
//                resultMap.put("code", "0");
//                resultMap.put("msg", "用户已失效！");
//                return resultMap;
//            }
//            if (token == null && staffId != null) {
//                jedis = JedisUtil.getJedis();
//                token = jedis.get(staffId);
//            }
//            Claims claims = JwtUtils.parseJwt(token);
//            Object object = claims.get("staffInfo");
//            JSONObject objJson = JSONObject.fromObject(object);
//            TblStaff user = (TblStaff) JSONObject.toBean(objJson, TblStaff.class);
//            BigDecimal companyId = user.getCurrentOrg().getOrgid();
//            PageInfo<TblJobMySql> pageInfo = new PageInfo<TblJobMySql>();
//            pageInfo.setCurrentPage(pageNumber);
//            pageInfo.setPageSize(pageSize);
//            pageInfo.setTlist(tblJobMySqlDao.selectListByPageInfo(pageInfo, companyId));
//            pageInfo.setTotalRecord(tblJobMySqlDao.selectListByPageInfoCount(pageInfo, companyId));
//            resultMap.put("code", "1");
//            resultMap.put("msg", "访问接口成功");
//            resultMap.put("pageInfo", pageInfo);
//            return resultMap;
//        }
//    }
//
//    @Override
//    public TblJob findByJobId(String jobid) {
//        List<TblJob> list = this.tblJobDao.findByRid(jobid);
//        return list != null && list.size() > 0 ? (TblJob) list.get(0) : null;
//    }
//
//    @Override
//    public TblJobMySql findByMySqlJobId(String jobid) {
//        List<TblJobMySql> list = this.tblJobMySqlDao.findByRid(jobid);
//        return list != null && list.size() > 0 ? (TblJobMySql) list.get(0) : null;
//    }
//
//    @Override
//    public void updateJob(TblJob newJob) {
//        tblJobDao.updateJob(newJob);
//    }
//
//    @Override
//    public void updateMySqlJob(TblJobMySql jobMySql) {
//        tblJobMySqlDao.updateJob(jobMySql);
//    }
//
//    @Override
//    public Map<String, Object> listJob(Integer pageNumber, Integer pageSize, String token) {
//        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
//            Map<String, Object> resultMap = new HashMap<String, Object>(0);
//            try {
//                TblStaffUtil user = DealUserToken.parseUserToken(token);
//                BigDecimal companyId = user.getCurrentOrg().getOrgid();
//                PageInfo<TblJob> pageInfo = new PageInfo<TblJob>();
//                pageInfo.setCurrentPage(pageNumber);
//                pageInfo.setPageSize(pageSize);
//                pageInfo.setTlist(tblJobDao.selectListByPageInfo(pageInfo, companyId));
//                pageInfo.setTotalRecord(tblJobDao.selectListByPageCount(pageInfo, companyId));
//                resultMap.put("code", "1");
//                resultMap.put("msg", "访问接口成功");
//                resultMap.put("pageInfo", pageInfo);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return resultMap;
//        } else {
//            Map<String, Object> resultMap = new HashMap<String, Object>(0);
//            try {
//                TblStaffUtil user = DealUserToken.parseUserToken(token);
//                BigDecimal companyId = user.getCurrentOrg().getOrgid();
//                PageInfo<TblJobMySql> pageInfo = new PageInfo<TblJobMySql>();
//                pageInfo.setCurrentPage(pageNumber);
//                pageInfo.setPageSize(pageSize);
//                pageInfo.setTlist(tblJobMySqlDao.selectListByPageInfo(pageInfo, companyId));
//                pageInfo.setTotalRecord(tblJobMySqlDao.selectListByPageCount(pageInfo, companyId));
//                resultMap.put("code", "1");
//                resultMap.put("msg", "访问接口成功");
//                resultMap.put("pageInfo", pageInfo);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return resultMap;
//        }
//    }
//
//    @Override
//    public void deleteJob(BigDecimal jobid) {
//        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
//            tblJobDao.deleteJob(jobid);
//        } else {
//            tblJobMySqlDao.deleteJob(jobid);
//        }
//    }
//
//	
//	@Override
//	public Map<String, Object> syncPost(Integer operaType, String data) throws Exception {
//        Map<String, Object> resultMap = new HashMap<String, Object>(0);
//		try {
//			JSONArray arr= JSONArray.fromObject(data);
//			for(Object obj:arr){
//				 JSONObject js=(JSONObject)obj;
//				//先查询之前是否有对应的数据
//				List<TblJob> tbl=tblJobDao.findbyHistoryId(js.getString("jobid"));
//				if(tbl==null){
//						TblJob tb=new TblJob();
//						tb.setHistorycode(js.getString("jobid"));
//						tb.setJobname(js.getString("jobname"));
//						tb.setDatasource("zz");
//						tb.setCompanyId(js.getString("companyid"));
//						tb.setStatus(js.getString("status"));
//						tblJobDao.saveJob(tb);
//				}else {
//					TblJob tb=tbl.get(0);
//					tb.setHistorycode(js.getString("jobid"));
//					tb.setJobname(js.getString("jobname"));
//					tb.setDatasource("zz");
//					tb.setCompanyId(js.getString("companyid"));
//					tb.setStatus(js.getString("status"));
//					tblJobDao.updateJob(tb);
//				}
//			}
//			  tblJobDao.updateZzCategoryid();
//			  resultMap.put("code", "0");
//              resultMap.put("msg", "岗位同步成功");
//			System.out.println("=================岗位同步成功!====================");
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		return resultMap;
//	}
//
//	//人资接口同步
//	@Override
//	public void syncJob(String data) throws Exception {
//		// TODO Auto-generated method stub
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//		if (!StringUtils.isEmpty(data)) {
//			try {
//			net.sf.json.JSONObject paramStr = net.sf.json.JSONObject.fromObject(data);
//			if(paramStr.get("success").toString()=="true"){
//				net.sf.json.JSONObject msg = net.sf.json.JSONObject.fromObject(paramStr.get("msg").toString());
//			net.sf.json.JSONArray dataList = net.sf.json.JSONArray.fromObject(msg.get("dataList"));
//			for (Object o : dataList) {
//				net.sf.json.JSONObject obj = (net.sf.json.JSONObject) o;
//				List<TblJob> tbl = tblJobDao.findbyHistoryId(obj.getString("id"));
//				if (tbl.size()==0) {
//					TblJob job = new TblJob();
//					job.setHistorycode(obj.getString("id"));
//					job.setDatasource("zz");
//					job.setJobname(obj.getString("name"));
//					job.setStatus(obj.getString("isDelete") == "true" ? "1" : "0");
//					job.setCode(obj.getString("code"));
//					job.setCategoryId(obj.getString("categoryId"));
//					job.setCategoryName(obj.getString("categoryName"));
//					job.setCreateTime(sdf.parse(obj.getString("createTime")));
//					job.setDescription(obj.getString("description"));
//					tblJobDao.saveJob(job);
//				} else {
//					TblJob job = tbl.get(0);
//					job.setHistorycode(obj.getString("id"));
//					job.setDatasource("zz");
//					job.setJobname(obj.getString("name"));
//					job.setStatus(obj.getString("isDelete") == "true" ? "1" : "0");
//					job.setCode(obj.getString("code"));
//					job.setCategoryId(obj.getString("categoryId"));
//					job.setCategoryName(obj.getString("categoryName"));
//					job.setDescription(obj.getString("description"));
//					tblJobDao.updateJob(job);
//				}
//			}
//			
//			}
//			//最后差一个修改categoryId的sql
//			System.out.println("========岗位同步已完成!===========");
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//		}
//			
//	}
//	
}
