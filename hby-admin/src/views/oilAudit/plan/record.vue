<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-left-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.name"
              clearable
              placeholder="项目名称"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="fetchData"
            >
              查询
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-left-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="项目名称" prop="data">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit('recordInfo', row)">
            {{ row.data }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="项目经理"
        prop="data"
        width="100"
      />
      <el-table-column align="center" label="被审计单位" prop="data" />
      <el-table-column
        align="center"
        label="项目类型"
        prop="data"
        width="100"
      />
      <el-table-column align="center" label="项目来源" prop="data" />
      <el-table-column
        align="center"
        label="开始时间"
        prop="data"
        width="100"
      />
      <el-table-column align="center" label="实际开始时间" prop="data" />
      <el-table-column align="center" label="实际结束时间" prop="data" />
      <el-table-column align="center" label="状态" prop="data" />
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
    <RecordInfo ref="recordInfo" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getList } from '@/oapi/systemLog'
  import RecordInfo from './components/RecordInfo'

  export default {
    name: 'Download',
    components: { RecordInfo },
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
      handleEdit(type, row) {
        this.$refs[type].showEdit(row)
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
