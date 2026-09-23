package com.huabo.cybermonitor.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.RedisFinalUtis;
import com.huabo.cybermonitor.entity.Organization;
import com.huabo.cybermonitor.entity.Tree;
import com.huabo.cybermonitor.mapper.OrganizationMapper;
import com.huabo.cybermonitor.service.IOrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

    @Autowired
    OrganizationMapper organizationMapper;

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
        query.eq("orgtype", orgtype);
        query.eq("FATHERORGID", -1);
        query.eq("icode", orgid);
        String hyzskType = null;
        if (orgtype == Organization.TYPE_HY_ZSK) {
            hyzskType = orgname;
            query.eq("HYZSKTYPE", orgname);
        }
        long num = this.baseMapper.selectCount(query);
        if (num == 0) {
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
        List<Organization> list = organizationMapper.findOrgTreeByOrgtypeAndOrgid(orgid, orgtype, hyzskType);
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
            for (Organization org : list) {
                if (org == null || org.getOrgid() == null || org.getFatherorgid() == null || org.getOrgname() == null) {
                    continue;
                }
                tree += "tree.nodes[\'" +
                        org.getFatherorgid().toEngineeringString() + "_" + org.getOrgid().toString() +
                        "\']=\"text:" + org.getOrgname() + ";url:" + pageurl + "?pid=" + org.getOrgid().toString() +
                        "\";\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tree;
    }

    /**
     * 组织结构树
     *
     * @param nodeId
     * @return
     */
    @Override
    public List<Tree> getNodeAll(BigDecimal nodeId) {
        Jedis jedis = JedisUtil.getJedis();
        try {
//            缓存处理
            if (jedis.exists(RedisFinalUtis.ORGDEPTLIST + nodeId)) {
                String str = jedis.get(RedisFinalUtis.ORGDEPTLIST + nodeId);
                List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                return trees;
            } else {
                List<Tree> trees = new ArrayList<>();
                List<Tree> children = new ArrayList<>();
                QueryWrapper qw = new QueryWrapper();
                qw.eq("orgid", nodeId);
                qw.lt("orgtype", 100);
                qw.orderByAsc("orderid");
                List<Organization> list = organizationMapper.selectList(qw);
                for (Organization tblOrganization : list) {
                    if (tblOrganization.getStatus() != null && tblOrganization.getStatus().equals("1")) {
                        continue;
                    }
                    Set<Organization> chil = tblOrganization.getChildren();
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

    /**
     * 行业指标库---main页面
     * @param orgid
     * @return
     */
    @Override
    public List<Organization> findOrgTreeObjByHY(String orgid) {
        return  this.baseMapper.findOrganizationByOrgid(orgid);
    }

    @Override
    public Organization belongToCompany(String orgid) {
        List<Object[]> list = this.baseMapper.belongToCompany(orgid);
        List<Organization> lists= toTblOrganizations(list);
        if (lists != null && lists.size()>0) {
            return lists.get(0);
        }
        return null;
    }

    private List<Organization> toTblOrganizations(List<Object[]> objs){
        if (objs!=null && objs.size()>0) {
            List<Organization> list = new ArrayList<>();
            for (int i = 0; i < objs.size(); i++) {
                Object[] obj = objs.get(i);
                Organization org = new Organization();
                org.setOrgid(obj[0]!=null?new BigDecimal(obj[0].toString()):null);
                org.setOrgname(obj[1]!=null?obj[1].toString():"");
                org.setFatherorgid(obj[2]!=null?new BigDecimal(obj[2].toString()):null);
                org.setOrgnumber(obj[3]!=null?obj[3].toString():"");
                org.setOrgmeno(obj[4]!=null?obj[4].toString():"");
                org.setMemo(obj[5]!=null?obj[5].toString():"");
                org.setIcode(obj[6]!=null?obj[6].toString():"");
                org.setOrgtype(obj[7]!=null?new BigDecimal(obj[7].toString()):new BigDecimal("0"));
                org.setStatus(obj[8]!=null?new BigDecimal(obj[8].toString()):null);
                org.setIszy(obj[9]!=null?obj[9].toString():"");
                list.add(org);
            }
            return list;
        }
        return null;
    }

    /**
     * 生成树
     */
    private List<Tree> getNoteTrees(Set<Organization> chil) {
        List<Tree> children = new ArrayList<Tree>();
        for (Organization tblOrganization2 : chil) {

            if (tblOrganization2.getStatus() != null && tblOrganization2.getStatus().equals("1")) {
                continue;
            }
            if (tblOrganization2.getOrgtype() != null && tblOrganization2.getOrgtype().equals("0")) {
                continue;
            }
            if (tblOrganization2.getStatus() != null && tblOrganization2.getStatus().equals("0")) {
                Tree tree = new Tree();
                if (tblOrganization2.getChildren().size() > 0) {
                    List<Tree> children1 = new ArrayList<Tree>();
                    children1 = getNoteTrees(tblOrganization2.getChildren());
                    tree.setChildren(children1);
                }
                tree.setName(tblOrganization2.getOrgname());
                tree.setId(tblOrganization2.getOrgid());
                tree.setpId(tblOrganization2.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(tblOrganization2.getChildren().size() > 0 ? true : false);
                children.add(tree);
            }
        }
        return children;
    }


}
