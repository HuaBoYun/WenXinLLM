package com.huabo.audit.oracle.service;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsPaper;
import com.huabo.audit.oracle.entity.TblYqnsPaperPx;
import com.huabo.audit.oracle.vo.XmdqVo;

/**
* 描述: Service
*/
public interface TblYqnsPaperService   {
   
	/**
	 * 保存或修改
	 * @param token
	 * @param ry
	 * @return
	 * @throws Exception
	 */
	JsonBean saveOrupdate(String token,TblYqnsPaper ry,String attids) throws Exception ;
	/**
	 * 查询详情
	 * @param token
	 * @param ryid
	 */
	JsonBean findByid(String token, BigDecimal ryid) throws Exception;
 
	/**
	 * 查询附件
	 * @param token
	 * @param ryid
	 */
	JsonBean findattlistByid(String token, BigDecimal ryid) throws Exception;
 
	
	
	/**
	 * 查询列表 
	 * @param token
	 * @param pageNumber
	 * @param pageSize
	 * @param XmdqVo
	 * @return
	 * @throws Exception
	 */
	JsonBean findAllList(String token, Integer pageNumber, Integer pageSize,XmdqVo vo) throws Exception;
	
	
	/**
	 * 删除
	 * @param token
	 * @param ryid
	 */
	JsonBean deleteone(String token, BigDecimal ryid) throws Exception;
	
	/**
	 * 删除附件
	 * @param token
	 * @param ryid
	 */
	JsonBean deleteatt(String token, String attid) throws Exception;
	
	
	/**
	 * 上报
	 * @param token
	 * @param ryid
	 */
	JsonBean lwsb(String token,String ryid) throws Exception;
	
	/**
	 * 退回
	 * @param token
	 * @param ryid
	 */
	JsonBean thlw(String token,String ryid) throws Exception;
	
	/**
	 * 台账列表
	 * @param token
	 * @param pageNumber
	 * @param pageSize
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	JsonBean findAlltzList(String token, Integer pageNumber, Integer pageSize, XmdqVo vo) throws Exception;
	
	
	/**
	 * 论文排序修改
	 * @param token
	 * @param ryid
	 * @param code
	 * @return
	 * @throws Exception
	 */
	JsonBean lwpxxg(String token, BigDecimal ryid,Integer code) throws Exception;
	
	/**
	 * 根据填报id查询论文内容
	 * @param token
	 * @param tbid
	 * @return
	 * @throws Exception
	 */
	JsonBean findbytbnr(String token, BigDecimal tbid) throws Exception;
	
	
	/**
	 * 选择论文上报内容
	 * @param token
	 * @param pageNumber
	 * @param pageSize
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	JsonBean findAllxztbList(String token, Integer pageNumber, Integer pageSize, TblYqnsPaper vo) throws Exception;
	
	
	/**
	 * 根据论文上报id查询评委评分
	 * @param token
	 * @param perid
	 * @return
	 * @throws Exception
	 */
	JsonBean findbyfslist(String token, BigDecimal perid) throws Exception;
	
	
	/**
	 * 论文上报-保存评委评分
	 * @param token
	 * @param ry
	 * @param attids
	 * @return
	 * @throws Exception
	 */
	JsonBean fssaveOrupdate(String token, TblYqnsPaperPx ry) throws Exception;
	
	/**
	 * 论文汇总排名表
	 * @param token
	 * @param pageNumber
	 * @param pageSize
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	JsonBean findAllzpxList(String token, Integer pageNumber, Integer pageSize, XmdqVo vo) throws Exception;
	
	/**
	 * 根据评分规则获取获奖名称
	 * @param token
	 * @return
	 * @throws Exception
	 */
	JsonBean selectByListmc(String token) throws Exception;
	
	/**
	 * 台账导出
	 * @param token
	 * @param pageNumber
	 * @param pageSize
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	List<TblYqnsPaper> findAllexportList(String token, Integer pageNumber, Integer pageSize, XmdqVo vo) throws Exception;
	
	
	/**
	 * 验证评委是否打分完成
	 * @param token
	 * @param perid
	 * @return
	 * @throws Exception
	 */
	JsonBean yzpf(String token, BigDecimal perid) throws Exception;
	
	
	/**
	 * 删除关联论文
	 * @param token
	 * @param perid
	 * @return
	 * @throws Exception
	 */
	JsonBean deletebyglid(String token, BigDecimal perid) throws Exception ;
}
