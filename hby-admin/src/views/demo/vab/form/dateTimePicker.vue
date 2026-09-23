<template>
  <div class="date-time-picker-container">
    <el-card shadow="hover">
      <template #header>
        <span>日期和时间点</span>
      </template>
      <el-date-picker
        v-model="value1"
        placeholder="选择日期时间"
        type="datetime"
      />
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>日期和时间范围</span>
      </template>
      <el-date-picker
        v-model="value2"
        end-placeholder="结束日期"
        range-separator="至"
        start-placeholder="开始日期"
        type="datetimerange"
      />
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>默认的起始与结束时刻</span>
      </template>
      <el-date-picker
        v-model="value3"
        align="right"
        :default-time="['12:00:00', '08:00:00']"
        end-placeholder="结束日期"
        start-placeholder="开始日期"
        type="datetimerange"
      />
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'DateTimePicker',
    data() {
      return {
        value1: '',
        value2: [
          new Date(2000, 10, 10, 10, 10),
          new Date(2000, 10, 11, 10, 10),
        ],
        value3: '',
      }
    },
  }
</script>

<style lang="scss" scoped>
  .date-time-picker-container {
    padding: 0 !important;
    background: $base-color-background !important;
  }
</style>
