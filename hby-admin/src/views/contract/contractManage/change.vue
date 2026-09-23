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
                v-model="queryForm.contractbd"
                clearable
                placeholder="合同标的"
                v-if="item.name === '合同标的'"
              />
              <el-select
                v-model="queryForm.contracttype"
                filterable
                placeholder="合同类型"
                style="width: 100%"
                v-if="item.name === '合同类型'"
              >
                <el-option
                  v-for="item in typeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"
                />
              </el-select>
              <el-input
                v-model="queryForm.orgmeno"
                clearable
                placeholder="请选择承办部门"
                style="width: 187px"
                v-if="item.name === '承办部门'"
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.department.show()"
                v-if="item.name === '承办部门'"
              >
                选择
              </el-button>
              <el-input
                v-model="queryForm.budgetname"
                clearable
                placeholder="请选择相对方"
                v-if="item.name === '相对方'"
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
          <el-table-column
            align="center"
            label="合同类型"
            prop="contracttype"
            v-if="item.name === '合同类型'"
          />
          <el-table-column
            align="center"
            label="合同金额"
            prop="contractmoney"
            v-if="item.name === '合同金额'"
          />
          <el-table-column
            align="center"
            label="开始日期"
            prop="startdate"
            v-if="item.name === '开始日期'"
          />
          <el-table-column
            align="center"
            label="结束日期"
            prop="enddate"
            v-if="item.name === '结束日期'"
          />
          <el-table-column
            align="center"
            label="审批状态"
            prop="contractstatus"
            v-if="item.name === '审批状态'"
          >
            <template #default="{ row }">
              {{ mapContractStatus(row) }}
            </template>
          </el-table-column>
        </div>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="
                row.contractstatus != 0 &&
                row.contractstatus != 2 &&
                row.contractstatus != 3
              "
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <!-- <el-dropdown-item @click.native="handleDeal(row)">
                  查看流程图
                </el-dropdown-item> -->
                <el-dropdown-item
                  :disabled="!row.contractstatus"
                  @click.native="handleManage(row)"
                >
                  办理
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handelApproval(row)"
                  :disabled="row.contractstatus"
                >
                  提交审批
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleOffice(row)">
                  合同正文预览
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleRemind(row)">
                  定时提醒
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handelRecall(row)"
                  :disabled="row.contractstatus"
                >
                  撤回
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
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
    <SealDepartment ref="department" @selected="handleDepartmentSelected" />
    <RemindEdit ref="remind" />
    <ChangeEdit ref="common" @fetch-data="fetchData" />
    <ChangeDetail ref="detail" :isDialog="false" />
    <Deal ref="deal" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import {
    checkStageInfo,
    checkStatusForRecall,
    getContractList,
    getContractTypes,
    recallContractModify,
  } from '@/api/contract/manage'
  // import DepartmentOptions from './components/options/department.vue'
  import { contractStatusOptions } from '@/views/contract/consts'
  import ChangeDetail from './components/contractsEdit/ChangeDetail.vue'
  import ChangeEdit from './components/contractsEdit/ChangeEdit'
  import Deal from './components/Deal.vue'
  import SealDepartment from './components/options/sealDepartment.vue'
  import RemindEdit from './components/RemindEdit.vue'
  // import { renderContract } from '@/views/contract/methods'
  import ProcessList from './components/ProcessList.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'

  export default {
    name: 'Change',
    components: {
      RemindEdit,
      ChangeEdit,
      ChangeDetail,
      SealDepartment,
      Deal,
      ProcessList,
      filterTable,
      filterSearch,
      WfqdDeal,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {},
        currentEdit: 'moren',
        typeOptions: [],

        localKey: 'contract-contractManage-change-search',
        tableKey: 'contract-contractManage-change-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '合同名称' },
          { name: '合同类型' },
          { name: '合同金额' },
          { name: '开始日期' },
          { name: '结束日期' },
          { name: '审批状态' },
        ],
      }
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
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.contractid,
          tableId: 7,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
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
          { name: '合同标的', key: 'contractbd' },
          { name: '合同类型', key: 'contracttype' },
          { name: '承办部门', key: 'orgmeno' },
          { name: '相对方', key: 'budgetname' },
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
      //重置
      resetQueryForm() {
        this.queryForm = {
          contractno: undefined,
          contractname: undefined,
          contracttype: undefined,
          contractbd: undefined,
          budgetname: undefined,
          contractdept: undefined,
          orgmeno: undefined,
          flowId: 622325,
          pageNumber: 1,
          pageSize: 20,
        }
      },
      //重置
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
        } = await getContractList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      //删除
      handleDeal(row) {
        if (row.contractstatus == 0) {
          this.$baseMessage('请先提交审批', 'error', 'vab-hey-message-error')
          return
        }
        this.$refs['deal'].show(row, 'change')
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        this.$refs['detail'].showDetail(row, row.contracttype)
      },
      handleEdit(row) {
        this.$refs['common'].showEdit(row, row.contracttype)
      },
      handleRemind(row) {
        this.$refs['remind'].showEdit(row)
      },
      async handelApproval(row) {
        if (row.contractstatus === 0 || row.contractstatus === '') {
          const { code, msg } = await checkStageInfo({
            contractId: row.contractid,
          })
          if (code == 1) {
            const tableId = 7
            const fromId = row.contractid
            this.$refs['process'].save(tableId, fromId, row.contracttype)
          }
        } else {
          this.$baseMessage('流程进行中', 'error', 'vab-hey-message-error')
        }
      },
      async handelRecall(row) {
        // 撤回单独用一个状态判断接口，返回非0和非1值时提示
        const { code } = await checkStatusForRecall({
          contractId: row.contractid,
        })
        if (code == 1) {
          this.$baseConfirm('你确定要撤回吗', null, async () => {
            const { code, msg } = await recallContractModify({
              contractId: row.contractid,
              flowname: 'HTGL005',
            })
            if (code == 1) {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              this.fetchData()
            }
          })
        } else {
          this.$baseMessage(
            '已审批，无法撤回',
            'error',
            'vab-hey-message-error'
          )
        }
      },
      handleDepartmentSelected(node) {
        this.queryForm.contractdept = node.id
        this.queryForm.orgmeno = node.name
      },
      handleOffice(val) {
        const info = JSON.parse(localStorage.getItem('userInfo'))
        window.open(
          `https://office.wenxin.example.com/api/office/getContractReview?fileType=word&contractId=${val.contractid}&uid=${info.staffid}&name=${info.username}`
        )
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
