<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="type === 'edit' ? '编辑' : '添加' + '审计模板'"
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
            label-width="180px"
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
              <el-form-item
                label="模板名称"
                prop="templateName"
                label-width="100px"
              >
                <el-input v-model="postForm.templateName" class="filter-item" />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="关联审计类型（多选）" prop="typeIds">
                <el-select
                  v-model="postForm.typeIds"
                  class="filter-item"
                  style="width: 100%"
                  clearable
                  multiple
                >
                  <el-option
                    v-for="item in typeOpts"
                    :key="item.id"
                    :label="item.typeName"
                    :value="String(item.id)"
                  />
                </el-select>
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
  import { getDetail, addOrUpdate } from '@/oapi/baseConfig/gcsjmb'
  export default {
    name: 'pfxglAdd',
    props: ['typeOpts'],
    data() {
      const createUser = JSON.parse(localStorage.getItem('userInfo')).realname
      return {
        type: 'edit',
        loading: false,
        dialogFormVisible: false,
        postForm: {
          createUser,
          templateName: '',
          typeIds: [],
        },
        id: '',
        rules: {
          templateName: [
            { required: true, message: '请填写模板名称', trigger: 'blur' },
          ],
          typeIds: [
            {
              required: true,
              message: '请选择关联审计类型',
              trigger: 'change',
            },
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
          this.postForm.templateName = row.templateName
          this.postForm.typeIds = row.typeIds.split(',')
          this.id = row.id
        }
      },
      close() {
        this.$refs.postForm.resetFields()
        this.dialogFormVisible = false
        this.postForm.templateName = ''
        this.postForm.typeIds = []
        this.id = ''
      },
      handleSubmit() {
        this.$refs.postForm.validate((valid) => {
          if (valid) {
            this.loading = true
            let param = Object.assign({}, this.postForm, {
              typeIds: this.postForm.typeIds.join(','),
            })
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
