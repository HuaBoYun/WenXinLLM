<template>
  <div class="app-container contract-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)', padding: '14px 20px', borderRadius: '6px', color: '#fff', marginBottom: '14px' }">
      <div class="page-header-left"><i class="el-icon-document-checked" style="color:#fff;font-size:22px;margin-right:10px;"></i><span>合同智能审查</span></div>
      <div class="page-header-desc">通过AI智能审查合同关键条款，识别风险条款与缺失条款</div>
    </div>

    <el-card class="search-card" shadow="never" :style="{ borderLeft: '3px solid ' + themeColor }">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="合同类型" prop="contractType">
          <el-select v-model="queryForm.contractType" placeholder="请选择" clearable style="width:130px">
            <el-option v-for="item in contractTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="审查状态" prop="reviewStatus">
          <el-select v-model="queryForm.reviewStatus" placeholder="请选择" clearable style="width:130px">
            <el-option v-for="item in reviewStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="queryForm.riskLevel" placeholder="请选择" clearable style="width:130px">
            <el-option v-for="item in riskLevelOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top:10px">
      <div style="margin-bottom:10px">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增审查</el-button>
        <el-button type="danger" size="small" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>
      <el-table v-loading="loading" :data="tableData" border stripe size="small" row-key="reviewId" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="contractName" label="合同名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="contractType" label="合同类型" width="120" align="center" />
        <el-table-column prop="reviewStatus" label="审查状态" width="110" align="center">
          <template slot-scope="{ row }">
            <el-tag v-if="row.reviewStatus && reviewStatusMap[row.reviewStatus]" :type="reviewStatusMap[row.reviewStatus].type" size="small">{{ reviewStatusMap[row.reviewStatus].label }}</el-tag>
            <span v-else>—</span>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="110" align="center">
          <template slot-scope="{ row }">
            <el-tag v-if="row.riskLevel && riskLevelMap[row.riskLevel]" :type="riskLevelMap[row.riskLevel].type" size="small">{{ riskLevelMap[row.riskLevel].label }}</el-tag>
            <span v-else>—</span>
          </template>
        </el-table-column>
        <el-table-column prop="reviewTime" label="审查时间" width="160" align="center">
          <template slot-scope="{ row }">
            {{ row.reviewTime ? row.reviewTime.replace('T', ' ').substring(0, 16) : '—' }}
          </template>
        </el-table-column>
        <el-table-column prop="reviewer" label="审查人" width="100" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="small" icon="el-icon-view" @click="handleDetail(row)">详情</el-button>
            <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="text" size="small" icon="el-icon-delete" style="color:#F56C6C" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background layout="total, sizes, prev, pager, next" :total="total" :page-size="queryForm.pageSize" :current-page="queryForm.pageNo" @size-change="handleSizeChange" @current-change="handleCurrentChange" style="margin-top:12px;text-align:right" />
    </el-card>

    <!-- 新增审查对话框 -->
    <el-dialog title="新增审查任务" :visible.sync="addDialogVisible" width="500px" append-to-body>
      <el-form :model="addForm" ref="addForm" label-width="90px" size="small">
        <el-form-item label="合同名称" prop="contractName" :rules="[{required:true,message:'请输入合同名称',trigger:'blur'}]">
          <el-input v-model="addForm.contractName" placeholder="请输入合同名称" />
        </el-form-item>
        <el-form-item label="合同类型" prop="contractType" :rules="[{required:true,message:'请选择合同类型',trigger:'change'}]">
          <el-select v-model="addForm.contractType" placeholder="请选择" style="width:100%">
            <el-option v-for="item in contractTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="addForm.riskLevel" placeholder="请选择（可不选）" clearable style="width:100%">
            <el-option v-for="item in riskLevelOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="审查时间" prop="reviewTime">
          <el-date-picker v-model="addForm.reviewTime" type="datetime" placeholder="请选择审查时间" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%" />
        </el-form-item>
        <el-form-item label="审查人" prop="reviewer">
          <el-input v-model="addForm.reviewer" placeholder="请输入审查人" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="small" @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" size="small" @click="submitAdd">确定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑审查对话框 -->
    <el-dialog title="编辑审查记录" :visible.sync="editDialogVisible" width="550px" append-to-body>
      <el-form :model="editForm" ref="editForm" label-width="90px" size="small">
        <el-form-item label="合同名称" prop="contractName" :rules="[{required:true,message:'请输入合同名称',trigger:'blur'}]">
          <el-input v-model="editForm.contractName" placeholder="请输入合同名称" />
        </el-form-item>
        <el-form-item label="合同类型" prop="contractType" :rules="[{required:true,message:'请选择合同类型',trigger:'change'}]">
          <el-select v-model="editForm.contractType" placeholder="请选择" style="width:100%">
            <el-option v-for="item in contractTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="审查状态" prop="reviewStatus">
          <el-select v-model="editForm.reviewStatus" placeholder="请选择" style="width:100%">
            <el-option v-for="item in reviewStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="editForm.riskLevel" placeholder="请选择" clearable style="width:100%">
            <el-option v-for="item in riskLevelOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="审查人" prop="reviewer">
          <el-input v-model="editForm.reviewer" placeholder="请输入审查人" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="editForm.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="small" @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" size="small" @click="submitEdit">确定</el-button>
      </span>
    </el-dialog>

    <!-- 审查详情对话框 -->
    <el-dialog :title="'审查详情 — ' + (currentRow.contractName || '')" :visible.sync="detailDialogVisible" width="700px" append-to-body>
      <el-descriptions :column="2" border size="small" style="margin-bottom:16px">
        <el-descriptions-item label="合同名称">{{ currentRow.contractName }}</el-descriptions-item>
        <el-descriptions-item label="合同类型">{{ currentRow.contractType }}</el-descriptions-item>
        <el-descriptions-item label="审查状态">
          <el-tag v-if="currentRow.reviewStatus && reviewStatusMap[currentRow.reviewStatus]" :type="reviewStatusMap[currentRow.reviewStatus].type" size="small">{{ reviewStatusMap[currentRow.reviewStatus].label }}</el-tag>
          <span v-else>—</span>
        </el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag v-if="currentRow.riskLevel && riskLevelMap[currentRow.riskLevel]" :type="riskLevelMap[currentRow.riskLevel].type" size="small">{{ riskLevelMap[currentRow.riskLevel].label }}</el-tag>
          <span v-else>—</span>
        </el-descriptions-item>
        <el-descriptions-item label="审查人">{{ currentRow.reviewer || '—' }}</el-descriptions-item>
        <el-descriptions-item label="审查时间">{{ currentRow.reviewTime ? currentRow.reviewTime.replace('T', ' ').substring(0, 16) : '—' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ currentRow.remark || '—' }}</el-descriptions-item>
      </el-descriptions>
      <el-collapse v-model="activeCollapse">
        <el-collapse-item title="风险条款" name="risk">
          <el-table :data="parsedRiskClauses" border size="mini">
            <el-table-column prop="clause" label="条款内容" min-width="200" show-overflow-tooltip />
            <el-table-column prop="reason" label="风险原因" min-width="160" show-overflow-tooltip />
            <el-table-column prop="suggestion" label="修改建议" min-width="160" show-overflow-tooltip />
          </el-table>
          <el-empty v-if="parsedRiskClauses.length === 0" description="暂无风险条款" :image-size="60" />
        </el-collapse-item>
        <el-collapse-item title="缺失条款" name="missing">
          <el-table :data="parsedMissingClauses" border size="mini">
            <el-table-column prop="clauseName" label="缺失条款名称" min-width="160" />
            <el-table-column prop="importance" label="重要程度" width="100" align="center">
              <template slot-scope="{ row }">
                <el-tag :type="row.importance === '必须' ? 'danger' : 'warning'" size="mini">{{ row.importance }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="suggestion" label="补充建议" min-width="200" show-overflow-tooltip />
          </el-table>
          <el-empty v-if="parsedMissingClauses.length === 0" description="暂无缺失条款" :image-size="60" />
        </el-collapse-item>
        <el-collapse-item title="偏离条款" name="deviation">
          <el-table :data="parsedDeviationClauses" border size="mini">
            <el-table-column prop="clause" label="条款内容" min-width="160" show-overflow-tooltip />
            <el-table-column prop="standard" label="标准要求" min-width="160" show-overflow-tooltip />
            <el-table-column prop="deviation" label="偏离说明" min-width="160" show-overflow-tooltip />
          </el-table>
          <el-empty v-if="parsedDeviationClauses.length === 0" description="暂无偏离条款" :image-size="60" />
        </el-collapse-item>
      </el-collapse>
      <span slot="footer">
        <el-button size="small" @click="detailDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { getSmartReviewList, getSmartReviewDetail, addSmartReview, updateSmartReview, deleteSmartReview, batchDeleteSmartReview } from '@/api/stateAssets/contractPenetration'
import { mapGetters } from 'vuex'

export default {
  name: 'ContractSmartReview',
  data() {
    return {
      queryForm: { contractType: '', reviewStatus: '', riskLevel: '', pageNo: 1, pageSize: 10 },
      total: 0,
      loading: false,
      multipleSelection: [],
      addDialogVisible: false,
      editDialogVisible: false,
      detailDialogVisible: false,
      activeCollapse: ['risk'],
      addForm: { contractName: '', contractType: '', riskLevel: '', reviewTime: '', reviewer: '' },
      editForm: { id: '', contractName: '', contractType: '', reviewStatus: '', riskLevel: '', reviewer: '', remark: '' },
      currentRow: {},
      contractTypeOptions: [
        { label: '采购合同', value: '采购合同' },
        { label: '销售合同', value: '销售合同' },
        { label: '工程合同', value: '工程合同' },
        { label: '租赁合同', value: '租赁合同' },
        { label: '服务合同', value: '服务合同' }
      ],
      reviewStatusOptions: [
        { label: '待审查', value: 'pending' },
        { label: '审查中', value: 'reviewing' },
        { label: '已完成', value: 'done' },
        { label: '有风险', value: 'risk' }
      ],
      riskLevelOptions: [
        { label: '高风险', value: 'high' },
        { label: '中风险', value: 'medium' },
        { label: '低风险', value: 'low' }
      ],
      reviewStatusMap: {
        pending: { label: '待审查', type: 'info' },
        reviewing: { label: '审查中', type: 'warning' },
        done: { label: '已完成', type: 'success' },
        risk: { label: '有风险', type: 'danger' }
      },
      riskLevelMap: {
        high: { label: '高风险', type: 'danger' },
        medium: { label: '中风险', type: 'warning' },
        low: { label: '低风险', type: 'success' }
      },
      tableData: []
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
    parsedRiskClauses() {
      return this.parseJsonField(this.currentRow.riskClauses)
    },
    parsedMissingClauses() {
      return this.parseJsonField(this.currentRow.missingClauses)
    },
    parsedDeviationClauses() {
      return this.parseJsonField(this.currentRow.deviationClauses)
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    parseJsonField(val) {
      if (!val) return []
      if (Array.isArray(val)) return val
      try { return JSON.parse(val) } catch (e) { return [] }
    },
    async fetchData() {
      this.loading = true
      try {
        const res = await getSmartReviewList({
          contractType: this.queryForm.contractType,
          reviewStatus: this.queryForm.reviewStatus,
          riskLevel: this.queryForm.riskLevel,
          pageNumber: this.queryForm.pageNo,
          pageSize: this.queryForm.pageSize
        })
        if (res && res.result === 200 && res.data) {
          this.tableData = res.data.tlist || []
          this.total = res.data.totalRecord || 0
        } else {
          this.tableData = []
          this.total = 0
        }
      } catch (e) {
        this.tableData = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    handleQuery() {
      this.queryForm.pageNo = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryForm.contractType = ''
      this.queryForm.reviewStatus = ''
      this.queryForm.riskLevel = ''
      this.queryForm.pageNo = 1
      this.fetchData()
    },
    handleSelectionChange(val) { this.multipleSelection = val },
    handleAdd() {
      this.addForm = { contractName: '', contractType: '', riskLevel: '', reviewTime: '', reviewer: '' }
      this.addDialogVisible = true
    },
    async submitAdd() {
      this.$refs.addForm.validate(async(valid) => {
        if (!valid) return
        try {
          const res = await addSmartReview({ ...this.addForm, reviewStatus: 'pending' })
          if (res && res.result === 200) {
            this.$message.success('审查任务已创建')
            this.addDialogVisible = false
            this.fetchData()
          } else {
            this.$message.error(res.msg || '创建失败')
          }
        } catch (e) {
          this.$message.error('创建失败')
        }
      })
    },
    handleEdit(row) {
      this.editForm = {
        id: row.reviewId,
        contractName: row.contractName || '',
        contractType: row.contractType || '',
        reviewStatus: row.reviewStatus || '',
        riskLevel: row.riskLevel || '',
        reviewer: row.reviewer || '',
        remark: row.remark || ''
      }
      this.editDialogVisible = true
    },
    async submitEdit() {
      this.$refs.editForm.validate(async(valid) => {
        if (!valid) return
        try {
          const res = await updateSmartReview(this.editForm)
          if (res && res.result === 200) {
            this.$message.success('编辑成功')
            this.editDialogVisible = false
            this.fetchData()
          } else {
            this.$message.error(res.msg || '编辑失败')
          }
        } catch (e) {
          this.$message.error('编辑失败')
        }
      })
    },
    async handleDetail(row) {
      try {
        const res = await getSmartReviewDetail(row.reviewId)
        if (res && res.result === 200 && res.data) {
          this.currentRow = res.data
        } else {
          this.currentRow = row
        }
      } catch (e) {
        this.currentRow = row
      }
      this.activeCollapse = ['risk']
      this.detailDialogVisible = true
    },
    async handleDelete(row) {
      this.$confirm(`确认删除合同「${row.contractName}」的审查记录？`, '提示', { type: 'warning' }).then(async() => {
        try {
          const res = await deleteSmartReview(row.reviewId)
          if (res && res.result === 200) {
            this.$message.success('删除成功')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (e) { this.$message.error('删除失败') }
      }).catch(() => {})
    },
    async handleBatchDelete() {
      this.$confirm(`确认删除选中的 ${this.multipleSelection.length} 条记录？`, '提示', { type: 'warning' }).then(async() => {
        try {
          const ids = this.multipleSelection.map(r => r.reviewId)
          const res = await batchDeleteSmartReview(ids)
          if (res && res.result === 200) {
            this.$message.success('批量删除成功')
            this.multipleSelection = []
            this.fetchData()
          } else {
            this.$message.error(res.msg || '批量删除失败')
          }
        } catch (e) { this.$message.error('批量删除失败') }
      }).catch(() => {})
    },
    handleSizeChange(val) { this.queryForm.pageSize = val; this.fetchData() },
    handleCurrentChange(val) { this.queryForm.pageNo = val; this.fetchData() }
  }
}
</script>

<style scoped>
.contract-page .page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 0 16px;
}
.contract-page .page-header-left {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
}
.contract-page .page-header-left i {
  color: inherit;
}
.contract-page .page-header-desc {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.85);
}
.contract-page .search-card .el-form-item {
  margin-bottom: 0;
}
</style>
