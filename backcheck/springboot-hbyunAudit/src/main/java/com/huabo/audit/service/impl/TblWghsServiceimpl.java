package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblWgzzShbg;
import com.huabo.audit.oracle.entity.TblWgzzWghs;
import com.huabo.audit.oracle.mapper.TblWghsMapper;
import com.huabo.audit.oracle.mapper.TblWgzzShbgMapper;
import com.huabo.audit.oracle.service.TblStaffOracleService;
import com.huabo.audit.service.TblWgzzWghsService;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.service.impl
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:10:38
 */
@Service
public class TblWghsServiceimpl implements TblWgzzWghsService {

    @Resource
    TblWghsMapper tblWghsMapper;

    @Resource
    TblWgzzShbgMapper tblWgzzShbgMapper;
    @Resource
	private TblStaffOracleService tblStaffOracleService;
    
    @Resource
    private UserProvider userProvider;

    //违规核实列表查询
    @Override
    public JsonBean getByWghsList(String token, Integer pageNumber, Integer pageSize, String clueNaber,String verifycontent) throws Exception {
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
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblWgzzWghs> pageInfo = new PageInfo<TblWgzzWghs>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
		List<TblWgzzWghs> byWghsList = this.tblWghsMapper.getByWghsList(pageInfo, clueNaber,verifycontent);
		byWghsList.forEach(item -> {
			if (Objects.nonNull(item.getHandlerid())) {
				item.setHandlername(tblStaffOracleService.getCreatorUserInfo(item.getHandlerid()));
			}
		});
		pageInfo.setTlist(byWghsList);
        pageInfo.setTotalRecord(this.tblWghsMapper.getByContWghsList(pageInfo, clueNaber,verifycontent));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

	@Override
	public List<TblWgzzWghs> getByWghsExtList(Integer pageNumber, Integer pageSize) {
		PageInfo<TblWgzzWghs> pageInfo = new PageInfo<TblWgzzWghs>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		return this.tblWghsMapper.getByWghsList(pageInfo, null,null);
	}

	@Override
    public JsonBean getByWghsXQList(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblWgzzWghs pan = tblWghsMapper.getByWghsXQList(id);
        pan.setHandlername(tblStaffOracleService.getCreatorUserInfo(pan.getHandlerid()));
        resultMap.put("pan",pan);

        return ResponseFormat.retParam(1,200,resultMap);
    }
    //违规核实新增/修改
    @Override
    public JsonBean getByaddlist(String token, TblWgzzWghs tblWgzzWghs, String attIds) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        tblWgzzWghs.setImpcreateusername(new Date());
        Map<String, Object> resultMap = new HashMap<>();
        if (tblWgzzWghs.getId() != null) {
            //修改
            tblWghsMapper.updateByPrimaryKeySelective(tblWgzzWghs);
            if(tblWgzzWghs.getCluenaber()!=null && !tblWgzzWghs.getCluenaber().equals("")) {
            	  tblWghsMapper.deletefile(tblWgzzWghs.getId());
            }
          
            if (attIds != null && !"".equals(attIds)) {
                String[] attId = attIds.split(",");
                for (String aid : attId) {
                    this.tblWghsMapper.insertAttInfoForPlan(tblWgzzWghs.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        } else {
              tblWgzzWghs.setStatus(0);
              tblWgzzWghs.setCreator(Integer.parseInt(loginStaff.getStaffid().toString()));
            //新增
            tblWghsMapper.insertSelective(tblWgzzWghs);
            if (attIds != null && !"".equals(attIds)) {
                String[] attId = attIds.split(",");
                for (String aid : attId) {
                    this.tblWghsMapper.insertAttInfoForPlan(tblWgzzWghs.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        }

        resultMap.put("tblWgzzWghs", tblWgzzWghs);


        return ResponseFormat.retParam(1, 200, resultMap);
    }

    //违规核实删除
    @Override
    public JsonBean getByremoveList(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.tblWghsMapper.getByWghsRemove(id);
        return ResponseFormat.retParam(1, 200, null);
    }

    //违规核实附件删除
    @Override
    public R deleteAttInfoByAttId(String token, String attId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return R.fail("用户已失效！");
        }
        return this.deleteRealtionAttInfo(attId);
    }

    private R deleteRealtionAttInfo(String attId) throws Exception {
        boolean b = false;
        TblAttachment att = this.tblWghsMapper.selectEntityById(attId);
        if(att!=null){
            this.tblWghsMapper.deleteFileInfoByAttId(att.getAttid());
            this.tblWghsMapper.deleteEntity(att.getAttid());
        }
        return R.success();
    }

    //核实报告列表查询
    @Override
    public JsonBean getBySHBGList(String token, Integer pageNumber, Integer pageSize, String clueNaber,String verifycontent) throws Exception {
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
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblWgzzShbg> pageInfo = new PageInfo<TblWgzzShbg>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblWgzzShbgMapper.getByWghsSHBGList(pageInfo, clueNaber,verifycontent));
        pageInfo.setTotalRecord(this.tblWgzzShbgMapper.getByContSHBGList(pageInfo, clueNaber,verifycontent));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

	@Override
	public List<TblWgzzShbg> getBySHBGExtList(Integer pageNumber, Integer pageSize){
		PageInfo<TblWgzzShbg> pageInfo = new PageInfo<TblWgzzShbg>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		return this.tblWgzzShbgMapper.getByWghsSHBGList(pageInfo, null,null);
	}

	//核实报告列表查询详情
    @Override
    public JsonBean getBySHBGXQList(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblWgzzShbg pan = tblWgzzShbgMapper.getBySHBGBHList(id);
        resultMap.put("pan",pan);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    //核实报告新增/修改
    @Override
    public JsonBean getBySHBGaddlist(String token, TblWgzzShbg tblWgzzShbg, String attIds) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<>();

        tblWgzzShbg.setImpcreateusername(new Date());
        tblWgzzShbg.setStatus(0);
        tblWgzzShbg.setCreator(loginStaff.getStaffid());

        if (tblWgzzShbg.getId() != null) {
            //修改
            tblWgzzShbgMapper.updateByPrimaryKeySelective(tblWgzzShbg);

            tblWgzzShbgMapper.deletefile(tblWgzzShbg.getId());
            if (attIds != null && !"".equals(attIds)) {
                String[] attId = attIds.split(",");
                for (String aid : attId) {
                    this.tblWgzzShbgMapper.insertSHBGAttInfoForPlan(tblWgzzShbg.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        } else {
            //新增
            tblWgzzShbgMapper.insertSelective(tblWgzzShbg);
            if (attIds != null && !"".equals(attIds)) {
                String[] attId = attIds.split(",");
                for (String aid : attId) {
                    this.tblWgzzShbgMapper.insertSHBGAttInfoForPlan(tblWgzzShbg.getId(), aid);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        }

        resultMap.put("tblWgzzShbg", tblWgzzShbg);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    //核实报告删除
    @Override
    public JsonBean getBySHBGremoveList(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.tblWgzzShbgMapper.getBySHBGRemove(id);
        return ResponseFormat.retParam(1, 200, null);
    }
    
    

    //核实报告上传会议文件列表
    @Override
    public JsonBean getByidyhList(String token, BigDecimal id) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", tblWgzzShbgMapper.selectbyHy(id));
        return ResponseFormat.retParam(1, 200, resultMap);
    }
    
    
    //核实报告上传会议文件
    @Override
    public JsonBean uploadattbyid(BigDecimal id, String attId) throws Exception {
        tblWgzzShbgMapper.insertSHBGAttInfoHy(id, attId);
        TblAttachment att = this.tblWgzzShbgMapper.selectEntityById(attId);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", att);
        return ResponseFormat.retParam(1, 200, resultMap);
    }
    //核实报告删除会议文件
    @Override
    public JsonBean delattbyid(BigDecimal attid, String token) throws Exception {
    	 TblStaffUtil loginStaff = userProvider.get();
    	 if (loginStaff == null) {
             return ResponseFormat.retParam(0, 20006, null);
         }
    	
        tblWgzzShbgMapper.deleteSHBGFileInfoByAttIdhy(attid);
        this.tblWgzzShbgMapper.deleteEntity(attid);
        return ResponseFormat.retParam(1, 200, null);
    }

    //核实报告附件删除
    @Override
    public R removeAttInfoByAttId(String token, String attId) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return R.fail("用户已失效！");
        }
        return this.removeRealtionAttInfo(attId);
    }

    private R removeRealtionAttInfo(String attId) throws Exception {
        boolean b = false;
        TblAttachment att = this.tblWgzzShbgMapper.selectEntityById(attId);
        if(att!=null){
            this.tblWgzzShbgMapper.deleteSHBGFileInfoByAttId(att.getAttid());
            this.tblWgzzShbgMapper.deleteEntity(att.getAttid());
        }
        return R.success();
    }

    @Override
    public JsonBean getByWghsBHlist(String token, Integer pageNumber, Integer pageSize, String clueNaber) throws Exception {
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
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblWgzzWghs> pageInfo = new PageInfo<TblWgzzWghs>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.tblWghsMapper.getByWghsBHList(pageInfo, clueNaber));
        pageInfo.setTotalRecord(this.tblWghsMapper.getByContWghsBHList(pageInfo, clueNaber));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean wgbgFileList(String token, BigDecimal id) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            List<TblAttachment> attList = this.tblWgzzShbgMapper.findAttachmentListByWgbg(id);
            resultMap.put("data", attList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1,200,resultMap);
    }
    
    
    
    @Override
    public JsonBean wghsFileList(String token, BigDecimal id) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            List<TblAttachment> attList = this.tblWghsMapper.findAttachmentListByWghs(id);
            resultMap.put("data", attList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1,200,resultMap);
    }
}
