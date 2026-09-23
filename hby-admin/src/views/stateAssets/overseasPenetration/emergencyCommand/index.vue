<template>
  <div class="app-container overseas-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-warning-outline"></i><span>境外应急指挥中心</span></div>
      <div class="page-header-desc">境外应急事件管理与处置跟踪，全流程穿透监控</div>
    </div>

    <!-- 应急统计卡片 -->
    <el-row :gutter="16" style="margin-bottom:14px">
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" :style="{ background: ipLightBg }"><i class="el-icon-document" :style="{ color: ipSecondary, fontSize: '26px' }"></i></div>
            <div class="kpi-info"><div class="kpi-value">{{ emergencyStats.total }}</div><div class="kpi-label">事件总数</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#FFF1F0"><i class="el-icon-warning" style="color:#F5222D;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#F5222D">{{ emergencyStats.pending }}</div><div class="kpi-label">待处置</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#FFF7E6"><i class="el-icon-loading" style="color:#FA8C16;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#FA8C16">{{ emergencyStats.processing }}</div><div class="kpi-label">处置中</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#F6FFED"><i class="el-icon-circle-check" style="color:#52C41A;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#52C41A">{{ emergencyStats.done }}</div><div class="kpi-label">已处置</div></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="所在国家">
          <el-input v-model="queryForm.country" placeholder="请输入国家" clearable style="width: 140px" />
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="queryForm.riskLevel" placeholder="全部" clearable style="width: 120px">
            <el-option label="高" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="处置状态">
          <el-select v-model="queryForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="待处置" value="pending" />
            <el-option label="处置中" value="processing" />
            <el-option label="已处置" value="done" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top: 10px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" size="small" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">批量删除</el-button>
        <el-button type="warning" size="small" icon="el-icon-document" style="margin-left: 8px" @click="handlePlan">应急预案管理</el-button>
      </div>
      <el-table v-loading="loading" :data="filteredList" border @selection-change="val => multipleSelection = val" style="width: 100%"
        :header-cell-style="{ background: ipLightBg, color: ipSecondary }">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="事件名称" prop="eventName" min-width="180" show-overflow-tooltip />
        <el-table-column label="所在国家" prop="country" width="120" align="center" />
        <el-table-column label="风险等级" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="riskTagType(scope.row.riskLevel)" size="small">{{ riskLabel(scope.row.riskLevel) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发生时间" prop="occurTime" width="160" align="center" />
        <el-table-column label="处置状态" width="110" align="center">
          <template slot-scope="scope">
            <el-tag :type="statusTagType(scope.row.status)" size="small">{{ statusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="负责人" prop="leader" width="100" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="dialogType = 'view'; form = { ...scope.row }; dialogVisible = true">查看</el-button>
            <el-button size="mini" type="text" @click="dialogType = 'edit'; form = { ...scope.row }; dialogVisible = true">编辑</el-button>
            <el-button size="mini" type="text" style="color: #F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top: 15px; text-align: right"
        :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]"
        :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="val => { queryForm.pageSize = val; fetchData() }"
        @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
    </el-card>

    <el-dialog :title="{ add: '新增应急事件', edit: '编辑应急事件', view: '查看应急事件' }[dialogType]"
      :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="事件名称" prop="eventName">
          <el-input v-model="form.eventName" placeholder="请输入事件名称" :disabled="dialogType === 'view'" />
        </el-form-item>
        <el-form-item label="所在国家" prop="country">
          <el-input v-model="form.country" placeholder="请输入所在国家" :disabled="dialogType === 'view'" />
        </el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="form.riskLevel" placeholder="请选择" :disabled="dialogType === 'view'" style="width: 100%">
            <el-option label="高" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="发生时间" prop="occurTime">
          <el-date-picker v-model="form.occurTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择发生时间" :disabled="dialogType === 'view'" style="width: 100%" />
        </el-form-item>
        <el-form-item label="处置状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择" :disabled="dialogType === 'view'" style="width: 100%">
            <el-option label="待处置" value="pending" />
            <el-option label="处置中" value="processing" />
            <el-option label="已处置" value="done" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="leader">
          <el-input v-model="form.leader" placeholder="请输入负责人" :disabled="dialogType === 'view'" />
        </el-form-item>
        <el-form-item label="事件描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入事件描述" :disabled="dialogType === 'view'" />
        </el-form-item>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 应急预案管理弹窗 -->
    <el-dialog title="应急预案管理" :visible.sync="planDialogVisible" width="900px" :close-on-click-modal="false">
      <div style="margin-bottom: 10px; display: flex; justify-content: space-between; align-items: center">
        <el-form :inline="true" size="small" style="margin-bottom: 0">
          <el-form-item label="预案类型" style="margin-bottom:0">
            <el-select v-model="planQuery.planType" placeholder="全部" clearable style="width:120px" @change="fetchPlanData">
              <el-option label="自然灾害" value="NATURAL_DISASTER" />
              <el-option label="安全事件" value="SECURITY" />
              <el-option label="政治动荡" value="POLITICAL" />
              <el-option label="公共卫生" value="HEALTH" />
              <el-option label="火灾事故" value="FIRE" />
              <el-option label="其他" value="OTHER" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态" style="margin-bottom:0">
            <el-select v-model="planQuery.status" placeholder="全部" clearable style="width:100px" @change="fetchPlanData">
              <el-option label="启用" value="ACTIVE" />
              <el-option label="停用" value="INACTIVE" />
            </el-select>
          </el-form-item>
        </el-form>
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddPlan">新增预案</el-button>
      </div>
      <el-table :data="planList" v-loading="planLoading" border size="small"
        :header-cell-style="{ background: ipLightBg, color: ipSecondary }">
        <el-table-column label="序号" type="index" width="50" align="center" />
        <el-table-column label="预案名称" prop="planName" min-width="160" show-overflow-tooltip />
        <el-table-column label="预案类型" prop="planType" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="small" type="info">{{ planTypeLabel(scope.row.planType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预案级别" prop="planLevel" width="90" align="center">
          <template slot-scope="scope">
            <el-tag size="small" :type="planLevelTagType(scope.row.planLevel)">{{ planLevelLabel(scope.row.planLevel) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="责任部门" prop="responsibleDept" width="120" align="center" />
        <el-table-column label="责任人" prop="responsiblePerson" width="90" align="center" />
        <el-table-column label="版本" prop="version" width="70" align="center" />
        <el-table-column label="状态" prop="status" width="80" align="center">
          <template slot-scope="scope">
            <el-tag size="small" :type="scope.row.status === 'ACTIVE' ? 'success' : 'info'">{{ scope.row.status === 'ACTIVE' ? '启用' : '停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleViewPlan(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEditPlan(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color:#F56C6C" @click="handleDeletePlan(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 预案新增/编辑弹窗 -->
    <el-dialog :title="{ add: '新增应急预案', edit: '编辑应急预案', view: '查看应急预案' }[planFormType]"
      :visible.sync="planFormVisible" width="620px" :close-on-click-modal="false" append-to-body>
      <el-form :model="planForm" :rules="planRules" ref="planForm" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="预案名称" prop="planName">
              <el-input v-model="planForm.planName" placeholder="请输入" :disabled="planFormType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预案类型" prop="planType">
              <el-select v-model="planForm.planType" placeholder="请选择" :disabled="planFormType === 'view'" style="width:100%">
                <el-option label="自然灾害" value="NATURAL_DISASTER" />
                <el-option label="安全事件" value="SECURITY" />
                <el-option label="政治动荡" value="POLITICAL" />
                <el-option label="公共卫生" value="HEALTH" />
                <el-option label="火灾事故" value="FIRE" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预案级别" prop="planLevel">
              <el-select v-model="planForm.planLevel" placeholder="请选择" :disabled="planFormType === 'view'" style="width:100%">
                <el-option label="一级（重大）" value="LEVEL_1" />
                <el-option label="二级（较大）" value="LEVEL_2" />
                <el-option label="三级（一般）" value="LEVEL_3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="版本号">
              <el-input v-model="planForm.version" placeholder="如 V1.0" :disabled="planFormType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="责任部门" prop="responsibleDept">
              <el-input v-model="planForm.responsibleDept" placeholder="请输入" :disabled="planFormType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="责任人">
              <el-input v-model="planForm.responsiblePerson" placeholder="请输入" :disabled="planFormType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="planForm.contactPhone" placeholder="请输入" :disabled="planFormType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="planForm.status" placeholder="请选择" :disabled="planFormType === 'view'" style="width:100%">
                <el-option label="启用" value="ACTIVE" />
                <el-option label="停用" value="INACTIVE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="适用场景">
              <el-input v-model="planForm.applicableScenario" type="textarea" :rows="2" placeholder="请描述适用场景" :disabled="planFormType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="响应流程">
              <el-input v-model="planForm.responseProcess" type="textarea" :rows="3" placeholder="请描述响应流程" :disabled="planFormType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="planForm.remark" type="textarea" :rows="2" placeholder="请输入备注" :disabled="planFormType === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" v-if="planFormType !== 'view'">
        <el-button @click="planFormVisible = false">取 消</el-button>
        <el-button type="primary" :loading="planSubmitLoading" @click="handleSubmitPlan">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>


<script>
import { getOverseasEmergencyList, addOverseasEmergency, updateOverseasEmergency, deleteOverseasEmergency, getEmergencyPlanList, addEmergencyPlan, updateEmergencyPlan, deleteEmergencyPlan } from '@/api/stateAssets/overseasPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'EmergencyCommand',
  mixins: [investThemeMixin],
  computed: {
    emergencyStats() {
      const d = this.list
      return {
        total: d.length,
        pending: d.filter(r => r.status === 'pending').length,
        processing: d.filter(r => r.status === 'processing').length,
        done: d.filter(r => r.status === 'done').length
      }
    },
    filteredList() {
      let data = this.list
      if (this.queryForm.country) data = data.filter(r => (r.country || '').includes(this.queryForm.country))
      if (this.queryForm.riskLevel) data = data.filter(r => r.riskLevel === this.queryForm.riskLevel)
      if (this.queryForm.status) data = data.filter(r => r.status === this.queryForm.status)
      this.total = data.length
      const s = (this.queryForm.pageNumber - 1) * this.queryForm.pageSize
      return data.slice(s, s + this.queryForm.pageSize)
    }
  },
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      multipleSelection: [],
      queryForm: {
        country: '',
        riskLevel: '',
        status: '',
        pageNumber: 1,
        pageSize: 10
      },
      dialogVisible: false,
      dialogType: 'add',
      submitLoading: false,
      form: {},
      rules: {
        eventName: [{ required: true, message: '请输入事件名称', trigger: 'blur' }],
        country: [{ required: true, message: '请输入所在国家', trigger: 'blur' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
        occurTime: [{ required: true, message: '请选择发生时间', trigger: 'change' }],
        status: [{ required: true, message: '请选择处置状态', trigger: 'change' }]
      },
      // 应急预案相关
      planDialogVisible: false,
      planLoading: false,
      planList: [],
      planQuery: { planType: '', status: '' },
      planFormVisible: false,
      planFormType: 'add',
      planSubmitLoading: false,
      planForm: {},
      planRules: {
        planName: [{ required: true, message: '请输入预案名称', trigger: 'blur' }],
        planType: [{ required: true, message: '请选择预案类型', trigger: 'change' }],
        planLevel: [{ required: true, message: '请选择预案级别', trigger: 'change' }],
        responsibleDept: [{ required: true, message: '请输入责任部门', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getOverseasEmergencyList(this.queryForm)
        if (res && res.result === 200) {
          this.list = (res.data && res.data.tlist) || []
          this.total = (res.data && res.data.totalRecord) || 0
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
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm = { country: '', riskLevel: '', status: '', pageNumber: 1, pageSize: 10 }
      this.fetchData()
    },
    handleAdd() {
      this.dialogType = 'add'
      this.form = {}
      this.dialogVisible = true
      this.$nextTick(() => this.$refs.form && this.$refs.form.clearValidate())
    },
    async handleDelete(row) {
      try {
        await this.$confirm(`确认删除事件「${row.eventName}」？`, '提示', { type: 'warning' })
        const res = await deleteOverseasEmergency(row.emergencyId)
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
    async handleBatchDelete() {
      try {
        await this.$confirm(`确认删除选中的 ${this.multipleSelection.length} 条记录？`, '提示', { type: 'warning' })
        for (const item of this.multipleSelection) {
          await deleteOverseasEmergency(item.emergencyId)
        }
        this.$message.success('批量删除成功')
        this.fetchData()
      } catch (e) {
        if (e !== 'cancel') this.$message.error('删除失败')
      }
    },
    handlePlan() {
      this.planDialogVisible = true
      this.fetchPlanData()
    },
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          // 映射前端字段到后端实体字段
          const submitData = { ...this.form }
          if (submitData.description) {
            submitData.eventDesc = submitData.description
            delete submitData.description
          }
          // severity 与 riskLevel 保持一致
          if (submitData.riskLevel) {
            submitData.severity = submitData.riskLevel
          }
          const apiCall = this.dialogType === 'add' ? addOverseasEmergency : updateOverseasEmergency
          const res = await apiCall(submitData)
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
    riskTagType(level) { return { high: 'danger', medium: 'warning', low: 'info', HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }[level] || 'info' },
    riskLabel(level) { return { high: '高', medium: '中', low: '低', HIGH: '高', MEDIUM: '中', LOW: '低' }[level] || level },
    statusTagType(status) { return { pending: 'danger', processing: 'warning', done: 'success', PENDING: 'danger', PROCESSING: 'warning', RESOLVED: 'success' }[status] || 'info' },
    statusLabel(status) { return { pending: '待处置', processing: '处置中', done: '已处置', PENDING: '待处置', PROCESSING: '处置中', RESOLVED: '已处置' }[status] || status },
    // ========== 应急预案方法 ==========
    async fetchPlanData() {
      this.planLoading = true
      try {
        const res = await getEmergencyPlanList({ ...this.planQuery, pageNumber: 1, pageSize: 100 })
        if (res && res.result === 200) {
          this.planList = (res.data && res.data.tlist) || []
        } else {
          this.planList = []
        }
      } catch (e) {
        this.planList = []
      } finally {
        this.planLoading = false
      }
    },
    handleAddPlan() {
      this.planFormType = 'add'
      this.planForm = { status: 'ACTIVE', version: 'V1.0' }
      this.planFormVisible = true
      this.$nextTick(() => this.$refs.planForm && this.$refs.planForm.clearValidate())
    },
    handleEditPlan(row) {
      this.planFormType = 'edit'
      this.planForm = { ...row }
      this.planFormVisible = true
      this.$nextTick(() => this.$refs.planForm && this.$refs.planForm.clearValidate())
    },
    handleViewPlan(row) {
      this.planFormType = 'view'
      this.planForm = { ...row }
      this.planFormVisible = true
    },
    async handleDeletePlan(row) {
      try {
        await this.$confirm(`确认删除预案「${row.planName}」？`, '提示', { type: 'warning' })
        const res = await deleteEmergencyPlan(row.planId)
        if (res && res.result === 200) {
          this.$message.success('删除成功')
          this.fetchPlanData()
        } else {
          this.$message.error((res && res.msg) || '删除失败')
        }
      } catch (e) {
        if (e !== 'cancel') this.$message.error('删除失败')
      }
    },
    handleSubmitPlan() {
      this.$refs.planForm.validate(async (valid) => {
        if (!valid) return
        this.planSubmitLoading = true
        try {
          const apiCall = this.planFormType === 'add' ? addEmergencyPlan : updateEmergencyPlan
          const res = await apiCall(this.planForm)
          if (res && res.result === 200) {
            this.$message.success(this.planFormType === 'add' ? '新增成功' : '编辑成功')
            this.planFormVisible = false
            this.fetchPlanData()
          } else {
            this.$message.error((res && res.msg) || '操作失败')
          }
        } catch (e) {
          this.$message.error('操作失败，请稍后重试')
        } finally {
          this.planSubmitLoading = false
        }
      })
    },
    planTypeLabel(v) { return { NATURAL_DISASTER: '自然灾害', SECURITY: '安全事件', POLITICAL: '政治动荡', HEALTH: '公共卫生', FIRE: '火灾事故', OTHER: '其他' }[v] || v },
    planLevelTagType(v) { return { LEVEL_1: 'danger', LEVEL_2: 'warning', LEVEL_3: 'info' }[v] || 'info' },
    planLevelLabel(v) { return { LEVEL_1: '一级', LEVEL_2: '二级', LEVEL_3: '三级' }[v] || v }
  }
}
</script>

<style lang="scss" scoped>
.overseas-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 14px; padding: 14px 20px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 50%, var(--ip-bright, #1677FF) 100%);
  border-radius: 6px; color: #fff;
}
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600;
  i { font-size: 22px; margin-right: 10px; }
}
.page-header-desc { font-size: 13px; opacity: 0.85; }
.kpi-card { display: flex; align-items: center; }
.kpi-icon-wrap { width: 50px; height: 50px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 14px; flex-shrink: 0; }
.kpi-info { flex: 1; }
.kpi-value { font-size: 28px; font-weight: bold; color: #303133; line-height: 1; }
.kpi-label { font-size: 13px; color: #909399; margin-top: 5px; }
::v-deep .el-card { border-radius: 6px; }
</style>
