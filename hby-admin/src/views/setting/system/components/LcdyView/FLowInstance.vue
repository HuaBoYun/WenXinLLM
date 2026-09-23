<!--
 * @Date: 2022-01-21 09:06:33
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-28 17:20:01
 * @FilePath: /hb-admin/src/views/setting/system/components/LcdyView/FLowInstance.vue
-->
<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogTableVisible"
    width="1000px"
    :close-on-click-modal="false"
    @close="close"
  >
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="Id"
        prop="id"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="流程Id"
        prop="processDefinitionId"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="businessKey" prop="businessKey" />
      <el-table-column align="center" label="是否结束" prop="isEnded">
        <template #default="{ row }">{{ row.isEnded ? '是' : '否' }}</template>
      </el-table-column>
      <el-table-column align="center" label="是否终止" prop="isSuspended">
        <template #default="{ row }">
          {{ row.isSuspended ? '是' : '否' }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="200"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleTacking(row)">
            流程跟踪
          </el-button>
        </template>
      </el-table-column>
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
    <FlowDiagram ref="diagram" />
  </el-dialog>
</template>

<script>
  import { getViewInstanceList } from '@/api/setting/system'
  import FlowDiagram from './FlowDiagram.vue'
  export default {
    name: 'LcdyView',
    components: { FlowDiagram },
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '流程实例',
        dialogTableVisible: false,
        queryForm: {
          processDefinitionId: undefined,
          module: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {},
    methods: {
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      show(row, processSetting) {
        this.queryForm.processDefinitionId = row.id
        this.queryForm.module = processSetting.module
        this.dialogTableVisible = true
        this.fetchData()
      },
      close() {
        this.dialogTableVisible = false
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getViewInstanceList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleTacking(row) {
        this.$refs['diagram'].show(row)
      },
    },
  }
</script>
