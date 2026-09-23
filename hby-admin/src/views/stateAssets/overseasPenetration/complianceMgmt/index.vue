<template>
  <div class="app-container overseas-page" :style="themeVars">
    <!-- Banner -->
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-document-checked"></i><span>合规管理</span></div>
      <div class="page-header-desc">境外企业合规检查、反腐败与出口管制合规穿透管理</div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" style="margin-bottom:14px">
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" :style="{ background: ipLightBg }"><i class="el-icon-document-checked" :style="{ color: ipSecondary, fontSize: '26px' }"></i></div>
            <div class="kpi-info">
              <div class="kpi-value">{{ stats.total }}</div>
              <div class="kpi-label">合规事项总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#F6FFED"><i class="el-icon-circle-check" style="color:#52C41A;font-size:26px"></i></div>
            <div class="kpi-info">
              <div class="kpi-value" style="color:#52C41A">{{ stats.compliant }}</div>
              <div class="kpi-label">已合规</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#FFF1F0"><i class="el-icon-warning" style="color:#F5222D;font-size:26px"></i></div>
            <div class="kpi-info">
              <div class="kpi-value" style="color:#F5222D">{{ stats.nonCompliant }}</div>
              <div class="kpi-label">不合规</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#FFF7E6"><i class="el-icon-time" style="color:#FA8C16;font-size:26px"></i></div>
            <div class="kpi-info">
              <div class="kpi-value" style="color:#FA8C16">{{ stats.pending }}</div>
              <div class="kpi-label">待评估</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询栏 -->
    <el-card shadow="never" style="margin-bottom:10px">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.unitName" placeholder="请输入" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="合规类型">
          <el-select v-model="queryForm.complianceType" placeholder="请选择" clearable style="width:130px">
            <el-option label="反腐败" value="ANTI_CORRUPTION" />
            <el-option label="出口管制" value="EXPORT_CONTROL" />
            <el-option label="数据保护" value="DATA_PROTECTION" />
            <el-option label="劳工合规" value="LABOR" />
            <el-option label="制裁合规" value="SANCTION" />
          </el-select>
        </el-form-item>
        <el-form-item label="合规状态">
          <el-select v-model="queryForm.complianceStatus" placeholder="请选择" clearable style="width:120px">
            <el-option label="合规" value="COMPLIANT" />
            <el-option label="不合规" value="NON_COMPLIANT" />
            <el-option label="待评估" value="PENDING" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table
        v-loading="loading"
        :data="filteredList"
        border
        style="width:100%"
        :header-cell-style="{ background: ipLightBg, color: ipSecondary }"
      >
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="企业名称" prop="unitName" min-width="160" show-overflow-tooltip />
        <el-table-column label="所在国家" prop="country" width="100" align="center" />
        <el-table-column label="合规类型" prop="complianceType" width="110" align="center">
          <template slot-scope="scope">
            <el-tag type="info" size="small">{{ typeLabel(scope.row.complianceType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="合规状态" prop="complianceStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="statusTagType(scope.row.complianceStatus)" size="small">
              {{ statusLabel(scope.row.complianceStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="最近检查日期" prop="lastCheckDate" width="130" align="center" />
        <el-table-column label="下次检查日期" prop="nextCheckDate" width="130" align="center" />
        <el-table-column label="问题数" prop="issueCount" width="80" align="center">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.issueCount > 0 ? '#F5222D' : '#52C41A', fontWeight: 'bold' }">
              {{ scope.row.issueCount || 0 }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="负责人" prop="leader" width="90" align="center" />
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">详情</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color:#F5222D" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        style="margin-top:15px;text-align:right"
        :current-page="queryForm.pageNumber"
        :page-sizes="[10, 20, 50]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="val => { queryForm.pageSize = val; queryForm.pageNumber = 1 }"
        @current-change="val => { queryForm.pageNumber = val }"
      />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      :title="dialogType === 'add' ? '新增合规事项' : '编辑合规事项'"
      :visible.sync="dialogVisible"
      width="620px"
      :close-on-click-modal="false"
    >
      <el-form :model="form" :rules="rules" ref="form" label-width="110px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="企业名称" prop="unitName">
              <el-input v-model="form.unitName" placeholder="请输入企业名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在国家" prop="country">
              <el-input v-model="form.country" placeholder="请输入国家" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合规类型" prop="complianceType">
              <el-select v-model="form.complianceType" placeholder="请选择" style="width:100%">
                <el-option label="反腐败" value="ANTI_CORRUPTION" />
                <el-option label="出口管制" value="EXPORT_CONTROL" />
                <el-option label="数据保护" value="DATA_PROTECTION" />
                <el-option label="劳工合规" value="LABOR" />
                <el-option label="制裁合规" value="SANCTION" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合规状态" prop="complianceStatus">
              <el-select v-model="form.complianceStatus" placeholder="请选择" style="width:100%">
                <el-option label="合规" value="COMPLIANT" />
                <el-option label="不合规" value="NON_COMPLIANT" />
                <el-option label="待评估" value="PENDING" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最近检查日期" prop="lastCheckDate">
              <el-date-picker v-model="form.lastCheckDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下次检查日期">
              <el-date-picker v-model="form.nextCheckDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="问题数量" prop="issueCount">
              <el-input-number v-model="form.issueCount" :min="0" :max="99" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="leader">
              <el-input v-model="form.leader" placeholder="请输入负责人" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注说明">
              <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 详情抽屉 -->
    <el-drawer title="合规详情" :visible.sync="drawerVisible" direction="rtl" size="520px">
      <div class="drawer-content" v-if="currentRow">
        <div class="drawer-section">
          <div class="drawer-section-title">基本信息</div>
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="企业名称">{{ currentRow.unitName }}</el-descriptions-item>
            <el-descriptions-item label="所在国家">{{ currentRow.country }}</el-descriptions-item>
            <el-descriptions-item label="合规类型">
              <el-tag type="info" size="small">{{ typeLabel(currentRow.complianceType) }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="合规状态">
              <el-tag :type="statusTagType(currentRow.complianceStatus)" size="small">
                {{ statusLabel(currentRow.complianceStatus) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="最近检查日期">{{ currentRow.lastCheckDate }}</el-descriptions-item>
            <el-descriptions-item label="下次检查日期">{{ currentRow.nextCheckDate }}</el-descriptions-item>
            <el-descriptions-item label="问题数量">
              <span :style="{ color: currentRow.issueCount > 0 ? '#F5222D' : '#52C41A' }">{{ currentRow.issueCount }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="负责人">{{ currentRow.leader }}</el-descriptions-item>
            <el-descriptions-item label="备注说明" :span="2">{{ currentRow.remark || '暂无' }}</el-descriptions-item>
          </el-descriptions>
        </div>
        <div class="drawer-section" style="margin-top:20px">
          <div class="drawer-section-title">整改进度</div>
          <el-steps :active="currentRow.rectStep || 0" align-center finish-status="success" style="margin-top:16px">
            <el-step title="发现问题" icon="el-icon-search" />
            <el-step title="制定方案" icon="el-icon-document" />
            <el-step title="整改实施" icon="el-icon-setting" />
            <el-step title="复核验收" icon="el-icon-check" />
            <el-step title="归档完结" icon="el-icon-folder-checked" />
          </el-steps>
        </div>
        <div class="drawer-section" style="margin-top:20px">
          <div class="drawer-section-title">问题清单</div>
          <el-table :data="currentRow.issues || []" size="small" border style="margin-top:10px">
            <el-table-column label="问题描述" prop="desc" min-width="160" show-overflow-tooltip />
            <el-table-column label="严重程度" prop="level" width="90" align="center">
              <template slot-scope="scope">
                <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }[scope.row.level]" size="mini">
                  {{ { HIGH: '严重', MEDIUM: '一般', LOW: '轻微' }[scope.row.level] || '-' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="整改状态" prop="status" width="90" align="center">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === 'DONE' ? 'success' : 'danger'" size="mini">
                  {{ scope.row.status === 'DONE' ? '已整改' : '待整改' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getOverseasComplianceList, saveOverseasCompliance, deleteOverseasCompliance } from '@/api/stateAssets/overseasPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'OverseasComplianceMgmt',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      submitLoading: false,
      list: [],
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 10, unitName: '', complianceType: '', complianceStatus: '' },
      dialogVisible: false,
      dialogType: 'add',
      drawerVisible: false,
      currentRow: null,
      form: {},
      rules: {
        unitName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        complianceType: [{ required: true, message: '请选择合规类型', trigger: 'change' }],
        complianceStatus: [{ required: true, message: '请选择合规状态', trigger: 'change' }],
        lastCheckDate: [{ required: true, message: '请选择检查日期', trigger: 'change' }],
        leader: [{ required: true, message: '请输入负责人', trigger: 'blur' }]
      }
    }
  },
  computed: {
    stats() {
      const d = this.list
      return {
        total: d.length,
        compliant: d.filter(r => r.complianceStatus === 'COMPLIANT').length,
        nonCompliant: d.filter(r => r.complianceStatus === 'NON_COMPLIANT').length,
        pending: d.filter(r => r.complianceStatus === 'PENDING').length
      }
    },
    filteredList() {
      let data = this.list
      if (this.queryForm.unitName) data = data.filter(r => r.unitName.includes(this.queryForm.unitName))
      if (this.queryForm.complianceType) data = data.filter(r => r.complianceType === this.queryForm.complianceType)
      if (this.queryForm.complianceStatus) data = data.filter(r => r.complianceStatus === this.queryForm.complianceStatus)
      this.total = data.length
      const s = (this.queryForm.pageNumber - 1) * this.queryForm.pageSize
      return data.slice(s, s + this.queryForm.pageSize)
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getOverseasComplianceList(this.queryForm)
        if (res && res.result === 200) {
          this.list = (res.data && res.data.tlist) || []
        } else {
          this.list = []
        }
      } catch (e) {
        this.list = []
      } finally {
        this.loading = false
      }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryForm.pageNumber = 1; this.fetchData() },
    handleAdd() {
      this.dialogType = 'add'
      this.form = { issueCount: 0 }
      this.dialogVisible = true
      this.$nextTick(() => this.$refs.form && this.$refs.form.clearValidate())
    },
    handleEdit(row) {
      this.dialogType = 'edit'
      this.form = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => this.$refs.form && this.$refs.form.clearValidate())
    },
    handleView(row) {
      this.currentRow = row
      this.drawerVisible = true
    },
    async handleDelete(row) {
      try {
        await this.$confirm(`确认删除「${row.unitName}」的合规记录？`, '提示', { type: 'warning' })
        const res = await deleteOverseasCompliance(row.complianceId)
        if (res && res.result === 200) {
          this.$message.success('删除成功')
          this.fetchData()
        } else {
          this.$message.error((res && res.msg) || '删除失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('删除失败')
      }
    },
    async handleSubmit() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.submitLoading = true
        try {
          const res = await saveOverseasCompliance(this.form)
          if (res && res.result === 200) {
            this.$message.success(this.dialogType === 'add' ? '新增成功' : '编辑成功')
            this.dialogVisible = false
            this.fetchData()
          } else {
            this.$message.error((res && res.msg) || '操作失败')
          }
        } catch (e) {
          this.$message.error('操作失败，请稍后重试')
        } finally {
          this.submitLoading = false
        }
      })
    },
    typeLabel(v) {
      return { ANTI_CORRUPTION: '反腐败', EXPORT_CONTROL: '出口管制', DATA_PROTECTION: '数据保护', LABOR: '劳工合规', SANCTION: '制裁合规' }[v] || v
    },
    statusTagType(v) { return { COMPLIANT: 'success', NON_COMPLIANT: 'danger', PENDING: 'warning' }[v] || 'info' },
    statusLabel(v) { return { COMPLIANT: '合规', NON_COMPLIANT: '不合规', PENDING: '待评估' }[v] || v }
  }
}
</script>

<style lang="scss" scoped>
.overseas-page {
  padding: 16px;
  background: #f0f2f5;
  min-height: calc(100vh - 84px);
}
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 14px;
  padding: 14px 20px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 50%, var(--ip-bright, #1677FF) 100%);
  border-radius: 6px;
  color: #fff;
}
.page-header-left {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 600;
  i { font-size: 22px; margin-right: 10px; }
}
.page-header-desc { font-size: 13px; opacity: 0.85; }
.kpi-card { display: flex; align-items: center; }
.kpi-icon-wrap {
  width: 50px; height: 50px; border-radius: 10px;
  display: flex; align-items: center; justify-content: center;
  margin-right: 14px; flex-shrink: 0;
}
.kpi-info { flex: 1; }
.kpi-value { font-size: 28px; font-weight: bold; color: #303133; line-height: 1; }
.kpi-label { font-size: 13px; color: #909399; margin-top: 5px; }
::v-deep .el-card { border-radius: 6px; }
.drawer-content { padding: 16px 20px; }
.drawer-section-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--ip-secondary, #0050A0);
  border-left: 3px solid var(--ip-bright, #1677FF);
  padding-left: 10px;
  margin-bottom: 12px;
}
</style>
