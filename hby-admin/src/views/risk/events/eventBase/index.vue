<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <type-tree @select="select" ref="typeTree" />
      </div>
      <div class="right">
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
                  <el-input
                    v-model="queryForm.code"
                    clearable
                    placeholder="事件编号"
                    v-if="item.name === '事件编号'"
                  />
                  <el-input
                    v-model="queryForm.name"
                    clearable
                    placeholder="事件名称"
                    v-if="item.name === '事件名称'"
                  />
                  <el-date-picker
                    align="right"
                    end-placeholder="结束日期"
                    range-separator="至"
                    format="yyyy-MM-dd"
                    start-placeholder="开始日期"
                    type="daterange"
                    unlink-panels
                    v-model="queryForm.Date"
                    value-format="yyyy-MM-dd"
                    v-if="item.name === '日期'"
                  />
                  <div
                    v-if="item.name === '填报单位'"
                    style="display: flex; align-items: center"
                  >
                    <el-input
                      v-model="queryForm.orgName"
                      placeholder="填报单位"
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
                    type="primary"
                    native-type="submit"
                    @click="fetchData('reset')"
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
                    :class="
                      searchMore ? 'search-more is-opened' : 'search-more'
                    "
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
            <!-- <el-tooltip
              class="item"
              effect="dark"
              content="表格筛选"
              placement="top"
            > -->
            <el-button type="success" @click="handleAdd()">新建</el-button>
            <el-button
              type="success"
              @click="handleBatchExport()"
              :disabled="selectedRows.length === 0"
              style="margin-right: 8px"
            >
              批量导出
            </el-button>
            <!-- <el-button
              type="primary"
              style="margin-right: 8px"
              @click="$refs.industryCopy.showEdit()"
            >
              从行业复制
            </el-button> -->
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
            <!-- </el-tooltip> -->
          </vab-query-form-right-panel>

          <el-table
            v-loading="listLoading"
            :data="list"
            @selection-change="handleSelectionChange"
            row-key="riseveid"
            reserve-selection
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column
              align="center"
              label="事件编号"
              prop="riskeventcode"
              width="140"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleRead(row)">
                  {{ row.riskeventcode }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column align="center" label="版本号" prop="version" />

            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="事件名称"
                prop="riskeventname"
                show-overflow-tooltip
                v-if="item.name === '事件名称'"
              />
              <el-table-column
                align="center"
                label="发生部门"
                prop="occureddepartment"
                show-overflow-tooltip
                v-if="item.name === '发生部门'"
              />
              <el-table-column
                align="center"
                label="发生日期"
                prop="occureddate"
                v-if="item.name === '发生日期'"
              />
              <el-table-column
                align="center"
                label="损失事件定性类别"
                prop="losseventcategory"
                show-overflow-tooltip
                v-if="item.name === '损失事件定性类别'"
              >
                <template slot-scope="{ row }">
                  <span>
                    {{ row.losseventcategory == '1' ? '一般事件' : '重大事件' }}
                  </span>
                </template>
              </el-table-column>
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
              <template slot-scope="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row)"
                  :disabled="!!+row.status || createId != row.createstaffid"
                >
                  修改
                </el-button>

                <el-dropdown style="margin-left: 10px">
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
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
                          !!+row.status ||
                          btnLoading ||
                          createId != row.createstaffid
                        "
                      >
                        提交审批
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item @click.native="handleExport(row)">
                      <el-button type="text">导出</el-button>
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handleReport(row)"
                      :disabled="
                        row.status != 6 ||
                        row.reportStatus == 1 ||
                        createId != row.createstaffid
                      "
                    >
                      <el-button
                        type="text"
                        :disabled="
                          row.status != 6 ||
                          row.reportStatus == 1 ||
                          createId != row.createstaffid
                        "
                      >
                        上报
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handleUpdate(row)"
                      :disabled="
                        row.reportStatus != 1 || createId != row.createstaffid
                      "
                    >
                      <el-button
                        type="text"
                        :disabled="
                          row.reportStatus != 1 || createId != row.createstaffid
                        "
                      >
                        更新
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item @click.native="handleHistory(row)">
                      <el-button type="text">查看上报记录</el-button>
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handleDelete(row)"
                      :disabled="!!+row.status"
                    >
                      <el-button
                        type="text"
                        :disabled="
                          !!+row.status || createId != row.createstaffid
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
          :current-page="queryForm.pageNo"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
    <EventEdit ref="edit" @fetch-data="fetchData" :riskcatid="riskcatid" />
    <EventRead ref="read" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
    <HandleHistory ref="historyList" />
    <DepartmentSelect ref="departmentSelect" @submit="handleDepartmentSelect" />
  </div>
</template>

<script>
  import {
    getRiskListById,
    deletFxsj,
    exportRiskEvent,
    reportToLeader,
    exportRiskEvents,
  } from '@/api/risk/riskEvents'
  import TypeTree from '@/views/risk/components/TypeTree.vue'
  import EventEdit from './components/EventEdit.vue'
  import EventRead from './components/EventRead.vue'
  import { UTCformat } from '@/utils'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import HandleHistory from './components/HistoryList.vue'
  import DepartmentSelect from '@/components/departmentSelect.vue'
  import { getFlowPkInfo } from '@/api/contract/manage'
  export default {
    name: 'Fillin',
    components: {
      TypeTree,
      EventEdit,
      EventRead,
      filterSearch,
      filterTable,
      ProcessList,
      WfqdDeal,
      HandleHistory,
      DepartmentSelect,
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNo: 1,
          pageSize: 20,
          orgid: '',
          orgName: '',
          type: '',
        },
        riskcatid: undefined,
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-event-eventBase-search',
        tableKey: 'risk-event-eventBase-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '事件名称' },
          { name: '发生部门' },
          { name: '发生日期' },
          { name: '损失事件定性类别' },
          { name: '审批状态' },
        ], //所有表格项
        filedNow: [],
        selectList: [],
        selectedRows: [], // 存储多选选中的行
        btnLoading: false,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    created() {
      // 接收路由参数
      if (this.$route.query.type) {
        this.queryForm.type = this.$route.query.type
      }
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
    methods: {
      /**
       * @description: 提交审批
       * @return {*}
       */
      handleApproval(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        try {
          this.btnLoading = true
          //提交审批
          this.$refs['process'].save(93, row.riseveid)
        } catch {
          this.btnLoading = false
        }
      },
      /**
       * @description: 办理
       * @return {*}
       */
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.riseveid,
          tableId: 93,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '事件编号', key: 'code' },
          { name: '事件名称', key: 'name' },
          { name: '日期', key: 'Date' },
          { name: '填报单位', key: 'orgid' },
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
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      /**
       * @description: 左侧树，点击回调
       * @return {*}
       */
      select(info) {
        console.log('select', info, '===')
        this.riskcatid = info.riskcatid
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleSizeChange(val) {
        console.log('handleSizeChange')
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange(val) {
        console.log('handleCurrentChange')
        this.queryForm.pageNo = val
        this.fetchData()
      },
      /**
       * @description: 分页，初始化
       * @return {*}
       */
      queryData() {
        console.log('queryData')
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      async fetchData(type) {
        console.log('执行次数')
        this.btnLoading = false
        this.listLoading = true
        if (type && type == 'reset') {
          this.$refs['form'].resetFields()
          // 手动清空筛选字段
          this.queryForm.orgid = ''
          this.queryForm.orgName = ''
          this.queryForm.type = ''
        }
        const { Date } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        delete this.queryForm.Date
        const {
          data: { page },
        } = await getRiskListById({
          ...this.queryForm,
          riskcatid: this.riskcatid,
          startDate,
          endDate,
        })

        page.list = page.list.map((v) => {
          v.discovereddate = UTCformat(v.discovereddate)
          v.occureddate = UTCformat(v.occureddate)
          return v
        })

        this.list = page.list
        // page.records = page.records.map((v) => {
        //   v.discovereddate = UTCformat(v.discovereddate)
        //   v.occureddate = UTCformat(v.occureddate)
        //   return v
        // })

        // this.list = page.records
        this.total = page.total
        this.listLoading = false
      },
      handleSendTo() {},
      handleCopyFrom() {},
      /**
       * @description: 新建左侧树
       * @return {*}
       */
      async handleAdd() {
        const data = this.$refs.typeTree.getData()
        await this.getBottomNode(data)
        const bottomId = this.selectList.filter(
          (item) => item.riskcatid == this.riskcatid
        )

        if (bottomId.length == 0) {
          this.$baseMessage('请在最下级创建！', 'error')
          return
        }

        this.$refs['edit'].showEdit(null, this.riskcatid)
      },
      /**
       * @description: 数据格式化
       * @return {*}
       */
      getBottomNode(tree) {
        tree.forEach((item) => {
          if (item.children && item.children.length > 0) {
            this.getBottomNode(item.children)
          } else {
            this.selectList.push(item)
          }
        })
      },
      /**
       * @description: 部门选择回调
       * @return {*}
       */
      handleDepartmentSelect(data) {
        this.queryForm.orgid = data.id
        this.queryForm.orgName = data.label
      },
      /**
       * @description: 打开编辑
       * @return {*}
       */
      handleEdit(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$refs['edit'].showEdit(row, this.riskcatid)
      },
      /**
       * @description: 打开详细
       * @return {*}
       */
      handleRead(row) {
        this.$refs['edit'].showEdit(row, this.riskcatid, '', 'detail')
      },
      /**
       * @description: 数据删除
       * @return {*}
       */
      handleDelete(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        console.log(row, '删除====')
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deletFxsj({ riseveid: row.riseveid })
          if (code !== 0) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
          await this.fetchData()
        })
      },
      //导出
      async handleExport(val) {
        const data = await exportRiskEvent({
          riseveid: val.riseveid,
        })
        let fileName = '风险事件报告'
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
      //更新
      async handleUpdate(val) {
        this.$refs['edit'].showEdit(val, this.riskcatid, true)
      },
      //查看历史记录
      async handleHistory(val) {
        this.$refs['historyList'].showEdit(val)
      },
      //上报
      async handleReport(val) {
        const { data, code, msg } = await reportToLeader({
          riseveid: Number(val.riseveid),
        })
        if (code !== 0) {
          this.$baseMessage('操作成功', 'success', 'vab-hey-message-success')
        }
        await this.fetchData()
      },

      // 处理表格多选
      handleSelectionChange(selection) {
        this.selectedRows = selection
      },

      // 批量导出
      async handleBatchExport() {
        if (this.selectedRows.length === 0) {
          this.$baseMessage('请选择要导出的数据', 'warning')
          return
        }

        // 获取选中行的ID，组成字符串
        const ids = this.selectedRows.map((row) => row.riseveid).join(',')

        // 构建查询参数，包含筛选条件
        const { Date } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }

        try {
          // 创建一个不包含pageNo和pageSize的查询参数对象
          const { pageNo, pageSize, Date, ...queryParams } = this.queryForm

          const data = await exportRiskEvents({
            ids,
            ...queryParams,
            riskcatid: this.riskcatid,
            startDate,
            endDate,
          })
          let fileName = `风险事件.xlsx`
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

          this.$baseMessage('导出成功', 'success', 'vab-hey-message-success')
        } catch (error) {
          console.error('导出失败', error)
          this.$baseMessage('导出失败', 'error')
        }
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .lr-layout {
    background: #f6f8f9;
    display: flex;
  }

  .lr-layout > .left {
    width: 230px;
    max-width: 230px;
    overflow: hidden;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 10px 20px 20px;
    background: #ffffff;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }
</style>
