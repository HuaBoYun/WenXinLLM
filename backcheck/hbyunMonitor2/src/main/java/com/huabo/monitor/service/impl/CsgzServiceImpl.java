package com.huabo.monitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.monitor.entity.TblTestplan;
import com.huabo.monitor.mapper.*;
import com.huabo.monitor.service.CsgzService;
import com.huabo.monitor.util.ConstClass;
import com.huabo.monitor.util.DateUtils;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * @author：yhr
 * @date:2022-09-13 11:34
 * @description:
 */
@Service
public class CsgzServiceImpl implements CsgzService {

    @Resource
    TblTestplanMapper testplanMapper;

    @Resource
    YhrPageMapper yhrPageMapper;

    @Resource
    TblTesttaskMapper testtaskMapper;

    @Resource
    TblTesttaskAttMapper testtaskAttMapper;


    @Resource
    TblTestplanMatrixMapper testplanMatrixMapper;

    @Resource
    TblTestelementMapper  testelementMapper;


    @Override
    public IPage<TblTestplan> findAllTrack(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, BigDecimal orgid) {
        IPage<TblTestplan> page=new Page<TblTestplan>(pageNumber, ConstClass.DEFAULT_SIZE);

        String sql="SELECT DISTINCT * from TBL_TESTPLAN te WHERE TE.PLANSTATUS!='已完成' and TE.PLANSTATUS!='未启动' and TE.ORGID in ( select ORGID from TBL_ORGANIZATION where (FATHERORGID="+orgid+" AND ORGTYPE=0 or ORGID="+orgid+"))";

        if (plan!=null) {
            if (plan.getPlanname()!=null) {
                sql+=" and PLANNAME like '%"+plan.getPlanname()+"%' ";

            }
            if (plan.getPlannumber()!=null) {
                sql+=" and PLANNUMBER like '%"+plan.getPlannumber()+"%' ";

            }
            if (plan.getPlanyear()!=null) {
                sql+=" and PLANYEAR like '%"+plan.getPlanyear()+"%'";

            }
            if (plan.getPlanstatus()!=null) {
                sql+=" and PLANSTATUS like '%"+plan.getPlanstatus()+"%'";

            }
        }

        if (StringUtils.isNotBlank(starttime_min)) {
            sql+=" and STARTTIME >=to_date('"+starttime_min+"','yyyy-MM-dd') ";

        }
        if (StringUtils.isNotBlank(starttime_max)) {
            sql+=" and ENDTIME <=to_date('"+starttime_max+"','yyyy-MM-dd') ";

        }

        sql+=" ORDER BY TESTPLANID desc";

        return  this.testplanMapper.getSqlPage(page,sql);
    }

    @Override
    public IPage<TblTestplan> findAllnoSjTrack(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, String userid) {
        IPage<TblTestplan> page=new Page<TblTestplan>(pageNumber, ConstClass.DEFAULT_SIZE);

        String sql=" SELECT * FROM TBL_TESTPLAN pl  WHERE PL.PLANSTATUS!='已完成' and PL.PLANSTATUS!='未启动' and (PL.CREATID ="+userid+" or PL.STAFFID= "+userid+") ";

        if (plan!=null) {
            if (plan.getPlanname()!=null) {
                sql+=" and PLANNAME like '%"+plan.getPlanname()+"%' ";

            }
            if (plan.getPlannumber()!=null) {
                sql+=" and PLANNUMBER like '%"+plan.getPlannumber()+"%' ";

            }
            if (plan.getPlanyear()!=null) {
                sql+=" and PLANYEAR like '%"+plan.getPlanyear()+"%'";

            }
            if (plan.getPlanstatus()!=null) {
                sql+=" and PLANSTATUS like '%"+plan.getPlanstatus()+"%'";

            }
        }
        if (StringUtils.isNotBlank(starttime_min)) {
            sql+=" and STARTTIME >=to_date('"+starttime_min+"','yyyy-MM-dd') ";

        }
        if (StringUtils.isNotBlank(starttime_max)) {
            sql+=" and ENDTIME <=to_date('"+starttime_max+"','yyyy-MM-dd') ";

        }

        sql+=" ORDER BY TESTPLANID desc";
        return   this.testplanMapper.getSqlPage(page,sql);
    }

