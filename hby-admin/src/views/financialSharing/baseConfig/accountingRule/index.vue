<template>
  <div class="accounting-rule-container">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="规则名称" prop="ruleName">
          <el-input
            v-model="searchForm.ruleName"
            placeholder="请输入规则名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="规则类型" prop="ruleType">
          <el-select
            v-model="searchForm.ruleType"
            placeholder="请选择规则类型"
            clearable
            style="width: 150px"
          >
            <el-option label="分录规则" value="ENTRY" />
            <el-option label="分发规则" value="DISPATCH" />
            <el-option label="转换规则" value="TRANSFORM" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-select
            v-model="searchForm.isEnabled"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增规则</el-button>
      <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete" :disabled="!multipleSelection.length">
        批量删除
      </el-button>
      <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
    </div>

    <!-- 表格区域 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="ruleCode" label="规则编码" width="150" show-overflow-tooltip />
        <el-table-column prop="ruleName" label="规则名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="ruleType" label="规则类型" width="120">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.ruleType === 'ENTRY'" type="primary">分录规则</el-tag>
            <el-tag v-else-if="scope.row.ruleType === 'DISPATCH'" type="success">分发规则</el-tag>
            <el-tag v-else-if="scope.row.ruleType === 'TRANSFORM'" type="warning">转换规则</el-tag>
            <el-tag v-else type="info">{{ scope.row.ruleType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80" align="center" />
        <el-table-column prop="isEnabled" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isEnabled"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="handleTest(scope.row)">测试</el-button>
            <el-button size="mini" type="warning" @click="handleCopy(scope.row)">复制</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        :model="formData"
        :rules="formRules"
        ref="ruleForm"
        label-width="120px"
      >
        <el-form-item label="规则编码" prop="ruleCode">
          <el-input v-model="formData.ruleCode" placeholder="请输入规则编码" />
        </el-form-item>
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="formData.ruleName" placeholder="请输入规则名称" />
        </el-form-item>
        <el-form-item label="规则类型" prop="ruleType">
          <el-select v-model="formData.ruleType" placeholder="请选择规则类型" style="width: 100%">
            <el-option label="分录规则" value="ENTRY" />
            <el-option label="分发规则" value="DISPATCH" />
            <el-option label="转换规则" value="TRANSFORM" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-input-number v-model="formData.priority" :min="1" :max="999" />
        </el-form-item>


        <el-form-item label="借方科目" prop="debitAccount">
          <el-input v-model="formData.debitAccount" placeholder="请输入借方科目" />
        </el-form-item>
        <el-form-item label="贷方科目" prop="creditAccount">
          <el-input v-model="formData.creditAccount" placeholder="请输入贷方科目" />
        </el-form-item>
        <el-form-item label="核算维度配置" prop="dimensionConfig">
          <el-input
            v-model="formData.dimensionConfig"
            type="textarea"
            :rows="4"
            placeholder="请输入核算维度配置(JSON格式)"
          />
        </el-form-item>
        <el-form-item label="生效日期" prop="effectiveDate">
          <el-date-picker
            v-model="formData.effectiveDate"
            type="date"
            placeholder="选择生效日期"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="失效日期" prop="expiryDate">
          <el-date-picker
            v-model="formData.expiryDate"
            type="date"
            placeholder="选择失效日期"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="是否启用" prop="isEnabled">
          <el-switch v-model="formData.isEnabled" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="saveLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 测试对话框 -->
    <el-dialog
      title="规则测试"
      :visible.sync="testDialogVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form :model="testForm" label-width="120px">
        <el-form-item label="测试数据">
          <el-input
            v-model="testForm.testData"
            type="textarea"
            :rows="8"
            placeholder='请输入测试数据(JSON格式)&#10;示例：{"amount": 1000, "description": "测试业务"}'
          />
        </el-form-item>
        <el-form-item label="测试结果" v-if="testResult">
          <el-input
            v-model="testResult"
            type="textarea"
            :rows="8"
            readonly
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="testDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleExecuteTest" :loading="testLoading">执行测试</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { accountingRuleApi } from '@/api/financialSharing/baseConfig'

export default {
  name: 'AccountingRule',
  data() {
    return {
      loading: false,
      saveLoading: false,
      testLoading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        ruleName: '',
        ruleType: '',
        isEnabled: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增会计规则',
      formData: {
        ruleId: null,
        ruleCode: '',
        ruleName: '',
        ruleType: '',
        priority: 1,
        debitAccount: '',
        creditAccount: '',
        dimensionConfig: '',
        effectiveDate: '',
        expiryDate: '',
        isEnabled: 1,
        remark: ''
      },
      formRules: {
        ruleCode: [
          { required: true, message: '请输入规则编码', trigger: 'blur' }
        ],
        ruleName: [
          { required: true, message: '请输入规则名称', trigger: 'blur' }
        ],
        ruleType: [
          { required: true, message: '请选择规则类型', trigger: 'change' }
        ],
        priority: [
          { required: true, message: '请输入优先级', trigger: 'blur' }
        ]
      },
      testDialogVisible: false,
      testForm: {
        ruleId: null,
        testData: ''
      },
      testResult: ''
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          page: this.pagination.currentPage - 1,
          size: this.pagination.pageSize
        }
        const res = await accountingRuleApi.getList(params)
        if (res.code === 1) {
          this.tableData = res.data.records || res.data.tlist || []
          this.pagination.total = res.data.total || res.data.totalRecord || 0
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        console.error('查询失败:', error)
        this.$message.error('查询失败')
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.handleSearch()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增会计规则'
      this.formData = {
        ruleId: null,
        ruleCode: '',
        ruleName: '',
        ruleType: '',
        priority: 1,
        debitAccount: '',
        creditAccount: '',
        dimensionConfig: '',
        effectiveDate: '',
        expiryDate: '',
        isEnabled: 1,
        remark: ''
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.ruleForm && this.$refs.ruleForm.clearValidate()
      })
    },

    // 编辑
    async handleEdit(row) {
      this.dialogTitle = '编辑会计规则'
      try {
        const res = await accountingRuleApi.getDetail(row.ruleId)
        if (res.code === 1) {
          // 数据兼容处理：如果旧字段存在但新字段不存在，则进行转换
          const data = { ...res.data }

          // 兼容旧数据：ruleCondition -> debitAccount
          if (data.ruleCondition && !data.debitAccount) {
            data.debitAccount = data.ruleCondition
          }

          // 兼容旧数据：ruleAction -> creditAccount
          if (data.ruleAction && !data.creditAccount) {
            data.creditAccount = data.ruleAction
          }

          this.formData = data
          this.dialogVisible = true
          this.$nextTick(() => {
            this.$refs.ruleForm && this.$refs.ruleForm.clearValidate()
          })
        } else {
          this.$message.error(res.msg || '获取详情失败')
        }
      } catch (error) {
        console.error('获取详情失败:', error)
        this.$message.error('获取详情失败')
      }
    },

    // 提交
    handleSubmit() {
      this.$refs.ruleForm.validate(async (valid) => {
        if (valid) {
          this.saveLoading = true
          try {
            const res = await accountingRuleApi.save(this.formData)
            if (res.code === 1) {
              this.$message.success(this.formData.ruleId ? '修改成功' : '新增成功')
              this.dialogVisible = false
              this.loadData()
            } else {
              this.$message.error(res.msg || '保存失败')
            }
          } catch (error) {
            console.error('保存失败:', error)
            this.$message.error('保存失败')
          } finally {
            this.saveLoading = false
          }
        }
      })
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除该规则吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await accountingRuleApi.delete(row.ruleId)
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },

    // 批量删除
    handleBatchDelete() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm(`确定要删除选中的 ${this.multipleSelection.length} 条规则吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.multipleSelection.map(item => item.ruleId)
          const res = await accountingRuleApi.batchDelete(ids)
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },

    // 测试
    handleTest(row) {
      this.testForm.ruleId = row.ruleId
      // 提供默认测试数据示例
      this.testForm.testData = JSON.stringify({
        amount: 1000,
        description: '测试业务数据',
        businessType: 'TEST'
      }, null, 2)
      this.testResult = ''
      this.testDialogVisible = true
    },

    // 执行测试
    async handleExecuteTest() {
      if (!this.testForm.testData) {
        this.$message.warning('请输入测试数据')
        return
      }

      // 验证测试数据是否为有效JSON
      let testDataObj
      try {
        testDataObj = JSON.parse(this.testForm.testData)
      } catch (e) {
        this.$message.error('测试数据格式错误，请输入有效的JSON格式')
        return
      }

      this.testLoading = true
      try {
        const res = await accountingRuleApi.test(this.testForm.ruleId, testDataObj)
        if (res.code === 1) {
          this.testResult = JSON.stringify(res.data, null, 2)
          this.$message.success('测试执行成功')
        } else {
          this.$message.error(res.msg || '测试失败')
          this.testResult = JSON.stringify({
            error: res.msg || '测试失败',
            code: res.code
          }, null, 2)
        }
      } catch (error) {
        console.error('测试失败:', error)
        this.$message.error('测试失败：' + (error.message || '网络错误'))
        this.testResult = JSON.stringify({
          error: error.message || '网络错误',
          details: error
        }, null, 2)
      } finally {
        this.testLoading = false
      }
    },

    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制会计规则'
      this.formData = {
        ...row,
        ruleId: null,
        ruleCode: row.ruleCode + '_copy',
        ruleName: row.ruleName + '_副本'
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.ruleForm && this.$refs.ruleForm.clearValidate()
      })
    },

    // 状态切换
    async handleStatusChange(row) {
      try {
        const res = await accountingRuleApi.updateStatus(row.ruleId, row.isEnabled)
        if (res.code === 1) {
          this.$message.success('状态更新成功')
          this.loadData()
        } else {
          this.$message.error(res.msg || '状态更新失败')
          // 恢复原状态
          row.isEnabled = row.isEnabled === 1 ? 0 : 1
        }
      } catch (error) {
        console.error('状态更新失败:', error)
        this.$message.error('状态更新失败')
        // 恢复原状态
        row.isEnabled = row.isEnabled === 1 ? 0 : 1
      }
    },

    // 导出
    handleExport() {
      accountingRuleApi.export(this.searchForm).then(res => {
        if (res.code === 1) {
          this.$message.success('导出成功')
          if (res.data && res.data.url) {
            window.open(res.data.url)
          }
        } else {
          this.$message.error(res.msg || '导出失败')
        }
      }).catch(() => {
        this.$message.error('导出失败')
      })
    },

    // 分页大小改变
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.currentPage = 1
      this.loadData()
    },

    // 当前页改变
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadData()
    },

    // 选择改变
    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    // 对话框关闭
    handleDialogClose() {
      this.$refs.ruleForm && this.$refs.ruleForm.resetFields()
    }
  }
}
</script>

<style lang="scss" scoped>
.accounting-rule-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);

  .search-container {
    background: #fff;
    padding: 20px;
    margin-bottom: 20px;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .search-form {
      margin-bottom: 0;
    }
  }

  .toolbar {
    background: #fff;
    padding: 15px 20px;
    margin-bottom: 20px;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }

  .table-container {
    background: #fff;
    padding: 20px;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .pagination-container {
      margin-top: 20px;
      text-align: right;
    }
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>