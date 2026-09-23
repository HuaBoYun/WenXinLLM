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

        <el-button
          type="success"
          @click="handleAdd"
          v-if="hasAuth('YYWJFCGadd')"
        >
          新建
        </el-button>
      </vab-query-form-right-panel>
      <el-table :data="list" v-loading="loading">
        <el-table-column
          align="center"
          label="行号"
          prop="index"
          width="50"
        ></el-table-column>
        <el-table-column align="center" label="参照文件" prop="file1">
          <template #default="{ row, $index }">
            <el-button type="text" v-if="row.tableData1.length != 0">
              {{ row.tableData1[0].name }}
            </el-button>
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :before-upload="handleBeforeUpload"
              :http-request="fileUpload"
              v-if="row.tableData1.length == 0"
            >
              <el-button
                type="text"
                @click="handleIndex($index, row.id, 'ori_file1')"
              >
                选择
              </el-button>
            </el-upload>
          </template>
        </el-table-column>
        <!-- <el-table-column
          align="center"
          label="页数"
          prop="firstNum"
          width="60"
        /> -->
        <el-table-column align="center" label="比较文件" prop="file2">
          <template #default="{ row, $index }">
            <el-button type="text" v-if="row.tableData2.length != 0">
              {{ row.tableData2[0].name }}
            </el-button>
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :before-upload="handleBeforeUpload"
              :http-request="fileUpload"
              v-if="row.tableData2.length == 0"
            >
              <el-button
                type="text"
                @click="handleIndex($index, row.id, 'ori_file2')"
              >
                选择
              </el-button>
            </el-upload>
          </template>
        </el-table-column>
        <!-- <el-table-column
          align="center"
          label="页数"
          prop="secondNum"
          width="60"
        /> -->
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="状态"
            prop="status"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{ status[+row.status] }}
            </template>
          </el-table-column>
        </div>

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row, $index }">
            <el-button
              type="text"
              v-if="
                row.tableData1.length != 0 &&
                row.tableData2.length != 0 &&
                hasAuth('YYWJFCGopen')
              "
              @click="handleOpen(row)"
            >
              开始对比
            </el-button>
            <el-button
              type="text"
              @click="handleDelete($index, row.id)"
              v-if="hasAuth('YYWJFCGdelete')"
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
  import { hasAuth, polling } from '@/utils'
  import axios from 'axios'

  export default {
    name: 'FileTamperproof',
    components: { filterTable, filterSearch, compare },
    data() {
      return {
        baseApi: 'https://office.wenxin.example.com',
        api: '/api/app/filescompare/',
        headers: {
          token: store.getters['user/token'],
        },
        list: [],
        loading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          createTime: '',
          fileName: '',
          pageNumber: 0,
          pageSize: 20,
        },
        filedAll: [{ name: '状态' }], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'file-file-tamperproof-search',
        tableKey: 'file-file-tamperproof-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        tableData1: [],
        tableData2: [],
        activiteIndex: 0,
        status: ['未比对', '比对中', '比对成功', '比对失败'],
        activiteId: '',
        activiteFileName: '',
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
        // this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        // this.fetchData()
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
      fetchData() {
        this.loading = true
        axios({
          url: `https://office.wenxin.example.com/api/app/filescompare/`,
          method: 'get',
          headers: { 'Content-Type': 'application/json' },
        })
          .then((res) => {
            const info = res.data.results.map((i, index) => {
              return {
                ...i,
                tableData1: i.ori_file1_name
                  ? [{ name: i.ori_file1_name }]
                  : [],
                tableData2: i.ori_file2_name
                  ? [{ name: i.ori_file2_name }]
                  : [],
                index: index + 1,
              }
            })
            this.list = info
          })
          .finally(() => {
            this.loading = false
          })
      },

      handleAdd() {
        this.loading = true
        axios({
          url: `https://office.wenxin.example.com/api/app/filescompare/`,
          method: 'post',
          data: {
            name: '',
            ori_file1: null,
            ori_file2: null,
            ret_file1: null,
            ret_file2: null,
            detail: '',
            status: 0,
          },
          headers: { 'Content-Type': 'application/json' },
        })
          .then((res) => {
            if (res.data.id) {
              this.fetchData()
            }
          })
          .finally(() => {
            this.loading = false
          })
      },

      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handleIndex(index, id, fileName) {
        this.activiteIndex = index
        this.activiteId = id
        this.activiteFileName = fileName
      },
      handleDelete(index, id) {
        this.loading = true
        axios({
          url: `https://office.wenxin.example.com/api/app/filescompare/${id}/`,
          method: 'DELETE',
          headers: { 'Content-Type': 'application/json' },
        })
          .then((res) => {
            this.fetchData()
          })
          .finally(() => {
            this.loading = false
          })
      },
      handleOpen(row) {
        // 已比对完成，直接打开详情
        if (row.status == 2) {
          this.loading = true
          axios({
            url: `https://office.wenxin.example.com/api/app/filescompare/${row.id}/`,
            method: 'get',
            headers: { 'Content-Type': 'application/json' },
          })
            .then((k) => {
              if (k.data.id) {
                this.$refs['compare'].show(k.data)
              }
            })
            .finally(() => {
              this.loading = false
            })
          return
        }
        // 未比对未完成，需要轮询
        this.loading = true
        axios({
          url: `https://office.wenxin.example.com/api/app/filescompare/${row.id}/exec_compare/`,
          method: 'post',
          headers: { 'Content-Type': 'application/json' },
        })
          .then((res) => {
            if (res.data.task_id) {
              //轮训比对状态
              polling(
                'get',
                `https://office.wenxin.example.com/api/app/filescompare/${row.id}/exec_compare/?task_id=${res.data.task_id}`,
                2000
              )
                .then((j) => {
                  //轮训结束调取比对详情接口
                  axios({
                    url: `https://office.wenxin.example.com/api/app/filescompare/${row.id}/`,
                    method: 'get',
                    headers: { 'Content-Type': 'application/json' },
                  })
                    .then((k) => {
                      if (k.data.id) {
                        this.loading = false
                        this.$refs['compare'].show(k.data)
                        this.fetchData()
                      }
                    })
                    .catch((err) => {
                      this.$message.error('获取对比详情失败！')
                      this.loading = false
                    })
                })
                .catch((err) => {
                  this.$message.error('轮询失败！')
                  this.loading = false
                })
              return
            }
            this.$message.error('获取task_id失败！')
            this.loading = false
          })
          .catch((err) => {
            this.$message.error('对比失败！')
            this.loading = false
          })
      },
      fileUpload(val) {
        // 创建新的数据对象
        let formData = new FormData()
        // 将上传的文件放到数据对象中
        formData.append(this.activiteFileName, val.file)
        axios({
          url: `https://office.wenxin.example.com/api/app/filescompare/${this.activiteId}/`,
          method: 'patch',
          data: formData,
          headers: { 'Content-Type': 'multipart/form-data' },
        }).then((res) => {
          if (res.data.id) {
            if (this.activiteFileName === 'ori_file1') {
              this.list[this.activiteIndex].tableData1 = [val.file] //显示文件名
            } else {
              this.list[this.activiteIndex].tableData2 = [val.file] //显示文件名
            }
          }
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
