<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-left-panel :span="1" />
      <vab-query-form-right-panel :span="23">
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
          v-if="hasAuth('ZYKGLadd')"
        >
          新建资源库
        </el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="资源库名称" prop="repoName" />
      <el-table-column align="center" label="资源库类型" prop="type">
        <template #default="{ row }">
          {{ row.type == 'File' ? '文件资源库' : '数据库资源库' }}
          <!-- {{ row.type }} -->
        </template>
      </el-table-column>
      <el-table-column align="center" label="文件资源库路径" prop="baseDir" />
      <!-- <el-table-column
        align="center"
        label="数据库主机名或IP地址"
        prop="dbHost"
      />
      <el-table-column align="center" label="资源库数据库名称" prop="dbName" /> -->
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
              <el-dropdown-item
                @click.native="handleEdit(row)"
                v-if="hasAuth('ZYKGLedit')"
              >
                编辑
              </el-dropdown-item>
              <el-dropdown-item
                @click.native="handleDelete(row)"
                v-if="hasAuth('ZYKGLdelete')"
              >
                删除
              </el-dropdown-item>
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
    <NewLibraryResourceLibrary ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { getAccountCate } from '@/api/workbench/accountData/accountData'
  import { getList } from '@/api/systemLog'
  import { doDelete } from '@/api/table'
  import { getRepositoryList, deleteRepositoryList } from '@/api/sjzt/etl/etl'
  import NewLibraryResourceLibrary from './components/newLibraryResourceLibrary.vue'

  export default {
    name: 'resourcePoolManagement',
    components: { NewLibraryResourceLibrary },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          // pageNo: 1,
          // pageSize: 20,
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
        this.listLoading = true
        const { rows, total } = await getRepositoryList(this.queryForm)

        this.list = rows
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit('edit', row)
      },
      handleCommand(row) {},
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteRepositoryList({ ids: row.id })
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
<!-- <style scoped lang="scss">
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
</style> -->
