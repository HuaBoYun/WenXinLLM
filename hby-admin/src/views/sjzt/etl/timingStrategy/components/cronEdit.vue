<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-form>
      <el-row :gutter="10">
        <el-col :span="4">
          <el-form-item label="每:">
            <el-select
              v-model="myFrequency.base"
              style="width: 90%"
              @change="selectChange1"
            >
              <el-option
                v-for="item in frequency"
                :label="item.label"
                :value="item.value"
                :key="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item v-show="myFrequency.base == 6" label="月=>">
            <el-select
              style="width: 85%"
              v-model="myFrequency.monthValues"
              multiple
              @change="selectChange6"
            >
              <el-option
                v-for="(item, key) in monthValues"
                :label="item"
                :value="key"
                :key="key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item v-show="myFrequency.base == 4" label="星期=>">
            <el-select
              v-model="myFrequency.dayValues"
              multiple
              style="width: 80%"
              @change="selectChange4"
            >
              <el-option
                v-for="(item, key) in dayValues"
                :label="item"
                :value="key"
                :key="key"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item v-show="myFrequency.base >= 5" label="几号=>">
            <el-select
              style="width: 69%"
              v-model="myFrequency.dayOfMonthValues"
              multiple
              @change="selectChange5"
            >
              <el-option
                v-for="(item, index) in dayOfMonthValues"
                :label="item.label"
                :value="item.value"
                :key="index"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item
            v-show="myFrequency.base >= 2"
            :label="myFrequency.base >= 3 ? '点222=>' : '分钟11=>'"
          >
            <el-select
              v-show="myFrequency.base >= 3"
              v-model="myFrequency.hourValues"
              multiple
              style="width: 80%"
              @change="selectChange3"
            >
              <el-option
                v-for="item in hourValues"
                :label="item"
                :value="item"
                :key="item"
              ></el-option>
            </el-select>
            <el-select
              v-model="myFrequency.minuteValues"
              v-show="myFrequency.base < 3"
              multiple
              style="width: 70%"
              @change="selectChange2"
            >
              <el-option
                v-for="item in minuteValues"
                :label="item"
                :value="item"
                :key="item"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item v-show="myFrequency.base >= 3" label="分钟=>">
            <el-select
              v-model="myFrequency.minuteValues"
              multiple
              style="width: 70%"
              @change="selectChange2"
            >
              <el-option
                v-for="item in minuteValues"
                :label="item"
                :value="item"
                :key="item"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="23">
          <el-form-item label="cron表达式：">
            {{ jobCron }}
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="23">
          <el-form-item label="最近5次运行时间：">
            <el-button type="primary" size="small" @click="runCron">
              点击查看
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="23">
          <div v-for="item in cronList" style="padding: 0 5px">{{ item }}</div>
        </el-col>
      </el-row>
    </el-form>
  </el-dialog>
