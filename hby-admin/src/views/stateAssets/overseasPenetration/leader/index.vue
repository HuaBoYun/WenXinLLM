<template>
  <div class="app-container overseas-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-s-custom"></i><span>境外负责人管理</span></div>
      <div class="page-header-desc">管理境外企业及项目负责人信息，明确责任归属与联络通道</div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" style="margin-bottom:14px">
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" :style="{ background: ipLightBg }"><i class="el-icon-s-custom" :style="{ color: ipSecondary, fontSize: '26px' }"></i></div>
            <div class="kpi-info"><div class="kpi-value" :style="{ color: ipSecondary }">{{ leaderStats.total }}</div><div class="kpi-label">负责人总数</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#FFF7E6"><i class="el-icon-office-building" style="color:#FA8C16;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#FA8C16">{{ leaderStats.countryCount }}</div><div class="kpi-label">覆盖国家</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" style="background:#F6FFED"><i class="el-icon-user-solid" style="color:#52C41A;font-size:26px"></i></div>
            <div class="kpi-info"><div class="kpi-value" style="color:#52C41A">{{ leaderStats.onsite }}</div><div class="kpi-label">驻场负责人</div></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" :style="{ background: ipLightBg }"><i class="el-icon-s-flag" :style="{ color: ipSecondary, fontSize: '26px' }"></i></div>
            <div class="kpi-info"><div class="kpi-value" :style="{ color: ipSecondary }">{{ leaderStats.remote }}</div><div class="kpi-label">远程负责人</div></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询栏 -->
    <el-card shadow="never" style="margin-bottom:10px">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="姓名">
          <el-input v-model="queryForm.name" placeholder="请输入" clearable style="width:130px" />
        </el-form-item>
        <el-form-item label="所在国家">
          <el-input v-model="queryForm.country" placeholder="请输入" clearable style="width:120px" />
        </el-form-item>
        <el-form-item label="负责类型">
          <el-select v-model="queryForm.leaderType" placeholder="全部" clearable style="width:120px">
            <el-option label="企业负责人" value="COMPANY" />
            <el-option label="项目负责人" value="PROJECT" />
            <el-option label="安全负责人" value="SAFETY" />
            <el-option label="合规负责人" value="COMPLIANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="在职状态">
          <el-select v-model="queryForm.status" placeholder="全部" clearable style="width:110px">
            <el-option label="在职" value="ACTIVE" />
            <el-option label="离职" value="INACTIVE" />
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
      >
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="姓名" prop="name" width="100" align="center" />
        <el-table-column label="性别" prop="gender" width="70" align="center" />
        <el-table-column label="所属企业" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="所在国家" prop="country" width="110" align="center" />
        <el-table-column label="负责类型" prop="leaderType" width="110" align="center">
          <template slot-scope="scope">
            <el-tag type="info" size="small">{{ leaderTypeLabel(scope.row.leaderType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="职务" prop="position" width="120" align="center" />
        <el-table-column label="联系电话" prop="phone" width="150" align="center" />
        <el-table-column label="邮箱" prop="email" min-width="170" show-overflow-tooltip />
        <el-table-column label="驻场方式" prop="workMode" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.workMode === 'ONSITE' ? 'success' : 'warning'" size="small">
              {{ scope.row.workMode === 'ONSITE' ? '驻场' : '远程' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="在职状态" prop="status" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'info'" size="small">
              {{ scope.row.status === 'ACTIVE' ? '在职' : '离职' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
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
      :title="{ add: '新增负责人', edit: '编辑负责人', view: '查看负责人' }[dialogType]"
      :visible.sync="dialogVisible"
      width="660px"
      :close-on-click-modal="false"
    >
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender" placeholder="请选择" :disabled="dialogType === 'view'" style="width:100%">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属企业" prop="companyName">
              <el-input v-model="form.companyName" placeholder="请输入所属企业" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在国家" prop="country">
              <el-input v-model="form.country" placeholder="请输入国家" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责类型" prop="leaderType">
              <el-select v-model="form.leaderType" placeholder="请选择" :disabled="dialogType === 'view'" style="width:100%">
                <el-option label="企业负责人" value="COMPANY" />
                <el-option label="项目负责人" value="PROJECT" />
                <el-option label="安全负责人" value="SAFETY" />
                <el-option label="合规负责人" value="COMPLIANCE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职务" prop="position">
              <el-input v-model="form.position" placeholder="请输入职务" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱">
              <el-input v-model="form.email" placeholder="请输入邮箱" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="驻场方式">
              <el-select v-model="form.workMode" placeholder="请选择" :disabled="dialogType === 'view'" style="width:100%">
                <el-option label="驻场" value="ONSITE" />
                <el-option label="远程" value="REMOTE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="在职状态">
              <el-select v-model="form.status" placeholder="请选择" :disabled="dialogType === 'view'" style="width:100%">
                <el-option label="在职" value="ACTIVE" />
                <el-option label="离职" value="INACTIVE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" :disabled="dialogType === 'view'" />
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
import { getOverseasLeaderList, saveOverseasLeader, deleteOverseasLeader } from '@/api/stateAssets/overseasPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'OverseasLeaderMgmt',
  mixins: [investThemeMixin],
  computed: {
    leaderStats() {
      const d = this.list.filter(r => r.status === 'ACTIVE')
      return {
        total: d.length,
        countryCount: new Set(d.map(r => r.country)).size,
        onsite: d.filter(r => r.workMode === 'ONSITE').length,
        remote: d.filter(r => r.workMode === 'REMOTE').length
      }
    },
    filteredList() {
      let data = this.list
      if (this.queryForm.name) data = data.filter(r => r.name.includes(this.queryForm.name))
      if (this.queryForm.country) data = data.filter(r => r.country.includes(this.queryForm.country))
      if (this.queryForm.leaderType) data = data.filter(r => r.leaderType === this.queryForm.leaderType)
      if (this.queryForm.status) data = data.filter(r => r.status === this.queryForm.status)
      this.total = data.length
      const s = (this.queryForm.pageNumber - 1) * this.queryForm.pageSize
      return data.slice(s, s + this.queryForm.pageSize)
    }
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      list: [],
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 10, name: '', country: '', leaderType: '', status: '' },
      dialogVisible: false,
      dialogType: 'add',
      form: {},
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        companyName: [{ required: true, message: '请输入所属企业', trigger: 'blur' }],
        country: [{ required: true, message: '请输入所在国家', trigger: 'blur' }],
        leaderType: [{ required: true, message: '请选择负责类型', trigger: 'change' }],
        position: [{ required: true, message: '请输入职务', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }]
      }
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getOverseasLeaderList(this.queryForm)
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
      this.form = { status: 'ACTIVE', workMode: 'ONSITE', gender: '男' }
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
      this.dialogType = 'view'
      this.form = { ...row }
      this.dialogVisible = true
    },
    async handleDelete(row) {
      try {
        await this.$confirm(`确认删除负责人「${row.name}」？`, '提示', { type: 'warning' })
        const res = await deleteOverseasLeader(row.leaderId)
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
          const res = await saveOverseasLeader(this.form)
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
    leaderTypeLabel(v) {
      return { COMPANY: '企业负责人', PROJECT: '项目负责人', SAFETY: '安全负责人', COMPLIANCE: '合规负责人' }[v] || v
    }
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
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); }
::v-deep .el-card { border-radius: 6px; }
</style>
