<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-left-panel>
        <span></span>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button type="success" @click="handleEdit">新建</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="报告名称" prop="data">
        <template #default="{ row }">
          <el-button type="text" @click="handleRead(row)">
            {{ row.data }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="报告时间" prop="data" />
      <el-table-column align="center" label="报告类型" prop="data" />
      <el-table-column align="center" label="报告方式" prop="data" />
      <el-table-column align="center" label="操作" width="120">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
          <el-button type="text">导出</el-button>
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
    <CustomEdit ref="edit" />
    <CustomRead ref="read" />
  </div>
</template>

<script>
  import { getList } from '@/api/systemLog'
  import { doDelete } from '@/api/table'
  import CustomEdit from './components/CustomEdit.vue'
  import CustomRead from './components/CustomRead.vue'

  export default {
    name: 'CustomReportList',
    components: { CustomEdit, CustomRead },
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
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleRead(row) {
        this.$refs['read'].showRead(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
    },
  }
</script>
