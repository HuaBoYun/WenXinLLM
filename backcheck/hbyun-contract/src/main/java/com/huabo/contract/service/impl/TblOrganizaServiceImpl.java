package com.huabo.contract.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.*;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblOrganization;
import com.huabo.contract.mapper.TblOrganizationMapper;
import com.huabo.contract.node.Node;
import com.huabo.contract.service.TblOrganizaService;
import com.huabo.contract.util.RedisFinalUtis;
import com.huabo.contract.util.Tree;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
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
    private UserProvider userProvider;

	@Override
	public List<Tree> getTree(BigDecimal nodeId) throws Exception {
		Jedis jedis = JedisUtil.getJedis();
        List var5;
        try {
            List trees;
            if (!jedis.exists("orgdept_list_" + nodeId)) {
                List<Tree> trees1 = new ArrayList();
                trees1 = this.tblOrganizationMapper.getNodes(nodeId);
                Iterator var12 = trees1.iterator();
                Tree tree = null;
                int count = 0 ;
                while (var12.hasNext()) {
                    TblOrganization tblOrganization = (TblOrganization) var12.next();
                    tree = new Tree();
                    tree.setName(tblOrganization.getOrgname());
                    tree.setId(tblOrganization.getOrgid());
                    tree.setpId(tblOrganization.getFatherorgid());
                    tree.setOpen(true);
                    count = this.tblOrganizationMapper.selectCountByFahterOrgid(tblOrganization.getOrgid());
                    tree.setIsParent(count > 0);
                    if ((tblOrganization.getOrgtype() == null || tblOrganization.getOrgtype() == 0) && (tblOrganization.getStatus() == null || tblOrganization.getStatus() != 1) && tblOrganization.getStatus() != null && tblOrganization.getStatus() == 0) {
                        trees1.add(tree);
                    }
                }

                ArrayList var13 = (ArrayList) trees1;
                return var13;
            }else {
            	String str = jedis.get("orgdept_list_" + nodeId);
                trees = JSONArray.parseArray(str, Tree.class);
            }
            var5 = trees;
        } finally {
        	jedis.close();
        }
        return var5;
	}

	@Override
	public List<Tree> getNodeAll(BigDecimal nodeId) throws Exception {
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
                List<TblOrganization> list = this.tblOrganizationMapper.findBysql(nodeId);
                Iterator var7 = list.iterator();

                while (var7.hasNext()) {
                    TblOrganization tblOrganization = (TblOrganization) var7.next();
                    if (tblOrganization.getStatus() == null || tblOrganization.getStatus() != 1) {
                        Set<TblOrganization> chil =  this.tblOrganizationMapper.selectSetByFatherOrgId(tblOrganization.getOrgid());
                        children = this.getNoteTrees(chil);
                        Tree tree = new Tree();
                        tree.setChildren(children);
                        tree.setName(tblOrganization.getOrgname());
                        tree.setId(tblOrganization.getOrgid());
                        tree.setpId(tblOrganization.getFatherorgid());
                        tree.setOpen(true);
                        tree.setIsParent(chil.size() > 0);
                        trees.add(tree);
                    }
                }

                ArrayList var16 = (ArrayList) trees;
                return var16;
            }
        } finally {
        	jedis.close();
        }
	}

	private List getNoteTrees(Set<TblOrganization> chil) throws Exception {
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
                Set<TblOrganization> children2 = this.tblOrganizationMapper.selectSetByFatherOrgId(tblOrganization2.getOrgid());
                if (children2.size() > 0) {
                    new ArrayList();
                   
                    List<Tree> children1 = this.getNoteTrees(children2);
                    tree.setChildren(children1);
                }
                tree.setName(tblOrganization2.getOrgname());
                tree.setId(tblOrganization2.getOrgid());
                tree.setpId(tblOrganization2.getFatherorgid());
                tree.setOpen(true);
                tree.setIsParent(children2.size() > 0);
                children.add(tree);
            }
        }
	}

	@Override
	public String selectChidrenIdStrsByFatherOrgId(String orgId) throws Exception {
		List<String> orgIdList = this.tblOrganizationMapper.selectChildrenIdListByFatherOrgId(orgId);
		if(orgIdList == null || orgIdList.size() == 0){
			return orgId;
		}
		String orgIdIdStrs = String.join(",", orgIdList);
		return this.getChidrenIdStrsByFatherOrgIds(orgIdIdStrs,orgId+","+orgIdIdStrs);
	}

	private String getChidrenIdStrsByFatherOrgIds(String orgIdIdStrs, String totalIds) throws Exception {
		List<String> orgIdList = this.tblOrganizationMapper.selectChildrenIdListByFatherOrgId(orgIdIdStrs);
		if(orgIdList == null || orgIdList.size() == 0){
			return totalIds;
		}
		orgIdIdStrs = String.join(",", orgIdList);
		return this.getChidrenIdStrsByFatherOrgIds(orgIdIdStrs,totalIds+","+orgIdIdStrs);
	}

	@Override
	public String selectDeptIdStrsByFatherOrgId(BigDecimal orgid) throws Exception {
		List<String> orgIdList = this.tblOrganizationMapper.selectDeptIdListByFatherOrgId(orgid.toString());
		if(orgIdList == null || orgIdList.size() == 0){
			return orgid.toString();
		}
		String orgIdIdStrs = String.join(",", orgIdList);
		return this.getChidrenDeptIdStrsByFatherOrgIds(orgIdIdStrs,orgIdIdStrs);
	}

	private String getChidrenDeptIdStrsByFatherOrgIds(String orgIdIdStrs, String totalIds) {
		List<String> orgIdList = this.tblOrganizationMapper.selectDeptIdListByFatherOrgId(orgIdIdStrs);
		if(orgIdList == null || orgIdList.size() == 0){
			return totalIds;
		}
		orgIdIdStrs = String.join(",", orgIdList);
		return this.getChidrenDeptIdStrsByFatherOrgIds(orgIdIdStrs,totalIds+","+orgIdIdStrs);
	}

	@Override
	public String findOrgByAllGSJT(String token, String staffId) {
		Jedis jedis = JedisUtil.getJedis();
        Node root = null;
        try {
            TblStaffUtil user = userProvider.get();

            if (jedis.exists(RedisFinalUtis.ORGFATHERIDSTR + user.getCurrentOrg().getOrgid())) {
                String str = jedis.get(RedisFinalUtis.ORGFATHERIDSTR + user.getCurrentOrg().getOrgid());
                return str;
            } else {
                List<TblOrganization> organizations = tblOrganizationMapper.findByOrgid(user.getCurrentOrg().getOrgid());
                TblOrganization organization = tblOrganizationMapper.findById(user.getCurrentOrg().getOrgid());
                List<TblOrganization> list = tblOrganizationMapper.findByOrgid(user.getCurrentOrg().getOrgid());
                list.add(organization);
                walkDepartmentListGS(organizations, list);
                Map nodeList = new LinkedHashMap();

                Integer str = null;
                String pc = ",";
                for (int i = 0; i < list.size(); i++) {
                    TblOrganization tbl = list.get(i);
                    //Map dataRecord = (Map) it.next();
                    if (tbl.getStatus() != null && tbl.getStatus() == 1) {
                        continue;
                    }
                    if (i > 0 && tbl.getOrgtype() != null && tbl.getOrgtype() == 0 && !tbl.getOrgid().toString().equals(user.getCurrentOrg().getOrgid())) {
                        pc += tbl.getOrgid().toString() + ",";
                        continue;
                    }
                    if (null != tbl.getFatherorgid()) {
                        if (pc.contains("," + tbl.getFatherorgid().toString() + ",")) {
                            pc += tbl.getOrgid().toString() + ",";
                            continue;
                        }
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
                                node.state = "closed";//closed
                                break;
                            }
                            if (copyMap.getFatherorgid().toString().equals(node.id) && copyMap.getOrgtype() > 0 && copyMap.getOrgtype() < 100) {
                                node.state = "closed";
                                break;
                            }
                        }
                    }
                    if (null != tbl.getFatherorgid()) {
                        if (tbl.getFatherorgid().toString().equals("-1") || tbl.getFatherorgid().toString().equals("1")) {
                            node.state = "open";
                        }
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
                logger.info("组织切换返回值为：" + root.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	jedis.close();
        }
        return root.toString();
	}

	private static void walkDepartmentListGS(Collection<TblOrganization> toList, List<TblOrganization> list) {
        Iterator var2 = toList.iterator();

        while(var2.hasNext()) {
            TblOrganization top = (TblOrganization)var2.next();
            if (top.getOrgtype() != null && top.getOrgtype() != 0 && top.getStatus() != null && top.getStatus() == 0) {
                TblOrganization copy = new TblOrganization();
                copy.setOrgid(top.getOrgid());
                copy.setOrgname(top.getOrgname());
                copy.setFatherorgid(top.getFatherorgid());
                copy.setOrgtype(top.getOrgtype());
                copy.setStatus(top.getStatus());
                list.add(copy);
                walkDepartmentListGS(top.getChildren(), list);
            }
        }

    }

	@Override
	public JsonBean findOrgDeptTree(String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		HashMap<String, Object> fields = new HashMap<String, Object>();
		fields.put("nodeId",user.getCurrentOrg().getOrgid().toString());
		
		String orgtree = null;
		try {
			orgtree = HttpClient.request(com.huabo.contract.util.HttpClient.getDeptString, fields, null);
			
			if(!StringUtils.isNotBlank(orgtree)) {
				orgtree = this.findOrgByAllJT(user.getCurrentOrg().getOrgid().toString());
			}
		} catch (Exception e) {
			orgtree = this.findOrgByAllJT(user.getCurrentOrg().getOrgid().toString());
		}
		return ResponseFormat.retParam(1, 200, orgtree);
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

	@Override
	public String findOrgByAllJT(String orgid) throws Exception {
		Jedis jedis = JedisUtil.getJedis();
        try {
            if (jedis.exists(RedisFinalUtis.ORGROOTSTR + orgid)) {
                String str = jedis.get(RedisFinalUtis.ORGROOTSTR + orgid);
                return str;
            } else {
                List<TblOrganization> organizations = tblOrganizationMapper.findBysql(new BigDecimal(orgid));
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
                                node.state = "closed";//closed
                                break;
                            }
                            if (copyMap.getFatherorgid().toString().equals(node.id) && copyMap.getOrgtype() > 0 && copyMap.getOrgtype() < 100) {
                                node.state = "open";
                                break;
                            }
                        }
                    }
                    if (null != tbl.getFatherorgid()) {
                        if (tbl.getFatherorgid().toString().equals("-1")) {
                            node.state = "open";
                        }
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
        	jedis.close();
        }
	}

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

	@Override
	public List<Tree> getNodeAlls(BigDecimal nodeId) throws Exception {
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
                    Set<TblOrganization> chil = tblOrganizationMapper.findByfatherorgId(tblOrganization.getOrgid());
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
        	jedis.close();
        }
	}

	@Override
	public JsonBean getOrgTreeListByAuditObj(String token, BigDecimal nodeId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
    	
    	
		if (null == nodeId) {
			nodeId = loginStaff.getLinkOrg().getOrgid();
		}
		String str = null;
		HashMap<String, Object> fields = new HashMap<String, Object>();
		fields.put("nodeId",nodeId);
		try {
			str = HttpClient.request(HttpClient.getOrgChildrenUrl, fields, null);
			if(!StringUtils.isNotBlank(str)) {
				List<Tree> trees = new ArrayList<Tree>();
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
		           // tree.setIsParent(getChildrenByCommpany(tblOrganization.getChildren()));
		            trees.add(tree);
		        }
		        str = JSONObject.toJSONString(trees);
			}
		} catch (Exception e) {
			List<Tree> trees = new ArrayList<Tree>();
	        List<TblOrganization> list = this.tblOrganizationMapper.selectChildrenOrgList(nodeId);
	        for (TblOrganization tblOrganization : list) {
	            if (tblOrganization.getOrgtype() != null && tblOrganization.getOrgtype() == 0) continue;
	            Tree tree = new Tree();
	            tree.setName(tblOrganization.getOrgname());
	            tree.setId(tblOrganization.getOrgid());
	            tree.setpId(tblOrganization.getFatherorgid());
	            tree.setOpen(true);
	            //tree.setIsParent(getChildrenByCommpany(tblOrganization.getChildren()));
	            trees.add(tree);
	        }
	        str = JSONObject.toJSONString(trees);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("orgTree", str);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	/**
	 * 获取组织名称
	 * @param orgId
	 * @return
	 */
	@Override
	public String getOrgName(Long orgId) {
		return tblOrganizationMapper.getOrgName(orgId);
	}

	/**
	 * 获取组织名称
	 * @param orgIds
	 * @return
	 */
	@Override
	public String getOrgNames(String orgIds) {
		if (StringUtil.isEmpty(orgIds)) {
			return null;
		}
		List<String> orgNames = tblOrganizationMapper.getOrgNames(orgIds);
		if (CollectionUtil.isEmpty(orgNames)) {
			return null;
		}
		return orgNames.stream().distinct().collect(Collectors.joining(","));
	}

}