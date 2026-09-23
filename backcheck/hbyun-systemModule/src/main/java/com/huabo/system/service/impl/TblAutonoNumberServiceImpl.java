package com.huabo.system.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblAutonoInfo;
import com.huabo.system.entity.TblOrgNo;
import com.huabo.system.mapper.TblAutonoNumberDao;
import com.huabo.system.mapper.TblOrgNoMapper;
import com.huabo.system.service.TblAutonoNumberService;
import com.huabo.system.utils.Tree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("TblAutonoNumberService")
@Slf4j
public class TblAutonoNumberServiceImpl implements TblAutonoNumberService {

    @Resource
    public TblAutonoNumberDao tblAutonoNumberDao;

    @Resource
    private TblOrgNoMapper tblOrgNoMapper;
    
    @Resource
    private UserProvider userProvider;


    @Override
    public int deleteOrgNo(BigDecimal orgid) {
    	return this.tblAutonoNumberDao.deleteBy(orgid);
    }

    @Override
    public Map<String, Object> getNumberList(BigDecimal orgid, BigDecimal fatherrightid, Integer pageNumber, Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            PageInfo<TblOrgNo> pageInfo = new PageInfo<TblOrgNo>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTlist(tblOrgNoMapper.selectListByPageInfo(pageInfo, orgid, fatherrightid));
            pageInfo.setTotalRecord(tblOrgNoMapper.selectCountByPageInfo(orgid, fatherrightid));
            resultMap.put("data", pageInfo);
        resultMap.put("code", "1");
        resultMap.put("msg", "成功！");
        return resultMap;
    }

    @Override
    public String getNumberTree(String token, String staffId, BigDecimal parentid) {
        TblStaffUtil staff = null;
        try {
            staff = userProvider.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
        BigDecimal orgid = staff.getCurrentOrg().getOrgid();
        List<Tree> list = new ArrayList<Tree>();
        Tree tree = null;

        	//根据公司主键和父级节点查询所有编号设置信息
            List<TblAutonoInfo> root = tblAutonoNumberDao.listBySql(orgid, parentid);
            for (TblAutonoInfo tblAutonoInfo : root) {
                tree = new Tree();
                tree.setName(tblAutonoInfo.getNoname());
                tree.setId(tblAutonoInfo.getNoid());
                tree.setTarget("mainFramex");
                tree.setOpen(true);
                tree.setpId(tblAutonoInfo.getPatentid());
                tree.setIsParent(false);
                list.add(tree);
            }

        //编号设置不为空 则生成根级节点
        if (list.size() != 0) {
            Tree tree1 = new Tree();
            tree1.setIsParent(true);
            tree1.setName("系統编号");
            tree1.setId(new BigDecimal(1));
            tree1.setTarget("mainFramex");
            tree1.setOpen(true);
            tree1.setChildren(list);
            return com.alibaba.fastjson.JSONObject.toJSONString(tree1);
        } else {
            return null;
        }
    }

    @Override
    public Map<String, Object> updateNumber(String token, String staffId, TblOrgNo tblOrgNo) {
        Map<String,Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil staff = userProvider.get();
            BigDecimal orgid = staff.getCurrentOrg().getOrgid();
            //需要修改的自定义对象
            tblOrgNo.setOrgid(orgid);

            this.tblOrgNoMapper.updateNumber(tblOrgNo);

            resultMap.put("code", "1");
            resultMap.put("msg", "修改成功！");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public void saveOrgNumber(BigDecimal orgid) {
		 List<String> noIdList = this.tblAutonoNumberDao.selectNoIdListByOrgId(orgid);
		 if (noIdList.size() != 0) {
		     for (int i = 0; i < noIdList.size(); ++i) {
		         // TblOrgNoId noId = new TblOrgNoId();
		         TblOrgNo orgNo = new TblOrgNo();
		         Object obj = noIdList.get(i);
		         orgNo.setNoid(new BigDecimal(obj.toString()));
		         orgNo.setOrgid(orgid);
		         orgNo.setIsusedefault(0);
		         this.tblOrgNoMapper.save(orgNo);
		     }
		 }
    }

    @Override
    public String findRootNumberByParentId(String string, String string2, String string3, String string4,
                                           String string5, String string6, BigDecimal orgid, int i, Object object, Object object2, Object object3,
                                           Object object4, BigDecimal bigDecimal) {
        // TODO Auto-generated method stub
        return null;
    }


}
