<template>
  <div class="date-picker-container">
    <el-card shadow="hover">
      <template #header>
        <span>选择日</span>
      </template>
      <el-date-picker v-model="value1" placeholder="选择日期" type="date" />
      <el-date-picker
        v-model="value2"
        align="right"
        :picker-options="pickerOptions"
        placeholder="选择日期"
        type="date"
      />
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>其他日期单位</span>
      </template>
      <el-date-picker
        v-model="value3"
        format="yyyy 第 WW 周"
        placeholder="选择周"
        type="week"
      />
      <el-date-picker v-model="value4" placeholder="选择月" type="month" />
      <el-date-picker v-model="value5" placeholder="选择年" type="year" />
      <el-date-picker
        v-model="value6"
        placeholder="选择一个或多个日期"
        type="dates"
      />
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>选择日期范围</span>
      </template>
      <el-date-picker
        v-model="value7"
        end-placeholder="结束日期"
        range-separator="至"
        start-placeholder="开始日期"
        type="daterange"
      />
    </el-card>
    <el-card shadow="hover">
      <template #header>
        <span>选择月份范围</span>
      </template>
      <el-date-picker
        v-model="value8"
        end-placeholder="结束月份"
        range-separator="至"
        start-placeholder="开始月份"
        type="monthrange"
      />
    </el-card>
  </div>
</template>

<script>
  export default {
    name: 'DatePicker',
    data() {
      return {
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now()
          },
          shortcuts: [
            {
              text: '今天',
              onClick(picker) {
                picker.$emit('pick', new Date())
              },
            },
            {
              text: '昨天',
              onClick(picker) {
                const date = new Date()
                date.setTime(date.getTime() - 3600 * 1000 * 24)
                picker.$emit('pick', date)
              },
            },
            {
              text: '一周前',
              onClick(picker) {
                const date = new Date()
                date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
                picker.$emit('pick', date)
              },
            },
          ],
        },
        value1: '',
        value2: '',
        value3: '',
        value4: '',
        value5: '',
        value6: '',
        value7: '',
        value8: '',
      }
    },
  }
</script>

<style lang="scss" scoped>
  .date-picker-container {
    padding: 0 !important;
    background: $base-color-background !important;

    ::v-deep {
      .el-range-separator {
        box-sizing: content-box;
      }

      .el-input {
        width: 180px;

        &:first-child {
          margin-right: 10px;
        }

        & + .el-input {
          margin-right: 10px;
          margin-bottom: 10px;
          margin-left: 0;
        }
      }
    }
  }
</style>
