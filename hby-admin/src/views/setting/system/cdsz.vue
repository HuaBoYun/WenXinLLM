<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <CdszTree @node-click="handleNodeClick" />
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
              @click="handleEdit()"
              v-if="hasAuth('XTCDSZadd')"
            >
              新建
            </el-button>
          </vab-query-form-right-panel>
          <el-table v-loading="listLoading" :data="list" @sort-change="sortChange">
            <el-table-column
              align="center"
              label="模块代码"
              prop="rightid"
              show-overflow-tooltip
            />
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="模块名称"
                prop="rightname"
                show-overflow-tooltip
                v-if="item.name === '模块名称'"
              />
              <el-table-column
                align="center"
                label="上级模块代码"
                prop="fatherrightid"
                show-overflow-tooltip
                v-if="item.name === '上级模块代码'"
              />
              <el-table-column
                align="center"
                label="状态"
                prop="indicatorstatus"
                v-if="item.name === '状态'"
              >
                <template #default="{ row }">
                  {{ row.indicatorstatus == 1 ? '启用' : '弃用' }}
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
                  @click="handleChangeStatus(row)"
                  v-if="hasAuth('XTCDSZstatus')"
                >
                  {{ row.indicatorstatus == 1 ? '弃用' : '启用' }}
                </el-button>
                <el-button
                  type="text"
                  @click="handleEdit(row)"
                  v-if="hasAuth('XTCDSZedit')"
                >
                  修改
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="hasAuth('XTCDSZdelete')"
                >
                  删除
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

        <div v-if="detail">
          <ul>
            <li>模块代码：{{ detail.rightid }}</li>
            <li>模块名称：{{ detail.rightname }}</li>
            <li>URL： {{ detail.righturl }}</li>
            <li>上级模块代码：{{ detail.fatherrightid }}</li>
            <li>显示顺序：{{ detail.funcorder }}</li>
            <li>是否为报表： {{ detail.custompage ? '是' : '否' }}</li>
            <li>
              是否启用：{{ detail.indicatorstatus == '1' ? '启用' : '弃用' }}
            </li>
          </ul>
        </div>
      </div>
    </div>

    <CdszEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getMenuSettingList,
    updateMenuSettingStatus,
    deleteMenuSetting,
  } from '@/api/setting/system'
  import CdszEdit from '@/views/setting/system/components/CdszEdit'
  import CdszTree from './components/CdszTree.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: 'Cdsz',
    components: { CdszEdit, CdszTree, filterSearch, filterTable },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          fatherrightid: -1,
          pageNumber: 1,
          pageSize: 20,
        },
        detail: null,
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-system-cdsz-search',
        tableKey: 'setting-system-cdsz-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '模块名称' },
          { name: '上级模块代码' },
          { name: '状态' },
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
        const { findByManageParentId, objTblManageRight } =
          await getMenuSettingList({...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag})
        if (!findByManageParentId) {
          this.detail = objTblManageRight
          this.list = []
        }
        if (!objTblManageRight) {
          this.list = findByManageParentId
          this.detail = null
        }
        this.listLoading = false
      },
      handleView() {
        this.$refs['edit'].showEdit()
      },
      async handleChangeStatus(row) {
        // 状态切换无限制
        const { code, msg } = await updateMenuSettingStatus({
          rightid: row.rightid,
          status: row.indicatorstatus == 0 ? 1 : 0,
        })
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        }
      },
      handleEdit(row) {
        if (!row) {
          // 新建：只能新建一级菜单

          if (this.queryForm.fatherrightid != 1) {
            const msg = '只能新建一级菜单'
            this.$baseMessage(msg, 'error', 'vab-hey-message-error')
            return
          }
          this.$refs['edit'].showEdit()
        } else {
          // 修改：只有弃用状态下可以修改，修改以后可以再变成启用状态
          if (row.indicatorstatus == 1) {
            const msg = '已启用'
            this.$baseMessage(msg, 'error', 'vab-hey-message-error')
            return
          }
          this.$refs['edit'].showEdit(row)
        }
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteMenuSetting({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleNodeClick(data) {
        this.queryForm.fatherrightid = data.value
        this.fetchData()
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
