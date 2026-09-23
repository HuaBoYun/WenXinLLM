<template>
  <!-- 报表数据 -->
  <div class="system-log-container">
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="账套名称">
        <template #default="{ row }">
          <el-button type="text" @click="openTable(row)">
            {{ row.tableName }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="单位名称"
        prop="orgName"
      ></el-table-column>
      <el-table-column
        align="center"
        label="会计年度"
        prop="bookYear"
      ></el-table-column>
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
  </div>
</template>

<script>
  export default {
    name: 'Consult',
    components: {},
    data() {
      return {
        bookInfo: {
          bookYear: '',
          orgName: '',
        },
        list: [
          {
            tableName: '资产负债表',
          },
          {
            tableName: '利润表',
          },
        ],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNo: 1,
          pageSize: 20,
        },
      }
    },
    async mounted() {
      const bookInfoStr = localStorage.getItem('bookInfo')
      if (bookInfoStr) {
        this.bookInfo = JSON.parse(bookInfoStr)
      }
      this.fetchData()
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      fetchData() {
        this.list.forEach((key) => {
          key.bookYear = this.bookInfo.bookYear
          key.orgName = this.bookInfo.orgName
        })
        this.listLoading = false
      },
      openTable(row) {
        if (row.tableName == '利润表') {
          this.$router.push('/shenji/statement/profitSheet')
        } else {
          this.$router.push('/shenji/statement/balanceSheet')
        }
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
    },
  }
</script>
