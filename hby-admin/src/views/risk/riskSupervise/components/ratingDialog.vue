<template>
  <el-dialog
    title="改分"
    :visible.sync="dialogVisible"
    width="400px"
    :modal-append-to-body="false"
    :append-to-body="true"
    :close-on-click-modal="false"
    @close="close"
  >
    <el-form :model="formData" ref="form" :rules="rules">
      <el-form-item label="分数" prop="score" label-width="100px">
        <el-input v-model="formData.score" style="width: 200px"></el-input>
      </el-form-item>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </span>
  </el-dialog>
</template>
<script>
  import { updateScore } from '@/api/risk/riskfill'

  export default {
    data() {
      return {
        dialogVisible: false,
        formData: {
          score: '',
        },
        rules: {
          score: [
            {
              pattern: /^-?\d*\.?\d+$/,
              message: '只能输入数字、小数和负数',
              trigger: 'blur',
            },
          ],
        },
        minScore: 0,
      }
    },
    mounted() {},
    methods: {
      show(val) {
        this.dialogVisible = true
        this.minScore = val.scoreDetails
        this.formData.id = val.id
      },
      close() {
        this.formData = {}
        this.dialogVisible = false
      },
      submit() {
        this.$refs.form.validate((valid) => {
          if (valid) {
            updateScore(this.formData).then(() => {
              this.$message.success('操作成功')
              this.close()
              this.$emit('fetch-data')
            })
          }
        })
      },
    },
  }
</script>
<style lang="scss" scoped></style>
