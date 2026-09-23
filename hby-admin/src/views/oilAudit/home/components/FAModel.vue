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
            label="方案编号"
            prop="xtcode"
          />
          <el-table-column 
            align="center"
            label="项目名称"
            prop="xmmc"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.xmmc }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="计划名称"
            prop="jhmc" 
          />
          <el-table-column
            align="center"
            label="编制人"
            prop="cjr" 
          ></el-table-column>
          <el-table-column
            align="center"
            label="编制日期"
            prop="cjsj" 
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

    <Views ref="edit" @fetchData="fetchData"></Views>
  </el-dialog>
</template>
<script> 
import { xmglgzfaList,  } from '@/oapi/audit/project'
import Views from '@/views/oilAudit/plan/components/gzfaView.vue'

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
        } = await xmglgzfaList(this.queryForm)
        this.list = list
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, 'detail')
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
