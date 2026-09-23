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
                    v-model="queryForm.rid"
                    clearable
                    placeholder="编号"
                    v-if="item.name === '编号'"
                  />

                  <el-input
                    v-model="queryForm.rname"
                    clearable
                    placeholder="名称"
                    v-if="item.name === '名称'"
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
              v-if="hasAuth('JSGLadd')"
            >
              新建
            </el-button>
          </vab-query-form-right-panel>
          <el-table
            v-loading="listLoading"
            :data="list"
            @sort-change="sortChange"
          >
            <el-table-column
              align="center"
              label="编号"
              prop="rid"
              sortable="custom"
            />
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="名称"
                prop="rname"
                v-if="item.name === '名称'"
              />
              <el-table-column
                align="center"
                label="描述"
                prop="rdesc"
                show-overflow-tooltip
                v-if="item.name === '描述'"
              />
              <el-table-column
                align="center"
                label="状态"
                prop="rstatus"
                v-if="item.name === '状态'"
              >
                <template #default="{ row }">
                  {{ row.rstatus == 1 ? '启用' : '禁用' }}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="是否授权"
                prop="rightCount"
                v-if="item.name === '是否授权'"
              >
                <template #default="{ row }">
                  {{ row.rightCount > 0 ? '是' : '否' }}
                </template>
              </el-table-column>
            </div>
            <el-table-column align="center" label="操作" width="190">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row)"
                  v-if="hasAuth('JSGLedit')"
                >
                  修改
                </el-button>
                <el-dropdown style="margin-left: 10px">
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>
                      <el-button
                        type="text"
                        @click="$refs.roleAuthList.showEdit(row)"
                        v-if="hasAuth('JSGLauth')"
                      >
                        授权
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-button
                        type="text"
                        @click="orgGsdel1(row)"
                        v-if="hasAuth('JSGLstatus')"
                      >
                        {{ row.rstatus == 0 ? '启用' : '禁用' }}
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-button
                        type="text"
                        @click="handleDelete(row)"
                        v-if="hasAuth('JSGLdelete')"
                      >
                        删除
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-button
                        type="text"
                        @click.native="handlCompanyRoleTree(row)"
                      >
                        公司授权
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-button
                        type="text"
                        @click.native="removeCompanyRole(row)"
                      >
                        取消公司授权
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-button
                        type="text"
                        @click.native="handlUserRoleTree(row)"
                      >
                        用户授权
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item>
                      <el-button type="text" @click.native="removeUser(row)">
                        取消用户授权
                      </el-button>
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
      <role-edit ref="edit" @fetch-data="fetchData" />
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
      <ProcessList ref="process" @fetchData="fetchData" />
    </div>
  </div>
</template>

