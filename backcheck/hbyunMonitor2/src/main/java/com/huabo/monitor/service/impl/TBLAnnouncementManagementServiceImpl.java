package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TBLAnnouncementManagement;
import com.huabo.monitor.mapper.TBLAnnouncementManagementMapper;
import com.huabo.monitor.service.TBLAnnouncementManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class TBLAnnouncementManagementServiceImpl implements TBLAnnouncementManagementService {
    @Autowired
    private TBLAnnouncementManagementMapper tblAnnouncementManagementMapper;
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean saveOrUpdate(TBLAnnouncementManagement entity) throws Exception {
        //用户是否登录
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        // 数据校验
        if (StringUtils.isBlank(entity.getTitle())) {
            return new JsonBean(0, "公告标题不能为空", null);
        }
        if (entity.getTitle().length() > 200) {
            return new JsonBean(0, "公告标题长度不能超过200字符", null);
        }
        if (StringUtils.isBlank(entity.getAcontent())) {
            return new JsonBean(0, "公告内容不能为空", null);
        }
        if (entity.getAcontent().length() > 5000) {
            return new JsonBean(0, "公告内容长度不能超过5000字符", null);
        }
        if (entity.getExpirationTime() == null) {
            return new JsonBean(0, "失效时间不能为空", null);
        }
        if (entity.getExpirationTime().before(new Date())) {
            return new JsonBean(0, "失效时间不能早于当前时间", null);
        }

        //新增
        if (entity.getId() == null || entity.getId().compareTo(BigDecimal.ZERO) == 0) {
            entity.setCreateTime(new Date());
            entity.setIsDelete(0);
            entity.setStatus(6);
            entity.setId(RandomUtil.uuBigDecimalId());
            entity.setStaffid(staff.getStaffid());
            entity.setStaffName(staff.getRealname());
            int result = tblAnnouncementManagementMapper.insert(entity);
            if (result > 0) {
                return new JsonBean(1, "success", entity);
            } else
                return new JsonBean(0, "error", null);
        } else {
            //修改
            TBLAnnouncementManagement tblAnnouncementManagement = tblAnnouncementManagementMapper.selectById(entity.getId());
            if (tblAnnouncementManagement == null) {
                return new JsonBean(0, "内容不存在", null);
            }

            // 权限验证：只有创建者可以修改
            if (!tblAnnouncementManagement.getStaffid().equals(staff.getStaffid())) {
                return new JsonBean(0, "无权限修改此公告", null);
            }


            int result = tblAnnouncementManagementMapper.updateById(entity);
            if (result > 0) {
                return new JsonBean(1, "修改成功", entity);
            } else
                return new JsonBean(0, "修改失败", null);
        }
    }

    @Override
    public JsonBean getHomepage_List(Integer pageNumber, Integer pageSize, String title, String start, String end, String createTimeStart, String createTimeEnd, Integer type) throws Exception {
        //用户是否登录
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        QueryWrapper<TBLAnnouncementManagement> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }
        if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            queryWrapper.between("EXPIRATION_TIME", LocalDate.parse(start, formatter), LocalDate.parse(end, formatter));
        }
        if (StringUtils.isNotBlank(createTimeStart) && StringUtils.isNotBlank(createTimeEnd)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            queryWrapper.between("CREATE_TIME", LocalDate.parse(createTimeStart, formatter), LocalDate.parse(createTimeEnd, formatter));
        }
        if (type != null && type != 0) {
            if (type == 1){
                queryWrapper.ge("EXPIRATION_TIME", LocalDate.now());
            }else
                queryWrapper.lt("EXPIRATION_TIME", LocalDate.now());
        }
        queryWrapper.eq("is_delete", 0);
        queryWrapper.orderByDesc("create_time");
        PageHelper.startPage(pageNumber, pageSize);
        PageInfo<TBLAnnouncementManagement> pageInfo = new PageInfo<>(tblAnnouncementManagementMapper.selectList(queryWrapper));
        List<TBLAnnouncementManagement> list = pageInfo.getList();
        for (TBLAnnouncementManagement tblAnnouncementManagement : list) {
            if (tblAnnouncementManagement.getExpirationTime().before(new Date())) {
                tblAnnouncementManagement.setIsValid("无效");
            } else
                tblAnnouncementManagement.setIsValid("有效");
        }
        return new JsonBean(1, "success", pageInfo);
    }

    @Override
    public JsonBean deleteById(BigDecimal id) throws Exception {
        //用户是否登录
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TBLAnnouncementManagement tblAnnouncementManagement = tblAnnouncementManagementMapper.selectById(id);
        if (tblAnnouncementManagement == null) {
            return new JsonBean(0, "内容不存在", null);
        }

        // 权限验证：只有创建者可以删除
        if (!tblAnnouncementManagement.getStaffid().equals(staff.getStaffid())) {
            return new JsonBean(0, "无权限删除此公告", null);
        }

        tblAnnouncementManagement.setIsDelete(1);
        int result = tblAnnouncementManagementMapper.updateById(tblAnnouncementManagement);
        if (result > 0) {
            return new JsonBean(1, "删除成功", null);
        } else
            return new JsonBean(0, "删除失败", null);
    }

    @Override
    public JsonBean getDetail(BigDecimal id) throws Exception {
        //用户是否登录
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TBLAnnouncementManagement tblAnnouncementManagement = tblAnnouncementManagementMapper.selectById(id);
        if (tblAnnouncementManagement == null) {
            return new JsonBean(0, "内容不存在", null);
        }
        return new JsonBean(1, "success", tblAnnouncementManagement);
    }

    @Override
    public JsonBean getIndexList(Integer pageNumber, Integer pageSize) throws Exception {
        //用户是否登录
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        // 限制页面大小
        if (pageSize > 100) {
            pageSize = 100;
        }

        PageHelper.startPage(pageNumber, pageSize);
        QueryWrapper<TBLAnnouncementManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("EXPIRATION_TIME", LocalDate.now());
        queryWrapper.eq("IS_DELETE", 0);
        queryWrapper.eq("STATUS", 6);
        queryWrapper.orderByDesc("CREATE_TIME");
        List<TBLAnnouncementManagement> tblAnnouncementManagements = tblAnnouncementManagementMapper.selectList(queryWrapper);

        // 使用PageInfo包装分页信息
        PageInfo<TBLAnnouncementManagement> pageInfo = new PageInfo<>(tblAnnouncementManagements);
        return new JsonBean(1, "查询成功", pageInfo);
    }


}
