<template>
  <el-dialog
    title="底稿"
    :visible.sync="dialogVisible"
    width="50%"
    :modal="false"
    :close-on-click-modal="false"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-form
      ref="form"
      checkable
      :inline="true"
      label-width="0"
      :model="queryForm"
      @submit.native.prevent
    >
      <el-form-item>
        <el-input
          v-model="queryForm.draftNumber"
          clearable
          placeholder="底稿编号"
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
        <el-button type="primary" @click="reset">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @select-all="handleSelectAll"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        align="center"
        label="底稿编号"
        prop="draftNumber"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="底稿名称"
        prop="draftName"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="审计项目名称"
        prop="projectName"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="审计事项"
        prop="auditMatters"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="被审计单位名称"
        prop="auditeeName"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="创建时间"
        prop="createTime"
        show-overflow-tooltip
        :formatter="formatDates"
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
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template> -->
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  import { getManuscriptPage } from '@/oapi/audit/implement'
  import { formatDay } from '@/utils/index'
  export default {
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          draftNumber: '',
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        projectInfo: {},
        select: [],
        projectId: '',
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
      async showEdit(projectId) {
        this.projectId = projectId
        this.dialogVisible = true
        this.listLoading = true
        this.select = []
        await this.fetchData()
      },
      async fetchData() {
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getManuscriptPage({
          ...this.queryForm,
          projectId: this.projectId,
        })
        this.tableData = list
        this.total = total
        this.setCheckedRows()
        this.listLoading = false
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
      //选择
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.id == row.id)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select.some((x) => x.id == row.id)) {
              this.select.push(row)
            }
          })
        } else {
          this.tableData.map((row) => {
            const i = this.select.findIndex((x) => x.id == row.id)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },
      reset() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
          draftNumber: '',
        }
        this.fetchData()
      },
      close() {
        this.reset()
        this.dialogVisible = false
      },
      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.tableData.find((item) => {
                return row.id == item.id
              }),
              true
            )
          })
        })
      },
      /**
       * @description   点击确定,把数据回传到父组件,关闭当前组件
       * @param {*}
       * @return {*}
       */
      save() {
        if (this.select.length == 0) {
          this.$baseMessage(
            '请选择关联底稿！',
            'error',
            'vab-hey-message-error'
          )
          return false
        }
        this.$emit('selected', this.select)
        this.dialogVisible = false
      },
      handleSizeChange(val) {
        console.log(val)
        this.queryForm.pageSize = val
        this.showEdit(this.projectId)
      },
      handleCurrentChange(val) {
        console.log(val)
        this.queryForm.pageNumber = val
        this.showEdit(this.projectId)
      },
      formatDates(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
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
