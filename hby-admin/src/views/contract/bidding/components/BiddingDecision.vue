<template>
  <el-dialog
    title="投标决策"
    :visible.sync="dialogVisible"
    width="60%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item label="项目名称">
        <el-input v-model="projectInfo.projectName" disabled />
      </el-form-item>
      
      <el-form-item label="业主单位">
        <el-input v-model="projectInfo.ownerName" disabled />
      </el-form-item>
      
      <el-form-item label="合同金额">
        <el-input v-model="projectInfo.contractAmount" disabled />
      </el-form-item>

      <el-form-item label="投标决策" prop="bidDecision">
        <el-radio-group v-model="form.bidDecision">
          <el-radio :label="1">参与投标</el-radio>
          <el-radio :label="2">放弃投标</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="决策理由" prop="decisionReason">
        <el-input
          v-model="form.decisionReason"
          type="textarea"
          :rows="4"
          placeholder="请输入决策理由"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSave">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { updateBidDecision } from '@/api/contract/bidding'

  export default {
    name: 'BiddingDecision',
    data() {
      return {
        dialogVisible: false,
        projectInfo: {},
        form: {
          id: null,
          bidDecision: null,
          decisionReason: ''
        },
        rules: {
          bidDecision: [
            { required: true, message: '请选择投标决策', trigger: 'change' }
          ],
          decisionReason: [
            { required: true, message: '请输入决策理由', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      showEdit(data) {
        this.dialogVisible = true
        this.projectInfo = { ...data }
        this.form.id = data.id
        this.form.bidDecision = data.bidDecision || null
        this.form.decisionReason = data.decisionReason || ''
      },
      
      handleClose() {
        this.dialogVisible = false
        this.form = {
          id: null,
          bidDecision: null,
          decisionReason: ''
        }
        this.projectInfo = {}
        this.$nextTick(() => {
          this.$refs.form && this.$refs.form.clearValidate()
        })
      },
      
      async handleSave() {
        try {
          await this.$refs.form.validate()
          
          const response = await updateBidDecision(this.form)
          
          if (response.code === 1) {
            this.$message.success('投标决策保存成功')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.msg || '保存失败')
          }
        } catch (error) {
          if (error.message) {
            this.$message.error('保存失败：' + error.message)
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
