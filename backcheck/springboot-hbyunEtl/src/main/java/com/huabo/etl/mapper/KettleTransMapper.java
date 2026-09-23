package com.huabo.etl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.etl.domain.KettleTrans;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 转换Mapper接口
 *
 * @author zhibo.cao
 * @date 2022-12-01
 */
public interface KettleTransMapper  extends BaseMapper<KettleTrans> {
    /**
     * 查询转换
     *
     * @param id 转换ID
     * @return 转换
     */
    public KettleTrans selectKettleTransById(Long id);

    /**
     * 查询转换列表
     *
     * @param kettleTrans 转换
     * @return 转换集合
     */
    public List<KettleTrans> selectKettleTransList(@Param("KettleTrans") KettleTrans kettleTrans);


    /**
     * 修改转换
     *
     * @param kettleTrans 转换
     * @return 结果
     */
    public int updateKettleTrans(KettleTrans kettleTrans);

    /**
     * 删除转换
     *
     * @param id 转换ID
     * @return 结果
     */
    public int deleteKettleTransById(Long id);

    /**
     * 批量删除转换
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteKettleTransByIds(String[] ids);

    /**
     * 通过名称查询
     * @param transName
     * @return
     */
    int selectKettleTransByTransName(String transName);

    List<String> queryTransLog(String transName);

    Long checkQuartzExist(String checkStr);

    List<KettleTrans> selectKettleTransByName(@Param("transName") String transName);
}
