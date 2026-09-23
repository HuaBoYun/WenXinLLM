<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-left-panel :span="24">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.planName"
              clearable
              placeholder="计划名称"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.planYear"
              clearable
              placeholder="计划年度"
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
          <el-form-item>
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-left-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="计划年度"
        prop="palnyear"
        width="100"
      />
      <el-table-column align="center" label="计划名称" prop="planname">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row, true)">
            {{ row.planname }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="项目总数"
        prop="totalItem"
        width="100"
      />
      <el-table-column
        align="center"
        label="未实施项目"
        prop="unenforcedItem"
      />
      <el-table-column
        align="center"
        label="进行项目"
        prop="condectItem"
        width="100"
      />
      <el-table-column align="center" label="完成项目" prop="completeItem" />
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <LookEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { auditPlanListLook } from '@/oapi/audit/plan'
  import LookEdit from './components/LookEdit'

  export default {
    name: 'Download',
    components: { LookEdit },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
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
        let result = await auditPlanListLook(this.queryForm)
        this.list = result.data.pageInfo.tlist
        this.total = result.data.pageInfo.totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
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
