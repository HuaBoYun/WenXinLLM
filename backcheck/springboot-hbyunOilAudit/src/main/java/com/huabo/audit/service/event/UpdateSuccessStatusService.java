package com.huabo.audit.service.event;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.huabo.audit.oracle.entity.TblCirculation;
import com.huabo.audit.oracle.mapper.TblCirculationMapper;
import com.huabo.audit.service.CallbackUpdateService;
import com.huabo.audit.util.process.ProcessVariableEnum;

@Component
public class UpdateSuccessStatusService implements JavaDelegate{

	@Autowired
	private TblCirculationMapper cirmapper;
	@Autowired
	private CallbackUpdateService callbackUpdateService;
	@Override
	public void execute(DelegateExecution exection) {
		Object variable = exection.getVariable(ProcessVariableEnum.model.toString());
    	TblCirculation modle = JSON.parseObject(variable.toString(), new TypeReference<TblCirculation>(){});
		modle.setCystate(TblCirculation.STATE_TG);
		System.out.println("流程办理最终成功。。。。。。。。。。。。。。");
		this.cirmapper.updateCirculationInfoById(modle);
		this.callbackUpdateService.updateStatus(modle.getCyid(),TblCirculation.STATE_TG);
	}

}
