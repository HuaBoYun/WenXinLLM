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
                v-model="queryForm.fname"
                placeholder="查询名称"
                v-if="item.name === '查询名称'"
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
      <el-table v-loading="listLoading" :data="list">
        <el-table-column type="index" label="序号" width="80" align="center" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="名称"
            prop="fname"
            v-if="item.name === '名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleRead(row)">
                {{ row.fid ? row.fname : row.sqlconfigname }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="数据源名称"
            prop="datatext"
            v-if="item.name === '数据源名称'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="表单状态"
            prop="fstatus"
            v-if="item.name === '表单状态'"
          >
            <template #default="{ row }">
              {{
                row.fstatus == 1
                  ? '启用'
                  : row.fstatus == 2
                  ? '弃用'
                  : row.fstatus == 3
                  ? '已发布'
                  : '未发布'
              }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="目标端表名"
            prop="oursTableName"
            v-if="item.name === '目标端表名'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="源端表名"
            prop="outsTableName"
            v-if="item.name === '源端表名'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="创建时间"
            prop="createTime"
            v-if="item.name === '创建时间'"
          ></el-table-column>
          <el-table-column
            align="center"
            label="采集状态"
            prop="fname"
            v-if="item.name === '采集状态'"
          >
            <template #default="{ row }">
              {{
                row.brv
                  ? row.brv.iscompleted == 1
                    ? '采集中'
                    : row.brv.iscompleted == 2
                    ? '已完成'
                    : '采集中'
                  : '未采集'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-dropdown trigger="click">
              <el-button type="primary" size="mini">
                操作
                <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="qiyong('启用', row)"
                    :disabled="row.fstatus != 3 && row.fstatus != 2"
                  >
                    启用
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="qiyong('弃用', row)"
                    :disabled="row.fstatus != 1"
                  >
                    弃用
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="fabu(row)"
                    :disabled="
                      row.fstatus == 1 || row.fstatus == 2 || row.fstatus == 3
                    "
                  >
                    发布
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleEdit(row)"
                    :disabled="
                      row.fstatus == 1 || row.fstatus == 3 || row.fstatus == 2
                    "
                  >
                    修改
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleStart(row)"
                    :disabled="
                      !(
                        row.fstatus == 1 &&
                        (!row.brv || row.brv.iscompleted == 2)
                      )
                    "
                  >
                    开始采集
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleStop(row)"
                    :disabled="
                      !(row.brv && row.brv.iscompleted != 2 && row.fstatus == 1)
                    "
                  >
                    停止采集
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleJL(row)"
                    :disabled="!row.brv"
                  >
                    采集记录
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="downloadTemplate(row)"
                    :disabled="
                      !(
                        (row.financeType === 2 || row.financeType === 3) &&
                        row.fstatus === 1
                      )
                    "
                  >
                    下载导入模板
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-upload
                    class="upload-demo"
                    :show-file-list="false"
                    :action="baseApi + api"
                    :headers="headers"
                    :on-success="handleSuccess"
                    :file-list="fileList"
                    :multiple="true"
                    :data="{ tableId: row.fid }"
                    :disabled="
                      !(
                        (row.financeType === 2 || row.financeType === 3) &&
                        row.fstatus === 1
                      )
                    "
                  >
                    <el-button
                      type="text"
                      :disabled="
                        !(
                          (row.financeType === 2 || row.financeType === 3) &&
                          row.fstatus === 1
                        )
                      "
                    >
                      导入数据
                    </el-button>
                  </el-upload>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="importRecord(row)"
                    :disabled="
                      !(
                        (row.financeType === 2 || row.financeType === 3) &&
                        row.fstatus === 1
                      )
                    "
                  >
                    导入记录
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    @click="handleDelete(row)"
                    :disabled="
                      row.fstatus == 1 || row.fstatus == 3 || row.fstatus == 2
                    "
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <edit ref="edit" @fetchData="fetchData" :planId="queryForm.planid"></edit>
    <jl ref="jl" @fetchData="fetchData" :planId="queryForm.planid"></jl>
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
    <!-- 导入记录对话框 -->
    <import-record-modal
      :visible.sync="recordModalVisible"
      :tableId="curTable.fid"
      @close="onRecordClose"
    />
  </div>
</template>

<script>
  import {
    getYWSJList,
    getYWSJDelete,
    executeChouqu,
    YWSJFabu,
    changeStatus,
    startCaiJi,
    stopCaiJi,
  } from '@/api/cwsc'
  import { downloadTemplate } from '@/api/workbench/accountManage'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import edit from './components/ywsjEdit.vue'
  import jl from './components/czjlModal.vue'
  import ImportRecordModal from '@/views/cwsc/jcpz/companyData/components/ImportRecordModal.vue'
  const { baseURL } = require('@/config')
  import store from '@/store'
  export default {
    name: 'NormalReportList',
    components: { filterTable, filterSearch, edit, jl, ImportRecordModal },
    data() {
      return {
        baseApi: baseURL,
        api: '/finance/budata/importTemplateData',
        headers: {
          token: store.getters['user/token'],
        },
        list: [],
        listLoading: true,
        fileList: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        recordModalVisible: false,
        curTable: {},
        queryForm: {
          fname: '',
          planid: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '名称' },
          { name: '数据源名称' },
          { name: '表单状态' },
          { name: '目标端表名' },
          { name: '源端表名' },
          { name: '创建时间' },
          { name: '采集状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'cwsc-jcpz-ywsj-search',
        tableKey: 'cwsc-jcpz-ywsj-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
      }
    },
    created() {
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      // 从url里取参数
      const { query } = this.$route
      if (query.fid) {
        this.queryForm.planid = query.fid
        this.fetchData()
      }
    },
    methods: {
      // 定义表单所有项
      // 定义表单所有项
      getFiled() {
        let fields = [{ name: '查询名称', key: 'fname' }]
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
      // 动态表格开始
      /**
       * @description: 从上一次缓存中初始化表头
       * @return {*}
       */
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
          data: { records, total },
        } = await getYWSJList(this.queryForm)
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleExport() {},
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit')
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleRead(row) {
        this.$refs['edit'].showEdit(row, 'detail')
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除数据吗', null, async () => {
          const res = await getYWSJDelete({ fid: row.fid })
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
          fname: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleView(row) {
        this.$refs['check'].showEdit(row)
      },
      handleChouqu(row) {
        executeChouqu({
          finitPlanid: this.queryForm.planid,
          finitsqlid: row.finitsqlid,
          fid: row.fid ? row.fid : row.sqlconfigid,
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage('执行成功', 'success', 'vab-hey-message-success')
            this.fetchData()
          }
        })
      },
      handleStart(row) {
        startCaiJi({
          fid: row.fid || '',
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage('开始采集', 'success', 'vab-hey-message-success')
            this.fetchData()
          }
        })
      },
      handleStop(row) {
        stopCaiJi({
          fid: row.fid || '',
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage('停止采集', 'success', 'vab-hey-message-success')
            this.fetchData()
          }
        })
      },
      fabu(row) {
        YWSJFabu({
          fid: row.fid || '',
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage('发布成功', 'success', 'vab-hey-message-success')
            this.fetchData()
          }
        })
      },
      qiyong(a, b) {
        changeStatus({
          fstatus: a == '启用' ? 1 : 2,
          fid: b.fid || '',
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage(
              a == '启用' ? '启用成功' : '弃用成功',
              'success',
              'vab-hey-message-success'
            )
            this.fetchData()
          }
        })
      },
      handleJL(row) {
        this.$refs['jl'].show(row)
      },
      async downloadTemplate(row) {
        try {
          const response = await downloadTemplate({
            tableId: row.fid,
          })

          if (response && response.code === 1) {
            // 如果后端返回的是base64编码的数据
            if (response.data && typeof response.data.body === 'string') {
              // 假设文件名包含在响应中，或者使用默认名称
              const fileName = `${row.fname}_模板.xlsx`
              this.downloadBase64Excel(response.data.body, fileName)
            } else if (response.data) {
              // 如果返回的是blob数据，使用原有的下载方式
              let blob = new Blob([response.data], {
                type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
              })
              const fileName =
                response.fileName || `${this.curTable.fname}_模板.xlsx`

              if (window.navigator.msSaveOrOpenBlob) {
                navigator.msSaveBlob(blob, fileName)
              } else {
                let link = document.createElement('a')
                link.href = window.URL.createObjectURL(blob)
                link.download = fileName
                link.click()
                window.URL.revokeObjectURL(link.href)
              }
              this.$message.success('模板下载成功！')
            } else {
              this.$message.error('模板下载失败！')
            }
          } else {
            this.$message.error('模板下载失败！')
          }
        } catch (error) {
          console.error('下载模板失败:', error)
          this.$message.error('模板下载失败！')
        }
      },
      // 下载base64编码的Excel文件
      downloadBase64Excel(base64Data, fileName = 'template.xlsx') {
        try {
          // 将base64字符串转换为二进制数据
          const binaryString = atob(base64Data)
          const bytes = new Uint8Array(binaryString.length)
          for (let i = 0; i < binaryString.length; i++) {
            bytes[i] = binaryString.charCodeAt(i)
          }

          // 创建Blob对象
          const blob = new Blob([bytes], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
          })

          // 创建下载链接
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.download = fileName

          // 触发下载
          document.body.appendChild(link)
          link.click()

          // 清理资源
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)

          this.$message.success('文件下载成功！')
        } catch (error) {
          console.error('下载文件失败:', error)
          this.$message.error('文件下载失败！')
        }
      },
      handleSuccess(response) {
        if (response.code == 1) {
          this.$message.success('数据导入成功！')
        } else {
          this.$message.error('数据导入失败！')
        }
      },
      onRecordClose() {
        this.recordModalVisible = false
      },
      importRecord(row) {
        this.curTable = row
        this.recordModalVisible = true
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
