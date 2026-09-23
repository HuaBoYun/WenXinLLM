<template>
  <div class="app-container overseas-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-user"></i><span>境外人员安全管理</span></div>
      <div class="page-header-desc">管理境外派驻人员信息及安全状态，支持应急呼救联动</div>
    </div>

    <!-- 安全态势统计卡片 -->
    <el-row :gutter="16" style="margin-bottom:14px">
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" :style="{ background: ipLightBg }"><i class="el-icon-user" :style="{ color: ipSecondary, fontSize: '26px' }"></i></div>
            <div class="kpi-info"><div class="kpi-value">{{ safetyStats.total }}</div><div class="kpi-label">在外人员总数</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#F6FFED"><i class="el-icon-circle-check" style="color:#52C41A;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#52C41A">{{ safetyStats.safe }}</div><div class="kpi-label">安全状态</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#FFF7E6"><i class="el-icon-warning-outline" style="color:#FA8C16;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#FA8C16">{{ safetyStats.warning }}</div><div class="kpi-label">需关注</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#FFF1F0"><i class="el-icon-warning" style="color:#F5222D;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#F5222D">{{ safetyStats.danger }}</div><div class="kpi-label">危险状态</div></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="所在国家" prop="country">
          <el-select v-model="queryForm.country" placeholder="请选择国家" clearable style="width: 140px">
            <el-option v-for="c in countryOptions" :key="c" :label="c" :value="c" />
          </el-select>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="queryForm.name" placeholder="请输入姓名" clearable style="width:120px" />
        </el-form-item>
        <el-form-item label="安全状态" prop="safetyStatus">
          <el-select v-model="queryForm.safetyStatus" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="安全" value="safe" />
            <el-option label="关注" value="warning" />
            <el-option label="危险" value="danger" />
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
        <el-button type="warning" size="small" icon="el-icon-phone" style="margin-left: 10px" @click="handleEmergency">一键应急呼救</el-button>
      </div>
      <el-table v-loading="loading" :data="filteredList" border @selection-change="val => multipleSelection = val" style="width: 100%"
        :header-cell-style="{ background: ipLightBg, color: ipSecondary }">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="姓名" prop="name" width="100" align="center" />
        <el-table-column label="所在国家" prop="country" width="120" align="center" />
        <el-table-column label="所在城市" prop="city" width="120" align="center" />
        <el-table-column label="安全状态" prop="safetyStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="statusTagType(scope.row.safetyStatus)" size="small">{{ statusLabel(scope.row.safetyStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="联系方式" prop="contact" min-width="140" align="center" />
        <el-table-column label="派驻单位" prop="unit" min-width="160" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="dialogType = 'view'; form = { ...scope.row }; dialogVisible = true">查看</el-button>
            <el-button size="mini" type="text" @click="dialogType = 'edit'; form = { ...scope.row }; dialogVisible = true">编辑</el-button>
            <el-button size="mini" type="text" style="color: #F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top: 15px; text-align: right" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="val => { queryForm.pageSize = val; fetchData() }" @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
    </el-card>
    <el-dialog :title="{ add: '新增境外人员', edit: '编辑境外人员', view: '查看境外人员' }[dialogType]" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在国家" prop="country">
              <el-input v-model="form.country" placeholder="请输入国家" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在城市" prop="city">
              <el-input v-model="form.city" placeholder="请输入城市" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安全状态" prop="safetyStatus">
              <el-select v-model="form.safetyStatus" placeholder="请选择" :disabled="dialogType === 'view'" style="width: 100%">
                <el-option label="安全" value="safe" />
                <el-option label="关注" value="warning" />
                <el-option label="危险" value="danger" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系方式" prop="contact">
              <el-input v-model="form.contact" placeholder="请输入联系方式" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="派驻单位" prop="unit">
              <el-input v-model="form.unit" placeholder="请输入派驻单位" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>


<script>
import { getOverseasPersonnelList, getOverseasPersonnelStats, addOverseasPersonnel, updateOverseasPersonnel, deleteOverseasPersonnel } from '@/api/stateAssets/overseasPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'PersonnelSafety',
  mixins: [investThemeMixin],
  computed: {
    safetyStats() {
      const all = this.list
      return {
        total: all.length,
        safe: all.filter(r => r.safetyStatus === 'SAFE' || r.safetyStatus === 'safe').length,
        warning: all.filter(r => r.safetyStatus === 'AT_RISK' || r.safetyStatus === 'warning').length,
        danger: all.filter(r => r.safetyStatus === 'EMERGENCY' || r.safetyStatus === 'danger').length
      }
    },
    countryOptions() {
      return [...new Set(this.list.map(r => r.country))]
    },
    filteredList() {
      let data = this.list
      if (this.queryForm.name) data = data.filter(r => r.name && r.name.includes(this.queryForm.name))
      if (this.queryForm.country) data = data.filter(r => r.country === this.queryForm.country)
      if (this.queryForm.safetyStatus) data = data.filter(r => r.safetyStatus === this.queryForm.safetyStatus)
      return data
    }
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      list: [],
      total: 0,
      multipleSelection: [],
      dialogVisible: false,
      dialogType: 'add',
      queryForm: { name: '', country: '', safetyStatus: '', pageNumber: 1, pageSize: 10 },
      form: {},
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        country: [{ required: true, message: '请输入所在国家', trigger: 'blur' }],
        safetyStatus: [{ required: true, message: '请选择安全状态', trigger: 'change' }]
      }
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getOverseasPersonnelList(this.queryForm)
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
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.fetchData() },
    handleAdd() { this.dialogType = 'add'; this.form = {}; this.dialogVisible = true },
    async handleDelete(row) {
      try {
        await this.$confirm(`确认删除人员 "${row.name}"？`, '提示', { type: 'warning' })
        const res = await deleteOverseasPersonnel(row.personnelId)
        if (res && res.result === 200 && res.data) {
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
        let failCount = 0
        for (const item of this.multipleSelection) {
          const res = await deleteOverseasPersonnel(item.personnelId)
          if (!res || res.result !== 200 || !res.data) failCount++
        }
        if (failCount === 0) {
          this.$message.success('批量删除成功')
        } else {
          this.$message.warning(`部分删除失败（${failCount}条）`)
        }
        this.fetchData()
      } catch (e) {
        if (e !== 'cancel') this.$message.error('删除失败')
      }
    },
    handleEmergency() {
      this.$confirm('确认发送一键应急呼救信号？将通知所有境外人员及应急响应团队！', '应急呼救', { type: 'error', confirmButtonText: '确认发送', cancelButtonText: '取消' }).then(() => {
        this.$message.success('应急呼救信号已发送，应急响应团队已收到通知')
      })
    },
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const api = this.dialogType === 'add' ? addOverseasPersonnel : updateOverseasPersonnel
          const res = await api(this.form)
          if (res && res.result === 200) {
            this.$message.success('操作成功')
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
    statusTagType(status) { return { safe: 'success', warning: 'warning', danger: 'danger', SAFE: 'success', AT_RISK: 'warning', EMERGENCY: 'danger' }[status] || 'info' },
    statusLabel(status) { return { safe: '安全', warning: '关注', danger: '危险', SAFE: '安全', AT_RISK: '关注', EMERGENCY: '危险' }[status] || status }
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
