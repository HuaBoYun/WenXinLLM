package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JwtUtils;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblBiReportMenu;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.mapper.TblBiReportMenuMapper;
import com.huabo.system.mapper.TblBiUserPageMapper;
import com.huabo.system.service.TblBiReportMenuService;

import io.jsonwebtoken.Claims;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

@Service
public class TblBiReportMenuServiceImpl implements TblBiReportMenuService {

    @Resource
    private TblBiReportMenuMapper tblBiReportMenuMapper;
    
    @Resource
    private TblBiUserPageMapper tblBiUserPageMapper;
    
    @Resource
    private UserProvider userProvider;


    @Override
    public TblBiReportMenu geTblBiReportMenu(String id) {
        return tblBiReportMenuMapper.selectId(id);
    }

    @Override
    public Object isExistBiPageCode(String code) {
        Integer num = 0;
            num = tblBiReportMenuMapper.selectCode(code);
        return num > 0 ? false : true;
    }

    @Override
    public void updateReportMenu(TblBiReportMenu page) {
        tblBiReportMenuMapper.updateReportMenu(page);
    }

    @Override
    public void saveReportMenu(TblBiReportMenu tbrm) {
        tblBiReportMenuMapper.insertTbrm(tbrm);
    }


    @Override
    public void deleteReportMenu(TblBiReportMenu geTblBiReportMenu) {
        tblBiReportMenuMapper.deleteById(geTblBiReportMenu.getPageid());
    }


    @Override
    public TblBiReportMenu selectTblBiReportMenu(String idStr) {
            return tblBiReportMenuMapper.selectTblBiReportMenu(idStr);
    }

    @Override
    public void addPage(TblBiReportMenu page) {
        tblBiReportMenuMapper.addPage(page);
    }

    @Override
    public List<TblBiReportMenu> selectByFatherid(String idStr) {
            return tblBiReportMenuMapper.selectIdStr(idStr);
    }

    @Override
    public Map<String, Object> finreportMenuList(Integer pageNumber, Integer pageSize, String token, String staffId, String type) {

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
            PageInfo<TblBiReportMenu> pageInfo = new PageInfo<TblBiReportMenu>();
            Claims claims = JwtUtils.parseJwt(token);
            Object object = claims.get("staffInfo");
            JSONObject objJson = JSONObject.fromObject(object);
            TblStaff staff = (TblStaff) JSONObject.toBean(objJson, TblStaff.class);
            BigDecimal orgid = staff.getCurrentOrg().getOrgid();

            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            
            Page<TblBiReportMenu> page = new Page<TblBiReportMenu>(pageNumber,pageSize);
            page.setOptimizeCountSql(false); // 禁用自动优化
            IPage<TblBiReportMenu> pageList = tblBiReportMenuMapper.selectMenuList(page, orgid, type);
            
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            resultMap.put("data", pageInfo);

        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");

        return resultMap;

    }

    @Override
    public TblBiReportMenu geTblBiReport(BigDecimal pageid) {
            return tblBiReportMenuMapper.geTblBiReport(pageid);
    }

    @Override
    public JsonBean deleteReportMenuPageId(BigDecimal pageid, String token, String type) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
    	Integer count = 0;
    	
        	//判断该有没有下发过
        	//1.判断有没有下发给用户
        	count = this.tblBiUserPageMapper.selectPageCount(pageid);
        	if(count > 0) {
        		return ResponseFormat.retParam(0, "已下发给用户，无法删除", null);
        	}
        	//1.判断有没有下发至模块
        	count = this.tblBiUserPageMapper.selectModuleTypePageCount(pageid);
        	if(count > 0) {
        		return ResponseFormat.retParam(0, "已下发至模块，无法删除", null);
        	}
        	//删掉自己
            tblBiReportMenuMapper.deletePageId(pageid);
            //一级仓库删除时 要删除所有二级信息
            if("1".equals(type)) {
            	tblBiReportMenuMapper.deleteByFatherId(pageid);
            }
        return ResponseFormat.retParam(1, 200, null);
    }

	@Override
	public JsonBean distributeThemeReportToUser(String token, Integer type, String[] pageIds, String[] staffIds, String fatherId)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        Integer count = 0 ;//判断当前用户是否有这张报表
        List<TblBiReportMenu> chilList = null;
        //根据下发类型判断下发主题层级
        if(type == 1) {
        	//下发的一级主题实现逻辑
        	//先循环人员进行处理
        	for(String staffId:staffIds) {
        		//分批次循环报表主题进行判断
        		for(String pageId : pageIds) {
        			//1.先判断一级主题有没有下发过给当前用户
        			count = this.tblBiUserPageMapper.selectUserPageCount(staffId,pageId);
        			if(count == 0) {
        				//一级主题没哟下发则下发一级主题 pageType为一级主题默认为0
        				tblBiUserPageMapper.insertEntity(staffId,pageId,"0");
        			}
        			//查询当前一级主题下所有的二级主题并放放入到关系中
        			chilList = this.tblBiReportMenuMapper.selectListByFatherId(pageId);
        			for (TblBiReportMenu menu : chilList) {
        				count = this.tblBiUserPageMapper.selectUserPageCount(staffId,menu.getPageid().toString());
        				if(count != 0) {
        					continue;
        				}
        				tblBiUserPageMapper.insertEntity(staffId,menu.getPageid().toString(),menu.getType());
					}
        		}
        	}
        }else {
        	TblBiReportMenu menu = null;
        	for(String staffId:staffIds) {
	        	//如果二级仓库主题 则先判断一级仓库主题有没有下发过
	        	count = this.tblBiUserPageMapper.selectUserPageCount(staffId,fatherId);
	        	if(count == 0) {
    				//一级主题没哟下发则下发一级主题 pageType为一级主题默认为0
    				tblBiUserPageMapper.insertEntity(staffId,fatherId,"0");
    			}
	        	//循环判断二级主题有没有下发过
	        	for(String pageId : pageIds) {
	        		count = this.tblBiUserPageMapper.selectUserPageCount(staffId,pageId);
        			if(count != 0) {
        				continue;
        			}
        			//一级主题没哟下发则下发一级主题 pageType为一级主题默认为0
    				menu = this.tblBiReportMenuMapper.selectTblBiReportMenu(pageId);
    				tblBiUserPageMapper.insertEntity(staffId,pageId,menu.getType());
	        	}
        	}
        }
        return ResponseFormat.retParam(1, 200, null);
	}
}
