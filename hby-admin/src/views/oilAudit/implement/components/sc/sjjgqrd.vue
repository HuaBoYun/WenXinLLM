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
                v-model="queryForm.orgidnames"
                clearable
                placeholder="被审计单位名称"
                v-if="item.name === '被审计单位名称'"
              ></el-input>
              <el-input
                v-model="queryForm.projectname"
                clearable
                placeholder="审计项目名称"
                v-if="item.name === '审计项目名称'"
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
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="编号" prop="resultcode">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.resultcode }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="审计项目名称"
            prop="projectname"
            v-if="item.name === '审计项目名称'"
          ></el-table-column>
          <el-table-column
            v-if="item.name === '被审计单位'"
            align="center"
            label="被审计单位"
            prop="orgidnames"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '合同编号'"
            align="center"
            label="合同编号"
            prop="contractcode"
          />
          <el-table-column
            v-if="item.name === '合同名称'"
            align="center"
            label="合同名称"
            prop="contractname"
          />
          <el-table-column
            v-if="item.name === '施工单位' && !isJZK"
            align="center"
            label="施工单位"
            prop="sgorgname"
          />
          <el-table-column
            v-if="item.name === '供应商' && isJZK"
            align="center"
            label="供应商"
            prop="sgorgname"
          />
          <el-table-column
            v-if="item.name === '合同金额(元)'"
            align="center"
            label="合同金额(元)"
            prop="contractmoney"
          />
          <el-table-column
            v-if="item.name === '核增金额(元)'"
            align="center"
            label="核增金额(元)"
            prop="hzmoney"
          />
          <el-table-column
            v-if="item.name === '核减金额(元)'"
            align="center"
            label="核减金额(元)"
            prop="hjmoney"
          />
          <el-table-column
            v-if="item.name === '审计认定金额(元)'"
            align="center"
            label="审计认定金额(元)"
            prop="sdmoney"
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
      </el-table>
    </el-card>
    <SJJGQRDview ref="edit" @fetchData="fetchData"></SJJGQRDview>
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
    <ProcessList ref="process" />
    <WfqdDeal ref="wfqdDeal" />
  </div>
</template>

<script>
  import { resultList, resultDeleteone } from '@/oapi/audit/implement'
  import SJJGQRDview from '../sjjgqrdView.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  export default {
    components: {
      SJJGQRDview,
      filterTable,
      filterSearch,
      ProcessList,
      WfqdDeal,
    },
    props: {
      isShow: {
        //项目查看传参
        type: Boolean,
        default: true,
      },
      row: {
        type: Object,
        default: () => {},
      },
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          orgidnames: undefined,
          projectname: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '审计项目名称' },
          { name: '被审计单位' },
          { name: '合同名称' },
          { name: '合同编号' },
          { name: '合同金额(元)' },
          { name: '核增金额(元)' },
          { name: '核减金额(元)' },
          { name: '审计认定金额(元)' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'oilAudit-implement-sjjgqrd-search',
        tableKey: 'oilAudit-implement-sjjgqrd-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        isJZK: false,
      }
    },
    created() {
      //判断权限是否有经责科.展示不同title
      let userInfo = JSON.parse(localStorage.getItem('userInfo'))
      if (!userInfo.roleNames.includes('经责科审计人员')) {
        this.isJZK = false
        this.filedAll.splice(5, 0, { name: '施工单位' })
      } else {
        this.filedAll.splice(5, 0, { name: '供应商' })
        this.isJZK = true
      }
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
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
      handleApproval(row) {
        this.$refs['process'].save(151, row.resultid)
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.resultid,
          tableId: 151,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '被审计单位名称', key: 'orgidnames' },
          { name: '审计项目名称', key: 'projectname' },
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
        const {
          data: {
            data: { tlist, totalRecord },
          },
        } = await resultList({
          ...this.queryForm,
          projectId: this.row && this.row.projectId,
          templateId: this.row && this.row.templateId,
        })
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
      },
      handleExport() {},
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit', this.row)
      },
      handleAdd(i) {
        this.$refs['edit'].showEdit(null, 'add', i)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await resultDeleteone({
            resultid: row.resultid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error')
          }
        })
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          orgidnames: undefined,
          projectname: undefined,
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
