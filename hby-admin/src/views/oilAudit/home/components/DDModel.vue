<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1300px"
    @close="close"
    :close-on-click-modal="false"
  >

    <el-table :data="list"> 
      <el-table-column
            align="center"
            :key="index"
            label="项目名称"
            prop="projectName" 
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.projectName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="主审"
            prop="chiefAuditor" 
          ></el-table-column>
          <el-table-column
            align="center"
            label="参与督导人员"
            prop="supervisionParticipants" 
          ></el-table-column>
          <el-table-column 
            align="center"
            label="督导日期"
            prop="supervisionDate"
            show-overflow-tooltip
          />
          <el-table-column 
            align="center"
            label="现场情况"
            prop="onsiteCondition"
            show-overflow-tooltip
          />
          <el-table-column 
            align="center"
            label="存在问题及需协调解决的问题"
            prop="issuesAndCoordination"
            show-overflow-tooltip
          />
          <el-table-column 
            align="center"
            label="督导意见"
            prop="supervisionOpinions"
          />
          <el-table-column 
            align="center"
            label="备注"
            prop="remarks"
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

    <SJDDJLview ref="edit" @fetchData="fetchData"></SJDDJLview>
  </el-dialog>
</template>
<script> 
  import {
    overseeRecordsList,
    overseeRecordsDelete,
  } from '@/oapi/audit/implement' 
  import { formatDay } from '@/utils/index' 
  import SJDDJLview from '@/views/oilAudit/implement/components/sjddjlView.vue'

  export default {
    name: 'assignTable',
    components: {  SJDDJLview },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        list: [],
        dialogFormVisible: false,
        title: '底稿',
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        assignInfo: null,
        queryForm: {
          xmnd:"",
          probleMdraft:'',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: { 
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
        } = await overseeRecordsList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAddOrUpdate(row, disabled) {
        this.$refs['edit'].showModal(row, false, disabled)
      },
      showEdit(row) {
        this.dialogFormVisible = true
        if (row) {
          this.queryForm.xmnd = row.xmnd  
          this.queryForm.staffId = row.staffId 
          this.fetchData()
        }
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
