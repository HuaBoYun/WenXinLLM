package com.huabo.audit.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsEnginAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsFundAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsGzfa;
import com.huabo.audit.oracle.entity.TblYqnsXmdq;
import com.huabo.audit.oracle.mapper.EnginAuditProjectMapper;
import com.huabo.audit.oracle.mapper.FundAuditProjectMapper;
import com.huabo.audit.oracle.mapper.TblYqnsGzfaMapper;
import com.huabo.audit.oracle.mapper.TblYqnsXmdqMapper;
import com.huabo.audit.service.TblYqnsGzfaService;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_GZFA(工作方案表)】的数据库操作Service实现
 */
@Service
public class TblYqnsGzfaServiceImpl extends ServiceImpl<TblYqnsGzfaMapper, TblYqnsGzfa>
        implements TblYqnsGzfaService {
    TblStaffUtil loginStaff;
    
    @Resource
    private TblYqnsXmdqMapper tblYqnsXmdqMapper;
    
    
    @Resource
    private FundAuditProjectMapper fundAuditProjectMapper;
    
    
    @Resource
    private EnginAuditProjectMapper enginAuditProjectMapper;

    @Resource
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;
    

    /**
     * 查询
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsGzfa vo, Integer xmnd, BigDecimal staffId) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        /*vo.setStaffLoginId(loginStaff.getStaffid().toString());
        PageInfo<TblYqnsGzfa> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));*/

        //查询条件
        QueryWrapper<TblYqnsGzfa> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getJhmc())) {
            wrapper.lambda().like(TblYqnsGzfa::getJhmc, vo.getJhmc());
        }
        if (StringUtils.isNotBlank(vo.getXmmc())) {
            wrapper.lambda().like(TblYqnsGzfa::getXmmc, vo.getXmmc());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate()!= null) {
            wrapper.lambda().ge(TblYqnsGzfa::getStartDate, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsGzfa::getEndDate, vo.getEndDate());
        } 
        if( xmnd == null && staffId == null) {
        	 if (StringUtils.isNotBlank(staff.getDeptIds())) {
             	wrapper.and(q -> q.eq("EXT1", staff.getStaffid()).or().like("RYIDS", staff.getStaffid()).or().inSql("EXT1", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
             }else {
             	wrapper.and(q -> q.eq("EXT1", staff.getStaffid()).or().like("RYIDS", staff.getStaffid()));
             }
        }else { 
        	wrapper.lambda().eq(TblYqnsGzfa::getSpzt, 6);
        }
        if(xmnd != null) {
        	wrapper.lambda().ge(TblYqnsGzfa::getCjsj, DateUtil.getYearStartDate(xmnd)).le(TblYqnsGzfa::getCjsj, DateUtil.getYearEndDate(xmnd));
        }
        if(staffId != null) {
        	wrapper.lambda().eq(TblYqnsGzfa::getExt1,staffId);
        }
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsGzfa> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        
        PageResult<TblYqnsGzfa> page = new PageResult<TblYqnsGzfa>().build(pageInfo);
        if(page.getTlist()!=null && page.getTlist().size()>0) {
			for (TblYqnsGzfa fa : page.getTlist()) {
				if(fa!=null && fa.getFalx()!=null && fa.getXmdqid()!=null && !fa.getFalx().equals("基建")) {
		        	TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
		                    .eq("DELETED", 0)
		                    .eq("ID", fa.getXmdqid()));
		        	if(fund!=null) {
			        	TblYqnsXmdq xmqd=new TblYqnsXmdq();
			        	xmqd.setGljhxmid(fund.getGljhxmid());
			        	xmqd.setPlanid(fund.getPlanid());
			        	xmqd.setPlanname(fund.getPlanname());
			        	xmqd.setGljhxmlx(fund.getGljhxmlx());
			        	xmqd.setZsstaffid(fund.getApproverId());
			        	xmqd.setZsname(fund.getApprover());
			        	xmqd.setSiteEndTime(fund.getXcendtime());
			        	xmqd.setXmname(fund.getName());
			        	xmqd.setAssistApprover(fund.getAssistApprover());
			         	xmqd.setAssistApproverId(fund.getAssistApproverId());
			         	xmqd.setFzzStafffId(fund.getFzzStafffId());
			         	xmqd.setFzzName(fund.getFzzName());
			         	xmqd.setSsorgname(fund.getExePhraseUnit());
			         	xmqd.setSsorgid(fund.getExePhraseUnitId());
			        	fa.setXmqd(xmqd);
		        	}
		        }
		        if(fa!=null && fa.getFalx()!=null && fa.getXmdqid()!=null && fa.getFalx().equals("基建")) {
		        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
		                     .eq("DELETED", 0)
		                     .eq("ID", fa.getXmdqid()));
		        	 if(endin!=null) {
		        		 TblYqnsXmdq xmqd=new TblYqnsXmdq();
				         	xmqd.setGljhxmid(endin.getGljhxmid());
				         	xmqd.setPlanid(endin.getPlanid()); 
				         	xmqd.setPlanname(endin.getPlanname());
				         	xmqd.setGljhxmlx(endin.getGljhxmlx());
				         	xmqd.setZsstaffid(endin.getApproverId());
				         	xmqd.setZsname(endin.getApprover());
				         	xmqd.setSiteEndTime(endin.getXcendtime());
				         	xmqd.setXmname(endin.getName());
				         	xmqd.setAssistApprover(endin.getAssistApprover());
				         	xmqd.setAssistApproverId(endin.getAssistApproverId());
				         	xmqd.setFzzStafffId(endin.getFzzStafffId());
				         	xmqd.setFzzName(endin.getFzzName());
				         	xmqd.setSsorgname(endin.getExePhraseUnit());
				         	xmqd.setSsorgid(endin.getExePhraseUnitId());
				         	fa.setXmqd(xmqd);
		        	 }
		        	 
		        }
			}
			
		}
