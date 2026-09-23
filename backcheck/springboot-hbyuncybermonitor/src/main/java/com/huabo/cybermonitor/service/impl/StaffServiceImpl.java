package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.StaffMapper;
import com.huabo.cybermonitor.service.IStaffService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    @Resource
    StaffMapper staffMapper;
    // 老项目 预警推送方法 userServiceImple.PageBean findAllPageBeanPid(Integer startIndex,  int pageSize,TblOrganization organization){
    public void findAllPageBeanPid(IPage ip,  Organization organization) {
        long start = (ip.getCurrent() - 1) * ip.getSize();
        long end = start + ip.getSize();
        System.out.println(start+"--------"+end);
        List<Map<String,Object>> list;
        long count=0;
        if(organization.getOrgtype()!=null && organization.getOrgtype().toString().equals("0")){
            list=staffMapper.getList2(start,end,organization);
            count=staffMapper.getCount2(organization);
        }else{
            list=staffMapper.getList(start,end,organization);
            count=staffMapper.getCount(organization);
        }
        System.out.println("列表功能:"+list);
        ip.setRecords(list);
        ip.setTotal(count);

    }

    @Override
    public void setRealName(IPage<MonitorSolution> page) {
        if(page!=null){
            final List<MonitorSolution> records = 
                    page.getRecords();

            for (MonitorSolution record : records) {
                if(record.getStaffid()!=null){
                    record.setRealname(this.getById(record.getStaffid()).getRealname());
                }
            }
            
        }
    }

    @Override
    public void setRealNameForMonitorSolutionresult(IPage<MonitorSolutionresult> page) {
        if(page!=null){
            final List<MonitorSolutionresult> records =
                    page.getRecords();

            for (MonitorSolutionresult record : records) {
                if(record.getStaffid()!=null){
                    record.setRealname(this.getById(record.getStaffid()).getRealname());
                }
            }

        }
    }

    @Override
    public void setRealNameIndicator(List<Indicator> set) {
           if(set!=null){
               for (Indicator record : set) {
                   if(record.getStaffid()!=null){
                       record.setRealname(this.getById(record.getStaffid()).getRealname());
                   }
               }
           }
    }
}
