<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <LeftOrgTree @select="changeNode" />
      </div>
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-left-panel>
              <el-form
                ref="form"
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item v-for="(item, index) in searchItem" :key="index">
                  <el-input
                    v-model="queryForm.ruleno"
                    clearable
                    placeholder="规则编码"
                    style="width: 140px; margin-right: 20px"
                    v-if="item.name === '规则编码'"
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
          <vab-query-form-right-panel :span="24">
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
              v-if="hasAuth('YWBDHJKadd')"
            >
              新建
            </el-button>
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
            <el-table-column align="center" label="规则编码" prop="ruleno" sortable="custom"/>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="规则名称"
                prop="rulename"
                v-if="item.name === '规则名称'"
              />
              <el-table-column
                align="center"
                label="规则描述"
                prop="ruletip"
                show-overflow-tooltip
                v-if="item.name === '规则描述'"
              />
              <el-table-column
                align="center"
                label="创建时间"
                prop="createtime"
                v-if="item.name === '创建时间'"
                sortable="custom"
              />
              <el-table-column
                align="center"
                label="状态"
                prop="rulestatus"
                v-if="item.name === '状态'"
              >
                <template slot-scope="scope">
                  {{ scope.row.rulestatus === 1 ? '启用' : '禁用' }}
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
                  @click="handleEdit(row)"
                  v-if="hasAuth('YWBDHJKedit')"
                >
                  修改
                </el-button>
                <el-button
                  type="text"
                  @click="changeStatus(row)"
                  v-if="hasAuth('YWBDHJKstart')"
                >
                  {{ row.rulestatus == 1 ? '禁用' : '启用' }}
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="hasAuth('YWBDHJKdelete')"
                >
                  删除
                </el-button>
                <!-- <el-dropdown style="margin-left: 10px" @command="handleCommand">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>启用</el-dropdown-item>
                <el-dropdown-item>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown> -->
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-pagination
          class="pagination"
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

    <business-edit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    changeBussinessStatus,
    deleteBussiness,
    getBussinessList,
  } from '@/api/monitor/formMonitor/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import LeftOrgTree from '@/views/monitor/components/LeftOrgTree'
  import BusinessEdit from '@/views/monitor/formMonitor/components/BusinessEdit'
  import FormList from '@/views/monitor/formMonitor/components/FormList'
  import { hasAuth } from '@/utils'

  export default {
    name: 'Business',
    components: {
      BusinessEdit,
      FormList,
      LeftOrgTree,
      filterTable,
      filterSearch,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          ruleno: '',
          // name: '',
          pageNumber: 1,
          pageSize: 20,
        },
        orgId: '',
        orgName: '',
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'monitor-formMonitor-business-search',
        tableKey: 'monitor-formMonitor-business-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '规则名称' },
          { name: '规则描述' },
          { name: '创建时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        sortFields: '',
        sortFlag: 'asc',

      }
    },
    created() {
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
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
        let fields = [{ name: '规则编码', key: 'ruleno' }]
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
      changeNode(node) {
        this.orgId = node.id
        this.orgName = node.label
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

      fetchData() {
        this.listLoading = true
        getBussinessList({ ...this.queryForm, orgId: this.orgId , sortFields: this.sortFields,
          sortFlag: this.sortFlag,}).then(
          (res) => {
            this.list = res.data.pageInfo.records
            this.total = res.data.pageInfo.total
          }
        )
        this.listLoading = false
      },
      resetQueryForm() {
        this.queryForm = {
          ruleno: '',
          // name: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      handleAdd() {
        if (this.orgId == '') {
          this.$baseMessage('请选择节点', 'error', 'vab-hey-message-error')
        } else {
          const row = {
            orgid: this.orgId,
            orgname: this.orgName,
          }
          this.$refs['edit'].showEdit(row, '添加')
        }
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, '编辑')
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteBussiness({ ruleid: row.ruleid })
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
      handleCommand(command) {
        switch (command) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit()
            break
        }
      },
      changeStatus(row) {
        changeBussinessStatus({
          ruleid: row.ruleid,
          status: row.rulestatus == 1 ? 2 : 1,
        }).then((res) => {
          if (res.code === 200) {
            this.$baseMessage(
              row.rulestatus == 1 ? '禁用成功' : '启用成功',
              'success',
              'vab-hey-message-success'
            )
            this.fetchData()
          }
        })
      },
    },
  }
</script>
<style scoped>
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
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 10px;
    background: #fff;
    height: 100%;
  }
  .lr-layout > .right {
    padding: 0 20px 0 0;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
</style>
