<template>
  <div class="time-picker-container">
    <el-card shadow="hover">
      <template #header>
        <span>固定时间点</span>
      </template>
      <el-time-select
        v-model="value"
        :picker-options="{
          start: '08:30',
          step: '00:15',
          end: '18:30',
        }"
        placeholder="选择时间"
      />
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>任意时间点</span>
      </template>
      <el-time-picker
        v-model="value1"
        :picker-options="{
          selectableRange: '18:30:00 - 20:30:00',
        }"
        placeholder="任意时间点"
      />
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>固定时间范围</span>
      </template>
      <el-time-select
        v-model="startTime"
        :picker-options="{
          start: '08:30',
          step: '00:15',
          end: '18:30',
        }"
        placeholder="起始时间"
        style="margin-right: 10px"
      />
      <el-time-select
        v-model="endTime"
        :picker-options="{
          start: '08:30',
          step: '00:15',
          end: '18:30',
          minTime: startTime,
        }"
        placeholder="结束时间"
      />
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'Timepicker',
    data() {
      return {
        value: '',
        value1: new Date(2016, 9, 10, 18, 40),
        startTime: '',
        endTime: '',
      }
    },
  }
</script>

<style lang="scss" scoped>
  .time-picker-container {
    padding: 0 !important;
    background: $base-color-background !important;
  }
</style>
