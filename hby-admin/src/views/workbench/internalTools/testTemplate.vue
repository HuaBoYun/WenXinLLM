<template>
  <div class="system-log-container">
    <el-card shadow="never">
      <vab-query-form class="margin-b0">
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
              <el-input
                v-model="queryForm.templeNumber"
                clearable
                placeholder="模板编号"
                v-if="item.name === '模板编号'"
              />
              <el-input
                v-model="queryForm.templename"
                clearable
                placeholder="模板名称"
                v-if="item.name === '模板名称'"
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
              <el-button native-type="submit" type="primary" @click="reset()">
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
      </vab-query-form>
    </el-card>
    <el-card shadow="never" class="secondCard">
      <vab-query-form-right-panel class="option-row">
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
        <el-button
          type="success"
          @click="handleAdd"
          v-if="!hasAuth('NKCSMBadd')"
        >
          新建
        </el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
        <el-table-column
          align="center"
          label="模板编号	"
          prop="templeNumber"
          #default="{ row }"
          sortable="custom"
        >
          <template>
            <el-button
              type="text"
              @click="handleDetail(row)"
              v-if="!hasAuth('NKCSMBdetail')"
            >
              {{ row.templeNumber }}
            </el-button>
            <div v-else>{{ row.templeNumber }}</div>
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="模板名称"
            prop="templename"
            v-if="item.name === '模板名称'"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="模板说明"
            prop="templeDesc"
            v-if="item.name === '模板说明'"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="创建人"
            prop="staff.realname"
            v-if="item.name === '创建人'"
          />
          <el-table-column
            align="center"
            label="创建时间"
            prop="createtime"
            v-if="item.name === '创建时间'"
            sortable="custom"
          />
          <el-table-column
            align="center"
            label="来源"
            prop="source"
            v-if="item.name === '来源'"
          />
        </div>

        <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="
                row.source != '自建' || createId != row.staffId || row.count > 0
              "
              v-if="!hasAuth('NKCSMBedit')"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleIssued(row)"
              :disabled="row.issued === '是'"
              v-if="!hasAuth('NKCSMBissued')"
            >
              下发
            </el-button>
            <el-button type="text" @click="handleCopy(row)">复制</el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              :disabled="
                row.source != '自建' || createId != row.staffId || row.count > 0
              "
              v-if="!hasAuth('NKCSMBdelete')"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      class="pager"
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <TemplateEditStep1 ref="step1" @fetch-data="fetchData" />
    <!-- <CompanyTreeModel ref="companyTreeModel" @selected="handleSelect" /> -->
    <ChooseMechanismTest ref="companyTreeModel" @selected="handleSelect" />
  </div>
</template>

<script>
  import {
    defTmplList,
    tempdelete,
    saveissued,
    copyIssued,
  } from '@/api/internal/testTemplate'
  import { doDelete } from '@/api/table'
  import TemplateEditStep1 from './components/TemplateEditStep1'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import CompanyTreeModel from '@/components/CompanyTreeModel'
  import ChooseMechanismTest from './components/ChooseMechanismTest'
  import { searchTableMixis } from '@/mixis/index'
  import { formatDay } from '@/utils/index'
  import { hasAuth } from '@/utils'

  export default {
    name: 'StandardList',
    components: {
      TemplateEditStep1,
      CompanyTreeModel,
      ChooseMechanismTest,
      filterSearch,
      filterTable,
    },
    mixins: [searchTableMixis],
    data() {
      return {
        idList: [],
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          templeNumber: '',
          templename: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '模板名称' },
          { name: '模板说明' },
          { name: '创建人' },
          { name: '创建时间' },
          { name: '来源' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'workbench-intemalTools-testTemplate-search',
        tableKey: 'workbench-intemalTools-testTemplate-list',
        searchMore: true,
        select: {},
        sortFields: '',
        sortFlag: 'asc',
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
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
      async sortChange(column) {
        let { order, prop } = column
        let p = prop
        this.sortFields = p || ''
        if (order === 'ascending') {
          this.sortFlag = 'asc'
        } else if (order === 'descending') {
          this.sortFlag = 'desc'
        } else {
          this.sortFlag = ''
        }
        await this.fetchData()
      },
      getFiled() {
        return [
          { name: '模板编号', key: 'elementNumber' },
          { name: '模板名称', key: 'elementname' },
        ]
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
      formatDay(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
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
      async fetchData(type) {
        if (type && type === 'reset') {
          // TODO 清空
        }
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await defTmplList({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      reset() {
        this.queryForm = {}
        this.fetchData()
      },
      next() {
        this.$refs['step2'].show()
      },
      previous() {
        this.$refs['step1'].show()
      },
      handleAdd() {
        this.$refs['step1'].show()
      },
      handleEdit(row) {
        // if (this.createId != row.staffId) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$refs['step1'].show(row, 'edit')
      },
      handleDetail(row) {
        this.$refs['step1'].show(row, 'view')
      },
      handleIssued(row) {
        this.select = row
        this.$refs['companyTreeModel'].show(true)
      },
      async handleSelect(row) {
        //..todo
        //树的ID没选完
        // let ids = row.map((item) => item.id)
        this.getIdList(row)
        //

        const { msg, code } = await saveissued({
          tempIds: this.select.testtemid,
          orgIds: this.idList.toString(),
        })
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.idList = []
          this.fetchData()
        }
      },
      getIdList(arr) {
        //递归树，获取ID
        for (let index = 0; index < arr.length; index += 1) {
          this.idList.push(arr[index].id)
          if (
            arr[index].children instanceof Array &&
            arr[index].children.length > 0
          ) {
            this.getIdList(arr[index].children)
          }
        }
      },
      handleDelete(row) {
        // if (this.createId != row.staffId) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await tempdelete({ templId: row.testtemid })
          if (code == 1) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
      //复制
      handleCopy(row) {
        copyIssued({
          tempId: row.testtemid,
        }).then((res) => {
          if (res.code == 1) {
            this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
            this.fetchData()
          }
        })
      },
    },
  }
</script>

<style scoped>
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }

  .secondCard {
    margin-top: -5px !important;
  }

  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }

  .pager {
    margin-bottom: 20px !important;
  }
</style>
