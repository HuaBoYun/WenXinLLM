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
                v-model="queryForm.businesstype"
                clearable
                placeholder="业务类别"
                v-if="item.name === '业务类别'"
              />
              <el-input
                v-model="queryForm.elementnumber"
                clearable
                placeholder="要素编号"
                v-if="item.name === '要素编号'"
              />
              <el-input
                v-model="queryForm.elementname"
                clearable
                placeholder="要素名称"
                v-if="item.name === '要素名称'"
              />
              <el-input
                v-model="queryForm.auditpoint"
                clearable
                placeholder="审查要点"
                v-if="item.name === '审查要点'"
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
          @click="handleEdit()"
          v-if="!hasAuth('NKYSWHadd')"
        >
          新建
        </el-button>
        <el-button
          type="success"
          @click="handleExport()"
          v-if="!hasAuth('NKYSWHexport')"
          disabled
        >
          导出
        </el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
        <el-table-column
          align="center"
          label="要素编号"
          prop="elementnumber"
          width="120"
          sortable="custom"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.elementnumber }}
            </el-button>
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="要素名称"
            prop="elementname"
            v-if="item.name === '要素名称'"
          />
          <el-table-column
            align="center"
            label="业务类别"
            prop="businesstype"
            v-if="item.name === '业务类别'"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="评分规则"
            v-if="item.name === '评分规则'"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ row.assessrules ? row.assessrules.toFixed(1) : '' }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="审查要点"
            prop="auditpoint"
            v-if="item.name === '审查要点'"
            show-overflow-tooltip
          />
        </div>

        <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              v-if="!hasAuth('NKYSWHedit')"
              :disabled="createId != row.createstaffid"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              v-if="!hasAuth('NKYSWHdelete')"
              :disabled="createId != row.createstaffid"
            >
              删除
            </el-button>
            <!-- <el-button type="text" @click="handleExport(row)">导出</el-button> -->
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
    <FactorEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    defQuexianList,
    yswhexport,
    defBasicDel,
    defBasicCheck,
  } from '@/api/internal/factorMaintenance'
  import { doDelete } from '@/api/table'
  import FactorEdit from './components/FactorEdit'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { searchTableMixis } from '@/mixis/index'
  import { downloadFile } from '@/utils/otherUtils'
  import { hasAuth } from '@/utils'

  export default {
    name: 'StandardList',
    components: { FactorEdit, filterSearch, filterTable },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          asseleid: 0,
          assessrules: 0,
          auditpoint: '',
          businessattribute: '',
          businesstype: '',
          elementNumber: '',
          elementname: '',
          memo: '',
          standardscore: 0,
          status: '',
          tblComany: '',
          tblcomany: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '要素名称' },
          { name: '业务类别' },
          { name: '评分规则' },
          { name: '审查要点' },
          // { name: '建立时间' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'workbench-intemalTools-factorMaintenance-search',
        tableKey: 'workbench-intemalTools-factorMaintenance-list',
        searchMore: true,
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
          { name: '业务类别', key: 'businesstype' },
          { name: '要素编号', key: 'elementNumber' },
          { name: '要素名称', key: 'elementname' },
          { name: '审查要点', key: 'auditpoint' },
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
        const { pageSize, ...other } = this.queryForm
        // const info = {
        //   other,
        //   //区域显隐控制,暂时写死为空，不知道这个传什么
        //   choiceSearch: '',
        // }
        let info = other
        info.choiceSearch = ''
        info.pageSize = pageSize

        const {
          data: {
            pageBean: { records, total },
          },
        } = await defQuexianList({
          ...info,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        })
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleEdit(row) {
        if (row) {
          // if (this.createId != row.createstaffid) {
          //   return this.$message.error('只有创建人可以操作')
          // }
          defBasicCheck({ id: row.asseleid }).then((res) => {
            if (res.code == 1) {
              this.$refs['edit'].showEdit(row)
            }
          })
        } else {
          this.$refs['edit'].showEdit(row)
        }
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, true)
      },
      async handleExport() {
        //
        this.listLoading = true
        const res = await yswhexport()
        downloadFile(res, '要素维护.xlsx')
        this.listLoading = false
        //
        // if (!res) return
        // let filename = '要素维护'
        // // let filename = 'aaa'
        // let blob = new Blob([res]) //res即为blob数据，请注意自己的数据形式
        // let url = window.URL.createObjectURL(blob, {
        //   type: 'application/vnd.ms-excel',
        // })
        // const link = document.createElement('a')
        // link.style.display = 'none'
        // link.href = url
        // link.setAttribute('download', filename)
        // document.documentElement.appendChild(link)
        // link.click()
        // document.documentElement.removeChild(link)
        // if (row) {
        //
        // } else {
        //
        //   // const res = yswhexport({ eleid: row.asseleid })
        // }
      },
      handleDelete(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { data, code, msg } = await defBasicDel({
            deleteId: row.asseleid,
          })

          if (code === 1) {
            this.$baseMessage(data, 'success', 'vab-hey-message-success')
            this.fetchData()
          }
        })
      },
      resetSearch() {
        this.queryForm = {}
        this.fetchData()
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
