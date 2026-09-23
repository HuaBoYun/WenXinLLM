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
                v-model="queryForm.contractno"
                clearable
                placeholder="合同编号"
                v-if="item.name === '合同编号'"
              />
              <el-input
                v-model="queryForm.contractname"
                clearable
                placeholder="合同名称"
                v-if="item.name === '合同名称'"
              />
              <el-input
                v-model="queryForm.budgetname"
                clearable
                placeholder="相对方信息"
                v-if="item.name === '相对方信息'"
              />
              <el-select
                v-model="queryForm.contracttype"
                filterable
                placeholder="合同类型"
                style="width: 100%"
                v-if="item.name === '合同类型'"
              >
                <el-option
                  v-for="item in typeOptions2"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"
                />
              </el-select>
              <el-select
                v-model="queryForm.isWy"
                placeholder="异常合同"
                v-if="item.name === '异常合同'"
              >
                <el-option label="是" value="是" />
                <el-option label="否" value="否" />
              </el-select>
              <el-input
                v-model="queryForm.contractitem"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
              />
              <el-select
                v-model="queryForm.dctype"
                placeholder="收付款方向"
                v-if="item.name === '收付款方向'"
              >
                <el-option label="收款" value="收款" />
                <el-option label="付款" value="付款" />
              </el-select>
              <el-select
                v-model="queryForm.contractstatus"
                clearable
                placeholder="合同状态"
                v-if="item.name === '合同状态'"
              >
                <el-option
                  v-for="item in typeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
              <el-row v-if="item.name === '合同金额'">
                <el-col :span="11">
                  <el-input
                    v-model="queryForm.minMoney"
                    clearable
                    placeholder="最少合同金额"
                  />
                </el-col>
                <el-col class="line" :span="2" style="text-align: center">
                  -
                </el-col>
                <el-col :span="11">
                  <el-input
                    v-model="queryForm.maxMoney"
                    clearable
                    placeholder="最大合同金额"
                  />
                </el-col>
              </el-row>
              <el-date-picker
                v-model="date"
                align="right"
                end-placeholder="结束日期"
                :picker-options="pickerOptions"
                range-separator="至"
                start-placeholder="创建时间"
                type="daterange"
                unlink-panels
                value-format="yyyy-MM-dd"
                v-if="item.name === '创建时间'"
              />
              <div v-if="item.name === '所属公司'">
                <el-input
                  v-model="queryForm.orgname"
                  disabled
                  placeholder="请选择所属公司"
                  :style="{ width: '70%' }"
                />
                <el-button
                  :style="{ marginLeft: '10px' }"
                  type="primary"
                  @click="showGroupLeader"
                >
                  选择
                </el-button>
              </div>
              <div v-if="item.name === '承办部门'">
                <el-input
                  v-model="queryForm.contractdeptName"
                  clearable
                  disabled
                  placeholder="请选择承办部门"
                  style="width: 187px"
                  @clear="$forceUpdate()"
                />
                <el-button
                  :style="{ marginLeft: '10px' }"
                  type="primary"
                  @click="$refs.department.showEdit()"
                >
                  选择
                </el-button>
              </div>
              <div v-if="item.name === '承办人'">
                <el-input
                  v-model="queryForm.contractstaffName"
                  clearable
                  disabled
                  placeholder="请选择承办人"
                  style="width: 187px"
                  @clear="$forceUpdate()"
                />
                <el-button
                  :style="{ marginLeft: '10px' }"
                  type="primary"
                  @click="$refs.executor.showEdit()"
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
        <el-button type="success" @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="合同编号" prop="contractno" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="合同名称"
            prop="contractname"
            v-if="item.name === '合同名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.contractname }}
              </el-button>
            </template>
          </el-table-column>
          <!-- <el-table-column align="center" label="相对方名称" prop="budgetname" /> -->
          <el-table-column
            align="center"
            label="公司名称 "
            prop="orgname"
            v-if="item.name === '公司名称'"
          />
          <el-table-column
            align="center"
            label="合同类型"
            prop="contracttype"
            v-if="item.name === '合同类型'"
          />
          <el-table-column
            align="center"
            label="项目名称"
            prop="topicname"
            v-if="item.name === '项目名称'"
          />
          <el-table-column
            align="center"
            label="收付款方向"
            prop="dctype"
            v-if="item.name === '收付款方向'"
          />
          <el-table-column
            align="center"
            label="合同金额"
            prop="contractmoney"
            v-if="item.name === '合同金额'"
          />
          <el-table-column
            align="center"
            label="是否违约 "
            prop="isWy"
            v-if="item.name === '是否违约'"
          />
          <el-table-column
            align="center"
            label="创建日期"
            prop="createtime"
            v-if="item.name === '创建日期'"
          />
        </div>
        <el-table-column align="center" label="合同状态" prop="contractstatus">
          <template #default="{ row }">
            {{ mapContractStatus(row) }}
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
    <CreateDetail ref="common" @fetch-data="fetchData" />
    <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    />
    <SealDepartment ref="department" @submit="handleDepartmentSelected" />
    <ExecutorOptions ref="executor" @projectManage="handleExecutorSelected" />
  </div>
