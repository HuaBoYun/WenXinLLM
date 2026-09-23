<!--
 * @Date: 2022-02-28 18:42:59
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-25 18:30:10
 * @FilePath: /hb-admin/src/views/setting/system/components/VersionHistory.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogTableVisible"
    width="500px"
    :close-on-click-modal="false"
    @close="close"
  >
    <el-table v-loading="listLoading" :data="list">
      <el-table-column type="selection" width="55" />
      <el-table-column
        align="center"
        label="流程编号"
        prop="flowid"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="流程名称"
        prop="flowname"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="机构" prop="comName" />
      <el-table-column align="center" label="主责部门" prop="depName" />
      <el-table-column align="center" label="创建时间" prop="createtime" />
      <el-table-column align="center" label="关联流程ID" prop="settingid" />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="100"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleViewHistory(row)">
            历史版本
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
  </el-dialog>
</template>

<script>
  import { getFlowManageListHistory } from '@/api/setting/system'
  export default {
    name: 'VersionHistory',
    data() {
      return {
        title: '查看历史版本',
        dialogTableVisible: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        flowid: undefined,
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
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getFlowManageListHistory({ flowid: this.flowid })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      show(row) {
        this.dialogTableVisible = true
        this.flowid = row.flowid
        this.fetchData()
      },
      close() {
        this.dialogTableVisible = false
      },
    },
  }
</script>
