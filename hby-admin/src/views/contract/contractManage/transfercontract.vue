<!--
 * @Date: 2022-04-16 22:35:32
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-11 10:29:10
 * @FilePath: /hb-admin/src/views/contract/contractManage/seal.vue
-->
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
                v-model="queryForm.contractName"
                clearable
                placeholder="合同名称"
                v-if="item.name === '合同名称'"
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
        <el-button type="success" @click="handleAdd()">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="合同编号" prop="contractNo">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.contractNo }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="合同名称"
            prop="contractName"
            v-if="item.name === '合同名称'"
          />
          <el-table-column
            align="center"
            label="合同份数"
            prop="contractCnt"
            v-if="item.name === '合同份数'"
          />
          <el-table-column
            align="center"
            label="所属单位"
            prop="tranOrgName"
            v-if="item.name === '所属单位'"
          />
          <el-table-column
            align="center"
            label="经办部门"
            prop="handDeptName"
            v-if="item.name === '经办部门'"
          />
          <el-table-column
            align="center"
            label="经办人"
            prop="handStaffName"
            v-if="item.name === '经办人'"
          />
          <el-table-column
            align="center"
            label="状态"
            prop="tranStatus"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{
                row.tranStatus == 1
                  ? '审批中'
                  : row.tranStatus == 2
                  ? '已退回'
                  : row.tranStatus == 3
                  ? '已撤回'
                  : row.tranStatus == 4
                  ? '已终止'
                  : row.tranStatus == 5
                  ? '已跟踪'
                  : row.tranStatus == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column align="center" label="操作" show-overflow-tooltip>
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="
                row.tranStatus != 0 &&
                row.tranStatus != 2 &&
                row.tranStatus != 3
              "
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleApproval(row)"
              :disabled="row.tranStatus"
            >
              申请移交
            </el-button>
            <el-button
              type="text"
              @click="handleManage(row)"
              :disabled="!row.tranStatus"
            >
              办理
            </el-button>
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
    <TansfercontractEdit ref="edit" @fetch-data="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
import { getContractTranList } from '@/api/contract/manage'
import TansfercontractEdit from './components/TansfercontractEdit.vue'
import ProcessList from './components/ProcessList.vue'
import filterSearch from '@/components/filterSearch.vue'
import filterTable from '@/components/filterTable.vue'
import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
import { getFlowPkInfo } from '@/api/contract/manage'

export default {
  name: 'Seal',
  components: {
    TansfercontractEdit,
    WfqdDeal,
    ProcessList,
    filterSearch,
    filterTable,
  },
  data() {
    return {
      list: [],
      listLoading: true,
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      queryForm: {
        contractName: undefined,
      },

      localKey: 'contract-contractManage-transfercontract-search',
      tableKey: 'contract-contractManage-transfercontract-list',
      searchNow: [],
      searchMore: true,
      searchItem: [],
      searchAll: this.getFiled(), //所有搜索项
      filedNow: [],
      filedAll: [
        { name: '合同名称' },
        { name: '合同份数' },
        { name: '所属单位' },
        { name: '经办部门' },
        { name: '经办人' },
        { name: '状态' },
      ],
    }
  },
  created() {
    this.resetQueryForm()
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
        formId: row.tranId,
        tableId: 1,
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
      let fields = [{ name: '合同名称', key: 'contractName' }]
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
    //打印
    handlePrint(row) {
      this.$refs['PrintCom'].printClick(row)
    },
    //重置
    resetQueryForm() {
      this.queryForm = {
        contractname: undefined,
        flowId: 622324,
        pageNumber: 1,
        pageSize: 20,
      }
    },
    //重置
    resetSearch() {
      this.resetQueryForm()
      this.fetchData()
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
      } = await getContractTranList(this.queryForm)
      this.list = tlist
      this.total = totalRecord
      this.listLoading = false
    },
    handleAdd() {
      this.$refs['edit'].showEdit(null, 'add')
    },
    /**
     * @description: 打开详情表单弹框
     * @param {*} row 选择的行数据
     * @return {*}
     */
    handleDetail(row) {
      this.$refs['edit'].showEdit(row, 'detail')
    },
    handleEdit(row) {
      this.$refs['edit'].showEdit(row, 'edit')
    },
    //流程
    handleApproval(row) {
      if (!row.tranStatus) {
        // this.$refs['process'].show(row, 4)
        const tableId = 1
        const fromId = row.tranId
        this.$refs['process'].save(tableId, fromId)
      } else {
        this.$baseMessage('流程进行中', 'error', 'vab-hey-message-error')
      }
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
