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
              <el-select
                v-model="queryForm.aiStatus"
                clearable
                placeholder="AI学习"
                v-if="item.name === 'AI学习'"
              >
                <el-option
                  key="未学习"
                  value="未学习"
                  label="未学习"
                ></el-option>
                <el-option
                  key="学习中"
                  value="学习中"
                  label="学习中"
                ></el-option>
                <el-option
                  key="完成学习"
                  value="完成学习"
                  label="完成学习"
                ></el-option>
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

        <el-upload
          class="upload-demo"
          style="display: inline-block"
          :show-file-list="false"
          :action="baseApi + api"
          :headers="headers"
          :on-success="handleSuccess"
          :before-upload="handleBeforeUpload"
        >
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            v-if="hasAuth('WJKWHupload')"
          >
            上传文本库
          </el-button>
        </el-upload>
        <el-button-group style="margin-left: 10px">
          <el-button
            type="danger"
            plain
            icon="el-icon-s-tools"
            @click="handleAIScript"
            v-if="hasAuth('WJKWHallAiScript')"
          >
            AI全量学习
          </el-button>
          <el-button
            type="danger"
            plain
            icon="el-icon-setting"
            @click="handleAIScript"
            v-if="hasAuth('WJKWHAiScript')"
          >
            AI增量学习
          </el-button>
        </el-button-group>
      </vab-query-form-right-panel>
      <el-table :data="list" @sort-change="sortChange">
        <el-table-column
          align="center"
          label="行号"
          prop="index"
          width="50"
        ></el-table-column>
        <el-table-column
          align="center"
          label="文件名"
          prop="fileName"
        ></el-table-column>
        <el-table-column
          align="center"
          label="文件类型"
          prop="fileType"
          width="60"
        />
        <el-table-column
          align="center"
          label="文件大小(KB)"
          prop="fileSize"
          width="60"
        />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="上传者"
            prop="uploader"
            v-if="item.name === '上传者'"
          />
          <el-table-column
            align="center"
            label="上传时间"
            prop="createTime"
            v-if="item.name === '上传时间'"
            sortable="custom"
          />
          <el-table-column
            align="center"
            label="AI学习"
            prop="status"
            v-if="item.name === 'AI学习'"
          />
        </div>

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ $index }">
            <el-button
              type="text"
              @click="handleTransformFile"
              v-if="hasAuth('WJKWHtransform')"
            >
              转换
            </el-button>
            <el-button
              type="text"
              @click="handleDelete($index)"
              v-if="hasAuth('WJKWHdelete')"
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
    name: 'FileDocumentMaintenance',
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
        filedAll: [
          { name: '上传者' },
          { name: '上传时间' },
          { name: 'AI学习' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'file-file-document-maintenance-search',
        tableKey: 'file-file-document-maintenance-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        tableData1: [],
        tableData2: [],
        activiteIndex: 0,
        sortFields: '',
        sortFlag: 'asc',
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
        let fields = [
          { name: '文件名', key: 'fileName' },
          { name: '创建时间', key: 'createTime' },
          { name: 'AI学习', key: 'aiStatus' },
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
      handleAIScript() {
        this.$confirm('是否确认执行AI学习指令？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            this.$message({
              type: 'success',
              message: '已执行',
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消',
            })
          })
      },
      handleSuccess(file) {
        if (file.result == '200') {
          this.$baseMessage(file.msg, 'success')
          const info = {
            index: 1,
            fileName: '测试文件1',
            fileType: 'docx',
            fileSize: '11.99kb',
            uploader: '张三',
            createTime: '2023-06-24 16:45:12',
            status: '未学习',
            tableData1: [],
            tableData2: [],
          }
          this.list = this.list.concat([info])
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
        this.$confirm('是否确认删除所选数据项？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            this.list.splice(index, 1)
            this.$message({
              type: 'success',
              message: '已删除',
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消',
            })
          })
      },
      handleTransformFile() {
        this.$confirm('是否确定转换所选数据项？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            this.$message({
              type: 'success',
              message: '已转换',
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消',
            })
          })
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
