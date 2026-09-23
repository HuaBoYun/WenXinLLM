<!-- 撤回modal -->
<template>
  <el-dialog
    title="撤回审核"
    :visible.sync="dialogVisible"
    width="450px"
    :append-to-body="true"
    :close-on-click-modal="false"
    v-if="dialogVisible"
  >
    <el-form
      ref="form"
      :inline="true"
      :model="formData"
      v-loading="loading"
      @submit.native.prevent
    >
      <el-row>
        <el-form-item label="撤回原因">
          <el-col :span="24">
            <el-input
              v-model="formData.remark"
              clearable
              type="textarea"
              placeholder="撤回原因"
              style="width: 300px"
            />
          </el-col>
        </el-form-item>
      </el-row>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="submit" :loading="buttonLoading">
        确 定
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { handleCYReback } from '@/api/setting/msg'
  export default {
    name: 'chyy',
    data() {
      return {
        loading: false,
        dialogVisible: false,
        formData: {
          remark: '',
        },
        row: null,
        buttonLoading: false,
      }
    },
    methods: {
      showModal(row) {
        this.dialogVisible = true

        this.row = row
        this.formData.remark = ''
      },
      close() {
        this.dialogVisible = false
        this.formData.remark = ''
      },
      async submit() {
        if (this.row) {
          this.buttonLoading = true
          this.loading = true
          try {
            const res = await handleCYReback({
              flowTaskOperatorRecordListId: this.row.id,
              processId: this.row.processId,
              flowId: this.row.flowId,
              handleOpinion: this.formData.remark,
            })
            if (res && res.code === 1) {
              this.$message({
                type: 'success',
                message: '操作成功！',
              })
              this.close()
              this.$emit('fetchData')
            }
          } catch (error) {
            console.error('撤回操作失败:', error)
          } finally {
            this.loading = false
            this.buttonLoading = false
          }
        }
      },
    },
  }
</script>

<style scoped lang="scss"></style>
