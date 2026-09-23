package com.huabo.monitor.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.monitor.entity.TblAssess;
import com.huabo.monitor.entity.TblAssesslevel;
import com.huabo.monitor.entity.TblTestTemplate;
import com.huabo.monitor.mapper.OpenQueryMapperSqlConfig;
import com.huabo.monitor.mapper.TblAssesslevelMapper;
import com.huabo.monitor.service.TblAssesslevelService;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.util.PageResult;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

@Service("TblAssesslevelService")
public class TblAssesslevelServiceImpl implements TblAssesslevelService {

    @Resource
    private TblAssesslevelMapper tblAssesslevelMapper;

    @Override
    public JsonBean findByPageBean(Integer pageNumber, Integer pageSize,String orgid,TblStaffUtil user ) throws Exception{
        if (pageNumber == null || pageNumber <= 0){
            pageNumber = 1;
        }
        if (pageSize <= 0 || pageSize >15){
            pageSize = 15;
        }
//        Page<TblAssesslevel> page = new Page<>();
//        page.setSize(pageSize);
//        page.setCurrent(pageNumber);
//        LambdaQueryWrapper<TblAssesslevel> wrapper = Wrappers.<TblAssesslevel>lambdaQuery();
//        if (StringUtils.isNotBlank(orgid)){
//            wrapper.eq(TblAssesslevel::getTblcomany, orgid);
//        }
//        Page<TblAssesslevel> tblAssesslevelPage = tblAssesslevelMapper.selectPage(page, wrapper);
//
//        
        
//        PageInfo<TblAssesslevel> pageInfo = new PageInfo<TblAssesslevel>();
//        TblAssesslevel temp = new TblAssesslevel();
//    	temp.setTblcomany(orgid);
//    	pageInfo.setCondition(temp);
//    	pageInfo.setPageSize(pageSize);
//    	pageInfo.setCurrentPage(pageNumber);
    	//pageInfo.setTlist(tblAssesslevelMapper.selectPageInfo(pageInfo));
//    	pageInfo.setTotalRecord(tblAssesslevelMapper.selectPageCount(pageInfo));
//        return ResponseFormat.retParam(1, 200, pageInfo);
//		QueryWrapper<TblAssesslevel> wrapper = new QueryWrapper<>();
//	    if(StringUtils.isNotBlank(orgid)){
//		   wrapper.eq("tblComany",orgid);
//	     }
//	     wrapper.orderByDesc(true, "asslevid");
		 String sql = GeneralSQLConcatConfig.concatSecrectSql(user.getCurrentOrg().getUseSecrect(), false, "tblcomany", "LINKDEPTID", "CREATESTAFFID", "SECRECTLEVELID", "STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds());
	     com.github.pagehelper.PageInfo<TblAssesslevel> page=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->tblAssesslevelMapper.selectPageInfo(sql));
	     FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(page.getList())){
				page.getList().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
			}
	     
	     
	     PageResult<TblAssesslevel> build = new PageResult<TblAssesslevel>().build(page);
	    return ResponseFormat.retParam(1, 200, build);
    } 

    @Override
    public JsonBean findById(BigDecimal id) {
        TblAssesslevel tblAssesslevel = tblAssesslevelMapper.selectById(id);
        if(tblAssesslevel!=null){
        	  FiexibleNameAssignment ment=new FiexibleNameAssignment();
				//对灵活字段中的姓名名称及机构名称赋值
				fieldOrgStaffId item=new fieldOrgStaffId();
				BeanUtils.copyProperties(tblAssesslevel,item); 
				fieldOrgStaffName nameEntity=ment.setOpenName(item);
				BeanUtils.copyProperties(nameEntity,tblAssesslevel ); 
        }
      
        if (null == tblAssesslevel){
            return ResponseFormat.retParam(0,50001,"未找到该数据");
        }
        return ResponseFormat.retParam(1, 200, tblAssesslevel);
    }

    @Override
    public JsonBean add(TblAssesslevel tblAssesslevel) {
        tblAssesslevelMapper.insert(tblAssesslevel);
        return ResponseFormat.retParam(1, 200, "success");
    }

    @Override
    public JsonBean update(TblAssesslevel tblAssesslevel) {
        if (null == tblAssesslevel){
            return ResponseFormat.retParam(0, 10002, "参数为空，请检查后重试");
        }
        BigDecimal asslevid = tblAssesslevel.getAsslevid();
        TblAssesslevel dbAssesslevel = tblAssesslevelMapper.selectById(asslevid);
        if (null == dbAssesslevel){
            return ResponseFormat.retParam(0, 50001, "跟新数据 不存在，请检查后重试");
        }
        tblAssesslevelMapper.updateById(tblAssesslevel);
        return ResponseFormat.retParam(1, 200, "success");
    }

    @Override
    public JsonBean delete(BigDecimal asslevid) {
        if (null == asslevid){
            return ResponseFormat.retParam(0, 10002, "缺少参数，请检查重试");
        }
        TblAssesslevel dbAssesslevel = tblAssesslevelMapper.selectById(asslevid);
        if (null == dbAssesslevel){
            return ResponseFormat.retParam(0, 50001, "要删除的数据不存在");
        }
        tblAssesslevelMapper.deleteById(asslevid);
        return ResponseFormat.retParam(1, 200, "success");
    }

    @Override
    public List<TblAssesslevel> findAll(String tblCompany) {
        return null;
    }
}
