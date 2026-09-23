<template>
  <el-drawer
    :before-close="close"
    size="1000px"
    :title="title"
    :visible.sync="dialogFormVisible"
  >
    <el-row class="box_row flex">
      <el-button type="primary" @click="handleExport">导出</el-button>
    </el-row>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="计划编号" prop="PLANNUMBER" />
      <el-table-column align="center" label="计划名称" prop="PLANNAME" />
      <el-table-column align="center" label="测试人" prop="REALNAME" />
      <el-table-column
        align="center"
        label="检查数据"
        prop="ZS"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          <el-button
            v-if="row.ZS > 0"
            type="text"
            @click="$refs['ResultsView'].showEdit(row, 'NUMBER')"
          >
            {{ row.ZS }}
          </el-button>
          <div v-else>{{ row.ZS }}</div>
        </template>
      </el-table-column>
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
    <ResultsView ref="ResultsView" />
  </el-drawer>
</template>

<script>
  import { xxjgList, resultExport } from '@/api/internal/new/results'
  import ResultsView from '@/views/internal/new/hgjc/components/ResultsView'
  import { downloadFile } from '@/utils/otherUtils'
  export default {
    name: 'ResultskView',
    components: { ResultsView },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          planid: '',
          pageNumber: 1,
          pageSize: 10,
        },
        title: '合规结果汇总-详细结果',
        dialogFormVisible: false,
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
        } = await xxjgList(this.queryForm)
        this.list = records
        this.total = total
        this.listLoading = false
      },
      showEdit(row) {
        this.queryForm.planid = row.TESTPLANID
        this.queryData()
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      async handleExport() {
        // const info = {
        //   planid: this.list.PLANID,
        // }
        const res = await resultExport({ planid: this.list[0].PLANID })
        downloadFile(res, '测试结果汇总-详细结果.xlsx')
      },
    },
  }
</script>
<style scoped>
  .box_row {
    margin-bottom: 10px;
  }
  .flex {
    display: flex;
    justify-content: flex-end;
  }
</style>
