package com.huabo.know.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.know.entity.*;
import com.huabo.know.mapper.*;
import com.huabo.know.service.ZshService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.know.utils.HtmlDownloadUtil;
import com.huabo.know.vo.param.JudicialCaseListParam;
import com.huabo.know.vo.param.LawRegulationParam;
import com.huabo.know.vo.param.LegalPracticeListParam;
import com.huabo.know.vo.result.LabelData;
import com.huabo.know.vo.result.LabelNode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2024-04-03
 */
@Service
public class ZshServiceImpl extends ServiceImpl<TblZsgxLegalPracticeMapper, TblZsgxLegalPractice> implements ZshService {

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private TblZsgxLegalPracticeMapper tblZsgxLegalPracticeMapper;

    @Autowired
    private TblZsgxJudicialCaseMapper tblZsgxJudicialCaseMapper;

    @Autowired
    private TblZsgxLawRegulationMapper tblZsgxLawRegulationMapper;

    @Autowired
    private TblZsgxLabelMapper tblZsgxLabelMapper;
    @Autowired
    private TblZsgxLabelTypeMapper tblZsgxLabelTypeMapper;

    /**
     * 查询法律实务列表
     * @param token 用户登录token
     * @param legalPracticeListParam    检索条件
     * @return  查询法律实务列表
     */
    @Override
    public JsonBean getLegalPracticeList(String token, LegalPracticeListParam legalPracticeListParam) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        //分页查询法律实务列表
        IPage<TblZsgxLegalPractice> tblZsgxTermsPage = getLegalPracticeList(legalPracticeListParam);

        Map<String,Object> resultMap = new HashMap<String,Object>(1);
        resultMap.put("data", tblZsgxTermsPage);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    private IPage<TblZsgxLegalPractice> getLegalPracticeList(LegalPracticeListParam legalPracticeListParam) {
        String title = legalPracticeListParam.getTitle();
        String content = legalPracticeListParam.getContent();
        String articleByAuthor = legalPracticeListParam.getArticleByAuthor();
        String contractTypeCode = legalPracticeListParam.getContractTypeCode();
        String journalIdLsCode = legalPracticeListParam.getJournalIdLsCode();
        String firmIdCode = legalPracticeListParam.getFirmIdCode();
        String issueYear = legalPracticeListParam.getIssueYear();
        Date issueDateStart = legalPracticeListParam.getIssueDateStart();
        Date issueDateEnd = legalPracticeListParam.getIssueDateEnd();
        Integer pageNumber = legalPracticeListParam.getPageNumber();
        Integer pageSize = legalPracticeListParam.getPageSize();
        String issueDateSort = legalPracticeListParam.getIssueDateSort();
        QueryWrapper<TblZsgxLegalPractice> queryWrapper = new QueryWrapper();
        if (!StringUtil.isEmpty(title)) {
            queryWrapper.like("TITLE", title);
        }
        if (!StringUtil.isEmpty(content)) {
            queryWrapper.like("CONTENT", content);
        }
        if (!StringUtil.isEmpty(articleByAuthor)) {
            queryWrapper.like("ARTICLE_BY_AUTHOR", articleByAuthor);
        }
        if (!StringUtil.isEmpty(contractTypeCode)) {
            queryWrapper.likeRight("CONTRACT_TYPE_CODE", contractTypeCode);
        }
        if (!StringUtil.isEmpty(journalIdLsCode)) {
            queryWrapper.likeRight("JOURNAL_ID_LS_CODE", journalIdLsCode);
        }
        if (!StringUtil.isEmpty(firmIdCode)) {
            queryWrapper.likeRight("FIRM_ID_CODE", firmIdCode);
        }
        if (!StringUtil.isEmpty(issueYear)) {
            queryWrapper.eq("ISSUE_YEAR", issueYear);
        }
        if (Objects.nonNull(issueDateStart) && Objects.nonNull(issueDateEnd)) {
            queryWrapper.between("ISSUE_DATE", issueDateStart, issueDateEnd);
        }
        if (!StringUtil.isEmpty(issueDateSort) && issueDateSort.equals("asc")) {
            queryWrapper.orderByAsc("ISSUE_DATE");
        }
        if (!StringUtil.isEmpty(issueDateSort) && issueDateSort.equals("desc")) {
            queryWrapper.orderByDesc("ISSUE_DATE");
        }
        queryWrapper.eq("DELETED",0);
        return tblZsgxLegalPracticeMapper.selectPage(new Page<TblZsgxLegalPractice>(pageNumber, pageSize), queryWrapper);
    }

