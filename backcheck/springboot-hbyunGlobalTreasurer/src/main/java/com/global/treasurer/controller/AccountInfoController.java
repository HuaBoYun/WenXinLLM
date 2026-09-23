package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblThirdPartyAccount;
import com.global.treasurer.mapper.TblThirdPartyAccountMapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账户信息控制器
 *
 * @author AI Developer
 * @date 2026-01-15
 */
@RestController
@RequestMapping("/account/info")
@Api(tags = "账户信息管理")
public class AccountInfoController {
    @Resource
    private TblThirdPartyAccountMapper tblThirdPartyAccountMapper;

    @Resource
    private UserProvider userProvider;

    /**
     * 获取账户列表(GET方式,兼容前端请求)
     */
    @GetMapping("/list")
    @ApiOperation("获取账户列表")
    public String getAccountListGet(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            return getAccountList(params);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取账户列表(POST方式)
     */
    @PostMapping("/list")
    @ApiOperation("获取账户列表")
    public String getAccountListPost(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            return getAccountList(params);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 公共查询方法
     */
    private String getAccountList(Map<String, Object> params) {
        try {
            // 分页参数
            int pageNum = 1;
            int pageSize = 10;

            if (params.get("pageNum") != null) {
                try {
                    pageNum = Integer.parseInt(params.get("pageNum").toString());
                } catch (NumberFormatException e) {
                    // ignore
                }
            }

            if (params.get("pageSize") != null) {
                try {
                    pageSize = Integer.parseInt(params.get("pageSize").toString());
                } catch (NumberFormatException e) {
                    // ignore
                }
            }

            // 使用PageHelper分页
            com.github.pagehelper.PageHelper.startPage(pageNum, pageSize);

            // 构建查询条件
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<TblThirdPartyAccount> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();

            // 其他可选过滤条件
            Object accountCode = params.get("accountCode");
            if (accountCode != null && !StringUtils.isEmpty(accountCode)) {
                queryWrapper.like(TblThirdPartyAccount::getAccountCode, accountCode.toString());
            }

            Object accountName = params.get("accountName");
            if (accountName != null && !StringUtils.isEmpty(accountName)) {
                queryWrapper.like(TblThirdPartyAccount::getAccountName, accountName.toString());
            }

            // 查询列表
            List<TblThirdPartyAccount> list = tblThirdPartyAccountMapper.selectList(queryWrapper);

            // PageInfo格式
            com.github.pagehelper.PageInfo<TblThirdPartyAccount> pageInfo =
                new com.github.pagehelper.PageInfo<>(list);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", list);
            result.put("totalRecord", pageInfo.getTotal());
            result.put("pageNo", pageNum);
            result.put("pageSize", pageSize);

            return JsonBean.success(result);

        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}
