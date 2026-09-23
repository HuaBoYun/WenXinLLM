package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingConfigDTO;
import com.global.treasurer.dto.FinancingConfigQueryDTO;
import com.global.treasurer.entity.TblFinancingBasicParams;
import com.global.treasurer.mapper.FinancingConfigMapper;
import com.global.treasurer.service.FinancingConfigService;
import com.hbfk.util.BizException;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 融资配置Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-13
 */
@Service
public class FinancingConfigServiceImpl extends ServiceImpl<FinancingConfigMapper, TblFinancingBasicParams>
        implements FinancingConfigService {
    private static final Logger log = LoggerFactory.getLogger(FinancingConfigServiceImpl.class);

    @Resource
    private FinancingConfigMapper financingConfigMapper;

    @Resource
    private UserProvider userProvider;

    @Override
    public PageInfo<TblFinancingBasicParams> getConfigList(FinancingConfigQueryDTO queryDTO) {
        // 设置默认分页参数
        if (queryDTO.getPageNum() == null || queryDTO.getPageNum() <= 0) {
            queryDTO.setPageNum(1);
        }
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() <= 0) {
            queryDTO.setPageSize(10);
        }

        // 分页查询
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 构建查询条件
        QueryWrapper<TblFinancingBasicParams> queryWrapper = new QueryWrapper<>();

        // 参数类型模糊查询
        if (queryDTO.getParamType() != null && !queryDTO.getParamType().trim().isEmpty()) {
            queryWrapper.like("PARAM_TYPE", queryDTO.getParamType().trim());
        }

        // 参数名称模糊查询
        if (queryDTO.getParamName() != null && !queryDTO.getParamName().trim().isEmpty()) {
            queryWrapper.like("PARAM_NAME", queryDTO.getParamName().trim());
        }

        // 参数值模糊查询
        if (queryDTO.getParamValue() != null && !queryDTO.getParamValue().trim().isEmpty()) {
            queryWrapper.like("PARAM_VALUE", queryDTO.getParamValue().trim());
        }

        // 状态精确查询
        if (queryDTO.getStatus() != null) {
            queryWrapper.eq("STATUS", queryDTO.getStatus());
        }

        // 按创建时间倒序排列
        queryWrapper.orderByDesc("CREATE_TIME");

        List<TblFinancingBasicParams> list = financingConfigMapper.selectList(queryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    public TblFinancingBasicParams getConfigById(Long paramId) {
        if (paramId == null) {
            throw new BizException("参数ID不能为空");
        }
        return financingConfigMapper.selectById(paramId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblFinancingBasicParams createConfig(FinancingConfigDTO dto) {
        if (dto == null) {
            throw new BizException("配置信息不能为空");
        }
        if (dto.getParamType() == null || dto.getParamType().trim().isEmpty()) {
            throw new BizException("参数类型不能为空");
        }
        if (dto.getParamName() == null || dto.getParamName().trim().isEmpty()) {
            throw new BizException("参数名称不能为空");
        }
        if (dto.getParamValue() == null || dto.getParamValue().trim().isEmpty()) {
            throw new BizException("参数值不能为空");
        }

        // 检查参数名称是否重复
        QueryWrapper<TblFinancingBasicParams> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PARAM_NAME", dto.getParamName().trim());
        Integer count = financingConfigMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BizException("参数名称已存在");
        }

        TblFinancingBasicParams params = new TblFinancingBasicParams();
        params.setParamType(dto.getParamType().trim());
        params.setParamName(dto.getParamName().trim());
        params.setParamValue(dto.getParamValue().trim());
        params.setDescription(dto.getDescription());
        params.setStatus(dto.getStatus() != null ? dto.getStatus() : "ENABLE");
        params.setRemark(dto.getRemark());

        // 设置创建人信息
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff != null) {
                params.setCreateUser(loginStaff.getStaffid().longValue());
            }
        } catch (Exception e) {
            log.warn("获取当前用户信息失败，使用默认值", e);
        }

        params.setCreateTime(new Date());

        financingConfigMapper.insert(params);
        log.info("创建融资配置成功，参数名称: {}", dto.getParamName());
        return params;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblFinancingBasicParams updateConfig(FinancingConfigDTO dto) {
        if (dto == null) {
            throw new BizException("配置信息不能为空");
        }
        if (dto.getParamId() == null) {
            throw new BizException("参数ID不能为空");
        }

        // 检查配置是否存在
        TblFinancingBasicParams existingParams = financingConfigMapper.selectById(dto.getParamId());
        if (existingParams == null) {
            throw new BizException("配置不存在");
        }

        // 如果修改了参数名称，检查是否重复
        if (dto.getParamName() != null && !dto.getParamName().trim().isEmpty()
                && !dto.getParamName().trim().equals(existingParams.getParamName())) {
            QueryWrapper<TblFinancingBasicParams> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("PARAM_NAME", dto.getParamName().trim());
            queryWrapper.ne("PARAM_ID", dto.getParamId());
            Integer count = financingConfigMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BizException("参数名称已存在");
            }
        }

        // 更新字段
        if (dto.getParamType() != null && !dto.getParamType().trim().isEmpty()) {
            existingParams.setParamType(dto.getParamType().trim());
        }
        if (dto.getParamName() != null && !dto.getParamName().trim().isEmpty()) {
            existingParams.setParamName(dto.getParamName().trim());
        }
        if (dto.getParamValue() != null && !dto.getParamValue().trim().isEmpty()) {
            existingParams.setParamValue(dto.getParamValue().trim());
        }
        if (dto.getDescription() != null) {
            existingParams.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            existingParams.setStatus(dto.getStatus());
        }
        if (dto.getRemark() != null) {
            existingParams.setRemark(dto.getRemark());
        }

        // 设置更新人信息
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff != null) {
                existingParams.setUpdateUser(loginStaff.getStaffid().longValue());
            }
        } catch (Exception e) {
            log.warn("获取当前用户信息失败，使用默认值", e);
        }

        existingParams.setUpdateTime(new Date());

        financingConfigMapper.updateById(existingParams);
        log.info("更新融资配置成功，参数ID: {}", dto.getParamId());
        return existingParams;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConfig(Long paramId) {
        if (paramId == null) {
            throw new BizException("参数ID不能为空");
        }

        // 检查配置是否存在
        TblFinancingBasicParams params = financingConfigMapper.selectById(paramId);
        if (params == null) {
            throw new BizException("配置不存在");
        }

        int result = financingConfigMapper.deleteById(paramId);
        if (result > 0) {
            log.info("删除融资配置成功，参数ID: {}", paramId);
        }
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long paramId, String status) {
        if (paramId == null) {
            throw new BizException("参数ID不能为空");
        }
        if (status == null || (!status.equals("ENABLE") && !status.equals("DISABLE"))) {
            throw new BizException("状态值不合法");
        }

        // 检查配置是否存在
        TblFinancingBasicParams params = financingConfigMapper.selectById(paramId);
        if (params == null) {
            throw new BizException("配置不存在");
        }

        params.setStatus(status);

        // 设置更新人信息
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff != null) {
                params.setUpdateUser(loginStaff.getStaffid().longValue());
            }
        } catch (Exception e) {
            log.warn("获取当前用户信息失败，使用默认值", e);
        }

        params.setUpdateTime(new Date());

        int result = financingConfigMapper.updateById(params);
        if (result > 0) {
            log.info("更新融资配置状态成功，参数ID: {}, 状态: {}", paramId, status);
        }
        return result > 0;
    }
}
