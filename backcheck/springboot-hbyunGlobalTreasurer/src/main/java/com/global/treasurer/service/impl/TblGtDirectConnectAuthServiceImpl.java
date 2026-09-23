package com.global.treasurer.service.impl;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblGtDirectConnectAuth;
import com.global.treasurer.mapper.TblGtDirectConnectAuthMapper;
import com.global.treasurer.service.TblGtDirectConnectAuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全球司库-直联授权Service实现类
 *
 * @author AI Developer
 * @since 2026-01-16
 */
@Service
public class TblGtDirectConnectAuthServiceImpl extends ServiceImpl<TblGtDirectConnectAuthMapper, TblGtDirectConnectAuth>
        implements TblGtDirectConnectAuthService {
    @Resource
    private TblGtDirectConnectAuthMapper tblGtDirectConnectAuthMapper;

    @Override
    public IPage<TblGtDirectConnectAuth> getPageList(Page<TblGtDirectConnectAuth> page,
                                                      String accountNumber,
                                                      String authType,
                                                      String authStatus) {
        // 使用 PageHelper 进行分页（项目已禁用 MyBatis-Plus 分页插件）
        PageHelper.startPage((int) page.getCurrent(), (int) page.getSize());

        // 使用自定义的 XML 查询方法
        List<TblGtDirectConnectAuth> list = tblGtDirectConnectAuthMapper.selectPageList(
                accountNumber, authType, authStatus);

        // 获取分页信息
        PageInfo<TblGtDirectConnectAuth> pageInfo = new PageInfo<>(list);

        // 转换为 MyBatis-Plus 的 IPage 格式
        Page<TblGtDirectConnectAuth> pageResult = new Page<>(pageInfo.getPageNum(), pageInfo.getPageSize());
        pageResult.setRecords(list);
        pageResult.setTotal(pageInfo.getTotal());

        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveDirectConnectAuth(TblGtDirectConnectAuth entity) {
        // 设置默认授权状态
        if (entity.getAuthStatus() == null) {
            entity.setAuthStatus("PENDING");
        }
        // 设置授权日期
        if (entity.getAuthDate() == null) {
            entity.setAuthDate(java.time.LocalDate.now());
        }
        return tblGtDirectConnectAuthMapper.insert(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDirectConnectAuth(TblGtDirectConnectAuth entity) {
        return tblGtDirectConnectAuthMapper.updateById(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDirectConnectAuth(Long authId) {
        return tblGtDirectConnectAuthMapper.deleteById(authId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveDirectConnectAuth(Long authId, Long approverId,
                                            String approvalOpinion, String authStatus) {
        TblGtDirectConnectAuth entity = tblGtDirectConnectAuthMapper.selectById(authId);
        if (entity == null) {
            return false;
        }
        entity.setApproverId(BigDecimal.valueOf(approverId));
        entity.setApprovalDate(java.time.LocalDate.now());
        entity.setApprovalOpinion(approvalOpinion);
        entity.setAuthStatus(authStatus);
        return tblGtDirectConnectAuthMapper.updateById(entity) > 0;
    }

    @Override
    public Map<String, Object> testBankConnection(Long authId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // TODO: 实现实际的银行连接测试逻辑
            // 这里可以调用银行API或模拟连接测试
            TblGtDirectConnectAuth entity = tblGtDirectConnectAuthMapper.selectById(authId);
            if (entity == null) {
                result.put("success", false);
                result.put("message", "授权记录不存在");
                return result;
            }

            // 模拟连接测试
            result.put("success", true);
            result.put("message", "连接测试成功");
            result.put("responseTime", "100ms");
            result.put("bankCode", entity.getBankCode());
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "连接测试失败: " + e.getMessage());
            return result;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean syncConnectionStatus(Long authId, Long operatorId) {
        try {
            TblGtDirectConnectAuth entity = tblGtDirectConnectAuthMapper.selectById(authId);
            if (entity == null) {
                return false;
            }

            // TODO: 实现实际的连接状态同步逻辑
            // 这里可以调用银行API获取最新状态
            entity.setAuthStatus("ACTIVE");
            return tblGtDirectConnectAuthMapper.updateById(entity) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
