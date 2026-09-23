<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel :span="24">
          <el-form
            ref="form"
            checkable
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              v-for="(item, index) in searchItem"
              :key="index"
              :prop="item.key"
            >
              <el-input
                v-model="queryForm.plancode"
                clearable
                placeholder="计划编号"
                v-if="item.name === '计划编号'"
              />
              <el-input
                v-model="queryForm.planName"
                clearable
                placeholder="计划名称"
                v-if="item.name === '计划名称'"
              />

              <el-date-picker
                v-model="queryForm.Date"
                type="daterange"
                align="right"
                unlink-panels
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :picker-options="pickerOptions"
                v-if="item.name === '计划日期'"
              ></el-date-picker>

              <el-select
                v-model="queryForm.planType"
                clearable
                placeholder="计划类型"
                v-if="item.name === '计划类型'"
              >
                <el-option
                  v-for="item in typeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>

              <el-select
                v-model="queryForm.status"
                clearable
                placeholder="请选择审批状态"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '审批状态'"
              >
                <el-option
                  v-for="item in aprstatusList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
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
                type="primary"
                native-type="submit"
                @click="fetchData('reset')"
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
            <el-form-item>
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

    <el-card shadow="never" class="secondCard">
      <vab-query-form-right-panel class="option-row">
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
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleEdit(false, 'add')">
          新建
        </el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="计划编号"
          prop="plancode"
          width="100"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row, 'detail')">
              {{ row.plancode }}
            </el-button>
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="计划名称"
            prop="planName"
            show-overflow-tooltip
            v-if="item.name === '计划名称'"
          />
          <el-table-column
            align="center"
            label="制定机构"
            prop="organization.memo"
            v-if="item.name === '制定机构'"
          />
          <el-table-column
            align="center"
            label="计划开始时间"
            prop="startDate"
            v-if="item.name === '计划开始时间'"
          />
          <el-table-column
            align="center"
            label="计划结束时间"
            prop="endDate"
            v-if="item.name === '计划结束时间'"
          />
          <el-table-column
            align="center"
            label="下发人员"
            prop="issuedStaffName"
            v-if="item.name === '下发人员'"
          />
          <el-table-column
            align="center"
            label="下发时间"
            prop="issueddate"
            v-if="item.name === '下发时间'"
          />
          <el-table-column
            align="center"
            label="是否下发"
            prop="data"
            v-if="item.name === '是否下发'"
          >
            <template #default="{ row }">
              {{ planStatus(row.toIssued) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="审批状态"
            prop="status"
            v-if="item.name === '审批状态'"
          >
            <template #default="{ row }">
              {{
                row.status == 1
                  ? '审批中'
                  : row.status == 2
                  ? '需调整'
                  : row.status == 3
                  ? '已撤销'
                  : row.status == 4
                  ? '已终止'
                  : row.status == 5
                  ? '已跟踪'
                  : row.status == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button
              type="text"
              :disabled="!!+row.status || createId != row.createstaffid"
              @click="handleEdit(row, 'edit')"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="handleManage(row)">
                  <el-button type="text" :disabled="!Number(row.status)">
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleApproval(row)">
                  <el-button
                    type="text"
                    :disabled="
                      !!+row.status ||
                      btnLoading ||
                      createId != row.createstaffid
                    "
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    @click.native="handleIssued(row)"
                    :disabled="
                      !(row.status == 6 && row.toIssued != 1) ||
                      createId != row.createstaffid
                    "
                    type="text"
                  >
                    下发
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleDelete(row)">
                  <el-button
                    type="text"
                    :disabled="!!+row.status || createId != row.createstaffid"
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
      class="pager"
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <PlanEdit ref="edit" @fetchData="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
    <SelectPersonModal
      ref="SelectPersonModal"
      @projectManage="selectP"
      :multiple="true"
    />
    <ZXPerson ref="ZXPerson" @projectManage="selectP" />
  </div>
</template>

<script>
  import {
    getGroupPlanlist,
    riplandel,
    down,
    riskPlanSituation,
    toIssued,
  } from '@/api/systemLog'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { formatDate } from '@/utils'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import PlanRead from '@/views/risk/assessment/task/components/PlanRead.vue'
  import SelectPersonModal from '@/components/duoxuanPerson.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { xiafaListNew } from '@/oapi/audit/preparation'

  export default {
    name: 'PlanList',
    components: {
      PlanEdit: () => import('./components/GroupPlanView.vue'),
      PlanRead,
      filterSearch,
      filterTable,
      ProcessList,
      WfqdDeal,
      SelectPersonModal,
      ZXPerson,
    },
    data() {
      return {
        pickerOptions: {
          shortcuts: [
            {
              text: '最近一周',
              onClick(picker) {
                const end = new Date()
                const start = new Date()
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
                picker.$emit('pick', [start, end])
              },
            },
            {
              text: '最近一个月',
              onClick(picker) {
                const end = new Date()
                const start = new Date()
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
                picker.$emit('pick', [start, end])
              },
            },
            {
              text: '最近三个月',
              onClick(picker) {
                const end = new Date()
                const start = new Date()
                start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
                picker.$emit('pick', [start, end])
              },
            },
          ],
        },
        list: [],
        dialogFormVisible: false,
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          status: '',
          pageNo: 1,
          pageSize: 20,
          plancode: undefined,
          planName: undefined,
          startDate: undefined,
          endDate: undefined,
          planType: undefined,
          Date: undefined,
        },
        aprstatusList: [
          {
            label: '审批中',
            value: '1',
          },
          {
            label: '需调整',
            value: '2',
          },
          {
            label: '已撤销',
            value: '3',
          },
          {
            label: '已终止',
            value: '4',
          },
          {
            label: '已跟踪',
            value: '5',
          },
          {
            label: '已完成',
            value: '6',
          },
          {
            label: '未审批',
            value: '0',
          },
        ],
        typeOptions: [
          {
            label: '年度计划',
            value: 1,
          },
          {
            label: '临时性计划',
            value: 2,
          },
        ],

        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-assessment-plan-search',
        tableKey: 'risk-assessment-plan-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '计划名称' },
          { name: '制定机构' },
          { name: '计划开始时间' },
          { name: '计划结束时间' },
          { name: '下发人员' },
          { name: '下发时间' },
          { name: '是否下发' },
          { name: '审批状态' },
        ], //所有表格项
        filedNow: [],
        btnLoading: false,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
        id: null,
        plancode: '',
      }
    },
    created() {
      this.fetchData()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
    },
    /**
     * @description: 流程提交 回调
     * @return {*}
     */
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        console.log('qwe')
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      /**
       * @description: 提交审批
       * @return {*}
       */
      handleApproval(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        try {
          this.btnLoading = true
          //提交审批
          this.$refs['process'].save(214, row.id)
        } catch (error) {
          this.btnLoading = false
        }
      },
      /**
       * @description: 办理
       * @return {*}
       */
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 214,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '计划编号', key: 'plancode' },
          { name: '计划名称', key: 'planName' },
          { name: '计划日期', key: 'startDate' },
          { name: '计划类型', key: 'planType' },
          { name: '审批状态', key: 'status' },
        ]
        return fields
      },
      initSearch() {
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.localKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.searchNow = tempArr
          } else {
            this.searchNow = this.searchAll
          }

          // 重置非展示搜索项
          this.searchAll.forEach((x) => {
            if (!this.searchNow.some((y) => y.key === x.key)) {
              if (Array.isArray(this.queryForm[x.key])) {
                this.queryForm[x.key] = []
              } else if (this.queryForm[x.key] instanceof Object) {
                this.queryForm[x.key] = {}
              } else {
                this.queryForm[x.key] = ''
              }
            }
          })

          if (this.searchMore) {
            this.searchItem = this.searchNow
          } else {
            this.searchItem = this.searchNow.slice(0, 4)
          }
        })
      },
      // 动态表格开始
      initTable() {
        this.loading = true
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.tableKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.filedNow = tempArr
          } else {
            this.filedNow = this.filedAll
          }
          this.loading = false
        })
      },
      showMore() {
        this.searchMore = !this.searchMore
        console.log(this.searchMore)
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      /**
       * @description: 数字转文字，字典
       * @return {*}
       */
      planStatus(v) {
        return v == 1 ? '已下发' : '未下发'
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      /**
       * @description: 分页 初始化
       * @return {*}
       */
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      async fetchData(type) {
        this.btnLoading = false
        if (type && type == 'reset') {
          this.queryForm.Date = []
          this.$refs['form'].resetFields()
        }
        this.listLoading = true
        const { Date, ...other } = this.queryForm
        if (Date && Date.length) {
          other.startDate = formatDate(Date[0])
          other.endDate = formatDate(Date[1])
        }
        const {
          data: { pageBean },
        } = await getGroupPlanlist(other)

        this.list = pageBean.list
        this.total = pageBean.total
        this.listLoading = false
      },
      /**
       * @description: 下发
       * @return {*}
       */
      async handleIssued(row) {
        this.id = row.id
        this.plancode = row.plancode
        // 如果 secrectLevelId 存在且不为空字符串，则使用 ZXPerson
        if (row.secrectLevelId && row.secrectLevelId !== '') {
          this.$refs.ZXPerson.showEdit(row.secrectLevelId)
        } else {
          this.$refs['SelectPersonModal'].showEdit()
        }
      },
      async selectP(val) {
        // 获取姓名列表并拼接
        const realNames = val.map((item) => item.realname).join(',')
        const ids = val.map((item) => item.staffid).join(',')
        try {
          const res = await toIssued({
            id: this.id,
            staffids: ids,
            staffnames: realNames,
          })
          if (res.code == 1) {
            this.$baseMessage('选择下发人员成功', 'success')
            const arr = val.map((item) => {
              return {
                formId: this.id,
                distributionTitle: this.plancode,
                reciver: item.staffid,
                isread: 0,
                moduleType: 'fxgk',
              }
            })
            // 调用xiafa方法
            xiafaListNew({
              tableId: '642506393923653',
              jsondistribution: JSON.stringify(arr),
            }).then((response) => {
              if (response.msg == '成功') {
                this.$baseMessage('下发通知成功', 'success')
                this.fetchData()
              }
            })
          }
        } catch (error) {
          console.log(error)
        }
      },
      /**
       * @description: 打开编辑
       * @return {*}
       */
      handleEdit(row, type) {
        // if (type == 'edit') {
        //   if (this.createId != row.createstaffid) {
        //     return this.$message.error('只有创建人可以操作')
        //   }
        // }
        this.$refs['edit'].showEdit(row, type)
      },
      /**
       * @description: 打开详情
       * @return {*}
       */
      handleRead(row) {
        this.$refs['read'].showRead(row)
      },
      /**
       * @description: 删除
       * @return {*}
       */
      handleDelete(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        console.log(row)
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await riplandel({
            id: row.id,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }

  .secondCard {
    margin-top: -5px !important;
  }

  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }
</style>
