<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <el-input
                v-model="queryForm.name"
                clearable
                placeholder="报告名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '报告名称'"
              />

              <el-date-picker
                v-model="queryForm.time"
                clearable
                end-placeholder="报告结束日期"
                format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="报告开始日期"
                style="width: 200px; margin-right: 20px"
                type="daterange"
                value-format="yyyy-MM-dd"
                v-if="item.name === '时间'"
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
        <el-table-column align="center" label="报告名称" prop="reportname">
          <template #default="{ row }">
            <el-button
              style="color: red"
              type="text"
              @click="handleDeatil(row)"
            >
              {{ row.reportname }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="报告时间"
            prop="reporttime"
            :formatter="formatDate"
            v-if="item.name === '报告时间'"
          />
          <el-table-column
            align="center"
            label="报告类型"
            prop="reporttype"
            show-overflow-tooltip
            v-if="item.name === '报告类型'"
          />
          <el-table-column
            align="center"
            label="报告方式"
            prop="reportmode"
            show-overflow-tooltip
            v-if="item.name === '报告方式'"
          />
          <el-table-column
            align="center"
            label="状态"
            prop="status"
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{
                row.status == 1
                  ? '审批中'
                  : row.status == 2
                  ? '需调整'
                  : row.status == 3
                  ? '已撤销'
                  : row.status == 4
                  ? '已终止'
                  : row.status == 5
                  ? '已跟踪'
                  : row.status == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>

        <el-table-column align="center" label="操作" show-overflow-tooltip>
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="
                (row.status && row.status != 0) || createId != row.createstaffid
              "
            >
              修改
            </el-button>

            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click.native="handleManage(row)"
                    :disabled="!+row.status"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleApproval(row)"
                    :disabled="
                      !!+row.status ||
                      btnLoading ||
                      createId != row.createstaffid
                    "
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item v-if="row.status == 6">
                  <el-upload
                    class="upload"
                    :show-file-list="false"
                    :action="baseURL + uploadApi"
                    :data="{ reportid: row.reportid, isDecision: 1 }"
                    :headers="headers"
                    :on-success="handleSuccess"
                  >
                    <el-button type="text">上传决策文件</el-button>
                  </el-upload>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    :disabled="!!+row.status || createId != row.createstaffid"
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item></el-dropdown-item>
                <el-dropdown-item>
                  <el-button type="text" @click="handExcel(row)">
                    导出
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
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
    <CustomView ref="edit" @fetch-data="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  // import { getList } from '@/api/systemLog'
  // import { doDelete } from '@/api/table'
  import {
    deleteReport,
    expReportFile,
    getZDYList,
  } from '@/api/internal/report'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { formatDay } from '@/utils/index'
  import CustomView from '@/views/internal/report/components/CustomView'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  export default {
    name: 'Download',
    components: {
      CustomView,
      filterTable,
      filterSearch,
      ProcessList,
      WfqdDeal,
    },
    data() {
      return {
        baseURL: baseURL,
        uploadApi: '/nkhg/nbkz/pjbg/updateupload',
        headers: { token: token },
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          name: '',
          time: [],
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '报告时间' },
          { name: '报告类型' },
          { name: '报告方式' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'internal-report-custom-search',
        tableKey: 'internal-report-custom-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        btnLoading: false,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        console.log('qwe')
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      handleApproval(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        try {
          this.btnLoading = true
          //提交审批
          this.$refs['process'].save(45, row.reportid)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.reportid,
          tableId: 45,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '报告名称', key: 'name' },
          { name: '时间', key: 'time' },
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
        console.log(this.searchMore)
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
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
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
          name: '',
          time: [],
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.btnLoading = false
        this.listLoading = true
        const { time, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (time.length > 0) {
          startDate = time[0]
          endDate = time[1]
        }
        const {
          data: {
            pageBean: { records, total },
          },
        } = await getZDYList({ ...other, startDate, endDate })
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleDeatil(row) {
        this.$refs['edit'].showEdit(row, 'deatil')
      },
      handleEdit(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$refs['edit'].showEdit(row, 'edit')
      },
      handleDelete(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteReport({ ids: row.reportid })
          if (code == 200) {
            this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error', 'vab-hey-message-error')
          }
        })
      },
      async handExcel(row) {
        const data = await expReportFile({
          reportid: row.reportid,
        })
        if (data) {
          let fileName = row.reportname + '.doc'
          let blob = new Blob([data])
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
        } else {
          this.$baseMessage(
            row.reportname + '无内容',
            'error',
            'vab-hey-message-error'
          )
        }
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleSuccess(res, file, c) {
        console.log('res', res)
        if (res.code === 200) {
          this.$message.success('上传成功！')
        } else {
          this.$message.error('系统错误！')
        }
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
</style>
