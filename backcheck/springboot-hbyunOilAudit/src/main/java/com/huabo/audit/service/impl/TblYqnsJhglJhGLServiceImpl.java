package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.service.TblYqnsJhglJhGLService;
import com.huabo.audit.util.PageInfoUtil;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JHGL_Jh(计划管理计划) 关联 】的数据库操作Service实现
 */
@Service
public class TblYqnsJhglJhGLServiceImpl extends ServiceImpl<TblYqnsJhglJhGLMapper, TblYqnsJhglJhGL>
        implements TblYqnsJhglJhGLService {

	@Resource
	private TblYqnsJhglJhGLMapper tblYqnsJhglJhGLMapper;
	
	@Resource
	private TblYqnsGcxmzjZjbMapper tblYqnsGcxmzjZjbMapper;
	
	@Resource
	private TblYqnsJsxmTzwcqkMapper tblYqnsJsxmTzwcqkMapper;
	
	@Resource
	private LeaveAudit3LMapper leaveAudit3LMapper;

    @Autowired
    private TblStaffMapper tblStaffMapper;
    @Autowired
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private UserProvider userProvider;



    /**
     * 新增修改批量处理
     *
     * @param token
     * @param voList
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdateList(String token, long Jhid, List<TblYqnsJhglJhGL> voList) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (null != voList && voList.size() != 0  ){
            voList.stream().forEach(vo -> {
            	vo.setId(RandomUtil.uuBigDecimalId());
                vo.setJhid(Jhid); 
                vo.setGxsj(new Date()); 
                vo.setGxr(loginStaff.getStaffid().toString());
            });
            boolean ret = this.saveBatch(voList);
            
            for (TblYqnsJhglJhGL gl : voList) {
            	if("23".equals(gl.getGltype())) {
					this.tblYqnsJhglJhGLMapper.insertSjdwlrsjSbGl(gl.getId(),Jhid,gl.getRelaOrgId());
				}
				if("31".equals(gl.getGltype())) {
					this.tblYqnsJhglJhGLMapper.insertGcxmjshzGl(gl.getId(),Jhid,gl.getRelaOrgName());
				}
				if("32".equals(gl.getGltype())) {
					this.tblYqnsJhglJhGLMapper.insertJsxmtzGl(gl.getId(),Jhid,gl.getRelaOrgName());
				}
//				if("23".equals(gl.getGltype()) || "31".equals(gl.getGltype()) || "32".equals(gl.getGltype())) {
//					this.tblYqnsJhglJhGLMapper.insertHzjhSjbGl(gl.getId(),Jhid,gl.getRelaid());
//				}
			}
            if (!ret) {
                return ResponseFormat.retParam(0, -1, Boolean.FALSE);
            }
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }



    /**
     * 通过Jhid查询明细 
     *
     * @param Jhid
     * @return
     */
    @Override
    public List<TblYqnsJhglJhGL> findListByJHID(String token,String Jhid) {
        // 进行数据获取和查询
        LambdaQueryWrapper<TblYqnsJhglJhGL> query = new LambdaQueryWrapper<TblYqnsJhglJhGL>()
                .eq(StringUtil.isNotEmpty(Jhid), TblYqnsJhglJhGL::getJhid, Jhid);
        List<TblYqnsJhglJhGL>  list= this.baseMapper.selectList(query);
        return list;
    }


    /**
     * 修改
     * @param token
     * @param entity
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsJhglJhGL entity) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // check id is null
        if (null != entity.getId()) {
            entity.setGxsj(new Date());
            entity.setGxr(loginStaff.getStaffid().toString());
        }
        boolean ret = this.saveOrUpdate(entity);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 删除
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean deleteGL(String token, String id) throws Exception {
        // 验证token
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // check id is null
        if (id != null) {
            boolean ret = this.removeById(id);
            if (!ret) {
                return ResponseFormat.retParam(1, 200, null);
            }
            tblYqnsJhglJhGLMapper.deleteGlcgRela(id);
            this.tblYqnsJhglJhGLMapper.deleteGlRela(id);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }



	@Override
	public JsonBean deleteGLByIds(String token, BigDecimal jhid, BigDecimal formid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        QueryWrapper<TblYqnsJhglJhGL> wrapper = new QueryWrapper<TblYqnsJhglJhGL>();
        wrapper.eq("JHID", jhid);
        wrapper.eq("GLID", formid);
		this.remove(wrapper);
		return ResponseFormat.retParam(1, 200, null);
	}



	@Override
	public JsonBean getSjdwlrsjSbList(String token, BigDecimal relaId, BigDecimal id, Integer pageNumber,
			Integer pageSize,String type) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Page<LeaveAudit3LEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> this.leaveAudit3LMapper.selectListByjhzgGlId(id,relaId,type));
        PageInfo<LeaveAudit3LEntity> pageInfo = new PageInfoUtil<LeaveAudit3LEntity>().parsePageInfo(page);
        if (CollectionUtils.isNotEmpty(pageInfo.getTlist())) {
            for (LeaveAudit3LEntity leaveAudit3LEntity : pageInfo.getTlist()) {
                buildReserveProperty(leaveAudit3LEntity);
            }
        }
        return ResponseFormat.retParam(1, 200, pageInfo);
	}



	@Override
	public JsonBean getGcxmjsListByhz(String token, BigDecimal relaId, BigDecimal id, Integer pageNumber,
			Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Page<TblYqnsGcxmzjZjb> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> this.tblYqnsGcxmzjZjbMapper.selectListByjhzgGlRela(id,relaId));
        PageInfo<TblYqnsGcxmzjZjb> pageInfo = new PageInfoUtil<TblYqnsGcxmzjZjb>().parsePageInfo(page);
        //构建预留字段返回
        if (CollectionUtils.isNotEmpty(pageInfo.getTlist())) {
            for (TblYqnsGcxmzjZjb tblYqnsGcxmzjZjb : pageInfo.getTlist()) {
                buildReserveProperty(tblYqnsGcxmzjZjb);
            }
        }
        return ResponseFormat.retParam(1, 200, pageInfo);
	}



	@Override
	public JsonBean getJsxmtzListByhz(String token, BigDecimal relaId, BigDecimal id, Integer pageNumber,
			Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Page<TblYqnsJsxmTzwcqk> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> this.tblYqnsJsxmTzwcqkMapper.selectListByjhzgGlId(id,relaId));
        PageInfo<TblYqnsJsxmTzwcqk> pageInfo = new PageInfoUtil<TblYqnsJsxmTzwcqk>().parsePageInfo(page);
        return ResponseFormat.retParam(1, 200, pageInfo);
	}


	
	@Override
	public JsonBean getcwanbList(String token, BigDecimal glid, BigDecimal id, Integer pageNumber,
			Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Page<LeaveAudit3LEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> this.leaveAudit3LMapper.getcwanbList(id,glid));
        PageInfo<LeaveAudit3LEntity> pageInfo = new PageInfoUtil<LeaveAudit3LEntity>().parsePageInfo(page);
        return ResponseFormat.retParam(1, 200, pageInfo);
	}


    /**
     * 构建预留字段返回
     * @param entity
     */
    private void buildReserveProperty(LeaveAudit3LEntity entity) {
        if (Objects.isNull(entity)) {
            return;
        }

        //预留人员单选
        if(Objects.nonNull(entity.getStaffid1())) {
            TblStaff tblStaff = tblStaffMapper.getById(entity.getStaffid1().toString());
            entity.setRealname1(Objects.isNull(tblStaff) ? null : tblStaff.getRealname());
        }
        if(Objects.nonNull(entity.getStaffid2())) {
            TblStaff tblStaff = tblStaffMapper.getById(entity.getStaffid2().toString());
            entity.setRealname2(Objects.isNull(tblStaff) ? null : tblStaff.getRealname());
        }
        if(Objects.nonNull(entity.getStaffid3())) {
            TblStaff tblStaff = tblStaffMapper.getById(entity.getStaffid3().toString());
            entity.setRealname3(Objects.isNull(tblStaff) ? null : tblStaff.getRealname());
        }
        if(Objects.nonNull(entity.getStaffid4())) {
            TblStaff tblStaff = tblStaffMapper.getById(entity.getStaffid4().toString());
            entity.setRealname4(Objects.isNull(tblStaff) ? null : tblStaff.getRealname());
        }
        if(Objects.nonNull(entity.getStaffid5())) {
            TblStaff tblStaff = tblStaffMapper.getById(entity.getStaffid5().toString());
            entity.setRealname5(Objects.isNull(tblStaff) ? null : tblStaff.getRealname());
        }
        //预留人员多选
        if(StringUtil.isNotEmpty(entity.getStaffids1())) {
            List<String> realnames = tblStaffMapper.getByIds(entity.getStaffids1());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setRealnames1(result);
        }
        if(StringUtil.isNotEmpty(entity.getStaffids2())) {
            List<String> realnames = tblStaffMapper.getByIds(entity.getStaffids2());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setRealnames2(result);
        }
        if(StringUtil.isNotEmpty(entity.getStaffids3())) {
            List<String> realnames = tblStaffMapper.getByIds(entity.getStaffids3());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setRealnames3(result);
        }
        if(StringUtil.isNotEmpty(entity.getStaffids4())) {
            List<String> realnames = tblStaffMapper.getByIds(entity.getStaffids4());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setRealnames4(result);
        }
        if(StringUtil.isNotEmpty(entity.getStaffids5())) {
            List<String> realnames = tblStaffMapper.getByIds(entity.getStaffids5());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setRealnames5(result);
        }

        //预留组织单选
        if(Objects.nonNull(entity.getOrgid1())) {
            String realname = tblOrganizationMapper.findNameById(Long.valueOf(entity.getOrgid1().toString()));
            entity.setOrgname1(realname);
        }
        if(Objects.nonNull(entity.getOrgid2())) {
            String realname = tblOrganizationMapper.findNameById(Long.valueOf(entity.getOrgid2().toString()));
            entity.setOrgname2(realname);
        }
        if(Objects.nonNull(entity.getOrgid3())) {
            String realname = tblOrganizationMapper.findNameById(Long.valueOf(entity.getOrgid3().toString()));
            entity.setOrgname3(realname);
        }
        if(Objects.nonNull(entity.getOrgid4())) {
            String realname = tblOrganizationMapper.findNameById(Long.valueOf(entity.getOrgid4().toString()));
            entity.setOrgname4(realname);
        }
        if(Objects.nonNull(entity.getOrgid5())) {
            String realname = tblOrganizationMapper.findNameById(Long.valueOf(entity.getOrgid5().toString()));
            entity.setOrgname5(realname);
        }
        //预留组织多选
        if(StringUtil.isNotEmpty(entity.getOrgids1())) {
            List<String> realnames = tblOrganizationMapper.getByIds(entity.getOrgids1());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setOrgnames1(result);
        }
        if(StringUtil.isNotEmpty(entity.getOrgids2())) {
            List<String> realnames = tblOrganizationMapper.getByIds(entity.getOrgids2());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setOrgnames2(result);
        }
        if(StringUtil.isNotEmpty(entity.getOrgids3())) {
            List<String> realnames = tblOrganizationMapper.getByIds(entity.getOrgids3());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setOrgnames3(result);
        }
        if(StringUtil.isNotEmpty(entity.getOrgids4())) {
            List<String> realnames = tblOrganizationMapper.getByIds(entity.getOrgids4());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setOrgnames4(result);
        }
        if(StringUtil.isNotEmpty(entity.getOrgids5())) {
            List<String> realnames = tblOrganizationMapper.getByIds(entity.getOrgids5());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setOrgnames5(result);
        }
    }
    /**
     * 构建预留字段返回
     * @param entity
     */
    private void buildReserveProperty(TblYqnsGcxmzjZjb entity) {
        if (Objects.isNull(entity)) {
            return;
        }

        //预留人员单选
        if(Objects.nonNull(entity.getStaffid1())) {
            TblStaff tblStaff = tblStaffMapper.getById(entity.getStaffid1().toString());
            entity.setRealname1(Objects.isNull(tblStaff) ? null : tblStaff.getRealname());
        }
        if(Objects.nonNull(entity.getStaffid2())) {
            TblStaff tblStaff = tblStaffMapper.getById(entity.getStaffid2().toString());
            entity.setRealname2(Objects.isNull(tblStaff) ? null : tblStaff.getRealname());
        }
        if(Objects.nonNull(entity.getStaffid3())) {
            TblStaff tblStaff = tblStaffMapper.getById(entity.getStaffid3().toString());
            entity.setRealname3(Objects.isNull(tblStaff) ? null : tblStaff.getRealname());
        }
        if(Objects.nonNull(entity.getStaffid4())) {
            TblStaff tblStaff = tblStaffMapper.getById(entity.getStaffid4().toString());
            entity.setRealname4(Objects.isNull(tblStaff) ? null : tblStaff.getRealname());
        }
        if(Objects.nonNull(entity.getStaffid5())) {
            TblStaff tblStaff = tblStaffMapper.getById(entity.getStaffid5().toString());
            entity.setRealname5(Objects.isNull(tblStaff) ? null : tblStaff.getRealname());
        }
        //预留人员多选
        if(StringUtil.isNotEmpty(entity.getStaffids1())) {
            List<String> realnames = tblStaffMapper.getByIds(entity.getStaffids1());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setRealnames1(result);
        }
        if(StringUtil.isNotEmpty(entity.getStaffids2())) {
            List<String> realnames = tblStaffMapper.getByIds(entity.getStaffids2());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setRealnames2(result);
        }
        if(StringUtil.isNotEmpty(entity.getStaffids3())) {
            List<String> realnames = tblStaffMapper.getByIds(entity.getStaffids3());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setRealnames3(result);
        }
        if(StringUtil.isNotEmpty(entity.getStaffids4())) {
            List<String> realnames = tblStaffMapper.getByIds(entity.getStaffids4());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setRealnames4(result);
        }
        if(StringUtil.isNotEmpty(entity.getStaffids5())) {
            List<String> realnames = tblStaffMapper.getByIds(entity.getStaffids5());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setRealnames5(result);
        }

        //预留组织单选
        if(Objects.nonNull(entity.getOrgid1())) {
            String realname = tblOrganizationMapper.findNameById(Long.valueOf(entity.getOrgid1().toString()));
            entity.setOrgname1(realname);
        }
        if(Objects.nonNull(entity.getOrgid2())) {
            String realname = tblOrganizationMapper.findNameById(Long.valueOf(entity.getOrgid2().toString()));
            entity.setOrgname2(realname);
        }
        if(Objects.nonNull(entity.getOrgid3())) {
            String realname = tblOrganizationMapper.findNameById(Long.valueOf(entity.getOrgid3().toString()));
            entity.setOrgname3(realname);
        }
        if(Objects.nonNull(entity.getOrgid4())) {
            String realname = tblOrganizationMapper.findNameById(Long.valueOf(entity.getOrgid4().toString()));
            entity.setOrgname4(realname);
        }
        if(Objects.nonNull(entity.getOrgid5())) {
            String realname = tblOrganizationMapper.findNameById(Long.valueOf(entity.getOrgid5().toString()));
            entity.setOrgname5(realname);
        }
        //预留组织多选
        if(StringUtil.isNotEmpty(entity.getOrgids1())) {
            List<String> realnames = tblOrganizationMapper.getByIds(entity.getOrgids1());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setOrgnames1(result);
        }
        if(StringUtil.isNotEmpty(entity.getOrgids2())) {
            List<String> realnames = tblOrganizationMapper.getByIds(entity.getOrgids2());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setOrgnames2(result);
        }
        if(StringUtil.isNotEmpty(entity.getOrgids3())) {
            List<String> realnames = tblOrganizationMapper.getByIds(entity.getOrgids3());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setOrgnames3(result);
        }
        if(StringUtil.isNotEmpty(entity.getOrgids4())) {
            List<String> realnames = tblOrganizationMapper.getByIds(entity.getOrgids4());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setOrgnames4(result);
        }
        if(StringUtil.isNotEmpty(entity.getOrgids5())) {
            List<String> realnames = tblOrganizationMapper.getByIds(entity.getOrgids5());
            String result = realnames.stream().collect(Collectors.joining(", "));
            entity.setOrgnames5(result);
        }
    }

}




