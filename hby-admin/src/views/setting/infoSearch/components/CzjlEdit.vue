<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    :close-on-click-modal="false"
    @close="close"
  >
    <el-form ref="form" label-width="80px" :model="formData" :rules="rules">
      <el-form-item label="当前余额">
        <span>{{ money }}</span>
      </el-form-item>
      <el-form-item label="充值金额" prop="field101">
        <el-input
          v-model="formData.field101"
          clearable
          placeholder=""
          :style="{ width: '100%' }"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  // import { saveCharge } from '@/api/setting/infoSearch'

  export default {
    name: 'ThemeEdit',
    data: function () {
      return {
        money: '',
        formData: {
          field101: undefined,
        },
        rules: {
          field101: [
            {
              required: true,
              message: '请输入充值金额',
              trigger: 'blur',
            },
          ],
        },
        title: '充值',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      showEdit(money) {
        this.money = money
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            // const { code, msg } = await saveCharge(this.formData)
            // if (code == 1) {
            //   this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            //   this.$emit('fetch-data')
            //   this.close()
            // }
          }
        })
      },
    },
  }
</script>
