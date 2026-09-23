<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-table :data="list">
      <el-table-column align="center" label="步骤编号" prop="stepno" />
      <el-table-column align="center" label="步骤标题" prop="steptitle" />
      <el-table-column align="center" label="步骤内容" prop="stepcontent" />
    </el-table>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>
<script>
  import { getResultData } from '@/api/setting/org'
  export default {
    data() {
      return {
        dialogFormVisible: false,
        title: '结果',
        list: [],
      }
    },

    methods: {
      async show(row) {
        this.dialogFormVisible = true
        this.fentchData(row.stepid)
      },
      async fentchData(id) {
        const {
          data: { data },
        } = await getResultData({ stepId: id })
        this.list = data
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
