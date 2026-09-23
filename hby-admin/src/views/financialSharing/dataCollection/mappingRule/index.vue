<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :model="queryForm" class="query-form" inline>
      <el-form-item label="规则编码">
        <el-input v-model="queryForm.ruleCode" placeholder="请输入规则编码" clearable style="width: 200px" />
      </el-form-item>
      <el-form-item label="规则名称">
        <el-input v-model="queryForm.ruleName" placeholder="请输入规则名称" clearable style="width: 200px" />
      </el-form-item>
      <el-form-item label="数据源">
        <el-select v-model="queryForm.sourceId" placeholder="请选择数据源" clearable style="width: 200px">
          <el-option
            v-for="item in dataSourceList"
            :key="item.sourceId"
            :label="item.sourceName"
            :value="item.sourceId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="目标表">
        <el-input v-model="queryForm.targetTable" placeholder="请输入目标表" clearable style="width: 200px" />
      </el-form-item>
      <el-form-item label="是否启用">
        <el-select v-model="queryForm.isEnabled" placeholder="请选择" clearable style="width: 120px">
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
    <el-table
      v-loading="loading"
      :data="dataList"
      border
      stripe
      style="width: 100%"
    >
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="ruleCode" label="规则编码" width="150" show-overflow-tooltip />
      <el-table-column prop="ruleName" label="规则名称" width="200" show-overflow-tooltip />
      <el-table-column prop="sourceId" label="数据源" width="150" show-overflow-tooltip>
        <template slot-scope="scope">
          {{ getDataSourceName(scope.row.sourceId) }}
        </template>
      </el-table-column>
      <el-table-column prop="targetTable" label="目标表" width="150" show-overflow-tooltip />
      <el-table-column prop="conflictStrategy" label="冲突策略" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.conflictStrategy === 'SKIP'" type="info" size="small">跳过</el-tag>
          <el-tag v-else-if="scope.row.conflictStrategy === 'UPDATE'" type="warning" size="small">更新</el-tag>
          <el-tag v-else-if="scope.row.conflictStrategy === 'ERROR'" type="danger" size="small">报错</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="batchSize" label="批次大小" width="100" align="center" />
      <el-table-column prop="isIncremental" label="是否增量" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isIncremental === 'Y'" type="success" size="small">是</el-tag>
          <el-tag v-else type="info" size="small">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="isEnabled" label="启用状态" width="100" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isEnabled"
            active-value="Y"
            inactive-value="N"
            @change="handleToggleEnabled(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="sortNo" label="排序号" width="80" align="center" />
      <el-table-column prop="createTime" label="创建时间" width="160" align="center">
        <template slot-scope="scope">
          {{ formatDateTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="small" icon="el-icon-document-copy" @click="handleCopy(scope.row)">复制</el-button>
          <el-button type="text" size="small" icon="el-icon-delete" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNumber"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="900px"
      :close-on-click-modal="false"
      @close="closeDialog"
    >
      <el-form
        ref="mappingRuleForm"
        :model="mappingRuleForm"
        :rules="mappingRuleRules"
        label-width="120px"
      >
        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规则编码" prop="ruleCode">
              <el-input v-model="mappingRuleForm.ruleCode" placeholder="请输入规则编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规则名称" prop="ruleName">
              <el-input v-model="mappingRuleForm.ruleName" placeholder="请输入规则名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="数据源" prop="sourceId">
              <el-select v-model="mappingRuleForm.sourceId" placeholder="请选择数据源" style="width: 100%">
                <el-option
                  v-for="item in dataSourceList"
                  :key="item.sourceId"
                  :label="item.sourceName"
                  :value="item.sourceId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标表" prop="targetTable">
              <el-input v-model="mappingRuleForm.targetTable" placeholder="请输入目标表名" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 数据配置 -->
        <el-divider content-position="left">数据配置</el-divider>
        <el-form-item label="源查询语句" prop="sourceQuery">
          <el-input
            v-model="mappingRuleForm.sourceQuery"
            type="textarea"
            :rows="3"
            placeholder="请输入SQL查询语句或API路径"
          />
        </el-form-item>
        <el-form-item label="字段映射" prop="fieldMappings">
          <el-input
            v-model="mappingRuleForm.fieldMappings"
            type="textarea"
            :rows="4"
            placeholder='请输入字段映射JSON，例如：[{"source":"id","target":"ID"},{"source":"name","target":"NAME"}]'
          />
        </el-form-item>
        <el-form-item label="过滤条件">
          <el-input
            v-model="mappingRuleForm.filterCondition"
            type="textarea"
            :rows="2"
            placeholder="请输入过滤条件，例如：status = 'ACTIVE'"
          />
        </el-form-item>

        <!-- 转换与校验 -->
        <el-divider content-position="left">转换与校验</el-divider>
        <el-form-item label="转换规则">
          <el-input
            v-model="mappingRuleForm.transformRules"
            type="textarea"
            :rows="3"
            placeholder='请输入转换规则JSON，例如：[{"field":"amount","type":"multiply","value":100}]'
          />
        </el-form-item>
        <el-form-item label="校验规则">
          <el-input
            v-model="mappingRuleForm.validationRules"
            type="textarea"
            :rows="3"
            placeholder='请输入校验规则JSON，例如：[{"field":"amount","type":"required"},{"field":"email","type":"email"}]'
          />
        </el-form-item>

        <!-- 执行配置 -->
        <el-divider content-position="left">执行配置</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="冲突策略" prop="conflictStrategy">
              <el-select v-model="mappingRuleForm.conflictStrategy" placeholder="请选择" style="width: 100%">
                <el-option label="跳过" value="SKIP" />
                <el-option label="更新" value="UPDATE" />
                <el-option label="报错" value="ERROR" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="批次大小" prop="batchSize">
              <el-input-number v-model="mappingRuleForm.batchSize" :min="1" :max="10000" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否增量" prop="isIncremental">
              <el-radio-group v-model="mappingRuleForm.isIncremental">
                <el-radio label="Y">是</el-radio>
                <el-radio label="N">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="增量字段" v-if="mappingRuleForm.isIncremental === 'Y'">
              <el-input v-model="mappingRuleForm.incrementalField" placeholder="请输入增量字段名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="排序号" prop="sortNo">
              <el-input-number v-model="mappingRuleForm.sortNo" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否启用" prop="isEnabled">
              <el-radio-group v-model="mappingRuleForm.isEnabled">
                <el-radio label="Y">启用</el-radio>
                <el-radio label="N">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import {
  queryMappingRulePage,
  queryMappingRuleById,
  saveMappingRule,
  deleteMappingRule,
  toggleMappingRuleEnabled,
  copyMappingRule,
  queryDataSourcePage
} from '@/api/financialSharing/dataCollection'

export default {
  name: 'MappingRule',
  components: { Pagination },
  data() {
    return {
      loading: false,
      dataList: [],
      total: 0,
      dataSourceList: [],
      queryForm: {
        ruleCode: '',
        ruleName: '',
        sourceId: '',
        targetTable: '',
        isEnabled: '',
        pageNumber: 1,
        pageSize: 10
      },
      dialogVisible: false,
      dialogTitle: '',
      mappingRuleForm: {
        ruleId: null,
        sourceId: '',
        ruleCode: '',
        ruleName: '',
        targetTable: '',
        sourceQuery: '',
        fieldMappings: '',
        filterCondition: '',
        transformRules: '',
        validationRules: '',
        conflictStrategy: 'SKIP',
        batchSize: 1000,
        isIncremental: 'N',
        incrementalField: '',
        sortNo: 0,
        isEnabled: 'Y'
      },
      mappingRuleRules: {
        ruleCode: [
          { required: true, message: '请输入规则编码', trigger: 'blur' }
        ],
        ruleName: [
          { required: true, message: '请输入规则名称', trigger: 'blur' }
        ],
        sourceId: [
          { required: true, message: '请选择数据源', trigger: 'change' }
        ],
        targetTable: [
          { required: true, message: '请输入目标表', trigger: 'blur' }
        ],
        fieldMappings: [
          { required: true, message: '请输入字段映射', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.getList()
    this.getDataSourceList()
  },
  methods: {
    // 获取列表数据
    getList() {
      this.loading = true
      queryMappingRulePage(this.queryForm).then(response => {
        if (response.code === 1) {
          this.dataList = response.data.records || []
          this.total = response.data.total || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 获取数据源列表
    getDataSourceList() {
      queryDataSourcePage({ pageNumber: 1, pageSize: 1000, isEnabled: 'Y' }).then(response => {
        if (response.code === 1) {
          this.dataSourceList = response.data.records || []
        }
      })
    },
    // 获取数据源名称
    getDataSourceName(sourceId) {
      const source = this.dataSourceList.find(item => item.sourceId === sourceId)
      return source ? source.sourceName : sourceId
    },
    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },
    // 重置
    handleReset() {
      this.queryForm = {
        ruleCode: '',
        ruleName: '',
        sourceId: '',
        targetTable: '',
        isEnabled: '',
        pageNumber: 1,
        pageSize: 10
      }
      this.getList()
    },
    // 新增
    handleAdd() {
      this.dialogTitle = '新增映射规则'
      this.dialogVisible = true
      this.mappingRuleForm = {
        ruleId: null,
        sourceId: '',
        ruleCode: '',
        ruleName: '',
        targetTable: '',
        sourceQuery: '',
        fieldMappings: '',
        filterCondition: '',
        transformRules: '',
        validationRules: '',
        conflictStrategy: 'SKIP',
        batchSize: 1000,
        isIncremental: 'N',
        incrementalField: '',
        sortNo: 0,
        isEnabled: 'Y'
      }
      this.$nextTick(() => {
        if (this.$refs.mappingRuleForm) {
          this.$refs.mappingRuleForm.clearValidate()
        }
      })
    },
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑映射规则'
      queryMappingRuleById({ ruleId: row.ruleId }).then(response => {
        if (response.code === 1) {
          this.mappingRuleForm = { ...response.data }
          this.dialogVisible = true
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      })
    },
    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除该映射规则吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMappingRule({ ruleId: row.ruleId }).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        })
      }).catch(() => {})
    },
    // 启用/禁用
    handleToggleEnabled(row) {
      const action = row.isEnabled === 'Y' ? '启用' : '禁用'
      toggleMappingRuleEnabled({
        ruleId: row.ruleId,
        isEnabled: row.isEnabled
      }).then(response => {
        if (response.code === 1) {
          this.$message.success(action + '成功')
          this.getList()
        } else {
          this.$message.error(response.msg || action + '失败')
          // 恢复原状态
          row.isEnabled = row.isEnabled === 'Y' ? 'N' : 'Y'
        }
      }).catch(() => {
        // 恢复原状态
        row.isEnabled = row.isEnabled === 'Y' ? 'N' : 'Y'
      })
    },
    // 复制
    handleCopy(row) {
      this.$confirm('确定要复制该映射规则吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        copyMappingRule({ ruleId: row.ruleId }).then(response => {
          if (response.code === 1) {
            this.$message.success('复制成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '复制失败')
          }
        })
      }).catch(() => {})
    },
    // 提交表单
    submitForm() {
      this.$refs.mappingRuleForm.validate(valid => {
        if (valid) {
          saveMappingRule(this.mappingRuleForm).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg || '保存成功')
              this.dialogVisible = false
              this.getList()
            } else {
              this.$message.error(response.msg || '保存失败')
            }
          })
        }
      })
    },
    // 关闭对话框
    closeDialog() {
      this.$refs.mappingRuleForm.resetFields()
    },
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return ''
      const date = new Date(dateTime)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.query-form {
  background: #f5f5f5;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.el-table {
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: right;
}

.el-divider {
  margin: 20px 0;
}
</style>

