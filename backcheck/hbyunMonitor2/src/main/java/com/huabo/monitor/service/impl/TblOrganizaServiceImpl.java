package com.huabo.monitor.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.*;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.config.DateBaseConfig;
import com.huabo.monitor.entity.*;
import com.huabo.monitor.mapper.*;
import com.huabo.monitor.mysql.entity.FindMySql;
import com.huabo.monitor.mysql.entity.TblBiReportMenuMySql;
import com.huabo.monitor.mysql.entity.TblManageRightMySql;
import com.huabo.monitor.mysql.entity.TblOrganizationMySql;
import com.huabo.monitor.mysql.mapper.TblLoginTypeMySqlMapper;
import com.huabo.monitor.mysql.mapper.TblManageRightMySqlDAO;
import com.huabo.monitor.mysql.mapper.TblOrganizationMySqlMapper;
import com.huabo.monitor.mysql.mapper.TblStaffMySqlMapper;
import com.huabo.monitor.service.TblOrganizaService;
import com.huabo.monitor.util.Tree;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TblOrganizaServiceImpl implements TblOrganizaService {
    private static final Logger logger = LoggerFactory.getLogger(TblOrganizaServiceImpl.class);

    @Resource
    private TblOrganizationMapper tblOrganizationMapper;

    @Resource
    private TblLoginTypeMapper tblLoginTypeMapper;

    @Resource
    private TblBiReportMenuMapper tblBiReportMenuMapper;

    @Resource
    private TblStaffMapper tblStaffMapper;

    @Resource
    private TblManageRightDAO tblManageRightDAO;

    @Resource
    private TblOrganizationMySqlMapper tblOrganizationMySqlMapper;

    @Resource
    private TblLoginTypeMySqlMapper tblLoginTypeMySqlMapper;

    @Resource
    private TblBiReportMenuMySqlMapper tblBiReportMenuMySqlMapper;

    @Resource
    private TblStaffMySqlMapper tblStaffMySqlMapper;

    @Resource
    private TblManageRightMySqlDAO tblManageRightMySqlDAO;
    
    @Resource
    private UserProvider userProvider;


    @Override
    public Map<String, Object> findOrganizationInfoByModuleId(Integer pageNumber, Integer pageSize, TblOrganization tblOrganization, Integer moduleId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setCondition(tblOrganization);
        pageInfo.setTlist(tblOrganizationMapper.selectStaffInfoByModuleIdList(pageInfo, moduleId));
        pageInfo.setTotalRecord(tblOrganizationMapper.selectStaffInfoByModuleIdCount(pageInfo, moduleId));
        resultMap.put("code", "1");
        resultMap.put("msg", "数据访问成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }

    @Override
    public Map<String, Object> findMySqlOrganizationInfoByModuleId(Integer pageNumber, Integer pageSize, TblOrganizationMySql tblOrganization, Integer moduleId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblOrganizationMySql> pageInfo = new PageInfo<TblOrganizationMySql>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setCondition(tblOrganization);
        pageInfo.setTlist(tblOrganizationMySqlMapper.selectStaffInfoByModuleIdList(pageInfo, moduleId));
        pageInfo.setTotalRecord(tblOrganizationMySqlMapper.selectStaffInfoByModuleIdCount(pageInfo, moduleId));
        resultMap.put("code", "1");
        resultMap.put("msg", "数据访问成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }

    @Override
    public void modify(TblOrganization org) {
        this.tblOrganizationMapper.saveModiOrganization(org);
    }

    @Override
    public void modifyMySql(TblOrganizationMySql org) {
        this.tblOrganizationMySqlMapper.saveModiOrganization(org);
    }

    @Override
    public Serializable countOrg() {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            TblOrganization tblOrganization = tblLoginTypeMapper.selectcOunt();
            return tblOrganization;
        } else {
            TblOrganizationMySql tblOrganization = tblLoginTypeMySqlMapper.selectcOunt();
            return tblOrganization;
        }
    }


    @Override
    public List<TblOrganization> gsisXj(String id) {
        List<TblOrganization> to = tblOrganizationMapper.selectId(id);
        return to;
    }

    @Override
    public List<TblOrganizationMySql> gsisXjMySql(String id) {
        List<TblOrganizationMySql> to = tblOrganizationMySqlMapper.selectId(id);
        return to;
    }

    @Override
    public List<TblOrganization> findChildrenByOrgid(String orgid) {
        List<TblOrganization> findAllOrgUserLeft = this.tblOrganizationMapper.findAllOrgUserLeft(orgid);
        return findAllOrgUserLeft;
    }

    @Override
    public List<TblOrganizationMySql> findChildrenByMySqlOrgid(String orgid) {
        List<TblOrganizationMySql> findAllOrgUserLeft = this.tblOrganizationMySqlMapper.findAllOrgUserLeft(orgid);
        return findAllOrgUserLeft;
    }

//    @Override
//    public void deleteOrg(TblOrganization org) {
//        this.tblOrganizationMapper.delete(org);
//    }

    @Override
    public void add(TblOrganization org) {
        this.tblOrganizationMapper.saveTblOrganization(org);

    }

    @Override
    public void addMySql(TblOrganizationMySql org) {
        this.tblOrganizationMySqlMapper.saveTblOrganization(org);
    }

    @Override
    public List<TblOrganization> getHyOrgTree() {
        String tree = "";
        List<TblOrganization> tbl = tblOrganizationMapper.selectzong();
        if (tbl == null || tbl.size() == 0) {
            TblOrganization org = new TblOrganization();
            org.setOrgname("行业");
            org.setFatherorgid(new BigDecimal("-1"));
            org.setOrgnumber("00");
            org.setOrgmeno("行业");
            org.setMemo("行业");
            org.setOrgtype(100);
            this.tblOrganizationMapper.saveTblOrganization(org);
            tbl.add(org);
        }

//        TblOrganization org;
//        for(Iterator var8 = tbl.iterator(); var8.hasNext(); tree = tree + "tree.nodes['" + org.getFatherorgid().toEngineeringString() + "_" + org.getOrgid().toString() + "']=\"text:" + org.getOrgname() + ";method:check(" + org.getOrgid().toString() + "," + org.getOrgtype().toString() + ")\";\n") {
//            Object o = var8.next();
//            org = (TblOrganization)o;
//        }
//
//        return tree;
//        TblOrganization org;
//        for(Iterator var8 = tbl.iterator(); var8.hasNext(); tree = tree + "tree.nodes['" + org.getFatherorgid().toEngineeringString() + "_" + org.getOrgid().toString() + "']=\"text:" + org.getOrgname() + ";method:check(" + org.getOrgid().toString() + "," + org.getOrgtype().toString() + ")\";\n") {
//            Object o = var8.next();
//            org = (TblOrganization)o;
//        }

        return tbl;
    }

    @Override
    public List<TblOrganizationMySql> getMySqlHyOrgTree() {
        String tree = "";
        List<TblOrganizationMySql> tbl = tblOrganizationMySqlMapper.selectzong();
        if (tbl == null || tbl.size() == 0) {
            TblOrganizationMySql org = new TblOrganizationMySql();
            org.setOrgname("行业");
            org.setFatherorgid(new BigDecimal("-1"));
            org.setOrgnumber("00");
            org.setOrgmeno("行业");
            org.setMemo("行业");
            org.setOrgtype(100);
            this.tblOrganizationMySqlMapper.saveTblOrganization(org);
            tbl.add(org);
        }

//        TblOrganization org;
//        for(Iterator var8 = tbl.iterator(); var8.hasNext(); tree = tree + "tree.nodes['" + org.getFatherorgid().toEngineeringString() + "_" + org.getOrgid().toString() + "']=\"text:" + org.getOrgname() + ";method:check(" + org.getOrgid().toString() + "," + org.getOrgtype().toString() + ")\";\n") {
//            Object o = var8.next();
//            org = (TblOrganization)o;
//        }
//
//        return tree;
//        TblOrganization org;
//        for(Iterator var8 = tbl.iterator(); var8.hasNext(); tree = tree + "tree.nodes['" + org.getFatherorgid().toEngineeringString() + "_" + org.getOrgid().toString() + "']=\"text:" + org.getOrgname() + ";method:check(" + org.getOrgid().toString() + "," + org.getOrgtype().toString() + ")\";\n") {
//            Object o = var8.next();
//            org = (TblOrganization)o;
//        }
        return tbl;
    }

    @Override
    public TblOrganization getHY() {
        List<TblOrganization> listSql = tblOrganizationMapper.selectGetHY();

        return listSql != null && listSql.size() > 0 ? (TblOrganization) listSql.get(0) : null;
    }

    @Override
    public TblOrganizationMySql getMySqlHY() {
        List<TblOrganizationMySql> listSql = tblOrganizationMySqlMapper.selectGetHY();

        return listSql != null && listSql.size() > 0 ? (TblOrganizationMySql) listSql.get(0) : null;
    }

    @Override
    public TblOrganization findByname(String orgname) {
        List<TblOrganization> list = this.tblOrganizationMapper.findBysql(orgname);
        return list != null && list.size() > 0 ? (TblOrganization) list.get(0) : null;
    }

    @Override
    public TblOrganizationMySql findByMySqlname(String orgname) {
        List<TblOrganizationMySql> list = this.tblOrganizationMySqlMapper.findBysql(orgname);
        return list != null && list.size() > 0 ? (TblOrganizationMySql) list.get(0) : null;
    }

//    @Override
//    public List<TblOrganization> findOrgByType(String pid, String type) {
//
//        List<TblOrganization> fatherorgid = tblOrganizationMapper.selectPid(pid);
//        List<TblOrganization> organme = tblOrganizationMapper.selectOrganme(pid);
//        List<TblOrganization> industry = tblLoginTypeMapper.selectIndustry();
//        TblOrganization tbl = new TblOrganization();
//        tbl.add(fatherorgid);
//        tbl.add(organme);
//        tbl.add(industry);
//        List<TblOrganization> org = new ArrayList();
//
//        while(tbl.hasNext()) {
//            Object[] objects = (Object[])tbl.next();
//            TblOrganization o = new TblOrganization();
//            o.setOrgname(objects[0] == null ? "" : objects[0].toString());
//            o.setOrgnumber(objects[1] == null ? "" : objects[1].toString());
//            o.setOrgid(new BigDecimal(objects[2].toString()));
//            org.add(o);
//        }
//        return org;
//    }

    @Override
    public List<TblOrganization> isParent(String id) {
        List<TblOrganization> list = this.tblOrganizationMapper.findSql(id);
        return list;
    }

    @Override
    public List<TblOrganizationMySql> isParentMySql(String id) {
        List<TblOrganizationMySql> list = this.tblOrganizationMySqlMapper.findSql(id);
        return list;
    }

//    @Override
//    public void delete(String id) {
//        TblOrganization org = new TblOrganization();
//        org.setOrgid(new BigDecimal(id));
//        this.tblOrganizationMapper.delete(org);
//    }

    @Override
    public List<TblOrganization> findHYAuthorize(String hyid, String pid) {
//        StringBuffer sb = new StringBuffer();
//
//        return this.tblOrganizationDAO.findBysql(sb.toString());
        return tblOrganizationMapper.findBy(hyid, pid);
    }

    @Override
    public List<TblOrganizationMySql> findMySqlHYAuthorize(String hyid, String pid) {
        //        StringBuffer sb = new StringBuffer();
//
//        return this.tblOrganizationDAO.findBysql(sb.toString());
        return tblOrganizationMySqlMapper.findBy(hyid, pid);
    }

    @Override
    public Map<String, Object> findAllHYOrg(Integer pageNumber, Integer pageSize) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTlist(tblOrganizationMapper.selectListByPageInfo(pageInfo));
            pageInfo.setTotalRecord(tblOrganizationMapper.selectCountByPageInfo(pageInfo));
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("pageInfo", pageInfo);
            return resultMap;
        } else {
            PageInfo<TblOrganizationMySql> pageInfo = new PageInfo<TblOrganizationMySql>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTlist(tblOrganizationMySqlMapper.selectListByPageInfo(pageInfo));
            pageInfo.setTotalRecord(tblOrganizationMySqlMapper.selectCountByPageInfo(pageInfo));
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("pageInfo", pageInfo);
            return resultMap;
        }
    }

    @Override
    public Map<String, Object> findAllHYOrgs(Integer pageNumber, Integer pageSize, String pid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);

            pageInfo.setTlist(tblOrganizationMapper.selectListByPid(pageInfo, pid));
            pageInfo.setTotalRecord(tblOrganizationMapper.selectCountByPid(pageInfo, pid));
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("data", pageInfo);
            return resultMap;
        } else {
            PageInfo<TblOrganizationMySql> pageInfo = new PageInfo<TblOrganizationMySql>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);

            pageInfo.setTlist(tblOrganizationMySqlMapper.selectListByPid(pageInfo, pid));
            pageInfo.setTotalRecord(tblOrganizationMySqlMapper.selectCountByPid(pageInfo, pid));
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("data", pageInfo);
            return resultMap;
        }
    }

    @Override
    public Map<String, Object> findAllOrgBM(Integer pageNumber, Integer pageSize, Find find, BigDecimal pid) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();

        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTlist(tblOrganizationMapper.selectListByPageInfoOrgid(pageInfo, find, pid));
        pageInfo.setTotalRecord(tblOrganizationMapper.selectCountPage(pageInfo, find, pid));
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }

    @Override
    public Map<String, Object> findAllMySqlOrgBM(Integer pageNumber, Integer pageSize, Find find, BigDecimal pid) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblOrganizationMySql> pageInfo = new PageInfo<TblOrganizationMySql>();

        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTlist(tblOrganizationMySqlMapper.selectListByPageInfoOrgid(pageInfo, find, pid));
        pageInfo.setTotalRecord(tblOrganizationMySqlMapper.selectCountPage(pageInfo, find, pid));
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }

