package com.huabo.system.service.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblFinanceData;
import com.huabo.system.mapper.TblFinanceDataMapper;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.service.TblFinanceDataService;

@Service("TblFinanceDataService")
public class TblFinanceDataServiceImpl implements TblFinanceDataService {
	
	@Resource
    private UserProvider userProvider;

    //private static final String jdurl= ResourceBundle.getBundle("setting/jdbc").getString("url").toString();

    @Value("${spring.datasource.url}")
    private String jdurl;

    @Resource
    private TblFinanceDataMapper tblFinanceDataMapper;
    @Resource
    private TblOrganizationMapper tblOrganizationMapper;

    @Override
    public Map<String, Object> del(String orderId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            tblFinanceDataMapper.deleteByorderid(orderId);
        resultMap.put("code", "1");
        resultMap.put("msg", "3");
        return resultMap;
    }

    @Override
    public Map<String, Object> findByCompanyId(Integer pageNumber, String token, String staffId, Integer pageSize) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil staff = userProvider.get();
                PageInfo<TblFinanceData> pageInfo = new PageInfo<TblFinanceData>();
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                
                Page<TblFinanceData> page = new Page<TblFinanceData>(pageNumber,pageSize);
                page.setOptimizeCountSql(false); // 禁用自动优化
                IPage<TblFinanceData> pageList = tblFinanceDataMapper.selectListByPageInfo(page, staff.getCurrentOrg().getOrgid());
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int)pageList.getTotal());
                resultMap.put("data", pageInfo);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public TblFinanceData get(String selectid) {
        return tblFinanceDataMapper.selectByOrderId(selectid);
    }

    @Override
    public String selectDateByCompanyid(HttpServletRequest request, String token, String staffId, TblFinanceData tlf, String parseStart, String pendDate, String fid) throws Exception {
        List<TblFinanceData> financeData = new ArrayList<>();
        //验证年份
        int prseStart = Integer.parseInt(parseStart);
        int parseEnd = Integer.parseInt(pendDate);
        Interval interval = new Interval(prseStart, parseEnd);
        List<Interval> arrayList = new ArrayList<Interval>();
        Interval inter = null;
        TblStaffUtil staff = userProvider.get();
        BigDecimal companyid = staff.getCurrentOrg().getOrgid();


        if (tlf.getOrderId() != null) {
            financeData = tblFinanceDataMapper.selectDateByCompanyidAndOrderId(companyid, tlf.getOrderId());
        } else {
            financeData = tblFinanceDataMapper.selectDateByCompanyid(companyid);
        }
        for (TblFinanceData tblFinanceData : financeData) {
            inter = new Interval(tblFinanceData.getStartdate(), tblFinanceData.getEnddate());
            boolean contains = interval.contains(inter);
            boolean contains1 = inter.contains(interval);
            //包含
            if (prseStart < tblFinanceData.getStartdate() && parseEnd > tblFinanceData.getStartdate()) {
                return JsonBean.error("年份重复");
            }
            if (prseStart < tblFinanceData.getEnddate() && parseEnd > tblFinanceData.getEnddate()) {
                return JsonBean.error("年份重复");
            }
            if (contains == true) {
                return JsonBean.error("年份重复");
            }
            if (contains1 == true) {
                return JsonBean.error("年份重复");
            }
        }
        if (tlf != null && tlf.getOrderId() == null) {
            // 新建1
            tlf.setFid(fid);
            tlf.setDestConn(jdurl);
            tlf.setDestDbType("Oracle");
            tlf.setDestUserId("BATHDATA");
            tlf.setDestPassWord("1");
            tlf.setStartdate(prseStart);
            tlf.setEnddate(parseEnd);
            tlf.setCompanyId(companyid.toString());
            String orgname = this.tblOrganizationMapper.findByorgid(companyid);
            tlf.setCompanyName(orgname);
            tlf.setStatus(1);
            tlf.setOrderId(RandomUtil.uuBigDecimalId());
            Integer saveFirstOne = tblFinanceDataMapper.saveFirst(tlf);
            //新建的账套名
            //该公司下是否有数据
            TblFinanceData selectModuleName = tblFinanceDataMapper.selectModuleName(companyid);
            String newBookName;
            if (selectModuleName != null) {
                newBookName = selectModuleName.getDestUserId();
            } else {
                newBookName = this.unRepeatSixCode() + companyid;
                int checkSchema = tblFinanceDataMapper.checkSchema(newBookName);
                if (checkSchema == 0) {
                    //tblFinanceDataMapper.createSchema(newBookName);
                    TblFinanceData tlf2 = new TblFinanceData(RandomUtil.uuBigDecimalId(), null, "Oracle", jdurl, "BATHDATA", "1", "Oracle", jdurl, newBookName, "1", companyid.toString(), staff.getCurrentOrg().getOrgname(), 2, null, null, null, null, null, null);
                    Integer saveFirstTwo = tblFinanceDataMapper.saveFirst(tlf2);
                }
            }
            //新建2
            return JsonBean.success();
        } else {
            // 修改1
            tlf.setFid(fid);
            tlf.setStartdate(prseStart);
            tlf.setEnddate(parseEnd);
            tblFinanceDataMapper.updateFin(tlf);
            return JsonBean.success();
        }

    }

    @Override
    public String findModelName(Integer type, BigDecimal orgid) {
    	return tblFinanceDataMapper.selectName(type, orgid);
    }

    /**
     * 登录页管理--生成路径
     *
     * @return
     */
    public String unRepeatSixCode() {
        String sixChar = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        String time = sdf.format(date);
        for (int i = 0; i < time.length() / 2; i++) {
            String singleChar;
            String x = time.substring(i * 2, (i + 1) * 2);
            int b = Integer.parseInt(x);
            if (b < 10) {
                singleChar = Integer.toHexString(Integer.parseInt(x));
            } else if (b >= 10 && b < 36) {
                singleChar = String.valueOf((char) (Integer.parseInt(x) + 55));
            } else {
                singleChar = String.valueOf((char) (Integer.parseInt(x) + 61));
            }
            sixChar = sixChar + singleChar;

        }
        System.out.println("生成一个6位不可重复的字符编码是：" + sixChar);
        return sixChar;
    }
}
