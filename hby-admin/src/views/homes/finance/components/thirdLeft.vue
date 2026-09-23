<template>
  <el-card>
    <div class="title">各部门预算执行情况</div>
    <el-table :data="tableData1" style="width: 100%">
      <el-table-column prop="depart" label="部门"></el-table-column>
      <el-table-column prop="zys" label="总预算"></el-table-column>
      <el-table-column prop="yy" label="已用"></el-table-column>
      <el-table-column prop="dj" label="冻结"></el-table-column>
      <el-table-column prop="ky" label="可用"></el-table-column>
      <el-table-column prop="bl" label="比率">
        <template #default="{ row }">
          <el-progress :percentage="row.bl"></el-progress>
        </template>
      </el-table-column>
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
            depart: '总经办',
            zys: '1000元',
            yy: '100元',
            dj: '100元',
            ky: '100元',
            bl: '10',
          },
          {
            depart: '总经办',
            zys: '1000元',
            yy: '100元',
            dj: '100元',
            ky: '100元',
            bl: '20',
          },
          {
            depart: '总经办',
            zys: '1000元',
            yy: '100元',
            dj: '100元',
            ky: '100元',
            bl: '30',
          },
          {
            depart: '总经办',
            zys: '1000元',
            yy: '100元',
            dj: '100元',
            ky: '100元',
            bl: '40',
          },
          {
            depart: '总经办',
            zys: '1000元',
            yy: '100元',
            dj: '100元',
            ky: '100元',
            bl: '50',
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
