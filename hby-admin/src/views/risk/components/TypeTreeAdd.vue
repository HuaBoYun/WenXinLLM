<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
  >
    <el-form ref="form" label-width="80px" :model="form" :rules="rules">
      <el-form-item label="风险编号" prop="sn">
        <el-input v-model.trim="form.title" />
      </el-form-item>
      <el-form-item label="风险名称" prop="title">
        <el-input v-model.trim="form.author" />
      </el-form-item>
      <el-form-item label="父风险" prop="parent">
        <el-input v-model.trim="form.author" disabled />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button
        v-if="title === '编辑'"
        style="float: left"
        type="danger"
        @click="save"
      >
        删 除
      </el-button>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { doEdit } from '@/api/table'

  export default {
    name: 'TypeTreeAdd',
    data() {
      return {
        form: {
          sn: '',
          title: '',
          parent: '',
        },
        rules: {
          sn: [{ required: true, trigger: 'blur', message: '请输入风险编号' }],
          title: [
            { required: true, trigger: 'blur', message: '请输入风险名称' },
          ],
        },
        title: '',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        if (!row) {
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
            const { msg } = await doEdit(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
