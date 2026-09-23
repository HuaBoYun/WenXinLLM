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
                v-model="queryForm.projectname"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
              />
              <el-input
                clearable
                placeholder="人员"
                v-if="item.name === '人员'"
                v-model="queryForm.realname"
                disabled
                style="width: 187px"
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="openPersonModal"
                v-if="item.name === '人员'"
              >
                选择
              </el-button>
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
          <el-button type="success" @click="handleAdd">新建</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="审计人员"
          width="100"
          prop="auditor.realname"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail('查看', row)">
              {{ row.auditor ? row.auditor.realname : '' }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="参与审计项目名称"
            v-if="item.name === '参与审计项目名称'"
            prop="auditProjectName"
          />

          <el-table-column
            align="center"
            label="总分"
            v-if="item.name === '总分'"
            prop="totalScore"
            show-overflow-tooltip
            width="120"
          />
          <el-table-column
            align="center"
            label="考核结果"
            v-if="item.name === '考核结果'"
            prop="totalScore"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <div v-if="scope.row.totalScore > 90">{{ '优秀' }}</div>
              <div
                v-if="scope.row.totalScore < 90 && scope.row.totalScore > 80"
              >
                {{ '良好' }}
              </div>
              <div
                v-if="scope.row.totalScore < 80 && scope.row.totalScore > 60"
              >
                {{ '合格' }}
              </div>
              <div v-if="scope.row.totalScore < 60">{{ '不合格' }}</div>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="状态"
            v-if="item.name === '状态'"
            prop="status"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{
                row.status == 1
                  ? '审批中'
                  : row.status == 2
                  ? '已退回'
                  : row.status == 3
                  ? '已撤回'
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
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="180"
        >
          <!-- <template #default="{ row }">
            <el-button
              type="text"
              :disabled="!!row.status"
              @click="handleEdit('编辑', row)"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleShenPi(row)"
              :disabled="!!row.status || btnLoading"
            >
              审批
            </el-button>
            <el-button
              type="text"
              :disabled="!!row.status"
              @click="deleteData(row)"
            >
              删除
            </el-button>
          </template> -->
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit('编辑', row)"
              :disabled="!!row.status || createId != row.createstaffid"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    @click="handleManage(row)"
                    :disabled="!row.status"
                    type="text"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    :disabled="
                      !!row.status ||
                      btnLoading ||
                      createId != row.createstaffid
                    "
                    type="text"
                    @click="handleShenPi(row)"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!!row.status || createId != row.createstaffid"
                    @click="deleteData(row)"
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
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <personsModal ref="person" @handlePersonInfo="handlePersonInfo" />
    <evaluationEdit
      ref="edit"
      @handleReload="handleReload"
      @personCheck="personCheck"
    />
    <PersonnelCheckModal ref="personCheck" />
    <ProcessList ref="process" />
    <WfqdDeal ref="wfqdDeal" />
  </div>
</template>
<script>
  import personsModal from './components/components/selectPersonModal.vue'
  import evaluationEdit from './components/evaluationEdit.vue'
  import PersonnelCheckModal from '@/views/audit/structure/components/personnelEdit.vue'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import {
    deletePersonInfo,
    evaluationShenPi,
    loadEvaluationData,
  } from '@/api/audit/structure'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import { getFlowPkInfo } from '@/api/contract/manage'
  export default {
    mixins: [searchTableMixis],
    components: {
      filterSearch,
      filterTable,
      evaluationEdit,
      personsModal,
      ProcessList,
      WfqdDeal,
      PersonnelCheckModal,
    },
    name: 'xxx',
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          projectname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        listLoading: false,
        list: [],
        statusName: [
          '未审批',
          '审批中',
          '已退回',
          '已撤回',
          '已终止',
          '',
          '已完成',
        ],
        // 筛选列表配置
        filedAll: [
          { name: '参与审计项目名称' },
          { name: '总分' },
          { name: '考核结果' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-structure-evaluation-search',
        tableKey: 'audit-structure-evaluation-list',
        searchMore: true,
        btnLoading: false,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    created() {
      this.fetchData()
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
      getFiled() {
        return [
          { name: '项目名称', key: 'projectname' },
          { name: '人员', key: 'realname' },
        ]
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.btnLoading = false
        this.listLoading = true
        const { ...other } = this.queryForm
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await loadEvaluationData({ ...other })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async handleDetail(a, b) {
        await this.$refs['edit'].showEdit(a, b)
      },
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
      },
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
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      handleEdit(a, b) {
        this.$refs['edit'].showEdit(a, b)
      },
      openPersonModal() {
        this.$refs['person'].showEdit()
      },
      handlePersonInfo(v) {
        this.$set(this.queryForm, `staffid`, v[0].staffid)
        this.$set(this.queryForm, `realname`, v[0].realname)
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      deleteData(row) {
        deletePersonInfo({
          staffScoreid: row.staffScoreid,
        }).then((res) => {
          if (res.code == 1) {
            this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
            this.fetchData()
          }
        })
      },
      personCheck(info) {
        this.$refs['personCheck'].showEdit(info, '查看')
      },
      handleReload() {
        this.fetchData()
      },
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.staffScoreid,
          tableId: 15,
        })

        this.$refs.wfqdDeal.show(res.data, false)
      },
      handleShenPi(row) {
        try {
          this.btnLoading = true
          const tableId = 15
          const fromId = row.staffScoreid
          this.$refs['process'].save(tableId, fromId)
        } catch (error) {
          this.btnLoading = false
        } // if (this.statusName[row.status] != '未审批' && row.aprStatus) {
        //   this.$baseMessage('流程进行中', 'error')
        // } else {
        //   this.$refs['process'].show(row, 15)
        // }
        // evaluationShenPi({
        //   staffScoreid: row.staffScoreid,
        // }).then((res) => {
        //   if (res.codes === '1') {
        //     this.$baseMessage('操作成功', 'success')
        //     this.fetchData()
        //   }
        // })
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
