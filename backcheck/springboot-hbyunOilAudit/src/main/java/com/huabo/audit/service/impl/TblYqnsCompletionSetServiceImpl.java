package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.*;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.*;
import com.huabo.audit.service.TblYqnsCompletionSetService;
import com.huabo.audit.util.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TblYqnsCompletionSetServiceImpl extends ServiceImpl<TblYqnsCompletionSetMapper, TblYqnsCompletionSet>
        implements TblYqnsCompletionSetService {

    TblStaffUtil loginStaff;
    
    @Resource
    private TblYqnsJsxmTzwcqkMapper tblYqnsJsxmTzwcqkMapper;

    @Resource
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private EnginAuditProjectMapper enginAuditProjectMapper;
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
    @Override
    public JsonBean getCompletionList(String token, Integer pageNumber, Integer pageSize, TblYqnsCompletionSet vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        //查询条件
        QueryWrapper<TblYqnsCompletionSet> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getCompletionreportunitname())) {
            wrapper.lambda().like(TblYqnsCompletionSet::getCompletionreportunitname, vo.getCompletionreportunitname());
        }
        if (StringUtils.isNotBlank(vo.getCompletionname())) {
            wrapper.lambda().like(TblYqnsCompletionSet::getCompletionname, vo.getCompletionname());
        }
        
        if (StringUtils.isNotBlank(vo.getCompletioncode())) {
            wrapper.lambda().like(TblYqnsCompletionSet::getCompletioncode, vo.getCompletioncode());
        }

        //倒序
        wrapper.orderByDesc(true, "COMPLETIONID");

        //获得数据
        PageInfo<TblYqnsCompletionSet> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsCompletionSet> build = new PageResult<TblYqnsCompletionSet>().build(pageInfo);

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(build.getTlist());
        return ResponseFormat.retParam(1, 200, build);
    }

    @Override
    public JsonBean saveOrUpdate(String token, TblYqnsCompletionSet vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        if(vo.getCompletionid()==null) {
        	vo.setCompletionid(RandomUtil.uuBigDecimalId());
        }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, vo);
    }

    @Override
    public JsonBean delete(String token, BigDecimal id) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        LambdaQueryWrapper<TblYqnsCompletionSet> query = new LambdaQueryWrapper<TblYqnsCompletionSet>()
                .eq(TblYqnsCompletionSet::getCompletionid, id);
        int ret = this.baseMapper.delete(query);
        if (ret < 1) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    @Override
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsCompletionSet vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        String[] titles = {"序号","序号", "合同编号","工程或费用名称", "实施单位名称",  "批复概算投资", "合同金额", "结算金额", "投资节超（概算-实际完成）", "投资节超情况说明", "填报单位名称", "金额单位"};
        List<TblYqnsCompletionSet> list;
        com.hbfk.util.PageInfo<TblYqnsCompletionSet> pageInfo = new com.hbfk.util.PageInfo<>();
        pageInfo.setPageSize(5000);
        pageInfo.setCurrentPage(1);
        list = this.baseMapper.selectListByPageInfo(pageInfo, vo);
        List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsCompletionSet bean : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement(); //"序号"
            obj[1] = bean.getSortindex(); //"序号"
            obj[2] = bean.getCompletioncode(); // "合同编号"
            obj[3] = bean.getCompletionname(); //"工程或费用名称"
            obj[4] = bean.getCompletionimplunitname(); //"实施单位名称"
            obj[5] = bean.getCompletionapproval(); //"批复概算投资"
            obj[6] = bean.getContractmoney(); //"合同金额"
            obj[7] = bean.getCompletionsetmoney(); //"结算金额"
            obj[8] = bean.getCompletionjiechao(); //"投资节超（概算-实际完成）"
            obj[9] = bean.getCompletionjiechaodeal(); //"投资节超情况说明"
            obj[10] = bean.getCompletionreportunitname(); //"填报单位名称"
            obj[11] = bean.getCompletionamountunit(); //"金额单位"
            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("竣工结算".getBytes(), "UTF-8") + ".xlsx");
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
            TblYqnsCompletionSet o = new TblYqnsCompletionSet();
            o.setSortindex(new BigDecimal(obj[0].toString())); //"序号"
            o.setCompletioncode(obj[1].toString()); // "合同编号"
            o.setCompletionname(obj[2].toString()); //"工程或费用名称"
            QueryWrapper<TblOrganization> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ORGNAME",obj[3].toString());
            TblOrganization one = tblOrganizationMapper.selectOne(queryWrapper);
            if (!Objects.isNull(one)){
                o.setCompletionimplunitid(one.getOrgid()); //"实施单位id"
            }
            o.setCompletionimplunitname(obj[3].toString()); //"实施单位名称"
            o.setCompletionapproval(obj[4].toString()); //"批复概算投资"
            o.setContractmoney(obj[5].toString()); //"合同金额"
            o.setCompletionsetmoney(obj[6].toString()); //"结算金额"
            o.setCompletionjiechao(obj[7].toString()); //"投资节超（概算-实际完成）"
            o.setCompletionjiechaodeal(obj[8].toString()); //"投资节超情况说明"
            QueryWrapper<TblOrganization> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("ORGNAME",obj[9].toString());
            TblOrganization one1 = tblOrganizationMapper.selectOne(queryWrapper1);
            if (!Objects.isNull(one1)){
                o.setCompletionreportunitid(one1.getOrgid()); //"填报单位id"
            }
            o.setCompletionreportunitname(obj[9].toString()); //"填报单位名称"
            o.setCompletionamountunit(obj[10].toString()); //"金额单位"
            this.baseMapper.insert(o);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }
	@Override
	public JsonBean syncConstructionProject(String token) throws Exception {
		loginStaff = userProvider.get();
	    if (loginStaff == null) {
	    	return ResponseFormat.retParam(0, 20006, null);
	    }
	    
	    List<TblYqnsJsxmTzwcqk> syncList = tblYqnsJsxmTzwcqkMapper.selectSyncListData();
	    TblYqnsCompletionSet com = null;
	    BigDecimal maxNo = this.baseMapper.selectMaxNo();
	    if(maxNo == null) { 
	    	maxNo = BigDecimal.valueOf(0);
	    }
	    for (TblYqnsJsxmTzwcqk sync : syncList) {
	    	com = new TblYqnsCompletionSet();
	    	maxNo = maxNo.add(BigDecimal.valueOf(1));
	    	com.setSortindex(maxNo);
	    	com.setNo(AutoNoUtil.getAutoNo(token, TblYqnsCompletionSet.NOID));
	    	com.setCompletionreportunitid(StringUtils.isNotBlank(sync.getTbdwName())?new BigDecimal(sync.getTbdwId()):null);
	    	com.setCompletionreportunitname(StringUtils.isNotBlank(sync.getTbdwName())?sync.getTbdwName():"");
	    	com.setCompletioncode(StringUtils.isNotBlank(sync.getHtbh())?sync.getHtbh():"");
	    	com.setCompletionname(StringUtils.isNoneBlank(sync.getGchfymc())?sync.getGchfymc():null);
	    	com.setCompletionimplunitname(StringUtils.isNotBlank(sync.getSsdw())?sync.getSsdw():null);
	    	com.setCompletionapproval(sync.getPfgstzje() != null?sync.getPfgstzje().toString():null);
	    	com.setCompletionsetmoney(sync.getJsje()!= null?sync.getJsje().toString():null);
	    	com.setCompletionjiechao(StringUtils.isNotBlank(sync.getTzjc())?sync.getTzjc():null);
	    	com.setCompletionjiechaodeal(StringUtils.isNotBlank(sync.getTzjcqksm())?sync.getTzjcqksm():null);
	    	com.setIsSync(1);
	    	sync.setIsSync(1);
	    	sync.setXmstatus(1);
	    	sync.setXmzttype("竣工决算审计");
	    	baseMapper.insert(com);
	    	tblYqnsJsxmTzwcqkMapper.updateById(sync);
		}
	    if(syncList != null && syncList.size() > 0 ) {
	    	return ResponseFormat.retParam(1, 200, "已同步"+syncList.size()+"条数据");
	    }else {
	    	return ResponseFormat.retParam(1, 200, "未找到需要同步数据");
	    }
	}
}
