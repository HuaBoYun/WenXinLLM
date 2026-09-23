<template>
  <el-dialog
    title="责任清单预览"
    :visible.sync="dialogVisible"
    width="95%"
    :close-on-click-modal="false"
    class="preview-dialog"
    @close="handleClose"
    @open="handleOpen"
    top="3vh"
  >
    <!-- 筛选条件区域 -->
    <el-card shadow="never" style="margin-bottom: 20px">
      <el-form
        ref="form"
        :inline="true"
        label-width="0"
        :model="queryForm"
        @submit.native.prevent
        style="margin-bottom: -18px"
      >
        <el-form-item>
          <el-input
            v-model="queryForm.issuesCode"
            clearable
            placeholder="问题编号"
          />
        </el-form-item>

        <el-form-item>
          <el-input
            v-model="queryForm.issuesName"
            clearable
            placeholder="问题名称"
          />
        </el-form-item>

        <el-form-item>
          <el-input
            v-model="queryForm.oneorgname"
            clearable
            placeholder="一级单位"
          />
        </el-form-item>

        <el-form-item>
          <el-input
            v-model="queryForm.auditObjectName"
            clearable
            placeholder="具体责任单位"
          />
        </el-form-item>

        <el-form-item>
          <el-input
            v-model="queryForm.problemsrc"
            clearable
            placeholder="问题来源"
          />
        </el-form-item>

        <el-form-item>
          <el-date-picker
            v-model="queryForm.reportyear"
            placeholder="审计报告出具年份"
            type="year"
            format="yyyy"
            value-format="yyyy"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="queryForm.problemtype"
            placeholder="问题类别"
            style="width: 100%"
          >
            <el-option
              v-for="(item, index) in questionTypeList"
              :key="index"
              :label="item.auditType"
              :value="item.auditType"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-select
            v-model="queryForm.recttype"
            placeholder="整改类型"
            style="width: 100%"
          >
            <el-option label="立行立改" value="立行立改" />
            <el-option label="分阶段整改" value="分阶段整改" />
            <el-option label="持续整改" value="持续整改" />
          </el-select>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button icon="el-icon-search" type="primary" @click="handleSearch">
            查询
          </el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleExport">
            导出责任清单
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-table
      :data="previewData"
      border
      style="width: 100%"
      v-loading="loading"
      height="50vh"
    >
      <!-- 单位名称 -->
      <el-table-column label="单位名称" align="center">
        <el-table-column
          prop="oneorgname"
          label="一级单位"
          width="120"
          align="center"
        />
        <el-table-column
          prop="auditObjectName"
          label="具体责任单位"
          width="150"
          align="center"
        />
      </el-table-column>

      <!-- 问题来源 -->
      <el-table-column
        prop="problemsrc"
        label="问题来源"
        width="120"
        align="center"
      />

      <!-- 审计报告出具年份 -->
      <el-table-column
        prop="reportyear"
        label="审计报告出具年份"
        width="120"
        align="center"
      />

      <!-- 问题类别 -->
      <el-table-column
        prop="problemtype"
        label="问题类别"
        width="120"
        align="center"
      />

      <!-- 问题在审计报告中的序号及表述 -->
      <el-table-column
        prop="reportexpression"
        label="问题在审计报告中的序号及表述"
        width="250"
        align="center"
        show-overflow-tooltip
      />

      <!-- 整改责任清单 -->
      <el-table-column label="整改责任清单" align="center">
        <el-table-column
          prop="auditObjectName"
          label="具体责任单位"
          width="150"
          align="center"
        />
        <el-table-column
          prop="queexpression"
          label="具体问题表述"
          width="200"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column
          prop="quemoney"
          label="问题金额（万元）"
          width="120"
          align="center"
        />
        <el-table-column
          prop="supervision"
          label="负责监督管理责任的主管部门"
          width="180"
          align="center"
          show-overflow-tooltip
        />
      </el-table-column>

      <!-- 整改目标清单 -->
      <el-table-column label="整改目标清单" align="center">
        <el-table-column
          prop="recttype"
          label="整改类型"
          width="100"
          align="center"
        />
        <el-table-column
          prop="legalbasis"
          label="法规政策依据"
          width="150"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column
          prop="rectdemand"
          label="整改要求"
          width="150"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column
          prop="recttimelimit"
          label="整改时限"
          width="120"
          align="center"
        />
      </el-table-column>

      <!-- 具体责任单位细化的整改要求 -->
      <el-table-column label="具体责任单位细化的整改要求" align="center">
        <el-table-column
          prop="recttype"
          label="整改完成标准"
          width="150"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column
          prop="rectificationMeasures"
          label="细化的整改措施"
          width="180"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column
          prop="deadline"
          label="对应的完成时间"
          width="120"
          align="center"
        >
          <template #default="{ row }">
            {{ row.deadline ? formatDate(row, { property: 'deadline' }) : '' }}
          </template>
        </el-table-column>
      </el-table-column>

      <!-- 具体责任单位整改责任人 -->
      <el-table-column label="具体责任单位整改责任人" align="center">
        <el-table-column
          prop="firstresponstaffname"
          label="整改第一责任人"
          width="120"
          align="center"
        />
        <el-table-column
          prop="assistleader"
          label="协助整改工作的领导"
          width="150"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column
          prop="maindeptheadtel"
          label="牵头整改部门责任人及联系电话"
          width="200"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column
          prop="assistdeptheadtel"
          label="配合整改部门责任人及联系电话"
          width="200"
          align="center"
          show-overflow-tooltip
        />
        <el-table-column
          prop="auditdeptheadtel"
          label="审计部门责任人及联系电话"
          width="200"
          align="center"
          show-overflow-tooltip
        />
      </el-table-column>

      <!-- 是否已完成整改 -->
      <el-table-column
        prop="conclusion"
        label="是否已完成整改"
        width="120"
        align="center"
      />
    </el-table>

    <!-- 翻页组件 -->
    <el-pagination
      background
      :current-page="queryForm.pageNum"
      layout="total, sizes, prev, pager, next, jumper"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
      style="margin-top: 20px; text-align: center"
    />
  </el-dialog>
