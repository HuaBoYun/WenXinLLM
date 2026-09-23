<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
  >
    <el-row :gutter="15">
      <el-form
        ref="elForm"
        label-width="125px"
        :model="formData"
        :rules="rules"
        size="medium"
      >
        <el-col :span="12">
          <el-form-item label="问题类型" prop="targetName">
            <el-input
              v-model="formData.targetName"
              clearable
              placeholder="请输入问题类型"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="描述" prop="targetDesc">
            <el-input
              v-model="formData.targetDesc"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入描述"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="rest">重填</el-button>
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </template>
  </el-dialog>
</template>
<script>
  //
  import {
    mergeTblNbsjTarget,
    selectNbsjTargetInfo,
  } from '@/api/workbench/auditTools'
  export default {
    props: {
      templeteId: {
        type: Number,
        default: '',
      },
    },
    name: 'treeEidt',
    components: {},
    data() {
      return {
        dialogFormVisible: false,
        formData: {
          targetName: '',
          targetDesc: '',
          status: 0,
          parentId: '',
        },
        title: '新增',
        rules: {
          targetName: [
            {
              required: true,
              message: '请输入审计目标名称',
              trigger: 'blur',
            },
          ],
        },
      }
    },
    methods: {
      show(targetId, status, title) {
        this.formData.parentId = targetId
        this.formData.status = status
        this.dialogFormVisible = true
        if (title == 'add') {
          this.title = '新增'
        } else {
          this.title = '编辑'
          this.getData(targetId)
        }
      },
      async getData(targetId) {
        const res = await selectNbsjTargetInfo({ targetId })
        this.formData = res.data.target
      },
      close() {
        this.formData.targetName = ''
        this.formData.targetDesc = ''
        this.formData.targetId = ''
        this.dialogFormVisible = false
        this.$emit('success')
      },
      rest() {
        this.formData.targetName = ''
        this.formData.targetDesc = ''
      },
      save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const { createTime, updateTime, ...other } = this.formData
            const { msg, code, data } = await mergeTblNbsjTarget({
              ...other,
              tempId: this.templeteId,
            })
            if (code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.$emit('success')
              this.formData.targetId = ''
            } else {
              this.$baseMessage(msg, 'error')
            }
            this.close()
          }
        })
      },
    },
  }
</script>
<style scoped></style>
