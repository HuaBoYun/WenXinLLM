 
<template>
  <el-card class="table-responsive">
    <h4 class="title">风险识别与评估</h4>
    <el-input
      class="search"
      placeholder="风险名称"
      prefix-icon="el-icon-search"
    ></el-input>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column label="风险编号" prop="name" align="center" />
      <el-table-column label="风险名称" prop="code" align="center" />
      <el-table-column label="制定机构" prop="level" align="center" />
      <el-table-column label="风险点总数" prop="soure" align="center" />
      <el-table-column label="风险级别" prop="person" align="center" />
    </el-table>
    <el-pagination background layout="prev, pager, next" :total="1000" />
  </el-card>
</template>

<script>
  export default {
    data() {
      return {
        tableData: [],
      }
    },
  }
</script>

<style scoped>
  .table-responsive {
    border: 1px solid #d2d2d2;
  }
  .title {
    margin-left: 10px;
  }
  .search {
    width: 15%;
    margin-bottom: 20px;
    margin-left: 20px;
  }
</style>