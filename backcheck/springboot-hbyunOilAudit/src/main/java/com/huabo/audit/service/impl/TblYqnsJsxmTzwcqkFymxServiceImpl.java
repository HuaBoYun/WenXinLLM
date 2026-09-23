package com.huabo.audit.service.impl;

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
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqkFymx;
import com.huabo.audit.oracle.entity.TblYqnsSjzgWtzg;
import com.huabo.audit.oracle.mapper.TblYqnsJsxmTzwcqkFymxMapper;
import com.huabo.audit.service.TblYqnsJsxmTzwcqkFymxService;
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
* @author Administrator
* @description 针对表【TBL_YQNS_JSXM_TZWCQK_FYMX(建设项目投资完成情况费用明细表)】的数据库操作Service实现
* @createDate 2023-09-14 22:49:04
*/
@Service
public class TblYqnsJsxmTzwcqkFymxServiceImpl extends ServiceImpl<TblYqnsJsxmTzwcqkFymxMapper, TblYqnsJsxmTzwcqkFymx>
    implements TblYqnsJsxmTzwcqkFymxService {
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
    public JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsJsxmTzwcqkFymx vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        /*PageInfo<TblYqnsJsxmTzwcqkFymx> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(pageSize);
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setTlist(this.baseMapper.selectListByPageInfo(pageInfo, vo));
        pageInfo.setTotalRecord(this.baseMapper.selectCountByPageInfo(pageInfo, vo));*/

        IPage<TblYqnsJsxmTzwcqkFymx> query = new Page<>(pageNumber, pageSize);
        //查询条件
        QueryWrapper<TblYqnsJsxmTzwcqkFymx> wrapper = new QueryWrapper<>();
        //查询 需要判空 在查询 模糊查询
        if (StringUtils.isNotBlank(vo.getHtbh())) {
            wrapper.lambda().like(TblYqnsJsxmTzwcqkFymx::getHtbh, vo.getHtbh());
        }
        if (StringUtils.isNotBlank(vo.getJhwh())) {
            wrapper.lambda().like(TblYqnsJsxmTzwcqkFymx::getJhwh, vo.getJhwh());
        }
        //查询 需要判空 在查询 某个字段 大于且等于 某个时间
        if (vo.getStartDate()!= null) {
            wrapper.lambda().ge(TblYqnsJsxmTzwcqkFymx::getStartDate, vo.getStartDate());
        }
        //查询 需要判空 在查询 某个字段 小于 某个时间
        if (vo.getEndDate() != null) {
            //addDays 加一天  根据各自系统自行判断是否需要结束时间+1天 有一些项目是前端自行加一天的
            wrapper.lambda().lt(TblYqnsJsxmTzwcqkFymx::getEndDate, vo.getEndDate());
        }
        //倒序
        wrapper.orderByDesc(true, "CJSJ");
        //升序 wrapper.orderByAsc(true,"createTime");
        //获得数据
        com.github.pagehelper.PageInfo<TblYqnsJsxmTzwcqkFymx> pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> baseMapper.selectList(wrapper));
        PageResult<TblYqnsJsxmTzwcqkFymx> page = new PageResult<TblYqnsJsxmTzwcqkFymx>().build(pageInfo);
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
    public JsonBean saveOrUpdate(String token, TblYqnsJsxmTzwcqkFymx vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        vo.setOrgid(loginStaff.getCurrentOrg().getOrgid().longValue());
        vo.setExt1(loginStaff.getStaffid().toString());
        if (vo.getJsxmtzwcqkfymxid() != null) {
            vo.setGxr(loginStaff.getRealname());
            vo.setGxsj(new Date());
        } else {
            vo.setCjr(loginStaff.getRealname());
            vo.setCjsj(new Date());
        }
        if(vo.getJsxmtzwcqkfymxid()==null) {
            vo.setJsxmtzwcqkfymxid(RandomUtil.uuLongId());
          }
        boolean ret = this.saveOrUpdate(vo);
        if (!ret) {
            return ResponseFormat.retParam(0, -1, Boolean.FALSE);
        }
        this.baseMapper.deleteAttByPk(vo.getJsxmtzwcqkfymxid());
        List<String> attIds = vo.getAttIds();
        if (attIds != null) {
            for (String attId : attIds) {
                this.baseMapper.saveAtt(vo.getJsxmtzwcqkfymxid().toString(), attId);
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
    public JsonBean detail(String token, TblYqnsJsxmTzwcqkFymx vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        TblYqnsJsxmTzwcqkFymx bean = this.getById(vo.getJsxmtzwcqkfymxid());
        if(bean == null){
            return ResponseFormat.retParam(0, -1, "记录不存在");
        }
        List<TblAttachment> attachments = this.baseMapper.selectAttachmentListByPk(bean.getJsxmtzwcqkfymxid());
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
    public JsonBean delete(String token, TblYqnsJsxmTzwcqkFymx vo) throws Exception {
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
     * 条件删除
     * @param fymx
     * @return
     */
    @Override
    public int deleteByWrapper(TblYqnsJsxmTzwcqkFymx fymx){
        QueryWrapper wrapper = new QueryWrapper(fymx);
        return this.baseMapper.delete(wrapper);
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
    public JsonBean exportData(HttpServletResponse response, String token, TblYqnsJsxmTzwcqkFymx vo) throws Exception {
        JsonBean retParam = validToken(token);
        if (retParam != null) return retParam;
        String[] titles = {"序号", "合同编号", "工程或费用名称", "实施单位", "批复概算投资", "合同金额", "结算金额", "投资节超"};
        List<TblYqnsJsxmTzwcqkFymx> list;
        PageInfo<TblYqnsJsxmTzwcqkFymx> pageInfo = new PageInfo<>();
        pageInfo.setPageSize(5000);
        pageInfo.setCurrentPage(1);

        if (vo.getIds() != null && vo.getIds().size() > 0) {
            list = this.baseMapper.selectBatchIds(vo.getIds());
        } else {
            list = this.baseMapper.selectListByPageInfo(pageInfo, vo);
        }
        List<Object[]> objs = new ArrayList<>();
        AtomicLong xh = new AtomicLong(1);
        for (TblYqnsJsxmTzwcqkFymx bean : list) {
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
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("建设项目投资完成情况费用明细".getBytes(), "UTF-8") + ".xlsx");
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
            TblYqnsJsxmTzwcqkFymx o = new TblYqnsJsxmTzwcqkFymx();
            o.setHtbh(obj[1] == null ? null : obj[1].toString());
            o.setGchfymc(obj[2] == null ? null : obj[2].toString());
            o.setSsdw(obj[3] == null ? null : obj[3].toString());
            o.setPfgstzje(StringUtils.isNotEmpty(obj[4].toString()) ? new BigDecimal(obj[4].toString()) : null);
            o.setHtje(StringUtils.isNotEmpty(obj[5].toString()) ? new BigDecimal(obj[5].toString()) : null);
            o.setJsje(StringUtils.isNotEmpty(obj[6].toString()) ? new BigDecimal(obj[6].toString()) :  null);
            o.setTzjc(obj[7] == null ? null : obj[7].toString());
            this.baseMapper.insert(o);
        }
        return ResponseFormat.retParam(1, 200, Boolean.TRUE);
    }
}




