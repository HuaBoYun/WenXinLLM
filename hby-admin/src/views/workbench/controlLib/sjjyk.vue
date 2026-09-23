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
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.code"
                clearable
                placeholder="编号"
                v-if="item.name === '编号'"
              />
              <el-input
                v-model="queryForm.tatle"
                clearable
                placeholder="标题"
                v-if="item.name === '标题'"
              />
              <el-input
                v-model="queryForm.experiencetype"
                clearable
                placeholder="经验类型"
                v-if="item.name === '经验类型'"
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
            <el-form-item style="cursor: pointer">
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

    <el-card shadow="never">
      <vab-query-form>
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
          <el-button type="success" @click="handleEdit(false, '新建', null)">
            新建
          </el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="标题"
          width="170"
          prop="tatle"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <el-button @click="handleEdit(true, '详情', scope.row)" type="text">
              {{ scope.row.tatle }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="编码"
            v-if="item.name === '编码'"
            prop="code"
          />
          <el-table-column
            align="center"
            label="经验类型"
            v-if="item.name === '经验类型'"
            prop="experiencetype"
          />
          <el-table-column
            align="center"
            label="开始时间"
            v-if="item.name === '开始时间'"
            prop="createdtime"
            :formatter="formatDate"
          />

          <el-table-column
            align="center"
            label="人员信息"
            v-if="item.name === '人员信息'"
            prop="createStaff"
          >
            <template slot-scope="scope">
              {{ scope.row.createStaff.realname }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="概述"
            v-if="item.name === '概述'"
            prop="overview"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="状态"
            v-if="item.name === '状态'"
            prop="state"
          >
            <template #default="{ row }">
              {{
                row.state == 1
                  ? '审批中'
                  : row.state == 2
                  ? '已退回'
                  : row.state == 3
                  ? '已撤回'
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
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="200"
        >
          <template slot-scope="scope">
            <!-- <el-button
              type="text"
              @click="handleDeal(scope.row)"
              :disabled="!scope.row.state"
            >
              办理
            </el-button>
            <el-button
              type="text"
              @click="handleShenPi(scope.row)"
              :disabled="!!scope.row.state || btnLoading"
            >
              提交审批
            </el-button> -->
            <el-button
              type="text"
              @click="handleEdit(false, '修改', scope.row)"
              :disabled="!!scope.row.state"
            >
              修改
            </el-button>

            <el-button
              type="text"
              @click="handleDelete(scope.row)"
              :disabled="!!scope.row.state"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <Edit ref="edit" @fetch-data="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />

    <WfqdDeal ref="wfqddeal" />
  </div>
</template>
<script>
  import { getSJJYKList, sjjykDelete } from '@/api/workbench/auditTools'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import { parseTime } from '@/utils/index'
  import Edit from './components/sjjykEdit.vue'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  export default {
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable, Edit, ProcessList, WfqdDeal },
    name: 'xxx',
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          code: '',
          experiencetype: '',
          tatle: '',
        },
        list: [],
        listLoading: true,
        // 筛选列表配置
        filedAll: [
          { name: '编码' },
          { name: '经验类型' },
          { name: '开始时间' },
          { name: '人员信息' },
          { name: '概述' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'workbench-controlLib-sjjyk-search',
        tableKey: 'workbench-controlLib-sjjyk-list',
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
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      getFiled() {
        return [
          { name: '编号', key: 'code' },
          { name: '标题', key: 'tatle' },
          { name: '经验类型', key: 'experiencetype' },
        ]
      },
      handleDetail(row) {},

      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      async fetchData() {
        this.btnLoading = false
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getSJJYKList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleEdit(disabled, type, row) {
        this.$refs['edit'].showEdit(disabled, type, row)
      },

      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await sjjykDelete({ jykid: row.jykid })
          if (code === 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
      },
      handleShenPi(row) {
        try {
          this.$baseConfirm('你确定要审核当前项吗', null, async () => {
            this.btnLoading = true
            const tableId = 28
            const fromId = row.jykid
            this.$refs['process'].save(tableId, fromId)
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleDeal(row) {
        const res = await getFlowPkInfo({
          formId: row.jykid,
          tableId: 28,
        })

        this.$refs.wfqddeal.show(res.data, false)
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
