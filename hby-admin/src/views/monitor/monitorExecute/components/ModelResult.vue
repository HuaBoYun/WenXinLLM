<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="执行人" prop="REALNAME" />
      <el-table-column align="center" label="执行时间" prop="SAVETIMEFORMAT" />
      <el-table-column align="center" label="详细结果" prop="createdate">
        <template #default="{ $index }">
          <span style="color: red; cursor: pointer" @click="openSteps($index)">
            结果
          </span>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
    />
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
    <!-- <model-result-steps ref="modelResultSteps" /> -->
  </el-dialog>
</template>

<script>
  import { getModelResult } from '@/api/monitor/model'
  import { resultmgmtyj } from '@/api/monitor/exectue/index'
  // import ModelResultSteps from '../components/ModelResultSteps.vue'

  export default {
    name: 'ModelResult',
    // components: { ModelResultSteps },
    data() {
      return {
        title: '模型管理 - 预警结果',
        dialogFormVisible: false,
        layout: 'total, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          orgId: '',
          soultionId: '',
          source: '',
          pageNumber: 1,
          pageSize: 20,
        },
        list: [],
        listLoading: false,
      }
    },
    methods: {
      showEdit(row, orgId) {
        this.queryForm.orgId = orgId
        ;(this.queryForm.soultionId = row.SOLUTIONID),
          (this.queryForm.source = row.STAFFID)
        this.dialogFormVisible = true
        this.fetchData()
      },
      close() {
        this.dialogFormVisible = false
      },
      async fetchData() {
        this.listLoading = true
        const { code, data } = await resultmgmtyj(this.queryForm)
        if (code === 200) {
          const {
            pageBean: { records, total },
          } = data
          this.total = total
          this.list = records
        }
        this.listLoading = false
      },
      openSteps(index) {
        const { RESULTID } = this.list[index]
        this.$refs['modelResultSteps'].showEdit(RESULTID)
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
