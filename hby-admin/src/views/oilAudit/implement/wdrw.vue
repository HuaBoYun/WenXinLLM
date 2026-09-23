<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <task-tree @getChildParam="getData" :projectId="this.projectId" />
      </div>
      <div class="right">
        <vab-query-form>
          <el-card shadow="never" v-if="isShow">
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
                    v-model="queryForm.businessType"
                    clearable
                    placeholder="问题单元"
                    v-if="item.name === '问题单元'"
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
              </el-form>
            </vab-query-form-top-panel>
          </el-card>
        </vab-query-form>

        <el-card shadow="never">
          <vab-query-form-right-panel :span="24" v-if="isShow">
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
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list">
            <el-table-column
              align="center"
              label="问题单元"
              prop="businessType"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleRecordList(row)">
                  {{ row.businessType }}
                </el-button>
              </template>
            </el-table-column>

            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="审计问题"
                v-if="item.name === '审计问题'"
                prop="riskPoint"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="审计程序"
                v-if="item.name === '审计程序'"
                prop="suditProcess"
                show-overflow-tooltip
              />
              <el-table-column
                align="center"
                label="完成时间"
                v-if="item.name === '完成时间'"
                prop="finishtime"
                show-overflow-tooltip
                :formatter="formatDate"
              />
              <el-table-column
                align="center"
                label="状态"
                v-if="item.name === '状态'"
                prop="finish"
                show-overflow-tooltip
              >
                <template #default="{ row }">
                  {{ row.finish === 1 ? '已完成' : '未完成' }}
                </template>
              </el-table-column>
            </div>
            <el-table-column align="center" label="" width="1" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="220"
              v-if="isShow"
            >
              <template #default="{ row }">
                <el-button type="text" @click="createDraft(row)">
                  生成底稿
                </el-button>
                <el-button type="text" @click="checkRelate(row)">
                  查看关联底稿
                </el-button>
                <el-button type="text" @click="handleFinal(row)">
                  完成
                </el-button>
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
      </div>
    </div>
    <!-- <TaskTree ref="edit" @fetch-data="fetchData" /> -->
    <record-list-info ref="recordListInfo" />
    <myNewDraftInfo ref="draft" />
    <relateDraftModalList ref="modal" />
  </div>
</template>

<script>
  import { myTaskDisp, newMyTaskFinal } from '@/api/audit/implement'
  import { myTaskList } from '@/oapi/audit/project.js'

  import { formatDate } from '@/utils/index'
  // import DraftManageInfo from './components/myDraftInfo'
  import myNewDraftInfo from '@/views/oilAudit/implement/components/newMyDraftView.vue'
  import relateDraftModalList from '@/views/oilAudit/implement/components/relateDraftModal.vue'
  import RecordListInfo from '@/views/audit/implement/components/RecordListInfo'
  import TaskTree from '@/views/audit/implement/components/TaskTree'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import Template from '@/views/contract/contractManage/template.vue'

  export default {
    name: 'Download',
    props: {
      //项目查看传入
      isShow: {
        type: Boolean,
        default: true,
      },
      projectId: {
        type: Number,
        default: null,
      },
    },
    components: {
      TaskTree,
      RecordListInfo,
      // DraftManageInfo
      myNewDraftInfo,
      relateDraftModalList,
      filterSearch,
      filterTable,
      Template,
    },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          targetId: undefined,
          businessType: '',
          name: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '审计问题' },
          { name: '审计程序' },
          { name: '完成时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-implement-wdrw-search',
        tableKey: 'oilAudit-implement-wdrw-list',
        searchMore: true,
      }
    },
    created() {
      // this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      getFiled() {
        return [{ name: '问题单元', key: 'businessType' }]
      },
      getData(id) {
        this.queryForm.targetId = id
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      resetQueryForm() {
        this.queryForm = {
          targetId: undefined,
          businessType: '',
          name: '',
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
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await myTaskList({
          ...this.queryForm,
          projectId: this.projectId,
        })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      createDraft(row) {
        this.$refs['draft'].showModal(row, true)
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
      async handleRecordList(row) {
        const { data } = await myTaskDisp({ programid: row.programid })
        this.$refs['recordListInfo'].showEdit(data)
      },
      handleFinal(row) {
        this.$baseConfirm('你确定要完成当前项吗', null, async () => {
          const { msg, code } = await newMyTaskFinal({
            operateid: row.operateid,
          })
          if (code === 1) {
            this.$baseMessage(msg, 'success')
          }
          await this.fetchData()
        })
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await doDelete({ ids: row.id })
          if (code == 0) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
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
      checkRelate(row) {
        this.$refs['modal'].showEdit(row.operateid)
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
    min-width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
    background: #fff;
    overflow: scroll;
  }

  .lr-layout > .right {
    width: calc(100% - 210px);
  }
</style>
