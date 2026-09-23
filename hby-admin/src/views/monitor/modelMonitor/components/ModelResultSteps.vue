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
    <model-result-final ref="modelResultFinal" />
  </el-dialog>
</template>

<script>
  import { getModelResultSteps } from '@/api/monitor/model'
  import ModelResultFinal from '../components/ModelResultFinal.vue'

  export default {
    name: 'ModelResultSteps',
    components: { ModelResultFinal },
    data() {
      return {
        title: '模型管理 - 预警结果',
        dialogFormVisible: false,
        queryForm: {
          modelId: '',
          soluId: '',
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
      openStepInfo(index) {
        const { modelId } = this.list[index]
        this.$refs['modelResultFinal'].showEdit(modelId)
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
