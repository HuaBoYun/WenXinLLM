<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    :close-on-click-modal="false"
    @close="close"
  >
    <el-row :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="24">
          <el-form-item label="主题名称" prop="homeName">
            <el-input v-model.trim="form.homeName" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="主题组件名字" prop="homePath">
            <el-input v-model.trim="form.homePath" />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import {
    saveHomePage,
    getPrivewAttInfo,
    getDefaultHomeInfo,
  } from '@/api/setting/loginPage'

  export default {
    name: 'LoginPageEdit',
    components: {},
    data() {
      return {
        form: {
          homeName: '',
          homePath: '',
          id: '',
        },
        rules: {},
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
          getDefaultHomeInfo({ id: row.id }).then((res) => {
            Object.keys(this.form).forEach((key) => {
              this.form[key] = res.data[key]
            })
          })
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
            const type = this.form.id ? '修改' : '保存'
            const form = {
              ...this.form,
            }
            const res = await saveHomePage(form)
            if (res.code == 200) {
              this.$baseMessage(
                `${type}成功`,
                'success',
                'vab-hey-message-success'
              )
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
    },
  }
</script>

<style></style>
