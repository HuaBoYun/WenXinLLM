<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <flow-category-list @node-change="handleNodeChange" />
      </div>
      <div class="right">
        <!-- <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button
            native-type="submit"
            type="success"
            @click="$refs['choose'].show()"
          >
            选择流程
          </el-button>
        </vab-query-form-right-panel>
      </vab-query-form> -->
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
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
            <el-table-column
              align="center"
              label="流程编号"
              prop="flowid"
              show-overflow-tooltip
              sortable="custom"
            />
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="流程名称"
                prop="flowname"
                show-overflow-tooltip
                v-if="item.name === '流程名称'"
              />
              <el-table-column
                align="center"
                label="机构"
                prop="comName"
                v-if="item.name === '机构'"
              />
              <el-table-column
                align="center"
                label="主责部门"
                prop="deparChargeName"
                v-if="item.name === '主责部门'"
              />
              <el-table-column
                align="center"
                label="创建时间"
                prop="createtime"
                v-if="item.name === '创建时间'"
              />
              <el-table-column
                align="center"
                label="关联流程ID"
                prop="settingid"
                v-if="item.name === '关联流程ID'"
              />
            </div>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="100"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleSetting(row)"
                  v-if="hasAuth('YWLCSZsetting')"
                >
                  设置
                </el-button>
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
      </div>
      <ChooseFlow ref="choose" @selected="handleFlowSelected" />
    </div>
  </div>
</template>

<script>
  import { getBusinessList, saveAssociateBusiness } from '@/api/setting/system'
  import FlowCategoryList from '@/views/setting/system/components/FlowCategoryList'
  import ChooseFlow from './components/ChooseFlow.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: 'Ywlcsz',
    components: { FlowCategoryList, ChooseFlow, filterSearch, filterTable },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          faflowid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        currentRow: undefined,
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-system-ywlcsz-search',
        tableKey: 'setting-system-ywlcsz-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '流程名称' },
          { name: '机构' },
          { name: '主责部门' },
          { name: '创建时间' },
          { name: '关联流程ID' },
        ], //所有表格项
        filedNow: [],
        sortFields: '',
        sortFlag: 'asc',
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
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
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
        } = await getBusinessList({...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag})
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleNodeChange(data) {
        this.queryForm.faflowid = data.value
        this.fetchData()
      },
      handleRowChange(row) {
        this.currentRow = row
      },
      async handleFlowSelected(data) {
        const { msg, code } = await saveAssociateBusiness({
          settingid: data.settingId,
          flowid: this.currentRow.flowid,
        })
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        }
      },
      handleSetting(row) {
        this.currentRow = row
        this.$refs['choose'].show()
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '流程编号', key: 'flownumber' },
          { name: '流程名称', key: 'flowname' },
          { name: '责任部门', key: 'departincharge' },
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
                this.queryForm[x.key] = ''
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
      showMore() {
        this.searchMore = !this.searchMore
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
    },
  }
</script>
<style scoped>
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
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .lr-layout {
    background: #f6f8f9;
    display: flex;
  }

  .lr-layout > .left {
    width: 250px;
    overflow: hidden;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 20px 20px 20px;
    background: #ffffff;
  }
  .right {
    flex: 1;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }
</style>