</template>

<script>
  import { getContractTypes } from '@/api/contract/manage'
  import SealDepartment from '@/components/departmentSelect.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import {
    exportStandingAllBook,
    getStandingAllBookList,
  } from '@/api/contract/standingbook'
  import CompanySelectModal from '@/components/CampanySelectModal'
  import { contractStatusOptions } from '@/views/contract/consts'
  import CreateDetail from '@/views/contract/contractManage/components/contractsEdit/CreateDetail'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'

  export default {
    name: 'Index',
    components: {
      CreateDetail,
      CompanySelectModal,
      SealDepartment,
      ExecutorOptions,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        date: undefined,
        queryForm: {},
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
        currentEdit: false,
        typeOptions: [],

        localKey: 'contract-account-hthz-search',
        tableKey: 'contract-account-hthz-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '合同名称' },
          { name: '公司名称' },
          { name: '合同类型' },
          { name: '项目名称' },
          { name: '收付款方向' },
          { name: '合同金额' },
          { name: '是否违约' },
          { name: '创建日期' },
        ],
      }
    },
    watch: {
      date(val) {
        if (val) {
          const [startdate, enddate] = val
          this.queryForm.startdate = startdate
          this.queryForm.enddate = enddate
        }
      },
    },
    created() {
      this.resetQueryForm()
      this.fetchTypes()
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
          { name: '合同编号', key: 'contractno' },
          { name: '合同名称', key: 'contractname' },
          { name: '相对方信息', key: 'budgetname' },
          { name: '合同类型', key: 'contracttype' },
          { name: '异常合同', key: 'isWy' },
          { name: '项目名称', key: 'contractitem' },
          { name: '收付款方向', key: 'dctype' },
          { name: '合同状态', key: 'contractstatus' },
          { name: '合同金额', key: 'money' },
          { name: '创建时间', key: 'date' },
          { name: '所属公司', key: 'orgname' },
          { name: '承办部门', key: 'contractdeptName' },
          { name: '承办人', key: 'contractstaffName' },
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
      //部门选择
      handleDepartmentSelected(node) {
        console.log('node', node)
        this.queryForm.contractdept = node.id
        this.queryForm.contractdeptName = node.label
        this.$forceUpdate()
      },
      handleExecutorSelected(node) {
        this.queryForm.contractstaff = node[0].staffid
        this.queryForm.contractstaffName = node[0].realname
        this.$forceUpdate()
      },
      //重置
      resetQueryForm() {
        this.queryForm = {
          contractno: undefined,
          contractname: undefined,
          contracttype: undefined,
          contractitem: undefined,
          budgetname: undefined,
          dctype: undefined,
          startdate: undefined,
          enddate: undefined,
          minMoney: undefined,
          maxMoney: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
      },
      //重置搜索
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      mapContractStatus(row) {
        const res = contractStatusOptions.filter((item) => {
          return item.value === row.contractstatus
        })
        return res[0].label
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        this.$refs['common'].showDetail(row, row.contracttype)
      },
      async fetchTypes() {
        const res = await getContractTypes()
        this.typeOptions2 = res.typeofList.reduce((prev, cur) => {
          const data = cur.childrenList.map((item) => {
            return {
              label: item.typename,
              value: item.typeid,
            }
          })
          return prev.concat(data)
        }, [])
        this.typeOptions = [
          { label: '未审批', value: '0' },
          { label: '审批中', value: '1' },
          // { label: '需调整', value: '2' },
          // { label: '已通过', value: '3' },
          { label: '已终止', value: '4' },
          // { label: '已跟踪', value: '5' },
          { label: '用印中', value: '6' },
          { label: '履行中', value: '7' },
          { label: '已归档', value: '8' },
          { label: '已暂停', value: '9' },
          { label: '已变更', value: '10' },
          { label: '已终止', value: '11' },
          { label: '履行完毕', value: '17' },
        ]
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
            pageInfo: { tlist, totalRecord },
          },
        } = await getStandingAllBookList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      async handleExport() {
        const data = await exportStandingAllBook(this.queryForm)
        let fileName = '合同汇总'
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

      showGroupLeader() {
        this.$refs.companySelect.show({
          labelKey: 'workUnitName',
          idKey: 'workUnitId',
          title: '工作单位',
        })
      },
      handleCompanyTreeSelected(val) {
        this.$set(this.queryForm, 'orgid', val.id)
        this.$set(this.queryForm, 'orgname', val.label)
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
