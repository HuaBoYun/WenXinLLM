<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-table :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column align="center" label="问题编号" prop="BUGNUMBER" />
      <el-table-column align="center" label="发现人" prop="DISCOVERPERSON" />
      <el-table-column align="center" label="问题来源" prop="SOURCE" />
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
      <el-button @click="close">关 闭</el-button>
      <el-button type="primary" @click="submit()">选定</el-button>
    </template>
    <ResultsInfo ref="ResultsInfo" />
  </el-dialog>
</template>

<script>
  import { questionList, addProblem } from '@/api/internal/result'
  import ResultsInfo from '@/views/internal/evaluationManagement/components/ResultsInfo.vue'
  export default {
    name: 'ResultsAdd',
    components: { ResultsInfo },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          targetId: '',
          orgid: '',
          pageNumber: 1,
          pageSize: 5,
        },
        tableData: [],
        title: '问题管理',
        dialogFormVisible: false,
        multipleSelection: [],
      }
    },
    // created() {
    //   this.fetchData()
    // },
    methods: {
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageBean: { records, total },
          },
        } = await questionList(this.queryForm)
        this.list = records
        this.total = total
        this.listLoading = false
      },
      showEdit(row) {
        this.queryForm.targetId = row.assid
        this.queryForm.orgid = row.orgid
        this.queryData()
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      async submit() {
        if (this.multipleSelection.length === 0) {
          this.$baseMessage('请选择问题', 'error', 'vab-hey-message-error')
          return
        }
        const { data, code, msg } = await addProblem({
          targetid: this.queryForm.targetId,
          proid: this.multipleSelection.map((item) => item.BUGID).join(','),
        })
        if (code == 200) {
          this.$baseMessage('选择成功', 'success', 'vab-hey-message-success')
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
        this.close()
      },
    },
  }
</script>
<style lang="scss" scoped>
  .box_row {
    margin-bottom: 20px;
  }
  .flex {
    display: flex;
    justify-content: flex-end;
  }
</style>
