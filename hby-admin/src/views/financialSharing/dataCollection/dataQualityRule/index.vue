<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="规则编码">
        <el-input v-model="queryForm.ruleCode" placeholder="请输入规则编码" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="规则名称">
        <el-input v-model="queryForm.ruleName" placeholder="请输入规则名称" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="规则类型">
        <el-select v-model="queryForm.ruleType" placeholder="请选择规则类型" clearable style="width: 150px">
          <el-option label="完整性" value="COMPLETENESS" />
          <el-option label="准确性" value="ACCURACY" />
          <el-option label="一致性" value="CONSISTENCY" />
          <el-option label="及时性" value="TIMELINESS" />
          <el-option label="有效性" value="VALIDITY" />
        </el-select>
      </el-form-item>
      <el-form-item label="检查级别">
        <el-select v-model="queryForm.checkLevel" placeholder="请选择检查级别" clearable style="width: 120px">
          <el-option label="错误" value="ERROR" />
          <el-option label="警告" value="WARNING" />
          <el-option label="提示" value="INFO" />
        </el-select>
      </el-form-item>
      <el-form-item label="启用状态">
        <el-select v-model="queryForm.isEnabled" placeholder="请选择启用状态" clearable style="width: 120px">
          <el-option label="启用" value="Y" />
          <el-option label="禁用" value="N" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="dataList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="规则编码" prop="ruleCode" width="150" />
      <el-table-column label="规则名称" prop="ruleName" width="200" show-overflow-tooltip />
      <el-table-column label="规则类型" prop="ruleType" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.ruleType === 'COMPLETENESS'" type="primary">完整性</el-tag>
          <el-tag v-else-if="scope.row.ruleType === 'ACCURACY'" type="success">准确性</el-tag>
          <el-tag v-else-if="scope.row.ruleType === 'CONSISTENCY'" type="info">一致性</el-tag>
          <el-tag v-else-if="scope.row.ruleType === 'TIMELINESS'" type="warning">及时性</el-tag>
          <el-tag v-else-if="scope.row.ruleType === 'VALIDITY'" type="danger">有效性</el-tag>
          <el-tag v-else>{{ scope.row.ruleType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="检查级别" prop="checkLevel" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.checkLevel === 'ERROR'" type="danger">错误</el-tag>
          <el-tag v-else-if="scope.row.checkLevel === 'WARNING'" type="warning">警告</el-tag>
          <el-tag v-else-if="scope.row.checkLevel === 'INFO'" type="info">提示</el-tag>
          <el-tag v-else>{{ scope.row.checkLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="阈值" prop="thresholdValue" width="100" align="center" />
      <el-table-column label="排序号" prop="sortNo" width="80" align="center" />
      <el-table-column label="启用状态" prop="isEnabled" width="100" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isEnabled"
            active-value="Y"
            inactive-value="N"
            @change="handleToggleEnabled(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="描述" prop="description" min-width="200" show-overflow-tooltip />
      <el-table-column label="创建时间" prop="createTime" width="160">
        <template slot-scope="scope">
          {{ formatDateTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNumber"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 批量操作按钮 -->
    <div v-show="selectedIds.length > 0" class="batch-operation">
      <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete">批量删除</el-button>
      <span class="selected-count">已选择 {{ selectedIds.length }} 项</span>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="900px" @close="closeDialog">
      <el-form ref="ruleForm" :model="ruleForm" :rules="ruleRules" label-width="120px">
        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规则编码" prop="ruleCode">
              <el-input v-model="ruleForm.ruleCode" placeholder="请输入规则编码" :disabled="!!ruleForm.ruleId" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规则名称" prop="ruleName">
              <el-input v-model="ruleForm.ruleName" placeholder="请输入规则名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规则类型" prop="ruleType">
              <el-select v-model="ruleForm.ruleType" placeholder="请选择规则类型" style="width: 100%">
                <el-option label="完整性" value="COMPLETENESS" />
                <el-option label="准确性" value="ACCURACY" />
                <el-option label="一致性" value="CONSISTENCY" />
                <el-option label="及时性" value="TIMELINESS" />
                <el-option label="有效性" value="VALIDITY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查级别" prop="checkLevel">
              <el-select v-model="ruleForm.checkLevel" placeholder="请选择检查级别" style="width: 100%">
                <el-option label="错误" value="ERROR" />
                <el-option label="警告" value="WARNING" />
                <el-option label="提示" value="INFO" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="阈值" prop="thresholdValue">
              <el-input v-model="ruleForm.thresholdValue" placeholder="请输入阈值" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序号" prop="sortNo">
              <el-input-number v-model="ruleForm.sortNo" :min="0" :max="9999" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="是否启用" prop="isEnabled">
              <el-radio-group v-model="ruleForm.isEnabled">
                <el-radio label="Y">启用</el-radio>
                <el-radio label="N">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 检查配置 -->
        <el-divider content-position="left">检查配置</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="检查SQL" prop="checkSql">
              <el-input
                v-model="ruleForm.checkSql"
                type="textarea"
                :rows="6"
                placeholder="请输入检查SQL语句，例如：SELECT COUNT(*) FROM table WHERE field IS NULL"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="检查条件" prop="checkCondition">
              <el-input
                v-model="ruleForm.checkCondition"
                type="textarea"
                :rows="3"
                placeholder="请输入检查条件（JSON格式），例如：{&quot;operator&quot;: &quot;>&quot;, &quot;value&quot;: 0}"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 提示信息 -->
        <el-divider content-position="left">提示信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="错误提示" prop="errorMessage">
              <el-input v-model="ruleForm.errorMessage" placeholder="请输入错误提示信息" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="改进建议" prop="suggestion">
              <el-input v-model="ruleForm.suggestion" placeholder="请输入改进建议" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="规则描述" prop="description">
              <el-input
                v-model="ruleForm.description"
                type="textarea"
                :rows="3"
                placeholder="请输入规则描述"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { queryQualityRulePage, queryQualityRuleById, saveQualityRule, deleteQualityRule, batchDeleteQualityRule, toggleQualityRuleEnabled } from '@/api/financialSharing/dataCollection'
import { formatDateTime } from '@/utils/dateUtil'

export default {
  name: 'DataQualityRule',
  data() {
    return {
      // 加载状态
      loading: false,
      // 查询表单
      queryForm: {
        pageNumber: 1,
        pageSize: 10,
        ruleCode: null,
        ruleName: null,
        ruleType: null,
        checkLevel: null,
        isEnabled: null
      },
      // 数据列表
      dataList: [],
      // 总记录数
      total: 0,
      // 选中的ID列表
      selectedIds: [],
      // 对话框标题
      dialogTitle: '',
      // 对话框显示状态
      dialogVisible: false,
      // 规则表单
      ruleForm: {
        ruleId: null,
        ruleCode: '',
        ruleName: '',
        ruleType: '',
        checkLevel: 'WARNING',
        checkSql: '',
        checkCondition: '',
        thresholdValue: '',
        errorMessage: '',
        suggestion: '',
        isEnabled: 'Y',
        sortNo: 0,
        description: ''
      },
      // 表单验证规则
      ruleRules: {
        ruleCode: [
          { required: true, message: '请输入规则编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        ruleName: [
          { required: true, message: '请输入规则名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        ruleType: [
          { required: true, message: '请选择规则类型', trigger: 'change' }
        ],
        checkLevel: [
          { required: true, message: '请选择检查级别', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 格式化日期时间
    formatDateTime(dateTime) {
      return formatDateTime(dateTime)
    },

    // 查询列表
    getList() {
      this.loading = true
      queryQualityRulePage(this.queryForm).then(response => {
        if (response.code === 1) {
          this.dataList = response.data.records || []
          this.total = response.data.total || 0
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    // 查询按钮
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },

    // 重置按钮
    handleReset() {
      this.queryForm = {
        pageNumber: 1,
        pageSize: 10,
        ruleCode: null,
        ruleName: null,
        ruleType: null,
        checkLevel: null,
        isEnabled: null
      }
      this.getList()
    },

    // 新增按钮
    handleAdd() {
      this.resetForm()
      this.dialogTitle = '新增质量规则'
      this.dialogVisible = true
    },

    // 编辑按钮
    handleEdit(row) {
      this.resetForm()
      queryQualityRuleById({ ruleId: row.ruleId }).then(response => {
        if (response.code === 1) {
          this.ruleForm = response.data
          this.dialogTitle = '编辑质量规则'
          this.dialogVisible = true
        }
      })
    },

    // 删除按钮
    handleDelete(row) {
      this.$confirm('确认删除该质量规则吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteQualityRule({ ruleId: row.ruleId }).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          }
        })
      })
    },

    // 批量删除
    handleBatchDelete() {
      if (this.selectedIds.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm('确认删除选中的质量规则吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        batchDeleteQualityRule(this.selectedIds).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          }
        })
      })
    },

    // 启用/禁用切换
    handleToggleEnabled(row) {
      const text = row.isEnabled === 'Y' ? '禁用' : '启用'
      this.$confirm(`确认${text}该规则吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        toggleQualityRuleEnabled({
          ruleId: row.ruleId,
          isEnabled: row.isEnabled
        }).then(response => {
          if (response.code === 1) {
            this.$message.success(`${text}成功`)
            this.getList()
          } else {
            // 恢复原状态
            row.isEnabled = row.isEnabled === 'Y' ? 'N' : 'Y'
          }
        }).catch(() => {
          // 恢复原状态
          row.isEnabled = row.isEnabled === 'Y' ? 'N' : 'Y'
        })
      }).catch(() => {
        // 取消操作，恢复原状态
        row.isEnabled = row.isEnabled === 'Y' ? 'N' : 'Y'
      })
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedIds = selection.map(item => item.ruleId)
    },

    // 提交表单
    handleSubmit() {
      this.$refs.ruleForm.validate(valid => {
        if (valid) {
          saveQualityRule(this.ruleForm).then(response => {
            if (response.code === 1) {
              this.$message.success(this.ruleForm.ruleId ? '修改成功' : '新增成功')
              this.dialogVisible = false
              this.getList()
            }
          })
        }
      })
    },

    // 关闭对话框
    closeDialog() {
      this.dialogVisible = false
      this.resetForm()
    },

    // 重置表单
    resetForm() {
      this.ruleForm = {
        ruleId: null,
        ruleCode: '',
        ruleName: '',
        ruleType: '',
        checkLevel: 'WARNING',
        checkSql: '',
        checkCondition: '',
        thresholdValue: '',
        errorMessage: '',
        suggestion: '',
        isEnabled: 'Y',
        sortNo: 0,
        description: ''
      }
      if (this.$refs.ruleForm) {
        this.$refs.ruleForm.clearValidate()
      }
    }
  }
}
</script>

<style scoped>
.query-form {
  background: #fff;
  padding: 20px;
  margin-bottom: 10px;
}
</style>

