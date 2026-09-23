package com.huabo.financialdata.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.financialdata.entity.dto.accSubject.AccSubjectListPageQuery;
import com.huabo.financialdata.entity.entity.Account;
import com.huabo.financialdata.entity.entity.AccountQuery;
import com.huabo.financialdata.entity.enums.AccountDcEnum;
import com.huabo.financialdata.entity.vo.accBook.AccBookVO;
import com.huabo.financialdata.entity.vo.accSubject.AccSubjectPageInfoVO;
import com.huabo.financialdata.mapper.AccountMapper;
import com.huabo.financialdata.service.IAccountService;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 会计科目表 - 服务接口实现
 *
 * @author lee
 * @version 1.0.0
 **/
@Service
public class AccountServiceImpl implements IAccountService {

    @Resource
    private AccountMapper accountMapper;

    /**
     * 会计科目表 - 分页查询
     *
     * @param query  请求参数
     * @return 获取分页结果
     */
    @Override
    public PageInfo<AccSubjectPageInfoVO> getListByPage(AccBookVO accBookVO, AccSubjectListPageQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        AccountQuery example = new AccountQuery();
        //数据库设置
        example.setDbSource(accBookVO.getAcctId());
        //查询参数赋值
        example.setACCID(query.getAccId());
        example.setACCNAME1(query.getAccName());
        String dc = query.getDc();
        if (AccountDcEnum.getCodeByName(query.getDc()) != null && AccountDcEnum.getCodeByName(query.getDc()) != "") {
            dc = AccountDcEnum.getCodeByName(query.getDc());
        }
        example.setDC(dc);
//        example.setDC(query.getDc());
        example.setTYPENAME(query.getTypeName());
        example.setHIGHACCID(query.getHighAccId());
        example.setTYPEID(query.getTypeId());
        example.setACCNAME2(query.getAccAllName());
        example.setAYEAR(query.getBookYear());
        example.setISDCACC(query.getDcAccStatus());
        example.setIGRADE(query.getIgrade());
        example.setAYEAR(Integer.parseInt(accBookVO.getBookYear()));
        example.setStatus(query.getStatus() == null ? "等于" : query.getStatus());
        //排序
        example.setOrderByClause("ACCID");
        List<Account> list = accountMapper.selectByCondition(example);
        PageInfo pageInfo = new PageInfo<>(list);
        if (CollectionUtil.isNotEmpty(list)) {
            //处理上一级科目
            List<String> highAccIdList = list.stream().map(Account::getHIGHACCID).collect(Collectors.toList());
            AccountQuery exampleTemp = new AccountQuery();
            exampleTemp.setDbSource(accBookVO.getAcctId());
            exampleTemp.setAYEAR(Integer.parseInt(accBookVO.getBookYear()));
            StringJoiner joiner = new StringJoiner(",");
            for (String accId : highAccIdList) {
                joiner.add(accId + "");
            }
            exampleTemp.setAccIdStrs("('"+joiner.toString().replace(",", "','")+"')");
            System.out.println(highAccIdList.toString());
            List<Account> highAccInfoList = accountMapper.selectByCondition(exampleTemp);
            Map<String, Account> highAccInfoMaps = highAccInfoList.stream().collect(Collectors.toMap(Account::getACCID, Function.identity(), (oldValue, newValue) -> newValue));
            //映射VO数据
            List<AccSubjectPageInfoVO> voList = Lists.newArrayList();
            list.forEach(item -> voList.add(doInfoVoMapper(item, highAccInfoMaps)));
            pageInfo.setList(voList);
        }

        return pageInfo;
    }

    /**
     * 将账户信息映射为AccSubjectPageInfoVO对象
     * @param account 账户对象
     * @param highAccInfoMaps 上一级分类信息的映射表
     * @return 映射后的AccSubjectPageInfoVO对象
     */
    private AccSubjectPageInfoVO doInfoVoMapper(Account account, Map<String, Account> highAccInfoMaps) {
        AccSubjectPageInfoVO vo = new AccSubjectPageInfoVO();
        vo.setAccId(account.getACCID());
        vo.setAccName(account.getACCNAME1());
        vo.setDc(account.getDC());
        //枚举获取：科目方向 中文描述
        vo.setDcName(AccountDcEnum.getNameByCode(vo.getDc()));
        vo.setTypeName(account.getTYPENAME());
        vo.setHighAccId(account.getHIGHACCID());
        //处理上一级分类名称
        Account temp = highAccInfoMaps.get(account.getHIGHACCID());
        if (Objects.nonNull(temp)) {
            vo.setHighAccName(temp.getACCNAME1());
        }
        vo.setTypeId(account.getACCID());
        vo.setAccAllName(account.getACCNAME2());
        vo.setSicash(account.getSICASH());
        vo.setBzkmStatus(account.getISBZKM());
        vo.setDcAccStatus(account.getISDCACC());
        vo.setGrade(account.getIGRADE());
        vo.setYear(account.getAYEAR());
        return vo;
    }

}
