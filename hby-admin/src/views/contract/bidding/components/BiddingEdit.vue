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
          <el-form-item label="项目名称" prop="projectName">
            <el-input v-model="form.projectName" placeholder="请输入项目名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目类型" prop="projectType">
            <el-select v-model="form.projectType" placeholder="请选择项目类型" style="width: 100%">
              <el-option label="建筑工程" :value="1" />
              <el-option label="市政工程" :value="2" />
              <el-option label="水利工程" :value="3" />
              <el-option label="交通工程" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="招标方式" prop="biddingMethod">
            <el-select v-model="form.biddingMethod" placeholder="请选择招标方式" style="width: 100%">
              <el-option label="公开招标" :value="1" />
              <el-option label="邀请招标" :value="2" />
              <el-option label="竞争性谈判" :value="3" />
              <el-option label="单一来源" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="业主单位" prop="ownerName">
            <el-input v-model="form.ownerName" placeholder="请输入业主单位" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目地点" prop="projectLocation">
            <el-input v-model="form.projectLocation" placeholder="请输入项目地点" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同金额" prop="contractAmount">
            <el-input-number
              v-model="form.contractAmount"
              :min="0"
              :precision="2"
              placeholder="请输入合同金额"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="招标公告日期" prop="biddingAnnouncementDate">
            <el-date-picker
              v-model="form.biddingAnnouncementDate"
              type="date"
              placeholder="选择招标公告日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开标时间" prop="bidOpeningDate">
            <el-date-picker
              v-model="form.bidOpeningDate"
              type="datetime"
              placeholder="选择开标时间"
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
              placeholder="选择投标截止时间"
              style="width: 100%"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="投标保证金" prop="bidBondAmount">
            <el-input-number
              v-model="form.bidBondAmount"
              :min="0"
              :precision="2"
              placeholder="请输入投标保证金"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="项目规模" prop="projectScale">
        <el-input
          v-model="form.projectScale"
          type="textarea"
          :rows="2"
          placeholder="请输入项目规模描述"
        />
      </el-form-item>

      <el-form-item label="资质要求" prop="qualificationRequirements">
        <el-input
          v-model="form.qualificationRequirements"
          type="textarea"
          :rows="2"
          placeholder="请输入资质要求"
        />
      </el-form-item>

      <el-form-item label="技术要求" prop="technicalRequirements">
        <el-input
          v-model="form.technicalRequirements"
          type="textarea"
          :rows="2"
          placeholder="请输入技术要求"
        />
      </el-form-item>

      <el-form-item label="商务要求" prop="commercialRequirements">
        <el-input
          v-model="form.commercialRequirements"
          type="textarea"
          :rows="2"
          placeholder="请输入商务要求"
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
    createBiddingProject,
    updateBiddingProject
  } from '@/api/contract/bidding'

  export default {
    name: 'BiddingEdit',
    data() {
      return {
        dialogVisible: false,
        isDetail: false,
        title: '',
        form: {
          id: null,
          projectName: '',
          projectType: null,
          biddingMethod: null,
          ownerName: '',
          projectLocation: '',
          contractAmount: null,
          biddingAnnouncementDate: '',
          bidOpeningDate: '',
          bidSubmissionDeadline: '',
          bidBondAmount: null,
          projectScale: '',
          qualificationRequirements: '',
          technicalRequirements: '',
          commercialRequirements: '',
          projectStatus: 1
        },
        rules: {
          projectName: [
            { required: true, message: '请输入项目名称', trigger: 'blur' }
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
          contractAmount: [
            { required: true, message: '请输入合同金额', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      showEdit(type, data) {
        this.dialogVisible = true
        this.isDetail = type === 'detail'
        
        if (type === 'add') {
          this.title = '新建招投标项目'
          this.resetForm()
        } else if (type === 'edit') {
          this.title = '编辑招投标项目'
          this.form = { ...data }
        } else if (type === 'detail') {
          this.title = '招投标项目详情'
          this.form = { ...data }
        }
      },
      
      resetForm() {
        this.form = {
          id: null,
          projectName: '',
          projectType: null,
          biddingMethod: null,
          ownerName: '',
          projectLocation: '',
          contractAmount: null,
          biddingAnnouncementDate: '',
          bidOpeningDate: '',
          bidSubmissionDeadline: '',
          bidBondAmount: null,
          projectScale: '',
          qualificationRequirements: '',
          technicalRequirements: '',
          commercialRequirements: '',
          projectStatus: 1
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
            response = await updateBiddingProject(this.form)
          } else {
            response = await createBiddingProject(this.form)
          }
          
          if (response.code === 1) {
            this.$message.success(this.form.id ? '更新成功' : '创建成功')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
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
