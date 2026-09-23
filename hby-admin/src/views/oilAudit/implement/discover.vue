<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-model="queryForm.auditMatters"
                clearable
                placeholder="审计事项"
                v-if="item.name === '审计事项'"
              />
              <el-input
                v-model="queryForm.realname"
                clearable
                placeholder="发现人"
                v-if="item.name === '发现人'"
                :style="{ width: '256px' }"
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.executor.show()"
                v-if="item.name === '发现人'"
              >
                选择
              </el-button>
              <el-select
                v-model="queryForm.status"
                placeholder="事实确认"
                v-if="item.name === '事实确认'"
              >
                <el-option label="是" value="2" />
                <el-option label="未确认" value="1" />
              </el-select>
              <el-select
                v-model="queryForm.recStatus"
                placeholder="是否整改"
                v-if="item.name === '是否整改'"
              >
                <el-option label="是" :value="1" />
                <el-option label="否" :value="0" />
              </el-select>
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
        <el-button type="success" @click="exportData">导出</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="底稿编号" prop="draftNumber">
          <template #default="{ row }">
            <el-button type="text" @click="handleAddOrUpdate(row, true)">
              {{ row.draftNumber }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="底稿名称"
            prop="draftName"
            show-overflow-tooltip
            v-if="item.name === '底稿名称'"
          />
          <el-table-column
            align="center"
            label="审计项目名称"
            prop="projectName"
            v-if="item.name === '审计项目名称'"
          />

          <el-table-column
            align="center"
            label="审计事项"
            prop="auditMatters"
            v-if="item.name === '审计事项'"
          />
          <el-table-column
            align="center"
            label="被审计单位名称"
            prop="auditeeName"
            v-if="item.name === '被审计单位名称'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="创建时间"
            prop="createTime"
            v-if="item.name === '创建时间'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="状态"
            prop="status"
            v-if="item.name === '状态'"
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
        <el-table-column width="1" />
        <!-- <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              :disabled="row.status != 6"
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
    <!-- <LcdyEdit ref="edit" @fetch-data="fetchData" /> -->
    <executor-options ref="executor" @selected="handleExecutorSelected" />
    <!-- <DraftManageInfo ref="edit" @fetch-data="fetchData" /> -->
    <Views ref="edit" @queryData="queryData"></Views>
  </div>
</template>

<script>
  import {
    discoverDelete,
    questionStoreList,
    discoverStatus,
    draftManageDelete,
    questionStoreDetail,
    exportDiscover,
    whetherLeader,
  } from '@/oapi/audit/implement'
  import LcdyEdit from '@/views/setting/system/components/LcdyEdit'
  import DraftManageInfo from './components/myDraftInfo'
  import ExecutorOptions from './components/options/executor.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import Views from '@/views/oilAudit/implement/components/newMyDraftView.vue'
  export default {
    name: 'Download',
    components: {
      LcdyEdit,
      ExecutorOptions,
      DraftManageInfo,
      filterSearch,
      filterTable,
      Views,
    },
    mixins: [searchTableMixis],
    props: {
      //项目查看传参
      isShow: {
        type: Boolean,
        default: true,
      },
      projectId: {
        type: Number,
        default: null,
      },
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          findPeople: '',
          realname: '',
          recStatus: '',
          status: '',
          businessAffiliation: '',
          pageNumber: 1,
          pageSize: 20,
        },
        projectInfo: {},
        cloudEdit: false,
        filedAll: [
          { name: '底稿名称' },
          { name: '审计项目名称' },
          { name: '审计事项' },
          { name: '被审计单位名称' },
          { name: '创建时间' },
          { name: '状态' },
          { name: '底稿编号' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-implement-discover-search',
        tableKey: 'oilAudit-implement-discover-list',
        searchMore: true,
      }
    },
    async created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      let res11 = await whetherLeader() //判断是否为组长
      if (res11.data.ifLeader) {
        this.cloudEdit = true
      } else {
        this.cloudEdit = false
      }
    },
    methods: {
      getFiled() {
        return [
          { name: '审计事项', key: 'businessAffiliation' },
          { name: '发现人', key: 'realname' },
          { name: '事实确认', key: 'status' },
          { name: '是否整改', key: 'recStatus' },
        ]
      },
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
      handleExecutorSelected(node) {
        this.queryForm.realname = node.realname
        this.queryForm.findPeople = node.staffid
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
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await questionStoreList({
          ...this.queryForm,
          projectId: this.projectId,
        })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
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
      handleAddOrUpdate(row, disabled) {
        this.$refs['edit'].showModal(row, false, disabled)
      },
      async exportData() {
        const data = await exportDiscover({ ...this.queryForm })
        let fileName = '审计发现'
        let blob = new Blob([data], {
          type: 'application/vnd.ms-excel',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
    },
  }
</script>

<style scoped>
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }

  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 100%;
  }
</style>
