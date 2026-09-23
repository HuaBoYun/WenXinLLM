<template>
  <div class="invest-project-table">
    <!-- 搜索栏 -->
    <div class="table-toolbar" v-if="showToolbar">
      <el-input v-model="queryForm.projectName" placeholder="搜索项目名称" size="small" clearable style="width:200px" @keyup.enter.native="handleQuery">
        <i slot="prefix" class="el-icon-search"></i>
      </el-input>
      <el-select v-model="queryForm.investType" placeholder="投资类型" size="small" clearable style="width:120px">
        <el-option label="股权投资" value="EQUITY" />
        <el-option label="债权投资" value="DEBT" />
        <el-option label="基金投资" value="FUND" />
        <el-option label="混合型" value="MIXED" />
        <el-option label="其他" value="OTHER" />
      </el-select>
      <el-select v-model="queryForm.projectStatus" placeholder="项目状态" size="small" clearable style="width:120px">
        <el-option label="执行中" value="EXECUTING" />
        <el-option label="已完成" value="COMPLETED" />
        <el-option label="已退出" value="EXITED" />
        <el-option label="暂停" value="PAUSED" />
      </el-select>
      <el-button size="small" type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
      <el-button size="small" icon="el-icon-refresh" @click="handleReset">重置</el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="list" v-loading="loading" border stripe size="small" style="width:100%" :max-height="maxHeight">
      <el-table-column label="项目编号" prop="projectId" width="130" />
      <el-table-column label="项目名称" prop="projectName" min-width="170" show-overflow-tooltip />
      <el-table-column label="投资类型" width="90" align="center">
        <template slot-scope="{ row }">
          <el-tag size="mini" type="info">{{ row.investTypeLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="投资企业" prop="company" width="100" />
      <el-table-column label="投资金额(万元)" width="130" align="right">
        <template slot-scope="{ row }">
          <span style="font-weight:600">{{ formatAmount(row.investAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否主业" width="80" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="row.isMainBiz ? 'success' : 'warning'" size="mini">{{ row.isMainBiz ? '主业' : '非主业' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="项目状态" width="90" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="statusTagMap[row.projectStatus]" size="mini">{{ statusTextMap[row.projectStatus] || row.projectStatus || '--' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="预期收益率" width="100" align="right">
        <template slot-scope="{ row }">{{ row.expectedReturn != null ? row.expectedReturn + '%' : '-' }}</template>
      </el-table-column>
      <el-table-column label="实际收益率" width="100" align="right">
        <template slot-scope="{ row }">
          <span :style="{color: getReturnColor(row), fontWeight:'600'}">
            {{ row.actualReturn != null ? row.actualReturn + '%' : '-' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="立项日期" prop="approvalDate" width="100" align="center" />
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-if="total > 0"
      background
      :current-page.sync="queryForm.pageNumber"
      :page-size.sync="queryForm.pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next"
      :page-sizes="[10, 20, 50]"
      @current-change="fetchData"
      @size-change="handleSizeChange"
      style="margin-top:12px; text-align:right"
    />
  </div>
</template>

<script>
/**
 * 投资项目列表公共组件
 * @description 可在任意页面引入，展示投资项目台账列表，字段与 investPenetration/project/index.vue 一致
 */
import { getInvestProjectList } from '@/api/stateAssets/investPenetration'

const INVEST_TYPE_MAP = { EQUITY: '股权投资', DEBT: '债权投资', FUND: '基金投资', MIXED: '混合型', OTHER: '其他' }

export default {
  name: 'InvestProjectTable',
  props: {
    /** 公司ID，传入后按该公司及下级公司穿透查询 */
    companyId: { type: String, default: '' },
    /** 是否显示搜索工具栏 */
    showToolbar: { type: Boolean, default: true },
    /** 表格最大高度 */
    maxHeight: { type: [Number, String], default: 500 },
    /** 是否在组件挂载时自动加载数据 */
    autoLoad: { type: Boolean, default: true }
  },
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 10, projectName: '', investType: '', projectStatus: '' },
      statusTagMap: { EXECUTING: '', COMPLETED: 'success', EXITED: 'info', PAUSED: 'warning' },
      statusTextMap: { EXECUTING: '执行中', COMPLETED: '已完成', EXITED: '已退出', PAUSED: '暂停' }
    }
  },
  watch: {
    companyId(val) {
      if (val) this.fetchData()
    }
  },
  created() {
    if (this.autoLoad) this.fetchData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const params = {
          companyId: this.companyId || undefined,
          pageNumber: this.queryForm.pageNumber,
          pageSize: this.queryForm.pageSize,
          projectName: this.queryForm.projectName || undefined,
          investType: this.queryForm.investType || undefined,
          projectStatus: this.queryForm.projectStatus || undefined
        }
        const res = await getInvestProjectList(params)
        if (res && (res.result === 200 || res.code === 1) && res.data) {
          const rawList = res.data.tlist || res.data.list || res.data.records || []
          this.list = rawList.map(item => ({
            ...item,
            isMainBiz: item.isMainBiz === 'Y',
            investTypeLabel: INVEST_TYPE_MAP[item.investType] || item.investType || '--',
            company: item.companyName || item.company || '--'
          }))
          this.total = res.data.totalRecord || res.data.total || 0
        } else {
          this.list = []
          this.total = 0
        }
      } catch (e) {
        console.error('[InvestProjectTable] fetchData', e)
        this.list = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    handleReset() {
      this.queryForm = { pageNumber: 1, pageSize: 10, projectName: '', investType: '', projectStatus: '' }
      this.fetchData()
    },
    handleSizeChange() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    formatAmount(val) {
      if (!val && val !== 0) return '--'
      const n = Number(val)
      if (isNaN(n)) return val
      return n.toLocaleString('zh-CN')
    },
    getReturnColor(row) {
      if (row.actualReturn == null) return '#999'
      if (row.actualReturn < row.expectedReturn * 0.7) return '#FF4D4F'
      if (row.actualReturn < row.expectedReturn) return '#FA8C16'
      return '#52C41A'
    }
  }
}
</script>

<style lang="scss" scoped>
.invest-project-table {
  .table-toolbar {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 12px;
    flex-wrap: wrap;
  }
}
</style>
