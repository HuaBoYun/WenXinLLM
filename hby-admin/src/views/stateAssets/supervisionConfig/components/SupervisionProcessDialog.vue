<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="processForm"
      :model="processForm"
      :rules="processRules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="流程名称" prop="processName">
            <el-input
              v-model="processForm.processName"
              placeholder="请输入流程名称"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="流程编码" prop="processCode">
            <el-input
              v-model="processForm.processCode"
              placeholder="请输入流程编码"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="流程类型" prop="processType">
            <el-select
              v-model="processForm.processType"
              placeholder="请选择流程类型"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="审批流程" value="APPROVAL"></el-option>
              <el-option label="监管流程" value="SUPERVISION"></el-option>
              <el-option label="预警流程" value="WARNING"></el-option>
              <el-option label="处置流程" value="DISPOSAL"></el-option>
              <el-option label="报告流程" value="REPORTING"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="流程状态" prop="status">
            <el-select
              v-model="processForm.status"
              placeholder="请选择状态"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="启用" value="ACTIVE"></el-option>
              <el-option label="停用" value="INACTIVE"></el-option>
              <el-option label="草稿" value="DRAFT"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="优先级" prop="priority">
            <el-select
              v-model="processForm.priority"
              placeholder="请选择优先级"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="高" value="HIGH"></el-option>
              <el-option label="中" value="MEDIUM"></el-option>
              <el-option label="低" value="LOW"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="creator">
            <el-input
              v-model="processForm.creator"
              placeholder="请输入创建人"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="流程描述" prop="description">
        <el-input
          v-model="processForm.description"
          type="textarea"
          :rows="3"
          placeholder="请输入流程描述"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="触发条件" prop="triggerCondition">
        <el-input
          v-model="processForm.triggerCondition"
          type="textarea"
          :rows="3"
          placeholder="请输入触发条件"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="流程步骤" prop="processSteps">
        <el-table :data="processSteps" border style="width: 100%">
          <el-table-column prop="stepOrder" label="步骤序号" width="100" align="center">
            <template slot-scope="scope">
              <el-input-number
                v-if="dialogType !== 'view'"
                v-model="scope.row.stepOrder"
                :min="1"
                size="mini"
                style="width: 100%"
              />
              <span v-else>{{ scope.row.stepOrder }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="stepName" label="步骤名称" width="150">
            <template slot-scope="scope">
              <el-input
                v-if="dialogType !== 'view'"
                v-model="scope.row.stepName"
                placeholder="请输入步骤名称"
                size="mini"
              />
              <span v-else>{{ scope.row.stepName }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="stepType" label="步骤类型" width="120">
            <template slot-scope="scope">
              <el-select
                v-if="dialogType !== 'view'"
                v-model="scope.row.stepType"
                placeholder="选择类型"
                size="mini"
                style="width: 100%"
              >
                <el-option label="审批" value="APPROVAL"></el-option>
                <el-option label="通知" value="NOTIFICATION"></el-option>
                <el-option label="执行" value="EXECUTION"></el-option>
                <el-option label="条件" value="CONDITION"></el-option>
              </el-select>
              <span v-else>{{ getStepTypeText(scope.row.stepType) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="assignee" label="处理人" width="120">
            <template slot-scope="scope">
              <el-input
                v-if="dialogType !== 'view'"
                v-model="scope.row.assignee"
                placeholder="处理人"
                size="mini"
              />
              <span v-else>{{ scope.row.assignee }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="timeLimit" label="时限(小时)" width="100" align="center">
            <template slot-scope="scope">
              <el-input-number
                v-if="dialogType !== 'view'"
                v-model="scope.row.timeLimit"
                :min="1"
                size="mini"
                style="width: 100%"
              />
              <span v-else>{{ scope.row.timeLimit }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="步骤说明" min-width="150">
            <template slot-scope="scope">
              <el-input
                v-if="dialogType !== 'view'"
                v-model="scope.row.description"
                placeholder="步骤说明"
                size="mini"
              />
              <span v-else>{{ scope.row.description }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" align="center" v-if="dialogType !== 'view'">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" @click="removeStep(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button
          v-if="dialogType !== 'view'"
          type="primary"
          size="mini"
          @click="addStep"
          style="margin-top: 10px"
        >
          添加步骤
        </el-button>
      </el-form-item>

      <el-form-item label="超时处理" prop="timeoutAction">
        <el-select
          v-model="processForm.timeoutAction"
          placeholder="请选择超时处理方式"
          style="width: 100%"
          :disabled="dialogType === 'view'"
        >
          <el-option label="自动通过" value="AUTO_APPROVE"></el-option>
          <el-option label="自动拒绝" value="AUTO_REJECT"></el-option>
          <el-option label="转交上级" value="ESCALATE"></el-option>
          <el-option label="发送提醒" value="REMINDER"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="适用范围" prop="applicableScope">
        <el-checkbox-group v-model="processForm.applicableScope" :disabled="dialogType === 'view'">
          <el-checkbox label="CENTRAL_ENTERPRISE">央企</el-checkbox>
          <el-checkbox label="STATE_ENTERPRISE">国企</el-checkbox>
          <el-checkbox label="LISTED_COMPANY">上市公司</el-checkbox>
          <el-checkbox label="FINANCIAL_INSTITUTION">金融机构</el-checkbox>
          <el-checkbox label="OTHER">其他</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="processForm.remarks"
          type="textarea"
          :rows="2"
          placeholder="请输入备注信息"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        v-if="dialogType !== 'view'"
        @click="handlePreview"
      >
        预览流程
      </el-button>
      <el-button
        v-if="dialogType !== 'view'"
        type="primary"
        @click="handleSubmit"
        :loading="loading"
      >
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveSupervisionProcess } from '@/api/stateAssets/supervisionConfig'

export default {
  name: 'SupervisionProcessDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    processData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add' // add, edit, view
    }
  },
  data() {
    return {
      loading: false,
      processForm: {
        id: '',
        processName: '',
        processCode: '',
        processType: '',
        status: 'ACTIVE',
        priority: 'MEDIUM',
        creator: '',
        description: '',
        triggerCondition: '',
        timeoutAction: '',
        applicableScope: [],
        remarks: ''
      },
      processSteps: [
        {
          stepOrder: 1,
          stepName: '提交申请',
          stepType: 'EXECUTION',
          assignee: '申请人',
          timeLimit: 24,
          description: '提交监管申请'
        },
        {
          stepOrder: 2,
          stepName: '初审',
          stepType: 'APPROVAL',
          assignee: '初审员',
          timeLimit: 48,
          description: '进行初步审核'
        },
        {
          stepOrder: 3,
          stepName: '复审',
          stepType: 'APPROVAL',
          assignee: '复审员',
          timeLimit: 72,
          description: '进行复审确认'
        }
      ],
      processRules: {
        processName: [
          { required: true, message: '请输入流程名称', trigger: 'blur' }
        ],
        processCode: [
          { required: true, message: '请输入流程编码', trigger: 'blur' }
        ],
        processType: [
          { required: true, message: '请选择流程类型', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择流程状态', trigger: 'change' }
        ],
        priority: [
          { required: true, message: '请选择优先级', trigger: 'change' }
        ],
        creator: [
          { required: true, message: '请输入创建人', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入流程描述', trigger: 'blur' }
        ],
        triggerCondition: [
          { required: true, message: '请输入触发条件', trigger: 'blur' }
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
        add: '新建监管流程',
        edit: '编辑监管流程',
        view: '查看监管流程'
      }
      return titleMap[this.dialogType] || '新建监管流程'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      if (this.dialogType === 'add') {
        this.processForm = {
          id: '',
          processName: '',
          processCode: '',
          processType: '',
          status: 'ACTIVE',
          priority: 'MEDIUM',
          creator: '',
          description: '',
          triggerCondition: '',
          timeoutAction: '',
          applicableScope: [],
          remarks: ''
        }
        this.processSteps = [
          {
            stepOrder: 1,
            stepName: '提交申请',
            stepType: 'EXECUTION',
            assignee: '申请人',
            timeLimit: 24,
            description: '提交监管申请'
          },
          {
            stepOrder: 2,
            stepName: '初审',
            stepType: 'APPROVAL',
            assignee: '初审员',
            timeLimit: 48,
            description: '进行初步审核'
          },
          {
            stepOrder: 3,
            stepName: '复审',
            stepType: 'APPROVAL',
            assignee: '复审员',
            timeLimit: 72,
            description: '进行复审确认'
          }
        ]
      } else {
        this.processForm = { ...this.processData }
        this.processSteps = this.processData.steps || this.processSteps
        // 确保数组字段正确初始化
        this.processForm.applicableScope = this.processData.applicableScope || []
      }
      
      this.$nextTick(() => {
        if (this.$refs.processForm) {
          this.$refs.processForm.clearValidate()
        }
      })
    },

    addStep() {
      const newOrder = this.processSteps.length + 1
      this.processSteps.push({
        stepOrder: newOrder,
        stepName: '',
        stepType: 'APPROVAL',
        assignee: '',
        timeLimit: 24,
        description: ''
      })
    },

    removeStep(index) {
      this.processSteps.splice(index, 1)
      // 重新排序
      this.processSteps.forEach((step, idx) => {
        step.stepOrder = idx + 1
      })
    },

    getStepTypeText(type) {
      const typeMap = {
        'APPROVAL': '审批',
        'NOTIFICATION': '通知',
        'EXECUTION': '执行',
        'CONDITION': '条件'
      }
      return typeMap[type] || type
    },

    handlePreview() {
      this.$message.success('流程预览功能开发中...')
    },

    handleSubmit() {
      this.$refs.processForm.validate((valid) => {
        if (valid) {
          // 验证步骤完整性
          if (this.processSteps.length === 0) {
            this.$message.error('请至少添加一个流程步骤')
            return
          }
          
          for (let i = 0; i < this.processSteps.length; i++) {
            const step = this.processSteps[i]
            if (!step.stepName || !step.stepType || !step.assignee) {
              this.$message.error(`第${i + 1}个步骤信息不完整`)
              return
            }
          }
          
          this.loading = true
          const formData = { ...this.processForm }
          formData.steps = this.processSteps
          
          saveSupervisionProcess(formData).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg || '操作成功')
              this.handleClose()
              this.$emit('refresh')
            } else {
              this.$message.error(response.msg || '操作失败')
            }
            this.loading = false
          }).catch(error => {
            console.error('保存监管流程失败:', error)
            this.$message.error('操作失败')
            this.loading = false
          })
        }
      })
    },

    handleClose() {
      this.dialogVisible = false
      this.loading = false
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