</template>
<script>
  export default {
    data() {
      return {
        dialogFormVisible: false,
        jobCron: '',
        txtLen: 500,
        isCron: true,
        myFrequency: {
          base: 1,
          dayValues: '',
          dayOfMonthValues: '',
          monthValues: '',
          hourValues: '',
          minuteValues: '',
        },
        modifyFrequency: {
          dayValues: '',
          dayOfMonthValues: '',
          monthValues: '',
          hourValues: '',
          minuteValues: '',
        },
        frequency: [
          {
            value: 1,
            label: '分钟',
          },
          {
            value: 2,
            label: '小时',
          },
          {
            value: 3,
            label: '天',
          },
          {
            value: 4,
            label: '星期',
          },
          {
            value: 5,
            label: '月',
          },
          {
            value: 6,
            label: '年',
          },
        ],
        baseFrequency: {
          minute: 1,
          hour: 2,
          day: 3,
          week: 4,
          month: 5,
          year: 6,
        },
        dayValues: {
          1: '星期日',
          2: '星期一',
          3: '星期二',
          4: '星期三',
          5: '星期四',
          6: '星期五',
          7: '星期六',
        },
        dayOfMonthValues: [],
        monthValues: {
          1: '一月',
          2: '二月',
          3: '三月',
          4: '四月',
          5: '五月',
          6: '六月',
          7: '七月',
          8: '八月',
          9: '九月',
          10: '十月',
          11: '十一月',
          12: '十二月',
        },
        hourValues: [
          0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
          20, 21, 22, 23,
        ],
        minuteValues: [0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55],
        baseCout: 0,
        id: '',
        cronList: [],
      }
    },
    methods: {
      runCron() {
        let params = {
          jobCron: this.jobCron,
        }
        ajax.getJson(
          getTimeUrl,
          params,
          (res) => {
            if (res.result == 200) this.cronList = res.obj
            else this.cronList = []
          },
          (err) => {}
        )
      },
      getMothValue() {
        let list = []
        for (let i = 0; i < 31; i++) {
          let j = i
          let currentJson = {
            value: ++j,
            label: j + '号',
          }
          list[i] = currentJson
        }
        this.dayOfMonthValues = list
      },
      hasClear(val) {
        if (val < this.baseCout) {
          this.myFrequency.minuteValues = []
          this.myFrequency.hourValues = []
          this.myFrequency.dayValues = []
          this.myFrequency.dayOfMonthValues = []
          this.myFrequency.monthValues = []
        }
      },
      selectChange1(val) {
        this.hasClear(val)
        this.baseCout = val
        if (this.modifyFrequency.minuteValues)
          this.myFrequency.minuteValues = this.modifyFrequency.minuteValues
        if (this.modifyFrequency.hourValues)
          this.myFrequency.hourValues = this.modifyFrequency.hourValues
        if (this.modifyFrequency.dayValues)
          this.myFrequency.dayValues = this.modifyFrequency.dayValues
        if (this.modifyFrequency.dayOfMonthValues)
          this.myFrequency.dayOfMonthValues =
            this.modifyFrequency.dayOfMonthValues
        if (this.modifyFrequency.monthValues)
          this.myFrequency.monthValues = this.modifyFrequency.monthValues
        this.modifyFrequency = {
          dayValues: '',
          dayOfMonthValues: '',
          monthValues: '',
          hourValues: '',
          minuteValues: '',
        }
        this.getCron(val)
      },
      getCron(val) {
        var cron = ['0', '*', '*', '*', '*', '?']
        if (val && val >= this.baseFrequency.hour) {
          if (this.myFrequency.minuteValues.length == 0)
            this.myFrequency.minuteValues.push(0)
          cron[1] =
            typeof this.myFrequency.minuteValues !== 'undefined'
              ? this.myFrequency.minuteValues
              : '0'
        }
        if (val && val >= this.baseFrequency.day) {
          if (this.myFrequency.hourValues.length == 0)
            this.myFrequency.hourValues.push(0)
          cron[2] =
            typeof this.myFrequency.hourValues !== 'undefined'
              ? this.myFrequency.hourValues
              : '*'
        }
        if (val && val === this.baseFrequency.week) {
          if (this.myFrequency.dayValues.length == 0)
            this.myFrequency.dayValues.push('1')
          cron[3] = '?'
          cron[5] = this.myFrequency.dayValues
        }
        if (val && val >= this.baseFrequency.month) {
          if (this.myFrequency.dayOfMonthValues.length == 0)
            this.myFrequency.dayOfMonthValues.push(1)
          cron[3] =
            typeof this.myFrequency.dayOfMonthValues !== 'undefined'
              ? this.myFrequency.dayOfMonthValues
              : '?'
        }
        if (val && val === this.baseFrequency.year) {
          if (this.myFrequency.monthValues.length == 0)
            this.myFrequency.monthValues.push('1')
          cron[4] =
            typeof this.myFrequency.monthValues !== 'undefined'
              ? this.myFrequency.monthValues
              : '*'
        }
        this.jobCron = cron.join(' ')
      },
      selectChange2(val) {
        this.myFrequency.minuteValues = val
        this.getCron(this.myFrequency.base)
      },
      selectChange3(val) {
        this.myFrequency.hourValues = val
        this.getCron(this.myFrequency.base)
      },
      selectChange4(val) {
        this.myFrequency.dayValues = val
        this.getCron(this.myFrequency.base)
      },
      selectChange5(val) {
        this.myFrequency.dayOfMonthValues = val
        this.getCron(this.myFrequency.base)
      },
      selectChange6(val) {
        this.myFrequency.monthValues = val
        this.getCron(this.myFrequency.base)
      },
      //获取的编辑详情接口
      getModifyData(val) {
        this.id = val.id
        let params = {
          id: val.id,
        }
        ajax.getJson(
          getTimeTaskMessageUrl,
          params,
          (res) => {
            if (res.result == 200) {
              this.fromDefaultCron(res.obj.jobCron)
            }
          },
          (err) => {}
        )
      },
      fromDefaultCron(value) {
        let cron = value.replace(/\s+/g, ' ').split(' ')
        var frequency = { base: '1' } // default: every minute
        var tempArray = []
        if (
          cron[1] === '*' &&
          cron[2] === '*' &&
          cron[3] === '*' &&
          cron[4] === '*' &&
          cron[5] === '?'
        ) {
          frequency.base = 1 // every minute
        } else if (
          cron[2] === '*' &&
          cron[3] === '*' &&
          cron[4] === '*' &&
          cron[5] === '?'
        ) {
          frequency.base = 2 // every hour
        } else if (cron[3] === '*' && cron[4] === '*' && cron[5] === '?') {
          frequency.base = 3 // every day
        } else if (cron[3] === '?') {
          frequency.base = 4 // every week
        } else if (cron[4] === '*' && cron[5] === '?') {
          frequency.base = 5 // every month
        } else if (cron[5] === '?') {
          frequency.base = 6 // every year
        }
        if (cron[1] !== '*') {
          //preparing to handle multiple minutes
          tempArray = cron[1].split(',')
          for (var i = 0; i < tempArray.length; i++) {
            tempArray[i] = +tempArray[i]
          }
          frequency.minuteValues = tempArray
        }
        if (cron[2] !== '*') {
          //preparing to handle multiple hours
          tempArray = cron[2].split(',')
          for (var i = 0; i < tempArray.length; i++) {
            tempArray[i] = +tempArray[i]
          }
          frequency.hourValues = tempArray
        }
        if (cron[3] !== '*' && cron[3] !== '?') {
          //preparing to handle multiple days of the month
          tempArray = cron[3].split(',')
          for (var i = 0; i < tempArray.length; i++) {
            tempArray[i] = +tempArray[i]
          }
          frequency.dayOfMonthValues = tempArray
        }
        if (cron[4] !== '*') {
          //preparing to handle multiple months
          tempArray = cron[4].split(',')
          for (var i = 0; i < tempArray.length; i++) {
            tempArray[i] = +tempArray[i] + ''
          }
          frequency.monthValues = tempArray
        }
        if (cron[5] !== '*' && cron[5] !== '?') {
          //preparing to handle multiple days of the week
          tempArray = cron[5].split(',')
          for (var i = 0; i < tempArray.length; i++) {
            tempArray[i] = +tempArray[i] + ''
          }
          frequency.dayValues = tempArray
        }
        this.myFrequency.base = frequency.base
        if (frequency.dayOfMonthValues)
          this.modifyFrequency.dayOfMonthValues = frequency.dayOfMonthValues
        if (frequency.hourValues)
          this.modifyFrequency.hourValues = frequency.hourValues
        if (frequency.minuteValues)
          this.modifyFrequency.minuteValues = frequency.minuteValues
        if (frequency.dayValues)
          this.modifyFrequency.dayValues = frequency.dayValues
        if (frequency.monthValues)
          this.modifyFrequency.monthValues = frequency.monthValues
      },
      async showEdit(title, row) {
        this.formData = {}
        this.dialogFormVisible = true
      },
    },
    created() {},
  }
</script>
