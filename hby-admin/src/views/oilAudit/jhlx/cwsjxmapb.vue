<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-top-panel>
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
              v-if="item.name === '填报单位'"
            >
              <el-input
                v-model="queryForm.name"
                clearable
                placeholder="填报单位"
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
        <el-button type="success" @click="handleAdd(null)">新增</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" :key="index" label="编号" prop="code">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.code }}
            </el-button>
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="批次"
            prop="batc"
            v-if="item.name === '批次'"
          />
          <el-table-column
            align="center"
            label="填报单位"
            prop="tbrgname"
            v-if="item.name === '填报单位'"
          />
          <el-table-column
            align="center"
            label="创建人"
            prop="createname"
            v-if="item.name === '创建人'"
          />
          <el-table-column
            align="center"
            label="创建时间"
            prop="createdate"
            v-if="item.name === '创建时间'"
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
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="row.status != '0'"
            >
              修改
            </el-button>
            <!-- <el-button type="text" @click="handleDelete(row)">删除</el-button> -->
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  @click.native="handleManage(row)"
                  :disabled="row.status == '0'"
                >
                  办理
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handleApproval(row)"
                  :disabled="row.status != '0' || btnLoading"
                >
                  提交审批
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handleDelete(row)"
                  :disabled="row.status != '0'"
                >
                  删除
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
    <cwsjxmapbOut ref="edit" @fetchData="fetchData" />

    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqdDeal" />
  </div>
</template>

<script>
  import {
    fundAuditOutProjectList,
    fundAuditOutProjectDelete,
  } from '@/api/monitor/question'
  import cwsjxmapbOut from './components/cwsjxmapbOut'
  import { formatDate } from '@/utils/index'

  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  export default {
    name: 'cwsjxmapb',
    components: {
      ProcessList,
      WfqdDeal,
      filterSearch,
      filterTable,
      cwsjxmapbOut,
    },

    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          name: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '批次' },
          { name: '填报单位' },
          { name: '创建人' },
          { name: '创建时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-jhlx-cwsjxmapb-search',
        tableKey: 'oilAudit-jhlx-cwsjxmapb-list',
        searchMore: true,
        btnLoading: false,
      }
    },
    created() {
      this.fetchData()

      this.initTable()
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
          this.$refs['process'].save(124, row.tbid)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.tbid,
          tableId: 124,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      getFiled() {
        return [{ name: '填报单位', key: 'name' }]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          name: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
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
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await fundAuditOutProjectList(this.queryForm)
        this.list = list || []
        this.total = total || 0
        this.listLoading = false
      },
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
          const { msg, code } = await fundAuditOutProjectDelete({
            tbid: row.tbid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
</style>
