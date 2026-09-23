<template>
  <div class="system-log-container">
    <vab-query-form-right-panel :span="24">
      <!-- <el-tooltip class="item" effect="dark" content="表格筛选" placement="top">
        <el-popover placement="right" trigger="click">
          <filter-table
            :list="filedAll"
            :name="tableKey"
            @updateTableShow="initTable"
          />
          <el-button
            slot="reference"
            icon="el-icon-s-grid"
            class="biaoge"
            style="margin-bottom: 10px; margin-right: 10px"
          ></el-button>
        </el-popover>
      </el-tooltip> -->
      <el-button
        type="success"
        @click="handleAdd"
        style="margin-bottom: 10px; margin-right: 10px"
        v-if="hasAuth('ETLJKGLadd')"
      >
        新建
      </el-button>
    </vab-query-form-right-panel>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="资源库名称" prop="data" />
      <el-table-column align="center" label="资源库类型" prop="data" />
      <el-table-column align="center" label="文件资源库路径" prop="data" />
      <el-table-column
        align="center"
        label="数据库主机名或IP地址"
        prop="data"
      />
      <el-table-column align="center" label="资源库数据库名称" prop="data" />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-dropdown style="margin-left: 10px" @command="handleCommand(row)">
            <el-button type="text">操作</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>aaa</el-dropdown-item>
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
    <LcdyEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getAccountCate } from '@/api/workbench/accountData/accountData'
  import { getList } from '@/api/systemLog'
  import { doDelete } from '@/api/table'
  import LcdyEdit from '@/views/setting/system/components/LcdyEdit'
  import { hasAuth } from '@/utils'

  export default {
    name: 'monitoringManagement',
    components: { LcdyEdit },
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
        // } = await getAccountCate(this.queryForm)
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
