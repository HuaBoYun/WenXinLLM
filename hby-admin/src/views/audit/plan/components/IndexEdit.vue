<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1100px"
      :append-to-body="true"
      @close="close"
      v-if="isEdit"
    >
      <IndexEditDialog
        ref="edit"
        @close="close"
        @fetch-data="fetchData"
        :is-edit="isEdit"
        :process-data="processData"
      />
    </el-dialog>
    <IndexEditDialog
      ref="edit"
      @close="close"
      v-else
      @fetch-data="fetchData"
      :is-edit="isEdit"
      :process-data="processData"
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
      // isEdit: {
      //   type: Boolean,
      //   default: true,
      // },
    },
    data() {
      return {
        dialogFormVisible: false,
        title: '',
        formDataAdd: {},
        isEdit: true,
        processData: {},
      }
    },
    created() {},
    methods: {
      fetchData() {
        this.dialogFormVisible = false
        this.$emit('fetch-data')
      },
      async showDetail(row, type, isEdit, processData) {
        console.log('row, type, isEdit', row, type, isEdit)
        console.log('indexEdit => processData', processData)
        this.processData = processData
        this.isEdit = isEdit
        let userInfo = JSON.parse(localStorage.getItem('userInfo'))
        if (
          processData.cy.cystaffid == userInfo.staffid &&
          processData.cy.cystate == '需调整'
        ) {
          this.isEdit = true
        }
        if (!this.isEdit) {
          this.dialogFormVisible = false
          // this.formDataAdd.planid = this.getQueryVariable(row.cyurl, 'planid')
          this.$nextTick(() => {
            console.log('showDetail')
            this.$refs['edit'].showEdit(row, true, 0, false)
          })
        }
      },
      // getQueryVariable(url, variable) {
      //   var query = url.substring(1)
      //   var vars = query.split('?')
      //   for (var i = 0; i < vars.length; i++) {
      //     var pair = vars[i].split('=')
      //     if (pair[0] == variable) {
      //       return pair[1]
      //     }
      //   }
      //   return false
      // },
      async showEdit(row, disabled, planNum, isEdit) {
        this.dialogFormVisible = true
        if (isEdit == undefined) {
          this.isEdit = true
        } else {
          console.log('isedit', isEdit)
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
