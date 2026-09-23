<template>
  <div class="system-log-container">
    <el-col :span="24">
      <h3>我的评价</h3>
    </el-col>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="评价名称" prop="assessname" />
      <el-table-column align="center" label="被评价对象" prop="orgname" />
      <el-table-column align="center" label="任务状态" prop="status">
        <template #default="{ row }">
          <span v-if="row.status == 1">未开始</span>
          <span v-else-if="row.status == 2">未处理</span>
          <span v-else-if="row.status == 4">已处理</span>
          <span v-else>已完成</span>
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
  import { mygrade_list } from '@/api/setting/msg'
  // import DisputeEdit from '@/views/contract/legal/components/DisputeEdit'

  export default {
    name: 'Wdpj',
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
          pageInfo: { tlist, totalRecord },
        } = await mygrade_list(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
    },
  }
</script>
