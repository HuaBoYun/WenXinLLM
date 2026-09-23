<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <industry-tree
          ref="leftlist"
          @select="handleSelectTree"
          @selectFirst="handleSelectFirst"
        />
      </div>
      <div class="right">
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
              v-if="hasAuth('HYJGadd')"
            >
              新建
            </el-button>
            <el-button
              type="success"
              @click="qxSaveCD1"
              v-if="hasAuth('HYJGauth')"
            >
              授权分配
            </el-button>
          </vab-query-form-right-panel>
          <el-table
            v-loading="listLoading"
            :data="list"
            @selection-change="handleSelectionChange"
            @sort-change="sortChange"
          >
            <el-table-column type="selection" />
            <el-table-column align="center" label="机构编号" prop="orgnumber"   sortable="custom">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row, true)"
                  v-if="hasAuth('HYJGdetail')"
                >
                  {{ row.orgnumber }}
                </el-button>
                <div v-else>{{ row.orgnumber }}</div>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="机构名称"
                prop="orgname"
                v-if="item.name === '机构名称'"
              />
              <el-table-column
                align="center"
                label="机构简介"
                prop="orgmeno"
                show-overflow-tooltip
                v-if="item.name === '机构简介'"
              />
              <el-table-column
                align="center"
                label="备注"
                prop="memo"
                show-overflow-tooltip
                v-if="item.name === '备注'"
              />
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
                  @click="handleEdit(row)"
                  v-if="hasAuth('HYJGedit')"
                >
                  修改
                </el-button>
                <el-dropdown style="margin-left: 10px">
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <!-- <el-dropdown-item @click.native="qxSaveCD1(row)">
                  授权分配
                </el-dropdown-item> -->
                    <el-dropdown-item
                      @click.native="authorizeDel1(row)"
                      v-if="hasAuth('HYJGcancel')"
                    >
                      取消授权
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handleDelete(row)"
                      v-if="hasAuth('HYJGdelete')"
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
      </div>
    </div>
    <industry-edit ref="edit" @fetch-data="handleListChange" />
    <company-info ref="companyInfo" />
    <menu-list ref="menuList" />
    <!-- <industry-list ref="industryList" /> -->
    <CompanySelect ref="company-select" />
    <IndustrySelect ref="industry-select" />
  </div>
</template>

<script>
  import { orgHylist } from '@/api/setting/org'
  import { orgHydel } from '@/api/setting/org'
  import IndustryTree from '@/views/setting/org/components/IndustryTree'
  import CompanyInfo from '@/views/setting/org/components/CompanyInfo'
  import MenuList from '@/views/setting/org/components/MenuList'
  import IndustryEdit from '@/views/setting/org/components/IndustryEdit'
  // import IndustryList from '@/views/setting/org/components/IndustryList'
  import CompanySelect from './components/CompanySelect.vue'
  import IndustrySelect from './components/IndustrySelect.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: 'Industry',
    components: {
      IndustryEdit,
      MenuList,
      CompanyInfo,
      IndustryTree,
      // IndustryList,
      CompanySelect,
      IndustrySelect,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        companyList1: [],
        queryForm: {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 10,
          pid: '',
        },
        multipleSelection: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-org-industry-search',
        tableKey: 'setting-org-industry-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '机构名称' },
          { name: '机构简介' },
          { name: '备注' },
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
      handleListChange() {
        this.fetchData()
        this.fetchTree()
      },
      handleSizeChange(val) {
        this.queryForm.pageNumber = val
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
      //列表
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await orgHylist({...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag})
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      fetchTree() {
        this.$refs['leftlist'].fetchData()
      },
      handleAdd() {
        this.$refs['edit'].showEdit(this.queryForm.pid)
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(this.queryForm.pid, row, disabled)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { code, msg } = await orgHydel({ orgid: row.orgid })
          if (code == 1) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            await this.fetchData()
            this.fetchTree()
          }
        })
      },
      authorizeDel1(row) {
        this.$refs['industry-select'].show(row.orgid)
        // this.$baseConfirm('你确定要取消授权当前项吗', null, async () => {
        //   const { msg } = await authorizeDel({ hyid: row.orgid })
        //   this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        //   await this.fetchData()
        // })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      //行业架构-授权分配
      async qxSaveCD1() {
        // this.listLoading = true
        // const { data } = await organAccreditByHY({ orgids: row.orgid })
        // this.listLoading = false
        // this.$refs['industryList'].showEdit(row.orgid)
        //
        if (!this.multipleSelection.length) {
          this.$baseMessage('请选择', 'error', 'vab-hey-message-error')
          return
        }
        const orgids = this.multipleSelection.map((item) => item.orgid)
        this.$refs['company-select'].show(orgids.join(','))
      },
      handleSelectTree(org) {
        //
        this.queryForm.pid = org.id
        this.fetchData()
      },
      handleSelectFirst(org) {
        this.queryForm.pid = org.id
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
