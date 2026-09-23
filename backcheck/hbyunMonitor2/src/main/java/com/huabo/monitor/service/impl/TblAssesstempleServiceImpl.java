package com.huabo.monitor.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.monitor.entity.TblAssesstemple;
import com.huabo.monitor.mapper.OpenQueryMapperSqlConfig;
import com.huabo.monitor.mapper.TblAssesscategoryMapper;
import com.huabo.monitor.mapper.TblAssesstempleMapper;
import com.huabo.monitor.service.ITblAssesstempleService;
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
public class TblAssesstempleServiceImpl extends ServiceImpl<TblAssesstempleMapper, TblAssesstemple> implements ITblAssesstempleService {
	
	@Resource
    TblAssesstempleMapper tblAssesstempleMapper;
    @Resource
    TblAssesscategoryMapper tblAssesscategoryMapper;

	
	@Override
    public IPage<TblAssesstemple> findAll(BigDecimal orgid, Integer pageNumber, TblAssesstemple assesstemple) {
        IPage<TblAssesstemple> page = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
        String hql = "SELECT t.*,s.realname as realname,(select listagg(orgname,',') within group(order by orgname) as orgname from tbl_organization where orgid in (select orgid from Tbl_Temple_Organization where ASSTEMID=t.asstemid)) as reorgText,(select listagg(orgid,',') within group(order by orgid) as orgid from tbl_organization where orgid in (select orgid from Tbl_Temple_Organization where ASSTEMID=t.asstemid)) as reorg,(select count(0) from TBL_ASSESS WHERE asstemid=t.ASSTEMID) as numbers FROM Tbl_assesstemple t left join tbl_staff s on s.staffid=t.staffid where t.orgid="+orgid;
        if (StringUtils.isNotBlank(assesstemple.getTemplename())) {
            hql += " and t.templename like '%" + assesstemple.getTemplename() + "%'";
        }
        if (StringUtils.isNotBlank(assesstemple.getTemplenumber())) {
            hql += " and t.templeNumber like '%" + assesstemple.getTemplenumber() + "%'";
        }
        hql += " order by t.asstemid desc";
        return tblAssesstempleMapper.getSqlPage(page, hql);
    }
	
	@Override
    public List<TblAssesstemple> getTmplByNumber(String number, BigDecimal orgid) {
        return tblAssesstempleMapper.getTmplByNumber(number, orgid);
    }
	
	@Override
    public Serializable add(TblAssesstemple tblAssesstemple) {
        int insert = tblAssesstempleMapper.insert(tblAssesstemple);
        return insert;
    }
	
	@Override
    public void modify(TblAssesstemple tblAssesstemple) {
        tblAssesstempleMapper.updateById(tblAssesstemple);
    }
	
	@Override
    public TblAssesstemple findByid(BigDecimal id) {
		TblAssesstemple ass=tblAssesstempleMapper.selectById(id);
		if(ass!=null){
		 String reorg=listAggregate(tblAssesstempleMapper.getOrgId(id));
		 String reorgtext=listAggregate(tblAssesstempleMapper.getOrgname(id));
		 ass.setReorg(reorg);
		 ass.setReorgText(reorgtext);
		}
      return ass;
      //return tblAssesstempleMapper.findById(id);
    }
	
	 public static String listAggregate(List<String> input) {
	        StringBuilder result = new StringBuilder();
	        for (int i=0; i < input.size()-1; i++) {
	            result.append(input.get(i)).append(","); // 在每个元素之间添加分隔符（这里使用逗号）
	        }
	        if (!input.isEmpty()) {
	            result.append(input.get(input.size() - 1)); // 最后一个元素不需要添加分隔符
	        }
	        return result.toString();
	    }
	
	@Override
    public void delete(TblAssesstemple tblAssesstemple) {
        tblAssesstempleMapper.deleteTempleOrg(tblAssesstemple.getAsstemid());
        tblAssesscategoryMapper.deleteByTempleId(tblAssesstemple.getAsstemid());
        tblAssesstempleMapper.deleteById(tblAssesstemple);
    }

	@Override
	public void saveTempleOrg(BigDecimal orgid, BigDecimal tempid) {
		// TODO Auto-generated method stub
		tblAssesstempleMapper.insertTempOrg(orgid,tempid);
	}

	@Override
	public void removeTempleOrg(BigDecimal tmplId) {
		// TODO Auto-generated method stub
         tblAssesstempleMapper.deleteTempOrg(tmplId);
	}

	@Override
	public void insertTemples(TblAssesstemple tblAssesstemple)throws Exception{
		// TODO Auto-generated method stub
		//tblAssesstempleMapper.insertTemples(tblAssesstemple);
		tblAssesstemple.setAsstemid(RandomUtil.uuBigDecimalId());
		tblAssesstempleMapper.insert(tblAssesstemple);
		
	}

	@Override
	public void updateTemples(TblAssesstemple tblAssesstemple)throws Exception {
		// TODO Auto-generated method stub
		tblAssesstempleMapper.updateById(tblAssesstemple);

	}

	@Override
	public PageInfo<TblAssesstemple> findAllNewPage(String orgid, Integer pageNumber,
			TblAssesstemple assesstemple, Integer pageSize,TblStaffUtil user) throws Exception {
//		 QueryWrapper<TblAssesstemple> query=new QueryWrapper<>();
//		   if(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(orgid)){
//			   query.eq("orgid", orgid);
//		   }
//		   if (StringUtils.isNotBlank(assesstemple.getTemplename())) {
//	           query.like("templename", assesstemple.getTemplename());
//	       }
//	       if (StringUtils.isNotBlank(assesstemple.getTemplenumber())) {
//	           query.like("templeNumber", assesstemple.getTemplenumber());
//	       }
//		   query.orderByDesc("asstemid");
		assesstemple.setOrgid(new BigDecimal(orgid));
		   // List<TblAssesstemple> liet1=tblAssesstempleMapper.selectList(query);
		String sql = GeneralSQLConcatConfig.concatSecrectSql(user.getCurrentOrg().getUseSecrect(), false, "orgid", "LINKDEPTID", "STAFFID", "SECRECTLEVELID", "STAFFSCOPEIDS", user.getStaffid(), user.getDeptIds(), user.getSecrectScopeIds());
   	 
		   PageInfo<TblAssesstemple> info=PageMethod.startPage(pageNumber,pageSize).doSelectPageInfo(()->tblAssesstempleMapper.selectPageList(assesstemple,sql));
//		   for (TblAssesstemple tblAssesstemple : info.getList()) {
//				 String reorg=listAggregate(tblAssesstempleMapper.getOrgId(tblAssesstemple.getAsstemid()));
//				 String reorgtext=listAggregate(tblAssesstempleMapper.getOrgname(tblAssesstemple.getAsstemid()));
//				 Integer number=tblAssesstempleMapper.getNumber(tblAssesstemple.getAsstemid());
//				 tblAssesstemple.setReorg(reorg);
//				 tblAssesstemple.setReorgText(reorgtext);
//				 tblAssesstemple.setNumbers(number);
//			}
		   FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(info.getList())){
				info.getList().forEach(entity->{
					try {
						 String reorg=listAggregate(tblAssesstempleMapper.getOrgId(entity.getAsstemid()));
						 String reorgtext=listAggregate(tblAssesstempleMapper.getOrgname(entity.getAsstemid()));
						 Integer number=tblAssesstempleMapper.getNumber(entity.getAsstemid());
						 entity.setReorg(reorg);
						 entity.setReorgText(reorgtext);
						 entity.setNumbers(number);
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
		   
			return info;
	}
}
