<template>
  <div class="system-log-container">
    <el-tree
      class="sidebar"
      :data="treeData"
      node-key="typeId"
      default-expand-all
      :props="defaultProps"
      @node-click="handleNodeClick"
      :expand-on-click-node="false"
    ></el-tree>
    <div class="content">
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.auditType"
                clearable
                placeholder="审计类型"
                v-if="item.name === '审计类型'"
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
            <el-form-item style="cursor: pointer">
              <span
                :class="searchMore ? 'search-more is-opened' : 'search-more'"
                @click="showMore"
              >
                <span>{{ searchMore ? '收起' : '展开' }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-form-item>
          </el-form>
        </vab-query-form-top-panel>
      </el-card>

      <el-card shadow="never">
        <vab-query-form>
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
                <el-button
                  slot="reference"
                  icon="el-icon-s-grid"
                  class="biaoge"
                  style="margin-bottom: 10px; margin-right: 10px"
                ></el-button>
              </el-popover>
            </el-tooltip>
            <el-button type="success" @click="addNode">新建</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table :data="tableData" style="width: 100%">
          <el-table-column width="1" />
          <div v-for="(item, index) in filedNow" :key="index">
            <el-table-column
              prop="auditType"
              align="center"
              v-if="item.name === '审计类型说明'"
              label="审计类型说明"
            />
            <el-table-column
              prop="version"
              align="center"
              v-if="item.name === '审计类型版本'"
              label="审计类型版本"
            />
            <el-table-column
              prop="version"
              align="center"
              v-if="item.name === '状态'"
              label="状态"
            >
              <template #default="{ row }">
                {{ row.status == 1 ? '禁用' : '正常' }}
              </template>
            </el-table-column>
          </div>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button type="text" @click="editNode(scope.row)">
                编辑
              </el-button>
              <el-button type="text" @click="detailNode(scope.row)">
                详情
              </el-button>
              <el-button type="text" @click="deleteNode(scope.row)">
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
    </div>
    <CategoryEdit ref="edit" @fetch-data="addNewItem" />
  </div>
</template>

<script>
  import {
    getNbsjTypeOfList,
    removeNbsjTypeOf,
  } from '@/api/workbench/auditTools'
  import CategoryEdit from '@/views/workbench/auditTools/components/CategoryEdit'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable, CategoryEdit },
    data() {
      return {
        treeData: [
          {
            typeId: 1,
            auditType: '全部',
            childrenList: [],
          },
        ],
        tableData: [{ nodeName: '节点1' }, { nodeName: '节点2' }],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          auditType: '',
          pageNumber: 1,
          pageSize: 20,
        },
        addData: {},
        selectedTypeId: 1,
        // 筛选列表配置
        filedAll: [
          { name: '审计类型说明' },
          { name: '审计类型版本' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'workbench-auditTools-category-search',
        tableKey: 'workbench-auditTools-category-list',
        searchMore: true,
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
      getFiled() {
        return [{ name: '审计类型', key: 'auditType' }]
      },
      async fetchData() {
        this.getParamentList()
        this.getChildList(this.selectedTypeId || 1)
      },
      async getParamentList() {
        const { data } = await getNbsjTypeOfList({ parentId: 0 })
        this.treeData[0].childrenList = data.date
      },
      async getChildList(typeId) {
        const targetId = typeId ?? this.selectedTypeId ?? 1
        const { data } = await getNbsjTypeOfList({
          ...this.queryForm,
          parentId: targetId,
        })
        this.tableData = data.tlist || []
        this.total = data.totalRecord || 0
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getChildList(this.selectedTypeId || 1)
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getChildList(this.selectedTypeId || 1)
      },
      handleNodeClick(data) {
        this.selectedTypeId = data.typeId
        this.getChildList(this.selectedTypeId)
        this.addData = data
        // this.tableData = data.childrenList
      },
      // 编辑节点逻辑
      editNode(row) {
        this.$refs.edit.showEdit(row, 'edit')
      },
      // 详情节点逻辑
      detailNode(row) {
        this.$refs.edit.showEdit(row, 'detail')
      },
      deleteNode(row) {
        this.$confirm('是否确认删除所选数据项？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            removeNbsjTypeOf({ typeId: row.typeId }).then((res) => {
              if (res.code != 1) return
              this.$message.success('删除成功')
              this.getChildList(row.typeId)
              this.filterTreeData(this.treeData, row.typeId)
              console.log(
                '🚀 ~ removeNbsjTypeOf ~ this.filterTreeData(this.treeData, row.typeId):',
                this.treeData
              )
            })
          })
          .catch(() => {})
        // 删除节点逻辑
      },
      addNode() {
        this.$refs.edit.showEdit(this.addData, 'add')
        // 添加节点逻辑
      },
      //新增完以后刷新
      addNewItem(parentid, data) {
        let id = parentid ? parentid : 1
        this.treeData[0].childrenList = data
        this.getChildList(id)
      },
      //静默处理左侧树结构
      filterTreeData(array, targetTypeId) {
        for (let i = 0; i < array.length; i++) {
          const item = array[i]
          // 检查当前项的 typeId 是否匹配
          if (item.typeId === targetTypeId) {
            // 删除当前项及其子项
            array.splice(i, 1)
            return true // 如果删除成功，返回 true
          }
          // 如果当前项有子项并且是数组，递归检查子项
          if (Array.isArray(item.childrenList)) {
            const found = this.filterTreeData(item.childrenList, targetTypeId)
            if (found) {
              // 如果在子项中找到并删除了匹配的项，也需要检查并可能删除当前项
              if (item.typeId === targetTypeId) {
                array.splice(i, 1)
                return true
              }
            }
          }
        }
        return false // 如果没有找到匹配的项，返回 false
      },

      resetQueryForm() {
        this.queryForm = {
          auditType: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.getChildList(this.selectedTypeId || 1)
      },
    },
    computed: {
      defaultProps() {
        return {
          children: 'childrenList',
          label: 'auditType',
        }
      },
    },
  }
</script>

<style scoped>
  .system-log-container {
    display: flex;
    padding: 0 !important;
    background: #f6f8f9 !important;
  }

  .margin-b0 {
    margin-bottom: 0;
  }

  .sidebar {
    padding: 10px;
    min-width: 200px;
    margin-right: 20px;
    overflow-x: scroll;
  }

  .content {
    flex: 1;
    /* background: #fff; */
    padding: 10px;
  }

  .add-node {
    margin-top: 10px;
  }
</style>
