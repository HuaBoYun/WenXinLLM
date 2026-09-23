<template>
  <el-dialog
    title="模板"
    :visible.sync="dialogVisibleUE"
    width="1200px"
    :append-to-body="true"
    :close-on-click-modal="false"
    :modal-append-to-body="false"
  >
    <el-tabs v-model="activeName">
      <el-tab-pane label="内控评价汇总" name="first">
        <EvaluationManagement :isUEditor="true" />
      </el-tab-pane>
      <el-tab-pane label="内控测试汇总" name="second">
        <reportList :isUEditor="true" />
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>
<script>
  import reportList from '@/views/internal/internalTest/results.vue'
  import EvaluationManagement from '@/views/internal/evaluationManagement/result.vue'
  export default {
    components: { reportList, EvaluationManagement },
    data() {
      return { dialogVisibleUE: false, activeName: 'first' }
    },
    mounted() {},
    methods: {
      showDialog() {
        this.dialogVisibleUE = true
      },
    },
  }
</script>
<style lang="scss" scoped></style>
