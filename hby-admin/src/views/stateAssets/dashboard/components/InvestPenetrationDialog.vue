<template>
  <el-dialog
    title="投资穿透 - 项目与风险总览"
    :visible.sync="dialogVisible"
    width="1000px"
    :close-on-click-modal="false"
    custom-class="invest-penetration-dialog"
    @close="handleClose"
  >
    <el-tabs v-model="activeTab" type="border-card">
      <!-- 项目列表 Tab -->
      <el-tab-pane label="项目列表" name="project">
        <div class="tab-toolbar">
          <el-input v-model="projectQuery.keyword" placeholder="搜索项目名称" size="small" clearable style="width:220px" @keyup.enter.native="loadProjectList">
            <i slot="prefix" class="el-icon-search"></i>
          </el-input>
          <el-button size="small" type="primary" @click="loadProjectList"><i class="el-icon-refresh"></i> 查询</el-button>
        </div>
        <el-table :data="projectList" v-loading="projectLoading" border stripe size="small" style="width:100%" max-height="400">
          <el-table-column label="项目编号" prop="projectId" width="130" />
          <el-table-column label="项目名称" prop="projectName" min-width="170" show-overflow-tooltip />
          <el-table-column label="投资类型" width="90" align="center">
            <template slot-scope="{ row }">
              <el-tag size="mini" type="info">{{ row.investTypeLabel }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="投资企业" prop="company" width="100" />
          <el-table-column label="投资金额(万元)" prop="investAmount" width="130" align="right">
            <template slot-scope="{ row }"><span style="font-weight:600">{{ formatAmount(row.investAmount) }}</span></template>
          </el-table-column>
          <el-table-column label="是否主业" width="80" align="center">
            <template slot-scope="{ row }">
              <el-tag :type="row.isMainBiz ? 'success' : 'warning'" size="mini">{{ row.isMainBiz ? '主业' : '非主业' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="项目状态" width="90" align="center">
            <template slot-scope="{ row }">
              <el-tag :type="statusTagType(row.projectStatus)" size="mini">{{ statusTextMap[row.projectStatus] || row.projectStatus || '--' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="预期收益率" width="100" align="right">
            <template slot-scope="{ row }"><span>{{ row.expectedReturn != null ? row.expectedReturn + '%' : '-' }}</span></template>
          </el-table-column>
          <el-table-column label="实际收益率" width="100" align="right">
            <template slot-scope="{ row }">
              <span :style="{color: row.actualReturn != null && row.actualReturn < row.expectedReturn * 0.7 ? '#FF4D4F' : row.actualReturn != null && row.actualReturn < row.expectedReturn ? '#FA8C16' : '#52C41A', fontWeight:'600'}">
                {{ row.actualReturn != null ? row.actualReturn + '%' : '-' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="立项日期" prop="approvalDate" width="100" align="center" />
        </el-table>
        <el-pagination
          v-if="projectTotal > 0"
          :current-page.sync="projectQuery.pageNum"
          :page-size.sync="projectQuery.pageSize"
          :total="projectTotal"
          layout="total, prev, pager, next, sizes"
          :page-sizes="[10, 20, 50]"
          @current-change="loadProjectList"
          @size-change="loadProjectList"
          style="margin-top:12px; text-align:right"
        />
      </el-tab-pane>

      <!-- 风险列表 Tab -->
      <el-tab-pane label="风险预警列表" name="risk">
        <div class="tab-toolbar">
          <el-select v-model="riskQuery.warningLevel" placeholder="风险等级" size="small" clearable style="width:140px">
            <el-option label="高风险" value="HIGH" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="低风险" value="LOW" />
          </el-select>
          <el-button size="small" type="primary" @click="loadRiskList"><i class="el-icon-refresh"></i> 查询</el-button>
        </div>
        <el-table :data="riskList" v-loading="riskLoading" border stripe size="small" style="width:100%" max-height="400">
          <el-table-column type="index" label="序号" width="55" align="center" />
          <el-table-column prop="warningTitle" label="预警标题" min-width="200" show-overflow-tooltip />
          <el-table-column prop="warningLevelName" label="风险等级" width="100" align="center">
            <template slot-scope="{ row }">
              <el-tag :type="riskTagType(row.warningLevel)" size="mini">{{ row.warningLevelName || row.warningLevel || '--' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="warningTypeName" label="预警类型" width="120" align="center" />
          <el-table-column prop="projectName" label="关联项目" width="160" show-overflow-tooltip />
          <el-table-column prop="warningTime" label="预警时间" width="150" align="center" />
          <el-table-column prop="statusName" label="处理状态" width="100" align="center">
            <template slot-scope="{ row }">
              <el-tag :type="row.status === 'PROCESSED' ? 'success' : 'warning'" size="mini">{{ row.statusName || '待处理' }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-if="riskTotal > 0"
          :current-page.sync="riskQuery.pageNum"
          :page-size.sync="riskQuery.pageSize"
          :total="riskTotal"
          layout="total, prev, pager, next, sizes"
          :page-sizes="[10, 20, 50]"
          @current-change="loadRiskList"
          @size-change="loadRiskList"
          style="margin-top:12px; text-align:right"
        />
      </el-tab-pane>
    </el-tabs>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button type="primary" @click="goInvestModule">进入投资穿透模块 <i class="el-icon-arrow-right"></i></el-button>
    </div>
  </el-dialog>
</template>

<script>
/**
 * 投资穿透弹窗 - 项目列表 + 风险预警列表
 * @description 点击投资穿透卡片时弹出，展示项目台账和风险预警两个列表
 */
import { getInvestProjectList, getInvestWarningList } from '@/api/stateAssets/investPenetration'

export default {
  name: 'InvestPenetrationDialog',
  props: {
    visible: { type: Boolean, default: false },
    enterpriseId: { type: String, default: '' }
  },
  data() {
    return {
      activeTab: 'project',
      // 项目列表
      projectList: [],
      projectTotal: 0,
      projectLoading: false,
      projectQuery: { pageNum: 1, pageSize: 10, keyword: '' },
      // 风险列表
      riskList: [],
      riskTotal: 0,
      riskLoading: false,
      riskQuery: { pageNum: 1, pageSize: 10, warningLevel: '' },
      // 状态映射
      statusTextMap: { EXECUTING: '执行中', COMPLETED: '已完成', EXITED: '已退出', PAUSED: '暂停' }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.activeTab = 'project'
        this.loadProjectList()
        this.loadRiskList()
      }
    }
  },
  methods: {
    async loadProjectList() {
      this.projectLoading = true
      try {
        const params = {
          companyId: this.enterpriseId,
          pageNumber: this.projectQuery.pageNum,
          pageSize: this.projectQuery.pageSize,
          projectName: this.projectQuery.keyword || undefined
        }
        const res = await getInvestProjectList(params)
        if (res && (res.result === 200 || res.code === 1) && res.data) {
          const INVEST_TYPE_MAP = { EQUITY: '股权投资', DEBT: '债权投资', FUND: '基金投资', MIXED: '混合型', OTHER: '其他' }
          const rawList = res.data.tlist || res.data.list || res.data.records || []
          this.projectList = rawList.map(item => ({
            ...item,
            isMainBiz: item.isMainBiz === 'Y',
            investTypeLabel: INVEST_TYPE_MAP[item.investType] || item.investType || '--',
            company: item.companyName || item.company || '--'
          }))
          this.projectTotal = res.data.totalRecord || res.data.total || 0
        } else {
          this.projectList = []
          this.projectTotal = 0
        }
      } catch (e) {
        console.error('[InvestPenetrationDialog] loadProjectList', e)
        this.projectList = []
        this.projectTotal = 0
      } finally {
        this.projectLoading = false
      }
    },
    async loadRiskList() {
      this.riskLoading = true
      try {
        const params = {
          companyId: this.enterpriseId,
          pageNum: this.riskQuery.pageNum,
          pageSize: this.riskQuery.pageSize,
          warningLevel: this.riskQuery.warningLevel || undefined
        }
        const res = await getInvestWarningList(params)
        if (res && (res.result === 200 || res.code === 1) && res.data) {
          this.riskList = res.data.tlist || res.data.list || res.data.records || []
          this.riskTotal = res.data.totalRecord || res.data.total || 0
        } else {
          this.riskList = []
          this.riskTotal = 0
        }
      } catch (e) {
        console.error('[InvestPenetrationDialog] loadRiskList', e)
        this.riskList = []
        this.riskTotal = 0
      } finally {
        this.riskLoading = false
      }
    },
    formatAmount(val) {
      if (!val && val !== 0) return '--'
      const n = Number(val)
      if (isNaN(n)) return val
      return n.toLocaleString('zh-CN')
    },
    statusTagType(status) {
      const map = { EXECUTING: '', COMPLETED: 'success', EXITED: 'info', PAUSED: 'warning' }
      return map[status] || 'info'
    },
    riskTagType(level) {
      const map = { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }
      return map[level] || 'info'
    },
    goInvestModule() {
      this.$router.push('/modelMonitor/Tzjkjsc')
      this.handleClose()
    },
    handleClose() {
      this.$emit('update:visible', false)
      this.$emit('close')
    }
  }
}
</script>

<style lang="scss" scoped>
.tab-toolbar {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}

.risk-high { color: #c62828; font-weight: 600; }
.risk-medium { color: #e65100; font-weight: 600; }
.risk-low { color: #2e7d32; font-weight: 600; }

.dialog-footer {
  text-align: right;
}
</style>
