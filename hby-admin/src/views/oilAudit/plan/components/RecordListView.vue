<template>
  <div class="system-log-container">
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="业务单元" prop="data">
        <template #default="{ row }">
          <el-button type="text" @click="$refs['recordListInfo'].showEdit()">
            {{ row.data }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="风险描述"
        prop="data"
        width="100"
      />
      <el-table-column align="center" label="控制措施" prop="data" />
      <el-table-column
        align="center"
        label="审计程序"
        prop="data"
        width="100"
      />
      <el-table-column align="center" label="所需资料" prop="data" />
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
    <record-list-info ref="recordListInfo" />
  </div>
</template>

<script>
  import { getList } from '@/oapi/systemLog'
  import RecordListInfo from './RecordListInfo'

  export default {
    name: 'Download',
    components: { RecordListInfo },
    data() {
      return {
        list: [],
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
      async fetchData() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await getList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
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
