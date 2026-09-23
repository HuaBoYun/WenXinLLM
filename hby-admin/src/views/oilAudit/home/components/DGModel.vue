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
        label="底稿编号"
        prop="draftNumber"
        width="170"
      >
      <template #default="{ row }">
              <el-button
                type="text"
                @click="handleAddOrUpdate(row, true)"
                style="white-space: pre-line; line-height: 16px"
              >
                {{ row.draftNumber }}
              </el-button>
            </template>
      </el-table-column>
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
          />

          <el-table-column
            align="center"
            label="审计事项"
            prop="auditMatters" 
          />
          <el-table-column
            align="center"
            label="被审计单位名称"
            prop="auditeeName" 
          ></el-table-column>
          <el-table-column
            align="center"
            label="创建时间"
            prop="createTime" 
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

  <Views ref="edit" @queryData="queryData"></Views>
  </el-dialog>
</template>
<script> 
  import { 
    getDetail,
    getList, 
  } from '@/oapi/audit/newMyDraft'
   import Views from '@/views/oilAudit/implement//components/newMyDraftView.vue'
  import { formatDay } from '@/utils/index' 

  export default {
    name: 'assignTable',
    components: {  Views },
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
          isall:'',
          staffId:'',
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
        } = await getList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAddOrUpdate(row, disabled) {
        this.$refs['edit'].showModal(row, false, disabled)
      },
      showEdit(row,type, isall) {
        this.dialogFormVisible = true
        if (row) {
          this.queryForm.xmnd = row.xmnd 
          this.queryForm.probleMdraft = type
          this.queryForm.staffId = row.staffId
          
          this.queryForm.isall = isall?isall : ''
          if(isall == 2) {
            this.queryForm.staffId = row.staffId
          } else {
            this.queryForm.staffId = ''
          }
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
