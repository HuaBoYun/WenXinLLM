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
              placeholder="疑点编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.name"
              clearable
              placeholder="疑点名称"
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
      <el-table-column align="center" label="疑点编号" prop="data">
        <template #default="{ row }">
          <el-button
            style="color: red"
            type="text"
            @click="$refs['DubaInfo'].showEdit()"
          >
            {{ row.data }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="疑点名称" prop="data" />
      <el-table-column align="center" label="	疑点描述" prop="data" />
      <el-table-column align="center" label="测试结果" prop="data" />
      <el-table-column
        align="center"
        label="编制人"
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
              <el-dropdown-item @click="handleDelete(row)">
                删除
              </el-dropdown-item>
              <el-dropdown-item @click.native="$refs['Send'].showEdit()">
                发送至底稿
              </el-dropdown-item>
              <el-dropdown-item @click.native="$refs['Send2'].showEdit()">
                发送至底稿附件
              </el-dropdown-item>
              <el-dropdown-item @click.native="$refs['Send3'].showEdit()">
                发送至疑点
              </el-dropdown-item>
              <el-dropdown-item @click.native="$refs['Send4'].showEdit()">
                发送至缺陷
              </el-dropdown-item>
              <el-dropdown-item @click.native="$refs['Send5'].showEdit()">
                发送至风险
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
    <DubaView ref="edit" @fetch-data="fetchData" />
    <DubaInfo ref="DubaInfo" />
    <Send ref="Send" />
    <Send2 ref="Send2" />
    <Send3 ref="Send3" />
    <Send4 ref="Send4" />
    <Send5 ref="Send5" />
  </div>
</template>

<script>
  import { getList } from '@/api/systemLog'
  import { doDelete } from '@/api/table'
  import DubaInfo from '@/views/workbench/workPaper/components/DubaInfo'
  import DubaView from '@/views/workbench/workPaper/components/DubaView'
  import Send from '@/views/internal/components/Send'
  import Send2 from '@/views/internal/components/Send2'
  import Send3 from '@/views/internal/components/Send3'
  import Send4 from '@/views/internal/components/Send4'
  import Send5 from '@/views/internal/components/Send5'
  export default {
    name: 'DubaManag',
    components: { DubaView, DubaInfo, Send, Send2, Send3, Send4, Send5 },
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
