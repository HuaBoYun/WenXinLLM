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
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblCourse;
import com.huabo.system.entity.TblVideoType;
import com.huabo.system.mapper.TblCourseMapper;
import com.huabo.system.mapper.TblVideoTypeMapper;
import com.huabo.system.service.TblCourseService;

@Service
public class TblCourseServiceImpl implements TblCourseService {
    @Resource
    private TblCourseMapper courseMapper;

    @Resource
    private TblVideoTypeMapper tblVideoTypeMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public void savetblCourse(TblCourse tblCourse) {
        courseMapper.savetblCourse(tblCourse);
    }

    @Override
    public void updatetblCourse(TblCourse tblCourse) {
        courseMapper.updatetblCourse(tblCourse);
    }

    @Override
    public TblCourse geTblCourse(String id) {
        return courseMapper.selectByCourseId(id);
    }

    @Override
    public List<TblCourse> findByFatherid(String pid) {
        return courseMapper.listBypid(pid);
    }

    @Override
    public Map<String, Object> courseList(Integer pageNumber, Integer pageSize, String token, String staffId, String coursename1, String coursetype1) {


        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            if (token == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            TblStaffUtil staff = userProvider.get();
            BigDecimal orgid = staff.getCurrentOrg().getOrgid();

                List<TblVideoType> tblVideoTypes = tblVideoTypeMapper.findByorgid(staff.getCurrentOrg().getOrgid());
                PageInfo<TblCourse> pageInfo = new PageInfo<TblCourse>();
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                
                Page<TblCourse> page = new Page<TblCourse>(pageNumber,pageSize);
                page.setOptimizeCountSql(false); // 禁用自动优化
                IPage<TblCourse> pageList = courseMapper.selectListByPageInfo(page, orgid, coursename1, coursetype1);
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int) pageList.getTotal());
                Map<String, Object> dataMap = new HashMap<String, Object>(0);
                dataMap.put("pageInfo", pageInfo);
                dataMap.put("typelist", tblVideoTypes);
                dataMap.put("coursename1", coursename1);
                dataMap.put("coursetype1", coursetype1);
                resultMap.put("data", dataMap);

            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> deletetblCourse(String id) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            courseMapper.deleteByCourseId(id);
        resultMap.put("code", "1");
        resultMap.put("msg", "删除成功");
        return resultMap;

    }
}
