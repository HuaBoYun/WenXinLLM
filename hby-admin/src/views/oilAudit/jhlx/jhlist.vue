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
            >
              <el-input
                v-model="queryForm.sjxmmc"
                clearable
                v-if="item.name === '计划名称'"
                placeholder="计划名称"
              />
              <!-- <el-date-picker
                v-model="queryForm.gxsj"
                placeholder="更新时间"
                type="datetime"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                v-if="item.name === '更新时间'"
              /> -->
              <el-input
                v-model="queryForm.cjr"
                clearable
                v-if="item.name === '编制人'"
                placeholder="编制人"
              />
              <el-date-picker
                type="date"
                v-model="queryForm.cjsj"
                placeholder="创建时间"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                v-if="item.name === '创建时间'"
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
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '计划编号'"
            align="center"
            label="计划编号"
            prop="jhcgNo"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '计划名称'"
            align="center"
            :key="index"
            label="计划名称"
            prop="sjxmmc"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.sjxmmc }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '计划年度'"
            prop="xmnd"
            label="计划年度"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '计划类型'"
            prop="jhlx"
            label="计划类型"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ row.jhlx == 1 ? '年初计划' : '新增计划' }}
            </template>
          </el-table-column>
          <el-table-column
            v-if="item.name === '编制人'"
            align="center"
            label="编制人"
            prop="cjr"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '创建时间'"
            align="center"
            label="创建时间"
            prop="cjsj"
            show-overflow-tooltip
          />
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
                <!-- <el-dropdown-item @click.native="handleCopy(row)">
                  复制
                </el-dropdown-item> -->
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

    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <jhcgEdit ref="edit" @fetchData="fetchData" />

    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqdDeal" />
  </div>
</template>

<script>
  import {
    jhgljhList,
    jhgljhDelete,
    copyUniquecg,
  } from '@/api/monitor/question'
  // import DraftInfo from "./components/DraftInfo";
  import jhcgEdit from './components/jhcgEdit2'
  import { formatDate } from '@/utils/index'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  export default {
    name: 'jhcg',
    components: {
      // DraftInfo,
      jhcgEdit,
      filterSearch,
      filterTable,
      ProcessList,
      WfqdDeal,
    },

    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          sjxmmc: '',
          cjr: '',
          cjsj: '',
          sj: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '计划编号' },
          { name: '计划名称' },
          { name: '计划年度' },
          { name: '计划类型' },
          { name: '更新时间' },
          { name: '编制人' },
          { name: '创建时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-jhlx-fgldhz-search',
        tableKey: 'oilAudit-jhlx-fgldhz-list',
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
      async handleCopy(row) {
        const res = await copyUniquecg({
          jhcgid: row.jhid,
        })
        console.log(res)
        if (res.code == 1) {
          this.$baseMessage(res.msg, 'success')
          await this.fetchData()
        }
      },
      handleApproval(row) {
        try {
          this.btnLoading = true
          this.$refs['process'].save(121, row.jhid)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.jhid,
          tableId: 121,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      getFiled() {
        return [
          { name: '计划名称', key: 'sjxmmc' },
          { name: '更新时间', key: 'gxsj' },
          { name: '编制人', key: 'cjr' },
          { name: '创建时间', key: 'cjsj' },
        ]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          sjxmmc: '',
          cjr: '',
          cjsj: '',
          sj: '',
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
          data: { tlist, totalRecord },
        } = await jhgljhList(this.queryForm)
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null)
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, true)
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, false)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await jhgljhDelete({
            jhid: row.jhid,
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
