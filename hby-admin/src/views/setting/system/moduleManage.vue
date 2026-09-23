<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-left-panel :span="18">
              <el-form
                ref="form"
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item v-for="(item, index) in searchItem" :key="index">
                  <el-input
                    v-model="queryForm.uniqueIdentification"
                    clearable
                    placeholder="模块标识"
                    v-if="item.name === '模块标识'"
                  />

                  <el-input
                    v-model="queryForm.projectName"
                    clearable
                    placeholder="模块名称"
                    v-if="item.name === '模块名称'"
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
              @click="handleAdd('mid')"
              v-if="hasAuth('XTMKPZaddMid')"
            >
              新建外部模块
            </el-button>
            <el-button
              type="success"
              @click="handleAdd()"
              v-if="isSuper && hasAuth('XTCDSZadd')"
            >
              新建内部模块
            </el-button>
          </vab-query-form-right-panel>
          <el-table
            v-loading="listLoading"
            :data="list"
            @sort-change="sortChange"
          >
            <el-table-column
              align="center"
              label="模块名称"
              prop="projectName"
            />
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="模块图标"
                prop="icon"
                v-if="item.name === '模块图标'"
              >
                <template #default="{ row }">
                  <vab-icon :icon="row.icon" />
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="模块标识"
                prop="uniqueIdentification"
                v-if="item.name === '模块标识'"
              />
              <el-table-column
                align="center"
                label="图标颜色"
                prop="color"
                v-if="item.name === '图标颜色'"
              >
                <template #default="{ row }">
                  <div :style="{ background: row.color }" class="color"></div>
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="模块排序"
                prop="sort"
                v-if="item.name === '模块排序'"
              />
              <el-table-column
                align="center"
                label="跳转类型"
                prop="projectType"
                v-if="item.name === '跳转类型'"
              >
                <template #default="{ row }">
                  {{ row.projectType == 2 ? '外部模块' : '内部模块' }}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="外部链接"
                prop="otherProjectRoute"
                v-if="item.name === '外部链接'"
              />
            </div>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="240"
            >
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDepart(row.id)"
                  v-if="hasAuth('XTCDSZauth') && (isSuper || isAdmin)"
                >
                  授权
                </el-button>
                <el-button
                  type="text"
                  @click="handleEdit(row.projectType == 2 ? 'mid' : '', row)"
                  v-if="hasAuth('XTCDSZedit') && (isSuper || isAdmin)"
                >
                  编辑
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="isSuper && hasAuth('XTCDSZdelete')"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
        <Views ref="edit" @fetch_data="fetchData" />
        <MidViews ref="editMid" @fetch_data="fetchData" />
        <SelectDepart ref="depart" @fetch_data="fetchData" />
      </div>
    </div>
  </div>
</template>

<script>
  import {
    getModuleList,
    deleteModule,
    isSuper,
    isAdmin,
  } from '@/api/setting/system'
  import Views from './components/moduleEdit.vue'
  import SelectDepart from './components/moduleForDepart.vue'
  import MidViews from './components/midModuleEdits.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'

  export default {
    name: '',
    components: { Views, MidViews, SelectDepart, filterSearch, filterTable },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        isSuper: false, //是否上帝账号
        isAdmin: false, //是否授权管理员
        queryForm: {
          uniqueIdentification: '',
          projectName: '',
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-system-moduleManage-search',
        tableKey: 'setting-system-moduleManage-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '模块图标' },
          { name: '模块标识' },
          { name: '图标颜色' },
          { name: '模块排序' },
          { name: '跳转类型' },
          { name: '外部链接' },
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
      isSuper().then((res) => {
        this.isSuper = res.data
      })
      isAdmin().then((res) => {
        this.isAdmin = res.data
      })
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
      handleDepart(id) {
        this.$refs['depart'].showEdit(id)
      },
      handleAdd(type) {
        if (type == 'mid') {
          this.$refs['editMid'].showEdit()
          return
        }
        this.$refs['edit'].showEdit()
      },
      handleEdit(type, row) {
        if (type == 'mid') {
          this.$refs['editMid'].showEdit(row)
          return
        }
        this.$refs['edit'].showEdit(row)
      },

      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          deleteModule({ id: row.id })
            .then(() => {
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
              this.fetchData()
            })
            .catch(() => {
              this.$message({
                type: 'info',
                message: '已取消删除',
              })
            })
        })
      },
      fetchData() {
        getModuleList({
          ...this.queryForm,
          sortFields: this.sortFields,
          sortFlag: this.sortFlag,
        }).then((res) => {
          this.list = res.data
          this.total = res.data.length
        })
      },
      resetQueryForm() {
        this.queryForm = {
          uniqueIdentification: '',
          projectName: '',
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '模块标识', key: 'uniqueIdentification' },
          { name: '模块名称', key: 'projectName' },
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
  .color {
    display: inline-block;
    width: 20px;
    height: 20px;
  }
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
