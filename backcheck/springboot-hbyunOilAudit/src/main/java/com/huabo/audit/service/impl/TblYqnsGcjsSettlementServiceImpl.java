package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.hbfk.util.*;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.service.TblYqnsGcjsSettlementService;
import com.huabo.audit.util.PageResult;


@Service
public class TblYqnsGcjsSettlementServiceImpl extends ServiceImpl<TblYqnsGcjsSettlementMapper, TblYqnsGcjsSettlement>
        implements TblYqnsGcjsSettlementService {

    @Resource
    private TblYqnsGcjsSettlementMapper tblYqnsGcjsSettlementMapper;
    
    @Resource
    private TblYqnsGcxmzjMapper tblYqnsGcxmzjMapper;

    @Resource
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private EnginAuditProjectMapper enginAuditProjectMapper;

    TblStaffUtil loginStaff;

    @Autowired
    private ReservePropertyService reservePropertyService;

    
    @Resource
    private UserProvider userProvider;


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
     * 列表查询
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     * @throws Exception
     */

    @Override
    public JsonBean getSettlementList(String token, Integer pageNumber, Integer pageSize, TblYqnsGcjsSettlement vo) throws Exception {

        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        //查询条件
        QueryWrapper<TblYqnsGcjsSettlement> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getSettlementcode())) {
            wrapper.lambda().like(TblYqnsGcjsSettlement::getSettlementcode, vo.getSettlementcode());
        }
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getSettlementname())) {
            wrapper.lambda().like(TblYqnsGcjsSettlement::getSettlementname, vo.getSettlementname());
        }
        //倒序
        wrapper.orderByDesc(true, "SETTLEMENTID");

        //获得数据
        PageInfo<TblYqnsGcjsSettlement> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsGcjsSettlement> build = new PageResult<TblYqnsGcjsSettlement>().build(pageInfo);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(build.getTlist());
        return ResponseFormat.retParam(1, 200, build);
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
    public JsonBean saveOrUpdate(String token, TblYqnsGcjsSettlement vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;

        if(vo.getSettlementid()==null) {
        	vo.setSettlementid(RandomUtil.uuBigDecimalId());
        }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        
        
        List<TblYqnsGcxmzj> syncList = tblYqnsGcxmzjMapper.selectByHtbh(vo.getSettlementcode());
        if(syncList!=null && syncList.size()>0) {
        	for (TblYqnsGcxmzj sync : syncList) {
        		sync.setIsSync(1);
    			sync.setXmstatus(1);
    			sync.setXmzttype("工程结算审计");
    			tblYqnsGcxmzjMapper.updateById(sync);
			}
        }
        return ResponseFormat.retParam(1, 200, vo);
    }


    /**
     * 删除
     * @param token
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean delete(String token, BigDecimal id) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        LambdaQueryWrapper<TblYqnsGcjsSettlement> query = new LambdaQueryWrapper<TblYqnsGcjsSettlement>()
                .eq(TblYqnsGcjsSettlement::getSettlementid, id);
        int ret = this.baseMapper.delete(query);
        if (ret < 1) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    @Override
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsGcjsSettlement vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        String[] titles = {"序号","序号", "合同编号", "工程名称", "建设单位名称", "二审审查金额", "施工单位名称", "备用字段"};
        List<TblYqnsGcjsSettlement> list;
        com.hbfk.util.PageInfo<TblYqnsGcjsSettlement> pageInfo = new com.hbfk.util.PageInfo<>();
        pageInfo.setPageSize(5000);
        pageInfo.setCurrentPage(1);
        list = this.baseMapper.selectListByPageInfo(pageInfo, vo);
        List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsGcjsSettlement bean : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement();
            obj[1] = bean.getSortindex();
            obj[2] = bean.getSettlementcode();
            obj[3] = bean.getSettlementname();
            obj[4] = bean.getSettlementunitname();
            obj[5] = bean.getSettlementinstanceamount();
            obj[6] = bean.getSettlementconsttunitname();
            obj[7] = bean.getSettlementconstext();
            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("工程结算".getBytes(), "UTF-8") + ".xlsx");
        ImportOrExportExcelUtil.exportExcel(titles, objs, response.getOutputStream(), null);
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

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
                obj[k] = cell;
            }
            objList.add(obj);
        }
        for (Object[] obj : objList) {
            TblYqnsGcjsSettlement o = new TblYqnsGcjsSettlement();
            o.setSortindex(new BigDecimal(obj[0].toString()));
            o.setSettlementcode(obj[1].toString());
            o.setSettlementname(obj[2].toString());
            QueryWrapper<TblOrganization> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ORGNAME",obj[3].toString());
            TblOrganization one = tblOrganizationMapper.selectOne(queryWrapper);
            if (!Objects.isNull(one)){
                o.setSettlementunitid(one.getOrgid()); //"建设单位名称id"
            }
            o.setSettlementunitname(obj[3].toString());
            o.setSettlementinstanceamount(obj[4].toString());
            QueryWrapper<TblOrganization> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("ORGNAME",obj[5].toString());
            TblOrganization one1 = tblOrganizationMapper.selectOne(queryWrapper1);
            if (!Objects.isNull(one1)){
                o.setSettlementconsttunitid(one1.getOrgid()); //"施工单位名称id"
            }
            o.setSettlementconsttunitname(obj[5].toString());

            this.baseMapper.insert(o);
            List<TblYqnsGcxmzj> syncList = tblYqnsGcxmzjMapper.selectByHtbh(o.getSettlementcode());
            if(syncList!=null && syncList.size()>0) {
            	for (TblYqnsGcxmzj sync : syncList) {
            		sync.setIsSync(1);
        			sync.setXmstatus(1);
        			sync.setXmzttype("工程结算审计");
        			tblYqnsGcxmzjMapper.updateById(sync);
    			}
            }
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


	@Override
	public JsonBean syncConstructionProject(String token) throws Exception {
		loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        String tip = "";
        TblYqnsGcjsSettlement sel = null;
        BigDecimal maxNo = this.baseMapper.selectMaxNo();
        if(maxNo == null) {
        	maxNo = BigDecimal.valueOf(0);
        }
        List<TblYqnsGcxmzj> syncList = tblYqnsGcxmzjMapper.selectSyncConstructionProject();
        for (TblYqnsGcxmzj sync : syncList) {
			sel = new TblYqnsGcjsSettlement();
			maxNo = maxNo.add(BigDecimal.valueOf(1));
			sel.setSortindex(maxNo);
			sel.setNo(AutoNoUtil.getAutoNo(token, TblYqnsGcjsSettlement.NOID));
			sel.setSettlementcode(StringUtils.isNotBlank(sync.getHtbh())?sync.getHtbh():null);
			sel.setSettlementname(StringUtils.isNotBlank(sync.getGcmc())?sync.getGcmc():null);
			sel.setSettlementunitname(StringUtils.isNotBlank(sync.getJsdw())?sync.getJsdw():null);
			sel.setSettlementconsttunitname(StringUtils.isNotBlank(sync.getSgdw())?sync.getSgdw():null);
			sel.setSettlementinstanceamount(sync.getEsscje()!=null?sync.getEsscje().toString():null);
			sel.setIsSync(1);
			sync.setIsSync(1);
			sync.setXmstatus(1);
			sync.setXmzttype("工程结算审计");
			baseMapper.insert(sel);
			tblYqnsGcxmzjMapper.updateById(sync);
		}
        if(syncList != null && syncList.size() > 0) {
        	tip+="已同步"+syncList.size()+"条数据";
        }else {
        	tip += "未找到需要同步数据！";
        }
        return ResponseFormat.retParam(1, 200, tip);
	}
}
