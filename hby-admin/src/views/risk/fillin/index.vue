<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form
          ref="form"
          checkable
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.name"
              clearable
              placeholder="风险编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.name"
              clearable
              placeholder="一类风险"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.name"
              clearable
              placeholder="二类风险"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.name"
              clearable
              placeholder="三类风险"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.name"
              clearable
              placeholder="涉及资金（万元）"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.name"
              clearable
              placeholder="单位名称"
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
            <el-button native-type="submit" @click="fetchData">重置</el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
      <vab-query-form-left-panel>
        <span></span>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column type="selection" width="55" />
      <el-table-column
        align="center"
        label="风险编号"
        prop="data"
        width="100"
      />
      <el-table-column
        align="center"
        label="一类风险"
        prop="data"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="二类风险"
        prop="data"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        align="center"
        label="三类风险"
        prop="data"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        align="center"
        label="涉及资金（万元）"
        prop="data"
        show-overflow-tooltip
        width="150"
      />
      <el-table-column
        align="center"
        label="单位名称"
        prop="data"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column align="center" label="创建时间" prop="data" />
      <el-table-column align="center" label="审批状态" prop="data" />
      <el-table-column align="center" label="操作">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit2(row)">办理</el-button>
          <el-dropdown style="margin-left: 10px">
            <el-button type="text">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>提交审批</el-dropdown-item>
              <el-dropdown-item>修改</el-dropdown-item>
              <el-dropdown-item>删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
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
    <FillinEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getList } from '@/api/systemLog'
  import { doDelete } from '@/api/table'
  import FillinEdit from './components/FillinEdit'

  export default {
    name: 'Fillin',
    components: { FillinEdit },
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
