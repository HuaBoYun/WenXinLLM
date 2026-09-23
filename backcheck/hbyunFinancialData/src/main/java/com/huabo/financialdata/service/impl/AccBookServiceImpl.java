package com.huabo.financialdata.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.hbfk.config.SystemStaticValue;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.BaseDao;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.financialdata.cache.IAccBookCache;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.base.LoginUserInfoDO;
import com.huabo.financialdata.entity.dto.accBook.AccountBookListQuery;
import com.huabo.financialdata.entity.entity.AccBook;
import com.huabo.financialdata.entity.entity.Financedate;
import com.huabo.financialdata.entity.enums.AccBookStatusEnum;
import com.huabo.financialdata.entity.vo.accBook.AccBookVO;
import com.huabo.financialdata.mapper.AccBookMapper;
import com.huabo.financialdata.service.IAccBookService;
import com.huabo.financialdata.service.IManageUserBookService;
import com.huabo.financialdata.util.LoginTokenUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
@Service
public class AccBookServiceImpl extends ServiceImpl<AccBookMapper, AccBook> implements IAccBookService {


    @Resource
    private IAccBookCache accBookCache;
    @Resource
    private AccBookMapper accbookMapper;
    @Resource
    private IManageUserBookService manageUserBookService;
    
    @Resource
    private UserProvider userProvider;


    /**
     * 分页查询
     *
     * @param query 请求参数
     * @return 返回结果
     */
    @Override
    public PageInfo<AccBookVO> getListByPage(AccountBookListQuery query) {
        //1. 查询用户-授权账套
        List<String> bookIdList = manageUserBookService.getBookIdListByStaffId(query.getStaffId());
        if (CollectionUtil.isEmpty(bookIdList)) {
            PageInfo pageInfo = new PageInfo<>(bookIdList);
            return pageInfo;
        }

        //2. 查询具体的账套
        //分页查询
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        QueryWrapper<AccBook> accBookQueryWrapper = new QueryWrapper<>();
        accBookQueryWrapper.select("bookid,\n" +
                "					bookname,\n" +
                "					orgid,\n" +
                "					orgname,\n" +
                "					acctid,\n" +
                "					bookdesc,\n" +
                "					bookyear,\n" +
                "					balancesheeturl,\n" +
                "					incomestatementsurl,\n" +
                "					cashflowstatementsurl");
        accBookQueryWrapper.in("bookid", bookIdList);
        //模糊查询
        if (StringUtils.isNoneBlank(query.getBookName())) {
            accBookQueryWrapper.like("bookname", "%" + query.getBookName() + "%");
        }
        if (StringUtils.isNoneBlank(query.getOrgName())) {
            accBookQueryWrapper.like("orgname", "%" + query.getOrgName() + "%");
        }
        if (StringUtils.isNoneBlank(query.getBookYear())) {
            accBookQueryWrapper.like("bookyear", "%" + query.getBookYear() + "%");
        }
        //排序
        accBookQueryWrapper.orderByDesc("bookyear");
        List<AccBook> accBookList = accbookMapper.selectList(accBookQueryWrapper);
        //数据vo映射处理
        PageInfo pageInfo = new PageInfo<>(accBookList);
        List<AccBookVO> temp = Lists.newArrayList();
        if (temp != null) {
            accBookList.forEach(item -> temp.add(doAccBookVoMapper(item)));
        }
        pageInfo.setList(temp);
        return pageInfo;
    }

    /**
     * 获取该组织下得账套ID集合
     *
     * @param orgId 组织ID
     * @return 返回该组织下得账套ID集合
     */
    @Override
    public List<String> getListByOrgId(BigDecimal orgId) {
        QueryWrapper<AccBook> accBookQueryWrapper = new QueryWrapper<>();
        accBookQueryWrapper.select("bookid,\n" +
                "					bookname,\n" +
                "					orgid,\n" +
                "					orgname,\n" +
                "					acctid,\n" +
                "					bookdesc,\n" +
                "					bookyear,\n" +
                "					balancesheeturl,\n" +
                "					incomestatementsurl,\n" +
                "					cashflowstatementsurl");
        accBookQueryWrapper.eq("orgid", orgId);
        List<AccBook> accBookList = accbookMapper.selectList(accBookQueryWrapper);
        if (CollectionUtil.isEmpty(accBookList)) {
            return null;
        }
        return accBookList.stream().map(AccBook::getBookid).collect(Collectors.toList());
    }

