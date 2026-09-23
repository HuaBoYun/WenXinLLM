<!--
 * @Author: 康某 dev@example.com
 * @Date: 2022-09-21 23:13:26
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-09-21 23:17:29
 * @FilePath: \hb-admin\src\views\msg\components\options\zfDialog.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <el-dialog
    :title="'转审'"
    @close="closeCurrent"
    :visible.sync="vizibile"
    :append-to-body="true"
    width="30%"
    :close-on-click-modal="false"
  >
    <el-form ref="formRef" :model="form" label-width="100px" :rules="rules">
      <el-form-item label="转审给谁" prop="transferStaffName">
        <el-input
          disabled
          placeholder="请选择转审给谁"
          v-model="form.transferStaffName"
          :style="
            $store.state.work.processMobile
              ? 'width: 61%; margin-right: 8px'
              : 'width: 79%; margin-right: 8px'
          "
        ></el-input>
        <el-button type="primary" @click="handleSelectUser">请选择</el-button>
      </el-form-item>
      <el-form-item label="转审原因" prop="handleOpinion">
        <el-input
          type="textarea"
          v-model="form.handleOpinion"
          placeholder="请输入转审原因"
        ></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button type="primary" @click="save" :loading="loading">确定</el-button>
      <el-button @click="closeCurrent">取消</el-button>
    </div>
    <ExecutorOption ref="executor" @projectManage="handleExecutorSelected" />
  </el-dialog>
</template>

<script>
  import { ymWorkTransfer } from '@/api/contract/manage'
  // import ExecutorOption from '@/views/contract/contractManage/components/options/executor.vue'
  import ExecutorOption from '@/components/danxuanPersonSPMJ.vue'
  export default {
    components: {
      ExecutorOption,
    },
    data() {
      return {
        form: {},
        id: '',
        vizibile: false,
        loading: false,
        rules: {
          transferStaffName: [
            { required: true, message: '请选择转审人', trigger: 'blur' },
          ],
          handleOpinion: [
            { required: true, message: '请输入审批意见', trigger: 'blur' },
          ],
        },
      }
    },
    methods: {
      close() {
        this.closeCurrent()
        this.$bus.$emit('updateMsg', 0)
        this.$emit('close')
      },
      closeCurrent() {
        this.form = {}
        this.vizibile = false
      },

      show(e, flowTaskInfo) {
        this.id = e
        this.flowTaskInfo = flowTaskInfo
        this.vizibile = true
      },
      async save() {
        this.$refs['formRef'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            try {
              let obj = {
                flowTaskInfoOperatorId: this.id,
                ...this.form,
                id: this.flowTaskInfo.id,
                flowId: this.flowTaskInfo.flowId,
              }
              delete obj.transferStaffName
              const { code } = await ymWorkTransfer(obj)
              if (code == 1) {
                this.$message.success('转审成功')
                localStorage.removeItem('SPsecrectLevelId')
                this.close()
              }
            } finally {
              this.loading = false
            }
          } else {
            return false
          }
        })
      },
      handleSelectUser() {
        const mjId = JSON.parse(localStorage.getItem('SPsecrectLevelId'))
        this.$refs['executor'].showEdit(mjId)
      },
      handleExecutorSelected(e) {
        console.log('🚀 ~ e:', e)
        this.$set(this.form, 'transferStaffId', e[0].staffid)
        this.$set(this.form, 'transferStaffName', e[0].realname)
        this.$nextTick(() => {
          this.$refs['formRef'].clearValidate()
        })
      },
    },
  }
</script>

<style lang="scss" scoped></style>
