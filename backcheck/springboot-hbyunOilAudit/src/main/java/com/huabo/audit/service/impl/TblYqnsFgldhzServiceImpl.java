package com.huabo.audit.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.*;
import com.huabo.audit.oracle.mapper.TblYqnsFgldhzMapper;
import com.huabo.audit.oracle.mapper.TblYqnsGzfaMapper;
import com.huabo.audit.service.TblYqnsFgldhzService;
import com.huabo.audit.service.TblYqnsGzfaService;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wystan
 * @description 针对表【TBL_YQNS_FGLDHZ(分管领导汇总表)】的数据库操作service
 * @Entity TblYqnsFgldhz
 */
@Service
public class TblYqnsFgldhzServiceImpl extends ServiceImpl<TblYqnsFgldhzMapper, TblYqnsFgldhz>
        implements TblYqnsFgldhzService {
    TblStaffUtil loginStaff;
    
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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsFgldhz vo) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        PageInfo<TblYqnsFgldhz> pageInfo = new PageInfo<>();
        /*pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));*/

        //查询条件
        QueryWrapper<TblYqnsFgldhz> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getGsldxm())) {
            wrapper.lambda().like(TblYqnsFgldhz::getGsldxm, vo.getGsldxm());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate()!= null) {
            wrapper.lambda().ge(TblYqnsFgldhz::getStartDate, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsFgldhz::getCjsj, vo.getEndDate());
        }
        if (vo.getIds() != null) {
            wrapper.lambda().in(TblYqnsFgldhz::getFgldhzid, vo.getIds());
        }
        
        if(StringUtils.isNotEmpty(vo.getCreateYear())) {
        	LocalDate startlocalDate = LocalDate.parse(vo.getCreateYear()+"-01-01", DateTimeFormatter.ISO_DATE);
            LocalDate endlocalDate = LocalDate.parse(vo.getCreateYear()+"-12-31", DateTimeFormatter.ISO_DATE);
            if (StringUtils.isNotBlank(vo.getCreateYear())) {
            	wrapper.lambda().ge(TblYqnsFgldhz::getCjsj, startlocalDate);
            	wrapper.lambda().lt(TblYqnsFgldhz::getCjsj, endlocalDate);
            }
        }
        
        if (StringUtils.isNotBlank(loginStaff.getDeptIds())) {
        	wrapper.and(q -> q.eq("CJR", loginStaff.getStaffid()).or().like("PERSON_IDS", loginStaff.getStaffid()).or().inSql("CJR", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+loginStaff.getDeptIds()+") AND ORGID = "+loginStaff.getCurrentOrg().getOrgid()));
        }else {
        	wrapper.and(q -> q.eq("CJR", loginStaff.getStaffid()).or().like("PERSON_IDS", loginStaff.getStaffid()));
        }
        
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        Page<TblYqnsFgldhz> pageResult = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> baseMapper.selectList(wrapper));
        pageInfo .setTotalRecord((int)pageResult.getTotal());
        pageInfo.setTlist(pageResult.getResult());
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);

        return ResponseFormat.retParam(1, 200, pageInfo);
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
    public JsonBean saveOrUpdate(String token, TblYqnsFgldhz vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) {
            return retParam;
        }
        vo.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
        vo.setExt1(loginStaff.getStaffid().toString());
        if (vo.getFgldhzid() != null) {
            vo.setGxr(loginStaff.getRealname());
            vo.setGxsj(new Date());
        } else {
            vo.setCjr(loginStaff.getStaffid().toString());
            vo.setCjrxm(loginStaff.getRealname());
            vo.setCjsj(new Date());
        }
        if(vo.getFgldhzid()==null) {
        	vo.setFgldhzid(RandomUtil.uuBigDecimalId());
        }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        this.baseMapper.deleteAttByPk(vo.getFgldhzid().toString());
        List<String> attIds = vo.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getFgldhzid().toString(), attId);
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
    public JsonBean detail(String token, TblYqnsFgldhz vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) {
            return retParam;
        }
        TblYqnsFgldhz bean = this.getById(vo.getFgldhzid());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getFgldhzid().toString());
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
    public JsonBean delete(String token, TblYqnsFgldhz vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) {
            return retParam;
        }
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
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsFgldhz vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) {
            return retParam;
        }
        String[] titles = {"序号", "公司领导", "立项要求","落实建议"};

//        List<TblYqnsFgldhz> list = this.baseMapper.selectList(null);
        
        JsonBean jsonBean = this.list(token, 1, 10000, vo);
        List<TblYqnsFgldhz> list = ((PageInfo) jsonBean.getData()).getTlist();
        
        List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsFgldhz bean : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement();
            obj[1] = bean.getGsldxm();
            obj[2] = bean.getLxyq();
            obj[3] = bean.getLsjy();
            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String("分管领导汇总表".getBytes("UTF-8"), "ISO-8859-1") + ".xlsx");
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
        if (retParam != null) {
            return retParam;
        }
        XSSFWorkbook workBook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workBook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        List<Object[]> objList = new ArrayList<>();
        // 导入excel 从第二行为数据行 i = 1
        for (int i = 1; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            int lastCellNum = row.getLastCellNum();
            Object[] obj = new Object[lastCellNum];
            // 导入excel 从第1列为数据行 i = 1
            for (int k = 1; k < lastCellNum; k++) {
                XSSFCell cell = row.getCell(k);
                obj[k] = cell.getStringCellValue();
            }
            objList.add(obj);
        }
        for (Object[] obj : objList) {
            TblYqnsFgldhz o = new TblYqnsFgldhz();
            o.setCjr(loginStaff.getStaffid().toString());
            o.setCjrxm(loginStaff.getRealname());
            o.setCjsj(new Date());
            o.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
            o.setExt1(loginStaff.getStaffid().toString());
            o.setGsldxm(String.valueOf(obj[1]));
            o.setLxyq(String.valueOf(obj[2]));
            o.setLsjy(String.valueOf(obj[3]));
            //todo
            this.baseMapper.insert(o);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }


    @Override
    public void distribute(String ids, String personIds) throws Exception {
        String[] idArr = ids.split(",");

        for (String id : idArr){
            TblYqnsFgldhz entity = this.baseMapper.selectById(id);
            if(entity != null){
                entity.setPersonIds(personIds);
                this.baseMapper.updateById(entity);
            }
        }

    }


}




