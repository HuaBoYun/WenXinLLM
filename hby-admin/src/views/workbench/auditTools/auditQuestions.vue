<template>
  <div class="system-log-container">
    <el-card shadow="never">
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-tooltip
            class="item"
            effect="dark"
            content="表格筛选"
            placement="top"
          >
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
          </el-tooltip>
          <el-button type="success" @click="handleAdd">新建</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column width="1" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column align="center" v-if="item.name === '统计类型说明'" label="统计类型说明" prop="auditType" />
          <el-table-column align="center" v-if="item.name === '统计类型版本'" label="统计类型版本" prop="version" />
          <el-table-column align="center" v-if="item.name === '状态'" :formatter="formatStatus" label="状态" prop="status" />
        </div>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <AuditQuestionsInfo ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getNbsjQuestionTypeListPage,
    sjwttype_del,
  } from '@/api/workbench/auditTools'
  import AuditQuestionsInfo from '@/views/workbench/auditTools/components/auditQuestionsInfo'
  import { searchTableMixis } from '@/mixis/index'
  import filterTable from '@/components/filterTable'

  export default {
    name: 'Consult',
    mixins: [searchTableMixis],
    components: { filterTable, AuditQuestionsInfo },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 20,
        },
        // 筛选列表配置
        filedAll: [
          { name: '统计类型说明' },
          { name: '统计类型版本' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        localKey: 'workbench-auditTools-auditQuestions-search',
        tableKey: 'workbench-auditTools-auditQuestions-list',
      }
    },
    created() {
      this.fetchData()
      this.initTable()
    },
    methods: {
      formatStatus(row) {
        return row.status === 1 ? '禁用' : '正常'
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
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getNbsjQuestionTypeListPage(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        console.dir(row)
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await sjwttype_del({ typeId: row.typeId })
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
      handleCommand(command) {
        switch (command) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit()
            break
        }
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
</style>