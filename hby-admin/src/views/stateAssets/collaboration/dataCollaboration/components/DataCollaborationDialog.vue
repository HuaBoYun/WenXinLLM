<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose">
    
    <el-form
      ref="collaborationForm"
      :model="collaborationForm"
      :rules="formRules"
      label-width="120px"
      :disabled="dialogType === 'view'">
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseName">
            <el-input v-model="collaborationForm.enterpriseName" placeholder="请输入企业名称"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="协同类型" prop="collaborationType">
            <el-select v-model="collaborationForm.collaborationType" placeholder="请选择协同类型">
              <el-option label="数据报送" value="DATA_SUBMISSION"></el-option>
              <el-option label="数据同步" value="DATA_SYNC"></el-option>
              <el-option label="质量检查" value="QUALITY_CHECK"></el-option>
              <el-option label="标准统一" value="STANDARD_UNIFY"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据源" prop="dataSource">
            <el-select v-model="collaborationForm.dataSource" placeholder="请选择数据源">
              <el-option label="财务系统" value="FINANCIAL_SYSTEM"></el-option>
              <el-option label="业务系统" value="BUSINESS_SYSTEM"></el-option>
              <el-option label="人力资源系统" value="HR_SYSTEM"></el-option>
              <el-option label="资产管理系统" value="ASSET_SYSTEM"></el-option>
              <el-option label="外部数据源" value="EXTERNAL_SOURCE"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="目标系统" prop="targetSystem">
            <el-select v-model="collaborationForm.targetSystem" placeholder="请选择目标系统">
              <el-option label="监管平台" value="SUPERVISION_PLATFORM"></el-option>
              <el-option label="数据仓库" value="DATA_WAREHOUSE"></el-option>
              <el-option label="分析系统" value="ANALYSIS_SYSTEM"></el-option>
              <el-option label="报告系统" value="REPORT_SYSTEM"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="同步频率" prop="syncFrequency">
            <el-select v-model="collaborationForm.syncFrequency" placeholder="请选择同步频率">
              <el-option label="实时同步" value="REALTIME"></el-option>
              <el-option label="每小时" value="HOURLY"></el-option>
              <el-option label="每日" value="DAILY"></el-option>
              <el-option label="每周" value="WEEKLY"></el-option>
              <el-option label="每月" value="MONTHLY"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据格式" prop="dataFormat">
            <el-select v-model="collaborationForm.dataFormat" placeholder="请选择数据格式">
              <el-option label="JSON" value="JSON"></el-option>
              <el-option label="XML" value="XML"></el-option>
              <el-option label="CSV" value="CSV"></el-option>
              <el-option label="Excel" value="EXCEL"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="协同状态" prop="status">
            <el-select v-model="collaborationForm.status" placeholder="请选择协同状态">
              <el-option label="进行中" value="IN_PROGRESS"></el-option>
              <el-option label="已完成" value="COMPLETED"></el-option>
              <el-option label="已失败" value="FAILED"></el-option>
              <el-option label="已暂停" value="PAUSED"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="优先级" prop="priority">
            <el-select v-model="collaborationForm.priority" placeholder="请选择优先级">
              <el-option label="高" value="HIGH"></el-option>
              <el-option label="中" value="MEDIUM"></el-option>
              <el-option label="低" value="LOW"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="协同描述" prop="description">
        <el-input
          v-model="collaborationForm.description"
          type="textarea"
          :rows="3"
          placeholder="请输入协同描述">
        </el-input>
      </el-form-item>
      
      <el-form-item label="数据范围" prop="dataScope">
        <el-checkbox-group v-model="collaborationForm.dataScope">
          <el-checkbox label="FINANCIAL_DATA">财务数据</el-checkbox>
          <el-checkbox label="BUSINESS_DATA">业务数据</el-checkbox>
          <el-checkbox label="ASSET_DATA">资产数据</el-checkbox>
          <el-checkbox label="HR_DATA">人力资源数据</el-checkbox>
          <el-checkbox label="RISK_DATA">风险数据</el-checkbox>
          <el-checkbox label="COMPLIANCE_DATA">合规数据</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      
      <el-form-item label="质量要求" prop="qualityRequirements">
        <el-input
          v-model="collaborationForm.qualityRequirements"
          type="textarea"
          :rows="2"
          placeholder="请输入数据质量要求">
        </el-input>
      </el-form-item>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="负责人" prop="responsiblePerson">
            <el-input v-model="collaborationForm.responsiblePerson" placeholder="请输入负责人"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系方式" prop="contactInfo">
            <el-input v-model="collaborationForm.contactInfo" placeholder="请输入联系方式"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="dialogType !== 'view'" type="primary" @click="handleSubmit">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { createDataCollaboration, updateDataCollaboration } from '@/api/stateAssets/dataCollaboration'

export default {
  name: 'DataCollaborationDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    dialogType: {
      type: String,
      default: 'add' // add, edit, view
    },
    collaborationData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      collaborationForm: {
        enterpriseName: '',
        collaborationType: '',
        dataSource: '',
        targetSystem: '',
        syncFrequency: '',
        dataFormat: '',
        status: 'IN_PROGRESS',
        priority: 'MEDIUM',
        description: '',
        dataScope: [],
        qualityRequirements: '',
        responsiblePerson: '',
        contactInfo: ''
      },
      formRules: {
        enterpriseName: [
          { required: true, message: '请输入企业名称', trigger: 'blur' }
        ],
        collaborationType: [
          { required: true, message: '请选择协同类型', trigger: 'change' }
        ],
        dataSource: [
          { required: true, message: '请选择数据源', trigger: 'change' }
        ],
        targetSystem: [
          { required: true, message: '请选择目标系统', trigger: 'change' }
        ],
        syncFrequency: [
          { required: true, message: '请选择同步频率', trigger: 'change' }
        ],
        dataFormat: [
          { required: true, message: '请选择数据格式', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    dialogTitle() {
      const titleMap = {
        add: '新增数据协同',
        edit: '编辑数据协同',
        view: '查看数据协同'
      }
      return titleMap[this.dialogType] || '数据协同'
    }
  },
  watch: {
    collaborationData: {
      handler(newVal) {
        if (newVal && Object.keys(newVal).length > 0) {
          this.collaborationForm = { ...newVal }
        }
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    // 提交
    handleSubmit() {
      this.$refs.collaborationForm.validate(async (valid) => {
        if (valid) {
          try {
            if (this.dialogType === 'add') {
              await createDataCollaboration(this.collaborationForm)
              this.$message.success('新增成功')
            } else if (this.dialogType === 'edit') {
              await updateDataCollaboration(this.collaborationForm)
              this.$message.success('更新成功')
            }
            this.handleClose()
            this.$emit('refresh')
          } catch (error) {
            this.$message.error('操作失败')
            console.error('提交失败:', error)
          }
        }
      })
    },
    
    // 关闭
    handleClose() {
      this.dialogVisible = false
      this.$refs.collaborationForm.resetFields()
      this.collaborationForm = {
        enterpriseName: '',
        collaborationType: '',
        dataSource: '',
        targetSystem: '',
        syncFrequency: '',
        dataFormat: '',
        status: 'IN_PROGRESS',
        priority: 'MEDIUM',
        description: '',
        dataScope: [],
        qualityRequirements: '',
        responsiblePerson: '',
        contactInfo: ''
      }
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
