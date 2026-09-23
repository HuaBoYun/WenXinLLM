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
                v-model="queryForm.advicecoed"
                clearable
                placeholder="审计通知书编号"
                v-if="item.name === '审计通知书编号'"
              />
              <el-input
                v-model="queryForm.advicename"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
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
      <vab-query-form-right-panel :span="24">
        <!-- <el-tooltip
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
          </el-tooltip> -->
        <el-button
          type="success"
          @click="handleAdd(false, false)"
          v-if="isShow"
        >
          新建
        </el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="项目名称"
          prop="projectname"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleAdd(row, true)"
              style="white-space: pre-line; line-height: 16px"
            >
              {{ row.projectname }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="审计项目实施部门"
          prop="department"
        />
        <el-table-column align="center" label="审计实施时间" prop="sjsstime">
          <template #default="{ row }">{{ formatDay(row.sjsstime) }}</template>
        </el-table-column>
        <el-table-column align="center" label="审计组成员">
          <el-table-column align="center" label="组长" prop="teamleader" />
          <el-table-column align="center" label="副组长" prop="fznames" />
          <el-table-column align="center" label="主审" prop="mainreviewer" />
          <el-table-column align="center" label="助审" prop="helpreviewer" />
        </el-table-column>
        <el-table-column align="center" label="经办人" prop="operator" />
        <!-- <el-table-column align="center" label="审计组长" prop="teamleader" /> -->
        <!-- <el-table-column
          align="center"
          label="审计部（审计中心）主任审批意见"
          prop="proposal"
        /> -->
        <!-- <el-table-column align="center" label="文号" width="170">
<template #default="{ row }">
    <el-button type="text" @click="handleAdd(row, true)">
        {{ row.advicecoed }}
    </el-button>
</template>
      </el-table-column>
      <el-table-column align="center" label="标题" prop="advicename" />
      <el-table-column
        align="center"
        label="创建人"
        prop="tblCreater.realname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="创建时间"
        prop="creatrtime"
        show-overflow-tooltip
      /> -->
        <el-table-column
          align="center"
          label="状态"
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
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="100"
        >
          <template #default="{ row }">
            <el-button
              v-if="isShow"
              type="text"
              @click="handleAdd(row, false)"
              :disabled="row.status !== 0"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleManage(row)"
                    :disabled="!row.status"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item v-if="isShow">
                  <el-button
                    type="text"
                    @click="handleApproval(row)"
                    :disabled="row.status == 1 || row.status == 6 || btnLoading"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>

                <el-dropdown-item v-if="isShow">
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    :disabled="row.status !== 0"
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item v-if="isShow">
                  <el-button
                    type="text"
                    @click="handleOut(row)"
                    :disabled="row.status !== 0"
                  >
                    作废
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
    <NoticeaprEdit ref="edit" @fetchData="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqdDeal" />
  </div>
</template>

<script>
  import {
    getnoticeaprList,
    handleTabs,
    noticeCancel,
    reportExport,
    deleteNotice1,
    cancleNotice,
  } from '@/oapi/audit/preparation'
  import { doDelete } from '@/oapi/table'
  import { UTCformat } from '@/utils'
  import NoticeaprEdit from './components/noticeaprEdit'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import { formatDay } from '@/utils'
  import { getFlowPkInfo } from '@/api/contract/manage.js'

  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'

  export default {
    name: 'Noticeapr',
    components: {
      NoticeaprEdit,
      filterSearch,
      filterTable,
      ProcessList,
      WfqdDeal,
    },
    mixins: [searchTableMixis],
    props: {
      isShow: {
        //项目查看传参
        type: Boolean,
        default: true,
      },
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          advicecoed: '',
          advicename: '',
          pageNumber: 1,
          pageSize: 20,
          Date: [],
        },
        filedAll: [
          { name: '项目名称' },
          { name: '审计实施时间' },
          { name: '审计组成员' },
          { name: '组长' },
          { name: '主审' },
          { name: '助审' },
          { name: '经办人' },
          { name: '审计部（审计中心）主任审批意见' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-prepare-notice-search',
        tableKey: 'oilAudit-prepare-notice-list',
        searchMore: true,
        formatDay: formatDay,
        btnLoading: false,
      }
    },
    created() {
      this.fetchData()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.adviceid,
          tableId: 188,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      getFiled() {
        return [
          // { name: '审计通知书编号', key: 'advicecoed' },
          { name: '项目名称', key: 'advicename' },
        ]
      },
      handleOut(row) {
        this.$baseConfirm('你确定要作废当前项吗', null, async () => {
          const { msg } = await cancleNotice({ adviceid: row.adviceid })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      resetQueryForm() {
        this.queryForm = {
          advicecoed: '',
          advicename: '',
          pageNumber: 1,
          pageSize: 20,
          Date: [],
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
      async toVoid(row) {
        let res = await noticeCancel({
          adviceid: row.adviceid,
        })
        if (res.code == 1) {
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        }
      },
      async fetchData() {
        this.btnLoading = false
        const { Date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }

        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getnoticeaprList({ ...other, startDate, endDate })
        this.list = tlist
        this.list.forEach((item) => {
          item.creatrtime = UTCformat(item.creatrtime)
        })
        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteNotice1({ adviceid: row.adviceid })
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
      handleTab(value) {
        handleTabs({
          adviceid: value.adviceid,
        }).then((res) => {
          if (res.msg === '成功') this.fetchData()
        })
      },
      async handleExport(row) {
        const data = await reportExport({ adviceid: row.adviceid })
        let fileName = 'test'
        let blob = new Blob([data], {
          type: 'application/msword',
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
      handleApproval(row) {
        //提交审批
        try {
          this.$baseConfirm('你确定要提交审批当前项吗', null, async () => {
            this.btnLoading = true
            this.$refs['process'].save(188, row.adviceid)
          })
        } catch (error) {
          this.btnLoading = false
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
