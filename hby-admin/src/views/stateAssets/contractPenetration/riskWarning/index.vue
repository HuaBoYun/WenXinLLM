<template>
  <div class="risk-wrap">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title">合同风险预警管理</div>
        <div class="banner-sub">汇聚合同领域所有风险预警事件，支持分级处置与全程追踪，高危预警实时告警</div>
      </div>
      <div class="banner-stats">
        <div class="b-stat" v-for="s in bannerQuadrants" :key="s.label">
          <span class="bq-num" :style="{color:s.color}">{{ s.value }}</span>
          <span class="bq-label">{{ s.label }}</span>
        </div>
      </div>
    </div>

    <!-- 统计卡 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col v-for="card in statCards" :key="card.label" :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-card-inner">
            <i :class="card.icon" :style="{color:card.color,fontSize:'28px'}"></i>
            <div class="stat-info">
              <div class="stat-value" :style="{color:card.color}">{{ card.value }}</div>
              <div class="stat-label">{{ card.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询栏 -->
    <el-card shadow="never" style="margin-bottom:16px">
      <el-row :gutter="12">
        <el-col :span="4">
          <el-input v-model="query.warnNo" placeholder="预警编号" clearable size="small"/>
        </el-col>
        <el-col :span="4">
          <el-input v-model="query.companyName" placeholder="企业名称" clearable size="small"/>
        </el-col>
        <el-col :span="4">
          <el-select v-model="query.level" placeholder="预警级别" clearable size="small" style="width:100%">
            <el-option label="高危" value="HIGH"/>
            <el-option label="中危" value="MEDIUM"/>
            <el-option label="低危" value="LOW"/>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="query.status" placeholder="处置状态" clearable size="small" style="width:100%">
            <el-option label="待处置" value="PENDING"/>
            <el-option label="处置中" value="PROCESSING"/>
            <el-option label="已关闭" value="CLOSED"/>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" size="small" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button size="small" icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 预警列表 -->
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>预警列表</span>
        <el-tag size="small" type="danger" style="margin-left:8px">{{ filteredList.length }} 条</el-tag>
      </div>
      <el-table
        :data="filteredList"
        :row-class-name="tableRowClass"
        border size="small" style="width:100%">
        <el-table-column label="预警编号" prop="warnNo" width="170" fixed/>
        <el-table-column label="预警时间" prop="warnTime" width="155"/>
        <el-table-column label="企业" prop="companyName" width="130" show-overflow-tooltip/>
        <el-table-column label="合同名称" prop="contractName" min-width="170" show-overflow-tooltip/>
        <el-table-column label="预警类型" prop="warnType" min-width="160" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span style="font-size:12px;color:#333">{{ row.warnType }}</span>
          </template>
        </el-table-column>
        <el-table-column label="风险级别" prop="level" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.level==='HIGH'?'danger':row.level==='MEDIUM'?'warning':''" size="mini">
              {{ row.level==='HIGH'?'高危':row.level==='MEDIUM'?'中危':'低危' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险评分" prop="score" width="90" align="center">
          <template slot-scope="{row}">
            <span :style="{fontWeight:'700',color:row.score>=80?'#F5222D':row.score>=60?'#FA8C16':'#52C41A'}">{{ row.score }}</span>
          </template>
        </el-table-column>
        <el-table-column label="处置状态" prop="status" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.status==='CLOSED'?'success':row.status==='PROCESSING'?'warning':'danger'" size="mini">
              {{ row.status==='CLOSED'?'已关闭':row.status==='PROCESSING'?'处置中':'待处置' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="负责人" prop="owner" width="90"/>
        <el-table-column label="操作" width="160" fixed="right">
          <template slot-scope="{row}">
            <el-button v-if="row.status==='PENDING'" type="text" size="mini" style="color:#F5222D" icon="el-icon-edit" @click="handleDispose(row)">处置</el-button>
            <el-button type="text" size="mini" icon="el-icon-view" @click="showDetail(row)">详情</el-button>
            <el-button v-if="row.status!=='CLOSED'" type="text" size="mini" style="color:#999" @click="closeWarning(row)">关闭</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 处置弹窗 -->
    <el-dialog title="预警处置" :visible.sync="disposeVisible" width="500px" append-to-body>
      <div v-if="currentRow" style="margin-bottom:16px">
        <el-alert :title="'【'+currentRow.level_label+'】'+currentRow.warnType" :type="currentRow.level==='HIGH'?'error':'warning'" :closable="false" show-icon/>
      </div>
      <el-form :model="disposeForm" label-width="100px" size="small">
        <el-form-item label="处置措施">
          <el-input v-model="disposeForm.measures" type="textarea" :rows="3" placeholder="请填写处置措施"/>
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="disposeForm.owner" placeholder="请输入负责人姓名"/>
        </el-form-item>
        <el-form-item label="计划完成">
          <el-date-picker v-model="disposeForm.deadline" type="date" placeholder="选择计划完成日期" style="width:100%" value-format="yyyy-MM-dd"/>
        </el-form-item>
        <el-form-item label="处置说明">
          <el-input v-model="disposeForm.remark" type="textarea" :rows="2" placeholder="补充说明"/>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="disposeVisible=false">取消</el-button>
        <el-button type="primary" size="small" @click="submitDispose">确认处置</el-button>
      </div>
    </el-dialog>

    <!-- 详情抽屉 -->
    <el-drawer title="预警详情" :visible.sync="detailVisible" size="460px" append-to-body>
      <div v-if="currentRow" style="padding:20px">
        <el-descriptions :column="1" size="small" border style="margin-bottom:20px">
          <el-descriptions-item label="预警编号">{{ currentRow.warnNo }}</el-descriptions-item>
          <el-descriptions-item label="预警时间">{{ currentRow.warnTime }}</el-descriptions-item>
          <el-descriptions-item label="企业">{{ currentRow.companyName }}</el-descriptions-item>
          <el-descriptions-item label="合同名称">{{ currentRow.contractName }}</el-descriptions-item>
          <el-descriptions-item label="预警类型">{{ currentRow.warnType }}</el-descriptions-item>
          <el-descriptions-item label="风险级别">
            <el-tag :type="currentRow.level==='HIGH'?'danger':currentRow.level==='MEDIUM'?'warning':''" size="mini">
              {{ currentRow.level==='HIGH'?'高危':currentRow.level==='MEDIUM'?'中危':'低危' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="风险评分">
            <span :style="{fontWeight:'700',color:currentRow.score>=80?'#F5222D':currentRow.score>=60?'#FA8C16':'#52C41A'}">{{ currentRow.score }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="处置状态">
            <el-tag :type="currentRow.status==='CLOSED'?'success':currentRow.status==='PROCESSING'?'warning':'danger'" size="mini">
              {{ currentRow.status==='CLOSED'?'已关闭':currentRow.status==='PROCESSING'?'处置中':'待处置' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="负责人">{{ currentRow.owner || '—' }}</el-descriptions-item>
          <el-descriptions-item v-if="currentRow.measures" label="处置措施">{{ currentRow.measures }}</el-descriptions-item>
          <el-descriptions-item v-if="currentRow.deadline" label="计划完成">{{ currentRow.deadline }}</el-descriptions-item>
        </el-descriptions>

        <div style="font-weight:600;margin-bottom:12px;color:#333">处置进度</div>
        <el-timeline v-if="currentRow.timeline && currentRow.timeline.length">
          <el-timeline-item
            v-for="(item, idx) in currentRow.timeline"
            :key="idx"
            :type="idx===0?'danger':idx===currentRow.timeline.length-1&&currentRow.status==='CLOSED'?'success':'warning'"
            :timestamp="item.action">
            <div style="font-size:13px;color:#666">{{ item.desc }}</div>
            <div v-if="item.time" style="font-size:12px;color:#999;margin-top:2px">{{ item.time }}</div>
          </el-timeline-item>
        </el-timeline>
        <el-timeline v-else>
          <el-timeline-item type="danger" timestamp="预警触发">
            <div style="font-size:13px;color:#666">系统自动识别风险：{{ currentRow.warnType }}</div>
          </el-timeline-item>
          <el-timeline-item v-if="currentRow.status!=='PENDING'" type="warning" timestamp="开始处置">
            <div style="font-size:13px;color:#666">已分配负责人：{{ currentRow.owner }}，正在处置中</div>
          </el-timeline-item>
          <el-timeline-item v-if="currentRow.status==='CLOSED'" type="success" timestamp="处置完成">
            <div style="font-size:13px;color:#666">预警已关闭，处置完成</div>
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getContractWarningList, handleContractWarning, closeContractWarning, getContractWarningDetail } from '@/api/stateAssets/contractPenetration'
import { mapGetters } from 'vuex'

export default {
  name: 'ContractRiskWarning',
  data() {
    return {
      list: [],
      query: { warnNo: '', companyName: '', level: '', status: '' },
      disposeVisible: false,
      disposeForm: {},
      detailVisible: false,
      currentRow: null,
      loading: false,
    }
  },
  computed: {
    filteredList() {
      // 后端已做过滤，前端直接返回列表
      return this.list
    },
    statCards() {
      const d = this.list
      return [
        { label: '预警总数', value: d.length, color: '#1677FF', icon: 'el-icon-bell' },
        { label: '高危预警', value: d.filter(r => r.level === 'HIGH').length, color: '#F5222D', icon: 'el-icon-warning' },
        { label: '待处置', value: d.filter(r => r.status === 'PENDING').length, color: '#FA8C16', icon: 'el-icon-time' },
        { label: '已关闭', value: d.filter(r => r.status === 'CLOSED').length, color: '#52C41A', icon: 'el-icon-circle-check' },
      ]
    },
    bannerQuadrants() {
      const d = this.list
      return [
        { label: '高危', value: d.filter(r => r.level === 'HIGH').length, color: '#ff7875' },
        { label: '中危', value: d.filter(r => r.level === 'MEDIUM').length, color: '#ffc069' },
        { label: '低危', value: d.filter(r => r.level === 'LOW').length, color: '#fffb8f' },
        { label: '处置中', value: d.filter(r => r.status === 'PROCESSING').length, color: '#85a5ff' },
      ]
    },
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
  mounted() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getContractWarningList({ ...this.query, pageNumber: 1, pageSize: 200 })
        if (res && res.result === 200 && res.data) {
          this.list = res.data.tlist || []
        } else {
          this.list = []
          this.$message.warning('暂无预警数据')
        }
      } catch (e) {
        this.list = []
        this.$message.error('获取预警列表失败')
      } finally {
        this.loading = false
      }
    },
    handleSearch() { this.fetchData() },
    handleReset() { this.query = { warnNo: '', companyName: '', level: '', status: '' }; this.fetchData() },
    tableRowClass({ row }) {
      if (row.level === 'HIGH') return 'row-high'
      if (row.level === 'MEDIUM') return 'row-medium'
      return ''
    },
    handleDispose(row) {
      this.currentRow = { ...row, level_label: row.level === 'HIGH' ? '高危' : row.level === 'MEDIUM' ? '中危' : '低危' }
      this.disposeForm = { measures: '', owner: '', deadline: '', remark: '' }
      this.disposeVisible = true
    },
    async submitDispose() {
      if (!this.disposeForm.measures) {
        this.$message.warning('请填写处置措施')
        return
      }
      try {
        const res = await handleContractWarning({ warnNo: this.currentRow.warnNo, ...this.disposeForm })
        if (res && res.result === 200) {
          this.$message.success('处置已提交')
          this.disposeVisible = false
          this.fetchData() // 重新拉取数据确保状态一致
        } else {
          this.$message.error(res.msg || '处置失败')
        }
      } catch (e) {
        this.$message.error('处置请求失败')
      }
    },
    async closeWarning(row) {
      try {
        await this.$confirm('确认关闭该预警？关闭后将不再显示为待处置状态。', '确认关闭', { type: 'warning' })
      } catch { return }
      try {
        const res = await closeContractWarning(row.warnNo)
        if (res && res.result === 200) {
          this.$message.success('预警已关闭')
          this.fetchData() // 重新拉取数据确保状态一致
        } else {
          this.$message.error(res.msg || '关闭失败')
        }
      } catch (e) {
        this.$message.error('关闭请求失败')
      }
    },
    async showDetail(row) {
      this.currentRow = { ...row, level_label: row.level === 'HIGH' ? '高危' : row.level === 'MEDIUM' ? '中危' : '低危' }
      // 尝试获取详情（含处置时间线）
      try {
        const res = await getContractWarningDetail(row.warnNo)
        if (res && res.result === 200 && res.data) {
          this.currentRow = { ...this.currentRow, ...res.data }
        }
      } catch (e) {
        // 详情接口失败时仍使用列表数据
      }
      this.detailVisible = true
    },
  },
}
</script>

<style scoped lang="scss">
.risk-wrap { padding: 16px; background: #f5f7fa; min-height: 100vh; }

.page-banner {
  border-radius: 8px; padding: 24px 28px; margin-bottom: 16px;
  display: flex; justify-content: space-between; align-items: center;
  .banner-title { font-size: 22px; font-weight: 700; color: #fff; margin-bottom: 6px; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.8); }
  .banner-stats { display: flex; gap: 28px; }
  .b-stat { text-align: center; background: rgba(255,255,255,0.15); border-radius: 8px; padding: 10px 18px;
    .bq-num { display: block; font-size: 28px; font-weight: 800; }
    .bq-label { font-size: 12px; color: rgba(255,255,255,0.85); }
  }
}

.stat-card { border-radius: 8px; }
.stat-card-inner { display: flex; align-items: center; gap: 14px;
  .stat-info { .stat-value { font-size: 26px; font-weight: 700; } .stat-label { font-size: 13px; color: #666; margin-top: 2px; } }
}

.card-header { display: flex; align-items: center; font-weight: 600; }

::v-deep .row-high td { background: #FFF1F0 !important; }
::v-deep .row-medium td { background: #FFFBE6 !important; }
</style>
