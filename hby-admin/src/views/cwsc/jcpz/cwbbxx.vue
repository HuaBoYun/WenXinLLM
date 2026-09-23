<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            checkable
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.handtext"
                clearable
                placeholder="版本名称"
                style="width: 200px"
                v-if="item.name === '版本名称'"
              ></el-input>
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
              <el-button @click="resetSearch()" type="primary">重置</el-button>
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
      <el-table
        v-loading="listLoading"
        :data="list"
        row-key="fid"
        :tree-props="{ children: 'childrenList' }"
      >
        <el-table-column label="版本名称" prop="handtext">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row, 'detail')"
              v-if="row.pid"
            >
              {{ row.handtext }}
            </el-button>
            <span v-else>{{ row.handtext }}</span>
          </template>
        </el-table-column>
        <!-- <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="修改时间"
            prop="modifiedtime"
            v-if="item.name === '修改时间'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="创建时间"
            prop="createdtime"
            v-if="item.name === '创建时间'"
          ></el-table-column>
        </div> -->
        <el-table-column align="center" label="操作" width="180">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleConfigTemplate(row)"
              :disabled="!!row.state"
              v-if="row.pid"
            >
              配置模板
            </el-button>
            <el-button
              type="text"
              @click="handleEdit(row, 'edit')"
              :disabled="!!row.state"
              v-if="row.pid"
            >
              修改
            </el-button>
            <el-button
              @click="handleDelete(row)"
              type="text"
              :disabled="!!row.state"
              v-if="row.pid"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <cwbbxxEdit ref="edit" @fetchData="fetchData"></cwbbxxEdit>
    <VersionFieldMappingModal
      ref="versionFieldMappingModal"
      @refresh="fetchData"
    ></VersionFieldMappingModal>

    <!-- <el-pagination
      class="pagination"
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    /> -->
  </div>
</template>

<script>
  import cwbbxxEdit from './components/cwbbxxEdit.vue'
  import VersionFieldMappingModal from './components/VersionFieldMappingModal.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { getCwbbxxList, deleteCwbbxx } from '@/api/cwsc'

  export default {
    components: {
      cwbbxxEdit,
      VersionFieldMappingModal,
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
          handtext: '',
          noticeName: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [{ name: '创建时间' }, { name: '修改时间' }], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'cwsc-jcpz-cwbbxx-search',
        tableKey: 'cwsc-jcpz-cwbbxx-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        select: [],
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '版本名称', key: 'name' },
          { name: '通知单名称', key: 'code' },
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
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
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
        const { data } = await getCwbbxxList(this.queryForm)
        this.list = data || []
        this.total = data.totalRecord || 0
        this.listLoading = false
      },
      handleEdit(row, type) {
        this.$refs['edit'].showEdit(row, type)
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await deleteCwbbxx({ fid: row.fid })
          if (res.msg == '成功') {
            this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          createType: 1,
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleView(row) {
        this.$refs['check'].showEdit(row)
      },
      handleConfigTemplate(row) {
        this.$refs['versionFieldMappingModal'].show(row)
      },
    },
  }
</script>

<style scoped lang="scss">
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
