package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.huabo.system.entity.TblRole;
import com.huabo.system.entity.TblStaff;

public interface TblRoleService {
    List<TblRole> findAll(BigDecimal var1);

    void save(TblRole tr);

    TblRole findByid(String var1);

    List<TblStaff> isSY(String var1);

    void update(TblRole var1);

//    void delete(TblRole var1);

    /**
     * 查找角色分页数
     * @param role  角色相关查询条件
     * @param pageNumber	起始页
     * @param pageSize		每页数量
     * @param token			用户登录令牌
     * @param orgIds		公司主键
     * @param roleName		角色名称
     * @param orgName		公司名称
     * @return
     */
    Map<String, Object> findTblRoleAll(TblRole role, Integer pageNumber, Integer pageSize,String token, String orgIds, String roleName, String orgName);

    void delete(TblRole role);

    void deleteByRid(Integer rid);


//    void delete(BigDecimal rid);

//    void deleteRid(BigDecimal rid);
    
    public Boolean findIsByJs(String jsmc,TblStaff staff);

	void inertOrgandRole(String orgid, String roleid, BigDecimal rootId) throws Exception;

	void delOrgandRole(String orgid, String roleid) throws Exception;

	Integer selectReaprtRoleName(String rname, BigDecimal rid, BigDecimal pid) throws Exception;

	Map<String, Object> roleDetail(String token, BigDecimal roleId) throws Exception;

	void grantToUser(String staffids, String roleid) throws Exception;

	void unGrantFromUser(String staffids, String roleid) throws Exception;

	void grantToOrg(String orgids, String roleid) throws Exception;

	void unGrantFromOrg(String orgids, String roleid) throws Exception;
}