    /**
     * 获取登录账户已选中的账套信息
     *
     * @param staffId      登录用户ID
     * @param currentOrgId 组织ID
     * @return 返回已选择的账套信息
     */
    @Override
    public AccBookVO getSelectedBookByStaffId(BigDecimal staffId, BigDecimal orgId) {
        AccBook accBook = accBookCache.getCacheByStaffId(staffId);
        if (Objects.isNull(accBook)) {
            //防止缓存被击穿，做同步操作
            String synchronizedCode = "synchronized.accbook.user.selected.lock" + staffId;
            synchronized (synchronizedCode) {
                accBook = accBookCache.getCacheByStaffId(staffId);
                if (Objects.isNull(accBook)) {
                    // 获取登录用户选中的账套ID
                    accBook = accbookMapper.getSelectedBookByStaffId(staffId);
                    if (Objects.nonNull(accBook)) {
                        accBookCache.addCacheByStaffId(staffId, accBook);
                    }
                }
            }
        }
        return Objects.isNull(accBook) ? null : doAccBookVoMapper(accBook);
    }

    /**
     * 切换账簿
     *
     * @param staffId      登录用户Id
     * @param currentOrgId 当前用户选择的组织ID
     * @param bookId       选择的账簿ID
     * @return 返回切换账簿
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean chooseBook(BigDecimal staffId, BigDecimal currentOrgId, String bookId) {
        //1. 查询该组织下所有账套
        /*List<String> bookIdList = getListByOrgId(currentOrgId);
        if (CollectionUtil.isEmpty(bookIdList)) {
            return false;
        }*/
    	
        //2.重置该用户下所有账套状态为：未选中
        manageUserBookService.updateStatusByStaffIdAndBookIdList(staffId, AccBookStatusEnum.NULL.getCode());

