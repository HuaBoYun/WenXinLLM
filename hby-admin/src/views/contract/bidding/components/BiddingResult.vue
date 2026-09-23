<template>
  <el-dialog
    title="投标结果录入"
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

      <el-form-item label="投标结果" prop="bidResult">
        <el-radio-group v-model="form.bidResult">
          <el-radio :label="1">中标</el-radio>
          <el-radio :label="2">未中标</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="中标金额" prop="winningAmount" v-if="form.bidResult === 1">
        <el-input-number
          v-model="form.winningAmount"
          :min="0"
          :precision="2"
          placeholder="请输入中标金额"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="排名" prop="ranking">
        <el-input-number
          v-model="form.ranking"
          :min="1"
          placeholder="请输入排名"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="结果公布日期" prop="resultAnnouncementDate">
        <el-date-picker
          v-model="form.resultAnnouncementDate"
          type="date"
          placeholder="选择结果公布日期"
          style="width: 100%"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>

      <el-form-item label="合同签订日期" prop="contractSigningDate" v-if="form.bidResult === 1">
        <el-date-picker
          v-model="form.contractSigningDate"
          type="date"
          placeholder="选择合同签订日期"
          style="width: 100%"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>

      <el-form-item label="经验总结" prop="lessonsLearned">
        <el-input
          v-model="form.lessonsLearned"
          type="textarea"
          :rows="4"
          placeholder="请输入经验总结"
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
  import { updateBidResult } from '@/api/contract/bidding'

  export default {
    name: 'BiddingResult',
    data() {
      return {
        dialogVisible: false,
        projectInfo: {},
        form: {
          id: null,
          bidResult: null,
          winningAmount: null,
          ranking: null,
          resultAnnouncementDate: '',
          contractSigningDate: '',
          lessonsLearned: ''
        },
        rules: {
          bidResult: [
            { required: true, message: '请选择投标结果', trigger: 'change' }
          ],
          winningAmount: [
            { required: true, message: '请输入中标金额', trigger: 'blur' }
          ],
          ranking: [
            { required: true, message: '请输入排名', trigger: 'blur' }
          ],
          resultAnnouncementDate: [
            { required: true, message: '请选择结果公布日期', trigger: 'change' }
          ]
        }
      }
    },
    methods: {
      showEdit(data) {
        this.dialogVisible = true
        this.projectInfo = { ...data }
        this.form.id = data.id
        this.form.bidResult = data.bidResult || null
        this.form.winningAmount = data.winningAmount || null
        this.form.ranking = data.ranking || null
        this.form.resultAnnouncementDate = data.resultAnnouncementDate || ''
        this.form.contractSigningDate = data.contractSigningDate || ''
        this.form.lessonsLearned = data.lessonsLearned || ''
      },
      
      handleClose() {
        this.dialogVisible = false
        this.form = {
          id: null,
          bidResult: null,
          winningAmount: null,
          ranking: null,
          resultAnnouncementDate: '',
          contractSigningDate: '',
          lessonsLearned: ''
        }
        this.projectInfo = {}
        this.$nextTick(() => {
          this.$refs.form && this.$refs.form.clearValidate()
        })
      },
      
      async handleSave() {
        try {
          await this.$refs.form.validate()
          
          const response = await updateBidResult(this.form.id, this.form)
          
          if (response.code === 1) {
            this.$message.success('投标结果保存成功')
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
