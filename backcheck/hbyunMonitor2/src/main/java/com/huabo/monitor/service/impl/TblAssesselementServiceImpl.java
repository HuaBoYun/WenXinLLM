package com.huabo.monitor.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.monitor.entity.TblAssesselement;
import com.huabo.monitor.mapper.OpenQueryMapperSqlConfig;
import com.huabo.monitor.mapper.TblAssesselementMapper;
import com.huabo.monitor.service.ITblAssesselementService;
import com.huabo.monitor.util.ConstClass;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
@Service
public class TblAssesselementServiceImpl extends ServiceImpl<TblAssesselementMapper, TblAssesselement> implements ITblAssesselementService {
	
	@Resource
    TblAssesselementMapper tblAssesselementMapper;
	
	@Override
    public IPage<TblAssesselement> findByPageBean(String orgid, Integer pageNumber, TblAssesselement assesselement,Integer pagesize) {
        IPage<TblAssesselement> page = new Page<>(pageNumber, pagesize);

        List<Object> listparam = new ArrayList<>();
        String hql = "select * from TBL_ASSESSELEMENT t where 1=1  and t.tblComany = "+orgid;
        if (StringUtils.isNotBlank(assesselement.getElementname())) {
            hql += " and t.elementname LIKE '%" + assesselement.getElementname() + "%'";
        }
        if (StringUtils.isNotBlank(assesselement.getBusinessattribute())) {
            hql += " and t.businessattribute =  "+assesselement.getBusinessattribute();
        }
        if (StringUtils.isNotBlank(assesselement.getBusinesstype())) {
            hql += " and t.businesstype LIKE '%" + assesselement.getBusinesstype() + "%'";
        }
        if (StringUtils.isNotBlank(assesselement.getElementnumber())) {
            hql += " and t.elementNumber LIKE '%" + assesselement.getElementnumber() + "%'";
        }
        if (StringUtils.isNotBlank(assesselement.getAuditpoint())) {
            hql += " and t.auditpoint LIKE '%" + assesselement.getAuditpoint() + "%'";
        }
        hql += " order by t.asseleid desc";

        return this.tblAssesselementMapper.getSqlPage(page, hql);
    }
	
	@Override
    public TblAssesselement getNumber(String number, String orgid) {
        List<TblAssesselement> list = this.tblAssesselementMapper.getNumber(number, orgid);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
	
	@Override
    public void add(TblAssesselement tblAssesselement) {
        tblAssesselementMapper.insert(tblAssesselement);
    }
	
	@Override
    public void Update(TblAssesselement tblAssesselement) {
        tblAssesselementMapper.updateById(tblAssesselement);
    }
	
	@Override
    public TblAssesselement findById(BigDecimal id) {
        return tblAssesselementMapper.selectById(id);
    }
	 
	@Override
    public TblAssesselement get(BigDecimal id) {
        return tblAssesselementMapper.selectById(id);
    }
	
	@Override
    public void delete(TblAssesselement tblAssesselement) {
		if(tblAssesselement!=null){
	        tblAssesselementMapper.deleteById(tblAssesselement);
		}
    }
	
	@Override
    public List<TblAssesselement> getAssEssByIn(String assessIds) {
//		 String hql = "select * from TBL_ASSESSELEMENT t  where t.ASSELEID   in ("+assessIds+")";
//        return tblAssesselementMapper.getAssEssByIn(hql);
        String[] list=assessIds.split(",");
        List<TblAssesselement> mentList=new ArrayList<>();
        for(String s:list){
        	mentList.add(tblAssesselementMapper.selectById(new BigDecimal(s)));
        }
       return mentList;
    }

    @Override
    public List<TblAssesselement> getComany(String orgid) {
        return tblAssesselementMapper.getComany(orgid);
    }
    
    @Override
    public IPage<TblAssesselement> findByPageBean(String orgid, Integer pageNumber, TblAssesselement assesselement, String notInStr) {
         IPage<TblAssesselement> page = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);

        List<Object> listparam = new ArrayList<>();
        String hql = "select * from TBL_ASSESSELEMENT t where 1=1  and t.tblComany = "+orgid;
        listparam.add(orgid);
        if (StringUtils.isNotBlank(notInStr)) {
            hql += " and t.asseleid not in (" + notInStr + ")";
        }
        if (StringUtils.isNotBlank(assesselement.getElementname())) {
            hql += " and t.elementname like '%" + assesselement.getElementname() + "%'";
        }
        if (StringUtils.isNotBlank(assesselement.getBusinessattribute())) {
            hql += " and t.businessattribute = " +assesselement.getBusinessattribute();
        }
        if (StringUtils.isNotBlank(assesselement.getBusinesstype())) {
            hql += " and t.businesstype = "+assesselement.getBusinesstype();
        }
        if (StringUtils.isNotBlank(assesselement.getElementnumber())) {
            hql += " and t.elementNumber like '%" + assesselement.getElementnumber() + "%'";
            listparam.add("%" + assesselement.getElementnumber() + "%");
        }
        hql += "order by t.elementNumber , t.elementname desc";
        return this.tblAssesselementMapper.getSqlPage(page, hql);
    	
    }

