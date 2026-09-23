package com.huabo.etl.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huabo.etl.domain.XRepository;
import com.huabo.etl.mapper.XRepositoryMapper;
import com.huabo.etl.repo.RepoTree;
import com.huabo.etl.repo.RepositoryTree;
import com.huabo.etl.repo.XRepoManager;
import com.huabo.etl.service.IXRepositoryService;
import com.huabo.etl.utils.DateUtils;
import com.huabo.etl.utils.KettleUtil;
import org.apache.commons.collections.CollectionUtils;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.filerep.KettleFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 资源库Service业务层处理
 *
 * @author zhibo.cao
 * @date 2021-07-12
 */
@Service
public class XRepositoryServiceImpl implements IXRepositoryService {
    @Autowired
    private XRepositoryMapper xRepositoryMapper;

    /**
     * 查询资源库
     *
     * @param id 资源库ID
     * @return 资源库
     */
    @Override
    public XRepository selectXRepositoryById(Long id) {
        return xRepositoryMapper.selectXRepositoryById(id);
    }

    /**
     * 查询资源库列表
     *
     * @param xRepository 资源库
     * @return 资源库
     */
    @Override
    public List<XRepository> selectXRepositoryList(XRepository xRepository) {
        return xRepositoryMapper.selectXRepositoryList(xRepository);
    }

    /**
     * 新增资源库
     *
     * @param xRepository 资源库
     * @return 结果
     */
    @Override
    public int insertXRepository(XRepository xRepository) {
        xRepository.setType("File");
        return xRepositoryMapper.insert(xRepository);
    }

    /**
     * 修改资源库
     *
     * @param xRepository 资源库
     * @return 结果
     */
    @Override
    public int updateXRepository(XRepository xRepository) {
        xRepository.setUpdateTime(DateUtils.getNowDate());
        return xRepositoryMapper.updateXRepository(xRepository);
    }

    /**
     * 删除资源库对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteXRepositoryByIds(List<Long> ids) {
        List<XRepository> xRepositories = xRepositoryMapper.selectBatchIds(ids);
        if (CollUtil.isNotEmpty(xRepositories)) {
            xRepositories.forEach(xRepository -> {
                FileUtil.del(xRepository.getBaseDir());
            });
        }
        return xRepositoryMapper.deleteBatchIds(ids);
    }

    /**
     * 删除资源库信息
     *
     * @param id 资源库ID
     * @return 结果
     */
    @Override
    public int deleteXRepositoryById(Long id) {
        return xRepositoryMapper.deleteById(id);
    }

    @Override
    public List<RepoTree> selectRepoRoot(XRepository repository) {
        List<XRepository> repositoryList = xRepositoryMapper.selectXRepositoryList(repository);
        List<RepoTree> ztrees = initZtree2(repositoryList);
        return ztrees;
    }

    /**
     * 通过名称查询
     *
     * @param repoName
     * @return
     */
    @Override
    public XRepository selectByName(String repoName) {
        //1. 构建动态查询条件
        LambdaQueryWrapper<XRepository> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(XRepository::getRepoName, repoName);
        return xRepositoryMapper.selectOne(queryWrapper);
    }

    @Override
    public List<RepoTree> selectRepoTree(Long id, String type) {
        XRepository xrs = xRepositoryMapper.selectXRepositoryById(id);
        List<RepositoryTree> repositoryTrees = getRepoTress(xrs, type);
        List<RepositoryTree> subTrees = new ArrayList<>();
        String pId = String.valueOf(xrs.getId());
//        try
//        {

//            repositoryTrees.forEach(item -> {
//                if (item.getParent().equals(pId)) {
//                    if (item.isLasted()) {
//                        if (!StringUtils.isEmpty(type)) {
//                            if (item.getType().indexOf(type) != -1) {
//                                subTrees.add(item);
//                            }
//                        } else {
//                            subTrees.add(item);
//                        }
//
//                    } else {
//                        subTrees.add(item);
//                    }
//                }});
//        }catch (Exception e)
//        {
//            StringWriter sw = new StringWriter();
//            e.printStackTrace(new PrintWriter(sw));
//            //throw new UserDefinedException(BaseResultConstant.UNKNOW_EXCEPTION, sw.toString().substring(0, 800));
//        }

        List<RepoTree> ztrees = initZtree(repositoryTrees, String.valueOf(id));
        return ztrees;
    }


