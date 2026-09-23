package com.huabo.audit.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/*import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;*/
import org.springframework.stereotype.Service;

import com.huabo.audit.service.ProcessService;

@Service
public class ProcessServiceImpl implements ProcessService {

	/*@Resource
	private RepositoryService repositoryService;
	@Resource
	private RuntimeService runtimeService;
	@Resource 
	private TaskService taskService;*/

	@Override
	public List<String> getButtonsForTransition(String taskId) throws Exception {

		List<String> btnList = new ArrayList<String>(0);

		/*// 获取当前任务
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if(task == null){
			return null;
		}
		//获取当前模型
		BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
		// 获取当前节点
		FlowElement flowElement = bpmnModel.getFlowElement(task.getTaskDefinitionKey());
		// 这里不转也有方法拿到，我这是为了后人阅读方便
		UserTask userTask = (UserTask)flowElement;
		//获取节点出口线段
		List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
		List<String> btnList = new ArrayList<String>(0);
		for (SequenceFlow sequenceFlow : outgoingFlows) {
			btnList.add(sequenceFlow.getName());
		}*/
		
		return btnList;
	}

}
