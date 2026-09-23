package com.huabo.financialdata.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.financialdata.entity.entity.ManageUserBook;
import com.huabo.financialdata.mapper.ManageUserBookMapper;
import com.huabo.financialdata.service.IManageUserBookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户账簿授权管理
 *
 * @author lee
 * @version 1.0.0
 **/
@Service
public class ManageUserBookServiceImpl extends ServiceImpl<ManageUserBookMapper, ManageUserBook> implements IManageUserBookService {

    @Resource
    private ManageUserBookMapper manageUserBookMapper;

    /**
     * 查询用户已关联授权的账套ID集合
     *
     * @param staffId 用户ID
     * @return 返回用户已授权的账套ID集合
     */
    @Override
    public List<String> getBookIdListByStaffId(BigDecimal staffId) {
        QueryWrapper<ManageUserBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("staffid", staffId);
        List<ManageUserBook> manageUserBookList = manageUserBookMapper.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(manageUserBookList)) {
            return null;
        }

        return manageUserBookList.stream().map(ManageUserBook::getBookid).collect(Collectors.toList());
    }

    /**
     * 更新用户和其授权的账套选择中状态为NULL（null-->未选择）
     *
     * @param staffId    用户ID
     * @param status     待更新的状态：null-->未选中 0-->选中
     */
    @Override
    public void updateStatusByStaffIdAndBookIdList(BigDecimal staffId, Integer status) {
        manageUserBookMapper.updateBookStatusNoCheck(staffId, status);
    }

    /**
     * 根据staffId和账套ID列表更新账套状态
     * @param staffId
     * @param bookIdList
     * @param status
     */
    @Override
    public void updateStatusByStaffIdAndBookIdListone(BigDecimal staffId, String bookIdList, Integer status) {
        manageUserBookMapper.updatestatusone(staffId, bookIdList, status);
    }


}
