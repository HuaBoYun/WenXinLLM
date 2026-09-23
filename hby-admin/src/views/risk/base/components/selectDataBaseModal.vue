<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="close">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      @select="handleSelection"
      style="width: 100%"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        align="center"
        label="数据库名称"
        prop="dataBaseUsers"
      ></el-table-column>
      <el-table-column align="center" label="数据库类型" prop="dataBaseType" />
      <el-table-column
        align="center"
        label="数据库名称"
        prop="dataBaseOwnership"
      />
      ¡
    </el-table>
  </el-dialog>
</template>

<script>
  import { getDataSourceList } from '@/api/setting/org'
  export default {
    components: {},
    data() {
      return {
        dialogFormVisible: false,
        loading: false,
        footer: true,
        title: '选择数据库',
        list: [],
        current: undefined,
        multipleSelection: [],
        queryForm: {
          createType: 2,
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    methods: {
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
      },
      showEdit() {
        this.fetchData()
        this.dialogFormVisible = true
      },
      handleSubmit() {
        if (!this.current) {
          this.$baseMessage('请选择数据库！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('getData', this.multipleSelection)
        this.close()
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getDataSourceList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
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
</style>
