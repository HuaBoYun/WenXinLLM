<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.plannumber"
                clearable
                placeholder="计划编号"
                v-if="item.name === '计划编号'"
              />

              <el-input
                v-model="queryForm.planname"
                clearable
                placeholder="计划名称"
                v-if="item.name === '计划名称'"
              />

              <el-date-picker
                v-model="queryForm.time"
                clearable
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                format="yyyy-MM-dd"
                range-separator="-"
                type="daterange"
                value-format="yyyy-MM-dd"
                v-if="item.name === '时间'"
              />
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
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
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
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <!-- <el-table-column type="selection" width="55" /> -->
        <el-table-column align="center" label="计划编号" prop="plannumber">
          <template #default="{ row }">
            <el-button
              style="color: red"
              type="text"
              @click="handleDetail(row)"
            >
              {{ row.plannumber }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="计划名称"
            prop="planname"
            v-if="item.name === '计划名称'"
          />

          <el-table-column
            align="center"
            label="被测试公司"
            prop="testedorgs"
            width="340"
            show-overflow-tooltip
            v-if="item.name === '被测试公司'"
          />
          <el-table-column
            align="center"
            label="计划开始时间"
            prop="starttime"
            show-overflow-tooltip
            :formatter="formatDate"
            v-if="item.name === '计划开始时间'"
          />
          <el-table-column
            align="center"
            label="计划结束时间"
            prop="endtime"
            show-overflow-tooltip
            :formatter="formatDate"
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
            label="是否启动"
            prop="data"
            v-if="item.name === '是否启动'"
          >
            <template #default="{ row }">
              {{ row.toIssued == 1 ? '已启动' : '未启动' }}
            </template>
          </el-table-column>
          <!-- <el-table-column
            align="center"
            label="是否下发"
            prop="data"
            v-if="item.name === '是否下发'"
          >
            <template #default="{ row }">
              {{ row.toIssued == 1 ? '已下发' : '未下发' }}
            </template>
          </el-table-column> -->

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
              :disabled="!!Number(row.status) || createId != row.creatid"
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
                      !!Number(row.status) ||
                      btnLoading ||
                      createId != row.creatid
                    "
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <!-- <el-dropdown-item>
                  <el-button
                    @click.native="handleIssued(row)"
                    :disabled="!(row.status == 6 && row.toIssued != 1)"
                    type="text"
                  >
                    下发
                  </el-button>
                </el-dropdown-item> -->
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!(row.status == 6 && row.toIssued != 1)"
                    @click="handleStart(row)"
                  >
                    启动
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleDelete(row)">
                  <el-button
                    type="text"
                    :disabled="!!Number(row.status) || createId != row.creatid"
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
      class="pagination"
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
    <TestPlanView ref="edit" @fetch-data="fetchData" menuKey="TestPlan" />
    <TestListInfo ref="testListInfo" />
    <ApportionView ref="apportionView" />
    <SelectPersonModal
      ref="SelectPersonModal"
      @projectManage="selectP"
      :multiple="true"
      :formSecrectId="formSecrectId"
    />
  </div>
</template>

<script>
  // import { getList } from '@/api/systemLog'
  import {
    groupCtrltestPlanList,
    groupDelete,
    startPlan,
    toIssued,
    startIssued,
  } from '@/api/internal/plan'
  import { formatDay } from '@/utils/index'
  // import { doDelete } from '@/api/table'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ApportionView from '@/views/internal/internalTest/components/options/ApportionView'
  import TestPlanView from '@/views/internal/internalTest/components/TestPlanView'
  import TestListInfo from '@/views/internal/internalTest/components/TestListInfo'
  import SelectPersonModal from '@/components/duoxuanPerson.vue'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import processApproval from '@/mixins/processApproval'
  import { xiafaListNew } from '@/oapi/audit/preparation'
  export default {
    name: 'Plan',
    components: {
      TestPlanView,
      TestListInfo,
      ApportionView,
      filterTable,
      filterSearch,
      SelectPersonModal,
      WfqdDeal,
      ProcessList,
    },
    mixins: [processApproval],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          planname: '',
          plannumber: '',
          time: [],
          status: '',
          pageNumber: 1,
          pageSize: 20,
        },

        filedAll: [
          { name: '计划名称' },
          { name: '被测试公司' },
          { name: '计划开始时间' },
          { name: '计划结束时间' },
          { name: '下发人员' },
          { name: '下发时间' },
          // { name: '是否下发' },
          { name: '是否启动' },
          { name: '审批状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'internal-internalTest-plan-search',
        tableKey: 'internal-internalTest-plan-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        btnLoading: false,
        formSecrectId: null,
        id: null,
        plannumber: '',
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      handleApproval(row) {
        // if (this.createId != row.creatid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.handleProcessApproval(215, row.id)
        // try {
        //   this.btnLoading = true
        //   this.$refs['process'].save(215, row.id)
        // } catch (error) {
        //   this.btnLoading = false
        // }
      },
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 215,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '计划编号', key: 'plannumber' },
          { name: '计划名称', key: 'planname' },
          { name: '时间', key: 'time' },
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
                this.queryForm[x.key] = null
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
      showMore() {
        this.searchMore = !this.searchMore
        console.log(this.searchMore)
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
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
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = {
          planname: '',
          plannumber: '',
          time: [],
          status: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        const { time, ...other } = this.queryForm
        let starttime_max = ''
        let starttime_min = ''
        if (time.length > 0) {
          starttime_min = time[0]
          starttime_max = time[1]
        }
        this.listLoading = true
        const {
          data: {
            pageBean: { records, total },
          },
        } = await groupCtrltestPlanList({
          starttime_max,
          starttime_min,
          ...other,
        })
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        // if (this.createId != row.creatid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$refs['edit'].showEdit(row)
      },
      handleDetail(row) {
        // this.$refs['testListInfo'].showEdit(row)
        this.$refs['edit'].showEdit(row, true)
      },
      handleCommand(command) {
        // switch (command) {
        //   case 'allocation':
        //     console.log('分配')
        //     break
        //   case 'start':
        //     console.log('启动')
        //     break
        //   case 'delete':
        //   this.handleDelete()
        //     break
        // }
      },
      handleAllocation(row) {
        // console.log('planstatus', row.planstatus)
        if (row.planstatus == '未启动') {
          this.$refs['apportionView'].show(row)
        } else {
          this.$message({
            message: '该方案已启动或已完成',
            type: 'error',
          })
        }
      },
      handleStart(row) {
        this.$baseConfirm('你确定要启动当前项吗', null, async () => {
          const { msg, code, data } = await startIssued({
            id: row.id,
          })
          if (code == 1) {
            this.$baseMessage('启动成功', 'success')
            await this.fetchData()
            // 调用xiafa方法
            xiafaListNew({
              tableId: '647125403299909',
              jsondistribution: data.data.jsonString,
            }).then((response) => {
              if (response.msg == '成功') {
                this.$baseMessage('下发通知成功', 'success')
                this.fetchData()
              }
            })
          }
        })
      },

      handleDelete(row) {
        // if (this.createId != row.creatid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code, data } = await groupDelete({
            selectProjectid: row.id,
          })
          if (code == 1) {
            this.$baseMessage('删除成功', 'success')
            await this.fetchData()
          }
        })
      },
      /**
       * @description: 下发
       * @return {*}
       */
      async handleIssued(row) {
        this.id = row.id
        this.formSecrectId = row.secrectLevelId
        this.plannumber = row.plannumber
        this.$refs['SelectPersonModal'].showEdit()
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
            // 为每个id创建单独的xiafaData对象
            const arr = val.map((item) => {
              return {
                formId: this.id,
                distributionTitle: this.plannumber,
                reciver: item.staffid,
                isread: 0,
                moduleType: 'nkhg',
              }
            })
            // 调用xiafa方法
            xiafaListNew({
              tableId: '647125403299909',
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
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
</style>
