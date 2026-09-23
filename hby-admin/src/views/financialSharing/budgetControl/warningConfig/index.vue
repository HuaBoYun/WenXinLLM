<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="配置编码">
        <el-input v-model="queryForm.configCode" placeholder="请输入配置编码" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="配置名称">
        <el-input v-model="queryForm.configName" placeholder="请输入配置名称" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="预警类型">
        <el-select v-model="queryForm.warningType" placeholder="请选择预警类型" clearable style="width: 150px">
          <el-option label="预算超支预警" value="BUDGET_OVERRUN" />
          <el-option label="预算占用预警" value="BUDGET_OCCUPANCY" />
          <el-option label="预算余额预警" value="BUDGET_BALANCE" />
          <el-option label="控制阻止预警" value="CONTROL_BLOCK" />
        </el-select>
      </el-form-item>
      <el-form-item label="预警级别">
        <el-select v-model="queryForm.warningLevel" placeholder="请选择预警级别" clearable style="width: 120px">
          <el-option label="低" value="LOW" />
          <el-option label="中" value="MEDIUM" />
          <el-option label="高" value="HIGH" />
          <el-option label="紧急" value="URGENT" />
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
    <el-table v-loading="loading" :data="dataList" border>
      <el-table-column label="配置编码" prop="configCode" width="150" />
      <el-table-column label="配置名称" prop="configName" width="180" show-overflow-tooltip />
      <el-table-column label="预警类型" prop="warningType" width="140">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.warningType === 'BUDGET_OVERRUN'" type="danger">预算超支预警</el-tag>
          <el-tag v-else-if="scope.row.warningType === 'BUDGET_OCCUPANCY'" type="warning">预算占用预警</el-tag>
          <el-tag v-else-if="scope.row.warningType === 'BUDGET_BALANCE'" type="info">预算余额预警</el-tag>
          <el-tag v-else-if="scope.row.warningType === 'CONTROL_BLOCK'" type="danger">控制阻止预警</el-tag>
          <el-tag v-else>{{ scope.row.warningType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="阈值类型" prop="thresholdType" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.thresholdType === 'AMOUNT'">金额</span>
          <span v-else-if="scope.row.thresholdType === 'PERCENTAGE'">百分比</span>
          <span v-else>{{ scope.row.thresholdType }}</span>
        </template>
      </el-table-column>
      <el-table-column label="阈值" prop="thresholdValue" width="100" align="right" />
      <el-table-column label="预警级别" prop="warningLevel" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.warningLevel === 'LOW'" type="info" size="small">低</el-tag>
          <el-tag v-else-if="scope.row.warningLevel === 'MEDIUM'" type="warning" size="small">中</el-tag>
          <el-tag v-else-if="scope.row.warningLevel === 'HIGH'" type="danger" size="small">高</el-tag>
          <el-tag v-else-if="scope.row.warningLevel === 'URGENT'" type="danger" size="small">紧急</el-tag>
          <el-tag v-else size="small">{{ scope.row.warningLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="接收人" prop="receivers" width="150" show-overflow-tooltip />
      <el-table-column label="发送方式" prop="sendMethods" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.sendMethods">
            {{ formatSendMethods(scope.row.sendMethods) }}
          </span>
        </template>
      </el-table-column>
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
      <el-table-column label="创建时间" prop="createTime" width="160">
        <template slot-scope="scope">
          {{ formatDateTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="240">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="warning" @click="handleTest(scope.row)">测试</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" @close="closeDialog">
      <el-form ref="configForm" :model="configForm" :rules="configRules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="配置编码" prop="configCode">
              <el-input v-model="configForm.configCode" placeholder="请输入配置编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="配置名称" prop="configName">
              <el-input v-model="configForm.configName" placeholder="请输入配置名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预警类型" prop="warningType">
              <el-select v-model="configForm.warningType" placeholder="请选择预警类型" style="width: 100%">
                <el-option label="预算超支预警" value="BUDGET_OVERRUN" />
                <el-option label="预算占用预警" value="BUDGET_OCCUPANCY" />
                <el-option label="预算余额预警" value="BUDGET_BALANCE" />
                <el-option label="控制阻止预警" value="CONTROL_BLOCK" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警级别" prop="warningLevel">
              <el-select v-model="configForm.warningLevel" placeholder="请选择预警级别" style="width: 100%">
                <el-option label="低" value="LOW" />
                <el-option label="中" value="MEDIUM" />
                <el-option label="高" value="HIGH" />
                <el-option label="紧急" value="URGENT" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="组织ID" prop="orgId">
              <el-input v-model="configForm.orgId" placeholder="请输入组织ID（可选）" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="科目编码" prop="subjectCode">
              <el-input v-model="configForm.subjectCode" placeholder="请输入科目编码（可选）" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="阈值类型" prop="thresholdType">
              <el-select v-model="configForm.thresholdType" placeholder="请选择阈值类型" style="width: 100%">
                <el-option label="金额" value="AMOUNT" />
                <el-option label="百分比" value="PERCENTAGE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="阈值" prop="thresholdValue">
              <el-input v-model.number="configForm.thresholdValue" placeholder="请输入阈值" type="number" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="接收人" prop="receivers">
          <el-input v-model="configForm.receivers" placeholder="请输入接收人，多个用逗号分隔" />
        </el-form-item>
        <el-form-item label="发送方式" prop="sendMethods">
          <el-checkbox-group v-model="sendMethodsArray">
            <el-checkbox label="EMAIL">邮件</el-checkbox>
            <el-checkbox label="SMS">短信</el-checkbox>
            <el-checkbox label="INTERNAL">站内信</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="消息模板" prop="messageTemplate">
          <el-input
            v-model="configForm.messageTemplate"
            type="textarea"
            :rows="4"
            placeholder="请输入消息模板，支持变量：{orgId}、{subjectCode}、{period}、{amount}、{threshold}"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="configForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import {
  queryWarningConfigPage,
  queryWarningConfigById,
  saveWarningConfig,
  deleteWarningConfig,
  toggleWarningConfigEnabled,
  testWarningConfig
} from '@/api/financialSharing/budgetControl'

export default {
  name: 'WarningConfig',
  components: { Pagination },
  data() {
    return {
      loading: false,
      dataList: [],
      total: 0,
      queryForm: {
        configCode: '',
        configName: '',
        warningType: '',
        warningLevel: '',
        isEnabled: '',
        pageNumber: 1,
        pageSize: 10
      },
      dialogVisible: false,
      dialogTitle: '',
      configForm: {
        configId: null,
        configCode: '',
        configName: '',
        warningType: '',
        orgId: '',
        subjectCode: '',
        period: '',
        thresholdType: '',
        thresholdValue: null,
        warningLevel: '',
        receivers: '',
        messageTemplate: '',
        sendMethods: '',
        isEnabled: 'Y',
        remark: ''
      },
      sendMethodsArray: [],
      configRules: {
        configCode: [
          { required: true, message: '请输入配置编码', trigger: 'blur' }
        ],
        configName: [
          { required: true, message: '请输入配置名称', trigger: 'blur' }
        ],
        warningType: [
          { required: true, message: '请选择预警类型', trigger: 'change' }
        ],
        warningLevel: [
          { required: true, message: '请选择预警级别', trigger: 'change' }
        ],
        thresholdType: [
          { required: true, message: '请选择阈值类型', trigger: 'change' }
        ],
        thresholdValue: [
          { required: true, message: '请输入阈值', trigger: 'blur' }
        ],
        receivers: [
          { required: true, message: '请输入接收人', trigger: 'blur' }
        ],
        messageTemplate: [
          { required: true, message: '请输入消息模板', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    // 获取列表数据
    getList() {
      this.loading = true
      queryWarningConfigPage(this.queryForm).then(response => {
        if (response.code === 1) {
          this.dataList = response.data.records
          this.total = response.data.total
        } else {
          this.$message.error(response.msg || '查询失败')
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },
    // 重置
    handleReset() {
      this.queryForm = {
        configCode: '',
        configName: '',
        warningType: '',
        warningLevel: '',
        isEnabled: '',
        pageNumber: 1,
        pageSize: 10
      }
      this.getList()
    },
    // 新增
    handleAdd() {
      this.dialogTitle = '新增预警配置'
      this.dialogVisible = true
      this.configForm = {
        configId: null,
        configCode: '',
        configName: '',
        warningType: '',
        orgId: '',
        subjectCode: '',
        period: '',
        thresholdType: '',
        thresholdValue: null,
        warningLevel: '',
        receivers: '',
        messageTemplate: '',
        sendMethods: '',
        isEnabled: 'Y',
        remark: ''
      }
      this.sendMethodsArray = []
      this.$nextTick(() => {
        if (this.$refs.configForm) {
          this.$refs.configForm.clearValidate()
        }
      })
    },
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑预警配置'
      queryWarningConfigById({ configId: row.configId }).then(response => {
        if (response.code === 1) {
          this.configForm = { ...response.data }
          // 解析发送方式
          if (this.configForm.sendMethods) {
            this.sendMethodsArray = this.configForm.sendMethods.split(',')
          } else {
            this.sendMethodsArray = []
          }
          this.dialogVisible = true
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      })
    },
    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除该预警配置吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteWarningConfig({ configId: row.configId }).then(response => {
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
      toggleWarningConfigEnabled({
        configId: row.configId,
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
    // 测试配置
    handleTest(row) {
      this.$confirm('确定要测试该预警配置吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        testWarningConfig({ configId: row.configId }).then(response => {
          if (response.code === 1) {
            this.$alert(response.data, '测试结果', {
              confirmButtonText: '确定',
              type: 'success'
            })
          } else {
            this.$message.error(response.msg || '测试失败')
          }
        })
      }).catch(() => {})
    },
    // 提交表单
    submitForm() {
      this.$refs.configForm.validate(valid => {
        if (valid) {
          // 组装发送方式
          this.configForm.sendMethods = this.sendMethodsArray.join(',')

          saveWarningConfig(this.configForm).then(response => {
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
      this.$refs.configForm.resetFields()
      this.sendMethodsArray = []
    },
    // 格式化发送方式
    formatSendMethods(sendMethods) {
      if (!sendMethods) return ''
      const methods = sendMethods.split(',')
      const labels = []
      methods.forEach(method => {
        if (method === 'EMAIL') labels.push('邮件')
        else if (method === 'SMS') labels.push('短信')
        else if (method === 'INTERNAL') labels.push('站内信')
      })
      return labels.join('、')
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
</style>

