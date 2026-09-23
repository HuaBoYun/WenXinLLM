package com.huabo.audit.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsJsxmJbqk;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqk;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqkFymx;
import com.huabo.audit.oracle.mapper.TblYqnsJsxmTzwcqkMapper;
import com.huabo.audit.service.TblYqnsJsxmJbqkService;
import com.huabo.audit.service.TblYqnsJsxmTzwcqkFymxService;
import com.huabo.audit.service.TblYqnsJsxmTzwcqkService;
import com.huabo.audit.util.PageResult;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JSXM_TZWCQK(建设项目投资完成情况)】的数据库操作Service实现
 */
@Service
public class TblYqnsJsxmTzwcqkServiceImpl extends ServiceImpl<TblYqnsJsxmTzwcqkMapper, TblYqnsJsxmTzwcqk>
        implements TblYqnsJsxmTzwcqkService {
    TblStaffUtil loginStaff;

    @Resource
    TblYqnsJsxmTzwcqkFymxService fymxService;
    
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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsJsxmTzwcqk vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        /*PageInfo<TblYqnsJsxmTzwcqk> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));*/

        IPage<TblYqnsJsxmTzwcqk> query = new Page<>(pageNumber, pageSize);
        //查询条件
        QueryWrapper<TblYqnsJsxmTzwcqk> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getHtbh())) {
            wrapper.lambda().like(TblYqnsJsxmTzwcqk::getHtbh, vo.getHtbh());
        }
        if (StringUtils.isNotBlank(vo.getJhwh())) {
            wrapper.lambda().like(TblYqnsJsxmTzwcqk::getJhwh, vo.getJhwh());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate()!= null) {
            wrapper.lambda().ge(TblYqnsJsxmTzwcqk::getStartDate, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsJsxmTzwcqk::getEndDate, vo.getEndDate());
        }
        
        if (StringUtils.isNotBlank(vo.getTbdwId())) {
            wrapper.lambda().eq(TblYqnsJsxmTzwcqk::getTbdwId, vo.getTbdwId());
        }
        
        //倒序
        wrapper.orderByDesc(true, "JSXMTZWCQKID");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsJsxmTzwcqk> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsJsxmTzwcqk> page = new PageResult<TblYqnsJsxmTzwcqk>().build(pageInfo);
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
    public JsonBean saveOrUpdate(String token, TblYqnsJsxmTzwcqk vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        vo.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
        vo.setExt1(loginStaff.getStaffid().toString());
        if (vo.getJsxmtzwcqkid() != null) {
            vo.setGxr(loginStaff.getRealname());
            vo.setGxsj(new Date());
        } else {
            vo.setCjr(loginStaff.getRealname());
            vo.setCjsj(new Date());
        }
        Integer htbh = baseMapper.selectbyHtbh(vo.getHtbh());
    	if(htbh>0) {
    		vo.setXmstatus(1);
    		vo.setXmzttype("竣工决算审计");
    	}else {
    		vo.setXmstatus(0);
    	}
    	if(vo.getJsxmtzwcqkid()==null) {
            vo.setJsxmtzwcqkid(RandomUtil.uuLongId());
          }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }

//        TblYqnsJsxmTzwcqkFymx fymx = new TblYqnsJsxmTzwcqkFymx();
//        fymx.setJsxmtzwcqkid(vo.getJsxmtzwcqkid());
//        if (vo.getFymxIds() == null || vo.getFymxIds().size() == 0) {
//            this.fymxService.deleteByWrapper(fymx);
//        } else {
//            for (String fymxId : vo.getFymxIds()) {
//                fymx.setJsxmtzwcqkfymxid(Long.valueOf(fymxId));
//                this.fymxService.updateById(fymx);
//            }
//        }

        this.baseMapper.deleteAttByPk(vo.getJsxmtzwcqkid());
        List<String> attIds = vo.getAttids();
        if (attIds != null) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getJsxmtzwcqkid().toString(), attId);
            }
        }

