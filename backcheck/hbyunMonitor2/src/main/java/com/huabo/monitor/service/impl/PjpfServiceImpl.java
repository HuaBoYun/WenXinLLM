package com.huabo.monitor.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.monitor.entity.*;
import com.huabo.monitor.mapper.*;
import com.huabo.monitor.service.PjpfService;
import com.huabo.monitor.util.ConstClass;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.util.IPageResult;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author meng
 * @date 2022-08-26 9:52
 * @description
 */
@Service
@Transactional
public class PjpfServiceImpl extends ServiceImpl<TblAssessMapper, TblAssess> implements PjpfService {

    @Autowired
    TblAssessMapper tblAssessMapper;

    @Autowired
    TblAssessStaffMapper tblAssessStaffMapper;

    @Autowired
    TblAssesscategoryMapper assesscategoryMapper;
    @Autowired
    TblAssesselementMapper  assesselementMapper;
    @Autowired
    TblAssessMarkVoMapper   markVoMapper;

    @Autowired
    YhrPageMapper   yhrPageMapper;


    @Override
    public IPage<TblAssessVo> findPageBean(IPage<TblAssessVo> iPage,
                                           BigDecimal staffid,
                                           String projkey,
                                           String projname,
                                           String startDate,
                                           String endDate) {

        return tblAssessMapper.findPageBean(iPage,staffid,projkey,projname, startDate,endDate,TblAssess.START);
    }

    @Override
    public IPage getTblAssessGroupOrg(IPage iPage, BigDecimal selectedPlans, BigDecimal userStaffid) {
        return tblAssessStaffMapper.getTblAssessGroupOrg(iPage, selectedPlans, userStaffid);
    }

    @Override
    public String GetTree(BigDecimal tmplId,String url,String userid) {
        List<Tree> list = new ArrayList<Tree>();
        //List<TblAssesscategory> root = this.tblAssesscategoryDAO.getTreeRoot(tmplId);
        String sql="select * from  TBL_ASSESSCATEGORY where ASSCATID in (  SELECT   DISTINCT AC.ASSCATID FROM tbl_AssessElement ae LEFT JOIN TBL_ASSELE_CATEGORY ac ON ae.ASSELEID = ac.ASSELEID";
        sql+= " LEFT JOIN tbl_Assess_Mark am ON ae.ASSELEID = am.ASSELEID ";
        sql+= " LEFT JOIN TBL_ASSESS_STAFF asf ON asf.assmarkid = am.assmarkid ";
        sql+= " where asf.staffid="+userid+" ) AND ASSTEMID= "+tmplId;
        List<TblAssesscategory> root = assesscategoryMapper.listBySql(sql);
        for (TblAssesscategory tblAssesscategory : root) {
            Tree tree = new Tree();
            tree.setName(tblAssesscategory.getCatname());
            tree.setId(tblAssesscategory.getAsscatid());
            tree.setTarget("mainFramex");
            //	tree.setUrl(url+"&tmplId="+tmplId+"&nodeId="+tblAssesscategory.getAsscatid());
            List<Tree> childre = this.addChildren(tmplId, tblAssesscategory.getAsscatid(),url);
            if(childre.size()>0){
                tree.setChildren(childre);
            }
            list.add(tree);
        }
        return JSONObject.toJSONString(list);
    }


    public List<Tree> addChildren(Serializable tmplId, BigDecimal pId, String url){
        List<Tree> list = new ArrayList<Tree>();
        QueryWrapper<TblAssesscategory> qw=new  QueryWrapper<>();
        qw.eq("fatherasscatid",pId);
        List<TblAssesscategory> root = this.assesscategoryMapper.selectList(qw);
        for (TblAssesscategory tblAssesscategory : root) {
            Tree tree = new Tree();
            tree.setName(tblAssesscategory.getCatname());
            tree.setId(tblAssesscategory.getAsscatid());
            tree.setTarget("mainFramex");
            //tree.setIsParent(true);
            //tree.setUrl(url+"&tmplId="+tmplId+"&nodeId="+tblAssesscategory.getAsscatid());
            List<Tree> trees = 	addChildren(tmplId, tblAssesscategory.getAsscatid(),url);
            if(trees.size()>0){
                tree.setChildren(trees);
            }
            list.add(tree);

        }
        return list;
    }


