<template>
  <el-dialog
    title="审计报告定稿"
    :visible.sync="dialogVisible"
    width="50%"
    :modal="false"
    :close-on-click-modal="false"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <vab-query-form>
      <vab-query-form-top-panel :span="24">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.document"
              clearable
              placeholder="文号"
            />
          </el-form-item>
          <el-form-item>
            <el-input v-model="queryForm.title" clearable placeholder="标题" />
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
      </vab-query-form-top-panel>
    </vab-query-form>

    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column align="center" label="文号" prop="document">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.document }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="标题" prop="title" />
      <el-table-column
        align="center"
        label="创建人"
        prop="cjr"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="创建时间"
        prop="cjsj"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="项目名称"
        prop="projectName"
        show-overflow-tooltip
      />
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
    <!-- <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template> -->
    <SuggestView ref="edit" @fetchData="fetchData" />
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  import { sjbgdgList } from '@/oapi/audit/report'
  import SuggestView from '@/views/oilAudit/report/components/SuggestView.vue'
  export default {
    components: {
      SuggestView,
    },
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
        queryForm: {
          document: '',
          title: '',
          pageNumber: 1,
          pageSize: 20,
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
      }
    },
    methods: {
      async handleDetail(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
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
        if (this.multipleSelection.length == 0) {
          this.$baseMessage(
            '请选择审计项目！',
            'error',
            'vab-hey-message-error'
          )
          return false
        }
        this.$emit('selected', this.multipleSelection)
        this.dialogVisible = false
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await sjbgdgList(this.queryForm)
        this.tableData = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
      },
      resetQueryForm() {
        this.queryForm = {
          document: '',
          title: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
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
