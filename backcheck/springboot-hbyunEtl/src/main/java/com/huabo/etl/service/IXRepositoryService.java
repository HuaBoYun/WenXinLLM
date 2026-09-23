package com.huabo.etl.service;

import com.huabo.etl.domain.XRepository;
import com.huabo.etl.repo.RepoTree;

import java.util.List;

/**
 * 资源库Service接口
 *
 * @author zhibo.cao
 * @date 2021-07-12
 */
public interface IXRepositoryService {
    /**
     * 查询资源库
     *
     * @param id 资源库ID
     * @return 资源库
     */
    public XRepository selectXRepositoryById(Long id);

    /**
     * 查询资源库列表
     *
     * @param xRepository 资源库
     * @return 资源库集合
     */
    public List<XRepository> selectXRepositoryList(XRepository xRepository);

    /**
     * 新增资源库
     *
     * @param xRepository 资源库
     * @return 结果
     */
    public int insertXRepository(XRepository xRepository);

    /**
     * 修改资源库
     *
     * @param xRepository 资源库
     * @return 结果
     */
    public int updateXRepository(XRepository xRepository);

    /**
     * 批量删除资源库
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteXRepositoryByIds(List<Long> ids);

    /**
     * 删除资源库信息
     *
     * @param id 资源库ID
     * @return 结果
     */
    public int deleteXRepositoryById(Long id);

    List<RepoTree> selectRepoTree(Long id, String type);

    List<RepoTree> selectRepoRoot(XRepository repository);

    /**
     * 通过名称查询
     * @param repoName
     * @return
     */
    XRepository selectByName(String repoName);
}
