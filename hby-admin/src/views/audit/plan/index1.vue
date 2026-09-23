<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.plancode"
              clearable
              placeholder="计划编号2"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.planname"
              clearable
              placeholder="计划名称"
            />
          </el-form-item>
          <!-- <el-form-item>
            <el-input
              v-model="queryForm.principalid"
              clearable
              placeholder="计划负责人"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="showGroupLeader"
            >
              选择
            </el-button>
          </el-form-item> -->
          <el-form-item>
            <el-input
              v-model="queryForm.palnyear"
              clearable
              placeholder="计划年度"
            />
          </el-form-item>
          <el-form-item>
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
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
      <vab-query-form-left-panel>
        <span></span>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <!-- <el-button type="success" @click="handleAdd(false, false)">
          新建
        </el-button> -->
        <el-button type="success" @click="handleNewAdd">生成调整计划</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>

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
      <!-- <el-table-column
        align="center"
        label="计划负责人"
        prop="principalStaff.realname"
        show-overflow-tooltip
      /> -->
      <!-- <el-table-column
        align="center"
        label="计划开始时间"
        prop="starttime"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="计划结束时间"
        prop="endtime"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="计划费用估算（元）"
        prop="palncost"
        show-overflow-tooltip
      /> -->
      <el-table-column
        align="center"
        label="审批状态"
        prop="opinionstatus"
        show-overflow-tooltip
      >
        <!-- <template #default="{ row }">
          {{
            row.opinionstatus == 0
              ? '未审批'
              : row.opinionstatus == 1
              ? '审批中'
              : row.opinionstatus == 2
              ? '已退回'
              : row.opinionstatus == 3
              ? '已完成'
              : row.opinionstatus == 4
              ? '审批终止'
              : '已生成项目'
          }}
        </template> -->
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
          <!-- <el-button
            type="text"
            v-if="
              row.opinionstatus == '0' ||
              row.opinionstatus == '2' ||
              !row.opinionstatus
            "
            @click="handleAdd(row, false)"
          >
            修改
          </el-button> -->
          <el-button
            type="text"
            @click="handleSubmit(row)"
            :disabled="row.opinionstatus"
          >
            提交审批
          </el-button>
          <el-button
            type="text"
            @click="handleDelete(row)"
            :disabled="row.opinionstatus"
          >
            删除
          </el-button>
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
  </div>
</template>

<script>
  import {
    createPlan,
    deleteAuditPlanByPlanId,
    getAuditPlanPageList,
    submitAuditPlanApproval,
  } from '@/api/audit/plan'
  import { UTCformat } from '@/utils'
  import selectTeam from '../project/components/formComponents/selectTeam.vue'
  import PlanLeader from './components/childCom/PlanLeader'
  import IndexEdit from './components/IndexEdit'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'

  export default {
    name: 'Download',
    components: { IndexEdit, PlanLeader, selectTeam, ProcessList },
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
    /**
     * @description 提交流程后，回调，刷新页面列表数据
     * @param {*}
     * @return {*}
     */
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      /**
       * @description 计划负责人 选人组件选择后，返回值，把值存起来，做后续
       * @param {*}
       * @return {*}
       */
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
      /**
       * @description 计划负责人  唤起选人组件
       * @param {*}
       * @return {*}
       */
      showGroupLeader(sIndex) {
        this.$refs['select'].showEdit('leader')
      },
      /**
       * @description 重置筛选，把筛选条件清空
       * @param {*}
       * @return {*}
       */
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      /**
       * @description 重置按钮，点击触发的函数
       * @param {*}
       * @return {*}
       */
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      /**
       * @description 分页，选择每页几条数据，查询每页多少条数据
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description 分页，选择页码，查询第几页的数据
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      /**
       * @description 查询按钮，回到第一页，查询数据
       * @param {*}
       * @return {*}
       */
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description 查询接口，条件查询
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 数据请求
       * @return {*}
       */
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

        let planNum = ''
        if (tlist && tlist.length) {
          tlist.forEach((item) => {
            item.starttime = UTCformat(item.starttime)
            item.endtime = UTCformat(item.endtime)
          })
          this.list = tlist
          this.total = totalRecord
          let planNumRes = tlist[0].plancode.split('-')
          if (planNumRes && planNumRes.length == 2) {
            planNum = tlist[0].plancode
          } else {
            planNum = 'APL-1'
          }
        } else {
          planNum = 'APL-1'
        }
        this.list = tlist
        this.total = totalRecord
        this.planNum = planNum
        this.listLoading = false
      },
      /**
       * @description 新建按钮触发，唤起新建弹框
       * @param {*}
       * @return {*}
       */
      handleAdd(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum)
      },
      /**
       * @description 详情/编辑 按钮触发，唤起 详情/编辑 弹框
       * @param {*}
       * @return {*}
       */
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      /**
       * @description  删除按钮触发，删除当前行数据
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
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
      /**
       * @description  提交流程，提交计划流程
       * @param {*}
       * @return {*}
       */
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
      /**
       * @description  生成调整计划 按钮触发，请求接口，后台生成调整计划
       * @param {*}
       * @return {*}
       */
      handleNewAdd() {
        createPlan().then((res) => {
          if (res.data.tips) {
            this.$baseMessage(res.data.tips, 'error', 'vab-hey-message-error')
            return
          }
          this.fetchData()
        })
      },
    },
  }
</script>
