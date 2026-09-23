package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.mapper.PaymentTermsMapper;
import com.financial.sharing.service.PaymentTermsService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账期设置服务实现类
 * 
 * @author Financial Sharing System
 * @since 2024-12-28
 */
@Slf4j
@Service
public class PaymentTermsServiceImpl implements PaymentTermsService {

    @Resource
    private PaymentTermsMapper paymentTermsMapper;

    @Override
    public MyJsonBean<PageResult> getPaymentTermsList(PageableParam param) {
        try {
            // 使用 PageHelper 进行分页
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());

            // 构建查询参数
            Map<String, Object> queryParam = new HashMap<>();
            // 这里可以添加更多查询条件

            // 查询数据列表
            List<Map<String, Object>> list = paymentTermsMapper.selectPaymentTermsList(queryParam);

            // 获取分页信息
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);

            // 构建返回结果
            PageResult result = new PageResult();
            result.setTlist(pageInfo.getList());
            result.setTotalRecord((int) pageInfo.getTotal());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询账期设置列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getPaymentTermsById(Long termsId) {
        try {
            Map<String, Object> termsData = paymentTermsMapper.selectPaymentTermsDetail(termsId);
            if (termsData == null) {
                return MyJsonBean.errorData("账期设置不存在");
            }
            return MyJsonBean.successData(termsData);
        } catch (Exception e) {
            log.error("查询账期设置详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean saveOrUpdatePaymentTerms(Object param) {
        try {
            // TODO: 实现保存或更新逻辑
            return MyJsonBean.successData("保存成功");
        } catch (Exception e) {
            log.error("保存账期设置失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean deletePaymentTerms(Long termsId) {
        try {
            // TODO: 实现删除逻辑
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除账期设置失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }
}