	@Override
	public PageInfo<TblAssesselement> getPageBean(String orgid, Integer pageNumber, TblAssesselement assesselement,
			Integer pagesize,TblStaffUtil user) {
		// TODO Auto-generated method stub
	//	 QueryWrapper<TblAssesselement> query=new QueryWrapper<>();
         if(Objects.nonNull(orgid)){
        	// query.eq("tblComany", orgid);
             assesselement.setTblcomany(orgid);
         }
//         if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(assesselement.getElementname())){
//        	 query.like("elementname", assesselement.getElementname());
//         }
//         if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(assesselement.getBusinessattribute())){
//        	 query.like("businessattribute", assesselement.getBusinessattribute());
//         }
//         if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(assesselement.getBusinesstype())){
//        	 query.like("businesstype", assesselement.getBusinesstype());
//         }
//         if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(assesselement.getElementnumber())){
//        	 query.like("elementNumber", assesselement.getElementnumber());
//         }
//         if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(assesselement.getAuditpoint())){
//        	 query.like("auditpoint", assesselement.getAuditpoint());
        // }
       // query.orderByDesc("asseleid");
         PageInfo<TblAssesselement> pageinfo=null;
         try {
        	 
        	 String sql = GeneralSQLConcatConfig.concatSecrectSql(user.getCurrentOrg().getUseSecrect(), false, "tblcomany", "LINKDEPTID", "CREATESTAFFID", "SECRECTLEVELID", "STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds());
        	 
             System.out.println(sql);
		  pageinfo=PageMethod.startPage(pageNumber,pagesize).doSelectPageInfo(()-> tblAssesselementMapper.selectPageInfo(assesselement,sql));
		  FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(pageinfo.getList())){
				pageinfo.getList().forEach(entity->{
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
         } catch (Exception e) {
 			// TODO: handle exception
 			e.printStackTrace();
 		}
		  return pageinfo;
	}

	@Override
	public PageInfo<TblAssesselement> findByPageBeanNew(String orgid, Integer pageNumber,
			TblAssesselement assesselement, List<String> notInStr) {

    	QueryWrapper<TblAssesselement> query=new QueryWrapper<>();
    	 if (notInStr!=null&&notInStr.size()>0) {
             query.notIn("asseleid", notInStr);   
         }
         if (StringUtils.isNotBlank(assesselement.getElementname())) {
             query.like("elementname", assesselement.getElementname());
         }
         if (StringUtils.isNotBlank(assesselement.getBusinessattribute())) {
             query.eq("businessattribute", assesselement.getBusinessattribute());
         }
         if (StringUtils.isNotBlank(assesselement.getBusinesstype())) {
             query.eq("businesstype", assesselement.getBusinesstype());
         }
         if (StringUtils.isNotBlank(assesselement.getElementnumber())) {
             query.like("elementNumber", assesselement.getElementnumber());
         }
         query.orderByDesc("elementNumber,elementname");
         
         PageInfo<TblAssesselement> pageInfo=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->tblAssesselementMapper.selectList(query));
	    
         FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(pageInfo.getList())){
				pageInfo.getList().forEach(entity->{
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
         return pageInfo;
	}
}
