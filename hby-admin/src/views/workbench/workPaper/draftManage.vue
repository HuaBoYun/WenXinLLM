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
              placeholder="底稿编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.name"
              clearable
              placeholder="底稿名称"
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
      <vab-query-form-right-panel>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="底稿编号" prop="data">
        <template #default="{ row }">
          <el-button
            style="color: red"
            type="text"
            @click="$refs['DraftInfo'].showEdit()"
          >
            {{ row.data }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="底稿名称" prop="data" />
      <el-table-column align="center" label="审计目标" prop="data" />
      <el-table-column align="center" label="审计对象" prop="data" />
      <el-table-column
        align="center"
        label="拟稿人"
        prop="data"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="拟稿日期"
        prop="data"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="180"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-dropdown style="margin-left: 10px" @command="handleCommand">
            <el-button type="text">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>导入</el-dropdown-item>
              <el-dropdown-item>导出</el-dropdown-item>
              <el-dropdown-item @click="handleDelete(row)">
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
    <DraftView ref="edit" @fetch-data="fetchData" />
    <DraftInfo ref="DraftInfo" />
  </div>
</template>

<script>
  import { getList } from '@/api/systemLog'
  import { doDelete } from '@/api/table'
  import DraftInfo from '@/views/workbench/workPaper/components/DraftInfo'
  import DraftView from '@/views/workbench/workPaper/components/DraftView'
  export default {
    name: 'Plan',
    components: { DraftView, DraftInfo },
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
          pageSize: 10,
        },
        field103Options: [
          {
            label: '未启动',
            value: 1,
          },
          {
            label: '已启动',
            value: 2,
          },
          {
            label: '执行中',
            value: 3,
          },
          {
            label: '已完成',
            value: 4,
          },
        ],
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
    },
  }
</script>
