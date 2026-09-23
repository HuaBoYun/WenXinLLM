package com.huabo.audit.service;

import java.util.List;

import org.activiti.bpmn.model.SequenceFlow;

public interface ProcessService {

	List<String> getButtonsForTransition(String tId) throws Exception;


}
