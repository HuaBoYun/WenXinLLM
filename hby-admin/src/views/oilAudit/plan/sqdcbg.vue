<template>
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
                v-model="queryForm.xmmc"
                clearable
                placeholder="项目名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '项目名称'"
              ></el-input>
              <el-input
                v-model="queryForm.jhmc"
                clearable
                placeholder="计划名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '计划名称'"
              ></el-input>
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
        <el-button type="success" @click="handleAdd()">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '方案编号'"
            align="center"
            label="方案编号"
            prop="xtcode"
          />
          <el-table-column
            v-if="item.name === '项目名称'"
            align="center"
            label="项目名称"
            prop="xmmc"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.xmmc }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="计划名称"
            prop="jhmc"
            v-if="item.name === '计划名称'"
          />
          <el-table-column
            align="center"
            label="编制人"
            prop="cjr"
            v-if="item.name === '编制人'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="编制日期"
            prop="cjsj"
            v-if="item.name === '编制日期'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="状态"
            prop="spzt"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{
                row.spzt == 1
                  ? '审批中'
                  : row.spzt == 2
                  ? '已退回'
                  : row.spzt == 3
                  ? '已撤回'
                  : row.spzt == 4
                  ? '已终止'
                  : row.spzt == 5
                  ? '已跟踪'
                  : row.spzt == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="!!row.spzt"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  @click.native="handleManage(row)"
                  :disabled="!row.spzt"
                >
                  办理
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handleApproval(row)"
                  :disabled="!!row.spzt || btnLoading"
                >
                  提交审批
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handleDelete(row)"
                  :disabled="!!row.spzt"
                >
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <Views ref="edit" @fetchData="fetchData"></Views>
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
    <WfqdDeal ref="wfqdDeal" />
  </div>
</template>

<script>
  import { xmglsqdcbgList, xmglsqdcbgDelete } from '@/oapi/audit/project'
  import Views from './components/sqdcbgView.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  export default {
    components: {
      Views,
      filterTable,
      filterSearch,
      ProcessList: () =>
        import('@/views/contract/contractManage/components/ProcessList'),
      WfqdDeal: () => import('@/views/msg/components/options/WfqdDeal'),
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          jhmc: undefined,
          xmmc: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '方案编号' },
          { name: '项目名称' },
          { name: '计划名称' },
          { name: '编制人' },
          { name: '编制日期' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'oilAudit-plan-sqdcbg-search',
        tableKey: 'oilAudit-plan-sqdcbg-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        btnLoading: false,
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) this.fetchData()
      })
    },
    methods: {
      handleApproval(row) {
        try {
          this.btnLoading = true
          this.$refs['process'].save(161, row.sqdcbgid)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.sqdcbgid,
          tableId: 161,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '项目名称', key: 'xmmc' },
          { name: '计划名称', key: 'xmmc' },
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
        this.btnLoading = false
        this.listLoading = true
        const {
          data: { list, total },
        } = await xmglsqdcbgList(this.queryForm)
        this.list = list || []
        this.total = total || 0
        this.listLoading = false
      },
      handleExport() {},
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit')
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await xmglsqdcbgDelete({ ids: row.sqdcbgid })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          jhmc: undefined,
          xmmc: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleView(row) {
        this.$refs['check'].showEdit(row)
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
