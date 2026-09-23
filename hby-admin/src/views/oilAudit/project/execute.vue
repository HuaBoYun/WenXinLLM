<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.planYear"
              clearable
              placeholder="计划年度"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.planNumber"
              clearable
              placeholder="计划编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.planName"
              clearable
              placeholder="计划名称"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.projectCode"
              clearable
              placeholder="项目编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.prjoectName"
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
          <el-form-item>
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="计划编号"
        prop="planCode"
        width="100"
      />
      <el-table-column align="center" label="计划名称" prop="data">
        <template #default="{ row }">
          <el-button type="text" @click="handleEditName(row, true)">
            {{ row.planName }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="项目编号"
        prop="projectCode"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="项目名称"
        prop="prjoectName"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row, true)">
            {{ row.prjoectName }}
          </el-button>
        </template>
      </el-table-column>
      <!-- <el-table-column
        align="center"
        label="项目计划"
        prop="data"
        show-overflow-tooltip
      /> -->
      <el-table-column align="center" label="操作" width="240">
        <template #default="{ row }">
          <el-button type="text" @click="handleEditName(row, true)">
            计划一览
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
    <IndexEdit ref="edit" @fetch-data="fetchData" />
    <LookEdit ref="look" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getProjectRunMounth } from '@/oapi/audit/project'

  import { doDelete } from '@/oapi/table'
  import IndexEdit from './components/IndexEdit'
  import LookEdit from './components/LookEdit'

  export default {
    name: 'Download',
    components: { IndexEdit, LookEdit },
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
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getProjectRunMounth(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      handleEditName(row, disabled) {
        this.$refs['look'].showEdit(row, disabled)
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
