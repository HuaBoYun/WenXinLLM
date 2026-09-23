package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.monitor.entity.TblStaff;
import com.huabo.monitor.mapper.TblStaffMapper;
import com.huabo.monitor.service.ITblStaffService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@Service

public class TblStaffServiceImpl extends ServiceImpl<TblStaffMapper, TblStaff> implements ITblStaffService {
    @Resource
    TblStaffMapper  staffMapper;


    @Override
    public void findStaffByOrgid(IPage<TblStaff> page, BigDecimal orgid) {
        staffMapper.findStaffByOrgid(page,orgid);
    }
    
    @Override
    public TblStaff getStaff(BigDecimal staffid) {
        return staffMapper.selectById(staffid);
    }

	@Override
	public List<TblStaff> findStaffByOrgid(TblStaff queryParam) {
		// TODO Auto-generated method stub
		
		return staffMapper.findStaffByOrgid(queryParam);
	}

	@Override
	public String selectNamesByids(String ids) throws Exception {
		// TODO Auto-generated method stub
		String names="";
		try {
			if(StringUtils.isNotBlank(ids)){
				List<TblStaff> list=staffMapper.getOrgNameByIds(ids.split(","));
				names = list.stream()
                        .map(TblStaff::getRealname)  // 提取name字段
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
				 TblStaff  staff=staffMapper.selectById(id);
				 name=staff.getRealname();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return name;
	}
    
    
}
