package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JwtUtils;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblJob;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.mapper.TblJobDao;
import com.huabo.system.service.TblJobService;

import io.jsonwebtoken.Claims;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

@Service("TblJobService")
public class TblJobServiceImpl implements TblJobService {

    @Resource
    public TblJobDao tblJobDao;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public List<TblJob> findAll(BigDecimal companyid) {
        return this.tblJobDao.listBySql(companyid);
    }

    @Override
    public void saveJob(TblJob job) {
        this.tblJobDao.saveJob(job);
    }

    @Override
    public TblJob findByid(String jid) {
        return tblJobDao.selectJid(jid);
    }

    @Override
    public void delete(TblJob selectedId) {
        tblJobDao.deleteSelectedId(selectedId);
    }

    @Override
    public Map<String, Object> list(Integer pageNumber, Integer pageSize, String token, String staffId) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            Jedis jedis = null;
            if (token == null && staffId == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            if (token == null && staffId != null) {
                jedis = JedisUtil.getJedis();
                token = jedis.get(staffId);
                if(jedis!=null) {
            		jedis.close();
            	}
            }
            Claims claims = JwtUtils.parseJwt(token);
            Object object = claims.get("staffInfo");
            JSONObject objJson = JSONObject.fromObject(object);
            TblStaff user = (TblStaff) JSONObject.toBean(objJson, TblStaff.class);
            BigDecimal companyId = user.getCurrentOrg().getOrgid();
            PageInfo<TblJob> pageInfo = new PageInfo<TblJob>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            
            Page<TblJob> page = new Page<TblJob>(pageNumber,pageSize);
            page.setOptimizeCountSql(false); // 禁用自动优化
            IPage<TblJob> pageList = tblJobDao.selectListByPageInfo(page, companyId,null,null,null);
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("pageInfo", pageInfo);
            return resultMap;
    }

    @Override
    public TblJob findByJobId(String jobid) {
        List<TblJob> list = this.tblJobDao.findByRid(jobid);
        return list != null && list.size() > 0 ? (TblJob) list.get(0) : null;
    }

    @Override
    public void updateJob(TblJob newJob) {
        tblJobDao.updateJob(newJob);
    }

    @Override
    public Map<String, Object> listJob(Integer pageNumber, Integer pageSize, String token, String orgIds, String jobName, String orgName) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil user = userProvider.get();
                BigDecimal companyId = user.getCurrentOrg().getOrgid();
                PageInfo<TblJob> pageInfo = new PageInfo<TblJob>();
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                
                Page<TblJob> page = new Page<TblJob>(pageNumber,pageSize);
                page.setOptimizeCountSql(false); // 禁用自动优化
                IPage<TblJob> pageList = tblJobDao.selectListByPageInfo(page, companyId,orgIds,jobName,orgName);
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int) pageList.getTotal());
                resultMap.put("code", "1");
                resultMap.put("msg", "访问接口成功");
                resultMap.put("pageInfo", pageInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
    }

    @Override
    public void deleteJob(BigDecimal jobid) {
            tblJobDao.deleteJob(jobid);
    }

	
	@Override
	public Map<String, Object> syncPost(Integer operaType, String data) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			JSONArray arr= JSONArray.fromObject(data);
			for(Object obj:arr){
				 JSONObject js=(JSONObject)obj;
				//先查询之前是否有对应的数据
				List<TblJob> tbl=tblJobDao.findbyHistoryId(js.getString("jobid"));
				if(tbl==null){
						TblJob tb=new TblJob();
						tb.setHistorycode(js.getString("jobid"));
						tb.setJobname(js.getString("jobname"));
						tb.setDatasource("zz");
						tb.setCompanyId(js.getString("companyid"));
						tb.setStatus(js.getString("status"));
						tb.setJobid(RandomUtil.uuBigDecimalId());
						tblJobDao.saveJob(tb);
				}else {
					TblJob tb=tbl.get(0);
					tb.setHistorycode(js.getString("jobid"));
					tb.setJobname(js.getString("jobname"));
					tb.setDatasource("zz");
					tb.setCompanyId(js.getString("companyid"));
					tb.setStatus(js.getString("status"));
					tblJobDao.updateJob(tb);
				}
			}
			  tblJobDao.updateZzCategoryid();
			  resultMap.put("code", "0");
              resultMap.put("msg", "岗位同步成功");
			System.out.println("=================岗位同步成功!====================");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultMap;
	}

	//人资接口同步
	@Override
	public void syncJob(String data) throws Exception {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if (!StringUtils.isEmpty(data)) {
			try {
			net.sf.json.JSONObject paramStr = net.sf.json.JSONObject.fromObject(data);
			if(paramStr.get("success").toString()=="true"){
				net.sf.json.JSONObject msg = net.sf.json.JSONObject.fromObject(paramStr.get("msg").toString());
			net.sf.json.JSONArray dataList = net.sf.json.JSONArray.fromObject(msg.get("dataList"));
			for (Object o : dataList) {
				net.sf.json.JSONObject obj = (net.sf.json.JSONObject) o;
				List<TblJob> tbl = tblJobDao.findbyHistoryId(obj.getString("id"));
				if (tbl.size()==0) {
					TblJob job = new TblJob();
					job.setHistorycode(obj.getString("id"));
					job.setDatasource("zz");
					job.setJobname(obj.getString("name"));
					job.setStatus(obj.getString("isDelete") == "true" ? "1" : "0");
					job.setCode(obj.getString("code"));
					job.setCategoryId(obj.getString("categoryId"));
					job.setCategoryName(obj.getString("categoryName"));
					job.setCreateTime(sdf.parse(obj.getString("createTime")));
					job.setDescription(obj.getString("description"));
					job.setJobid(RandomUtil.uuBigDecimalId());
					tblJobDao.saveJob(job);
				} else {
					TblJob job = tbl.get(0);
					job.setHistorycode(obj.getString("id"));
					job.setDatasource("zz");
					job.setJobname(obj.getString("name"));
					job.setStatus(obj.getString("isDelete") == "true" ? "1" : "0");
					job.setCode(obj.getString("code"));
					job.setCategoryId(obj.getString("categoryId"));
					job.setCategoryName(obj.getString("categoryName"));
					job.setDescription(obj.getString("description"));
					tblJobDao.updateJob(job);
				}
			}
			
			}
			//最后差一个修改categoryId的sql
			System.out.println("========岗位同步已完成!===========");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
			
	}
	
}
