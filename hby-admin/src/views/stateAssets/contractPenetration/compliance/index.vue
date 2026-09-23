<template>
  <div class="compliance-wrap">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title">合同审批合规追踪</div>
        <div class="banner-sub">识别未经法务审核、超权限签约、审批链路不完整等合规问题，推动整改落实</div>
      </div>
      <div class="banner-right">
        <div v-for="item in bannerStats" :key="item.label" class="banner-stat">
          <span class="stat-num">{{ item.value }}</span>
          <span class="stat-label">{{ item.label }}</span>
        </div>
      </div>
    </div>

    <!-- 统计卡 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col v-for="card in statCards" :key="card.label" :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-card-inner">
            <i :class="card.icon" :style="{color: card.color, fontSize:'28px'}"></i>
            <div class="stat-info">
              <div class="stat-value" :style="{color: card.color}">{{ card.value }}</div>
              <div class="stat-label">{{ card.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询栏 -->
    <el-card shadow="never" style="margin-bottom:16px">
      <el-row :gutter="12">
        <el-col :span="5">
          <el-input v-model="query.companyName" placeholder="企业名称" clearable size="small" prefix-icon="el-icon-office-building"/>
        </el-col>
        <el-col :span="4">
          <el-select v-model="query.contractType" placeholder="合同类型" clearable size="small" style="width:100%">
            <el-option v-for="t in contractTypes" :key="t" :label="t" :value="t"/>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="query.violationType" placeholder="违规类型" clearable size="small" style="width:100%">
            <el-option v-for="v in violationTypes" :key="v" :label="v" :value="v"/>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="query.rectifyStatus" placeholder="整改状态" clearable size="small" style="width:100%">
            <el-option label="待整改" value="PENDING"/>
            <el-option label="整改中" value="PROCESSING"/>
            <el-option label="已关闭" value="CLOSED"/>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" size="small" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button size="small" icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 图表区 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>合规率趋势（近6个月）</span></div>
          <div ref="trendChart" style="height:220px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>违规类型分布</span></div>
          <div ref="violationChart" style="height:220px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 合规追踪列表 -->
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>合规追踪列表</span>
        <el-tag size="small" type="danger" style="margin-left:8px">{{ filteredList.length }} 条违规记录</el-tag>
      </div>
      <el-table
        :data="filteredList"
        :row-class-name="tableRowClass"
        border
        size="small"
        style="width:100%">
        <el-table-column label="合同编号" prop="contractNo" width="160" fixed/>
        <el-table-column label="合同名称" prop="contractName" min-width="180" show-overflow-tooltip/>
        <el-table-column label="合同类型" prop="contractType" width="110">
          <template slot-scope="{row}">
            <span class="tag-pill" :style="contractTypeTagStyle(row.contractType)">{{ row.contractType }}</span>
          </template>
        </el-table-column>
        <el-table-column label="企业" prop="companyName" width="140" show-overflow-tooltip/>
        <el-table-column label="合同金额(万)" prop="amount" width="120" align="right">
          <template slot-scope="{row}">
            <span style="font-weight:600;color:#0050A0">{{ row.amount.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column label="违规类型" prop="violationType" width="150">
          <template slot-scope="{row}">
            <span class="tag-pill" :style="violationTagStyle(row.violationType)">{{ row.violationType }}</span>
          </template>
        </el-table-column>
        <el-table-column label="违规等级" prop="violationLevel" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.violationLevel==='HIGH'?'danger':row.violationLevel==='MEDIUM'?'warning':''" size="mini">
              {{ row.violationLevel==='HIGH'?'高危':row.violationLevel==='MEDIUM'?'中危':'低危' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="签订日期" prop="signDate" width="110"/>
        <el-table-column label="整改状态" prop="rectifyStatus" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.rectifyStatus==='CLOSED'?'success':row.rectifyStatus==='PROCESSING'?'warning':'danger'" size="mini">
              {{ row.rectifyStatus==='CLOSED'?'已关闭':row.rectifyStatus==='PROCESSING'?'整改中':'待整改' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" size="mini" icon="el-icon-share" @click="showChain(row)">审批链路</el-button>
            <el-button v-if="row.rectifyStatus==='PENDING'" type="text" size="mini" icon="el-icon-edit" style="color:#FA8C16" @click="handleRectify(row)">下发整改</el-button>
            <el-button type="text" size="mini" icon="el-icon-view" @click="showDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 整改弹窗 -->
    <el-dialog title="下发整改通知" :visible.sync="rectifyVisible" width="500px" append-to-body>
      <el-form :model="rectifyForm" label-width="100px" size="small">
        <el-form-item label="合同编号"><span>{{ rectifyForm.contractNo }}</span></el-form-item>
        <el-form-item label="违规问题"><span style="color:#F5222D">{{ rectifyForm.violationType }}</span></el-form-item>
        <el-form-item label="整改措施" required>
          <el-input v-model="rectifyForm.measures" type="textarea" :rows="3" placeholder="请描述整改措施"/>
        </el-form-item>
        <el-form-item label="责任人" required>
          <el-input v-model="rectifyForm.owner" placeholder="请输入责任人姓名"/>
        </el-form-item>
        <el-form-item label="完成时限" required>
          <el-date-picker v-model="rectifyForm.deadline" type="date" placeholder="选择整改期限" style="width:100%" value-format="yyyy-MM-dd"/>
        </el-form-item>
        <el-form-item label="整改说明">
          <el-input v-model="rectifyForm.remark" type="textarea" :rows="2" placeholder="补充说明"/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="rectifyVisible=false">取消</el-button>
        <el-button type="primary" size="small" :loading="rectifyLoading" @click="submitRectify">确定下发</el-button>
      </div>
    </el-dialog>

    <!-- 审批链路抽屉 -->
    <el-drawer title="审批链路详情" :visible.sync="chainVisible" size="480px" append-to-body>
      <div v-if="currentRow" style="padding:20px">
        <div v-if="chainLoading" style="text-align:center;padding:40px">
          <i class="el-icon-loading" style="font-size:24px;color:#409EFF"></i>
          <p style="color:#999;margin-top:8px">加载中...</p>
        </div>
        <template v-else>
          <el-descriptions :column="2" size="small" border style="margin-bottom:20px">
            <el-descriptions-item label="合同编号">{{ currentRow.contractNo }}</el-descriptions-item>
            <el-descriptions-item label="合同名称">{{ currentRow.contractName }}</el-descriptions-item>
            <el-descriptions-item label="违规类型">
              <span :style="violationTagStyle(currentRow.violationType)" class="tag-pill">{{ currentRow.violationType }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="整改状态">
              <el-tag :type="currentRow.rectifyStatus==='CLOSED'?'success':currentRow.rectifyStatus==='PROCESSING'?'warning':'danger'" size="mini">
                {{ currentRow.rectifyStatus==='CLOSED'?'已关闭':currentRow.rectifyStatus==='PROCESSING'?'整改中':'待整改' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
          <div style="font-weight:600;margin-bottom:12px;color:#333">审批链路（步骤条）</div>
          <el-steps v-if="currentRow.approvalChain && currentRow.approvalChain.length" :active="getActiveStep(currentRow)" direction="vertical" process-status="error">
            <el-step
              v-for="(step,i) in currentRow.approvalChain"
              :key="i"
              :title="step.step"
              :status="step.status==='done'?'finish':step.status==='skip'?'error':'process'"
              :icon="step.status==='skip'?'el-icon-warning':''">
              <div slot="description" style="font-size:12px;color:#666;margin-top:4px">
                <span v-if="step.status==='skip'" style="color:#F5222D">⚠ {{ step.comment || '该环节被跳过，存在合规风险' }}</span>
                <span v-else>操作人：{{ step.operator || '—' }} &nbsp;|&nbsp; 时间：{{ step.time || '—' }}</span>
              </div>
            </el-step>
          </el-steps>
          <el-empty v-else description="暂无审批链路数据" :image-size="80"/>
        </template>
      </div>
    </el-drawer>

    <!-- 详情抽屉 -->
    <el-drawer title="合规详情" :visible.sync="detailVisible" size="480px" append-to-body>
      <div v-if="currentRow" style="padding:20px">
        <div v-if="detailLoading" style="text-align:center;padding:40px">
          <i class="el-icon-loading" style="font-size:24px;color:#409EFF"></i>
          <p style="color:#999;margin-top:8px">加载中...</p>
        </div>
        <template v-else>
          <el-descriptions :column="1" size="small" border>
            <el-descriptions-item label="合同编号">{{ currentRow.contractNo }}</el-descriptions-item>
            <el-descriptions-item label="合同名称">{{ currentRow.contractName }}</el-descriptions-item>
            <el-descriptions-item label="企业">{{ currentRow.companyName }}</el-descriptions-item>
            <el-descriptions-item label="合同类型">{{ currentRow.contractType }}</el-descriptions-item>
            <el-descriptions-item label="合同金额">{{ currentRow.amount ? currentRow.amount.toLocaleString() : '—' }} 万元</el-descriptions-item>
            <el-descriptions-item label="签订日期">{{ currentRow.signDate || '—' }}</el-descriptions-item>
            <el-descriptions-item v-if="currentRow.counterpartyName" label="对方单位">{{ currentRow.counterpartyName }}</el-descriptions-item>
            <el-descriptions-item v-if="currentRow.effectiveDate" label="生效日期">{{ currentRow.effectiveDate }}</el-descriptions-item>
            <el-descriptions-item v-if="currentRow.expiryDate" label="到期日期">{{ currentRow.expiryDate }}</el-descriptions-item>
            <el-descriptions-item label="法务审核">
              <el-tag :type="currentRow.hasLegalReview==='1'?'success':'danger'" size="mini">
                {{ currentRow.hasLegalReview==='1'?'已审核':'未审核' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="财务审核">
              <el-tag :type="currentRow.hasFinanceReview==='1'?'success':'danger'" size="mini">
                {{ currentRow.hasFinanceReview==='1'?'已审核':'未审核' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="违规类型">
              <span :style="violationTagStyle(currentRow.violationType)" class="tag-pill">{{ currentRow.violationType }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="违规等级">
              <el-tag :type="currentRow.violationLevel==='HIGH'?'danger':currentRow.violationLevel==='MEDIUM'?'warning':''" size="mini">
                {{ currentRow.violationLevel==='HIGH'?'高危':currentRow.violationLevel==='MEDIUM'?'中危':'低危' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="整改状态">
              <el-tag :type="currentRow.rectifyStatus==='CLOSED'?'success':currentRow.rectifyStatus==='PROCESSING'?'warning':'danger'" size="mini">
                {{ currentRow.rectifyStatus==='CLOSED'?'已关闭':currentRow.rectifyStatus==='PROCESSING'?'整改中':'待整改' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>

          <!-- 整改历史记录 -->
          <div v-if="currentRow.rectifyHistory && currentRow.rectifyHistory.length" style="margin-top:20px">
            <div style="font-weight:600;margin-bottom:12px;color:#333">整改记录</div>
            <el-timeline>
              <el-timeline-item
                v-for="(item, idx) in currentRow.rectifyHistory"
                :key="idx"
                :type="item.rectifyStatus==='CLOSED'?'success':item.rectifyStatus==='PROCESSING'?'warning':'danger'"
                :timestamp="item.issuedTime">
                <div style="font-size:13px">
                  <div><strong>责任人：</strong>{{ item.owner }}</div>
                  <div><strong>整改措施：</strong>{{ item.measures }}</div>
                  <div v-if="item.deadline"><strong>期限：</strong>{{ item.deadline }}</div>
                  <div v-if="item.remark"><strong>备注：</strong>{{ item.remark }}</div>
                </div>
              </el-timeline-item>
            </el-timeline>
          </div>

          <div style="margin-top:20px">
            <el-button type="primary" size="small" icon="el-icon-share" @click="showChain(currentRow);detailVisible=false">查看审批链路</el-button>
            <el-button v-if="currentRow.rectifyStatus==='PENDING'" type="warning" size="small" icon="el-icon-edit" @click="handleRectify(currentRow);detailVisible=false">下发整改</el-button>
          </div>
        </template>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getComplianceList, getApprovalChain, submitRectification, getComplianceDetail } from '@/api/stateAssets/contractPenetration'

export default {
  name: 'ContractCompliance',
  data() {
    return {
      list: [],
      query: { companyName: '', contractType: '', violationType: '', rectifyStatus: '' },
      contractTypes: ['采购合同', '销售合同', '工程合同', '租赁合同', '金融合同', '劳务合同', '服务合同'],
      violationTypes: ['未经法务审核', '超权限签约', '未经财务审核', '审批链不完整', '合同要素缺失'],
      rectifyVisible: false,
      rectifyForm: {},
      rectifyLoading: false,
      chainVisible: false,
      chainLoading: false,
      detailVisible: false,
      detailLoading: false,
      currentRow: null,
      trendChart: null,
      violationChart: null,
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
    filteredList() {
      return this.list.filter(r => {
        if (this.query.companyName && !r.companyName.includes(this.query.companyName)) return false
        if (this.query.contractType && r.contractType !== this.query.contractType) return false
        if (this.query.violationType && r.violationType !== this.query.violationType) return false
        if (this.query.rectifyStatus && r.rectifyStatus !== this.query.rectifyStatus) return false
        return true
      })
    },
    statCards() {
      const d = this.list
      const compliant = 0 // 本页展示违规合同，合规总量从API获取
      const violation = d.length
      const pending = d.filter(r => r.rectifyStatus === 'PENDING').length
      const closed = d.filter(r => r.rectifyStatus === 'CLOSED').length
      const rate = 88.5 // 模拟合规率
      return [
        { label: '违规合同', value: violation, color: '#F5222D', icon: 'el-icon-warning' },
        { label: '高危违规', value: d.filter(r => r.violationLevel === 'HIGH').length, color: '#CF1322', icon: 'el-icon-warning-outline' },
        { label: '待整改', value: pending, color: '#FA8C16', icon: 'el-icon-edit' },
        { label: '已关闭', value: closed, color: '#52C41A', icon: 'el-icon-circle-check' },
      ]
    },
    bannerStats() {
      return [
        { label: '总合规率', value: '88.5%' },
        { label: '本月违规', value: this.list.filter(r => r.signDate >= '2026-04-01').length },
        { label: '整改完成', value: this.list.filter(r => r.rectifyStatus === 'CLOSED').length },
      ]
    },
  },
  mounted() {
    this.fetchData()
    this.$nextTick(() => {
      this.initTrendChart()
      this.initViolationChart()
    })
  },
  beforeDestroy() {
    if (this.trendChart) this.trendChart.dispose()
    if (this.violationChart) this.violationChart.dispose()
  },
  methods: {
    async fetchData() {
      try {
        const res = await getComplianceList({ ...this.query, pageNumber: 1, pageSize: 100 })
        if (res && res.result === 200 && res.data) {
          this.list = res.data.tlist || []
        } else {
          this.list = []
        }
      } catch {
        this.list = []
      }
      this.$nextTick(() => {
        this.initViolationChart()
      })
    },
    handleSearch() { this.fetchData() },
    handleReset() {
      this.query = { companyName: '', contractType: '', violationType: '', rectifyStatus: '' }
      this.fetchData()
    },
    tableRowClass({ row }) {
      if (row.violationLevel === 'HIGH') return 'row-violation'
      if (row.violationLevel === 'MEDIUM') return 'row-warning'
      return ''
    },
    contractTypeTagStyle(v) {
      const map = {
        '采购合同': { background: '#EBF1FF', color: '#1677FF', border: '1px solid #ADC6FF' },
        '销售合同': { background: '#F6FFED', color: '#52C41A', border: '1px solid #B7EB8F' },
        '工程合同': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' },
        '租赁合同': { background: '#F9F0FF', color: '#722ED1', border: '1px solid #D3ADF7' },
        '金融合同': { background: '#FFF1F0', color: '#F5222D', border: '1px solid #FFA39E' },
        '劳务合同': { background: '#E8F4FF', color: '#0050A0', border: '1px solid #91CAFF' },
        '服务合同': { background: '#F0FFF4', color: '#389E0D', border: '1px solid #95DE64' },
      }
      return map[v] || {}
    },
    violationTagStyle(v) {
      const map = {
        '未经法务审核': { background: '#FFF1F0', color: '#F5222D', border: '1px solid #FFA39E' },
        '超权限签约': { background: '#FFF1F0', color: '#F5222D', border: '1px solid #FFA39E' },
        '未经财务审核': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' },
        '审批链不完整': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' },
        '合同要素缺失': { background: '#FFFBE6', color: '#FAAD14', border: '1px solid #FFE58F' },
      }
      return map[v] || {}
    },
    async showChain(row) {
      this.currentRow = row
      this.chainVisible = true
      this.chainLoading = true
      try {
        const id = row.contractId || row.contractNo
        const res = await getApprovalChain(id)
        if (res && res.result === 200 && res.data) {
          // 将后端返回的chain数据合并到currentRow
          const chainData = res.data.chain || []
          this.$set(this.currentRow, 'approvalChain', chainData)
          // 补充合同信息
          if (res.data.contractNo) this.$set(this.currentRow, 'contractNo', res.data.contractNo)
          if (res.data.contractName) this.$set(this.currentRow, 'contractName', res.data.contractName)
        }
      } catch (e) {
        this.$message.error('获取审批链路失败')
      } finally {
        this.chainLoading = false
      }
    },
    async showDetail(row) {
      this.currentRow = row
      this.detailVisible = true
      this.detailLoading = true
      try {
        const contractNo = row.contractNo || row.contractId
        const res = await getComplianceDetail(contractNo)
        if (res && res.result === 200 && res.data) {
          // 用后端返回的完整数据更新currentRow
          const detail = res.data
          this.currentRow = {
            ...this.currentRow,
            contractNo: detail.contractNo || this.currentRow.contractNo,
            contractName: detail.contractName || this.currentRow.contractName,
            companyName: detail.companyName || this.currentRow.companyName,
            contractType: detail.contractType || this.currentRow.contractType,
            amount: detail.amount || this.currentRow.amount,
            signDate: detail.signDate || this.currentRow.signDate,
            violationType: detail.violationType || this.currentRow.violationType,
            violationLevel: detail.violationLevel || this.currentRow.violationLevel,
            rectifyStatus: detail.rectifyStatus || this.currentRow.rectifyStatus,
            approvalChain: detail.approvalChain || this.currentRow.approvalChain,
            counterpartyName: detail.counterpartyName,
            effectiveDate: detail.effectiveDate,
            expiryDate: detail.expiryDate,
            hasLegalReview: detail.hasLegalReview,
            hasFinanceReview: detail.hasFinanceReview,
            rectifyHistory: detail.rectifyHistory || []
          }
        }
      } catch (e) {
        this.$message.error('获取合规详情失败')
      } finally {
        this.detailLoading = false
      }
    },
    handleRectify(row) {
      this.rectifyForm = {
        contractNo: row.contractNo,
        contractId: row.contractId,
        violationType: row.violationType,
        measures: '',
        owner: '',
        deadline: '',
        remark: ''
      }
      this.rectifyVisible = true
    },
    async submitRectify() {
      // 表单校验
      if (!this.rectifyForm.measures) {
        this.$message.warning('请填写整改措施')
        return
      }
      if (!this.rectifyForm.owner) {
        this.$message.warning('请填写责任人')
        return
      }
      if (!this.rectifyForm.deadline) {
        this.$message.warning('请选择完成时限')
        return
      }
      this.rectifyLoading = true
      try {
        const res = await submitRectification(this.rectifyForm)
        if (res && res.result === 200) {
          this.$message.success('整改通知已下发')
          this.rectifyVisible = false
          // 刷新列表数据
          this.fetchData()
        } else {
          this.$message.error(res.msg || '下发整改失败')
        }
      } catch (e) {
        this.$message.error('下发整改失败，请稍后重试')
      } finally {
        this.rectifyLoading = false
      }
    },
    getActiveStep(row) {
      if (!row || !row.approvalChain) return 0
      const idx = row.approvalChain.findIndex(s => s.status !== 'done')
      return idx === -1 ? row.approvalChain.length : idx
    },
    initTrendChart() {
      const el = this.$refs.trendChart
      if (!el) return
      if (this.trendChart) this.trendChart.dispose()
      this.trendChart = echarts.init(el)
      this.trendChart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['合规率', '目标基准线'], bottom: 0 },
        grid: { left: 40, right: 20, top: 20, bottom: 40 },
        xAxis: { type: 'category', data: ['11月', '12月', '1月', '2月', '3月', '4月'], axisLine: { lineStyle: { color: '#ddd' } } },
        yAxis: { type: 'value', min: 80, max: 100, axisLabel: { formatter: '{value}%' } },
        series: [
          {
            name: '合规率', type: 'line', smooth: true, symbol: 'circle', symbolSize: 6,
            data: [92.1, 90.8, 89.5, 87.2, 88.0, 88.5],
            itemStyle: { color: '#237804' },
            areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: 'rgba(35,120,4,0.3)' }, { offset: 1, color: 'rgba(35,120,4,0)' }] } },
          },
          {
            name: '目标基准线', type: 'line', symbol: 'none', lineStyle: { type: 'dashed', color: '#FA8C16', width: 2 },
            data: [90, 90, 90, 90, 90, 90], itemStyle: { color: '#FA8C16' },
          },
        ],
      })
    },
    initViolationChart() {
      const el = this.$refs.violationChart
      if (!el) return
      if (this.violationChart) this.violationChart.dispose()
      this.violationChart = echarts.init(el)
      // 动态统计违规类型
      const typeCount = {}
      this.list.forEach(r => { if (r.violationType) typeCount[r.violationType] = (typeCount[r.violationType] || 0) + 1 })
      const colorMap = { '未经法务审核': '#F5222D', '超权限签约': '#F5222D', '未经财务审核': '#FA8C16', '审批链不完整': '#FA8C16', '合同要素缺失': '#FAAD14' }
      const categories = Object.keys(typeCount)
      const values = categories.map(k => ({ value: typeCount[k], itemStyle: { color: colorMap[k] || '#1677FF' } }))
      this.violationChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 100, right: 30, top: 20, bottom: 30 },
        xAxis: { type: 'value' },
        yAxis: { type: 'category', data: categories.length > 0 ? categories : ['未经法务审核', '审批链不完整', '未经财务审核', '超权限签约', '合同要素缺失'] },
        series: [{
          type: 'bar', barWidth: 16,
          data: values,
          label: { show: true, position: 'right', formatter: '{c} 件' },
        }],
      })
    },
  },
}
</script>

<style scoped lang="scss">
.compliance-wrap { padding: 16px; background: #f5f7fa; min-height: 100vh; }

.page-banner {
  border-radius: 8px; padding: 24px 28px; margin-bottom: 16px;
  display: flex; justify-content: space-between; align-items: center;
  .banner-title { font-size: 22px; font-weight: 700; color: #fff; margin-bottom: 6px; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.75); }
  .banner-right { display: flex; gap: 32px; }
  .banner-stat { text-align: center; color: #fff;
    .stat-num { display: block; font-size: 26px; font-weight: 700; }
    .stat-label { font-size: 12px; opacity: 0.8; }
  }
}

.stat-card { border-radius: 8px; }
.stat-card-inner { display: flex; align-items: center; gap: 14px;
  .stat-info { .stat-value { font-size: 26px; font-weight: 700; } .stat-label { font-size: 13px; color: #666; margin-top: 2px; } }
}

.card-header { display: flex; align-items: center; font-weight: 600; }
.tag-pill { display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 12px; }

::v-deep .row-violation td { background: #FFF1F0 !important; }
::v-deep .row-warning td { background: #FFFBE6 !important; }
</style>
