package com.huabo.fxgl.service.impl;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PropertyFileReader;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.TblOrganization;
import com.huabo.fxgl.mapper.OrganizationMapper;
import com.huabo.fxgl.service.IOrganizationService;
import com.huabo.fxgl.util.RedisFinalUtis;
import com.huabo.fxgl.util.Tree;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
@Slf4j
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;
    
    @Resource
    private UserProvider userProvider;



    @Override
    public List<Organization> findOrganizationByOrgid(String orgid) {

        return organizationMapper.findOrganizationByOrgid(orgid);

    }

    @Override
    public List<Organization> findOrganizationAll() {
        return organizationMapper.findOrganizationAll();
    }

    @Override
    public void initOrgByOrgtype(int orgtype, String orgname, String orgid) {
        QueryWrapper<Organization> query = new QueryWrapper<>();
        query.eq("orgtype",orgtype);
        query.eq("FATHERORGID",-1);
        query.eq("icode",orgid);
        String hyzskType = null;
        if(orgtype == Organization.TYPE_HY_ZSK){
            hyzskType = orgname;
            query.eq("HYZSKTYPE",orgname);
        }
        long num = this.baseMapper.selectCount(query);
        if(num ==0){
            Organization o = new Organization();
            o.setOrgname(orgname);
            o.setFatherorgid(new BigDecimal("-1"));
            o.setOrgnumber(orgtype + "");
            o.setOrgtype(new BigDecimal(orgtype));
            o.setIcode(orgid);
            o.setHyzsktype(hyzskType);
            organizationMapper.insert(o);
        }
    }

    @Override
    public String findOrgTreeByOrgtypeAndOrgid(String orgid, int orgtype, String hyzskType) {
        String tyStr = "";
        List<Organization> list = organizationMapper.findOrgTreeByOrgtypeAndOrgid(orgid,orgtype,hyzskType);
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            Organization o = list.get(i);
            str += "tree.nodes['" + (i == 0 ? -1 : o.getFatherorgid()) + "_" + o.getOrgid() + "']=\"text:" + o.getOrgname() + ";method:check(" + o.getOrgid() + "," + o.getOrgtype() + ")\";\n";
        }
        return str;
    }


    @Override
    public String getOrgTree(String pageurl) {
        String tree = "";
        try {
            List<Organization> list = organizationMapper.query_orgtype();
            for(Organization org: list){
                if(org == null || org.getOrgid()==null || org.getFatherorgid() == null || org.getOrgname()==null){continue;}
                tree += "tree.nodes[\'" +
                        org.getFatherorgid().toEngineeringString() + "_" + org.getOrgid().toString() +
                        "\']=\"text:" + org.getOrgname() + ";url:" + pageurl +"?pid=" + org.getOrgid().toString() +
                        "\";\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tree;
    }
    @Override
    public Organization getHYFirst() {
        //SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = ( SELECT orgid FROM TBL_ORGANIZATION WHERE ORGNAME = '行业' AND ORGTYPE = 100 ) AND ROWNUM = 1
        QueryWrapper queryWrapper=new QueryWrapper();
        List<Organization> list=baseMapper.getHYFirst(queryWrapper);
        if (list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    /*@Override
    public String findOrgByorgId(String orgid) {

        List orgByorgId = organizationMapper.findOrgByorgId(orgid);

        String zrbm = "";
        for (int i = 0; i < orgByorgId.size(); i++) {
            zrbm += orgByorgId.get(i) + ",";
        }
        if (!zrbm.equals("")) {
            zrbm = zrbm.substring(0, zrbm.length() - 1);
        }
        return zrbm;
    }*/
    @Override
    public Organization findById(String id) {
        return getById(new BigDecimal(id));
    }


    @Override
    public List<Organization> findOrgByUser(String userName) {
        return baseMapper.findOrgBysql(userName);
    }


    @Override
    public String findOrgByorgId(String orgid) {
        List<String> orgNames = organizationMapper.findOrgInOrgId(orgid);
        StringBuilder zrbm = new StringBuilder();
        for (int i = 0; i < orgNames.size(); i++) {
            zrbm.append(orgNames.get(i));
            if (i < orgNames.size()-1)
                zrbm.append(",");
        }

        return zrbm.toString();
    }

    @Override
    public List<Tree> getNodeAllbm(BigDecimal nodeId) {
        Jedis jedis = JedisUtil.getJedis();
        try{
            if(jedis.exists(RedisFinalUtis.ORGTREEDEPTLIST+nodeId)){
                String str = jedis.get(RedisFinalUtis.ORGTREEDEPTLIST+nodeId);
                List<Tree> trees  = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                return trees;
            }else{
//                String sql = "select * from tbl_organization where ORGID = " + nodeId + "  and orgtype < 100 ORDER BY orderid ASC";
                List<Tree> trees = new ArrayList<Tree>();
                List<Tree> children = new ArrayList<Tree>();
                List<Organization> list = organizationMapper.getNodeAllbm(nodeId);
                for (Organization organization : list) {
                    List<Organization> chil = organization.getChildren();
                    children = getNoteTreesbm(chil);
                    Tree tree = new Tree();
                    tree.setChildren(children);
                    tree.setName(organization.getOrgname());
                    tree.setId(organization.getOrgid());
                    tree.setpId(organization.getFatherorgid());
                    tree.setOpen(true);
                    tree.setIsParent(organization.getChildren().size() > 0 ? true : false);
                    trees.add(tree);
                }
                return trees;
            }
        }finally{
            JedisUtil.returnResource(jedis);
        }
    }

    private List<Tree> getNoteTreesbm(List<Organization> chil) {
        List<Tree> children = new ArrayList<Tree>();
        for (Organization organization2 : chil) {

			/*if (tblOrganization2.getStatus()!=null && tblOrganization2.getStatus()==1) {
				continue;
			}*/
            if (organization2.getOrgtype() != null && organization2.getOrgtype() != new BigDecimal(0)) {
                continue;
            }
            if (organization2.getStatus() != null && organization2.getStatus() == new BigDecimal(0)) {
                Tree tree = new Tree();
                if (organization2.getChildren().size() > 0) {
                    List<Tree> children1 = new ArrayList<Tree>();
                    children1 = getNoteTrees(organization2.getChildren());
                    tree.setChildren(children1);
                }
                tree.setName(organization2.getOrgname());
                tree.setId(organization2.getOrgid());
                tree.setpId(organization2.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(organization2.getChildren().size() > 0 ? true : false);
                children.add(tree);
            }
        }
        return children;
    }

    private List<Tree> getNoteTrees(List<Organization> chil) {
        List<Tree> children = new ArrayList<Tree>();
        for (Organization organization2 : chil) {

            if (organization2.getStatus() != null && organization2.getStatus() == new BigDecimal(1)) {
                continue;
            }
            if (organization2.getOrgtype() != null && organization2.getOrgtype() != new BigDecimal(0)) {
                continue;
            }
            if (organization2.getStatus() != null && organization2.getStatus() == new BigDecimal(0)) {
                Tree tree = new Tree();
                if (organization2.getChildren().size() > 0) {
                    List<Tree> children1 = new ArrayList<Tree>();
                    children1 = getNoteTrees(organization2.getChildren());
                    tree.setChildren(children1);
                }
                tree.setName(organization2.getOrgname());
                tree.setId(organization2.getOrgid());
                tree.setpId(organization2.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(organization2.getChildren().size() > 0 ? true : false);
                children.add(tree);
            }
        }
        return children;
    }

    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 获得组织树
     * @Date 2022/8/13
     * @param str
     * @param orgid
     * @param hbOrgEntity
     * @return com.hbfk.util.JsonBean
     * @url:
     **/
    private static final String GROUP_STRUCTURE = PropertyFileReader.getItem("group.structure");
    @Override
    public JsonBean getOrganizationTree(String str, String orgid, String token) throws Exception {
        final TblStaffUtil tblStaffUtil = userProvider.get();
        final TblOrganizationUtil linkOrg = tblStaffUtil.getLinkOrg();
        final TblOrganizationUtil currentOrg = tblStaffUtil.getCurrentOrg();//用户选择的组织
        boolean is = false;
        if(StringUtils.isNotBlank(str) && str.equals("3")&&StringUtils.isNotBlank(GROUP_STRUCTURE)){
            is = true;
        }
        List<Organization> tree=null;
        //TODO
        if(is){
            tree = baseMapper.selectByOrgId(currentOrg.getOrgid().toString());
        }else {
            tree = baseMapper.selectByOrgId(currentOrg.getOrgid().toString());
        }
        return new JsonBean(1,"操作成功",tree);
    }
    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 通过集团去查询组织树
     * @Date 2022/8/13
     * @param origd
     * @return com.hbfk.util.JsonBean
     * @url:
     **/
    private List<Organization> getOrganizationTreeByGroup(String origd){
        QueryWrapper<Organization> queryWrapper=new QueryWrapper<>();
        queryWrapper.lt("ORGTYPE",100).eq("orgid",origd).eq("status",0).orderByAsc("orderid");
        List<Organization> list = list(queryWrapper);
        List<Organization> organizations = list();
        if (list!=null&&list.size()>0) {
            return list.stream().map(item -> {
                item.setChildren(getChildren(item, organizations));
                return item;
            }).collect(Collectors.toList());

        }
        return  null;
    }

    @Override
    public Boolean isSJByOrgId(String userOrgid) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("audittype", 1);
        queryWrapper.eq("orgid", userOrgid);
        return count(queryWrapper) != 0;

    }

//    @Override
    public List<Tree> getTree(BigDecimal nodeId) {
        /*Jedis jedis = JedisUtil.getJedis();
        try{
            if(jedis.exists(RedisFinalUtis.ORGDEPTLIST+nodeId)){
                String str = jedis.get(RedisFinalUtis.ORGDEPTLIST+nodeId);
                List<Tree> trees  = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                return trees;
            }else{
                List<Tree> trees = new ArrayList<Tree>();
                List<Organization> list = tblOrganizationDAO.getNodes(nodeId);
                for (Organization tblOrganization : list) {
                    Tree tree = new Tree();
                    tree.setName(tblOrganization.getOrgname());
                    tree.setId(tblOrganization.getOrgid());
                    tree.setpId(tblOrganization.getFatherorgid());
                    tree.setOpen(true);
                    tree.setIsParent(tblOrganization.getChildren().size() > 0 ? true : false);
                    if (tblOrganization.getOrgtype() != null && Organization.getORGTYPE() != 0) {
                        continue;
                    }
                    if (tblOrganization.getStatus() != null && tblOrganization.getStatus().intValue() == 1) {
                        continue;
                    }
                    if (tblOrganization.getStatus() != null && tblOrganization.getStatus().intValue() ==0) {
                        trees.add(tree);
                    }
                }
                return trees;

            }
        }finally{
            JedisUtil.returnResource(jedis);
        }*/
        return null;
    }

    @Override
    public List<Tree> getNodeAllJT(BigDecimal nodeId) {
        Jedis jedis = JedisUtil.getJedis();
        try{
            if(jedis.exists(RedisFinalUtis.ORGLIST+nodeId)){
                String str = jedis.get(RedisFinalUtis.ORGLIST+nodeId);
                List<Tree> trees  = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                return trees;
            }else{
                List<Tree> trees = new ArrayList<Tree>();
                List<Tree> children = new ArrayList<Tree>();
                List<Organization> list = organizationMapper.getNodeAllJT(nodeId);
                for (Organization tblOrganization : list) {
                    if (tblOrganization.getStatus() != null && tblOrganization.getStatus().intValue() == 1) {
                        continue;
                    }
                    List<Organization> chil = tblOrganization.getChildren();
                    children = getNoteTreesJT(chil);
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
        }finally{
            JedisUtil.returnResource(jedis);
        }
    }

    @Override
    public List<Tree> getTreeHy(BigDecimal nodeId) {
        Jedis jedis = JedisUtil.getJedis();
        try{
            if(jedis.exists(RedisFinalUtis.ORGTYPELIST+nodeId)){
                String str = jedis.get(RedisFinalUtis.ORGTYPELIST+nodeId);
                List<Tree> trees  = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                return trees;
            }else{
                List<Tree> trees = new ArrayList<Tree>();
//                "from TblOrganization  t where t.fatherorgid = ? order by t.orderid asc"
                QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("fatherorgid", nodeId);
                queryWrapper.orderByAsc("orderid");
                List<Organization> list = list(queryWrapper);
                for (Organization tblOrganization : list) {
                    if (tblOrganization.getStatus() != null && tblOrganization.getStatus().intValue() == 1) {
                        continue;
                    }
                    Tree tree = new Tree();
                    tree.setName(tblOrganization.getOrgname());
                    tree.setId(tblOrganization.getOrgid());
                    tree.setpId(tblOrganization.getFatherorgid());
                    tree.setOpen(true);
                    queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("fatherorgid", tblOrganization.getOrgid());
                    tree.setIsParent(count(queryWrapper) > 0 ? true : false);
                    if (tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype().intValue() != 100) {
                        continue;
                    }
                    if (tblOrganization.getStatus() == null || tblOrganization.getStatus().intValue() == 0) {
                        trees.add(tree);
                    }
                }
                return trees;
            }
        }finally{
            JedisUtil.returnResource(jedis);
        }
    }
    /*@Override
    public List<Tree> getTreeHy(String nodeId) {
        return null;
    }*/

    @Override
    public List<Tree> getNodeAllHy(BigDecimal nodeId) {
        Jedis jedis = JedisUtil.getJedis();
        try{
            if(jedis.exists(RedisFinalUtis.ORGNODEHYTREE+nodeId)){
                String str = jedis.get(RedisFinalUtis.ORGNODEHYTREE+nodeId);
                List<Tree> trees  = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                return trees;
            }else{
//                String sql = "select * from tbl_organization where ORGID = " + nodeId + " and orgtype = 100 ORDER BY orderid ASC";
                List<Tree> trees = new ArrayList<Tree>();
                List<Tree> children = new ArrayList<Tree>();
                QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("ORGID", nodeId);
                queryWrapper.eq("orgtype", 100);
                queryWrapper.orderByAsc("orderid");
                List<Organization> list = list(queryWrapper);
                for (Organization tblOrganization : list) {
                    queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("fatherorgid", tblOrganization.getOrgid());
                    List<Organization> chil = list(queryWrapper);
                    for (Organization tblOrganization2 : chil) {
                        Tree tree = new Tree();
                        tree.setName(tblOrganization2.getOrgname());
                        tree.setId(tblOrganization2.getOrgid());
                        tree.setpId(tblOrganization2.getFatherorgid());
                        //tree.setOpen(true);
                        queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("fatherorgid", tblOrganization.getOrgid());
                        tree.setIsParent(count(queryWrapper) > 0 ? true : false);
                        if (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype().intValue() != 100) {
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
                    tree.setIsParent(children.size() > 0 ? true : false);
                    trees.add(tree);
                }
                return trees;
            }
        }finally{
            JedisUtil.returnResource(jedis);
        }
    }

    private List<Tree> getNoteTreesJT(List<Organization> chil) {
        List<Tree> children = new ArrayList<Tree>();
        for (Organization tblOrganization2 : chil) {

            if (tblOrganization2.getStatus() != null && tblOrganization2.getStatus().intValue() == 1) {
                continue;
            }
            if (tblOrganization2.getStatus() != null && tblOrganization2.getStatus().intValue() == 0) {
                Tree tree = new Tree();
                if (tblOrganization2.getChildren().size() > 0) {
                    List<Tree> children1 = new ArrayList<Tree>();
                    children1 = getNoteTreesJT(tblOrganization2.getChildren());
                    tree.setChildren(children1);
                }
                tree.setName(tblOrganization2.getOrgname());
                tree.setId(tblOrganization2.getOrgid());
                tree.setpId(tblOrganization2.getFatherorgid());
                tree.setOpen(false);
                tree.setIsParent(tblOrganization2.getChildren().size() > 0 ? true : false);
                children.add(tree);
            }
        }
        return children;
    }




    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description
     * @Date 2022/8/13
     * @param origd
     * @return java.util.List<com.huabo.fxgl.entity.Organization>
     * @url:
     **/
    private List<Organization> getOrganizationTreeAll(String origd){
        QueryWrapper<Organization> queryWrapper=new QueryWrapper<>();
        queryWrapper.lt("ORGTYPE",100).eq("orgid",origd).eq("status",0).orderByAsc("orderid");
        List<Organization> list = list(queryWrapper);
        List<Organization> organizations = list();
        if (list!=null&&list.size()>0) {
            return list.stream().map(item -> {
                item.setChildren(getChildren(item, organizations));
                return item;
            }).collect(Collectors.toList());

        }
        return  null;
    }

    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 递归查询该节点下的所有子节点
     * @Date 2022/8/12
     * @param root
     * @param list
     * @return java.util.List<com.huabo.fxgl.entity.Organization>
     * @url:
     **/
    public List<Organization> getChildren(Organization root,List<Organization> list){
        return list.stream().filter(item -> {
            if (item.getStatus() != null && item.getStatus().equals(new BigDecimal(1))) {
                return false;
            }
            if (item.getOrgtype() != null && !item.getOrgtype().equals(new BigDecimal(0))) {
                return false;
            }
            return item.getFatherorgid() != null && item.getFatherorgid().equals(root.getOrgid());
        }).peek(item -> item.setChildren(getChildren(item, list))).collect(Collectors.toList());
    }

    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 通过orgId判断是不是审计部门
     * @Date 2022/8/8
     * @param toString
     * @return boolean
     * @url:
     **/
    @Override
    public boolean isAuditByOrgId(String orgId) {
        QueryWrapper<Organization> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("audittype",1).eq("orgid",orgId);
        return this.count(queryWrapper)>0;
    }
    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 缺陷管理-->新建-->发现人-->组织架构
     * @Date 2022/8/13
     * @param nodeId
     * @param type
     * @param orgId
     * @param staff
     * @return com.hbfk.util.JsonBean
     * @url:
     **/
    @Override
    public JsonBean userLeft(String nodeId, String type, String orgId, String token) throws Exception {
        TblStaffUtil tblStaffUtil = userProvider.get();
        TblOrganizationUtil linkOrg = tblStaffUtil.getLinkOrg();
        TblOrganizationUtil currentOrg = tblStaffUtil.getCurrentOrg();//用户选择的组织
        if (nodeId == null) {
            nodeId=orgId;
            if (null==orgId){
                nodeId= linkOrg.getOrgid().toString();
            }
        }
        if (StringUtils.isNotBlank(type)){
            return  new JsonBean(1,"操作成功",getTreeByOrgId(orgId));
        }else {
            boolean is = false;
            if(StringUtils.isNotBlank(type) &&StringUtils.isNotBlank(GROUP_STRUCTURE)){
                is = true;
            }
            List<Organization> tree=null;
            if(is){
                tree= baseMapper.selectByOrgId(nodeId);
            }else {
                tree= baseMapper.selectByOrgId(nodeId);
            }
            return new JsonBean(1,"操作成功",tree);
        }
    }

    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 通过orgid去查询该公司架构
     * @Date 2022/8/13
     * @param orgid
     * @return com.huabo.fxgl.entity.Organization
     * @url:
     **/
    private Organization getTreeByOrgId(String orgid){
        Organization organization = getById(orgid);
        organization.setChildren(baseMapper.selectChildOrgTree(orgid));
        return organization;
    }
    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 通过组织表的父id的去查询他下面的所有子id的集合，集合包括父id,只包括父子两级
     * @Date 2022/8/12
     * @param id
     * @return java.util.List<java.math.BigDecimal>
     * @url:
     **/
    @Override
    public List<BigDecimal> getIdsByFatherId(BigDecimal id) {
        QueryWrapper<Organization> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("FATHERORGID",id).eq("ORGTYPE",0);
        final List<Organization> list = this.list(queryWrapper);
        List<BigDecimal> childIds =null;
        if (list!=null&&list.size()>0){
            childIds = list.stream().map(Organization::getOrgid).collect(Collectors.toList());
            childIds.add(id);
       }
       if (list!=null&&list.size()>0){
        	for(Organization o:list){
        		List<BigDecimal> childs=getChilIds(o.getOrgid());
        		childIds.addAll(childs);
        	}
        }
       if(childIds!=null){
    	   childIds = childIds.stream()
                   .filter(Objects::nonNull) // 过滤掉null值
                   .distinct()               // 去重
                   .collect(Collectors.toList());
       }
     
        return   childIds;
    }
    
    public List<BigDecimal> getChilIds(BigDecimal id) {
        QueryWrapper<Organization> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("FATHERORGID",id).eq("ORGTYPE",0);
        List<BigDecimal> ids=new ArrayList<>();
        final List<Organization> list = this.list(queryWrapper);
        if (list!=null&&list.size()>0){
        	ids = list.stream().map(Organization::getOrgid).collect(Collectors.toList());
        	ids.add(id);
       }
        return ids;
    }
    
    @Override
    public List<BigDecimal> getIdsByFathersId(BigDecimal id) {
        QueryWrapper<Organization> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("FATHERORGID",id).gt("ORGTYPE",0);
        List<BigDecimal> ids=new ArrayList<>();
        ids.add(id);
        final List<Organization> list = this.list(queryWrapper);
        if (list!=null&&list.size()>0){
        	for(Organization o:list){
        		List<BigDecimal> childs=getIds(o.getOrgid());
        		ids.addAll(childs);
        	}
        }
        ids = ids.stream()
                .filter(Objects::nonNull) // 过滤掉null值
                .distinct()               // 去重
                .collect(Collectors.toList());
        return  ids;
    }

    public List<BigDecimal> getIds(BigDecimal id) {
        QueryWrapper<Organization> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("FATHERORGID",id).gt("ORGTYPE",0);
        List<BigDecimal> ids=new ArrayList<>();
        final List<Organization> list = this.list(queryWrapper);
        if (list!=null&&list.size()>0){
            List<BigDecimal> childIds = list.stream().map(Organization::getOrgid).collect(Collectors.toList());
            ids.addAll(childIds);
            for(BigDecimal b:childIds){
            	ids.addAll(getIds(b));
            }
        }
        return  ids;
    }
    
    /*
    * @author zuoshun
    * @version v1.0.1
    * @Description
    * @Date 2022/8/15
    * @param type
    * @param nodeId
    * @param hbOrgEntity
    * @return com.hbfk.util.JsonBean
    * @url:
    **/
    @Override
    public JsonBean getAllDepartmentWithTree(String type, String nodeId, String token) throws Exception {
        TblStaffUtil tblStaffUtil = userProvider.get();
        TblOrganizationUtil currentOrg = tblStaffUtil.getCurrentOrg();
        log.info("fasfd==={}",nodeId==null);
        if (StringUtils.isEmpty(nodeId)){
            nodeId=currentOrg.getOrgid().toString();
            log.info("-------------{}",currentOrg.getOrgid().toString());
        }
        //查询redis是否有缓存


        log.info("nodeId={}",nodeId);
        //如果没有缓存就去查询
        QueryWrapper<Organization> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("ORGID",nodeId).lt("orgtype",100).orderByAsc("orderid");
        //通过前端传过来的orgid去查询该根节点
        final List<Organization> list = this.list(queryWrapper);
        log.info("根节点={}",list==null?null:list.toString());
        if (list==null||list.size()<=0){//如果没有数据返回一个空的数据
            return new JsonBean();
        }
        final List<Organization> collect = list.stream().map(item -> {
            item.setChildren(baseMapper.selectChildOrgList(item.getOrgid().toString()));
            return item;
        }).collect(Collectors.toList());

        return new JsonBean(1,"操作成功",collect);
    }

    /**
     * @author xujiajun
     * @param id
     * @return
     */
    /*@Override
    public Organization findById(BigDecimal id) {
        log.debug("getting TblOrganization instance with id: " + id);
        try {
            Organization instance = baseMapper.selectById(id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }*/

    
    
    
    @Override
	public List<Tree> getTrees(BigDecimal nodeId) {
        Jedis jedis = JedisUtil.getJedis();
        try {
            if (jedis.exists(RedisFinalUtis.ORGDEPTLIST + nodeId)) {
                String str = jedis.get(RedisFinalUtis.ORGDEPTLIST + nodeId);
                List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                return trees;
            } else {
                List<Tree> trees = new ArrayList<Tree>();
                List<TblOrganization> list = this.organizationMapper.getNode(nodeId);
                for (TblOrganization tblOrganization : list) {
                    Tree tree = new Tree();
                    tree.setName(tblOrganization.getOrgname());
                    tree.setId(tblOrganization.getOrgid());
                    tree.setpId(tblOrganization.getFatherorgid());
                    tree.setOpen(true);
                    tree.setIsParent(tblOrganization.getChildren().size() > 0 ? true : false);
                    if (tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype() != 0) {
                        continue;
                    }
                    if (tblOrganization.getStatus() != null && tblOrganization.getStatus() == 1) {
                        continue;
                    }
                    if (tblOrganization.getStatus() != null && tblOrganization.getStatus() == 0) {
                        trees.add(tree);
                    }
                }
                return trees;

            }
        } finally {
            JedisUtil.returnResource(jedis);
        }
	}
    
    
    @Override
	public List<Tree> getNodeAlls(BigDecimal nodeId) {
        Jedis jedis = JedisUtil.getJedis();
        try {
            if (jedis.exists(RedisFinalUtis.ORGDEPTLIST + nodeId)) {
                String str = jedis.get(RedisFinalUtis.ORGDEPTLIST + nodeId);
                List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                return trees;
            } else {
                List<Tree> trees = new ArrayList<Tree>();
                List<Tree> children = new ArrayList<Tree>();
                List<TblOrganization> list = organizationMapper.findBysql(nodeId);
                for (TblOrganization tblOrganization : list) {
                    if (tblOrganization.getStatus() != null && tblOrganization.getStatus() == 1) {
                        continue;
                    }
                    //Set<TblOrganization> chil = tblOrganization.getChildren();
                    Set<TblOrganization> chil = organizationMapper.findByfatherorgId(tblOrganization.getOrgid());
                    children = getNoteTrees(chil);
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
                if (tblOrganization2.getChildren().size() > 0) {
                    new ArrayList();
                    List<Tree> children1 = this.getNoteTrees(tblOrganization2.getChildren());
                    tree.setChildren(children1);
                }

                tree.setName(tblOrganization2.getOrgname());
                tree.setId(tblOrganization2.getOrgid());
                tree.setpId(tblOrganization2.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(tblOrganization2.getChildren().size() > 0);
                children.add(tree);
            }
        }
    }
    
	@Override
	public String selectNamesByids(String ids) throws Exception {
		// TODO Auto-generated method stub
		String names="";
		try {
			if(org.apache.commons.lang.StringUtils.isNotBlank(ids)){
				List<Organization> list=organizationMapper.getOrgNameByOrgids(ids.split(","));
				names = list.stream()
                        .map(Organization::getOrgname)  // 提取name字段
                        .collect(Collectors.joining(", "));  // 以逗号分隔
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return names;
	}
	
	@Override
	public String selectIdsByNames(String names,BigDecimal orgid) throws Exception {
		// TODO Auto-generated method stub
		String ids="";
		try {
			if(org.apache.commons.lang.StringUtils.isNotBlank(names)){
				List<Organization> list=organizationMapper.getOrgIdsByNames(names.split(","),orgid);
				ids = list.stream()
                        .map(Organization::getOrgid)  
                        .map(BigDecimal::toString)// 提取name字段
                        .collect(Collectors.joining(","));  // 以逗号分隔
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ids;
	}
	
	@Override
	public String selectNamesByNames(String names,BigDecimal orgid) throws Exception {
		// TODO Auto-generated method stub
		String ids="";
		try {
			if(org.apache.commons.lang.StringUtils.isNotBlank(names)){
				List<Organization> list=organizationMapper.getOrgIdsByNames(names.split(","),orgid);
				ids = list.stream()
                        .map(Organization::getOrgname)  
                        .collect(Collectors.joining(","));  // 以逗号分隔
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ids;
	}

	@Override
	public String selectNameByids(BigDecimal id) throws Exception {
		String name="";
		try {
			if(id!=null&&id.compareTo(new BigDecimal(0))>0){
				Organization  org=organizationMapper.selectById(id);
				 name=org.getOrgname();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return name;
	}

	@Override
	public List<Organization> getOrgsByOrgids(String ids, List<BigDecimal> orgids) throws Exception {
		// TODO Auto-generated method stub
		List<Organization> list=null;
		try {
			if(org.apache.commons.lang.StringUtils.isNotBlank(ids)){
				  list=organizationMapper.getOrgIdsByNames2(ids.split(","),orgids);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<BigDecimal> getAllDeptIds(BigDecimal orgid) {
		// TODO Auto-generated method stub
		List<BigDecimal> orgids=new ArrayList<>();
		try {
			List<TblOrganization> list=organizationMapper.getAllOrgid();
			orgids=getAllSubDepartmentIds(list,orgid);
//			orgids = list.stream()
//                    .map(TblOrganization::getOrgid)  
//                    .collect(Collectors.toList());  
		} catch (Exception e) {
			// TODO: handle exception
		}
		return orgids;
	}
	
	public List<BigDecimal> getAllSubDepartmentIds(List<TblOrganization> allDepartments, BigDecimal topDepartmentId) {
		// 1. 构建父部门到子部门的映射
		Map<BigDecimal, List<TblOrganization>> parentToChildrenMap = allDepartments.stream()
				.filter(dept -> dept.getFatherorgid() != null)
				.collect(Collectors.groupingBy(TblOrganization::getFatherorgid));

		// 2. 收集所有子部门ID
		List<BigDecimal> result = new ArrayList<>();
		findSubDepartmentIds(topDepartmentId, parentToChildrenMap, result);

		return result;
	}

	private void findSubDepartmentIds(BigDecimal parentId, Map<BigDecimal, List<TblOrganization>> parentToChildrenMap,
			List<BigDecimal> result) {
		List<TblOrganization> children = parentToChildrenMap.get(parentId);
		if (children != null && !children.isEmpty()) {
			// 获取子部门ID并添加到结果
			List<BigDecimal> childIds = children.stream().map(TblOrganization::getOrgid).collect(Collectors.toList());

			result.addAll(childIds);

			// 递归查找子部门的子部门
			for (BigDecimal childId : childIds) {
				findSubDepartmentIds(childId, parentToChildrenMap, result);
			}
		}
	}
}
