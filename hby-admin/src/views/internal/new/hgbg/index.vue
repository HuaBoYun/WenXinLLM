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
            <el-form-item
              v-for="(item, index) in searchItem"
              :key="index"
              :prop="item.key"
            >
              <el-input
                v-model="queryForm.reportName"
                clearable
                :placeholder="$translateTitle('报告名称')"
                v-if="item.name === '报告名称'"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="fetchData"
              >
                {{ $translateTitle('查询') }}
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button @click="fetchData('reset')">
                {{ $translateTitle('重置') }}
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
                <span>
                  {{
                    searchMore
                      ? $translateTitle('收起')
                      : $translateTitle('展开')
                  }}
                </span>
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
        <el-button type="success" @click="handleAdd">
          {{ $translateTitle('新建') }}
        </el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          :label="$translateTitle('序号')"
          type="index"
        />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            :label="$translateTitle('报告名称')"
            prop="reportName"
            v-if="item.name === '报告名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.reportName }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            :label="$translateTitle('报告类型')"
            prop="reporttType"
            v-if="item.name === '报告类型'"
          ></el-table-column>
          <!-- <el-table-column
            align="center"
            :label="$translateTitle('层级')"
            prop="hierarchy"
            v-if="item.name === '层级'"
          ></el-table-column> -->
          <el-table-column
            align="center"
            :label="$translateTitle('拟稿人')"
            prop="draftsmanName"
            v-if="item.name === '拟稿人'"
          ></el-table-column>
          <el-table-column
            align="center"
            :label="$translateTitle('部门负责人')"
            prop="departmentHeadName"
            v-if="item.name === '部门负责人'"
          ></el-table-column>
          <el-table-column
            align="center"
            :label="$translateTitle('创建人')"
            prop="creatorName"
            v-if="item.name === '创建人'"
          ></el-table-column>
          <el-table-column
            align="center"
            :label="$translateTitle('创建时间')"
            prop="createdTime"
            v-if="item.name === '创建时间'"
          ></el-table-column>
          <el-table-column
            align="center"
            :label="$translateTitle('审批状态')"
            prop="state"
            v-if="item.name === '审批状态'"
          >
            <template #default="{ row }">
              {{
                row.state == 1
                  ? '审批中'
                  : row.state == 2
                  ? '需调整'
                  : row.state == 3
                  ? '已撤销'
                  : row.state == 4
                  ? '已终止'
                  : row.state == 5
                  ? '已跟踪'
                  : row.state == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>

        <el-table-column align="center" :label="$translateTitle('操作')">
          <template #default="{ row }">
            <!-- <el-button type="text">评价</el-button> -->
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="row.state != 0"
            >
              {{ $translateTitle('修改') }}
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">{{ $translateTitle('更多') }}</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleManage(row)"
                    :disabled="!row.state"
                  >
                    {{ $translateTitle('办理') }}
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleApproval(row)"
                    :disabled="!!row.state"
                  >
                    {{ $translateTitle('提交审批') }}
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    :disabled="!!row.state"
                  >
                    {{ $translateTitle('删除') }}
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
    <Views ref="edit" @fetchData="fetchData"></Views>
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import Views from './components/indexView.vue'
  import { getReportList, reportDelete } from '@/api/hggl/hgjhgl'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'
  export default {
    name: '',
    components: { filterTable, filterSearch, Views, ProcessList, WfqdDeal },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          planName: '',
        },
        filedAll: [
          { name: '报告名称' },
          { name: '报告类型' },
          { name: '层级' },
          { name: '拟稿人' },
          { name: '部门负责人' },
          { name: '创建人' },
          { name: '创建时间' },
          { name: '审批状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'internal-hgbg-hgbg-search',
        tableKey: 'internal-hgbg-hgbg-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        isHeadquartersLegal: false, // 法务总部权限
        type: ['', '类型1', '类型2', '类型3'],
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
        console.log('qwe')
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(65, row.id)
      },
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 65,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [{ name: '报告名称', key: 'reportName' }]
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
        console.log(this.searchMore)
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
      handleAdd() {
        this.$refs['edit'].showEdit('新增')
      },
      fetchData(type) {
        this.listLoading = true
        if (type && type === 'reset') {
          this.$refs['form'].resetFields()
          this.queryForm.planName = ''
        }
        getReportList(this.queryForm).then((res) => {
          this.list = res.data.tlist
          this.total = res.data.totalRecord
          this.listLoading = false
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
      handleEdit(row) {
        this.$refs['edit'].showEdit('编辑', row)
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit('详情', row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await reportDelete({ id: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
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
