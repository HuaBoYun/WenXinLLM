<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="type === 'edit' ? '编辑' : '添加' + '方法类型'"
      :visible.sync="dialogFormVisible"
      width="800px"
      @close="close"
    >
      <el-row :gutter="24" v-loading="loading">
        <el-card>
          <el-form
            ref="postForm"
            :rules="rules"
            :model="postForm"
            label-width="100px"
          >
            <el-col :span="12">
              <el-form-item label="创建人" prop="createUser">
                <el-input
                  v-model="postForm.createUser"
                  clearable
                  placeholder="请输入创建人"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="方法名称" prop="methodName">
                <el-input v-model="postForm.methodName" class="filter-item" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="方法内容" prop="methodContent">
                <el-input
                  v-model="postForm.methodContent"
                  class="filter-item"
                  type="textarea"
                />
              </el-form-item>
            </el-col>
          </el-form>
        </el-card>
      </el-row>
      <template #footer>
        <el-button @click="close">关 闭</el-button>
        <el-button :loading="loading" type="primary" @click="handleSubmit">
          保 存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import { getDetail, addOrUpdate } from '@/oapi/baseConfig/sjffwh'
  export default {
    name: 'pfxglAdd',
    data() {
      const createUser = JSON.parse(localStorage.getItem('userInfo')).realname
      return {
        type: 'edit',
        loading: false,
        dialogFormVisible: false,
        postForm: {
          createUser,
          methodName: '',
          methodContent: '',
        },
        id: '',
        rules: {
          methodName: [
            { required: true, message: '请填写方法名称', trigger: 'blur' },
          ],
          methodContent: [
            { required: true, message: '请填写方法内容', trigger: 'blur' },
          ],
        },
      }
    },
    methods: {
      async showModal(row) {
        this.dialogFormVisible = true
        this.type = row ? 'edit' : 'add'
        if (row) {
          // const res = await getDetail({ id: row.id })
          // this.postForm = res.data
          this.postForm.methodName = row.methodName
          this.postForm.methodContent = row.methodContent
          this.id = row.id
        }
      },
      close() {
        this.$refs.postForm.resetFields()
        this.dialogFormVisible = false
        this.postForm.methodName = ''
        this.postForm.methodContent = ''
        this.id = ''
      },
      handleSubmit() {
        this.$refs['postForm'].validate((valid) => {
          if (valid) {
            this.loading = true
            let param = Object.assign({}, this.postForm)
            if (this.type === 'edit') {
              param.id = this.id
            }
            addOrUpdate(param)
              .then(() => {
                this.$baseMessage('保存成功', 'success')
                this.$emit('queryData')
                this.close()
              })
              .catch((res) => {
                this.$baseMessage(res.msg, 'error')
              })
              .finally(() => {
                this.loading = false
              })
          }
        })
      },
    },
  }
</script>
