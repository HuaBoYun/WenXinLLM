<template>
  <div class="system-log-container">
    <el-col :span="24">
      <h3>指标预警</h3>
    </el-col>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="指标编码" prop="indicatorcode">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.indicatorcode }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="指标名称" prop="indicatorname" />
      <el-table-column align="center" label="说明" prop="indicatordes" />
      <el-table-column
        align="center"
        label="异常"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">异常</el-button>
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
  import { zbList } from '@/api/setting/msg'
  // import DisputeEdit from '@/views/contract/legal/components/DisputeEdit'

  export default {
    name: 'Zpyj',
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
          data: { tlist, totalRecord },
        } = await zbList(this.queryForm)
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
