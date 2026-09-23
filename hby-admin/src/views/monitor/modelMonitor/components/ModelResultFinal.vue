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
      <el-table-column align="center" label="步骤" prop="step" />
      <el-table-column align="center" label="结果">
        <template #default="{ $index }">
          <span
            style="color: red; cursor: pointer"
            @click="openStepInfo($index)"
          >
            结果
          </span>
        </template>
      </el-table-column>
    </el-table>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getModelResultSteps } from '@/api/monitor/model'

  export default {
    name: 'ModelResultSteps',
    data() {
      return {
        title: '预警详细',
        dialogFormVisible: false,
        queryForm: {
          modelId: '',
          soluId: '',
          source: 0,
        },
        list: [],
        listLoading: false,
      }
    },
    methods: {
      showEdit(modelid) {
        this.dialogFormVisible = true
        this.queryForm.modelId = modelid
        this.fetchData()
      },
      close() {
        this.dialogFormVisible = false
      },
      async fetchData() {
        this.listLoading = true
        const { code, data } = await getModelResultSteps(this.queryForm)
        if (code === 200) {
          const { list } = data
          this.list = list.map((v) => ({ step: v }))
        }
        this.listLoading = false
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
