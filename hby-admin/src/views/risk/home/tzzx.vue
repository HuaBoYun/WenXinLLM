<template>
  <!--  <div id="abc" class="system-log-container"></div>-->
  <div>
    <el-row>
      <el-card>
        <div slot="header" class="clearfix">
          <span>待办中心</span>
          <!-- <i
                class="el-icon-more"
                style="float: right; padding: 3px 0; cursor: pointer"
              ></i> -->
        </div>

        <div class="box">
          <el-tabs
            v-model="activeName"
            @tab-click="handleTabClick"
            type="border-card"
          >
            <el-tab-pane
              v-for="(item, index) in tabList"
              :key="index"
              :label="item.title"
              :name="item.name"
            >
              <!-- 待办事宜 -->
              <div v-show="activeName === 'wddb'">
                <el-table v-loading="wddblistLoading" :data="wddblist">
                  <el-table-column
                    align="center"
                    label="流程名称"
                    prop="fullName"
                  />
                  <el-table-column
                    align="center"
                    label="任务名称"
                    prop="flowName"
                  />
                  <el-table-column
                    prop="status"
                    label="流程状态"
                    width="130"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <el-tag type="primary" v-if="scope.row.status == 1">
                        等待审核
                      </el-tag>
                      <el-tag type="success" v-else-if="scope.row.status == 2">
                        审核通过
                      </el-tag>
                      <el-tag type="danger" v-else-if="scope.row.status == 3">
                        审核驳回
                      </el-tag>
                      <el-tag type="info" v-else-if="scope.row.status == 4">
                        流程撤回
                      </el-tag>
                      <el-tag type="info" v-else-if="scope.row.status == 5">
                        审核终止
                      </el-tag>
                      <el-tag type="warning" v-else>等待提交</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    label="创建时间"
                    prop="creatorTime"
                    :formatter="formatDate"
                  ></el-table-column>
                  <el-table-column
                    align="center"
                    label="操作"
                    show-overflow-tooltip
                    width="120"
                  >
                    <template #default="{ row }">
                      <el-button type="text" @click="handleDetailwddb(row)">
                        审批
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <el-pagination
                  background
                  :current-page="wddbqueryForm.currentPage"
                  :layout="layout"
                  :page-size="wddbqueryForm.pageSize"
                  :page-sizes="pageSizes"
                  :total="wddbtotal"
                  @current-change="handleCurrentChangewddb"
                  @size-change="handleSizeChangewddb"
                />
              </div>
              <div v-show="activeName === 'wdcy'">
                <el-table v-loading="wdcylistLoading" :data="wdcylist">
                  <el-table-column
                    align="center"
                    label="流程标题"
                    prop="fullName"
                  />
                  <el-table-column
                    align="center"
                    label="所属流程"
                    prop="flowName"
                  />
                  <el-table-column
                    prop="status"
                    label="流程状态"
                    width="130"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <el-tag type="success" v-if="scope.row.status == 1">
                        通过
                      </el-tag>
                      <el-tag type="danger" v-else>拒绝</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    label="发起时间"
                    prop="creatorTime"
                    :formatter="formatDate"
                  />
                  <el-table-column
                    align="center"
                    label="操作"
                    show-overflow-tooltip
                    width="120"
                  >
                    <template #default="{ row }">
                      <el-button
                        type="text"
                        @click="handleDetailwdcy(row, false)"
                      >
                        详情
                      </el-button>
                      <el-button
                        v-if="
                          row.taskStatus != 2 &&
                          row.taskStatus != 4 &&
                          row.taskStatus != 5
                        "
                        type="text"
                        @click="handleRebackwdcy(row)"
                      >
                        撤回
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <el-pagination
                  background
                  :current-page="wdcyqueryForm.currentPage"
                  :layout="layout"
                  :page-size="wdcyqueryForm.pageSize"
                  :page-sizes="pageSizes"
                  :total="wdcytotal"
                  @current-change="handleCurrentChangewdcy"
                  @size-change="handleSizeChangewdcy"
                />
              </div>
              <div v-show="activeName === 'wfqd'">
                <el-table v-loading="wfqdlistLoading" :data="wfqdlist">
                  <el-table-column
                    align="center"
                    label="流程标题"
                    prop="fullName"
                  />
                  <el-table-column
                    align="center"
                    label="所属流程"
                    prop="flowName"
                  />
                  <el-table-column
                    prop="status"
                    label="流程状态"
                    width="130"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <el-tag type="primary" v-if="scope.row.status == 1">
                        等待审核
                      </el-tag>
                      <el-tag type="success" v-else-if="scope.row.status == 2">
                        审核通过
                      </el-tag>
                      <el-tag type="danger" v-else-if="scope.row.status == 3">
                        审核驳回
                      </el-tag>
                      <el-tag type="info" v-else-if="scope.row.status == 4">
                        流程撤回
                      </el-tag>
                      <el-tag type="info" v-else-if="scope.row.status == 5">
                        审核终止
                      </el-tag>
                      <el-tag type="warning" v-else>等待提交</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    label="发起时间"
                    prop="startTime"
                    :formatter="formatDate"
                  />
                  <el-table-column
                    align="center"
                    label="操作"
                    show-overflow-tooltip
                    width="220"
                  >
                    <template #default="{ row }">
                      <el-button
                        type="text"
                        @click="showDetailwfqd(row, false)"
                      >
                        详情
                      </el-button>
                      <el-button
                        type="text"
                        :disabled="[1, 2, 5].indexOf(row.status) > -1"
                        @click="showDetailwfqd(row, true)"
                      >
                        编辑
                      </el-button>
                      <el-button
                        type="text"
                        :disabled="row.status != 4"
                        @click="deleteDatawfqd(row)"
                      >
                        删除
                      </el-button>
                      <el-button
                        type="text"
                        :disabled="row.status == 2 || row.status == 4"
                        @click="chehuiwfqd(row)"
                      >
                        撤销
                      </el-button>
                      <!-- <el-dropdown style="margin-left: 10px">
                      <el-button type="text">更多</el-button>
                      <el-dropdown-menu slot="dropdown"> -->
                      <!--   <el-dropdown-item>
                          <el-button
                            type="text"
                            :disabled="row.status != 1"
                            @click.native="handlePress(row)"
                          >
                            催办
                          </el-button>
                        </el-dropdown-item> -->
                      <!-- <el-dropdown-item>
                          <el-button type="text" :disabled="[1, 2, 5].indexOf(row.status) > -1"
                            @click.native="showDetailwfqd(row, true)">
                            编辑
                          </el-button>
                        </el-dropdown-item>
                        <el-dropdown-item>
                          <el-button type="text" :disabled="row.status == 2 || row.status == 4 || row.status == 3"
                            @click.native="chehuiwfqd(row)">
                            撤回
                          </el-button>
                        </el-dropdown-item>
                      </el-dropdown-menu>
                    </el-dropdown> -->
                    </template>
                  </el-table-column>
                </el-table>
                <el-pagination
                  background
                  :current-page="wfqdqueryForm.currentPage"
                  :layout="layout"
                  :page-size="wfqdqueryForm.pageSize"
                  :page-sizes="pageSizes"
                  :total="wfqdtotal"
                  @current-change="handleCurrentChangewfqd"
                  @size-change="handleSizeChangewfqd"
                />
              </div>
              <div v-show="activeName === 'cssy'">
                <el-table v-loading="cssylistLoading" :data="cssylist">
                  <el-table-column
                    align="center"
                    label="流程标题"
                    prop="fullName"
                  />
                  <el-table-column
                    align="center"
                    label="所属流程"
                    prop="flowName"
                  />
                  <el-table-column
                    prop="status"
                    label="流程状态"
                    width="130"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <el-tag type="primary" v-if="scope.row.status == 1">
                        等待审核
                      </el-tag>
                      <el-tag type="success" v-else-if="scope.row.status == 2">
                        审核通过
                      </el-tag>
                      <el-tag type="danger" v-else-if="scope.row.status == 3">
                        审核驳回
                      </el-tag>
                      <el-tag type="info" v-else-if="scope.row.status == 4">
                        流程撤回
                      </el-tag>
                      <el-tag type="info" v-else-if="scope.row.status == 5">
                        审核终止
                      </el-tag>
                      <el-tag type="warning" v-else>等待提交</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    label="发起时间"
                    prop="creatorTime"
                    :formatter="formatDate"
                  />
                  <el-table-column
                    align="center"
                    label="操作"
                    show-overflow-tooltip
                    width="120"
                  >
                    <template #default="{ row }">
                      <el-button
                        type="text"
                        @click="showDetailcssy(row, false)"
                      >
                        详情
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <el-pagination
                  background
                  :current-page="cssyqueryForm.currentPage"
                  :layout="layout"
                  :page-size="cssyqueryForm.pageSize"
                  :page-sizes="pageSizes"
                  :total="cssytotal"
                  @current-change="handleCurrentChangecssy"
                  @size-change="handleSizeChangecssy"
                />
              </div>
              <div v-show="activeName === 'cbtz'">
                <el-table v-loading="cblistLoading" :data="cblist">
                  <el-table-column
                    align="center"
                    label="催办人"
                    prop="creatorName"
                    #default="{ row }"
                  >
                    <el-button type="text" @click="handleDetail(row)">
                      {{ row.creatorName }}
                    </el-button>
                  </el-table-column>
                  <el-table-column
                    align="center"
                    label="催办时间"
                    prop="createdTime"
                  />
                  <el-table-column
                    align="center"
                    label="催办内容"
                    prop="reminderContent"
                    show-overflow-tooltip
                  />
                </el-table>
                <el-pagination
                  background
                  :current-page="cblistqueryForm.currentPage"
                  :layout="layout"
                  :page-size="cblistqueryForm.pageSize"
                  :page-sizes="pageSizes"
                  :total="cblisttotal"
                  @current-change="handleCurrentChangecblist"
                  @size-change="handleSizeChangecblist"
                />
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-card>
    </el-row>

    <!-- 消息通知区块：从首页驾驶舱复制，展示未阅/已阅下发通知事项 -->
    <el-card style="margin-bottom: 16px; margin-top: 16px;">
      <div
        class="xiafa-title"
        style="font-size: 16px; margin: 10px 10px 20px 0px"
      >
        <span>消息通知</span>
      </div>
      <xiafa />
    </el-card>

    <!-- 催办消息区块：从首页驾驶舱复制，展示未读催办消息 -->
    <el-card style="margin-bottom: 16px;">
      <div
        class="xiafa-title"
        style="font-size: 16px; margin: 10px 10px 20px 0px"
      >
        <span>催办消息</span>
      </div>
      <CB />
    </el-card>

    <WddbDeal ref="Wddbdeal" @fetchData="refush" />
    <WdcyDeal ref="WdcyDeal" @fetchData="refush" />
    <Chyy ref="chyy" @fetchData="refush" />
    <CssyDeal ref="cssyDeal" />
    <WfqdDeal ref="wfqddeal" />
    <CBDetail ref="detail"></CBDetail>
  </div>
