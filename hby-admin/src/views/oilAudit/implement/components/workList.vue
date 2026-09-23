<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1300px"
    @close="close"
    :close-on-click-modal="false"
  >

    <el-table :data="list"  ref="multipleTable"   @selection-change="handleSelectionChange"> 
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
            align="center" 
            label="索引号"
            prop="indexno" 
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.indexno }}
              </el-button>
            </template>
          </el-table-column>

          <el-table-column
            align="center" 
            label="被审计单位名称"
            prop="auditeeName" 
          ></el-table-column>

          <el-table-column
            align="center"
            label="实施审计时间"
            prop="implementationTime" 
          />
          <el-table-column
            align="center"
            label="审计项目名称"
            prop="projectName" 
          ></el-table-column> 
          <el-table-column
            align="center"
            label="审计内容和目标"
            prop="contentObjectives" 
          />
          <el-table-column
            align="center"
            label="执行的审计程序和工作过程"
            prop="executedProceduresProcesses" 
          />
          <el-table-column
            align="center"
            label="发现的疑点、线索及查证情况"
            prop="verificationSituation" 
          />
          <el-table-column
            align="center"
            label="审计线索及数据来源"
            prop="cluesSources" 
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
    <template #footer>
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>

    <SJGZJLview ref="edit" @fetchData="fetchData"></SJGZJLview>
  </el-dialog>
</template>
<script> 
  import {
    getRecordsListByMyDraft
  } from '@/oapi/audit/newMyDraft' 
  import { formatDay } from '@/utils/index' 
  import SJGZJLview from '@/views/oilAudit/implement/components/sjgzjlView.vue'

  export default {
    name: 'assignTable',
    components: {  SJGZJLview },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        list: [],
        dialogFormVisible: false,
        title: '审计工作记录',
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        assignInfo: null,
        queryForm: { 
          pageNumber: 1,
          pageSize: 20,
        },
        multipleSelection:[],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: { 
      save() {
      if (!this.multipleSelection || !this.multipleSelection.length) {
        return this.$message.error('请选择数据')
      }
      // this.appendVisible = true
      this.$emit('fetch', this.multipleSelection)
      this.dialogFormVisible = false
    },
      handleSelectionChange(val) {
      this.multipleSelection = val
    },
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      formatDates(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
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
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getRecordsListByMyDraft(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAddOrUpdate(row, disabled) {
        this.$refs['edit'].showModal(row, false, disabled)
      },
      showEdit(row) {
        this.dialogFormVisible = true 
          this.fetchData() 
      },
      close() {
        this.dialogFormVisible = false
        this.tableData = []
        this.queryForm.solutionid = undefined
      },
    },
  }
</script>

<style></style>
