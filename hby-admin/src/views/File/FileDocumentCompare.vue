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
                v-model="queryForm.fileName"
                clearable
                placeholder="文件名"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '文件名'"
              />
              <el-date-picker
                v-model="queryForm.createTime"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                v-if="item.name === '创建时间'"
              ></el-date-picker>
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
              <el-button @click="resetSearch">重置</el-button>
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
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>

        <el-button type="success" @click="handleAdd" v-if="hasAuth('BSCCadd')">
          新建
        </el-button>
      </vab-query-form-right-panel>
      <el-table :data="list">
        <el-table-column
          align="center"
          label="行号"
          prop="index"
          width="50"
        ></el-table-column>
        <el-table-column align="center" label="参照文件" prop="file1">
          <template #default="{ row, $index }">
            <el-button type="text" v-if="row.tableData1.length != 0">
              {{ row.tableData1.attname }}
            </el-button>
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleSuccess1"
              :before-upload="handleBeforeUpload"
              v-if="row.tableData1.length == 0"
            >
              <el-button type="text" @click="handleIndex($index)">
                选择
              </el-button>
            </el-upload>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="页数"
          prop="firstNum"
          width="60"
        />
        <el-table-column align="center" label="比较文件" prop="file2">
          <template #default="{ row, $index }">
            <el-button type="text" v-if="row.tableData2.length != 0">
              {{ row.tableData2.attname }}
            </el-button>
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleSuccess2"
              :before-upload="handleBeforeUpload"
              v-if="row.tableData2.length == 0"
            >
              <el-button type="text" @click="handleIndex($index)">
                选择
              </el-button>
            </el-upload>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="页数"
          prop="secondNum"
          width="60"
        />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="文件数"
            prop="similarity"
            v-if="item.name === '文件数'"
          />
          <el-table-column
            align="center"
            label="对比数"
            prop="creator"
            v-if="item.name === '对比数'"
          />
          <el-table-column
            align="center"
            label="状态"
            prop="status"
            v-if="item.name === '状态'"
          />
        </div>

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row, $index }">
            <el-button
              type="text"
              v-if="
                row.tableData1.length != 0 &&
                row.tableData2.length != 0 &&
                hasAuth('BSCCopen')
              "
              @click="handleOpen"
            >
              开始对比
            </el-button>
            <el-button
              type="text"
              @click="handleDelete($index)"
              v-if="hasAuth('BSCCdelete')"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <compare ref="compare"></compare>
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
</template>

<script>
  import filterTable from '@/components/filterTable.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import compare from './components/compare.vue'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import { hasAuth } from '@/utils'

  export default {
    name: 'FileDrawingCompare',
    components: { filterTable, filterSearch, compare },
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          createTime: '',
          fileName: '',
          pageNumber: 0,
          pageSize: 20,
        },
        filedAll: [{ name: '文件数' }, { name: '对比数' }, { name: '状态' }], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'file-file-document-compare-search',
        tableKey: 'file-file-document-compare-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        tableData1: [],
        tableData2: [],
        activiteIndex: 0,
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
          { name: '文件名', key: 'fileName' },
          { name: '创建时间', key: 'createTime' },
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
      resetQueryForm() {
        this.queryForm = {
          createTime: '',
          fileName: '',
          pageNumber: 0,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = false
      },

      handleAdd() {
        const info = {
          index: 1,
          file1: '测试文件1',
          firstNum: 1,
          file2: '对比文件1',
          secondNum: 1,
          similarity: '10%',
          creator: '张三',
          status: '对比完成',
          tableData1: [],
          tableData2: [],
        }
        this.list = this.list.concat([info])
      },
      handleSuccess1(file) {
        if (file.result == '200') {
          this.list[this.activiteIndex].tableData1 = file.data
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handleSuccess2(file) {
        if (file.result == '200') {
          this.list[this.activiteIndex].tableData2 = file.data
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handleIndex(index) {
        this.activiteIndex = index
      },
      handleDelete(index) {
        this.list.splice(index, 1)
      },
      handleOpen() {
        this.$refs['compare'].show()
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
