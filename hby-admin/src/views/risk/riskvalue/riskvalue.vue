<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
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
                v-model="queryForm.risknumber"
                clearable
                placeholder="风险编号"
                v-if="item.name === '风险编号'"
              />
              <el-input
                v-model="queryForm.riskname"
                clearable
                placeholder="风险名称"
                v-if="item.name === '风险名称'"
              />
              <el-input
                v-model="queryForm.belongstoModel"
                placeholder="请选择牵头责任部门"
                :style="{ width: '100%' }"
                v-if="item.name === '牵头责任部门'"
                @click.native="handleShowBelongStoModel"
              ></el-input>
              <el-input
                v-model="queryForm.unitname"
                clearable
                placeholder="所属公司"
                v-if="item.name === '所属公司'"
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
              <el-button
                type="primary"
                native-type="submit"
                @click="resetQueryForm"
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
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleExport()">导出</el-button>
        <!-- <el-button type="success" @click="handleEdit(null)">新增</el-button> -->
      </vab-query-form-right-panel>
      <el-table
        ref="multipleTable"
        v-loading="listLoading"
        :data="list"
        @selection-change="handleSelectionChange"
        @select="handleSelect"
        @select-all="handleSelectAll"
      >
        <el-table-column type="selection" width="55" disabled></el-table-column>
        <el-table-column align="center" label="风险编号" prop="risknumber">
          <template #default="{ row }">
            <el-button type="text" @click="handleRead(row)">
              {{ row.risknumber }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="风险名称"
            prop="riskname"
            show-overflow-tooltip
            v-if="item.name === '风险名称'"
          />
          <el-table-column
            align="center"
            label="风险描述"
            prop="riskdes"
            show-overflow-tooltip
            v-if="item.name === '风险描述'"
          />
          <el-table-column
            align="center"
            label="所属公司"
            prop="unitname"
            show-overflow-tooltip
            v-if="item.name === '所属公司'"
          />
          <el-table-column
            align="center"
            label="最新上报月份"
            prop="reportmonth"
            show-overflow-tooltip
            v-if="item.name === '最新上报月份'"
          />
          <el-table-column
            align="center"
            label="创建时间"
            prop="riskcreatedt"
            show-overflow-tooltip
            v-if="item.name === '创建时间'"
          />
          <el-table-column
            align="center"
            label="关闭审批状态"
            show-overflow-tooltip
            v-if="item.name === '关闭审批状态'"
          >
            <template #default="{ row }">
              {{
                row.closestatus == 1
                  ? '审批中'
                  : row.closestatus == 2
                  ? '需调整'
                  : row.closestatus == 3
                  ? '已撤销'
                  : row.closestatus == 4
                  ? '已终止'
                  : row.closestatus == 5
                  ? '已跟踪'
                  : row.closestatus == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="是否关闭"
            show-overflow-tooltip
            v-if="item.name === '是否关闭'"
            #default="{ row }"
          >
            {{ row.riskstatus == 0 ? '已关闭' : '未关闭' }}
          </el-table-column>
        </div>
        <el-table-column align="center" label="操作" width="200">
          <template #default="{ row }">
            <el-button type="text" @click="handleValue(row)">
              查看评估列表
            </el-button>

            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleApproval(row)"
                    :disabled="!!row.closestatus"
                  >
                    关闭
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!row.closestatus"
                    @click.native="handleDeal(row)"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="$refs.info.showEdit(row)"
                  >
                    评估信息
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
    <RiskEdit ref="edit" :treeId="treeId" @fetch-data="fetchData" />
    <el-dialog
      title="部门"
      :visible.sync="visibile"
      width="30%"
      :before-close="handleClose"
    >
      <el-tree
        :data="belongstoTextOptions"
        :expand-on-click-node="false"
        node-key="value"
        :props="{
          children: 'children',
          label: 'label',
        }"
        @node-click="handleNodeClick"
      />
    </el-dialog>
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
    <EvaluatingInformation ref="info" />
  </div>
</template>

<script>
  import {
    queryAssessedRiskList,
    exportRiskEvaluated,
  } from '@/api/risk/riskfill'
  import RiskEdit from '@/views/risk/identify/creation/components/RiskEdit.vue'
  import CopyToIndustry from '@/views/risk/components/CopyToIndustry'
  import IndustryCopy from '@/views/risk/components/IndustryCopy'
  import { formatOptions } from '@/utils/validate'
  import { zgjkLeft } from '@/api/setting/org'
  import { UTCformat, formatMonth } from '@/utils'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  import EvaluatingInformation from '@/views/risk/riskvalue/EvaluatingInformation.vue'
  export default {
    name: 'Fillin',
    components: {
      RiskEdit,
      CopyToIndustry,
      IndustryCopy,
      filterTable,
      filterSearch,
      ProcessList,
      WfqdDeal,
      EvaluatingInformation,
    },
    data() {
      return {
        visibile: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        treeId: '',
        queryForm: {
          risknumber: '',
          riskname: '',
          belongsto: '',
          belongstoModel: undefined,
          pageNumber: 1,
          pageSize: 20,
          riskcatid: '',
          unitname: '',
          reportmonth: '',
        },
        belongstoTextOptions: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-identify-creation-search',
        tableKey: 'risk-identify-creation-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '风险名称' },
          { name: '风险描述' },
          { name: '所属公司' },
          { name: '最新上报月份' },
          { name: '创建时间' },
          { name: '关闭审批状态' },
          { name: '是否关闭' },
          { name: '上报月份' },
        ], //所有表格项
        filedNow: [],
        selectList: [],
        // 新增：全局选中状态管理
        globalSelectedIds: new Set(), // 存储所有选中的ID
        globalSelectedData: [], // 存储所有选中的完整数据
      }
    },
    created() {
      this.getBelongstoTextOptions()
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
      handleApproval(row) {
        this.$refs['process'].save(206, row.riskid)
      },
      async handleDeal(row) {
        const res = await getFlowPkInfo({
          formId: row.riskid,
          tableId: 206,
        })
        this.$refs.wfqddeal.show(res.data, false)
      },
      /**
       * @description: 选择部门
       * @return {*}
       */
      handleNodeClick(e) {
        console.log(e, 'e====')
        this.queryForm.belongstoModel = e.label
        this.queryForm.belongsto = e.value
        this.visibile = false
      },
      /**
       * @description: 关闭选择部门页面
       * @return {*}
       */
      handleClose() {
        this.visibile = false
      },
      //handleShowBelongStoModel
      /**
       * @description: 打开选择部门页面
       * @return {*}
       */
      handleShowBelongStoModel() {
        this.visibile = true
        console.log(this.belongstoTextOptions)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '风险编号', key: 'risknumber' },
          { name: '风险名称', key: 'riskname' },
          { name: '牵头责任部门', key: 'belongstoModel' },
          { name: '所属公司', key: 'unitname' },
          { name: '最新上报月份', key: 'reportmonth' },
        ]
        return fields
      },
      /**
       * @description: 初始化 搜索
       * @return {*}
       */
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
       * @description: 分页
       * @return {*}
       */
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
      treeData(id) {
        this.treeId = id
        this.fetchData()
      },
      /**
       * @description: 重置
       * @return {*}
       */
      resetQueryForm() {
        this.queryForm = {
          risknumber: '',
          riskname: '',
          belongsto: '',
          belongstoModel: undefined,
          pageNumber: 1,
          pageSize: 20,
          unitname: '',
          reportmonth: '',
        }
        // 重置时清空选择状态
        this.globalSelectedIds.clear()
        this.globalSelectedData = []
        this.fetchData()
      },
      /**
       * @description: 请求数据
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            data: {
              pageInfo: { tlist, totalRecord },
            },
          },
        } = await queryAssessedRiskList(this.queryForm)
        tlist.map((v) => {
          v.riskcreatedt = UTCformat(v.riskcreatedt)
          return v
        })
        this.list = tlist
        this.total = totalRecord
        console.log('🚀 ~ fetchData ~ this.total :', this.total)
        this.listLoading = false

        // 恢复选中状态
        this.$nextTick(() => {
          this.restoreSelection()
        })
      },
      async getBelongstoTextOptions() {
        const result = await zgjkLeft()
        let newValue = formatOptions(result, 'name', 'id')
        this.belongstoTextOptions = newValue
        this.fetchData()
      },

      /**
       * @description: 打开详情
       * @return {*}
       */
      handleRead(row) {
        this.$refs['edit'].showEdit(row, '', true)
      },
      handleValue(row) {
        this.$router.push({
          path: '/riskfill/riskvalueList',
          query: {
            risknumber: row.risknumber,
            riskid: row.riskid,
            closestatus: row.closestatus,
          },
        })
      },
      handleSelectionChange(val) {
        this.selectList = val
      },

      // 处理单行选择
      handleSelect(selection, row) {
        const isSelected = selection.some((item) => item.riskid === row.riskid)
        if (isSelected) {
          // 添加到全局选中
          this.globalSelectedIds.add(row.riskid)
          // 更新全局选中数据，避免重复
          const existingIndex = this.globalSelectedData.findIndex(
            (item) => item.riskid === row.riskid
          )
          if (existingIndex === -1) {
            this.globalSelectedData.push(row)
          }
        } else {
          // 从全局选中中移除
          this.globalSelectedIds.delete(row.riskid)
          this.globalSelectedData = this.globalSelectedData.filter(
            (item) => item.riskid !== row.riskid
          )
        }
      },

      // 处理全选
      handleSelectAll(selection) {
        if (selection.length > 0) {
          // 全选：添加当前页所有数据到全局选中
          this.list.forEach((row) => {
            this.globalSelectedIds.add(row.riskid)
            const existingIndex = this.globalSelectedData.findIndex(
              (item) => item.riskid === row.riskid
            )
            if (existingIndex === -1) {
              this.globalSelectedData.push(row)
            }
          })
        } else {
          // 取消全选：从全局选中中移除当前页所有数据
          this.list.forEach((row) => {
            this.globalSelectedIds.delete(row.riskid)
          })
          this.globalSelectedData = this.globalSelectedData.filter(
            (item) =>
              !this.list.some((listItem) => listItem.riskid === item.riskid)
          )
        }
      },

      // 恢复选中状态
      restoreSelection() {
        if (this.$refs.multipleTable) {
          // 清除当前选中状态
          this.$refs.multipleTable.clearSelection()
          // 恢复选中状态
          this.list.forEach((row) => {
            if (this.globalSelectedIds.has(row.riskid)) {
              this.$refs.multipleTable.toggleRowSelection(row, true)
            }
          })
        }
      },
      //导出
      async handleExport(val) {
        const ids = Array.from(this.globalSelectedIds)
        const data = await exportRiskEvaluated({
          id: ids.toString(),
          ...this.queryForm,
        })
        let fileName = '月度评估'
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

        this.$message.success(`已导出 ${this.globalSelectedData.length} 条数据`)
      },

      // handleClose(row) {
      //   this.$confirm('确定关闭吗?', '提示', {
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     type: 'warning',
      //   }).then(() => {
      //     closeAssessedRisk({ id: row.riskid }).then(() => {
      //       this.$message({
      //         type: 'success',
      //         message: '关闭成功!',
      //       })
      //       this.fetchData()
      //     })
      //   })
      // },
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
</style>
