package com.huabo.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblManageScreenRight;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.mapper.TblManageScreenRightDAO;
import com.huabo.system.service.TblManageScreenRightService;
import com.huabo.system.utils.Tree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class TblManageScreenRightServiceImpl implements TblManageScreenRightService {

    @Resource
    private TblManageScreenRightDAO tblManageScreenRightDAO;
    
    @Resource
    private UserProvider userProvider;
    
    @Override
    public JsonBean getScreenRoleRightList(BigDecimal rightId) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (rightId == null) {
            rightId = new BigDecimal(-1);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        if (loginStaff.getRoleIdStrs() == null || "".equals(loginStaff.getRoleIdStrs())) {
            resultMap.put("rightList", null);
            return ResponseFormat.retParam(1, 200, resultMap);
        }
		//通过父级权限ID 和用户所属角色 获取用友的 报表权限列表数据
		List<TblManageScreenRight> rightList = this.tblManageScreenRightDAO.selectChildrenRightListByRole(rightId, loginStaff.getRoleIdStrs());
		//递归查询子集报表权限列表数据
		this.setChidrenRightListByRole(rightList, loginStaff.getRoleIdStrs());
		resultMap.put("rightList", rightList);
        return ResponseFormat.retParam(1, 200, resultMap);
    }
    

    @Override
    public void grantScreenRight(String userid, String priid) {
            tblManageScreenRightDAO.deleteUserId(userid);
            String[] rightIds = priid.split(",");
            for (String rightId : rightIds) {
                tblManageScreenRightDAO.insertUserId(userid, rightId);
            }
    }

    @Override
    public String getTree(TblStaff tblStaff) throws Exception {
            List<TblManageScreenRight> rightList = this.tblManageScreenRightDAO.findListByGetRight(tblStaff.getStaffid());
            List<Tree> list = new ArrayList(0);
            Tree tree = null;

            for (Iterator var6 = rightList.iterator(); var6.hasNext(); list.add(tree)) {
                TblManageScreenRight right = (TblManageScreenRight) var6.next();
                tree = new Tree();
                tree.setName(right.getRightName());
                tree.setId(right.getRightId());
                tree.setTarget("mainFramex");
                tree.setChecked(right.getStaffid() == null ? false : true);
                tree.setpId(right.getFatherId());
                tree.setUrl("");
                tree.setOpen(true);
                List<Tree> childre = this.addChildren(right.getRightId(), tblStaff.getStaffid());
                if (childre.size() > 0) {
                    tree.setChildren(childre);
                }
            }

            return JSONObject.toJSONString(list);
    }

    private List<Tree> addChildren(BigDecimal rightId, BigDecimal staffid) throws Exception {
            //String sql = "SELECT TMSR.RIGHTID,TMSR.RIGHTNAME,TMSR.FATHERID,(SELECT STAFFID FROM TBL_MANAGE_USER_SCREEN WHERE RIGTHID = TMSR.RIGHTID AND STAFFID = " + staffid + ") STAFFID  FROM TBL_MANAGE_SCREEN_RIGHT TMSR WHERE TMSR.RIGHTSTATUS = 1 AND TMSR.FATHERID = " + rightId;
            List<TblManageScreenRight> rightList = this.tblManageScreenRightDAO.findListByGet(staffid, rightId);
            List<Tree> list = new ArrayList(0);
            Tree tree = null;

            for (Iterator var7 = rightList.iterator(); var7.hasNext(); list.add(tree)) {
                TblManageScreenRight right = (TblManageScreenRight) var7.next();
                tree = new Tree();
                tree.setName(right.getRightName());
                tree.setId(right.getRightId());
                tree.setTarget("mainFramex");
                tree.setChecked(right.getStaffid() == null ? false : true);
                tree.setpId(right.getFatherId());
                tree.setUrl("");
                tree.setOpen(true);
                List<Tree> childre = this.addChildren(right.getRightId(), staffid);
                if (childre.size() > 0) {
                    tree.setChildren(childre);
                }
            }

            return list;
    }

    @Override
    public void grantScreenRightToRole(String roleId, String rightIds) throws Exception {
		//1.删除之前的授权数据
		tblManageScreenRightDAO.deleteRoleId(roleId);
		//2.分割权限字符串，循环授权
		String[] rights = rightIds.split(",");
		for (String rightId : rights) {
		    tblManageScreenRightDAO.insertRoleId(roleId, rightId);
		}
    }

   

    private void setChidrenRightListByRole(List<TblManageScreenRight> rightList, String roleIdStrs) {
        List<TblManageScreenRight> childrenList = null;
        for (TblManageScreenRight right : rightList) {
            childrenList = this.tblManageScreenRightDAO.selectChildrenRightListByRole(right.getRightId(), roleIdStrs);
            this.setChidrenRightListByRole(childrenList, roleIdStrs);
            right.setChildren(childrenList);
        }
    }

    @Override
    public JsonBean getAllRightList(String token, BigDecimal rightId, BigDecimal roleId) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //父级ID为空 默认为-1 根级节点
        if (rightId == null) {
            rightId = new BigDecimal(-1);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
	    //根据父级权限ID获取所有下级权限数据
	    List<TblManageScreenRight> rightList = this.tblManageScreenRightDAO.selectAllRightListByRole(rightId);
	    this.setAllChidrenRightListByRole(rightList, roleId);
	    resultMap.put("rightList", rightList);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    private void setAllChidrenRightListByRole(List<TblManageScreenRight> rightList, BigDecimal roleId) {
        List<TblManageScreenRight> childrenList = null;
        Integer count = 0;
        for (TblManageScreenRight right : rightList) {
            childrenList = this.tblManageScreenRightDAO.selectAllRightListByRole(right.getRightId());
            count = this.tblManageScreenRightDAO.judgeScreenRightByRoleId(right.getRightId(), roleId);
            if (count == 0) {
                right.setChecked(false);
            } else {
                right.setChecked(true);
            }
            this.setAllChidrenRightListByRole(childrenList, roleId);
            right.setChildren(childrenList);
        }
    }

}
