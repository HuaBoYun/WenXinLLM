<template>
  <el-dialog
    title="标注"
    :visible.sync="dialogVisible"
    width="600px"
    :append-to-body="true"
    :close-on-click-modal="false"
  >
    <div class="system-log-container">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="标注类型">
          <el-radio-group v-model="form.type">
            <el-radio label="0">标注</el-radio>
            <el-radio label="1">删除</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="标注内容">
          <el-input
            v-model="form.content"
            placeholder="请输入模板名称"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="mark">标注</el-button>
          <el-button @click="dialogVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
</template>
<script>
  export default {
    name: 'UEditorAnnotation',
    data() {
      return {
        dialogVisible: false,
        form: {
          type: '0',
          content: '',
        },
        editor: '',
        node: '',
        userInfo: JSON.parse(localStorage.getItem('userInfo')),
      }
    },
    methods: {
      showDialog(editor, node) {
        this.form.type = '0'
        this.form.content = ''
        this.editor = editor
        this.node = node
        this.dialogVisible = true
      },
      mark() {
        let timeStr = this.parseTime(new Date().getTime())
        // 标注
        if (Number(this.form.type) === 0) {
          if (this.form.content === '') {
            this.$message.warning('标注内容不能为空')
            return
          }
          let title =
            this.userInfo.realname +
            ' 标注：' +
            this.form.content +
            ' ' +
            timeStr
          this.editor.execCommand(
            'insertHtml',
            '<a class="mark-content" title="' +
              title +
              '">' +
              this.node.innerHTML +
              '</a>'
          )
        }
        // 删除
        if (Number(this.form.type) === 1) {
          let title =
            this.userInfo.realname +
            ' 删除：' +
            this.form.content +
            ' ' +
            timeStr
          this.editor.execCommand(
            'insertHtml',
            '<a class="mark-delete" title="' +
              title +
              '">' +
              this.node.innerHTML +
              '</a>'
          )
        }
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
