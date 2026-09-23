package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsWgzzCljg;
import com.huabo.audit.oracle.entity.TblYqnsWgzzFlczyj;
import com.huabo.audit.oracle.entity.TblYqnsWgzzWtdz;
import com.huabo.audit.oracle.entity.TblYqnsWgzzWthc;
import com.huabo.audit.oracle.entity.TblYqnsWgzzWtsl;
import com.huabo.audit.oracle.entity.TblYqnsWgzzYsjgws;
import com.huabo.audit.oracle.entity.TblYqnsWgzzYstz;
import com.huabo.audit.oracle.mapper.TblYqnsWgzzCljgMapper;
import com.huabo.audit.oracle.mapper.TblYqnsWgzzFlczyjMapper;
import com.huabo.audit.oracle.mapper.TblYqnsWgzzWtdzMapper;
import com.huabo.audit.oracle.mapper.TblYqnsWgzzWthcMapper;
import com.huabo.audit.oracle.mapper.TblYqnsWgzzWtslMapper;
import com.huabo.audit.oracle.mapper.TblYqnsWgzzYsjgwsMapper;
import com.huabo.audit.oracle.mapper.TblYqnsWgzzYstzMapper;
import com.huabo.audit.service.TblWgzzWghsService;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;

@Service
public class TblWghsServiceimpl implements TblWgzzWghsService {

    @Resource
    TblYqnsWgzzFlczyjMapper tblYqnsWgzzFlczyjMapper;

    @Resource
    TblYqnsWgzzWthcMapper tblYqnsWgzzWthcMapper;

    @Resource
    TblYqnsWgzzWtdzMapper tblYqnsWgzzWtdzMapper;

    @Resource
    TblYqnsWgzzYsjgwsMapper tblYqnsWgzzYsjgwsMapper;

    @Resource
    TblYqnsWgzzYstzMapper tblYqnsWgzzYstzMapper;

    @Resource
    TblYqnsWgzzWtslMapper tblYqnsWgzzWtslMapper;