//    @Override
//    public Map<String, Object> findAllHYOrgPid(Integer pageNumber, Integer pageSize, String staffId,String token) {
//        Map<String,Object> resultMap = new HashMap<String, Object>(0);
//        PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
//        pageInfo.setCurrentPage(pageNumber);
//        pageInfo.setPageSize(pageSize);
//
//        Claims claims = JwtUtils.parseJwt(token);
//        Object object = claims.get("staffInfo");
//        JSONObject objJson = JSONObject.fromObject(object);
//        TblStaff staff = (TblStaff) JSONObject.toBean(objJson,TblStaff.class);
//        BigDecimal orgid = staff.getCurrentOrg().getOrgid();
//
//        try {
//            pageInfo.setTlist(tblOrganizationMapper.selectListByPageInOrgid(pageInfo,orgid));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        resultMap.put("code", "1");
//        resultMap.put("msg", "访问接口成功");
//        resultMap.put("data", pageInfo);
//        return resultMap;
//    }

    @Override
    public Map<String, Object> getSJZYK(TblOrganization organization, String wpzjk, Integer currentPage, Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
        if (pageSize != null) {
            pageInfo.setPageSize(pageSize);
        }
        pageInfo.setCurrentPage(currentPage);
        try {
            pageInfo.setTlist(tblOrganizationMapper.selectListByPageInfoo(pageInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }

    @Override
    public Map<String, Object> getMySqlSJZYK(TblOrganizationMySql organization, String wpzjk, Integer currentPage, Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblOrganizationMySql> pageInfo = new PageInfo<TblOrganizationMySql>();
        if (pageSize != null) {
            pageInfo.setPageSize(pageSize);
        }
        pageInfo.setCurrentPage(currentPage);
        try {
            pageInfo.setTlist(tblOrganizationMySqlMapper.selectListByPageInfoo(pageInfo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }

    //组织架构标记
    @Override
    public List<Tree> getNodeAllbm(BigDecimal nodeId) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            //return (List<Tree>) tblOrganizationMapper.selectByPrimaryKey(nodeId);
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGTREEDEPTLIST + nodeId)) {
                    String str = jedis.get(RedisFinalUtis.ORGTREEDEPTLIST + nodeId);
                    List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                    return trees;
                } else {
                    List<Tree> trees = new ArrayList<Tree>();
                    List<Tree> children = new ArrayList<Tree>();
                    List<TblOrganization> list = tblOrganizationMapper.findByNodeId(nodeId);
                    for (TblOrganization tblOrganization : list) {
                        // Set<TblOrganization> chil = tblOrganization.getChildren();
                        Set<TblOrganization> chil = tblOrganizationMapper.findByfatherorgId(tblOrganization.getOrgid());
                        children = getNoteTreesbm(chil);
                        Tree tree = new Tree();
                        tree.setChildren(children);
                        tree.setName(tblOrganization.getOrgname());
                        tree.setId(tblOrganization.getOrgid());
                        tree.setpId(tblOrganization.getFatherorgid());
                        tree.setOpen(true);
                        tree.setIsParent(chil.size() > 0 ? true : false);
                        trees.add(tree);
                    }
                    return trees;
                }
            } finally {
                JedisUtil.returnResource(jedis);
            }
        } else {
            //return (List<Tree>) tblOrganizationMapper.selectByPrimaryKey(nodeId);
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGTREEDEPTLIST + nodeId)) {
                    String str = jedis.get(RedisFinalUtis.ORGTREEDEPTLIST + nodeId);
                    List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                    return trees;
                } else {
                    List<Tree> trees = new ArrayList<Tree>();
                    List<Tree> children = new ArrayList<Tree>();
                    List<TblOrganizationMySql> list = tblOrganizationMySqlMapper.findByNodeId(nodeId);
                    for (TblOrganizationMySql tblOrganization : list) {
                        // Set<TblOrganization> chil = tblOrganization.getChildren();
                        Set<TblOrganizationMySql> chil = tblOrganizationMySqlMapper.findByfatherorgId(tblOrganization.getOrgid());
                        children = getMySqlNoteTreesbm(chil);
                        Tree tree = new Tree();
                        tree.setChildren(children);
                        tree.setName(tblOrganization.getOrgname());
                        tree.setId(tblOrganization.getOrgid());
                        tree.setpId(tblOrganization.getFatherorgid());
                        tree.setOpen(true);
                        tree.setIsParent(chil.size() > 0 ? true : false);
                        trees.add(tree);
                    }
                    return trees;
                }
            } finally {
                JedisUtil.returnResource(jedis);
            }
        }
    }

