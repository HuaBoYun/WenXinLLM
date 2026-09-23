<template>
  <div class="app-container contract-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + ' 0%, ' + themeColor + 'cc 100%)' }">
      <div class="page-header-left"><i class="el-icon-finished"></i><span>合同审批追踪</span></div>
      <div class="page-header-desc">追踪合同审批流程与审签倒置风险</div>
    </div>
    <el-card shadow="never" class="search-card" :style="{ borderLeft: '3px solid ' + themeColor }">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="合同名称" prop="contractName">
          <el-input v-model="queryForm.contractName" placeholder="请输入" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="审批状态" prop="approvalStatus">
          <el-select v-model="queryForm.approvalStatus" placeholder="请选择" clearable style="width: 130px">
            <el-option label="待审批" value="PENDING" />
            <el-option label="审批中" value="IN_PROGRESS" />
            <el-option label="已通过" value="APPROVED" />
            <el-option label="已驳回" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" style="margin-top: 10px" :style="{ '--table-header-bg': themeColorLight }">
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column label="合同编号" prop="contractNo" width="140" />
        <el-table-column label="合同名称" prop="contractName" min-width="180" show-overflow-tooltip />
        <el-table-column label="合同金额(万元)" prop="contractAmount" width="130" align="right" />
        <el-table-column label="申请人" prop="applicant" width="120" align="center" show-overflow-tooltip />
        <el-table-column label="申请日期" prop="applyDate" width="110" align="center" />
        <el-table-column label="当前审批人" prop="currentApprover" width="100" align="center" />
        <el-table-column label="审批状态" prop="approvalStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="statusTagType(scope.row.approvalStatus)" size="small">
              {{ statusLabel(scope.row.approvalStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="审签倒置" prop="signReversed" width="80" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.signReversed === 'Y'" type="danger" size="small">是</el-tag>
            <span v-else>否</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        style="margin-top: 15px; text-align: right"
        :current-page="queryForm.pageNumber"
        :page-sizes="[10, 20, 50]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 审批流程详情弹窗 -->
    <el-dialog title="审批流程详情" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <div v-loading="flowLoading">
        <!-- 合同基本信息 -->
        <div class="flow-contract-info" v-if="flowData.contractName">
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="合同编号">{{ flowData.contractNo || '-' }}</el-descriptions-item>
            <el-descriptions-item label="合同名称">{{ flowData.contractName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="合同金额(万元)">{{ flowData.contractAmount || '-' }}</el-descriptions-item>
            <el-descriptions-item label="所属企业">{{ flowData.companyName || '-' }}</el-descriptions-item>
          </el-descriptions>
        </div>
        <!-- 审批流程时间线 -->
        <div class="flow-timeline" v-if="flowData.approvalSteps && flowData.approvalSteps.length">
          <h4 style="margin: 16px 0 12px">审批流程</h4>
          <el-timeline>
            <el-timeline-item
              v-for="(step, idx) in flowData.approvalSteps"
              :key="idx"
              :type="stepTimelineType(step.approvalAction)"
              :icon="stepIcon(step.approvalAction)"
              :timestamp="formatTime(step.approvalTime)"
              placement="top"
            >
              <el-card shadow="never" class="flow-step-card">
                <div class="flow-step-header">
                  <span class="flow-step-name">{{ step.stepName }}</span>
                  <el-tag :type="stepTagType(step.approvalAction)" size="mini">
                    {{ stepActionLabel(step.approvalAction) }}
                  </el-tag>
                </div>
                <div class="flow-step-body">
                  <span v-if="step.approverName">审批人：{{ step.approverName }}</span>
                  <span v-if="step.approverDept" style="margin-left: 12px">部门：{{ step.approverDept }}</span>
                </div>
                <div class="flow-step-comment" v-if="step.approvalComment">
                  意见：{{ step.approvalComment }}
                </div>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
        <el-empty v-else-if="!flowLoading" description="暂无审批流程数据" />
      </div>
      <div slot="footer">
        <el-button @click="dialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import { getApprovalTrackList, getApprovalFlow } from '@/api/stateAssets/contractPenetration'

export default {
  name: 'ContractApprovalTrack',
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
  },
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 10, contractName: '', approvalStatus: '' },
      dialogVisible: false,
      flowLoading: false,
      flowData: {},
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    /** 查询审批追踪列表 */
    async fetchData() {
      this.loading = true
      try {
        const res = await getApprovalTrackList(this.queryForm)
        if (res && res.result === 200) {
          const data = res.data || {}
          this.list = (data.tlist || []).map(item => ({
            ...item,
            applyDate: item.applyDate ? String(item.applyDate).substring(0, 10) : '',
          }))
          this.total = data.totalRecord || 0
        } else {
          this.list = []
          this.total = 0
        }
      } catch (e) {
        this.list = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    /** 查询按钮 */
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    /** 重置按钮 */
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    /** 分页 - 每页条数变化 */
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },
    /** 分页 - 页码变化 */
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    /** 查看审批流程 */
    async handleView(row) {
      this.dialogVisible = true
      this.flowLoading = true
      this.flowData = {}
      try {
        const res = await getApprovalFlow(row.contractId)
        if (res && res.result === 200) {
          this.flowData = res.data || {}
        } else {
          this.$message.error(res.msg || '获取审批流程失败')
        }
      } catch (e) {
        this.$message.error('获取审批流程失败')
      } finally {
        this.flowLoading = false
      }
    },
    /** 审批状态标签类型 */
    statusTagType(status) {
      const map = { PENDING: 'info', IN_PROGRESS: 'warning', APPROVED: 'success', REJECTED: 'danger' }
      return map[status] || 'info'
    },
    /** 审批状态标签文字 */
    statusLabel(status) {
      const map = { PENDING: '待审批', IN_PROGRESS: '审批中', APPROVED: '已通过', REJECTED: '已驳回' }
      return map[status] || status
    },
    /** 时间线节点类型 */
    stepTimelineType(action) {
      const map = { APPROVE: 'success', REJECT: 'danger', PENDING: 'info' }
      return map[action] || 'info'
    },
    /** 时间线节点图标 */
    stepIcon(action) {
      const map = { APPROVE: 'el-icon-check', REJECT: 'el-icon-close', PENDING: 'el-icon-time' }
      return map[action] || 'el-icon-time'
    },
    /** 步骤标签类型 */
    stepTagType(action) {
      const map = { APPROVE: 'success', REJECT: 'danger', PENDING: 'info' }
      return map[action] || 'info'
    },
    /** 步骤动作文字 */
    stepActionLabel(action) {
      const map = { APPROVE: '已通过', REJECT: '已驳回', PENDING: '待处理' }
      return map[action] || action
    },
    /** 格式化时间 */
    formatTime(time) {
      if (!time) return ''
      return String(time).substring(0, 19).replace('T', ' ')
    },
  },
}
</script>
<style lang="scss" scoped>
.contract-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.search-card { }
::v-deep .el-table th { background: var(--table-header-bg, #e6f7ff); }
::v-deep .el-card { border-radius: 6px; }

.flow-contract-info { margin-bottom: 16px; }
.flow-timeline { margin-top: 8px; }
.flow-step-card {
  padding: 8px 12px;
  .flow-step-header {
    display: flex; align-items: center; justify-content: space-between; margin-bottom: 6px;
    .flow-step-name { font-weight: 600; font-size: 14px; }
  }
  .flow-step-body { font-size: 13px; color: #666; margin-bottom: 4px; }
  .flow-step-comment { font-size: 12px; color: #999; margin-top: 4px; }
}
</style>
