<template>
  <el-card>
    <div class="title">发票管理</div>
    <el-table :data="tableData1" style="width: 100%; height: 355px">
      <el-table-column prop="num" label="发票号码"></el-table-column>
      <el-table-column prop="zys" label="发票状态"></el-table-column>
      <el-table-column prop="yy" label="价税合计"></el-table-column>
      <el-table-column prop="dj" label="销售方"></el-table-column>
      <el-table-column prop="ky" label="发票真伪"></el-table-column>
      <el-table-column prop="bl" label="开票日期"></el-table-column>
      <el-table-column prop="type" label="发票类型"></el-table-column>
    </el-table>
  </el-card>
</template>

<script>
  export default {
    name: '',
    components: {},
    data() {
      return {
        tableData1: [
          {
            num: '123456789',
            zys: '已开票',
            yy: '￥1000.00',
            dj: '上海市',
            ky: '真票',
            bl: '2019-01-01',
            type: '增值税专用发票',
          },
          {
            num: '123456789',
            zys: '已开票',
            yy: '￥1000.00',
            dj: '上海市',
            ky: '真票',
            bl: '2019-01-01',
            type: '增值税专用发票',
          },
          {
            num: '123456789',
            zys: '已开票',
            yy: '￥1000.00',
            dj: '上海市',
            ky: '真票',
            bl: '2019-01-01',
            type: '增值税专用发票',
          },
          {
            num: '123456789',
            zys: '已开票',
            yy: '￥1000.00',
            dj: '上海市',
            ky: '真票',
            bl: '2019-01-01',
            type: '增值税专用发票',
          },
        ],
      }
    },
  }
</script>

<style scoped>
  .title {
    font-size: 20px;
    font-weight: 700;
    color: #333;
    padding: 10px 0;
  }
</style>
