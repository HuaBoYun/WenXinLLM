package com.huabo.audit.service.impl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblWgzzEntity;
import com.huabo.audit.oracle.mapper.TblWgzzMapper;
import com.huabo.audit.oracle.vo.TblWgzzVo;
import com.huabo.audit.service.TblWgzzService;
import com.huabo.audit.util.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.service.impl
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/26
 * @Time:10:47
 */
@Service
public class TblWgzzServiceimpl implements TblWgzzService {

	@Autowired
    TblWgzzMapper tblWgzzMapper;
	
	@Resource
    private UserProvider userProvider;

    //违规追责-列表查询
    @Override
    public JsonBean getwfzzlist(String token,Integer pageNumber, Integer pageSize,TblWgzzEntity param) throws Exception{

        /*TblStaffUtil loginStaff = userProvider.get();
        if(loginStaff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
*/
        if(pageNumber == null) {
            pageNumber = 1;
        }
        if(pageSize==null) {
            pageSize=15;
        }
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        PageInfo<TblWgzzEntity> pageInfo = new PageInfo<TblWgzzEntity>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblWgzzMapper.getwgzzList(pageInfo,param));
        pageInfo.setTotalRecord(this.tblWgzzMapper.getwgzzContList(pageInfo,param.getCluenaber()));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1,200,resultMap);
    }

	@Override
	public PageInfo<TblWgzzEntity> getwfzzextlist(String token, Integer pageNumber, Integer pageSize, TblWgzzEntity param) throws Exception {
		if(pageNumber == null) {
			pageNumber = 1;
		}
		if(pageSize==null) {
			pageSize=15;
		}
		PageInfo<TblWgzzEntity> pageInfo = new PageInfo<TblWgzzEntity>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblWgzzMapper.getwgzzList(pageInfo,param));
		pageInfo.setTotalRecord(this.tblWgzzMapper.getwgzzContList(pageInfo,param.getCluenaber()));
		pageInfo.getTotalPage();
		return pageInfo;
	}

	@Override
    public JsonBean getwgzzXQList(String token, BigDecimal clueid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if(loginStaff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        TblWgzzEntity pan = tblWgzzMapper.getwgzzXQList(clueid);
        resultMap.put("pan",pan);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    //违规追责新增/修改
    @Override
    public JsonBean saveUpdatewgzz(TblWgzzEntity tblWgzzEntity, String token) throws Exception{
        TblStaffUtil loginStaff = userProvider.get();
        if(loginStaff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        tblWgzzEntity.setCreator(loginStaff.getStaffid());
        tblWgzzEntity.setImpcreateusername(new Date());
        tblWgzzEntity.setClueunit(loginStaff.getCurrentOrg().getOrgid());
        tblWgzzEntity.setStatus(0);
        if(tblWgzzEntity.getIsaccepted()==null) {
        	tblWgzzEntity.setIsaccepted(0);
        }
        if(tblWgzzEntity.getClueid() != null){
            //修改
            tblWgzzMapper.updateByPrimaryKeySelective(tblWgzzEntity);
        }else {
            //新增
            tblWgzzMapper.insertSelective(tblWgzzEntity);
        }
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        resultMap.put("tblWgzzEntity",tblWgzzEntity);
        return  ResponseFormat.retParam(1,200,resultMap);
    }

    //违规追责删除
    @Override
    public JsonBean deletewgzz(String token, BigDecimal clueid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if(loginStaff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        this.tblWgzzMapper.deletewgzz(clueid);
        return ResponseFormat.retParam(1,200,null) ;
    }

    //违规经营投资问题线索管理台账-列表查询
    @Override
    public JsonBean getwgzzArreyByList(String token, Integer pageNumber, Integer pageSize,String clueNaber) throws Exception{

        TblStaffUtil loginStaff = userProvider.get();
        if(loginStaff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        if(pageNumber == null) {
            pageNumber = 1;
        }
        if(pageSize==null) {
            pageSize=15;
        }
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        PageInfo<TblWgzzEntity> pageInfo = new PageInfo<TblWgzzEntity>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
		TblWgzzEntity tblWgzzEntity = new TblWgzzEntity();
		tblWgzzEntity.setCluenaber(clueNaber);
		pageInfo.setTlist(this.tblWgzzMapper.getwgzzList(pageInfo,tblWgzzEntity));
        pageInfo.setTotalRecord(this.tblWgzzMapper.getwgzzContList(pageInfo,clueNaber));
        pageInfo.getTotalPage();
        resultMap.put("pageInfo", pageInfo);

        return ResponseFormat.retParam(1,200,resultMap);
    }

    //违规经营投资问题线索管理台账-新增/修改
    @Override
    public JsonBean updateaddWgzz(TblWgzzEntity tblWgzzEntity, String token) throws Exception{
        TblStaffUtil loginStaff = userProvider.get();
        if(loginStaff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        tblWgzzEntity.setImpcreateusername(new Date());
        tblWgzzEntity.setStatus(0);
        if(tblWgzzEntity.getClueid() != null){
            //修改
            tblWgzzMapper.updateByPrimaryKeySelective(tblWgzzEntity);
        }else {
            //新增
            tblWgzzMapper.insertSelective(tblWgzzEntity);
        }
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        resultMap.put("tblWgzzEntity",tblWgzzEntity);
        return  ResponseFormat.retParam(1,200,resultMap);
    }
    //违规经营投资问题线索管理台账删除
    @Override
    public JsonBean removewgzz(String token, BigDecimal clueid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if(loginStaff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        this.tblWgzzMapper.deletewgzz(clueid);
        return ResponseFormat.retParam(1,200,null) ;
    }

    @Override
    public JsonBean wgzzFileList(String token, BigDecimal clueid) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            List<TblAttachment> attList = this.tblWgzzMapper.findAttachmentListByWgzz(clueid);
            resultMap.put("data", attList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1,200,resultMap);
    }
    
    @Override
	public PageInfo<TblWgzzEntity> getwfzzYslist(String token, Integer pageNumber, Integer pageSize, TblWgzzEntity param,Integer type) throws Exception {
		if(pageNumber == null) {
			pageNumber = 1;
		}
		if(pageSize==null) {
			pageSize=15;
		}
		PageInfo<TblWgzzEntity> pageInfo = new PageInfo<TblWgzzEntity>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblWgzzMapper.getwgzzYsList(pageInfo,param,type));
		pageInfo.setTotalRecord(this.tblWgzzMapper.getwgzzContList(pageInfo,param.getCluenaber()));
		pageInfo.getTotalPage();
		return pageInfo;
	}

}
