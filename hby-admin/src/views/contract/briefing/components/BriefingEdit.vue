<template>
  <el-dialog
    :title="title"
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
      :disabled="isDetail"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="交底标题" prop="briefingTitle">
            <el-input v-model="form.briefingTitle" placeholder="请输入交底标题" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="交底类型" prop="briefingType">
            <el-select v-model="form.briefingType" placeholder="请选择交底类型" style="width: 100%">
              <el-option label="技术交底" :value="1" />
              <el-option label="安全交底" :value="2" />
              <el-option label="质量交底" :value="3" />
              <el-option label="环保交底" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目ID" prop="projectId">
            <el-input v-model="form.projectId" placeholder="请输入项目ID" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="交底人" prop="briefingPersonId">
            <el-select v-model="form.briefingPersonId" placeholder="请选择交底人" style="width: 100%">
              <el-option label="张三" :value="1" />
              <el-option label="李四" :value="2" />
              <el-option label="王五" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="接收人" prop="receiverId">
            <el-select v-model="form.receiverId" placeholder="请选择接收人" style="width: 100%">
              <el-option label="赵六" :value="4" />
              <el-option label="钱七" :value="5" />
              <el-option label="孙八" :value="6" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="交底日期" prop="briefingDate">
            <el-date-picker
              v-model="form.briefingDate"
              type="date"
              placeholder="选择交底日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="交底地点" prop="briefingLocation">
            <el-input v-model="form.briefingLocation" placeholder="请输入交底地点" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="交底状态" prop="briefingStatus" v-if="isDetail">
            <el-select v-model="form.briefingStatus" disabled style="width: 100%">
              <el-option label="草稿" :value="1" />
              <el-option label="待确认" :value="2" />
              <el-option label="已确认" :value="3" />
              <el-option label="已完成" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="交底内容" prop="briefingContent">
        <el-input
          v-model="form.briefingContent"
          type="textarea"
          :rows="4"
          placeholder="请输入交底内容"
        />
      </el-form-item>

      <el-form-item label="技术要求" prop="technicalRequirements">
        <el-input
          v-model="form.technicalRequirements"
          type="textarea"
          :rows="3"
          placeholder="请输入技术要求"
        />
      </el-form-item>

      <el-form-item label="安全要求" prop="safetyRequirements">
        <el-input
          v-model="form.safetyRequirements"
          type="textarea"
          :rows="3"
          placeholder="请输入安全要求"
        />
      </el-form-item>

      <el-form-item label="质量标准" prop="qualityStandards">
        <el-input
          v-model="form.qualityStandards"
          type="textarea"
          :rows="2"
          placeholder="请输入质量标准"
        />
      </el-form-item>

      <el-form-item label="环保要求" prop="environmentalRequirements">
        <el-input
          v-model="form.environmentalRequirements"
          type="textarea"
          :rows="2"
          placeholder="请输入环保要求"
        />
      </el-form-item>

      <el-form-item label="注意事项" prop="precautions">
        <el-input
          v-model="form.precautions"
          type="textarea"
          :rows="2"
          placeholder="请输入注意事项"
        />
      </el-form-item>

      <el-form-item label="应急措施" prop="emergencyMeasures">
        <el-input
          v-model="form.emergencyMeasures"
          type="textarea"
          :rows="2"
          placeholder="请输入应急措施"
        />
      </el-form-item>

      <el-form-item label="验收标准" prop="acceptanceStandards">
        <el-input
          v-model="form.acceptanceStandards"
          type="textarea"
          :rows="2"
          placeholder="请输入验收标准"
        />
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="form.remarks"
          type="textarea"
          :rows="2"
          placeholder="请输入备注"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSave" v-if="!isDetail">保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    createProjectBriefing,
    updateProjectBriefing
  } from '@/api/contract/briefing'

  export default {
    name: 'BriefingEdit',
    data() {
      return {
        dialogVisible: false,
        isDetail: false,
        title: '',
        form: {
          id: null,
          projectId: null,
          briefingTitle: '',
          briefingType: null,
          briefingPersonId: null,
          receiverId: null,
          briefingDate: '',
          briefingLocation: '',
          briefingContent: '',
          technicalRequirements: '',
          safetyRequirements: '',
          qualityStandards: '',
          environmentalRequirements: '',
          precautions: '',
          emergencyMeasures: '',
          acceptanceStandards: '',
          remarks: '',
          briefingStatus: 1
        },
        rules: {
          briefingTitle: [
            { required: true, message: '请输入交底标题', trigger: 'blur' }
          ],
          briefingType: [
            { required: true, message: '请选择交底类型', trigger: 'change' }
          ],
          projectId: [
            { required: true, message: '请输入项目ID', trigger: 'blur' }
          ],
          briefingPersonId: [
            { required: true, message: '请选择交底人', trigger: 'change' }
          ],
          receiverId: [
            { required: true, message: '请选择接收人', trigger: 'change' }
          ],
          briefingDate: [
            { required: true, message: '请选择交底日期', trigger: 'change' }
          ],
          briefingContent: [
            { required: true, message: '请输入交底内容', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      showEdit(type, data) {
        this.dialogVisible = true
        this.isDetail = type === 'detail'
        
        if (type === 'add') {
          this.title = '新建项目交底'
          this.resetForm()
        } else if (type === 'edit') {
          this.title = '编辑项目交底'
          this.form = { ...data }
        } else if (type === 'detail') {
          this.title = '项目交底详情'
          this.form = { ...data }
        }
      },
      
      resetForm() {
        this.form = {
          id: null,
          projectId: null,
          briefingTitle: '',
          briefingType: null,
          briefingPersonId: null,
          receiverId: null,
          briefingDate: '',
          briefingLocation: '',
          briefingContent: '',
          technicalRequirements: '',
          safetyRequirements: '',
          qualityStandards: '',
          environmentalRequirements: '',
          precautions: '',
          emergencyMeasures: '',
          acceptanceStandards: '',
          remarks: '',
          briefingStatus: 1
        }
        this.$nextTick(() => {
          this.$refs.form && this.$refs.form.clearValidate()
        })
      },
      
      handleClose() {
        this.dialogVisible = false
        this.resetForm()
      },
      
      async handleSave() {
        try {
          await this.$refs.form.validate()

          let response
          if (this.form.id) {
            response = await updateProjectBriefing(this.form.id, this.form)
          } else {
            response = await createProjectBriefing(this.form)
          }

          console.log('项目交底保存接口返回数据:', response)

          // 修复：使用示例云标准状态码 1 表示成功
          if (response.code === 1) {
            this.$message.success(this.form.id ? '更新成功' : '创建成功')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          console.error('项目交底保存失败:', error)
          if (error.message) {
            this.$message.error('操作失败：' + error.message)
          }
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
