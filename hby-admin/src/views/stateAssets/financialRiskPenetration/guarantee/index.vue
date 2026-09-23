<template>
  <div class="fin-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-s-check"></i><span>担保记录台账</span></div>
      <div class="page-header-desc">登记各级企业对外担保信息，识别互保/连环担保风险</div>
    </div>

    <!-- 统计卡 -->
    <el-row :gutter="16" class="mb-16">
      <el-col :span="6" v-for="s in statCards" :key="s.label">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-inner">
            <div class="stat-icon" :style="{ background: s.color + '18', color: s.color }"><i :class="s.icon"></i></div>
            <div class="stat-info">
              <div class="stat-value">{{ s.value }}<span v-if="s.unit" class="stat-unit">{{ s.unit }}</span></div>
              <div class="stat-label">{{ s.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表 -->
    <el-row :gutter="16" class="mb-16">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header"><span>担保集中度分析（亿元）</span></div>
          <div ref="concChart" style="height:220px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header"><span>担保到期分布（亿元）</span></div>
          <div ref="expiryChart" style="height:220px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询 -->
    <el-card shadow="never" class="mb-16">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="担保方">
          <el-input v-model="queryForm.companyName" placeholder="请输入担保方" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="被担保方">
          <el-input v-model="queryForm.guaranteedName" placeholder="请输入被担保方" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.guaranteeStatus" placeholder="请选择" clearable style="width:110px">
            <el-option label="正常" value="NORMAL" /><el-option label="异常" value="ABNORMAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="queryForm.riskLevel" placeholder="请选择" clearable style="width:110px">
            <el-option label="高风险" value="HIGH" /><el-option label="中风险" value="MEDIUM" /><el-option label="低风险" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增</el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <div slot="header">
        <span>担保台账列表</span>
        <el-tag v-if="mutualGuaranteeMsg" type="danger" size="mini" style="margin-left:10px">{{ mutualGuaranteeMsg }}</el-tag>
        <span style="font-size:13px;color:#909399;margin-left:10px">（共 {{ total }} 条）</span>
      </div>
      <el-table v-loading="loading" :data="list" border stripe style="width:100%"
        :row-class-name="getRowClass">
        <el-table-column label="担保方" prop="companyName" min-width="150" show-overflow-tooltip />
        <el-table-column label="被担保方" prop="guaranteedName" min-width="150" show-overflow-tooltip />
        <el-table-column label="担保金额(万元)" prop="guaranteeAmount" width="130" align="right">
          <template slot-scope="scope">
            <span :style="{fontWeight:'600',color:ipSecondary}">{{ formatAmount(scope.row.guaranteeAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="担保比例说明" prop="guaranteeRatio" min-width="150" show-overflow-tooltip />
        <el-table-column label="到期日" prop="endDate" width="110" align="center" />
        <el-table-column label="反担保措施" prop="counterGuarantee" width="100" align="center" />
        <el-table-column label="状态" prop="guaranteeStatus" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.guaranteeStatus === 'NORMAL' ? 'success' : 'danger'" size="mini">
              {{ scope.row.guaranteeStatus === 'NORMAL' ? '正常' : '异常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[scope.row.riskLevel]" size="mini">
              {{ { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }[scope.row.riskLevel] || scope.row.riskLevel || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top:16px;text-align:right">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next"
          :total="total"
          :current-page.sync="queryForm.pageNumber"
          :page-size.sync="queryForm.pageSize"
          :page-sizes="[10, 15, 20, 50]"
          @size-change="getList"
          @current-change="getList"
        />
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="680px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="110px" size="small">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="担保方" prop="companyName">
              <el-input v-model="form.companyName" placeholder="请输入担保方" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="被担保方" prop="guaranteedName">
              <el-input v-model="form.guaranteedName" placeholder="请输入被担保方" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="担保金额(万)" prop="guaranteeAmount">
              <el-input-number v-model="form.guaranteeAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="担保类型" prop="guaranteeType">
              <el-select v-model="form.guaranteeType" placeholder="请选择" style="width:100%">
                <el-option label="连带责任担保" value="JOINT" />
                <el-option label="一般担保" value="GENERAL" />
                <el-option label="抵押担保" value="MORTGAGE" />
                <el-option label="质押担保" value="PLEDGE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="担保比例" prop="guaranteeRatio">
              <el-input v-model="form.guaranteeRatio" placeholder="如：融资余额100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="反担保措施" prop="counterGuarantee">
              <el-input v-model="form.counterGuarantee" placeholder="请输入反担保措施" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="起始日期" prop="startDate">
              <el-date-picker v-model="form.startDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到期日期" prop="endDate">
              <el-date-picker v-model="form.endDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="状态" prop="guaranteeStatus">
              <el-select v-model="form.guaranteeStatus" placeholder="请选择" style="width:100%">
                <el-option label="正常" value="NORMAL" />
                <el-option label="异常" value="ABNORMAL" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="form.riskLevel" placeholder="请选择" style="width:100%">
                <el-option label="高风险" value="HIGH" />
                <el-option label="中风险" value="MEDIUM" />
                <el-option label="低风险" value="LOW" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="是否关联方">
              <el-radio-group v-model="form.isRelatedParty">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button size="small" type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog title="担保记录详情" :visible.sync="viewDialogVisible" width="600px">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="担保方">{{ viewRow.companyName }}</el-descriptions-item>
        <el-descriptions-item label="被担保方">{{ viewRow.guaranteedName }}</el-descriptions-item>
        <el-descriptions-item label="担保金额(万元)">{{ formatAmount(viewRow.guaranteeAmount) }}</el-descriptions-item>
        <el-descriptions-item label="担保类型">{{ viewRow.guaranteeType }}</el-descriptions-item>
        <el-descriptions-item label="担保比例">{{ viewRow.guaranteeRatio }}</el-descriptions-item>
        <el-descriptions-item label="反担保措施">{{ viewRow.counterGuarantee }}</el-descriptions-item>
        <el-descriptions-item label="起始日期">{{ viewRow.startDate }}</el-descriptions-item>
        <el-descriptions-item label="到期日期">{{ viewRow.endDate }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ viewRow.guaranteeStatus === 'NORMAL' ? '正常' : '异常' }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">{{ { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }[viewRow.riskLevel] }}</el-descriptions-item>
        <el-descriptions-item label="是否关联方">{{ viewRow.isRelatedParty === 1 ? '是' : '否' }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ viewRow.remark }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button size="small" @click="viewDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getGuaranteeRecordList,
  addGuaranteeRecord,
  updateGuaranteeRecord,
  deleteGuaranteeRecord
} from '@/api/stateAssets/financialRiskPenetration'
import { investThemeMixin } from '../../themeMixin'

const defaultForm = {
  guaranteeId: null,
  companyName: '',
  guaranteedName: '',
  guaranteeAmount: 0,
  guaranteeType: '',
  guaranteeRatio: '',
  counterGuarantee: '',
  startDate: '',
  endDate: '',
  guaranteeStatus: 'NORMAL',
  riskLevel: 'LOW',
  isRelatedParty: 0,
  remark: ''
}

export default {
  name: 'FinancialRiskGuarantee',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      total: 0,
      list: [],
      queryForm: {
        companyName: '',
        guaranteedName: '',
        guaranteeStatus: '',
        riskLevel: '',
        pageNumber: 1,
        pageSize: 15
      },
      charts: [],
      // 弹窗
      dialogVisible: false,
      dialogTitle: '新增担保记录',
      submitLoading: false,
      form: { ...defaultForm },
      formRules: {
        companyName: [{ required: true, message: '请输入担保方', trigger: 'blur' }],
        guaranteedName: [{ required: true, message: '请输入被担保方', trigger: 'blur' }],
        guaranteeAmount: [{ required: true, message: '请输入担保金额', trigger: 'blur' }],
        guaranteeType: [{ required: true, message: '请选择担保类型', trigger: 'change' }],
        startDate: [{ required: true, message: '请选择起始日期', trigger: 'change' }],
        endDate: [{ required: true, message: '请选择到期日期', trigger: 'change' }],
        guaranteeStatus: [{ required: true, message: '请选择状态', trigger: 'change' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }]
      },
      // 查看详情
      viewDialogVisible: false,
      viewRow: {},
      // 互保提示
      mutualGuaranteeMsg: ''
    }
  },
  computed: {
    statCards() {
      const totalAmount = this.list.reduce((sum, r) => sum + (Number(r.guaranteeAmount) || 0), 0)
      const abnormalCount = this.list.filter(r => r.guaranteeStatus === 'ABNORMAL').length
      const highRiskCount = this.list.filter(r => r.riskLevel === 'HIGH').length
      const mutualPairs = this.detectMutualGuarantee()
      return [
        { label: '担保总额', value: totalAmount.toLocaleString(), unit: '万元', icon: 'el-icon-s-check', color: this.ipSecondary },
        { label: '高风险担保', value: highRiskCount, unit: '笔', icon: 'el-icon-data-line', color: '#CF1322' },
        { label: '异常担保', value: abnormalCount, unit: '笔', icon: 'el-icon-warning', color: '#FA8C16' },
        { label: '互保对数', value: mutualPairs.length, unit: '对', icon: 'el-icon-connection', color: '#722ED1' }
      ]
    }
  },
  mounted() {
    this.getList()
  },
  beforeDestroy() {
    this.charts.forEach(c => c && c.dispose())
  },
  methods: {
    /** 获取列表数据 */
    getList() {
      this.loading = true
      getGuaranteeRecordList(this.queryForm).then(res => {
        if (res.result === 200) {
          this.list = res.data.tlist || []
          this.total = res.data.totalRecord || 0
          this.updateCharts()
          this.checkMutualGuarantee()
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      }).catch(() => {
        this.$message.error('请求失败，请检查网络')
      }).finally(() => {
        this.loading = false
      })
    },
    /** 查询 */
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },
    /** 重置 */
    resetQuery() {
      this.queryForm = {
        companyName: '',
        guaranteedName: '',
        guaranteeStatus: '',
        riskLevel: '',
        pageNumber: 1,
        pageSize: 15
      }
      this.getList()
    },
    /** 新增 */
    handleAdd() {
      this.dialogTitle = '新增担保记录'
      this.form = { ...defaultForm }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.formRef && this.$refs.formRef.clearValidate()
      })
    },
    /** 编辑 */
    handleEdit(row) {
      this.dialogTitle = '编辑担保记录'
      this.form = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.formRef && this.$refs.formRef.clearValidate()
      })
    },
    /** 提交表单 */
    handleSubmit() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        this.submitLoading = true
        const apiFn = this.form.guaranteeId ? updateGuaranteeRecord : addGuaranteeRecord
        apiFn(this.form).then(res => {
          if (res.result === 200) {
            this.$message.success(this.form.guaranteeId ? '修改成功' : '新增成功')
            this.dialogVisible = false
            this.getList()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        }).catch(() => {
          this.$message.error('请求失败')
        }).finally(() => {
          this.submitLoading = false
        })
      })
    },
    /** 查看详情 */
    handleView(row) {
      this.viewRow = { ...row }
      this.viewDialogVisible = true
    },
    /** 删除 */
    handleDelete(row) {
      this.$confirm('确认删除该担保记录？', '提示', { type: 'warning' }).then(() => {
        deleteGuaranteeRecord(row.guaranteeId).then(res => {
          if (res.result === 200) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        }).catch(() => {
          this.$message.error('请求失败')
        })
      }).catch(() => {})
    },
    /** 导出 */
    handleExport() {
      const loading = this.$loading({ lock: true, text: '正在导出数据...', background: 'rgba(0, 0, 0, 0.7)' })
      const exportParams = { pageNumber: 1, pageSize: 10000 }
      if (this.queryForm.companyName) exportParams.companyName = this.queryForm.companyName
      if (this.queryForm.guaranteeStatus) exportParams.guaranteeStatus = this.queryForm.guaranteeStatus
      if (this.queryForm.riskLevel) exportParams.riskLevel = this.queryForm.riskLevel

      getGuaranteeRecordList(exportParams).then(res => {
        loading.close()
        if (res.data && res.data.tlist && res.data.tlist.length > 0) {
          const rows = res.data.tlist
          const headers = ['担保方', '被担保方', '担保金额(万元)', '担保比例说明', '到期日', '反担保措施', '状态', '风险等级']
          const statusMap = { NORMAL: '正常', ABNORMAL: '异常' }
          const riskMap = { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }
          const csvRows = [headers.join(',')]
          rows.forEach(r => {
            csvRows.push([
              r.companyName || '',
              r.guaranteedName || '',
              r.guaranteeAmount || '',
              r.guaranteeRatio || '',
              r.endDate || '',
              r.counterGuarantee || '',
              statusMap[r.guaranteeStatus] || r.guaranteeStatus || '',
              riskMap[r.riskLevel] || r.riskLevel || ''
            ].map(v => '"' + String(v).replace(/"/g, '""') + '"').join(','))
          })
          const blob = new Blob(['\uFEFF' + csvRows.join('\n')], { type: 'text/csv;charset=utf-8' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `担保台账数据_${new Date().getTime()}.csv`
          link.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.warning('无数据可导出')
        }
      }).catch(() => {
        loading.close()
        this.$message.error('导出失败')
      })
    },
    /** 格式化金额 */
    formatAmount(val) {
      if (val === null || val === undefined) return '-'
      return Number(val).toLocaleString()
    },
    /** 检测互保关系 */
    detectMutualGuarantee() {
      const pairs = []
      for (let i = 0; i < this.list.length; i++) {
        for (let j = i + 1; j < this.list.length; j++) {
          const a = this.list[i]
          const b = this.list[j]
          if (a.companyName === b.guaranteedName && a.guaranteedName === b.companyName) {
            pairs.push([i, j])
          }
        }
      }
      return pairs
    },
    /** 互保提示 */
    checkMutualGuarantee() {
      const pairs = this.detectMutualGuarantee()
      if (pairs.length > 0) {
        const msgs = pairs.map(([i, j]) => `第${i + 1}、${j + 1}条`)
        this.mutualGuaranteeMsg = `互保提示：${msgs.join('，')}存在互保关系`
      } else {
        this.mutualGuaranteeMsg = ''
      }
    },
    /** 行样式 */
    getRowClass({ row }) {
      // 互保行高亮
      const pairs = this.detectMutualGuarantee()
      const mutualIndices = new Set()
      pairs.forEach(([i, j]) => { mutualIndices.add(i); mutualIndices.add(j) })
      const idx = this.list.indexOf(row)
      if (mutualIndices.has(idx)) return 'mutual-row'
      if (row.guaranteeStatus === 'ABNORMAL') return 'abnormal-row'
      return ''
    },
    /** 更新图表 */
    updateCharts() {
      this.$nextTick(() => {
        this.initConcChart()
        this.initExpiryChart()
      })
    },
    /** 担保集中度图表 */
    initConcChart() {
      if (!this.$refs.concChart) return
      let chart = this.charts.find(c => c && c.getDom() === this.$refs.concChart)
      if (!chart) {
        chart = echarts.init(this.$refs.concChart)
        this.charts.push(chart)
      }
      // 按担保金额排序取前6
      const sorted = [...this.list].sort((a, b) => (b.guaranteeAmount || 0) - (a.guaranteeAmount || 0)).slice(0, 6)
      const colorMap = { HIGH: '#CF1322', MEDIUM: '#FA8C16', LOW: this.ipBright }
      const yData = sorted.map(r => {
        const from = (r.companyName || '').substring(0, 4)
        const to = (r.guaranteedName || '').substring(0, 4)
        return `${from}→${to}`
      })
      const seriesData = sorted.map(r => ({
        value: ((r.guaranteeAmount || 0) / 10000).toFixed(2),
        itemStyle: { color: colorMap[r.riskLevel] || this.ipBright }
      }))
      chart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 130, right: 20, top: 10, bottom: 30 },
        yAxis: { type: 'category', data: yData.reverse() },
        xAxis: { type: 'value', name: '亿元' },
        series: [{ type: 'bar', barWidth: 14, label: { show: true, position: 'right' }, data: seriesData.reverse() }]
      }, true)
    },
    /** 担保到期分布图表 */
    initExpiryChart() {
      if (!this.$refs.expiryChart) return
      let chart = this.charts.find(c => c && c.getDom() === this.$refs.expiryChart)
      if (!chart) {
        chart = echarts.init(this.$refs.expiryChart)
        this.charts.push(chart)
      }
      // 按季度分组
      const quarterMap = {}
      this.list.forEach(r => {
        if (!r.endDate) return
        const d = new Date(r.endDate)
        const q = `${d.getFullYear()}Q${Math.ceil((d.getMonth() + 1) / 3)}`
        if (!quarterMap[q]) quarterMap[q] = { HIGH: 0, MEDIUM: 0, LOW: 0 }
        quarterMap[q][r.riskLevel] = (quarterMap[q][r.riskLevel] || 0) + ((r.guaranteeAmount || 0) / 10000)
      })
      const quarters = Object.keys(quarterMap).sort()
      chart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 50, right: 20, top: 30, bottom: 30 },
        xAxis: { type: 'category', data: quarters },
        yAxis: { type: 'value', name: '亿元' },
        series: [
          { name: '高风险担保', type: 'bar', stack: 'total', data: quarters.map(q => +(quarterMap[q].HIGH || 0).toFixed(2)), itemStyle: { color: '#CF1322' } },
          { name: '中风险担保', type: 'bar', stack: 'total', data: quarters.map(q => +(quarterMap[q].MEDIUM || 0).toFixed(2)), itemStyle: { color: '#FA8C16' } },
          { name: '低风险担保', type: 'bar', stack: 'total', data: quarters.map(q => +(quarterMap[q].LOW || 0).toFixed(2)), itemStyle: { color: '#52C41A' } }
        ]
      }, true)
    }
  }
}
</script>

<style lang="scss" scoped>
.fin-page { padding: 16px; background: #F0F2F5; min-height: calc(100vh - 84px); }
.mb-16 { margin-bottom: 16px; }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.stat-card { border-radius: 6px; }
.stat-inner { display: flex; align-items: center; }
.stat-icon { width: 44px; height: 44px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 12px; i { font-size: 22px; } }
.stat-value { font-size: 20px; font-weight: 700; color: #303133; line-height: 1; .stat-unit { font-size: 12px; font-weight: 400; color: #909399; margin-left: 2px; } }
.stat-label { font-size: 12px; color: #909399; margin-top: 4px; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); }
::v-deep .el-table .mutual-row { background: #FFF7E6 !important; }
::v-deep .el-table .abnormal-row { background: #FFF1F0 !important; }
::v-deep .el-card { border-radius: 6px; }
</style>