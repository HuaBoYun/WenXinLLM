package com.huabo.etl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.etl.domain.KettleJob;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 作业调度Mapper接口
 *
 * @author zhibo.cao
 * @date 2021-07-22
 */
public interface KettleJobMapper  extends BaseMapper<KettleJob> {
    /**
     * 查询作业调度
     *
     * @param id 作业调度ID
     * @return 作业调度
     */
    public KettleJob selectKettleJobById(Long id);

    /**
     * 查询作业调度列表
     *
     * @param kettleJob 作业调度
     * @param roleKeys
     * @return 作业调度集合
     */
    public List<KettleJob> selectKettleJobList(@Param("kettleJob") KettleJob kettleJob);

    /**
     * 修改作业调度
     *
     * @param kettleJob 作业调度
     * @return 结果
     */
    public int updateKettleJob(KettleJob kettleJob);

    /**
     * 删除作业调度
     *
     * @param id 作业调度ID
     * @return 结果
     */
    public int deleteKettleJobById(Long id);

    /**
     * 批量删除作业调度
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteKettleJobByIds(String[] ids);

    int selectJobByNameAndRepoId(@Param("jobName") String jobName, @Param("jobRepositoryId") Long jobRepositoryId);

    List<String> queryJobLog(String jobName);

    Long checkQuartzExist(String checkStr);

    List<KettleJob> selectKettleJobByName(@Param("jobName") String jobName);
}
