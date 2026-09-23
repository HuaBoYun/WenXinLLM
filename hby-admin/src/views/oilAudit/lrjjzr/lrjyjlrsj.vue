<template>
  <!-- 二级单位及成员离任审计 -->
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
                v-model="queryForm.projectName"
                clearable
                placeholder="审计项目名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '审计项目名称'"
              ></el-input>
              <el-input
                v-model="queryForm.auditOrg"
                clearable
                placeholder="被审计单位"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '被审计单位'"
              ></el-input>
              <el-date-picker
                v-model="queryForm.queryYear"
                type="year"
                placeholder="年份"
                value-format="yyyy"
                format="yyyy"
                v-if="item.name === '年份'"
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
        <el-button type="success" @click="handleAdd">新建</el-button>
        <el-upload
          class="upload-demo"
          :show-file-list="false"
          :action="baseApi + api"
          :data="{ isCover: isCover }"
          :headers="headers"
          :before-upload="beforeUpload"
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
        <el-table-column
          align="center"
          label="序号"
          prop="leaveNo"
        ></el-table-column>
        <el-table-column align="center" label="审计项目名称" prop="projectName">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleDetail(row)"
              style="white-space: pre-line; line-height: 16px"
            >
              {{ row.projectName }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="被审计单位"
            v-if="item.name === '被审计单位'"
          >
            <template #default="{ row }">
              <span>{{ row.auditOrg.orgname }}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="审计任职期间"
            v-if="item.name === '审计任职期间'"
          >
            <template #default="{ row }">
              <span>{{ row.auditStartTime }}~{{ row.auditEndTime }}</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="委托书编号"
            prop="entrustNo"
            v-if="item.name === '委托书编号'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="委托时间"
            prop="entrustTime"
            v-if="item.name === '委托时间'"
          ></el-table-column>
          <!-- <el-table-column
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
          </el-table-column> -->
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
          <el-button
            type="text"
            @click.native="handleDelete(row)"
            :disabled="!!row.status"
          >
            删除
          </el-button>
          <!-- <el-dropdown style="margin-left: 10px">
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
                :disabled="!!row.status"
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
          </el-dropdown> -->
        </el-table-column>
      </el-table>
    </el-card>
    <LRJYJLRSJview ref="edit" @fetchData="fetchData"></LRJYJLRSJview>
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
    <lxjybSelectModal @projectManage="getChildlistPro" ref="person" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import lxjybSelectModal from '@/components/selectPerson'
  import {
    ejdwlrsjList,
    ejdwlrsjDelete,
    lrjyjlrsjExportList,
    lrjyjlrsjXf,
  } from '@/oapi/audit/plan'
  import LRJYJLRSJview from './components/lrjyjlrsjView.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  import { getFlowPkInfo } from '@/api/contract/manage'
  import { xiafaListNew } from '@/oapi/audit/preparation'
  export default {
    components: {
      lxjybSelectModal,
      LRJYJLRSJview,
      filterTable,
      filterSearch,
      ProcessList: () =>
        import('@/views/contract/contractManage/components/ProcessList'),
      WfqdDeal: () => import('@/views/msg/components/options/WfqdDeal'),
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/plan/leave/audit2L/import',
        headers: { token },
        select: [],
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          queryYear: '',
        },
        isCover: 0,
        filedAll: [
          { name: '被审计单位' },
          { name: '审计任职期间' },
          { name: '委托书编号' },
          { name: '委托时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'oilAudit-lrjjzr-lrjyjlrsj-search',
        tableKey: 'oilAudit-lrjjzr-lrjyjlrsj-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
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
        if (value == 0) this.fetchData()
      })
    },
    methods: {
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(113, row.id)
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 113,
        })
        this.listLoading = false
        this.$refs.wfqddeal.show(res.data, false)
      },
      beforeUpload() {
        return new Promise((resolve, reject) => {
          this.$baseConfirm(
            '是否根据序号覆盖已存在的数据',
            null,
            () => {
              console.log('s是')
              this.isCover = 1
              resolve()
            },
            () => {
              console.log('s否')
              this.isCover = 0
              resolve()
            },
            '是',
            '否'
          )
        })
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
        const data = await lrjyjlrsjExportList({
          ...this.queryForm,
          ids: ids.join(),
        })
        let fileName = '二级单位及成员离任审计表'
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
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '审计项目名称', key: 'name' },
          { name: '被审计单位', key: 'code' },
          { name: '年份', key: 'code' },
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
          data: { tlist, totalRecord },
          code,
        } = await ejdwlrsjList(this.queryForm)
        if (code == 1) {
          if (tlist.length > 0) {
            this.list = tlist.map((item) => {
              // 每个赋值，为了避免某字段不存在造成阻塞导致新增修改的无法点击
              return {
                ...item,
                id: item.id,
                projectName: item.projectName || '',
                auditOrg: item.auditOrg || { orgname: '' },
                oldLevel: item.oldLevel || '',
                entrustNo: item.entrustNo || '',
                entrustTime: item.entrustTime || '',
                auditStartTime: item.auditStartTime || '',
                auditEndTime: item.auditEndTime || '',
                auditOrgId: item.auditOrgId || '',
                status: item.status || '',
              }
            })
            this.total = totalRecord || 0
          } else {
            this.list = []
            this.total = 0
          }
          this.listLoading = false
          this.setCheckedRows()
        }
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, false)
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, true)
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await ejdwlrsjDelete({ ids: row.id })
          if (res.code === 1) {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          queryYear: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleView(row) {
        this.$refs['check'].showEdit(row)
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
        //下发保存
        lrjyjlrsjXf({
          ids: ids.toString(),
          personIds: names.toString(),
        })
        //下发通知
        xiafaListNew({
          tableId: '1396',
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
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
  .upload-demo {
    display: inline-block;
    margin: 0 10px;
  }
</style>