<script>
  import { roleList, modifyRstatus, rqorg, rquser } from '@/api/setting/auth'
  import { roleDel } from '@/api/setting/auth'
  import RoleEdit from '@/views/setting/auth/components/RoleEdit'
  import RoleAuthList from '@/views/setting/auth/components/RoleAuthList'
  import RoleUserList from '@/views/setting/auth/components/RoleUserList'
  import CompanyRoleTree from '@/views/setting/auth/components/CompanyRoleTree'
  import UserTable from '@/views/setting/auth/components/UserTable'
  import RemoveUser from '@/views/setting/auth/components/RemoveUser'
  import RemoveCompanyRoleTree from '@/views/setting/auth/components/RemoveCompanyRoleTree'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'
  import { getFlowList } from '@/api/setting/auth'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  export default {
    name: 'Download',
    components: {
      RoleAuthList,
      RoleEdit,
      RoleUserList,
      CompanyRoleTree,
      UserTable,
      RemoveUser,
      RemoveCompanyRoleTree,
      filterSearch,
      filterTable,
      ProcessList,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          rid: '',
          rname: '',
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
          { name: '名称' },
          { name: '描述' },
          { name: '状态' },
          { name: '是否授权' },
        ], //所有表格项
        filedNow: [],
        sortFields: '',
        sortFlag: 'asc',
        requireValuedata: false,
      }
    },
    created() {
      this.fetchData()
      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      const info = JSON.parse(localStorage.getItem('userInfo'))
      // 判断是否需要流程校验
      if (info.requireValuedata) {
        this.requireValuedata = info.requireValuedata
      }
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
        const { code, data } = await rqorg({
          roleid: this.tow.rid,
          orgids: val.map((item) => item.id).join(','),
        })
        if (this.requireValuedata) {
          //  查询当前是否有流程
          getFlowList({
            targetId: data.recordId,
            targetType: 'grant',
            operationType: 8,
          }).then((res) => {
            if (res.data == 0) {
              // 可以提交流程
              this.$refs['process'].save(220, data.recordId)
              this.$baseMessage(
                '审批流程提交成功,请等待审批',
                'success',
                'vab-hey-message-success'
              )
              this.dialogTreeVisible = false
              this.keys = []
            } else {
              // 不可以提交流程
              this.$baseMessage(
                '当前用户流程已存在,请先走审批流程',
                'error',
                'vab-hey-message-error'
              )
              return
            }
          })
        } else {
          if (code == 1) {
            this.$message.success('授权成功')
            this.fetchData()
          }
        }
      },
      async handUserTableSubmit(val) {
        const { code, data } = await rquser({
          roleid: this.tow.rid,
          staffids: val
            .map((item) => {
              return item.staffid
            })
            .join(','),
        })
        if (this.requireValuedata) {
          //  查询当前是否有流程
          getFlowList({
            targetId: data.recordId,
            targetType: 'grant',
            operationType: 7,
          }).then((res) => {
            if (res.data == 0) {
              // 可以提交流程
              this.$refs['process'].save(220, data.recordId)
              this.$baseMessage(
                '审批流程提交成功,请等待审批',
                'success',
                'vab-hey-message-success'
              )
              this.dialogTreeVisible = false
              this.keys = []
            } else {
              // 不可以提交流程
              this.$baseMessage(
                '当前用户流程已存在,请先走审批流程',
                'error',
                'vab-hey-message-error'
              )
              return
            }
          })
        } else {
          if (code == 1) {
            this.$message.success('授权成功')
            this.fetchData()
          }
        }
      },
      resetQueryForm() {
        // 重置查询表单为初始状态
        this.queryForm = {
          rid: '',
          rname: '',
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
          data: { tlist, totalRecord },
        } = await roleList({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        // if (row.rstatus == 1) {
        //   this.$message.error('正在使用，无法操作')
        //   return
        // }
        this.$baseConfirm('你确定要删除当前项吗?', null, async () => {
          const { code, msg, confirm } = await roleDel({ rid: row.rid })

          // 流程校验
          if (this.requireValuedata) {
            //  查询当前是否有流程
            getFlowList({
              targetId: confirm.recordId,
              targetType: 'role',
              operationType: 3,
            }).then((res) => {
              if (res.data == 0) {
                // 可以提交流程
                this.$refs['process'].save(220, confirm.recordId)
                this.$baseMessage(
                  '审批流程提交成功,请等待审批',
                  'success',
                  'vab-hey-message-success'
                )
                this.close()
              } else {
                // 不可以提交流程
                this.$baseMessage(
                  '当前用户流程已存在,请先走审批流程',
                  'error',
                  'vab-hey-message-error'
                )
                return
              }
            })
          } else {
            if (code == 1) {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              await this.fetchData()
            }
          }
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      orgGsdel1(row) {
        let text = row.rstatus == 1 ? '禁用' : '启用'
        let newStatus = row.rstatus == 1 ? 0 : 1
        this.$baseConfirm(`你确定要${text}当前项吗`, null, async () => {
          const { msg, data } = await modifyRstatus({
            str: newStatus,
            selectedId: row.rid,
          })
          // 流程校验
          if (this.requireValuedata) {
            //  查询当前是否有流程
            getFlowList({
              targetId: data.recordId,
              targetType: 'role',
              operationType: row.rstatus == 1 ? 5 : 4,
            }).then((res) => {
              if (res.data == 0) {
                // 可以提交流程
                this.$refs['process'].save(220, data.recordId)
                this.$baseMessage(
                  '审批流程提交成功,请等待审批',
                  'success',
                  'vab-hey-message-success'
                )
                this.close()
              } else {
                // 不可以提交流程
                this.$baseMessage(
                  '当前用户流程已存在,请先走审批流程',
                  'error',
                  'vab-hey-message-error'
                )
                return
              }
            })
          } else {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '编号', key: 'rid' },
          { name: '名称', key: 'rname' },
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
