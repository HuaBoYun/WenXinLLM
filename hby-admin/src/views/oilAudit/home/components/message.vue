<template>
  <div class="system-log-container">
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="会议名称" prop="conferenceName" />
      <el-table-column align="center" label="会议时间" prop="">
        <template #default="{ row }">
          <span>
            {{ row.conferenceTimeStart + '-' + row.conferenceTimeEnd }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="会议主持人"
        prop="conferenceCompereName"
      ></el-table-column>
      <el-table-column
        align="center"
        label="创建时间"
        prop="createdTime"
      ></el-table-column>
    </el-table>

    <el-pagination
      class="pagination"
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
  </div>
</template>

<script>
  import { getList, deleteInfo } from '@/oapi/ypns_zhgl/hygl.js'
  export default {
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          homeType: 1,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      // 检索会议时间
      changeApplyPeriod(val) {
        if (val && val.length) {
          this.queryForm.conferenceTimeStartStart = val[1]
          this.queryForm.conferenceTimeEndEnd = val[0]
        }
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
        } = await getList(this.queryForm)
        this.list = tlist || []
        this.total = totalRecord || 0
        this.$emit('HYtotal', totalRecord)
        this.listLoading = false
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
</style>
