<template>
  <!-- 计划管理-计划需求 -->
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
                v-model="queryForm.jhxqmc"
                clearable
                v-if="item.name === '计划需求名称'"
                placeholder="计划需求名称"
              />
              <el-select
                :style="{ width: '100%' }"
                v-model="queryForm.sjxmlx"
                placeholder="项目类型"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '项目类型'"
              >
                <el-option label="工程项目审计" value="工程项目审计" />
                <el-option label="经济责任审计" value="经济责任审计" />
                <el-option label="管理及专项审计" value="管理及专项审计" />
              </el-select>
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
        <el-dropdown style="margin-right: 10px">
          <el-button type="success">
            新建
            <i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="handleAdd(1)">
              工程项目审计
            </el-dropdown-item>
            <el-dropdown-item @click.native="handleAdd(2)">
              经济责任审计
            </el-dropdown-item>
            <el-dropdown-item @click.native="handleAdd(3)">
              管理及专项审计
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-button type="success" @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @selection-change="handleSelectionChange"
      >
        <el-table-column align="center" type="selection" width="55" />
        <!-- <el-table-column
          align="center"
          label="序号"
          prop="sheetCode"
          width="100"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.sheetCode }}
              基本页面添加:
              计划需求名称、项目联系人、建议实施时间、组织方式、填报时间、项目联系人、联系电话、状态、当前处理人
            </el-button>
          </template>
        </el-table-column> -->
        <el-table-column align="center" label="计划需求名称" prop="jhxqmc">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row, 'detail')"
              style="white-space: pre-line; line-height: 16px"
            >
              {{ row.jhxqmc }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '项目类型'"
            align="center"
            label="项目类型"
            prop="sjxmlx"
            show-overflow-tooltip
          >
            <!-- <template #default="{ row }">
              {{
                row.projectType == 1
                  ? '工程项目审计'
                  : row.projectType == 2
                  ? '经济责任审计'
                  : '管理及专项审计'
              }}
            </template> -->
          </el-table-column>
          <el-table-column
            v-if="item.name === '建议实施时间'"
            align="center"
            label="建议实施时间"
            prop="jysjsssj"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <el-table-column
            v-if="item.name === '组织方式'"
            align="center"
            label="组织方式"
            prop="zzfs"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '填报时间'"
            align="center"
            label="填报时间"
            prop="tbsj"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <el-table-column
            v-if="item.name === '项目联系人'"
            align="center"
            label="项目联系人"
            prop="xmlxr"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '联系电话'"
            align="center"
            label="联系电话"
            prop="lxdh"
            show-overflow-tooltip
          />
          <!-- <el-table-column
            v-if="item.name === '状态'"
            align="center"
            label="状态"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span>{{ statusTextMap[+scope.row.spzt] }}</span>
            </template>
          </el-table-column> -->
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
          <el-table-column
            v-if="item.name === '当前处理人'"
            align="center"
            label="当前处理人"
            prop="cjr"
            show-overflow-tooltip
          />
        </div>
        <el-table-column
          label="操作"
          #default="{ row }"
          fixed="right"
          align="center"
        >
          <el-button type="text" @click="handleSend(row)">送审</el-button>
          <el-button
            type="text"
            @click="handleEdit(row, 'edit')"
            :disabled="!!row.spzt"
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
                  :disabled="!row.spzt"
                >
                  办理
                </el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  type="text"
                  @click="handleApproval(row)"
                  :disabled="!!row.spzt || btnLoading"
                >
                  提交审批
                </el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  :disabled="!!row.spzt"
                >
                  删除
                </el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
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
    <jhxqEdit1 ref="edit1" @fetchData="fetchData" />
    <jhxqEdit2 ref="edit2" @fetchData="fetchData" />
    <jhxqEdit3 ref="edit3" @fetchData="fetchData" />

    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqdDeal" />
  </div>
</template>

<script>
  // import { getDgListAll, dgDetail } from '@/api/monitor/question'
  import {
    jhxqList,
    jhxqDelete,
    jhxqExport,
    jhxSubmit,
  } from '@/oapi/audit/plan'
  import { downloadFile } from '@/utils/otherUtils'
  import jhxqEdit1 from './components/jhxqEdit1'
  import jhxqEdit2 from './components/jhxqEdit2'
  import jhxqEdit3 from './components/jhxqEdit3'
  import { formatDate } from '@/utils/index'

  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import { exportAudit } from '@/api/audit/analyse'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  export default {
    name: 'jhxq',
    components: {
      jhxqEdit1,
      jhxqEdit2,
      jhxqEdit3,
      filterSearch,
      filterTable,
      ProcessList,
      WfqdDeal,
    },

    mixins: [searchTableMixis],
    data() {
      return {
        select: [],
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          sjxmlx: '',
          jhxqmc: '',
          pageNumber: 1,
          pageSize: 20,
        },
        statusTextMap: ['', '未审批', '审批中', '审批驳回', '审批通过'], // 状态文字处理
        filedAll: [
          { name: '项目类型' },
          { name: '建议实施时间' },
          { name: '组织方式' },
          { name: '填报时间' },
          { name: '项目联系人' },
          { name: '状态' },
          { name: '当前处理人' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-jhlx-fwxqb-search',
        tableKey: 'oilAudit-jhlx-fwxqb-list',
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
    methods: {
      handleApproval(row) {
        try {
          this.btnLoading = true
          this.$refs['process'].save(122, row.jhxqid)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        console.log(row)
        // this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.jhxqid,
          tableId: 122,
        })
        // this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      getFiled() {
        return [
          { name: '计划需求名称', key: 'jhxqmc' },
          { name: '项目类型', key: 'sjxmlx' },
        ]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          sjxmlx: '',
          jhxqmc: '',
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
        // 列表数据
        this.btnLoading = false
        this.listLoading = true
        const {
          data: { list, total },
        } = await jhxqList(this.queryForm)
        this.list = list || []
        this.total = total || 0
        this.listLoading = false
      },
      handleAdd(type) {
        // 弹起窗口 新增
        this.$refs['edit' + type].showEdit('add', null)
      },
      async handleSend(row) {
        // 送审
        const { msg, code } = await jhxSubmit({ jhxqid: row.jhxqid })
        if (code == 0) {
          this.$baseMessage(msg, 'success')
        } else {
          this.$baseMessage(msg, 'error')
        }
        await this.fetchData()
      },
      async handleEdit(row, type) {
        // 弹起窗口 新增
        if (row.projectType == 1) {
          this.$refs['edit1'].showEdit(type, row)
        } else if (row.projectType == 2) {
          this.$refs['edit2'].showEdit(type, row)
        } else {
          this.$refs['edit3'].showEdit(type, row)
        }
      },
      handleDelete(row) {
        // 删除 项
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await jhxqDelete({ ids: row.jhxqid })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      handleSelectionChange(val) {
        this.select = val
      },
      async handleExport() {
        // 导出
        const ids = this.select.map((res) => res.jhxqid)
        const data = await jhxqExport({ ...this.queryForm, ids: ids.join() })
        let fileName = '计划需求'
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
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
