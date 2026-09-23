<!--
 * @Date: 2022-01-21 09:06:33
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-28 21:17:56
 * @FilePath: /hb-admin/src/views/setting/system/components/LcdyView/FlowHistory.vue
-->
<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogTableVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="流程实例Id"
        prop="id"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="businessKey"
        prop="businessKey"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="流程定义Id"
        prop="processDefinitionId"
      />
      <el-table-column align="center" label="开始时间" prop="startTime" />
      <el-table-column align="center" label="结束时间" prop="endTime" />
      <el-table-column
        align="center"
        label="流程持续时间"
        prop="durationInMillis"
      />
      <el-table-column
        align="center"
        label="开始流程Id"
        prop="startActivityId"
      />
      <el-table-column align="center" label="结束流程Id" prop="endActivityId" />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="200"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
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
  </el-dialog>
</template>

<script>
  import { deleteViewHistory, getViewHistoryList } from '@/api/setting/system'

  export default {
    name: 'LcdyView',
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '流程定义',
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
          data: { tlist, totalRecord },
        } = await getViewHistoryList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteViewHistory({ id: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
    },
  }
</script>
