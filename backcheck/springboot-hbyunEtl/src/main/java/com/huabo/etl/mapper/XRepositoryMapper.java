package com.huabo.etl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.etl.domain.XRepository;

import java.util.List;

/**
 * 资源库Mapper接口
 *
 * @author zhibo.cao
 * @date 2021-07-12
 */
public interface XRepositoryMapper extends BaseMapper<XRepository> {
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
     * 修改资源库
     *
     * @param xRepository 资源库
     * @return 结果
     */
    public int updateXRepository(XRepository xRepository);

    /**
     * 删除资源库
     *
     * @param id 资源库ID
     * @return 结果
     */
    public int deleteXRepositoryById(Long id);

    /**
     * 批量删除资源库
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteXRepositoryByIds(String[] ids);

    /**
     * @param id:
     * @Description:软删除
     * @Author: Kone.wang
     * @Date: 14:07
     * @return: int
     **/
    int updateIsDel(Long id);

    /**
     * @param ids:
     * @Description:批量软删除
     * @Author: Kone.wang
     * @Date: 2021/7/19 14:07
     * @return: int
     **/
    public int updateIsDelBatch(String[] ids);
}
