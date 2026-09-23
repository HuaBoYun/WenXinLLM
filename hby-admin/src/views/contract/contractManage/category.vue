<template>
  <div class="system-log-container lr-layout">
    <div class="left">
      <category-tree ref="category_tree" @node-change="handleNodeChange" />
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
                  v-model="queryForm.choiceTypeName"
                  clearable
                  placeholder="输入合同类型"
                  :style="{ width: '100%' }"
                  v-if="item.name === '合同类型'"
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
                  :class="searchMore ? 'search-more is-opened' : 'search-more'"
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
          <el-button type="success" @click="handleAdd">新建</el-button>
        </vab-query-form-right-panel>
        <el-table v-loading="listLoading" :data="list">
          <el-table-column align="center" label="名称" prop="typename" />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleEdit(row)">修改</el-button>
              <el-button type="text" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <el-pagination
        background
        class="pagination"
        :current-page="queryForm.pageNumber"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>

    <category-edit
      ref="edit"
      :parentid="queryForm.typeId"
      @fetch-data="fetchData"
    />
  </div>
</template>

<script>
  import {
    getContractTypeList,
    deleteContractType,
  } from '@/api/contract/manage'
  import CategoryEdit from '@/views/contract/contractManage/components/CategoryEdit'
  import CategoryTree from './components/CategoryTree.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'

  export default {
    name: 'Category',
    components: { CategoryEdit, CategoryTree, filterTable, filterSearch },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          choiceTypeName: '',
          typeId: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        field109Options: [
          {
            label: '类型1',
            value: 1,
          },
          {
            label: '类型2',
            value: 2,
          },
          {
            label: '类型3',
            value: 3,
          },
          {
            label: '类型4',
            value: 4,
          },
        ],
        localKey: 'contract-contractManage-category-search',
        tableKey: 'contract-contractManage-category-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [],
      }
    },
    created() {
      this.fetchData()
      //初始化表格&筛选
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initTable()
      this.initSearch()
    },
    methods: {
      // 动态筛选 动态表格 初始化数据&相关方法
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      getFiled() {
        let fields = [{ name: '合同类型', key: 'choiceTypeName' }]
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
      resetQueryForm() {
        this.queryForm = {
          choiceTypeName: '',
          pageNumber: 1,
          pageSize: 20,
        }
        // this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getContractTypeList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
        await this.$refs['category_tree'].fetchTree()
      },
      //树选择
      handleNodeChange(data) {
        this.queryForm.pageNumber = 1
        if (data.value == 0) {
          this.queryForm.typeId = undefined
        } else {
          this.queryForm.typeId = data.value
        }

        this.fetchData()
      },

      handleEdit(row) {
        const info = JSON.parse(localStorage.getItem('userInfo'))
        const name =
          JSON.parse(localStorage.getItem('userInfo')).roleNames.split(',') ||
          []
        const isSuperAdmin = name.includes('合同管理员')
        if (row) {
          if (row.createstaff == info.staffid || isSuperAdmin) {
            this.$refs['edit'].showEdit(row)
            return
          }
          this.$baseMessage('无权限操作', 'error', 'vab-hey-message-error')
        }

        // if (row && (row.orgid !== info.linkOrg.orgid || !isSuperAdmin)) {
        //   this.$baseMessage('无权限操作', 'error', 'vab-hey-message-error')
        //   return
        // }
        // if (row && (row.orgid !== info.linkOrg.orgid || isSuperAdmin)) {
        //   this.$baseMessage('无权限操作', 'error', 'vab-hey-message-error')
        //   return
        // }
      },
      //新增
      handleAdd() {
        const name =
          JSON.parse(localStorage.getItem('userInfo')).roleNames.split(',') ||
          []
        const isSuperAdmin = name.includes('合同类型维护人员')

        //isSuperAdmin为true的时候可以新建一级和二级，false的时候只能新建二级
        if (!this.queryForm.typeId && !isSuperAdmin) {
          this.$message.error('没有权限编辑一级菜单')
          return
        }
        this.$refs['edit'].showEdit()
      },
      //删除
      handleDelete(row) {
        const info = JSON.parse(localStorage.getItem('userInfo'))
        const name =
          JSON.parse(localStorage.getItem('userInfo')).roleNames.split(',') ||
          []
        const isSuperAdmin = name.includes('合同管理员')
        if (row) {
          if (row.createstaff == info.staffid || isSuperAdmin) {
            this.$baseConfirm('你确定要删除当前项吗', null, async () => {
              await deleteContractType({ typeId: row.typeid })
              // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              if (this.list.length === 1 && this.queryForm.pageNumber > 1) {
                this.queryForm.pageNumber -= 1
              }
              await this.fetchData()
              await this.$refs['category_tree'].fetchTree()
            })
            return
          }
          this.$baseMessage('无权限操作', 'error', 'vab-hey-message-error')
        }
        // if (row && (row.orgid !== info.linkOrg.orgid || !isSuperAdmin)) {
        //   this.$baseMessage('无权限操作', 'error', 'vab-hey-message-error')
        //   return
        // }
        // this.$baseConfirm('你确定要删除当前项吗', null, async () => {
        //   await deleteContractType({ typeId: row.typeid })
        //   // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        //   await this.fetchData()
        //   await this.$refs['category_tree'].fetchTree()
        // })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
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

  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }

  .secondCard {
    margin-top: -5px !important;
  }

  .pagination {
    margin-bottom: 20px !important;
  }
</style>
