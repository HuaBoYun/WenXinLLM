package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.fxgl.entity.Organization;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.mapper.StaffMapper;
import com.huabo.fxgl.service.IOrganizationService;
import com.huabo.fxgl.service.IStaffService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author ccc
 * @since 2022-07-15
 */
@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements IStaffService {


    @Autowired
    private IOrganizationService organizationService;
    
    @Resource
    private UserProvider userProvider;

   
    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 通过前端传入的pid-组织编号查询工作人员
     * @Date 2022/8/4
     * @param pid
     * @param type
     * @param organization
     * @param pageNumber
     * @return com.hbfk.util.JsonBean
     * @url:
     **/
    @Override
    public JsonBean userList(String pid, String type, String token, Integer pageNo, Integer pageSize) throws Exception {
        TblStaffUtil tblStaffUtil = userProvider.get();
        TblOrganizationUtil linkOrg = tblStaffUtil.getLinkOrg();
        TblOrganizationUtil currentOrg = tblStaffUtil.getCurrentOrg();//用户选择的组织
        Organization organization = new Organization();
        BeanUtils.copyProperties(currentOrg,organization);
        if(StringUtils.isNotEmpty(pid)){
            organization=organizationService.getById(pid);
        }
        QueryWrapper<Staff> queryWrapper=new QueryWrapper<>();
        if(organization.getOrgtype()!=null && organization.getOrgtype().toString().equals("0")){
            queryWrapper.eq("ORGID",organization.getOrgid()).apply("(STATUS is NULL or STATUS != 0)");
        }else {
            List<BigDecimal> ids= organizationService.getIdsByFatherId(organization.getOrgid());
            queryWrapper.in(ids!=null&&ids.size()>0,"ORGID",ids).apply("(STATUS is NULL or STATUS != 0)");
        }
        queryWrapper.orderByDesc("STAFFID");
        Page<Staff> page=new Page<Staff>(pageNo, pageSize);
        page(page,queryWrapper);
        for (Staff staff : page.getRecords()) {
            staff.setOrganization(organizationService.getById(staff.getOrgid()));
        }
        Map<String,Object> map=new HashMap<>();
        map.put("pageNo", pageNo);
        map.put("type",type);
        map.put("data",page);
        map.put("organization",organization);
        return new JsonBean(1,"操作成功",map);
    }

    @Override
    public IPage findAllPageBeanPid(IPage page, Organization organization) {
        IPage pages = null;
        if (organization.getOrgtype() != null && organization.getOrgtype().toString().equals("0")) {
            pages = baseMapper.selectStaffByOrgid(organization.getOrgid(), page);
        } else {
            pages = baseMapper.selectStaffByFatherOrgid(organization.getOrgid(), page);
        }
//        String sqlCount = "select count(*) from ("+sql+")";

      /*  if("MySql".equals(SysConfig.get("databaseType"))){
            sqlCount += " AS T2";
        }*/
        return pages;

    }

	@Override
	public String findRealNameById(String staffid) {
		// TODO Auto-generated method stub
		return baseMapper.findRealNameById(staffid);
	}
	
	@Override
	public String selectNamesByids(String ids) throws Exception {
		// TODO Auto-generated method stub
		String names="";
		try {
			if(StringUtils.isNotBlank(ids)){
				List<Staff> list=baseMapper.getOrgNameByIds(ids.split(","));
				names = list.stream()
                        .map(Staff::getRealname)  // 提取name字段
                        .collect(Collectors.joining(", "));  // 以逗号分隔
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return names;
	}
	
	@Override
	public String selectIdsByNames(String ids,BigDecimal orgid) throws Exception {
		// TODO Auto-generated method stub
		String names="";
		try {
			if(StringUtils.isNotBlank(ids)){
				List<Staff> list=baseMapper.getOrgIdByNames(ids.split(","),orgid);
				names = list.stream()
                        .map(Staff::getStaffid)
                        .map(BigDecimal::toString)// 提取name字段
                        .collect(Collectors.joining(", "));  // 以逗号分隔
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return names;
	}
	
	@Override
	public String selectNamesByNames(String ids,BigDecimal orgid) throws Exception {
		// TODO Auto-generated method stub
		String names="";
		try {
			if(StringUtils.isNotBlank(ids)){
				List<Staff> list=baseMapper.getOrgIdByNames(ids.split(","),orgid);
				names = list.stream()
                        .map(Staff::getRealname)
                        .collect(Collectors.joining(", "));  // 以逗号分隔
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return names;
	}

	@Override
	public String selectNameByids(BigDecimal id) throws Exception {
		String name="";
		try {
			if(id!=null&&id.compareTo(new BigDecimal(0))>0){
				 Staff  staff=baseMapper.selectById(id);
				 name=staff.getRealname();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return name;
	}

	@Override
	public List<Staff> getStaffsByOrgids(String ids, List<BigDecimal> orgids) throws Exception {
		// TODO Auto-generated method stub
		List<Staff> list=null;
		try {
			if(StringUtils.isNotBlank(ids)){
				  list=baseMapper.getOrgIdByNames2(ids.split(","),orgids);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
    
}