    @Override
    public List<ElemGrade>  findElementByassIdAndUserId(BigDecimal nodeId, BigDecimal assId, BigDecimal userId, BigDecimal orgId) {
        StringBuffer sql=new StringBuffer("select DISTINCT ae.ELEMENTNAME,ac.STANDARDSCORE,asf.score,asf.reason,asf.assstaffid,ATT.ATTID,"
        		+ "ATT.ATTNAME,ATT.uploader,ATT.attsize,ae.AUDITPOINT examination,ae.asseleid,ae.elementNumber  "
        		+ "from tbl_AssessElement  ae left join TBL_ASSELE_CATEGORY ac on ae.ASSELEID = ac.ASSELEID  "
        		+ " left join tbl_Assess_Mark  am on ae.ASSELEID = am.ASSELEID   left join TBL_ASSESS_STAFF asf "
        		+ "on asf.assmarkid = am.assmarkid  LEFT JOIN TBL_ATTACHMENT att on ASF.ATTID=ATT.ATTID"
        		+ "  where  am.assid = '"+assId+"' and am.assorgid ="+orgId);
        		if(nodeId!=null){
        			sql.append("and ac.ASSCATID = "+nodeId);
        		}
        		sql.append(" and asf.staffid= "+userId+" and am.suitable = 1 and am.ASSELEID = ae.ASSELEID order by asf.assstaffid  desc");
    	List<ElemGrade> list = this.assesselementMapper.getQuery(sql.toString());
        return list;
    }

    @Override
    public List<TblAssessStaff> checkSubmit(BigDecimal assId, BigDecimal orgId, BigDecimal staffid) {
        return tblAssessStaffMapper.checkSubmit(assId,orgId,staffid);
    }

    @Override
    public String doSubmint(BigDecimal assId, BigDecimal orgId, BigDecimal staffid) {
        List<TblAssessStaff> list=tblAssessStaffMapper.checkdoSubmint(assId,orgId,staffid);

        if(list!=null){
            for (TblAssessStaff tblAssessStaff : list) {
                tblAssessStaff.setStatus(new BigDecimal(2));
                tblAssessStaffMapper.updateById(tblAssessStaff);
            }
        }
        List<TblAssessStaff> assessStaffs2 = this.tblAssessStaffMapper.checkSubmitAll(assId);
        if (assessStaffs2.size() == 0) {
            TblAssess assess = this.tblAssessMapper.selectById(assId);
            this.markVoMapper.updateStateByAssid("3",assId);
            assess.setAssstatus(TblAssess.END);
            assess.setAssessdate(new Date());//LocalDateTime.now()
            this.tblAssessMapper.updateById(assess);
        }
        return JsonBean.success();

    }

    @Override
    public List<TblAssessStaff> checkSubmitAll(BigDecimal assId, BigDecimal orgId) {
        return tblAssessStaffMapper.checkSubmitAll(assId);
    }

    @Override
    public List<Object[]> pjpfExport(BigDecimal nodeId,BigDecimal assId,BigDecimal userId,BigDecimal orgId) {
        String sql = "select ae.elementNumber,ae.ELEMENTNAME,ac.STANDARDSCORE,ae.AUDITPOINT,asf.score,asf.reason,ATT.ATTNAME from tbl_AssessElement  ae left join TBL_ASSELE_CATEGORY ac on ae.ASSELEID = ac.ASSELEID   left join tbl_Assess_Mark  am on ae.ASSELEID = am.ASSELEID   left join TBL_ASSESS_STAFF asf on asf.assmarkid = am.assmarkid" +
                " LEFT JOIN TBL_ATTACHMENT att on ASF.ATTID=ATT.ATTID  where ac.ASSCATID = "+nodeId+" and am.assid = "+assId+" and am.assorgid ="+orgId+" and asf.staffid= "+userId+" and am.suitable = 1 and am.ASSELEID = ae.ASSELEID  order by ae.ELEMENTNAME";

        List<LinkedHashMap<String, Object>> mapList = this.yhrPageMapper.queryBySqlToListLinkMap(sql);
        List<Object[]> obj = new ArrayList<Object[]>();
        for (Map<String, Object> map : mapList) {
            System.out.println(map.values());
            Collection values = map.values();
            List list = new ArrayList(values);
            obj.add(list.toArray());
        }
        return obj;


    }

	@Override
	public PageInfo<TblAssessVo> findPageBeanNew(Integer pageNumber,BigDecimal staffid, String projkey, String projname, String startDate,
			String endDate,TblStaffUtil staff) throws Exception{
		// TODO Auto-generated method stub
		
		TblAssessVo vo=new TblAssessVo();
		vo.setAssstatus(TblAssess.START);
		vo.setStaffid(staffid);
		vo.setAssessid(projkey);
		vo.setAssessname(projname);
		if(StringUtils.isNotBlank(startDate)){
			vo.setStartdate(new SimpleDateFormat("yyyy-MM-dd").parse(startDate));
		}
	    if(StringUtils.isNotBlank(endDate)){
		   vo.setEnddate(new SimpleDateFormat("yyyy-MM-dd").parse(endDate));
		}
	   // String sql=TblAssessMapperSqlConfig.setSqlNew("t.", staff,0);
		PageInfo<TblAssessVo> pageInfo=PageMethod.startPage(pageNumber,ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->tblAssessMapper.findPageBeanNew(vo,null));
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
		
		IPageResult<TblAssessVo> iPage=new IPageResult<TblAssessVo>().buildIpage(pageInfo);
	    return pageInfo;
	}

}
