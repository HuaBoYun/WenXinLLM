package com.huabo.system.service.impl;


import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblBiReportMenu;
import com.huabo.system.entity.TblManageRight;
import com.huabo.system.entity.TblManageUserRight;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblSystemProjectOracle;
import com.huabo.system.mapper.TblManageRightDAO;
import com.huabo.system.mapper.TblManageUserRightMapper;
import com.huabo.system.mapper.TblSystemProjectOracleMapper;
import com.huabo.system.service.TblManageRightService;
import com.huabo.system.utils.RedisFinalUtis;
import com.huabo.system.utils.Tree;

import redis.clients.jedis.Jedis;

@Service("TblManageRightService")
public class TblManageRightServiceImpl implements TblManageRightService {

    @Resource
    private TblManageRightDAO tblManageRightDAO;

    @Resource
    private TblManageUserRightMapper tblManageUserRightMapper;

    @Resource
    private TblSystemProjectOracleMapper tblSystemProjectOracleMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public TblManageRight findById(String id) {
        return this.tblManageRightDAO.findById(new BigDecimal(id));
    }

    @Override
    public List<Tree> getOrgFatherRightforRedis(TblOrganization org) throws Exception {
        if (null != org) {
            List<Tree> list = new ArrayList();
            List<TblManageRight> root = this.tblManageRightDAO.getTreebyorgidRoots(org.getOrgid().toString());

            Tree tree = new Tree();
            for (Iterator var4 = root.iterator(); var4.hasNext(); list.add(tree)) {
                TblManageRight tblManageRight = (TblManageRight) var4.next();
                //tree = new Tree();
                tree.setName(tblManageRight.getRightname());
                tree.setId(tblManageRight.getRightid());
                tree.setTarget("mainFramex");
                tree.setChecked(tblManageRight.getRightid() != null);
                tree.setpId(tblManageRight.getFatherrightid());
                tree.setUrl("");
                tree.setOpen(true);
                List<Tree> childre = this.addChildren(org.getOrgid(), tblManageRight.getRightid(), (List) root);
                if (childre.size() > 0) {
                    tree.setChildren(childre);
                }
            }
            return list;
        } else {
            return null;
        }
    }

    @Override
    @Transient
    public JsonBean distributionPageIdByModuleType(String[] moduleTypes, String[] pageids, String token) throws Exception {
        Integer count = 0;
        //获取当前登录用户
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        BigDecimal orgid = staff.getCurrentOrg().getOrgid();
        
        	//循环处理所有下发的所属模块
        	for (String moduleType : moduleTypes) {
            	//循环所有下发的一级主题id
                for (String pageid : pageids) {
                	//通过所属功能模块类型和公司以及一级主题id ，查询当前一级主题是否下发过
                    count = this.tblManageRightDAO.selectRightCount(orgid, pageid, moduleType);
                    if (count > 0) {
                    	//如果当前公司的功能模块下发过当前一级主题，则删除掉相应的一级主题和对应的二级主题
                        this.tblManageRightDAO.deleteBySystemBimodule(orgid, moduleType, pageid);
                    }
                    //保存下发一级主题和相应的二级主题信息
                    this.tblManageRightDAO.insertMangeRight(orgid, pageid, moduleType);
                    List<Object> childrenList = this.tblManageRightDAO.selectChildrenListByFahterId(pageid);
                    for (Object childrenId : childrenList) {
                        this.tblManageRightDAO.insertMangeRightByPageBody(orgid, childrenId, moduleType, pageid);
                    }
                }
            }
        return ResponseFormat.retParam(1, 200 , null);
    }