    @Override
    public IPage<Map<String, Object>> findAllnoSjTrack2(BigDecimal selectProjectid, Integer pageNumber) {
        IPage<Map<String, Object>> page=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
        String sql="select  AA.PLANNUMBER, aa.PLANNAME, AA.REALNAME,CASE WHEN AA.ZS=AA.wc then cast('已完成' as varchar(12)) else cast('未完成' as varchar(12))  END as zt,"
                + " CASE WHEN AA.ZS = AA.tj THEN cast('是' as varchar(12)) ELSE cast('否' as varchar(12)) END AS sftj  from ( SELECT COUNT (*) zs, PLANID, CPUSERID, STA.REALNAME, PL.PLANNAME, PL.PLANNUMBER," +
                " (select count(*) from TBL_TESTTASK t1 WHERE   T1.CPUSERID=TASK.CPUSERID and TASK.PLANID=T1.PLANID and T1.TESTSTATUS='已完成') as wc ,"
                + " ( SELECT COUNT (*) FROM TBL_TESTTASK t1 WHERE T1.CPUSERID = TASK.CPUSERID AND TASK.PLANID = T1.PLANID AND T1.COMPLETESTAUS = 1 ) AS tj " +
                " FROM TBL_TESTTASK task LEFT JOIN TBL_STAFF sta on TASK.CPUSERID=STA.STAFFID  " +
                " LEFT JOIN TBL_TESTPLAN pl on TASK.PLANID=PL.TESTPLANID WHERE PLANID ="+selectProjectid+
                " GROUP BY TASK.CPUSERID, PLANID,STA.REALNAME,PL.PLANNAME, PL.PLANNUMBER ) aa ";
        return this.yhrPageMapper.getPage(page,sql);
    }

	@Override
	public PageInfo<TblTestplan> findAllTrackNew(TblTestplan plan, Integer pageNumber, String starttime_min,
			String starttime_max, BigDecimal orgid,TblStaffUtil user) throws Exception{
		    // TODO Auto-generated method stub
	    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotBlank(starttime_min)){
				 plan.setStarttime(sdf.parse(starttime_min));
			}
			if(StringUtils.isNotBlank(starttime_max)){
				 plan.setEndtime(sdf.parse(starttime_max));
			}
	        if(Objects.nonNull(orgid)){
	        	  plan.setOrgid(orgid);
	        }
	        String sql = GeneralSQLConcatConfig.concatSecrectSql(user.getCurrentOrg().getUseSecrect(), false, "te.LINKORGID", "te.LINKDEPTID", "te.CREATID", "te.SECRECTLEVELID", "te.STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds());
	       PageInfo<TblTestplan> iPage=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->testplanMapper.findAllTrackNew(plan,sql));
	       FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(iPage.getList())){
				iPage.getList().forEach(entity->{
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
	       
	       return iPage;
	}

	@Override
	public PageInfo<TblTestplan> findAllnoSjTrackNew(TblTestplan plan, Integer pageNumber, String starttime_min,
			String starttime_max, String toString,TblStaffUtil user)throws Exception {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(StringUtils.isNotBlank(starttime_min)){
			 plan.setStarttime(sdf.parse(starttime_min));
		}
		if(StringUtils.isNotBlank(starttime_max)){
			 plan.setEndtime(sdf.parse(starttime_max));
		}
	        if(Objects.nonNull(toString)){
	        	  plan.setCreatid(new BigDecimal(toString));
	        }
	  	    StringBuffer querySql=new StringBuffer();
		   if(plan.getCreatid()!=null){
		    	querySql.append(" or (PL.CREATID =").append(plan.getCreatid()).append(" or PL.STAFFID=").append(plan.getCreatid()).append(")");
		    }
	        String sql = GeneralSQLConcatConfig.concatSecrectSqlCase2(user.getCurrentOrg().getUseSecrect(), true, "pl.LINKORGID", "pl.LINKDEPTID", "pl.CREATID", "pl.SECRECTLEVELID", "pl.STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds(),querySql.toString());
	       PageInfo<TblTestplan> iPage=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->testplanMapper.findAllnoSjTrackNew(plan,sql));
	       FiexibleNameAssignment ment=new FiexibleNameAssignment();
				if(CollectionUtils.isNotEmpty(iPage.getList())){
					iPage.getList().forEach(entity->{
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
           return iPage;
	}

	@Override
	public PageInfo<Map<String, Object>> findAllnoSjTrack2New(BigDecimal selectProjectid, Integer pageNumber) {
		// TODO Auto-generated method stub
	        PageInfo<Map<String, Object>> iPage=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->testtaskMapper.findAllnoSjTrack2New(selectProjectid));
			   return iPage;
	}
}
