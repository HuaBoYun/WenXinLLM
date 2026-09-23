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
          label="项目编号"
          prop="qdcode"
          width="100"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row, true)" 
            >
              {{ row.qdcode }}
            </el-button>
          </template>
        </el-table-column> 
          <el-table-column 
            align="center"
            label="项目名称"
            prop="projectName"
          />
          <el-table-column 
            align="center"
            label="项目负责人"
            prop="projectOrderName"
            show-overflow-tooltip
          />
          <el-table-column 
            align="center"
            label="审计类型"
            prop="sjlxName"
            show-overflow-tooltip
          > 
          </el-table-column>
          <el-table-column 
            align="center"
            label="计划开始时间"
            prop="planStarttime"
            show-overflow-tooltip
          />
          <el-table-column 
            align="center"
            label="计划结束时间"
            prop="planEndtime"
            show-overflow-tooltip
          />
          <el-table-column 
            align="center"
            label="计划年度"
            prop="planYear"
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

    <Views ref="edit" @fetchData="fetchData"></Views>
  </el-dialog>
</template>
<script> 
import { implementPlanList } from '@/oapi/audit/project'
// import Views from '@/views/oilAudit/plan/components/IndexEdit.vue'
import Views from '@/views/oilAudit/project/components/IndexEdit.vue'


  export default {
    name: 'assignTable',
    components: {  Views },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        list: [],
        dialogFormVisible: false,
        title: '项目',
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        assignInfo: null,
        queryForm: {
          xmnd: undefined,
          dataType: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        planNum:'',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
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
          data: {  tlist: list, totalRecord: total 
          },
        } = await implementPlanList(this.queryForm)
        this.list = list
        this.planNum = list[0].projectCode
        this.total = total
        this.listLoading = false
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum)
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
 