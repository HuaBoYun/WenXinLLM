<template>
  <el-dialog
    :title="form.id ? '编辑招投标项目' : '新建招投标项目'"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      size="small"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目编号" prop="projectNo">
            <el-input
              v-model="form.projectNo"
              placeholder="请输入项目编号"
              :disabled="!!form.id"
            >
              <el-button
                slot="append"
                icon="el-icon-refresh"
                @click="generateProjectNo"
                v-if="!form.id"
              >
                生成
              </el-button>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" prop="projectName">
            <el-input
              v-model="form.projectName"
              placeholder="请输入项目名称"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目类型" prop="projectType">
            <el-select
              v-model="form.projectType"
              placeholder="请选择项目类型"
              style="width: 100%"
            >
              <el-option
                v-for="item in projectTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="招标方式" prop="biddingMethod">
            <el-select
              v-model="form.biddingMethod"
              placeholder="请选择招标方式"
              style="width: 100%"
            >
              <el-option
                v-for="item in biddingMethodOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="业主单位" prop="ownerName">
            <el-input
              v-model="form.ownerName"
              placeholder="请输入业主单位"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人" prop="contactPerson">
            <el-input
              v-model="form.contactPerson"
              placeholder="请输入联系人"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactPhone">
            <el-input
              v-model="form.contactPhone"
              placeholder="请输入联系电话"
              maxlength="20"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同金额" prop="contractAmount">
            <el-input-number
              v-model="form.contractAmount"
              placeholder="请输入合同金额"
              :precision="2"
              :min="0"
              :max="999999999.99"
              style="width: 100%"
              controls-position="right"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="投标保证金" prop="bidBondAmount">
            <el-input-number
              v-model="form.bidBondAmount"
              placeholder="请输入投标保证金"
              :precision="2"
              :min="0"
              :max="999999999.99"
              style="width: 100%"
              controls-position="right"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="履约保证金比例" prop="performanceBondRate">
            <el-input-number
              v-model="form.performanceBondRate"
              placeholder="请输入履约保证金比例"
              :precision="2"
              :min="0"
              :max="100"
              style="width: 100%"
              controls-position="right"
            >
              <template slot="append">%</template>
            </el-input-number>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="报名开始时间" prop="registrationStartTime">
            <el-date-picker
              v-model="form.registrationStartTime"
              type="datetime"
              placeholder="请选择报名开始时间"
              style="width: 100%"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报名截止时间" prop="registrationEndTime">
            <el-date-picker
              v-model="form.registrationEndTime"
              type="datetime"
              placeholder="请选择报名截止时间"
              style="width: 100%"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="投标截止时间" prop="bidSubmissionDeadline">
            <el-date-picker
              v-model="form.bidSubmissionDeadline"
              type="datetime"
              placeholder="请选择投标截止时间"
              style="width: 100%"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开标时间" prop="bidOpeningTime">
            <el-date-picker
              v-model="form.bidOpeningTime"
              type="datetime"
              placeholder="请选择开标时间"
              style="width: 100%"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目状态" prop="projectStatus">
            <el-select
              v-model="form.projectStatus"
              placeholder="请选择项目状态"
              style="width: 100%"
            >
              <el-option
                v-for="item in projectStatusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人" prop="responsiblePerson">
            <el-input
              v-model="form.responsiblePerson"
              placeholder="请输入负责人"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="项目描述" prop="projectDescription">
            <el-input
              v-model="form.projectDescription"
              type="textarea"
              :rows="3"
              placeholder="请输入项目描述"
              maxlength="1000"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="技术要求" prop="technicalRequirements">
            <el-input
              v-model="form.technicalRequirements"
              type="textarea"
              :rows="3"
              placeholder="请输入技术要求"
              maxlength="2000"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="商务要求" prop="commercialRequirements">
            <el-input
              v-model="form.commercialRequirements"
              type="textarea"
              :rows="3"
              placeholder="请输入商务要求"
              maxlength="2000"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="saveLoading" @click="handleSave">
        {{ form.id ? '更新' : '保存' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  createBiddingProject,
  updateBiddingProject,
  generateProjectNo
} from '@/api/contract/bidding'

export default {
  name: 'BiddingProjectForm',
  data() {
    return {
      dialogVisible: false,
      saveLoading: false,
      form: {
        id: null,
        projectNo: '',
        projectName: '',
        projectType: null,
        biddingMethod: null,
        ownerName: '',
        contactPerson: '',
        contactPhone: '',
        contractAmount: null,
        bidBondAmount: null,
        performanceBondRate: null,
        registrationStartTime: '',
        registrationEndTime: '',
        bidSubmissionDeadline: '',
        bidOpeningTime: '',
        projectStatus: 1,
        responsiblePerson: '',
        projectDescription: '',
        technicalRequirements: '',
        commercialRequirements: ''
      },
      rules: {
        projectNo: [
          { required: true, message: '请输入项目编号', trigger: 'blur' },
          { min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' }
        ],
        projectName: [
          { required: true, message: '请输入项目名称', trigger: 'blur' },
          { min: 2, max: 200, message: '长度在 2 到 200 个字符', trigger: 'blur' }
        ],
        projectType: [
          { required: true, message: '请选择项目类型', trigger: 'change' }
        ],
        biddingMethod: [
          { required: true, message: '请选择招标方式', trigger: 'change' }
        ],
        ownerName: [
          { required: true, message: '请输入业主单位', trigger: 'blur' }
        ],
        contactPhone: [
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        contractAmount: [
          { required: true, message: '请输入合同金额', trigger: 'blur' }
        ],
        registrationStartTime: [
          { required: true, message: '请选择报名开始时间', trigger: 'change' }
        ],
        registrationEndTime: [
          { required: true, message: '请选择报名截止时间', trigger: 'change' }
        ],
        bidSubmissionDeadline: [
          { required: true, message: '请选择投标截止时间', trigger: 'change' }
        ],
        bidOpeningTime: [
          { required: true, message: '请选择开标时间', trigger: 'change' }
        ]
      },
      projectTypeOptions: [
        { label: '建筑工程', value: 1 },
        { label: '市政工程', value: 2 },
        { label: '水利工程', value: 3 },
        { label: '交通工程', value: 4 },
        { label: '电力工程', value: 5 },
        { label: '通信工程', value: 6 }
      ],
      biddingMethodOptions: [
        { label: '公开招标', value: 1 },
        { label: '邀请招标', value: 2 },
        { label: '竞争性谈判', value: 3 },
        { label: '单一来源', value: 4 },
        { label: '询价采购', value: 5 }
      ],
      projectStatusOptions: [
        { label: '信息收集', value: 1 },
        { label: '投标决策', value: 2 },
        { label: '投标准备', value: 3 },
        { label: '投标提交', value: 4 },
        { label: '开标评标', value: 5 },
        { label: '结果公示', value: 6 },
        { label: '合同签订', value: 7 }
      ]
    }
  },
  methods: {
    showEdit(row) {
      this.dialogVisible = true
      if (row) {
        this.form = { ...row }
      } else {
        this.resetForm()
      }
    },
    
    resetForm() {
      this.form = {
        id: null,
        projectNo: '',
        projectName: '',
        projectType: null,
        biddingMethod: null,
        ownerName: '',
        contactPerson: '',
        contactPhone: '',
        contractAmount: null,
        bidBondAmount: null,
        performanceBondRate: null,
        registrationStartTime: '',
        registrationEndTime: '',
        bidSubmissionDeadline: '',
        bidOpeningTime: '',
        projectStatus: 1,
        responsiblePerson: '',
        projectDescription: '',
        technicalRequirements: '',
        commercialRequirements: ''
      }
    },

    async generateProjectNo() {
      try {
        const response = await generateProjectNo()
        if (response.code === 200) {
          this.form.projectNo = response.data
        } else {
          this.$message.error(response.message || '生成项目编号失败')
        }
      } catch (error) {
        this.$message.error('生成项目编号失败：' + error.message)
      }
    },

    async handleSave() {
      try {
        await this.$refs.form.validate()
        
        // 时间校验
        if (new Date(this.form.registrationEndTime) <= new Date(this.form.registrationStartTime)) {
          this.$message.error('报名截止时间必须晚于报名开始时间')
          return
        }
        
        if (new Date(this.form.bidSubmissionDeadline) <= new Date(this.form.registrationEndTime)) {
          this.$message.error('投标截止时间必须晚于报名截止时间')
          return
        }
        
        if (new Date(this.form.bidOpeningTime) <= new Date(this.form.bidSubmissionDeadline)) {
          this.$message.error('开标时间必须晚于投标截止时间')
          return
        }

        this.saveLoading = true
        
        let response
        if (this.form.id) {
          response = await updateBiddingProject(this.form.id, this.form)
        } else {
          response = await createBiddingProject(this.form)
        }
        
        if (response.code === 200) {
          this.$message.success(this.form.id ? '更新成功' : '创建成功')
          this.handleClose()
          this.$emit('fetch-data')
        } else {
          this.$message.error(response.message || '操作失败')
        }
      } catch (error) {
        if (error.message) {
          this.$message.error('操作失败：' + error.message)
        }
      } finally {
        this.saveLoading = false
      }
    },

    handleClose() {
      this.dialogVisible = false
      this.saveLoading = false
      this.$refs.form.resetFields()
      this.resetForm()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
