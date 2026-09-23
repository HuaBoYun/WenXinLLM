package com.huabo.compliance.service;

import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.Find;
import com.huabo.compliance.entity.TblOrganization;
import com.huabo.compliance.entity.TblStaff;
import com.huabo.compliance.mysql.entity.TblOrganizationMySql;
import com.huabo.compliance.mysql.entity.TblStaffMySql;
import com.huabo.compliance.util.PageBean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserService {
    TblStaff findById(String uid);

    TblStaffMySql findByMySqlId(String uid);

    void modify(TblStaff var1);

    void modifyMySql(TblStaffMySql var1);

    List<TblStaff> findUserByZbsjForQx(PageInfo<TblStaff> var1) throws Exception;

    List<TblStaffMySql> findUserByMySqlZbsjForQx(PageInfo<TblStaffMySql> var1) throws Exception;

    Map<String, Object> findAllPageBeanPid(String staffId, Find find, Integer pageNumber, Integer pageSize, BigDecimal pid);

    Map<String, Object> findAllMySqlPageBeanPid(String staffId, Find find, Integer pageNumber, Integer pageSize, BigDecimal pid);

    Map<String, Object> findAllPageBean(Find find, Integer pageNumber, Integer pageSize, String token, String staffId);

    Map<String, Object> findAllMySqlPageBean(Find find, Integer pageNumber, Integer pageSize, String token, String staffId);

//    Map<String, Object> findAll(String staffId, Find find,Integer pageNumber, Integer pageSize,String token);
//
//    Map<String, Object> findAllPid(String staffId, Find find,Integer pageNumber, Integer pageSize,String token);

    void add(TblStaff tblStaff);

    void addMySql(TblStaffMySql tblStaff);

    TblStaff findByUserId(String userid);

    TblStaffMySql findByMySqlUserId(String userid);

//    TblStaffMySql findByMySqlUserId(String userid);

    // void initializationSystemData(TblOrganizationUtil organization, TblStaffUtil user, String hyId);

    void findAllPageInfoByacctid(PageInfo<TblStaff> pageInfo, String bookid);

    void findAllMySqlPageInfoByacctid(PageInfo<TblStaffMySql> pageInfo, String bookid);

    Map<String, Object> findByAll(String pid, PageInfo<TblStaff> pageInfo);

    Map<String, Object> findByAllMySql(String pid, PageInfo<TblStaffMySql> pageInfo);

    Map<String, Object> findByAllORGID(BigDecimal orgid, PageInfo<TblStaff> pageInfo);

    Map<String, Object> findByAllMySqlORGID(BigDecimal orgid, PageInfo<TblStaffMySql> pageInfo);

    //void findUserByZbsjFor(PageInfo<TblStaff> pageInfo, String token, String staffId);

//    Map<String, Object> findUserByZbsjFor(PageInfo<TblStaff> pageInfo, String token, String staffId);

    public String selectDeptIdForUserId(String id);

    List findByAll(String pid);

    List findByAllORGID(String string);

    String getUserTree(String url, TblOrganization org);

    String getMySqlUserTree(String url, TblOrganizationMySql org);

    String getUserTreeWithMap(String url, HashMap map, TblOrganization org);

    String getMySqlUserTreeWithMap(String url, HashMap map, TblOrganizationMySql org);

    PageBean findAllPageBeanPid(Integer pageNumber, int pageSize, TblOrganization attribute);

    PageBean findAllMySqlPageBeanPid(Integer pageNumber, int pageSize, TblOrganizationMySql attribute);
}