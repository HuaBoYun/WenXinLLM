<template>
  <div class="system-log-container">
    <vab-query-form>
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
              v-if="item.name === '名称'"
              v-model="queryForm.hcname"
              placeholder="名称"
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
            <el-button native-type="submit" type="primary" @click="resetSearch">
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
        </el-form>
      </vab-query-form-top-panel>
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
        <el-button type="success" @click="handleEdit('新增', null)">
          新建
        </el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table v-loading="listLoading" :data="list">
      <div v-for="(item, index) in filedNow" :key="index">
        <el-table-column
          align="center"
          label="编号"
          prop="hcnumber"
          show-overflow-tooltip
          v-if="item.name === '编号'"
        />
        <el-table-column
          align="center"
          label="名称"
          prop="hcname"
          show-overflow-tooltip
          v-if="item.name === '名称'"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit('详情', row)">
              {{ row.hcname }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="填报单位"
          prop="editorgname"
          show-overflow-tooltip
          v-if="item.name === '填报单位'"
        />
        <el-table-column
          align="center"
          label="编制时间"
          prop="edittime"
          show-overflow-tooltip
          v-if="item.name === '编制时间'"
        />
        <el-table-column
          align="center"
          label="状态"
          prop="status"
          show-overflow-tooltip
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
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="180"
      >
        <template #default="{ row }">
          <el-button
            type="text"
            @click="handleEdit('编辑', row)"
            :disabled="!!row.status"
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
                  :disabled="!row.status"
                >
                  办理
                </el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  type="text"
                  @click="handleApproval(row)"
                  :disabled="!!row.status"
                >
                  提交审批
                </el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  :disabled="!!row.status"
                >
                  删除
                </el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <wthcView ref="edit" @fetchData="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqdDeal" />
  </div>
</template>
<script>
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import wthcView from './components/wthcView.vue'
  import { wthcList, wthcDelete } from '@/oapi/audit/wgzrzj.js'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import { getFlowPkInfo } from '@/api/setting/system.js'
  export default {
    components: { wthcView, filterSearch, filterTable, ProcessList, WfqdDeal },
    name: 'wthc',
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          hcname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        listLoading: false,
        list: [],
        filedAll: [
          { name: '编号' },
          { name: '名称' },
          { name: '填报单位' },
          { name: '编制时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-wgzrzj-wthc-search',
        tableKey: 'oilAudit-wgzrzj-wthc-list',
      }
    },
    created() {
      this.fetchData()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {},
    methods: {
      handleApproval(row) {
        this.$refs['process'].save(200, row.id)
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.slid,
          tableId: 200,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      getFiled() {
        return [{ name: '名称', key: 'hcname' }]
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
      handleEdit(name, row) {
        this.$refs.edit.show(name, row)
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await wthcList({ ...this.queryForm })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async handleDetail(a, b) {
        await this.$refs['edit'].showEdit(a, b)
      },
      resetQueryForm() {
        this.queryForm = {
          hcname: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await wthcDelete({
            id: row.id,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
    },
  }
</script>
