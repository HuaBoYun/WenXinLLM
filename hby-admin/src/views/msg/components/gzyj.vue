<template>
  <div class="system-log-container">
    <el-col :span="24">
      <h3>规则预警</h3>
    </el-col>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="规则编码" prop="rulecode">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.rulecode }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="规则名称" prop="rulename" />
      <el-table-column align="center" label="说明" prop="ruledescription" />
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
  import { gzList } from '@/api/setting/msg'
  // import DisputeEdit from '@/views/contract/legal/components/DisputeEdit'

  export default {
    name: 'Gzyj',
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
        } = await gzList(this.queryForm)
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
