<template>
  <div class="system-log-container">
    <vab-query-form-right-panel :span="24">
      <el-button
        type="success"
        @click="handleAdd"
        style="margin-bottom: 10px; margin-right: 10px"
        v-if="hasAuth('DSCLadd')"
      >
        新增执行策略
      </el-button>
    </vab-query-form-right-panel>
    <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
      <el-table-column align="center" label="执行策略名称" prop="data" />

      <el-table-column align="center" label="cron表达式" prop="data" />
      <el-table-column align="center" label="创建时间" prop="data"  sortable="custom"/>
      <el-table-column align="center" label="更新时间" prop="data"  sortable="custom"/>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button
            type="text"
            @click="handleEdit(row)"
            v-if="hasAuth('DSCLedit')"
          >
            编辑
          </el-button>
          <el-button
            type="text"
            @click="handleDelete(row)"
            v-if="hasAuth('DSCLdelete')"
          >
            删除
          </el-button>
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
    <ExecutionStrategy ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getAccountCate } from '@/api/workbench/accountData/accountData'
  import { getList } from '@/api/systemLog'
  import { doDelete } from '@/api/table'
  import ExecutionStrategy from './components/executionStrategy'
  import { hasAuth } from '@/utils'

  export default {
    name: 'timingStrategy',
    components: { ExecutionStrategy },
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
        sortFields: '',
        sortFlag: 'asc',
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async sortChange(column) {
        let { order, prop } = column
        let p = prop
        this.sortFields = p || ''
        if (order === 'ascending') {
          this.sortFlag = 'asc'
        } else if (order === 'descending') {
          this.sortFlag = 'desc'
        } else {
          this.sortFlag = ''
        }
        await this.fetchData()
      },
      goBack() {
        this.$router.back(-1)
      },
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
        // this.listLoading = true
        // const {
        //   data: { list, total },
        // } = await getAccountCate({...this.queryForm,
        //  sortFields: this.sortFields,
         // sortFlag: this.sortFlag})
        // this.list = list
        // this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
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
