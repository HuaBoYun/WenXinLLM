<template>
  <!--  <div id="abc" class="system-log-container"></div>-->
  <div id="app">
    <div class="header fixed-top">
      <el-form :inline="true" label-width="80px">
        <el-form-item label="年份">
          <el-date-picker
            v-model="queryYear"
            type="year"
            placeholder="选择年份"
            value-format="yyyy"
            format="yyyy"
          ></el-date-picker>
        </el-form-item>
        <!-- <el-form-item label="月">
          <el-select id="inputGroupSelectMonth" class="form-select">
            <el-option value="0">全部</el-option>
            <el-option value="1">1月</el-option>
            <el-option value="2">2月</el-option>
            <el-option value="3">3月</el-option>
            <el-option value="4">4月</el-option>
            <el-option value="5">5月</el-option>
            <el-option value="6">6月</el-option>
            <el-option value="7">7月</el-option>
            <el-option value="8">8月</el-option>
            <el-option value="9">9月</el-option>
            <el-option value="10">10月</el-option>
            <el-option value="11">11月</el-option>
            <el-option value="12">12月</el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="fentchAll" />
        </el-form-item>
      </el-form>
    </div>
    <div class="content">
      <el-row :gutter="10">
        <el-col :lg="24" :md="6" :sm="24">
          <el-card>
            <div style="padding: 0px 0 20px 10px; color: #788faa">
              审计问题整改迟缓事项
            </div>
            <el-table :data="list1">
              <!-- <el-table-column
                align="center"
                label="序号"
                type="index"
                :index="indexMethod"
              /> -->
              <el-table-column
                align="center"
                label="项目名称"
                prop="projectName"
              />
              <el-table-column
                align="center"
                label="被审计单位"
                prop="auditOrgName"
              />
              <el-table-column
                align="center"
                label="审计类型"
                prop="sjlxName"
              />
              <el-table-column align="center" label="主审" prop="zsname" />
              <el-table-column align="center" label="计划时间" prop="g">
                <template #default="{ row }">
                  {{ row.planStarttime + '至' + row.planEndtime }}
                </template>
              </el-table-column>
              <el-table-column align="center" label="是否境外" prop="isWw">
                <template #default="{ row }">
                  {{ row.isWw == 1 ? '是' : '否' }}
                </template>
              </el-table-column>
              <el-table-column align="center" label="问题金额" prop="wtje" />
            </el-table>
            <el-pagination
              background
              :current-page="queryForm.pageNumber"
              :layout="layout"
              :page-size="queryForm.pageSize"
              :total="total"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
            />
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="12" :md="6" :sm="24">
          <el-card>
            <div id="chats-11" style="width: 100%; height: 300px"></div>
          </el-card>
        </el-col>
        <el-col :lg="12" :md="6" :sm="24">
          <el-card>
            <div id="chats-12" style="width: 100%; height: 300px"></div>
          </el-card>
        </el-col>
        <!-- <el-col :lg="8" :md="6" :sm="24">
          <el-card>
            <div id="chats-13" style="width: 100%; height: 300px"></div>
          </el-card>
        </el-col> -->
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="24" :md="6" :sm="24">
          <el-card>
            <div id="chats-2" style="width: 100%; height: 300px"></div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="10">
        <el-col :lg="12" :md="6" :sm="24">
          <el-card>
            <div style="padding: 0px 0 20px 10px; color: #788faa">
              审计项目计划完成情况一览表
            </div>
            <el-table :data="list2" height="280" style="overflow-y: auto">
              <!-- <el-table-column align="center" label="序号" prop="key" /> -->
              <el-table-column
                align="center"
                label="被审计单位"
                prop="bsjdw"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="已完成项目数量"
                prop="wcs"
              />
              <el-table-column
                align="center"
                label="未完成项目数量"
                prop="wwcs"
              />
            </el-table>
            <!-- <el-pagination
              background
              :current-page="queryForm.pageNumber"
              :layout="layout"
              :page-size="queryForm.pageSize"
              :total="total"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
            /> -->
          </el-card>
        </el-col>
        <el-col :lg="12" :md="6" :sm="24">
          <el-card>
            <div style="padding: 0px 0 20px 10px; color: #788faa">
              审计项目数一览表
            </div>
            <el-table :data="list2" height="280" style="overflow-y: auto">
              <!-- <el-table-column align="center" label="序号" prop="key" /> -->
              <el-table-column
                align="center"
                label="被审计单位"
                prop="bsjdw"
                show-overflow-tooltip
              />
              <el-table-column align="center" label="项目数量" prop="zs" />
            </el-table>
            <!-- <el-pagination
              background
              :current-page="queryForm.pageNumber"
              :layout="layout"
              :page-size="queryForm.pageSize"
              :total="total"
              @current-change="handleCurrentChange"
              @size-change="handleSizeChange"
            /> -->
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import {
    getYHData,
    getSJXMData,
    getXMZTData,
    getBNMYData,
  } from '@/oapi/cwztfx.js'
  import { formatYear } from '@/utils'
  export default {
    name: 'Download',
    components: {},
    data() {
      return {
        list1: [],
        list2: [],
        total: 0, // 第一个表格的总数
        total1: 0,
        total2: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
        },
        queryYear: formatYear(new Date()),
      }
    },
    mounted() {
      // var chartDom13 = document.getElementById('chats-13')
      // var myChart13 = echarts.init(chartDom13)
      // var option13
      // window.addEventListener('resize', () => {
      //   setTimeout(() => {
      //     myChart13.resize()
      //   }, 100)
      // })
      // option13 = {
      //   title: {
      //     text: '高风险评估事项',
      //   },
      //   // legend: {
      //   //   data: ['Allocated Budget', 'Actual Spending'],
      //   // }
      //   radar: {
      //     // shape: 'circle',
      //     indicator: [
      //       { name: '人员伤亡', max: 6500 },
      //       { name: '间接损失', max: 16000 },
      //       { name: '贪污腐败', max: 30000 },
      //       { name: '经济损失', max: 38000 },
      //       { name: '直接损失', max: 52000 },
      //       { name: '环保事故', max: 25000 },
      //       { name: '安全事故', max: 25000 },
      //       { name: '企业负面影响', max: 25000 },
      //     ],
      //   },
      //   series: [
      //     {
      //       name: 'Budget vs spending',
      //       type: 'radar',
      //       data: [
      //         {
      //           value: [4200, 3000, 20000, 35000, 50000, 18000],
      //           name: 'Allocated Budget',
      //         },
      //         {
      //           value: [5000, 14000, 28000, 26000, 42000, 21000],
      //           name: 'Actual Spending',
      //         },
      //       ],
      //     },
      //   ],
      // }
      // option13 && myChart13.setOption(option13)
    },
    created() {
      this.getYHData()
      this.getSJXMData()
      this.getXMZTData()
      this.getBNMYData()

      this.queryYear = formatYear(new Date()).toString()
    },
    methods: {
      // 处理页码变化
      handleCurrentChange(page) {
        this.queryForm.pageNumber = page
        this.getYHData()
      },
      // 处理每页条数变化
      handleSizeChange(size) {
        this.queryForm.pageSize = size
        this.queryForm.pageNumber = 1
        this.getYHData()
      },
      getYHData() {
        getYHData({
          queryYear: this.queryYear,
          ...this.queryForm,
        }).then((res) => {
          this.list1 = res.data.tlist || []
          this.total = res.data.totalRecord || 0
        })
      },
      getSJXMData() {
        getSJXMData({ queryYear: this.queryYear }).then((res) => {
          this.list2 = res.data.list
          const info =
            res.data.list &&
            res.data.list.map((item) => {
              return { value: item.zs, name: item.bsjdw }
            })
          this.render11(info)
        })
      },
      getXMZTData() {
        getXMZTData({ queryYear: this.queryYear }).then((res) => {
          const info =
            res.data.list &&
            res.data.list.map((item) => {
              return { value: item.sl, name: item.status }
            })
          this.render12(info)
        })
      },
      getBNMYData() {
        getBNMYData({ queryYear: this.queryYear }).then((res) => {
          let info = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

          res.data.list &&
            res.data.list.forEach((item) => {
              info[item.yf - 1] = item.sl
            })

          this.render2(info)
        })
      },
      indexMethod(index) {
        // 序号列从 1 开始，所以需要 index + 1
        return index + 1
      },
      render11(data) {
        var chartDom11 = document.getElementById('chats-11')
        var myChart11 = echarts.init(chartDom11)
        var option11
        window.addEventListener('resize', () => {
          setTimeout(() => {
            myChart11.resize()
          }, 100)
        })

        option11 = {
          title: {
            text: '审计项目数',
            left: 'left',
          },
          tooltip: {
            trigger: 'item',
          },
          legend: {
            orient: 'horizontal',
            left: 'right',
            y: 'bottom',
            x: 'right',
          },
          series: [
            {
              name: 'Access From',
              type: 'pie',
              radius: '50%',
              data,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
            },
          ],
        }

        option11 && myChart11.setOption(option11)
      },
      render12(data) {
        var chartDom12 = document.getElementById('chats-12')
        var myChart12 = echarts.init(chartDom12)
        var option12
        window.addEventListener('resize', () => {
          setTimeout(() => {
            myChart12.resize()
          }, 100)
        })

        option12 = {
          title: {
            text: '审计项目计划完成情况',
            left: 'left',
          },
          tooltip: {
            trigger: 'item',
          },
          legend: {
            orient: 'vertical',
            left: 'right',
            y: 'middle',
            x: 'right',
          },
          series: [
            {
              name: 'Access From',
              type: 'pie',
              radius: '50%',
              data,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
            },
          ],
        }

        option12 && myChart12.setOption(option12)
      },
      render2(data) {
        var chartDom2 = document.getElementById('chats-2')
        var myChart2 = echarts.init(chartDom2)
        var option2
        const colors = ['#5470C6']
        option2 = {
          color: colors,
          title: {
            text: '集团审计问题数量趋势变化',
            left: 'left',
          },
          xAxis: [
            {
              type: 'category',
              data: [
                '1月',
                '2月',
                '3月',
                '4月',
                '5月',
                '6月',
                '7月',
                '8月',
                '9月',
                '10月',
                '11月',
                '12月',
              ],
            },
          ],
          tooltip: {
            trigger: 'axis', // 设置提示框触发的方式为鼠标悬浮到坐标轴上
            axisPointer: {
              type: 'cross', // 设置提示框指示器的类型
            },
          },
          yAxis: {
            type: 'value',
          },
          series: [
            {
              data,
              type: 'line',
            },
          ],
        }

        option2 && myChart2.setOption(option2)
      },
      fentchAll() {
        this.getYHData()
        this.getSJXMData()
        this.getXMZTData()
        this.getBNMYData()
      },
    },
  }
</script>

<style scoped></style>
