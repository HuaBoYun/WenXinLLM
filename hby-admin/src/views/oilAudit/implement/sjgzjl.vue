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
                v-model="queryForm.indexno"
                clearable
                placeholder="索引号"
                v-if="item.name === '索引号'"
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
              <el-button type="primary" @click="resetSearch()">重置</el-button>
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
        <el-button type="success" @click="handleAdd" v-if="isShow">
          新建
        </el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            :key="index"
            label="索引号"
            prop="indexno"
            v-if="item.name === '索引号'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.indexno }}
              </el-button>
            </template>
          </el-table-column>

          <el-table-column
            align="center"
            :key="index"
            label="被审计单位名称"
            prop="auditeeName"
            v-if="item.name === '被审计单位名称'"
          ></el-table-column>

          <el-table-column
            align="center"
            label="实施审计时间"
            prop="implementationTime"
            v-if="item.name === '实施审计时间'"
          />
          <el-table-column
            align="center"
            label="审计项目名称"
            prop="projectName"
            v-if="item.name === '审计项目名称'"
          ></el-table-column>
          <!-- <el-table-column
            align="center"
            label="分工负责审计内容"
            prop="responsibleContent"
            v-if="item.name === '分工负责审计内容'"
          /> -->
          <el-table-column
            align="center"
            label="审计内容和目标"
            prop="contentObjectives"
            v-if="item.name === '审计内容和目标'"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="执行的审计程序和工作过程"
            prop="executedProceduresProcesses"
            v-if="item.name === '执行的审计程序和工作过程'"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="发现的疑点、线索及查证情况"
            prop="verificationSituation"
            v-if="item.name === '发现的疑点、线索及查证情况'"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="审计线索及数据来源"
            prop="cluesSources"
            v-if="item.name === '审计线索及数据来源'"
            show-overflow-tooltip
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
          <el-table-column
            align="center"
            label="创建人"
            prop="realname"
            v-if="item.name === '创建人'"
          />
        </div>
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <!-- <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button> -->
            <el-button
              type="text"
              v-if="isShow"
              :disabled="!!Number(row.status)"
              @click="handleEdit(row)"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleManage(row)"
                    :disabled="!Number(row.status)"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item v-if="isShow">
                  <el-button
                    type="text"
                    @click="handleApproval(row)"
                    :disabled="!!Number(row.status) || btnLoading"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item v-if="isShow">
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    :disabled="!!Number(row.status)"
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
    <SJGZJLview ref="edit" @fetchData="fetchData"></SJGZJLview>
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
  import { workRecordsList, workRecordsDelete } from '@/oapi/audit/implement'
  import SJGZJLview from './components/sjgzjlView.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  export default {
    components: {
      SJGZJLview,
      filterTable,
      filterSearch,
      ProcessList,
      WfqdDeal,
    },
    props: {
      //项目查看传参
      isShow: {
        type: Boolean,
        default: true,
      },
      projectId: {
        type: Number,
        default: null,
      },
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          indexno: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '索引号' },
          { name: '被审计单位名称' },
          { name: '实施审计时间' },
          { name: '审计项目名称' },
          { name: '分工负责审计内容' },
          { name: '审计内容和目标' },
          { name: '执行的审计程序和工作过程' },
          { name: '发现的疑点、线索及查证情况' },
          { name: '审计线索及数据来源' },
          { name: '状态' },
          { name: '创建人' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'oilAudit-implement-sjgzjl-search',
        tableKey: 'oilAudit-implement-sjgzjl-list',
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
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      handleApproval(row) {
        try {
          this.btnLoading = true
          this.$refs['process'].save(153, row.id)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 153,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [{ name: '索引号', key: 'indexno' }]
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
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await workRecordsList({
          ...this.queryForm,
          projectId: this.projectId,
        })
        this.list = list || []
        this.total = total || 0
        this.listLoading = false
      },
      handleExport() {},
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit')
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await workRecordsDelete({ id: row.id })
          if (code == 1) {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
          } else {
            this.$baseMessage('失败', 'error')
          }
          await this.fetchData()
        })
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          indexno: '',
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
