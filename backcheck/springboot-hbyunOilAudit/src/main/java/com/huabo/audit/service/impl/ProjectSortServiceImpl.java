package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.ProjectSortEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.ProjectSortMapper;
import com.huabo.audit.service.ProjectSortService;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huabo.audit.util.PageInfoUtil;
/**
 * @author Rui
 * @ClassName ProjectSortServiceImpl
 * @Description
 * @DATE 2023/9/23
 */
@Service
public class ProjectSortServiceImpl implements ProjectSortService {

    @Autowired
    private ProjectSortMapper projectSortMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, String projectName) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

        ProjectSortEntity projectSortEntity = new ProjectSortEntity();

        if(StringUtil.isNotEmpty(projectName)){
            projectSortEntity.setProjectName(projectName);
        }

        Page<ProjectSortEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> projectSortMapper.selectByEntity(projectSortEntity));
		PageInfo<ProjectSortEntity> pageInfo = new PageInfoUtil<ProjectSortEntity>().parsePageInfo(page);


        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception{

        ProjectSortEntity projectSortEntity = projectSortMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", projectSortEntity);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public void updateEntity(ProjectSortEntity projectSortEntity) throws Exception{
        projectSortMapper.updateEntity(projectSortEntity);
    }

    @Override
    public void saveEntity(String token, ProjectSortEntity projectSortEntity) throws Exception{
        TblStaffUtil user = userProvider.get();
        if(user != null) {
            TblStaff tblStaff = new TblStaff();
            tblStaff.setStaffid(user.getStaffid());
            projectSortEntity.setCreateUser(tblStaff);
        }
        projectSortMapper.insertEntity(projectSortEntity);
    }

    @Override
    public void deleteByIds(String ids) throws Exception{

        projectSortMapper.deleteEntity(ids);
    }


}
