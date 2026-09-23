import request from '@/utils/request'

// 获取财务版本信息分类
export const getCwbbxxType = () => {
  return request({
    url: '/finance/config/fversion/getParentList',
    method: 'get',
  })
}

// 获取财务版本信息
export const getCwbbxxList = (data) => {
  return request({
    url: '/finance/config/fversion/getList',
    method: 'get',
    params: data,
  })
}

// 新增财务版本信息
export const saveCwbbxx = (data) => {
  return request({
    url: '/finance/config/fversion/save',
    method: 'post',
    data,
  })
}

// 删除财务版本信息
export const deleteCwbbxx = (data) => {
  return request({
    url: '/finance/config/fversion/remove',
    method: 'GET',
    params: data,
  })
}

// 获取财务版本信息详情
export const getCwbbxxDetail = (data) => {
  return request({
    url: '/finance/config/fversion/detail',
    method: 'get',
    params: data,
  })
}

// 数据源列表
export const getDataSourceList = (data) => {
  return request({
    url: '/finance/config/fdataSource/getList',
    method: 'get',
    params: data,
  })
}

// 数据源详情
export const getDataSourceDetail = (data) => {
  return request({
    url: '/finance/config/fdataSource/detail',
    method: 'get',
    params: data,
  })
}

// 数据源新增
export const saveDataSource = (data) => {
  return request({
    url: '/finance/config/fdataSource/save',
    method: 'post',
    data,
  })
}

// 数据源删除
export const deleteDataSource = (data) => {
  return request({
    url: '/finance/config/fdataSource/remove',
    method: 'get',
    params: data,
  })
}

// 数据源测试连接
export const testDataSource = (data) => {
  return request({
    url: '/finance/config/fdataSource/testCon',
    method: 'post',
    data,
  })
}

// 采集方案列表
export const getCjfaList = (data) => {
  return request({
    url: '/finance/config/fplan/getList',
    method: 'get',
    params: data,
  })
}

// 采集方案详情
export const getCjfaDetail = (data) => {
  return request({
    url: '/finance/config/fplan/detail',
    method: 'get',
    params: data,
  })
}

// 采集方案新增
export const saveCjfa = (data) => {
  return request({
    url: '/finance/config/fplan/save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
    data
  })
}

// 采集方案删除
export const deleteCjfa = (data) => {
  return request({
    url: '/finance/config/fplan/remove',
    method: 'get',
    params: data,
  })
}
// 新增的时候，调详情接口获取sql基本信息
export const getCjsqlAddDetail = (data) => {
  return request({
    url: '/finance/config/fconfigSql/detail',
    method: 'get',
    params: data,
  })
}

// 采集sql详情
export const getCjsqlDetail = (data) => {
  return request({
    url: '/finance/config/fplanSql/detail',
    method: 'get',
    params: data,
  })
}

// 采集sql列表
export const getCjsqlList = (data) => {
  return request({
    url: '/finance/config/fplanSql/getList',
    method: 'get',
    params: data,
  })
}
// 采集sql新增
export const saveCjsql = (data) => {
  return request({
    url: '/finance/config/fplanSql/save',
    method: 'post',
    data,
  })
}

// 采集sql删除
export const deleteCjsql = (data) => {
  return request({
    url: '/finance/config/fplanSql/remove',
    method: 'post',
    data,
  })
}

// 采集sql测试连接
export const testCjsql = (data) => {
  return request({
    url: '/finance/config/fplanSql/testSql',
    method: 'post',
    data,
  })
}

// 采集sql执行
export const executeCjsql = (data) => {
  return request({
    url: '/finance/config/fplanSql/excuteSql',
    method: 'post',
    data,
  })
}

// 账簿管理列表
export const getZbglList = (data) => {
  return request({
    url: '/finance/book/bookInfo/getList',
    method: 'get',
    params: data,
  })
}

// 账簿管理详情
export const getZbglDetail = (data) => {
  return request({
    url: '/finance/book/bookInfo/detail',
    method: 'get',
    params: data,
  })
}

// 账簿管理新增
export const saveZbgl = (data) => {
  return request({
    url: '/finance/book/bookInfo/save',
    method: 'post',
    data,
  })
}

// 账簿管理删除
export const deleteZbgl = (data) => {
  return request({
    url: '/finance/book/bookInfo/del',
    method: 'post',
    data,
  })
}

// 账簿管理列表
export const getZblxList = (data) => {
  return request({
    url: '/finance/book/setof/getList',
    method: 'get',
    params: data,
  })
}

