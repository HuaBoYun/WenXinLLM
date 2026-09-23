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
                v-model="queryForm.projectName"
                clearable
                v-if="item.name === '审计项目名称'"
                placeholder="审计项目名称"
              />
              <el-select
                v-model="queryForm.projectType"
                placeholder="项目类型"
                :style="{ width: '100%' }"
                clearable
                v-if="item.name === '项目类型'"
              >
                <el-option label="工程类" value="工程类" />
                <el-option label="财务类" value="财务类" />
              </el-select>
              <el-date-picker
                v-model="queryForm.createYear"
                type="year"
                value-format="yyyy"
                placeholder="请选择创建年度"
                v-if="item.name === '创建年度'"
              ></el-date-picker>
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
        <el-button type="success" @click="handleEdit(null)">新增</el-button>
        <el-upload
          class="upload-demo"
          :show-file-list="false"
          :action="baseApi + api"
          :headers="headers"
          :on-success="handleSuccess"
        >
          <el-button type="success">导入</el-button>
        </el-upload>
        <el-button type="success" @click="handleExport()">导出</el-button>
        <el-button type="success" @click="hadnlePush">下发</el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <el-table-column
          width="48"
          type="selection"
          :reserve-selection="true"
        ></el-table-column>
        <el-table-column align="center" label="序号" prop="id" width="100">
          <template #default="{ row, $index }">
            <el-button type="text" @click="handleDetail(row)">
              {{ $index + 1 }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column width="1" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '审计项目名称'"
            align="center"
            label="审计项目名称"
            prop="projectName"
          />
          <el-table-column
            v-if="item.name === '立项理由及审计目的'"
            align="center"
            label="立项理由及审计目的"
            prop="projectPurpose"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '重点关注内容'"
            align="center"
            label="重点关注内容"
            prop="concernsContent"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '填报单位'"
            align="center"
            label="填报单位"
            prop="tborgname"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '单位范围'"
            align="center"
            label="单位范围"
            prop="unitRange"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '时间范围'"
            align="center"
            label="时间范围"
            prop="timeRange"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '工程/财务'"
            align="center"
            label="工程/财务"
            prop="projectType"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '创建人'"
            align="center"
            label="创建人"
            prop="createUser.realname"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '创建时间'"
            align="center"
            label="创建时间"
            prop="createTime"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '备注'"
            align="center"
            label="备注"
            prop="remark"
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
        </div>

        <el-table-column
          label="操作"
          #default="{ row }"
          fixed="right"
          align="center"
        >
          <el-button
            type="text"
            @click="handleEdit(row)"
            :disabled="!!row.status"
          >
            修改
          </el-button>
          <el-dropdown style="margin-left: 10px">
            <el-button type="text">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                @click.native="handleManage(row)"
                :disabled="!row.status"
              >
                办理
              </el-dropdown-item>
              <el-dropdown-item
                @click.native="handleApproval(row)"
                :disabled="!!row.status || btnLoading"
              >
                提交审批
              </el-dropdown-item>
              <el-dropdown-item
                @click.native="handleDelete(row)"
                :disabled="!!row.status"
              >
                删除
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
    <lxjybEdit ref="edit" @fetchData="fetchData" />
    <!-- 人员 -->
    <lxjybSelectModal @projectManage="getChildlistPro" ref="person" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import lxjybSelectModal from '@/components/selectPerson'
  import lxjybEdit from './components/lxjybEdit'
  import { formatDate } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import {
    getList,
    deleteItem,
    exportList,
    lxjybXf,
  } from '@/api/oilAudit/jhgl/lxjyb'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  import { getFlowPkInfo } from '@/api/contract/manage'
  import { xiafaListNew } from '@/oapi/audit/preparation'

  export default {
    name: 'lxjyb',
    components: {
      lxjybEdit,
      filterSearch,
      filterTable,
      lxjybSelectModal,
      ProcessList: () =>
        import('@/views/contract/contractManage/components/ProcessList'),
      WfqdDeal: () => import('@/views/msg/components/options/WfqdDeal'),
    },
    mixins: [searchTableMixis],
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/plan/project/suggestion/import',
        headers: { token },
        select: [],
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          id: '',
          projectName: '',
          pageNumber: 1,
          pageSize: 20,
          projectType: '',
          createYear: '',
        },
        filedAll: [
          { name: '审计项目名称' },
          { name: '立项理由及审计目的' },
          { name: '重点关注内容' },
          { name: '填报单位' },
          { name: '单位范围' },
          { name: '时间范围' },
          { name: '工程/财务' },
          { name: '创建人' },
          { name: '创建时间' },
          { name: '备注' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-jhlx-lxjyb-search',
        tableKey: 'oilAudit-jhlx-lxjyb-list',
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
          //提交审批
          this.$refs['process'].save(102, row.id)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 102,
        })
        this.listLoading = false
        this.$refs.wfqddeal.show(res.data, false)
      },
      handleSuccess(response) {
        if (response.data == '200') {
          this.fetchData()
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },
      hadnlePush() {
        if (this.select && this.select.length > 0) {
          this.$refs.person.showEdit()
        } else {
          this.$baseMessage(
            '请选择需要下发的数据',
            'error',
            'vab-hey-message-error'
          )
        }
      },
      // 3、勾选列表操作
      handleSelectionChange(selection) {
        console.log(selection)
        this.select = selection
      },
      async handleExport() {
        const ids = this.select.map((res) => res.id)
        const data = await exportList({ ...this.queryForm, ids: ids.join() })
        let fileName = '立项建议表'
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
      getFiled() {
        return [
          { name: '审计项目名称', key: 'projectName' },
          { name: '项目类型', key: 'projectType' },
          { name: '创建年度', key: 'createYear' },
        ]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          id: '',
          projectName: '',
          pageNumber: 1,
          pageSize: 20,
          projectType: '',
          createYear: '',
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
          data: { tlist: list, totalRecord: total },
        } = await getList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
        this.setCheckedRows()
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit(row, true)
      },
      async handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteItem({ ids: row.id })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error')
          }
        })
      },
      async getChildlistPro(val) {
        const ids = this.select.map((res) => res.id)
        const titles = this.select.map((res) => res.projectName)
        const names = val.map((res) => res.staffid)

        const arr = []
        for (let i = 0; i < this.select.length; i++) {
          for (let k = 0; k < names.length; k++) {
            arr.push({
              formId: ids[i],
              distributionTitle: titles[i],
              isread: 0,
              reciver: names[k],
              moduleType: 'yqns',
            })
          }
        }
        //下发信息保存
        lxjybXf({
          ids: ids.toString(),
          personIds: names.toString(),
        })
        //下发通知
        xiafaListNew({
          tableId: '1511',
          jsondistribution: JSON.stringify([...arr]),
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage(res.msg, 'success')
            this.fetchData()
            this.select = []
          }
        })
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.id == row.id)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select.some((x) => x.id == row.id)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.id == row.id)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.id == item.id
              }),
              true
            )
          })
        })
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
  .upload-demo {
    display: inline-block;
    margin: 0 10px;
  }
</style>
