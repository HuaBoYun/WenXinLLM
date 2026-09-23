<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-table :data="fileList">
      <el-table-column
        align="center"
        label="底稿编号"
        prop="sheetCode"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="底稿名称"
        prop="sheetName"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="审计事项"
        prop="businessAffiliation"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="审计程序"
        prop="suditProcess"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="审计发现"
        prop="auditDiscoverable"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="审批人"
        prop="ejfh"
        show-overflow-tooltip
      />
    </el-table>
  </el-dialog>
</template>

<script>
  import { getAnalyseDetailData } from '@/oapi/audit/analyse'
  export default {
    data() {
      return {
        dialogFormVisible: false,
        title: '详情',
        fileList: [],
      }
    },
    methods: {
      close() {
        this.dialogFormVisible = false
      },
      async show(type, projectid) {
        this.dialogFormVisible = true
        const params = {
          projectid: projectid,
          type: type,
        }
        const res = await getAnalyseDetailData(params)
        this.fileList = res.data.data || []
      },
    },
  }
</script>
