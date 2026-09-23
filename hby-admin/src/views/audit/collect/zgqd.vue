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
                v-if="item.name === '业务编号'"
                v-model="queryForm.issuesCode"
                clearable
                placeholder="业务编号"
              />
              <el-input
                v-if="item.name === '问题名称'"
                v-model="queryForm.issuesName"
                clearable
                placeholder="问题名称"
              />
              <el-input
                v-if="item.name === '问题标题'"
                v-model="queryForm.issuesTitle"
                clearable
                placeholder="问题标题"
              />
              <el-select
                v-model="queryForm.issuesType"
                clearable
                placeholder="问题来源"
                v-if="item.name === '问题来源'"
              >
                <el-option label="审计" value="1" />
                <el-option label="内控" value="2" />
                <el-option label="非系统实施" value="3" />
                <el-option label="外部审计" value="4" />
                <el-option label="风险" value="5" />
              </el-select>
              <el-input
                v-if="item.name === '被审计对象'"
                v-model="queryForm.auditObjectName"
                clearable
                placeholder="被审计对象"
              />
              <el-input
                v-if="item.name === '项目名称'"
                v-model="queryForm.projectName"
                clearable
                placeholder="项目名称"
              />
              <el-input
                v-if="item.name === '项目编号'"
                v-model="queryForm.projectNo"
                clearable
                placeholder="项目编号"
              />
              <el-date-picker
                v-if="item.name === '拟稿日期'"
                v-model="queryForm.Date"
                clearable
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始日期"
                :style="{ width: '100%' }"
                type="daterange"
                value-format="yyyy-MM-dd"
              />
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
        <el-button
          type="primary"
          @click="handleDownloadTemplate"
          style="margin-bottom: 10px; margin-right: 10px"
        >
          下载模板
        </el-button>
        <el-upload
          class="upload-demo"
          :show-file-list="false"
          :action="baseApi + api"
          :headers="headers"
          :on-success="handleSuccess"
          style="display: inline-block"
        >
          <el-button
            type="success"
            style="margin-bottom: 10px; margin-right: 10px"
          >
            导入
          </el-button>
        </el-upload>
        <el-button type="success" @click="handleAdd">新建</el-button>
        <el-button type="success" @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="业务编号"
          prop="issuesCode"
          width="100"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.issuesCode }}
            </el-button>
          </template>
        </el-table-column>

        <template v-if="!loading">
          <div v-for="(item, index) in filedNow" :key="index">
            <!-- <el-table-column
              v-if="item.name === '问题名称'"
              align="center"
              label="问题名称"
              prop="issuesName"
              show-overflow-tooltip
            /> -->
            <el-table-column
              v-if="item.name === '问题来源'"
              align="center"
              label="问题来源"
              prop="issuesType"
              show-overflow-tooltip
              #default="{ row }"
            >
              {{
                ['审计', '内控', '非系统实施', '外部审计', '风险'][
                  Number(row.issuesType) - 1
                ]
              }}
            </el-table-column>
            <!-- <el-table-column
              v-if="item.name === '问题编号'"
              align="center"
              label="问题编号"
              prop="proCode"
              show-overflow-tooltip
            ></el-table-column> -->
            <!-- <el-table-column
              v-if="item.name === '项目编号'"
              align="center"
              label="项目编号"
              prop="projectNo"
              show-overflow-tooltip
            ></el-table-column> -->
            <el-table-column
              v-if="item.name === '项目名称'"
              align="center"
              label="项目名称"
              prop="projectName"
              show-overflow-tooltip
            ></el-table-column>
            <!-- <el-table-column
              v-if="item.name === '被审计对象'"
              align="center"
              label="被审计对象"
              prop="auditObjectName"
              show-overflow-tooltip
            ></el-table-column> -->
            <!-- <el-table-column
              v-if="item.name === '拟稿人'"
              align="center"
              label="拟稿人"
              prop="createStaffName"
              show-overflow-tooltip
            /> -->
            <el-table-column
              v-if="item.name === '问题名称'"
              align="center"
              label="问题名称"
              prop="issuesName"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '问题标题'"
              align="center"
              label="问题标题"
              prop="issuesTitle"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '责任人'"
              align="center"
              label="责任人"
              prop="responsiblePersonName"
              show-overflow-tooltip
            />
            <el-table-column
              v-if="item.name === '责任部门'"
              align="center"
              label="责任部门"
              prop="responsibleDeptName"
              show-overflow-tooltip
            />
            <!-- <el-table-column
              v-if="item.name === '责任部门'"
              align="center"
              label="风险等级"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                <div :class="getRiskLevelClass(row.riskLevel)">
                  {{ getRiskLevelText(row.riskLevel) }}
                </div>
              </template>
            </el-table-column> -->

            <el-table-column
              align="center"
              label="状态"
              v-if="item.name === '状态'"
              prop="state"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                {{
                  row.status == 7
                    ? '整改中'
                    : row.status == 8
                    ? '整改已完成'
                    : row.status == 9
                    ? '未销号'
                    : row.status == 10
                    ? '再次整改'
                    : row.status == 11
                    ? '关闭'
                    : '未整改'
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
              type="text"
              :disabled="!!(row.status && row.status != 0)"
              @click="handleEdit(row)"
            >
              修改
            </el-button>
            <el-button
              type="text"
              :disabled="!!(row.status && row.status != 0)"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
            <el-button
              type="text"
              :disabled="!!(row.status && row.status != 0)"
              @click="handleCLose(row)"
              v-if="showCloseBtn"
            >
              关闭
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
    <zgqdEdit
      v-if="showZgqdEdit"
      ref="edit"
      @fetch-data="fetchData"
      @closeDialog="closeDialog"
    />
    <executor-options ref="executor" @selected="handleExecutorSelected" />
    <ProcessList ref="process" @fetch-data="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import {
    getIssuesList,
    getIssuesDetail,
    exportIssuesList,
    delIssues,
    closeIssues,
  } from '@/api/zgzz/index.js'
  import { downloadTemplateFn } from '@/oapi/ypns_zhgl/ipqd.js'
  import { getContractTypes, getFlowPkInfo } from '@/api/contract/manage'
  import { searchTableMixis } from '@/mixis/index'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import zgqdEdit from './components/edit'
  import ExecutorOptions from '@/views/audit/implement/components/options/executor.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getAuthListForUser } from '@/api/setting/auths'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: {
      zgqdEdit,
      ExecutorOptions,
      filterSearch,
      filterTable,
      ProcessList,
      WfqdDeal,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/zgzz/import',
        headers: { token },
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          issuesName: '',
          issuesCode: '',
          issuesTitle: '',
          issuesType: '',
          auditObjectName: '',
          projectName: '',
          projectNo: '',
          Date: '',
          startDate: '',
          endDate: '',
          pageNumber: 1,
          pageSize: 20,
        },
        projectInfo: {},
        typeOptions: [],
        loading: false,
        search: {
          pageSize: 10,
          pageNum: 1,
          tagStatus: '',
        },
        filedAll: [
          { name: '问题来源' },
          { name: '项目名称' },
          { name: '问题名称' },
          { name: '问题标题' },
          { name: '责任人' },
          { name: '责任部门' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-collect-zgqd-search',
        tableKey: 'audit-collect-zgqd-list',
        searchMore: true,
        projectInfo: {},
        showZgqdEdit: false,
        statusMap: {
          7: '整改中',
          8: '整改完成',
          9: '未销号问题',
          10: '再次整改',
          11: '关闭',
        },
        showCloseBtn: false,
      }
    },
    created() {
      getAuthListForUser({ moduleType: 'zhjd', type: 2 }).then((res) => {
        if (
          res &&
          res.data &&
          res.data.rightList &&
          res.data.rightList.length
        ) {
          const btnRightList = res.data.rightList
          btnRightList.some((x) => {
            if (x.perms === 'closebtn') {
              this.showCloseBtn = x.visible === 1
            }
          })
        }
      })
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
        //提交审批
        this.$refs['process'].save(49, row.issuesId)
      },
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.issuesId,
          tableId: 49,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      getFiled() {
        return [
          { name: '业务编号', key: 'issuesCode' },
          { name: '问题名称', key: 'issuesName' },
          { name: '问题标题', key: 'issuesTitle' },
          { name: '状态', key: 'status' },
          { name: '问题来源', key: 'issuesType' },
          { name: '被审计对象', key: 'auditObjectName' },
          { name: '项目名称', key: 'projectName' },
          { name: '项目编号', key: 'projectNo' },
          { name: '拟稿日期', key: 'Date' },
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
      handleExecutorSelected(node) {
        this.queryForm.createStaffName = node.realname
        this.queryForm.staffid = node.staffid
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data).split(' ')[0]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          issuesName: '',
          issuesCode: '',
          issuesTitle: '',
          issuesType: '',
          auditObjectName: '',
          projectName: '',
          projectNo: '',
          Date: '',
          startDate: '',
          endDate: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      async resetSearch() {
        await this.resetQueryForm()
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
        try {
          const obj = { ...this.queryForm }
          obj.startDate = obj.Date[0]
          obj.endDate = obj.Date[1]
          delete obj.Date
          const response = await getIssuesList(obj)

          // 添加详细的日志输出，帮助排查问题
          console.log('===== 接口返回完整数据 =====')
          console.log('response对象:', response)
          console.log('response.code:', response ? response.code : 'undefined')
          console.log('response.data:', response ? response.data : 'undefined')
          console.log('response.data类型:', response && response.data ? typeof response.data : 'undefined')

          if (response && response.data) {
            console.log('response.data.tlist:', response.data.tlist)
            console.log('response.data.totalRecord:', response.data.totalRecord)
          }
          console.log('===========================')

          // 检查返回数据结构
          if (response && response.data && response.data.tlist) {
            this.list = response.data.tlist
            this.total = response.data.totalRecord || 0
            console.log('✅ 数据加载成功，记录数:', this.list.length)
          } else {
            console.error('❌ 接口返回数据格式异常:', response)
            this.$baseMessage('获取数据失败：后端返回数据格式不正确', 'error')
            this.list = []
            this.total = 0
          }
        } catch (error) {
          console.error('❌ 获取整改清单失败:', error)
          this.$baseMessage('获取数据失败: ' + (error.message || '未知错误'), 'error')
          this.list = []
          this.total = 0
        } finally {
          this.listLoading = false
        }
      },
      handleSuccess(response) {
        if (response.data == '200') {
          this.fetchData()
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },
      // 下载模板
      async downLoadTemplate() {
        const data = await downloadTemplateFn()
        let filename = decodeURI(
          data.headers['content-disposition'].split('=')[1].split("'")[2]
        )
        let blob = new Blob([data.data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      async handleExport(row) {
        const obj = { ...this.queryForm }
        obj.startDate = obj.Date[0]
        obj.endDate = obj.Date[1]
        delete obj.Date
        const data = await exportIssuesList(obj)
        let fileName = '整改清单'
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
      handleAdd() {
        this.showZgqdEdit = true
        this.$nextTick(() => {
          this.$refs['edit'].showEdit('add', null)
        })
      },
      handleDetail(row) {
        this.showZgqdEdit = true
        this.$nextTick(async () => {
          const res = await getIssuesDetail({ issuesId: row.issuesId })
          this.$refs['edit'].showEdit('detail', res.data)
        })
      },
      handleEdit(row) {
        this.showZgqdEdit = true
        this.$nextTick(async () => {
          const res = await getIssuesDetail({ issuesId: row.issuesId })
          this.$refs['edit'].showEdit('edit', res.data)
        })
      },
      closeDialog() {
        this.showZgqdEdit = false
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await delIssues({ issuesId: row.issuesId })
          if (code == 1) {
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
      async handleCLose(row) {
        const { msg, code } = await closeIssues({
          issuesId: row.issuesId,
          status: 11,
        })
        if (code == 1) {
          this.$baseMessage(msg, 'success')
        }
        await this.fetchData()
      },
      handleDownloadTemplate() {
        // 获取当前域名和协议
        const baseUrl = window.location.origin
        // 拼接完整的文件URL
        const fileUrl = `${baseUrl}/files/整改清单导入模板.xlsx`

        // 创建一个隐藏的a标签用于下载
        const link = document.createElement('a')
        link.href = fileUrl
        link.setAttribute('download', '整改清单导入模板.xlsx')
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      },
      // 获取风险等级文本
      getRiskLevelText(level) {
        const levelMap = {
          '1': '很低',
          '2': '较低',
          '3': '中等',
          '4': '较高',
          '5': '很高',
        }
        return levelMap[level] || '-'
      },
      // 获取风险等级对应的CSS类名
      getRiskLevelClass(level) {
        const classMap = {
          '1': 'calCount1',
          '2': 'calCount2',
          '3': 'calCount3',
          '4': 'calCount4',
          '5': 'calCount5',
        }
        return classMap[level] || ''
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

  /* 风险等级样式 */
  .calCount1 {
    color: #52ffb7;
  }
  .calCount2 {
    color: #33d73b;
  }
  .calCount3 {
    color: #ffb500;
  }
  .calCount4 {
    color: #ff7f00;
  }
  .calCount5 {
    color: #e92129;
  }
</style>