</template>

<script>
  import { formatDay } from '@/utils/index'
  import {
    getMyRectificationLedgerList,
    exportZRList,
  } from '@/api/zgzz/index.js'
  import { getSJWTTypeDatas } from '@/api/audit/implement'

  export default {
    name: 'ResponsibilityPreviewDialog',
    props: {
      visible: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        previewData: [],
        loading: false,
        total: 0,
        questionTypeList: [],
        queryForm: {
          issuesCode: '',
          issuesName: '',
          oneorgname: '',
          auditObjectName: '',
          problemsrc: '',
          reportyear: '',
          problemtype: '',
          recttype: '',
          pageNum: 1,
          pageSize: 20,
        },
      }
    },
    computed: {
      dialogVisible: {
        get() {
          return this.visible
        },
        set(val) {
          this.$emit('update:visible', val)
        },
      },
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      handleClose() {
        this.$emit('update:visible', false)
        this.$emit('close')
      },
      handleOpen() {
        // 弹窗打开时加载数据和问题类型
        this.fetchData()
        this.getQuestionType()
      },
      // 获取问题类型数据
      async getQuestionType() {
        try {
          const { data } = await getSJWTTypeDatas()
          this.questionTypeList = data.data
        } catch (error) {
          console.error('获取问题类型失败:', error)
        }
      },
      // 处理查询
      handleSearch() {
        this.queryForm.pageNum = 1
        this.fetchData()
      },
      // 处理重置
      handleReset() {
        this.queryForm = {
          issuesCode: '',
          issuesName: '',
          oneorgname: '',
          auditObjectName: '',
          problemsrc: '',
          reportyear: '',
          problemtype: '',
          recttype: '',
          pageNum: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      // 处理导出
      async handleExport() {
        try {
          this.loading = true
          const obj = { ...this.queryForm }
          const data = await exportZRList(obj)
          const filename = '责任清单.xlsx'

          let blob = new Blob([data])
          let url = window.URL.createObjectURL(blob, {
            type: 'application/vnd.ms-excel',
          })
          const link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', filename)
          document.documentElement.appendChild(link)
          link.click()
          document.documentElement.removeChild(link)

          this.$message.success('导出成功')
        } catch (error) {
          this.$message.error('导出失败')
        } finally {
          this.loading = false
        }
      },
      async fetchData() {
        this.loading = true
        try {
          const {
            data: { tlist, totalRecord },
          } = await getMyRectificationLedgerList(this.queryForm)
          this.previewData = tlist.map((x) => {
            const { issues, reimpl, status, ...other } = x
            return {
              ...other,
              ...issues,
              ...reimpl,
              implId: other.implId,
              rectificationPlan: other.rectificationPlan,
              deadline: other.deadline ? new Date(other.deadline) : '',
            }
          })
          this.total = totalRecord
        } catch (error) {
          this.$message.error('获取数据失败')
        } finally {
          this.loading = false
        }
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNum = val
        this.fetchData()
      },
    },
  }
</script>

<style scoped lang="scss">
  // 预览弹窗样式
  .preview-dialog {
    :deep(.el-dialog__body) {
      padding: 20px;
      max-height: 90vh;
    }

    .preview-content {
      .preview-actions {
        text-align: center;
        padding: 10px 0;
        border-bottom: 1px solid #ebeef5;
      }

      .el-table {
        font-size: 12px;

        :deep(.el-table__header-wrapper) {
          .el-table__header {
            th {
              background-color: #f5f7fa;
              font-weight: bold;
              text-align: center;
              padding: 8px 0;

              .cell {
                padding: 0 5px;
                word-break: break-all;
              }
            }
          }
        }

        :deep(.el-table__body-wrapper) {
          .el-table__body {
            td {
              padding: 6px 0;

              .cell {
                padding: 0 5px;
                word-break: break-all;
                line-height: 1.4;
              }
            }
          }
        }
      }
    }
  }
</style>
