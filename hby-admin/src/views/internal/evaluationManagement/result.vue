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
                v-model="queryForm.assNumnber"
                clearable
                placeholder="评价项目编号"
                v-if="item.name === '评价项目编号'"
              />

              <el-input
                v-model="queryForm.assName"
                clearable
                placeholder="评价项目名称"
                v-if="item.name === '评价项目名称'"
              />
              <el-date-picker
                v-model="queryForm.date"
                clearable
                end-placeholder="结束时间"
                format="yyyy-MM-dd"
                range-separator="-"
                start-placeholder="开始时间"
                type="daterange"
                value-format="yyyy-MM-dd"
                v-if="item.name === '时间'"
              />

              <!-- 单位名称 -->
              <div
                v-if="item.name === '单位名称'"
                style="display: flex; align-items: center"
              >
                <el-input
                  v-model="queryForm.orgName"
                  clearable
                  placeholder="单位名称"
                  disabled
                  style="width: 200px; margin-right: 8px"
                />
                <el-button
                  type="primary"
                  size="small"
                  @click="$refs.departmentSelect.showEdit()"
                >
                  选择
                </el-button>
              </div>
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
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <!--      <el-table-column type="selection" width="55" />-->
        <el-table-column align="center" label="评价项目编号" #default="{ row }">
          <el-button type="text" @click="handleRead(row, true)">
            {{ row.assessid }}
          </el-button>
        </el-table-column>
        <el-table-column width="1" />
        <!-- <template #default="{ row }">
            <el-button
              type="text"
              @click="$refs['ResultskList'].showEdit(row, 'view')"
            >
              {{ row.assessid }}
            </el-button>
          </template> -->
        <!-- </el-table-column> -->
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="评价项目名称"
            prop="assessname"
            v-if="item.name === '评价项目名称'"
          />
          <el-table-column
            align="center"
            label="评价模板"
            prop="templename"
            v-if="item.name === '评价模板'"
          >
            <template #default="{ row }">
              <el-button
                type="text"
                @click="$refs['EvaluateModel'].showEdit(row.asstemid)"
              >
                {{ row.templename }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="评价期限"
            prop="startdate"
            width="220"
            v-if="item.name === '评价期限'"
          >
            <template #default="{ row }">
              {{ row.startdate }} - {{ row.enddate }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="发起日期"
            prop="assstartday"
            v-if="item.name === '发起日期'"
          />
          <el-table-column
            align="center"
            label="总分"
            prop="finalscorenew"
            show-overflow-tooltip
            v-if="item.name === '总分'"
          >
            <template #default="{ row }">
              {{
                row.finalscorenew ? Number(row.finalscorenew).toFixed(2) : ''
              }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="校正级别"
            prop="checklevel"
            show-overflow-tooltip
            v-if="item.name === '校正级别'"
          >
            <template #default="{ row }">
              {{ row.checklevel }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="状态"
            prop="assstatus"
            show-overflow-tooltip
            v-if="item.name === '状态'"
          >
            <template #default="{ row }">
              {{ assstatus[+row.assstatus] }}
            </template>
          </el-table-column>
          <!-- <el-table-column
            align="center"
            label="报告状态"
            prop="status"
            show-overflow-tooltip
            width="80"
            v-if="item.name === '报告状态'"
          >
            <template #default="{ row }">
              {{ status[+row.status] }}
            </template>
          </el-table-column> -->
        </div>

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="180"
        >
          <template #default="{ row }">
            <!-- <el-button type="text" @click="handGenerateReport(row)">
              生成报告
            </el-button>
            <el-button type="text" @click="handPreview(row)">预览</el-button> -->
            <el-button type="text" @click="handleEdit(row)">结果</el-button>
            <el-button
              type="text"
              @click="$refs['ResultskList'].showEdit(row, 'view')"
              v-if="!isUEditor"
            >
              评级校正
            </el-button>
            <el-button @click="copyData(row)" type="text" v-if="isUEditor">
              复制
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      class="pagination"
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <ResultskView ref="edit" @fetch-data="fetchData" />
    <EvaluateModel ref="EvaluateModel" @fetch-data="fetchData" />
    <ResultskList ref="ResultskList" @fetch-data="fetchData" />
    <ProjectView ref="read" @fetch-data="fetchData" />
    <department-select
      ref="departmentSelect"
      @submit="handleDepartmentSelected"
    />
    <department-select
      ref="departmentSelectOrgid"
      @submit="handleOrgidSelected"
    />
  </div>
</template>

<script>
  import {
    getResultList,
    preview,
    riskGenerateReport,
  } from '@/api/internal/result'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import DepartmentSelect from '@/components/departmentSelect.vue'
  import EvaluateModel from '@/views/internal/evaluationManagement/components/EvaluateModel'
  import ResultskList from '@/views/internal/evaluationManagement/components/ResultskList'
  import ResultskView from '@/views/internal/evaluationManagement/components/ResultskView'
  import ProjectView from '@/views/internal/evaluationManagement/components/ProjectView'

  export default {
    name: 'Result',
    //编辑器传入,判断是否有复制按钮
    props: {
      isUEditor: {
        type: Boolean,
        default: false,
      },
    },
    components: {
      EvaluateModel,
      ResultskView,
      ResultskList,
      filterTable,
      filterSearch,
      ProjectView,
      DepartmentSelect,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        assstatus: ['', '已立项', '已启动', '已完成', '已完成'],
        status: ['', '已立项', '已启动', '已完成', '已完成'],
        queryForm: {
          assNumnber: '',
          assName: '',
          orgName: '',
          orgid: '',
          orgName: '',
          unitLevel: '',
          businessField: '',
          businessField1: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '评价项目名称' },
          { name: '评价模板' },
          { name: '评价期限' },
          { name: '发起日期' },
          { name: '总分' },
          { name: '校正级别' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'internal-evaluationManagement-result-search',
        tableKey: 'internal-evaluationManagement-result-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
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
          { name: '评价项目编号', key: 'assNumnber' },
          { name: '评价项目名称', key: 'assName' },
          { name: '单位名称', key: 'orgName' },
          { name: '时间', key: 'Date' },
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
          assName: '',
          assNumnber: '',
          orgName: '',
          orgid: '',
          orgName: '',
          unitLevel: '',
          businessField: '',
          businessField1: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      handleDepartmentSelected(department) {
        this.queryForm.orgName = department.label
        this.queryForm.orgid = department.id
      },
      handleOrgidSelected(department) {
        this.queryForm.orgName = department.label
        this.queryForm.orgid = department.id
      },
      async fetchData() {
        this.listLoading = true
        const { date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (date) {
          startDate = date[0]
          endDate = date[1]
        }
        const {
          data: { pageBean },
        } = await getResultList({ ...other, startDate, endDate })
        this.list = pageBean.records
        this.total = pageBean.total
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit')
      },
      async handGenerateReport(row) {
        const { code, msg } = await riskGenerateReport({
          id: row.assessid,
          reportType: '',
        })
        if (code == '200') {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
      },
      async handPreview(row) {
        const { code, msg, data } = await preview({
          id: row.assid,
          reportType: 'nkhg',
        })
        window.open(
          data.previewurl +
            '?url=' +
            encodeURIComponent(Base64.encode(data.ftpUrl))
        )
      },
      handleRead(row, type) {
        this.$refs['read'].showEdit(row, type)
      },
      //复制
      copyData(row) {
        // 提取需要的字段
        const textToCopy = `评价项目编号：${row.assessid}  评价项目名称：${row.assessname}  评价模板：${row.templename}  发起日期：${row.assstartday}  校正级别：${row.checklevel}`

        // 复制到剪贴板 - 兼容性处理
        if (navigator.clipboard && navigator.clipboard.writeText) {
          // 现代浏览器支持 Clipboard API
          navigator.clipboard
            .writeText(textToCopy)
            .then(() => {
              this.$message.success('复制成功')
            })
            .catch((err) => {
              // this.$message.error('复制失败')
              console.error('复制失败:', err)
              // 降级到传统方法
              this.fallbackCopyTextToClipboard(textToCopy)
            })
        } else {
          // 降级到传统方法
          this.fallbackCopyTextToClipboard(textToCopy)
        }
      },
      // 降级复制方法
      fallbackCopyTextToClipboard(text) {
        const textArea = document.createElement('textarea')
        textArea.value = text

        // 避免在页面上显示
        textArea.style.top = '0'
        textArea.style.left = '0'
        textArea.style.position = 'fixed'
        textArea.style.opacity = '0'

        document.body.appendChild(textArea)
        textArea.focus()
        textArea.select()

        try {
          const successful = document.execCommand('copy')
          if (successful) {
            this.$message.success('复制成功')
          } else {
            console.error('复制失败:', err)
          }
        } catch (err) {
          // this.$message.error('复制失败')
          console.error('复制失败:', err)
        }

        document.body.removeChild(textArea)
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
