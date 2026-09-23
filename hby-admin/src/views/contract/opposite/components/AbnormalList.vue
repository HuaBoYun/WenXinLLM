<template>
  <el-drawer
    :before-close="close"
    size="1000px"
    :title="title"
    :visible.sync="dialogFormVisible"
  >
    <el-table :data="tableData">
      <el-table-column align="center" label="合同编号" prop="contractno" />
      <el-table-column align="center" label="合同名称" prop="contractname">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.contractname }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="开始时间" prop="startdate" />
      <el-table-column align="center" label="结束时间" prop="enddate" />
      <el-table-column align="center" label="履行情况" prop="nodememo" />
      <!-- <el-table-column align="center" label="验收情况" prop="nodecontent" /> -->
      <el-table-column align="center" label="是否违约" prop="iswy" />
      <el-table-column
        align="center"
        label="违约内容及违约责任"
        prop="nodepost"
      />
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <ContractInfo ref="contractInfo" />
  </el-drawer>
</template>

<script>
  import { abnormalPerformance } from '@/api/contract/opposite'
  import ContractInfo from '@/views/contract/opposite/components/ContractInfo'
  export default {
    name: 'AbnormalList',
    components: { ContractInfo },
    data() {
      return {
        queryForm: {
          pageNo: 1,
          pageSize: 20,
        },
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 3,
        title: '异常履约信息',
        dialogFormVisible: false,
        tableData: [],
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.queryForm.budgetid = row.budgetid
        this.getList()
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      async getList() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await abnormalPerformance(this.queryForm)
        this.tableData = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getList()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.getList()
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        this.$refs['contractInfo'].showDetail(row, row.contracttype)
      },
    },
  }
</script>
<style scoped></style>
