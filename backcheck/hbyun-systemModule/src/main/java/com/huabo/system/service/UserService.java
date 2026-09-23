package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.Find;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblStaff;

public interface UserService {
    TblStaff findById(String uid);

    List<TblStaff> findUserByZbsjForQx(PageInfo<TblStaff> var1) throws Exception;

    Map<String, Object> findAllPageBeanPid(String staffId, Find find, Integer pageNumber, Integer pageSize, BigDecimal pid);

    Map<String, Object> findAllPageBean(Find find, Integer pageNumber, Integer pageSize, String token, String staffId);

//    Map<String, Object> findAll(String staffId, Find find,Integer pageNumber, Integer pageSize,String token);
//
//    Map<String, Object> findAllPid(String staffId, Find find,Integer pageNumber, Integer pageSize,String token);

    void add(TblStaff tblStaff);

    TblStaff findByUserId(String userid);


//    TblStaffMySql findByMySqlUserId(String userid);

    // void initializationSystemData(TblOrganizationUtil organization, TblStaffUtil user, String hyId);

    void findAllPageInfoByacctid(PageInfo<TblStaff> pageInfo, String bookid) throws Exception;

    Map<String, Object> findByAll(String pid, PageInfo<TblStaff> pageInfo);

    Map<String, Object> findByAllORGID(BigDecimal orgid, PageInfo<TblStaff> pageInfo);

    //void findUserByZbsjFor(PageInfo<TblStaff> pageInfo, String token, String staffId);

//    Map<String, Object> findUserByZbsjFor(PageInfo<TblStaff> pageInfo, String token, String staffId);

    public String selectDeptIdForUserId(String id);

    List findByAll(String pid);

    List findByAllORGID(String string);

    String getUserTree(String url, TblOrganization org);

    String getUserTreeWithMap(String url, HashMap map, TblOrganization org);

    Map<String, Object> findAllBmfzrPageBean(String token, Integer pageNumber, Integer pageSize);
    
    Map<String, Object> findAllFgldPageBean(String token, Integer pageNumber, Integer pageSize);

	JsonBean finduserAllList(TblStaff staff, Integer pageNumber, Integer pageSize, String token, Integer isAll) throws Exception;

	JsonBean exportUserInfoList(TblStaff staff, String token, BigDecimal orgId, HttpServletResponse response) throws Exception;

	JsonBean exportUserInfoList(String token, MultipartFile file) throws Exception;
    
}