// 账簿管理详情
export const getZblxDetail = (data) => {
  return request({
    url: '/finance/book/setof/detail',
    method: 'get',
    params: data,
  })
}

// 账簿管理新增
export const saveZblx = (data) => {
  return request({
    url: '/finance/book/setof/save',
    method: 'post',
    data,
  })
}

// 账簿管理删除
export const deleteZblx = (data) => {
  return request({
    url: '/finance/book/setof/del',
    method: 'post',
    data,
  })
}

// 执行抽取
export const executeChouqu = (data) => {
  return request({
    url: '/finance/gather/exeUnique',
    method: 'GET',
    params: data,
  })
}

// 停止采集
export const stopChouqu = (data) => {
  return request({
    url: '/finance/gather/stopGather',
    method: 'GET',
    params: data,
  })
}

// 财务组织列表
export const getCwzzList = (data) => {
  return request({
    url: '/finance/orgs/getList',
    method: 'get',
    params: data,
  })
}

// 财务组织关联公司
export const relateCompany = (data) => {
  return request({
    url: '/finance/orgs/setCompanyInfo',
    method: 'post',
    data,
  })
}

// 账簿授权角色
export const zbglAuthRole = (data) => {
  return request({
    url: '/finance/book/bookRole/grant',
    method: 'post',
    data,
  })
}

// 账簿授权角色列表
export const zbglAuthRoleList = (data) => {
  return request({
    url: '/finance/book/bookRole/getRoleListByBook',
    method: 'get',
    params: data,
  })
}
// 取消授权
export const zbglCancelAuthRole = (data) => {
  return request({
    url: '/finance/book/bookRole/cancel',
    method: 'post',
    data,
  })
}

// 获取公司账簿授权角色列表
export const getGsZbglAuthRoleList = (data) => {
  return request({
    url: '/finance/book/bookStaff/getBookList',
    method: 'get',
    params: data,
  })
}

// 公司账簿选择
export const sureGsZb = (data) => {
  return request({
    url: '/finance/book/bookStaff/selected',
    method: 'post',
    data,
  })
}

// 会计科目表
export const getKmList = (data) => {
  return request({
    url: '/finance/account/getFinanceDataList',
    method: 'get',
    params: data,
  })
}

// 财务组织列表
export const getCwzzTreeList = (data) => {
  return request({
    url: '/finance/orgs/getTreeList',
    method: 'get',
    params: data,
  })
}

// 凭证库
export const getPzkList = (data) => {
  return request({
    url: '/finance/voucher/getFinanceDataList',
    method: 'get',
    params: data,
  })
}

// 获取采集方案结构树
export const getFaTree = (data) => {
  return request({
    url: '/finance/gather/getFinancePlanTreeList',
    method: 'get',
    params: data,
  })
}

// 停止方案采集
export const stopFaGather = (data) => {
  return request({
    url: '/finance/gather/stopFinancePlanProcess',
    method: 'get',
    params: data,
  })
}

// 开始方案采集
export const startFaGather = (data) => {
  return request({
    url: '/finance/gather/beginFinancePlan',
    method: 'get',
    params: data,
  })
}

// 采集子方案采集状态
export const getFaGatherStatus = (data) => {
  return request({
    url: '/finance/gather/getFinancePlanStatus',
    method: 'get',
    params: data,
  })
}

// 停止采集子方案
export const stopFaGatherSub = (data) => {
  return request({
    url: '/finance/gather/stopFinanceUniqueProcess',
    method: 'get',
    params: data,
  })
}

// 获取辅助账左侧树信息表
export const getAuxiliaryInfoTree = (data) => {
  return request({
    url: '/finance/auxiliary/getAccAssTreeList',
    method: 'get',
    params: data,
  })
}

// 获取辅助账余额表
export const getAuxiliaryBalanceTable = (data) => {
  return request({
    url: '/finance/auxiliary/getFinanceAccAssBalanceList',
    method: 'get',
    params: data,
  })
}

// 获取辅助账信息表
export const getAuxiliaryInfoTable = (data) => {
  return request({
    url: '/finance/auxiliary/getFinanceAccAssInfoList',
    method: 'get',
    params: data,
  })
}

// 获取辅助账总表
export const getAuxiliaryTotalTable = (data) => {
  return request({
    url: '/finance/auxiliary/getFinanceAccAssGeneralLedgerList',
    method: 'get',
    params: data,
  })
}

