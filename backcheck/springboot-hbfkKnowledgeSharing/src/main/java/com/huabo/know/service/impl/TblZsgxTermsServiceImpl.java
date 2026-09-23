package com.huabo.know.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.know.entity.TblZsgxTerms;
import com.huabo.know.mapper.TblZsgxTermsMapper;
import com.huabo.know.service.TblZsgxTermsService;
import com.huabo.know.utils.HtmlDownloadUtil;
import com.huabo.know.vo.param.CreateTermsParam;
import com.huabo.know.vo.param.TermsListParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2024-04-03
 */
@Slf4j
@Service
public class TblZsgxTermsServiceImpl extends ServiceImpl<TblZsgxTermsMapper, TblZsgxTerms> implements TblZsgxTermsService {


    @Autowired
    private TblZsgxTermsMapper tblZsgxTermsMapper;
    @Autowired
    private UserProvider userProvider;

    /**
     * 查询合同要素库列表
     * @param token	登录用户token
     * @param termsListParam	检索入参
     * @return 合同要素库列表
     * @throws Exception
     */
    @Override
    public JsonBean getTermsList(String token, TermsListParam termsListParam) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        //查询合同要素库列表
        IPage<TblZsgxTerms> termsPage = getTermsList(termsListParam);

        Map<String,Object> resultMap = new HashMap<String,Object>(1);
        resultMap.put("data", termsPage);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    private IPage<TblZsgxTerms> getTermsList(TermsListParam termsListParam) {
        String issueDateSort = termsListParam.getIssueDateSort();
        String title = termsListParam.getTitle();
        String contractTypeCode = termsListParam.getContractTypeCode();
        String interestPartyCode = termsListParam.getInterestPartyCode();
        String termsTypeCode = termsListParam.getTermsTypeCode();
        String issueYear = termsListParam.getIssueYear();
        Integer pageNumber = termsListParam.getPageNumber();
        Integer pageSize = termsListParam.getPageSize();
        QueryWrapper<TblZsgxTerms> queryWrapper = new QueryWrapper();
        if (!StringUtil.isEmpty(contractTypeCode)) {
            queryWrapper.likeRight("CONTRACT_TYPE_CODE", contractTypeCode);
        }
        if (!StringUtil.isEmpty(interestPartyCode)) {
            queryWrapper.likeRight("INTEREST_PARTY_CODE", interestPartyCode);
        }
        if (!StringUtil.isEmpty(termsTypeCode)) {
            queryWrapper.likeRight("TERMS_TYPE_CODE", termsTypeCode);
        }
        if (!StringUtil.isEmpty(issueYear)) {
            queryWrapper.eq("ISSUE_YEAR", issueYear);
        }
        if (!StringUtil.isEmpty(title)) {
            queryWrapper.like("TITLE", title);
        }
        if (!StringUtil.isEmpty(issueDateSort) && issueDateSort.equals("asc")) {
            queryWrapper.orderByAsc("ISSUE_DATE");
        }
        if (!StringUtil.isEmpty(issueDateSort) && issueDateSort.equals("desc")) {
            queryWrapper.orderByDesc("ISSUE_DATE");
        }
        queryWrapper.eq("DELETED",0);
        return tblZsgxTermsMapper.selectPage(new Page<TblZsgxTerms>(pageNumber, pageSize), queryWrapper);
    }

    /**
     * 查询合同要素详情
     * @param token	用户登录token
     * @param id	条款ID
     * @return	合同要素详情
     */
    @Override
    public JsonBean getTermsInfo(String token, String id) throws Exception {
        log.info("这里是日志：查询合同要素详情");
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblZsgxTerms tblZsgxTerms = this.getById(id);
        Map<String,Object> resultMap = new HashMap<String,Object>(1);
        if (Objects.isNull(tblZsgxTerms)) {
            resultMap.put("data", null);
            return ResponseFormat.retParam(1, 200, resultMap);
        } else {
            resultMap.put("data", tblZsgxTerms);
            return ResponseFormat.retParam(1, 200, resultMap);
        }
    }

    @Override
    public TblZsgxTerms getById(String id) {
        return tblZsgxTermsMapper.selectById(id);
    }

    /**
     * 新建合同要素
     * @param token	用户登录token
     * @param createTermsParam	新建合同要素入参
     * @return
     */
    @Override
    public JsonBean createTerms(String token, CreateTermsParam createTermsParam) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        String orgFatherName = loginStaff.getOrgFatherName();
        String orgName = loginStaff.getOrgName();
        String username = loginStaff.getUsername();

        TblZsgxTerms tblZsgxTerms = new TblZsgxTerms();
        BeanUtils.copyProperties(createTermsParam, tblZsgxTerms);
        tblZsgxTerms.setId(RandomUtil.uuStringId());
        tblZsgxTerms.setCreateCompany(orgFatherName);
        tblZsgxTerms.setCreateDept(orgName);
        tblZsgxTerms.setCreateBy(username);
        tblZsgxTerms.setUpdateBy(username);
        tblZsgxTerms.setCreateTime(new Date());
        tblZsgxTerms.setUpdateTime(new Date());
        tblZsgxTermsMapper.insert(tblZsgxTerms);

        Map<String,Object> resultMap = new HashMap<String,Object>(1);
        resultMap.put("data", true);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    @Override
    public JsonBean download(String id, HttpServletResponse response) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // 根据id查询合同要素
        TblZsgxTerms terms = this.getById(id);
        if (terms == null) {
            return ResponseFormat.retParam(50001, "数据未找到");
        }

        // 合同要素内容
        String content = terms.getContent();
        if (StringUtils.isEmpty(content)) {
            return ResponseFormat.retParam(50001, "数据未找到");
        }
        // 文件名称
        String fileName = StringUtils.isBlank(terms.getTitle()) ? "合同要素"+terms.getId() : terms.getTitle();
        // 下载
        HtmlDownloadUtil.h2w(content,fileName,response);
        return ResponseFormat.retParam(1, 200);
    }

    @Override
    public JsonBean zipDownload(List<String> ids, HttpServletResponse response) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (ids.size() > 30) {
            return ResponseFormat.retParam(10003, "最多支持下载30条记录");
        }

        List<TblZsgxTerms> termsByIds = getTermsByIds(ids);
        if (CollectionUtils.isEmpty(termsByIds)) {
            return ResponseFormat.retParam(50001, "数据未找到");
        }

        // 构建批量下载数据
        Map<String,String> map = new HashMap<>();
        for (TblZsgxTerms terms : termsByIds) {
            String content = map.get(terms.getTitle());
            if (content != null) {
                map.put(terms.getTitle() + terms.getId(), terms.getContent());
            } else {
                map.put(terms.getTitle(), terms.getContent());
            }
        }
        HtmlDownloadUtil.zipDownload(map,"合同要素",response);
        return ResponseFormat.retParam(1, 200);
    }

    private List<TblZsgxTerms> getTermsByIds(List<String> ids) {
        QueryWrapper<TblZsgxTerms> queryWrapper = new QueryWrapper();
        queryWrapper.in("ID",ids);
        return tblZsgxTermsMapper.selectList(queryWrapper);
    }

}
