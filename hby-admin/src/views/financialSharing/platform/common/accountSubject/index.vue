<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>会计科目管理</span>
      </div>

      <el-form :model="queryForm" ref="queryForm" :inline="true" class="demo-form-inline">
        <el-form-item label="科目编码" prop="subjectCode">
          <el-input
            v-model="queryForm.subjectCode"
            placeholder="请输入科目编码"
            clearable
            style="width: 200px"
          />
        </el-form-item>

        <el-form-item label="科目名称" prop="subjectName">
          <el-input
            v-model="queryForm.subjectName"
            placeholder="请输入科目名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>

        <el-form-item label="科目类型" prop="subjectType">
          <el-select
            v-model="queryForm.subjectType"
            placeholder="请选择科目类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="(name, value) in SUBJECT_TYPE_NAME"
              :key="value"
              :label="name"
              :value="parseInt(value)"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="科目级次" prop="subjectLevel">
          <el-select
            v-model="queryForm.subjectLevel"
            placeholder="请选择科目级次"
            clearable
            style="width: 120px"
          >
            <el-option label="1级" :value="1" />
            <el-option label="2级" :value="2" />
            <el-option label="3级" :value="3" />
            <el-option label="4级" :value="4" />
            <el-option label="5级" :value="5" />
          </el-select>
        </el-form-item>

        <el-form-item label="启用状态" prop="isEnabled">
          <el-select
            v-model="queryForm.isEnabled"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="(name, value) in ENABLED_STATUS_NAME"
              :key="value"
              :label="name"
              :value="parseInt(value)"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="box-card" style="margin-top: 20px">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
          >新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
          >修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
          >删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
          >导出</el-button>
        </el-col>
      </el-row>

      <!-- 数据表格 -->
      <el-table
        v-loading="loading"
        :data="accountSubjectList"
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="科目编码" prop="subjectCode" width="120" />
        <el-table-column label="科目名称" prop="subjectName" width="200" show-overflow-tooltip />
        <el-table-column label="科目级次" prop="subjectLevel" width="80" align="center" />
        <el-table-column label="上级科目" prop="parentSubjectName" width="180" show-overflow-tooltip />
        <el-table-column label="科目类型" prop="subjectTypeName" width="100" align="center" />
        <el-table-column label="余额方向" prop="balanceDirectionName" width="100" align="center" />
        <el-table-column label="是否末级" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isLeaf === 1 ? 'success' : 'info'" size="mini">
              {{ IS_LEAF_NAME[scope.row.isLeaf] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="启用状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isEnabled"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="160" align="center" />
        <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-show="total > 0"
        background
        :current-page="queryForm.pageNumber"
        :page-size="queryForm.pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </el-card>

    <!-- 添加或修改会计科目对话框 -->
    <account-subject-form
      v-if="formVisible"
      :visible.sync="formVisible"
      :form-type="formType"
      :form-data="formData"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script>
import {
  getAccountSubjectPage,
  saveOrUpdateAccountSubject,
  deleteAccountSubject,
  batchDeleteAccountSubjects,
  updateAccountSubjectStatus,
  batchUpdateAccountSubjectStatus
} from '@/api/financialSharing/system'
import {
  SUBJECT_TYPE_NAME,
  BALANCE_DIRECTION_NAME,
  IS_LEAF_NAME,
  ENABLED_STATUS_NAME,
  DEFAULT_PAGE_CONFIG,
  DEFAULT_TENANT_CONFIG
} from '../consts'
import AccountSubjectForm from '../components/AccountSubjectForm'
// import Pagination from '@/components/Pagination' // 使用 Element UI 的 el-pagination

export default {
  name: 'AccountSubject',
  components: {
    AccountSubjectForm
    // Pagination // 使用 Element UI 的 el-pagination，不需要注册组件
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 会计科目表格数据
      accountSubjectList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      formVisible: false,
      // 表单类型 add/edit/view
      formType: 'add',
      // 表单数据
      formData: {},
      // 查询参数
      queryForm: {
        ...DEFAULT_PAGE_CONFIG,
        ...DEFAULT_TENANT_CONFIG,
        subjectCode: null,
        subjectName: null,
        subjectType: null,
        subjectLevel: null,
        isEnabled: null
      },
      // 常量
      SUBJECT_TYPE_NAME,
      BALANCE_DIRECTION_NAME,
      IS_LEAF_NAME,
      ENABLED_STATUS_NAME
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询会计科目列表 */
    getList() {
      this.loading = true
      console.log('发送的查询参数:', this.queryForm)
      getAccountSubjectPage(this.queryForm).then(response => {
        if (response.code === 1) {
          this.accountSubjectList = response.data.tlist
          this.total = response.data.totalRecord
          console.log('查询成功，数据条数:', this.accountSubjectList.length)
        } else {
          this.$message.error(response.msg || '查询失败')
        }
        this.loading = false
      }).catch(error => {
        console.log('查询异常:', error)
        this.$message.error('查询失败：' + error.message)
        this.loading = false
      })
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },

    /** 重置按钮操作 */
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.queryForm = {
        ...DEFAULT_PAGE_CONFIG,
        ...DEFAULT_TENANT_CONFIG,
        subjectCode: null,
        subjectName: null,
        subjectType: null,
        subjectLevel: null,
        isEnabled: null
      }
      this.handleQuery()
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.formData = {}
      this.formType = 'add'
      this.formVisible = true
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      const subjectId = row ? row.subjectId : this.ids[0]
      if (!subjectId) {
        this.$message.warning('请选择要修改的数据')
        return
      }

      this.formData = row || this.accountSubjectList.find(item => item.subjectId === subjectId)
      this.formType = 'edit'
      this.formVisible = true
    },

    /** 查看按钮操作 */
    handleView(row) {
      this.formData = row
      this.formType = 'view'
      this.formVisible = true
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const subjectIds = row ? [row.subjectId] : this.ids
      const subjectNames = row ? [row.subjectName] : this.accountSubjectList.filter(item => this.ids.includes(item.subjectId)).map(item => item.subjectName)

      this.$confirm(`是否确认删除会计科目"${subjectNames.join('、')}"？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const deletePromise = subjectIds.length === 1
          ? deleteAccountSubject(subjectIds[0])
          : batchDeleteAccountSubjects(subjectIds)

        deletePromise.then(response => {
          if (response.code === 1) {
            this.getList()
            this.$message.success('删除成功')
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        })
      })
    },

    /** 导出按钮操作 */
    handleExport() {
      try {
        const data = this.list || this.tableData || []
        if (!data.length) { this.$message.warning('暂无数据可导出'); return }
        const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '科目导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) { this.$message.error('导出失败') }
    },

    /** 状态修改 */
    handleStatusChange(row) {
      const text = row.isEnabled === 1 ? '启用' : '禁用'
      this.$confirm(`确认要${text}"${row.subjectName}"吗？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateAccountSubjectStatus(row.subjectId, row.isEnabled).then(response => {
          if (response.data.code === 1) {
            this.$message.success(`${text}成功`)
          } else {
            this.$message.error(response.data.msg || `${text}失败`)
            // 恢复原状态
            row.isEnabled = row.isEnabled === 1 ? 0 : 1
          }
        }).catch(() => {
          // 恢复原状态
          row.isEnabled = row.isEnabled === 1 ? 0 : 1
        })
      }).catch(() => {
        // 恢复原状态
        row.isEnabled = row.isEnabled === 1 ? 0 : 1
      })
    },

    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.subjectId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },

    /** 行点击事件 */
    handleRowClick(row) {
      this.$refs.table && this.$refs.table.toggleRowSelection(row)
    },

    /** 表单成功回调 */
    handleFormSuccess() {
      this.formVisible = false
      this.getList()
    },

    /** 改变每一页请求数量 */
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.getList()
    },

    /** 跳转页数 */
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.getList()
    }
  }
}
</script>

<style scoped>
.demo-form-inline .el-form-item {
  margin-bottom: 10px;
}
</style>