//    @Override
//    public List<Tree> getNodeAllbm(BigDecimal nodeId) {
//        //return (List<Tree>) tblOrganizationMapper.selectByPrimaryKey(nodeId);
//        Jedis jedis = JedisUtil.getJedis();
//        try{
//            if(jedis.exists(RedisFinalUtis.ORGTREEDEPTLIST+nodeId)){
//                String str = jedis.get(RedisFinalUtis.ORGTREEDEPTLIST+nodeId);
//                List<Tree> trees  = (List<Tree>) JSONArray.parseArray(str, Tree.class);
//                return trees;
//            }else{
//                List<Tree> trees = new ArrayList<Tree>();
//                List<Tree> children = new ArrayList<Tree>();
//                List<TblOrganization> list = tblOrganizationMapper.findByNodeId(nodeId);
//                for (TblOrganization tblOrganization : list) {
//                    Set<TblOrganization> chil = tblOrganizationMapper.findByfatherorgId(tblOrganization.getOrgid());
//                    for (TblOrganization tblOrganization2 : chil) {
//                        if (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() != 0) continue;
//                        Tree tree = new Tree();
//                        tree.setName(tblOrganization2.getOrgname());
//                        tree.setId(tblOrganization2.getOrgid());
//                        tree.setpId(tblOrganization2.getFatherorgid());
//                        tree.setOpen(true);
//                        tree.setIsParent(getChildrenByCommpany1(tblOrganizationMapper.findByfatherorgId(tblOrganization2.getOrgid())));
//                        children.add(tree);
//                    }
//                    Tree tree = new Tree();
//                    tree.setChildren(children);
//                    tree.setName(tblOrganization.getOrgname());
//                    tree.setId(tblOrganization.getOrgid());
//                    tree.setpId(tblOrganization.getFatherorgid());
//                    tree.setOpen(true);
//                    tree.setIsParent(chil.size() > 0 ? true : false);
//                    trees.add(tree);
//                }
//                return trees;
//            }
//        }finally{
//            JedisUtil.returnResource(jedis);
//        }
//    }

    private List<Tree> getNoteTreesbm(Set<TblOrganization> chil) {
        List<Tree> children = new ArrayList<Tree>();
        for (TblOrganization tblOrganization2 : chil) {
            if (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() != 0) {
                continue;
            }
            if (tblOrganization2.getStatus() != null && tblOrganization2.getStatus() == 0) {
                Tree tree = new Tree();
                Set<TblOrganization> chil2 = tblOrganizationMapper.findByfatherorgId(tblOrganization2.getOrgid());
                if (chil2.size() > 0) {
                    List<Tree> children1 = new ArrayList<Tree>();
                    children1 = getNoteTrees(chil2);
                    tree.setChildren(children1);
                }
                tree.setName(tblOrganization2.getOrgname());
                tree.setId(tblOrganization2.getOrgid());
                tree.setpId(tblOrganization2.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(chil2.size() > 0 ? true : false);
                children.add(tree);
            }
        }
        return children;
    }

    private List<Tree> getMySqlNoteTreesbm(Set<TblOrganizationMySql> chil) {
        List<Tree> children = new ArrayList<Tree>();
        for (TblOrganizationMySql tblOrganization2 : chil) {
            if (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() != 0) {
                continue;
            }
            if (tblOrganization2.getStatus() != null && tblOrganization2.getStatus() == 0) {
                Tree tree = new Tree();
                Set<TblOrganizationMySql> chil2 = tblOrganizationMySqlMapper.findByfatherorgId(tblOrganization2.getOrgid());
                if (chil2.size() > 0) {
                    List<Tree> children1 = new ArrayList<Tree>();
                    children1 = getMySqlNoteTrees(chil2);
                    tree.setChildren(children1);
                }
                tree.setName(tblOrganization2.getOrgname());
                tree.setId(tblOrganization2.getOrgid());
                tree.setpId(tblOrganization2.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(chil2.size() > 0 ? true : false);
                children.add(tree);
            }
        }
        return children;
    }

    @Override
    public String findOrgByAllJT(String orgid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            return String.valueOf(tblOrganizationMapper.selectOrgid(orgid));
        } else {
            return String.valueOf(tblOrganizationMySqlMapper.selectOrgid(orgid));
        }
    }

//    @Override
//    public String findOrgByAll(String orgid) {
//        return null;
//    }

    //    @Override
//    public String findOrgByAll(String orgid) {
//        return String.valueOf(tblOrganizationMapper.selectById(orgid));
//    }
    @Override
    public String findOrgByAll(String orgid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGROOTSTR + orgid)) {
                    String str = jedis.get(RedisFinalUtis.ORGROOTSTR + orgid);
                    return str;
                } else {
                    List<TblOrganization> organizations = tblOrganizationMapper.findByOrg(orgid);
                    List<TblOrganization> list = new ArrayList<TblOrganization>();
                    walkDepartmentList(organizations, list);
                    Map nodeList = new LinkedHashMap();
                    Node root = null;
                    Integer str = null;
                    String pc = ",";
                    for (int i = 0; i < list.size(); i++) {
                        TblOrganization tbl = list.get(i);
                        //Map dataRecord = (Map) it.next();
                        if (tbl.getStatus() != null && tbl.getStatus() == 1) {
                            continue;
                        }
                        if (i > 0 && tbl.getOrgtype() != null && tbl.getOrgtype() != 0 && !tbl.getOrgid().toString().equals(orgid)) {
                            pc += tbl.getOrgid().toString() + ",";
                            continue;
                        }
                        if (pc.contains("," + tbl.getFatherorgid().toString() + ",")) {
                            pc += tbl.getOrgid().toString() + ",";
                            continue;
                        }
                        Node node = new Node();
                        node.id = tbl.getOrgid().toString();
                        node.text = tbl.getOrgname().toString();
                        node.parentId = null;
                        if (null != tbl.getFatherorgid()) {
                            node.parentId = tbl.getFatherorgid().toString();
                        }
                        for (int k = 0; k < list.size(); k++) {
                            TblOrganization copyMap = list.get(k);
                            if (copyMap.getStatus() == null || copyMap.getStatus() == 1) {
                                continue;
                            }
                            if (copyMap.getFatherorgid() != null) {
                                if (copyMap.getFatherorgid().toString().equals(node.id) && copyMap.getOrgtype() == 0) {
                                    node.state = "open";//closed
                                    break;
                                }
                                if (copyMap.getFatherorgid().toString().equals(node.id) && copyMap.getOrgtype() > 0 && copyMap.getOrgtype() < 100) {
                                    node.state = "open";
                                    break;
                                }
                            }
                        }
                        if (tbl.getFatherorgid().toString().equals("-1")) {
                            node.state = "open";
                        }
                        nodeList.put(node.id, node);
                    }
                    Set entrySet = nodeList.entrySet();
                    for (Iterator it = entrySet.iterator(); it.hasNext(); ) {
                        Node node = (Node) ((Map.Entry) it.next()).getValue();
                        if (node.parentId == null || node.parentId.equals("") || node.parentId.equals("0") || nodeList.get(node.parentId) == null) {
                            root = node;
                        } else {
                            ((Node) nodeList.get(node.parentId)).addChild(node);
                        }
                    }

                    return root.toString();
                }
            } finally {
                JedisUtil.returnResource(jedis);
            }
        } else {
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGROOTSTR + orgid)) {
                    String str = jedis.get(RedisFinalUtis.ORGROOTSTR + orgid);
                    return str;
                } else {
                    List<TblOrganizationMySql> organizations = tblOrganizationMySqlMapper.findByOrg(orgid);
                    List<TblOrganizationMySql> list = new ArrayList<TblOrganizationMySql>();
                    walkDepartmentMySqlList(organizations, list);
                    Map nodeList = new LinkedHashMap();
                    Node root = null;
                    Integer str = null;
                    String pc = ",";
                    for (int i = 0; i < list.size(); i++) {
                        TblOrganizationMySql tbl = list.get(i);
                        //Map dataRecord = (Map) it.next();
                        if (tbl.getStatus() != null && tbl.getStatus() == 1) {
                            continue;
                        }
                        if (i > 0 && tbl.getOrgtype() != null && tbl.getOrgtype() != 0 && !tbl.getOrgid().toString().equals(orgid)) {
                            pc += tbl.getOrgid().toString() + ",";
                            continue;
                        }
                        if (pc.contains("," + tbl.getFatherorgid().toString() + ",")) {
                            pc += tbl.getOrgid().toString() + ",";
                            continue;
                        }
                        Node node = new Node();
                        node.id = tbl.getOrgid().toString();
                        node.text = tbl.getOrgname().toString();
                        node.parentId = null;
                        if (null != tbl.getFatherorgid()) {
                            node.parentId = tbl.getFatherorgid().toString();
                        }
                        for (int k = 0; k < list.size(); k++) {
                            TblOrganizationMySql copyMap = list.get(k);
                            if (copyMap.getStatus() == null || copyMap.getStatus() == 1) {
                                continue;
                            }
                            if (copyMap.getFatherorgid() != null) {
                                if (copyMap.getFatherorgid().toString().equals(node.id) && copyMap.getOrgtype() == 0) {
                                    node.state = "open";//closed
                                    break;
                                }
                                if (copyMap.getFatherorgid().toString().equals(node.id) && copyMap.getOrgtype() > 0 && copyMap.getOrgtype() < 100) {
                                    node.state = "open";
                                    break;
                                }
                            }
                        }
                        if (tbl.getFatherorgid().toString().equals("-1")) {
                            node.state = "open";
                        }
                        nodeList.put(node.id, node);
                    }
                    Set entrySet = nodeList.entrySet();
                    for (Iterator it = entrySet.iterator(); it.hasNext(); ) {
                        Node node = (Node) ((Map.Entry) it.next()).getValue();
                        if (node.parentId == null || node.parentId.equals("") || node.parentId.equals("0") || nodeList.get(node.parentId) == null) {
                            root = node;
                        } else {
                            ((Node) nodeList.get(node.parentId)).addChild(node);
                        }
                    }

                    return root.toString();
                }
            } finally {
                JedisUtil.returnResource(jedis);
            }
        }

    }

    private static void walkDepartmentList(Collection<TblOrganization> toList, List<TblOrganization> list) {
        for (TblOrganization top : toList) {
            if (top.getStatus() != null && top.getStatus() == 0) {
                //顶级部门
                TblOrganization copy = new TblOrganization();//使用副本，因为原对象在Session中
                copy.setOrgid(top.getOrgid());
                copy.setOrgname(top.getOrgname());
                copy.setFatherorgid(top.getFatherorgid());
                copy.setOrgtype(top.getOrgtype());
                copy.setStatus(top.getStatus());
                list.add(copy);
                //子树
                walkDepartmentList(top.getChildren(), list);
            }
        }

    }

    private static void walkDepartmentMySqlList(Collection<TblOrganizationMySql> toList, List<TblOrganizationMySql> list) {
        for (TblOrganizationMySql top : toList) {
            if (top.getStatus() != null && top.getStatus() == 0) {
                //顶级部门
                TblOrganizationMySql copy = new TblOrganizationMySql();//使用副本，因为原对象在Session中
                copy.setOrgid(top.getOrgid());
                copy.setOrgname(top.getOrgname());
                copy.setFatherorgid(top.getFatherorgid());
                copy.setOrgtype(top.getOrgtype());
                copy.setStatus(top.getStatus());
                list.add(copy);
                //子树
                walkDepartmentMySqlList(top.getChildren(), list);
            }
        }

    }

    @Override
    public Map<String, Object> findAllCommpanyPageBeanGS(Integer pageNumber, Integer pageSize, String staffId, String token, Find find, BigDecimal orgId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Jedis jedis = null;
        try {
            PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
//            TblStaffUtil staff = userProvider.get();
//            BigDecimal orgid = orgId==null ? staff.getCurrentOrg().getOrgid() : orgId;

            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTlist(tblOrganizationMapper.findByPageBean(pageInfo, orgId, find));
            pageInfo.setTotalRecord(tblOrganizationMapper.findByPage(pageInfo, orgId, find));
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                JedisUtil.returnResource(jedis);
            }
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> findAllMySqlCommpanyPageBeanGS(Integer pageNumber, Integer pageSize, String staffId, String token, Find find, BigDecimal orgId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Jedis jedis = null;
        try {
            PageInfo<TblOrganizationMySql> mySqlPageInfo = new PageInfo<TblOrganizationMySql>();
//            TblStaffUtil staff = userProvider.get();
//            BigDecimal orgid = orgId==null ? staff.getCurrentOrg().getOrgid() : orgId;

            mySqlPageInfo.setCurrentPage(pageNumber);
            mySqlPageInfo.setPageSize(pageSize);
            mySqlPageInfo.setTlist(tblOrganizationMySqlMapper.findByPageBean(mySqlPageInfo, orgId, find));
            mySqlPageInfo.setTotalRecord(tblOrganizationMySqlMapper.findByPage(mySqlPageInfo, orgId, find));
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", mySqlPageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                JedisUtil.returnResource(jedis);
            }
        }
        return resultMap;
    }


    @Override
    public Map<String, Object> findAllCommpanyPageBeanGSXj(Integer startIndex, Integer pageSize, String staffId, Find find, String token, String orgId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Jedis jedis = null;

        try {
            if (token == null && staffId == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            if (token == null && staffId != null) {
                jedis = JedisUtil.getJedis();
                token = jedis.get(staffId);
            }
            Claims claims = JwtUtils.parseJwt(token);
            Object object = claims.get("staffInfo");
            JSONObject objJson = JSONObject.fromObject(object);
            TblStaff staff = (TblStaff) JSONObject.toBean(objJson, TblStaff.class);

            PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
            if (pageSize != null) {
                pageInfo.setPageSize(pageSize);
            }
            TblOrganization organization = new TblOrganization();
            organization.setOrderid(staff.getCurrentOrg().getOrgid());
            pageInfo.setTlist(tblOrganizationMapper.findAllCommpanyPageBeanGSXj(pageInfo, find, orgId));
            pageInfo.setTotalRecord(tblOrganizationMapper.findAllCommpanyPageBeanGS(pageInfo, find, orgId));
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
        } finally {
            if (jedis != null) {
                JedisUtil.returnResource(jedis);
            }
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> findAllMySqlCommpanyPageBeanGSXj(Integer startIndex, Integer pageSize, String staffId, FindMySql find, String token, String orgId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Jedis jedis = null;

        try {
            if (token == null && staffId == null) {
                resultMap.put("code", "0");
                resultMap.put("msg", "用户已失效！");
                return resultMap;
            }
            if (token == null && staffId != null) {
                jedis = JedisUtil.getJedis();
                token = jedis.get(staffId);
            }
            Claims claims = JwtUtils.parseJwt(token);
            Object object = claims.get("staffInfo");
            JSONObject objJson = JSONObject.fromObject(object);
            TblStaff staff = (TblStaff) JSONObject.toBean(objJson, TblStaff.class);

            PageInfo<TblOrganizationMySql> mySqlPageInfo = new PageInfo<TblOrganizationMySql>();
            if (pageSize != null) {
                mySqlPageInfo.setPageSize(pageSize);
            }
            TblOrganizationMySql organization = new TblOrganizationMySql();
            organization.setOrderid(staff.getCurrentOrg().getOrgid());
            mySqlPageInfo.setTlist(tblOrganizationMySqlMapper.findAllCommpanyPageBeanGSXj(mySqlPageInfo, find, orgId));
            mySqlPageInfo.setTotalRecord(tblOrganizationMySqlMapper.findAllCommpanyPageBeanGS(mySqlPageInfo, find, orgId));
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", mySqlPageInfo);
        } finally {
            if (jedis != null) {
                JedisUtil.returnResource(jedis);
            }
        }
        return resultMap;
    }

    @Override
    public List<Tree> getJTTreeGS(BigDecimal nodeId) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            List<Tree> trees = new ArrayList();
            List<TblOrganization> list = this.tblOrganizationMapper.getNodesaGS(nodeId);
            Iterator var4 = list.iterator();

            while (true) {
                TblOrganization tblOrganization;
                do {
                    if (!var4.hasNext()) {
                        return trees;
                    }

                    tblOrganization = (TblOrganization) var4.next();
                } while (tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype() == 0);

                Tree tree = new Tree();
                tree.setName(tblOrganization.getOrgname());
                tree.setId(tblOrganization.getOrgid());
                tree.setpId(tblOrganization.getFatherorgid());
                tree.setOpen(true);
                // tree.setIsParent(this.getChildrenByCommpany(tblOrganization.getChildren()));
                trees.add(tree);
            }
        } else {
            List<Tree> trees = new ArrayList();
            List<TblOrganizationMySql> list = this.tblOrganizationMySqlMapper.getNodesaGS(nodeId);
            Iterator var4 = list.iterator();

            while (true) {
                TblOrganizationMySql tblOrganization;
                do {
                    if (!var4.hasNext()) {
                        return trees;
                    }

                    tblOrganization = (TblOrganizationMySql) var4.next();
                } while (tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype() == 0);

                Tree tree = new Tree();
                tree.setName(tblOrganization.getOrgname());
                tree.setId(tblOrganization.getOrgid());
                tree.setpId(tblOrganization.getFatherorgid());
                tree.setOpen(true);
                // tree.setIsParent(this.getChildrenByCommpany(tblOrganization.getChildren()));
                trees.add(tree);
            }
        }
    }

    //    @Override
//    public List<Tree> getJTNodeAllGS(BigDecimal nodeId) {
//        List<Tree> trees = new ArrayList();
//        List<Tree> children = new ArrayList();
//        List<TblOrganization> list = this.tblOrganizationMapper.findByNodeId(nodeId);
//        Iterator var6 = list.iterator();
//
//        label28:
//        while(var6.hasNext()) {
//            TblOrganization tblOrganization = (TblOrganization)var6.next();
//            //Set<TblOrganization> chil = tblOrganization.getChildren();
//            Set<TblOrganization> chil = tblOrganizationMapper.findByfatherorgId(tblOrganization.getOrgid());
//            Iterator var9 = chil.iterator();
//
//            while(true) {
//                TblOrganization tblOrganization2;
//                do {
//                    if (!var9.hasNext()) {
//                        Tree tree = new Tree();
//                        tree.setChildren(children);
//                        tree.setName(tblOrganization.getOrgname());
//                        tree.setId(tblOrganization.getOrgid());
//                        tree.setpId(tblOrganization.getFatherorgid());
//                        tree.setOpen(true);
//                        tree.setIsParent(this.getChildrenByCommpany(tblOrganization.getChildren()));
//                        trees.add(tree);
//                        continue label28;
//                    }
//
//                    tblOrganization2 = (TblOrganization)var9.next();
//                } while(tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() == 0);
//
//                Tree tree = new Tree();
//                tree.setName(tblOrganization2.getOrgname());
//                tree.setId(tblOrganization2.getOrgid());
//                tree.setpId(tblOrganization2.getFatherorgid());
//                tree.setOpen(true);
//                tree.setIsParent(this.getChildrenByCommpany(tblOrganization2.getChildren()));
//                children.add(tree);
//            }
//        }
//
//        return trees;
//    }
//公司管理标记
    public List<Tree> getJTNodeAllGS(BigDecimal nodeId) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            List<Tree> trees = new ArrayList<Tree>();
            List<Tree> children = new ArrayList<Tree>();
            List<TblOrganization> list = tblOrganizationMapper.findByNodeId(nodeId);
            for (TblOrganization tblOrganization : list) {
                // Set<TblOrganization> chil = tblOrganization.getChildren();
                Set<TblOrganization> chil = tblOrganizationMapper.findByfatherorgId(tblOrganization.getOrgid());
                for (TblOrganization tblOrganization2 : chil) {
                    if (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() == 0) continue;
                    Tree tree = new Tree();
                    tree.setName(tblOrganization2.getOrgname());
                    tree.setId(tblOrganization2.getOrgid());
                    tree.setpId(tblOrganization2.getFatherorgid());
                    tree.setOpen(true);
                    tree.setIsParent(getChildrenByCommpany(tblOrganizationMapper.findByfatherorgId(tblOrganization2.getOrgid())));
                    children.add(tree);
                }
                Tree tree = new Tree();
                tree.setChildren(children);
                tree.setName(tblOrganization.getOrgname());
                tree.setId(tblOrganization.getOrgid());
                tree.setpId(tblOrganization.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(getChildrenByCommpany(chil));
                trees.add(tree);
            }
            return trees;
        } else {
            List<Tree> trees = new ArrayList<Tree>();
            List<Tree> children = new ArrayList<Tree>();
            List<TblOrganizationMySql> list = tblOrganizationMySqlMapper.findByNodeId(nodeId);
            for (TblOrganizationMySql tblOrganization : list) {
                // Set<TblOrganization> chil = tblOrganization.getChildren();
                Set<TblOrganizationMySql> chil = tblOrganizationMySqlMapper.findByfatherorgId(tblOrganization.getOrgid());
                for (TblOrganizationMySql tblOrganization2 : chil) {
                    if (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() == 0) continue;
                    Tree tree = new Tree();
                    tree.setName(tblOrganization2.getOrgname());
                    tree.setId(tblOrganization2.getOrgid());
                    tree.setpId(tblOrganization2.getFatherorgid());
                    tree.setOpen(true);
                    tree.setIsParent(getMySqlChildrenByCommpany(tblOrganizationMySqlMapper.findByfatherorgId(tblOrganization2.getOrgid())));
                    children.add(tree);
                }
                Tree tree = new Tree();
                tree.setChildren(children);
                tree.setName(tblOrganization.getOrgname());
                tree.setId(tblOrganization.getOrgid());
                tree.setpId(tblOrganization.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(getMySqlChildrenByCommpany(chil));
                trees.add(tree);
            }
            return trees;
        }

    }


    @Override
    public Map<String, Object> findAllCommpanyPageBean(Integer pageNumber, Integer pageSize, BigDecimal pid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                pageInfo.setTlist(tblOrganizationMapper.findAllCommpanyPageBeanStaffid(pageInfo, pid));
                pageInfo.setTotalRecord(tblOrganizationMapper.findAllCommpanyPage(pageInfo, pid));
                resultMap.put("code", "1");
                resultMap.put("msg", "访问接口成功");
                resultMap.put("data", pageInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                PageInfo<TblOrganizationMySql> pageInfo = new PageInfo<TblOrganizationMySql>();
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                pageInfo.setTlist(tblOrganizationMySqlMapper.findAllCommpanyPageBeanStaffid(pageInfo, pid));
                pageInfo.setTotalRecord(tblOrganizationMySqlMapper.findAllCommpanyPage(pageInfo, pid));
                resultMap.put("code", "1");
                resultMap.put("msg", "访问接口成功");
                resultMap.put("data", pageInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultMap;
        }
    }

    @Override
    public void saveModify(TblOrganization org) {
        this.tblOrganizationMapper.saveOrg(org);
    }

    @Override
    public void saveMySqlModify(TblOrganizationMySql org) {
        this.tblOrganizationMySqlMapper.saveOrg(org);
    }

    @Override
    public TblOrganization findByOrgid(String orgid) {
        TblOrganization togz = tblOrganizationMapper.selectOrgid(orgid);
        return togz;
    }

    @Override
    public TblOrganizationMySql findByMySqlOrgid(String orgid) {
        TblOrganizationMySql togz = tblOrganizationMySqlMapper.selectOrgid(orgid);
        return togz;
    }

    @Override
    public String deleteOrg(TblOrganization orgid) {
        JsonBean jsonBean = new JsonBean();
        if (tblOrganizationMapper.selectFindOrgid(orgid) == tblStaffMapper.selectFindOrgid(orgid)) {
            jsonBean.setCode(1);
            jsonBean.setMsg("不可以删除");
        } else {
            tblStaffMapper.deleteOrgid(orgid);
            tblOrganizationMapper.deleteOrgid(orgid);
            jsonBean.setCode(1);
            jsonBean.setMsg("删除成功");
        }
        return JSON.toJSONString(jsonBean);
    }

    @Override
    public String deleteMySqlOrg(TblOrganizationMySql orgid) {
        JsonBean jsonBean = new JsonBean();
        if (tblOrganizationMySqlMapper.selectFindOrgid(orgid) == tblStaffMySqlMapper.selectFindOrgid(orgid)) {
            jsonBean.setCode(1);
            jsonBean.setMsg("不可以删除");
        } else {
            tblStaffMySqlMapper.deleteOrgid(orgid);
            tblOrganizationMySqlMapper.deleteOrgid(orgid);
            jsonBean.setCode(1);
            jsonBean.setMsg("删除成功");
        }
        return JSON.toJSONString(jsonBean);
    }

    @Override
    public TblOrganization findByIdOrgid(String pid) {
        TblOrganization org = tblOrganizationMapper.findIdOrgid(pid);
        return org;
    }

    @Override
    public TblOrganizationMySql findByMySqlIdOrgid(String pid) {
        TblOrganizationMySql org = tblOrganizationMySqlMapper.findIdOrgid(pid);
        return org;
    }

    @Override
    public TblOrganization findByid(String id) {
        TblOrganization org = tblOrganizationMapper.findByid(id);
        return org;
    }

    @Override
    public TblOrganizationMySql findByMySqlid(String id) {
        TblOrganizationMySql org = tblOrganizationMySqlMapper.findByid(id);
        return org;
    }

    @Override
    public TblOrganization findId(String id) {
        TblOrganization tbl = tblOrganizationMapper.findByid(id);
        return tbl;
    }

    @Override
    public TblOrganizationMySql findMySqlId(String id) {
        TblOrganizationMySql tbl = tblOrganizationMySqlMapper.findByid(id);
        return tbl;
    }

    @Override
    public List<TblOrganization> findOrgByType(String pid) {
        TblOrganization tbl = tblOrganizationMapper.findByid(pid);
        return null;
    }

    @Override
    public List<TblOrganizationMySql> findOrgByMySqlType(String pid) {
        TblOrganizationMySql tbl = tblOrganizationMySqlMapper.findByid(pid);
        return null;
    }

    @Override
    public List<TblOrganization> findPid(String pid) {
        TblOrganization tbl = tblOrganizationMapper.findPid(pid);
        List<TblOrganization> tblOrganizationList = new ArrayList<>();
        tblOrganizationList.add(tbl);
        return tblOrganizationList;
    }

    @Override
    public List<TblOrganizationMySql> findMySqlPid(String pid) {
        TblOrganizationMySql tbl = tblOrganizationMySqlMapper.findPid(pid);
        List<TblOrganizationMySql> tblOrganizationMySqlList = new ArrayList<>();
        tblOrganizationMySqlList.add(tbl);
        return tblOrganizationMySqlList;
    }

    @Override
    public TblOrganization findByStringId(String id) {
        TblOrganization tbl = tblOrganizationMapper.findByid(id);
        return null;
    }

    @Override
    public TblOrganizationMySql findByMySqlStringId(String id) {
        TblOrganizationMySql tbl = tblOrganizationMySqlMapper.findByid(id);
        return null;
    }

    @Override
    public void delete(BigDecimal orgid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            tblOrganizationMapper.deleteOrg(orgid);
        } else {
            tblOrganizationMySqlMapper.deleteOrg(orgid);
        }
    }

    @Override
    public Map<String, Object> findAllHYOrgStaffid(Integer pageNumber, Integer pageSize, String staffId, String token, String pid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);

            if (pageSize != null) {
                pageInfo.setPageSize(pageSize);
            }
            Claims claims = JwtUtils.parseJwt(token);
            Object object = claims.get("staffInfo");
            JSONObject objJson = JSONObject.fromObject(object);
            TblStaff staff = (TblStaff) JSONObject.toBean(objJson, TblStaff.class);
            //BigDecimal orgid = staff.getCurrentOrg().getOrgid();
            TblOrganization organization = new TblOrganization();
            organization.setOrderid(staff.getCurrentOrg().getOrgid());
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setCondition(organization);

            pageInfo.setTlist(tblOrganizationMapper.selectListByStaffid(pageInfo, pid));
            pageInfo.setTotalRecord(tblOrganizationMapper.selectCountByPid(pageInfo, pid));
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("pageInfo", pageInfo);
            return resultMap;
        } else {
            PageInfo<TblOrganizationMySql> pageInfo = new PageInfo<TblOrganizationMySql>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);

            if (pageSize != null) {
                pageInfo.setPageSize(pageSize);
            }
            Claims claims = JwtUtils.parseJwt(token);
            Object object = claims.get("staffInfo");
            JSONObject objJson = JSONObject.fromObject(object);
            TblStaff staff = (TblStaff) JSONObject.toBean(objJson, TblStaff.class);
            //BigDecimal orgid = staff.getCurrentOrg().getOrgid();
            TblOrganizationMySql organization = new TblOrganizationMySql();
            organization.setOrderid(staff.getCurrentOrg().getOrgid());
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setCondition(organization);

            pageInfo.setTlist(tblOrganizationMySqlMapper.selectListByStaffid(pageInfo, pid));
            pageInfo.setTotalRecord(tblOrganizationMySqlMapper.selectCountByPid(pageInfo, pid));
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("pageInfo", pageInfo);
            return resultMap;
        }
    }

    @Override
    public TblOrganization findByOrg(BigDecimal orgid) {
        return tblOrganizationMapper.selectByOrgid(orgid);
    }

    @Override
    public TblOrganizationMySql findByMySqlOrg(BigDecimal orgid) {
        return tblOrganizationMySqlMapper.selectByOrgid(orgid);
    }

    @Override
    public Map<String, Object> finreportMenuList(Integer pageNumber, Integer pageSize, String type, String token, String orgid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);

            try {
                PageInfo<TblBiReportMenu> pageInfo = new PageInfo<TblBiReportMenu>();

                TblStaffUtil user = userProvider.get();
                if (orgid.equals("")||orgid==null) {
                    orgid = user.getCurrentOrg().getOrgid().toString();
                }
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                //pageInfo.setCondition(organization);
                pageInfo.setTlist(tblBiReportMenuMapper.selectType(pageInfo, type, new BigDecimal(orgid)));
                pageInfo.setTotalRecord(tblBiReportMenuMapper.selectOrgid(pageInfo, type, new BigDecimal(orgid)));
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("pageInfo", pageInfo);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);

            try {
                PageInfo<TblBiReportMenuMySql> pageInfo = new PageInfo<TblBiReportMenuMySql>();

                TblStaffUtil user = userProvider.get();
                if (orgid==null||"".equals(orgid)) {
                    orgid = user.getCurrentOrg().getOrgid().toString();
                }
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                //pageInfo.setCondition(organization);
                pageInfo.setTlist(tblBiReportMenuMySqlMapper.selectType(pageInfo, type, new BigDecimal(orgid)));
                pageInfo.setTotalRecord(tblBiReportMenuMySqlMapper.selectOrgid(pageInfo, type, new BigDecimal(orgid)));
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("pageInfo", pageInfo);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
        }
    }

    @Override
    public Map<String, Object> getJTTree(Integer pageNumber, Integer pageSize, String token, String staffId) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            //List<Tree> trees = new ArrayList();

            PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();

            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTlist(tblOrganizationMapper.getNodesa(pageInfo));
            pageInfo.setTotalRecord(tblOrganizationMapper.getNode());
