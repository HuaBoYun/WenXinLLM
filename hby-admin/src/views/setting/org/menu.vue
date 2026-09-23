<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <company-tree ref="leftlist" @select="handleSelectTree" :isAll="true" />
      </div>
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-left-panel :span="24">
              <el-form
                ref="form"
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item>
                  <el-input
                    v-model="queryForm.orgnumber"
                    clearable
                    placeholder="公司编号"
                  />
                </el-form-item>
                <el-form-item>
                  <el-input
                    v-model="queryForm.orgname"
                    clearable
                    placeholder="公司名称"
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
              </el-form>
            </vab-query-form-left-panel>
          </el-card>
        </vab-query-form>
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
          <el-table
            v-loading="listLoading"
            :data="list"
            @sort-change="sortChange"
          >
            <el-table-column
              align="center"
              label="公司编号"
              prop="orgnumber"
              sortable="custom"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row, true)"
                  v-if="hasAuth('JGCDSDdetail')"
                >
                  {{ row.orgnumber }}
                </el-button>
                <div v-else>{{ row.orgnumber }}</div>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="公司名称"
                prop="orgname"
                v-if="item.name === '公司名称'"
              />
              <el-table-column
                align="center"
                label="公司简介"
                prop="orgmeno"
                show-overflow-tooltip
                v-if="item.name === '公司简介'"
              />
              <el-table-column
                align="center"
                label="公司备注"
                prop="memo"
                show-overflow-tooltip
                v-if="item.name === '公司备注'"
              />
              <el-table-column
                align="center"
                label="是否授权"
                prop="isChecked"
                show-overflow-tooltip
                v-if="item.name === '是否授权'"
              >
                <template #default="{ row }">
                  {{ row.isChecked > 0 ? '是' : '否' }}
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
                  @click="qxSaveCD1(row)"
                  v-if="hasAuth('JGGSGLauth')"
                >
                  授权
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
    </div>
    <company-edit ref="edit" @fetch-data="fetchData" />
    <company-info ref="companyInfo" />
    <menu-list ref="menuList" />
  </div>
</template>

<script>
  import { qxsdCdqx } from '@/api/setting/org'
  import { doDelete } from '@/api/table'
  import CompanyTree from '@/views/setting/org/components/CompanyTree'
  import CompanyEdit from '@/views/setting/org/components/DepEdit'
  import CompanyInfo from '@/views/setting/org/components/CompanyInfo'
  import MenuList from '@/views/setting/org/components/MenuList'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: 'Menu',
    components: {
      MenuList,
      CompanyInfo,
      CompanyEdit,
      CompanyTree,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 10,
          pid: '',
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-org-menu-search',
        tableKey: 'setting-org-menu-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '公司名称' },
          { name: '公司简介' },
          { name: '公司备注' },
          { name: '是否授权' },
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
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '机构编号', key: 'code' },
          { name: '机构名称', key: 'name' },
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
        } = await qxsdCdqx({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      async qxSaveCD1(row) {
        this.$refs['menuList'].showEdit(row.orgid)
      },
      handleAdd() {
        this.$refs['edit'].showEdit(this.queryForm.pid)
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(this.queryForm.pid, row, disabled)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleSelectTree(org) {
        //
        this.queryForm.pid = org.id
        this.fetchData()
      },
    },
  }
</script>
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
