<template>
  <el-dialog
    title="项目"
    :visible.sync="dialogVisible"
    width="50%"
    :modal="false"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      :inline="true"
      label-width="0"
      :model="queryForm"
      @submit.native.prevent
    >
      <el-form-item>
        <el-input
          v-model="queryForm.projectName"
          clearable
          placeholder="项目名称"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          icon="el-icon-search"
          native-type="submit"
          type="primary"
          @click="fetchData"
        >
          查询
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-button native-type="submit" type="primary" @click="resetSearch">
          重置
        </el-button>
      </el-form-item>
    </el-form>
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        align="center"
        label="项目名称"
        prop="name"
      >
        <template #default="{ row }">
          <el-button
            type="text"
            @click="handleCwxmDetail(row, true)"
            style="font-size: 14px;"
            v-if="row.gljhxmlx == '23'"
          >
            {{ row.name }}
          </el-button>
          <span v-if="row.gljhxmlx !== '23'">{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="实施单位" prop="exePhraseUnit" />
      <el-table-column
        align="center"
        label="审计范围"
        prop="auditRange"
      ></el-table-column>
      <el-table-column
        align="center"
        label="小组"
        prop="auditGroup"
      ></el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>

    <cwsjxmapbOutView ref="cwsjxmapbOutView" type="report" @selected="cwModalSelect" />
  </el-dialog>
</template>
<script>
  import { cwxzfindList } from '@/oapi/audit/project'
  import cwsjxmapbOutView from './cwsjxmapbOutView.vue'
  export default {
    components: {
      cwsjxmapbOutView,
    },
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          projectName: '',
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
      }
    },
    methods: {
      /**
       * @description  初始化
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit() {
        this.dialogVisible = true
        this.fetchData()
      },
      async fetchData() {
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await cwxzfindList(this.queryForm)
        this.tableData = tlist || []
        this.total = totalRecord || 0
      },
      /**
       * @description  选择列表数据，把数据存入multipleSelection
       * @param {*}
       * @return {*}
       */
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      /**
       * @description   点击确定,把数据回传到父组件,关闭当前组件
       * @param {*}
       * @return {*}
       */
      save() {
        this.$emit('selected', this.multipleSelection)
        this.dialogVisible = false
      },
      cwModalSelect(name, row) {
        console.log(this.multipleSelection)
        this.$emit('selected', this.multipleSelection, name, row)
        this.dialogVisible = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      resetSearch() {
        this.queryForm = { pageNumber: 1, pageSize: 20, projectName: '' }
        this.fetchData()
      },
      handleCwxmDetail(row) {
        this.$refs.multipleTable.clearSelection()
        this.$refs.multipleTable.toggleRowSelection(row, true)
        this.multipleSelection = [row]
        this.$refs['cwsjxmapbOutView'].showEdit({ data: row })
      },
    },
  }
</script>
<style scoped lang="scss">
  // 隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