    /**
     * 查询法律实务详情
     * @param token 用户登录token
     * @param id    法律实务id
     * @return  查询法律实务详情
     */
    @Override
    public JsonBean getLegalPracticeInfo(String token, String id) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblZsgxLegalPractice tblZsgxLegalPractice = tblZsgxLegalPracticeMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<String,Object>(1);
        if (Objects.isNull(tblZsgxLegalPractice)) {
            resultMap.put("data", null);
            return ResponseFormat.retParam(1, 200, resultMap);
        } else {
            resultMap.put("data", tblZsgxLegalPractice);
            return ResponseFormat.retParam(1, 200, resultMap);
        }
    }

    /**
     * 查询法律案例列表
     * @param token 用户登录token
     * @param judicialCaseListParam 检索条件
     * @return  法律案例列表
     */
    @Override
    public JsonBean getJudicialCaseList(String token, JudicialCaseListParam judicialCaseListParam) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        //分页查询法律案例列表
        IPage<TblZsgxJudicialCase> tblZsgxJudicialCasePage = getJudicialCaseList(judicialCaseListParam);

        Map<String,Object> resultMap = new HashMap<String,Object>(1);
        resultMap.put("data", tblZsgxJudicialCasePage);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    private IPage<TblZsgxJudicialCase> getJudicialCaseList(JudicialCaseListParam judicialCaseListParam) {
        String title = judicialCaseListParam.getTitle();
        String content = judicialCaseListParam.getContent();
        String caseFlag = judicialCaseListParam.getCaseFlag();
        String categoryCode = judicialCaseListParam.getCategoryCode();
        String caseClassCode = judicialCaseListParam.getCaseClassCode();
        String contractTypeCode = judicialCaseListParam.getContractTypeCode();
        String lastInstanceCourtCode = judicialCaseListParam.getLastInstanceCourtCode();
        String courtGradeCode = judicialCaseListParam.getCourtGradeCode();
        String trialStepCode = judicialCaseListParam.getTrialStepCode();
        String documentAttrCode = judicialCaseListParam.getDocumentAttrCode();
        String caseGradeCode = judicialCaseListParam.getCaseGradeCode();
        String noPublicReasonCode = judicialCaseListParam.getNoPublicReasonCode();
        String issueYear = judicialCaseListParam.getIssueYear();
        Date issueDateStart = judicialCaseListParam.getIssueDateStart();
        Date issueDateEnd = judicialCaseListParam.getIssueDateEnd();
        Date lastInstanceDateStart = judicialCaseListParam.getLastInstanceDateStart();
        Date lastInstanceDateEnd = judicialCaseListParam.getLastInstanceDateEnd();
        Integer pageNumber = judicialCaseListParam.getPageNumber();
        Integer pageSize = judicialCaseListParam.getPageSize();
        QueryWrapper<TblZsgxJudicialCase> queryWrapper = new QueryWrapper();
        if (!StringUtil.isEmpty(title)) {
            queryWrapper.like("TITLE", title);
        }
        if (!StringUtil.isEmpty(content)) {
            queryWrapper.like("CONTENT", content);
        }
        if (!StringUtil.isEmpty(caseFlag)) {
            queryWrapper.like("CASE_FLAG", caseFlag);
        }
        if (!StringUtil.isEmpty(categoryCode)) {
            queryWrapper.likeRight("CATEGORY_CODE", categoryCode);
        }
        if (!StringUtil.isEmpty(caseClassCode)) {
            queryWrapper.likeRight("CASE_CLASS_CODE", caseClassCode);
        }
        if (!StringUtil.isEmpty(contractTypeCode)) {
            queryWrapper.likeRight("CONTRACT_TYPE_CODE", contractTypeCode);
        }
        if (!StringUtil.isEmpty(lastInstanceCourtCode)) {
            queryWrapper.likeRight("LAST_INSTANCE_COURT_CODE", lastInstanceCourtCode);
        }
        if (!StringUtil.isEmpty(courtGradeCode)) {
            queryWrapper.likeRight("COURT_GRADE_CODE", courtGradeCode);
        }
        if (!StringUtil.isEmpty(trialStepCode)) {
            queryWrapper.likeRight("TRIAL_STEP_CODE", trialStepCode);
        }
        if (!StringUtil.isEmpty(documentAttrCode)) {
            queryWrapper.likeRight("DOCUMENT_ATTR_CODE", documentAttrCode);
        }
        if (!StringUtil.isEmpty(caseGradeCode)) {
            queryWrapper.likeRight("CASE_GRADE_CODE", caseGradeCode);
        }
        if (!StringUtil.isEmpty(noPublicReasonCode)) {
            queryWrapper.likeRight("NO_PUBLIC_REASON_CODE", noPublicReasonCode);
        }
        if (!StringUtil.isEmpty(issueYear)) {
            queryWrapper.eq("ISSUE_YEAR", issueYear);
        }
        if (Objects.nonNull(issueDateStart) && Objects.nonNull(issueDateEnd)) {
            queryWrapper.between("ISSUE_DATE", issueDateStart, issueDateEnd);
        }
        if (Objects.nonNull(lastInstanceDateStart) && Objects.nonNull(lastInstanceDateEnd)) {
            queryWrapper.between("LAST_INSTANCE_DATE", lastInstanceDateStart, lastInstanceDateEnd);
        }
        queryWrapper.eq("DELETED",0);
        return tblZsgxJudicialCaseMapper.selectPage(new Page<TblZsgxJudicialCase>(pageNumber, pageSize), queryWrapper);
    }

    /**
     * 查询法律案例详情
     * @param token 用户登录token
     * @param id    法律案例id
     * @return  法律案例详情
     */
    @Override
    public JsonBean getJudicialCaseInfo(String token, String id) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblZsgxJudicialCase tblZsgxJudicialCase = tblZsgxJudicialCaseMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<String,Object>(1);
        if (Objects.isNull(tblZsgxJudicialCase)) {
            resultMap.put("data", null);
            return ResponseFormat.retParam(1, 200, resultMap);
        } else {
            resultMap.put("data", tblZsgxJudicialCase);
            return ResponseFormat.retParam(1, 200, resultMap);
        }
    }

    /**
     * 查询法律法规列表
     * @param token 用户登录token
     * @param lawRegulationParam    检索条件
     * @return  法律法规列表
     */
    @Override
    public JsonBean getLawRegulationList(String token, LawRegulationParam lawRegulationParam) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        //分页查询法律法规列表
        IPage<TblZsgxLawRegulation> tblZsgxLawRegulationPage = getLawRegulationList(lawRegulationParam);

        Map<String,Object> resultMap = new HashMap<String,Object>(1);
        resultMap.put("data", tblZsgxLawRegulationPage);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    private IPage<TblZsgxLawRegulation> getLawRegulationList(LawRegulationParam lawRegulationParam) throws ParseException {
        String title = lawRegulationParam.getTitle();
        String content = lawRegulationParam.getContent();
        String documentNo = lawRegulationParam.getDocumentNo();
        String contractTypeCode = lawRegulationParam.getContractTypeCode();
        String effectivenessDicCode = lawRegulationParam.getEffectivenessDicCode();
        String timelinessDicCode = lawRegulationParam.getTimelinessDicCode();
        String issueDepartmentCode = lawRegulationParam.getIssueDepartmentCode();
        String issueYear = lawRegulationParam.getIssueYear();
        Date issueDateStart = lawRegulationParam.getIssueDateStart();
        Date issueDateEnd = lawRegulationParam.getIssueDateEnd();
        Date implementDateStart = lawRegulationParam.getImplementDateStart();
        Date implementDateEnd = lawRegulationParam.getImplementDateEnd();
        Integer pageNumber = lawRegulationParam.getPageNumber();
        Integer pageSize = lawRegulationParam.getPageSize();
        QueryWrapper<TblZsgxLawRegulation> queryWrapper = new QueryWrapper();
        if (!StringUtil.isEmpty(title)) {
            queryWrapper.like("TITLE", title);
        }
        if (!StringUtil.isEmpty(content)) {
            queryWrapper.like("CONTENT", content);
        }
        if (!StringUtil.isEmpty(documentNo)) {
            queryWrapper.like("DOCUMENT_NO", documentNo);
        }
        if (!StringUtil.isEmpty(contractTypeCode)) {
            queryWrapper.likeRight("CONTRACT_TYPE_CODE", contractTypeCode);
        }
        if (!StringUtil.isEmpty(effectivenessDicCode)) {
            queryWrapper.likeRight("EFFECTIVENESS_DIC_CODE", effectivenessDicCode);
        }
        if (!StringUtil.isEmpty(timelinessDicCode)) {
            queryWrapper.likeRight("TIMELINESS_DIC_CODE", timelinessDicCode);
        }
        if (!StringUtil.isEmpty(issueDepartmentCode)) {
            queryWrapper.likeRight("ISSUE_DEPARTMENT_CODE", issueDepartmentCode);
        }
        if (!StringUtil.isEmpty(issueYear)) {
            queryWrapper.eq("ISSUE_YEAR", issueYear);
        }
        if (Objects.nonNull(issueDateStart) && Objects.nonNull(issueDateEnd)) {
            queryWrapper.between("ISSUE_DATE", issueDateStart, issueDateEnd);
        }
        if (Objects.nonNull(implementDateStart) && Objects.nonNull(implementDateEnd)) {
            queryWrapper.between("IMPLEMENT_DATE", implementDateStart, implementDateEnd);
        }
        queryWrapper.eq("DELETED",0);
        return tblZsgxLawRegulationMapper.selectPage(new Page<TblZsgxLawRegulation>(pageNumber, pageSize), queryWrapper);
    }

    /**
     * 查询法律法规详情
     * @param token 用户登录token
     * @param id    法律法规id
     * @return  法律法规详情
     */
    @Override
    public JsonBean getLawRegulationInfo(String token, String id) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblZsgxLawRegulation tblZsgxLawRegulation = tblZsgxLawRegulationMapper.selectById(id);
        Map<String,Object> resultMap = new HashMap<String,Object>(1);
        if (Objects.isNull(tblZsgxLawRegulation)) {
            resultMap.put("data", null);
            return ResponseFormat.retParam(1, 200, resultMap);
        } else {
            resultMap.put("data", tblZsgxLawRegulation);
            return ResponseFormat.retParam(1, 200, resultMap);
        }
    }

    @Override
    public JsonBean sidebar(List<String> types) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        // 按照类型查询标签类型是否存在
        List<TblZsgxLabelType> labelTypes = getLabelType(types);
        if (CollectionUtils.isEmpty(labelTypes) || labelTypes.size() != types.size()) {
            return ResponseFormat.retParam(0, 50001, null);
        }

        //根据类型批量查询标签数据
        List<TblZsgxLabel> labels = this.getLabelsByTypes(types);
        if (CollectionUtils.isEmpty(labels)) {
            return ResponseFormat.retParam(0, 50001, null);
        }

        // 标签按照 标签类型分组
        Map<String, List<LabelNode>> labelsMap = labels.stream().map(source -> {
            LabelNode labelNode = new LabelNode();
            BeanUtils.copyProperties(source, labelNode);
            return labelNode;
        }).collect(Collectors.groupingBy(LabelNode::getTypeValue));

        //构建树形返回结果
        List<LabelData> result = new ArrayList<>();
        for (TblZsgxLabelType labelType : labelTypes) {
            LabelData labelData = new LabelData();
            labelData.setLabel(labelType.getTypeName());
            labelData.setValue(labelType.getTypeValue());

            // 构建标签树
            List<LabelNode> labelNodes = labelsMap.get(labelType.getTypeValue());
            List<LabelNode> labelNodeTree = this.buildLabelTree(labelNodes);
            labelData.setData(labelNodeTree);
            result.add(labelData);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>(1);
        resultMap.put("data", result);
        return ResponseFormat.retParam(1, 200, resultMap);
    }

    /**
     * 根据types批量查询标签类型
     * @param types
     * @return
     */
    private List<TblZsgxLabelType> getLabelType(List<String> types) {
        QueryWrapper<TblZsgxLabelType> queryWrapper = new QueryWrapper();
        queryWrapper.eq("DELETED",0);
        queryWrapper.in("TYPE_VALUE",types);
        queryWrapper.orderByAsc("SORT");
        return tblZsgxLabelTypeMapper.selectList(queryWrapper);
    }

    /**
     * 根据types批量查询标签
     * @param types
     * @return
     */
    private List<TblZsgxLabel> getLabelsByTypes(List<String> types) {
        QueryWrapper<TblZsgxLabel> queryWrapper = new QueryWrapper();
        queryWrapper.eq("DELETED",0);
        queryWrapper.in("TYPE_VALUE",types);
        queryWrapper.orderByAsc(Arrays.asList("TYPE_VALUE","PID","SORT"));
        return tblZsgxLabelMapper.selectList(queryWrapper);
    }

    /**
     * 构建标签树
     * @param nodes
     * @return
     */
    private List<LabelNode> buildLabelTree(List<LabelNode> nodes) {
        Map<String, LabelNode> nodeMap = nodes.stream()
                .collect(Collectors.toMap(LabelNode::getId, Function.identity()));

        List<LabelNode> rootNodes = new ArrayList<>();
        for (LabelNode node : nodes) {
            LabelNode parent = nodeMap.get(node.getPid());
            if (parent != null) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(node);
            } else {
                rootNodes.add(node);
            }
        }
        return rootNodes;
    }

    /**
     * 法律实务下载
     *
     * @param id       法律实务主键id
     * @param response
     * @return
     */
    @Override
    public JsonBean legalPracticeDownload(String id, HttpServletResponse response) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblZsgxLegalPractice tblZsgxLegalPractice = tblZsgxLegalPracticeMapper.selectById(id);
        if (Objects.isNull(tblZsgxLegalPractice)) {
            return ResponseFormat.retParam(50001, "数据未找到");
        }
        String title = tblZsgxLegalPractice.getTitle();
        if (StringUtils.isBlank(title)) {
            title = "法律实务" + tblZsgxLegalPractice.getId();
        }
        HtmlDownloadUtil.h2w(tblZsgxLegalPractice.getContent(), title, response);
        return ResponseFormat.retParam(1, 200);
    }

    @Override
    public JsonBean legalPracticeZipDownload(List<String> ids, HttpServletResponse response) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (CollectionUtils.isEmpty(ids)) {
            return ResponseFormat.retParam(10002, "ids参数为空");
        }
        if (ids.size() > 30) {
            return ResponseFormat.retParam(10003, "最多下载30个");
        }
        QueryWrapper<TblZsgxLegalPractice> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("ID", ids);
        List<TblZsgxLegalPractice> legalPracticeList = tblZsgxLegalPracticeMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(legalPracticeList)) {
            return ResponseFormat.retParam(50001, "数据未找到");
        }
        //整理数据格式
        Map<String,String> map = new HashMap<>();
        for (TblZsgxLegalPractice legalPractice : legalPracticeList) {
            map.put(legalPractice.getTitle() + legalPractice.getId(), legalPractice.getContent());
        }
        HtmlDownloadUtil.zipDownload(map,"法律实务", response);
        return ResponseFormat.retParam(1, 200);
    }

    @Override
    public JsonBean judicialCaseDownload(String id, HttpServletResponse response) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblZsgxJudicialCase tblZsgxJudicialCase = tblZsgxJudicialCaseMapper.selectById(id);
        if (Objects.isNull(tblZsgxJudicialCase)) {
            return ResponseFormat.retParam(50001, "数据未找到");
        }
        String title = tblZsgxJudicialCase.getTitle();
        if (StringUtils.isBlank(title)) {
            title = "法律案例" + tblZsgxJudicialCase.getId();
        }
        HtmlDownloadUtil.h2w(tblZsgxJudicialCase.getContent(), title, response);
        return ResponseFormat.retParam(1, 200);
    }

    @Override
    public JsonBean judicialCaseZipDownload(List<String> ids, HttpServletResponse response) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (CollectionUtils.isEmpty(ids)) {
            return ResponseFormat.retParam(10002, "ids参数为空");
        }
        if (ids.size() > 30) {
            return ResponseFormat.retParam(10003, "最多下载30个");
        }
        QueryWrapper<TblZsgxJudicialCase> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("ID", ids);
        List<TblZsgxJudicialCase> judicialCaseList = tblZsgxJudicialCaseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(judicialCaseList)) {
            return ResponseFormat.retParam(50001, "数据未找到");
        }
        //整理数据格式
        Map<String,String> map = new HashMap<>();
        for (TblZsgxJudicialCase judicialCase : judicialCaseList) {
            map.put(judicialCase.getTitle() + judicialCase.getId(), judicialCase.getContent());
        }
        HtmlDownloadUtil.zipDownload(map,"法律案例", response);
        return ResponseFormat.retParam(1, 200);
    }

    @Override
    public JsonBean lawRegulationDownload(String id, HttpServletResponse response) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblZsgxLawRegulation tblZsgxLawRegulation = tblZsgxLawRegulationMapper.selectById(id);
        if (Objects.isNull(tblZsgxLawRegulation)) {
            return ResponseFormat.retParam(50001, "数据未找到");
        }
        String title = tblZsgxLawRegulation.getTitle();
        if (StringUtils.isBlank(title)) {
            title = "法律法规" + tblZsgxLawRegulation.getId();
        }
        HtmlDownloadUtil.h2w(tblZsgxLawRegulation.getContent(), title, response);
        return ResponseFormat.retParam(1, 200);
    }

    @Override
    public JsonBean lawRegulationZipDownload(List<String> ids, HttpServletResponse response) throws Exception {
        //鉴权
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (CollectionUtils.isEmpty(ids)) {
            return ResponseFormat.retParam(10002, "ids参数为空");
        }
        if (ids.size() > 30) {
            return ResponseFormat.retParam(10003, "最多下载30个");
        }
        QueryWrapper<TblZsgxLawRegulation> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("ID", ids);
        List<TblZsgxLawRegulation> lawRegulationList = tblZsgxLawRegulationMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(lawRegulationList)) {
            return ResponseFormat.retParam(50001, "数据未找到");
        }
        //整理数据格式
        Map<String,String> map = new HashMap<>();
        for (TblZsgxLawRegulation lawRegulation : lawRegulationList) {
            map.put(lawRegulation.getTitle() + lawRegulation.getId(), lawRegulation.getContent());
        }
        HtmlDownloadUtil.zipDownload(map,"法律法规", response);
        return ResponseFormat.retParam(1, 200);
    }

}
