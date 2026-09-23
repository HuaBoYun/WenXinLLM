package com.huabo.audit.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
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
import com.huabo.audit.oracle.mapper.TblYqnsJsxmJbqkMapper;
import com.huabo.audit.service.TblYqnsJsxmJbqkService;
import com.huabo.audit.service.TblYqnsJsxmTzwcqkService;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_JSXM_JBQK(建设项目基本情况表)】的数据库操作Service实现
 */
@Service
public class TblYqnsJsxmJbqkServiceImpl extends ServiceImpl<TblYqnsJsxmJbqkMapper, TblYqnsJsxmJbqk>
        implements TblYqnsJsxmJbqkService {
    TblStaffUtil loginStaff;

    /**
     * 投资完成情况逻辑处理类
     */
    @Resource
    TblYqnsJsxmTzwcqkService tzwcqkService;
    
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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsJsxmJbqk vo) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
       /* PageInfo<TblYqnsJsxmJbqk> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));*/

//        IPage<TblYqnsJsxmJbqk> query = new Page<>(pageNumber, pageSize);
        //查询条件
        QueryWrapper<TblYqnsJsxmJbqk> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getHtbh())) {
            wrapper.lambda().like(TblYqnsJsxmJbqk::getHtbh, vo.getHtbh());
        }
        if (StringUtils.isNotBlank(vo.getJhwh())) {
            wrapper.lambda().like(TblYqnsJsxmJbqk::getJhwh, vo.getJhwh());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        //查询 需要判空 在查询 某个字段 小于 某个时间
        
       
        if (vo.getCreateyear()!=null && vo.getCreateyear().length()>0) {
        	LocalDate startlocalDate = LocalDate.parse(vo.getCreateyear()+"-01-01", DateTimeFormatter.ISO_DATE);
            LocalDate endlocalDate = LocalDate.parse(vo.getCreateyear()+"-12-31", DateTimeFormatter.ISO_DATE);
            wrapper.lambda().ge(TblYqnsJsxmJbqk::getCjsj, startlocalDate);
            wrapper.lambda().lt(TblYqnsJsxmJbqk::getCjsj, endlocalDate);
        }
        
        if (vo.getTborgid()!=null) {
            wrapper.lambda().eq(TblYqnsJsxmJbqk::getTborgid, vo.getTborgid());
        }
         
        if (StringUtils.isNotBlank(loginStaff.getDeptIds())) {
        	wrapper.and(q -> q.eq("EXT1", loginStaff.getStaffid()).or().inSql("EXT1", "SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+loginStaff.getDeptIds()+")"));
        }else {
        	wrapper.and(q -> q.eq("EXT1", loginStaff.getStaffid()));
        } 
        
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //时间倒序 
//        wrapper.orderByDesc(true,"createTime");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsJsxmJbqk> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsJsxmJbqk> page = new PageResult<TblYqnsJsxmJbqk>().build(pageInfo);
        return ResponseFormat.retParam(1, 200, page);
    }

    @Override
	public JsonBean getListDraftPlan(String token, Integer pageNumber, Integer pageSize, TblYqnsJsxmJbqk vo) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
       /* PageInfo<TblYqnsJsxmJbqk> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));*/

//        IPage<TblYqnsJsxmJbqk> query = new Page<>(pageNumber, pageSize);
        //查询条件
        QueryWrapper<TblYqnsJsxmJbqk> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getHtbh())) {
            wrapper.lambda().like(TblYqnsJsxmJbqk::getHtbh, vo.getHtbh());
        }
        if (StringUtils.isNotBlank(vo.getJhwh())) {
            wrapper.lambda().like(TblYqnsJsxmJbqk::getJhwh, vo.getJhwh());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        //查询 需要判空 在查询 某个字段 小于 某个时间
       
        if (vo.getCreateyear()!=null && vo.getCreateyear().length()>0) {
        	LocalDate startlocalDate = LocalDate.parse(vo.getCreateyear()+"-01-01", DateTimeFormatter.ISO_DATE);
            LocalDate endlocalDate = LocalDate.parse(vo.getCreateyear()+"-12-31", DateTimeFormatter.ISO_DATE);
            wrapper.lambda().ge(TblYqnsJsxmJbqk::getCjsj, startlocalDate);
            wrapper.lambda().lt(TblYqnsJsxmJbqk::getCjsj, endlocalDate);
        }
        
        if (vo.getTborgid()!=null) {
            wrapper.lambda().eq(TblYqnsJsxmJbqk::getTborgid, vo.getTborgid());
        }
         
        wrapper.notInSql("JSXMJBQKID", "SELECT GLID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '31'");
        
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //时间倒序 
//        wrapper.orderByDesc(true,"createTime");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsJsxmJbqk> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsJsxmJbqk> page = new PageResult<TblYqnsJsxmJbqk>().build(pageInfo);
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
    public JsonBean saveOrUpdate(String token, TblYqnsJsxmJbqk vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        vo.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
        vo.setExt1(loginStaff.getStaffid().toString());
        if (vo.getJsxmjbqkid() != null) {
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
    	if(vo.getJsxmjbqkid()==null) {
            vo.setJsxmjbqkid(RandomUtil.uuLongId());
          }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        List<String> attIds = vo.getAttIds();
        if (attIds != null) {
        	this.baseMapper.deleteAttByPk(vo.getJsxmjbqkid());
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getJsxmjbqkid().toString(), attId);
            }
        } 

        syncTzwcqk(vo);
        return ResponseFormat.retParam(1, 200, vo);
    }

    /**
     * 分类验证
     * @param token  token
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
     * @return 是否在季度最后15天内
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
     * 同步投资完成情况
     *
     * @param vo
     * @throws Exception
     */
    private void syncTzwcqk(TblYqnsJsxmJbqk vo) throws Exception {
        TblYqnsJsxmTzwcqk tzwcqk = new TblYqnsJsxmTzwcqk();
        tzwcqk.setHtbh(vo.getHtbh());
        tzwcqk.setJhwh(vo.getJhwh());
        tzwcqk.setGchfymc(vo.getYsxmmc());
        tzwcqk.setHtje(vo.getGcfyjsje());
        tzwcqk.setJsje(vo.getGcfyjsje());
        tzwcqk.setPfgstzje(vo.getGcfyjsje());
        tzwcqk.setFl(vo.getFl());
        Map<String, Object> params = new HashMap<>();
        params.put("htbh", vo.getHtbh());
        List<TblYqnsJsxmTzwcqk> tzwcqkList = this.tzwcqkService.getBaseMapper().selectByMap(params);
        if (tzwcqkList.size() > 0) {
            for (TblYqnsJsxmTzwcqk o : tzwcqkList) {
                tzwcqk.setGxr(loginStaff.getRealname());
                tzwcqk.setGxsj(new Date());
                tzwcqk.setJsxmtzwcqkid(o.getJsxmtzwcqkid());
                this.tzwcqkService.updateById(tzwcqk);
            }
        } else {
            tzwcqk.setCjr(loginStaff.getRealname());
            tzwcqk.setCjsj(new Date());
            tzwcqk.setExt1(loginStaff.getCurrentOrg().getOrgid().toString());
            this.tzwcqkService.save(tzwcqk);
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
    public JsonBean detail(String token, TblYqnsJsxmJbqk vo) throws Exception {
//        JsonBean retParam = validToken(token);
//        if (retParam != null) return retParam;
        TblYqnsJsxmJbqk bean = this.getById(vo.getJsxmjbqkid());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getJsxmjbqkid());
        bean.setAttachments(attachments);
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
    public JsonBean delete(String token, TblYqnsJsxmJbqk vo) throws Exception {
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
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsJsxmJbqk vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        String[] titles = {"序号", "合同编号", "验收项目名称", "计划文号", "计划投资金额", "承包方式", "项目负责人", "联系电话"};
        List<TblYqnsJsxmJbqk> list;
        PageInfo<TblYqnsJsxmJbqk> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(5000);
        pageInfo.setCurrentPage(1);

        if (vo.getIds() != null && vo.getIds().size() > 0) {
            list = this.baseMapper.selectBatchIds(vo.getIds());
        } else {
            list = this.baseMapper.selectListByPageInfo(pageInfo, vo);
        }
        List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsJsxmJbqk bean : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement();
            obj[1] = bean.getHtbh();
            obj[2] = bean.getYsxmmc();
            obj[3] = bean.getJhwh();
            obj[4] = bean.getJhtzje();
            obj[5] = bean.getCbfs();
            obj[6] = bean.getXmfzr();
            obj[7] = bean.getLxdh();
            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("建设项目基本情况".getBytes(), "UTF-8") + ".xlsx");
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
            TblYqnsJsxmJbqk o = new TblYqnsJsxmJbqk();
            o.setHtbh(obj[1] == null ? null : obj[1].toString());
            o.setYsxmmc(obj[2] == null ? null : obj[2].toString());
            o.setJhwh(obj[3] == null ? null : obj[3].toString());
            o.setJhtzje(obj[4] == null ? null : new BigDecimal(obj[4].toString()));
            o.setCbfs(obj[5] == null ? null : obj[5].toString());
            o.setXmfzr(obj[6] == null ? null : obj[6].toString());
            o.setLxdh(obj[7] == null ? null : obj[7].toString());
            this.baseMapper.insert(o);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }

    @Override
    public List<TblYqnsJsxmJbqk> findByIds( String ids)   {
        // 验证token
        // 将逗号分隔的字符串转换为ID列表
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)    // 可选，移除任何多余的空白
                .map(Long::parseLong) // 将字符串转换为Long
                .collect(Collectors.toList());
        // 查询工程审计类型
        List<TblYqnsJsxmJbqk> beanList = this.listByIds(idList);
        return beanList;
    }

    
    
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
    public JsonBean jswclist(String token, Integer pageNumber, Integer pageSize, TblYqnsJsxmJbqk vo) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
       /* PageInfo<TblYqnsJsxmJbqk> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));*/

