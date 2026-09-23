<template>
  <div>
    <el-table border :data="tableData" style="width: 100%">
      <el-table-column label="编号" prop="number" align="center" />
      <el-table-column label="名称" prop="content" align="center" />
      <el-table-column label="操作" align="center">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row.number)">
            结果
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination background layout="prev, pager, next" :total="2" />
    <el-dialog
      title="结果"
      :visible.sync="dialogVisible"
      width="40%"
      :before-close="handleClose"
    >
      <el-table border :data="tableResult1" style="width: 100%">
        <el-table-column label="ID" prop="number" align="center" />
        <el-table-column label="NAME" prop="content" align="center" />
        <el-table-column label="YE" prop="ye" align="center" />
      </el-table>
    </el-dialog>
    <el-dialog
      title="结果"
      :visible.sync="dialogVisible2"
      width="40%"
      :before-close="handleClose"
    >
      <el-table border :data="tableResult2" style="width: 100%">
        <el-table-column label="ID" prop="number" align="center" />
        <el-table-column label="发货数量" prop="content" align="center" />
        <el-table-column label="红绿灯预警" prop="ye" align="center" >
          <div class="divColor"></div>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>
<script>
export default {
  name: 'Download',
  data() {
    return {
      tableData: [
        {
          number: 'CJTZ01',
          content: '企业增加注册资本或者资本公积时是否计提印花税',
        },
        {
          number: 'RULE-01',
          content: '发运数量不能低于50',
        },
      ],
      tableResult1: [
        {
          number: 'CJTZ01',
          content: '企业增加注册资本否计提印花税',
          ye: 0,
        },
        {
          number: 'CJTZ01',
          content: '企业增加注册资本否计提印花税',
          ye: 0,
        },
      ],
      tableResult2: [
        {
          number: 'CJTZ01',
          content: '175',
          ye: '绿色',
        },
      ],
      dialogVisible: false,
      dialogVisible2: false,
    }
  },
  methods: {
    handleDetail(row) {
      if (row == 'CJTZ01') {
        this.dialogVisible = true
      } else {
        this.dialogVisible2 = true
      }
    },
  },
}
</script>
<style scoped>
.divColor{
  width: 20px;
  height: 20px;
  background: linear-gradient(0deg, rgba(7,98,19,1) 0%, rgba(9,121,33,1) 52%);
  margin: 0 auto;
  border-radius: 50%;
}
</style>