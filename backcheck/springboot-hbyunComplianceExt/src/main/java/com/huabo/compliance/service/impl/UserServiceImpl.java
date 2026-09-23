package com.huabo.compliance.service.impl;


import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.compliance.config.DateBaseConfig;
import com.huabo.compliance.entity.Find;
import com.huabo.compliance.entity.TblOrganization;
import com.huabo.compliance.entity.TblStaff;
import com.huabo.compliance.mapper.TblManageRightDAO;
import com.huabo.compliance.mapper.TblStaffMapper;
import com.huabo.compliance.mysql.entity.TblOrganizationMySql;
import com.huabo.compliance.mysql.entity.TblStaffMySql;
import com.huabo.compliance.mysql.mapper.TblManageRightMySqlDAO;
import com.huabo.compliance.mysql.mapper.TblStaffMySqlMapper;
import com.huabo.compliance.service.UserService;
import com.huabo.compliance.util.PageBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TblStaffMapper tblStaffMapper;

    @Resource
    private TblManageRightDAO tblManageRightDAO;

    @Resource
    private TblStaffMySqlMapper tblStaffMySqlMapper;

    @Resource
    private TblManageRightMySqlDAO tblManageRightMySqlDAO;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public TblStaff findById(String id) {
        return this.tblStaffMapper.findById(new BigDecimal(id));
    }

    @Override
    public TblStaffMySql findByMySqlId(String id) {
        return this.tblStaffMySqlMapper.findById(new BigDecimal(id));
    }

    @Override
    public void modify(TblStaff username) {
        this.tblStaffMapper.updateUser(username);

    }

    @Override
    public void modifyMySql(TblStaffMySql username) {
        this.tblStaffMySqlMapper.updateUser(username);
    }

    @Override
    public List<TblStaff> findUserByZbsjForQx(PageInfo<TblStaff> pageInfo) throws Exception {
        List<TblStaff> list = new ArrayList();
        List<TblStaff> dan = tblStaffMapper.selectDan();
        List<TblStaff> count = tblStaffMapper.selectCountt();
        list.add((TblStaff) dan);
        list.add((TblStaff) count);
        return list;
    }

    @Override
    public List<TblStaffMySql> findUserByMySqlZbsjForQx(PageInfo<TblStaffMySql> var1) throws Exception {
        List<TblStaffMySql> list = new ArrayList();
        List<TblStaffMySql> dan = tblStaffMySqlMapper.selectDan();
        List<TblStaffMySql> count = tblStaffMySqlMapper.selectCountt();
        list.add((TblStaffMySql) dan);
        list.add((TblStaffMySql) count);
        return list;
    }

    @Override
    public Map<String, Object> findAllPageBeanPid(String staffId, Find find, Integer pageNumber, Integer pageSize, BigDecimal pid) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            PageInfo<TblStaff> pageInfo = new PageInfo<TblStaff>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTlist(tblStaffMapper.selectListByPageInfoOrgid(pageInfo, pid, find));
            pageInfo.setTotalRecord(tblStaffMapper.selectListByPageInfoCount(pid));
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> findAllMySqlPageBeanPid(String staffId, Find find, Integer pageNumber, Integer pageSize, BigDecimal pid) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            PageInfo<TblStaffMySql> pageInfo = new PageInfo<TblStaffMySql>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTlist(tblStaffMySqlMapper.selectListByPageInfoOrgid(pageInfo, pid, find));
            pageInfo.setTotalRecord(tblStaffMySqlMapper.selectListByPageInfoCount(pid));
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> findAllPageBean(Find find, Integer pageNumber, Integer pageSize, String token, String staffId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblStaff> pageInfo = new PageInfo<TblStaff>();
        try {
            TblStaffUtil staff = userProvider.get();
            BigDecimal orgid = staff.getCurrentOrg().getOrgid();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);

            pageInfo.setTlist(tblStaffMapper.selectListByPageInfoFind(pageInfo, orgid, find));
            pageInfo.setTotalRecord(tblStaffMapper.selectListByPageInfoCountOrgid(orgid));
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> findAllMySqlPageBean(Find find, Integer pageNumber, Integer pageSize, String token, String staffId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblStaffMySql> pageInfo = new PageInfo<TblStaffMySql>();
        try {
            TblStaffUtil staff = userProvider.get();
            BigDecimal orgid = staff.getCurrentOrg().getOrgid();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);

            pageInfo.setTlist(tblStaffMySqlMapper.selectListByPageInfoFind(pageInfo, orgid, find));
            pageInfo.setTotalRecord(tblStaffMySqlMapper.selectListByPageInfoCountOrgid(orgid));
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

//    @Override
//    public Map<String, Object> findAll(String staffId, Find find,Integer pageNumber, Integer pageSize,String token) {
//        Map<String,Object> resultMap = new HashMap<String, Object>(0);
//        com.hbfk.util.PageInfo<UserService> pageInfo = new com.hbfk.util.PageInfo<UserService>();
//        if(pageSize != null) {
//            pageInfo.setPageSize(pageSize);
//        }
//        Claims claims = JwtUtils.parseJwt(token);
//        Object object = claims.get("staffInfo");
//        JSONObject objJson = JSONObject.fromObject(object);
//        TblStaff staff = (TblStaff) JSONObject.toBean(objJson,TblStaff.class);
//
//        TblStaff tblStaff = new TblStaff();
//        tblStaff.setOrgid(staff.getCurrentOrg().getOrgid());
//        pageInfo.setCurrentPage(pageNumber);
//        pageInfo.setPageSize(pageSize);
//        pageInfo.setCondition((UserService) tblStaff);
//        pageInfo.setTlist(tblStaffMapper.selectListByPageIn(pageInfo));
//        resultMap.put("code", "1");
//        resultMap.put("msg", "访问接口成功");
//        resultMap.put("data", pageInfo);
//        return resultMap;
//    }
//
//    @Override
//    public Map<String, Object> findAllPid(String staffId, Find find,Integer pageNumber, Integer pageSize,String token) {
//        Map<String,Object> resultMap = new HashMap<String, Object>(0);
//        com.hbfk.util.PageInfo<TblStaff> pageInfo = new com.hbfk.util.PageInfo<TblStaff>();
//        if(pageSize != null) {
//            pageInfo.setPageSize(pageSize);
//        }
//        Claims claims = JwtUtils.parseJwt(token);
//        Object object = claims.get("staffInfo");
//        JSONObject objJson = JSONObject.fromObject(object);
//        TblStaff staff = (TblStaff) JSONObject.toBean(objJson,TblStaff.class);
//        BigDecimal orgid = staff.getCurrentOrg().getOrgid();
//        TblStaff tblStaff = new TblStaff();
//        tblStaff.setOrgid(staff.getCurrentOrg().getOrgid());
//        pageInfo.setCurrentPage(pageNumber);
//        pageInfo.setPageSize(pageSize);
//        pageInfo.setCondition(tblStaff);
//        pageInfo.setTlist(tblStaffMapper.selectListByPageInfoo(pageInfo));
//        resultMap.put("code", "1");
//        resultMap.put("msg", "访问接口成功");
//        resultMap.put("data", pageInfo);
//        return resultMap;
//    }

    @Override
    public void add(TblStaff tblStaff) {
        tblStaffMapper.insertUser(tblStaff);
    }

    @Override
    public void addMySql(TblStaffMySql tblStaff) {
        tblStaffMySqlMapper.insertUser(tblStaff);
    }

    @Override
    public TblStaff findByUserId(String userid) {
        return tblStaffMapper.selectByUserId(userid);
    }

    @Override
    public TblStaffMySql findByMySqlUserId(String userid) {
        return tblStaffMySqlMapper.selectByUserId(userid);
    }

//    @Override
//    public TblStaffMySql findByMySqlUserId(String userid) {
//        TblStaffMySql tblStaff = tblStaffMySqlMapper.selectByUserId(userid);
//        return tblStaff;
//    }

//    @Override
//    public void initializationSystemData(TblOrganizationUtil organization, TblStaffUtil user, String hyId) {
////        Session session = this.getSession();
////        Transaction tx = session.beginTransaction();
//
//        try {
//            List<String> sqlList = new ArrayList(0);
//            String sql = null;
//            Query query = null;
//            Object oneObjId = null;
//            Object twoObjId = null;
//            new ArrayList(0);
//            String htRightId = this.tblManageRightDAO.selectByRightName();
//            Integer htCount = this.tblManageRightDAO.selectCountHtrightId(organization.getOrgid(),htRightId);
//            List objList;
//            if (htCount == 0) {
//                this.tblManageRightDAO.insertRightNew(organization.getOrgid(),htRightId);
//                objList = this.tblManageRightDAO.selectRightIdsByParentId(htRightId);
//                //this.grantContractRightForOrg(organization.getOrgid(), objList);
//            } else {
//                this.tblManageRightDAO.UpdateRightNew(organization.getOrgid(),htRightId);
//            }
//             htCount = this.tblManageRightDAO.selectManageUserRight(user.getStaffid(),htRightId);
//            if (htCount == 0) {
//                sql = "INSERT INTO TBL_MANAGE_USER_RIGHT(STAFFID,RIGHTID) VALUES(" + user.getStaffid() + "," + htRightId + ")";
//                query = session.createSQLQuery(sql);
//                query.executeUpdate();
//                sql = "SELECT RIGHTID FROM TBL_MANAGE_RIGHT WHERE FATHERRIGHTID = " + htRightId;
//                objList = this.selectRightIdsByParentId(sql);
//                //this.grantContractRightForStaff(user.getStaffid(), objList);
//            }
//
//            String uuid = ClientDataSourceFactory.getUuid();
//            sql = "INSERT INTO TBL_ACCBOOK (BOOKID,BOOKNAME,ORGID,ORGNAME,ACCTID,BOOKDESC,BOOKYEAR,BALANCESHEETURL,INCOMESTATEMENTSURL,CASHFLOWSTATEMENTSURL) VALUES ('" + uuid + "', '" + organization.getOrgname() + "2018财务账套', '" + organization.getOrgid() + "', '" + organization.getOrgname() + "','HBFKCWZT2018',NULL,'2018','formlet=demo%2F%5B4e09%5D%5B5927%5D%5B62a5%5D%5B8868%5D%2F%5B8d44%5D%5B4ea7%5D%5B8d1f%5D%5B503a%5D%5B8868%5D2017.frm','formlet=demo%2F%5B4e09%5D%5B5927%5D%5B62a5%5D%5B8868%5D%2F%5B5229%5D%5B6da6%5D%5B8868%5D2017.frm',NULL)";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_ACCBOOK (BOOKID, BOOKNAME, ORGID, ORGNAME, ACCTID, BOOKDESC, BOOKYEAR, BALANCESHEETURL, INCOMESTATEMENTSURL, CASHFLOWSTATEMENTSURL) VALUES ('" + ClientDataSourceFactory.getUuid() + "', '" + organization.getOrgname() + "2018业务账套', '" + organization.getOrgid() + "', '" + organization.getOrgname() + "','HBFKCWZT2018SUPPLY', '" + uuid + "', '2018', NULL, NULL, NULL)";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_MANAGE_USER_BOOK(STAFFID,BOOKID,STATUS) VALUES ('" + user.getStaffid() + "','" + uuid + "',0)";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object objId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISK_ASSESSMENTSTD (ASSSTDID, ASSNUMBER, ASSNAME, ASSDES, MEMO, ASSSTATUS, COMPANYID) VALUES ('" + objId + "', 'TZFXPG-CJJT001', '投资风险评估', '投资风险评估', NULL, '1', '" + organization.getOrgid() + "')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_RISK_POSSIBILITY (POSSID, RPLEVEL, POSSDES, MEMO, ASSSTDID) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'1', '财务影响 极轻微的财务损失：企业财务损失占净资产的1‰（即约180万元）以下 安全影响 对企业的安全运营产生轻微的影响，基本没有造成人员的伤亡及经济损失。 战略影响 对企业的战略目标的实现产生轻微的影响 合规影响 对企业的合规行为产生轻微的影响 运营影响 对企业的运营产生轻微的影',NULL, '" + objId + "')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK_POSSIBILITY (POSSID, RPLEVEL, POSSDES, MEMO, ASSSTDID) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'2', '财务影响 轻微的财务损失：企业财务损失占净资产的 1‰-3‰（即约180万-530万元) 安全影响 造成轻微的人员受伤，或者微小的设备运营影响以及轻微的经济损失。 1.造成1人轻伤 2.部分设备的正常运营受到很短暂影响 战略影响 3.较小的影响企业战略计划的实施，没有对战略目标的实现产生影响 4.短期或有限的社会形象的下降 5.在保留客户上产生有限的影响 合规影响 在一些不严重并且是独立的案例中，未能遵循法律和法规的要求 运营影响 1.在系统上的损失导致较小的营运中断（长至一个小时） 2.短期或有限的营运损失，对市值的影响相对最小 3.成本有限的增加对收入和利润产生的影响相对最小 4.在一些部门损失了某些职员（职位空缺期长至一个月）',NULL, '" + objId + "')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK_POSSIBILITY (POSSID, RPLEVEL, POSSDES, MEMO, ASSSTDID) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'3', '财务影响 中等的财务损失：企业财务损失占净资产的 3‰-8‰（即约530万-1,400万元) 安全影响 造成少量的人员受伤，或者较小的设备运营影响以及一定的经济损失。 1.造成1至4人轻伤 2.部分设备的正常运营受到较短暂影响 3.小范围内的用户停气 战略影响 1.在一定程度上影响企业战略计划的实施，对战略目标的实现产生较小的影响，通过企业的调整可以挽回 2.暂时的社会形象的下降，但是通过补救措施可以恢复 3.损失了高端客户或联盟、客户的忠诚度以及销售的机会 合规影响 在某些情况下，未能遵循法律和法规的要求 运营影响 1.在系统上的损失导致较大的营运中断（长至半天） 2.营运损失导致市值暂时但可恢复的减少 3.成本短期的上升对当前的所得和盈利率产生影响 4.在一些部门，损失或无法招聘到一定数量的关键职员（职位空缺期长至三个月）',NULL, '" + objId + "')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK_POSSIBILITY (POSSID, RPLEVEL, POSSDES, MEMO, ASSSTDID) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'4', '财务影响 重大的财务损失：企业财务损失占净资产的 8‰-2%（即约1,400万-3,500 万元) 安全影响 造成一定范围的人员受伤，或者中度的设备运营影响以及较大的经济损失。 1.造成5人以上轻伤，或者出现1至2人重伤 2.部分重要设备的正常运营在一定期间内受到影响 3.一定范围内的用户停气 战略影响 1.较大的影响企业战略计划的实施，对战略目标的实现产生一定的影响，需要通过较大的调整才能够挽回 2.公司的社会形象有重大的下降，需要通过较大的补救措施，才能够恢复 3.在一定区域内损失了一块重大的客户基础 合规影响 未能遵循法律和法规的要求，虽然重大但仍可恢复 运营影响 1.在系统上的损失导致重大的营运中断（长至一天） 2.重大营运损失导致市值重大但可恢复的减少 3.承受过多的成本对当前的所得和盈利率产生影响 ',NULL, '" + objId + "')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK_POSSIBILITY (POSSID, RPLEVEL, POSSDES, MEMO, ASSSTDID) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'5', '财务影响 重大的财务损失：企业财务损失占净资产的2% （即约3,500 万元）以上 安全影响 造成较大范围的人员受伤，或者出现人员死亡，或者较大的设备运营影响以及较大的经济损失。 1.造成大范围的人员轻伤，或3人以上重伤，或者出现死亡的情况 2.部分重要设备的正常运营在较长的期间内受到影响 3.较大范围内的用户停气 战略影响 1.重大的影响企业战略计划的实施，对战略目标的实现产生很大的影响，不一定能够通过调整进行挽回 2.公司的社会形象有非常重大的下降，不一定能够通过补救措施进行挽回 3.在很大范围内损失了一块重大的客户基础 合规影响 未能遵循法律和法规的要求，并且十分严重，影响公司的持续经营 运营影响 1.在系统上的损失导致严重的影响或正常营运的中断（超过一天） 2.重大营运损失导致市值的重大减少' ,NULL, '" + objId + "')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK_INFLUDEGREE (DEGREEID, ASSSTDID, RILEVEL, INFLUDEGREEDES, MEMO) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '" + objId + "', '1', '定量：发生风险概率<10%或者5年内发生可能1次;定性：业务极少发生，发生风险的绝对数量非常小', NULL)";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK_INFLUDEGREE (DEGREEID, ASSSTDID, RILEVEL, INFLUDEGREEDES, MEMO) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '" + objId + "', '2', '定量：发生风险概率在10%－30%或者3－5年内可能发生1次;定性：业务较少发生，发生风险的绝对数量很小', NULL)";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK_INFLUDEGREE (DEGREEID, ASSSTDID, RILEVEL, INFLUDEGREEDES, MEMO) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '" + objId + "', '3', '定量：发生风险概率在30%－60%或者2－3年内至少发生1次;定性：业务发生频率中等，发生风险的绝对数量不太多', NULL)";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK_INFLUDEGREE (DEGREEID, ASSSTDID, RILEVEL, INFLUDEGREEDES, MEMO) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '" + objId + "', '4', '定量：发生风险概率在60%－90%或者半年至2年内至少发生1次;定性：业务发生频率较普通偏高，风险发生的绝对数量较多', NULL)";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK_INFLUDEGREE (DEGREEID, ASSSTDID, RILEVEL, INFLUDEGREEDES, MEMO) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '" + objId + "', '5', '定量：发生风险概率>90%或者半年内至少发生1次;定性：业务发生很频繁，风险发生绝对数量很多', NULL)";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_ASSESSLEVEL (ASSLEVID, LEVELNAME, UPPERREGIONDES, LEVELUPPER, LEVELLOWER, LOWERREGIONDES, LEVELDES, MODIFIEDDATE, MEMO, TBLCOMANY) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '基础级', NULL,'59','0',NULL,'基础级', TO_DATE('" + DateUtils.getNowTime() + "','YYYY-MM-DD HH24:MI:SS'), NULL, '" + organization.getOrgid() + "')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_ASSESSLEVEL (ASSLEVID, LEVELNAME, UPPERREGIONDES, LEVELUPPER, LEVELLOWER, LOWERREGIONDES, LEVELDES, MODIFIEDDATE, MEMO, TBLCOMANY) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '受控级', NULL,'60','79',NULL,'受控级', TO_DATE('" + DateUtils.getNowTime() + "','YYYY-MM-DD HH24:MI:SS'), NULL, '" + organization.getOrgid() + "')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_ASSESSLEVEL (ASSLEVID, LEVELNAME, UPPERREGIONDES, LEVELUPPER, LEVELLOWER, LOWERREGIONDES, LEVELDES, MODIFIEDDATE, MEMO, TBLCOMANY) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '规范级', NULL, '80','89',NULL,'规范级', TO_DATE('" + DateUtils.getNowTime() + "','YYYY-MM-DD HH24:MI:SS'), NULL, '" + organization.getOrgid() + "')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_ASSESSLEVEL (ASSLEVID, LEVELNAME, UPPERREGIONDES, LEVELUPPER, LEVELLOWER, LOWERREGIONDES, LEVELDES, MODIFIEDDATE, MEMO, TBLCOMANY) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '成熟级', NULL, '90','100', NULL,'成熟级', TO_DATE('" + DateUtils.getNowTime() + "','YYYY-MM-DD HH24:MI:SS'), NULL, '" + organization.getOrgid() + "')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,POSITION)VALUES(HIBERNATE_SEQUENCE.NEXTVAL,'fwgl','法务管理'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "',0,SYSDATE,1,'1')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,POSITION)VALUES(HIBERNATE_SEQUENCE.NEXTVAL,'fwgl','全面预算'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "',0,SYSDATE,1,'2')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            oneObjId = query.uniqueResult();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,POSITION)VALUES('" + oneObjId + "','htgl','合同管理'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "',0,SYSDATE,1,'3')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,POSITION)VALUES(HIBERNATE_SEQUENCE.NEXTVAL,'fwgl','采购业务'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "',0,SYSDATE,1,'4')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,POSITION)VALUES(HIBERNATE_SEQUENCE.NEXTVAL,'fwgl','人力资源'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "',0,SYSDATE,1,'5')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,POSITION)VALUES(HIBERNATE_SEQUENCE.NEXTVAL,'fwgl','资产管理'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "',0,SYSDATE,1,'6')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,POSITION)VALUES(HIBERNATE_SEQUENCE.NEXTVAL,'fwgl','资金活动'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "',0,SYSDATE,1,'7')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object twoObjId0 = query.uniqueResult();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,FIRINGSTATUS) VALUES ('" + twoObjId0 + "','FLOW_10','合同范本'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "','" + oneObjId + "',SYSDATE,1,1)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object twoObjId1 = query.uniqueResult();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,FIRINGSTATUS) VALUES ('" + twoObjId1 + "','FLOW_11','相对方管理'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "','" + oneObjId + "',SYSDATE,1,1)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object twoObjId2 = query.uniqueResult();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,FIRINGSTATUS) VALUES ('" + twoObjId2 + "','FLOW_12','合同订立'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "','" + oneObjId + "',SYSDATE,1,1)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object twoObjId3 = query.uniqueResult();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,FIRINGSTATUS) VALUES ('" + twoObjId3 + "','FLOW_13','合同用印'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "','" + oneObjId + "',SYSDATE,1,1)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object twoObjId4 = query.uniqueResult();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,FIRINGSTATUS) VALUES ('" + twoObjId4 + "','FLOW_14','合同变更'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "','" + oneObjId + "',SYSDATE,1,1)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object twoObjId6 = query.uniqueResult();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,FIRINGSTATUS) VALUES ('" + twoObjId6 + "','FLOW_16','合同归档'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "','" + oneObjId + "',SYSDATE,1,1)";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object threeObjId0 = query.uniqueResult();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,SETTINGID,FROMID,FIRINGSTATUS) VALUES ('" + threeObjId0 + "','HTGL007','合同范本'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "','" + twoObjId0 + "',SYSDATE,1,'cyhw_jjhtjc',21,1)";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object threeObjId1 = query.uniqueResult();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,SETTINGID,FROMID,FIRINGSTATUS) VALUES ('" + threeObjId1 + "','HTGL001','相对方管理'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "','" + twoObjId1 + "',SYSDATE,1,'cyhw_xmzc',20,1)";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object threeObjId2 = query.uniqueResult();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,SETTINGID,FROMID,FIRINGSTATUS) VALUES ('" + threeObjId2 + "','HTGL002','合同订立'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "','" + twoObjId2 + "',SYSDATE,1,'cyhw_jjhtjc',21,1)";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object threeObjId3 = query.uniqueResult();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,SETTINGID,FROMID,FIRINGSTATUS) VALUES ('" + threeObjId3 + "','HTGL003','合同用印'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "','" + twoObjId3 + "',SYSDATE,1,'cyhw_xmzc',20,1)";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object threeObjId4 = query.uniqueResult();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,SETTINGID,FROMID,FIRINGSTATUS) VALUES ('" + threeObjId4 + "','HTGL005','合同变更'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "','" + twoObjId4 + "',SYSDATE,1,'cyhw_jjhtjc',21,1)";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object threeObjId6 = query.uniqueResult();
//            sql = "INSERT INTO TBL_FLOW(FLOWID,FLOWNUMBER,FLOWNAME,COMPANY,DEPARTINCHARGE,VERSION,EDITOR,FATHERFLOWID,CREATETIME,FLOWBYSYSTEM,SETTINGID,FROMID,FIRINGSTATUS) VALUES ('" + threeObjId6 + "','HTGL006','合同归档'," + organization.getOrgid() + "," + organization.getOrgid() + ",1,'" + user.getRealname() + "','" + twoObjId6 + "',SYSDATE,1,'cyhw_xmzc',20,1)";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object modelId = query.uniqueResult();
//            sql = "INSERT INTO TBL_SYSTEM_MODULE (MODELID,MODELNAME,MODELURL,MODELSTATUS,MODELTYPE,MODELORG,CREATETIME,CREATEPERSON,MODIFYTIME,MODIFYPERSON,MODELNO,MODELORDER) VALUES (" + modelId + ",'合同管理', '../images/slices/1(5).png', '1', '" + htRightId + "'," + organization.getOrgid() + ", SYSDATE, " + user.getStaffid() + ", SYSDATE, NULL, 'PRCM001',1)";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_SYSTEM_MODELFLOW (MODELID, FLOWID, MODELNO) VALUES (" + modelId + ", " + threeObjId0 + ", '1')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_SYSTEM_MODELFLOW (MODELID, FLOWID, MODELNO) VALUES (" + modelId + ", " + threeObjId1 + ", '2')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_SYSTEM_MODELFLOW (MODELID, FLOWID, MODELNO) VALUES (" + modelId + ", " + threeObjId2 + ", '3')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_SYSTEM_MODELFLOW (MODELID, FLOWID, MODELNO) VALUES (" + modelId + ", " + threeObjId3 + ", '4')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_SYSTEM_MODELFLOW (MODELID, FLOWID, MODELNO) VALUES (" + modelId + ", " + threeObjId4 + ", '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_SYSTEM_MODELFLOW (MODELID, FLOWID, MODELNO) VALUES (" + modelId + ", " + threeObjId6 + ", '7')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_SYSTEM_MODELORG (MODELID, ORGID) VALUES (" + modelId + ", " + organization.getOrgid() + ")";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_SYSTEM_MODELORG (MODELID, ORGID) VALUES ((SELECT MODELID FROM TBL_SYSTEM_MODULE WHERE MODELNAME='合同履行'  AND MODELORG = 116821 AND CREATEPERSON = 127822), " + organization.getOrgid() + ")";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_SYSTEM_MODELORG (MODELID, ORGID) VALUES ((SELECT MODELID FROM TBL_SYSTEM_MODULE WHERE MODELNAME='法务管理'  AND MODELORG = 116821 AND CREATEPERSON = 127822), " + organization.getOrgid() + ")";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_SYSTEM_BIMODULE(ORGID,PAGEID,MODULERIGHTID,PAGEBODY) SELECT '" + organization.getOrgid() + "',TSB.PAGEID,TSB.MODULERIGHTID AS RIGHTID,TSB.PAGEBODY FROM TBL_SYSTEM_BIMODULE TSB WHERE TSB.MODULERIGHTID = '" + htRightId + "' AND TSB.ORGID = '116821'";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object form_xdf1 = query.uniqueResult();
//            sql = "INSERT INTO TBL_CYHW_PROJECTBUDGET(COUNTERPARTNO, BUDGETID, BUDGETNAME, TOTALTMONEY, SERVICETYPE, PROJECTEASON, PROJECTGOAL, PROJECTSTAGEGOAL, PROJECTCONDITION, ORGID, CREATEUSER, CREATETIME, FLOWID, INSPECTIONSTATUS, COUNTERPARTADDRESS, COUNTERPARTCODE, COUNTERPARTHANK, COUNTERPARTHANKACCOUNT, COUNTERPARTNETADDRESS, COUNTERPARTPHONE, RECORDTYPE,RESULTDESCRIPTION ) VALUES ( 'CPNO-001'," + form_xdf1 + ", 'XX文化传媒公司', '1000000', '是', '911009847278NP', NULL, '" + user.getRealname() + "', '1130723198401249924', '" + organization.getOrgid() + "', '" + user.getStaffid() + "', SYSDATE, '" + threeObjId1 + "', '6', '上海', '2048', '建设银行浦东分行', '1130723198401249924', 'www.huabocn.com', '020-19488234', 'HTGL001','RX457564546575654' )";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object form_htdl1 = query.uniqueResult();
//            sql = "INSERT INTO TBL_CYHW_UNIT ( CONTRACTID, FLOWID, UNITNAME, CONTRACTNAME, CONTRACTNO, CONTRACTDEPT, CONTRACTLINK, CONTRACTMONEY, CONTRACTSTATUS, LINKDEPT, ORGID, CREATEUSER, CREATETIME, MOMOCONCAT, RISKCONTROL, DESCRIBE, CONTRACTSTAFF, SCRILEVEL, STARTDATE, ENDDATE, CONTRACTITEM, CONTRACTTYPE, CONTRACTDATETYPE, CONTRACTXZ, CONTRACTXDFXINFO, RECORDTYPE, RECORDPARENT, HZSUMOWING, JIJIATYPE, MONEYTYPE, DCTYPE, CONTRACTBD, CONTRACTZD, JBSTAFF, JBDEPT, JBUNIT, ZXUNIT, CONTRACTCHILDREN, CONTRACTPLAN ) VALUES (" + form_htdl1 + ", '" + threeObjId2 + "', NULL, 'XX采购合同', 'HBYUN-HTXT1583659429404', '" + organization.getOrgid() + "', NULL, '1000000', '8', NULL, '" + organization.getOrgid() + "', '" + user.getStaffid() + "', SYSDATE, NULL, NULL, NULL, '" + user.getStaffid() + "', NULL, SYSDATE, SYSDATE, 'XX采购项目', '租赁合同', '固定期限', '境内', '" + form_xdf1 + "', 'HTGL002', NULL, '壹拾万元整', '固定总价', '人民币', '收款', '合同标的', '是', '" + user.getStaffid() + "', '" + organization.getOrgid() + "', '" + organization.getOrgid() + "', '" + organization.getOrgid() + "', '是', '是' )";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object form_htdl1_1 = query.uniqueResult();
//            sql = "INSERT INTO TBL_CONTRACT_PLANNODE ( NODEID, NODECONTENT, PROJECTID, BLPROJECTID, PLANSTARTDATE, PLANENDDATE, NODEPOST, NODEPLANPAYDATE,DISPATCHSTAFF,DISPATCHDEPT,PLANNODESTATUS ) VALUES (" + form_htdl1_1 + ", '首付款', '" + form_htdl1 + "', NULL, SYSDATE-10, SYSDATE+10, '40', SYSDATE," + user.getStaffid() + "," + organization.getOrgid() + ",2 )";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object form_htdl1_2 = query.uniqueResult();
//            sql = "INSERT INTO TBL_CONTRACT_PLANNODE ( NODEID, NODECONTENT, PROJECTID, BLPROJECTID, PLANSTARTDATE, PLANENDDATE, NODEPOST, NODEPLANPAYDATE,DISPATCHSTAFF,DISPATCHDEPT,PLANNODESTATUS ) VALUES (" + form_htdl1_2 + ", '验收款', '" + form_htdl1 + "', NULL, SYSDATE+11, SYSDATE+31, '40', SYSDATE+21," + user.getStaffid() + "," + organization.getOrgid() + ",2 )";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object form_htdl1_3 = query.uniqueResult();
//            sql = "INSERT INTO TBL_CONTRACT_PLANNODE ( NODEID, NODECONTENT, PROJECTID, BLPROJECTID, PLANSTARTDATE, PLANENDDATE, NODEPOST, NODEPLANPAYDATE,DISPATCHSTAFF,DISPATCHDEPT,PLANNODESTATUS ) VALUES (" + form_htdl1_3 + ", '尾款', '" + form_htdl1 + "', NULL, SYSDATE+32, SYSDATE+52, '20', SYSDATE+42," + user.getStaffid() + "," + organization.getOrgid() + ",2 )";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_CONTRACT_SPNODE (SPNODEID, NODEID, STARTDATE, ENDDATE, NODEMEMO, NODECONTENT, CONTRACTID, NODEPOST)VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '" + form_htdl1_1 + "', SYSDATE+11, SYSDATE+12, '已经落实完', '已经落实完','" + form_htdl1 + "', '已经落实完')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_CONTRACT_SPNODE (SPNODEID, NODEID, STARTDATE, ENDDATE, NODEMEMO, NODECONTENT, CONTRACTID, NODEPOST)VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '" + form_htdl1_2 + "', SYSDATE+32, SYSDATE+33, '已经落实完', '已经落实完','" + form_htdl1 + "', '已经落实完')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_CONTRACT_SPNODE (SPNODEID, NODEID, STARTDATE, ENDDATE, NODEMEMO, NODECONTENT, CONTRACTID, NODEPOST)VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '" + form_htdl1_3 + "', SYSDATE+53, SYSDATE+54, '已经落实完', '已经落实完','" + form_htdl1 + "', '已经落实完')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_CONTRACT_TYPEOF(TYPEID,TYPENAME,CREATESTAFF,CREATETIME,ORGID,SETTINGID) SELECT HIBERNATE_SEQUENCE.NEXTVAL,TYPENAME," + user.getStaffid() + ",SYSDATE," + organization.getOrgid() + ",'cyhw_jjhtjc' FROM TBL_CONTRACT_TYPEOF WHERE ORGID = 116821";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object kpIdone = query.uniqueResult();
//            sql = "INSERT INTO TBL_CONTRACT_INVOICESMANAGEMEN (INVOICEID, BUDGETID, INVOICENO, INVOICEMONEY, INVOICEDATE, INVOICEPOST, INVOICECONTENT, INVOICETYPE, INVOICESTATUS, INVOICESPDATE, INVOICESPORG, INVOICEKPORG, INVOICEOGR, CREATESTAFF, INVOICEHEADTEXT) VALUES ('" + kpIdone + "', '" + form_xdf1 + "', '14920754', '400000', SYSDATE+12, '40', '首付款', '增值税专用发票', '6', SYSDATE+12, '" + organization.getOrgname() + "', 'XX文化传媒公司', '" + organization.getOrgid() + "', '" + user.getStaffid() + "', 'XX文化传媒公司')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object kpIdtwo = query.uniqueResult();
//            sql = "INSERT INTO TBL_CONTRACT_INVOICESMANAGEMEN (INVOICEID, BUDGETID, INVOICENO, INVOICEMONEY, INVOICEDATE, INVOICEPOST, INVOICECONTENT, INVOICETYPE, INVOICESTATUS, INVOICESPDATE, INVOICESPORG, INVOICEKPORG, INVOICEOGR, CREATESTAFF, INVOICEHEADTEXT) VALUES ('" + kpIdtwo + "', '" + form_xdf1 + "', '15203564', '400000', SYSDATE+32, '40', '验收款', '增值税专用发票', '6', SYSDATE+22, '" + organization.getOrgname() + "', 'XX文化传媒公司', '" + organization.getOrgid() + "', '" + user.getStaffid() + "', 'XX文化传媒公司')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object kpIdthree = query.uniqueResult();
//            sql = "INSERT INTO TBL_CONTRACT_INVOICESMANAGEMEN (INVOICEID, BUDGETID, INVOICENO, INVOICEMONEY, INVOICEDATE, INVOICEPOST, INVOICECONTENT, INVOICETYPE, INVOICESTATUS, INVOICESPDATE, INVOICESPORG, INVOICEKPORG, INVOICEOGR, CREATESTAFF, INVOICEHEADTEXT) VALUES ('" + kpIdthree + "', '" + form_xdf1 + "', '15842524', '200000', SYSDATE+52, '20', '尾款', '增值税专用发票', '6', SYSDATE+53, '" + organization.getOrgname() + "', 'XX文化传媒公司', '" + organization.getOrgid() + "', '" + user.getStaffid() + "', 'XX文化传媒公司')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_CONTRACT_COLLECTION (COLLECTIONID, INVOICEID, COLLECTIONBANK, COLLECTIONSKDATE, COLLECTIONACCOUNT, COLLECTIONORGNAME, CONTRACTID, NODEID, CREATESTAFF, COLLECTIONSTATUS, LINKORG, CREATEDATE) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '" + kpIdone + "', '招商银行亦庄支行',SYSDATE+12, '首付款', '" + organization.getOrgname() + "', '" + form_htdl1 + "', '" + form_htdl1_1 + "', '" + user.getStaffid() + "', '6', '" + organization.getOrgid() + "',SYSDATE)";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_CONTRACT_COLLECTION (COLLECTIONID, INVOICEID, COLLECTIONBANK, COLLECTIONSKDATE, COLLECTIONACCOUNT, COLLECTIONORGNAME, CONTRACTID, NODEID, CREATESTAFF, COLLECTIONSTATUS, LINKORG, CREATEDATE) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '" + kpIdtwo + "', '招商银行亦庄支行',SYSDATE+22, '验收款', '" + organization.getOrgname() + "', '" + form_htdl1 + "', '" + form_htdl1_2 + "', '" + user.getStaffid() + "', '6', '" + organization.getOrgid() + "',SYSDATE)";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_CONTRACT_COLLECTION (COLLECTIONID, INVOICEID, COLLECTIONBANK, COLLECTIONSKDATE, COLLECTIONACCOUNT, COLLECTIONORGNAME, CONTRACTID, NODEID, CREATESTAFF, COLLECTIONSTATUS, LINKORG, CREATEDATE) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '" + kpIdthree + "', '招商银行亦庄支行',SYSDATE+53, '尾款款', '" + organization.getOrgname() + "', '" + form_htdl1 + "', '" + form_htdl1_3 + "', '" + user.getStaffid() + "', '6', '" + organization.getOrgid() + "',SYSDATE)";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object form_htyy1 = query.uniqueResult();
//            sql = "INSERT INTO TBL_CYHW_PROJECTBUDGET ( BUDGETID, BUDGETNAME, PROJECTEASON, ORGID, CREATEUSER, CREATETIME, FLOWID, INSPECTIONSTATUS, RECORDTYPE, RECORDPARENT ) VALUES (" + form_htyy1 + ", '" + organization.getOrgname() + "', '张**', '" + organization.getOrgid() + "', '" + user.getStaffid() + "', SYSDATE, '" + threeObjId3 + "', '6', 'HTGL003', '" + form_htdl1 + "' )";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object form_htbl1 = query.uniqueResult();
//            sql = "INSERT INTO TBL_CYHW_UNIT ( CONTRACTID, FLOWID, UNITNAME, CONTRACTNAME, CONTRACTNO, CONTRACTDEPT, CONTRACTLINK, CONTRACTMONEY, CONTRACTSTATUS, LINKDEPT, ORGID, CREATEUSER, CREATETIME, MOMOCONCAT, RISKCONTROL, DESCRIBE, CONTRACTSTAFF, SCRILEVEL, STARTDATE, ENDDATE, CONTRACTITEM, CONTRACTTYPE, CONTRACTDATETYPE, CONTRACTXZ, CONTRACTXDFXINFO, RECORDTYPE, HZSUMOWING, JIJIATYPE, MONEYTYPE, DCTYPE, CONTRACTBD, CONTRACTZD, JBSTAFF, JBDEPT, JBUNIT, ZXUNIT, CONTRACTCHILDREN, CONTRACTPLAN ,RECORDPARENT) VALUES (" + form_htbl1 + ", '" + threeObjId4 + "', NULL, 'XX采购项目补录', 'HBYUN-HTXT1583659429405', '" + organization.getOrgid() + "', NULL, '1000000', '6', NULL, '" + organization.getOrgid() + "', '" + user.getStaffid() + "', SYSDATE, NULL, NULL, NULL, '" + user.getStaffid() + "', NULL, SYSDATE, SYSDATE, 'XX采购项目补录', '租赁合同', '固定期限', '境内', '" + form_xdf1 + "', 'HTGL005', '壹拾万元整', '固定总价', '人民币', '收款', '合同标的', '是', '" + user.getStaffid() + "', '" + organization.getOrgid() + "', '" + organization.getOrgid() + "', '" + organization.getOrgid() + "', '是', '是'," + form_htdl1 + " )";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object form_htgd1 = query.uniqueResult();
//            sql = "INSERT INTO TBL_CYHW_PROJECTBUDGET ( BUDGETID, BUDGETNAME, SERVICETYPE, ISGOVERNMENT, PROJECTGOAL, PROJECTRISK, ORGID, CREATEUSER, CREATETIME, FLOWID, INSPECTIONSTATUS, RECORDTYPE, RECORDPARENT ) VALUES (" + form_htgd1 + ", 'XX采购合同', '租赁合同', '1000000', 'HBYUN-HTXT1583659429404', '合同归档。', '" + organization.getOrgid() + "', '" + user.getStaffid() + "', SYSDATE, '" + threeObjId6 + "', '8', 'HTGL006', '" + form_htdl1 + "' )";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object pjoneId = query.uniqueResult();
//            sql = "INSERT INTO TBL_ASSESSTEMPLE (ASSTEMID,TEMPLENAME,ORGID,TEMPLENUMBER,STAFFID,MODIFYDATETIME,TEMPLEDES) VALUES (" + pjoneId + ",'" + organization.getOrgname() + "成熟度评价'," + organization.getOrgid() + ",'HBTZACSDPJ'," + user.getStaffid() + ",SYSDATE,'成熟度系数：A档（1/0.9/0.8）:建立全面系统、形成持续改进的机制，制度要求规范、详细、或比较先进、高效，制度得到全面、彻底有效地落实;B档（0.7/0.6/0.5）：建立规范、详细的制度要求，并对关键过程实施有效的控制，并能实时改进，做到举一反三；C档(0.4/0.3)：有基本的制度要求，并得到基本落实；D档(0.2/0.1)：有基本制度规定但未落实；没有相应制度规定但有执行；E档(0)：无制度无执行。备注：若评价结果为D档或E档，应确定相应内控缺陷，其余成熟度等级可提出管理建议。')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_TEMPLE_ORGANIZATION(ASSTEMID,ORGID) VALUES (" + pjoneId + "," + organization.getOrgid() + ")";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object pjoneLevelId = query.uniqueResult();
//            sql = "INSERT INTO TBL_ASSESSCATEGORY(ASSCATID,CATNAME,CATWEIGHT,FATHERASSCATID,ASSTEMID,CATDES) VALUES (" + pjoneLevelId + ",'风控体系',60,null," + pjoneId + ",'风控体系')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object pjtwoLevelId = query.uniqueResult();
//            sql = "INSERT INTO TBL_ASSESSCATEGORY(ASSCATID,CATNAME,CATWEIGHT,FATHERASSCATID,ASSTEMID,CATDES) VALUES (" + pjtwoLevelId + ",'组织架构',40,null," + pjoneId + ",'组织架构')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object ysoneId = query.uniqueResult();
//            sql = "INSERT INTO TBL_ASSESSELEMENT(ASSELEID,ELEMENTNAME,STANDARDSCORE,BUSINESSTYPE,AUDITPOINT,ASSESSRULES,STATUS,ELEMENTNUMBER,TBLCOMANY) VALUES ('" + ysoneId + "','业务部门（第一道防线）的风险管理职责和履职',0,'风控体系','A(1/0.9/0.8):业务部门能够与其他两道防线相互制约，互相促进，形成良性互动，运行机制和运行效果能够达到国内外先进水平，并在集团以外进行推广B(0.7/0.6/0.5):明确业务部门的风险管理职能，业务部门基本能够按照各自职责开展工作，并留有履职记录C(0.4/0.3/0.2):正式发文明确业务部门的风险管理职责，但无业务部门的履职记录D(0.1/0):未明确业务部门的风险管理职能',5,1,'TX1.1'," + organization.getOrgid() + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object ystwoId = query.uniqueResult();
//            sql = "INSERT INTO TBL_ASSESSELEMENT(ASSELEID,ELEMENTNAME,STANDARDSCORE,BUSINESSTYPE,AUDITPOINT,ASSESSRULES,STATUS,ELEMENTNUMBER,TBLCOMANY) VALUES ('" + ystwoId + "','风险内控职能部门（第二道防线）的职责和履职',0,'风控体系','A(1/0.9/0.8):风控职能部门职责清晰、定位准确，能够结合本单位情况，运用先进、高效的方法开展工作，使得风险管理工作与业务活动充分融合，形成机制并在国内外达到领先水平B(0.7/0.6/0.5):风控职能部门职责基本清晰，能够主动策划、组织、推动各项工作的开展，并适时监督检查，给予指导C(0.4/0.3/0.2):正式发文成立风控职能部门，有明确的职责要求，但职责空泛、模糊，风控职能部门未能完全履行职责，无履职记录D(0.1/0):未成立风控职能部门，或风控职能部门形同虚设',5,2,'TX1.2'," + organization.getOrgid() + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object ysthreeId = query.uniqueResult();
//            sql = "INSERT INTO TBL_ASSESSELEMENT(ASSELEID,ELEMENTNAME,STANDARDSCORE,BUSINESSTYPE,AUDITPOINT,ASSESSRULES,STATUS,ELEMENTNUMBER,TBLCOMANY) VALUES ('" + ysthreeId + "','内部审计部门（第三道防线）的职责和履职',0,'风控体系','A(1/0.9/0.8):能够与其他两道防线相互制约，互相促进，能够形成良性互动并高效运行，运行机制和运行效果能够达到国内外先进水平，并在集团以外进行推广B(0.7/0.6/0.5):明确内审部门的风险管理职能，业务部门基本能够按照各自职责开展工作C(0.4/0.3/0.2):正式发文明确内审部门的风险管理职责，但无履职记录D(0.1/0):未明确内部审计部门监督风险管理体系运行的职责',5,3,'TX1.3'," + organization.getOrgid() + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object ysfourId = query.uniqueResult();
//            sql = "INSERT INTO TBL_ASSESSELEMENT(ASSELEID,ELEMENTNAME,STANDARDSCORE,BUSINESSTYPE,AUDITPOINT,ASSESSRULES,STATUS,ELEMENTNUMBER,TBLCOMANY) VALUES ('" + ysfourId + "','内控建设',0,'组织架构','应正式发布该业务相关的风险管理和内部控制手册，包括编制业务涉及的所有相关流程，明确业务不相容岗位清单，明确业务权限表，针对识别出的流程风险，明确关键控制点。',5,39,'ZZJG1.1'," + organization.getOrgid() + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object ysfiveId = query.uniqueResult();
//            sql = "INSERT INTO TBL_ASSESSELEMENT(ASSELEID,ELEMENTNAME,STANDARDSCORE,BUSINESSTYPE,AUDITPOINT,ASSESSRULES,STATUS,ELEMENTNUMBER,TBLCOMANY) VALUES ('" + ysfiveId + "','内控建设',0,'组织架构','针对上年度业务缺陷，制定整改工作方案，明确整改措施、计划节点、责任人等，方案需按照计划有效落实。',5,40,'ZZJG1.2'," + organization.getOrgid() + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object yssixId = query.uniqueResult();
//            sql = "INSERT INTO TBL_ASSESSELEMENT(ASSELEID,ELEMENTNAME,STANDARDSCORE,BUSINESSTYPE,AUDITPOINT,ASSESSRULES,STATUS,ELEMENTNUMBER,TBLCOMANY) VALUES ('" + yssixId + "','组织架构的设计',0,'组织架构','应建立规范的公司治理结构，股东（大）会、董事会、监事会、经理层健全，确保决策、执行和监督相互分离，形成制衡。',5,41,'ZZJG1.2'," + organization.getOrgid() + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_ASSELE_CATEGORY(ASSELEID,ASSCATID,ELEMENTCATEGORYID,STANDARDSCORE) VALUES (" + ysoneId + "," + pjoneLevelId + ",HIBERNATE_SEQUENCE.NEXTVAL,5)";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_ASSELE_CATEGORY(ASSELEID,ASSCATID,ELEMENTCATEGORYID,STANDARDSCORE) VALUES (" + ystwoId + "," + pjoneLevelId + ",HIBERNATE_SEQUENCE.NEXTVAL,5)";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_ASSELE_CATEGORY(ASSELEID,ASSCATID,ELEMENTCATEGORYID,STANDARDSCORE) VALUES (" + ystwoId + "," + pjtwoLevelId + ",HIBERNATE_SEQUENCE.NEXTVAL,5)";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_ASSELE_CATEGORY(ASSELEID,ASSCATID,ELEMENTCATEGORYID,STANDARDSCORE) VALUES (" + yssixId + "," + pjtwoLevelId + ",HIBERNATE_SEQUENCE.NEXTVAL,5)";
//            sqlList.add(sql);
//            sql = " INSERT INTO TBL_BUGCRITERION (BUGCRIID, BUGCRILEVEL, BUGCRIDEFINE, BUGCRIRATION, BUGCRISTABILITY, STATUS, VERSION, ORGID) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'一般缺陷','是指除重大缺陷、重要缺陷以外的其他控制缺陷。','财务指标(合并) 重要性水平资产总额0.5%    营业收入         0.5%     税前利润         5%<整体重要性水平的20%    一次性重伤5人以内，无死亡；受到省级（含省级）以下政府部门处罚但未对公司定期报告披露造成负面影响。','其他与财务报告有关内部控制缺陷。决策程序效率不高；违反内部规章，但未形成损失；一般岗位业务人员流失严重；媒体出现负面新闻，但影响不大；一般业务制度或系统存在缺陷；一般缺陷未得到整改；',2,1," + organization.getOrgid() + ")";
//            sqlList.add(sql);
//            sql = " INSERT INTO TBL_BUGCRITERION (BUGCRIID, BUGCRILEVEL, BUGCRIDEFINE, BUGCRIRATION, BUGCRISTABILITY, STATUS, VERSION, ORGID) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'重要缺陷','是指一个或多个控制缺陷的组合，其严重程度低于重大缺陷，但仍有可能导致企业偏离控制目标。重要缺陷的严重程度低于重大缺陷，不会严重危及内部控制的整体有效性，但也应当引起董事会、经理层的充分关注。','财务指标(合并) 重要性水平\t资产总额       0.5%\t\t营业收入       0.5%\t\t税前利润       5%\t\t占整体重要性比例的20%-100%     已经发现并报告给管理层的重大内部控制缺陷在经过合理的时间后，整改不全面，不彻底；内部控制环境不完善；会计计量不及时、不准确，造成信息滞后或信息错误；财务制度存在严重缺陷。受到国家政府部门处罚，但未对公司定期报告披露造成负面影响；一次性死亡1人以上，或重伤5人以上；被媒体曝光且产生负面影响。','民主决策程序存在但不够完善；决策程序导致出现一般失误；违反企业内部规章，形成损失；关键岗位业务人员流失严重；媒体出现负面新闻，波及局部区域；重要业务制度或系统存在缺陷；内部控制重要或一般缺陷未得到整改。',2,1," + organization.getOrgid() + ")";
//            sqlList.add(sql);
//            sql = " INSERT INTO TBL_BUGCRITERION (BUGCRIID, BUGCRILEVEL, BUGCRIDEFINE, BUGCRIRATION, BUGCRISTABILITY, STATUS, VERSION, ORGID) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'重大缺陷','是指一个或多个控制缺陷的组合，可能导致企业严重偏离控制目标。当存在任何一个或多个内部控制重大缺陷时，应当在内部控制评价报告中做出内部控制无效的结论。','财务指标(合并) 重要性水平\t资产总额      0.5%\t\t营业收入         0.5%\t\t税前利润         5%≥整体重要性水平\t\t已经对外正式披露并对公司定期报告披露造成负面影响；企业关键岗位人员流失严重；一次性死亡5人以上，或重伤10人以上；被媒体频频曝光负面新闻。','发现公司管理层存在的任何程度的舞弊；已经发现并报告给管理层的重大内部控制缺陷在经过合理的时间后，并未加以改正；内部控制环境无效；影响收益趋势的缺陷；影响关联交易总额超过股东批准的关联交易额度的缺陷；外部审计发现的重大错报不是由公司首先发现的；其他可能影响报表使用者正确判断的缺陷。缺乏民主决策程序；决策程序导致重大失误；违反国家法律法规并受到处罚；中高级管理人员和高级技术人员流失严重；媒体频现负面新闻，涉及面广；重要业务缺乏制',2,1," + organization.getOrgid() + ")";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object zyoneId = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TEMPLETE (TEMPLETEID, TEMPLETECODE, TEMPLETENAME, TEMPLETETYPE, TEMPLETEDESC, STAFFID, CREATEDATE, UPDATEDATE, UPDATESTAFFID, STATUS, TEMPTYPE, ORGID) VALUES (" + zyoneId + ", 'HBTZAZHGLSJZY','综合管理审计','综合管理审计','综合管理审计模板'," + user.getStaffid() + ",SYSDATE,NULL," + user.getStaffid() + ",1,1," + organization.getOrgid() + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjoneId = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TEMPLETE (TEMPLETEID, TEMPLETECODE, TEMPLETENAME, TEMPLETETYPE, TEMPLETEDESC, STAFFID, CREATEDATE, UPDATEDATE, UPDATESTAFFID, STATUS, TEMPTYPE, ORGID) VALUES (" + sjoneId + ", 'HBTZAZHGLSJ', '综合管理审计', '综合管理审计', '综合管理审计模板', " + user.getStaffid() + ",SYSDATE, NULL, " + user.getStaffid() + ",1,0," + organization.getOrgid() + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_NBSJ_TEMP_ORG(TEMPLATEID,ORGID) VALUES (" + zyoneId + "," + organization.getOrgid() + ")";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_NBSJ_TEMP_ORG(TEMPLATEID,ORGID) VALUES (" + sjoneId + "," + organization.getOrgid() + ")";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object mbjdoneId = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TARGETTYPE(TARGETID, TARGETNAME, TARGETDESC, PARENTID, CREATETIME, UPDATETIME, TEMPID, STATUS) VALUES (" + mbjdoneId + ",'综合管理审计', NULL, NULL, SYSDATE, SYSDATE," + zyoneId + ", 0)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object mbjdthreeId = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TARGETTYPE(TARGETID, TARGETNAME, TARGETDESC, PARENTID, CREATETIME, UPDATETIME, TEMPID, STATUS) VALUES (" + mbjdthreeId + ",'综合管理审计', NULL, NULL, SYSDATE, SYSDATE," + sjoneId + ", 0)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object jdoneId = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TARGETTYPE (TARGETID, TARGETNAME, TARGETDESC, PARENTID, CREATETIME, UPDATETIME, TEMPID, STATUS) VALUES (" + jdoneId + ",'会计及预算管理', '会计及预算管理'," + mbjdoneId + ", SYSDATE, SYSDATE, " + zyoneId + ", 0)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object jdfiveId = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TARGETTYPE (TARGETID, TARGETNAME, TARGETDESC, PARENTID, CREATETIME, UPDATETIME, TEMPID, STATUS) VALUES (" + jdfiveId + ",'人力资源管理', '人力资源管理'," + mbjdoneId + ", SYSDATE, SYSDATE, " + zyoneId + ", 0)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object jdsixId = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TARGETTYPE (TARGETID, TARGETNAME, TARGETDESC, PARENTID, CREATETIME, UPDATETIME, TEMPID, STATUS) VALUES (" + jdsixId + ",'固定资产管理', '固定资产管理'," + mbjdoneId + ", SYSDATE, SYSDATE, " + zyoneId + ", 0)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object jdthreeId = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TARGETTYPE (TARGETID, TARGETNAME, TARGETDESC, PARENTID, CREATETIME, UPDATETIME, TEMPID, STATUS) VALUES (" + jdthreeId + ",'人力资源管理', '人力资源管理'," + mbjdthreeId + ", SYSDATE, SYSDATE, " + sjoneId + ", 0)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object jdtenId = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TARGETTYPE (TARGETID, TARGETNAME, TARGETDESC, PARENTID, CREATETIME, UPDATETIME, TEMPID, STATUS) VALUES (" + jdtenId + ",'会计及预算管理', '会计及预算管理'," + mbjdthreeId + ", SYSDATE, SYSDATE, " + sjoneId + ", 0)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object jdeleId = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TARGETTYPE (TARGETID, TARGETNAME, TARGETDESC, PARENTID, CREATETIME, UPDATETIME, TEMPID, STATUS) VALUES (" + jdeleId + ",'资金管理', '资金管理'," + mbjdthreeId + ", SYSDATE, SYSDATE, " + sjoneId + ", 0)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (HIBERNATE_SEQUENCE.NEXTVAL," + jdtenId + ",'会计政策的制定和审批','合规性 操作性','询问公司财务部门管理人员关于公司会计制度制定及更新的相关操作流程； 通过日期追踪到最近一稿的公司现行会计制度，查看最新的相关法律法规是否被更新体现。 查阅公司高级管理层批准会计制度（制定及变更）的书面记录，检查是否符合公司的规定。','1.公司会计处理违反国家相关规定；会计处理不符合管理层的要求；提供错误的会计报告。'," + sjoneId + ", SYSDATE, SYSDATE, '', NULL,'公司会计政策的制定有相应的操作程序。')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (HIBERNATE_SEQUENCE.NEXTVAL," + jdtenId + ",'会计政策的制定和审批','合规性 操作性','询问公司财务人员定期业务培训方面的政策； 获得并检查与培训相关的书面材料，如培训记录，轮训计划，培训后所得的证书等。 选择财务人员询问是否参加了培训； 查看审计所属期间会计政策的变更公司是否相应作了追溯调整； 询问财务部门管理人员，在审计所属期间有无会计科目的变更，并查核原因以及公司管理层的签字。','1.公司会计处理违反国家相关规定；会计处理不符合管理层的要求；提供错误的会计报告。'," + sjoneId + ", SYSDATE, SYSDATE, '', NULL,'公司会计政策的制定有相应的操作程序。')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (HIBERNATE_SEQUENCE.NEXTVAL," + jdtenId + ",'会计政策的制定和审批','合规性 操作性','  询问公司财务部门管理人员关于公司会计制度制定及更新的相关操作流程； 通过日期追踪到最近一稿的公司现行会计制度，查看最新的相关法律法规是否被更新体现。 查阅公司高级管理层批准会计制度（制定及变更）的书面记录，检查是否符合公司的规定。 ','1.公司会计处理违反国家相关规定；会计处理不符合管理层的要求；提供错误的会计报告。'," + sjoneId + ", SYSDATE, SYSDATE, '', NULL,'根据国家现行会计准则、会计制度及相关规定定期更新公司现行会计制定。')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (HIBERNATE_SEQUENCE.NEXTVAL," + jdeleId + ",'贷款管理','操作性合规性','访谈编制资金预算的部门（财务部或资金部），了解编制资金预算或授信额度预算的流程，与公司的战略目标、经营计划的关系，以及预算的批准人等； 选取当年编制的资金预算，了解并核对其与经营计划的联系，查看有无董事会的批准。','融资预算和授信额度与公司战略规划不相吻合；潜在的舞弊行为。'," + sjoneId + ", SYSDATE, SYSDATE, '', NULL,'根据公司发展规划、经营计划估算公司对新增信贷资金的需求，制定融资预算申请或信用额度更新，上报董事会授权批准。')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (HIBERNATE_SEQUENCE.NEXTVAL," + jdeleId + ",'贷款管理','操作性合规性','对负责融资的部门（财务部或资金部）进行访谈，了解授信协议的签署过程，包括讨论与审批流程； 追踪相关的会议纪要，检查是否按规定进行讨论与审批。','融资预算和授信额度与公司战略规划不相吻合；潜在的舞弊行为。'," + sjoneId + ", SYSDATE, SYSDATE, '', NULL,'银行授信条款需经有关职能部门充分讨论，授信协议需得到公司管理层审批。')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (HIBERNATE_SEQUENCE.NEXTVAL," + jdoneId + ",'会计政策的制定和审批','合规性 操作性','询问公司财务部门管理人员关于公司会计制度制定及更新的相关操作流程； 通过日期追踪到最近一稿的公司现行会计制度，查看最新的相关法律法规是否被更新体现。 查阅公司高级管理层批准会计制度（制定及变更）的书面记录，检查是否符合公司的规定。','1.公司会计处理违反国家相关规定；会计处理不符合管理层的要求；提供错误的会计报告。'," + zyoneId + ", SYSDATE, SYSDATE, '', NULL,'公司会计政策的制定有相应的操作程序。')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (HIBERNATE_SEQUENCE.NEXTVAL," + jdoneId + ",'会计政策的制定和审批','合规性 操作性','询问公司财务人员定期业务培训方面的政策； 获得并检查与培训相关的书面材料，如培训记录，轮训计划，培训后所得的证书等。 选择财务人员询问是否参加了培训； 查看审计所属期间会计政策的变更公司是否相应作了追溯调整； 询问财务部门管理人员，在审计所属期间有无会计科目的变更，并查核原因以及公司管理层的签字。','1.公司会计处理违反国家相关规定；会计处理不符合管理层的要求；提供错误的会计报告。'," + zyoneId + ", SYSDATE, SYSDATE, '', NULL,'公司会计政策的制定有相应的操作程序。')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (HIBERNATE_SEQUENCE.NEXTVAL," + jdoneId + ",'会计政策的制定和审批','合规性 操作性','  询问公司财务部门管理人员关于公司会计制度制定及更新的相关操作流程； 通过日期追踪到最近一稿的公司现行会计制度，查看最新的相关法律法规是否被更新体现。 查阅公司高级管理层批准会计制度（制定及变更）的书面记录，检查是否符合公司的规定。 ','1.公司会计处理违反国家相关规定；会计处理不符合管理层的要求；提供错误的会计报告。'," + zyoneId + ", SYSDATE, SYSDATE, '', NULL,'根据国家现行会计准则、会计制度及相关规定定期更新公司现行会计制定。')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (HIBERNATE_SEQUENCE.NEXTVAL," + jdfiveId + ",'人力资源档案管理','操作性','对人力资源部相关人员进行访谈，了解有关人力资源资料管理的相关制度及规定； 获得书面的人力资源资料管理的相关制度及规定的范本； 观察人力资源资料的存放环境，评估是否能保证资料的安全； 获得人力资源档案范本，查看是否连续编号和使用； 使用判断抽样法，抽取    份人力资源资料样本，检查其内容是否与人力资源主文件一致。','不完整的信息。'," + zyoneId + ", SYSDATE, SYSDATE, '', NULL,'所有人力资源资料应及时分类归档，存放于安全的地方并由人力资源部设专人保管。')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (HIBERNATE_SEQUENCE.NEXTVAL," + jdfiveId + ",'人力资源档案管理','操作性','对人力资源部相关人员进行访谈，了解有关人力资源资料管理的相关制度及规定； 获得书面的人力资源资料管理的相关制度及规定的范本； 观察人力资源资料的存放环境，评估是否能保证资料的安全； 获得人力资源档案范本，查看是否连续编号和使用； 使用判断抽样法，抽取    份人力资源资料样本，检查其内容是否与人力资源主文件一致。','不完整的信息。'," + zyoneId + ", SYSDATE, SYSDATE, '', NULL,'所有人力资源资料应及时分类归档，存放于安全的地方并由人力资源部设专人保管。')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (HIBERNATE_SEQUENCE.NEXTVAL," + jdsixId + ",'固定资产的购置及入帐（供应商选择）','操作性合规性','获得书面的固定资产采购申请单范本； 使用判断抽样法，从归档的文件中抽取__张固定资产采购申请单，查看是否有申请部门负责人及相应管理人员的审批签字，且在财务部门备案； 寻找预算外的采购申请，查看其是否有高级管理层的签字确认。','未经授权的购买行为；无效和低效投资；公司资源的浪费。'," + zyoneId + ", SYSDATE, SYSDATE, '', NULL,'根据实际情况制订管理层的审批权限，以正式文件形式下发各部门。所有固定资产购买都必须填写书面的申请表格，并按审批权限报请相应管理层审批。')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (HIBERNATE_SEQUENCE.NEXTVAL," + jdsixId + ",'固定资产的购置及入帐（供应商选择）','操作性合规性','获得书面的固定资产采购申请单范本； 使用判断抽样法，从归档的文件中抽取__张固定资产采购申请单，查看是否有申请部门负责人及相应管理人员的审批签字，且在财务部门备案； 寻找预算外的采购申请，查看其是否有高级管理层的签字确认。','未经授权的购买行为；无效和低效投资；公司资源的浪费。'," + zyoneId + ", SYSDATE, SYSDATE, '', NULL,'根据实际情况制订管理层的审批权限，以正式文件形式下发各部门。所有固定资产购买都必须填写书面的申请表格，并按审批权限报请相应管理层审批。')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (HIBERNATE_SEQUENCE.NEXTVAL," + jdthreeId + ",'人力资源档案管理','操作性','对人力资源部相关人员进行访谈，了解有关人力资源资料管理的相关制度及规定； 获得书面的人力资源资料管理的相关制度及规定的范本； 观察人力资源资料的存放环境，评估是否能保证资料的安全； 获得人力资源档案范本，查看是否连续编号和使用； 使用判断抽样法，抽取    份人力资源资料样本，检查其内容是否与人力资源主文件一致。','不完整的信息。'," + sjoneId + ", SYSDATE, SYSDATE, '', NULL,'所有人力资源资料应及时分类归档，存放于安全的地方并由人力资源部设专人保管。')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (HIBERNATE_SEQUENCE.NEXTVAL," + jdthreeId + ",'人力资源档案管理','操作性','对人力资源部相关人员进行访谈，了解有关人力资源资料管理的相关制度及规定； 获得书面的人力资源资料管理的相关制度及规定的范本； 观察人力资源资料的存放环境，评估是否能保证资料的安全； 获得人力资源档案范本，查看是否连续编号和使用； 使用判断抽样法，抽取    份人力资源资料样本，检查其内容是否与人力资源主文件一致。','不完整的信息。'," + sjoneId + ", SYSDATE, SYSDATE, '', NULL,'所有人力资源资料应及时分类归档，存放于安全的地方并由人力资源部设专人保管。')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjplanid = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_AUDITPLAN ( PLANID, PLANCODE, PLANNAME, PALNYEAR, PLANTYPE, AUDITORGID, PALNCOST, STARTTIME, ENDTIME, PRINCIPALID, LEADERID, REMARKS, CREATESTAFFID, CREATETIME, UPDATETIMR, STATUS, OPINIONSTATUS, ISAUDITOR ) VALUES (" + sjplanid + ", 'ZHSJ-01', '综合管理审计计划', '2020', '综合管理审计'," + organization.getOrgid() + ", '20000', SYSDATE, SYSDATE," + user.getStaffid() + ", NULL, NULL," + user.getStaffid() + ", SYSDATE, NULL, '0', '5', '1' )";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_NBSJ_PLANORG (PLANID, ORGID) VALUES (" + sjplanid + "," + organization.getOrgid() + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjtypeid = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TYPE (TYPEID, AUDITTYPE, STATUS, VERSION, ORGID) VALUES (" + sjtypeid + ", '经济责任审计', '2', '1'," + organization.getOrgid() + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjtempletid = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TEMPLETE (TEMPLETEID, TEMPLETECODE, TEMPLETENAME, TEMPLETETYPE, TEMPLETEDESC, STAFFID, CREATEDATE, UPDATEDATE, UPDATESTAFFID, STATUS, TEMPTYPE, ORGID) VALUES (" + sjtempletid + ", 'HBTZAZHGLSJ', '综合管理审计', '综合管理审计', '综合管理审计模板', " + user.getStaffid() + ",SYSDATE, NULL, " + user.getStaffid() + ",1,2," + organization.getOrgid() + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "insert into TBL_NBSJ_TEMP_ORG (TEMPLATEID, orgid) values (" + sjtempletid + "," + organization.getOrgid() + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjxmmb_targetOne = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TARGETTYPE(TARGETID, TARGETNAME, TARGETDESC, PARENTID, CREATETIME, UPDATETIME, TEMPID, STATUS) VALUES (" + sjxmmb_targetOne + ",'综合管理审计', NULL, NULL, SYSDATE, SYSDATE," + sjtempletid + ", 0)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjxmmb_targetTwo = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TARGETTYPE (TARGETID, TARGETNAME, TARGETDESC, PARENTID, CREATETIME, UPDATETIME, TEMPID, STATUS) VALUES (" + sjxmmb_targetTwo + ",'会计及预算管理', '会计及预算管理'," + sjxmmb_targetOne + ", SYSDATE, SYSDATE, " + sjtempletid + ", 0)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjxmmb_targetTwo2 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TARGETTYPE (TARGETID, TARGETNAME, TARGETDESC, PARENTID, CREATETIME, UPDATETIME, TEMPID, STATUS) VALUES (" + sjxmmb_targetTwo2 + ",'资金管理', '资金管理'," + sjxmmb_targetOne + ", SYSDATE, SYSDATE, " + sjtempletid + ", 0)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjprogramOne = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (" + sjprogramOne + "," + sjxmmb_targetTwo + ",'会计政策的制定和审批','合规性 操作性','询问公司财务部门管理人员关于公司会计制度制定及更新的相关操作流程； 通过日期追踪到最近一稿的公司现行会计制度，查看最新的相关法律法规是否被更新体现。 查阅公司高级管理层批准会计制度（制定及变更）的书面记录，检查是否符合公司的规定。','1.公司会计处理违反国家相关规定；会计处理不符合管理层的要求；提供错误的会计报告。'," + sjtempletid + ", SYSDATE, SYSDATE, '', NULL,'公司会计政策的制定有相应的操作程序。')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjprogramTwo = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (" + sjprogramTwo + "," + sjxmmb_targetTwo + ",'会计政策的制定和审批','合规性 操作性','询问公司财务人员定期业务培训方面的政策； 获得并检查与培训相关的书面材料，如培训记录，轮训计划，培训后所得的证书等。 选择财务人员询问是否参加了培训； 查看审计所属期间会计政策的变更公司是否相应作了追溯调整； 询问财务部门管理人员，在审计所属期间有无会计科目的变更，并查核原因以及公司管理层的签字。','1.公司会计处理违反国家相关规定；会计处理不符合管理层的要求；提供错误的会计报告。'," + sjtempletid + ", SYSDATE, SYSDATE, '', NULL,'公司会计政策的制定有相应的操作程序。')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjprogramThree = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (" + sjprogramThree + "," + sjxmmb_targetTwo + ",'会计政策的制定和审批','合规性 操作性','  询问公司财务部门管理人员关于公司会计制度制定及更新的相关操作流程； 通过日期追踪到最近一稿的公司现行会计制度，查看最新的相关法律法规是否被更新体现。 查阅公司高级管理层批准会计制度（制定及变更）的书面记录，检查是否符合公司的规定。 ','1.公司会计处理违反国家相关规定；会计处理不符合管理层的要求；提供错误的会计报告。'," + sjtempletid + ", SYSDATE, SYSDATE, '', NULL,'根据国家现行会计准则、会计制度及相关规定定期更新公司现行会计制定。')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjprogramFour = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (" + sjprogramFour + "," + sjxmmb_targetTwo2 + ",'贷款管理','操作性合规性','访谈编制资金预算的部门（财务部或资金部），了解编制资金预算或授信额度预算的流程，与公司的战略目标、经营计划的关系，以及预算的批准人等； 选取当年编制的资金预算，了解并核对其与经营计划的联系，查看有无董事会的批准。','融资预算和授信额度与公司战略规划不相吻合；潜在的舞弊行为。'," + sjtempletid + ", SYSDATE, SYSDATE, '', NULL,'根据公司发展规划、经营计划估算公司对新增信贷资金的需求，制定融资预算申请或信用额度更新，上报董事会授权批准。')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjprogramFive = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID, TARGETID, BUSINESSTYPE, RISKSOURCE, SUDITPROCESS, RISKPOINT, TEMPID, UPDATETIME, CREATETIME, BIODATA, STATUS, CONTROL) VALUES (" + sjprogramFive + "," + sjxmmb_targetTwo2 + ",'贷款管理','操作性合规性','对负责融资的部门（财务部或资金部）进行访谈，了解授信协议的签署过程，包括讨论与审批流程； 追踪相关的会议纪要，检查是否按规定进行讨论与审批。','融资预算和授信额度与公司战略规划不相吻合；潜在的舞弊行为。'," + sjtempletid + ", SYSDATE, SYSDATE, '', NULL,'银行授信条款需经有关职能部门充分讨论，授信协议需得到公司管理层审批。')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjprojectid = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_PROJECT (PROJECTID, PRJOECTNAME, PLANYEAR, PROJECTSOURCE, STARTDATE, ENDDATE, PMID, TEMPID, COSTS, PURPOSE, SCOPES, PURSUANT, COMMENTS, UMPIREID, CONTROLID, AUDITTYPE, PROJECTCODE, STATUS, CREATETIME, UPDATETIME, CURRENTSTATRE, CREATESTAFFID, ASSIGBEDPMTIME, ASSIGBEDUMPETIME, ASSIGBEDCONTROLTIME, PLANID, AUDITORGID, TEMPZYID, UPDATESTATUS, ORGID, FINISHTIME, IMPLEMENTTIME, EXAMINETYPE, PRO_DESC, PRO_SJFS, AUDITSTAFFID, SJLX, SJZR, YQJCQK, EJFHR, TEMPLETEID, STAFFID, AUDITORG) VALUES (" + sjprojectid + ", '综合管理审计', '2020', '审计计划',SYSDATE,SYSDATE," + user.getStaffid() + "," + sjtempletid + ", '20000', '审计目标和范围', '审计内容和重点', '审计程序和方法', '对专家和外部审计结果的利用', NULL, NULL, '离任审计', 'SJXM-01', '2',SYSDATE, NULL, '1'," + user.getStaffid() + ",SYSDATE,SYSDATE,SYSDATE," + sjplanid + "," + organization.getOrgid() + "," + zyoneId + ", '1'," + organization.getOrgid() + ", NULL, NULL,4, '其他有关内容', '现场与非现场结合', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjteamid = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_PROJECTTEAM (TEAMID, TEAMNAME, CREATETIME, CREATEUSERID) VALUES (" + sjteamid + ", '审计小组',SYSDATE," + user.getStaffid() + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object teamstaffid = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_TEAMSTAFF (TEAMID, STAFFID, STAFFTYPE, ID) VALUES (" + sjteamid + "," + user.getStaffid() + ", '0'," + teamstaffid + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_NBSJ_PRO_TEAM (PROJECTID, TEAMID) VALUES (" + sjprojectid + "," + sjteamid + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object taskid1 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_AUTHORIZATION (AUTHID,AUTHSTAFFID,TEAMSTAFFID,AUTHTIME,PROJECTID,PROGRAMID) VALUES (" + taskid1 + "," + user.getStaffid() + "," + teamstaffid + ",SYSDATE," + sjprojectid + "," + sjprogramOne + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object operateid1 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_OPERATE (OPERATEID, AUTHID, FINISH, FINISHTIME, SHEETID) VALUES (" + operateid1 + "," + taskid1 + ",'0', NULL, NULL)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object taskid2 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_AUTHORIZATION (AUTHID,AUTHSTAFFID,TEAMSTAFFID,AUTHTIME,PROJECTID,PROGRAMID) VALUES (" + taskid2 + "," + user.getStaffid() + "," + teamstaffid + ",SYSDATE," + sjprojectid + "," + sjprogramTwo + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object operateid2 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_OPERATE (OPERATEID, AUTHID, FINISH, FINISHTIME, SHEETID) VALUES (" + operateid2 + "," + taskid2 + ",'0', NULL, NULL)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object taskid3 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_AUTHORIZATION (AUTHID,AUTHSTAFFID,TEAMSTAFFID,AUTHTIME,PROJECTID,PROGRAMID) VALUES (" + taskid3 + "," + user.getStaffid() + "," + teamstaffid + ",SYSDATE," + sjprojectid + "," + sjprogramThree + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object operateid3 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_OPERATE (OPERATEID, AUTHID, FINISH, FINISHTIME, SHEETID) VALUES (" + operateid3 + "," + taskid3 + ",'0', NULL, NULL)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object taskid4 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_AUTHORIZATION (AUTHID,AUTHSTAFFID,TEAMSTAFFID,AUTHTIME,PROJECTID,PROGRAMID) VALUES (" + taskid4 + "," + user.getStaffid() + "," + teamstaffid + ",SYSDATE," + sjprojectid + "," + sjprogramFour + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object operateid4 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_OPERATE (OPERATEID, AUTHID, FINISH, FINISHTIME, SHEETID) VALUES (" + operateid4 + "," + taskid4 + ",'0', NULL, NULL)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object taskid5 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_AUTHORIZATION (AUTHID,AUTHSTAFFID,TEAMSTAFFID,AUTHTIME,PROJECTID,PROGRAMID) VALUES (" + taskid5 + "," + user.getStaffid() + "," + teamstaffid + ",SYSDATE," + sjprojectid + "," + sjprogramFive + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object operateid5 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_OPERATE (OPERATEID, AUTHID, FINISH, FINISHTIME, SHEETID) VALUES (" + operateid5 + "," + taskid5 + ",'0', NULL, NULL)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object selectid = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_STAFFSELECT (SELECTID, PROJECTID, STAFFID) VALUES (" + selectid + "," + sjprojectid + "," + user.getStaffid() + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjtzsid = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_ADVICENOTE (ADVICEID, ADVICECOED, ADVICENAME, CREATRTIME, PROGECTID, CREATESTAFFID, STATUS, CONTENT, DES) VALUES (" + sjtzsid + ", 'SJTZS-01', '审计通知书',SYSDATE," + sjprojectid + "," + user.getStaffid() + ", '4','<p style=\"text-align:center\"><strong><span style=\";font-family:宋体;font-weight:bold;font-size:29px\"><span style=\"font-family:宋体\"></span></span></strong>××审计通知书<strong><span style=\";font-family:宋体;font-weight:bold;font-size:29px\"><span style=\"font-family:宋体\"></span></span></strong></p><p><span style=\";font-family:黑体;font-size:29px\"></span>&nbsp;&nbsp;&nbsp; 关于对×××单位×××部门进行审计的通知<br/>×××××公司：<br/>&nbsp;&nbsp;&nbsp; 根据我局本年度工作计划，兹订于20××年×月×日起，对你单位×××部门19××年全年的财务收支情况进行就地审计，预计审计1 5天。请做好有关资料的准备工作，对审计组的工作予以积极配合。<br/>&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;审计组长：×××&nbsp; <br/></p>', NULL)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjsheet1 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_SHEET (SHEETID, SHEETCODE, SHEETNAME, SHEETTARGET, AUDITORG, CREATESTAFF, CREATETIME, PROJECTID, UPDATETIME, RISKATTRBUTION, BUSINESSAFFILIATION, STATE, AUDITDESC, AUDITCOURSE, AUDITDISCOVERABLE, STATUS, APPROVER, RISKLEVEL, PROGRAMID, QUESTITLE, COMPTIME, TARGETNAME, BUSINESSTYPE, SUDITPROCESS, SJBWL, TARGETID) VALUES (" + sjsheet1 + ", 'SJDG-01', '审计底稿', '审计目的'," + organization.getOrgid() + "," + user.getStaffid() + ",SYSDATE," + sjprojectid + ", NULL, NULL, '综合管理审计', 4,'审计程序执行过程','审计意见及建议','审计发现', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL," + operateid1 + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjquesid1 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_QUESTION (QUESTIONID, SHEETID, STATUS, GROUPSTATUS, RECSTATUS) VALUES (" + sjquesid1 + "," + sjsheet1 + ",2,0,1)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "update TBL_NBSJ_OPERATE set sheetId=" + sjsheet1 + ",finish=1,finishTime=SYSDATE where operateId=" + operateid1;
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjqrs01 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_FACTBOOK (FACTID, PROJECTID, FACTCODE, FACTNAME, CREATETIME, UPDATETIME, STATUS, CREATESTAFFID, FACTSTAFFID, DESCRIBE, FHSTAFFID, EXAMEDORG, EXAMCONTENT) VALUES (" + sjqrs01 + "," + sjprojectid + ", 'SJQRS-01', NULL,SYSDATE,SYSDATE,5," + user.getStaffid() + "," + user.getStaffid() + ", '事实描述事实描述事实描述事实描述事实描述事实描述', " + user.getStaffid() + ", NULL, NULL)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjqrs_ques1 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_QUESTIONAFFIRM (FACTID, QUESTIONID, DESCRIBE, AFFIRMTIME, AFFIRMID) VALUES (" + sjqrs01 + "," + sjquesid1 + ", NULL, NULL," + sjqrs_ques1 + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjsheet2 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_SHEET (SHEETID, SHEETCODE, SHEETNAME, SHEETTARGET, AUDITORG, CREATESTAFF, CREATETIME, PROJECTID, UPDATETIME, RISKATTRBUTION, BUSINESSAFFILIATION, STATE, AUDITDESC, AUDITCOURSE, AUDITDISCOVERABLE, STATUS, APPROVER, RISKLEVEL, PROGRAMID, QUESTITLE, COMPTIME, TARGETNAME, BUSINESSTYPE, SUDITPROCESS, SJBWL, TARGETID) VALUES (" + sjsheet2 + ", 'SJDG-01', '审计底稿', '审计目的'," + organization.getOrgid() + "," + user.getStaffid() + ",SYSDATE," + sjprojectid + ", NULL, NULL, '综合管理审计', 4,'审计程序执行过程','审计意见及建议','审计发现', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL," + operateid2 + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjquesid2 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_QUESTION (QUESTIONID, SHEETID, STATUS, GROUPSTATUS, RECSTATUS) VALUES (" + sjquesid2 + "," + sjsheet2 + ",2,0,1)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "update TBL_NBSJ_OPERATE set sheetId=" + sjsheet2 + ",finish=1,finishTime=SYSDATE where operateId=" + operateid2;
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjqrs02 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_FACTBOOK (FACTID, PROJECTID, FACTCODE, FACTNAME, CREATETIME, UPDATETIME, STATUS, CREATESTAFFID, FACTSTAFFID, DESCRIBE, FHSTAFFID, EXAMEDORG, EXAMCONTENT) VALUES (" + sjqrs02 + "," + sjprojectid + ", 'SJQRS-01', NULL,SYSDATE,SYSDATE,5," + user.getStaffid() + "," + user.getStaffid() + ", '事实描述事实描述事实描述事实描述事实描述事实描述', " + user.getStaffid() + ", NULL, NULL)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjqrs_ques2 = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_QUESTIONAFFIRM (FACTID, QUESTIONID, DESCRIBE, AFFIRMTIME, AFFIRMID) VALUES (" + sjqrs02 + "," + sjquesid2 + ", NULL, NULL," + sjqrs_ques2 + ")";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjjcid = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_ENTERMEETING (ENTERID, ENTERCOED, ENTERNAME, CREATRTIME, PROGECTID, CREATESTAFFID, STATUS, CONTENT) VALUES (" + sjjcid + ", 'SJJC-01', '2020年审计启动会',SYSDATE," + sjprojectid + "," + user.getStaffid() + ", '0','<p style=\"text-align: center;\"><span style=\"font-size: 24px;\">2020年审计启动会纪要</span><br/></p><p style=\"text-indent: 2em;\" dir=\"ltr\"><span style=\"font-size: 20px;\">会议主题：</span></p><p style=\"text-indent: 2em;\" dir=\"ltr\"><span style=\"font-size: 20px;\">被审计单位参会人员：</span></p><p style=\"text-indent: 2em;\" dir=\"ltr\"><span style=\"font-size: 20px;\">审计组参会人员：</span></p><p style=\"text-indent: 2em;\" dir=\"ltr\"><span style=\"font-size: 20px;\">会议时间：</span></p><p style=\"text-indent: 2em;\" dir=\"ltr\"><span style=\"font-size: 20px;\">会议具体内容：</span></p><p style=\"text-indent: 2em;\" dir=\"ltr\"><span style=\"font-size: 20px;\">1、强调本次审计涉及到事项</span></p><p style=\"text-indent: 2em;\" dir=\"ltr\"><span style=\"font-size: 20px;\">2、本次审计涉及到的准备事项</span></p><p style=\"text-indent: 2em;\" dir=\"ltr\"><span style=\"font-size: 20px;\">3、本次审计的截至时间及相关事项。</span></p>')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjlcid = query.uniqueResult();
//            sql = "INSERT INTO TBL_NBSJ_LEAVEMEETING (LEAVEID, LEAVECOED, LEAVENAME, CREATRTIME, PROGECTID, CREATESTAFFID, STATUS, CONTENT) VALUES (" + sjlcid + ", 'SJLC-01', '2020年审计离场总结',SYSDATE," + sjprojectid + "," + user.getStaffid() + ", '0','<p style=\"text-align: center;\">2020年审计离场总结</p><p>参会人员</p><p>被审单位人员</p><p>审计人员</p><p>会议内容：</p><p><br/></p>')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object sjbgid = query.uniqueResult();
//            sql = "INSERT INTO TBL_REPORT (REPORTID, REPORTNAME, REPORTTIME, REPORTTYPE, REPORTMODE, REPORTER, REPORTDEPARTMENT, REPORTTEMPID, REPORTSTATUS, REPORTFILE, MEMO, TYPE, REPDESC, PROJECTID, ORGID, YJDES, FHSTAFFID, ZQYJSTAFFID, REPORTCODE) VALUES (" + sjbgid + ", 'SXBG-01',SYSDATE, '终稿', '定期报告','" + user.getRealname() + "','" + organization.getOrgname() + "', NULL,3, NULL, '审计报告', 'nbsj','<p style=\"margin-left:48px;text-align:justify;text-justify:inter-ideograph;vertical-align:bottom;line-height:150%\"><strong><span style=\";font-family:仿宋_GB2312;font-weight:bold;font-size:24px\">XX</span></strong><strong><span style=\";font-family:仿宋_GB2312;font-weight:bold;font-size:24px\"><span style=\"font-family:仿宋_GB2312\">审计</span></span></strong><strong><span style=\";font-family:&#39;Times New Roman&#39;;font-weight:bold;font-size:24px\"><span style=\"font-family:仿宋_GB2312\">报告</span></span></strong><span style=\"font-family:仿宋_GB2312;font-weight:bold;font-style:normal;font-size:21px\"><br/></span></p><p style=\"margin-left:48px;text-align:justify;text-justify:inter-ideograph;vertical-align:bottom;line-height:150%\"><span style=\"font-family:仿宋_GB2312;font-weight:bold;font-style:normal;font-size:21px\">一、&nbsp;</span><strong><span style=\";font-family:仿宋_GB2312;line-height:150%;font-weight:bold;font-size:21px\"><span style=\"font-family:仿宋_GB2312\">背景情况</span></span></strong><span style=\"font-family:仿宋_GB2312;font-weight:bold;font-style:normal;font-size:21px\"><br/></span></p><p style=\"margin-left:48px;text-align:justify;text-justify:inter-ideograph;vertical-align:bottom;line-height:150%\"><span style=\"font-family:仿宋_GB2312;font-weight:bold;font-style:normal;font-size:21px\">二、&nbsp;</span><strong><span style=\";font-family:仿宋_GB2312;line-height:150%;font-weight:bold;font-size:21px\"><span style=\"font-family:仿宋_GB2312\">审计结果</span></span></strong></p><p style=\";vertical-align:bottom;line-height:150%\"><strong><span style=\";font-family:仿宋_GB2312;line-height:150%;font-weight:bold;font-size:21px\"><span style=\"font-family:仿宋_GB2312\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 三、审计建议</span></span></strong></p><p style=\"text-indent:32px;text-align:justify;text-justify:inter-ideograph;line-height:150%\"><span style=\";font-family:&#39;Times New Roman&#39;;font-size:16px\">&nbsp;</span></p><p style=\"margin-left:392px;text-indent:74px;line-height:150%\"><strong><span style=\";font-family:仿宋_GB2312;line-height:150%;font-weight:bold;font-size:19px\">XX</span></strong><strong><span style=\";font-family:&#39;Times New Roman&#39;;line-height:150%;font-weight:bold;font-size:19px\"><span style=\"font-family:仿宋_GB2312\">有限公司</span></span></strong></p><p style=\"margin-left:420px;text-indent:18px;line-height:150%\"><strong><span style=\";font-family:仿宋_GB2312;line-height:150%;font-weight:bold;font-size:19px\">20</span></strong><strong><span style=\";font-family:仿宋_GB2312;line-height:150%;font-weight:bold;font-size:19px\">20</span></strong><strong><span style=\";font-family:&#39;Times New Roman&#39;;line-height:150%;font-weight:bold;font-size:19px\"><span style=\"font-family:仿宋_GB2312\">年</span></span></strong><strong><span style=\";font-family:仿宋_GB2312;line-height:150%;font-weight:bold;font-size:19px\">X</span></strong><strong><span style=\";font-family:&#39;Times New Roman&#39;;line-height:150%;font-weight:bold;font-size:19px\"><span style=\"font-family:仿宋_GB2312\">月</span></span></strong><strong><span style=\";font-family:仿宋_GB2312;line-height:150%;font-weight:bold;font-size:19px\">X</span></strong><strong><span style=\";font-family:&#39;Times New Roman&#39;;line-height:150%;font-weight:bold;font-size:19px\"><span style=\"font-family:仿宋_GB2312\">日</span></span></strong></p><p><br/></p>'," + sjprojectid + "," + organization.getOrgid() + ", NULL, NULL, NULL, NULL)";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object baseDataRootId = query.uniqueResult();
//            sql = "INSERT INTO TBL_BI_DATASOURCE (DSID, DSNAME, ORGID, STAFFID, CREATEDATE, FATHERID) VALUES ('" + baseDataRootId + "', '基础数据','" + organization.getOrgid() + "', NULL, SYSDATE, '-1')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object oneDataRootId = query.uniqueResult();
//            sql = "INSERT INTO TBL_BI_DATASOURCE (DSID, DSNAME, ORGID, STAFFID, CREATEDATE, FATHERID) VALUES ('" + oneDataRootId + "', '生产运营','" + organization.getOrgid() + "', NULL, SYSDATE, '" + baseDataRootId + "')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object DSID1 = query.uniqueResult();
//            sql = "INSERT INTO TBL_BI_DATASOURCE (DSID, DSNAME, SQLSTRING, ORGID,STAFFID, CREATEDATE, FORBIDDEN, ISLEAF, FATHERID, DSDES) VALUES (" + DSID1 + ",'实收资本贷方发生额','SELECT sum(BQMC) FROM \"$\".TBL_ACC_SUM WHERE AMONTH in(to_char(sysdate, ''MM'' ),to_char(add_months(sysdate,-1), ''mm'')) AND ACCID = ''6041'' AND AYEAR=''@''','" + organization.getOrgid() + "','" + user.getStaffid() + "',SYSDATE, '0', '1', '" + oneDataRootId + "', '实收资本贷方发生额')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object DSID2 = query.uniqueResult();
//            sql = "INSERT INTO TBL_BI_DATASOURCE (DSID, DSNAME, SQLSTRING, ORGID,STAFFID, CREATEDATE, FORBIDDEN, ISLEAF, FATHERID, DSDES) VALUES (" + DSID2 + ",'资本公积贷方发生额','SELECT sum(BQMC) FROM \"$\".TBL_ACC_SUM WHERE AMONTH in(to_char(sysdate, ''MM'' ),to_char(add_months(sysdate,-1), ''mm'')) AND ACCID = ''604101'' AND AYEAR=@','" + organization.getOrgid() + "','" + user.getStaffid() + "',SYSDATE, '0', '1', '" + oneDataRootId + "', '资本公积贷方发生额')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object indicatorid = query.uniqueResult();
//            sql = "INSERT INTO TBL_INDICATOR (INDICATORID, INDICATORCODE, INDICATORNAME, FORMULA, INDICATORDES, MEMO, ORGID, DEPARTMENTINCHARGE, INDICATORSTATUS, AUDITINGSTATUS, CREATEDATE, UNITTYPE, FORMULADES, INDCATID, RUNSTATUS, STAFFID, CONNECTIONSTRINGS, INDICATORDB, FORLUMACHS ) VALUES ( " + indicatorid + ", 'LCSYZCB', '留存收益资产比', '" + DSID1 + "/" + DSID2 + "', '留存收益资产比', NULL, '" + user.getTblOrganization().getOrgid() + "', NULL, '启用', NULL,SYSDATE, '比率', '实收资本贷方发生额/资本公积贷方发生额', NULL, '0', '" + user.getStaffid() + "', NULL, '0', '实收资本贷方发生额/资本公积贷方发生额' )";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_INDICATORTHRESHOLD (THRESHOLDID, SEQUENCENUMBER, TOLERANCE, THRESHOLDNAME, REGIONVALUE, PREWARNINGMETHOD, MEMO, INDICATORID, TOLERANCELOWER, TOLERANCEUPPER) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '2', '1', '中', '[100,50]', NULL, NULL, " + indicatorid + ", '100', '50')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_INDICATORTHRESHOLD (THRESHOLDID, SEQUENCENUMBER, TOLERANCE, THRESHOLDNAME, REGIONVALUE, PREWARNINGMETHOD, MEMO, INDICATORID, TOLERANCELOWER, TOLERANCEUPPER) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '1', '0', '低', '[50,-∞]', NULL, NULL, " + indicatorid + ", '50', '-∞')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_INDICATORTHRESHOLD (THRESHOLDID, SEQUENCENUMBER, TOLERANCE, THRESHOLDNAME, REGIONVALUE, PREWARNINGMETHOD, MEMO, INDICATORID, TOLERANCELOWER, TOLERANCEUPPER) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '3', '2', '高', '[+∞,100]', NULL, NULL, " + indicatorid + ", '+∞', '100')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object DSID3 = query.uniqueResult();
//            sql = "INSERT INTO TBL_BI_DATASOURCE (DSID, DSNAME, SQLSTRING, ORGID,STAFFID, CREATEDATE, FORBIDDEN, ISLEAF, FATHERID, DSDES) VALUES (" + DSID3 + ",'应收账款','SELECT tem.qmmd FROM ( SELECT NVL (qmmd, 0) qmmd FROM \"$\".TBL_ACC_SUM WHERE ACCID = ''1221'' AND AYEAR =@ AND amonth = LTRIM ( TO_CHAR ( TO_CHAR ( ADD_MONTHS (TRUNC(SYSDATE) ,- 1), ''mm'' ), ''99'' ))) tem','" + organization.getOrgid() + "','" + user.getStaffid() + "',SYSDATE, '0', '1', '" + oneDataRootId + "', '应收账款')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object DSID4 = query.uniqueResult();
//            sql = "INSERT INTO TBL_BI_DATASOURCE (DSID, DSNAME, SQLSTRING, ORGID,STAFFID, CREATEDATE, FORBIDDEN, ISLEAF, FATHERID, DSDES) VALUES (" + DSID4 + ",'其他应收款','SELECT sum(BQMC) FROM \"$\".TBL_ACC_SUM WHERE AMONTH in(to_char(sysdate, ''MM'' ),to_char(add_months(sysdate,-1), ''mm'')) AND ACCID = ''604101'' AND AYEAR=@','" + organization.getOrgid() + "','" + user.getStaffid() + "',SYSDATE, '0', '1', '" + oneDataRootId + "', '其他应收款')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object indicatorid2 = query.uniqueResult();
//            sql = "INSERT INTO TBL_INDICATOR (INDICATORID, INDICATORCODE, INDICATORNAME, FORMULA, INDICATORDES, MEMO, ORGID, DEPARTMENTINCHARGE, INDICATORSTATUS, AUDITINGSTATUS, CREATEDATE, UNITTYPE, FORMULADES, INDCATID, RUNSTATUS, STAFFID, CONNECTIONSTRINGS, INDICATORDB, FORLUMACHS ) VALUES ( " + indicatorid2 + ", 'ZCZZL', '资产周转率', '" + DSID3 + "/" + DSID4 + "', '资产周转率', NULL, '" + user.getTblOrganization().getOrgid() + "', NULL, '启用', NULL,SYSDATE, '比率', '应收账款/其他应收款', NULL, '0', '" + user.getStaffid() + "', NULL, '0', '应收账款/其他应收款' )";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_INDICATORTHRESHOLD (THRESHOLDID, SEQUENCENUMBER, TOLERANCE, THRESHOLDNAME, REGIONVALUE, PREWARNINGMETHOD, MEMO, INDICATORID, TOLERANCELOWER, TOLERANCEUPPER) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '2', '1', '中', '[80,40]', NULL, NULL, " + indicatorid2 + ", '80', '40')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_INDICATORTHRESHOLD (THRESHOLDID, SEQUENCENUMBER, TOLERANCE, THRESHOLDNAME, REGIONVALUE, PREWARNINGMETHOD, MEMO, INDICATORID, TOLERANCELOWER, TOLERANCEUPPER) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '1', '0', '低', '[40,-∞]', NULL, NULL, " + indicatorid2 + ", '40', '-∞')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_INDICATORTHRESHOLD (THRESHOLDID, SEQUENCENUMBER, TOLERANCE, THRESHOLDNAME, REGIONVALUE, PREWARNINGMETHOD, MEMO, INDICATORID, TOLERANCELOWER, TOLERANCEUPPER) VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '3', '2', '高', '[+∞,80]', NULL, NULL, " + indicatorid2 + ", '+∞', '80')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object GZID1 = query.uniqueResult();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object GZID2 = query.uniqueResult();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object GZID4 = query.uniqueResult();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object GZID5 = query.uniqueResult();
//            sql = "INSERT INTO TBL_MONITOR_RULE (RULEID, RULENAME, RULEDESCRIPTION, RULECODE, RULEPRIORITY, RISKLEVEL, SATUS, REGEXP, RULESQL, MEMO, INRULEDB, TIPS, ORGID, CONNECTIONSTRINGS, RUNSTATUS, STAFFID, REPORTSQL) VALUES (" + GZID1 + ", '查询凭证明细中借方金额等于9000的记录', '查询凭证明细中借方金额等于9000的记录', 'CJTZ01', '高', '高', '启用', NULL,'select DAT 记账日期,PZH 凭证号,LINETEXT 摘要,ACCID 科目代码,ACCNAME1 科目名称,MD 借方余额,MC 贷方余额 ,CASE WHEN MD >= 8000 THEN ''红'' WHEN MD > 7000 THEN ''黄'' WHEN MD <= 9000 THEN ''绿'' else '''' END 红绿灯预警 from ( select b.PZH,c.ACCID,c.ACCNAME1,b.LINETEXT,nvl(b.MD,0) MD,nvl(b.MC,0) MC,substr(b.GLH,0,10) DAT from ( SELECT PZH,LINETEXT,MD,MC,GLH,ACCID from TBL_ACC_BSEG@ ) b left join TBL_ACCOUNT c on b.ACCID = c.ACCID where 1=1 AND c.ACCNAME1 like ''%库存%'' order by MD DESC) WHERE MC<8000','查询凭证明细中借方金额等于6000的记录',0,null,'" + user.getTblOrganization().getOrgid() + "','" + uuid + "',0," + user.getStaffid() + ",null)";
//            sqlList.add(sql);
//            sql = "create table HBFKCWZT2018.ZNJK_GZ_" + GZID1 + " (\"ID\" NUMBER NOT NULL,\"EXECTIME\"  VARCHAR2(500 BYTE),\"记账日期\" VARCHAR2(4000 BYTE) ,\"凭证号\" VARCHAR2(4000 BYTE) ,\"摘要\" VARCHAR2(4000 BYTE) ,\"科目代码\" VARCHAR2(4000 BYTE) ,\"科目名称\" VARCHAR2(4000 BYTE) ,\"借方余额\" VARCHAR2(4000 BYTE) ,\"贷方余额\" VARCHAR2(4000 BYTE) ,\"红绿灯预警\" VARCHAR2(4000 BYTE) )";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_MONITOR_RULE (RULEID, RULENAME, RULEDESCRIPTION, RULECODE, RULEPRIORITY, RISKLEVEL, SATUS, REGEXP, RULESQL, MEMO, INRULEDB, TIPS, ORGID, CONNECTIONSTRINGS, RUNSTATUS, STAFFID, REPORTSQL) VALUES (" + GZID2 + ",'劳务费审计风险','该指标反映联网实时审计核查数据校验条件','CJTZ02','高', '高', '启用',NULL,'select substr(GLH,0,10) 记账日期,PZH 凭证号,LINETEXT 摘要,ACCID 科目代码,ACCNAME1 科目名称,MD 借方余额,MC 贷方余额 from ( select b.PZH,c.ACCID,c.ACCNAME1,b.LINETEXT,nvl(b.MD,0) MD,nvl(b.MC,0) MC,substr(b.GLH,0,10) GLH,b.AYEAR from ( SELECT PZH,LINETEXT,MD,MC,GLH,ACCID,AYEAR from TBL_ACC_BSEG@ ) b left join TBL_ACCOUNT c on b.ACCID = c.ACCID AND b.AYEAR = c.AYEAR where 1=1 AND c.ACCNAME1 like ''%聘请中介机构费%'')b where MD >= 20000 AND linetext != ''结转损益'' order by MD DESC',NULL,0,null,'" + user.getTblOrganization().getOrgid() + "','" + uuid + "',0," + user.getStaffid() + ",null)";
//            sqlList.add(sql);
//            sql = " create table HBFKCWZT2018.ZNJK_GZ_" + GZID2 + " (\"ID\" NUMBER NOT NULL,\"EXECTIME\"  VARCHAR2(500 BYTE),\"记账日期\" VARCHAR2(4000 BYTE) ,\"凭证号\" VARCHAR2(4000 BYTE) ,\"摘要\" VARCHAR2(4000 BYTE) ,\"科目代码\" VARCHAR2(4000 BYTE) ,\"科目名称\" VARCHAR2(4000 BYTE) ,\"借方余额\" VARCHAR2(4000 BYTE) ,\"贷方余额\" VARCHAR2(4000 BYTE) )";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_MONITOR_RULE (RULEID, RULENAME, RULEDESCRIPTION, RULECODE, RULEPRIORITY, RISKLEVEL, SATUS, REGEXP, RULESQL, MEMO, INRULEDB, TIPS, ORGID, CONNECTIONSTRINGS, RUNSTATUS, STAFFID, REPORTSQL) VALUES (" + GZID4 + ",'现金支付审计风险','该指标反映联网实时审计核查数据校验条件','CJTZ06','高', '高', '启用',NULL,'SELECT SUBSTR (b.glh, 0, 10) 记账日期, b.pzh 凭证号, b.linetext 摘要, c.accid 科目代码, c.accname1 科目名称, NVL (b.md, 0) 借方余额, NVL (b.mc, 0) 贷方余额 FROM ( SELECT * FROM TBL_ACC_BSEG2018 ) b INNER JOIN ( SELECT tem.glh, tem.pzh, TEM.AYEAR, COUNT (*) FROM ( SELECT DISTINCT TE.glh, TE.pzh, TE.AYEAR, SUBSTR (TE.accid, 0, 4) FROM ( SELECT * FROM TBL_ACC_BSEG2018 ) te WHERE ( TE.accid LIKE ''%'' OR TE.accid LIKE ''2211%'' )) tem GROUP BY tem.glh, tem.pzh, TEM.AYEAR HAVING COUNT (*) > 1 ) w ON b.pzh = w.pzh AND w.glh = b.glh AND w.AYEAR = b.AYEAR LEFT JOIN TBL_ACCOUNT c ON b.accid = c.accid AND b.AYEAR = c.AYEAR WHERE ( b.ACCID LIKE ''1001%'' OR b.ACCID LIKE ''2211%'' ) AND b.mc != 0 ORDER BY SUBSTR (b.glh, 0, 10), b.pzh, b.glh',NULL,0,null,'" + user.getTblOrganization().getOrgid() + "','" + uuid + "',0," + user.getStaffid() + ",null)";
//            sqlList.add(sql);
//            sql = "CREATE TABLE HBFKCWZT2018.ZNJK_GZ_" + GZID4 + " ( \"ID\" NUMBER NOT NULL, \"EXECTIME\" VARCHAR2 (500 BYTE), \"记账日期\" VARCHAR2 (4000 BYTE), \"凭证号\" VARCHAR2 (4000 BYTE), \"摘要\" VARCHAR2 (4000 BYTE), \"科目代码\" VARCHAR2 (4000 BYTE), \"科目名称\" VARCHAR2 (4000 BYTE), \"借方余额\" VARCHAR2 (4000 BYTE), \"贷方余额\" VARCHAR2 (4000 BYTE))";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_MONITOR_RULE (RULEID, RULENAME, RULEDESCRIPTION, RULECODE, RULEPRIORITY, RISKLEVEL, SATUS, REGEXP, RULESQL, MEMO, INRULEDB, TIPS, ORGID, CONNECTIONSTRINGS, RUNSTATUS, STAFFID, REPORTSQL) VALUES (" + GZID5 + ",'期末科目校验','期末结账时对临时性科目进行校验','TJTB08','高', '高', '启用',NULL,'SELECT ''应付职工薪酬（除工会经费、职教费外）'' 摘要, TO_CHAR ((TE.num1 - TE.num2)) AS 余额 FROM ( SELECT ( SELECT SUM (QMMC) FROM TBL_ACC_SUM WHERE AYEAR = @ AND ACCID = ''2211'' AND AMONTH IN ( TO_CHAR (SYSDATE, ''MM''), TO_CHAR ( ADD_MONTHS (SYSDATE ,- 1), ''mm'' ))) AS num1, ( SELECT SUM (QMMC) FROM TBL_ACC_SUM WHERE AYEAR = @ AND ACCID IN (22111108, 22111109) AND AMONTH IN ( TO_CHAR (SYSDATE, ''MM''), TO_CHAR ( ADD_MONTHS (SYSDATE ,- 1), ''mm'' ))) AS num2 FROM dual ) te WHERE (TE.num1 - TE.num2) != 0 UNION ALL SELECT ''制造费用期末余额'' name1, TO_CHAR (te.qmmd) 余额 FROM ( SELECT CASE WHEN COUNT (*) = 0 THEN 0 ELSE SUM (NVL(qmmd, 0)) END AS qmmd FROM TBL_ACC_SUM WHERE AYEAR = @ AND ACCID IN (5101) AND AMONTH IN ( TO_CHAR (SYSDATE, ''MM''), TO_CHAR ( ADD_MONTHS (SYSDATE ,- 1), ''mm'' ))) te WHERE TE.QMMD != 0 UNION ALL SELECT ''固定资产清理期末余额'' name1, TO_CHAR (te.qmmc) 余额 FROM ( SELECT CASE WHEN COUNT (*) = 0 THEN 0 ELSE SUM (NVL(qmmc, 0)) END AS qmmc FROM TBL_ACC_SUM WHERE AYEAR = @ AND ACCID IN (1606) AND AMONTH IN ( TO_CHAR (SYSDATE, ''MM''), TO_CHAR ( ADD_MONTHS (SYSDATE ,- 1), ''mm'' ))) te WHERE te.QMMC != 0 UNION ALL SELECT TE.NAME1, TE.BQYE 余额 FROM ( SELECT ''应付职工薪酬--'' || c.accname1 AS name1, ''贷方：'' || s.bqmc || ''借方：'' || s.bqmd AS bqye FROM TBL_ACC_SUM s LEFT JOIN TBL_ACCOUNT c ON s.accid = c.ACCID AND s.AYEAR = c.AYEAR WHERE s.AYEAR = @ AND s.ACCID IN (22111108, 22111109) AND s.AMONTH IN ( TO_CHAR (SYSDATE, ''MM''), TO_CHAR ( ADD_MONTHS (SYSDATE ,- 1), ''mm'' )) AND s.BQMC = 0 ) te',NULL,0,null,'" + user.getTblOrganization().getOrgid() + "','" + uuid + "',0," + user.getStaffid() + ",null)";
//            sqlList.add(sql);
//            sql = "create table HBFKCWZT2018.ZNJK_GZ_" + GZID5 + " (\"ID\" NUMBER NOT NULL,\"EXECTIME\"  VARCHAR2(500 BYTE),\"摘要\" VARCHAR2(4000 BYTE) ,\"余额\" VARCHAR2(4000 BYTE) )";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object csmbIdone = query.uniqueResult();
//            sql = "INSERT INTO TBL_COM_EXT_TESTTEMPLE (TESTTEMID,TEMPLENAME,TEMPLENUMBER,CREATEDATETIME,TEMPLEDESC,TBLCOMANY,STAFFID,SOURCE) VALUES (" + csmbIdone + ",'测试模板名称01','CSMBBH02',SYSDATE,'测试模板说明02','" + organization.getOrgid() + "','" + user.getStaffid() + "','自建')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object csjdIdrootone = query.uniqueResult();
//            sql = "INSERT INTO TBL_COM_EXT_TESTTEMPL_TYPE (TYPEID,TYPENAME,PARENTID,TESTTEMPLETAID,CREATETIME,TYPECODE) VALUES (" + csjdIdrootone + ",'组织架构',NULL," + csmbIdone + ",SYSDATE,'ZZJG')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object csjdIdroottwo = query.uniqueResult();
//            sql = "INSERT INTO TBL_COM_EXT_TESTTEMPL_TYPE (TYPEID,TYPENAME,PARENTID,TESTTEMPLETAID,CREATETIME,TYPECODE) VALUES (" + csjdIdroottwo + ",'财务报告',NULL," + csmbIdone + ",SYSDATE,'CWBG')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object csjdIdone = query.uniqueResult();
//            sql = "INSERT INTO TBL_COM_EXT_TESTTEMPL_TYPE (TYPEID,TYPENAME,PARENTID,TESTTEMPLETAID,CREATETIME,TYPECODE) VALUES (" + csjdIdone + ",'内部组织机构及部门职责'," + csjdIdrootone + "," + csmbIdone + ",SYSDATE,'NBZZJGJBMZZ')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object csjdIdtwo = query.uniqueResult();
//            sql = "INSERT INTO TBL_COM_EXT_TESTTEMPL_TYPE (TYPEID,TYPENAME,PARENTID,TESTTEMPLETAID,CREATETIME,TYPECODE) VALUES (" + csjdIdtwo + ",'董事会工作流程'," + csjdIdrootone + "," + csmbIdone + ",SYSDATE,'DSHGZLC')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object csjdIdthree = query.uniqueResult();
//            sql = "INSERT INTO TBL_COM_EXT_TESTTEMPL_TYPE (TYPEID,TYPENAME,PARENTID,TESTTEMPLETAID,CREATETIME,TYPECODE) VALUES (" + csjdIdthree + ",'日常会计处理和期末关账'," + csjdIdroottwo + "," + csmbIdone + ",SYSDATE,'RCKJCLHQMGZ')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object csjdIdfour = query.uniqueResult();
//            sql = "INSERT INTO TBL_COM_EXT_TESTTEMPL_TYPE (TYPEID,TYPENAME,PARENTID,TESTTEMPLETAID,CREATETIME,TYPECODE) VALUES (" + csjdIdfour + ",'财务报告管理'," + csjdIdroottwo + "," + csmbIdone + ",SYSDATE,'CWBGGL')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_COM_EXT_TESTELEMENT (ELEMENTID,RISKTYPE,CHECKMETHOD,CONTROLMETHOD,CONTROLTYPE,CONTROLREQ,MATERIAL,TYPEID,CREATETIME,TEMPLID,ELEMENTCODE,BUSINESSDESC,CONTROLTARGET,CONTROLMEASURES) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'1.组织架构存在缺陷，导致组织的运行效率低下；2.组织架构调整方案不符合企业自身特点及实际情况，可能会影响企业效率；3.新的组织架构运行得不到内部部门的积极支持，可能影响企业经营目标的实现。','1.了解现有组织架构是否应有利于企业战略目标的实现，是否与企业内部主导业务流程相符、是否满足企业内部高校管理的要求、是否广泛征得相关人员如董事、监事、高级管理人员和其他员工的意见；2.《组织架构调整方案》的审批是否符合规定程序和要求;3.新的组织架构文件资料是否按企业统一规范编写、审批、发布','自动','预防性','随时','1.公司章程；2.组织结构调整管理办法。'," + csjdIdone + ",SYSDATE," + csmbIdone + ",'226761','组织架构的评估与调整流程','组织机构能够适应公司发展，运行效率较高，确保企业经营目标的实现。','1.当前组织构架能够满足企业运行需要。2.组织机构调整有会议纪要')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_COM_EXT_TESTELEMENT (ELEMENTID,RISKTYPE,CHECKMETHOD,CONTROLMETHOD,CONTROLTYPE,CONTROLREQ,MATERIAL,TYPEID,CREATETIME,TEMPLID,ELEMENTCODE,BUSINESSDESC,CONTROLTARGET,CONTROLMEASURES) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'1.员工不了解和掌握公司组织结构的情况，不能正确履行职责职责；2.部门职责说明不明确，影响企业经营活动的顺利开展；3.岗位说明不准确，影响企业经营活动的顺利开展。','1.根据组织架构的设计规范，对现有治理结构和内部机构设置进行全面梳理，确保本企业治理结构、内部机构设置和运行机制等符合现代企业制度要求；2.查看部门职责是否说明清楚，有没有职能交叉的现象；3.企业有没有梳理内部机构设置，是否重点关注内部机构设置的合理性和运行的高效性，内部机构设置和运行中是否存在职能交叉、缺失或运行效率低下，若出现是否进行及时解决。','自动','预防性','随时','1.《组织结构图》；2.《部门及职位职责说明书》人力资源部提供。'," + csjdIdone + ",SYSDATE," + csmbIdone + ",'226760','组织机构设置与权限体系的分配','组织机构设置要符合公司运营流程，明确部门职责，并及时根据需要进行调整，确保公司经营活动顺利开展。','1.目前公司实行事业部制，已经建立完整的组织机构，并且会根据公司运营实际情况及时进行调整。 2.“四定”工作明确了各部门职责及各岗位职责。')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_COM_EXT_TESTELEMENT (ELEMENTID,RISKTYPE,CHECKMETHOD,CONTROLMETHOD,CONTROLTYPE,CONTROLREQ,MATERIAL,TYPEID,CREATETIME,TEMPLID,ELEMENTCODE,BUSINESSDESC,CONTROLTARGET,CONTROLMEASURES) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'董事会决议未得到及时有效地执行，使董事会议案得不到解决。','1.抽取董事会会议议案，并检查相应的执行记录。','自动','预防性','随时','1.董事会会议议案；2.董事会会议议案执行记录。'," + csjdIdone + ",SYSDATE," + csmbIdone + ",'226765','董事会决议的执行与监督情况','确保董事会会议决议得到切实有效的贯彻执行，维护公司利益和股东权益','董事长在拟定提案前，应当视需要主动征求总经理和其他高级管理人员的意见。')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_COM_EXT_TESTELEMENT (ELEMENTID,RISKTYPE,CHECKMETHOD,CONTROLMETHOD,CONTROLTYPE,CONTROLREQ,MATERIAL,TYPEID,CREATETIME,TEMPLID,ELEMENTCODE,BUSINESSDESC,CONTROLTARGET,CONTROLMEASURES) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'1.会计处理出现漏洞，影响企业财务信息的准确性，可能导致企业财务报告错报、漏报或信息披露错误；2.虚列或隐瞒收入，推迟或提前确认收入；3.随意改变费用、成本的确认标准或计量方法，虚列、多列、不列或者少列费用、成本；4.结账的时间、程序不符合相关规定；5.关账后又随意打开已关闭的会计期间。','1.了解企业关账日、关账人员及关账前应确认的事项；2.检查相关文件，确认企业是否在对账无误时方可关账，发现账面错误时是否及时调整并审核；3.关账后需调整分录时，是否经过严格审核。','自动','预防性','随时','1.试算平衡表；2.账务调整申请；3.试算平衡表。'," + csjdIdone + ",SYSDATE," + csmbIdone + ",'227133','期末关账程序的运行与审核','按照财务程序进行，保证入账及时准确。','按照手续齐全单据准确编制会计凭证，做到日清月结；每月核对有关明细科目的余额，做到账证、账实、账账、账表相符')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_COM_EXT_TESTELEMENT (ELEMENTID,RISKTYPE,CHECKMETHOD,CONTROLMETHOD,CONTROLTYPE,CONTROLREQ,MATERIAL,TYPEID,CREATETIME,TEMPLID,ELEMENTCODE,BUSINESSDESC,CONTROLTARGET,CONTROLMEASURES) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'董事会决议未得到及时有效地执行，使董事会议案得不到解决。','1.抽取董事会会议议案，并检查相应的执行记录。','自动','预防性','随时','1.董事会会议议案；2.董事会会议议案执行记录。'," + csjdIdtwo + ",SYSDATE," + csmbIdone + ",'226766','确保董事会会议决议得到切实有效的贯彻执行，维护公司利益和股东权益','董事长在拟定提案前，应当视需要主动征求总经理和其他高级管理人员的意见。','')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_COM_EXT_TESTELEMENT (ELEMENTID,RISKTYPE,CHECKMETHOD,CONTROLMETHOD,CONTROLTYPE,CONTROLREQ,MATERIAL,TYPEID,CREATETIME,TEMPLID,ELEMENTCODE,BUSINESSDESC,CONTROLTARGET,CONTROLMEASURES) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'2.档案的保管不符合国家法律、法规和公司的要求，导致查找不便，追责困难。','1.查阅公司章程，获取董事会会议议题和董事会会议通知等资料，了解董事会的会议的召集是否经过规定程序和审批；','自动','预防性','随时','董事会会议议案的确定与审批'," + csjdIdtwo + ",SYSDATE," + csmbIdone + ",'226767','确保董事会运作及重大信息上报符合相关规定及程序。','在发出召开董事会会议的通知前，董事会办公室应当充分征求各董事的意见，初步形成会议提案后交董事长拟定。董事长在拟定提案前，应当视需要主动征求总经理和其他高级管理人员的意见。','')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_COM_EXT_TESTELEMENT (ELEMENTID,RISKTYPE,CHECKMETHOD,CONTROLMETHOD,CONTROLTYPE,CONTROLREQ,MATERIAL,TYPEID,CREATETIME,TEMPLID,ELEMENTCODE,BUSINESSDESC,CONTROLTARGET,CONTROLMEASURES) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'1.会计处理出现漏洞，影响企业财务信息的准确性，可能导致企业财务报告错报、漏报或信息披露错误；','1.了解企业关账日、关账人员及关账前应确认的事项；','自动','预防性','随时','会计凭证的录入和审核'," + csjdIdthree + ",SYSDATE," + csmbIdone + ",'226768','及时准确，符合业务实际，符合财务制度。','按照财务制度进行科目设置','')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_COM_EXT_TESTELEMENT (ELEMENTID,RISKTYPE,CHECKMETHOD,CONTROLMETHOD,CONTROLTYPE,CONTROLREQ,MATERIAL,TYPEID,CREATETIME,TEMPLID,ELEMENTCODE,BUSINESSDESC,CONTROLTARGET,CONTROLMEASURES) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'减值准备计提方法随意变更，资产损失审查不严、审批手续不全，导致财务安全风险。','会计政策的制定和科目的维护','自动','预防性','随时','会计凭证的录入和审核'," + csjdIdthree + ",SYSDATE," + csmbIdone + ",'226769','符合法律法规，按照业务实际确定科目','会计凭证打印后审核','')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_COM_EXT_TESTELEMENT (ELEMENTID,RISKTYPE,CHECKMETHOD,CONTROLMETHOD,CONTROLTYPE,CONTROLREQ,MATERIAL,TYPEID,CREATETIME,TEMPLID,ELEMENTCODE,BUSINESSDESC,CONTROLTARGET,CONTROLMEASURES) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'未对财务报告进行整理归档，保存不善。','1.访谈相关负责人，了解财务报告保管归档是否符合制度规定；','自动','预防性','随时','对外提供财务报告的归档'," + csjdIdfour + ",SYSDATE," + csmbIdone + ",'226770','对财务报告进行整理归档，妥善保管。','财务归档','')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_COM_EXT_TESTELEMENT (ELEMENTID,RISKTYPE,CHECKMETHOD,CONTROLMETHOD,CONTROLTYPE,CONTROLREQ,MATERIAL,TYPEID,CREATETIME,TEMPLID,ELEMENTCODE,BUSINESSDESC,CONTROLTARGET,CONTROLMEASURES) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'在财务报告对外提供前未按规定程序进行审核，对内容的真实性、完整性以及格式的合规性等审核不充分。','获取财务报告对外提供相关审批资料，检查是否经过合理、适当审批。','自动','预防性','随时','财务报告对外提供前的审核'," + csjdIdfour + ",SYSDATE," + csmbIdone + ",'226771','按规定程序审核后披露','总经理签字后对外提供','')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object bbId = query.uniqueResult();
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID,PAGENAME,UNIT,CREATER,MEMO2,PAGECODE,CREATEDATE,RIGHTID,TREEID,\"TYPE\",\"SORT\") VALUES (" + bbId + ",'财务主题分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','FRCW',SYSDATE,'194','" + user.getStaffid() + "','0','1')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID, PAGENAME,UNIT,CREATER,MEMO2, PAGECODE,CREATEDATE, PAGEBODY, RIGHTID, TREEID, RQURL, \"TYPE\", \"SORT\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'财务总览'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','CWZL',SYSDATE," + bbId + ",'194','" + user.getStaffid() + "','http://192.0.2.200:9001/browse/0/A98CB6AD58DC4633906BBBC477EDC118','5', '1')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID, PAGENAME,UNIT,CREATER,MEMO2, PAGECODE,CREATEDATE, PAGEBODY, RIGHTID, TREEID, RQURL, \"TYPE\", \"SORT\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'利润分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','LRFX',SYSDATE," + bbId + ",'194','" + user.getStaffid() + "','http://192.0.2.200:9001/browse/0/FAAC722E742449DDBB7DBCCCB9571AC9','5', '2')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID, PAGENAME,UNIT,CREATER,MEMO2, PAGECODE,CREATEDATE, PAGEBODY, RIGHTID, TREEID, RQURL, \"TYPE\", \"SORT\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'收入分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','SRFX',SYSDATE," + bbId + ",'194','" + user.getStaffid() + "','http://192.0.2.200:9001/browse/0/04C4949FA0114E309EB4F6EA22237578','5', '3')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID, PAGENAME,UNIT,CREATER,MEMO2, PAGECODE,CREATEDATE, PAGEBODY, RIGHTID, TREEID, RQURL, \"TYPE\", \"SORT\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'成本费用分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','CBFYFX',SYSDATE," + bbId + ",'194','" + user.getStaffid() + "','http://192.0.2.200:9001/browse/0/743953662A4E4CFA95F843F721037C87','5', '4')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID, PAGENAME,UNIT,CREATER,MEMO2, PAGECODE,CREATEDATE, PAGEBODY, RIGHTID, TREEID, RQURL, \"TYPE\", \"SORT\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'资产负债分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','ZCFZFX',SYSDATE," + bbId + ",'194','" + user.getStaffid() + "','http://192.0.2.200:9001/browse/0/507D37BAF753422BAB713E2FCF4FC1CA','5', '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID, PAGENAME,UNIT,CREATER,MEMO2, PAGECODE,CREATEDATE, PAGEBODY, RIGHTID, TREEID, RQURL, \"TYPE\", \"SORT\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'现金流量分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','XJLLFX',SYSDATE," + bbId + ",'194','" + user.getStaffid() + "','http://192.0.2.200:9001/browse/0/96DA7CB3F99E43D39E77CB613F5C778F','5', '6')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID, PAGENAME,UNIT,CREATER,MEMO2, PAGECODE,CREATEDATE, PAGEBODY, RIGHTID, TREEID, RQURL, \"TYPE\", \"SORT\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'应收应付账款分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','YSYFZKFX',SYSDATE," + bbId + ",'194','" + user.getStaffid() + "','http://192.0.2.200:9001/browse/0/C9E2166539714F72AA1E62A98BF8629C','5', '7')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID, PAGENAME,UNIT,CREATER,MEMO2, PAGECODE,CREATEDATE, PAGEBODY, RIGHTID, TREEID, RQURL, \"TYPE\", \"SORT\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'盈利能力指标分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','YLNLZBFX',SYSDATE," + bbId + ",'194','" + user.getStaffid() + "','http://192.0.2.200:9001/browse/0/200ACC950A9B4D31B63533FAD27BD86C','5', '8')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object bbId2 = query.uniqueResult();
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID,PAGENAME,UNIT,CREATER,MEMO2,PAGECODE,CREATEDATE,RIGHTID,TREEID,\"TYPE\",\"SORT\") VALUES (" + bbId2 + ",'供应链分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','FRCW',SYSDATE,'194','" + user.getStaffid() + "','0','1')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID, PAGENAME,UNIT,CREATER,MEMO2, PAGECODE,CREATEDATE, PAGEBODY, RIGHTID, TREEID, RQURL, \"TYPE\", \"SORT\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'采购总体分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','CGZTFX',SYSDATE," + bbId2 + ",'194','" + user.getStaffid() + "','http://192.0.2.200:9001/browse/0/6C20C933E6AE46BD84FDDDBCEDDC0E84','5', '1')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID, PAGENAME,UNIT,CREATER,MEMO2, PAGECODE,CREATEDATE, PAGEBODY, RIGHTID, TREEID, RQURL, \"TYPE\", \"SORT\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'采购订单分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','CGDDFX',SYSDATE," + bbId2 + ",'194','" + user.getStaffid() + "','http://192.0.2.200:9001/browse/0/167E369DF63C40F1B0BF61EACDC7F732','5', '2')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID, PAGENAME,UNIT,CREATER,MEMO2, PAGECODE,CREATEDATE, PAGEBODY, RIGHTID, TREEID, RQURL, \"TYPE\", \"SORT\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'采购到货分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','FRCWZHZB',SYSDATE," + bbId2 + ",'194','" + user.getStaffid() + "','http://192.0.2.200:9001/browse/0/9A078AC253DE4DC7BE852D76BE13C238','5', '3')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID, PAGENAME,UNIT,CREATER,MEMO2, PAGECODE,CREATEDATE, PAGEBODY, RIGHTID, TREEID, RQURL, \"TYPE\", \"SORT\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'销售总体分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','FRCWZHZB',SYSDATE," + bbId2 + ",'194','" + user.getStaffid() + "','http://192.0.2.200:9001/browse/0/3DA89B20172440EE95AE62A94CB35484','5', '4')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID, PAGENAME,UNIT,CREATER,MEMO2, PAGECODE,CREATEDATE, PAGEBODY, RIGHTID, TREEID, RQURL, \"TYPE\", \"SORT\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'销售订单收入分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','FRCWZHZB',SYSDATE," + bbId2 + ",'194','" + user.getStaffid() + "','http://192.0.2.200:9001/browse/0/565EC5BF53B6423CB03B91534BFB5E2D','5', '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID, PAGENAME,UNIT,CREATER,MEMO2, PAGECODE,CREATEDATE, PAGEBODY, RIGHTID, TREEID, RQURL, \"TYPE\", \"SORT\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'库存总体分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','FRCWZHZB',SYSDATE," + bbId2 + ",'194','" + user.getStaffid() + "','http://192.0.2.200:9001/browse/0/B1009EF82BBD4B35828FB8D17EEFBE20','5', '6')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_PAGE (PAGEID, PAGENAME,UNIT,CREATER,MEMO2, PAGECODE,CREATEDATE, PAGEBODY, RIGHTID, TREEID, RQURL, \"TYPE\", \"SORT\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'采购退货分析'," + user.getTblOrganization().getOrgid() + ",'" + user.getUsername() + "','0','FRCWZHZB',SYSDATE," + bbId2 + ",'194','" + user.getStaffid() + "','http://192.0.2.200:9001/browse/0/1942BA44B8E94A80B41CAA5FDE907CF4','5', '7')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object qybbId = query.uniqueResult();
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME, UNIT, MEMO2, PAGECODE, CREATEDATE, RIGHTID, \"TYPE\") VALUES ('" + qybbId + "', '财务主题分析', '" + user.getTblOrganization().getOrgid() + "','0', 'CWZTDE',SYSDATE,'194','0')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME,UNIT,MEMO2, PAGECODE, CREATEDATE, PAGEBODY, RIGHTID, RQURL, \"TYPE\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '财务总览','" + user.getTblOrganization().getOrgid() + "', '0', '0000122',SYSDATE, '" + qybbId + "', '194','http://192.0.2.200:9001/browse/0/A98CB6AD58DC4633906BBBC477EDC118', '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME,UNIT,MEMO2, PAGECODE, CREATEDATE, PAGEBODY, RIGHTID, RQURL, \"TYPE\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '利润分析','" + user.getTblOrganization().getOrgid() + "', '0', '000001',SYSDATE, '" + qybbId + "', '194','http://192.0.2.200:9001/browse/0/FAAC722E742449DDBB7DBCCCB9571AC9', '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME,UNIT,MEMO2, PAGECODE, CREATEDATE, PAGEBODY, RIGHTID, RQURL, \"TYPE\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '收入分析','" + user.getTblOrganization().getOrgid() + "', '0', 'LRZX01',SYSDATE, '" + qybbId + "', '194','http://192.0.2.200:9001/browse/0/04C4949FA0114E309EB4F6EA22237578', '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME,UNIT,MEMO2, PAGECODE, CREATEDATE, PAGEBODY, RIGHTID, RQURL, \"TYPE\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '成本费用分析','" + user.getTblOrganization().getOrgid() + "', '0', 'LRZX01',SYSDATE, '" + qybbId + "', '194','http://192.0.2.200:9001/browse/0/743953662A4E4CFA95F843F721037C87', '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME,UNIT,MEMO2, PAGECODE, CREATEDATE, PAGEBODY, RIGHTID, RQURL, \"TYPE\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '资产负债分析','" + user.getTblOrganization().getOrgid() + "', '0', 'LRZX01',SYSDATE, '" + qybbId + "', '194','http://192.0.2.200:9001/browse/0/507D37BAF753422BAB713E2FCF4FC1CA', '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME,UNIT,MEMO2, PAGECODE, CREATEDATE, PAGEBODY, RIGHTID, RQURL, \"TYPE\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '现金流量分析','" + user.getTblOrganization().getOrgid() + "', '0', 'LRZX01',SYSDATE, '" + qybbId + "', '194','http://192.0.2.200:9001/browse/0/96DA7CB3F99E43D39E77CB613F5C778F', '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME,UNIT,MEMO2, PAGECODE, CREATEDATE, PAGEBODY, RIGHTID, RQURL, \"TYPE\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '应收应付账款分析','" + user.getTblOrganization().getOrgid() + "', '0', 'LRZX01',SYSDATE, '" + qybbId + "', '194','http://192.0.2.200:9001/browse/0/C9E2166539714F72AA1E62A98BF8629C', '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME,UNIT,MEMO2, PAGECODE, CREATEDATE, PAGEBODY, RIGHTID, RQURL, \"TYPE\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '盈利能力指标分析','" + user.getTblOrganization().getOrgid() + "', '0', 'LRZX01',SYSDATE, '" + qybbId + "', '194','http://192.0.2.200:9001/browse/0/200ACC950A9B4D31B63533FAD27BD86C', '5')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object qybbId2 = query.uniqueResult();
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME, UNIT, MEMO2, PAGECODE, CREATEDATE, RIGHTID, \"TYPE\") VALUES ('" + qybbId2 + "', '供应链分析', '" + user.getTblOrganization().getOrgid() + "','0', 'CWZTDE',SYSDATE,'194','0')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME,UNIT,MEMO2, PAGECODE, CREATEDATE, PAGEBODY, RIGHTID, RQURL, \"TYPE\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '采购总体分析','" + user.getTblOrganization().getOrgid() + "', '0', '0000122',SYSDATE, '" + qybbId2 + "', '194','http://192.0.2.200:9001/browse/0/6C20C933E6AE46BD84FDDDBCEDDC0E84', '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME,UNIT,MEMO2, PAGECODE, CREATEDATE, PAGEBODY, RIGHTID, RQURL, \"TYPE\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '采购订单分析','" + user.getTblOrganization().getOrgid() + "', '0', '000001',SYSDATE, '" + qybbId2 + "', '194','http://192.0.2.200:9001/browse/0/167E369DF63C40F1B0BF61EACDC7F732', '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME,UNIT,MEMO2, PAGECODE, CREATEDATE, PAGEBODY, RIGHTID, RQURL, \"TYPE\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '采购到货分析','" + user.getTblOrganization().getOrgid() + "', '0', 'LRZX01',SYSDATE, '" + qybbId2 + "', '194','http://192.0.2.200:9001/browse/0/9A078AC253DE4DC7BE852D76BE13C238', '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME,UNIT,MEMO2, PAGECODE, CREATEDATE, PAGEBODY, RIGHTID, RQURL, \"TYPE\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '销售总体分析','" + user.getTblOrganization().getOrgid() + "', '0', 'LRZX01',SYSDATE, '" + qybbId2 + "', '194','http://192.0.2.200:9001/browse/0/3DA89B20172440EE95AE62A94CB35484', '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME,UNIT,MEMO2, PAGECODE, CREATEDATE, PAGEBODY, RIGHTID, RQURL, \"TYPE\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '销售订单收入分析','" + user.getTblOrganization().getOrgid() + "', '0', 'LRZX01',SYSDATE, '" + qybbId2 + "', '194','http://192.0.2.200:9001/browse/0/565EC5BF53B6423CB03B91534BFB5E2D', '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME,UNIT,MEMO2, PAGECODE, CREATEDATE, PAGEBODY, RIGHTID, RQURL, \"TYPE\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '库存总体分析','" + user.getTblOrganization().getOrgid() + "', '0', 'LRZX01',SYSDATE, '" + qybbId2 + "', '194','http://192.0.2.200:9001/browse/0/B1009EF82BBD4B35828FB8D17EEFBE20', '5')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_BI_REPORT_MENU (PAGEID, PAGENAME,UNIT,MEMO2, PAGECODE, CREATEDATE, PAGEBODY, RIGHTID, RQURL, \"TYPE\") VALUES (HIBERNATE_SEQUENCE.NEXTVAL, '采购退货分析','" + user.getTblOrganization().getOrgid() + "', '0', 'LRZX01',SYSDATE, '" + qybbId2 + "', '194','http://192.0.2.200:9001/browse/0/1942BA44B8E94A80B41CAA5FDE907CF4', '5')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object fxonejdId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISKCATEGORY (RISKCATNUMBER,\tRISKCATNAME,RISKCATID,RISKCATDES,RISKSTATUS,FATHERRISKCATID,ISLEAF,UNIT,MODULETYPE) VALUES ('RISK-01','战略风险','" + fxonejdId + "',NULL,0,(SELECT RISKCATID FROM TBL_RISKCATEGORY WHERE UNIT = '" + organization.getOrgid() + "' AND RISKCATNAME = '企业风险'),1,'" + organization.getOrgid() + "','FXSJK')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object fxtwojdId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISKCATEGORY (RISKCATNUMBER,\tRISKCATNAME,RISKCATID,RISKCATDES,RISKSTATUS,FATHERRISKCATID,ISLEAF,UNIT,MODULETYPE) VALUES ('RISK-02','财务风险','" + fxtwojdId + "',NULL,0,(SELECT RISKCATID FROM TBL_RISKCATEGORY WHERE UNIT = '" + organization.getOrgid() + "' AND RISKCATNAME = '企业风险'),1,'" + organization.getOrgid() + "','FXSJK')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object fxthreejdId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISKCATEGORY (RISKCATNUMBER,\tRISKCATNAME,RISKCATID,RISKCATDES,RISKSTATUS,FATHERRISKCATID,ISLEAF,UNIT,MODULETYPE) VALUES ('RISK-03','运营风险','" + fxthreejdId + "',NULL,0,(SELECT RISKCATID FROM TBL_RISKCATEGORY WHERE UNIT = '" + organization.getOrgid() + "' AND RISKCATNAME = '企业风险'),1,'" + organization.getOrgid() + "','FXSJK')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object fxfourjdId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISKCATEGORY (RISKCATNUMBER,\tRISKCATNAME,RISKCATID,RISKCATDES,RISKSTATUS,FATHERRISKCATID,ISLEAF,UNIT,MODULETYPE) VALUES ('RISK-04','法律风险','" + fxfourjdId + "',NULL,0,(SELECT RISKCATID FROM TBL_RISKCATEGORY WHERE UNIT = '" + organization.getOrgid() + "' AND RISKCATNAME = '企业风险'),1,'" + organization.getOrgid() + "','FXSJK')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object fxfivejdId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISKCATEGORY (RISKCATNUMBER,\tRISKCATNAME,RISKCATID,RISKCATDES,RISKSTATUS,FATHERRISKCATID,ISLEAF,UNIT,MODULETYPE) VALUES ('RISK-05','市场风险','" + fxfivejdId + "',NULL,0,(SELECT RISKCATID FROM TBL_RISKCATEGORY WHERE UNIT = '" + organization.getOrgid() + "' AND RISKCATNAME = '企业风险'),1,'" + organization.getOrgid() + "','FXSJK')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object fxsixsjdId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISKCATEGORY (RISKCATNUMBER,\tRISKCATNAME,RISKCATID,RISKCATDES,RISKSTATUS,FATHERRISKCATID,ISLEAF,UNIT,MODULETYPE) VALUES ('RISK-01-01','公司治理风险','" + fxsixsjdId + "',NULL,0,'" + fxonejdId + "',1,'" + organization.getOrgid() + "','FXSJK')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-01-01-01','管理层通过增资扩股以各种方式直接或间接持股，可能导致不合规股东利益输送，甚至违反法律','" + organization.getOrgid() + "','" + fxsixsjdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','其他')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-01-01-02','持股的管理层参与制订改制方案、确定国有产权折股价、选择中介机构，以及清产核资、财务审计、离任审计、资产评估中的重大事项，可能导致舞弊的事情发生','" + organization.getOrgid() + "','" + fxsixsjdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','其他')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object fxsevenjdId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISKCATEGORY (RISKCATNUMBER,\tRISKCATNAME,RISKCATID,RISKCATDES,RISKSTATUS,FATHERRISKCATID,ISLEAF,UNIT,MODULETYPE) VALUES ('RISK-01-02','决策风险'," + fxsevenjdId + ",NULL,0," + fxonejdId + ",1," + organization.getOrgid() + ",'FXSJK')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-01-02-01','立项缺乏可行性研究或者可行性研究流于形式，决策不当，盲目上马，可能导致项目立项不符合公司战略需求，甚至产生重大资金浪费','" + organization.getOrgid() + "','" + fxsevenjdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','工程项目')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-01-02-02','影响重大的交易和事项的处理未规定进行审批，可能导致违法风险的发生','" + organization.getOrgid() + "','" + fxsevenjdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','工程项目')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object fxeightjdId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISKCATEGORY (RISKCATNUMBER,\tRISKCATNAME,RISKCATID,RISKCATDES,RISKSTATUS,FATHERRISKCATID,ISLEAF,UNIT,MODULETYPE) VALUES ('RISK-02-01','会计核算风险'," + fxeightjdId + ",NULL,0," + fxtwojdId + ",1," + organization.getOrgid() + ",'FXSJK')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-02-01-01','各类交易和事项的现金流量的界限不清，可能导致报表披露不实，甚至有违法风险的发生','" + organization.getOrgid() + "','" + fxeightjdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','账务处理方面')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-02-01-02','合并财务报表合并范围和合并方法存在错误，可能导致报表披露不实，甚至有违法风险的发生','" + organization.getOrgid() + "','" + fxeightjdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','账务处理方面')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object fxninejdId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISKCATEGORY (RISKCATNUMBER,\tRISKCATNAME,RISKCATID,RISKCATDES,RISKSTATUS,FATHERRISKCATID,ISLEAF,UNIT,MODULETYPE) VALUES ('RISK-02-02','资产管理风险','" + fxninejdId + "',NULL,0,'" + fxtwojdId + "',1,'" + organization.getOrgid() + "','FXSJK')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-02-02-01','无形资产权属关系不清，可能导致无形资产资产管理失控，造成损失，甚至有诉讼的风险','" + organization.getOrgid() + "','" + fxninejdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','无形资产')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-02-02-02','未取得土地使用权有效证明文件，可能导致出现无效不动产的可能，甚至带来违规违法风险','" + organization.getOrgid() + "','" + fxninejdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','无形资产')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object fxtenjdId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISKCATEGORY (RISKCATNUMBER,\tRISKCATNAME,RISKCATID,RISKCATDES,RISKSTATUS,FATHERRISKCATID,ISLEAF,UNIT,MODULETYPE) VALUES ('RISK-03-01','人力资源风险','" + fxtenjdId + "',NULL,0,'" + fxthreejdId + "',1,'" + organization.getOrgid() + "','FXSJK')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-03-01-01','研究项目专业人员配备不合理，可能导致研究项目不能达到预期效果','" + organization.getOrgid() + "','" + fxtenjdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','研究与开发')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-03-01-02','与核心研究人员签订的劳动合同重要条款缺失，可能导致研究成果被剽窃，给公司带来损失，甚至研发进程失败受挫','" + organization.getOrgid() + "','" + fxtenjdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','研究与开发')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object fxelejdId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISKCATEGORY (RISKCATNUMBER,\tRISKCATNAME,RISKCATID,RISKCATDES,RISKSTATUS,FATHERRISKCATID,ISLEAF,UNIT,MODULETYPE) VALUES ('RISK-03-02','建设项目管控风险'," + fxelejdId + ",NULL,0," + fxthreejdId + ",1," + organization.getOrgid() + ",'FXSJK')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-03-02-01','项目验收资料不完整、不规范，可能导致工程项目不合规，甚至有舞弊的可能','" + organization.getOrgid() + "','" + fxelejdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','竣工验收方面')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-03-02-02','验收程序不合规，可能导致工程项目不合规，甚至有舞弊的可能','" + organization.getOrgid() + "','" + fxelejdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','竣工验收方面')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object fxtwejdId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISKCATEGORY (RISKCATNUMBER,\tRISKCATNAME,RISKCATID,RISKCATDES,RISKSTATUS,FATHERRISKCATID,ISLEAF,UNIT,MODULETYPE) VALUES ('RISK-04-01','合同管理风险','" + fxtwejdId + "',NULL,0,'" + fxfourjdId + "',1,'" + organization.getOrgid() + "','FXSJK')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-04-01-01','未按规定履行决策和审批程序，擅自签订或变更合同，可能导致合同管理失控，发生损失甚至产生无效合同，并产生诉讼风险','" + organization.getOrgid() + "','" + fxtwejdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','合同管理')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-04-01-02','未按照合同约定收取货款，会形成资金占用','" + organization.getOrgid() + "','" + fxtwejdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','合同管理')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object fxthreeteenjdId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISKCATEGORY (RISKCATNUMBER,\tRISKCATNAME,RISKCATID,RISKCATDES,RISKSTATUS,FATHERRISKCATID,ISLEAF,UNIT,MODULETYPE) VALUES ('RISK-04-02','纠纷诉讼风险','" + fxthreeteenjdId + "',NULL,0,'" + fxfourjdId + "',1,'" + organization.getOrgid() + "','FXSJK')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-04-02-01','存在销售或贸易纠纷，形成诉讼案件，未及时妥善处理，存在违法风险，造成诉讼损失','" + organization.getOrgid() + "','" + fxthreeteenjdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','其他')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-04-02-02','应收债权存在诉讼情况，可能导致债权资产的损失','" + organization.getOrgid() + "','" + fxthreeteenjdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','金融资产（应收票据、应收账款、预付账款、其他应收款、应收股利、应收利息、交易性金融资产、可供出售金融资产、持有至到期投资）')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object fxfourteenjdId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISKCATEGORY (RISKCATNUMBER,\tRISKCATNAME,RISKCATID,RISKCATDES,RISKSTATUS,FATHERRISKCATID,ISLEAF,UNIT,MODULETYPE) VALUES ('RISK-05-01','销售风险','" + fxfourteenjdId + "',NULL,0,'" + fxfivejdId + "',1,'" + organization.getOrgid() + "','FXSJK')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-05-01-01','存在应收款项坏账损失风险','" + organization.getOrgid() + "','" + fxfourteenjdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','销售业务')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-05-01-02','应收款项发生损失的，未履行审批程序，可能导致坏账计提不准的风险，甚至影响报表真实性','" + organization.getOrgid() + "','" + fxfourteenjdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','销售业务')";
//            sqlList.add(sql);
//            query = session.createSQLQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL AS ID FROM DUAL");
//            Object fxfiveteenjdId = query.uniqueResult();
//            sql = "INSERT INTO TBL_RISKCATEGORY (RISKCATNUMBER,\tRISKCATNAME,RISKCATID,RISKCATDES,RISKSTATUS,FATHERRISKCATID,ISLEAF,UNIT,MODULETYPE) VALUES ('RISK-05-02','信用风险','" + fxfiveteenjdId + "',NULL,0,'" + fxfivejdId + "',1,'" + organization.getOrgid() + "','FXSJK')";
//            query = session.createSQLQuery(sql);
//            query.executeUpdate();
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-05-02-01','未定期对客户信用情况进行评估，可能导致与低信用客户合作，发生损失','" + organization.getOrgid() + "','" + fxfiveteenjdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','金融资产（应收票据、应收账款、预付账款、其他应收款、应收股利、应收利息、交易性金融资产、可供出售金融资产、持有至到期投资）')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_RISK (RISKID,RISKNUMBER,RISKDES,BELONGSTO,RISKCATID,\"VERSION\",UNIT,REORG,RISKCREATEDT,STAFFID,RISKNAME) VALUES (HIBERNATE_SEQUENCE.NEXTVAL,'RISK-05-02-02','客户信用档案不健全，可能导致由于信用不好带来的应收账款风险，甚至产生坏账','" + organization.getOrgid() + "','" + fxfiveteenjdId + "',1.0,'" + organization.getOrgid() + "','" + organization.getOrgid() + "',SYSDATE,'" + user.getStaffid() + "','金融资产（应收票据、应收账款、预付账款、其他应收款、应收股利、应收利息、交易性金融资产、可供出售金融资产、持有至到期投资）')";
//            sqlList.add(sql);
//            sql = "SELECT ORGID from TBL_ORGANIZATION WHERE orgname='行业' and ORGTYPE>=100";
//            query = session.createSQLQuery(sql);
//            Object hyRootId = query.uniqueResult();
//            sql = "INSERT INTO TBL_INDUSTRY_INNER (ORGID, INDUSTRYID) VALUES ('" + hyRootId + "', '" + organization.getOrgid() + "')";
//            sqlList.add(sql);
//            sql = "INSERT INTO TBL_INDUSTRY_INNER (ORGID, INDUSTRYID) VALUES ('" + hyId + "', '" + organization.getOrgid() + "')";
//            sqlList.add(sql);
//            sql = "UPDATE TBL_ORGANIZATION SET ISINITIALIZATION = '0' WHERE ORGID = " + organization.getOrgid();
//            sqlList.add(sql);
//
//            for(int i = 0; i < sqlList.size(); ++i) {
//                query = session.createSQLQuery((String)sqlList.get(i));
//                query.executeUpdate();
//            }
//
//            tx.commit();
//        } catch (Exception var139) {
//            tx.rollback();
//            var139.printStackTrace();
//        } finally {
//            this.releaseSession(session);
//        }
//    }


    @Override
    public void findAllPageInfoByacctid(PageInfo<TblStaff> pageInfo, String bookid) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        pageInfo.setTlist(this.tblStaffMapper.findAllPageInfoByacctid(pageInfo, bookid));
        pageInfo.setTotalRecord(this.tblStaffMapper.findAllPageInfoByacctidcCount(bookid));

        resultMap.put("code", "1");
        resultMap.put("msg", "数据访问成功");
        resultMap.put("data", pageInfo);
    }

    @Override
    public void findAllMySqlPageInfoByacctid(PageInfo<TblStaffMySql> pageInfo, String bookid) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        pageInfo.setTlist(this.tblStaffMySqlMapper.findAllPageInfoByacctid(pageInfo, bookid));
        pageInfo.setTotalRecord(this.tblStaffMySqlMapper.findAllPageInfoByacctidcCount(bookid));

        resultMap.put("code", "1");
        resultMap.put("msg", "数据访问成功");
        resultMap.put("data", pageInfo);
    }

    @Override
    public Map<String, Object> findByAll(String pid, PageInfo<TblStaff> pageInfo) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        pageInfo.setTlist(this.tblStaffMapper.findByAll(pid, pageInfo));
        pageInfo.setTotalRecord(this.tblStaffMapper.findByAllCount(pid));

        resultMap.put("code", "1");
        resultMap.put("msg", "数据访问成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }

    @Override
    public Map<String, Object> findByAllMySql(String pid, PageInfo<TblStaffMySql> pageInfo) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        pageInfo.setTlist(this.tblStaffMySqlMapper.findByAll(pid, pageInfo));
        pageInfo.setTotalRecord(this.tblStaffMySqlMapper.findByAllCount(pid));

        resultMap.put("code", "1");
        resultMap.put("msg", "数据访问成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }

    @Override
    public Map<String, Object> findByAllORGID(BigDecimal orgid, PageInfo<TblStaff> pageInfo) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        pageInfo.setTlist(this.tblStaffMapper.findByAllORGID(orgid, pageInfo));
        pageInfo.setTotalRecord(this.tblStaffMapper.findByAllORGIDCount(orgid));

        resultMap.put("code", "1");
        resultMap.put("msg", "数据访问成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }

    @Override
    public Map<String, Object> findByAllMySqlORGID(BigDecimal orgid, PageInfo<TblStaffMySql> pageInfo) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        pageInfo.setTlist(this.tblStaffMySqlMapper.findByAllORGID(orgid, pageInfo));
        pageInfo.setTotalRecord(this.tblStaffMySqlMapper.findByAllORGIDCount(orgid));

        resultMap.put("code", "1");
        resultMap.put("msg", "数据访问成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }

    @Override
    public String selectDeptIdForUserId(String id) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            // TODO Auto-generated method stub
            return null;
        } else {
            // TODO Auto-generated method stub
            return null;
        }
    }

    @Override
    public List findByAll(String pid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            // TODO Auto-generated method stub
            return null;
        } else {
            // TODO Auto-generated method stub
            return null;
        }
    }

    @Override
    public List findByAllORGID(String string) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            // TODO Auto-generated method stub
            return null;
        } else {
            // TODO Auto-generated method stub
            return null;
        }
    }

    @Override
    public String getUserTree(String url, TblOrganization org) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getMySqlUserTree(String url, TblOrganizationMySql org) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUserTreeWithMap(String url, HashMap map, TblOrganization org) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getMySqlUserTreeWithMap(String url, HashMap map, TblOrganizationMySql org) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PageBean findAllPageBeanPid(Integer pageNumber, int pageSize, TblOrganization attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PageBean findAllMySqlPageBeanPid(Integer pageNumber, int pageSize, TblOrganizationMySql attribute) {
        // TODO Auto-generated method stub
        return null;
    }

//    @Override
//    public Map<String, Object> findUserByZbsjFor(PageInfo<TblStaff> pageInfo, String token, String staffId) {
//        Map<String,Object> resultMap = new HashMap<String, Object>(0);
//        Jedis jedis = null;
//        if(token == null && staffId == null) {
//            resultMap.put("code", "0");
//            resultMap.put("msg", "用户已失效！");
//            return resultMap;
//        }
//        if(token == null && staffId != null) {
//            jedis = JedisUtil.getJedis();
//            token = jedis.get(staffId);
//        }
//        Claims claims = JwtUtils.parseJwt(token);
//        Object object = claims.get("staffInfo");
//        JSONObject objJson = JSONObject.fromObject(object);
//        TblStaff user = (TblStaff) JSONObject.toBean(objJson,TblStaff.class);
//        BigDecimal orgid = user.getCurrentOrg().getOrgid();
//
//        return resultMap;
//    }

//    private void grantContractRightForOrg(BigDecimal orgid, List<Object> objList) {
//        String sql = null;
//        if (objList != null) {
//            Iterator var4 = objList.iterator();
//
//            while(var4.hasNext()) {
//                Object object = var4.next();
//                sql = "INSERT INTO TBL_ORG_RIGHT_NEW(ORGID,RIGHTID,INDICATORSTATUS) VALUES(" + orgid + "," + object + ",1)";
//                this.executeSql(sql);
//                sql = "SELECT RIGHTID FROM TBL_MANAGE_RIGHT WHERE FATHERRIGHTID = " + object;
//                this.grantContractRightForOrg(orgid, this.selectRightIdsByParentId(sql));
//            }
//        }
//
//    }
}
