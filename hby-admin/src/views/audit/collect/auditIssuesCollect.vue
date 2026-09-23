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
                v-if="item.name === '审计项目名称'"
                v-model="queryForm.projectName"
                clearable
                placeholder="审计项目名称"
              />
              <el-input
                v-if="item.name === '审计事项'"
                v-model="queryForm.bsusinessAffiliation"
                clearable
                placeholder="审计事项"
              />
              <el-row v-if="item.name === '发现人'">
                <el-input
                  v-model="queryForm.staffName"
                  clearable
                  placeholder="发现人"
                  :style="{ width: '256px' }"
                  disabled
                />
                <el-button
                  :style="{ marginLeft: '10px' }"
                  type="primary"
                  @click="$refs.executor.showEdit()"
                >
                  选择
                </el-button>
              </el-row>
              <!-- <el-select
                v-if="item.name === '事实确认'"
                v-model="queryForm.status"
                placeholder="事实确认"
              >
                <el-option label="是" value="2" />
                <el-option label="未确认" value="1" />
              </el-select>
              <el-select
                v-if="item.name === '是否整改'"
                v-model="queryForm.recStatus"
                placeholder="是否整改"
              >
                <el-option label="是" :value="1" />
                <el-option label="否" :value="0" />
              </el-select> -->
            </el-form-item>

            <el-form-item>
              <el-button
                icon="el-icon-search"
                type="primary"
                @click="queryData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="resetSearch">重置</el-button>
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
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never">
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
        <!-- <el-button type="success" @click="handleAdd">新建</el-button> -->
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="底稿编号"
          prop="sheetCode"
          show-overflow-tooltip
          width="140"
          #default="{ row }"
        >
          <el-button type="text" @click="handleDetail(row)">
            {{ row.sheetCode }}
          </el-button>
        </el-table-column>

        <template v-if="!loading">
          <div v-for="(item, index) in filedNow" :key="index">
            <el-table-column
              align="center"
              label="审计事项"
              v-if="item.name === '审计事项'"
              prop="businessAffiliation"
              width="100"
              show-overflow-tooltip
            >
              <!-- <el-button type="text" @click="handleDetail(row)">{{ row.businessAffiliation }}</el-button> -->
            </el-table-column>
            <el-table-column
              v-if="item.name === '审计项目名称'"
              align="center"
              label="审计项目名称"
              show-overflow-tooltip
              prop="projectName"
            ></el-table-column>
            <el-table-column
              v-if="item.name === '被审计单位'"
              align="center"
              label="被审计单位"
              show-overflow-tooltip
              prop="orgIdNames"
            ></el-table-column>
            <el-table-column
              v-if="item.name === '审计发现'"
              align="center"
              label="审计发现"
              prop="auditDiscoverable"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              v-if="item.name === '关联工作底稿编号'"
              label="关联工作底稿编号"
              prop="sheetCode"
              show-overflow-tooltip
              width="140"
            />
            <el-table-column
              align="center"
              v-if="item.name === '发现人'"
              label="发现人"
              prop="realname"
              show-overflow-tooltip
            />
            <!-- <el-table-column
              align="center"
              v-if="item.name === '是否事实确认'"
              label="是否事实确认"
              prop="status"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{ row.status === 1 ? "否" : row.status === 2 ? "是" : "未确认" }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              v-if="item.name === '是否整改'"
              label="是否整改"
              prop="recStatus"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{ row.recStatus === 1 ? "是" : "否" }}
              </template>
            </el-table-column> -->
          </div>
        </template>
        <el-table-column width="1"></el-table-column>

        <!-- <el-table-column align="center" label="操作" show-overflow-tooltip width="120">
          <template #default="{ row }">
            <el-button
              type="text"
              :disabled="row.status !== 2"
              @click="handleStatus(row)"
            >
              发起整改
            </el-button>
          </template>
        </el-table-column> -->
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
    <LcdyEdit ref="edit" @fetch-data="fetchData" />
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <MyDraftInfo ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    discoverDelete,
    discoverListwgzz,
    discoverStatus,
    myDraftDetail,
  } from '@/api/audit/implement'
  import { getContractTypes } from '@/api/contract/manage'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import LcdyEdit from '@/views/audit/implement/components/LcdyEdit'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import MyDraftInfo from '@/views/audit/implement/components/myDraftInfo'

  export default {
    mixins: [searchTableMixis],
    name: 'Download',
    components: {
      LcdyEdit,
      ExecutorOptions,
      filterSearch,
      filterTable,
      MyDraftInfo,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          projectName: '',
          businessAffiliation: '',
          staffName: '',
          staffid: '',
          pageNumber: 1,
          pageSize: 20,
        },
        // projectInfo: {},

        /*  */
        typeOptions: [],
        loading: false,
        search: {
          pageSize: 10,
          pageNum: 1,
          tagStatus: '',
        },
        filedAll: [
          { name: '审计事项' },
          { name: '审计项目名称' },
          { name: '被审计单位' },
          { name: '审计发现' },
          { name: '发现人' },
          // { name: "是否事实确认" },
          // { name: "是否整改" },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-collect-discover-search',
        tableKey: 'audit-collect-discover-list',
        searchMore: true,
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
    methods: {
      getFiled() {
        return [
          { name: '审计项目名称', key: 'projectName' },
          { name: '审计事项', key: 'bsusinessAffiliation' },
          { name: '发现人', key: 'staffid' },
          // { name: "事实确认", key: "status" },
          // { name: "是否整改", key: "recStatus" },
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

      handleExecutorSelected(node) {
        console.log('🚀 ~ handleExecutorSelected ~ node:', node)
        this.queryForm.staffName = node[0].realname
        this.queryForm.staffid = node[0].staffid
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          projectName: '',
          bsusinessAffiliation: '',
          staffName: '',
          staffid: '',
          pageNumber: 1,
          pageSize: 20,
        }
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
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await discoverListwgzz(this.queryForm)
        // this.projectInfo = project;
        // console.log(project)
        // let name = []
        //     project.auditStaffName?name.push(project.auditStaffName):''
        //     project.auditOrgName?name.push(project.auditOrgName):''
        //   if (list && list.length) {

        //     list.map((item) => {
        //       item.projectName = project.projectName
        //       item.businessAffiliation = item.nbsjSheet.businessAffiliation
        //       item.auditDiscoverable = item.nbsjSheet.auditDiscoverable
        //       item.targetName =  name.join(',')
        //       item.auditOrgName = project.auditOrgName
        //         let str = []
        //         item.yjfh?str.push(item.yjfh):''
        //         item.ejfh?str.push(item.ejfh) :''
        //       item.approver = str.join(',')
        //     })
        //   }
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await discoverDelete({
            questionid: row.questionId,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      handleStatus(row) {
        this.$baseConfirm('你确定要发起整改吗', null, async () => {
          const { msg, code } = await discoverStatus({
            questionid: row.questionId,
          })
          if (code === 1) {
            this.$baseMessage(msg, 'success')
          }
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      async handleDetail(row) {
        const data = await myDraftDetail({ sheetid: row.sheetId })
        this.$refs['edit'].showEdit('detail', data.data)
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
