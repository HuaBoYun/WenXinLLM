<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel>
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
              <el-date-picker
                align="right"
                end-placeholder="报告结束日期"
                range-separator="至"
                format="yyyy-MM-dd"
                start-placeholder="报告开始日期"
                type="daterange"
                unlink-panels
                v-model="queryForm.Date"
                value-format="yyyy-MM-dd"
                v-if="item.name === '日期'"
              />
              <!-- 决策单位 -->
              <div
                v-if="item.name === '决策单位'"
                style="display: flex; align-items: center"
              >
                <el-input
                  v-model="queryForm.orgName"
                  placeholder="决策单位"
                  disabled
                  style="width: 200px; margin-right: 8px"
                />
                <el-button
                  type="primary"
                  size="small"
                  @click="$refs.departmentSelect.showEdit()"
                >
                  选择
                </el-button>
              </div>

              <!-- 填报部门 -->
              <div
                v-if="item.name === '填报部门'"
                style="display: flex; align-items: center"
              >
                <el-input
                  v-model="queryForm.deptName"
                  placeholder="填报部门"
                  disabled
                  style="width: 200px; margin-right: 8px"
                />
                <el-button
                  type="primary"
                  size="small"
                  @click="$refs.departmentRef.show()"
                >
                  选择
                </el-button>
              </div>
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
              <el-button type="primary" @click="fetchData('reset')">
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
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="报告名称" prop="reportname">
          <template #default="{ row }">
            <el-button type="text" @click="handleRead(row)">
              {{ row.reportname }}
            </el-button>
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="报告时间"
            prop="reporttime"
            :formatter="formatDay"
            v-if="item.name === '报告时间'"
          />
          <el-table-column
            align="center"
            label="报告类型"
            prop="reporttype"
            show-overflow-tooltip
            v-if="item.name === '报告类型'"
          />
          <el-table-column
            align="center"
            label="报告方式"
            prop="reportmode"
            v-if="item.name === '报告方式'"
          />
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
          <el-table-column
            align="center"
            label="上报状态"
            prop="reportsubstatus"
            v-if="item.name === '上报状态'"
          >
            <template #default="{ row }">
              {{ row.reportsubstatus == 1 ? '已上报' : '未上报' }}
            </template>
          </el-table-column>
        </div>
        <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="
                (row.status && row.status != 0) || createId != row.reporterid
              "
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleSB(row)"
                    :disabled="
                      !(row.status == 6 && row.reportsubstatus != 1) ||
                      createId != row.reporterid
                    "
                  >
                    上报
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handleManage(row)"
                  :disabled="!+row.status"
                >
                  <el-button type="text" :disabled="!+row.status">
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handleApproval(row)"
                  :disabled="!!+row.status"
                >
                  <el-button
                    type="text"
                    :disabled="
                      !!+row.status || btnLoading || createId != row.reporterid
                    "
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleDownload(row)" disabled>
                  <el-button type="text" disabled>导出</el-button>
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handleDelete(row)"
                  :disabled="row.status && row.status != 0"
                >
                  <el-button
                    type="text"
                    :disabled="
                      (row.status && row.status != 0) ||
                      createId != row.reporterid
                    "
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
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <NormalEdit ref="edit" @reloadTable="reloadTable" />
    <NormalRead ref="read" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
    <DepartmentSelect ref="departmentSelect" @submit="handleDepartmentSelect" />
    <Department ref="departmentRef" @selected="handleDeptSelect" />
  </div>
</template>

<script>
  import {
    deleteReport,
    getReportList,
    download,
    download1,
    reportSB,
  } from '@/api/risk/report'
  import { formatDay } from '@/utils/index'
  import NormalEdit from './components/NormalEdit.vue'
  import NormalRead from './components/NormalRead.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import DepartmentSelect from '@/components/departmentSelect.vue'
  import Department from '@/views/audit/report/components/options/department.vue'
  import { getFlowPkInfo } from '@/api/contract/manage'
  export default {
    name: 'NormalReportList',
    components: {
      NormalEdit,
      NormalRead,
      filterSearch,
      filterTable,
      ProcessList,
      WfqdDeal,
      DepartmentSelect,
      Department,
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          type: 'fx_zdy',
          // type: 'fx',
          Date: [],
          orgid: '',
          orgName: '',
          deptid: '',
          deptName: '',
          title: '',
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-report-normal-search',
        tableKey: 'risk-report-normal-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '报告时间' },
          { name: '报告类型' },
          { name: '报告方式' },
          { name: '状态' },
          { name: '上报状态' },
        ], //所有表格项
        filedNow: [],
        btnLoading: false,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    created() {
      // 接收路由参数
      if (this.$route.query.type) {
        this.queryForm.type = this.$route.query.type
      }
      if (this.$route.query.title) {
        this.queryForm.title = this.$route.query.title
      }
      this.fetchData()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
    },
    /**
     * @description: 流程提交回调
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
    /**
     * @description: 提交审批
     * @return {*}
     */
    methods: {
      handleApproval(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        try {
          this.btnLoading = true
          //提交审批
          this.$refs['process'].save(94, row.reportid)
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
          formId: row.reportid,
          tableId: 94,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '日期', key: 'Date' },
          { name: '决策单位', key: 'orgid' },
          { name: '填报部门', key: 'deptid' },
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
       * @description: 下载附件
       * @return {*}
       */
      async handleDownload(row) {
        const data = await download({ reportid: row.reportid })
        // const data = await downloadTest({
        //   reportType: 'fygk',
        //   // orgId: row.orgid,
        //   orgId: row.reportid,
        // })
        let fileName = row.reportname + '.doc'
        let blob = new Blob([data], {
          // type: 'application/vnd.ms-excel',
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
      formatDay(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
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
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      /**
       * @description: 分页，初始化
       * @return {*}
       */
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 重置数据
       * @return {*}
       */
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      /**
       * @description: 重置
       * @return {*}
       */
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      reloadTable() {
        this.fetchData()
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      fetchData(type) {
        this.btnLoading = false
        this.listLoading = true
        if (type && type == 'reset') {
          this.$refs['form'].resetFields()
          // 手动清空筛选字段
          this.queryForm.orgid = ''
          this.queryForm.orgName = ''
          this.queryForm.deptid = ''
          this.queryForm.deptName = ''
          this.queryForm.title = ''
        }
        const { Date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        getReportList({ ...other, startDate, endDate }).then((res) => {
          this.list = res.data.page.list
          this.total = res.data.page.total
          this.listLoading = false
        })
      },
      handleExport() {},
      /**
       * @description: 打开编辑
       * @return {*}
       */
      handleEdit(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$refs['edit'].showEdit(row, 'edit')
      },
      /**
       * @description: 打开新建
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      /**
       * @description: 打开详情
       * @return {*}
       */
      handleRead(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      /**
       * @description: 删除
       * @return {*}
       */
      handleDelete(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteReport({ ids: row.reportid })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      async handleSB(row) {
        const { code } = await reportSB({ reportid: row.reportid })
        if (code == 1) {
          this.$baseMessage('上报成功', 'success', 'vab-hey-message-success')
          await this.fetchData()
        }
      },
      handleDepartmentSelect(selected) {
        this.queryForm.orgid = selected.id
        this.queryForm.orgName = selected.label
      },
      handleDeptSelect(selected) {
        this.queryForm.deptid = selected.id
        this.queryForm.deptName = selected.name
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