//        if(page.getTlist()!=null && page.getTlist().size()>0) {
//        	 for (TblYqnsGzfa gzfa : page.getTlist()) {
//        		 if(gzfa.getXmdqid()!=null) {
//        			 TblYqnsXmdq xmdq = tblYqnsXmdqMapper.selectById(gzfa.getXmdqid());
//            		 gzfa.setXmqd(xmdq);
//        		 }else {
//        			 TblYqnsXmdq xmdq = new TblYqnsXmdq();
//            		 gzfa.setXmqd(xmdq);
//        		 }
//        		 
//			}
//        }

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(page.getTlist());
        return ResponseFormat.retParam(1, 200, page);
    }


    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean saveOrUpdate(String token, TblYqnsGzfa vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        vo.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
        vo.setExt1(loginStaff.getStaffid().toString());
        if (vo.getGzfaid() != null) {
            vo.setGxr(loginStaff.getRealname());
            vo.setGxsj(new Date());
        } else {
            vo.setCjr(loginStaff.getRealname());
            vo.setCjsj(new Date());
        }
        if(vo.getGzfaid()==null) {
        	vo.setGzfaid(RandomUtil.uuBigDecimalId());
        }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        this.baseMapper.deleteAttByPk(vo.getGzfaid().toString());
        List<String> attIds = vo.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getGzfaid().toString(), attId);
            }
        }

        return ResponseFormat.retParam(1, 200, vo);
    }

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean detail(String token, TblYqnsGzfa vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsGzfa bean = this.getById(vo.getGzfaid()); 
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getGzfaid().toString());
        bean.setAttachments(attachments);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(bean);
        return ResponseFormat.retParam(1, 200, bean);
    }

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo    ids[]
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JsonBean delete(String token, TblYqnsGzfa vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        boolean ret = this.removeByIds(vo.getIds());
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        for (String id : vo.getIds()) {
            this.baseMapper.deleteAttByPk(id);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 校验登录token有效性
     *
     * @param token
     * @return null or not null
     * @throws Exception
     */
    private JsonBean validToken(String token) throws Exception {
        loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        return null;
    }

    /**
     * 导出
     *
     * @param response
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsGzfa vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        String[] titles = {"序号", "项目名称", "计划名称"};
        List<TblYqnsGzfa> list;
        PageInfo<TblYqnsGzfa> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(5000);
        pageInfo.setCurrentPage(1);

        if (vo.getIds() != null && vo.getIds().size() > 0) {
            list = this.baseMapper.selectBatchIds(vo.getIds());
        } else {
            list = this.baseMapper.selectListByPageInfo(pageInfo, vo);
        }
        List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsGzfa bean : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement();
            obj[1] = bean.getXmmc();
            obj[2] = bean.getJhmc();
            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("工作方案".getBytes(), "UTF-8") + ".xlsx");
        ImportOrExportExcelUtil.exportExcel(titles, objs, response.getOutputStream(), null);
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    /**
     * 导入
     *
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public JsonBean importData(MultipartFile file, String token) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        XSSFWorkbook workBook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workBook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        List<Object[]> objList = new ArrayList<>();
        for (int i = 1; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            int lastCellNum = row.getLastCellNum();
            Object[] obj = new Object[lastCellNum];
            for (int k = 0; k < lastCellNum; k++) {
                XSSFCell cell = row.getCell(k);
                obj[k] = cell.getStringCellValue();
            }
            objList.add(obj);
        }
        for (Object[] obj : objList) {
            TblYqnsGzfa o = new TblYqnsGzfa();
            o.setCjr(loginStaff.getRealname());
            o.setCjsj(new Date());
            o.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
            o.setExt1(loginStaff.getStaffid().toString());
            o.setXmmc(obj[1].toString());
            o.setJhmc(obj[2].toString());
            this.baseMapper.insert(o);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    /**
     * 下发
     *
     * @param token
     * @param vo
     * @return
     */
    @Override
    public JsonBean xf(String token, TblYqnsGzfa vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        if(vo.getIds() == null || vo.getRyIdsList() == null || vo.getIds().size() == 0 || vo.getRyIdsList().size() == 0){
            return ResponseFormat.retParam(0, "列表未选择或人员未选择", Boolean.FALSE);
        }
        for (String id : vo.getIds()) {
            TblYqnsGzfa bean = this.getById(id);
            String join = StrUtil.join(",", vo.getRyIdsList());
            bean.setRyIds(join);
            this.updateById(bean);
        }
        return ResponseFormat.retParam(1, "操作成功", Boolean.TRUE);
    }
}




