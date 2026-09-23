package com.huabo.compliance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.compliance.entity.*;
import com.huabo.compliance.mapper.*;
import com.huabo.compliance.service.PjjgService;
import com.huabo.compliance.util.ConstClass;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;


@Service
@Transactional
public class PjjgServiceImpl implements PjjgService {

    @Resource
    TblAssessTargetMapper  assessTargetMapper;

    @Resource
    private TblAssessMapper tblAssessMapper;
    @Resource
    OrganizationServiceMapper organizationServiceMapper;
    @Resource
    YhrPageMapper yhrPageMapper;

    @Resource
    TblAssEleCategoryMapper tblAssEleCategoryMapper;

    @Resource
    TblAssessTargetMapper tblAssessTargetMapper;

    @Resource
    TblAssessMarkMapper tblAssessMarkMapper;
    @Resource
    TblAssessMarkVoMapper markVoMapper;
    @Resource
    TblAssessStaffMapper assessStaffMapper;
    @Resource
    TblAssesstempleMapper tblAssesstempleMapper;

    @Resource
    TblAssesslevelMapper tblAssesslevelMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public IPage<TblAssessTargetVo> getOrgList(BigDecimal assId, Integer pageNumber) {
        IPage<TblAssessTargetVo> iPage = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);

        return assessTargetMapper.getOrgList(iPage,assId);
    }

    @Override
    public TblAssessTargetVo getMyOneTargetVo(BigDecimal assesstargetid) {
        return assessTargetMapper.getMyOneTargetVo(assesstargetid);
    }

    @Override
    public IPage<Map<String, Object>> findByPageBean(Integer startIndex, String orgid, String orgtype) {

        String sql=" ";
        String sqlCount=" ";
        if (orgtype!=null && !"".equals(orgtype) && orgtype.equals("0")) {
            sql=" SELECT BUG.BUGID,BUG.BUGNUMBER,o.orgname,'缺陷',BUG.DISCOVERPERSON from TBL_BUG bug " +
                    "LEFT JOIN TBL_ORGANIZATION o on o.orgid=BUG.BUGDEPARTMENT WHERE BUG.NEEDREFORM='是' and BUG.INBUGIDB is NULL and BUG.BUGDEPARTMENT ="+orgid;
            sql+=" UNION ALL ";
            sql+=" SELECT SH.WORKSHEETID,SH.WORKSHEETNUMBER,SH.AUDITEDORG,'底稿',SH.RECORDER from TBL_WORKSHEET sh LEFT JOIN TBL_ORGANIZATION o ON o.orgid = SH.ORGID " +
                    " WHERE SH.RECTIFICATION=1 	and SH.ORGID  = "+orgid;
            sql+="  union ALL ";
            sql+="  SELECT RISK.RISKID,RISKNUMBER,ORG.ORGNAME,'风险发现' ,STA.REALNAME from TBL_NBKZ_RISK risk  LEFT JOIN TBL_STAFF sta on risk.CREATEORID=STA.STAFFID " +
                    "  LEFT JOIN TBL_ORGANIZATION org on RISK.ORGID=ORG.ORGID  WHERE RISK.orgid="+orgid;

            sqlCount=" SELECT count(*) FROM (";
            sqlCount+=" SELECT BUG.BUGID,BUG.BUGNUMBER,o.orgname,'缺陷',BUG.DISCOVERPERSON from TBL_BUG bug " +
                    "LEFT JOIN TBL_ORGANIZATION o on o.orgid=BUG.BUGDEPARTMENT WHERE BUG.NEEDREFORM='是' and BUG.INBUGIDB is NULL and BUG.BUGDEPARTMENT ="+orgid;
            sqlCount+=" UNION ALL ";
            sqlCount+=" SELECT SH.WORKSHEETID,SH.WORKSHEETNUMBER,SH.AUDITEDORG,'底稿',SH.RECORDER from TBL_WORKSHEET sh LEFT JOIN TBL_ORGANIZATION o ON o.orgid = SH.ORGID " +
                    " WHERE SH.RECTIFICATION=1 	and SH.ORGID  = "+orgid;
            sqlCount+="  union ALL ";
            sqlCount+="  SELECT RISK.RISKID,RISKNUMBER,ORG.ORGNAME,'风险发现',STA.REALNAME from TBL_NBKZ_RISK risk  LEFT JOIN TBL_STAFF sta on risk.CREATEORID=STA.STAFFID " +
                    "  LEFT JOIN TBL_ORGANIZATION org on RISK.ORGID=ORG.ORGID  WHERE RISK.orgid="+orgid;
            sqlCount+=")";
            sql = this.convertSql(sql);
            sqlCount = this.convertSql(sqlCount);
        }else{

                sql=" SELECT BUG.BUGID,BUG.BUGNUMBER,o.orgname,'缺陷',BUG.DISCOVERPERSON from TBL_BUG bug " +
                        "LEFT JOIN TBL_ORGANIZATION o on o.orgid=BUG.BUGDEPARTMENT WHERE BUG.NEEDREFORM='是' and BUG.INBUGIDB is NULL and BUG.BUGDEPARTMENT in ( select ORGID from TBL_ORGANIZATION where 1=1 start with  fatherorgid="+orgid+" connect by prior ORGID= fatherorgid) ";
                sql+=" UNION ALL ";
                sql+=" SELECT SH.WORKSHEETID,SH.WORKSHEETNUMBER,SH.AUDITEDORG,'底稿',SH.RECORDER from TBL_WORKSHEET sh LEFT JOIN TBL_ORGANIZATION o ON o.orgid = SH.ORGID " +
                        " WHERE SH.RECTIFICATION=1 	and SH.ORGID    in ( select ORGID from TBL_ORGANIZATION where 1=1 start with  fatherorgid="+orgid+" connect by prior ORGID= fatherorgid) ";
                sql+="  union ALL ";
                sql+="  SELECT RISK.RISKID,RISKNUMBER,ORG.ORGNAME,'风险发现' ,STA.REALNAME from TBL_NBKZ_RISK risk  LEFT JOIN TBL_STAFF sta on risk.CREATEORID=STA.STAFFID " +
                        "  LEFT JOIN TBL_ORGANIZATION org on RISK.ORGID=ORG.ORGID  WHERE RISK.orgid  in ( select ORGID from TBL_ORGANIZATION where 1=1 start with  fatherorgid="+orgid+" connect by prior ORGID= fatherorgid) ";
                sqlCount=" SELECT count(*) FROM (";
                sqlCount+=" SELECT BUG.BUGID,BUG.BUGNUMBER,o.orgname,'缺陷',BUG.DISCOVERPERSON from TBL_BUG bug " +
                        "LEFT JOIN TBL_ORGANIZATION o on o.orgid=BUG.BUGDEPARTMENT WHERE BUG.NEEDREFORM='是' and BUG.INBUGIDB is NULL and BUG.BUGDEPARTMENT in ( select ORGID from TBL_ORGANIZATION where 1=1 start with  fatherorgid="+orgid+" connect by prior ORGID= fatherorgid) ";
                sqlCount+=" UNION ALL ";
                sqlCount+=" SELECT SH.WORKSHEETID,SH.WORKSHEETNUMBER,SH.AUDITEDORG,'底稿',SH.RECORDER from TBL_WORKSHEET sh LEFT JOIN TBL_ORGANIZATION o ON o.orgid = SH.ORGID " +
                        " WHERE SH.RECTIFICATION=1 	and SH.ORGID  in ( select ORGID from TBL_ORGANIZATION where 1=1 start with  fatherorgid="+orgid+" connect by prior ORGID= fatherorgid) ";
                sqlCount+="  union ALL ";
                sqlCount+="  SELECT RISK.RISKID,RISKNUMBER,ORG.ORGNAME,'风险发现' ,STA.REALNAME from TBL_NBKZ_RISK risk  LEFT JOIN TBL_STAFF sta on risk.CREATEORID=STA.STAFFID " +
                        "  LEFT JOIN TBL_ORGANIZATION org on RISK.ORGID=ORG.ORGID  WHERE RISK.orgid  in ( select ORGID from TBL_ORGANIZATION where 1=1 start with  fatherorgid="+orgid+" connect by prior ORGID= fatherorgid) ";
                sqlCount+=")";
                sql = this.convertSql(sql);
                sqlCount = this.convertSql(sqlCount);

        }
        IPage<Map<String, Object>> page = new Page<>(startIndex, ConstClass.DEFAULT_SIZE);

        long start = (page.getCurrent() - 1) * page.getSize();
        long end = start + page.getSize();

        page.setTotal(this.yhrPageMapper.queryCount(sqlCount));
        page.setRecords(this.yhrPageMapper.queryList(start, end, sql));
        return page;
    }

    @Override
    public IPage<TblAssessTargetVo> findMarkByOrgGroupZuPing(BigDecimal assId, BigDecimal staffid,Integer pageNumber) {
        IPage<TblAssessTargetVo> iPage = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
        return assessTargetMapper.findMarkByOrgGroupZuPing(iPage,assId,staffid);
    }

    @Override
    public List<TblAssessMarkVo> getAssessMarkByAssIdAndOrgId(BigDecimal assId, BigDecimal orgId) {
        return this.markVoMapper.getAssessMarkByAssIdAndOrgId(assId,orgId);
    }

    @Override
    public Double getSumScoreByAssidAndOrgId(BigDecimal assid, BigDecimal orgid) {
        return this.markVoMapper.getSumScoreByAssidAndOrgId(assid,orgid);
    }

    @Override
    public JsonBean calculate(BigDecimal assId, BigDecimal orgid,String token)  throws Exception{
    	 TblStaffUtil staff = userProvider.get();
        TblAssess assess = this.tblAssessMapper.selectById(assId);
        if (!assess.getAssstatus().equals(TblAssess.JISUAN)) {
            /* 要素得分项-评价分 */
            List<TblAssessMarkVo> assessMarks = this.markVoMapper.getAssessMarkByAssIdAndOrgId(assId,orgid);
            for (TblAssessMarkVo tblAssessMark : assessMarks) {
                Set<TblAssessStaffVo> assessStaffs = tblAssessMark.getCanpingrens();
                double sum = 0;
                for (TblAssessStaffVo tblAssessStaff : assessStaffs) {
                    Double assWeight = tblAssessStaff.getAssweight().doubleValue();
                    Integer score = tblAssessStaff.getScore().intValue();
                    sum += score * assWeight / 100;
                }
                // 四舍五入
                BigDecimal bigDecimal = new BigDecimal(sum).setScale(1, BigDecimal.ROUND_HALF_UP);// 保留1位小数
                // 新实体
                TblAssessMark tm=this.tblAssessMarkMapper.selectById(tblAssessMark.getAssmarkid());

                tm.setScore(bigDecimal);
                // 修改计算状态
                tm.setState(TblAssessMark.SUM);
                this.tblAssessMarkMapper.updateById(tm);
            }


            QueryWrapper<TblAssessMark> qw=new QueryWrapper<>();
            qw.eq("suitable",1).eq("assid",assId).ne("state",4);
            List<TblAssessMark> list = this.tblAssessMarkMapper.selectList(qw);
            if (list.size() == 0) {
                assess.setAssstatus(TblAssess.JISUAN);
                this.tblAssessMapper.updateById(assess);
                // 计算最终得分
                Double sum1 = this.markVoMapper.getSumScoreByAssidAndOrgId(assId,orgid);

                System.out.println("总分为：" + sum1);
                BigDecimal bd = new BigDecimal(sum1).setScale(2, BigDecimal.ROUND_HALF_UP);// 保留两位小数
                sum1 = bd.doubleValue();
                System.out.println("--四舍五入--" + sum1);
                TblOrganization organization = this.organizationServiceMapper.loadTblOrganization(orgid);

                List<TblAssessTarget> assessTargets = this.assessTargetMapper.getTarget(assess.getAssid(),
                        organization.getOrgid());
                if (assessTargets.size() > 0) {
                    TblAssessTarget assessTarget = assessTargets.get(0);
                    assessTarget.setFinalscore(sum1.floatValue());
                    assessTarget.setOrgid(organization.getOrgid());
                    List<TblAssesslevel> assesslevels = this.tblAssesslevelMapper
                            .getRegion(new BigDecimal(sum1).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue(),staff.getCurrentOrg().getOrgid());
                    if (assesslevels.size() > 0) {
                        assessTarget.setFinallevel(assesslevels.get(assesslevels.size() - 1).getLevelname());
                    }else{
                        return new JsonBean(0,"计算失败-请进行等级维护!",null);
                    }
                    this.assessTargetMapper.updateById(assessTarget);
                    return new JsonBean(200,"成功",null);
                } else {
                    return new JsonBean(0,"计算失败",null);

                }
            }
            return new JsonBean(200,"成功",null);
        } else {
            return new JsonBean(200,"已经计算",null);
        }
    }

    @Override
    public JsonBean yaoSuMingXi(BigDecimal assId, BigDecimal orgid) {
        Map<String, Object> mv = new HashMap<>();

        TblAssessVo tblAssessVo = this.tblAssessMapper.queryTblAssessVoByAssidAndOrgid(assId, orgid);
        final List<Map<String, Object>> tixiquanzhong = this.markVoMapper.getPingFenTiXiQuanZhong(assId, orgid);

        List<Map<String, Object>> pingfenyaosu =  this.markVoMapper.getPingFenYaoSu(assId, orgid);

 //       List<TblAssessMarkVo> assessMarks = this.markVoMapper.getAssessMarkByAssIdAndOrgId(assId,orgid);

//        if(assessMarks.size()>0){
//            mv.put("assessMark",assessMarks.get(0));
//        }
//        mv.put("assessMarks",assessMarks);
        mv.put("tblAssess",tblAssessVo);
        mv.put("tixiquanzhong",tixiquanzhong);
        mv.put("pingfenyaosu",pingfenyaosu);


        return new JsonBean(200,"成功",mv);
    }


    public  Map<String, Object> yaoSuMingXi2(BigDecimal assId, BigDecimal orgid) {
        Map<String, Object> mv = new HashMap<>();

        TblAssessVo tblAssessVo = this.tblAssessMapper.queryTblAssessVoByAssidAndOrgid(assId, orgid);
        final List<Map<String, Object>> tixiquanzhong = this.markVoMapper.getPingFenTiXiQuanZhong(assId, orgid);

        List<Map<String, Object>> pingfenyaosu =  this.markVoMapper.getPingFenYaoSu(assId, orgid);

        mv.put("tblAssess",tblAssessVo);
        mv.put("viewList",tixiquanzhong);
        mv.put("yaosu",pingfenyaosu);
        return  mv;
    }

    @Override
    public List<Map<String, Object>> getPingJiaXiangQing(BigDecimal assmarkid, BigDecimal asscatid) {
        return this.markVoMapper.getPingJiaXiangQing(assmarkid,asscatid);
    }

    @Override
    public List<Object[]> ysmxExport(BigDecimal assid) {

        String sql =  "select DISTINCT ae.elementNumber,ae.ELEMENTNAME,ac.STANDARDSCORE,ae.AUDITPOINT,asf.score,s.realname,asf.reason,att.ATTNAME "
                + " from tbl_AssessElement  ae "
                + " left join TBL_ASSELE_CATEGORY ac on ae.ASSELEID = ac.ASSELEID "
                + " left join tbl_Assess_Mark  am on ae.ASSELEID = am.ASSELEID "
                + " left join TBL_ASSESS_STAFF asf on asf.assmarkid = am.assmarkid "
                + " LEFT JOIN TBL_ATTACHMENT att on ASF.ATTID = att.ATTID "
                + " LEFT JOIN TBL_STAFF s ON s.STAFFID = asf.STAFFID "
                + " where am.assid = "
                + assid
                + " and am.suitable = 1 "
                + " order by ae.ELEMENTNAME ";
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

    private String convertSql(String sql) {

            sql=sql.replace("'缺陷'","CAST('缺陷' AS VARCHAR(12)) source");
            sql=sql.replace("'底稿'","CAST('底稿' AS VARCHAR(12)) source");
            sql=sql.replace("'审计发现'","CAST('审计发现' AS VARCHAR(12)) source");
            sql=sql.replace("'问题'","CAST('问题' AS VARCHAR(12)) source");
            sql=sql.replace("'风险发现'","CAST('风险发现' AS VARCHAR(12)) source");
            //sql=sql.replace("SH.AUDITDISCOVERABLE","TO_CHAR(SH.AUDITDISCOVERABLE) AS AUDITDISCOVERABLE");

        return sql;
    }
}