//        Iterator var4 = list.iterator();
//
//        while(true) {
//            TblOrganization tblOrganization;
//            do {
//                if (!var4.hasNext()) {
//                    return trees;
//                }
//
//                tblOrganization = (TblOrganization)var4.next();
//            } while(tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype() == 0);
//
//            Tree tree = new Tree();
//            tree.setName(tblOrganization.getOrgname());
//            tree.setId(tblOrganization.getOrgid());
//            tree.setpId(tblOrganization.getFatherorgid());
//            tree.setOpen(true);

//
//            // tree.setIsParent(this.getChildrenByCommpany(tblOrganization.getChildren()));
//            trees.add(tree);
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
            return resultMap;
        } else {
            //List<Tree> trees = new ArrayList();

            PageInfo<TblOrganizationMySql> pageInfo = new PageInfo<TblOrganizationMySql>();

            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTlist(tblOrganizationMySqlMapper.getNodesa(pageInfo));
            pageInfo.setTotalRecord(tblOrganizationMySqlMapper.getNode());
//        Iterator var4 = list.iterator();
//
//        while(true) {
//            TblOrganization tblOrganization;
//            do {
//                if (!var4.hasNext()) {
//                    return trees;
//                }
//
//                tblOrganization = (TblOrganization)var4.next();
//            } while(tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype() == 0);
//
//            Tree tree = new Tree();
//            tree.setName(tblOrganization.getOrgname());
//            tree.setId(tblOrganization.getOrgid());
//            tree.setpId(tblOrganization.getFatherorgid());
//            tree.setOpen(true);

//
//            // tree.setIsParent(this.getChildrenByCommpany(tblOrganization.getChildren()));
//            trees.add(tree);
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
            return resultMap;
        }

    }

    @Override
    public Map<String, Object> getJTNodeAll(Integer pageNumber, Integer pageSize, BigDecimal nodeId) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            PageInfo<TblOrganization> pageInfo = new PageInfo<TblOrganization>();

            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTlist(tblOrganizationMapper.getNodesaNodeId(pageInfo, nodeId));
            pageInfo.setTotalRecord(tblOrganizationMapper.getNodeNodeId(nodeId));
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
            return resultMap;
        } else {
            PageInfo<TblOrganizationMySql> pageInfo = new PageInfo<TblOrganizationMySql>();

            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTlist(tblOrganizationMySqlMapper.getNodesaNodeId(pageInfo, nodeId));
            pageInfo.setTotalRecord(tblOrganizationMySqlMapper.getNodeNodeId(nodeId));
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
            return resultMap;
        }
    }

    @Override
    public void modiOrganization(TblOrganization organization) {
        tblOrganizationMapper.saveModiOrganization(organization);
    }

    @Override
    public void modiOrganizationMySql(TblOrganizationMySql organizationMySql) {
        tblOrganizationMySqlMapper.saveModiOrganization(organizationMySql);
    }

    @Override
    public Integer addReturnId(TblOrganization organization) {
        return this.tblOrganizationMapper.addReturnId(organization);
    }

    @Override
    public Integer addMySqlReturnId(TblOrganizationMySql organization) {
        return this.tblOrganizationMySqlMapper.addReturnId(organization);
    }

    @Override
    public void isCompanyAddWPZJ(TblOrganization org) {
        TblOrganization o = new TblOrganization();
        o.setOrgname("外聘专家库");
        o.setOrgid(org.getOrgid());
        o.setFatherorgid(org.getOrgid());
        o.setOrgnumber(org.getOrgnumber() + "wpzj");
        o.setMemo("外聘专家库");
        o.setOrgmeno("外聘专家库");
        o.setOrgtype(0);
        o.setIszy("wpzjk");
        tblOrganizationMapper.addReturnId(o);
    }

    @Override
    public void isCompanyAddMySqlWPZJ(TblOrganizationMySql org) {
        TblOrganizationMySql o = new TblOrganizationMySql();
        o.setOrgname("外聘专家库");
        o.setOrgid(org.getOrgid());
        o.setFatherorgid(org.getOrgid());
        o.setOrgnumber(org.getOrgnumber() + "wpzj");
        o.setMemo("外聘专家库");
        o.setOrgmeno("外聘专家库");
        o.setOrgtype(0);
        o.setIszy("wpzjk");
        tblOrganizationMySqlMapper.addReturnId(o);
    }

    @Override
    public Integer addReturnOrg(TblOrganization organization) {
        return this.tblOrganizationMapper.addReturnId(organization);
    }

    @Override
    public Integer addMySqlReturnOrg(TblOrganizationMySql organization) {
        return this.tblOrganizationMySqlMapper.addReturnId(organization);
    }

    @Override
    public TblOrganization isCompanyAddWPZ(TblOrganization org) {
        TblOrganization o = new TblOrganization();
        o.setOrgname("外聘专家库");
        o.setFatherorgid(org.getOrgid());
        o.setOrgnumber(org.getOrgnumber() + "wpzj");
        o.setMemo("外聘专家库");
        o.setOrgmeno("外聘专家库");
        o.setOrgtype(0);
        o.setIszy("wpzjk");
        return this.tblOrganizationMapper.saveModiOrgan(o);
    }

    @Override
    public TblOrganizationMySql isCompanyAddMySqlWPZ(TblOrganizationMySql org) {
        TblOrganizationMySql o = new TblOrganizationMySql();
        o.setOrgname("外聘专家库");
        o.setFatherorgid(org.getOrgid());
        o.setOrgnumber(org.getOrgnumber() + "wpzj");
        o.setMemo("外聘专家库");
        o.setOrgmeno("外聘专家库");
        o.setOrgtype(0);
        o.setIszy("wpzjk");
        return this.tblOrganizationMySqlMapper.saveModiOrgan(o);
    }

    @Override
    public void saveAtion(TblOrganization organization) {
        tblOrganizationMapper.saveModiOrganization(organization);
    }

    @Override
    public void saveMySqlAtion(TblOrganizationMySql organization) {
        tblOrganizationMySqlMapper.saveModiOrganization(organization);
    }

    @Override
    public void updateZuZhi(TblOrganization organization) {
        tblOrganizationMapper.updateZuZhi(organization);
    }

    @Override
    public void updateMySqlZuZhi(TblOrganizationMySql organization) {
        tblOrganizationMySqlMapper.updateZuZhi(organization);
    }

    @Override
    public void saveAtionHangYe(TblOrganization ation) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            tblOrganizationMapper.saveAtionHangYe(ation);
        } else {
            TblOrganizationMySql ationMySql = new TblOrganizationMySql();
            BeanUtils.copyProperties(ation,ationMySql);
            tblOrganizationMySqlMapper.saveAtionHangYe(ationMySql);
        }
    }

    @Override
    public TblOrganization findOrgid(String pid) {
        return tblOrganizationMapper.selectByPid(pid);
    }

    @Override
    public TblOrganizationMySql findMySqlOrgid(String pid) {
        return tblOrganizationMySqlMapper.selectByPid(pid);
    }

    @Override
    public void updateAtionHangYe(TblOrganization organization) {
        tblOrganizationMapper.updateAtionHangYe(organization);
    }

    @Override
    public void updateMySqlAtionHangYe(TblOrganizationMySql organization) {
        tblOrganizationMySqlMapper.updateAtionHangYe(organization);
    }

    @Override
    public TblOrganization findById(String orgid) {
        TblOrganization organization = tblOrganizationMapper.selectByOrgId(new BigDecimal(orgid));
        return organization;
    }

    @Override
    public TblOrganizationMySql findByMySqlId(String orgid) {
        TblOrganizationMySql organization = tblOrganizationMySqlMapper.selectByOrgId(new BigDecimal(orgid));
        return organization;
    }

    @Override
    public List<Tree> getTree(BigDecimal nodeId) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Jedis jedis = JedisUtil.getJedis();

            List var5;
            try {
                List trees;
                if (!jedis.exists("orgdept_list_" + nodeId)) {
                    List<Tree> tres = new ArrayList();
                    tres = this.tblOrganizationMapper.getNodes(nodeId);
                    Iterator var12 = tres.iterator();

                    while (var12.hasNext()) {
                        TblOrganization tblOrganization = (TblOrganization) var12.next();
                        Tree tree = new Tree();
                        tree.setName(tblOrganization.getOrgname());
                        tree.setId(tblOrganization.getOrgid());
                        tree.setpId(tblOrganization.getFatherorgid());
                        tree.setOpen(true);
                        tree.setIsParent(tblOrganization.getChildren().size() > 0);
                        if ((tblOrganization.getOrgtype() == null || tblOrganization.getOrgtype() == 0) && (tblOrganization.getStatus() == null || tblOrganization.getStatus() != 1) && tblOrganization.getStatus() != null && tblOrganization.getStatus() == 0) {
                            tres.add(tree);
                        }
                    }

                    ArrayList var13 = (ArrayList) tres;
                    return var13;
                }

                String str = jedis.get("orgdept_list_" + nodeId);
                trees = JSONArray.parseArray(str, Tree.class);
                var5 = trees;
            } finally {
                JedisUtil.returnResource(jedis);
            }

            return var5;
        } else {
            Jedis jedis = JedisUtil.getJedis();

            List var5;
            try {
                List trees;
                if (!jedis.exists("orgdept_list_" + nodeId)) {
                    List<Tree> tres = new ArrayList();
                    tres = this.tblOrganizationMySqlMapper.getNodes(nodeId);
                    Iterator var12 = tres.iterator();

                    while (var12.hasNext()) {
                        TblOrganizationMySql tblOrganization = (TblOrganizationMySql) var12.next();
                        Tree tree = new Tree();
                        tree.setName(tblOrganization.getOrgname());
                        tree.setId(tblOrganization.getOrgid());
                        tree.setpId(tblOrganization.getFatherorgid());
                        tree.setOpen(true);
                        tree.setIsParent(tblOrganization.getChildren().size() > 0);
                        if ((tblOrganization.getOrgtype() == null || tblOrganization.getOrgtype() == 0) && (tblOrganization.getStatus() == null || tblOrganization.getStatus() != 1) && tblOrganization.getStatus() != null && tblOrganization.getStatus() == 0) {
                            tres.add(tree);
                        }
                    }

                    ArrayList var13 = (ArrayList) tres;
                    return var13;
                }

                String str = jedis.get("orgdept_list_" + nodeId);
                trees = JSONArray.parseArray(str, Tree.class);
                var5 = trees;
            } finally {
                JedisUtil.returnResource(jedis);
            }

            return var5;
        }
    }

    @Override
    public List<Tree> getNodeAll(BigDecimal nodeId) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Jedis jedis = JedisUtil.getJedis();

            try {
                List children;
                if (jedis.exists("orgdept_list_" + nodeId)) {
                    String str = jedis.get("orgdept_list_" + nodeId);
                    List<Tree> trees = JSONArray.parseArray(str, Tree.class);
                    children = trees;
                    return children;
                } else {
                    List<Tree> trees = new ArrayList();
                    new ArrayList();
                    List<TblOrganization> list = this.tblOrganizationMapper.findByNode(nodeId);
                    Iterator var7 = list.iterator();

                    while (var7.hasNext()) {
                        TblOrganization tblOrganization = (TblOrganization) var7.next();
                        if (tblOrganization.getStatus() == null || tblOrganization.getStatus() != 1) {
                            Set<TblOrganization> chil = tblOrganization.getChildren();
                            children = this.getNoteTrees(chil);
                            Tree tree = new Tree();
                            tree.setChildren(children);
                            tree.setName(tblOrganization.getOrgname());
                            tree.setId(tblOrganization.getOrgid());
                            tree.setpId(tblOrganization.getFatherorgid());
                            tree.setOpen(true);
                            tree.setIsParent(tblOrganization.getChildren().size() > 0);
                            trees.add(tree);
                        }
                    }

                    ArrayList var16 = (ArrayList) trees;
                    return var16;
                }
            } finally {
                JedisUtil.returnResource(jedis);
            }
        } else {
            Jedis jedis = JedisUtil.getJedis();

            try {
                List children;
                if (jedis.exists("orgdept_list_" + nodeId)) {
                    String str = jedis.get("orgdept_list_" + nodeId);
                    List<Tree> trees = JSONArray.parseArray(str, Tree.class);
                    children = trees;
                    return children;
                } else {
                    List<Tree> trees = new ArrayList();
                    new ArrayList();
                    List<TblOrganizationMySql> list = this.tblOrganizationMySqlMapper.findByNode(nodeId);
                    Iterator var7 = list.iterator();

                    while (var7.hasNext()) {
                        TblOrganizationMySql tblOrganization = (TblOrganizationMySql) var7.next();
                        if (tblOrganization.getStatus() == null || tblOrganization.getStatus() != 1) {
                            Set<TblOrganizationMySql> chil = tblOrganization.getChildren();
                            children = this.getMySqlNoteTrees(chil);
                            Tree tree = new Tree();
                            tree.setChildren(children);
                            tree.setName(tblOrganization.getOrgname());
                            tree.setId(tblOrganization.getOrgid());
                            tree.setpId(tblOrganization.getFatherorgid());
                            tree.setOpen(true);
                            tree.setIsParent(tblOrganization.getChildren().size() > 0);
                            trees.add(tree);
                        }
                    }

                    ArrayList var16 = (ArrayList) trees;
                    return var16;
                }
            } finally {
                JedisUtil.returnResource(jedis);
            }
        }
    }

    @Override
    public String GetTreeOrg(BigDecimal id, Map<BigDecimal, Object> map, String orgid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            List<Tree> list = new ArrayList();
            List<TblManageRight> root = this.tblManageRightDAO.getTreeRoots(id.toString());
            List<TblManageRight> list1 = this.tblManageRightDAO.getOrgRight(new BigDecimal(orgid));

            for (TblManageRight tblManageRight : root) {
                for (TblManageRight right : list1) {
                    if (tblManageRight.getRightid().toString().equals(right.getRightid().toString())) {
                        Tree tree = new Tree();
                        tree.setName(tblManageRight.getRightname());
                        tree.setId(tblManageRight.getRightid());
                        tree.setTarget("mainFramex");
                        tree.setChecked(map.get(tblManageRight.getRightid()) == null ? false : true);
                        tree.setpId(tblManageRight.getFatherrightid());
                        tree.setUrl("");
                        tree.setOpen(true);
                        List<Tree> childre = this.addChildren(id, tblManageRight.getRightid(), map, list1);
                        if (childre.size() > 0) {
                            tree.setChildren(childre);
                        }
                        list.add(tree);
                    }
                }
            }
            return com.alibaba.fastjson.JSONObject.toJSONString(list);
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            List<Tree> list = new ArrayList();
            List<TblManageRightMySql> root = this.tblManageRightMySqlDAO.getTreeRoots(id.toString());
            List<TblManageRightMySql> list1 = this.tblManageRightMySqlDAO.getOrgRight(new BigDecimal(orgid));

            for (TblManageRightMySql tblManageRight : root) {
                for (TblManageRightMySql right : list1) {
                    if (tblManageRight.getRightid().toString().equals(right.getRightid().toString())) {
                        Tree tree = new Tree();
                        tree.setName(tblManageRight.getRightname());
                        tree.setId(tblManageRight.getRightid());
                        tree.setTarget("mainFramex");
                        tree.setChecked(map.get(tblManageRight.getRightid()) == null ? false : true);
                        tree.setpId(tblManageRight.getFatherrightid());
                        tree.setUrl("");
                        tree.setOpen(true);
                        List<Tree> childre = this.addMySqlChildren(id, tblManageRight.getRightid(), map, list1);
                        if (childre.size() > 0) {
                            tree.setChildren(childre);
                        }
                        list.add(tree);
                    }
                }
            }
            return com.alibaba.fastjson.JSONObject.toJSONString(list);
        }

    }

    @Override
    public List<Tree> getJTTreeNodeId(BigDecimal nodeId) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            List<Tree> trees = new ArrayList();
            List<TblOrganization> list = this.tblOrganizationMapper.getNodesaGSNodeId(nodeId);
            Iterator var4 = list.iterator();

            while (true) {
                TblOrganization tblOrganization;
                do {
                    if (!var4.hasNext()) {
                        return trees;
                    }

                    tblOrganization = (TblOrganization) var4.next();
                } while (tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype() == 0);

                Tree tree = new Tree();
                tree.setName(tblOrganization.getOrgname());
                tree.setId(tblOrganization.getOrgid());
                tree.setpId(tblOrganization.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(this.getChildrenByCommpany(tblOrganization.getChildren()));
                trees.add(tree);
            }
        } else {
            List<Tree> trees = new ArrayList();
            List<TblOrganizationMySql> list = this.tblOrganizationMySqlMapper.getNodesaGSNodeId(nodeId);
            Iterator var4 = list.iterator();

            while (true) {
                TblOrganizationMySql tblOrganization;
                do {
                    if (!var4.hasNext()) {
                        return trees;
                    }

                    tblOrganization = (TblOrganizationMySql) var4.next();
                } while (tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype() == 0);

                Tree tree = new Tree();
                tree.setName(tblOrganization.getOrgname());
                tree.setId(tblOrganization.getOrgid());
                tree.setpId(tblOrganization.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(this.getMySqlChildrenByCommpany(tblOrganization.getChildren()));
                trees.add(tree);
            }
        }
    }

    @Override
    public List<TblOrganization> parentHy(String orgids) {
//        if (StringUtils.isNotBlank(orgids)) {
//            orgids = orgids.substring(0, orgids.length() - 1);
//        }
//        Set<TblOrganization> listall = new HashSet();
//        String[] ips = orgids.split(",");
//
//        for(int i = 0; i < ips.length; ++i) {
//            //Object result = this.tblOrganizationMapper.excuteFunReturnUnique("select getparentHyorgList(" + ips[i] + ")");
//            //sql = "select * from TBL_ORGANIZATION WHERE FIND_IN_SET(ORGID,'" + result + "')";
//            List<TblOrganization> list = this.tblOrganizationMapper.findByResult(ips);
//            listall.addAll(list);
//        }
        List<TblOrganization> list = this.tblOrganizationMapper.findByorgids(orgids);
        return list;
    }

    @Override
    public List<TblOrganizationMySql> parentMySqlHy(String orgids) {
//        if (StringUtils.isNotBlank(orgids)) {
//            orgids = orgids.substring(0, orgids.length() - 1);
//        }
        Set<TblOrganizationMySql> listall = new HashSet();
        String[] ips = orgids.split(",");
        for (int i = 0; i < ips.length; ++i) {
            String orgid = this.tblOrganizationMySqlMapper.excuteFunReturnUnique(ips[i]);
            List<TblOrganizationMySql> list = this.tblOrganizationMySqlMapper.findByorgid(orgid);
            listall.addAll(list);
        }

        List<TblOrganizationMySql> orgList = new ArrayList<>(listall);
        return orgList;
    }


    @Override
    public List getOrgTreeLefts(BigDecimal orgid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            String tree = "";
            List list = this.tblOrganizationMapper.findAllOrgUserLeft(orgid.toString());
            int index = 0;

            TblOrganization org;
//        for(Iterator var5 = list.iterator(); var5.hasNext(); tree = tree + "tree.nodes['" + org.getFatherorgid().toEngineeringString() + "_" + org.getOrgid().toString() + "']=\"text:" + org.getOrgname() + ";url:listorg?pageNumber=1&pid=" + org.getOrgid().toString() + "\";\n") {
//            Object o = var5.next();
//            org = (TblOrganization)o;
//            if (index == 0) {
//                tree = tree + "tree.nodes['-1_" + org.getOrgid().toString() + "']=\"text:" + org.getOrgname() + ";url:listorg?pageNumber=1&pid=" + org.getOrgid().toString() + "\";\n";
//            }
//
//            ++index;
//        }

            return list;
        } else {
            String tree = "";
            List list = this.tblOrganizationMySqlMapper.findAllOrgUserLeft(orgid.toString());
            int index = 0;

            TblOrganization org;
//        for(Iterator var5 = list.iterator(); var5.hasNext(); tree = tree + "tree.nodes['" + org.getFatherorgid().toEngineeringString() + "_" + org.getOrgid().toString() + "']=\"text:" + org.getOrgname() + ";url:listorg?pageNumber=1&pid=" + org.getOrgid().toString() + "\";\n") {
//            Object o = var5.next();
//            org = (TblOrganization)o;
//            if (index == 0) {
//                tree = tree + "tree.nodes['-1_" + org.getOrgid().toString() + "']=\"text:" + org.getOrgname() + ";url:listorg?pageNumber=1&pid=" + org.getOrgid().toString() + "\";\n";
//            }
//
//            ++index;
//        }

            return list;
        }
    }

    @Override
    public List getOrgTreeLeftsOneLevel(BigDecimal orgid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            List list = this.tblOrganizationMapper.findAllOrgUserLeftOneLevel(orgid.toString());
            return list;
        } else {
            List list = this.tblOrganizationMySqlMapper.findAllOrgUserLeftOneLevel(orgid.toString());
            return list;
        }
    }

    @Override
    public TblOrganization findByoId(BigDecimal orgid) {
        return tblOrganizationMapper.findByoId(orgid);
    }

    @Override
    public TblOrganizationMySql findByMySqloId(BigDecimal orgid) {
        return tblOrganizationMySqlMapper.findByoId(orgid);
    }


    @Override
    public TblOrganizationUtil selectFatherOrgIdByID(String orgId) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            List<TblOrganizationUtil> organization = tblOrganizationMapper.findByOrgid(orgId);
            if (organization != null && organization.size() > 0) {
                return organization.get(0);
            }
            return null;
        } else {
            List<TblOrganizationUtil> organization = tblOrganizationMySqlMapper.findByOrgId(orgId);
            if (organization != null && organization.size() > 0) {
                return organization.get(0);
            }
            return null;
        }
    }

    @Override
    public String findIniStatus(BigDecimal orgid) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            return tblOrganizationMapper.findIniStatus(orgid);
        } else {
            return tblOrganizationMySqlMapper.findIniStatus(orgid);
        }
    }

    @Override
    public TblOrganization getHYFirst() {
        List<TblOrganization> list = tblOrganizationMapper.findAllorganization();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public TblOrganizationMySql getMySqlHYFirst() {
        List<TblOrganizationMySql> list = tblOrganizationMySqlMapper.findAllorganization();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Tree> getTreeHy(BigDecimal nodeId) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGTYPELIST + nodeId)) {
                    String str = jedis.get(RedisFinalUtis.ORGTYPELIST + nodeId);
                    List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                    return trees;
                } else {
                    List<Tree> trees = new ArrayList<Tree>();
                    List<TblOrganization> list = this.tblOrganizationMapper.getNodeId(nodeId);
                    for (TblOrganization tblOrganization : list) {
                        if (tblOrganization.getStatus() != null && tblOrganization.getStatus() == 1) {
                            continue;
                        }
                        Tree tree = new Tree();
                        tree.setName(tblOrganization.getOrgname());
                        tree.setId(tblOrganization.getOrgid());
                        tree.setpId(tblOrganization.getFatherorgid());
                        tree.setOpen(true);
                        tree.setIsParent(tblOrganization.getChildren().size() > 0 ? true : false);
                        if (tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype() != 100) {
                            continue;
                        }
                        if (tblOrganization.getStatus() == null || tblOrganization.getStatus() == 0) {
                            trees.add(tree);
                        }
                    }
                    return trees;
                }
            } finally {
                JedisUtil.returnResource(jedis);
            }
        } else {
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGTYPELIST + nodeId)) {
                    String str = jedis.get(RedisFinalUtis.ORGTYPELIST + nodeId);
                    List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                    return trees;
                } else {
                    List<Tree> trees = new ArrayList<Tree>();
                    List<TblOrganizationMySql> list = this.tblOrganizationMySqlMapper.getNodeId(nodeId);
                    for (TblOrganizationMySql tblOrganization : list) {
                        if (tblOrganization.getStatus() != null && tblOrganization.getStatus() == 1) {
                            continue;
                        }
                        Tree tree = new Tree();
                        tree.setName(tblOrganization.getOrgname());
                        tree.setId(tblOrganization.getOrgid());
                        tree.setpId(tblOrganization.getFatherorgid());
                        tree.setOpen(true);
                        tree.setIsParent(tblOrganization.getChildren().size() > 0 ? true : false);
                        if (tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype() != 100) {
                            continue;
                        }
                        if (tblOrganization.getStatus() == null || tblOrganization.getStatus() == 0) {
                            trees.add(tree);
                        }
                    }
                    return trees;
                }
            } finally {
                JedisUtil.returnResource(jedis);
            }
        }
    }

    @Override
    public List<Tree> getNodeAllHy(BigDecimal nodeId) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGNODEHYTREE + nodeId)) {
                    String str = jedis.get(RedisFinalUtis.ORGNODEHYTREE + nodeId);
                    List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                    return trees;
                } else {
                    List<Tree> trees = new ArrayList<Tree>();
                    List<Tree> children = new ArrayList<Tree>();
                    List<TblOrganization> list = tblOrganizationMapper.findByNodeIdNew(nodeId);
                    for (TblOrganization tblOrganization : list) {
                        Set<TblOrganization> chil = tblOrganizationMapper.findByfatherorgId(tblOrganization.getOrgid());
                        // Set<TblOrganization> chil = tblOrganization.getChildren();
                        for (TblOrganization tblOrganization2 : chil) {
                            Tree tree = new Tree();
                            tree.setName(tblOrganization2.getOrgname());
                            tree.setId(tblOrganization2.getOrgid());
                            tree.setpId(tblOrganization2.getFatherorgid());
                            //tree.setOpen(true);
                            tree.setIsParent(tblOrganization2.getChildren().size() > 0 ? true : false);
                            if (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() != 100) {
                                continue;
                            }
                            children.add(tree);
                        }
                        Tree tree = new Tree();
                        tree.setChildren(children);
                        tree.setName(tblOrganization.getOrgname());
                        tree.setId(tblOrganization.getOrgid());
                        tree.setpId(tblOrganization.getFatherorgid());
                        tree.setOpen(true);
                        tree.setIsParent(tblOrganization.getChildren().size() > 0 ? true : false);
                        trees.add(tree);
                    }
                    return trees;
                }
            } finally {
                JedisUtil.returnResource(jedis);
            }
        } else {
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGNODEHYTREE + nodeId)) {
                    String str = jedis.get(RedisFinalUtis.ORGNODEHYTREE + nodeId);
                    List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                    return trees;
                } else {
                    List<Tree> trees = new ArrayList<Tree>();
                    List<Tree> children = new ArrayList<Tree>();
                    List<TblOrganizationMySql> list = tblOrganizationMySqlMapper.findByNodeIdNew(nodeId);
                    for (TblOrganizationMySql tblOrganization : list) {
                        Set<TblOrganizationMySql> chil = tblOrganizationMySqlMapper.findByfatherorgId(tblOrganization.getOrgid());
                        // Set<TblOrganization> chil = tblOrganization.getChildren();
                        for (TblOrganizationMySql tblOrganization2 : chil) {
                            Tree tree = new Tree();
                            tree.setName(tblOrganization2.getOrgname());
                            tree.setId(tblOrganization2.getOrgid());
                            tree.setpId(tblOrganization2.getFatherorgid());
                            //tree.setOpen(true);
                            tree.setIsParent(tblOrganization2.getChildren().size() > 0 ? true : false);
                            if (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() != 100) {
                                continue;
                            }
                            children.add(tree);
                        }
                        Tree tree = new Tree();
                        tree.setChildren(children);
                        tree.setName(tblOrganization.getOrgname());
                        tree.setId(tblOrganization.getOrgid());
                        tree.setpId(tblOrganization.getFatherorgid());
                        tree.setOpen(true);
                        tree.setIsParent(tblOrganization.getChildren().size() > 0 ? true : false);
                        trees.add(tree);
                    }
                    return trees;
                }
            } finally {
                JedisUtil.returnResource(jedis);
            }
        }
    }

    @Override
    public void updateorgn(TblOrganization orgn) {
        tblOrganizationMapper.updateById(orgn);
    }

    @Override
    public void updateMySqlorgn(TblOrganizationMySql orgn) {
        tblOrganizationMySqlMapper.updateById(orgn);
    }

