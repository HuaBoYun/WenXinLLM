<!--
 * @Date: 2022-04-21 14:23:38
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-21 15:13:38
 * @FilePath: /hb-admin/src/views/contract/execute/components/CompareEdit.vue
-->
<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form ref="form" label-width="60px" :model="formData" :rules="rules">
        <el-col :span="24">
          <el-form-item label="原文本" prop="planstartdate">
            <tinymce
              v-model="formData.content1"
              class="tinymceClass"
              :height="300"
              :menubar="['']"
              :toolbar="['']"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="新文本" prop="planstartdate">
            <tinymce
              v-model="formData.content2"
              :height="300"
              :menubar="['']"
              placeholder="新文本"
              :toolbar="['']"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="compare">比 对</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getCompareInfo, trackingApproval } from '@/api/contract/fulfil'

  export default {
    name: 'CompareEdit',
    data() {
      return {
        formData: {
          content1: undefined,
          content2: undefined,
        },
        rules: {
          content2: [
            {
              required: true,
              message: '请填写新文本',
              trigger: 'blur',
            },
          ],
        },
        title: '文档比对',
        dialogFormVisible: false,
        options: [],
        node: { attList: [] },
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.fetchInfo(row)
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
      },
      async fetchInfo(row) {
        const { data, code } = await getCompareInfo({
          contractId: row.contractid,
        })
        if (code == 1) {
          this.formData.content1 = data
        }
      },
      //比对
      compare() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg, code } = await trackingApproval({
              ...this.formData,
              tcu: this.row,
            })
            if (code == 1) {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
  /* 用css实现disable */
  .tinymacClass {
    position: relative;
    left: 0;
    top: 0;
    opacity: 0.5;
    width: 100%;
    height: 100%;
    background: #000;
    z-index: 998;
    pointer-events: none;
  }
</style>
