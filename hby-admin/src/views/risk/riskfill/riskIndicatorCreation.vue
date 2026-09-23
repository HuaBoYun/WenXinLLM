<template>
  <!-- 风险监测指标创建 -->
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            checkable
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-select
                v-model="queryForm.quartername"
                clearable
                placeholder="请选择季度"
                v-if="item.name == '季度'"
              >
                <el-option
                  v-for="option in quarterOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                ></el-option>
              </el-select>
              <el-date-picker
                v-if="item.name == '年度'"
                v-model="queryForm.riskyear"
                type="year"
                format="yyyy"
                value-format="yyyy"
                placeholder="年度"
              ></el-date-picker>
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
              <el-button @click="resetSearch()" type="primary">重置</el-button>
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
        </vab-query-form-left-panel>
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
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleAddDept" v-if="isFX">
          部门模板创建
        </el-button>
        <el-button type="success" @click="handleAdd">新建</el-button>
        <el-button
          type="success"
          @click="handleBatchDistribute"
          :disabled="select.length == 0"
        >
          下发
        </el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <el-table-column
          type="selection"
          width="55"
          :selectable="(row) => row.status == 6 && !row.lssuedstatus"
        ></el-table-column>
        <el-table-column align="center" label="季度" prop="quartername">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleDetail(row)"
              style="white-space: pre-line; line-height: 16px"
            >
              {{ row.quartername }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="年度"
            prop="riskyear"
            v-if="item.name == '年度'"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            align="center"
            label="备注"
            prop="notes"
            v-if="item.name == '备注'"
            show-overflow-tooltip
          ></el-table-column>
          <!-- <el-table-column
            v-if="item.name == '下发人员'"
            align="center"
            label="下发人员"
            prop="lssuedUserName"
            show-overflow-tooltip
          ></el-table-column> -->

          <el-table-column
            v-if="item.name == '下发时间'"
            align="center"
            label="下发时间"
            prop="lssuedDate"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ row.lssuedDate ? row.lssuedDate.split(' ')[0] : '' }}
            </template>
          </el-table-column>

          <el-table-column
            v-if="item.name == '是否下发'"
            align="center"
            label="是否下发"
            prop="lssuedstatus"
          >
            <template #default="{ row }">
              {{ row.lssuedstatus ? '已下发' : '未下发' }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name == '状态'"
            align="center"
            label="状态"
            prop="status"
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
        <el-table-column align="center" label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="!!+row.status"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!+row.status"
                    @click="handleDeal(row)"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!(row.status == 6 && !row.lssuedstatus)"
                    @click="handleDistribute(row)"
                  >
                    下发
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleViewIssuedPersons(row)"
                    :disabled="!row.lssuedstatus"
                  >
                    查看下发人员
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!!+row.status || btnLoading"
                    @click="handleSubmit(row)"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!!+row.status"
                    @click="handleDelete(row)"
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
      class="pagination"
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" @fetchData="fetchData" />
    <IndicatorEdit ref="indicatorEdit" @fetchData="fetchData" />
    <DeptTemplateDialog ref="deptTemplateDialog" />
    <DeptDistributeDialog
      ref="deptDistributeDialog"
      @deptPersonSelected="handleDeptPersonSelected"
    />

    <!-- 查看下发人员弹窗 -->
    <el-dialog
      title="查看下发人员"
      :visible.sync="issuedPersonsDialogVisible"
      width="800px"
      @close="closeIssuedPersonsDialog"
    >
      <el-table
        v-loading="issuedPersonsLoading"
        :data="issuedPersonsList"
        style="width: 100%"
      >
        <el-table-column
          prop="deptStaffName"
          label="下发人员"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="deptName"
          label="部门"
          align="center"
        ></el-table-column>
        <el-table-column prop="createTime" label="下发时间" align="center">
          <template #default="{ row }">
            {{ row.createTime ? row.createTime.split(' ')[0] : '' }}
          </template>
        </el-table-column>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button @click="closeIssuedPersonsDialog">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { formatDay } from '@/utils'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import IndicatorEdit from './components/IndicatorEdit.vue'
  import DeptTemplateDialog from './components/DeptTemplateDialog.vue'
  import DeptDistributeDialog from './components/DeptDistributeDialog.vue'
  import { xiafaListNew } from '@/oapi/audit/preparation'

  import {
    getMajorRiskCreateList,
    deleteMajorRiskCreate,
    monitoringIssuedNew,
    getIssuedList,
  } from '@/api/risk/monitoring'
  export default {
    name: 'RiskIndicatorCreation',
    components: {
      filterTable,
      filterSearch,
      ProcessList,
      WfqdDeal,
      IndicatorEdit,

      DeptTemplateDialog,
      DeptDistributeDialog,
    },
    data() {
      return {
        select: [],
        selectedRows: [], // 用于存储选中的行
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          quartername: '',
          pageNumber: 1,
          pageSize: 20,
          riskyear: '',
        },
        filedAll: [
          { name: '年度' },
          { name: '备注' },
          { name: '状态' },
          // { name: '下发人员' },
          { name: '下发时间' },
          { name: '是否下发' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-indicator-creation-search',
        tableKey: 'risk-indicator-creation-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        btnLoading: false,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
        quarterOptions: [
          { value: '一季度', label: '一季度' },
          { value: '二季度', label: '二季度' },
          { value: '三季度', label: '三季度' },
          { value: '四季度', label: '四季度' },
        ],
        quartername: '',
        // 查看下发人员弹窗相关
        issuedPersonsDialogVisible: false,
        issuedPersonsList: [],
        issuedPersonsLoading: false,
        isFX: false,
      }
    },
    created() {
      //判断权限是否有经责科.展示不同title
      let userInfo = JSON.parse(localStorage.getItem('userInfo'))
      if (!userInfo.roleNames.includes('风控管理员')) {
        this.isFX = false
      } else {
        this.isFX = true
      }
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '季度', key: 'quartername' },
          { name: '年度', key: 'riskyear' },
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
            if (!this.searchNow.some((y) => y.key == x.key)) {
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
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
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
          const {
            data: { pageInfo },
            code,
          } = await getMajorRiskCreateList(this.queryForm)
          this.list = pageInfo.list
          this.total = pageInfo.total
        } catch (error) {
          console.error('获取数据失败:', error)
          // 添加模拟数据用于测试
          this.list = []
          this.total = 1
        } finally {
          this.listLoading = false
        }
        this.setCheckedRows()
      },
      handleEdit(row, type) {
        this.$refs['indicatorEdit'].showEdit(row, 'edit', type)
      },
      handleDetail(row) {
        this.$refs['indicatorEdit'].showEdit(row, 'detail')
      },
      handleAdd() {
        this.$refs['indicatorEdit'].showEdit('', 'add')
      },
      handleAddDept() {
        this.$refs.deptTemplateDialog.show()
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await deleteMajorRiskCreate({ id: row.id })
          if (res.code == 1) {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        const pageSize = this.queryForm.pageSize
        this.queryForm = {
          quartername: '',
          pageNumber: 1,
          pageSize,
          riskyear: '',
        }
        this.fetchData()
      },
      async handleDeal(row) {
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 218,
        })
        this.$refs.wfqddeal.show(res.data, false)
      },
      handleSubmit(row) {
        try {
          this.btnLoading = true
          this.$refs['process'].save(218, row.id)
        } catch (error) {
          console.error(error)
        } finally {
          this.btnLoading = false
        }
      },
      handleSelection(selection, row) {
        // 检查行是否在当前表格中选中
        const isSelected = selection.some((item) => item.id === row.id)
        const index = this.select.findIndex((item) => item.id === row.id)

        // 如果行在表格中被选中，但不在select中，则添加
        if (isSelected && index === -1) {
          if (row.status == 6 && !row.lssuedstatus) {
            this.select.push(row)
          }
        }

        // 如果行在表格中未被选中，但在select中存在，则移除
        if (!isSelected && index !== -1) {
          this.select.splice(index, 1)
        }
      },
      handleSelectAll(selection) {
        // 获取当前页面符合条件的行（状态为6且未下发）
        const eligibleRows = this.list.filter(
          (row) => row.status == 6 && !row.lssuedstatus
        )

        if (selection.length > 0) {
          // 全选：将当前页面所有符合条件且未选中的行添加到select
          eligibleRows.forEach((row) => {
            if (!this.select.some((item) => item.id === row.id)) {
              this.select.push(row)
            }
          })
        } else {
          // 取消全选：从select中移除当前页面所有符合条件的行
          this.select = this.select.filter((selectedRow) => {
            return !eligibleRows.some((row) => row.id === selectedRow.id)
          })
        }
      },
      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          if (!this.$refs.multipleTable) return
          // 清除之前的选择状态
          this.$refs.multipleTable.clearSelection()
          // 设置新的选择状态
          this.select.forEach((row) => {
            const matchedRow = this.list.find((item) => item.id == row.id)
            if (matchedRow) {
              this.$refs.multipleTable.toggleRowSelection(matchedRow, true)
            }
          })
        })
      },
      async handleDistribute(row) {
        if (row) {
          // 单个下发，检查是否已下发
          if (row.lssuedstatus) {
            this.$baseMessage('该项目已下发，不能重复下发', 'warning')
            return
          }
          this.selectedRows = [row]
          this.id = row.id
          this.quartername = row.quartername
        } else {
          // 批量下发
          this.selectedRows = [...this.select]
        }
        // 打开部门分发弹窗
        this.$refs.deptDistributeDialog.show()
      },
      handleBatchDistribute() {
        if (this.select.length == 0) {
          this.$baseMessage('请先选择要下发的项', 'warning')
          return
        }

        // 再次检查确保所有选中项都满足可下发条件
        const validRows = this.select.filter(
          (row) => row.status == 6 && !row.lssuedstatus
        )

        if (validRows.length !== this.select.length) {
          this.$baseMessage(
            '选中项中包含不符合下发条件的数据，请重新选择',
            'warning'
          )
          return
        }

        this.handleDistribute()
      },
      // 处理部门人员选择结果
      async handleDeptPersonSelected(deptPersonList) {
        try {
          // 将部门人员数据转换为原有格式
          const val = deptPersonList.map((item) => ({
            staffid: item.staffid,
            realname: item.realname,
          }))
          // 处理批量下发
          if (this.selectedRows && this.selectedRows.length > 0) {
            const rowIds = this.selectedRows.map((row) => row.id).toString()

            // 调用 monitoringIssuedNew 接口
            const monitoringData = {
              ids: rowIds,
              data: deptPersonList.map((item) => ({
                creatId: 0,
                createTime: '',
                deptDate: '',
                deptId: item.deptId || 0,
                deptName: item.deptName || '',
                deptNotes: '',
                deptStaff: item.staffid || 0,
                deptStaffName: item.realname || '',
                fillId: 0,
                id: 0,
                versionId: item.versionId || '',
              })),
            }

            try {
              const res = await monitoringIssuedNew(monitoringData)
              if (res.code == 1) {
                this.$baseMessage('批量下发成功', 'success')
              } else {
                this.$baseMessage('批量下发失败', 'error')
                return
              }
            } catch (error) {
              console.error('monitoringIssuedNew 调用失败:', error)
              this.$baseMessage('批量下发失败', 'error')
              return
            }

            // 为每条选中的数据创建下发记录
            const xiafaPromises = this.selectedRows.map((row) => {
              const arr = val.map((item) => {
                return {
                  formId: row.id,
                  distributionTitle: row.quartername, // 使用季度名称或默认标题
                  reciver: item.staffid,
                  isread: 0,
                  moduleType: 'fxgk',
                }
              })
              // 为每条数据调用xiafa方法
              return xiafaListNew({
                tableId: '172049951177045',
                jsondistribution: JSON.stringify(arr),
              })
            })

            // 等待所有下发操作完成
            Promise.all(xiafaPromises)
              .then((responses) => {
                const allSuccess = responses.every(
                  (response) => response.msg === '成功'
                )
                if (allSuccess) {
                  this.$baseMessage('批量下发通知成功', 'success')
                } else {
                  this.$baseMessage('部分下发通知失败', 'warning')
                }
                this.fetchData()
                // 清空选择
                this.select = []
                this.selectedRows = []
              })
              .catch((error) => {
                console.error('下发通知失败:', error)
                this.$baseMessage('下发通知失败', 'error')
                this.fetchData()
              })
          } else if (this.id) {
            // 单个下发
            // 调用 monitoringIssuedNew 接口
            const monitoringData = {
              ids: this.id,
              data: deptPersonList.map((item) => ({
                creatId: 0,
                createTime: '',
                deptDate: '',
                deptId: item.deptId || 0,
                deptName: item.deptName || '',
                deptNotes: '',
                deptStaff: item.staffid || 0,
                deptStaffName: item.realname || '',
                fillId: 0,
                id: 0,
              })),
            }

            try {
              const res = await monitoringIssuedNew(monitoringData)
              if (res.code == 1) {
                this.$baseMessage('选择下发人员成功', 'success')
              } else {
                this.$baseMessage('选择下发人员失败', 'error')
                return
              }
            } catch (error) {
              console.error('monitoringIssuedNew 调用失败:', error)
              this.$baseMessage('选择下发人员失败', 'error')
              return
            }

            const arr = val.map((item) => {
              return {
                formId: this.id,
                distributionTitle: this.quartername,
                reciver: item.staffid,
                isread: 0,
                moduleType: 'fxgk',
              }
            })
            // 调用xiafa方法
            xiafaListNew({
              tableId: '172049951177045',
              jsondistribution: JSON.stringify(arr),
            }).then((response) => {
              if (response.msg == '成功') {
                this.$baseMessage('下发通知成功', 'success')
                this.select = []
                this.selectedRows = []
                this.fetchData()
              }
            })
          }
        } catch (error) {
          console.error('下发失败:', error)
          this.$baseMessage('下发失败', 'error')
        }
      },
      // 查看下发人员
      async handleViewIssuedPersons(row) {
        this.issuedPersonsDialogVisible = true
        this.issuedPersonsLoading = true
        this.issuedPersonsList = []

        try {
          // 调用接口获取下发人员列表
          const {
            data: { data },
          } = await getIssuedList({
            id: row.id, // 传递当前行的id
          })

          this.issuedPersonsList = data
        } catch (error) {
          console.error('获取下发人员列表失败:', error)
        } finally {
          this.issuedPersonsLoading = false
        }
      },
      // 关闭查看下发人员弹窗
      closeIssuedPersonsDialog() {
        this.issuedPersonsDialogVisible = false
        this.issuedPersonsList = []
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
  .upload-demo {
    display: inline-block;
    margin: 0 10px;
  }
</style>
