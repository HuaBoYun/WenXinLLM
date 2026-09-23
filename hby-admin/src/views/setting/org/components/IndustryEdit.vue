<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      :class="{ disabled: disabled }"
      :disabled="disabled"
      label-width="140px"
      :model="form"
      :rules="rules"
    >
      <el-form-item label="机构编号" prop="orgnumber">
        <el-input v-model.trim="form.orgnumber" />
      </el-form-item>
      <el-form-item label="机构名称" prop="orgname">
        <el-input v-model.trim="form.orgname" />
      </el-form-item>
      <el-form-item label="公司介绍">
        <el-input v-model.trim="form.orgmeno" type="textarea" />
      </el-form-item>
      <el-form-item label="公司备注">
        <el-input v-model.trim="form.memo" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button v-if="!disabled" type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { orgHysave } from '@/api/setting/org'

  export default {
    name: 'IndustryEdit',
    data() {
      return {
        disabled: false,
        form: {
          orgnumber: '',
          orgname: '',
          orgmeno: '',
          memo: '',
          fatherorgid: '',
        },
        rules: {
          orgnumber: [
            { required: true, trigger: 'blur', message: '请输入编号' },
          ],
          orgname: [{ required: true, trigger: 'blur', message: '请输入名称' }],
        },
        title: '',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      showEdit(pid, row, disabled) {
        this.disabled = disabled
        if (!row) {
          this.title = '添加'
        } else {
          this.title = disabled ? '查看' : '编辑'
          this.form = Object.assign({}, row)
        }
        this.form.fatherorgid = pid
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
            const { msg } = await orgHysave(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
