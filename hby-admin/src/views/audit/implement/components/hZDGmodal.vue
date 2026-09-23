<template>
  <el-dialog
    v-if="dialogFormVisible"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <vab-query-form>
      <vab-query-form-left-panel>
        <span></span>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button>取消</el-button>
        <el-button type="success" @click="saveInfo">选定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      ref="multipleTable"
      :row-key="getRowKeys"
      @selection-change="handleSelectionChange"
      :data="reportData"
      v-loading="listLoading"
    >
      <el-table-column
        type="selection"
        width="55"
        :reserve-selection="true"
      ></el-table-column>
      <el-table-column label="底稿编号" prop="sheetCode"></el-table-column>
      <el-table-column label="底稿名称" prop="sheetName"></el-table-column>
      <el-table-column label="审计程序" prop="suditProcess"></el-table-column>
      <el-table-column
        label="审计程序执行过程"
        prop="auditDesc"
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
  </el-dialog>
</template>
<script>
  import { getHZDGTableData } from '@/api/audit/implement'
  export default {
    name: 'xxxxx',
    created() {},
    data() {
      return {
        reportData: [],
        dialogFormVisible: false,
        title: '',
        layout: 'total, sizes, prev, pager, next, jumper',
        listLoading: false,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        total: 0,
        select: [],
      }
    },
    methods: {
      showEdits() {
        this.dialogFormVisible = true
        this.fetchData()
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
          data: {
            project: project,
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getHZDGTableData(this.queryForm)
        // this.projectInfo = project
        this.reportData = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      saveInfo() {
        this.$emit('getInfoFromModal', this.select)
        this.dialogFormVisible = false
      },
      // 2、设置row-key
      getRowKeys(row) {
        return row.sheetId
      },
      // 3、勾选列表操作
      handleSelectionChange(selection) {
        this.select = selection
      },
      // 4、回显已勾选的数据
      setCheckedRows() {
        let selectItem = []
        this.reportData.forEach((item) => {
          this.select.forEach((id) => {
            if (item.sheetId === id) {
              selectItem.push(item)
            }
          })
        })
        this.$refs.multipleTable.toggleRowSelection(selectItem)
      },
    },
  }
</script>
