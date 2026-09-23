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
      />
    </el-dialog>
    <IndexEditDialog
      ref="edit"
      @close="close"
      v-else
      @fetch-data="fetchData"
      :is-edit="isEdit"
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
       /**
       * @description:  回调父组件函数，刷新列表数据
       *  @param {*}   
       * @return {*}
       */ 
      fetchData() {
        this.$emit('fetch-data')
      },
       /**
       * @description:  显示页面，详细页面，无法编辑
       *  @param {*}   
       * @return {*}
       */ 
      async showDetail(row, type, isEdit) {
         //如果是办理页面，isEdit只能是false
        this.isEdit = false 
        if (!this.isEdit) {
          this.dialogFormVisible = false
          this.formDataAdd.planid = this.getQueryVariable(row.cyurl, 'planid')
          this.$nextTick(() => {
            this.$refs['edit'].showEdit(row, true, 0, false)
          })
        }
      },
       /**
       * @description:  获取url中的参数
       *  @param {*}   
       * @return {*}
       */ 
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
       /**
       * @description:  显示页面，编辑页面
       * @param {*}   
       * @return {*}
       */ 
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */      
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

         /**
       * @description: 新建按钮入口
       * @param {*}   
       * @return {*}
       */ 
        if (!row && !disabled) {
          this.title = '新增'
        } else if (row && disabled) {
         //查看按钮入口
          this.title = '查看'
        } else {
          //编辑按钮入口
          this.title = '编辑'
        }
      },
       /**
       * @description:  关闭页面
       * @param {*}   
       * @return {*}
       */ 
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
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