    @Override
    public Map<String, Object> findRightListbyModule(String token, String staffId) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblStaffUtil staff = userProvider.get();
                BigDecimal orgid = staff.getCurrentOrg().getOrgid();
                List<TblManageRight> manageList = tblManageRightDAO.selectRightListbyModule(orgid);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", manageList);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return resultMap;
    }

    @Override
    public String getTreeListSettingMenu(BigDecimal tmplId, Map<BigDecimal, Object> map, String token, String staffId) {
            List<Tree> list = new ArrayList<Tree>();
            try {
                TblStaffUtil staff = userProvider.get();
                BigDecimal orgid = staff.getCurrentOrg().getOrgid();
                Tree tree = null;
                Integer count;
                List<TblManageRight> root = tblManageRightDAO.findBySql(tmplId, orgid);
                for (TblManageRight tblManageRight : root) {
                    tree = new Tree();
                    tree.setName(tblManageRight.getRightname());
                    tree.setId(tblManageRight.getRightid());
                    tree.setTarget("mainFramex");
                    tree.setChecked(map.get(tblManageRight.getRightid()) == null ? false : true);
                    tree.setpId(tblManageRight.getFatherrightid());
                    tree.setUrl("");
                    tree.setOpen(true);
                    count = this.tblManageRightDAO.selectChildrenList(tblManageRight.getRightid());
                    if (count > 0) {
                        tree.setIsParent(true);
                    } else {
                        tree.setIsParent(false);
                    }
                    list.add(tree);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return com.alibaba.fastjson.JSONObject.toJSONString(list);
    }

    @Override
    public List<TblManageRight> findByManageParentId(BigDecimal fatherrightid, String token, String staffId) throws Exception {
        //List<Object[]> obs = tblManageRightDAO.OBJlistBySql(fatherrightid,orgid);
        List<TblManageRight> list = new ArrayList<TblManageRight>(0);
        TblStaffUtil staff = userProvider.get();
        BigDecimal orgid = staff.getCurrentOrg().getOrgid();
        list = tblManageRightDAO.OBJlistBySql(fatherrightid, orgid);
        return list;
    }

    @Override
    public TblManageRight findByRightname(String rightname) {
        return tblManageRightDAO.selectWorkRightInfo(rightname);
    }

    @Override
    public String getRightForUser(String token, String staffId) {
            String rightJson = null;
            Jedis jedis = null;
            try {
                TblStaffUtil staff = userProvider.get();
                BigDecimal userId = staff.getStaffid();
                BigDecimal orgid = staff.getCurrentOrg().getOrgid();
                List<Object[]> obs = tblManageRightDAO.findListById(userId, orgid);
                List<TblManageRight> list = new ArrayList<TblManageRight>();
                if (obs != null && obs.size() > 0) {
                    for (Object[] objects : obs) {
                        TblManageRight trs = new TblManageRight();
                        trs.setRightid(objects[0] != null ? new BigDecimal(objects[0].toString()) : null);
                        trs.setRightname(objects[1] != null && objects[1] != "" ? objects[1].toString() : objects[6].toString());
                        trs.setRighturl(objects[2] != null ? objects[2].toString() : null);
                        trs.setFatherrightid(objects[3] != null ? new BigDecimal(objects[3].toString()) : null);
                        trs.setFuncorder(objects[4] != null ? new BigDecimal(objects[4].toString()) : null);
                        trs.setIndicatorstatus(objects[5] != null ? objects[5].toString() : null);
                        list.add(trs);
                    }
                }
                rightJson = JSON.toJSONString(list);

                jedis.set(RedisFinalUtis.USERMANGERRIGHT + staff.getStaffid().toString(), rightJson);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
            	if(jedis!=null) {
            		jedis.close();
            	}
            }
            return rightJson;
    }

    @Override
    public void delright(TblManageRight vmr) {
            tblManageRightDAO.deleteById(vmr.getRightid());
    }

    @Override
    public JsonBean distributionPageIdByRightIdTwo(String[] moduleTypes, String[] pageids, String token, String pid) throws Exception {
    	String sql = "";
        Integer count = 0;
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        BigDecimal orgid = staff.getCurrentOrg().getOrgid();
        	for (String moduleType : moduleTypes) {
        		// 1.先判断一级主题是否已下发，如果没有下发，则重新下发
            	count = this.tblManageRightDAO.selectRightCount(orgid, pid, moduleType);
            	if(count == 0) {
            		this.tblManageRightDAO.insertMangeRight(orgid, pid, moduleType);
            	}
            	//2. 循环下发的二级主题，先判断是否已下发 如果没有下发则 下发，如果已下发则不执行
                for (String pageid : pageids) {
                	count = this.tblManageRightDAO.selectRightCount(orgid, pageid, moduleType);
                    if (count == 0) {
                        this.tblManageRightDAO.insertMangeRightByPageBody(orgid, pageid, moduleType, pid);
                    }
                }
            }
        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public String GetTree(String staffid, Map<BigDecimal, Object> map, String orgid) {
    	
            List<Tree> list = new ArrayList<Tree>();
            List<TblManageRight> root = this.tblManageRightDAO.getTreeRoots(staffid);
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
                        List<Tree> childre = this.addChildren1(staffid, tblManageRight.getRightid(), map, list1);
                        if (childre.size() > 0) {
                            tree.setChildren(childre);
                        }
                        list.add(tree);
                    }
                }
            }
            return JSONObject.toJSONString(list);
    }

    public List<Tree> addChildren1(String staffid, BigDecimal pId, Map<BigDecimal, Object> map, List<TblManageRight> lists) {
        List<Tree> list = new ArrayList<Tree>();
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
                    List<Tree> trees = addChildren1(staffid, tblManageRight.getRightid(), map, lists);
                    if (trees.size() > 0) {
                        tree.setChildren(trees);
                    }
                    list.add(tree);
                }
            }
        }
        return list;
    }

    public List<Tree> addChildren(Serializable tmplId, BigDecimal pId, List<TblManageRight> lists) {
        List<Tree> list = new ArrayList();
        List<TblManageRight> root = this.tblManageRightDAO.getTreeByNodeId(pId);
        Iterator var6 = root.iterator();

        while (var6.hasNext()) {
            TblManageRight tblManageRight = (TblManageRight) var6.next();
            Iterator var8 = lists.iterator();

            while (var8.hasNext()) {
                TblManageRight right = (TblManageRight) var8.next();
                if (tblManageRight.getRightid().toString().equals(right.getRightid().toString())) {
                    Tree tree = new Tree();
                    tree.setName(tblManageRight.getRightname());
                    tree.setId(tblManageRight.getRightid());
                    tree.setTarget("mainFramex");
                    tree.setChecked(tblManageRight.getRightid() != null);
                    tree.setpId(tblManageRight.getFatherrightid());
                    tree.setUrl("");
                    tree.setIsParent(false);
                    tree.setUrl("");
                    List<Tree> trees = this.addChildren(tmplId, tblManageRight.getRightid(), lists);
                    if (trees.size() > 0) {
                        tree.setChildren(trees);
                    }

                    list.add(tree);
                }
            }
        }

        return list;
    }

    @Override
    public JsonBean findChildrenRightListByUser(String token, BigDecimal rightId)
            throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (rightId == null) {
            rightId = BigDecimal.valueOf(-1);
        }
        List<TblManageRight> rightList = this.tblManageRightDAO.selectChildrenRightListByUser(rightId, loginStaff.getStaffid(), loginStaff.getLinkOrg().getOrgid());
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("rightList", rightList);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean saveManageRight(TblManageRight right, String token) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.tblManageRightDAO.insertTblManageRight(right);
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("rightId", right.getRightid());
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean findRightEntityById(BigDecimal rightId) throws Exception {
            TblManageRight right = this.tblManageRightDAO.findById(rightId);
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap.put("right", right);
            return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean modifyManageRight(TblManageRight right, String token) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.tblManageRightDAO.updateTblManageRight(right);
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("rightId", right.getRightid());
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    @Transactional(value = "oracleDataSourceTransactionManager", rollbackFor = Exception.class, timeout = 36000)
    public JsonBean removeManageRight(BigDecimal rightId) throws Exception {
            String sql = "DELETE FROM TBL_MANAGE_USER_RIGHT WHERE RIGHTID = " + rightId;
            this.tblManageRightDAO.executeDeleteSql(sql);
            sql = "DELETE FROM TBL_ORG_RIGHT WHERE RIGHTID = " + rightId;
            this.tblManageRightDAO.executeDeleteSql(sql);
            sql = "DELETE FROM TBL_ORG_RIGHT_NEW WHERE RIGHTID = " + rightId;
            this.tblManageRightDAO.executeDeleteSql(sql);
            sql = "DELETE FROM TBL_MANAGE_RIGHT WHERE RIGHTID = " + rightId;
            this.tblManageRightDAO.executeDeleteSql(sql);
            return ResponseFormat.retParam(1, 200, null);
    }

    // tblManageScreenRightDAO.deleteUserId(userid);
//    String a[] = priid.split(",");
//        for (String pri : a){
//        tblManageScreenRightDAO.insertUserId(userid,pri);
//    }
    @Override
    public void grantScreenRight(String userid, String priid) {
            tblManageRightDAO.deleteUserId(userid);
            String[] rightIds = priid.split(",");
            for (String rightId : rightIds) {
                tblManageRightDAO.insertUserId(userid, rightId);
            }
    }

    @Override
    public void save(TblManageRight viewTblManageRight) {
    	viewTblManageRight.setRightid(RandomUtil.uuBigDecimalId());
        this.tblManageRightDAO.insert(viewTblManageRight);
    }


    @Override
    public List<TblManageRight> findByUserAll(String userid) {
        return tblManageRightDAO.findByUserAll(userid);
    }

    @Override
    public void updateright(TblManageRight tblManageRight) {
        this.tblManageRightDAO.updateById(tblManageRight);
    }

    @Override
    public void inserUserRight(TblManageUserRight userRight) {
        this.tblManageUserRightMapper.inserUserRight(userRight);
    }

    @Override
    public List<TblManageRight> findByorgid(BigDecimal id) {
        return this.tblManageRightDAO.findByorgid(id);
    }

    @Override
    public List<TblBiReportMenu> findBiReportList(BigDecimal orgid, BigDecimal rightid) throws Exception {
            return null;
    }
    
    @Override
    public JsonBean cancelModuleList(String[] pageids, String token) throws Exception {
        //获取当前登录用户
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //凭借传过来的主题仓库主键
        String pageIdStrs = "";
        for (String id : pageids) {
			pageIdStrs += id+",";
		}
        pageIdStrs = pageIdStrs.substring(0, pageIdStrs.length()-1);
        Map<String,Object> resultMap = new HashMap<String, Object>(0);
        	//获取取消模块集合
        	List<TblSystemProjectOracle> list = this.tblSystemProjectOracleMapper.selectCancelListByTheme(staff.getCurrentOrg().getOrgid(),pageIdStrs);
        	resultMap.put("list", list);
        return ResponseFormat.retParam(1, 200 , resultMap);
    }
    
    @Override
    public JsonBean cancelBiModule(String[] pageids, String[] moduleTypes, String token, Integer type) throws Exception {
        //获取当前登录用户
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        	//判断取消的是一级主题还是二级主题
        	if(type == 1) {
        		//一级主题取消后 需取消下所有的二级主题
        		for (String module : moduleTypes) {
					for (String pageId : pageids) {
						//取消当前主题
						this.tblManageRightDAO.deleteBiModule(module,pageId,staff.getCurrentOrg().getOrgid());
						//取消一级主题下对应的所有的二级主题
						this.tblManageRightDAO.deleteBiModuleChildren(module,pageId,staff.getCurrentOrg().getOrgid());
					}
				}
        	}else {
        		//取消二级主题直接删除
        		for (String module : moduleTypes) {
					for (String pageId : pageids) {
						this.tblManageRightDAO.deleteBiModule(module,pageId,staff.getCurrentOrg().getOrgid());
					}
				}
        	}
        return ResponseFormat.retParam(1, 200 , null);
    }
    
    @Override
    public JsonBean cancelBiStaff(String[] pageids, String[] staffids, String token, Integer type) throws Exception {
        //获取当前登录用户
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        	//判断取消的是一级主题还是二级主题
        	if(type == 1) {
        		//一级主题取消后 需取消下所有的二级主题
        		for (String staffid : staffids) {
					for (String pageId : pageids) {
						//取消当前主题
						this.tblManageRightDAO.deleteBiStaff(staffid,pageId);
						//取消一级主题下对应的所有的二级主题
						this.tblManageRightDAO.deleteBiStaffChildren(staffid,pageId);
					}
				}
        	}else {
        		//取消二级主题直接删除
        		for (String staffid : staffids) {
					for (String pageId : pageids) {
						this.tblManageRightDAO.deleteBiStaff(staffid,pageId);
					}
				}
        	}
        return ResponseFormat.retParam(1, 200 , null);
    }
}
