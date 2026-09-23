package com.huabo.audit.service.impl;
 
import org.springframework.stereotype.Service;

import com.huabo.audit.config.DateBaseConfig;
import com.huabo.audit.oracle.entity.TblMyTask;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblMyTaskMapper;
import com.huabo.audit.service.TblMyTaskService;


import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TblMyTaskServiceImpl implements TblMyTaskService {

    @Resource
    private TblMyTaskMapper tblMyTaskMapper;
    
    @Resource
	public TblAttachmentMapper tblAttachmentMapper;
 

    @Override
    public void updateSetting(TblMyTask task) throws Exception {
        if (task.getId() == null) {
            this.tblMyTaskMapper.insertSelective(task);
        } else {
            this.tblMyTaskMapper.updateByPrimaryKeySelective(task);
        }
    }

//    @Override
//    public void updateMySqlSetting(TblMyTaskMySql task) throws Exception {
//        if (task.getId() == null) {
//            this.tblMyTaskMySqlMapper.insertSelective(task);
//        } else {
//            this.tblMyTaskMySqlMapper.updateByPrimaryKeySelective(task);
//        }
//    }

    @Override
    public List<TblMyTask> getByFromid(String processName) {
        return this.tblMyTaskMapper.getByFromid(processName);
    }

//    @Override
//    public List<TblMyTaskMySql> getByMySqlFromid(String processName) {
//        return this.tblMyTaskMySqlMapper.getByFromid(processName);
//    }

    @Override
    public List<TblMyTask> findByLendid(String lendid) {
        return this.tblMyTaskMapper.findByLendid(lendid);
    }

//    @Override
//    public List<TblMyTaskMySql> findByMySqlLendid(String lendid) {
//        return this.tblMyTaskMySqlMapper.findByLendid(lendid);
//    }

    @Override
    public TblMyTask findOndbyFrom(String fromid) {
        List<TblMyTask> list = this.tblMyTaskMapper.findOndbyFrom(fromid);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

//    @Override
//    public TblMyTaskMySql findOndbyMySqlFrom(String fromid) {
//        List<TblMyTaskMySql> list = this.tblMyTaskMySqlMapper.findOndbyFrom(fromid);
//        if (list != null && list.size() > 0) {
//            return list.get(0);
//        }
//        return null;
//    }


    @Override
    public void updateSetting(TblMyTask task,String attids) throws Exception {
        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            if (task.getId() == null) {
                this.tblMyTaskMapper.insertSelective(task);
                
                //附件
                Integer id = task.getId().intValue();
                if (attids != null && !"".equals(attids)) {
                    String[] ids = attids.split(",");
                    for (String attId : ids) {
                        this.tblMyTaskMapper.insertAttmentRelation(attId, id);
                    }
                }
                
            } else {
                this.tblMyTaskMapper.updateByPrimaryKeySelective(task);
                
                //附件
                Integer id = task.getId().intValue();
                this.tblMyTaskMapper.deleteAttmentRelationByBizId(id);
                if (attids != null && !"".equals(attids)) {
                    String[] ids = attids.split(",");
                    for (String attId : ids) {
                        this.tblMyTaskMapper.insertAttmentRelation(attId, id);
                    }
                }
                
            }
        } else {
            if (task.getId() == null) {
                this.tblMyTaskMapper.insertSelective(task);
            } else {
                this.tblMyTaskMapper.updateByPrimaryKeySelective(task);
            }
        }
    }

    @Override
    public void insertMyTaskSetting(TblMyTask task) throws Exception {
        if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            this.tblMyTaskMapper.insertMyTaskSetting(task);
        } else {
            this.tblMyTaskMapper.insertMyTaskSetting(task);
        }
    }

	@Override
	public List<TblMyTask> findByObj(String string, String string2, String string3, String string4) {
		List<TblMyTask> findByObj = tblMyTaskMapper.findByObj(string, string2, string3, string4);
		return findByObj;
	}

//	@Override
//	public Map<String, Object> delAprAttById(Integer attId) {
//		if(DateBaseConfig.DATABASETYPE.equals("Oracle")) {
//			Map<String, Object> resultMap = new HashMap<String, Object>(0);
//			try {
//				//删除关系
//				tblMyTaskMapper.deleteAttmentRelationByAttid(attId);
//				tblAttachmentMapper.deleteAttachmentByattId(attId);
//				resultMap.put("code", "1");
//				resultMap.put("msg", "删除成功！");
//				return resultMap;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return resultMap;
//		} else {
//			Map<String, Object> resultMap = new HashMap<String, Object>(0);
//			try {
//				//删除关系
//				tblMyTaskMapper.deleteAttmentRelationByAttid(attId);
//				tblAttachmentMapper.deleteAttachmentByattId(attId);
//				resultMap.put("code", "1");
//				resultMap.put("msg", "删除成功！");
//				return resultMap;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return resultMap;
//		}
//	}

//    @Override
//    public void insertMySqlMyTaskSetting(TblMyTaskMySql task) throws Exception {
//        this.tblMyTaskMySqlMapper.insertMyTaskSetting(task);
//    }
}
