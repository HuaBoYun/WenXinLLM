<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <company-tree ref="leftlist" @select="handleTreeSelect" :isAll="true" />
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
                    v-model="queryForm.code"
                    clearable
                    placeholder="机构编号"
                    v-if="item.name === '机构编号'"
                  />
                  <el-input
                    v-model="queryForm.name"
                    clearable
                    placeholder="机构名称"
                    v-if="item.name === '机构名称'"
                  />
                  <el-select
                    v-model="queryForm.status"
                    clearable
                    placeholder="状态"
                    v-if="item.name === '状态'"
                  >
                    <el-option label="启用" value="0" />
                    <el-option label="弃用" value="1" />
                  </el-select>
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
              v-if="hasAuth('JGGSGLadd')"
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
              label="公司编号"
              prop="orgnumber"
              sortable="custom"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleEdit(row, true)">
                  {{ row.orgnumber }}
                </el-button>
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
                label="状态"
                prop="status"
                v-if="item.name === '状态'"
              >
                <template #default="{ row }">
                  {{
                    row.status === 0
                      ? '启用'
                      : row.status === 1
                      ? '弃用'
                      : '未启用'
                  }}
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
                  @click="handleEdit(row, false)"
                  v-if="hasAuth('JGGSGLedit')"
                >
                  修改
                </el-button>
                <el-dropdown style="margin-left: 10px" @command="handleCommand">
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item
                      @click.native="orgGsdel1(row)"
                      v-if="hasAuth('JGGSGLstatus')"
                    >
                      {{ row.status === 0 ? '弃用' : '启用' }}
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handleDelete(row)"
                      v-if="hasAuth('JGGSGLdelete')"
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
    <company-edit ref="edit" @fetch-data="fetchData" @fetch-tree="fetchTree" />
    <company-info ref="companyInfo" />
    <ProcessList ref="process" @fetchData="fetchData" />
  </div>
</template>

<script>
  import { orgListorg } from '@/api/setting/org'
  import { orgisQY, orgGsdel } from '@/api/setting/org'
  import CompanyTree from '@/views/setting/org/components/CompanyTree'
  import CompanyEdit from '@/views/setting/org/components/CompanyEdit'
  import CompanyInfo from '@/views/setting/org/components/CompanyInfo'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'
  import { getFlowList } from '@/api/setting/auth'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  export default {
    name: 'Company',
    components: {
      CompanyInfo,
      CompanyEdit,
      CompanyTree,
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
          code: undefined,
          name: undefined,
          pageNumber: 1,
          pageSize: 10,
          orgId: '',
          status: '',
        },
        companyLists: '1',
        companyLists1: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-org-company-search',
        tableKey: 'setting-org-company-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '公司名称' },
          { name: '公司简介' },
          { name: '公司备注' },
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
      resetQueryForm() {
        this.queryForm = {
          code: undefined,
          name: undefined,
          pageNumber: 1,
          pageSize: 10,
          orgId: '',
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
        } = await orgListorg({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        })
        this.list = tlist.map((i) => {
          return {
            ...i,
            iszy: i.iszy !== undefined ? parseInt(i.iszy) : 0,
          }
        })
        this.total = totalRecord
        this.listLoading = false
      },
      fetchTree() {
        this.$refs['leftlist'].fetchData()
      },
      handleAdd() {
        this.$refs['edit'].showEdit(this.queryForm.orgId)
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(this.queryForm.orgId, row, disabled)
      },
      handleImport() {},
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { code, msg, data } = await orgisQY({ str: row.orgid })
          // 流程校验
          if (this.requireValuedata) {
            //  查询当前是否有流程
            getFlowList({
              targetId: data.recordId,
              targetType: 'company',
              operationType: 3,
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
            if (code == 1) {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              await this.fetchData()
              this.fetchTree()
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
      handleCommand(command) {
        switch (command) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit()
            break
        }
      },
      orgGsdel1(row) {
        let text = row.status === 0 ? '弃用' : '启用'
        let newStatus = row.status === 0 ? 1 : 0
        this.$baseConfirm('你确定要' + text + '当前项吗', null, async () => {
          const { msg, data } = await orgGsdel({
            str: newStatus,
            orgid: row.orgid,
          })
          // 流程校验
          if (this.requireValuedata) {
            //  查询当前是否有流程
            getFlowList({
              targetId: data.recordId,
              targetType: 'company',
              operationType: row.status === 0 ? 5 : 4,
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
            this.fetchTree()
          }
        })
      },
      handleTreeSelect(org) {
        console.log(org.id)
        this.queryForm.orgId = org.id
        this.fetchData()
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '机构编号', key: 'code' },
          { name: '机构名称', key: 'name' },
          { name: '状态', key: 'status' },
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
  .system-log-container {
    padding: 0 !important;
  }
  .lr-layout {
    display: flex;
    background: #f6f8f9;
  }

  .lr-layout > .left {
    width: 250px;
    overflow: hidden;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 20px 20px 20px;
    background: #ffffff;
  }

  .lr-layout > .right {
    flex: 1;
  }
  .secondCard {
    margin-top: -5px !important;
  }
</style>
