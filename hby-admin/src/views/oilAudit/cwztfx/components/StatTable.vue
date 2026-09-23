<template>
  <div class="stat-table-wrapper">
    <div class="table-title">审计项目/审计任务分类统计表</div>
    <el-table
      :data="data"
      style="width: 100%"
      :header-row-class-name="() => 'hide-default-header'"
      :cell-style="cellStyle"
      :row-class-name="rowClassName"
      border
    >
      <el-table-column
        prop="planUnit"
        label="实际计划实施单位"
        align="center"
      />
      <el-table-column prop="execUnit" label="实际实施单位" align="center" />
      <el-table-column label="项目/审计任务" align="center">
        <el-table-column prop="project" label="项目" align="center" />
        <el-table-column prop="task" label="任务" align="center" />
        <el-table-column prop="total" label="总计" align="center" />
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    props: ['data'],
    methods: {
      cellStyle({ row, column, rowIndex }) {
        return {
          color: '#222',
          fontSize: '16px',
          background: rowIndex % 2 === 0 ? '#fff' : '#f8f9e9',
          borderColor: '#eaf6fb',
          textAlign: 'center',
          padding: '0',
        }
      },
      rowClassName({ rowIndex }) {
        return rowIndex % 2 === 0 ? 'even-row' : 'odd-row'
      },
    },
  }
</script>

<style scoped>
  .stat-table-wrapper {
    margin-top: 16px;
  }
  .table-title {
    font-size: 20px;
    font-weight: bold;
    color: #444;
    margin-bottom: 8px;
    text-align: left;
  }
  .el-table .hide-default-header {
    display: none;
  }
  .group-header,
  .sub-header {
    background: #3b6e8c !important;
    color: #fff !important;
    font-weight: bold;
    font-size: 18px;
    border-color: #eaf6fb !important;
    height: 48px;
    padding: 0;
    text-align: center;
    position: relative;
  }
  .sum-header {
    vertical-align: bottom;
  }
  .header-text {
    display: flex;
    justify-content: center;
    align-items: flex-end;
    height: 32px;
  }
  .sum-sign {
    color: #7ee36b;
    font-size: 18px;
    font-weight: bold;
    line-height: 1;
    position: absolute;
    right: 12px;
    bottom: 6px;
  }
  .sum-arrow {
    color: #7ee36b;
    font-size: 14px;
    margin-left: 2px;
  }
  .el-table .even-row {
    background: #fff;
  }
  .el-table .odd-row {
    background: #f8f9e9;
  }
  .el-table td {
    color: #222;
    font-size: 16px;
    border-color: #eaf6fb;
    text-align: center;
    padding: 0;
  }
</style>
