<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-if="item.name === '计划编号'"
                v-model="queryForm.plancode"
                clearable
                placeholder="计划编号"
              />
              <el-input
                v-if="item.name === '计划名称'"
                v-model="queryForm.planname"
                clearable
                placeholder="计划名称"
              />
              <el-row style="display: flex" v-if="item.name === '计划负责人'">
                <el-input
                  v-model="queryForm.realname"
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
              </el-row>
              <el-input
                v-if="item.name === '计划年度'"
                v-model="queryForm.palnyear"
                clearable
                placeholder="计划年度"
              />
              <el-date-picker
                v-if="item.name === '计划时间'"
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
                @click="queryData"
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
            <el-form-item>
              <el-tooltip
                class="item"
                effect="dark"
                content="搜索筛选"
                placement="top"
              >
                <el-popover placement="left" trigger="click">
                  <filter-search
                    v-if="true"
                    :list="searchAll"
                    :name="localKey"
                    @updateSearchShow="initSearch"
                  />
                  <el-button slot="reference" style="height: 32px">
                    <vab-icon icon="filter" :is-custom-svg="true" />
                  </el-button>
                </el-popover>
              </el-tooltip>
            </el-form-item>
            <el-form-item style="cursor: pointer">
              <span
                :class="searchMore ? 'search-more is-opened' : 'search-more'"
                @click="showMore"
              >
                <span>{{ searchMore ? '收起' : '展开' }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-form-item>
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never">
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-tooltip
            class="item"
            effect="dark"
            content="表格筛选"
            placement="top"
          >
            <el-popover placement="right" trigger="click">
              <filter-table
                :list="filedAll"
                :name="tableKey"
                @updateTableShow="initTable"
              />
              <el-button
                slot="reference"
                icon="el-icon-s-grid"
                class="biaoge"
                style="margin-bottom: 10px; margin-right: 10px"
              ></el-button>
            </el-popover>
          </el-tooltip>
          <!-- <el-button
            type="primary"
            @click="handleDownloadTemplate"
            style="margin-bottom: 10px; margin-right: 10px"
          >
            下载模板
          </el-button> -->
          <el-button type="success" @click="handleAdd(false, false)">
            新建
          </el-button>
          <!-- <el-upload
            class="upload-demo"
            :show-file-list="false"
            :action="baseApi + api"
            :headers="headers"
            :on-success="handleSuccess"
          >
            <el-button
              type="success"
              style="margin-bottom: 10px; margin-right: 10px"
            >
              导入
            </el-button>
          </el-upload> -->
        </vab-query-form-right-panel>
      </vab-query-form>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="计划编号"
          prop="plancode"
          width="160"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row, true)">
              {{ row.plancode }}
            </el-button>
          </template>
        </el-table-column>

        <template v-if="!loading">
          <div v-for="(item, index) in filedNow" :key="index">
            <el-table-column
              v-if="item.name === '计划名称'"
              align="center"
              label="计划名称"
              prop="planname"
            />
            <el-table-column
              v-if="item.name === '计划年度'"
              align="center"
              label="计划年度"
              prop="palnyear"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '计划负责人'"
              align="center"
              label="计划负责人"
              prop="principalStaff.realname"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '计划开始时间'"
              align="center"
              label="计划开始时间"
              prop="starttime"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '计划结束时间'"
              align="center"
              label="计划结束时间"
              prop="endtime"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '计划费用估算（元）'"
              align="center"
              label="计划费用估算（元）"
              prop="palncost"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '审批状态'"
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
                    ? '需调整'
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
          </div>
        </template>

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button
              :disabled="
                !(
                  (auditAdmin == 1 && (
                    row.opinionstatus == null ||
                    row.opinionstatus == 0 ||
                    row.opinionstatus == 6
                  )) ||
                  (userInfo.staffid == row.createstaffid && (
                    row.opinionstatus == null ||
                    row.opinionstatus == 0
                  ))
                )
              "
              type="text"
              @click="handleAdd(row, false)"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleManage(row)"
                    :disabled="!row.opinionstatus"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleApproval(row)"
                    :disabled="!!row.opinionstatus || btnLoading"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleDelete(row)"
                    :disabled="
                      row.opinionstatus !== null ||
                      userInfo.staffid != row.createstaffid
                    "
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

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

    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import {
    deleteAuditPlanByPlanId,
    getAuditPlanPageList,
    submitAuditPlanApproval,
  } from '@/api/audit/plan'
  import { getContractTypes } from '@/api/contract/manage'
  import { searchTableMixis } from '@/mixis/index'
  import { UTCformat } from '@/utils'

  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import selectTeam from '@/views/audit/plan/components/selectTeam.vue'
  import PlanLeader from './components/childCom/PlanLeader'
  import IndexEdit from './components/IndexEdit'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']

  export default {
    mixins: [searchTableMixis],

    name: 'Download',
    components: {
      IndexEdit,
      PlanLeader,
      selectTeam,
      filterSearch,
      filterTable,
      ProcessList,
      WfqdDeal,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/auditPlan/import',
        headers: { token },
        list: [],
        planNum: '',
        listLoading: true,
        dialogShowFlag: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          realname: '',
          principalid: '',
          plancode: '',
          planname: '',
          palnyear: '',
          Date: [],
        },

        /*  */
        typeOptions: [],
        loading: false,
        search: {
          pageSize: 10,
          pageNum: 1,
        },
        filedAll: [
          { name: '计划名称' },
          { name: '计划年度' },
          { name: '计划负责人' },
          { name: '计划开始时间' },
          { name: '计划结束时间' },
          { name: '计划费用估算（元）' },
          { name: '审批状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-plan-index-search',
        tableKey: 'audit-plan-index-list',
        searchMore: true,
        btnLoading: false,
        userInfo: JSON.parse(localStorage.getItem('userInfo')),
      }
    },
    created() {
      this.fetchData()
      // this.fetchTypes()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
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
        try {
          this.btnLoading = true
          this.$refs['process'].save(32, row.planid)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.planid,
          tableId: 32,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      getFiled() {
        return [
          { name: '计划编号', key: 'plancode' },
          { name: '计划名称', key: 'planname' },
          { name: '计划负责人', key: 'realname' },
          { name: '计划时间', key: 'Date' },
        ]
      },

      async fetchTypes() {
        const res = await getContractTypes()
        this.typeOptions = res.typeofList.reduce((prev, cur) => {
          const data = cur.childrenList.map((item) => {
            return {
              label: item.typename,
              value: item.typeid,
            }
          })
          return prev.concat(data)
        }, [])
      },
      /*  */

      selectTeamList(val, flagTitle) {
        console.log('111', val)

        if (flagTitle) {
          this.leaderId = val[0].staffid
          this.queryForm.principalid = val[0].staffid
          this.queryForm.realname = val[0].realname
        } else {
          console.log('222', val)
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
        this.queryForm.pageSize = 10
        this.queryForm.pageNum = 1
        this.queryForm.realname = ''
        this.queryForm.principalid = ''
        this.queryForm.plancode = ''
        this.queryForm.planname = ''
        this.queryForm.palnyear = ''
        this.queryForm.Date = []
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
        this.btnLoading = false
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
            auditAdmin,
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
          this.auditAdmin = auditAdmin
          let planNumRes = tlist[0].plancode?.split('-')
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
      handleAdd(row, disabled) {
        console.log('this.planNum', this.planNum)

        this.$refs['edit'].showEdit(row, disabled, this.planNum)
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      handleSuccess(response) {
        if (response.data == '200') {
          this.fetchData()
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },
      handleDelete(row) {
        if (row.opinionstatus !== null) {
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
        this.listLoading = true
        const { code, msg } = await submitAuditPlanApproval({
          planId: row.planid,
        })
        this.listLoading = false
        if (code === 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        }
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleDownloadTemplate() {
        // 获取当前域名和协议
        const baseUrl = window.location.origin
        // 拼接完整的文件URL
        const fileUrl = `${baseUrl}/files/计划管理导入模板.xlsx`

        // 创建一个隐藏的a标签用于下载
        const link = document.createElement('a')
        link.href = fileUrl
        link.setAttribute('download', '计划管理导入模板.xlsx')
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
  ::v-deep .is-never-shadow {
    // margin: -26px;
  }
  // ::v-deep .el-form-item__content {
  //   height: 30px;
  // }
</style>
