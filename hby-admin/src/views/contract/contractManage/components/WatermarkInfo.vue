<template>
  <el-table :data="list">
    <el-table-column align="center" label="合同编号" prop="contractno" />
    <el-table-column align="center" label="合同名称" prop="contractname" />
    <el-table-column align="center" label="合同类型" prop="contracttype" />
    <el-table-column align="center" label="合同金额" prop="contractmoney" />
    <el-table-column align="center" label="创建日期" prop="createtime" />
  </el-table>
</template>
<script>
  export default {
    name: 'WatermarkInfo',
    props: {
      list: {
        type: Array,
        default: () => [],
      },
    },
  }
</script>
