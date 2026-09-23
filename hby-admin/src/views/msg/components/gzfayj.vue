<template>
  <div class="system-log-container">
    <el-col :span="24">
      <h3>规则方案预警</h3>
    </el-col>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="方案名称" prop="disputeitem" />
      <el-table-column align="center" label="类别" prop="contractname" />
      <el-table-column align="center" label="粒度" prop="disputetype" />
      <el-table-column align="center" label="创建时间" prop="attorney" />
      <el-table-column
        align="center"
        label="结果"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">结果</el-button>
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
    <!-- <DisputeEdit ref="edit" @fetch-data="fetchData" /> -->
  </div>
</template>

<script>
  import { yjfa1 } from '@/api/setting/msg'
  // import DisputeEdit from '@/views/contract/legal/components/DisputeEdit'
  export default {
    name: 'Gzfayj',
    // components: { DisputeEdit },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 6,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
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
          date: { tlist, totalRecord },
        } = await yjfa1(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleDetail(row) {
        this.$refs['edit'].showDetail(row)
      },
    },
  }
</script>
