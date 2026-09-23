<template>
  <div class="system-log-container">
    <vab-query-form>
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
                v-model="queryForm.registerNo"
                clearable
                placeholder="登记编号"
                v-if="item.name === '登记编号'"
              />
              <el-input
                v-model="queryForm.projectName"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
              />
              <el-input
                v-model="queryForm.contractorFullName"
                clearable
                placeholder="发包方名称"
                v-if="item.name === '发包方'"
              />
              <el-select
                v-model="queryForm.projectStatus"
                clearable
                placeholder="项目状态"
                v-if="item.name === '项目状态'"
              >
                <el-option label="登记" :value="1" />
                <el-option label="承接" :value="2" />
                <el-option label="执行" :value="3" />
                <el-option label="完成" :value="4" />
              </el-select>
              <el-select
                v-model="queryForm.reportStatus"
                clearable
                placeholder="报备状态"
                v-if="item.name === '报备状态'"
              >
                <el-option label="未报备" :value="0" />
                <el-option label="已报备" :value="1" />
                <el-option label="已审核" :value="2" />
              </el-select>
              <el-select
                v-model="queryForm.isFirstTalkReport"
                clearable
                placeholder="首谈报备"
                v-if="item.name === '首谈报备'"
              >
                <el-option label="否" :value="0" />
                <el-option label="是" :value="1" />
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
        <el-button type="success" @click="tongbu()">同步</el-button>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="登记编号"
            prop="registerNo"
            width="150"
            v-if="item.name === '登记编号'"
          />
          <el-table-column
            align="center"
            label="项目名称"
            prop="projectName"
            min-width="200"
            v-if="item.name === '项目名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.projectName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="发包方"
            prop="contractorFullName"
            min-width="180"
            v-if="item.name === '发包方'"
          />
          <el-table-column
            align="center"
            label="项目金额"
            prop="projectAmount"
            width="120"
            v-if="item.name === '项目金额'"
          >
            <template #default="{ row }">
              {{ formatMoney(row.projectAmount) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="项目状态"
            prop="projectStatus"
            width="100"
            v-if="item.name === '项目状态'"
          >
            <template #default="{ row }">
              <el-tag :type="getProjectStatusType(row.projectStatus)">
                {{ getProjectStatusName(row.projectStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="报备状态"
            prop="reportStatus"
            width="100"
            v-if="item.name === '报备状态'"
          >
            <template #default="{ row }">
              <el-tag :type="getReportStatusType(row.reportStatus)">
                {{ getReportStatusName(row.reportStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="首谈报备"
            prop="isFirstTalkReport"
            width="100"
            v-if="item.name === '首谈报备'"
          >
            <template #default="{ row }">
              <el-tag :type="row.isFirstTalkReport === 1 ? 'warning' : 'info'">
                {{ row.isFirstTalkReport === 1 ? '是' : '否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="登记人"
            prop="registerUserName"
            width="100"
            v-if="item.name === '登记人'"
          />
          <el-table-column
            align="center"
            label="登记时间"
            prop="registerTime"
            width="150"
            :formatter="formatDate"
            v-if="item.name === '登记时间'"
          />
          <el-table-column
            align="center"
            label="创建时间"
            prop="createTime"
            width="150"
            :formatter="formatDate"
            v-if="item.name === '创建时间'"
          />
        </div>
        <el-table-column align="center" label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" size="small" @click="handleDetail(row)">详情</el-button>
            <el-button type="text" size="small" @click="handleEdit(row)">编辑</el-button>

            <el-button
              type="text"
              size="small"
              @click="handleReport(row)"
              v-if="row.isFirstTalkReport === 1 && row.reportStatus === 0"
            >
              报备
            </el-button>
            <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      class="pagination"
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <ProjectEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getProjectInfoRegisterList,
    getProjectInfoRegisterById,
    saveProjectInfoRegister,
    deleteProjectInfoRegister,
    generateRegisterNo,
    batchDeleteProjectInfoRegister,

    updateReportStatus
  } from '@/api/contract/projectInfoRegister'
  import { synchronization } from '@/api/contract/opposite'
  import ProjectEdit from '@/views/contract/project/components/ProjectEdit'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'

  export default {
    name: 'Download',
    components: { ProjectEdit, filterTable, filterSearch },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          registerNo: '',
          projectName: '',
          contractorFullName: '',
          projectStatus: null,
          reportStatus: null,
          isFirstTalkReport: null,
          pageNumber: 1,
          pageSize: 20,
        },
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
        value1: '',
        value2: '',
        multipleSelection: [], // 多选数据

        localKey: 'contract-project-info-register-search',
        tableKey: 'contract-project-info-register-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '登记编号' },
          { name: '项目名称' },
          { name: '发包方' },
          { name: '项目金额' },
          { name: '项目状态' },
          { name: '报备状态' },
          { name: '首谈报备' },
          { name: '登记人' },
          { name: '登记时间' },
          { name: '创建时间' },
        ],
      }
    },
    created() {
      this.fetchData()
      //初始化表格&筛选
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initTable()
      this.initSearch()
    },
    methods: {
      // 动态筛选 动态表格 初始化数据&相关方法
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      getFiled() {
        let fields = [
          { name: '登记编号', key: 'registerNo' },
          { name: '项目名称', key: 'projectName' },
          { name: '发包方', key: 'contractorFullName' },
          { name: '项目状态', key: 'projectStatus' },
          { name: '报备状态', key: 'reportStatus' },
          { name: '首谈报备', key: 'isFirstTalkReport' },
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
        return formatDate(data)
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
      resetQueryForm() {
        this.queryForm = {
          registerNo: '',
          projectName: '',
          contractorFullName: '',
          projectStatus: null,
          reportStatus: null,
          isFirstTalkReport: null,
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
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
        try {
          const response = await getProjectInfoRegisterList(this.queryForm)
          if (response.code === 1) {
            this.list = response.data.list || []
            this.total = response.data.total || 0
          } else {
            this.$message.error(response.msg || '查询失败')
            this.list = []
            this.total = 0
          }
        } catch (error) {
          this.$message.error('查询失败：' + error.message)
          this.list = []
          this.total = 0
        }
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      async handleDetail(row) {
        try {
          const response = await getProjectInfoRegisterById(row.projectId)
          if (response.code === 1) {
            await this.$refs['edit'].showEdit('detail', response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      async handleEdit(row) {
        try {
          const response = await getProjectInfoRegisterById(row.projectId)
          if (response.code === 1) {
            await this.$refs['edit'].showEdit('edit', response.data)
          } else {
            this.$message.error(response.msg || '获取详情失败')
          }
        } catch (error) {
          this.$message.error('获取详情失败：' + error.message)
        }
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          try {
            const response = await deleteProjectInfoRegister(row.projectId)
            if (response.code === 1) {
              this.$baseMessage(response.msg || '删除成功', 'success')
              await this.fetchData()
            } else {
              this.$baseMessage(response.msg || '删除失败', 'error')
            }
          } catch (error) {
            this.$baseMessage('删除失败：' + error.message, 'error')
          }
        })
      },
      // 多选处理
      handleSelectionChange(selection) {
        this.multipleSelection = selection
      },
      // 批量删除
      handleBatchDelete() {
        if (this.multipleSelection.length === 0) {
          this.$message.warning('请选择要删除的项目')
          return
        }
        this.$baseConfirm('你确定要删除选中的项目吗', null, async () => {
          try {
            const projectIds = this.multipleSelection.map(item => item.projectId)
            const response = await batchDeleteProjectInfoRegister(projectIds)
            if (response.code === 1) {
              this.$baseMessage(response.msg || '批量删除成功', 'success')
              await this.fetchData()
            } else {
              this.$baseMessage(response.msg || '批量删除失败', 'error')
            }
          } catch (error) {
            this.$baseMessage('批量删除失败：' + error.message, 'error')
          }
        })
      },

      // 报备处理
      handleReport(row) {
        this.$baseConfirm('确认将此项目标记为已报备吗？', null, async () => {
          try {
            const response = await updateReportStatus(row.projectId, 1)
            if (response.code === 1) {
              this.$baseMessage('报备状态更新成功', 'success')
              await this.fetchData()
            } else {
              this.$baseMessage(response.msg || '报备状态更新失败', 'error')
            }
          } catch (error) {
            this.$baseMessage('报备状态更新失败：' + error.message, 'error')
          }
        })
      },
      // 格式化金额
      formatMoney(amount) {
        if (!amount) return '0.00'
        return parseFloat(amount).toLocaleString('zh-CN', {
          minimumFractionDigits: 2,
          maximumFractionDigits: 2
        })
      },
      // 获取项目状态名称
      getProjectStatusName(status) {
        const statusMap = {
          1: '登记',
          2: '承接',
          3: '执行',
          4: '完成'
        }
        return statusMap[status] || '未知'
      },
      // 获取项目状态类型
      getProjectStatusType(status) {
        const typeMap = {
          1: 'info',
          2: 'warning',
          3: 'primary',
          4: 'success'
        }
        return typeMap[status] || 'info'
      },
      // 获取报备状态名称
      getReportStatusName(status) {
        const statusMap = {
          0: '未报备',
          1: '已报备',
          2: '已审核'
        }
        return statusMap[status] || '未知'
      },
      // 获取报备状态类型
      getReportStatusType(status) {
        const typeMap = {
          0: 'info',
          1: 'warning',
          2: 'success'
        }
        return typeMap[status] || 'info'
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      //判断处理
      handleCommand(command) {
        switch (command) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit()
            break
        }
      },
      //同步
      tongbu() {
        synchronization().then(async (res) => {
          if (res.code == '1') {
            await this.$baseMessage(res.msg, 'success')
            await this.fetchData()
          }
        })
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
