<template>
  <!--  <div id="abc" class="system-log-container"></div>-->
  <div id="app" class="page">
    <div class="table-responsive">
      <h3>年度合同交付</h3>
      <span>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="form"
          @submit.native.prevent
        >
          <el-form-item :prop="form.year">
            <el-date-picker
              v-model="form.year"
              clearable
              type="year"
              value-format="yyyy"
              placeholder="选择年"
            ></el-date-picker>
          </el-form-item>
          <el-form-item :prop="form.season">
            <el-select v-model="form.season" clearable placeholder="请选择">
              <el-option
                v-for="item in seasons"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button
              native-type="submit"
              type="primary"
              @click="handleSearch"
            >
              查询
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button native-type="reset" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </span>
    </div>
    <el-row>
      <el-col :lg="24" :md="8" :sm="12">
        <h3>年度合同分析</h3>
        <el-table :data="list">
          <el-table-column
            align="center"
            prop="project"
            label=""
            width="180"
          ></el-table-column>
          <el-table-column align="center" label="1月" prop="jan" />
          <el-table-column align="center" label="2月" prop="feb" />
          <el-table-column align="center" label="3月" prop="march" />
          <el-table-column align="center" label="4月" prop="april" />
          <el-table-column align="center" label="5月" prop="may" />
          <el-table-column align="center" label="6月" prop="june" />
          <el-table-column align="center" label="7月" prop="july" />
          <el-table-column align="center" label="8月" prop="aug" />
          <el-table-column align="center" label="9月" prop="sept" />
          <el-table-column align="center" label="10月" prop="oct" />
          <el-table-column align="center" label="11月" prop="nov" />
          <el-table-column align="center" label="12月" prop="dec" />
        </el-table>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- <el-col :lg="16" :md="10" :sm="12">
        <div
          class="table-responsive"
          style="width: 100%; height: 340px; overflow-y: auto"
        >
          <div style="font-size: 18px; font-weight: 700">
            季度合同交付明细表
          </div>
          <el-table :data="list2">
            <el-table-column align="center" label="年" prop="year" />
            <el-table-column align="center" label="部门" prop="bm" />
            <el-table-column align="center" label="交付合同数量" prop="jfsl" />
            <el-table-column
              align="center"
              label="计划交付合同数量"
              prop="jhjfsl"
            />
            <el-table-column align="center" label="交付合同金额" prop="jfje" />
            <el-table-column
              align="center"
              label="计划交付合同金额"
              prop="jhjfje"
            />
            <el-table-column align="center" label="计划完成率" prop="jhwcl" />
          </el-table>
        </div>
      </el-col> -->
      <!-- <el-col :lg="8" :md="6" :sm="12">
        <div id="chats-5" style="width: 100%; height: 340px"></div>
      </el-col>
      <el-col :lg="24" :md="16" :sm="12">
        <div id="chats-2" style="width: 100%; height: 340px"></div>
      </el-col> -->

      <el-col :lg="24" :md="16" :sm="12">
        <el-button @click="setType(0)" :type="type == 0 ? 'primary' : ''">
          柱状图
        </el-button>
        <el-button @click="setType(1)" :type="type == 1 ? 'primary' : ''">
          折线图
        </el-button>
        <el-button @click="setType(2)" :type="type == 2 ? 'primary' : ''">
          饼图
        </el-button>
        <el-form
          style="margin-top: 20px"
          ref="form2"
          :inline="true"
          :rules="rules2"
          label-width="0"
          :model="form2"
        >
          <el-form-item :prop="form2.year">
            <el-date-picker
              v-model="form2.year"
              clearable
              type="year"
              value-format="yyyy"
              placeholder="选择年"
            ></el-date-picker>
          </el-form-item>
          <el-form-item :prop="form2.quarte">
            <el-select v-model="form2.quarte" clearable placeholder="请选择">
              <el-option
                v-for="item in seasons"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="x">
            <el-select v-model="form2.x" clearable placeholder="请选择x轴">
              <el-option
                v-for="item in xList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="y">
            <el-select
              v-model="form2.y"
              multiple
              clearable
              placeholder="请选择y轴"
            >
              <el-option
                v-for="item in yList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleSearch2">生成</el-button>
          </el-form-item>
          <el-form-item>
            <el-button native-type="reset" type="primary" @click="resetSearch2">
              重置
            </el-button>
          </el-form-item>
        </el-form>
        <div :style="type == 0 ? '' : 'display: none'">
          <div id="chats-6" style="width: 70vw; height: 500px"></div>
        </div>
        <div :style="type == 1 ? '' : 'display: none'">
          <div id="chats-7" style="width: 70vw; height: 500px"></div>
        </div>
        <div :style="type == 2 ? '' : 'display: none'">
          <div id="chats-8" style="width: 70vw; height: 500px"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {
    contractAnalysis,
    ContractCollection,
    orgContractPlan,
    orgPayContract,
    dynamicReport,
  } from '@/api/risk/delivery'
  // import { time } from 'console'
  import * as echarts from 'echarts'
  export default {
    name: 'Download',
    components: {},
    data() {
      return {
        testArr: [],
        chats2Xdata: [],
        chats2Ydata: [],
        chats5Xdata: [],
        chats5Ydata: [],
        ogetOrgs: [],
        chart5Xdata: [],
        seasons: [
          {
            value: '1',
            label: '第一季度',
          },
          {
            value: '2',
            label: '第二季度',
          },
          {
            value: '3',
            label: '第三季度',
          },
          {
            value: '4',
            label: '第四季度',
          },
        ],

        list2: [],
        form: {
          year: '',
          season: '',
        },

        list: [
          {
            project: '合同数量', //lyhtqdsl
            jan: '',
            feb: '',
            march: '',
            april: '',
            may: '',
            june: '',
            july: '',
            aug: '',
            sept: '',
            oct: '',
            nov: '',
            dec: '',
          },
          {
            project: '合同金额', //lyhtje
            jan: '',
            feb: '',
            march: '',
            april: '',
            may: '',
            june: '',
            july: '',
            aug: '',
            sept: '',
            oct: '',
            nov: '',
            dec: '',
          },
          {
            project: '合同付款金额', //lyhtfkje
            jan: '',
            feb: '',
            march: '',
            april: '',
            may: '',
            june: '',
            july: '',
            aug: '',
            sept: '',
            oct: '',
            nov: '',
            dec: '',
          },
          {
            project: '实际支付金额', //lyhtsjfkje
            jan: '',
            feb: '',
            march: '',
            april: '',
            may: '',
            june: '',
            july: '',
            aug: '',
            sept: '',
            oct: '',
            nov: '',
            dec: '',
          },
          {
            project: '合同收款金额', //lyhtskje
            jan: '',
            feb: '',
            march: '',
            april: '',
            may: '',
            june: '',
            july: '',
            aug: '',
            sept: '',
            oct: '',
            nov: '',
            dec: '',
          },
          {
            project: '实际收款金额', //lyhtsjskje
            jan: '',
            feb: '',
            march: '',
            april: '',
            may: '',
            june: '',
            july: '',
            aug: '',
            sept: '',
            oct: '',
            nov: '',
            dec: '',
          },
        ],
        chats6: null,
        chats7: null,
        chats8: null,
        type: 0,
        form2: {
          x: '',
          y: '',
          quarte: '',
          year: '',
        },
        rules2: {
          x: [{ required: true, message: '请选择x轴', trigger: 'change' }],
          y: [{ required: true, message: '请选择y轴', trigger: 'change' }],
        },
        xList: [
          {
            value: 1,
            label: '合同类型',
          },
          {
            value: 2,
            label: '承办部门',
          },
          {
            value: 3,
            label: '承办人',
          },
          {
            value: 4,
            label: '单位',
          },
          // {
          //   value: '签订起止日期',
          //   label: '签订起止日期',
          // },
        ],
        yList: [
          {
            value: 1,
            label: '合同金额',
          },
          {
            value: 2,
            label: '合同数量',
          },
        ],
        xAxisList: [],
        yAxisList: [],
        xAxisL: [],
        moneyL: [],
        countL: [],
      }
    },
    mounted() {},
    created() {
      this.contractAnalysisData()
      this.handleSearch2()
    },
    watch: {
      // form: {
      //   handler(newName, oldName) {
      //
      //     const time = {}
      //     // this.form.year = new Date(newName.year).getFullYear() || ''
      //     // this.form.season = newName.season
      //     time.year = new Date(newName.year).getFullYear() || ''
      //     time.quarte = newName.season
      //     //
      //     this.contractAnalysisData(time)
      //   },
      //   immediate: true,
      //   deep: true,
      // },
    },
    methods: {
      //重置
      async resetSearch2() {
        this.form2.x = ''
        this.form2.y = ''
        ;(await this.chats6) && this.chats6.dispose()
        ;(await this.chats7) && this.chats7.dispose()
        ;(await this.chats8) && this.chats8.dispose()
        this.xAxisList = []
        this.xAxisL = []
        this.moneyL = []
        this.countL = []
      },
      //处理数据
      async setType(type) {
        this.type = type

        if (this.xAxisList.length === 0) return
        if (this.type == 0) {
          ;(await this.chats6) && this.chats6.dispose()
          await this.getchats6()
        } else if (this.type == 1) {
          ;(await this.chats7) && this.chats7.dispose()
          await this.getchats7()
        } else if (this.type == 2) {
          ;(await this.chats8) && this.chats8.dispose()
          await this.getchats8()
        }
      },
      handleSearch2() {
        this.$refs['form2'].validate(async (valid) => {
          if (valid) {
            const yids = this.form2.y
            let indexYtype = 3
            if (yids.length < 2) {
              indexYtype = ~~yids.join('')
            }

            const queryForm = {
              indexXtype: this.form2.x,
              indexYtype,
              year: this.form2.year,
              quarte: this.form2.quarte,
            }

            const { data } = await dynamicReport(queryForm)
            this.xAxisList = data

            const xAxisL = []
            const moneyL = []
            const countL = []
            data.forEach((item) => {
              xAxisL.push(item.XINDEX)
              moneyL.push(item.CONTRACTMONEY)
              countL.push(item.CONTRACTCOUNT)
            })

            this.xAxisL = xAxisL
            this.moneyL = moneyL
            this.countL = countL

            this.yAxisList = yids.map((item1) => {
              return this.yList.find((item2) => item2.value === item1).label
            })

            if (this.type == 0) {
              ;(await this.chats6) && this.chats6.dispose()
              await this.getchats6()
            } else if (this.type == 1) {
              ;(await this.chats7) && this.chats7.dispose()
              await this.getchats7()
            } else if (this.type == 2) {
              ;(await this.chats8) && this.chats8.dispose()
              await this.getchats8()
            }
          } else {
            return false
          }
        })

        // this.xAxisList = []
        // this.yAxisList = []
      },
      loadData(index, data = [], name) {
        let obj = this.list[index]
        Object.keys(this.list[index]).forEach((item) => {
          data.forEach((item1) => {
            if (item == item1.name) {
              obj[item] = item1.value
            }
          })
        })
        obj.project = name
        this.list[index] = obj
      },
      //处理过滤数据
      contractAnalysisData(time = {}) {
        contractAnalysis(time).then((res) => {
          this.loadData(0, res.lyhtqdslArr, '合同数量')
          this.loadData(1, res.lyhtjeArr, '合同金额')
          this.loadData(2, res.lyfkjeArr, '合同付款金额')
          this.loadData(3, res.lyhtsjfkjeArr, '实际支付金额')
          this.loadData(4, res.lyhtskjeArr, '合同收款金额')
          this.loadData(5, res.lyhtsjskjeArr, '实际收款金额')

          // lyhtqdsl 合同数量
        })
        orgPayContract(time).then((res) => {
          this.list2 = res.data
        })
        //处理数据结构
        ContractCollection(time).then((res) => {
          this.chart5Xdata = res.getOrgs
          this.chart5Ydata = res.getOrgs.map((x) => {
            let i = res.orgCollectionAmount.findIndex((y) => y.NAME === x)
            const obj = {
              NAME: x,
              VALUE: '',
            }
            if (i !== -1) {
              obj.VALUE = res.orgCollectionAmount[i].VALUE
            } else {
              obj.VALUE = 0
            }

            return obj
          })
          this.orgCollectionAmount = res.orgCollectionAmount
          this.getchats5()
          this.getchats7()
          this.getchats8()
        })

        //处理数据
        orgContractPlan(time).then((res) => {
          this.chats2Xdata = res.getOrgs
          this.chats2Ydata = []
          this.ogetOrgs = res.getOrgs
          let arr = res.Amount
          res.getOrgs.forEach((element) => {
            let i = arr.findIndex((x) => x.NAME === element)
            if (i !== -1) {
              this.chats2Ydata.push(arr[i].VALUE)
            } else {
              this.chats2Ydata.push(0)
            }
          })
          this.getchats2()
        })
      },
      getchats2() {
        const chats2 = echarts.init(document.getElementById('chats-2'))
        // 指定图表的配置项和数据
        chats2.setOption({
          title: {
            text: '各部门交付合同计划完成情况',
            left: 'left',
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              crossStyle: {
                color: '#999',
              },
            },
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '10%',
            containLabel: true,
          },
          legend: {
            left: 'center',
            bottom: 'bottom',
            data: this.chats2Xdata,
          },
          xAxis: [
            {
              type: 'category',
              data: this.chats2Xdata,
              axisPointer: {
                type: 'shadow',
              },
            },
          ],
          yAxis: [
            {
              type: 'value',
              name: '单位：元',
            },
          ],
          series: [
            {
              name: '完成情况',
              type: 'bar',
              barWidth: 20,
              data: this.chats2Ydata,
            },
          ],
        })
      },
      getchats5() {
        const chats5 = echarts.init(document.getElementById('chats-5'))

        // 指定图表的配置项和数据
        chats5.setOption({
          title: {
            text: '各部门合同收款汇总表',
            left: 'left',
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow',
              crossStyle: {
                color: '#999',
              },
            },
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '10%',
            containLabel: true,
          },
          // legend: {
          //   left: 'center',
          //   bottom: 'bottom',
          //   data: ['2011'],
          // },
          xAxis: {
            type: 'value',
            boundaryGap: [0, 0.01],
          },
          yAxis: {
            type: 'category',
            data: this.chart5Xdata,
          },
          series: [
            {
              type: 'bar',
              barWidth: 20,
              data: this.chart5Ydata,
            },
          ],
        })
      },

      getchats6() {
        const chats6 = echarts.init(document.getElementById('chats-6'))
        // let that = this
        // 指定图表的配置项和数据
        chats6.setOption({
          title: {
            text: '柱状图',
            left: 'left',
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow',
            },
          },
          grid: {
            left: '10%',
            bottom: '32%',
          },
          legend: {
            data: this.yAxisList,
          },
          xAxis: {
            type: 'category',
            data: this.xAxisL,
            axisLabel: { interval: 0, rotate: 70 },
          },

          yAxis: {
            min: 1,
            logBase: 10,
            type: 'log',
          },
          series: this.yAxisList.map((x, index) => {
            return {
              name: x,
              data: x == '合同金额' ? this.moneyL : this.countL,
              type: 'bar',
            }
          }),
        })
        this.chats6 = chats6
      },
      getchats7() {
        const chats7 = echarts.init(document.getElementById('chats-7'))
        // let that = this
        // 指定图表的配置项和数据
        chats7.setOption({
          title: {
            text: '折线图',
            left: 'left',
          },
          grid: {
            left: '10%',
            bottom: '32%',
          },
          legend: {
            data: this.yAxisList,
          },
          xAxis: {
            type: 'category',
            data: this.xAxisL,
            axisLabel: { interval: 0, rotate: 70 },
          },
          yAxis: {
            min: 1,
            logBase: 10,
            type: 'log',
          },
          series: this.yAxisList.map((x, index) => {
            return {
              name: x,
              data: x == '合同金额' ? this.moneyL : this.countL,
              type: 'line',
            }
          }),
        })
        this.chats7 = chats7
      },
      getchats8() {
        let datas = []
        this.yAxisList.map((y, index1) => {
          let data = []
          this.xAxisL.map((x, index2) => {
            data.push({
              name: x,
              value:
                y == '合同金额' ? this.moneyL[index2] : this.countL[index2],
            })
          })
          datas.push(data)
        })
        const chats8 = echarts.init(document.getElementById('chats-8'))
        let that = this
        // 指定图表的配置项和数据
        chats8.setOption({
          title: {
            text: '饼图',
            left: 'left',
          },
          grid: {
            top: '10%',
          },
          series: datas.map(function (data, idx) {
            var top = idx * 50
            return {
              type: 'pie',
              radius: [20, 60],
              top: top + '%',
              height: '50%',
              left: 'center',
              width: 1000,
              itemStyle: {
                borderColor: '#fff',
                borderWidth: 1,
              },
              radiusAxis: {
                type: 'log',
              },
              label: {
                alignTo: 'edge',
                formatter: '{name|{b}}：' + that.yAxisList[idx] + '{c}',
                minMargin: 5,
                edgeDistance: 10,
                lineHeight: 15,
                rich: {
                  time: {
                    fontSize: 10,
                    color: '#999',
                  },
                },
              },
              labelLine: {
                length: 15,
                length2: 0,
                maxSurfaceAngle: 80,
              },
              data: data,
            }
          }),
        })
        this.chats8 = chats8
      },
      //回调
      handleSearch() {
        const time = {}
        time.year = new Date(this.form.year).getFullYear() || ''
        time.quarte = this.form.season
        this.contractAnalysisData(time)
      },
      //重置
      resetSearch() {
        this.$refs.form.resetFields()
        this.form.year = ''
        this.form.season = ''
        this.handleSearch()
      },
    },
  }
