/**
 * 人力资源管理API接口
 * 所有接口走 /monitor 前缀，由网关转发到后端服务
 */
import request from '@/utils/request'

// ==================== 员工管理 ====================
export const employeeApi = {
  // 获取员工列表
  getEmployeeList(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/employee/list',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 获取员工详情
  getEmployeeDetail(id) {
    return request({
      url: `/monitor/v1/enterprise/hr/employee/${id}`,
      method: 'get'
    })
  },

  // 新增员工
  createEmployee(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/employee',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 更新员工信息
  updateEmployee(id, data) {
    return request({
      url: `/monitor/v1/enterprise/hr/employee/${id}`,
      method: 'put',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 删除员工
  deleteEmployee(id) {
    return request({
      url: `/monitor/v1/enterprise/hr/employee/${id}`,
      method: 'delete'
    })
  },

  // 员工调岗
  transferEmployee(id, data) {
    return request({
      url: `/monitor/v1/enterprise/hr/employee/${id}/transfer`,
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 批量导入员工（文件上传）
  batchImportEmployee(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/employee/batch-import',
      method: 'post',
      headers: { 'Content-Type': 'multipart/form-data' },
      data
    })
  },

  // 导出员工数据
  exportEmployee(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/employee/export',
      method: 'get',
      params,
      responseType: 'blob'
    })
  }
}

// ==================== 组织架构管理 ====================
export const organizationApi = {
  // 获取部门树列表
  getDepartmentList(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/department/list',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 获取组织架构树
  getOrganizationTree(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/organization/tree',
      method: 'get',
      params
    })
  },

  // 获取部门详情
  getDepartmentDetail(id) {
    return request({
      url: `/monitor/v1/enterprise/hr/organization/department/${id}`,
      method: 'get'
    })
  },

  // 新增部门
  createDepartment(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/organization/department',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 更新部门信息
  updateDepartment(id, data) {
    return request({
      url: `/monitor/v1/enterprise/hr/organization/department/${id}`,
      method: 'put',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 删除部门
  deleteDepartment(id) {
    return request({
      url: `/monitor/v1/enterprise/hr/organization/department/${id}`,
      method: 'delete'
    })
  }
}

// ==================== 薪酬福利管理 ====================
export const compensationApi = {
  // 获取薪酬列表
  getCompensationList(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/compensation/list',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 获取薪酬详情
  getCompensationDetail(id) {
    return request({
      url: `/monitor/v1/enterprise/hr/compensation/${id}`,
      method: 'get'
    })
  },

  // 新增薪酬方案
  addCompensation(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/compensation',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 更新薪酬
  updateCompensation(id, data) {
    return request({
      url: `/monitor/v1/enterprise/hr/compensation/${id}`,
      method: 'put',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 删除薪酬记录
  deleteCompensation(id) {
    return request({
      url: `/monitor/v1/enterprise/hr/compensation/${id}`,
      method: 'delete'
    })
  },

  // 调薪
  adjustSalary(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/compensation/adjust',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 获取薪酬方案列表（兼容旧接口）
  getCompensationSchemeList(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/compensation/scheme/list',
      method: 'get',
      params
    })
  },

  // 获取员工薪酬记录（兼容旧接口）
  getEmployeeSalaryRecord(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/compensation/salary/record',
      method: 'get',
      params
    })
  },

  // 薪酬计算
  calculateSalary(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/compensation/salary/calculate',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 福利管理
  getBenefitsList(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/compensation/benefits/list',
      method: 'get',
      params
    })
  },

  // 导出薪酬数据
  exportCompensation(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/compensation/export',
      method: 'get',
      params,
      responseType: 'blob'
    })
  }
}

// ==================== 绩效考核管理 ====================
export const performanceApi = {
  // 获取绩效考核列表
  getPerformanceList(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/performance/list',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 获取绩效详情
  getPerformanceDetail(id) {
    return request({
      url: `/monitor/v1/enterprise/hr/performance/evaluation/${id}`,
      method: 'get'
    })
  },

  // 创建绩效考核
  createPerformanceEvaluation(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/performance/evaluation',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 更新绩效考核
  updatePerformanceEvaluation(id, data) {
    return request({
      url: `/monitor/v1/enterprise/hr/performance/evaluation/${id}`,
      method: 'put',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 删除绩效考核
  deletePerformanceEvaluation(id) {
    return request({
      url: `/monitor/v1/enterprise/hr/performance/evaluation/${id}`,
      method: 'delete'
    })
  },

  // 提交绩效评分
  submitPerformanceScore(id, data) {
    return request({
      url: `/monitor/v1/enterprise/hr/performance/evaluation/${id}/score`,
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 获取绩效统计
  getPerformanceStatistics(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/performance/statistics',
      method: 'get',
      params
    })
  },

  // 导出绩效考核数据
  exportPerformance(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/performance/export',
      method: 'get',
      params,
      responseType: 'blob'
    })
  }
}

// ==================== 培训发展管理 ====================
export const trainingApi = {
  // 获取培训列表
  getTrainingList(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/training/list',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 获取培训详情
  getTrainingDetail(id) {
    return request({
      url: `/monitor/v1/enterprise/hr/training/plan/${id}`,
      method: 'get'
    })
  },

  // 创建培训计划
  createTrainingPlan(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/training/plan',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 更新培训计划
  updateTrainingPlan(id, data) {
    return request({
      url: `/monitor/v1/enterprise/hr/training/plan/${id}`,
      method: 'put',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 删除培训计划
  deleteTrainingPlan(id) {
    return request({
      url: `/monitor/v1/enterprise/hr/training/plan/${id}`,
      method: 'delete'
    })
  },

  // 获取培训记录
  getTrainingRecord(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/training/record',
      method: 'get',
      params
    })
  },

  // 培训报名
  enrollTraining(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/training/enroll',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 培训评估
  evaluateTraining(id, data) {
    return request({
      url: `/monitor/v1/enterprise/hr/training/${id}/evaluate`,
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 导出培训数据
  exportTraining(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/training/export',
      method: 'get',
      params,
      responseType: 'blob'
    })
  }
}

// ==================== 招聘管理 ====================
export const recruitmentApi = {
  // 获取招聘列表
  getRecruitmentList(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/recruitment/list',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 获取招聘详情
  getRecruitmentDetail(id) {
    return request({
      url: `/monitor/v1/enterprise/hr/recruitment/${id}`,
      method: 'get'
    })
  },

  // 发布招聘职位
  publishRecruitmentPosition(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/recruitment',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 更新招聘职位
  updateRecruitment(id, data) {
    return request({
      url: `/monitor/v1/enterprise/hr/recruitment/${id}`,
      method: 'put',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 删除招聘职位
  deleteRecruitment(id) {
    return request({
      url: `/monitor/v1/enterprise/hr/recruitment/${id}`,
      method: 'delete'
    })
  },

  // 获取应聘者列表
  getCandidateList(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/recruitment/candidate/list',
      method: 'get',
      params
    })
  },

  // 面试安排
  scheduleInterview(data) {
    return request({
      url: '/monitor/v1/enterprise/hr/recruitment/interview/schedule',
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 录用决定
  makeHiringDecision(id, data) {
    return request({
      url: `/monitor/v1/enterprise/hr/recruitment/candidate/${id}/hire`,
      method: 'post',
      headers: { 'Content-Type': 'application/json;charset=UTF-8' },
      data
    })
  },

  // 导出招聘数据
  exportRecruitment(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/recruitment/export',
      method: 'get',
      params,
      responseType: 'blob'
    })
  }
}

// ==================== 人力资源统计 ====================
export const hrStatisticsApi = {
  // 获取人力资源概览统计
  getHROverviewStats() {
    return request({
      url: '/monitor/v1/enterprise/hr/statistics/overview',
      method: 'get'
    })
  },

  // 获取员工分布统计
  getEmployeeDistributionStats(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/statistics/employee-distribution',
      method: 'get',
      params
    })
  },

  // 获取薪酬统计
  getSalaryStatistics(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/statistics/salary',
      method: 'get',
      params
    })
  },

  // 获取培训统计
  getTrainingStatistics(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/statistics/training',
      method: 'get',
      params
    })
  },

  // 获取绩效统计
  getPerformanceStatisticsData(params) {
    return request({
      url: '/monitor/v1/enterprise/hr/statistics/performance',
      method: 'get',
      params
    })
  }
}

export default {
  employeeApi,
  organizationApi,
  compensationApi,
  performanceApi,
  trainingApi,
  recruitmentApi,
  hrStatisticsApi
}
