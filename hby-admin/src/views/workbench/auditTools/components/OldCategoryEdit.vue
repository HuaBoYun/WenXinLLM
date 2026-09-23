<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form ref="form" label-width="120px" :model="form" :rules="rules">
      <el-form-item label="审计类型">
        <el-input v-model.trim="form.auditType" />
      </el-form-item>
      <el-form-item label="审计类型状态">
        <el-select
          v-model="form.status"
          placeholder="请选择"
          :style="{ width: '100%' }"
        >
          <el-option
            v-for="item in options"
            :label="item.label"
            :value="item.value"
            :key="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="审计类型编号">
        <el-input
          v-model.trim="form.auditCode"
          oninput="value=value.replace(/[^\d]/g,'')"
          maxLength="2"
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
  import { getSjtypeSave } from '@/api/workbench/auditTools'

  export default {
    name: 'LcdyEdit',
    data() {
      return {
        form: {},
        rules: {
          code: [{ required: true, trigger: 'blur', message: '请输入编号' }],
          name: [{ required: true, trigger: 'blur', message: '请输入名称' }],
        },
        title: '',
        dialogFormVisible: false,
        options: [
          {
            value: 2,
            label: '正常',
          },
          {
            value: 1,
            label: '禁用',
          },
        ],
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        if (!row) {
          this.form = {}
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form = Object.assign({}, row)
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const res = await getSjtypeSave(this.form)
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
