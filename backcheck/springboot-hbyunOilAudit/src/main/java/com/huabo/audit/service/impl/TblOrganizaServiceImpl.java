package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.RedisFinalUtis;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.Tree;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.config.DateBaseConfig;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.service.TblOrganizaService;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Service
@Slf4j
public class TblOrganizaServiceImpl implements TblOrganizaService {
    private static final Logger logger = LoggerFactory.getLogger(TblOrganizaServiceImpl.class);

    @Resource
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean getOrgTreeListByAuditObj(String token, BigDecimal nodeId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        String str = null;
        HashMap<String, Object> fields = new HashMap<String, Object>();
        fields.put("nodeId", nodeId);
        try {
            str = HttpClient.request(HttpClient.getOrgChildrenUrl, fields, null);
            if (!StringUtils.isNotBlank(str)) {
                List<Tree> trees = new ArrayList<Tree>();
                if (null == nodeId) {
                    nodeId = loginStaff.getCurrentOrg().getOrgid();
                    TblOrganization organization = tblOrganizationMapper.findById(nodeId);
                    Tree tree = new Tree();
                    tree.setName(organization.getOrgname());
                    tree.setId(organization.getOrgid());
                    tree.setpId(organization.getFatherorgid());
                    tree.setOpen(true);
                    tree.setIsParent(getChildrenByCommpany(organization.getOrgid()));
                    trees.add(tree);
                } else {
                    List<TblOrganization> list = this.tblOrganizationMapper.selectChildrenOrgList(nodeId);
                    for (TblOrganization tblOrganization : list) {
                        if (tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype() == 0) continue;
						/*if (tblOrganization.getStatus()!=null && tblOrganization.getStatus()==1) {
							continue;
						}*/
                        Tree tree = new Tree();
                        tree.setName(tblOrganization.getOrgname());
                        tree.setId(tblOrganization.getOrgid());
                        tree.setpId(tblOrganization.getFatherorgid());
                        tree.setOpen(true);
                        tree.setIsParent(getChildrenByCommpany(tblOrganization.getOrgid()));
                        trees.add(tree);
                    }
                }


                str = JSONObject.toJSONString(trees);
            }
        } catch (Exception e) {
            List<Tree> trees = new ArrayList<Tree>();
            if (null == nodeId) {
                nodeId = loginStaff.getCurrentOrg().getOrgid();
                TblOrganization organization = tblOrganizationMapper.findById(nodeId);
                Tree tree = new Tree();
                tree.setName(organization.getOrgname());
                tree.setId(organization.getOrgid());
                tree.setpId(organization.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(getChildrenByCommpany(organization.getOrgid()));
                trees.add(tree);
            } else {
                List<TblOrganization> list = this.tblOrganizationMapper.selectChildrenOrgList(nodeId);
                for (TblOrganization tblOrganization : list) {
                    if (tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype() == 0) continue;
					/*if (tblOrganization.getStatus()!=null && tblOrganization.getStatus()==1) {
						continue;
					}*/
                    Tree tree = new Tree();
                    tree.setName(tblOrganization.getOrgname());
                    tree.setId(tblOrganization.getOrgid());
                    tree.setpId(tblOrganization.getFatherorgid());
                    tree.setOpen(true);
                    tree.setIsParent(getChildrenByCommpany(tblOrganization.getOrgid()));
                    trees.add(tree);
                }
            }
            str = JSONObject.toJSONString(trees);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("orgTree", str);
        return ResponseFormat.retParam(1, 200, resultMap);
    }


    //	private boolean getChildrenByCommpany(Set<TblOrganization> chil) {
//        for (TblOrganization org : chil) {
//            if (org.getOrgtype() != null && org.getOrgtype() != 0) {
//                return true;
//            }
//        }
//        return false;
//    }
    private boolean getChildrenByCommpany(BigDecimal nodeId) {

        try {
            List<TblOrganization> list = this.tblOrganizationMapper.selectChildrenOrgList(nodeId);
            for (TblOrganization org : list) {
                if (org.getOrgtype() != null && org.getOrgtype() != 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

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
            //this.tblOrganizationMapper.saveTblOrganization(org);
            //tbl.add(org);
        }
        return tbl;
    }

    public TblOrganization getHY() {
        List<TblOrganization> listSql = tblOrganizationMapper.selectGetHY();
        return listSql != null && listSql.size() > 0 ? (TblOrganization) listSql.get(0) : null;
    }


    @Override
    public List<Tree> getTrees(BigDecimal nodeId) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGDEPTLIST + nodeId)) {
                    String str = jedis.get(RedisFinalUtis.ORGDEPTLIST + nodeId);
                    List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                    return trees;
                } else {
                    List<Tree> trees = new ArrayList<Tree>();
                    List<TblOrganization> list = this.tblOrganizationMapper.getNode(nodeId);
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
                jedis.close();
            }
        } else {
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGDEPTLIST + nodeId)) {
                    String str = jedis.get(RedisFinalUtis.ORGDEPTLIST + nodeId);
                    List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                    return trees;
                } else {
                    List<Tree> trees = new ArrayList<Tree>();
                    List<TblOrganization> list = this.tblOrganizationMapper.getNode(nodeId);
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
                jedis.close();
            }
        }
    }


    @Override
    public List<Tree> getNodeAlls(BigDecimal nodeId) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGDEPTLIST + nodeId)) {
                    String str = jedis.get(RedisFinalUtis.ORGDEPTLIST + nodeId);
                    List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                    return trees;
                } else {
                    List<Tree> trees = new ArrayList<Tree>();
                    List<Tree> children = new ArrayList<Tree>();
                    List<TblOrganization> list = tblOrganizationMapper.findBysql(nodeId);
                    for (TblOrganization tblOrganization : list) {
                        if (tblOrganization.getStatus() != null && tblOrganization.getStatus() == 1) {
                            continue;
                        }
                        //Set<TblOrganization> chil = tblOrganization.getChildren();
                        try {
                            List<TblOrganization> childrenOrgList = tblOrganizationMapper.selectChildrenOrgList(tblOrganization.getOrgid());
                            //Set<TblOrganization> chil = tblOrganizationMapper.findByfatherorgId(tblOrganization.getOrgid());
                            children = getNoteTrees(childrenOrgList);
                            Tree tree = new Tree();
                            tree.setChildren(children);
                            tree.setName(tblOrganization.getOrgname());
                            tree.setId(tblOrganization.getOrgid());
                            tree.setpId(tblOrganization.getFatherorgid());
                            tree.setOpen(true);
                            tree.setIsParent(tblOrganization.getChildren().size() > 0 ? true : false);
                            trees.add(tree);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return trees;
                }
            } finally {
                jedis.close();
            }
        } else {
            Jedis jedis = JedisUtil.getJedis();
            try {
                if (jedis.exists(RedisFinalUtis.ORGDEPTLIST + nodeId)) {
                    String str = jedis.get(RedisFinalUtis.ORGDEPTLIST + nodeId);
                    List<Tree> trees = (List<Tree>) JSONArray.parseArray(str, Tree.class);
                    return trees;
                } else {
                    List<Tree> trees = new ArrayList<Tree>();
                    List<Tree> children = new ArrayList<Tree>();
                    List<TblOrganization> list = tblOrganizationMapper.findBysql(nodeId);
                    for (TblOrganization tblOrganization : list) {
                        if (tblOrganization.getStatus() != null && tblOrganization.getStatus() == 1) {
                            continue;
                        }
                        //Set<TblOrganization> chil = tblOrganization.getChildren();
                        //Set<TblOrganization> chil = tblOrganizationMapper.findByfatherorgId(tblOrganization.getOrgid());
                        try {
                            List<TblOrganization> childrenOrgList = tblOrganizationMapper.selectChildrenOrgList(tblOrganization.getOrgid());
                            children = getNoteTrees(childrenOrgList);
                            Tree tree = new Tree();
                            tree.setChildren(children);
                            tree.setName(tblOrganization.getOrgname());
                            tree.setId(tblOrganization.getOrgid());
                            tree.setpId(tblOrganization.getFatherorgid());
                            tree.setOpen(true);
                            tree.setIsParent(tblOrganization.getChildren().size() > 0 ? true : false);
                            trees.add(tree);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return trees;
                }
            } finally {
                jedis.close();
            }
        }
    }

    private List<Tree> getNoteTrees(List<TblOrganization> chil) {
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
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
                        List<Tree> children1 = this.getNoteTrees(tblOrganization2.getChildren1());
                        tree.setChildren(children1);
                    }

                    tree.setName(tblOrganization2.getOrgname());
                    tree.setId(tblOrganization2.getOrgid());
                    tree.setpId(tblOrganization2.getFatherorgid());
                    tree.setOpen(true);
                    tree.setIsParent(tblOrganization2.getChildren1().size() > 0);
                    children.add(tree);
                }
            }
        } else {
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
                        List<Tree> children1 = this.getNoteTrees(tblOrganization2.getChildren1());
                        tree.setChildren(children1);
                    }

                    tree.setName(tblOrganization2.getOrgname());
                    tree.setId(tblOrganization2.getOrgid());
                    tree.setpId(tblOrganization2.getFatherorgid());
                    tree.setOpen(true);
                    tree.setIsParent(tblOrganization2.getChildren1().size() > 0);
                    children.add(tree);
                }
            }
        }
    }


    /**
     * 通过ids查询
     * @param ids
     * @return
     */
    public List<TblOrganization> selectByIds(String ids) {
        return tblOrganizationMapper.selectByIds(ids);
    }
}
