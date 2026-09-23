<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    append-to-body
  >
    <el-col :span="24">
      <el-divider>体系权重得分</el-divider>
    </el-col>
    <el-table :data="tableData1">
      <el-table-column align="center" label="类别" prop="CATNAME1" />
      <el-table-column align="center" label="	权重" prop="CATW" />
    </el-table>
    <el-col :span="24">
      <div style="margin-top: 60px">
        <el-divider>要素得分明细</el-divider>
      </div>
    </el-col>
    <el-table :data="tableData2">
      <el-table-column align="center" label="类别" prop="CATNAME1" />
      <el-table-column align="center" label="要素名称" prop="ELEMENTNAME" />
      <el-table-column
        align="center"
        label="标准分"
        prop="STANDARDSCORE"
        width="100"
      />
      <el-table-column align="center" label="合规要点" prop="AUDITPOINT" />
    </el-table>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getTemplateModalTableDate } from '@/api/internal/project'
  export default {
    name: 'EvaluateModel',
    data() {
      return {
        tableData1: [],
        tableData2: [],
        title: '评价模板',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      async showEdit(id) {
        this.dialogFormVisible = true
        let res = await getTemplateModalTableDate({ tmplId: +id })
        this.tableData1 = res.data.tixiquanzhongs
        this.tableData2 = res.data.yaosudefens
      },
      close() {
        this.tableData1 = []
        this.tableData2 = []
        this.dialogFormVisible = false
      },
    },
  }
</script>
