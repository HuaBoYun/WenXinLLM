<template>
  <div class="budget-scope-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <el-page-header @back="handleBack" :content="`预算管控规则适用范围 - ${ruleName}`" />
    </div>

    <!-- 规则信息卡片 -->
    <el-card class="rule-info-card" shadow="never">
      <div slot="header">
        <span>规则信息</span>
      </div>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="规则编码">{{ ruleInfo.ruleCode }}</el-descriptions-item>
        <el-descriptions-item label="规则名称">{{ ruleInfo.ruleName }}</el-descriptions-item>
        <el-descriptions-item label="控制类型">{{ ruleInfo.controlTypeName }}</el-descriptions-item>
        <el-descriptions-item label="预警阈值">{{ ruleInfo.warningThreshold }}%</el-descriptions-item>
        <el-descriptions-item label="控制阈值">{{ ruleInfo.controlThreshold }}%</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="ruleInfo.isEnabled ? 'success' : 'danger'">
            {{ ruleInfo.isEnabled ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 适用范围配置 -->
    <el-card class="scope-config-card" shadow="never" v-loading="loading">
      <div slot="header">
        <span>适用范围配置</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="handleAddScope">
          <i class="el-icon-plus"></i> 添加范围
        </el-button>
      </div>

      <el-table :data="scopeList" border stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="scopeTypeName" label="范围类型" width="150" />
        <el-table-column prop="scopeValue" label="范围值" min-width="300">
          <template slot-scope="scope">
            <el-tag
              v-for="(value, index) in scope.row.scopeValueList"
              :key="index"
              style="margin-right: 5px; margin-bottom: 5px"
              closable
              @close="handleRemoveScopeValue(scope.$index, index)"
            >
              {{ value }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEditScope(scope.row, scope.$index)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDeleteScope(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="action-buttons">
        <el-button @click="handleBack">返回</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </div>
    </el-card>

    <!-- 添加/编辑范围对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form :model="scopeForm" :rules="scopeRules" ref="scopeFormRef" label-width="100px">
        <el-form-item label="范围类型" prop="scopeType">
          <el-select v-model="scopeForm.scopeType" placeholder="请选择范围类型" style="width: 100%">
            <el-option label="部门" value="DEPARTMENT" />
            <el-option label="项目" value="PROJECT" />
            <el-option label="费用类型" value="EXPENSE_TYPE" />
          </el-select>
        </el-form-item>
        <el-form-item label="范围值" prop="scopeValue">
          <el-input
            v-model="scopeForm.scopeValue"
            type="textarea"
            :rows="4"
            placeholder="请输入范围值，多个值用逗号分隔，如：DEPT001,DEPT002"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirmScope">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetControlRuleApi } from '@/api/financialSharing/baseConfig'

export default {
  name: 'BudgetControlRuleScope',
  data() {
    return {
      ruleId: null,
      ruleName: '',
      loading: false,
      saveLoading: false,
      ruleInfo: {},
      scopeList: [],
      dialogVisible: false,
      dialogTitle: '添加适用范围',
      currentEditIndex: -1,
      scopeForm: {
        scopeType: '',
        scopeValue: ''
      },
      scopeRules: {
        scopeType: [
          { required: true, message: '请选择范围类型', trigger: 'change' }
        ],
        scopeValue: [
          { required: true, message: '请输入范围值', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.ruleId = this.$route.query.ruleId
    if (!this.ruleId) {
      this.$message.error('缺少规则ID参数')
      this.handleBack()
      return
    }
    this.loadRuleInfo()
    this.loadScopeData()
  },
  methods: {
    // 加载规则信息
    async loadRuleInfo() {
      try {
        const response = await budgetControlRuleApi.getDetail(this.ruleId)
        if (response.code === 1) {
          this.ruleInfo = {
            ...response.data,
            controlTypeName: this.getControlTypeText(response.data.controlType)
          }
          this.ruleName = this.ruleInfo.ruleName || ''
        } else {
          this.$message.error(response.msg || '加载规则信息失败')
        }
      } catch (error) {
        this.$message.error('加载规则信息失败：' + error.message)
      }
    },
    // 加载适用范围数据
    async loadScopeData() {
      this.loading = true
      try {
        const response = await budgetControlRuleApi.getScope(this.ruleId)
        if (response.code === 1) {
          // 处理返回的数据，将scopeValue字符串转换为数组
          this.scopeList = (response.data || []).map(item => ({
            ...item,
            scopeTypeName: this.getScopeTypeText(item.scopeType),
            scopeValueList: item.scopeValue ? item.scopeValue.split(',') : []
          }))
        } else {
          this.$message.error(response.msg || '加载适用范围失败')
        }
      } catch (error) {
        this.$message.error('加载适用范围失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    // 获取控制类型文本
    getControlTypeText(type) {
      const typeMap = {
        'DEPARTMENT_MONTHLY': '部门月度控制',
        'DEPARTMENT_YEARLY': '部门年度控制',
        'PROJECT': '项目控制',
        'EXPENSE_TYPE': '费用类型控制'
      }
      return typeMap[type] || type
    },
    // 获取范围类型文本
    getScopeTypeText(type) {
      const typeMap = {
        'DEPARTMENT': '部门',
        'PROJECT': '项目',
        'EXPENSE_TYPE': '费用类型'
      }
      return typeMap[type] || type
    },
    // 添加范围
    handleAddScope() {
      this.dialogTitle = '添加适用范围'
      this.currentEditIndex = -1
      this.scopeForm = {
        scopeType: '',
        scopeValue: ''
      }
      this.dialogVisible = true
    },
    // 编辑范围
    handleEditScope(row, index) {
      this.dialogTitle = '编辑适用范围'
      this.currentEditIndex = index
      this.scopeForm = {
        scopeType: row.scopeType,
        scopeValue: row.scopeValue
      }
      this.dialogVisible = true
    },
    // 删除范围
    handleDeleteScope(index) {
      this.$confirm('确定要删除该适用范围吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.scopeList.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {})
    },
    // 移除范围值
    handleRemoveScopeValue(scopeIndex, valueIndex) {
      this.scopeList[scopeIndex].scopeValueList.splice(valueIndex, 1)
      // 更新scopeValue字符串
      this.scopeList[scopeIndex].scopeValue = this.scopeList[scopeIndex].scopeValueList.join(',')
    },
    // 确认添加/编辑范围
    handleConfirmScope() {
      this.$refs.scopeFormRef.validate(valid => {
        if (valid) {
          const scopeData = {
            scopeType: this.scopeForm.scopeType,
            scopeValue: this.scopeForm.scopeValue,
            scopeTypeName: this.getScopeTypeText(this.scopeForm.scopeType),
            scopeValueList: this.scopeForm.scopeValue.split(',').map(v => v.trim()).filter(v => v)
          }

          if (this.currentEditIndex >= 0) {
            // 编辑模式
            this.$set(this.scopeList, this.currentEditIndex, scopeData)
            this.$message.success('修改成功')
          } else {
            // 添加模式
            this.scopeList.push(scopeData)
            this.$message.success('添加成功')
          }

          this.dialogVisible = false
        }
      })
    },
    // 保存适用范围
    async handleSave() {
      if (this.scopeList.length === 0) {
        this.$message.warning('请至少添加一个适用范围')
        return
      }

      this.saveLoading = true
      try {
        // 转换数据格式，只保留必要字段
        const scopes = this.scopeList.map(item => ({
          scopeType: item.scopeType,
          scopeValue: item.scopeValue
        }))

        const response = await budgetControlRuleApi.saveScope(this.ruleId, scopes)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.handleBack()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.saveLoading = false
      }
    },
    // 返回
    handleBack() {
      this.$router.back()
    },
    // 关闭对话框
    handleDialogClose() {
      this.$refs.scopeFormRef.resetFields()
    }
  }
}
</script>

<style scoped>
.budget-scope-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.rule-info-card {
  margin-bottom: 20px;
}

.scope-config-card {
  min-height: 400px;
}

.action-buttons {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>