    public List<RepoTree> initZtree(List<RepositoryTree> repositoryList, String parentId) {

        List<RepoTree> ztrees = new ArrayList<RepoTree>();
        for (RepositoryTree rt : repositoryList) {
            if (rt.getId().equals(parentId) || rt.getText().equals("/")) {
                continue;
            }
            RepoTree ztree = new RepoTree();
            ztree.setId(rt.getId());
            ztree.setpId(rt.getParent());
            ztree.setName(rt.getText());
            ztree.setTitle(rt.getPath());
            ztrees.add(ztree);
        }
        return ztrees;
    }

    public List<RepoTree> initZtree2(List<XRepository> repositoryList) {

        List<RepoTree> ztrees = new ArrayList<RepoTree>();
        for (XRepository rt : repositoryList) {
            RepoTree ztree = new RepoTree();
            ztree.setId(String.valueOf(rt.getId()));
            ztree.setpId(" ");
            ztree.setName(rt.getRepoName());
            ztree.setTitle(rt.getBaseDir());
            ztrees.add(ztree);
        }
        return ztrees;
    }

    private List<RepositoryTree> getRepoTress(XRepository xr, String jobortrans) {
        List<RepositoryTree> repositoryTrees = new ArrayList<>();
        List<XRepository> xRepositoryList = xRepositoryMapper.selectXRepositoryList(xr);

        if (!CollectionUtils.isEmpty(xRepositoryList)) {
            xRepositoryList.forEach(item -> {
                List<RepositoryTree> tmpRepositoryList = new ArrayList<>();
                String type = item.getType();

                if (type.equalsIgnoreCase("File")) {
                    // 文件库
                    String baseDir = item.getBaseDir();

                    try {
                        KettleFileRepository repository = (KettleFileRepository) KettleUtil.
                                conFileRep(String.valueOf(item.getId()), item.getRepoName(), baseDir);
                        XRepoManager.getAllDirectoryTreeList(String.valueOf(item.getId()), repository, "/", tmpRepositoryList, jobortrans);
                        if (tmpRepositoryList.size() > 0) {
                            RepositoryDirectoryInterface rDirectory = repository.loadRepositoryDirectoryTree().findDirectory("/");
                            RepositoryTree repositoryTree = new RepositoryTree();
                            repositoryTree.setParent(String.valueOf(item.getId()));
                            repositoryTree.setId(item.getRepoId() + "@" + rDirectory.getObjectId().toString());
                            //repositoryTree.setId(String.valueOf(item.getId()));

                            repositoryTree.setText(rDirectory.getName().equals("\\/") ? "基础路径" : rDirectory.getName());
                            repositoryTree.setLasted(false);
                            repositoryTree.setType("tree");
                            repositoryTree.setPath("file");
                            tmpRepositoryList.add(repositoryTree);
                        }

                    } catch (KettleException e) {
                        StringWriter sw = new StringWriter();
                        e.printStackTrace(new PrintWriter(sw));
                        // throw new UserDefinedException(BaseResultConstant.UNKNOW_EXCEPTION, sw.toString().substring(0, 800));
                    }
                }


//                RepositoryTree repositoryTree;
//                repositoryTree = new RepositoryTree();
//                repositoryTree.setParent("99");
//                repositoryTree.setId(String.valueOf(item.getId()));
//                repositoryTree.setText(item.getRepoName());
//                repositoryTree.setLasted(false);
//                repositoryTree.setType(type);
//                repositoryTree.setPath("repo");
//                tmpRepositoryList.add(repositoryTree);
                repositoryTrees.addAll(tmpRepositoryList);
            });
        }

        return repositoryTrees;
    }

}
