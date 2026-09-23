<template>
  <div class="system-log-container">
    <div class="lr-layout">
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
                <el-form-item v-for="(item, index) in searchItem" :key="index">
                  <el-input
                    v-model="queryForm.createstaffname"
                    clearable
                    placeholder="创建人姓名"
                    v-if="item.name === '创建人姓名'"
                  />

                  <el-input
                    v-model="queryForm.dockstaffname"
                    clearable
                    placeholder="对接人姓名"
                    v-if="item.name === '对接人姓名'"
                  />

                  <el-input
                    v-model="queryForm.transefrstaffname"
                    clearable
                    placeholder="移交人姓名"
                    v-if="item.name === '移交人姓名'"
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
                <el-form-item>
                  <span
                    :class="
                      searchMore ? 'search-more is-opened' : 'search-more'
                    "
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
          <vab-query-form-right-panel class="option-row" :span="24">
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
              >
              <!-- v-if="hasAuth('GZYJadd')" -->
              新增
            </el-button>
          </vab-query-form-right-panel>
          <el-table
            v-loading="listLoading"
            :data="list"
            @sort-change="sortChange"
          >
            <el-table-column align="center" label="工作移交原因" prop="tranenablereason">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDetail(row)"
                >
                  {{ row.tranenablereason }}
                </el-button>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="对接人姓名"
                prop="dockstaffname"
                v-if="item.name === '对接人姓名'"
              />
              <el-table-column
                align="center"
                label="单据隶属公司"
                prop="linkorgid"
                show-overflow-tooltip
                v-if="item.name === '单据隶属公司'"
              />
              <el-table-column
                align="center"
                label="对接状态"
                prop="transtatus"
                v-if="item.name === '对接状态'"
              >
                <template #default="{ row }">
                  {{ row.transtatus == 1 ? '启用' : row.transtatus == 2 ? '弃用' : '未生效'}}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="移交人姓名"
                prop="transefrstaffname"
                v-if="item.name === '移交人姓名'"
              />
              <el-table-column
                align="center"
                label="移交部门数据"
                prop="tranorgnamestrs"
                show-overflow-tooltip
                v-if="item.name === '移交部门数据'"
              />
              <el-table-column
                align="center"
                label="移交时间"
                prop="transfertime"
                v-if="item.name === '移交时间'"
              >
                <template slot-scope="{ row }">
                  {{ row.transfertime? dayjs(row.transfertime).format('YYYY-MM-DD') : '' }}
                </template>
              </el-table-column>
            </div>
            <el-table-column align="center" label="操作" width="190">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row)"
                  >
                  <!-- v-if="hasAuth('GZYJedit')" -->
                  修改
                </el-button>
                <el-button
                  type="text"
                  @click="handleStatus(row)"
                  >
                  <!-- v-if="hasAuth('GZYJedit')" -->
                  {{ row.transtatus == '1'? '弃用' : '启用' }}
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
      <WorkEdit ref="edit" @fetchData="fetchData" />

      <role-auth-list ref="roleAuthList" @fetch-data="fetchData" />
      <RoleUserList ref="roleUserList" />
      <CompanyRoleTree
        ref="CompanyRoleTree"
        @selected="handCompanyRoleSubmit"
      />
      <RemoveCompanyRoleTree
        ref="RemoveCompanyRoleTree"
        @selected="handRemoveCompanyRoleSubmit"
      />
      <UserTable ref="UserTable" @selected="handUserTableSubmit" />
      <RemoveUser ref="RemoveUser" @selected="handRemoveUserSubmit" />
    </div>
  </div>
</template>