</script>
<style scoped>
  .el-col > div {
    border: 1px solid #dcdfe5;
    margin-bottom: 10px;
  }

  .page {
    padding: 20px;
  }

  .table-title {
    cursor: pointer;
  }

  .table th {
    position: relative;
  }

  .table-filter {
    position: absolute;
    border: 1px solid gainsboro;
    padding: 5px;
    left: 0;
    right: 0;
    top: 40px;
    background: white;
    min-width: 160px;
  }

  .table-filter input {
    padding: 5px;
    font-size: 14px;
    margin-right: 5px;
  }

  .table-filter .form-check {
    display: flex;
    flex-direction: column;
    text-align: left;
    font-size: 14px;
    font-weight: 400;
    padding: 5px;
  }

  .table-filter button {
    font-size: 12px;
    padding: 2px 10px;
  }

  .table-responsive {
    background: white;
    padding: 20px;
    margin-bottom: 20px;
  }

  .chats > div > div {
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }

  .select-year {
    display: flex;
    background: aliceblue;
    padding: 5px;
  }

  .select-year > div {
    margin-right: 10px;
    padding: 2px 5px;
    cursor: pointer;
  }

  .select-year .active {
    background: #ffaf0f;
    border-radius: 20px;
    color: white;
  }

  .margin-right-sm {
    margin-right: 15px;
  }
</style>
