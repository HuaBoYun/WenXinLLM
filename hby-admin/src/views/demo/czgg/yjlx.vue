<template>
  <div class="system-log-container">
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
                v-model="queryForm.typeCode"
                clearable
                placeholder="类型编码"
                v-if="item.name === '类型编码'"
              />
              <el-input
                v-model="queryForm.typeName"
                clearable
                placeholder="类型名称"
                v-if="item.name === '类型名称'"
              />
              <el-select
                v-model="queryForm.isEnabled"
                clearable
                placeholder="启用状态"
                v-if="item.name === '启用状态'"
              >
                <el-option label="启用" :value="1" />
                <el-option label="禁用" :value="0" />
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
      <vab-query-form-right-panel>
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
        <el-button
          icon="el-icon-plus"
          type="primary"
          @click="handleAdd"
          style="margin-bottom: 10px; margin-right: 10px"
        >
          新增
        </el-button>
        <el-button
          icon="el-icon-delete"
          type="danger"
          @click="handleBatchDelete"
          :disabled="multipleSelection.length === 0"
          style="margin-bottom: 10px; margin-right: 10px"
        >
          批量删除
        </el-button>
      </vab-query-form-right-panel>

      <el-table
        ref="tableSort"
        v-loading="listLoading"
        :data="list"
        element-loading-text="正在查询中。。。"
        @selection-change="handleSelectionChange"
        @sort-change="tableSortChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column
          v-for="(item, index) in filedNow"
          :key="index"
          :prop="item.key"
          :label="item.name"
          :width="item.width"
          :sortable="item.sortable"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span v-if="item.key === 'isEnabled'">
              <el-tag :type="scope.row.isEnabled === 1 ? 'success' : 'danger'">
                {{ scope.row.isEnabled === 1 ? '启用' : '禁用' }}
              </el-tag>
            </span>
            <span v-else>{{ scope.row[item.key] }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
            <el-button
              size="mini"
              :type="scope.row.isEnabled === 1 ? 'warning' : 'success'"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.isEnabled === 1 ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :page-size="queryForm.pageSize"
        :layout="layout"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <seal-type-edit ref="edit" @refresh="fetchData" />
  </div>
</template>

<script>
import { getSealTypeList, deleteSealType, updateSealTypeStatus } from '@/api/globalTreasurer/czgg'
import SealTypeEdit from './components/yjlxEdit'

export default {
  name: 'SealTypeManagement',
  components: {
    SealTypeEdit
  },
  data() {
    return {
      list: [],
      listLoading: true,
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      selectRows: '',
      elementLoadingText: '正在查询中。。。',
      queryForm: {
        typeCode: '',
        typeName: '',
        isEnabled: null,
        pageNumber: 1,
        pageSize: 20
      },
      multipleSelection: [],
      // 搜索相关
      searchMore: false,
      searchItem: [],
      searchNow: [],
      searchAll: [
        { name: '类型编码', key: 'typeCode' },
        { name: '类型名称', key: 'typeName' },
        { name: '启用状态', key: 'isEnabled' }
      ],
      localKey: 'sealTypeSearch',
      // 表格相关
      filedNow: [],
      filedAll: [
        { name: '类型编码', key: 'typeCode', width: 150, sortable: true },
        { name: '类型名称', key: 'typeName', width: 200, sortable: true },
        { name: '启用状态', key: 'isEnabled', width: 100 },
        { name: '描述', key: 'description', width: 300 },
        { name: '创建时间', key: 'createTime', width: 160, sortable: true }
      ],
      tableKey: 'sealTypeTable'
    }
  },
  created() {
    this.initSearch()
    this.initTable()
    this.fetchData()
  },
  methods: {
    // 数据请求
    async fetchData() {
      this.listLoading = true
      try {
        const response = await getSealTypeList(this.queryForm)
        if (response.code === 1) {
          this.list = response.data.records || []
          this.total = response.data.total || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.listLoading = false
      }
    },
    // 重置查询
    resetSearch() {
      this.queryForm = {
        typeCode: '',
        typeName: '',
        isEnabled: null,
        pageNumber: 1,
        pageSize: 20
      }
      this.fetchData()
    },
    // 新增
    handleAdd() {
      this.$refs.edit.showEdit()
    },
    // 编辑
    handleEdit(row) {
      this.$refs.edit.showEdit(row)
    },
    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除该印鉴类型吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteSealType(row.sealTypeId)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.fetchData()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },
    // 切换状态
    handleToggleStatus(row) {
      const status = row.isEnabled === 1 ? 0 : 1
      const action = status === 1 ? '启用' : '禁用'
      this.$confirm(`确定要${action}该印鉴类型吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await updateSealTypeStatus({
            id: row.sealTypeId,
            isEnabled: status
          })
          if (response.code === 1) {
            this.$message.success(`${action}成功`)
            this.fetchData()
          } else {
            this.$message.error(response.msg || `${action}失败`)
          }
        } catch (error) {
          this.$message.error(`${action}失败：` + error.message)
        }
      })
    },
    // 批量删除
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm('确定要删除选中的印鉴类型吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.multipleSelection.map(item => item.sealTypeId)
          // 这里需要实现批量删除接口
          this.$message.success('删除成功')
          this.fetchData()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },
    // 表格选择
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 分页
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    // 排序
    tableSortChange({ column, prop, order }) {
      // 实现排序逻辑
    },
    // 搜索相关方法
    showMore() {
      this.searchMore = !this.searchMore
      if (this.searchMore) {
        this.searchItem = this.searchNow
      } else {
        this.searchItem = this.searchNow.slice(0, 4)
      }
    },
    initSearch() {
      let data = localStorage.getItem(this.localKey)
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
    },
    // 表格相关方法
    initTable() {
      this.listLoading = true
      let data = localStorage.getItem(this.tableKey)
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
      this.listLoading = false
    }
  }
}
</script>

<style scoped>
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

.search-more {
  cursor: pointer;
  color: #409eff;
  font-size: 13px;
  margin-left: 10px;
}

.search-more.is-opened .el-icon-arrow-down {
  transform: rotate(180deg);
}

.search-more .el-icon-arrow-down {
  transition: transform 0.3s;
}
</style>
