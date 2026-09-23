package com.huabo.audit.oracle.service;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.dto.TblYqnsFundAuditProjectDto;
import com.huabo.audit.oracle.entity.TblYqnsFundAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsFundtb;
import com.huabo.audit.oracle.vo.SjdwjdVo;

/**
 * @program: springboot-hbyunMonitor
 * @description:
 * @author: WangZhenDong
 * @create: 2023-10-15 00:07
 **/
public interface FundAuditProjectService {
    /**
     * 新增/更新
     * @param param
     * @return
     */
    JsonBean saveOrUpdate(TblYqnsFundAuditProjectDto param);

    /**
     * 删除
     * @param id
     * @return
     */
    JsonBean delete(Long id);

    /**
     * 根据id查询记录
     * @param id
     * @return
     */
    JsonBean findById(Long id);

    /**
     * 查询列表
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
   	JsonBean saveOrupdate(String token,TblYqnsFundtb tb,String glids,String attids) throws Exception ;
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
   	 * 督导分工-查询列表
   	 * @param token
   	 * @param pageNumber
   	 * @param pageSize
   	 * @param vo
   	 * @return
   	 * @throws Exception
   	 */
   	JsonBean findAlcwddfglList(String token, Integer pageNumber, Integer pageSize, SjdwjdVo vo) throws Exception;
   	
   	
   	/**
   	 * 分配督导人员
   	 * @param token
   	 * @param ids
   	 * @param zyksryids
   	 * @param names
   	 * @return
   	 * @throws Exception
   	 */
   	JsonBean fpzyksry(String token, String ids,String zyksryids,String names) throws Exception;
   	
   	
   	/**
   	 * 实施方案选择财务安排表内容
   	 * @param pageNumber
   	 * @param pageSize
   	 * @param name
   	 * @return
   	 */
   	JsonBean xzfindList(String token,Integer pageNumber, Integer pageSize, String name)throws Exception  ;
   	
	/**
   	 * 项目组人员上报填报列表
   	 * @param token
   	 * @param pageNumber
   	 * @param pageSize
   	 * @param vo
   	 * @return
   	 * @throws Exception
   	 */
   	JsonBean getswsbList(String token, Integer pageNumber, Integer pageSize, SjdwjdVo vo) throws Exception ;
   	
   	/**
   	 * 批量下发助审人员
   	 * @param token
   	 * @param ids
   	 * @param zyksryids
   	 * @param names
   	 * @return
   	 * @throws Exception
   	 */
   	JsonBean fpkzsrys(String token, String ids,String zyksryids,String names) throws Exception ;
   	
   	
   	JsonBean outFileList(String token, String tbid) throws Exception;
   	
   	JsonBean deleteattid(String token, String attid) throws Exception;
   	
   	
   	JsonBean xmzsb(String token, String ids) throws Exception;
   	
   	JsonBean xmzsbth(String token, String ids) throws Exception;
   	
   	List<TblYqnsFundAuditProjectEntity>  findbytbidgetlist(String token, String tbid, List<String> idList) throws Exception;

	JsonBean fpslkry(String ids, String fpslkryid, String fpslkryname) throws Exception;
}
