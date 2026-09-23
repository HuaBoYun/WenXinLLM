<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-if="item.name === '项目名称'"
                v-model="queryForm.plancode"
                clearable
                placeholder="计划编号"
              />
              <el-input
                v-model="queryForm.planname"
                clearable
                placeholder="计划名称"
              />
              <el-input
                v-model="queryForm.palnyear"
                clearable
                placeholder="计划年度"
              />
              <el-date-picker
                v-model="queryForm.Date"
                type="daterange"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              ></el-date-picker>
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
              <el-button
                native-type="submit"
                type="primary"
                @click="resetSearch"
              >
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>

    <vab-query-form-right-panel>
      <el-button type="success" @click="handleAdd(false, false)">
        新建
      </el-button>
    </vab-query-form-right-panel>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="计划编号"
        prop="plancode"
        width="170"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row, true)">
            {{ row.plancode }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="计划名称" prop="planname" />
      <el-table-column
        align="center"
        label="计划年度"
        prop="palnyear"
        show-overflow-tooltip
      />

      <el-table-column
        align="center"
        label="审批状态"
        prop="opinionstatus"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{
            row.opinionstatus == 1
              ? '审批中'
              : row.opinionstatus == 2
              ? '已退回'
              : row.opinionstatus == 3
              ? '已撤回'
              : row.opinionstatus == 4
              ? '已终止'
              : row.opinionstatus == 5
              ? '已跟踪'
              : row.opinionstatus == 6
              ? '已完成'
              : '未审批'
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button
            type="text"
            :disabled="row.opinionstatus == 1 || row.opinionstatus == 6"
            @click="handleAdd(row, false)"
          >
            修改
          </el-button>
          <!-- <el-button
            type="text"
            @click="handleSubmit(row)"
            :disabled="row.opinionstatus"
          >
            提交审批
          </el-button> -->
          <el-dropdown style="margin-left: 10px">
            <el-button type="text">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button
                  type="text"
                  @click.native="handleManage(row, false)"
                  :disabled="row.opinionstatus != 1 && row.opinionstatus != 6"
                >
                  办理
                </el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  :disabled="row.opinionstatus == 1 || row.opinionstatus == 6"
                  type="text"
                  @click="handleApproval(row)"
                >
                  提交审批
                </el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  :disabled="row.opinionstatus == 1 || row.opinionstatus == 6"
                  @click="handleDelete(row)"
                  type="text"
                >
                  删除
                </el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <!-- <el-button
            type="text"
            @click="handleDelete(row)"
            :disabled="row.opinionstatus"
          >
            删除
          </el-button> -->
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <index-edit ref="edit" @fetch-data="fetchData"></index-edit>

    <!-- <plan-leader ref="planLeader" @planList="planList"></plan-leader> -->
    <!-- 选择组长组员子组件 -->
    <select-team ref="select" @selectTeamList="selectTeamList"></select-team>
    <ProcessList ref="process" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import {
    createPlan,
    deleteAuditPlanByPlanId,
    getAuditPlanPageList,
    submitAuditPlanApproval,
  } from '@/oapi/audit/plan'
  import { UTCformat } from '@/utils'
  import selectTeam from '../project/components/formComponents/selectTeam.vue'
  import PlanLeader from './components/childCom/PlanLeader'
  import IndexEdit from './components/IndexEdit'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/setting/system.js'

  export default {
    name: 'Download',
    components: { IndexEdit, PlanLeader, selectTeam, ProcessList, WfqdDeal },
    data() {
      return {
        list: [],
        planNum: '',
        listLoading: true,
        dialogShowFlag: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          principalid: '',
          Date: [],
        },
      }
    },
    created() {
      this.fetchData()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      handleApproval(row) {
        //提交审批
        this.listLoading = true
        this.$refs['process'].save(160, row.planid)
      },
      // 办理
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.planid,
          tableId: 160,
        })
        this.$refs.wfqddeal.show(res.data, false)
      },
      selectTeamList(val, flagTitle) {
        if (flagTitle) {
          this.leaderId = val[0].staffid
          this.queryForm.principalid = val[0].staffid
        } else {
          let arrStr = ''
          let arr = []
          val.forEach((item) => {
            arr.push(item.realname)
          })
          arrStr = arr.join(',')
          this.tableData[this.sIndex].zyNames = arrStr
          //拿到组员id字符串
          let arrStrZy = ''
          let arrZy = []
          val.forEach((item) => {
            arrZy.push(item.staffid)
          })
          arrStrZy = arrZy.join(',')
          this.zyStaffids = arrStrZy
        }
      },
      showGroupLeader(sIndex) {
        this.$refs['select'].showEdit('leader')
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        let { Date, principalid, ...other } = this.queryForm
        let planStartDate = ''
        let planEndDate = ''
        if (Date) {
          planStartDate = Date[0]
          planEndDate = Date[1]
        }
        if (principalid) {
          principalid = this.leaderId
        } else {
          principalid = ''
        }
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getAuditPlanPageList({
          ...other,
          principalid,
          planStartDate,
          planEndDate,
        })
        this.listLoading = false

        let planNum = ''
        if (tlist && tlist.length) {
          tlist.forEach((item) => {
            item.starttime = UTCformat(item.starttime)
            item.endtime = UTCformat(item.endtime)
          })
          this.list = tlist
          this.total = totalRecord
          if (tlist[0].plancode) {
            let planNumRes = tlist[0].plancode.split('-')
            if (planNumRes && planNumRes.length == 2) {
              planNum = tlist[0].plancode
            } else {
              planNum = 'APL-1'
            }
          }
        } else {
          planNum = 'APL-1'
        }
        this.list = tlist
        this.total = totalRecord
        this.planNum = planNum
      },
      handleAdd(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum)
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      handleDelete(row) {
        if (row.opinionstatus != 0) {
          this.$baseMessage('该状态不能删除', 'error')
        } else {
          this.$baseConfirm('你确定要删除当前项吗', null, async () => {
            const { msg } = await deleteAuditPlanByPlanId({
              planid: row.planid,
            })
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            await this.fetchData()
          })
        }
      },
      //提交
      async handleSubmit(row) {
        // const { msg, code } = await submitAuditPlanApproval({
        //   planId: row.planid,
        // })
        // if (code == 1) {
        //   this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        // }
        // this.fetchData()
        const tableId = 32
        const fromId = row.planid
        this.$refs['process'].save(tableId, fromId)
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleNewAdd() {},
    },
  }
</script>