<script>
  import { transferWorkList, transferEnableStatus } from '@/api/setting/auth'
  import { roleDel } from '@/api/setting/auth'
  import WorkEdit from '@/views/setting/auth/components/WorkEdit'
  import RoleAuthList from '@/views/setting/auth/components/RoleAuthList'
  import RoleUserList from '@/views/setting/auth/components/RoleUserList'
  import CompanyRoleTree from '@/views/setting/auth/components/CompanyRoleTree'
  import UserTable from '@/views/setting/auth/components/UserTable'
  import RemoveUser from '@/views/setting/auth/components/RemoveUser'
  import RemoveCompanyRoleTree from '@/views/setting/auth/components/RemoveCompanyRoleTree'
  import { hasAuth } from '@/utils'
  import * as dayjs from 'dayjs'
  export default {
    name: 'Download',
    components: {
      RoleAuthList,
      WorkEdit,
      RoleUserList,
      CompanyRoleTree,
      UserTable,
      RemoveUser,
      RemoveCompanyRoleTree,
    },
    data() {
      return {
        dayjs: dayjs,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          createstaffname: '',
          dockstaffname: '',
          transefrstaffname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        tow: {},
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-auth-role-search',
        tableKey: 'setting-auth-role-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '对接人姓名' },
          { name: '单据隶属公司' },
          { name: '对接状态' },
          { name: '移交人姓名' },
          { name: '移交部门数据' },
          { name: '移交时间' },
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
      handRemoveCompanyRoleSubmit() {
        this.fetchData()
      },
      handRemoveUserSubmit() {
        this.fetchData()
      },
      handGetroleUserList(row) {
        this.$refs['roleUserList'].showEdit(row)
      },
      handlCompanyRoleTree(row) {
        this.tow = row
        this.$refs['CompanyRoleTree'].showEdit(row.rid)
      },
      removeCompanyRole(row) {
        this.tow = row
        this.$refs['RemoveCompanyRoleTree'].showEdit(row)
      },
      handlUserRoleTree(row) {
        this.tow = row
        this.$refs['UserTable'].show(row.rid)
      },
      removeUser(row) {
        this.tow = row
        this.$refs['RemoveUser'].showEdit(row)
      },

      async handCompanyRoleSubmit(val) {
        console.log(val, 'ssssssssssss')
        const res = await rqorg({
          roleid: this.tow.rid,
          orgids: val.map((item) => item.id).join(','),
        })
        if (res.code == 1) {
          this.$message.success('授权成功')
          this.fetchData()
        }
      },
      async handUserTableSubmit(val) {
        console.log(val, 'ssssssssssss')
        const res = await rquser({
          roleid: this.tow.rid,
          staffids: val
            .map((item) => {
              return item.staffid
            })
            .join(','),
        })
        if (res.code == 1) {
          this.$message.success('授权成功')
          this.fetchData()
        }
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm
        this.queryForm = {
          createstaffname: '',
          dockstaffname: '',
          transefrstaffname: '',
          pageNumber: 1,
          pageSize: 20,
        }
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
          data: { records, total },
        } = await transferWorkList({
          ...this.queryForm
        })
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, '新增')
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit(row, '详情')
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, '编辑')
      },
      handleDelete(row) {
        // if (row.rstatus == 1) {
        //   this.$message.error('正在使用，无法操作')
        //   return
        // }
        this.$baseConfirm('你确定要删除当前项吗?', null, async () => {
          const { code, msg } = await roleDel({ rid: row.rid })
          if (code == 1) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleStatus(row) {

        let text = row.transtatus == 1 ? '禁用' : '启用'

        let newStatus = row.transtatus == 1 ? 2 : 1
        this.$baseConfirm('你确定要' + text + '当前项吗', null, async () => {
          const { msg } = await transferEnableStatus({
            transtatus: newStatus,
            transferid: row.transferid,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '创建人姓名', key: 'createstaffname' },
          { name: '对接人姓名', key: 'dockstaffname' },
          { name: '移交人姓名', key: 'transefrstaffname' },
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
  .tool-tip {
    max-width: 800px;
  }
  .lr-layout {
    background: #f6f8f9;
  }
  .system-log-container {
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
</style>
