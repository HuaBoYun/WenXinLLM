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
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.businessAffiliation"
                clearable
                placeholder="审计事项"
                v-if="item.name === '审计事项'"
              />
              <!-- <el-input
                v-model="queryForm.internalType"
                placeholder="请输入问题类型"
                v-if="item.name === '问题类型'"
              ></el-input> -->
              <el-select
                v-model="queryForm.internalType"
                style="width: 100%"
                v-if="item.name === '问题类型'"
                placeholder="问题类型"
              >
                <el-option
                  v-for="item in SJWTData"
                  :key="item.typeId"
                  :value="item.auditType"
                  :label="item.auditType"
                />
              </el-select>
              <el-input
                v-model="queryForm.realname"
                clearable
                placeholder="发现人"
                v-if="item.name === '发现人'"
                :style="{ width: '256px' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs['select'].showEdit('leader')"
                v-if="item.name === '发现人'"
              >
                选择
              </el-button>
            </el-form-item>

            <!-- <el-form-item>
              <el-select v-model="queryForm.status" placeholder="事实确认">
                <el-option label="是" value="2" />
                <el-option label="未确认" value="1" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select v-model="queryForm.recStatus" placeholder="是否整改">
                <el-option label="是" :value="1" />
                <el-option label="否" :value="0" />
              </el-select>
            </el-form-item> -->
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
        <el-button type="success" @click="exportData">导出</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <!-- <el-table-column
          align="center"
          label="审计事项"
          prop="nbsjSheet.auditDiscoverable"
          width="100"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.nbsjSheet.auditDiscoverable }}
            </el-button>
          </template>
        </el-table-column> -->
        <el-table-column
          align="center"
          label="底稿编号"
          prop="sheetcode"
          show-overflow-tooltip
          width="140"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.sheetcode }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="被审计对象"
            v-if="item.name === '被审计对象'"
            show-overflow-tooltip
            prop="orgidnames"
          ></el-table-column>
          <el-table-column
            align="center"
            label="问题类型"
            v-if="item.name === '问题类型'"
            show-overflow-tooltip
            prop="internalType"
          ></el-table-column>

          <el-table-column
            align="center"
            label="审计发现"
            v-if="item.name === '审计发现'"
            prop="auditDiscoverable"
            show-overflow-tooltip
          />

          <el-table-column
            align="center"
            label="发现人"
            v-if="item.name === '发现人'"
            prop="realname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="审计事项"
            v-if="item.name === '审计事项'"
            prop="businessaffiliation"
            show-overflow-tooltip
          />
        </div>
        <el-table-column width="1" />
        <el-table-column
          align="center"
          label="是否上报告"
          prop="ifsbg"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            {{ row.ifsbg === 1 ? '否' : '是' }}
          </template>
        </el-table-column>
        <!-- <el-table-column
          align="center"
          label="是否事实确认"
          prop="status"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            {{ row.status === 1 ? '否' : row.status === 2 ? '是' : '未确认' }}
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="是否整改"
          prop="recStatus"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            {{ row.recStatus === 1 ? '是' : '否' }}
          </template>
        </el-table-column> -->
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleReport(row)">
              {{ row.ifsbg === 1 ? '上报' : '不上报告' }}
            </el-button>
            <!-- <el-button
              type="text"
              :disabled="(row.status !== 2 || row.recStatus === 1) && !cloudEdit"
              @click="handleStatus(row)"
            >
              发起整改
            </el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button> -->
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
    <!-- <LcdyEdit ref="edit" @fetch-data="fetchData" /> -->
    <!-- <executor-options ref="executor" @selected="handleExecutorSelected" /> -->
    <DraftManageInfo ref="edit" @fetch-data="fetchData" />
    <select-team ref="select" @selectTeamList="selectTeamList"></select-team>
  </div>
</template>

<script>
  import {
    discoverDelete,
    discoverList,
    discoverStatus,
    draftManageDelete,
    exportDiscover,
    whetherLeader,
    sjzgIsfalse,
  } from '@/api/audit/implement'
  import LcdyEdit from '@/views/setting/system/components/LcdyEdit'
  import DraftManageInfo from './components/myDraftInfo'
  import ExecutorOptions from './components/options/executor.vue'
  import selectTeam from '@/views/audit/plan/components/selectTeam.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { searchTableMixis } from '@/mixis/index'
  import { getSJWTTypeDatas } from '@/api/audit/implement'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: {
      filterTable,
      filterSearch,
      LcdyEdit,
      ExecutorOptions,
      DraftManageInfo,
      selectTeam,
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
          internalType: '',
          pageNumber: 1,
          pageSize: 20,
        },
        projectInfo: {},
        cloudEdit: false,
        // 筛选、表格头自定义
        filedAll: [
          { name: '被审计对象' },
          { name: '问题类型' },
          { name: '审计发现' },
          { name: '发现人' },
          { name: '审计事项' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'audit-implement-discover-search',
        tableKey: 'audit-implement-discover-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        SJWTData: [],
      }
    },
    async created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.getSelectList()
      let res11 = await whetherLeader() //判断是否为组长
      if (res11.data.ifLeader) {
        this.cloudEdit = true
      } else {
        this.cloudEdit = false
      }
    },
    methods: {
      async getSelectList() {
        let resss = await getSJWTTypeDatas()
        this.SJWTData = resss.data.data || []
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '问题类型', key: 'internalType' },
          { name: '审计事项', key: 'businessAffiliation' },
          { name: '发现人', key: 'realname' },
        ]
        return fields
      },
      handleExecutorSelected(node) {
        this.queryForm.realname = node.realname
        this.queryForm.findPeople = node.staffid
      },
      resetQueryForm() {
        this.queryForm = {
          realname: '',
          businessAffiliation: '',
          internalType: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
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
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            project: project,
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await discoverList(this.queryForm)
        this.projectInfo = project
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
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
      handleReport(row) {
        const isReport = row.ifsbg === 1
        const confirmText = isReport ? '你确定要上报吗' : '你确定要取消上报吗'
        const paramValue = isReport ? 0 : 1

        this.$baseConfirm(confirmText, null, async () => {
          const { msg, code } = await sjzgIsfalse({
            questionid: row.sheetId,
            ifsbg: paramValue,
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
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        const data = await draftManageDelete({ sheetid: row.sheetId })
        this.$refs['edit'].showEdit('detail', data.data)
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
      selectTeamList(val) {
        this.queryForm.realname = val[0].realname
        this.queryForm.findPeople = val[0].staffid
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
</style>
