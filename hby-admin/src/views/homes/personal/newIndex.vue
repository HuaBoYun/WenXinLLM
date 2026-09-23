<template>
  <div style="background: #f7f7f7">
    <el-card>
      <div
        class="xiafa-title"
        style="font-size: 16px; margin: 10px 10px 20px 0px"
      >
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
            <el-table :data="wddblist" v-loading="wddblistLoading">
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
                  <el-button type="text" @click="handleDetailwdcy(row, false)">
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
              <el-table-column prop="status" label="流程状态" align="center">
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
              <!-- <el-table-column
                align="center"
                label="发起时间"
                prop="creatorTime"
                :formatter="formatDate"
              /> -->
              <el-table-column align="center" label="操作" width="200">
                <template #default="{ row }">
                  <el-button type="text" @click="showDetailwfqd(row, false)">
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
              <el-table-column align="center" label="是否已读" prop="isRead">
                <template #default="{ row }">
                  <el-tag type="success" v-if="row.isRead == 1">已读</el-tag>
                  <el-tag type="danger" v-else>未读</el-tag>
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
                  <el-button type="text" @click="showDetailcssy(row, false)">
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
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="showDetailwfqd(row, false)">
                    详情
                  </el-button>
                  <el-button type="text" @click="showDetailwfqd(row, true)">
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
                width="200"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="showDetailwfqd(row, false)">
                    详情
                  </el-button>
                  <el-button type="text" @click="showDetailwfqd(row, true)">
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
          <div v-show="activeName === 'xxdb'" class="notice-list">
            <el-table v-loading="xxdblistLoading" :data="xxdblist">
              <el-table-column
                align="center"
                label="序号"
                width="60"
                type="index"
              ></el-table-column>
              <el-table-column
                align="center"
                label="通知内容"
                prop="distributionTitle"
              ></el-table-column>
              <el-table-column
                align="center"
                label="类型"
                prop="distributionType"
              >
                <template #default="{ row }">
                  <span>
                    {{ getTypeName(row.distributionType) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="下发人"
                prop="createStaffName"
              ></el-table-column>
              <el-table-column
                align="center"
                label="下发时间"
                prop="createTime"
                show-overflow-tooltip
                :formatter="formatDate"
              />
              <el-table-column
                align="center"
                label="状态"
                prop="isread"
                width="80"
              >
                <template #default="{ row }">
                  <el-tag :type="row.isread === '1' ? 'success' : 'warning'">
                    {{ row.isread === '1' ? '已读' : '未读' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handleView(row)">
                    查看
                  </el-button>
                  <el-button
                    v-if="row.isread === '0'"
                    type="text"
                    @click="handleConfirm(row)"
                  >
                    确认
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页 -->
            <el-pagination
              background
              :current-page="xxdbqueryForm.pageNumber"
              :layout="layout"
              :page-size="xxdbqueryForm.pageSize"
              :total="xxdbtotal"
              @current-change="handleCurrentChangexxdb"
              @size-change="handleSizeChangexxdb"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    <el-card>
      <div class="xiafa-container">
        <div
          class="xiafa-title"
          style="font-size: 16px; margin: 10px 10px 20px 0px"
        >
          <span>消息通知</span>
        </div>
        <xiafa />
      </div>
    </el-card>
    <el-card>
      <div class="xiafa-container">
        <div
          class="xiafa-title"
          style="font-size: 16px; margin: 10px 10px 20px 0px"
        >
          <span>催办消息</span>
        </div>
        <CB />
      </div>
    </el-card>
    <WddbDeal ref="Wddbdeal" @fetchData="refush" />
    <WdcyDeal ref="WdcyDeal" @fetchData="refush" />
    <CssyDeal ref="cssyDeal" />
    <WfqdDeal ref="wfqddeal" />
    <Chyy ref="chyy" @fetchData="refushWdcy" />
  </div>
</template>
<script>
  import {
    my_circulation1,
    my_faqi,
    my_shiyi,
    getToDoList,
    getRemindList,
    getZGInfo,
    getAllMsgNum,
  } from '@/api/setting/msg'
  import {
    getDistributionListPage,
    modifyDistributionInfo,
  } from '@/oapi/setting/system'
  import { formatDate } from '@/utils/index'
  import WddbDeal from '@/views/msg/components/options/WddbDeal.vue'
  import WdcyDeal from '@/views/msg/components/options/WdcyDeal.vue'
  import CssyDeal from '@/views/msg/components/options/CssyDeal.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import Chyy from '@/views/msg/components/chyy.vue'
  import {
    ymWorkActionsWithdraw,
    ymWorkActionsDelete,
  } from '@/api/contract/manage'
  import xiafa from '@/views/homes/personal/xiafa-new.vue'
  import { getTypeData } from '@/oapi/setting/system'
  import CB from './cb.vue'
  export default {
    components: {
      WddbDeal,
      WdcyDeal,
      CssyDeal,
      WfqdDeal,
      Chyy,
      xiafa,
      CB,
    },
    data() {
      return {
        tabList: [
          {
            title: '待办事宜',
            component: 'wddb',
          },
          // {
          //   title: '消息待办',
          //   component: 'xxdb',
          // },
          {
            title: '已办事宜',
            component: 'wdcy',
          },
          {
            title: '我发起的',
            component: 'wfqd',
          },
          {
            title: '知会事宜',
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
        layout: 'total, sizes, prev, pager, next, jumper',
        activeName: 'wddb',
        pageSizes: [5, 10, 50, 100],
        wddblist: [],
        wddbqueryForm: {
          currentPage: 1,
          pageSize: 5,
        },
        wddbtotal: 0,
        wddblistLoading: false,
        wfqdlist: [],
        wfqdqueryForm: {
          currentPage: 1,
          pageSize: 5,
        },
        wfqdtotal: 0,
        wfqdlistLoading: false,
        wdcylist: [],
        wdcyqueryForm: {
          currentPage: 1,
          pageSize: 5,
        },
        wdcytotal: 0,
        wdcylistLoading: false,
        cssylist: [],
        cssyqueryForm: {
          currentPage: 1,
          pageSize: 5,
        },
        cssytotal: 0,
        cssylistLoading: false,
        dfsylist: [],
        dfsyqueryForm: {
          currentPage: 1,
          pageSize: 5,
          status: 4,
        },
        dfsytotal: 0,
        dfsylistLoading: false,
        bhsylist: [],
        bhsyqueryForm: {
          currentPage: 1,
          pageSize: 5,
          status: 3,
        },
        bhsytotal: 0,
        bhsylistLoading: false,
        xxdblist: [],
        xxdbqueryForm: {
          pageNumber: 1,
          pageSize: 5,
          isread: '0', // 只显示未读消息
        },
        xxdbtotal: 0,
        xxdblistLoading: false,
        typeData: [
          // { textValue: 'PGJH', textName: '评估计划' },
          // { textValue: 'ZDFXCJ', textName: '重大风险创建' },
          // { textValue: 'JTPGJH', textName: '集团评估计划' },
          // { textValue: 'FXJCZBCJ', textName: '风险监测指标创建' },
        ],
      }
    },
    mounted() {
      // 性能优化：只加载当前激活的tab（待办事宜），其他tab在切换时懒加载
      this.getDaiban()
    },
    methods: {
      async getDaiban() {
        this.wddblistLoading = true
        try {
          const {
            data: { list, totalCount },
          } = await getToDoList(this.wddbqueryForm)

          this.wddblist = list
          this.wddbtotal = totalCount
          this.tabList[0].title = '待办事宜(' + totalCount + ')'
        } catch (error) {
          console.error('获取待办数据失败:', error)
          this.$message.error('获取数据失败')
        } finally {
          this.wddblistLoading = false
        }
      },
      handleCurrentChangewddb(val) {
        this.wddbqueryForm.currentPage = val
        this.getDaiban()
      },
      handleSizeChangewddb(val) {
        this.wddbqueryForm.pageSize = val
        this.getDaiban()
      },
      handleDetailwddb(row) {
        let dataRow = JSON.parse(JSON.stringify(row))
        this.$refs.Wddbdeal.show(dataRow, false)
      },
      // 添加缺失的 refush 方法
      refush() {
        this.getDaiban()
      },
      // 已办事宜撤回后刷新列表
      refushWdcy() {
        this.fetchDatawdcy()
        this.getDaiban() // 撤回后可能回到待办，同时刷新待办列表
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      // tab切换事件
      handleTabClick(tab) {
        // 每次切换tab页重新调用接口
        switch (tab.name) {
          case 'wddb':
            this.getDaiban()
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
      handleDetailwdcy(row, isEdit) {
        let dataRow = JSON.parse(JSON.stringify(row))
        this.$refs.WdcyDeal.show(dataRow, isEdit)
      },
      handleRebackwdcy(row) {
        this.$refs['chyy'].showModal(row)
      },
      async fetchDatacssy() {
        this.cssylistLoading = true
        try {
          const {
            data: { list, totalCount },
          } = await my_shiyi(this.cssyqueryForm)
          this.cssylist = list
          this.cssytotal = totalCount
          this.tabList[3].title = '知会事宜(' + totalCount + ')'
        } catch (error) {
          console.error('获取知会事宜数据失败:', error)
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
      showDetailcssy(row, isEdit) {
        let dataRow = JSON.parse(JSON.stringify(row))
        this.$refs.cssyDeal.show(dataRow, isEdit)
      },
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
      // async fetchDataxxdb() {
      //   this.xxdblistLoading = true
      //   try {
      //     const {
      //       data: { tlist, totalRecord },
      //     } = await getDistributionListPage(this.xxdbqueryForm)
      //     this.xxdblist = tlist || []
      //     this.xxdbtotal = totalRecord || 0
      //     this.tabList[1].title = '消息待办(' + totalRecord + ')'
      //   } catch (error) {
      //     console.error('获取数据失败:', error)
      //     this.$message.error('获取数据失败，请稍后重试')
      //   } finally {
      //     this.xxdblistLoading = false
      //   }
      // },
      // handleCurrentChangexxdb(val) {
      //   this.xxdbqueryForm.pageNumber = val
      //   this.fetchDataxxdb()
      // },
      // handleSizeChangexxdb(val) {
      //   this.xxdbqueryForm.pageSize = val
      //   this.fetchDataxxdb()
      // },
      handleView(row) {
        this.$message.info(`查看消息：${row.distributionTitle}`)
        // 这里可以添加查看详情的逻辑
      },
      async handleConfirm(row) {
        try {
          const res = await modifyDistributionInfo({
            isread: 1,
            distributionId: row.distributionId,
          })

          if (res.code == 1) {
            this.$message.success('确认成功')
            this.fetchDataxxdb() // 刷新列表
          }
        } catch (error) {
          console.error('确认失败:', error)
          this.$message.error('确认失败，请稍后重试')
        }
      },
      getTypeName(type) {
        const typeItem = this.typeData.find((item) => item.textValue === type)
        return typeItem ? typeItem.textName : '未知类型'
      },
      async fetchTypeData() {
        const res = await getTypeData()
        if (res && res.data) {
          this.typeData = res.data
        }
      },
    },
  }
</script>

<style scoped lang="scss">
  h5 {
    font-size: 18px;
    margin: 0 0 10px 0;
    color: #333;
  }

  .el-col > div {
    border: 1px solid #dcdfe5;
    margin-bottom: 10px;
  }

  .page {
    padding: 20px;
  }

  .highlight {
    color: red; /* 设置文字颜色为红色 */
    font-weight: bold; /* 加粗文字 */
  }
</style>
