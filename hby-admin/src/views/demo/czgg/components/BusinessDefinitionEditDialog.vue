<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="dataForm"
      :rules="rules"
      :model="temp"
      label-position="left"
      label-width="120px"
      style="width: 700px; margin-left:50px;"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="业务品种编码" prop="businessCode">
            <el-input v-model="temp.businessCode" placeholder="请输入业务品种编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="业务品种名称" prop="businessName">
            <el-input v-model="temp.businessName" placeholder="请输入业务品种名称" />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="业务品种类型" prop="businessTypeId">
            <el-select
              v-model="temp.businessTypeId"
              placeholder="请选择业务品种类型"
              style="width: 100%"
              @change="handleBusinessTypeChange"
            >
              <el-option
                v-for="item in businessTypeList"
                :key="item.typeId"
                :label="item.typeName"
                :value="item.typeId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="业务方向" prop="businessDirection">
            <el-select
              v-model="temp.businessDirection"
              placeholder="请选择业务方向"
              style="width: 100%"
            >
              <el-option label="收入" value="IN" />
              <el-option label="支出" value="OUT" />
              <el-option label="双向" value="BOTH" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="币种代码" prop="currencyCode">
            <el-select
              v-model="temp.currencyCode"
              placeholder="请选择币种"
              style="width: 100%"
              @change="handleCurrencyChange"
            >
              <el-option label="人民币" value="CNY" />
              <el-option label="美元" value="USD" />
              <el-option label="欧元" value="EUR" />
              <el-option label="日元" value="JPY" />
              <el-option label="英镑" value="GBP" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险等级" prop="riskLevel">
            <el-select
              v-model="temp.riskLevel"
              placeholder="请选择风险等级"
              style="width: 100%"
            >
              <el-option label="低风险" value="LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="高风险" value="HIGH" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="审批流程" prop="approvalFlowId">
            <el-select
              v-model="temp.approvalFlowId"
              placeholder="请选择审批流程"
              style="width: 100%"
              @change="handleApprovalFlowChange"
            >
              <el-option
                v-for="item in approvalFlowList"
                :key="item.flowId"
                :label="item.flowName"
                :value="item.flowId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="isEnabled">
            <el-radio-group v-model="temp.isEnabled">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="业务规则" prop="businessRules">
        <el-input
          v-model="temp.businessRules"
          type="textarea"
          :rows="4"
          placeholder="请输入业务规则"
        />
      </el-form-item>
      
      <el-form-item label="描述" prop="description">
        <el-input
          v-model="temp.description"
          type="textarea"
          :rows="3"
          placeholder="请输入描述"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">
        取消
      </el-button>
      <el-button type="primary" @click="handleSave">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveOrUpdateBusinessDefinition, getEnabledBusinessTypes } from '@/api/globalTreasurer/czgg'

export default {
  name: 'BusinessDefinitionEditDialog',
  data() {
    return {
      dialogVisible: false,
      dialogTitle: '',
      temp: {
        businessDefinitionId: null,
        businessCode: '',
        businessName: '',
        businessTypeId: null,
        businessTypeName: '',
        businessDirection: '',
        currencyCode: '',
        currencyName: '',
        riskLevel: '',
        approvalFlowId: null,
        approvalFlowName: '',
        businessRules: '',
        description: '',
        isEnabled: 1,
        orgId: null
      },
      businessTypeList: [],
      approvalFlowList: [],
      currencyMap: {
        'CNY': '人民币',
        'USD': '美元',
        'EUR': '欧元',
        'JPY': '日元',
        'GBP': '英镑'
      },
      rules: {
        businessCode: [
          { required: true, message: '请输入业务品种编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        businessName: [
          { required: true, message: '请输入业务品种名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        businessTypeId: [
          { required: true, message: '请选择业务品种类型', trigger: 'change' }
        ],
        businessDirection: [
          { required: true, message: '请选择业务方向', trigger: 'change' }
        ],
        currencyCode: [
          { required: true, message: '请选择币种', trigger: 'change' }
        ],
        riskLevel: [
          { required: true, message: '请选择风险等级', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    show(row) {
      this.dialogVisible = true
      this.resetTemp()
      
      if (row) {
        this.dialogTitle = '编辑业务品种定义'
        this.temp = Object.assign({}, row)
      } else {
        this.dialogTitle = '新增业务品种定义'
        this.temp.orgId = this.$store.getters.orgId
      }
      
      this.getBusinessTypeList()
      this.getApprovalFlowList()
      
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    
    resetTemp() {
      this.temp = {
        businessDefinitionId: null,
        businessCode: '',
        businessName: '',
        businessTypeId: null,
        businessTypeName: '',
        businessDirection: '',
        currencyCode: '',
        currencyName: '',
        riskLevel: '',
        approvalFlowId: null,
        approvalFlowName: '',
        businessRules: '',
        description: '',
        isEnabled: 1,
        orgId: null
      }
    },
    
    getBusinessTypeList() {
      getEnabledBusinessTypes(this.$store.getters.orgId).then(response => {
        if (response.success) {
          this.businessTypeList = response.data
        }
      })
    },
    
    getApprovalFlowList() {
      // TODO: 获取审批流程列表
      this.approvalFlowList = [
        { flowId: 1, flowName: '标准审批流程' },
        { flowId: 2, flowName: '简化审批流程' },
        { flowId: 3, flowName: '高级审批流程' }
      ]
    },
    
    handleBusinessTypeChange(value) {
      const businessType = this.businessTypeList.find(item => item.typeId === value)
      if (businessType) {
        this.temp.businessTypeName = businessType.typeName
      }
    },
    
    handleCurrencyChange(value) {
      this.temp.currencyName = this.currencyMap[value] || value
    },
    
    handleApprovalFlowChange(value) {
      const approvalFlow = this.approvalFlowList.find(item => item.flowId === value)
      if (approvalFlow) {
        this.temp.approvalFlowName = approvalFlow.flowName
      }
    },
    
    handleSave() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 设置创建/更新用户
          if (this.temp.businessDefinitionId) {
            this.temp.updateUser = this.$store.getters.userId
          } else {
            this.temp.createUser = this.$store.getters.userId
            this.temp.updateUser = this.$store.getters.userId
          }
          
          saveOrUpdateBusinessDefinition(this.temp).then(response => {
            if (response.success) {
              this.$message.success(this.temp.businessDefinitionId ? '更新成功' : '创建成功')
              this.dialogVisible = false
              this.$emit('refresh')
            } else {
              this.$message.error(response.message || '保存失败')
            }
          })
        }
      })
    },
    
    handleClose() {
      this.resetTemp()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