//        this.syncTzjbqk(vo);
        return ResponseFormat.retParam(1, 200, vo);
    }

    @Resource
    TblYqnsJsxmJbqkService jbqkService;

    /**
     * 同步建设项目基本情况
     *
     * @param vo
     * @throws Exception
     */
    private void syncTzjbqk(TblYqnsJsxmTzwcqk vo) throws Exception {
        TblYqnsJsxmJbqk jbqk = new TblYqnsJsxmJbqk();
        jbqk.setHtbh(vo.getHtbh());
        jbqk.setJhwh(vo.getJhwh());
        jbqk.setYsxmmc(vo.getGchfymc());
        jbqk.setGcfyjsje(vo.getJsje());
        Map<String, Object> params = new HashMap<>();
        params.put("htbh", vo.getHtbh());
        List<TblYqnsJsxmJbqk> jbqkList = this.jbqkService.getBaseMapper().selectByMap(params);
        if (jbqkList.size() > 0) {
            for (TblYqnsJsxmJbqk o : jbqkList) {
                jbqk.setJsxmjbqkid(o.getJsxmjbqkid());
                jbqk.setGxr(loginStaff.getRealname());
                jbqk.setGxsj(new Date());
                this.jbqkService.updateById(jbqk);
            }
        } else {
            jbqk.setCjr(loginStaff.getRealname());
            jbqk.setExt1(loginStaff.getStaffid().toString());
            jbqk.setCjsj(new Date());
            this.jbqkService.save(jbqk);
        }

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
    public JsonBean detail(String token, TblYqnsJsxmTzwcqk vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsJsxmTzwcqk bean = this.getById(vo.getJsxmtzwcqkid());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getJsxmtzwcqkid());
        bean.setAttachments(attachments);

        TblYqnsJsxmTzwcqkFymx fymx = new TblYqnsJsxmTzwcqkFymx();
        fymx.setJsxmtzwcqkid(vo.getJsxmtzwcqkid());
        Wrapper<TblYqnsJsxmTzwcqkFymx> wrapper = new QueryWrapper(fymx);
        List<TblYqnsJsxmTzwcqkFymx> list = this.fymxService.list(wrapper);
        if (list != null && list.size() > 0) {
            List<TblYqnsJsxmTzwcqkFymx> fymxGcfy = list.stream().filter(b -> "工程费用".equals(b.getFylx())).collect(Collectors.toList());
            List<TblYqnsJsxmTzwcqkFymx> fymxQtfy = list.stream().filter(b -> "其他费用".equals(b.getFylx())).collect(Collectors.toList());
            bean.setFymxGcfy(fymxGcfy);
            bean.setFymxQtfy(fymxQtfy);
        }
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
    public JsonBean delete(String token, TblYqnsJsxmTzwcqk vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        boolean ret = this.removeByIds(vo.getIds());
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        for (String id : vo.getIds()) {
            this.baseMapper.deleteAttByPk(Long.valueOf(id));
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
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsJsxmTzwcqk vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        String[] titles = {"序号", "合同编号", "工程或费用名称", "实施单位", "批复概算投资", "合同金额", "结算金额", "投资节超"};
        List<TblYqnsJsxmTzwcqk> list;
        PageInfo<TblYqnsJsxmTzwcqk> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(5000);
        pageInfo.setCurrentPage(1);

        if (vo.getIds() != null && vo.getIds().size() > 0) {
            list = this.baseMapper.selectBatchIds(vo.getIds());
        } else {
            list = this.baseMapper.selectListByPageInfo(pageInfo, vo);
        }
        List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsJsxmTzwcqk bean : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement();
            obj[1] = bean.getHtbh();
            obj[2] = bean.getGchfymc();
            obj[3] = bean.getSsdw();
            obj[4] = bean.getPfgstzje();
            obj[5] = bean.getHtje();
            obj[6] = bean.getJsje();
            obj[7] = bean.getTzjc();
            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("建设项目投资完成情况".getBytes(), "UTF-8") + ".xlsx");
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
            TblYqnsJsxmTzwcqk o = new TblYqnsJsxmTzwcqk();
            o.setHtbh(obj[1] == null ? null : obj[1].toString());
            o.setGchfymc(obj[2] == null ? null : obj[2].toString());
            o.setSsdw(obj[3] == null ? null : obj[3].toString());
            o.setPfgstzje(obj[4] == null ? null : new BigDecimal(obj[4].toString()));
            o.setHtje(obj[5] == null ? null : new BigDecimal(obj[5].toString()));
            o.setJsje(obj[6] == null ? null : new BigDecimal(obj[6].toString()));
            o.setTzjc(obj[7] == null ? null : obj[7].toString());
            this.baseMapper.insert(o);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    /**
     * 分类验证
     * @param token token
     * @param vo TblYqnsJsxmJbqk
     * @return JsonBean
     */
    @Override
    public JsonBean flVerify(String token, TblYqnsJsxmJbqk vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        if(vo.getFl() == null || vo.getFl().isEmpty() || !"三类四类".contains(vo.getFl())){
            return ResponseFormat.retParam(0, "分类参数fl不存在或无值或不正确", Boolean.FALSE);
        }
        if("四类".equals(vo.getFl().trim()) && !this.isInDateRange(new Date())){
            return ResponseFormat.retParam(0, "分类四类不在季度最后15天上报范围内", Boolean.FALSE);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    /**
     *
     * @param date 当前日期
     * @return 四类是否在季度最后15天内
     */
    private boolean isInDateRange(Date date){
        Date e = DateUtil.endOfQuarter(date).toJdkDate();
        DateTime b = DateUtil.offsetDay(DateUtil.endOfQuarter(date).toJdkDate(), -14);
        List<DateTime> list = DateUtil.rangeToList(b.toJdkDate(), e, DateField.DAY_OF_MONTH);
        List<String> listStr = new ArrayList<>();
        for (DateTime dateTime : list) {
            listStr.add(DateUtil.format(dateTime.toJdkDate(), "yyyy-MM-dd"));
        }
        String dateStr = DateUtil.formatDate(date);
        if(listStr.contains(dateStr)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    
    /**
     * 验证金额
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Override
    public JsonBean checkMoney(String token, TblYqnsJsxmTzwcqk vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        
        TblYqnsJsxmTzwcqk bean = this.getById(vo.getJsxmtzwcqkid());
        if (bean == null) {
            return ResponseFormat.retParam(0, "记录不存在", null);
        }

        TblYqnsJsxmTzwcqkFymx fymx = new TblYqnsJsxmTzwcqkFymx();
        fymx.setJsxmtzwcqkid(vo.getJsxmtzwcqkid());
        fymx.setFylx("工程费用");
        Wrapper<TblYqnsJsxmTzwcqkFymx> wrapper = new QueryWrapper(fymx);
        List<TblYqnsJsxmTzwcqkFymx> list = this.fymxService.list(wrapper);
        if (list != null && list.size() > 0) {
        	double allMoney = 0;
        	TblYqnsJsxmTzwcqkFymx tyf = null;
            for (int i = 0; i < list.size(); i++) {
            	tyf = list.get(i);
            	allMoney = allMoney+tyf.getJsje().doubleValue();
			}
            if(bean.getJsje().doubleValue() == allMoney) {
            	return ResponseFormat.retParam(1, 200, null);
            }else {
            	return ResponseFormat.retParam(0, "金额不匹配", null);
            }
        }
        
        return ResponseFormat.retParam(0, "金额不匹配", null);
    }
    
}




