<template>
  <div class="app-container property-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-s-grid"></i><span>三表比对看板</span></div>
      <div class="page-header-desc">产权登记 / 工商登记 / 财务并表 三方数据比对，差异自动高亮，消除监管盲区</div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="14" class="stat-row">
      <el-col :span="6" v-for="c in statCards" :key="c.key">
        <div class="stat-card" :class="c.cls">
          <div class="stat-icon"><i :class="c.icon"></i></div>
          <div class="stat-body"><div class="stat-value">{{ c.value }}</div><div class="stat-label">{{ c.label }}</div></div>
        </div>
      </el-col>
    </el-row>

    <!-- 筛选 -->
    <el-card shadow="never" class="filter-card">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:200px" />
        </el-form-item>
        <el-form-item label="差异状态">
          <el-select v-model="queryForm.diffStatus" placeholder="全部" clearable style="width:130px">
            <el-option label="无差异" value="NONE" />
            <el-option label="有差异" value="DIFF" />
            <el-option label="仅产权登记" value="ONLY_PROPERTY" />
            <el-option label="仅工商登记" value="ONLY_BUSI" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          <el-button type="warning" icon="el-icon-download" :loading="exportLoading" @click="handleExport">导出差异报告</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 三表比对主体 -->
    <el-card shadow="never" class="compare-card">
      <div slot="header" class="card-header">
        <span>三表比对明细</span>
        <span style="font-size:12px;color:#909399;margin-left:12px">红色高亮=存在差异，需核查处置</span>
      </div>
      <el-table v-loading="loading" :data="list" border style="width:100%" :row-class-name="rowClassName">
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip fixed="left" />
        <el-table-column label="股东类型" prop="shareholderType" width="100" align="center">
          <template slot-scope="{ row }">
            <span>{{ formatShareholderType(row.shareholderType) }}</span>
          </template>
        </el-table-column>
        <!-- 产权登记列 -->
        <el-table-column label="产权登记" align="center">
          <el-table-column label="登记状态" prop="propertyStatus" width="100" align="center">
            <template slot-scope="{ row }">
              <el-tag :type="row.propertyStatus === '已登记' ? 'success' : 'danger'" size="small">{{ row.propertyStatus }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="持股比例" prop="propertyRatio" width="90" align="center">
            <template slot-scope="{ row }"><span :class="row.ratioDiff ? 'diff-text' : ''">{{ row.propertyRatio }}%</span></template>
          </el-table-column>
          <el-table-column label="注册资本(万)" prop="propertyCapital" width="110" align="right" />
        </el-table-column>
        <!-- 工商登记列 -->
        <el-table-column label="工商登记" align="center">
          <el-table-column label="登记状态" prop="bizStatus" width="100" align="center">
            <template slot-scope="{ row }">
              <el-tag :type="row.bizStatus === '正常' ? 'success' : 'warning'" size="small">{{ row.bizStatus }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="持股比例" prop="bizRatio" width="90" align="center">
            <template slot-scope="{ row }"><span :class="row.ratioDiff ? 'diff-text' : ''">{{ row.bizRatio }}%</span></template>
          </el-table-column>
          <el-table-column label="注册资本(万)" prop="bizCapital" width="110" align="right" />
        </el-table-column>
        <!-- 财务并表列 -->
        <el-table-column label="财务并表" align="center">
          <el-table-column label="并表状态" prop="financeStatus" width="100" align="center">
            <template slot-scope="{ row }">
              <el-tag :type="row.financeStatus === '已并表' ? 'success' : row.financeStatus === '未并表' ? 'danger' : 'warning'" size="small">{{ row.financeStatus }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="并表比例" prop="financeRatio" width="90" align="center">
            <template slot-scope="{ row }"><span :class="row.ratioDiff ? 'diff-text' : ''">{{ row.financeRatio }}%</span></template>
          </el-table-column>
        </el-table-column>
        <!-- 差异列 -->
        <el-table-column label="差异状态" width="110" align="center" fixed="right">
          <template slot-scope="{ row }">
            <el-tag :type="row.diffStatus === 'NONE' ? 'success' : 'danger'" size="small">
              {{ formatDiffStatus(row.diffStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" align="center" fixed="right">
          <template slot-scope="{ row }">
            <el-button size="mini" type="text" @click="handleDetail(row)">详情</el-button>
            <el-button v-if="row.diffStatus !== 'NONE'" size="mini" type="text" style="color:#fa8c16" @click="handleDispatch(row)">派单</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <div style="margin-top:12px;text-align:right">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next"
          :total="total"
          :page-size="queryForm.pageSize"
          :current-page="queryForm.pageNumber"
          :page-sizes="[10, 20, 50, 100]"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 详情抽屉 -->
    <el-drawer title="三表比对详情" :visible.sync="drawerVisible" size="640px" direction="rtl">
      <div v-if="currentDetail" style="padding:0 20px 20px">
        <el-alert v-if="currentDetail.diffStatus !== 'NONE'" title="该企业存在三表数据差异，请核查处置" type="error" show-icon :closable="false" style="margin:16px 0" />
        <el-divider content-position="left">差异分析</el-divider>
        <el-table :data="currentDetail.diffDetails" border size="small">
          <el-table-column label="差异维度" prop="dimension" width="120" />
          <el-table-column label="产权登记值" prop="propertyVal" align="center" />
          <el-table-column label="工商登记值" prop="bizVal" align="center" />
          <el-table-column label="财务并表值" prop="financeVal" align="center" />
          <el-table-column label="差异说明" prop="desc" min-width="140" show-overflow-tooltip />
        </el-table>
        <el-divider content-position="left">处置建议</el-divider>
        <el-alert :title="currentDetail.suggestion" type="warning" show-icon :closable="false" />
      </div>
      <div v-else style="padding:40px;text-align:center;color:#909399">加载中...</div>
    </el-drawer>

    <!-- 派单对话框 -->
    <el-dialog title="三表差异核查派单" :visible.sync="dispatchVisible" width="460px">
      <el-form :model="dispatchForm" label-width="100px" size="small">
        <el-form-item label="差异企业"><el-input :value="currentRow && currentRow.companyName" disabled /></el-form-item>
        <el-form-item label="核查责任人"><el-input v-model="dispatchForm.owner" placeholder="请输入" /></el-form-item>
        <el-form-item label="核查期限">
          <el-date-picker v-model="dispatchForm.deadline" type="date" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
        <el-form-item label="核查要求"><el-input v-model="dispatchForm.requirement" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dispatchVisible = false">取消</el-button>
        <el-button type="primary" :loading="dispatchLoading" @click="submitDispatch">确认派单</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {
  getThreeTableCompareList,
  getThreeTableCompareDetail,
  exportThreeTableCompare,
  submitThreeTableDispatch
} from '@/api/stateAssets/threeTableCompare'
import { mapGetters } from 'vuex'

export default {
  name: 'ThreeTableCompare',
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      queryForm: {
        companyName: '',
        diffStatus: '',
        pageNumber: 1,
        pageSize: 20
      },
      // 详情抽屉
      drawerVisible: false,
      currentDetail: null,
      detailLoading: false,
      // 派单对话框
      dispatchVisible: false,
      dispatchLoading: false,
      currentRow: null,
      dispatchForm: { owner: '', deadline: '', requirement: '' },
      // 导出
      exportLoading: false
    }
  },
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
    statCards() {
      const diff = this.list.filter(r => r.diffStatus !== 'NONE').length
      return [
        { key: 'total', label: '比对企业总数', value: this.total, icon: 'el-icon-office-building', cls: 'card-blue' },
        { key: 'diff', label: '存在差异数', value: diff, icon: 'el-icon-warning', cls: 'card-red' },
        { key: 'ok', label: '数据一致数', value: this.list.length - diff, icon: 'el-icon-circle-check', cls: 'card-green' },
        { key: 'rate', label: '一致率', value: this.total ? Math.round((this.total - diff) / this.total * 100) + '%' : '-', icon: 'el-icon-data-analysis', cls: 'card-blue' }
      ]
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      getThreeTableCompareList(this.queryForm).then(res => {
        const data = res.data || {}
        this.list = data.tlist || data.list || []
        this.total = data.totalRecord || 0
      }).catch(() => {
        this.list = []
        this.total = 0
      }).finally(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryForm = { companyName: '', diffStatus: '', pageNumber: 1, pageSize: 20 }
      this.fetchData()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    rowClassName({ row }) {
      return row.diffStatus !== 'NONE' ? 'row-danger' : ''
    },
    // ---- 详情 ----
    handleDetail(row) {
      this.currentRow = row
      this.currentDetail = null
      this.drawerVisible = true
      getThreeTableCompareDetail(row.id).then(res => {
        this.currentDetail = res.data || null
      }).catch(() => {
        this.$message.error('获取详情失败')
        this.drawerVisible = false
      })
    },
    // ---- 派单 ----
    handleDispatch(row) {
      this.currentRow = row
      this.dispatchForm = { owner: '', deadline: '', requirement: '' }
      this.dispatchVisible = true
    },
    submitDispatch() {
      if (!this.dispatchForm.owner) { this.$message.warning('请填写核查责任人'); return }
      if (!this.dispatchForm.deadline) { this.$message.warning('请选择核查期限'); return }
      this.dispatchLoading = true
      submitThreeTableDispatch({
        compareId: this.currentRow.id,
        companyName: this.currentRow.companyName,
        owner: this.dispatchForm.owner,
        deadline: this.dispatchForm.deadline,
        requirement: this.dispatchForm.requirement
      }).then(res => {
        // 后端 R 对象成功时 result=200，兼容 result=1 的情况
        const code = res && (res.result !== undefined ? res.result : res.code)
        const isSuccess = [200, 1, '200', '1'].includes(code) || res === undefined || res === null
        if (isSuccess) {
          this.$message.success(`三表差异核查派单已提交：${this.currentRow.companyName}`)
          this.dispatchVisible = false
        } else {
          this.$message.error((res && res.msg) || '派单提交失败，请重试')
        }
      }).catch(err => {
        const msg = (err && err.msg) || (err && err.message) || '派单提交失败，请重试'
        this.$message.error(msg)
      }).finally(() => {
        this.dispatchLoading = false
      })
    },
    // ---- 导出 ----
    async handleExport() {
      this.exportLoading = true
      try {
        const params = {
          companyName: this.queryForm.companyName || undefined,
          diffStatus: this.queryForm.diffStatus || undefined
        }
        const res = await exportThreeTableCompare(params)
        // request.js 对 blob 响应返回整个 axios response 对象 { data, headers, status }
        // 需要取 res.data 才是真正的 Blob
        const blobData = (res && res.data) ? res.data : res
        const blob = new Blob([blobData], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '三表比对差异报告.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) {
        console.error('导出失败', e)
        this.$message.error('导出失败，请重试')
      } finally {
        this.exportLoading = false
      }
    },
    // ---- 格式化 ----
    formatShareholderType(type) {
      const map = {
        ENTERPRISE: '企业法人',
        INDIVIDUAL: '自然人',
        GOVERNMENT: '国有机构',
        INSTITUTION: '事业单位',
        FUND: '基金'
      }
      return map[type] || type || '-'
    },
    formatDiffStatus(status) {
      const map = {
        NONE: '无差异',
        DIFF: '有差异',
        ONLY_PROPERTY: '仅产权登记',
        ONLY_BUSI: '仅工商登记'
      }
      return map[status] || status || '-'
    }
  }
}
</script>

<style lang="scss" scoped>
.property-page { padding:16px; background:#f0f2f5; min-height:calc(100vh - 84px); }
.page-header { display:flex; align-items:center; justify-content:space-between; margin-bottom:14px; padding:14px 20px; border-radius:6px; color:#fff;
  .page-header-left { display:flex; align-items:center; font-size:16px; font-weight:600; i { font-size:22px; margin-right:10px; } }
  .page-header-desc { font-size:13px; opacity:.85; }
}
.stat-row { margin-bottom:14px; }
.stat-card { display:flex; align-items:center; padding:16px; border-radius:8px; background:#fff; box-shadow:0 1px 4px rgba(0,0,0,.08);
  .stat-icon { font-size:30px; padding:10px; border-radius:8px; margin-right:12px; }
  .stat-value { font-size:26px; font-weight:bold; }
  .stat-label { font-size:12px; color:#909399; margin-top:2px; }
  &.card-blue  { .stat-icon { color:#1890ff; background:#e6f7ff; } .stat-value { color:#1890ff; } }
  &.card-red   { .stat-icon { color:#ff4d4f; background:#fff1f0; } .stat-value { color:#ff4d4f; } }
  &.card-green { .stat-icon { color:#52c41a; background:#f6ffed; } .stat-value { color:#52c41a; } }
}
.filter-card { margin-bottom:12px; }
.card-header { font-size:14px; font-weight:600; color:#303133; }
.diff-text { color:#ff4d4f; font-weight:bold; }
::v-deep .row-danger td { background:#fff1f0 !important; }
::v-deep .el-table th { background:#f9f0ff; }
::v-deep .el-card { border-radius:6px; }
</style>