//    @Override
//    public String findIniStatus(BigDecimal orgid) {
//        return tblOrganizationMapper.findIniStatus(orgid);
//    }

    public List<Tree> addChildren(Serializable tmplId, BigDecimal pId, Map<BigDecimal, Object> map, List<TblManageRight> lists) {
        List<Tree> list = new ArrayList();
        List<TblManageRight> root = this.tblManageRightDAO.getTreeByNodeId(pId);
        for (TblManageRight tblManageRight : root) {
            for (TblManageRight right : lists) {
                if (tblManageRight.getRightid().toString().equals(right.getRightid().toString())) {
                    Tree tree = new Tree();
                    tree.setName(tblManageRight.getRightname());
                    tree.setId(tblManageRight.getRightid());
                    tree.setTarget("mainFramex");
                    tree.setChecked(map.get(tblManageRight.getRightid()) == null ? false : true);
                    tree.setpId(tblManageRight.getFatherrightid());
                    tree.setUrl("");
                    tree.setIsParent(false);
                    tree.setUrl("");
                    List<Tree> trees = addChildren(tmplId, tblManageRight.getRightid(), map, lists);
                    if (trees.size() > 0) {
                        tree.setChildren(trees);
                    }
                    list.add(tree);
                }
            }
        }
        return list;
    }

    public List<Tree> addMySqlChildren(Serializable tmplId, BigDecimal pId, Map<BigDecimal, Object> map, List<TblManageRightMySql> lists) {
        List<Tree> list = new ArrayList();
        List<TblManageRightMySql> root = this.tblManageRightMySqlDAO.getTreeByNodeId(pId);
        for (TblManageRightMySql tblManageRight : root) {
            for (TblManageRightMySql right : lists) {
                if (tblManageRight.getRightid().toString().equals(right.getRightid().toString())) {
                    Tree tree = new Tree();
                    tree.setName(tblManageRight.getRightname());
                    tree.setId(tblManageRight.getRightid());
                    tree.setTarget("mainFramex");
                    tree.setChecked(map.get(tblManageRight.getRightid()) == null ? false : true);
                    tree.setpId(tblManageRight.getFatherrightid());
                    tree.setUrl("");
                    tree.setIsParent(false);
                    tree.setUrl("");
                    List<Tree> trees = addMySqlChildren(tmplId, tblManageRight.getRightid(), map, lists);
                    if (trees.size() > 0) {
                        tree.setChildren(trees);
                    }
                    list.add(tree);
                }
            }
        }
        return list;
    }

    private List<Tree> getNoteTrees(Set<TblOrganization> chil) {
        List<Tree> children = new ArrayList();
        Iterator var3 = chil.iterator();

        while (true) {
            TblOrganization tblOrganization2;
            do {
                do {
                    if (!var3.hasNext()) {
                        return children;
                    }

                    tblOrganization2 = (TblOrganization) var3.next();
                } while (tblOrganization2.getStatus() != null && tblOrganization2.getStatus() == 1);
            } while (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() != 0);

            if (tblOrganization2.getStatus() != null && tblOrganization2.getStatus() == 0) {
                Tree tree = new Tree();
                Set<TblOrganization> chil2 = tblOrganizationMapper.findByfatherorgId(tblOrganization2.getOrgid());
                if (chil2.size() > 0) {
                    new ArrayList();
                    List<Tree> children1 = this.getNoteTrees(chil2);
                    tree.setChildren(children1);
                }

                tree.setName(tblOrganization2.getOrgname());
                tree.setId(tblOrganization2.getOrgid());
                tree.setpId(tblOrganization2.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(chil2.size() > 0);
                children.add(tree);
            }
        }

    }

    private List<Tree> getMySqlNoteTrees(Set<TblOrganizationMySql> chil) {
        List<Tree> children = new ArrayList();
        Iterator var3 = chil.iterator();

        while (true) {
            TblOrganizationMySql tblOrganization2;
            do {
                do {
                    if (!var3.hasNext()) {
                        return children;
                    }

                    tblOrganization2 = (TblOrganizationMySql) var3.next();
                } while (tblOrganization2.getStatus() != null && tblOrganization2.getStatus() == 1);
            } while (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype() != 0);

            if (tblOrganization2.getStatus() != null && tblOrganization2.getStatus() == 0) {
                Tree tree = new Tree();
                Set<TblOrganizationMySql> chil2 = tblOrganizationMySqlMapper.findByfatherorgId(tblOrganization2.getOrgid());
                if (chil2.size() > 0) {
                    new ArrayList();
                    List<Tree> children1 = this.getMySqlNoteTrees(chil2);
                    tree.setChildren(children1);
                }

                tree.setName(tblOrganization2.getOrgname());
                tree.setId(tblOrganization2.getOrgid());
                tree.setpId(tblOrganization2.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(chil2.size() > 0);
                children.add(tree);
            }
        }

    }

//    @Override
//    public TblOrganization findSelectedUser(BigDecimal orgid) {
//        tblOrganizationMapper.findSelectedUser(orgid);
//        return null;
//    }


//    @Override
//    public TblOrganization getWPZJK(String orgid, String iszy) {
//
//        List<TblOrganization> list = this.tblOrganizationMapper.selectWpzjk(orgid,iszy);
//        return (TblOrganization) list;
//    }


    private boolean getChildrenByCommpany(Set<TblOrganization> chil) {
        Iterator var2 = chil.iterator();

        TblOrganization org;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            org = (TblOrganization) var2.next();
        } while (org.getOrgtype() == null || org.getOrgtype() == 0);

        return true;
    }

    private boolean getMySqlChildrenByCommpany(Set<TblOrganizationMySql> chil) {
        Iterator var2 = chil.iterator();

        TblOrganizationMySql org;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            org = (TblOrganizationMySql) var2.next();
        } while (org.getOrgtype() == null || org.getOrgtype() == 0);

        return true;
    }

    private boolean getChildrenByCommpany1(Set<TblOrganization> chil) {
        Iterator var2 = chil.iterator();

        TblOrganization org;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            org = (TblOrganization) var2.next();
        } while (org.getOrgtype() == null || org.getOrgtype() != 0);

        return true;
    }

    private boolean getMySqlChildrenByCommpany1(Set<TblOrganizationMySql> chil) {
        Iterator var2 = chil.iterator();

        TblOrganizationMySql org;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            org = (TblOrganizationMySql) var2.next();
        } while (org.getOrgtype() == null || org.getOrgtype() != 0);

        return true;
    }

    @Override
    public Tree getTreeRoot(BigDecimal nodeId) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            // TODO Auto-generated method stub
            return null;
        } else {
            // TODO Auto-generated method stub
            return null;
        }
    }

    @Override
    public TblOrganization get(BigDecimal bigDecimal) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            // TODO Auto-generated method stub
            return null;
        } else {
            // TODO Auto-generated method stub
            return null;
        }
    }

    @Override
    public TblOrganizationMySql getMySql(BigDecimal bigDecimal) {
        return null;
    }

    
    /*
	 * 1.第一遍全部插入
	 * 2.第二遍直接修改
	 * hierarchyId字段解析规则：机构 1 部门 2岗位 4人员 8 
	 * */  
	@Override
	public Map<String, Object> syncAllOrg(Integer operaType, String data) throws Exception {
		// TODO Auto-generated method stub
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			net.sf.json.JSONArray arr = net.sf.json.JSONArray.fromObject(data);
			for (Object o : arr) {
				JSONObject dept = (JSONObject) o;
				List<TblOrganization> list = tblOrganizationMapper.getOrgByHistoryid(dept.getString("orgid"));
				if (list.size() == 0) {
					TblOrganization org = new TblOrganization();
					org.setOrgname(dept.getString("orgname"));
					org.setStatus(Integer.valueOf(dept.getString("status")));
					org.setDatasource("zz");
					org.setOrgtype(Integer.valueOf(dept.getString("orgtype")));
					org.setHistorycode(dept.getString("orgid")); // 存储历史ID
					org.setHistorydepartmentid(dept.getString("fatherorgid"));
					org.setOrgnumber(dept.getString("orgnumber"));
					tblOrganizationMapper.saveAtionHangYe(org);
				} else if (list.size() == 1) {
					TblOrganization org = list.get(0);
					org.setOrgname(dept.getString("orgname"));
					org.setStatus(Integer.valueOf(dept.getString("status")));
					org.setDatasource("zz");
					org.setOrgtype(Integer.valueOf(dept.getString("orgtype")));
					org.setHistorycode(dept.getString("orgid")); // 存储历史ID
					org.setHistorydepartmentid(dept.getString("fatherorgid"));
					org.setOrgnumber(dept.getString("orgnumber"));
					tblOrganizationMapper.updateAtionHangYe(org);
				}
			}
			//第二遍循环调整
			for (Object o : arr) {
				JSONObject dept = (JSONObject) o;
				List<TblOrganization> list = tblOrganizationMapper.getOrgByHistoryid(dept.getString("orgid"));
				if (list.size() == 1) {
						TblOrganization org = list.get(0);
						if(dept.has("orgtype")&&dept.getString("orgtype").equals("1")){ //公司
							List<TblOrganization> list2 = tblOrganizationMapper.getOrgByHistoryid(dept.getString("fatherorgid"));
							if (list2 != null) {
								TblOrganization organ = tblOrganizationMapper.findPid(list2.get(0).getOrgid().toString());
								org.setFatherorgid(organ.getOrgid());
								org.setOrgtype(organ.getOrgtype()+1);
								tblOrganizationMapper.updateAtionHangYe(org);
							}
						}else{ //部门
							List<TblOrganization> list2 = tblOrganizationMapper.getOrgByHistoryid(dept.getString("fatherorgid"));
							if (list2 != null) {
								TblOrganization organ = tblOrganizationMapper.findPid(list2.get(0).getOrgid().toString());
								org.setFatherorgid(organ.getOrgid());
								tblOrganizationMapper.updateAtionHangYe(org);
							}
						}
				}
			}
			  resultMap.put("code", "0");
              resultMap.put("msg", "部门同步成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultMap;
	}

    //调用人资接口	
	@Override
	public void syncOrg(String data) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap=new HashMap<String, Object>();
		try {
			JSONObject json=JSONObject.fromObject(data);
			JSONObject msg=JSONObject.fromObject(json.get("msg"));
			net.sf.json.JSONArray arr = net.sf.json.JSONArray.fromObject(msg.get("depts"));
			for (Object o : arr) {
				JSONObject dept = (JSONObject) o;
				List<TblOrganization> list = tblOrganizationMapper.getOrgByHistoryid(dept.getString("id"));
				if (list.size() == 0) {
					TblOrganization org = new TblOrganization();
					if(!StringUtils.isEmpty(dept.getString("id"))){
					org.setOrgname(dept.getString("name"));
					org.setStatus(dept.getString("isDelete")=="true"?1:0);
					org.setDatasource("zz");
					if(dept.getString("name").indexOf("有限公司")!=-1){
						org.setOrgtype(1);
					}else{
						org.setOrgtype(0);
					}
					org.setHistorycode(dept.getString("id")); // 存储历史ID
					org.setHistorydepartmentid(dept.getString("parentId"));
					org.setOrgnumber(dept.getString("orderNum"));
					tblOrganizationMapper.saveAtionHangYe(org);
					}
				} else if (list.size() == 1) {
					TblOrganization org = list.get(0);
					org.setOrgname(dept.getString("name"));
					org.setStatus(dept.getString("isDelete")=="true"?1:0);
					org.setDatasource("zz");
					org.setHistorycode(dept.getString("id")); // 存储历史ID
					org.setHistorydepartmentid(dept.getString("parentId"));
					org.setOrgnumber(dept.getString("orderNum"));
					tblOrganizationMapper.updateAtionHangYe(org);
				}
			}
			//第二遍循环调整
			for (Object o : arr) {
				JSONObject dept = (JSONObject) o;
				List<TblOrganization> list = tblOrganizationMapper.getOrgByHistoryid(dept.getString("id"));
				if (list.size() == 1) {
						TblOrganization org = list.get(0);
						if(dept.has("orgtype")&&!dept.getString("orgtype").equals("0")){ //公司
							List<TblOrganization> list2 = tblOrganizationMapper.getOrgByHistoryid(dept.getString("parentId"));
							if (list2 != null) {
								TblOrganization organ = tblOrganizationMapper.findPid(list2.get(0).getOrgid().toString());
								org.setFatherorgid(organ.getOrgid());
								org.setOrgtype(organ.getOrgtype()+1);
								tblOrganizationMapper.updateAtionHangYe(org);
							}
						}else{ //部门
							List<TblOrganization> list2 = tblOrganizationMapper.getOrgByHistoryid(dept.getString("parentId"));
							if (list2.size()>0) {
								TblOrganization organ = tblOrganizationMapper.findPid(list2.get(0).getOrgid().toString());
								org.setFatherorgid(organ.getOrgid());
								tblOrganizationMapper.updateAtionHangYe(org);
							}
						}
						
				}
			}
			  resultMap.put("code", "0");
              resultMap.put("msg", "部门同步成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public JsonBean findOrganInfoDetail(String token, String orgid) throws Exception {
		// TODO Auto-generated method stub
		 Map<String, Object> resultMap = new HashMap<String, Object>(0); 
		 TblStaffUtil user = userProvider.get();
          if (user==null) {
        	  resultMap.put("code", "0");
              resultMap.put("msg", "用户已失效");
        	  return ResponseFormat.retParam(0,20006,resultMap);
          }
          try {
		    TblOrganizationInfo info = tblOrganizationMapper.findOrganInfoDetail(orgid);
        	  resultMap.put("code", "1");
              resultMap.put("msg", "数据访问成功");
              resultMap.put("info",  info);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
          return ResponseFormat.retParam(1,200,resultMap);
        
	}

	@Override
	public JsonBean updateOrganInfo(String token, TblOrganizationInfo info) throws Exception {
		// TODO Auto-generated method stub
				 Map<String, Object> resultMap = new HashMap<String, Object>(0); 
				TblStaffUtil user = userProvider.get();
		          if (user==null) {
		        	  resultMap.put("code", "0");
		              resultMap.put("msg", "用户已失效");
		        	  return ResponseFormat.retParam(0,20006,resultMap);
		          }
		          try {
		        	  if(info.getOrgid()!=null){
		        		  //查询中间表是否已经有此单位
		        		 TblOrganizationInfo in= tblOrganizationMapper.findOrganInfoDetail(info.getOrgid().toString());
		        		  if(in ==null){
		        		   tblOrganizationMapper.insertOrganInfo(info);
		        	      }else{
		        		    tblOrganizationMapper.updateOrganInfo(info);
		        	     }
		        	  }
		        	  resultMap.put("code", "1");
		              resultMap.put("msg", "数据访问成功");
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
		          return ResponseFormat.retParam(1,200,resultMap);
	}

	

	 /**
		 * 查询当前公司及子公司 递归优化后
		 * @param orgid
		 * @return
		 */
	   @Override
		public List<BigDecimal> getTblOrganizationAll(BigDecimal orgid) {
			List<TblOrganization> allList = tblOrganizationMapper.getOrgidOrgtype();
			if (CollectionUtil.isEmpty(allList)) {
				return Collections.emptyList();
			}
			List<BigDecimal> addList = new ArrayList<>();
			addList.add(orgid);
			List<BigDecimal> collect = allList.stream().filter(item -> Objects.equals(item.getFatherorgid(), orgid)).map(TblOrganization::getOrgid)
					.collect(Collectors.toList());
			if (CollectionUtil.isEmpty(collect)) {
				return addList;
			}
			addList.addAll(collect);
			doAllList(allList, addList, collect);
			return addList;
		}

		/**
		 * 查询子公司下的子公司 递归
		 * @param allList
		 * @param addList
		 * @param collect1
		 */
		private void doAllList(List<TblOrganization> allList, List<BigDecimal> addList, List<BigDecimal> collect1) {
			collect1.forEach(it1 -> {
				List<BigDecimal> collect2 = allList.stream().filter(it2 -> Objects.equals(it2.getFatherorgid(), it1)).map(TblOrganization::getOrgid)
						.collect(Collectors.toList());
				if (CollectionUtil.isEmpty(collect2)) {
					return;
				}
				addList.addAll(collect2);
				doAllList(allList, addList, collect2);
			});
		}
		
		
		private void doAddList(List<TblOrganization> allList, List<BigDecimal> addList, List<BigDecimal> collect1) {
			collect1.forEach(it1 -> {
				List<BigDecimal> collect2 = allList.stream().filter(it2 -> Objects.equals(it2.getFatherorgid(), it1)).map(TblOrganization::getOrgid)
						.collect(Collectors.toList());
				if (CollectionUtil.isEmpty(collect2)) {
					return;
				}
				addList.addAll(collect2);
				doAllList(allList, addList, collect2);
			});
		}
		
		private void doAddList1(List<TblOrganization> allList, List<BigDecimal> addList, List<BigDecimal> collect1) {
			collect1.forEach(it1 -> {
				TblOrganization org=tblOrganizationMapper.selectByOrgid(it1);
				List<BigDecimal> collect2 = allList.stream().filter(it2 -> Objects.equals(it2.getOrgid(), org.getFatherorgid())).map(TblOrganization::getOrgid)
						.collect(Collectors.toList());
				if (CollectionUtil.isEmpty(collect2)) {
					return;
				}
				addList.addAll(collect2);
				doAddList1(allList, addList, collect2);
			});
		}
		 /**
		 * 查询当前公司及子公司 递归优化后
		 * @param orgid
		 * @return
		 */
	   @Override
		public List<BigDecimal> getOrgIdListAutoNumber() {
			List<TblOrganization> allList = tblOrganizationMapper.getOrgidAutoNumber();
			if (CollectionUtil.isEmpty(allList)) {
				return Collections.emptyList();
			}
			List<BigDecimal> addList = new ArrayList<>();
			List<BigDecimal> collect = allList.stream().filter(item -> 
			Objects.equals(item.getFatherorgid(),new BigDecimal(-1))).map(TblOrganization::getOrgid)
					.collect(Collectors.toList());
			if (CollectionUtil.isEmpty(collect)) {
				return addList;
			}
			addList.addAll(collect);
			doAddList(allList, addList, collect);
			return addList;
		}

		@Override
		public List<BigDecimal> getOrgIdLis(BigDecimal orgid) throws Exception {
			// TODO Auto-generated method stub
			List<TblOrganization> allList = tblOrganizationMapper.getOrgidOrgtype();
			if (CollectionUtil.isEmpty(allList)) {
				return Collections.emptyList();
			}
			List<BigDecimal> addList = new ArrayList<>();
			addList.add(orgid);
			List<BigDecimal> collect = allList.stream().filter(item -> Objects.equals(item.getFatherorgid(),orgid)).map(TblOrganization::getOrgid)
					.collect(Collectors.toList());
			if (CollectionUtil.isEmpty(collect)) {
				return addList;
			}
			addList.addAll(collect);
			doAddList1(allList, addList, collect);
			return addList;
		}

		 
		@Override
		public List<BigDecimal> getOrgIdLisBySy(BigDecimal orgid) throws Exception {
			// TODO Auto-generated method stub
			List<TblOrganization> allList = tblOrganizationMapper.selectAllCompanyList();
			if (CollectionUtil.isEmpty(allList)) {
				return Collections.emptyList();
			}
			if (Objects.isNull(orgid)) {
				return Collections.emptyList();
			}
			List<BigDecimal> addList = new ArrayList<>();
			
			addList.add(orgid);
			List<BigDecimal> collect = allList.stream().filter(item -> Objects.equals(item.getOrgid(), orgid)).map(TblOrganization::getOrgid)
					.collect(Collectors.toList());
			if (CollectionUtil.isEmpty(collect)) {
				return addList;
			}
			addList.addAll(collect);
			List<TblOrganization> allList2 = tblOrganizationMapper.getOrgAll();
			doAllList(allList2, addList, collect);
			return addList;
		}

		@Override
		public String selectNamesByids(String ids) throws Exception {
			// TODO Auto-generated method stub
			String names="";
			try {
				if(org.apache.commons.lang.StringUtils.isNotBlank(ids)){
					List<TblOrganization> list=tblOrganizationMapper.getOrgNameByOrgids(ids.split(","));
					names = list.stream()
	                        .map(TblOrganization::getOrgname)  // 提取name字段
	                        .collect(Collectors.joining(", "));  // 以逗号分隔
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return names;
		}

		@Override
		public String selectNameByids(BigDecimal id) throws Exception {
			String name="";
			try {
				if(id!=null&&id.compareTo(new BigDecimal(0))>0){
					TblOrganization  org=tblOrganizationMapper.selectById(id);
					 name=org.getOrgname();
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return name;
		}
	    
 
}
