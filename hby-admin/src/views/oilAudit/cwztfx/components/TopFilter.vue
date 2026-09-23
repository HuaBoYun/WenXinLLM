<template>
  <div class="top-filter-row">
    <div class="filter-item">
      <span class="filter-label">年度</span>
      <el-date-picker
        v-model="year"
        type="year"
        placeholder="选择年"
        class="filter-date-picker"
        @change="emitChange"
      ></el-date-picker>
    </div>
    <div class="filter-item">
      <span class="filter-label">实施单位</span>
      <el-select
        v-model="unit"
        placeholder="请选择"
        class="filter-select"
        popper-class="filter-select-popper"
        :popper-append-to-body="false"
        clearable
        style="width: 100%"
      >
        <el-option label="全部" value="" />
        <el-option label="单位A" value="A" />
        <el-option label="单位B" value="B" />
      </el-select>
    </div>
    <div class="filter-item">
      <span class="filter-label">项目进度</span>
      <el-select
        v-model="progress"
        placeholder="请选择"
        @change="emitChange"
        class="filter-select"
        popper-class="filter-select-popper"
        :popper-append-to-body="false"
        clearable
        style="width: 100%"
      >
        <el-option label="全部" value="" />
        <el-option label="未启动" value="0" />
        <el-option label="已完成" value="1" />
      </el-select>
    </div>
    <div class="filter-item filter-item-right">
      <span class="filter-label">审计项目或任务数</span>
      <span class="filter-title-num">162</span>
    </div>
  </div>
</template>

<script>
  import { formatYear } from '@/utils'
  export default {
    data() {
      return {
        year: formatYear(new Date()).toString(), // 动态获取当前年份的日期对象，如 Mon Jan 01 2024 00:00:00 GMT+0800
        years: ['2022', '2021', '2020'],
        unit: '',
        progress: '',
      }
    },
    methods: {
      emitChange() {
        this.$emit('filter-change', {
          year: this.year,
          unit: this.unit,
          progress: this.progress,
        })
      },
    },
  }
</script>

<style scoped>
  .top-filter-row {
    display: flex;
    width: 100%;
    gap: 6px;
    margin-bottom: 8px;
  }
  .filter-item {
    display: flex;
    align-items: center;
    background: #6ea2c9;
    border-radius: 2px;
    padding: 0 8px;
    height: 34px;
    font-weight: bold;
    font-size: 18px;
    color: #fff;
    flex: 1 1 0;
  }
  .filter-label {
    margin-right: 10px;
    font-size: 18px;
    color: #fff;
    white-space: nowrap;
  }
  .filter-select {
    width: 110px;
    background: transparent;
    color: #fff;
    border: none;
    text-align: center;
  }
  .filter-select >>> .el-input__inner {
    background: transparent !important;
    color: #fff !important;
    border: none !important;
    height: 32px;
    font-size: 18px;
    font-weight: bold;
    text-align: center;
    box-shadow: none !important;
    padding-left: 0;
    padding-right: 0;
  }
  .filter-select >>> .el-input__icon {
    color: #fff !important;
  }
  .filter-select-popper {
    background: #6ea2c9 !important;
    color: #fff !important;
  }
  .filter-date-picker {
    background: transparent !important;
  }
  .filter-date-picker >>> .el-input__inner {
    background: transparent !important;
    color: #fff !important;
    border: none !important;
    height: 32px;
    font-size: 18px;
    font-weight: bold;
    text-align: center;
    box-shadow: none !important;
    padding-left: 0;
    padding-right: 0;
  }
  .filter-date-picker >>> .el-input__icon {
    color: #fff !important;
  }
  .filter-date-picker >>> .el-input__suffix {
    color: #fff !important;
  }
  .filter-item-right {
    min-width: 220px;
    justify-content: center;
    flex: 1 1 0;
  }
  .filter-title-num {
    color: #ffe600;
    font-size: 26px;
    margin-left: 8px;
    font-weight: bold;
  }
</style>
