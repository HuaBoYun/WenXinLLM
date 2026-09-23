package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.monitor.entity.TblStaff;
import com.huabo.monitor.entity.TblTestTemplate;
import com.huabo.monitor.mapper.TblStaffMapper;
import com.huabo.monitor.mapper.TblTestplanMapper;
import com.huabo.monitor.entity.TblTestplan;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.ITblTestplanService;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-09-07
 */
@Service
@Transactional
public class TblTestplanServiceImpl extends ServiceImpl<TblTestplanMapper, TblTestplan> implements ITblTestplanService{ 

    @Resource
    private TblTestplanMapper tblTestplanMapper;

    @Override
    public void savePlan(TblTestplan tblTestplan) throws Exception{
    	tblTestplan.setTestplanid(RandomUtil.uuBigDecimalId());
    	tblTestplanMapper.insert(tblTestplan);  //insertEntity
    }
    
    @Override
    public void update(TblTestplan tblTestplan) throws Exception{
        tblTestplanMapper.updateById(tblTestplan); //Entity
    }

    @Override
    public void add(TblTestplan tblTestplan) throws Exception{
    	tblTestplan.setTestplanid(RandomUtil.uuBigDecimalId());
        tblTestplanMapper.insert(tblTestplan);
    }


    @Override
    public TblTestplan getById(BigDecimal testplanid) {
    	TblTestplan plan=tblTestplanMapper.findById(testplanid);
		if (plan != null) {
			FiexibleNameAssignment ment = new FiexibleNameAssignment();
			// 对灵活字段中的姓名名称及机构名称赋值
			fieldOrgStaffId item = new fieldOrgStaffId();
			BeanUtils.copyProperties(plan, item);
			fieldOrgStaffName nameEntity = ment.setOpenName(item);
			BeanUtils.copyProperties(nameEntity, plan);
		}
        return tblTestplanMapper.findById(testplanid);
    }

    @Override
    public JsonBean selectList(String plannumber, String planname, String planstatus, Date starttime_min, Date starttime_max, Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public JsonBean updateById(BigDecimal testplanid) {
        if (testplanid==null){
            return ResponseFormat.retParam(0,10002,null);
        }
       // tblTestplanMapper.updateById(testplanid);
        return ResponseFormat.retParam(1,200,null);
    }

    @Override
    public void deleteById(BigDecimal testplanid) {
        tblTestplanMapper.deleteById(testplanid);
    }

    @Override
    public JsonBean saveAll(List<String> plans) {
        for (String plan : plans) {
            if (plan==null){
                return ResponseFormat.retParam(0,10002,null);
            }
           // tblTestplanMapper.insert(plan);
        }
        return ResponseFormat.retParam(1,200,null);
    }

    @Override
    public JsonBean select(String planname, String planyear, Integer pageNumber, Integer pageSize) {
        Page<TblTestTemplate> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNumber);
        LambdaQueryWrapper<TblTestplan> wrapper = Wrappers.<TblTestplan>lambdaQuery();
        if (planname!=null){
            wrapper.eq(TblTestplan::getPlanname,planname);
        }
        if (StringUtils.isNotBlank(planyear)){
            wrapper.eq(TblTestplan::getPlanyear,planyear);
        }
      //  tblTestplanMapper.selectPage(page,wrapper);
        return ResponseFormat.retParam(1,200,null);
    }
   
	
}
