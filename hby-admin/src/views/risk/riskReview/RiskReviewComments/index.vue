<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            checkable
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              v-for="(item, index) in searchItem"
              :key="index"
              :prop="item.key"
            >
              <el-input
                v-model="queryForm.mattername"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
              />
              <el-input
                v-model="queryForm.mattercode"
                clearable
                placeholder="三重一大事项编码"
                v-if="item.name === '三重一大事项编码'"
              />
              <el-input
                v-model="queryForm.riskreviewcode"
                clearable
                placeholder="风险审查报告编码"
                v-if="item.name === '风险审查报告编码'"
              />
              <el-input
                v-model="queryForm.unitname"
                clearable
                placeholder="公司名称"
                v-if="item.name === '公司名称'"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                type="primary"
                @click="queryData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="fetchData('reset')">
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
        </vab-query-form-top-panel>
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
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="项目名称" prop="mattername">
          <template #default="{ row }">
            <el-button type="text" @click="handleRead(row)">
              {{ row.mattername }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <!-- <el-table-column
            align="center"
            label="项目编码"
            prop="matterprojectcode"
            v-if="item.name === '项目编码'"
          /> -->
          <el-table-column
            align="center"
            label="三重一大事项编码"
            prop="mattercode"
            v-if="item.name === '三重一大事项编码'"
          />
          <el-table-column
            align="center"
            label="风险审查报告编码"
            prop="riskreviewcode"
            v-if="item.name === '风险审查报告编码'"
          />
          <el-table-column
            align="center"
            label="公司名称"
            prop="orgname"
            v-if="item.name === '公司名称'"
          />
          <el-table-column
            align="center"
            label="经办人"
            prop="staffidname"
            show-overflow-tooltip
            v-if="item.name === '经办人'"
          />
          <el-table-column
            align="center"
            label="经办部门"
            prop="staffdeptname"
            v-if="item.name === '经办部门'"
          />
          <el-table-column
            align="center"
            label="申请时间"
            prop="createtime"
            :formatter="formatDay"
            v-if="item.name === '申请时间'"
          />
          <el-table-column
            align="center"
            label="状态"
            prop="state"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{
                row.state == 1
                  ? '审批中'
                  : row.state == 2
                  ? '需调整'
                  : row.state == 3
                  ? '已撤销'
                  : row.state == 4
                  ? '已终止'
                  : row.state == 5
                  ? '已跟踪'
                  : row.state == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">审查意见</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-pagination
      background
      class="pager"
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <ReviewEdit ref="edit" @fetchData="fetchData" />
  </div>
</template>

<script>
  import {
    riskReviewDelete,
    riskReviewOpinionList,
  } from '@/api/risk/riskReview'
  import { formatDay } from '@/utils/index'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import ReviewEdit from './components/ReviewEdit.vue'
  export default {
    name: 'NormalReportList',
    components: {
      ReviewEdit,
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          mattername: '',
          unitname: '',
          mattercode: '',
          riskreviewcode: '',
          pageNumber: 1,
          pageSize: 20,
        },
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-riskReview-riskReviewBook-search',
        tableKey: 'risk-riskReview-riskReviewBook-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        stateOptions: [
          { label: '审批中', value: 1 },
          { label: '需调整', value: 2 },
          { label: '已撤销', value: 3 },
          // { label: '已终止', value: 4 },
          // { label: '已跟踪', value: 5 },
          { label: '已完成', value: 6 },
          { label: '未审批', value: 0 },
        ],
        filedAll: [
          // { name: '项目编码' },
          { name: '三重一大事项编码' },
          { name: '风险审查报告编码' },
          { name: '公司名称' },
          { name: '经办人' },
          { name: '经办部门' },
          { name: '申请时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
      }
    },
    created() {
      this.queryForm.unitname = this.$route.query.company
      this.fetchData()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        console.log('qwe')
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '项目名称', key: 'mattername' },
          { name: '公司名称', key: 'unitname' },
          { name: '三重一大事项编码', key: 'mattercode' },
          { name: '风险审查报告编码', key: 'riskreviewcode' },
          { name: '状态', key: 'state' },
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
        console.log(this.searchMore)
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      /**
       * @description: 下载
       * @return {*}
       */
      async handleDownload(row) {
        const data = await download({ reportid: row.reportid })
        // const data = await downloadTest({
        //   reportType: 'fygk',
        //   // orgId: row.orgid,
        //   orgId: row.reportid,
        // })
        let fileName = row.reportname + '.doc'
        let blob = new Blob([data], {
          // type: 'application/vnd.ms-excel',
          type: 'application/msword',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
      formatDay(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      /**
       * @description: 分页，初始化
       * @return {*}
       */
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 重置数据
       * @return {*}
       */
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      /**
       * @description: 重置
       * @return {*}
       */
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      reloadTable() {
        this.fetchData()
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      fetchData(type) {
        this.listLoading = true
        if (type && type == 'reset') this.$refs['form'].resetFields()

        riskReviewOpinionList({ ...this.queryForm }).then((res) => {
          this.list = res.data.pageInfo.list
          this.total = res.data.pageInfo.total
          this.listLoading = false
        })
      },
      handleExport() {},
      /**
       * @description: 打开编辑
       * @return {*}
       */
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit')
      },
      /**
       * @description: 打开新建
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit({}, 'add')
      },
      /**
       * @description: 打开详情
       * @return {*}
       */
      handleRead(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      /**
       * @description: 删除
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await riskReviewDelete({ reviewId: row.reviewid })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }

  .secondCard {
    margin-top: -5px !important;
  }

  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }

  .pager {
    margin-bottom: 20px !important;
  }
</style>
