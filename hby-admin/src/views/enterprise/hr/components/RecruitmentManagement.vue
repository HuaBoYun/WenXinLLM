<template>
  <div class="recruitment-management">
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="职位名称" prop="positionName"><el-input v-model="queryForm.positionName" placeholder="请输入职位名称" clearable /></el-form-item>
        <el-form-item label="部门" prop="department"><el-select v-model="queryForm.department" placeholder="请选择部门" clearable><el-option v-for="d in departmentOptions" :key="d" :label="d" :value="d" /></el-select></el-form-item>
        <el-form-item label="招聘状态" prop="status"><el-select v-model="queryForm.status" placeholder="请选择招聘状态" clearable><el-option label="发布中" value="发布中" /><el-option label="面试中" value="面试中" /><el-option label="已结束" value="已结束" /><el-option label="已暂停" value="已暂停" /></el-select></el-form-item>
        <el-form-item label="紧急程度" prop="urgency"><el-select v-model="queryForm.urgency" placeholder="请选择紧急程度" clearable><el-option label="紧急" value="紧急" /><el-option label="一般" value="一般" /><el-option label="不急" value="不急" /></el-select></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button><el-button icon="el-icon-refresh" @click="handleReset">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" class="action-card">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">发布职位</el-button>
      <el-button type="warning" icon="el-icon-download" @click="handleExport">导出招聘数据</el-button>
    </el-card>
    <el-card shadow="never" class="table-card">
      <el-table :data="recruitmentList" v-loading="loading" @selection-change="s => multipleSelection = s" stripe border>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="positionNo" label="职位编号" width="130" />
        <el-table-column prop="positionName" label="职位名称" width="150" />
        <el-table-column prop="department" label="部门" width="110" />
        <el-table-column prop="positionType" label="职位类型" width="90">
          <template slot-scope="scope"><el-tag :type="getTypeColor(scope.row.positionType)">{{ scope.row.positionType }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="requiredCount" label="需求人数" width="80" />
        <el-table-column prop="applicantCount" label="应聘人数" width="80"><template slot-scope="scope"><span class="applicant-count">{{ scope.row.applicantCount || 0 }}</span></template></el-table-column>
        <el-table-column prop="interviewCount" label="面试人数" width="80"><template slot-scope="scope"><span class="interview-count">{{ scope.row.interviewCount || 0 }}</span></template></el-table-column>
        <el-table-column prop="hiredCount" label="录用人数" width="80"><template slot-scope="scope"><span class="hired-count">{{ scope.row.hiredCount || 0 }}</span></template></el-table-column>
        <el-table-column prop="salaryRange" label="薪资范围" width="110" />
        <el-table-column prop="urgency" label="紧急程度" width="90">
          <template slot-scope="scope"><el-tag :type="getUrgencyType(scope.row.urgency)">{{ scope.row.urgency }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="publishDate" label="发布日期" width="110" />
        <el-table-column prop="deadline" label="截止日期" width="110" />
        <el-table-column prop="status" label="状态" width="90">
          <template slot-scope="scope"><el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="warning" @click="handleEdit(scope.row)" v-if="scope.row.status !== '已结束'">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container"><el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pagination.currentPage" :page-sizes="[10, 20, 50, 100]" :page-size="pagination.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" /></div>
    </el-card>

    <!-- 查看/新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="750px" :close-on-click-modal="false">
      <el-form :model="formData" ref="formRef" :rules="formRules" label-width="100px" :disabled="dialogMode === 'view'">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="职位编号" prop="positionNo"><el-input v-model="formData.positionNo" placeholder="如: REC20240801" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="职位名称" prop="positionName"><el-input v-model="formData.positionName" placeholder="请输入职位名称" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="部门" prop="department"><el-select v-model="formData.department" placeholder="请选择" style="width:100%"><el-option v-for="d in departmentOptions" :key="d" :label="d" :value="d" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="职位类型" prop="positionType"><el-select v-model="formData.positionType" style="width:100%"><el-option label="全职" value="全职" /><el-option label="兼职" value="兼职" /><el-option label="实习" value="实习" /><el-option label="外包" value="外包" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="需求人数"><el-input-number v-model="formData.requiredCount" :min="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="薪资范围"><el-input v-model="formData.salaryRange" placeholder="如: 15K-25K" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="紧急程度"><el-select v-model="formData.urgency" style="width:100%"><el-option label="紧急" value="紧急" /><el-option label="一般" value="一般" /><el-option label="不急" value="不急" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态"><el-select v-model="formData.status" style="width:100%"><el-option label="发布中" value="发布中" /><el-option label="面试中" value="面试中" /><el-option label="已结束" value="已结束" /><el-option label="已暂停" value="已暂停" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="发布日期"><el-date-picker v-model="formData.publishDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="截止日期"><el-date-picker v-model="formData.deadline" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24"><el-form-item label="职位描述"><el-input v-model="formData.jobDescription" type="textarea" :rows="3" placeholder="请输入职位描述" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24"><el-form-item label="任职要求"><el-input v-model="formData.requirements" type="textarea" :rows="3" placeholder="请输入任职要求" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <div slot="footer" v-if="dialogMode !== 'view'"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { recruitmentApi, organizationApi } from '@/api/enterprise/hr'

export default {
  name: 'RecruitmentManagement',
  data() {
    return {
      loading: false, submitLoading: false,
      queryForm: { positionName: '', department: '', status: '', urgency: '' },
      recruitmentList: [], multipleSelection: [], departmentOptions: [],
      pagination: { currentPage: 1, pageSize: 20, total: 0 },
      dialogVisible: false, dialogMode: 'add', formData: {},
      formRules: { positionName: [{ required: true, message: '请输入职位名称', trigger: 'blur' }], department: [{ required: true, message: '请选择部门', trigger: 'change' }] }
    }
  },
  computed: { dialogTitle() { return { add: '发布职位', edit: '编辑职位', view: '职位详情' }[this.dialogMode] || '' } },
  mounted() { this.loadDepartmentOptions(); this.loadRecruitmentList() },
  methods: {
    async loadDepartmentOptions() { try { const res = await organizationApi.getDepartmentList({}); if (res && res.data) { const depts = []; const ex = l => { l.forEach(d => { if (d.deptName) depts.push(d.deptName); if (d.children) ex(d.children) }) }; ex(res.data || []); this.departmentOptions = [...new Set(depts)] } } catch (e) {} },
    async loadRecruitmentList() {
      this.loading = true
      try { const res = await recruitmentApi.getRecruitmentList({ ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize }); if (res && res.data) { this.recruitmentList = res.data.tlist || []; this.pagination.total = res.data.totalRecord || 0 } else { this.recruitmentList = []; this.pagination.total = 0 } }
      catch (e) { this.recruitmentList = []; this.pagination.total = 0 } finally { this.loading = false }
    },
    handleSearch() { this.pagination.currentPage = 1; this.loadRecruitmentList() },
    handleReset() { this.$refs.queryForm.resetFields(); this.loadRecruitmentList() },
    handleAdd() { this.dialogMode = 'add'; this.formData = { enterpriseId: 'ent001', enterpriseName: '贵州国资投资集团', status: '发布中', positionType: '全职', urgency: '一般', requiredCount: 1, applicantCount: 0, interviewCount: 0, hiredCount: 0 }; this.dialogVisible = true },
    handleView(row) { this.dialogMode = 'view'; this.formData = { ...row }; this.dialogVisible = true },
    handleEdit(row) { this.dialogMode = 'edit'; this.formData = { ...row }; this.dialogVisible = true },
    handleDelete(row) { this.$confirm('确认删除该招聘职位吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => { try { await recruitmentApi.deleteRecruitment(row.id); this.$message.success('删除成功'); this.loadRecruitmentList() } catch (e) { this.$message.error('删除失败') } }).catch(() => {}) },
    handleExport() {
      this.$message.info('正在导出招聘数据...')
      recruitmentApi.exportRecruitment(this.queryForm).then(res => {
        if (res && res.data) {
          const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' })
          const link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = '招聘数据.xlsx'
          link.click()
          URL.revokeObjectURL(link.href)
          this.$message.success('导出成功')
        }
      }).catch(() => { this.$message.error('导出失败') })
    },
    async handleSubmit() {
      try { await this.$refs.formRef.validate() } catch (e) { return }
      this.submitLoading = true
      try {
        if (this.dialogMode === 'add') { await recruitmentApi.publishRecruitmentPosition(this.formData); this.$message.success('发布成功') }
        else { await recruitmentApi.updateRecruitment(this.formData.id, this.formData); this.$message.success('编辑成功') }
        this.dialogVisible = false; this.loadRecruitmentList()
      } catch (e) { this.$message.error(this.dialogMode === 'add' ? '发布失败' : '编辑失败') } finally { this.submitLoading = false }
    },
    handleSizeChange(v) { this.pagination.pageSize = v; this.loadRecruitmentList() },
    handleCurrentChange(v) { this.pagination.currentPage = v; this.loadRecruitmentList() },
    getTypeColor(t) { return { '全职': 'primary', '兼职': 'success', '实习': 'warning', '外包': 'info' }[t] || 'info' },
    getUrgencyType(u) { return { '紧急': 'danger', '一般': 'warning', '不急': 'success' }[u] || 'info' },
    getStatusType(s) { return { '发布中': 'primary', '面试中': 'warning', '已结束': 'success', '已暂停': 'info' }[s] || 'info' }
  }
}
</script>

<style lang="scss" scoped>
.recruitment-management {
  .search-card, .action-card, .table-card { margin-bottom: 16px; }
  .applicant-count { color: #409EFF; font-weight: 500; }
  .interview-count { color: #E6A23C; font-weight: 500; }
  .hired-count { color: #67C23A; font-weight: bold; }
  .pagination-container { margin-top: 20px; text-align: right; }
}
</style>
