<template>
  <el-dialog
    title="模板"
    :visible.sync="dialogVisible"
    width="600px"
    :append-to-body="true"
    :close-on-click-modal="false"
  >
    <div class="system-log-container">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="模板名称">
          <el-input
            v-model="form.tempTitle"
            placeholder="请输入模板名称"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="save">立即创建</el-button>
          <el-button @click="dialogVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
</template>
<script>
  import { saveTemp } from '@/api/setting/ueditor'
  export default {
    name: 'UEditorTemplateSave',
    data() {
      return {
        dialogVisible: false,
        form: {
          tempTitle: '',
          tempContent: '',
          tempType: '',
        },
      }
    },
    methods: {
      showDialog(type, content) {
        this.form.tempType = type
        this.form.tempContent = content
        this.dialogVisible = true
      },
      async save() {
        if (this.form.tempTitle === '') {
          this.$message.warning('请输入模板名称')
          return
        }
        if (this.form.tempContent === '') {
          this.$message.warning('模板内容不能为空')
          return
        }
        const { msg } = await saveTemp(this.form)
        this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        this.form.tempTitle = ''
        this.dialogVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 70px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
