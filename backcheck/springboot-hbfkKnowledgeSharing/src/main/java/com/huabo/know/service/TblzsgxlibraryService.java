package com.huabo.know.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.know.entity.Tblzsgxlibrary;

public interface TblzsgxlibraryService {

	/**
	 * 自定义场景列表 查询
	 * @param param
	 * @return
	 */
	JsonBean getList(String token,String type,Tblzsgxlibrary library, Integer pageNumber, Integer pageSize) throws Exception;

	/**
	 * 自定义场景 新增/更新
	 * @param param
	 * @return
	 */
	JsonBean saveOrUpdate(String token,Tblzsgxlibrary libary,String attids) throws Exception;

	/**
	 * 自定义场景详情 查询
	 * @param id
	 * @return
	 */
	JsonBean findById(String token,Long id) throws Exception;

	/**
	 * 自定义场景 刪除
	 * @param id
	 */
	JsonBean delete(String token,Long id) throws Exception;
	/**
	 * 获取附件列表
	 * @param token
	 * @param id
	 * @return
	 * @throws Exception
	 */
	JsonBean findByattId(String token,Long id) throws Exception;
	
	
	/**
	 * 关联附件删除
	 * @param id
	 */
	JsonBean deleteattid(String token,Long attid) throws Exception;
	
	/**
	 * 根据类型查询统计汇总数量
	 * @param token
	 * @param type 1、效力位阶 2、专题分类 3、制定机关 4、时效性    5、法规类别/文件夹名称  6、公布年份
	 * @param lrtype 类别：1、法律知识 2、合同文本3、知识文库
	 * @return
	 * @throws Exception
	 */
	JsonBean getBytypelist(String token,String type,String lrtype) throws Exception;
	
	
	
	/**
	 *增加查询次数
	 * @param id
	 * @return
	 */
	JsonBean addcxcount(String token,Long id) throws Exception;
	
	/**
	 *增加下载次数
	 * @param id
	 * @return
	 */
	JsonBean addxzcount(String token,Long id) throws Exception;
	
}
