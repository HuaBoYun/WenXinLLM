<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <dep-tree
          ref="leftlist"
          :loaded-select="false"
          @select="leftList"
          :isAll="true"
        />
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
                <el-form-item v-for="(item, index) in searchItem" :key="index">
                  <el-input
                    v-model="queryForm.username"
                    clearable
                    placeholder="用户名"
                    v-if="item.name === '用户名'"
                  />

                  <el-input
                    v-model="queryForm.realname"
                    clearable
                    placeholder="真实姓名"
                    v-if="item.name === '真实姓名'"
                  />
                </el-form-item>
                <el-form-item>
                  <el-checkbox v-model="queryForm.isAll">
                    是否筛选全集团
                  </el-checkbox>
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
              @click="handleAdd()"
              v-if="hasAuth('YHGLadd')"
            >
              新建
            </el-button>
            <el-button type="success" @click="handleExport()">导出</el-button>
            <file-upload
              accept=".xls,.xlsx"
              api="/setting/baseInfo/importUserInfoList"
              :show-file-list="false"
              @success="fetchData"
              style="display: inline-block; margin-left: 10px"
            >
              <el-button type="success">导入</el-button>
            </file-upload>
            <el-button
              type="success"
              @click="handleRecordList"
              style="margin-left: 10px"
            >
              导入记录
            </el-button>
          </vab-query-form-right-panel>
          <el-table
            v-loading="listLoading"
            :data="list"
            @sort-change="sortChange"
          >
            <el-table-column align="center" label="用户名" prop="username">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row, true)"
                  v-if="hasAuth('YHGLdetail')"
                >
                  {{ row.username }}
                </el-button>
                <div v-else>{{ row.username }}</div>
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
              <!-- <el-table-column
                align="center"
                label="角色"
                prop="roleNames"
                show-overflow-tooltip
                v-if="item.name === '角色'"
              /> -->
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
              <el-table-column
                align="center"
                label="所属部门"
                prop="orgname"
                show-overflow-tooltip
                v-if="item.name === '所属部门'"
              />

              <el-table-column
                align="center"
                label="状态"
                prop="status"
                #default="{ row }"
                v-if="item.name === '状态'"
              >
                {{ row.status === 1 ? '启用' : '弃用' }}
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
                  @click="handleEdit(row)"
                  v-if="hasAuth('YHGLedit')"
                >
                  修改
                </el-button>
                <el-button
                  type="text"
                  @click="handlePwdEdit(row)"
                  v-if="hasAuth('YHGLreset')"
                >
                  重置密码
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
    <user-pwd-edit ref="editpwd" />
    <user-info ref="userInfo" />
    <recordList ref="recordList" />
  </div>
</template>

<script>
  import { getUserListALL, exportUserInfoList } from '@/api/setting/auth'
  import UserInfo from '@/views/setting/auth/components/UserInfo'
  import UserEdit from '@/views/setting/auth/components/UserEdit'
  import UserPwdEdit from '@/views/setting/auth/components/UserPwdEdit'
  import DepTree from '@/components/DepTree.vue'
  // import DepTree from '@/views/setting/org/components/DepTree'
  import filterSearch from '@/components/filterSearch.vue'
  import FileUpload from '@/components/FileUploadSetting.vue'
  import filterTable from '@/components/filterTable.vue'
  import recordList from '@/views/setting/org/components/recordList'
  import { hasAuth } from '@/utils'

  export default {
    name: 'User',
    components: {
      UserEdit,
      UserPwdEdit,
      UserInfo,
      DepTree,
      recordList,
      FileUpload,
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
          isAll: 0,
          realname: '',
          username: '',
          orgid: '',
          pageNumber: 1,
          pageSize: 10,
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-auth-user-search',
        tableKey: 'setting-auth-user-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '用户真实姓名' },
          { name: '手机' },
          // { name: '角色' },
          { name: '电子邮件' },
          { name: '备注' },
          { name: '所属部门' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        sortFields: '',
        sortFlag: 'asc',
        requireValuedata: false, // 是否需要流程校验
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
      handleRecordList() {
        this.$refs['recordList'].show(2)
      },
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
        this.queryForm = {
          isAll: 0,
          realname: '',
          username: '',
          pageNumber: 1,
          pageSize: 10,
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
          data: { list, total },
        } = await getUserListALL({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
          isAll: this.queryForm.isAll ? 1 : 0,
        })
        this.list = list.map((i) => {
          return {
            ...i,
            orgname: i.orgname,
          }
        })
        this.total = total
        this.listLoading = false
      },
      async handleExport() {
        const data = await exportUserInfoList({ orgId: this.queryForm.orgid })

        let fileName = '人员信息.xlsx'
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
      handleAdd() {
        this.$refs['edit'].showEdit({}, false)
      },
      handlePwdEdit(row) {
        this.$refs['editpwd'].showEdit(row)
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
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
        this.queryForm.orgid = org.id
        this.fetchData()
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '用户名', key: 'username' },
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
