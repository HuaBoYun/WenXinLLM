package com.huabo.audit.service.impl;

import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblNbsjMb;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjMbMapper;
import com.huabo.audit.service.TblNbsjMbService;
import com.huabo.audit.util.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 审计模板
 *
 * @author T
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjMbServiceImpl implements TblNbsjMbService {

    @Resource
    private TblNbsjMbMapper tblNbsjMbMapper;

    @Resource
    private TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean saveOrUpdate(TblNbsjMb mb, String token, BigDecimal mbid, String attids) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        mb.setUpdatedtime(new Date());
        mb.setStaffid(user.getStaffid());
        mb.setOrgid(user.getCurrentOrg().getOrgid());
        if (mbid != null) {
            tblNbsjMbMapper.updateEntity(mb);
        } else {
            mb.setCreatedtime(new Date());
            tblNbsjMbMapper.insertEntity(mb);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        if (attids != null && !"".equals(attids)) {
            String[] attId = attids.split(",");
            for (String aid : attId) {
                tblNbsjMbMapper.insertAttInfoAtt(mb.getMbid(), aid);
            }
        }
        resultMap.put("mb", mb);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean delete(BigDecimal mbid, String token) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (mbid == null) {
            return ResponseFormat.retParam(0, 50001, null);
        }
        Integer integer = tblNbsjMbMapper.selectNbsjMbByyy(user.getCurrentOrg().getOrgid(), mbid);
        if (integer <= 0) {
            tblNbsjMbMapper.deleteAttInfoAttBymbid(mbid);
            tblNbsjMbMapper.deleteMbById(mbid);
            return ResponseFormat.retParam(1, 200, null);
        } else {
            return ResponseFormat.retParam(0, "模板库已被引用，不能删除", null);
        }

    }

    @Override
    public JsonBean findByMbid(BigDecimal mbid, String token) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("mb", tblNbsjMbMapper.selectNbsjMbByID(mbid));
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean findAll(String code, String name, Integer startIndex, Integer pageSize, String token, String auditype) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblNbsjMb mb = new TblNbsjMb();
        mb.setAudittype(auditype);
        mb.setMbcode(code);
        mb.setMbname(name);
		/*Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	PageInfo<TblNbsjMb> pageInfo = new PageInfo<TblNbsjMb>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(startIndex);
    	
    	pageInfo.setTlist(tblNbsjMbMapper.selectNbsjMbByPageInfo(pageInfo, user.getCurrentOrg().getOrgid(), mb));
    	pageInfo.setTotalRecord(tblNbsjMbMapper.selectNbsjMbByPageInfoCount(user.getCurrentOrg().getOrgid(), mb));
    	resultMap.put("pageInfo", pageInfo);
    	return  ResponseFormat.retParam(1,200,resultMap);*/

        //链表分页  xml 写法
        com.github.pagehelper.PageInfo<TblNbsjMb> pageInfo = PageMethod.startPage(startIndex, pageSize)
                .doSelectPageInfo(() -> {
                    try {
                        /*this.tblNbsjMbMapper.findList(user.getCurrentOrg().getOrgid(), mb);*/
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        //分页参数 转换成 分页对象 （不同系统分页对象返回可能不一样 可自行转换）
        PageResult<TblNbsjMb> build = new PageResult<TblNbsjMb>().build(pageInfo);
        return ResponseFormat.retParam(1, 200, build);
    }

    @Override
    public JsonBean getAttListByMbId(String token, BigDecimal mbid) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (mbid == null) {
            return ResponseFormat.retParam(0, 50001, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("attList", tblAttachmentMapper.selectAttListByMbId(mbid));
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean delAttListByattId(String token, BigDecimal attid) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (attid == null) {
            return ResponseFormat.retParam(0, 50001, null);
        }
        tblNbsjMbMapper.deleteAttInfoAttid(attid);
        tblAttachmentMapper.deleteEntity(attid);
        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public JsonBean findAllByDatapreID(String token, Integer dataperid) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("mblist", tblNbsjMbMapper.selectNbsjMbByDateperidPageInfo(dataperid));
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean delAttListBymbId(String token, BigDecimal mbid) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (mbid == null) {
            return ResponseFormat.retParam(0, 50001, null);
        }
        tblNbsjMbMapper.delAttListBymbId(mbid);
        return ResponseFormat.retParam(1, 200, null);
    }


}
