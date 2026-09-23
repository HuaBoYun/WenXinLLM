package com.huabo.audit.oracle.service;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.dto.TblYqnsEnginAuditProjectDto;
import com.huabo.audit.oracle.entity.TblYqnsEnginAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsEngintb;
import com.huabo.audit.oracle.vo.SjdwjdVo;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-14 10:41
 **/
public interface EnginAuditProjectService {
    /**
     * 新增/更新
     * @param param
     * @return
     */
    JsonBean saveOrUpdate(TblYqnsEnginAuditProjectDto param);

    /**
     * 删除
     * @param id
     * @return
     */
    JsonBean delete(Long id);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    JsonBean findById(Long id);

    /**
     * 查询工程审计项目列表
     * @param pageNumber
     * @param pageSize
     * @param name
     * @return
     */
    JsonBean findList(Integer pageNumber, Integer pageSize, String name);
    
    
    
   	/**
   	 * 批次 保存或修改
   	 * @param token
   	 * @param xmqd
   	 * @return
   	 * @throws Exception
   	 */
   	JsonBean saveOrupdate(String token,TblYqnsEngintb tb,String glids,String attids) throws Exception ;
   	/**
   	 *批次 查询详情
   	 * @param token
   	 * @param xmdqid
   	 */
   	JsonBean findByid(String token, BigDecimal jdid) throws Exception;
     
    
   	
   	
   	/**
   	 *批次 查询列表 
   	 * @param token
   	 * @param pageNumber
   	 * @param pageSize
   	 * @param tBlNbsjSheetVo
   	 * @return
   	 * @throws Exception
   	 */
   	JsonBean findAllList(String token, Integer pageNumber, Integer pageSize,SjdwjdVo vo) throws Exception;
   	
   	
   	/**
   	 *批次 删除
   	 * @param token
   	 * @param sheetid
   	 */
   	JsonBean deleteone(String token, BigDecimal jdid) throws Exception;
   	
   	/**
   	 * 下发项目组人员
   	 * @param token
   	 * @param ids
   	 * @param zyksryids
   	 * @param names
   	 * @return
   	 * @throws Exception
   	 */
   	JsonBean xfxmzry(String token, String ids,String zyksryids,String names) throws Exception;
   	
   	/**
   	 * 下发专业科室人员
   	 * @param token
   	 * @param ids
   	 * @param zyksryids
   	 * @param names
   	 * @return
   	 * @throws Exception
   	 */
   	JsonBean xfzyksry(String token, String ids,String zyksryids,String names) throws Exception;
   	
   	
   	
   	/**
   	 *督导分工查询列表 
   	 * @param token
   	 * @param pageNumber
   	 * @param pageSize
   	 * @param tBlNbsjSheetVo
   	 * @return
   	 * @throws Exception
   	 */
   	JsonBean findAllddfgList(String token, Integer pageNumber, Integer pageSize,SjdwjdVo vo) throws Exception;
   	
   	
   	/**
   	 * 下发专业科室督导人员
   	 * @param token
   	 * @param ids
   	 * @param zyksryids
   	 * @param names
   	 * @return
   	 * @throws Exception
   	 */
   	JsonBean fpzyksry(String token, String ids,String zyksryids,String names) throws Exception;
   	
   	
   	
   	/**
   	 * 实施方案-选择项目安排表内容
   	 * @param token
   	 * @param pageNumber
   	 * @param pageSize
   	 * @param name
   	 * @return
   	 * @throws Exception
   	 */
   	JsonBean xzfindList(String token,Integer pageNumber, Integer pageSize, String name) throws Exception;
   	
   	
   	/**
   	 * 项目组人员上报填报列表
   	 * @param token
   	 * @param pageNumber
   	 * @param pageSize
   	 * @param vo
   	 * @return
   	 * @throws Exception
   	 */
   	JsonBean getgcsbList(String token, Integer pageNumber, Integer pageSize, SjdwjdVo vo) throws Exception;
   	
   	/**
   	 * 批量下发人员助审人员
   	 * @param token
   	 * @param ids
   	 * @param zyksryids
   	 * @param names
   	 * @return
   	 * @throws Exception
   	 */
   	JsonBean fpkzsrys(String token, String ids,String zyksryids,String names) throws Exception;
   	
   	JsonBean outFileList(String token, String tbid) throws Exception;
   	
   	JsonBean deleteattid(String token, String attid) throws Exception;
   	
	
   	JsonBean xmzsb(String token, String ids) throws Exception;
   	
   	JsonBean xmzsbth(String token, String ids) throws Exception;
   	
   	List<TblYqnsEnginAuditProjectEntity>  findbytbidgetlist(String token, String tbid, List<String> idList) throws Exception;

	JsonBean fpslkry(String ids, String fpslkryid, String fpslkryname) throws Exception;
   	
}
