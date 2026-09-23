package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.huabo.audit.oracle.entity.TblYqnsGzfa;
import com.huabo.audit.oracle.entity.TblYqnsSjxmzd;
import com.huabo.audit.oracle.entity.TblYqnsSjzgZgbg;
import com.huabo.audit.oracle.mapper.TblYqnsGzfaMapper;
import com.huabo.audit.oracle.mapper.TblYqnsSjxmzdMapper;
import com.huabo.audit.service.TblYqnsGzfaService;
import com.huabo.audit.service.TblYqnsSjxmzdService;
import com.huabo.audit.util.PageResult;

import cn.hutool.core.util.StrUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_SJXMZD(审计项目制度表)】的数据库操作Service
 * @Entity TblYqnsSjxmzd
 */
@Service
public class TblYqnsSjxmzdServiceImpl extends ServiceImpl<TblYqnsSjxmzdMapper, TblYqnsSjxmzd>
        implements TblYqnsSjxmzdService {
    TblStaffUtil loginStaff;

    @Autowired
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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjxmzd vo) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        /*PageInfo<TblYqnsSjxmzd> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));
*/
        //查询条件
        QueryWrapper<TblYqnsSjxmzd> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getZdmc())) {
            wrapper.lambda().like(TblYqnsSjxmzd::getZdmc, vo.getZdmc());
        }
        if (StringUtils.isNotBlank(vo.getXmmc())) {
            wrapper.lambda().like(TblYqnsSjxmzd::getXmmc, vo.getXmmc());
        }
        if (StringUtils.isNotBlank(vo.getJhmc())) {
            wrapper.lambda().like(TblYqnsSjxmzd::getJhmc, vo.getJhmc());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate()!= null) {
            wrapper.lambda().ge(TblYqnsSjxmzd::getStartDate, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsSjxmzd::getEndDate, vo.getEndDate());
        }
        
        if (StringUtils.isNotBlank(staff.getDeptIds())) {
        	wrapper.and(q -> q.eq("EXT1", staff.getStaffid()).or().like("RYIDS", staff.getStaffid()).or().inSql("EXT1", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
        }else {
        	wrapper.and(q -> q.eq("EXT1", staff.getStaffid()).or().like("RYIDS", staff.getStaffid()));
        }
        
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsSjxmzd> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));

        //构建预留字段返回
        reservePropertyService.buildReserveProperty(pageInfo.getList());
        PageResult<TblYqnsSjxmzd> page = new PageResult<TblYqnsSjxmzd>().build(pageInfo);
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
    public JsonBean saveOrUpdate(String token, TblYqnsSjxmzd vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        vo.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
        vo.setExt1(loginStaff.getStaffid().toString());
        if (vo.getSjxmzdid() != null) {
            vo.setGxr(loginStaff.getRealname());
            vo.setGxsj(new Date());
        } else {
            vo.setCjr(loginStaff.getRealname());
            vo.setCjsj(new Date());
        }
        if(vo.getSjxmzdid()==null) {
        	vo.setSjxmzdid(RandomUtil.uuBigDecimalId());
        }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        this.baseMapper.deleteAttByPk(vo.getSjxmzdid().toString());
        List<String> attIds = vo.getAttIds();
        if (attIds != null && attIds.size() > 0) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getSjxmzdid().toString(), attId);
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
    public JsonBean detail(String token, TblYqnsSjxmzd vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsSjxmzd bean = this.getById(vo.getSjxmzdid());
        if (bean == null) {
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getSjxmzdid().toString());
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
    public JsonBean delete(String token, TblYqnsSjxmzd vo) throws Exception {
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
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsSjxmzd vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        String[] titles = {"序号", "制度名称", "计划名称"};
        List<TblYqnsSjxmzd> list;
        PageInfo<TblYqnsSjxmzd> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(5000);
        pageInfo.setCurrentPage(1);

        if (vo.getIds() != null && vo.getIds().size() > 0) {
            list = this.baseMapper.selectBatchIds(vo.getIds());
        } else {
            list = this.baseMapper.selectListByPageInfo(pageInfo, vo);
        }
        List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsSjxmzd bean : list) {
            Object[] obj = new Object[titles.length];
            obj[0] = xh.getAndIncrement();
            obj[1] = bean.getZdmc();
            obj[2] = bean.getJhmc();
            objs.add(obj);
        }
        response.setContentType("application/binary;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("审计项目制度".getBytes(), "UTF-8") + ".xlsx");
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
            TblYqnsSjxmzd o = new TblYqnsSjxmzd();
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
    public JsonBean xf(String token, TblYqnsSjxmzd vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        if(vo.getIds() == null || vo.getRyIdsList() == null || vo.getIds().size() == 0 || vo.getRyIdsList().size() == 0){
            return ResponseFormat.retParam(0, "列表未选择或人员未选择", Boolean.FALSE);
        }
        for (String id : vo.getIds()) {
        	TblYqnsSjxmzd bean = this.getById(id);
            String join = StrUtil.join(",", vo.getRyIdsList());
            bean.setRyIds(join);
            this.updateById(bean);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }
}




