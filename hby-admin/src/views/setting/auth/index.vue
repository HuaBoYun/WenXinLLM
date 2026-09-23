<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <dep-leftlist ref="leftlist" @select="leftList" :isAll="true" />
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
                    v-model="queryForm.userName"
                    clearable
                    placeholder="用户名"
                  />
                </el-form-item>
                <el-form-item>
                  <el-input
                    v-model="queryForm.name"
                    clearable
                    placeholder="真实姓名"
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

                <el-button
                  slot="reference"
                  icon="el-icon-s-grid"
                  class="biaoge"
                  style="margin-bottom: 10px; margin-right: 10px"
                ></el-button>
              </el-popover>
            </el-tooltip>
            <!-- <el-button type="success" @click="handleAdd">新建</el-button> -->
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list">
            <el-table-column align="center" label="用户名" prop="username">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="$refs['edit'].showEdit(row, true)"
                >
                  {{ row.username }}
                </el-button>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="用户真实姓名"
                prop="realname"
                v-if="item.name === '用户真实姓名'"
              />
              <el-table-column
                align="center"
                label="手机"
                prop="miblephone"
                v-if="item.name === '手机'"
              />
              <el-table-column
                align="center"
                label="固定电话"
                prop="fixedphone"
                v-if="item.name === '固定电话'"
              />
              <el-table-column
                align="center"
                label="电子邮件"
                prop="email"
                v-if="item.name === '电子邮件'"
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
                <el-button type="text" @click="$refs.authList.showEdit(row)">
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
    <user-edit ref="edit" @fetch-data="fetchData" />
    <user-info ref="userInfo" />
    <auth-list ref="authList" />
  </div>
</template>

<script>
  import { qxsdList } from '@/api/setting/auth'
  import DepLeftlist from '@/views/setting/org/components/DepTree'
  import UserInfo from '@/views/setting/auth/components/UserInfo'
  import UserEdit from '@/views/setting/auth/components/UserEdit'
  import AuthList from '@/views/setting/auth/components/AuthList'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: 'Index',
    components: {
      AuthList,
      UserEdit,
      UserInfo,
      DepLeftlist,
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
          userName: '',
          name: '',
          pageNumber: 1,
          pageSize: 10,
          orgId: '',
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-auth-index-search',
        tableKey: 'setting-auth-index-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '用户真实姓名' },
          { name: '手机' },
          { name: '固定电话' },
          { name: '电子邮件' },
          { name: '备注' },
        ], //所有表格项
        filedNow: [],
        sortFields: '',
        sortFlag: 'asc',
      }
    },
    created() {
      // this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
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
          data: { tlist, totalRecord },
        } = await qxsdList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
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
        // console.log(org)
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
