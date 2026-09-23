package com.huabo.etl.service;

import com.huabo.etl.domain.KettleTrans;
import com.huabo.etl.utils.AjaxResult;

import java.util.List;
import java.util.Map;

/**
 * 转换Service接口
 *
 * @author zhibo.cao
 * @date 2022-12-01
 */
public interface IKettleTransService {
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
    public List<KettleTrans> selectKettleTransList(KettleTrans kettleTrans);

    /**
     * 新增转换
     *
     * @param kettleTrans 转换
     * @return 结果
     */
    public AjaxResult insertKettleTrans(KettleTrans kettleTrans);

    /**
     * 修改转换
     *
     * @param kettleTrans 转换
     * @return 结果
     */
    public int updateKettleTrans(KettleTrans kettleTrans);

    /**
     * 批量删除转换
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteKettleTransByIds(List<Long> ids);

    /**
     * 删除转换信息
     *
     * @param id 转换ID
     * @return 结果
     */
    public int deleteKettleTransById(Long id);

    /**
     * @param trans :
     * @Description:立即执行一次转换
     * @Author: Kone.wang
     * @Date: 2021/7/15 14:31
     * @return: void
     **/
    AjaxResult runOne(KettleTrans trans);


    /**
     * 查询转换日志
     * @param trans
     * @return
     */
    List<String> queryTransLog(KettleTrans trans);


    /**
     * 加入定时任务
     * @param id
     * @param transName
     * @return
     */
    public AjaxResult runStopTransQuartz(String id, String transName);

    /**
     * 检查定时任务是否已经存在
     * @param checkStr
     * @return
     */
    Long checkQuartzExist(String checkStr);

    /**
     * 定时任务启动，不用异步
     * @param valueOf
     */
    void runTransRightNow(Long valueOf);

    /**
     * 通过名称启动
     * @param trans
     * @return
     */
    AjaxResult runByName(KettleTrans trans);

    /**
     * 统计
     * @return
     */
    Map<String, Object> count();

    /**
     * 通过名称查询转换
     * @param tranName
     * @return
     */
    KettleTrans selectKettleTransByName(String tranName);

    List<KettleTrans> selectKettleTransByRepoIds(List<Long> ids);
}
