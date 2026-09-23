<template>
  <el-dialog
    :close-on-click-modal="false"
    title="历史版本"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="风险编号" prop="risknumber">
        <template #default="{ row }">
          <el-button type="text" @click="handleRead(row)">
            {{ row.risknumber }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="风险名称"
        prop="riskname"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="机构" prop="unit" />
      <el-table-column
        align="center"
        label="版本"
        prop="version"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="创建时间" prop="riskcreatedt">
        <template slot-scope="{ row }">
          {{
            row.riskcreatedt ? dayjs(row.riskcreatedt).format('YYYY-MM-DD') : ''
          }}
        </template>
      </el-table-column>
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
    <RiskRead ref="read" />
    <RiskEdit ref="edit" :treeId="treeId" @fetch-data="fetchData" />
  </el-dialog>
</template>

<script>
  import { getCreationVersionHistoryList } from '@/api/risk'
  import dayjs from 'dayjs'
  import RiskRead from '../../creation/components/RiskRead.vue'
  import RiskEdit from '../../creation/components/RiskEdit.vue'

  export default {
    name: 'History',
    components: {
      RiskRead,
      RiskEdit,
    },
    data() {
      return {
        treeId: '',
        dayjs: dayjs,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNo: 1,
          pageSize: 20,
        },
        dialogFormVisible: false,
      }
    },
    created() {
      // this.fetchData()
    },
    methods: {
      /**
       * @description: 打开详情
       * @return {*}
       */
      handleRead(row) {
        // this.$refs['read'].showRead(row, row.riskcatid)
        this.$refs['edit'].showEdit(row, row.riskcatid, true)
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      /**
       * @description: 分页 初始化
       * @return {*}
       */
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: { hisVersion },
        } = await getCreationVersionHistoryList(this.queryForm)
        this.list = hisVersion.records
        this.total = hisVersion.total
        this.listLoading = false
      },
      /**
       * @description: 页面初始化
       * @return {*}
       */
      showHistory(row) {
        console.log(row)
        this.dialogFormVisible = true
        this.queryForm.riskid = row.riskid
        this.fetchData()
      },
      /**
       * @description: 关闭页面
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 250px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
