package com.huabo.system.entity;

import lombok.Data;

@Data
public class ProcessView {
	//流程定义
	private String key;
	private String id;
	private String revision;
	private String version;
	private String category;
	private String deploymentId;
	private String resourceName;
	private String tenantId;
	private String historyLevel;
	private String diagramResourceName;
	private String hasStartFormKey;
	private String suspensionState;
	private String identityLinksInitialized;
	private String graphicalNotationDefined;
	
	//流程实例
	private String businessKey;
	private String processInstanceId;
	private String processDefinitionKey;
	private String processDefinitionName;
	private String description;
	private String eventName;
	private String ended;
	private String active;
	private String concurrent;
	private String eventScope;
	private String scope;
	
	
	
	private String processDefinitionId;
	private String processDefinitionVersion;
	private String startTime;
	private String endTime;
	private String durationInMillis;
	private String deleteReason;
	private String endActivityId;
	private String startUserId;
	private String startActivityId;
	private String superProcessInstanceId;
	private String name;
	private String localizedName;
	private String localizedDescription;
	private String queryVariables;
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDeploymentId() {
		return deploymentId;
	}
	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getHistoryLevel() {
		return historyLevel;
	}
	public void setHistoryLevel(String historyLevel) {
		this.historyLevel = historyLevel;
	}
	public String getDiagramResourceName() {
		return diagramResourceName;
	}
	public void setDiagramResourceName(String diagramResourceName) {
		this.diagramResourceName = diagramResourceName;
	}
	public String getHasStartFormKey() {
		return hasStartFormKey;
	}
	public void setHasStartFormKey(String hasStartFormKey) {
		this.hasStartFormKey = hasStartFormKey;
	}
	public String getSuspensionState() {
		return suspensionState;
	}
	public void setSuspensionState(String suspensionState) {
		this.suspensionState = suspensionState;
	}
	public String getIdentityLinksInitialized() {
		return identityLinksInitialized;
	}
	public void setIdentityLinksInitialized(String identityLinksInitialized) {
		this.identityLinksInitialized = identityLinksInitialized;
	}
	public String getGraphicalNotationDefined() {
		return graphicalNotationDefined;
	}
	public void setGraphicalNotationDefined(String graphicalNotationDefined) {
		this.graphicalNotationDefined = graphicalNotationDefined;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}
	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}
	public String getProcessDefinitionName() {
		return processDefinitionName;
	}
	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEnded() {
		return ended;
	}
	public void setEnded(String ended) {
		this.ended = ended;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getConcurrent() {
		return concurrent;
	}
	public void setConcurrent(String concurrent) {
		this.concurrent = concurrent;
	}
	public String getEventScope() {
		return eventScope;
	}
	public void setEventScope(String eventScope) {
		this.eventScope = eventScope;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public String getProcessDefinitionVersion() {
		return processDefinitionVersion;
	}
	public void setProcessDefinitionVersion(String processDefinitionVersion) {
		this.processDefinitionVersion = processDefinitionVersion;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDurationInMillis() {
		return durationInMillis;
	}
	public void setDurationInMillis(String durationInMillis) {
		this.durationInMillis = durationInMillis;
	}
	public String getDeleteReason() {
		return deleteReason;
	}
	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}
	public String getEndActivityId() {
		return endActivityId;
	}
	public void setEndActivityId(String endActivityId) {
		this.endActivityId = endActivityId;
	}
	public String getStartUserId() {
		return startUserId;
	}
	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}
	public String getStartActivityId() {
		return startActivityId;
	}
	public void setStartActivityId(String startActivityId) {
		this.startActivityId = startActivityId;
	}
	public String getSuperProcessInstanceId() {
		return superProcessInstanceId;
	}
	public void setSuperProcessInstanceId(String superProcessInstanceId) {
		this.superProcessInstanceId = superProcessInstanceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocalizedName() {
		return localizedName;
	}
	public void setLocalizedName(String localizedName) {
		this.localizedName = localizedName;
	}
	public String getLocalizedDescription() {
		return localizedDescription;
	}
	public void setLocalizedDescription(String localizedDescription) {
		this.localizedDescription = localizedDescription;
	}
	public String getQueryVariables() {
		return queryVariables;
	}
	public void setQueryVariables(String queryVariables) {
		this.queryVariables = queryVariables;
	}
	

}
