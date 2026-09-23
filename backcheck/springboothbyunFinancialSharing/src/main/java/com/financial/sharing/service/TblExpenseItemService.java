package com.financial.sharing.service;

import com.financial.sharing.entity.TblExpenseItem;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.TblExpenseItemQueryParam;
import com.financial.sharing.vo.param.TblExpenseItemSaveParam;

/**
 * 费用项目服务接口
 *
 * @author system
 * @since 2025-01-30
 */
public interface TblExpenseItemService {

    /**
     * 分页查询费用项目列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean<PageResult<TblExpenseItem>> getList(TblExpenseItemQueryParam param);

    /**
     * 根据ID查询费用项目详情
     *
     * @param itemId 项目ID
     * @return 项目详情
     */
    MyJsonBean getById(String itemId);

    /**
     * 保存或更新费用项目
     *
     * @param param 保存参数
     * @return 操作结果
     */
    MyJsonBean saveOrUpdate(TblExpenseItemSaveParam param);

    /**
     * 删除费用项目
     *
     * @param itemId 项目ID
     * @return 操作结果
     */
    MyJsonBean delete(String itemId);

    /**
     * 更新启用状态
     *
     * @param itemId 项目ID
     * @param isEnabled 启用状态
     * @return 操作结果
     */
    MyJsonBean updateStatus(String itemId, Integer isEnabled);

    /**
     * 获取树形结构
     *
     * @return 树形结构
     */
    MyJsonBean getTree();

    /**
     * 获取子节点
     *
     * @param parentId 父级ID
     * @return 子节点列表
     */
    MyJsonBean getChildren(String parentId);

    /**
     * 移动节点
     *
     * @param itemId 项目ID
     * @param newParentId 新父级ID
     * @return 操作结果
     */
    MyJsonBean move(String itemId, String newParentId);

    /**
     * 获取节点路径
     *
     * @param itemId 项目ID
     * @return 节点路径
     */
    MyJsonBean getPath(String itemId);
}