//        IPage<TblYqnsJsxmJbqk> query = new Page<>(pageNumber, pageSize);
        //查询条件
        QueryWrapper<TblYqnsJsxmJbqk> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getHtbh())) {
            wrapper.lambda().like(TblYqnsJsxmJbqk::getHtbh, vo.getHtbh());
        }
        if (StringUtils.isNotBlank(vo.getJhwh())) {
            wrapper.lambda().like(TblYqnsJsxmJbqk::getJhwh, vo.getJhwh());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        //查询 需要判空 在查询 某个字段 小于 某个时间
        
       
        if (vo.getCreateyear()!=null && vo.getCreateyear().length()>0) {
        	LocalDate startlocalDate = LocalDate.parse(vo.getCreateyear()+"-01-01", DateTimeFormatter.ISO_DATE);
            LocalDate endlocalDate = LocalDate.parse(vo.getCreateyear()+"-12-31", DateTimeFormatter.ISO_DATE);
            wrapper.lambda().ge(TblYqnsJsxmJbqk::getCjsj, startlocalDate);
            wrapper.lambda().lt(TblYqnsJsxmJbqk::getCjsj, endlocalDate);
        }
        
//        if (vo.getTborgid()!=null) {
//            wrapper.lambda().eq(TblYqnsJsxmJbqk::getTborgid, vo.getTborgid());
//        }
         
        if (StringUtils.isNotBlank(loginStaff.getDeptIds())) {
        	wrapper.and(q -> q.eq("EXT1", loginStaff.getStaffid()).or().inSql("EXT1", "SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+loginStaff.getDeptIds()+")"));
        }else {
        	wrapper.and(q -> q.eq("EXT1", loginStaff.getStaffid()));
        } 
        
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //时间倒序 
//        wrapper.orderByDesc(true,"createTime");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsJsxmJbqk> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsJsxmJbqk> page = new PageResult<TblYqnsJsxmJbqk>().build(pageInfo);
        return ResponseFormat.retParam(1, 200, page);
    }

    
    
}




