<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    :append-to-body="true"
    width="60%"
    @close="closeCurrent"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-col :span="12">
          <el-form-item label="模板标题" prop="tempTitle">
            <el-input v-model="form.tempTitle" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="模板内容" prop="tempMemo">
            <el-input type="textarea" v-model="form.tempMemo" />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="closeCurrent">取 消</el-button>
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import {
    saveFlowTemplate,
    modifyFlowTemplate,
  } from '@/api/contract/manage.js'
  export default {
    name: 'OpinionEidt',
    props: {},
    data() {
      return {
        visible: false,
        title: '新建',
        form: {
          tempTitle: '',
          tempMemo: '',
          flowId: '',
          taskNodeId: '',
        },
        rules: {
          tempTitle: [
            { required: true, message: '请输入模板标题', trigger: 'blur' },
          ],
          tempMemo: [
            { required: true, message: '请输入模板内容', trigger: 'blur' },
          ],
        },
      }
    },
    methods: {
      async show(row, type, queryForm) {
        this.visible = true
        if (type == 'edit') {
          this.title = '编辑'
          this.form = row
          this.form.flowId = queryForm.flowId
          this.form.taskNodeId = queryForm.taskNodeId
        } else if (type == 'add') {
          this.title = '新建'
          this.form = {
            flowId: queryForm.flowId,
            taskNodeId: queryForm.taskNodeId,
          }
        } else {
          this.title = '查看'
          this.form = row
          this.form.flowId = queryForm.flowId
          this.form.taskNodeId = queryForm.taskNodeId
        }
      },
      submitForm() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            if (!this.form.tempId) {
              const res = await saveFlowTemplate(this.form)
              if (res.code == 1) {
                this.$message.success('保存成功')
                this.$emit('fetchData')
                this.closeCurrent()
              }
            } else {
              const res = await modifyFlowTemplate(this.form)
              if (res.code == 1) {
                this.$message.success('修改成功')
                this.$emit('fetchData')
                this.closeCurrent()
              }
            }
          } else {
            return false
          }
        })
      },
      resetForm() {
        this.$refs['form'].resetFields()
      },
      closeCurrent() {
        this.visible = false
        this.form = {}
      },
    },
  }
</script>

<style></style>