    @Resource
    TblYqnsWgzzCljgMapper tblYqnsWgzzCljgMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public JsonBean flczyjList(String token, Integer pageNumber, Integer pageSize,TblYqnsWgzzFlczyj tblYqnsWgzzFlczyj) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        tblYqnsWgzzFlczyj.setCreator(loginStaff.getStaffid());
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblYqnsWgzzFlczyj> pageInfo = new PageInfo<TblYqnsWgzzFlczyj>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblYqnsWgzzFlczyjMapper.getByFlczyjList(pageInfo, tblYqnsWgzzFlczyj));
        pageInfo.setTotalRecord(this.tblYqnsWgzzFlczyjMapper.getByFlczyjCount(pageInfo, tblYqnsWgzzFlczyj));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean flczyjDetail(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblYqnsWgzzFlczyj tblYqnsWgzzFlczyj = tblYqnsWgzzFlczyjMapper.flczyjDetail(id);
        resultMap.put("tblYqnsWgzzFlczyj",tblYqnsWgzzFlczyj);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public JsonBean flczyjSave(String token, TblYqnsWgzzFlczyj tblYqnsWgzzFlczyj) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<>();

        tblYqnsWgzzFlczyj.setStatus(0);
        tblYqnsWgzzFlczyj.setCreator(loginStaff.getStaffid());

        if (tblYqnsWgzzFlczyj.getId() != null) {
            //修改
            tblYqnsWgzzFlczyjMapper.updateByPrimaryKeySelective(tblYqnsWgzzFlczyj);

            tblYqnsWgzzFlczyjMapper.deletefile(tblYqnsWgzzFlczyj.getId());
            if (tblYqnsWgzzFlczyj.getAttIds()!=null && !"".equals(tblYqnsWgzzFlczyj.getAttIds())) {
                String[] attId = tblYqnsWgzzFlczyj.getAttIds().split(",");
                for (String aid : attId) {
                    this.tblYqnsWgzzFlczyjMapper.insertSHBGAttInfoForPlan(tblYqnsWgzzFlczyj.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        } else {
            //新增
            tblYqnsWgzzFlczyjMapper.insertSelective(tblYqnsWgzzFlczyj);
            if (tblYqnsWgzzFlczyj.getAttIds() != null && !"".equals(tblYqnsWgzzFlczyj.getAttIds())) {
                String[] attId = tblYqnsWgzzFlczyj.getAttIds().split(",");
                for (String aid : attId) {
                    this.tblYqnsWgzzFlczyjMapper.insertSHBGAttInfoForPlan(tblYqnsWgzzFlczyj.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        }

        resultMap.put("tblYqnsWgzzFlczyj", tblYqnsWgzzFlczyj);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean flczyjDelete(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.tblYqnsWgzzFlczyjMapper.flczyjDelete(id);

        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public R flczyjFilesDelete(String token, String attId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return R.fail("用户已失效！");
        }
        return this.removeRealtionAttInfo(attId);
    }

    private R removeRealtionAttInfo(String attId) throws Exception {
        boolean b = false;
        TblAttachment att = this.tblYqnsWgzzFlczyjMapper.selectEntityById(attId);
        if(att!=null){
            this.tblYqnsWgzzFlczyjMapper.deleteSHBGFileInfoByAttId(att.getAttid());
            this.tblYqnsWgzzFlczyjMapper.deleteEntity(att.getAttid());
        }
        return R.success();
    }

    @Override
    public JsonBean flczyjFileList(String token, BigDecimal id) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            List<TblAttachment> attList = this.tblYqnsWgzzFlczyjMapper.findAttachmentListByWgbg(id);
            resultMap.put("attList", attList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1,200,resultMap);
    }


    //==问题核查Begin
    @Override
    public JsonBean wthcList(String token, Integer pageNumber, Integer pageSize,TblYqnsWgzzWthc tblYqnsWgzzWthc) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        tblYqnsWgzzWthc.setCreator(loginStaff.getStaffid());
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblYqnsWgzzWthc> pageInfo = new PageInfo<TblYqnsWgzzWthc>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblYqnsWgzzWthcMapper.getByWthcList(pageInfo, tblYqnsWgzzWthc));
        pageInfo.setTotalRecord(this.tblYqnsWgzzWthcMapper.getByWthcCount(pageInfo, tblYqnsWgzzWthc));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean wthcDetail(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblYqnsWgzzWthc tblYqnsWgzzWthc = tblYqnsWgzzWthcMapper.wthcDetail(id);
        resultMap.put("tblYqnsWgzzWthc",tblYqnsWgzzWthc);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public JsonBean wthcSave(String token, TblYqnsWgzzWthc tblYqnsWgzzWthc) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<>();

        tblYqnsWgzzWthc.setStatus(0);
        tblYqnsWgzzWthc.setCreator(loginStaff.getStaffid());

        if (tblYqnsWgzzWthc.getId() != null) {
            //修改
            tblYqnsWgzzWthcMapper.updateByPrimaryKeySelective(tblYqnsWgzzWthc);

            tblYqnsWgzzWthcMapper.deletefile(tblYqnsWgzzWthc.getId());
            if (tblYqnsWgzzWthc.getAttIds()!=null && !"".equals(tblYqnsWgzzWthc.getAttIds())) {
                String[] attId = tblYqnsWgzzWthc.getAttIds().split(",");
                for (String aid : attId) {
                    this.tblYqnsWgzzWthcMapper.insertSHBGAttInfoForPlan(tblYqnsWgzzWthc.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        } else {
            //新增
        	tblYqnsWgzzWthc.setIsuse(0);
            tblYqnsWgzzWthcMapper.insertSelective(tblYqnsWgzzWthc);
            if (tblYqnsWgzzWthc.getAttIds() != null && !"".equals(tblYqnsWgzzWthc.getAttIds())) {
                String[] attId = tblYqnsWgzzWthc.getAttIds().split(",");
                for (String aid : attId) {
                    this.tblYqnsWgzzWthcMapper.insertSHBGAttInfoForPlan(tblYqnsWgzzWthc.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        }

        //更新所选的问题线索受理状态
        TblYqnsWgzzWtsl tblYqnsWgzzWtsl = new TblYqnsWgzzWtsl();
        tblYqnsWgzzWtsl.setId(tblYqnsWgzzWthc.getSlid());
        tblYqnsWgzzWtsl.setIsuse(1);
        tblYqnsWgzzWtslMapper.updateByPrimaryKeySelective(tblYqnsWgzzWtsl);

        resultMap.put("tblYqnsWgzzWthc", tblYqnsWgzzWthc);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean wthcDelete(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsWgzzWthc tblYqnsWgzzWthc = tblYqnsWgzzWthcMapper.wthcDetail(id);
        TblYqnsWgzzWtsl tblYqnsWgzzWtsl = new TblYqnsWgzzWtsl();
        tblYqnsWgzzWtsl.setId(tblYqnsWgzzWthc.getSlid());
        tblYqnsWgzzWtsl.setIsuse(0);
        tblYqnsWgzzWtslMapper.updateByPrimaryKeySelective(tblYqnsWgzzWtsl);
        this.tblYqnsWgzzWthcMapper.wthcDelete(id);

        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public R wthcFilesDelete(String token, String attId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return R.fail("用户已失效！");
        }
        TblAttachment att = this.tblYqnsWgzzWthcMapper.selectEntityById(attId);
        if(att!=null){
            this.tblYqnsWgzzWthcMapper.deleteSHBGFileInfoByAttId(att.getAttid());
            this.tblYqnsWgzzWthcMapper.deleteEntity(att.getAttid());
        }
        return R.success();
    }

    @Override
    public JsonBean wthcFileList(String token, BigDecimal id) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            List<TblAttachment> attList = this.tblYqnsWgzzWthcMapper.findAttachmentListByWgbg(id);
            resultMap.put("attList", attList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1,200,resultMap);
    }


    //==问题定责Begin
    @Override
    public JsonBean wtdzList(String token, Integer pageNumber, Integer pageSize,TblYqnsWgzzWtdz tblYqnsWgzzWtdz) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        tblYqnsWgzzWtdz.setCreator(loginStaff.getStaffid());
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblYqnsWgzzWtdz> pageInfo = new PageInfo<TblYqnsWgzzWtdz>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblYqnsWgzzWtdzMapper.getByWtdzList(pageInfo, tblYqnsWgzzWtdz));
        pageInfo.setTotalRecord(this.tblYqnsWgzzWtdzMapper.getByWtdzCount(pageInfo, tblYqnsWgzzWtdz));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean wtdzDetail(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblYqnsWgzzWtdz tblYqnsWgzzWtdz = tblYqnsWgzzWtdzMapper.wtdzDetail(id);
        resultMap.put("tblYqnsWgzzWtdz",tblYqnsWgzzWtdz);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public JsonBean wtdzSave(String token, TblYqnsWgzzWtdz tblYqnsWgzzWtdz) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<>();

        tblYqnsWgzzWtdz.setStatus(0);
        tblYqnsWgzzWtdz.setCreator(loginStaff.getStaffid());

        if (tblYqnsWgzzWtdz.getId() != null) {
            //修改
            tblYqnsWgzzWtdzMapper.updateByPrimaryKeySelective(tblYqnsWgzzWtdz);

            tblYqnsWgzzWtdzMapper.deletefile(tblYqnsWgzzWtdz.getId());
            if (tblYqnsWgzzWtdz.getAttIds()!=null && !"".equals(tblYqnsWgzzWtdz.getAttIds())) {
                String[] attId = tblYqnsWgzzWtdz.getAttIds().split(",");
                for (String aid : attId) {
                    this.tblYqnsWgzzWtdzMapper.insertSHBGAttInfoForPlan(tblYqnsWgzzWtdz.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        } else {
            //新增
        	tblYqnsWgzzWtdz.setIsuse(0);
            tblYqnsWgzzWtdzMapper.insertSelective(tblYqnsWgzzWtdz);
            if (tblYqnsWgzzWtdz.getAttIds() != null && !"".equals(tblYqnsWgzzWtdz.getAttIds())) {
                String[] attId = tblYqnsWgzzWtdz.getAttIds().split(",");
                for (String aid : attId) {
                    this.tblYqnsWgzzWtdzMapper.insertSHBGAttInfoForPlan(tblYqnsWgzzWtdz.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        }

        //更新所选的问题线索核查状态
        TblYqnsWgzzWthc tblYqnsWgzzWthc = new TblYqnsWgzzWthc();
        tblYqnsWgzzWthc.setId(tblYqnsWgzzWtdz.getHcid());
        tblYqnsWgzzWthc.setIsuse(1);
        tblYqnsWgzzWthcMapper.updateByPrimaryKeySelective(tblYqnsWgzzWthc);

        resultMap.put("tblYqnsWgzzWtdz", tblYqnsWgzzWtdz);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean wtdzDelete(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsWgzzWtdz tblYqnsWgzzWtdz = tblYqnsWgzzWtdzMapper.wtdzDetail(id);
        TblYqnsWgzzWthc tblYqnsWgzzWthc = new TblYqnsWgzzWthc();
        tblYqnsWgzzWthc.setId(tblYqnsWgzzWtdz.getHcid());
        tblYqnsWgzzWthc.setIsuse(0);
        tblYqnsWgzzWthcMapper.updateByPrimaryKeySelective(tblYqnsWgzzWthc);
        this.tblYqnsWgzzWtdzMapper.wtdzDelete(id);

        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public R wtdzFilesDelete(String token, String attId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return R.fail("用户已失效！");
        }
        TblAttachment att = this.tblYqnsWgzzWtdzMapper.selectEntityById(attId);
        if(att!=null){
            this.tblYqnsWgzzWtdzMapper.deleteSHBGFileInfoByAttId(att.getAttid());
            this.tblYqnsWgzzWtdzMapper.deleteEntity(att.getAttid());
        }
        return R.success();
    }

    @Override
    public JsonBean wtdzFileList(String token, BigDecimal id) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            List<TblAttachment> attList = this.tblYqnsWgzzWtdzMapper.findAttachmentListByWgbg(id);
            resultMap.put("attList", attList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public JsonBean wtdzIssued(String token, String staffIds,String ids) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        String[] dataIds = ids.split(",");
        String[] roleids = staffIds.split(",");
        try {
        	TblYqnsWgzzWtdz dataPre = null;
            for (String pid : dataIds) {
//                dataPre = this.tblYqnsWgzzWtdzMapper.selectById(Integer.valueOf(pid));
                for (String rid : roleids) {
                    if (tblYqnsWgzzWtdzMapper.checkIssue(Integer.valueOf(pid), Integer.valueOf(rid)) == 0) {
                        if (pid != null && pid.length() > 0) {
//                            dataPre.setFristuserid(rid);
//                            tblYqnsWgzzWtdzMapper.updateEntity(dataPre);
                        }
                        tblYqnsWgzzWtdzMapper.saveIssue(rid, pid);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, null);
    }


    //==移送结果文书Begin
    @Override
    public JsonBean ysjgwsList(String token, Integer pageNumber, Integer pageSize,TblYqnsWgzzYsjgws tblYqnsWgzzYsjgws) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        tblYqnsWgzzYsjgws.setCreator(loginStaff.getStaffid());
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblYqnsWgzzYsjgws> pageInfo = new PageInfo<TblYqnsWgzzYsjgws>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblYqnsWgzzYsjgwsMapper.getByYsjgwsList(pageInfo, tblYqnsWgzzYsjgws));
        pageInfo.setTotalRecord(this.tblYqnsWgzzYsjgwsMapper.getByYsjgwsCount(pageInfo, tblYqnsWgzzYsjgws));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean ysjgwsDetail(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblYqnsWgzzYsjgws tblYqnsWgzzYsjgws = tblYqnsWgzzYsjgwsMapper.ysjgwsDetail(id);
        resultMap.put("tblYqnsWgzzYsjgws",tblYqnsWgzzYsjgws);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public JsonBean ysjgwsSave(String token, TblYqnsWgzzYsjgws tblYqnsWgzzYsjgws) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<>();

        tblYqnsWgzzYsjgws.setStatus(0);
        tblYqnsWgzzYsjgws.setCreator(loginStaff.getStaffid());

        if (tblYqnsWgzzYsjgws.getId() != null) {
            //修改
            tblYqnsWgzzYsjgwsMapper.updateByPrimaryKeySelective(tblYqnsWgzzYsjgws);

            tblYqnsWgzzYsjgwsMapper.deletefile(tblYqnsWgzzYsjgws.getId());
            if (tblYqnsWgzzYsjgws.getAttIds()!=null && !"".equals(tblYqnsWgzzYsjgws.getAttIds())) {
                String[] attId = tblYqnsWgzzYsjgws.getAttIds().split(",");
                for (String aid : attId) {
                    this.tblYqnsWgzzYsjgwsMapper.insertSHBGAttInfoForPlan(tblYqnsWgzzYsjgws.getId(), aid);
                }
            }
        } else {
            //新增
            tblYqnsWgzzYsjgwsMapper.insertSelective(tblYqnsWgzzYsjgws);
            if (tblYqnsWgzzYsjgws.getAttIds() != null && !"".equals(tblYqnsWgzzYsjgws.getAttIds())) {
                String[] attId = tblYqnsWgzzYsjgws.getAttIds().split(",");
                for (String aid : attId) {
                    this.tblYqnsWgzzYsjgwsMapper.insertSHBGAttInfoForPlan(tblYqnsWgzzYsjgws.getId(), aid);
                }
            }
        }

        resultMap.put("tblYqnsWgzzYsjgws", tblYqnsWgzzYsjgws);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean ysjgwsDelete(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.tblYqnsWgzzYsjgwsMapper.ysjgwsDelete(id);

        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public R ysjgwsFilesDelete(String token, String attId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return R.fail("用户已失效！");
        }
        TblAttachment att = this.tblYqnsWgzzYsjgwsMapper.selectEntityById(attId);
        if(att!=null){
            this.tblYqnsWgzzYsjgwsMapper.deleteSHBGFileInfoByAttId(att.getAttid());
            this.tblYqnsWgzzYsjgwsMapper.deleteEntity(att.getAttid());
        }
        return R.success();
    }

    @Override
    public JsonBean ysjgwsFileList(String token, BigDecimal id) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            List<TblAttachment> attList = this.tblYqnsWgzzYsjgwsMapper.findAttachmentListByWgbg(id);
            resultMap.put("attList", attList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1,200,resultMap);
    }



    //==移送台账Begin
    @Override
    public JsonBean ystzList(String token, Integer pageNumber, Integer pageSize,TblYqnsWgzzYstz tblYqnsWgzzYstz) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        tblYqnsWgzzYstz.setCreator(loginStaff.getStaffid());
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblYqnsWgzzYstz> pageInfo = new PageInfo<TblYqnsWgzzYstz>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblYqnsWgzzYstzMapper.getByYstzList(pageInfo, tblYqnsWgzzYstz));
        pageInfo.setTotalRecord(this.tblYqnsWgzzYstzMapper.getByYstzCount(pageInfo, tblYqnsWgzzYstz));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean ystzDetail(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblYqnsWgzzYstz tblYqnsWgzzYstz = tblYqnsWgzzYstzMapper.ystzDetail(id);
        resultMap.put("tblYqnsWgzzYstz",tblYqnsWgzzYstz);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public JsonBean ystzSave(String token, TblYqnsWgzzYstz tblYqnsWgzzYstz) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<>();

        tblYqnsWgzzYstz.setStatus(0);
        tblYqnsWgzzYstz.setCreator(loginStaff.getStaffid());

        if (tblYqnsWgzzYstz.getId() != null) {
            //修改
            tblYqnsWgzzYstzMapper.updateByPrimaryKeySelective(tblYqnsWgzzYstz);

            tblYqnsWgzzYstzMapper.deletefile(tblYqnsWgzzYstz.getId());
            if (tblYqnsWgzzYstz.getAttIds()!=null && !"".equals(tblYqnsWgzzYstz.getAttIds())) {
                String[] attId = tblYqnsWgzzYstz.getAttIds().split(",");
                for (String aid : attId) {
                    this.tblYqnsWgzzYstzMapper.insertSHBGAttInfoForPlan(tblYqnsWgzzYstz.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        } else {
            //新增
            tblYqnsWgzzYstzMapper.insertSelective(tblYqnsWgzzYstz);
            if (tblYqnsWgzzYstz.getAttIds() != null && !"".equals(tblYqnsWgzzYstz.getAttIds())) {
                String[] attId = tblYqnsWgzzYstz.getAttIds().split(",");
                for (String aid : attId) {
                    this.tblYqnsWgzzYstzMapper.insertSHBGAttInfoForPlan(tblYqnsWgzzYstz.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        }

        resultMap.put("tblYqnsWgzzYstz", tblYqnsWgzzYstz);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean ystzDelete(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.tblYqnsWgzzYstzMapper.ystzDelete(id);

        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public R ystzFilesDelete(String token, String attId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return R.fail("用户已失效！");
        }
        TblAttachment att = this.tblYqnsWgzzYstzMapper.selectEntityById(attId);
        if(att!=null){
            this.tblYqnsWgzzYstzMapper.deleteSHBGFileInfoByAttId(att.getAttid());
            this.tblYqnsWgzzYstzMapper.deleteEntity(att.getAttid());
        }
        return R.success();
    }

    @Override
    public JsonBean ystzFileList(String token, BigDecimal id) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            List<TblAttachment> attList = this.tblYqnsWgzzYstzMapper.findAttachmentListByWgbg(id);
            resultMap.put("attList", attList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1,200,resultMap);
    }


    //==问题线索受理Begin
    @Override
    public JsonBean wtslList(String token, Integer pageNumber, Integer pageSize,TblYqnsWgzzWtsl tblYqnsWgzzWtsl) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        tblYqnsWgzzWtsl.setCreator(loginStaff.getStaffid());
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblYqnsWgzzWtsl> pageInfo = new PageInfo<TblYqnsWgzzWtsl>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblYqnsWgzzWtslMapper.getByWtslList(pageInfo, tblYqnsWgzzWtsl));
        pageInfo.setTotalRecord(this.tblYqnsWgzzWtslMapper.getByWtslCount(pageInfo, tblYqnsWgzzWtsl));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean wtslDetail(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblYqnsWgzzWtsl tblYqnsWgzzWtsl = tblYqnsWgzzWtslMapper.wtslDetail(id);
        resultMap.put("tblYqnsWgzzWtsl",tblYqnsWgzzWtsl);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public JsonBean wtslSave(String token, TblYqnsWgzzWtsl tblYqnsWgzzWtsl) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<>();

        tblYqnsWgzzWtsl.setStatus(0);
        tblYqnsWgzzWtsl.setCreator(loginStaff.getStaffid());

        if (tblYqnsWgzzWtsl.getId() != null) {
            //修改
            tblYqnsWgzzWtslMapper.updateByPrimaryKeySelective(tblYqnsWgzzWtsl);

            tblYqnsWgzzWtslMapper.deletefile(tblYqnsWgzzWtsl.getId());
            if (tblYqnsWgzzWtsl.getAttIds()!=null && !"".equals(tblYqnsWgzzWtsl.getAttIds())) {
                String[] attId = tblYqnsWgzzWtsl.getAttIds().split(",");
                for (String aid : attId) {
                    this.tblYqnsWgzzWtslMapper.insertSHBGAttInfoForPlan(tblYqnsWgzzWtsl.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        } else {
            //新增
        	tblYqnsWgzzWtsl.setIsuse(0);
            tblYqnsWgzzWtslMapper.insertSelective(tblYqnsWgzzWtsl);
            if (tblYqnsWgzzWtsl.getAttIds() != null && !"".equals(tblYqnsWgzzWtsl.getAttIds())) {
                String[] attId = tblYqnsWgzzWtsl.getAttIds().split(",");
                for (String aid : attId) {
                    this.tblYqnsWgzzWtslMapper.insertSHBGAttInfoForPlan(tblYqnsWgzzWtsl.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        }

        resultMap.put("tblYqnsWgzzWtsl", tblYqnsWgzzWtsl);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean wtslDelete(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.tblYqnsWgzzWtslMapper.wtslDelete(id);

        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public R wtslFilesDelete(String token, String attId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return R.fail("用户已失效！");
        }
        TblAttachment att = this.tblYqnsWgzzWtslMapper.selectEntityById(attId);
        if(att!=null){
            this.tblYqnsWgzzWtslMapper.deleteSHBGFileInfoByAttId(att.getAttid());
            this.tblYqnsWgzzWtslMapper.deleteEntity(att.getAttid());
        }
        return R.success();
    }

    @Override
    public JsonBean wtslFileList(String token, BigDecimal id) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            List<TblAttachment> attList = this.tblYqnsWgzzWtslMapper.findAttachmentListByWgbg(id);
            resultMap.put("attList", attList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1,200,resultMap);
    }


    //==处理结果Begin
    @Override
    public JsonBean cljgList(String token, Integer pageNumber, Integer pageSize,TblYqnsWgzzCljg tblYqnsWgzzCljg) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        tblYqnsWgzzCljg.setCreator(loginStaff.getStaffid());
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblYqnsWgzzCljg> pageInfo = new PageInfo<TblYqnsWgzzCljg>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblYqnsWgzzCljgMapper.getByCljgList(pageInfo, tblYqnsWgzzCljg));
        pageInfo.setTotalRecord(this.tblYqnsWgzzCljgMapper.getByCljgCount(pageInfo, tblYqnsWgzzCljg));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean cljgDetail(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblYqnsWgzzCljg tblYqnsWgzzCljg = tblYqnsWgzzCljgMapper.cljgDetail(id);
        resultMap.put("tblYqnsWgzzCljg",tblYqnsWgzzCljg);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    public JsonBean cljgSave(String token, TblYqnsWgzzCljg tblYqnsWgzzCljg) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<>();

        tblYqnsWgzzCljg.setStatus(0);
        tblYqnsWgzzCljg.setCreator(loginStaff.getStaffid());

        if (tblYqnsWgzzCljg.getId() != null) {
            //修改
            tblYqnsWgzzCljgMapper.updateByPrimaryKeySelective(tblYqnsWgzzCljg);

            tblYqnsWgzzCljgMapper.deletefile(tblYqnsWgzzCljg.getId());
            if (tblYqnsWgzzCljg.getAttIds()!=null && !"".equals(tblYqnsWgzzCljg.getAttIds())) {
                String[] attId = tblYqnsWgzzCljg.getAttIds().split(",");
                for (String aid : attId) {
                    this.tblYqnsWgzzCljgMapper.insertSHBGAttInfoForPlan(tblYqnsWgzzCljg.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        } else {
            //新增
        	tblYqnsWgzzCljg.setCreatetime(new Date());
            tblYqnsWgzzCljgMapper.insertSelective(tblYqnsWgzzCljg);
            if (tblYqnsWgzzCljg.getAttIds() != null && !"".equals(tblYqnsWgzzCljg.getAttIds())) {
                String[] attId = tblYqnsWgzzCljg.getAttIds().split(",");
                for (String aid : attId) {
                    this.tblYqnsWgzzCljgMapper.insertSHBGAttInfoForPlan(tblYqnsWgzzCljg.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        }

        //更新所选的问题线索定责状态
        TblYqnsWgzzWtdz tblYqnsWgzzWtdz = new TblYqnsWgzzWtdz();
        tblYqnsWgzzWtdz.setId(tblYqnsWgzzCljg.getDzid());
        tblYqnsWgzzWtdz.setIsuse(1);
        tblYqnsWgzzWtdzMapper.updateByPrimaryKeySelective(tblYqnsWgzzWtdz);

        resultMap.put("tblYqnsWgzzCljg", tblYqnsWgzzCljg);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean cljgDelete(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //更新所选的问题线索定责状态
        TblYqnsWgzzCljg tblYqnsWgzzCljg = tblYqnsWgzzCljgMapper.cljgDetail(id);
        TblYqnsWgzzWtdz tblYqnsWgzzWtdz = new TblYqnsWgzzWtdz();
        tblYqnsWgzzWtdz.setId(tblYqnsWgzzCljg.getDzid());
        tblYqnsWgzzWtdz.setIsuse(0);
        tblYqnsWgzzWtdzMapper.updateByPrimaryKeySelective(tblYqnsWgzzWtdz);
        this.tblYqnsWgzzCljgMapper.cljgDelete(id);

        return ResponseFormat.retParam(1, 200, null);
    }

    @Override
    public R cljgFilesDelete(String token, String attId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return R.fail("用户已失效！");
        }
        TblAttachment att = this.tblYqnsWgzzCljgMapper.selectEntityById(attId);
        if(att!=null){
            this.tblYqnsWgzzCljgMapper.deleteSHBGFileInfoByAttId(att.getAttid());
            this.tblYqnsWgzzCljgMapper.deleteEntity(att.getAttid());
        }
        return R.success();
    }

    @Override
    public JsonBean cljgFileList(String token, BigDecimal id) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            List<TblAttachment> attList = this.tblYqnsWgzzCljgMapper.findAttachmentListByWgbg(id);
            resultMap.put("attList", attList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1,200,resultMap);
    }

}
