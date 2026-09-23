<template>
  <el-card>
    <div class="title">供应商管理</div>
    <el-tabs v-model="activeName">
      <el-tab-pane label="供应商准入" name="first">
        <el-table :data="tableData1" style="width: 100%">
          <el-table-column
            prop="date"
            label="供应商名称"
            width="180"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="name"
            label="供应商类型"
            width="180"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="address"
            label="供应商状态"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="all"
            label="准入日期"
            show-overflow-tooltip
          ></el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="供应商评级" name="second">
        <el-table :data="tableData2" style="width: 100%">
          <el-table-column
            prop="date"
            label="供应商名称"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="name"
            label="总分"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="address"
            label="级别"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="all"
            label="评分日期"
            show-overflow-tooltip
          ></el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script>
  export default {
    name: '',
    data() {
      return {
        activeName: 'first',
        tableData1: [
          {
            date: '上海亘岩集团',
            name: '供应商',
            address: '已准入',
            all: '2019-01-01',
          },
          {
            date: '上海亘岩集团',
            name: '供应商',
            address: '已准入',
            all: '2019-01-01',
          },
          {
            date: '上海亘岩集团',
            name: '供应商',
            address: '已准入',
            all: '2019-01-01',
          },
          {
            date: '上海亘岩集团',
            name: '供应商',
            address: '已准入',
            all: '2019-01-01',
          },
          {
            date: '上海亘岩集团',
            name: '供应商',
            address: '已准入',
            all: '2019-01-01',
          },
          {
            date: '上海亘岩集团',
            name: '供应商',
            address: '已准入',
            all: '2019-01-01',
          },
        ],
        tableData2: [
          {
            date: '上海亘岩集团',
            name: '100',
            address: 'A',
            all: '2019-01-01',
          },
          {
            date: '上海亘岩集团',
            name: '100',
            address: 'A',
            all: '2019-01-01',
          },
          {
            date: '上海亘岩集团',
            name: '100',
            address: 'A',
            all: '2019-01-01',
          },
          {
            date: '上海亘岩集团',
            name: '100',
            address: 'A',
            all: '2019-01-01',
          },
          {
            date: '上海亘岩集团',
            name: '100',
            address: 'A',
            all: '2019-01-01',
          },
          {
            date: '上海亘岩集团',
            name: '100',
            address: 'A',
            all: '2019-01-01',
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