        //3.设置选中的账套
        manageUserBookService.updateStatusByStaffIdAndBookIdListone(staffId, bookId, AccBookStatusEnum.SELECTED.getCode());
        //4. 处理缓存数据 - 直接更新
        AccBook accBook = accbookMapper.getSelectedId(bookId);
        accBookCache.addCacheByStaffId(staffId, accBook);
        return true;
    }


    /**
     * 私有 - AccBookVO映射
     *
     * @param info 待补映射数据
     * @return 返回结果
     */
    private AccBookVO doAccBookVoMapper(AccBook info) {
        AccBookVO vo = new AccBookVO();
        vo.setBookId(info.getBookid());
        vo.setAcctId(info.getAcctid());
        vo.setBookName(info.getBookname());
        vo.setOrgId(info.getOrgid());
        vo.setOrgName(info.getOrgname());
        vo.setBookYear(info.getBookyear());
        vo.setBookDesc(info.getBookdesc());
        return vo;
    }

	@Override
	public JsonBean generateFinance(String token, String prompt) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		AccBookVO accBookVO = this.getSelectedBookByStaffId(loginStaff.getStaffid(), loginStaff.getCurrentOrg().getOrgid());
        if (Objects.isNull(accBookVO)) {
        	 return ResponseFormat.retParam(0, "当前登录用户未选中财务账套", null);
        }
        
        //根据账簿所属的公司主键获取财务账套库的连接信息
        Financedate finance = this.getFinanceDateInfo(accBookVO.getOrgId());
        
		//准备财务模型调用参数
        String[] dbInfo = this.dealDbUrlInfo(finance.getDestConn(),finance.getDestDbType());
        
        HashMap<String, Object> paramMap = new HashMap<String, Object>(0);
        paramMap.put("prompt", prompt);
        paramMap.put("temperature", 0.1);
        paramMap.put("max_tokens", 2048);
        
        Map<String, Object> timeMap = new HashMap<String, Object>(0);
        timeMap.put("start", 2014);
        timeMap.put("end", 2015);
        paramMap.put("time", timeMap);
        
        Map<String, Object> stopMap = new HashMap<String, Object>(0);
        stopMap.put("Observation", "Observation");
        paramMap.put("stop", stopMap);
        
        Map<String, Object> dbMap = new HashMap<String, Object>(0);
        dbMap.put("host", dbInfo[0]);
        dbMap.put("post", dbInfo[1]);
        dbMap.put("name", dbInfo[2]);
        dbMap.put("user", finance.getDestUserId());
        dbMap.put("password", finance.getDestPassWord());
        paramMap.put("database", dbMap);
        Map<String, String> headerMap = new HashMap<String,String>(0);
        String result = HttpClient.httpPostClient(HttpClient.generateUrl, paramMap,headerMap,HttpClient.PARAMBODY);
        return ResponseFormat.retParam(1, 200, result);
	}
	
	@Override
	public JsonBean getCwztDbInfo(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		AccBookVO accBookVO = this.getSelectedBookByStaffId(loginStaff.getStaffid(), loginStaff.getCurrentOrg().getOrgid());
        if (Objects.isNull(accBookVO)) {
        	 return ResponseFormat.retParam(0, "当前登录用户未选中财务账套", null);
        }
        
        //根据账簿所属的公司主键获取财务账套库的连接信息
        Financedate finance = this.getFinanceDateInfo(accBookVO.getOrgId());
        
		//准备财务模型调用参数
        String[] dbInfo = this.dealDbUrlInfo(finance.getDestConn(),finance.getDestDbType());
        
        Map<String, Object> dbMap = new HashMap<String, Object>(0);
        dbMap.put("host", dbInfo[0]);
        dbMap.put("post", dbInfo[1]);
        dbMap.put("name", dbInfo[2]);
        dbMap.put("user", finance.getDestUserId());
        dbMap.put("password", finance.getDestPassWord());
        dbMap.put("type", SystemStaticValue.DATABASETYPE.equals("Oracle")?"oracle":null);
        return ResponseFormat.retParam(1, 200, dbMap);
	}
	
	/**
	 * 分割数据库url 获取ip port 和 其他信息；
	 * @param destConn		数据库连接字符串
	 * @param destDbType	数据库类型
	 * @return 	[0] = 数据库地址
	 * 			[1] = 数据库端口
	 * 			[2] = 数据库实例名
	 */
	private String[] dealDbUrlInfo(String destConn, String destDbType) {
		String[] res = new String[3];
		if("Oracle".equals(destDbType)){
			String url = destConn.substring(destConn.indexOf("@")+1,destConn.length());
			String[] urls = url.split(":");
			res[0] = urls[0];
			if(urls.length > 2) {
				res[1] = urls[1];
				res[2] = urls[2];
			}else {
				String[] sls = urls[1].split("/");
				res[1] = sls[0];
				res[2] = sls[1];
			}
		}
		
		return res;
	}

	private Financedate getFinanceDateInfo(BigDecimal orgId) throws Exception {
		Financedate fin = new Financedate();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = BaseDao.getInstance().getConnection(HttpClient.cwDbUrl,HttpClient.cwDbUserName,HttpClient.cwDbPassword);
			ps = con.prepareStatement("SELECT DESTDBTYPE,DESTCONN,DESTUSERID,DESTPASSWORD,DESTBOOKID FROM TBL_FINANCEDATA WHERE STATUS = 2 AND COMPANYID = "+orgId);
			rs = ps.executeQuery();
			while (rs.next()) {
				fin.setDestDbType(rs.getString("DESTDBTYPE"));
				fin.setDestConn(rs.getString("DESTCONN"));
				fin.setDestUserId(rs.getString("DESTUSERID"));
				fin.setDestPassWord(rs.getString("DESTPASSWORD"));
				fin.setDestbookid(rs.getString("DESTBOOKID"));
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return fin;
	}

}
