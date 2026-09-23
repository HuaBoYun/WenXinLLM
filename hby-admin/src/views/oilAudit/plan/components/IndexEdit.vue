<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      :append-to-body="true"
      @close="close"
      v-if="isEdit"
      :close-on-click-modal="false"
    >
      <IndexEditDialog
        ref="edit"
        @close="close"
        @fetch-data="fetchData"
        :is-edit="isEdit"
        :title="title"
      />
    </el-dialog>
    <IndexEditDialog
      ref="edit"
      @close="close"
      v-else
      @fetch-data="fetchData"
      :is-edit="isEdit"
      :title="title"
    />
  </div>
</template>

<script>
  import IndexEditDialog from './IndexEditDialog.vue'

  export default {
    name: 'IndexEdit',
    components: {
      IndexEditDialog,
    },
    props: {
      isEdit: {
        type: Boolean,
        default: true,
      },
    },
    data() {
      return {
        dialogFormVisible: false,
        title: '',
        formDataAdd: {},
        formColudEdit: false,
      }
    },
    created() {},
    methods: {
      fetchData() {
        this.$emit('fetch-data')
      },
      async showDetail(row, type, isEdit) {
        console.log('🚀 ~ showDetail ~ isEdit:', isEdit)
        console.log('🚀 ~ showDetail ~ type:', type)
        //如果是办理页面，isEdit只能是false
        this.isEdit = false
        // this.formColudEdit = isEdit
        // let userInfo = JSON.parse(localStorage.getItem('userInfo'))
        // if (row.cystaffid == userInfo.staffid && row.cystate == '需调整') {
        //   this.isEdit = true
        // }
        if (!this.isEdit) {
          this.dialogFormVisible = false
          this.formDataAdd.planid = this.getQueryVariable(row.cyurl, 'planid')
          this.$nextTick(() => {
            this.$refs['edit'].showEdit(row, true, 0, false)
          })
        }
      },
      getQueryVariable(url, variable) {
        var query = url.substring(1)
        var vars = query.split('?')
        for (var i = 0; i < vars.length; i++) {
          var pair = vars[i].split('=')
          if (pair[0] == variable) {
            return pair[1]
          }
        }
        return false
      },
      async showEdit(row, disabled, planNum, isEdit) {
        this.dialogFormVisible = true
        if (isEdit == undefined) {
          this.isEdit = true
        } else {
          this.isEdit = isEdit
        }
        this.$nextTick(() => {
          this.$refs['edit'].showEdit(row, disabled, planNum)
        })

        //新建按钮入口
        if (!row && !disabled) {
          this.title = '新增'
        } else if (row && disabled) {
          //   //查看按钮入口
          this.title = '查看'
        } else {
          //   //编辑按钮入口
          this.title = '编辑'
        }
      },

      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  ::v-deep .el-form.disabled {
    input {
      border: 0;
      background-color: #ffffff;
    }
  }
  ::v-deep .el-date-editor.el-input,
  .el-date-editor.el-input__inner {
    width: 140px;
  }
  ::v-deep .el-input--small .el-input__inner {
    width: 140px;
  }
</style>
