<template>
  <div>
    <div>
      <!-- <div @click="openSJDX">审计对象指引点击</div>
      <div @click="openSJCXFF">审计程序方法指引点击</div>
      <div @click="openFGZD">法规制度指引点击</div>
      <div @click="openFGZD">问题定性指引点击</div> -->
      <el-row>
        <el-card>
          <div slot="header" class="clearfix">
            <span>待办中心</span>
          </div>
          <el-tabs
            v-model="activeName"
            type="border-card"
            @tab-click="handleTabClick"
          >
            <el-tab-pane
              v-for="(item, index) in tabList"
              :key="index"
              :name="item.component"
              :label="item.title"
            >
              <div v-show="activeName === 'wddb'" class="notice-list">
                <div style="margin-bottom: 10px">
                  <el-button type="primary" @click="openList()">
                    批量审批
                  </el-button>
                </div>
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
              <div v-show="activeName === 'wdcy'" class="notice-list">
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
              <div v-show="activeName === 'wfqd'" class="notice-list">
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
                    width="200"
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
              <div v-show="activeName === 'cssy'" class="notice-list">
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
              <!-- 待发事宜 -->
              <div v-show="activeName === 'dfsy'" class="notice-list">
                <el-table v-loading="dfsylistLoading" :data="dfsylist">
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
                    width="180"
                  >
                    <template #default="{ row }">
                      <el-button
                        type="text"
                        @click="showDetaildfsy(row, false)"
                      >
                        详情
                      </el-button>
                      <el-button type="text" @click="showDetaildfsy(row, true)">
                        编辑
                      </el-button>
                      <el-button type="text" @click="deleteDatadfsy(row)">
                        删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <el-pagination
                  background
                  :current-page="dfsyqueryForm.currentPage"
                  :layout="layout"
                  :page-size="dfsyqueryForm.pageSize"
                  :page-sizes="pageSizes"
                  :total="dfsytotal"
                  @current-change="handleCurrentChangedfsy"
                  @size-change="handleSizeChangedfsy"
                />
              </div>
              <!-- 驳回事宜 -->
              <div v-show="activeName === 'bhsy'" class="notice-list">
                <el-table v-loading="bhsylistLoading" :data="bhsylist">
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
                    width="150"
                  >
                    <template #default="{ row }">
                      <el-button
                        type="text"
                        @click="showDetailbhsy(row, false)"
                      >
                        详情
                      </el-button>
                      <el-button type="text" @click="showDetailbhsy(row, true)">
                        编辑
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
                <el-pagination
                  background
                  :current-page="bhsyqueryForm.currentPage"
                  :layout="layout"
                  :page-size="bhsyqueryForm.pageSize"
                  :page-sizes="pageSizes"
                  :total="bhsytotal"
                  @current-change="handleCurrentChangebhsy"
                  @size-change="handleSizeChangebhsy"
                />
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-row>
    </div>

    <WddbDeal ref="Wddbdeal" @fetchData="refush" />
    <WdcyDeal ref="WdcyDeal" @fetchData="refush" />
    <Chyy ref="chyy" @fetchData="refushWdcy" />
    <CssyDeal ref="cssyDeal" />
    <WfqdDeal ref="wfqddeal" />
    <!-- 审计对象 -->
    <sjdxModal ref="sjdx" />
    <!-- 审计程序方法 -->
    <sjcxffModal ref="sjcxff" />
    <!-- 法规制度方法 -->
    <fgzdModal ref="fgzd" />
    <!-- 批量审批 -->
    <WddbList ref="wddbRef" />
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
  import sjdxModal from '@/components/sjdx/sjdxModal.vue'
  import sjcxffModal from '@/components/sjcxff/sjcxffModal.vue'
  import fgzdModal from '@/components/fgzd/fgzdModal.vue'
  import WddbList from '@/views/msg/components/options/WddbList.vue'

  export default {
    name: 'Download',
    components: {
      WddbDeal,
      WdcyDeal,
      Chyy,
      CssyDeal,
      WfqdDeal,
      sjdxModal,
      sjcxffModal,
      fgzdModal,
      WddbList,
    },

    mounted() {},
    data() {
      return {
        tabList: [
          {
            title: '待办事宜',
            component: 'wddb',
          },
          {
            title: '已办事宜',
            component: 'wdcy',
          },
          {
            title: '我发起的',
            component: 'wfqd',
          },
          {
            title: '抄送事宜',
            component: 'cssy',
          },
          {
            title: '待发事宜',
            component: 'dfsy',
          },
          {
            title: '驳回事宜',
            component: 'bhsy',
          },
        ],
        activeName: 'wddb',
        wddbtotal: 0,
        wddblistLoading: false,
        wddblist: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        pageSizes: [5, 10, 15, 20, 50, 100],
        wddbqueryForm: {
          currentPage: 1,
          pageSize: 5,
        },

        wdcyqueryForm: {
          currentPage: 1,
          pageSize: 5,
        },
        wdcytotal: 0,
        wdcylistLoading: false,
        wdcylist: [],

        wfqdqueryForm: {
          currentPage: 1,
          pageSize: 5,
        },
        wfqdtotal: 0,
        wfqdlistLoading: false,
        wfqdlist: [],

        cssyqueryForm: {
          currentPage: 1,
          pageSize: 5,
        },
        cssytotal: 0,
        cssylistLoading: false,
        cssylist: [],
        // 待发事宜
        dfsyqueryForm: {
          flowName: '',
          status: 4,
          currentPage: 1,
          pageSize: 5,
        },
        dfsytotal: 0,
        dfsylistLoading: false,
        dfsylist: [],
        // 驳回事宜
        bhsyqueryForm: {
          flowName: '',
          status: 3,
          currentPage: 1,
          pageSize: 5,
        },
        bhsytotal: 0,
        bhsylistLoading: false,
        bhsylist: [],
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
      this.$bus.$on('updateMsg', (type) => {
        if (type === 0) {
          this.fetchDatawddb()
          this.fetchDatawdcy()
          this.fetchDatawfqd()
          this.fetchDatacssy()
          this.fetchDatadfsy()
          this.fetchDatabhsy()
        }
      })
      this.$bus.$on('updateMsg', (type) => {
        if (type === 0) {
          this.fetchDatawfqd()
          this.$refs['wfqddeal'].close()
        }
      })
    },
    created() {
      this.fetchDatawddb()
      this.fetchDatawdcy()
      this.fetchDatawfqd()
      this.fetchDatacssy()
      this.fetchDatadfsy()
      this.fetchDatabhsy()
      // this.getData()
    },
    methods: {
      refush() {
        this.fetchDatawddb()
        this.fetchDatawdcy()
        this.fetchDatawfqd()
        this.fetchDatacssy()
        this.fetchDatadfsy()
        this.fetchDatabhsy()
      },
      // 已办事宜撤回后刷新列表
      refushWdcy() {
        this.fetchDatawdcy()
        this.fetchDatawddb() // 撤回后可能回到待办，同时刷新待办列表
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
      handleClick(item, index) {
        this.$forceUpdate()
        this.tabIndex = index
      },
      // tab切换事件
      handleTabClick(tab) {
        // 每次切换tab页重新调用接口
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
          case 'dfsy':
            this.fetchDatadfsy()
            break
          case 'bhsy':
            this.fetchDatabhsy()
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
              } else {
                this.wfqdlistLoading = false
              }
            } catch (error) {
              console.error('撤销失败:', error)
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
      // 待发事宜相关方法
      async fetchDatadfsy() {
        this.dfsylistLoading = true
        try {
          const {
            data: { list, totalCount },
          } = await my_faqi(this.dfsyqueryForm)
          this.dfsylist = list
          this.dfsytotal = totalCount
          this.tabList[4].title = '待发事宜(' + totalCount + ')'
        } catch (error) {
          console.error('获取待发事宜数据失败:', error)
        } finally {
          this.dfsylistLoading = false
        }
      },
      handleCurrentChangedfsy(val) {
        this.dfsyqueryForm.currentPage = val
        this.fetchDatadfsy()
      },
      handleSizeChangedfsy(val) {
        this.dfsyqueryForm.pageSize = val
        this.fetchDatadfsy()
      },
      showDetaildfsy(row, isEdit) {
        let dataRow = JSON.parse(JSON.stringify(row))
        this.$refs.wfqddeal.show(dataRow, isEdit)
      },
      async deleteDatadfsy(row) {
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
            this.fetchDatadfsy()
          } else {
            this.$message.error('流程删除失败')
          }
        })
      },
      // 驳回事宜相关方法
      async fetchDatabhsy() {
        this.bhsylistLoading = true
        try {
          const {
            data: { list, totalCount },
          } = await my_faqi(this.bhsyqueryForm)
          this.bhsylist = list
          this.bhsytotal = totalCount
          this.tabList[5].title = '驳回事宜(' + totalCount + ')'
        } catch (error) {
          console.error('获取驳回事宜数据失败:', error)
        } finally {
          this.bhsylistLoading = false
        }
      },
      handleCurrentChangebhsy(val) {
        this.bhsyqueryForm.currentPage = val
        this.fetchDatabhsy()
      },
      handleSizeChangebhsy(val) {
        this.bhsyqueryForm.pageSize = val
        this.fetchDatabhsy()
      },
      showDetailbhsy(row, isEdit) {
        let dataRow = JSON.parse(JSON.stringify(row))
        this.$refs.wfqddeal.show(dataRow, isEdit)
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
      openSJDX() {
        this.$refs['sjdx'].showEdit()
      },
      openSJCXFF() {
        this.$refs['sjcxff'].showEdit()
      },
      openFGZD() {
        this.$refs['fgzd'].showEdit()
      },
      // 批量审批
      openList() {
        this.$refs['wddbRef'].showEdit()
      },
    },
  }
</script>
<style scoped>
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
    height: 200px !important;
    overflow: scroll;
  }

  .box {
    height: 100%;
    overflow: auto;
  }

  .clearfix {
    background: white;
  }

  .tabView {
    display: flex;
    /* justify-content: space-between; */
    padding-left: 20px;
    padding-right: 30px;
    padding-top: 10px;
    padding-bottom: 10px;
  }

  .tabSelect {
    background: #eeeeee;
  }

  .titleView {
    margin-left: 20px;
    text-align: center;
  }

  .viewNum {
    font-size: 20px;
    color: red;
  }

  .viewTitle {
    font-size: 14px;
    color: #111;
  }
</style>
