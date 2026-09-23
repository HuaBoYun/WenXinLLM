<template>
  <div class="app-container warning-center-page">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-bell"></i><span>预警中心</span></div>
      <div class="page-header-desc">统一汇聚全领域预警信息 · 红橙黄三级分类 · 自动派单 · 处置状态全程跟踪</div>
      <div class="header-right">
        <el-badge :value="unhandledCount" type="danger">
          <el-button size="small" type="danger" icon="el-icon-s-promotion" @click="handleBatchDispatch">批量派单</el-button>
        </el-badge>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="14" class="stat-row">
      <el-col :span="6" v-for="c in statCards" :key="c.key">
        <div class="stat-card" :class="c.cls" @click="filterByLevel(c.level)">
          <div class="stat-icon"><i :class="c.icon"></i></div>
          <div class="stat-body">
            <div class="stat-value">{{ c.value }}</div>
            <div class="stat-label">{{ c.label }}</div>
          </div>
          <div class="stat-badge" v-if="c.new > 0">+{{ c.new }}新</div>
        </div>
      </el-col>
    </el-row>

    <!-- 筛选 -->
    <el-card shadow="never" class="filter-card">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="预警级别">
          <el-select v-model="queryForm.level" placeholder="全部" clearable style="width:110px">
            <el-option label="🔴 红色" value="RED" /><el-option label="🟠 橙色" value="ORANGE" /><el-option label="🟡 黄色" value="YELLOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="监管领域">
          <el-select v-model="queryForm.domain" placeholder="全部" clearable style="width:130px">
            <el-option label="投资穿透" value="invest" /><el-option label="产权穿透" value="property" />
            <el-option label="财务穿透" value="finance" /><el-option label="薪酬穿透" value="salary" />
            <el-option label="采购穿透" value="procurement" /><el-option label="合同穿透" value="contract" />
            <el-option label="境外穿透" value="overseas" /><el-option label="金融风险" value="financial" />
          </el-select>
        </el-form-item>
        <el-form-item label="处置状态">
          <el-select v-model="queryForm.handleStatus" placeholder="全部" clearable style="width:120px">
            <el-option label="待处置" value="PENDING" /><el-option label="处置中" value="HANDLING" /><el-option label="已处置" value="HANDLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="企业名称"><el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:160px" /></el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 预警列表 -->
    <el-card shadow="never" class="table-card">
      <el-table v-loading="loading" :data="filteredList" border style="width:100%"
        :row-class-name="rowClassName" @selection-change="val => selectedRows = val">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="预警级别" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ RED:'danger', ORANGE:'warning', YELLOW:'' }[row.level]" size="small" effect="dark">
              {{ { RED:'🔴 红色', ORANGE:'🟠 橙色', YELLOW:'🟡 黄色' }[row.level] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="监管领域" prop="domainLabel" width="100" align="center" />
        <el-table-column label="预警类型" prop="warningType" width="130" align="center" />
        <el-table-column label="涉及企业" prop="companyName" min-width="140" show-overflow-tooltip />
        <el-table-column label="预警内容" prop="content" min-width="200" show-overflow-tooltip />
        <el-table-column label="预警时间" prop="warningTime" width="150" align="center" />
        <el-table-column label="处置状态" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ PENDING:'danger', HANDLING:'warning', HANDLED:'success' }[row.handleStatus]" size="small">
              {{ { PENDING:'待处置', HANDLING:'处置中', HANDLED:'已处置' }[row.handleStatus] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template slot-scope="{ row }">
            <el-button size="mini" type="text" @click="handleDetail(row)">详情</el-button>
            <el-divider direction="vertical" />
            <el-button v-if="row.handleStatus === 'PENDING'" size="mini" type="text" style="color:#fa8c16" @click="handleDispatch(row)">派单</el-button>
            <el-button v-else size="mini" type="text" @click="handleTrack(row)">跟踪</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top:12px;text-align:right"
        :current-page="queryForm.pageNumber" :page-sizes="[10,20,50]" :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total"
        @size-change="v => { queryForm.pageSize = v }" @current-change="v => { queryForm.pageNumber = v }" />
    </el-card>

    <!-- 详情抽屉 -->
    <el-drawer title="预警详情" :visible.sync="drawerVisible" size="600px" direction="rtl">
      <div v-if="currentRow" style="padding:0 20px 20px">
        <el-alert :title="`${currentRow.warningType} — ${currentRow.companyName}`"
          :type="{ RED:'error', ORANGE:'warning', YELLOW:'info' }[currentRow.level]" show-icon :closable="false" style="margin:16px 0" />
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="预警级别">
            <el-tag :type="{ RED:'danger', ORANGE:'warning', YELLOW:'' }[currentRow.level]" size="small">{{ { RED:'红色', ORANGE:'橙色', YELLOW:'黄色' }[currentRow.level] }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="监管领域">{{ currentRow.domainLabel }}</el-descriptions-item>
          <el-descriptions-item label="涉及企业">{{ currentRow.companyName }}</el-descriptions-item>
          <el-descriptions-item label="预警时间">{{ currentRow.warningTime }}</el-descriptions-item>
          <el-descriptions-item label="处置状态">
            <el-tag :type="{ PENDING:'danger', HANDLING:'warning', HANDLED:'success' }[currentRow.handleStatus]" size="small">
              {{ { PENDING:'待处置', HANDLING:'处置中', HANDLED:'已处置' }[currentRow.handleStatus] }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="处置责任人">{{ currentRow.owner || '未分配' }}</el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="left">预警详情</el-divider>
        <p style="font-size:13px;color:#333;line-height:1.8;padding:0 4px">{{ currentRow.detail }}</p>
        <el-divider content-position="left">处置建议</el-divider>
        <el-alert :title="currentRow.suggestion" type="warning" show-icon :closable="false" />
        <div v-if="currentRow.handleStatus !== 'PENDING'" style="margin-top:16px">
          <el-divider content-position="left">处置进展</el-divider>
          <el-timeline>
            <el-timeline-item v-for="(t, i) in currentRow.timeline" :key="i" :timestamp="t.time" placement="top" :color="t.color">
              {{ t.content }}
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-drawer>

    <!-- 派单对话框 -->
    <el-dialog title="预警核查派单" :visible.sync="dispatchVisible" width="500px">
      <el-form :model="dispatchForm" label-width="100px" size="small">
        <el-form-item label="预警类型"><el-input :value="dispatchForm.warningType" disabled /></el-form-item>
        <el-form-item label="涉及企业"><el-input :value="dispatchForm.companyName" disabled /></el-form-item>
        <el-form-item label="核查责任人"><el-input v-model="dispatchForm.owner" placeholder="请输入" /></el-form-item>
        <el-form-item label="核查期限">
          <el-date-picker v-model="dispatchForm.deadline" type="date" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
        <el-form-item label="核查要求"><el-input v-model="dispatchForm.requirement" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dispatchVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDispatch">确认派单</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
const MOCK_WARNINGS = [
  { id:1, level:'RED', domain:'invest', domainLabel:'投资穿透', warningType:'越权投资', companyName:'云鑫数字科技股份公司', content:'投资金额5.2亿超出总经理审批权限，属越权投资，董事会审批缺失', warningTime:'2026-04-28 09:15', handleStatus:'PENDING', owner:null, detail:'该项目投资金额52000万元，超出集团总经理审批权限（5000万），且未提交董事会审议，违反"三重一大"集体决策制度。', suggestion:'立即暂停项目推进，补充董事会审批程序，核查是否存在个人擅自决定行为。', timeline:[] },
  { id:2, level:'RED', domain:'finance', domainLabel:'财务穿透', warningType:'疑似小金库', companyName:'示例新能源科技有限公司', content:'发现资金流向个人账户320万元，无合同依据，疑似小金库', warningTime:'2026-04-28 10:30', handleStatus:'HANDLING', owner:'李审计', detail:'2026-04-05，示例新能源科技向张某个人账户转账320万元，用途登记为"咨询费"，但无对应合同，无实际服务记录，疑似体外循环资金。', suggestion:'立即冻结相关账户，调取银行流水，核查资金去向，追究相关责任人责任。', timeline:[{ time:'2026-04-28 11:00', content:'已立案核查，责任人：李审计', color:'#fa8c16' }] },
  { id:3, level:'RED', domain:'procurement', domainLabel:'采购穿透', warningType:'围标串标', companyName:'示例总部大楼改造工程', content:'投标文件相似度88%，三家投标人报价差异仅0.3%，疑似围标串标', warningTime:'2026-04-27 14:20', handleStatus:'PENDING', owner:null, detail:'示例总部大楼改造工程招标中，三家投标人文件相似度达88%，报价差异仅0.3%，明显低于正常市场波动范围，投标保证金来源账户存在关联。', suggestion:'暂停本次招标，启动围标串标专项调查，向监管部门报告。', timeline:[] },
  { id:4, level:'ORANGE', domain:'property', domainLabel:'产权穿透', warningType:'三表数据差异', companyName:'国华物流集团参股子公司', content:'产权登记缺失，工商已登记、财务已并表，违反产权登记前置要求', warningTime:'2026-04-27 16:00', handleStatus:'PENDING', owner:null, detail:'国华物流集团参股子公司已完成工商登记和财务并表，但产权登记系统中无对应记录，违反产权登记前置于工商登记的制度要求。', suggestion:'限期30天内补办产权登记手续，核查是否存在违规新设法人情况。', timeline:[] },
  { id:5, level:'ORANGE', domain:'salary', domainLabel:'薪酬穿透', warningType:'收入畸高', companyName:'示例新能源科技有限公司', content:'陈某薪酬180万元，超行业均值6倍，触发红色预警阈值', warningTime:'2026-04-26 09:00', handleStatus:'HANDLING', owner:'王合规', detail:'陈某年度总薪酬180万元，同岗位行业均值30万元，超标6倍，超出"个人薪酬超平均薪酬5倍"的红色预警阈值。', suggestion:'核查薪酬审批依据，确认是否经党委集体决策，追缴超标薪酬。', timeline:[{ time:'2026-04-26 10:00', content:'已启动薪酬合规核查', color:'#fa8c16' }, { time:'2026-04-27 14:00', content:'已约谈相关负责人', color:'#fa8c16' }] },
  { id:6, level:'ORANGE', domain:'contract', domainLabel:'合同穿透', warningType:'合同审签倒置', companyName:'博云智慧城市运营公司', content:'合同签署日期晚于实际执行日期15天，存在倒签合同风险', warningTime:'2026-04-25 11:30', handleStatus:'HANDLED', owner:'张法务', detail:'博云智慧城市运营公司与某供应商签订的服务合同，签署日期为2026-04-20，但实际服务已于2026-04-05开始执行，存在明显的倒签合同行为。', suggestion:'已完成整改，补充合同审批手续，建立合同签署前置审批机制。', timeline:[{ time:'2026-04-25 12:00', content:'已立案核查', color:'#fa8c16' }, { time:'2026-04-26 09:00', content:'已完成整改，补充审批手续', color:'#67c23a' }] },
  { id:7, level:'YELLOW', domain:'overseas', domainLabel:'境外穿透', warningType:'利润滞留', companyName:'示例东南亚投资公司', content:'境外利润汇回率仅35%，低于50%预警阈值', warningTime:'2026-04-24 15:00', handleStatus:'PENDING', owner:null, detail:'示例东南亚投资公司2025年度实现利润2000万元，但汇回境内仅700万元，汇回率35%，低于集团规定的50%最低汇回率要求。', suggestion:'核查利润滞留原因，确认是否存在外汇管制限制，制定利润汇回计划。', timeline:[] }
]
export default {
  name: 'WarningCenter',
  data() {
    return {
      loading: false, list: [], total: 0, selectedRows: [],
      queryForm: { pageNumber:1, pageSize:10, level:'', domain:'', handleStatus:'', companyName:'' },
      drawerVisible: false, currentRow: null,
      dispatchVisible: false, dispatchForm: { warningType:'', companyName:'', owner:'', deadline:'', requirement:'' }
    }
  },
  computed: {
    unhandledCount() { return this.list.filter(r => r.handleStatus === 'PENDING').length },
    statCards() {
      const red = this.list.filter(r => r.level === 'RED')
      const orange = this.list.filter(r => r.level === 'ORANGE')
      const yellow = this.list.filter(r => r.level === 'YELLOW')
      return [
        { key:'red', label:'红色预警', value: red.length, new: red.filter(r => r.handleStatus === 'PENDING').length, icon:'el-icon-warning', cls:'card-red', level:'RED' },
        { key:'orange', label:'橙色预警', value: orange.length, new: orange.filter(r => r.handleStatus === 'PENDING').length, icon:'el-icon-warning-outline', cls:'card-orange', level:'ORANGE' },
        { key:'yellow', label:'黄色预警', value: yellow.length, new: yellow.filter(r => r.handleStatus === 'PENDING').length, icon:'el-icon-info', cls:'card-yellow', level:'YELLOW' },
        { key:'handled', label:'已处置', value: this.list.filter(r => r.handleStatus === 'HANDLED').length, new:0, icon:'el-icon-circle-check', cls:'card-green', level:'' }
      ]
    },
    filteredList() {
      return this.list.filter(r => {
        const levelOk = !this.queryForm.level || r.level === this.queryForm.level
        const domainOk = !this.queryForm.domain || r.domain === this.queryForm.domain
        const statusOk = !this.queryForm.handleStatus || r.handleStatus === this.queryForm.handleStatus
        const nameOk = !this.queryForm.companyName || r.companyName.includes(this.queryForm.companyName)
        return levelOk && domainOk && statusOk && nameOk
      })
    }
  },
  created() { this.fetchData() },
  methods: {
    fetchData() { this.loading = true; setTimeout(() => { this.list = MOCK_WARNINGS; this.total = MOCK_WARNINGS.length; this.loading = false }, 300) },
    handleQuery() { this.queryForm.pageNumber = 1 },
    resetQuery() { this.queryForm = { pageNumber:1, pageSize:10, level:'', domain:'', handleStatus:'', companyName:'' } },
    filterByLevel(level) { this.queryForm.level = level },
    rowClassName({ row }) {
      if (row.level === 'RED' && row.handleStatus === 'PENDING') return 'row-danger'
      if (row.level === 'ORANGE' && row.handleStatus === 'PENDING') return 'row-warn'
      return ''
    },
    handleDetail(row) { this.currentRow = row; this.drawerVisible = true },
    handleTrack(row) { this.currentRow = row; this.drawerVisible = true },
    handleDispatch(row) {
      this.currentRow = row
      this.dispatchForm = { warningType: row.warningType, companyName: row.companyName, owner:'', deadline:'', requirement:'' }
      this.dispatchVisible = true
    },
    handleBatchDispatch() {
      if (!this.selectedRows.length) { this.$message.warning('请先选择预警记录'); return }
      this.$message.success(`已批量派单 ${this.selectedRows.length} 条预警记录`)
    },
    submitDispatch() {
      if (!this.dispatchForm.owner || !this.dispatchForm.deadline) { this.$message.warning('请填写完整派单信息'); return }
      const row = this.list.find(r => r.id === this.currentRow.id)
      if (row) { row.handleStatus = 'HANDLING'; row.owner = this.dispatchForm.owner }
      this.$message.success(`预警核查派单已提交：${this.dispatchForm.warningType}`)
      this.dispatchVisible = false
    }
  }
}
</script>
<style lang="scss" scoped>
.warning-center-page { padding:16px; background:#f0f2f5; min-height:calc(100vh - 84px); }
.page-header { display:flex; align-items:center; justify-content:space-between; margin-bottom:14px; padding:14px 20px; background:linear-gradient(135deg,#1a3a5c 0%,#ff4d4f 100%); border-radius:6px; color:#fff;
  .page-header-left { display:flex; align-items:center; font-size:16px; font-weight:600; i { font-size:22px; margin-right:10px; } }
  .page-header-desc { font-size:13px; opacity:.85; flex:1; margin:0 20px; }
}
.stat-row { margin-bottom:14px; }
.stat-card { display:flex; align-items:center; padding:16px; border-radius:8px; background:#fff; box-shadow:0 1px 4px rgba(0,0,0,.08); cursor:pointer; position:relative; transition:box-shadow .2s;
  &:hover { box-shadow:0 4px 12px rgba(0,0,0,.15); }
  .stat-icon { font-size:30px; padding:10px; border-radius:8px; margin-right:12px; }
  .stat-value { font-size:26px; font-weight:bold; }
  .stat-label { font-size:12px; color:#909399; margin-top:2px; }
  .stat-badge { position:absolute; top:8px; right:8px; background:#ff4d4f; color:#fff; font-size:11px; padding:2px 6px; border-radius:10px; }
  &.card-red    { .stat-icon { color:#ff4d4f; background:#fff1f0; } .stat-value { color:#ff4d4f; } }
  &.card-orange { .stat-icon { color:#fa8c16; background:#fff7e6; } .stat-value { color:#fa8c16; } }
  &.card-yellow { .stat-icon { color:#faad14; background:#fffbe6; } .stat-value { color:#faad14; } }
  &.card-green  { .stat-icon { color:#52c41a; background:#f6ffed; } .stat-value { color:#52c41a; } }
}
.filter-card { margin-bottom:12px; }
.table-card { margin-top:0; }
::v-deep .row-danger td { background:#fff1f0 !important; }
::v-deep .row-warn td { background:#fff7e6 !important; }
::v-deep .el-table th { background:#fff1f0; }
::v-deep .el-card { border-radius:6px; }
</style>