</template>

<script>
  import WddbDeal from '@/views/msg/components/options/WddbDeal.vue'
  import WdcyDeal from '@/views/msg/components/options/WdcyDeal.vue'
  import CssyDeal from '@/views/msg/components/options/CssyDeal.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import Chyy from '@/views/msg/components/chyy.vue'
  import { formatDate } from '@/utils/index'
  import {
    getToDoList,
    my_circulation1,
    my_faqi,
    my_shiyi,
  } from '@/api/setting/msg'
  import { getAllData } from '@/api/contract/home'
  import * as echarts from 'echarts'
  import {
    ymWorkActionsWithdraw,
    ymWorkActionsDelete,
    press,
  } from '@/api/contract/manage'
  import CBDetail from './CBdetail.vue'
  import { getHomeCBList } from '@/api/risk/report'
  // 从首页驾驶舱复制：消息通知和催办消息组件
  import xiafa from './xiafa.vue'
  import CB from '@/views/homes/personal/cb.vue'
  export default {
    name: 'Download',
    components: {
      WddbDeal,
      WdcyDeal,
      Chyy,
      CssyDeal,
      WfqdDeal,
      CBDetail,
      xiafa,
      CB,
    },

    data() {
      return {
        // tab列表配置
        tabList: [
          { title: '待办事宜', name: 'wddb' },
          { title: '已办事宜', name: 'wdcy' },
          { title: '我发起的', name: 'wfqd' },
          { title: '抄送事宜', name: 'cssy' },
          // { title: '催办通知', name: 'cbtz' }, // 暂时注释，先不在此页面展示
        ],
        activeName: 'wddb',
        // 记录各 tab 是否已完成首次加载，避免切换时重复请求
        tabLoaded: {
          wddb: false,
          wdcy: false,
          wfqd: false,
          cssy: false,
          // cbtz: false, // 暂时注释，与上方 tabList 同步
        },
        wddbtotal: 0,
        wddblistLoading: false,
        wddblist: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        pageSizes: [5, 10, 15, 20, 50, 100],
        wddbqueryForm: {
          currentPage: 1,
          pageSize: 10,
        },

        wdcyqueryForm: {
          currentPage: 1,
          pageSize: 10,
        },
        wdcytotal: 0,
        wdcylistLoading: false,
        wdcylist: [],

        wfqdqueryForm: {
          currentPage: 1,
          pageSize: 10,
        },
        wfqdtotal: 0,
        wfqdlistLoading: false,
        wfqdlist: [],

        cssyqueryForm: {
          currentPage: 1,
          pageSize: 10,
        },
        cssytotal: 0,
        cssylistLoading: false,
        cssylist: [],
        cblistLoading: false,
        cblist: [],
        cblisttotal: 0,
        cblistqueryForm: {
          pageNumber: 1,
          pageSize: 10,
          moduleRoute: 'zzfx',
        },
        form: {
          year: new Date().getFullYear().toString(),
          month: '',
        },
        headerInfo: {},

        chats2Xdata: [],
        chats2Ydata: [],
        chats5Xdata: [],
        chats5Ydata: [],
        chats4Xdata: [],
        chats4Ydata: [],
        chats3Xdata: [],
        chats3Ydata: [],
        leiht: [],
        info: [
          { qdje: '' },
          { htsl: '' },
          { wysl: '' },
          { hzfsl: '' },
          { ysje: '' },
          { sjskje: '' },
          { yfje: '' },
          { sjfkje: '' },
        ],
        year: undefined,
        month: undefined,
        qdje: '222', //本年签订合同金额
        htsl: '', //本年签订合同数量
        wysl: '', //违约的合同数量
        hzfsl: '', //合作方数量
        ysje: '', //本年应收款金额
        sjskje: '', //本年实际收款金
        yfje: '', //本年应付款金额
        sjfkje: '', //本年实际付款金额
      }
    },

    mounted() {
      // 审批完成后的消息通知：只刷当前 tab + 待办角标，不全量刷新
      this._updateMsgHandler = (type) => {
        if (type === 0) {
          this.refush()
          this.$refs['wfqddeal'] && this.$refs['wfqddeal'].close()
        }
      }
      this.$bus.$on('updateMsg', this._updateMsgHandler)
    },
    beforeDestroy() {
      // 组件销毁时注销监听，防止内存泄漏和监听器累积
      this.$bus.$off('updateMsg', this._updateMsgHandler)
    },
    created() {
      // 首屏只加载默认 tab（待办事宜）和统计图表
      // 其余 4 个 tab 数据在用户点击时按需加载
      this.fetchDatawddb()
      this.getData()
    },
    methods: {
      refush() {
        // 始终刷新待办数量（角标需要更新）
        this.fetchDatawddb()
        // 若当前不在待办 tab，额外刷新当前 tab，避免同时触发全部 5 个接口
        switch (this.activeName) {
          case 'wdcy':
            this.fetchDatawdcy()
            break
          case 'wfqd':
            this.fetchDatawfqd()
            break
          case 'cssy':
            this.fetchDatacssy()
            break
          case 'cbtz':
            this.fetchDatacblist()
            break
        }
      },
      handleSearch() {
        this.getData()
      },
      handleReset() {
        this.year = undefined
        this.month = undefined
        this.getData()
      },
      getData() {
        getAllData({
          year: this.year,
          month: this.month,
        }).then((res) => {
          this.$set(this.info[0], 'qdje', res.qdje)
          this.$set(this.info[1], 'htsl', res.htsl)
          this.$set(this.info[2], 'wysl', res.wysl)
          this.$set(this.info[3], 'hzfsl', res.hzfsl)
          this.$set(this.info[4], 'ysje', res.ysje)
          this.$set(this.info[5], 'sjskje', res.sjskje)
          this.$set(this.info[6], 'yfje', res.yfje)
          this.$set(this.info[7], 'sjfkje', res.sjfkje)
          this.$set(this.leiht, 0, res.contracttype[0])
          // contracttype
          this.leiht = res.contracttype.slice(0, 6)

          this.chats2Xdata = []
          this.chats2Ydata = []
          this.chats3Xdata = []
          this.chats3Ydata = []
          this.chats4Xdata = []
          this.chats4Ydata = []
          this.chats5Xdata = []
          this.chats5Ydata = []
          let arr = res.orgcount
          arr.forEach((element) => {
            this.chats2Xdata.push(element.NAME)
            this.chats2Ydata.push(element.VALUE)
          })
          let arr1 = res.companyAmount
          arr1.forEach((element) => {
            this.chats5Xdata.push(element.name)
            this.chats5Ydata.push(element.value)
          })
          let arr2 = res.companyCount
          arr2.forEach((element) => {
            this.chats4Xdata.push(element.name)
            this.chats4Ydata.push(element.value)
          })
          let arr3 = res.lyhtqdsl

          arr3.forEach((element) => {
            this.chats3Xdata.push(element.name)
            this.chats3Ydata.push(element.value)
          })
          this.getchats()
        })
      },
      getchats() {
        const chats1 = echarts.init(document.getElementById('chats-1'))
        // let that = this
        // 指定图表的配置项和数据
        chats1.setOption({
          title: {
            text: '各种类的合同情况',
            left: 'left',
          },
          tooltip: {
            trigger: 'item',
          },
          legend: {
            top: '15%',
            left: 'center',
          },
          series: [
            {
              name: '合同数量',
              type: 'pie',
              top: '15%',
              radius: ['20%', '50%'],
              data: this.leiht,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
            },
          ],
        })
        const chats2 = echarts.init(document.getElementById('chats-2'))

        // 指定图表的配置项和数据
        chats2.setOption({
          title: {
            text: '部门合同订单数量',
            left: 'left',
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow',
            },
          },
          xAxis: {
            data: this.chats2Xdata,
            axisLabel: {
              color: 'rgba(0, 0, 0, 1)',
              fontSize: '0.6rem',
              interval: 0, // 设置斜切
              rotate: 65, // 设置斜切
            },
          },
          yAxis: {},
          series: [
            {
              name: '合同订单数量',
              type: 'bar',
              barWidth: '60%',
              data: this.chats2Ydata,
            },
          ],
        })

        // 基于准备好的dom，初始化echarts实例
        const chats3 = echarts.init(document.getElementById('chats-3'))
        // 绘制图表
        chats3.setOption({
          title: {
            text: '历月合同签订数量',
            left: 'left',
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow',
            },
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true,
          },
          xAxis: [
            {
              type: 'category',
              data: [
                '一月',
                '二月',
                '三月',
                '四月',
                '五月',
                '六月',
                '七月',
                '八月',
                '九月',
                '十月',
                '十一月',
                '十二月',
              ],
              axisTick: {
                alignWithLabel: true,
              },
              axisLabel: {
                color: 'rgba(0, 0, 0, 1)',
                fontSize: '0.6rem',
                interval: 0, // 设置斜切
                rotate: 1, // 设置斜切
              },
            },
          ],
          yAxis: [
            {
              type: 'value',
            },
          ],
          series: [
            {
              name: '合同数量',
              type: 'bar',
              barWidth: '60%',
              data: this.chats3Ydata,
            },
          ],
        })

        const chats4 = echarts.init(document.getElementById('chats-4'))

        // 指定图表的配置项和数据
        chats4.setOption({
          title: {
            text: '全体系合同签订数量汇总',
            left: 'center',
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow',
            },
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true,
          },
          xAxis: [
            {
              type: 'category',
              data: this.chats4Xdata,
              axisTick: {
                alignWithLabel: true,
              },
              axisLabel: {
                color: 'rgba(0, 0, 0, 1)',
                fontSize: '0.6rem',
                interval: 0, // 设置斜切
                rotate: 65, // 设置斜切
              },
            },
          ],
          yAxis: [
            {
              type: 'value',
            },
          ],
          series: [
            {
              name: '合同签订数量',
              type: 'bar',
              barWidth: '60%',
              data: this.chats4Ydata,
            },
          ],
        })

        const chats5 = echarts.init(document.getElementById('chats-5'))

        // 指定图表的配置项和数据
        chats5.setOption({
          title: {
            text: '全体系合同付款金额汇总',
            left: 'center',
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow',
            },
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true,
          },
          xAxis: [
            {
              type: 'category',
              data: this.chats5Xdata,
              axisTick: {
                alignWithLabel: true,
              },
              axisLabel: {
                color: 'rgba(0, 0, 0, 1)',
                fontSize: '0.6rem',
                interval: 0, // 设置斜切
                rotate: 65, // 设置斜切
              },
            },
          ],
          yAxis: [
            {
              type: 'value',
            },
          ],
          series: [
            {
              name: '付款金额',
              type: 'bar',
              barWidth: '60%',
              data: this.chats5Ydata,
            },
          ],
        })
      },
      async fetchDatacssy() {
        this.cssylistLoading = true
        try {
          const {
            data: { list, totalCount },
          } = await my_shiyi(this.cssyqueryForm)
          this.cssylist = list
          this.cssytotal = totalCount
          this.tabList[3].title = '抄送事宜(' + totalCount + ')'
          this.tabLoaded.cssy = true
        } catch (error) {
          console.error('获取抄送事宜数据失败:', error)
        } finally {
          this.cssylistLoading = false
        }
      },
      handleCurrentChangecssy(val) {
        this.cssyqueryForm.currentPage = val
        this.fetchDatacssy()
      },
      handleSizeChangecssy(val) {
        this.cssyqueryForm.pageSize = val
        this.fetchDatacssy()
      },
      async fetchDatawfqd() {
        this.wfqdlistLoading = true
        try {
          const {
            data: { list, totalCount },
          } = await my_faqi(this.wfqdqueryForm)
          this.wfqdlist = list
          this.wfqdtotal = totalCount
          this.tabList[2].title = '我发起的(' + totalCount + ')'
          this.tabLoaded.wfqd = true
        } catch (error) {
          console.error('获取我发起的数据失败:', error)
        } finally {
          this.wfqdlistLoading = false
        }
      },
      handleCurrentChangewfqd(val) {
        this.wfqdqueryForm.currentPage = val
        this.fetchDatawfqd()
      },
      handleSizeChangewfqd(val) {
        this.wfqdqueryForm.pageSize = val
        this.fetchDatawfqd()
      },
      async fetchDatawdcy() {
        this.wdcylistLoading = true
        try {
          const {
            data: { list, totalCount },
          } = await my_circulation1(this.wdcyqueryForm)
          this.wdcylist = list
          this.wdcytotal = totalCount
          this.tabList[1].title = '已办事宜(' + totalCount + ')'
          this.tabLoaded.wdcy = true
        } catch (error) {
          console.error('获取已办事宜数据失败:', error)
        } finally {
          this.wdcylistLoading = false
        }
      },
      handleCurrentChangewdcy(val) {
        this.wdcyqueryForm.currentPage = val
        this.fetchDatawdcy()
      },
      handleSizeChangewdcy(val) {
        this.wdcyqueryForm.pageSize = val
        this.fetchDatawdcy()
      },
      async fetchDatawddb() {
        this.wddblistLoading = true
        try {
          const {
            data: { list, totalCount },
          } = await getToDoList(this.wddbqueryForm)
          this.wddblist = list
          this.wddbtotal = totalCount
          this.tabList[0].title = '待办事宜(' + totalCount + ')'
          this.tabLoaded.wddb = true
        } catch (error) {
          console.error('获取待办事宜数据失败:', error)
        } finally {
          this.wddblistLoading = false
        }
      },
      handleCurrentChangewddb(val) {
        this.wddbqueryForm.currentPage = val
        this.fetchDatawddb()
      },
      handleSizeChangewddb(val) {
        this.wddbqueryForm.pageSize = val
        this.fetchDatawddb()
      },
      // tab切换事件
      handleTabClick(tab) {
        this.activeName = tab.name
        // 已加载过的 tab 不重复请求，只有首次切换才发接口
        if (this.tabLoaded[tab.name]) return
        switch (tab.name) {
          case 'wddb':
            this.fetchDatawddb()
            break
          case 'wdcy':
            this.fetchDatawdcy()
            break
          case 'wfqd':
            this.fetchDatawfqd()
            break
          case 'cssy':
            this.fetchDatacssy()
            break
          case 'cbtz':
            this.fetchDatacblist()
            break
        }
      },
      handleDetailwddb(row) {
        let dataRow = JSON.parse(JSON.stringify(row))
        this.$refs.Wddbdeal.show(dataRow, false)
      },
      handleDetailwdcy(row, isEdit) {
        let dataRow = JSON.parse(JSON.stringify(row))
        this.$refs.WdcyDeal.show(dataRow, isEdit)
      },
      handleRebackwdcy(row) {
        this.$refs['chyy'].showModal(row)
      },
      showDetailwfqd(row, isEdit) {
        let dataRow = JSON.parse(JSON.stringify(row))
        this.$refs.wfqddeal.show(dataRow, isEdit)
      },
      async chehuiwfqd(row) {
        this.$baseConfirm(
          '是否确定撤销，撤销后流程退回到初始节点，需重新发起审批！',
          null,
          async () => {
            this.wfqdlistLoading = true
            try {
              const res = await ymWorkActionsWithdraw({
                id: row.id,
                flowId: row.flowId,
              })
              if (res.code == 1) {
                this.$message.success('撤销成功')
                this.fetchDatawfqd()
              }
            } finally {
              this.wfqdlistLoading = false
            }
          }
        )
      },
      async deleteDatawfqd(row) {
        this.$confirm('是否确认删除?', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          const res = await ymWorkActionsDelete({
            id: row.id,
            flowId: row.flowId,
          })
          if (res && res.code == 1) {
            this.$message.success('流程删除成功')
            this.fetchDatawfqd()
          } else {
            this.$message.error('流程删除失败')
          }
        })
      },
      showDetailcssy(row, isEdit) {
        let dataRow = JSON.parse(JSON.stringify(row))
        this.$refs.cssyDeal.show(dataRow, isEdit)
      },
      async handlePress(row) {
        const res = await press({
          id: row.id,
          flowId: row.flowId,
        })
        if (res.code == 1) {
          this.$message.success('催办成功')
        }
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      async fetchDatacblist() {
        this.cblistLoading = true
        try {
          const {
            data: { tlist, totalRecord },
          } = await getHomeCBList(this.cblistqueryForm)
          this.cblist = tlist
          this.cblisttotal = totalRecord
          this.tabList[4].title = '催办通知(' + totalRecord + ')'
          this.tabLoaded.cbtz = true
        } catch (error) {
          console.error('获取催办通知数据失败:', error)
        } finally {
          this.cblistLoading = false
        }
      },
      handleCurrentChangecblist(val) {
        this.cblistqueryForm.pageNumber = val
        this.fetchDatacblist()
      },
      handleSizeChangecblist(val) {
        this.cblistqueryForm.pageSize = val
        this.fetchDatacblist()
      },
      handleDetail(row) {
        this.$refs['detail'].showEdit(row)
      },
    },
  }
</script>
<style scoped lang="scss">
  .el-col > div {
    border: 1px solid #dcdfe5;
    margin-bottom: 10px;
  }

  .header {
    display: flex;
    padding: 15px 20px 0 20px;
    box-shadow: 5px -2px 5px 0;
    background: white;
  }

  .header .input-group {
    width: 180px;
    margin-right: 20px;
  }

  .header .btn-sm {
    height: 38px;
    padding: 0 20px;
  }

  .content {
    padding: 20px;
  }

  .one-item {
    display: flex;
    justify-content: space-between;
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }

  .one-item i {
    font-size: 24px;
  }

  .chats > div > div {
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }

  .box .el-card {
    height: 500px;
    overflow: scroll;
  }

  .box {
    height: 100%;
    overflow: auto;
  }

  .clearfix {
    background: white;
  }

  /* el-tabs 样式优化 */
  ::v-deep .el-tabs__active-bar {
    min-width: 28px;
  }

  ::v-deep .el-tabs__nav-wrap::after {
    height: 1px;
  }

  ::v-deep .el-tabs__item {
    font-size: 14px;
    padding: 0 20px;
  }
</style>