// 获取明细账
export const getAuxiliaryDetailTable = (data) => {
  return request({
    url: '/finance/detailBook/getFinanceDataList',
    method: 'get',
    params: data,
  })
}

// 获取日记账
export const getDiaryBookList = (data) => {
  return request({
    url: '/finance/diaryBook/getFinanceDataList',
    method: 'get',
    params: data,
  })
}

// 获取总分类账
export const getTotalAccountList = (data) => {
  return request({
    url: '/finance/sumTotal/getFinanceDataList',
    method: 'get',
    params: data,
  })
}

// 余额表
export const getYebList = (data) => {
  return request({
    url: '/finance/balance/getFinanceDataList',
    method: 'get',
    params: data,
  })
}
// 凭证明细信息
export const getFinanceDataList = (data) => {
  return request({
    url: '/finance/detail/getFinanceDataList',
    method: 'get',
    params: data,
  })
}

/**
 * 获取定时任务列表
 * @param {Object} params - 查询参数
 * @returns {Promise} - 返回Promise对象
 */
export function getScheduledTaskList(params) {
  return request({
    url: '/finance/scheduled/task/list',
    method: 'get',
    params,
  })
}

/**
 * 删除定时任务
 * @param {Object} params - 删除参数
 * @returns {Promise} - 返回Promise对象
 */
export function deleteScheduledTask(params) {
  return request({
    url: '/finance/scheduled/task/remove',
    method: 'get',
    params,
  })
}

/**
 * 修改定时任务状态
 * @param {Object} data - 状态修改参数
 * @returns {Promise} - 返回Promise对象
 */
export function changeScheduledTaskStatus(data) {
  return request({
    url: '/finance/scheduled/task/status',
    method: 'post',
    data,
  })
}

/**
 * 保存定时任务
 * @param {Object} data - 任务数据
 * @returns {Promise} - 返回Promise对象
 */
export function saveScheduledTask(data) {
  return request({
    url: '/finance/scheduled/task/add',
    method: 'post',
    data,
  })
}
/**
 * 修改定时任务
 * @param {Object} data - 任务数据
 * @returns {Promise} - 返回Promise对象
 */
export function updateScheduledTask(data) {
  return request({
    url: '/finance/scheduled/task/modify',
    method: 'post',
    data,
  })
}

/**
 * 获取定时任务详情
 * @param {Object} params - 查询参数
 * @returns {Promise} - 返回Promise对象
 */
export function getScheduledTaskDetail(params) {
  return request({
    url: '/finance/scheduled/task/detail',
    method: 'get',
    params,
  })
}

// 业务数据列表
export function getYWSJList(params) {
  return request({
    url: '/finance/budata/getTablInfoList',
    method: 'get',
    params,
  })
}


// 业务数据新建
export function getYWSJAdd(data) {
  return request({
    url: '/finance/budata/addTableInfo',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}


// 业务数据新建
export function getYWSJEdit(data) {
  return request({
    url: '/finance/budata/modifyTableInfo',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}

// 业务数据详情
export function getYWSJDetail(params) {
  return request({
    url: '/finance/budata/getTableInfo',
    method: 'get',
    params,
  })
}
// 业务数据列表删除
export function getYWSJDelete(data) {
  return request({
    url: '/finance/budata/removeTableInfo',
    method: 'get',
    params: data,
  })
}

// 业务数据列表发布
export function YWSJFabu(data) {
  return request({
    url: '/finance/budata/tableCreate',
    method: 'get',
    params: data,
  })
}

// 业务数据列表启用、禁用
export function changeStatus(data) {
  return request({
    url: '/finance/budata/tableFstatus',
    method: 'get',
    params: data,
  })
}

// 业务数据列表采集开始
export function startCaiJi(data) {
  return request({
    url: '/finance/budata/gatherBussinessData',
    method: 'get',
    params: data,
  })
}


// 业务数据列表采集停止
export function stopCaiJi(data) {
  return request({
    url: '/finance/budata/stopGatherData',
    method: 'get',
    params: data,
  })
}

// 业务数据采集记录
export function CaiJiLog(data) {
  return request({
    url: '/finance/gather/getFinanceRecordPageList',
    method: 'get',
    params: data,
  })
}
// 业务数据采集记录当前的采集结果
export function CaiJiLogResult(data) {
  return request({
    url: '/finance/gather/getRecordDetail',
    method: 'get',
    params: data,
  })
}

// sql测试
export function sqlTest(data) {
  return request({
    url: '/finance/budata/executeSql',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    },
  })
}