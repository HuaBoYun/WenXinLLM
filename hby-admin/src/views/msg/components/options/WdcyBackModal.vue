<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="撤回"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <el-row :gutter="14">
        <el-form
          ref="elForm"
          label-width="100px"
          :model="formData"
          :rules="rules"
          size="mini"
        >
          <el-col :span="22">
            <el-form-item
              label="撤回原因"
              label-width="140px"
              prop="handleOpinion"
            >
              <el-input
                v-model="formData.handleOpinion"
                type="textarea"
                :rows="2"
                placeholder="请输入撤回原因"
              />
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <div slot="footer">
        <el-button @click="close">取消</el-button>
        <el-button @click="add" type="primary">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import { handleCYReback } from '@/api/setting/msg'
  export default {
    data() {
      return {
        dialogFormVisible: false,
        rules: {
          handleOpinion: [
            { required: true, message: '请输入撤回原因', trigger: 'change' },
          ],
        },
        formData: {
          handleOpinion: '',
        },
        flowTaskOperatorRecordListId: '', //用于保存撤回原因接口
      }
    },
    methods: {
      show(id) {
        this.flowTaskOperatorRecordListId = id
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
        this.formData = {}
      },
      add() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            handleCYReback({
              flowTaskOperatorRecordListId: this.flowTaskOperatorRecordListId,
              handleOpinion: this.formData.handleOpinion,
            }).then((res) => {
              if (res.code === 1) {
                this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
                this.dialogFormVisible = false
                this.$emit('fetchData')
              }
            })
          }
        })
      },
    },
  }
</script>
