<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <dep-leftlist ref="leftlist" @select="leftList" :isAll="true" />
      </div>
      <div class="right">
        <vab-query-form>
          <vab-query-form-left-panel v-if="false">
            <el-form
              ref="form"
              :inline="true"
              label-width="0"
              :model="queryForm"
              @submit.native.prevent
            >
              <el-form-item>
                <el-input
                  v-model="queryForm.name"
                  clearable
                  placeholder="账套名称"
                />
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="queryForm.name"
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
            <el-button
              native-type="submit"
              type="primary"
              @click="handleRightOrCancel(1)"
              v-if="hasAuth('ZTSQauth')"
            >
              授权
            </el-button>
            <el-button
              native-type="submit"
              type="primary"
              @click="handleRightOrCancel(0)"
              v-if="hasAuth('ZTSQcancelAuth')"
            >
              取消授权
            </el-button>
          </vab-query-form-right-panel>
          <el-table
            ref="multipleTable"
            v-loading="listLoading"
            :data="list"
            @selection-change="handleSelectionChange"
            @sort-change="sortChange"
          >
            <el-table-column align="center" type="selection" width="60" />
            <el-table-column
              align="center"
              label="ID"
              prop="acctid"
              sortable="custom"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="
                    $refs.accountUser.showEdit({
                      pid: queryForm.pid,
                      bookid: row.bookid,
                    })
                  "
                  v-if="hasAuth('ZTSQdetail')"
                >
                  {{ row.acctid }}
                </el-button>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="名称"
                prop="bookname"
                v-if="item.name === '名称'"
              />
              <el-table-column
                align="center"
                label="公司"
                prop="orgname"
                v-if="item.name === '公司'"
              />
              <el-table-column
                align="center"
                label="年份"
                prop="bookyear"
                v-if="item.name === '年份'"
              />
            </div>
            <el-table-column width="1" />
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
      <user-edit ref="edit" @fetch-data="fetchData" />
      <user-info ref="userInfo" />
      <account-user ref="accountUser" />
      <auth-user ref="authUser" />
      <cancel-auth-user ref="cancelAuthUser" />
    </div>
  </div>
</template>

<script>
  import { newindexs } from '@/api/setting/auth'
  import CompanyTree from '@/views/setting/org/components/CompanyTree'
  import UserInfo from '@/views/setting/auth/components/UserInfo'
  import UserEdit from '@/views/setting/auth/components/UserEdit'
  import AccountUser from '@/views/setting/auth/components/AccountUser'
  import AuthUser from '@/views/setting/auth/components/AuthUser'
  import CancelAuthUser from '@/views/setting/auth/components/CancelAuthUser'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'
  import DepLeftlist from '@/views/setting/org/components/CompanyTree'

  export default {
    name: 'Index',
    components: {
      CancelAuthUser,
      AuthUser,
      AccountUser,
      UserEdit,
      UserInfo,
      CompanyTree,
      filterSearch,
      filterTable,
      DepLeftlist,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        multipleSelection: [],
        queryForm: {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 20,
          orgId: undefined,
          pid: undefined,
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-auth-accredit-search',
        tableKey: 'setting-auth-accredit-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [{ name: '名称' }, { name: '公司' }, { name: '年份' }], //所有表格项
        filedNow: [],
        sortFields: '',
        sortFlag: 'asc',
      }
    },
    created() {
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
      handleRightOrCancel(type) {
        const acctid = this.multipleSelection.map((i) => i.bookid).join(',')
        if (!acctid.length) {
          this.$message.error('请选择')
          return
        }
        if (type === 1) {
          this.$refs.authUser.showEdit({
            acctid,
            pid: this.queryForm.pid,
          })
        } else {
          this.$refs.cancelAuthUser.showEdit({
            acctid,
          })
        }
        this.$refs.multipleTable.clearSelection()
      },
      handleSelectionChange(val) {
        console.warn('handleSelectionChange', val)
        this.multipleSelection = val
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
          pageInfo: { tlist, totalRecord },
        } = await newindexs({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit(this.queryForm.pid)
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(this.queryForm.pid, row)
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleCommand(command) {
        switch (command) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit()
            break
        }
      },
      leftList(org) {
        //
        this.queryForm.pid = org.id
        this.fetchData()
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '用户名', key: 'userName' },
          { name: '真实姓名', key: 'name' },
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
