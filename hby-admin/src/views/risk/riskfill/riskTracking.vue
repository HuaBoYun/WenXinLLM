<template>
  <!-- 三级单位及成员离任审计 -->
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
              <el-input
                v-model="queryForm.jd"
                clearable
                placeholder="季度"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '季度'"
              ></el-input>
              <el-date-picker
                v-if="item.name === '年度'"
                v-model="queryForm.nd"
                type="year"
                format="yyyy"
                value-format="yyyy"
                placeholder="年度"
              ></el-date-picker>
              <el-input
                v-if="item.name === '上报单位'"
                placeholder="上报单位"
                v-model="queryForm.orgName"
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
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          align="center"
          label="季度"
          prop="quartername"
          width="100"
        >
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
            label="上报单位"
            prop="linkOrgName"
            v-if="item.name === '上报单位'"
          />
          <el-table-column
            align="center"
            label="上报人"
            prop="createname"
            v-if="item.name === '上报人'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="创建时间"
            prop="createtime"
            v-if="item.name === '创建时间'"
          ></el-table-column>
          <el-table-column
            v-if="item.name === '上报状态'"
            align="center"
            label="上报状态"
            prop="toreport"
          >
            <template #default="{ row }">
              {{ row.toreport == 1 ? '已上报' : '未上报' }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '状态'"
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
        <el-table-column align="center" label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="handleView(row)">
              下发风险列表
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    :disabled="!+row.status"
                    @click="handleDeal(row)"
                    type="text"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    @click="$refs.transferList.showEdit(row)"
                    type="text"
                  >
                    转派记录
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
    <WfqdDeal ref="wfqddeal" />
    <cetateEdit ref="add" @fetchData="fetchData" />
    <riskfillInside ref="inside" :isTracking="true" />
    <riskfillNewEdit ref="riskfillNewEdit" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
    <executor-options
      ref="executor"
      @projectManage="handleExecutor"
      :secrectLevelId="secrectLevelId"
    />
    <TransferList ref="transferList" />
  </div>
</template>

<script>
  import { getMajorRiskTrack } from '@/api/risk/create.js'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { baseURL } from '@/config'
  import cetateEdit from './cetateEdit.vue'
  import store from '@/store'
  import riskfillInside from './riskfill_Inside.vue'
  const token = store.getters['user/token']
  import { reportToLeader } from '@/api/risk/riskfill'
  import riskfillNewEdit from './riskfillNewEdit.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import TransferList from './TransferList.vue'
  export default {
    components: {
      filterTable,
      filterSearch,
      ProcessList,
      WfqdDeal,
      cetateEdit,
      riskfillInside,
      riskfillNewEdit,
      ExecutorOptions,
      TransferList,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/plan/leave/audit3L/importData',
        headers: { token },
        select: [],
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          jd: '',
          oldJob: '',
          pageNumber: 1,
          pageSize: 20,
          nd: '',
        },
        filedAll: [
          { name: '上报单位' },
          { name: '上报人' },
          { name: '创建时间' },
          { name: '上报状态' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-riskfill-riskcreate-search',
        tableKey: 'risk-riskfill-riskcreate-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
        secrectLevelId: '',
        id: '',
      }
    },
    created() {
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
          { name: '季度', key: 'jd' },
          { name: '年度', key: 'nd' },
          { name: '上报单位', key: 'orgName' },
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
          } = await getMajorRiskTrack(this.queryForm)
          this.list = pageInfo.list
          this.total = pageInfo.total
        } catch (error) {
          console.error('获取数据失败:', error)
        } finally {
          this.listLoading = false
        }
        this.setCheckedRows()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        const pageSize = this.queryForm.pageSize
        this.queryForm = {
          jd: '',
          oldJob: '',
          createType: 1,
          pageNumber: 1,
          pageSize,
          nd: '',
        }
        this.fetchData()
      },
      handleView(row) {
        this.$refs['inside'].showEdit(row)
      },
      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          if (!this.$refs.multipleTable) return
          // 清除之前的选择状态
          this.$refs.multipleTable.clearSelection()
          // 设置新的选择状态
          this.select.forEach((row) => {
            const matchedRow = this.list.find((item) => item.jdid === row.jdid)
            if (matchedRow) {
              this.$refs.multipleTable.toggleRowSelection(matchedRow, true)
            }
          })
        })
      },
      handleDetail(row) {
        this.$refs['riskfillNewEdit'].showEdit(row, 'detail')
      },
      async handleDeal(row) {
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 213,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      async handleExecutor(val) {
        const ids = this.select.map((res) => res.id)
        const { code } = await majorTransfer({
          ids: ids.toString(),
          staffId: val[0].staffid,
        })
        if (code == 1) this.$message.success('转派成功')
        this.select = []
        this.$nextTick(() => {
          this.$refs.multipleTable && this.$refs.multipleTable.clearSelection()
        })
        this.fetchData()
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
