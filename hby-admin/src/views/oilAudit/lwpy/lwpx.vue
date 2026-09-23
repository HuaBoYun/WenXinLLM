<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <div style="display: flex" v-if="item.name === '填报单位'">
                <el-input
                  v-model="queryForm.tbrgname"
                  :style="{ width: '80%' }"
                  disabled
                  placeholder="请输入填报单位"
                />
                <el-button
                  @click="$refs['audiTree'].showEdit()"
                  style="margin-left: 10px"
                  type="primary"
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
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never">
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
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="序号" type="index" width="50" />
        <el-table-column
          align="center"
          label="小组名称"
          prop="xzname"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.xzname }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="组长"
            v-if="item.name === '组长'"
            prop="zznames"
          />
          <el-table-column
            align="center"
            label="副组长"
            v-if="item.name === '副组长'"
            prop="fznames"
          />
          <el-table-column
            align="center"
            label="评委组"
            v-if="item.name === '评委组'"
            prop="pwenames"
          />
          <el-table-column
            align="center"
            label="填报单位"
            v-if="item.name === '填报单位'"
            prop="tbrgname"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '状态'"
            align="center"
            label="状态"
            prop="state"
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
          width="120"
        >
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="!!row.status"
              v-if="row.createstaffid == userInfo.staffid"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleEdit(row, 1)"
              :disabled="!!row.status"
              v-if="row.pwstaffids.includes(userInfo.staffid)"
            >
              排序
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleManage(row)"
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
                    v-if="row.fsstaffids == userInfo.staffid"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    @click="handleDelete(row)"
                    type="text"
                    :disabled="!!row.status"
                    v-if="row.createstaffid == userInfo.staffid"
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
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <lwpxEdit ref="edit" @fetchData="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
  </div>
</template>

<script>
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import { getLwpxList, deleteLwpx, yzpfLwpx } from '@/oapi/audit/lwpy'
  import { parseTime } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import lwpxEdit from '@/views/oilAudit/lwpy/components/lwpxEdit.vue'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'

  export default {
    name: 'lwpx',
    components: {
      filterSearch,
      filterTable,
      lwpxEdit,
      WfqdDeal,
      ProcessList,
      SelectDepartment,
    },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          tbrgid: '',
          tbrgname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '组长' },
          { name: '副组长' },
          { name: '评委组' },
          { name: '填报单位' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-lwpy-lwpx-search',
        tableKey: 'oilAudit-lwpy-lwpx-list',
        searchMore: true,
        userInfo: JSON.parse(localStorage.getItem('userInfo')),
      }
    },
    created() {
      console.log('🚀 ~ data ~ userInfo:', this.userInfo)
      this.fetchData()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      getDepartmentInfo(val) {
        this.queryForm.tbrgname = val.label
        this.queryForm.tbrgid = val.id
      },
      getFiled() {
        return [{ name: '填报单位', key: 'tbrgid' }]
      },
      formatDate(row, column) {
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
      },
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
          tbrgid: '',
          tbrgname: '',
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
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
            pageInfo: { tlist, totalRecord },
          },
        } = await getLwpxList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit('detail', row)
      },
      async handleEdit(row, type) {
        await this.$refs['edit'].showEdit('edit', row, type)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteLwpx({
            tbid: row.tbid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
            this.fetchData()
          }
        })
      },
      async handleApproval(row) {
        const { code, msg } = await yzpfLwpx({ tbid: row.tbid })
        if (code == 0) return
        //提交审批
        this.$refs['process'].save(191, row.tbid)
      },
      // 办理
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.tbid,
          tableId: 191,
        })
        this.$refs.wfqddeal.show(res.data, false)
      },
    },
  }
</script>

<style scoped>
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }

  .margin-b0 {
    margin-bottom: 0;
  }

  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 100%;
  }
</style>
