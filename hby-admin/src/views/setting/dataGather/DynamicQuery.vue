<!--
 * @Date: 2022-04-24 10:10:37
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-26 11:47:59
 * @FilePath: /hb-admin/src/views/setting/dataGather/DynamicQuery.vue
-->
<template>
  <el-form
    ref="form"
    :inline="true"
    label-width="0"
    :model="query"
    @submit.native.prevent
  >
    <el-form-item>
      <el-select
        v-model="query.typeName"
        clearable
        placeholder="查询类型"
        style="width: 100px"
      >
        <el-option
          v-for="item in typeNameOtions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </el-form-item>
    <el-form-item v-show="conditionOptions.length">
      <el-select v-model="query.strType" :style="'width:100px'">
        <el-option
          v-for="item in conditionOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </el-form-item>
    <el-form-item v-show="query.typeName == 'infoName'">
      <el-input
        v-model="query.infoName"
        clearable
        placeholder=""
        :style="'width:100px'"
      />
    </el-form-item>
    <el-form-item v-show="query.typeName == 'acWeek'">
      <el-checkbox-group v-model="query.acWeek">
        <el-checkbox
          v-for="weekday in weekdayOptions"
          :key="weekday.value"
          :label="weekday.value"
        >
          {{ weekday.label }}
        </el-checkbox>
      </el-checkbox-group>
    </el-form-item>
    <el-form-item v-show="query.typeName == 'setDate'">
      <el-checkbox-group v-model="query.setDate">
        <el-checkbox
          v-for="time in timeOptions"
          :key="time.value"
          :label="time.value"
        >
          {{ time.label }}
        </el-checkbox>
      </el-checkbox-group>
    </el-form-item>
    <el-form-item v-show="query.typeName == 'createTime'">
      <el-date-picker
        v-model="query.createTime"
        class="width-130"
        format="yyyy-MM-dd"
        placeholder="创建时间"
        value-format="yyyy-MM-dd"
      />
    </el-form-item>
    <el-form-item v-show="query.typeName == 'realName'">
      <el-input v-model="query.realName" clearable placeholder="创建人" />
    </el-form-item>
    <el-form-item v-show="query.typeName == 'status'">
      <el-select v-model="query.status" clearable placeholder="采集状态">
        <el-option label="已暂停" value="0" />
        <el-option label="已启动" value="1" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button
        icon="el-icon-search"
        native-type="submit"
        type="primary"
        @click="fetchData"
      >
        查询
      </el-button>
    </el-form-item>
    <el-form-item>
      <el-button native-type="submit" type="primary" @click="resetSearch">
        重置
      </el-button>
    </el-form-item>
  </el-form>
</template>
<script>
  export default {
    props: {
      queryForm: {
        type: Object,
        default: () => {},
      },
    },
    data() {
      return {
        query: {},
        typeNameOtions: [
          {
            label: '策略名称',
            value: 'infoName',
          },
          {
            label: '采集频率',
            value: 'acWeek',
          },
          {
            label: '采集时间',
            value: 'setDate',
          },
          {
            label: '创建时间',
            value: 'createTime',
          },
          {
            label: '创建人',
            value: 'realName',
          },
          {
            label: '采集状态',
            value: 'status',
          },
        ],
        conditionOptions: [],
        weekdayOptions: [
          {
            label: '周一',
            value: '1',
          },
          {
            label: '周二',
            value: '2',
          },
          {
            label: '周三',
            value: '3',
          },
          {
            label: '周四',
            value: '4',
          },
          {
            label: '周五',
            value: '5',
          },
          {
            label: '周六',
            value: '6',
          },
          {
            label: '周日',
            value: '7',
          },
          {
            label: '每天',
            value: '0',
          },
        ],
        timeOptions: [
          {
            label: '00:00',
            value: '0',
          },
          {
            label: '02:00',
            value: '2',
          },
          {
            label: '04:00',
            value: '4',
          },
          {
            label: '06:00',
            value: '6',
          },
          {
            label: '08:00',
            value: '8',
          },
          {
            label: '10:00',
            value: '10',
          },
          {
            label: '12:00',
            value: '12',
          },
          {
            label: '14:00',
            value: '14',
          },
          {
            label: '16:00',
            value: '16',
          },
          {
            label: '18:00',
            value: '18',
          },
          {
            label: '20:00',
            value: '20',
          },
          {
            label: '22:00',
            value: '22',
          },
        ],
      }
    },
    watch: {
      queryForm: {
        handler() {
          this.query = Object.assign({}, this.queryForm)
        },
        immediate: true,
      },
      'query.typeName': {
        handler(val) {
          if (val == 'infoName' || val == 'realName') {
            this.conditionOptions = [
              {
                label: '等于',
                value: '=',
              },
              {
                label: '不等于',
                value: '!=',
              },
              {
                label: '包含',
                value: 'LIKE',
              },
              {
                label: '不包含',
                value: 'NOT',
              },
            ]
          } else if (val == 'acWeek' || val == 'setDate') {
            this.conditionOptions = [
              {
                label: '包含',
                value: 'LIKE',
              },
              {
                label: '不包含',
                value: 'NOT',
              },
            ]
          } else if (val == 'createTime') {
            this.conditionOptions = [
              {
                label: '大于',
                value: '>',
              },
              {
                label: '等于',
                value: '=',
              },
              {
                label: '小于',
                value: '<',
              },
              {
                label: '不等于',
                value: '!=',
              },
              {
                label: '大于等于',
                value: '>=',
              },
              {
                label: '小于等于',
                value: '<=',
              },
            ]
          } else if (val == 'status') {
            this.conditionOptions = [
              // {
              //   label: '等于',
              //   value: '=',
              // },
            ]
          }
          if (this.conditionOptions.length) {
            this.query.strType = this.conditionOptions.length[0]
          }
        },
        immediate: true,
      },
    },
    methods: {
      fetchData() {
        const filed = this.query['typeName']
        const params = Object.assign({}, this.query, {
          strVal: this.query[filed],
        })
        this.$emit('fetch-data', params)
      },
      resetSearch() {
        this.$emit('reset-search', this.query)
      },
    },
  }
</script>
<style lang="scss" scoped>
  .el-checkbox {
    margin-right: 14px;
    .el-checkbox__label {
      padding-left: 2px;
    }
  }
</style>
