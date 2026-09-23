<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <!-- <CompanyTree ref="leftlist" @select="handleTreeSelect" /> -->
        <DepTree ref="leftlist" @select="handleTreeSelect" />
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
                  <el-select
                    v-model="queryForm.cxlevel"
                    clearable
                    placeholder="风险等级"
                    v-if="item.name === '风险等级'"
                  >
                    <el-option label="未评估" value="0" />
                    <el-option label="很低" value="1" />
                    <el-option label="较低" value="2" />
                    <el-option label="中等" value="3" />
                    <el-option label="较高" value="4" />
                    <el-option label="很高" value="5" />
                  </el-select>
                  <el-input
                    v-model="queryForm.linkDeptName"
                    disable
                    placeholder="请输入所属部门"
                    v-if="item.name === '所属部门'"
                  ></el-input>
                  <el-select
                    v-model="queryForm.riskcatname"
                    v-if="item.name === '风险领域'"
                    placeholder="请选择风险领域"
                    clearable
                  >
                    <el-option
                      v-for="(item, index) in selectList"
                      :key="index"
                      :label="item"
                      :value="item"
                    />
                    <!-- <el-option label="市场风险" value="市场风险" />
                  <el-option label="财务风险" value="财务风险" />
                  <el-option label="运营风险" value="运营风险" />
                  <el-option label="法律合规风险" value="法律合规风险" />
                  <el-option label="安全环保风险" value="安全环保风险" />
                  <el-option label="工程建设风险" value="工程建设风险" /> -->
                  </el-select>
                  <el-select
                    v-model="queryForm.riskcatidname"
                    v-if="item.name === '风险类型'"
                    placeholder="请选择风险类型"
                    clearable
                  >
                    <el-option
                      v-for="(item, index) in selectList1"
                      :key="index"
                      :label="item"
                      :value="item"
                    />
                  </el-select>
                  <el-select
                    v-model="queryForm.pgStatus"
                    v-if="item.name === '风险状态'"
                    placeholder="请选择风险状态"
                    clearable
                  >
                    <el-option label="已评估" value="ypg" />
                    <el-option label="未评估" value="wpg" />
                  </el-select>
                  <el-select
                    v-model="queryForm.gbStatus"
                    v-if="item.name === '关闭状态'"
                    placeholder="请选择关闭状态"
                    clearable
                  >
                    <el-option label="已关闭" value="0" />
                    <el-option label="未关闭" value="1" />
                  </el-select>
                  <el-input
                    v-model="queryForm.zrbmName"
                    clearable
                    placeholder="请输入牵头责任部门"
                    v-if="item.name === '牵头责任部门'"
                  />
                </el-form-item>
                <el-form-item>
                  <el-date-picker
                    v-model="queryForm.Date"
                    clearable
                    end-placeholder="结束日期"
                    format="yyyy-MM-dd"
                    range-separator="-"
                    start-placeholder="开始日期"
                    type="daterange"
                    value-format="yyyy-MM-dd"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button
                    icon="el-icon-search"
                    native-type="submit"
                    type="primary"
                    @click="fetchData(queryForm.riskcatid)"
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
          <vab-query-form>
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
              <el-button type="success" @click="handlerExport">导出</el-button>
              <el-button type="primary" v-if="isUEditor" @click="UEClose">
                取消
              </el-button>
              <el-button type="primary" v-if="isUEditor" @click="UESubmit">
                确定
              </el-button>
            </vab-query-form-right-panel>
          </vab-query-form>
          <el-table
            v-loading="listLoading"
            :data="list"
            @selection-change="handleSelectionChange"
            ref="multipleTable"
          >
            <el-table-column
              type="selection"
              width="55"
              v-if="isUEditor"
            ></el-table-column>
            <el-table-column align="center" label="风险编号" prop="risknumber">
              <template #default="{ row }">
                <el-button type="text" @click="handleRiskRead(row)">
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
                label="风险领域"
                prop="riskcatname"
                show-overflow-tooltip
                v-if="item.name === '风险领域'"
              />
              <el-table-column
                align="center"
                label="风险类型"
                prop="riskcatidname"
                show-overflow-tooltip
                v-if="item.name === '风险类型'"
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
                v-if="item.name === '所属公司'"
              />
              <el-table-column
                align="center"
                label="所属部门"
                prop="linkDeptName"
                v-if="item.name === '所属部门'"
              />
              <el-table-column
                align="center"
                label="牵头责任部门"
                prop="zrbmName"
                show-overflow-tooltip
                v-if="item.name === '牵头责任部门'"
              />
              <el-table-column
                align="center"
                label="风险等级"
                prop="level"
                show-overflow-tooltip
                v-if="item.name === '风险等级'"
              >
                <template #default="{ row }">
                  <div
                    :class="{
                      calCount1: row.level === '很低',
                      calCount2: row.level === '较低',
                      calCount3: row.level === '中等',
                      calCount4: row.level === '较高',
                      calCount5: row.level === '很高',
                    }"
                  >
                    {{ row.level }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="风险关闭状态"
                prop="gbStatus"
                v-if="item.name === '风险关闭状态'"
              >
                <template #default="{ row }">
                  {{ row.riskstatus == 0 ? '已关闭' : '未关闭' }}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="创建时间"
                prop="riskcreatedt"
                show-overflow-tooltip
                v-if="item.name === '创建时间'"
              />
            </div>
            <el-table-column align="center" label="操作">
              <template #default="{ row }">
                <el-button type="text" @click="handleTaskEdit(row)">
                  评估信息
                </el-button>
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
    <TaskEdit ref="taskEdit" />
    <RiskRead ref="riskRead" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  // import CompanyTree from '@/views/setting/org/components/CompanyTree'
  import DepTree from '@/views/risk/GroupRiskDatabase/companyDep.vue'
  import {
    riskTzlistGroup,
    exportRiskTzlistGroup,
    getCreationTreeData,
  } from '@/api/risk'
  import { formatOptions } from '@/utils/validate'
  import { parseTime } from '@/utils/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { downloadFile } from '@/utils/otherUtils'
  import { getFlowPkInfo } from '@/api/contract/manage'
  // import RiskRead from '@/views/risk/standingBook/components/RiskRead.vue'
  import RiskRead from '@/views/risk/identify/creation/components/RiskEdit.vue'
  import TaskEdit from '@/views/risk/standingBook/components/TaskEdit.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  export default {
    props: {
      //UE编辑器模板传值,判断是否有复制按钮
      isUEditor: {
        type: Boolean,
        default: false,
      },
    },
    name: 'TreatmentEdit',
    components: {
      // CompanyTree,
      filterSearch,
      filterTable,
      TaskEdit,
      RiskRead,
      DepTree,
      WfqdDeal,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        riskcategory: {},
        queryForm: {
          riskcatidname: '',
          riskcatname: '',
          orgid: '',
          risknumber: '',
          riskname: '',
          pageNo: 1,
          pageSize: 20,
          riskcatid: '',
          Date: [],
          cxlevel: '',
          linkDeptName: '',
          pgStatus: '',
          gbStatus: '',
          zrbmName: '',
        },
        belongstoTextOptions: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-identify-groupRisk-search',
        tableKey: 'risk-identify-groupRisk-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '风险名称' },
          { name: '风险领域' },
          { name: '风险类型' },
          { name: '风险描述' },
          { name: '所属公司' },
          { name: '所属部门' },
          { name: '牵头责任部门' },
          { name: '风险等级' },
          { name: '风险关闭状态' },
          { name: '创建时间' },
        ], //所有表格项
        filedNow: [],
        multipleSelection: [],
        selectList: [],
        selectList1: [],
      }
    },
    created() {
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
      this.getSelectData()
    },
    methods: {
      async getSelectData() {
        const { data } = await getCreationTreeData()
        this.selectList = []
        this.selectList1 = []

        // 将第一层children的riskcatname添加到selectList
        data.tree[0].children.map((item) => {
          this.selectList.push(item.riskcatname)
        })

        // 递归函数，用于遍历所有层级的children并添加riskcatname到selectList1
        const getAllRiskcatnames = (items) => {
          if (!items || !items.length) return

          items.forEach((item) => {
            // 将当前项的riskcatname添加到selectList1
            if (item.riskcatname) {
              this.selectList1.push(item.riskcatname)
            }

            // 如果有子项，递归处理
            if (item.children && item.children.length > 0) {
              getAllRiskcatnames(item.children)
            }
          })
        }
        // 从第一层children开始递归
        getAllRiskcatnames(data.tree[0].children)
      },
      handleTreeSelect(org) {
        console.log(org.id)
        this.queryForm.orgid = org.id
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      //编辑器多选
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '风险编号', key: 'risknumber' },
          { name: '风险名称', key: 'riskname' },
          { name: '风险领域', key: 'riskcatname' },
          { name: '风险等级', key: 'cxlevel' },
          { name: '所属部门', key: 'linkDeptName' },
          { name: '风险类型', key: 'riskcatidname' },
          { name: '风险状态', key: 'pgStatus' },
          { name: '关闭状态', key: 'gbStatus' },
          { name: '牵头责任部门', key: 'zrbmName' },
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
        this.queryForm.pageNo = val
        this.fetchData()
      },
      /**
       * @description: 分页，初始化
       * @return {*}
       */
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      async fetchData(id) {
        this.listLoading = true
        if (id && id === 'reset') {
          this.queryForm.Date = []
          this.queryForm.risknumber = ''
          this.queryForm.riskname = ''
          this.queryForm.riskcatidname = ''
          this.queryForm.riskcatname = ''
          this.queryForm.cxlevel = ''
          this.queryForm.linkDeptName = ''
          this.queryForm.pgStatus = ''
          this.queryForm.gbStatus = ''
          this.queryForm.zrbmName = ''
          this.queryForm.pageNo = 1
          this.queryForm.pageSize = 20
        }
        const { Date } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        const {
          data: {
            data: { list, total },
          },
        } = await riskTzlistGroup({
          ...this.queryForm,
          startDate,
          endDate,
        })

        this.list = list
        this.list.forEach((item) => {
          if (item.riskcreatedt) {
            item.riskcreatedt = parseTime(item.riskcreatedt, '{y}-{m}-{d}')
          }
        })
        this.total = total
        this.listLoading = false
      },

      /**
       * @description: 打开 评估信息
       * @return {*}
       */
      handleTaskEdit(row) {
        this.$refs['taskEdit'].showEdit(row, 1)
      },
      /**
       * @description: 打开详情
       * @return {*}
       */
      handleRead(row) {
        this.$refs['read'].showRead(row)
      },
      /**
       * @description: 打开 风险详情
       * @return {*}
       */
      async handleRiskRead(row) {
        if (row && row.status > 1) {
          const res = await getFlowPkInfo({
            formId: row.riskid,
            tableId: 90,
          })
          this.$refs.wfqddeal.show(res.data, false)
        } else {
          this.$refs['riskRead'].showEdit(row, row.riskcatid, true)
        }
        // this.$refs['riskRead'].showRead(row, row.riskcatid)
      },
      UEClose() {
        this.$emit('close')
      },
      UESubmit() {
        this.$emit('submit', this.multipleSelection)
      },
      clearSelection() {
        this.$refs.multipleTable.clearSelection()
        this.multipleSelection = []
      },

      async handlerExport() {
        let params = {
          ...this.queryForm,
        }
        delete params.pageNo
        delete params.pageSize
        const res = await exportRiskTzlistGroup({
          ...params,
          startDate: this.queryForm.Date[0] || '',
          endDate: this.queryForm.Date[1] || '',
        })
        downloadFile(res, '集团风险数据库.xlsx')
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
    width: 300px;
    overflow: hidden;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 20px 20px 20px;
    background: #ffffff;
  }
  .lr-layout > .right {
    flex: 1;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }
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
