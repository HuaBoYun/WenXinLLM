<template>
  <div class="yysx-container">
    <h3 v-if="showTitle" class="page-title">已阅事项</h3>

    <!-- 查询表单 -->
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
            <el-form-item>
              <el-select
                v-model="queryForm.distributionType"
                placeholder="请选择类型"
                clearable
              >
                <el-option
                  v-for="item in typeData"
                  :key="item.textValue"
                  :label="item.textName"
                  :value="item.textValue"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                type="primary"
                @click="fetchData"
              >
                查询
              </el-button>
              <el-button type="primary" @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>

    <!-- 数据列表 -->
    <el-table v-loading="listLoading" border :data="list" style="width: 100%">
      <el-table-column
        align="center"
        label="序号"
        width="60"
        type="index"
      ></el-table-column>
      <el-table-column
        align="center"
        label="通知内容"
        prop="distributionTitle"
      ></el-table-column>
      <el-table-column align="center" label="类型" prop="distributionType">
        <template #default="{ row }">
          <span>
            {{ getTypeName(row.distributionType) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="下发人"
        prop="createStaffName"
      ></el-table-column>
      <el-table-column
        align="center"
        label="下发时间"
        prop="createTime"
        show-overflow-tooltip
        :formatter="formatDate"
      />
      <el-table-column align="center" label="状态" prop="isread" width="80">
        <template #default="{ row }">
          <el-tag :type="row.isread === '1' ? 'success' : 'warning'">
            {{ '已读' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleViewDetail(row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <!-- 引入所需的弹窗组件 -->
    <PlanEdit ref="pgjh" />
    <RiskCreateEdit ref="zdfxcj" />
    <GroupPlanView ref="groupPlan" />
    <RiskIndicatorCreation ref="riskIndicatorCreation" />
    <PlanView ref="csfa" />
    <TestPlanView ref="testplan" menuKey="TestPlan" />
    <ProjectView ref="project" />
    <sjlxjytzEdit ref="sjlxjytzEdit" />
    <lxjybEdit ref="lxjybEdit" />
    <fgldhzEdit ref="fgldhzEdit" />
    <xqjybEdit ref="xqjybEdit" />
    <fwxqbEdit ref="fwxqbEdit" />
    <lrjyjlrsjView ref="lrjyjlrsjView" />
    <wwtjyjlrView ref="wwtjyjlrView" />
    <lrjjzrsqView ref="lrjjzrsqView" />
    <gzxfView ref="gzxfView" />
    <sjqkbView ref="sjqkbView" />
    <sjxmzdView ref="sjxmzdView" />
    <ipqdView ref="ipqdView" />
    <yxxmpxView ref="yxxmpxView" />
    <gcsjxmapView ref="gcsjxmapView" />
    <cwsjxmapView ref="cwsjxmapView" />
    <llyttzView ref="llyttzView" />
    <wtdzView ref="wtdzView" />
    <lwhjtzEdit ref="lwhjtzEdit" />
    <sjtzsEdit ref="sjtzsEdit" />
    <xmpyhjtzEdit ref="xmpyhjtzEdit" />
    <!-- 引入所需的弹窗组件 -->
    <NoticeInfo ref="ZNSJSJTZS" />
    <IndexView ref="SJJGWS" />
    <SchemeInfo
      v-if="showSchemeInfo"
      ref="SchemeInfo"
      @closeDialog="closeDialog"
    />
    <ProjectDataInfo ref="ProjectDataInfo" />
    <IndexEdit ref="RWFP" />
  </div>
</template>

<script>
  import { formatDay } from '@/utils/index'
  import { getDistributionListPage } from '@/oapi/setting/system'
  import { getTypeData } from '@/oapi/setting/system'
  import PlanEdit from '@/views/risk/assessment/plan/components/PlanEdit.vue'
  import RiskCreateEdit from '@/views/risk/riskfill/cetateEdit.vue'
  import GroupPlanView from '@/views/workbench/contractTools/components/GroupPlanView.vue'
  import RiskIndicatorCreation from '@/views/risk/riskfill/components/IndicatorEdit.vue'
  import PlanView from '@/views/internal/internalTest/components/PlanView'
  import TestPlanView from '@/views/internal/internalTest/components/TestPlanView'
  import ProjectView from '@/views/internal/evaluationManagement/components/ProjectView'
  import sjlxjytzEdit from '@/views/oilAudit/jhlx/components/sjlxjytzEdit'
  import lxjybEdit from '@/views/oilAudit/jhlx/components/lxjybEdit'
  import fgldhzEdit from '@/views/oilAudit/jhlx/components/fgldhzEdit'
  import xqjybEdit from '@/views/oilAudit/jhlx/components/xqjybEdit'
  import fwxqbEdit from '@/views/oilAudit/jhlx/components/fwxqbEdit'
  import lrjyjlrsjView from '@/views/oilAudit/lrjjzr/components/lrjyjlrsjView'
  import wwtjyjlrView from '@/views/oilAudit/lrjjzr/components/wwtjyjlrView'
  import lrjjzrsqView from '@/views/oilAudit/lrjjzr/components/lrjjzrsqJdView.vue'
  import gzxfView from '@/views/oilAudit/plan/components/gzfaView.vue'
  import sjqkbView from '@/views/oilAudit/project/components/auditProjectEdit.vue'
  import sjxmzdView from '@/views/oilAudit/plan/components/sjxmzdView.vue'
  import ipqdView from '@/views/oilAudit/zhgl/components/ipqdView.vue'
  import yxxmpxView from '@/views/oilAudit/xmpy/xmpyhz/edit.vue'
  import wtdzView from '@/views/oilAudit/wgzrzj/components/wtdzView.vue'
  import gcsjxmapView from '@/views/oilAudit/jhlx/components/gcsjxmapbEdit.vue'
  import cwsjxmapView from '@/views/oilAudit/jhlx/components/cwsjxmapbEdit.vue'
  import llyttzView from '@/views/oilAudit/lwpy/components/llyttzEdit.vue'
  import lwhjtzEdit from '@/views/oilAudit/lwpy/components/lwhjtzEdit.vue'
  import sjtzsEdit from '@/views/oilAudit/prepare/components/NoticeInfo.vue'
  import xmpyhjtzEdit from '@/views/oilAudit/xmpy/hjtz/edit.vue'
  import NoticeInfo from '@/views/audit/prepare/components/NoticeInfo.vue'
  import IndexView from '@/views/audit/report/components/IndexView'
  import SchemeInfo from '@/views/audit/rectify/components/SchemeInfo'
  import ProjectDataInfo from '@/views/audit/prepare/components/ProjectDataInfo.vue'
  import IndexEdit from '@/views/audit/project/components/IndexEditNew.vue'
  import { reportDetail } from '@/api/audit/report'
  import { getRectificationPlanDetail } from '@/api/zgzz/index.js'
  export default {
    name: 'Yysx',
    components: {
      PlanEdit,
      RiskCreateEdit,
      GroupPlanView,
      RiskIndicatorCreation,
      PlanView,
      TestPlanView,
      ProjectView,
      sjlxjytzEdit,
      lxjybEdit,
      fgldhzEdit,
      xqjybEdit,
      fwxqbEdit,
      lrjyjlrsjView,
      wwtjyjlrView,
      lrjjzrsqView,
      gzxfView,
      sjqkbView,
      sjxmzdView,
      ipqdView,
      yxxmpxView,
      gcsjxmapView,
      cwsjxmapView,
      llyttzView,
      sjtzsEdit,
      xmpyhjtzEdit,
      NoticeInfo,
      IndexView,
      SchemeInfo,
      ProjectDataInfo,
      IndexEdit,
    },
    props: {
      showTitle: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          isread: '1', // 只显示已读消息
          moduleType: '',
          pageNumber: 1,
          pageSize: 10,
          distributionType: '',
        },
        typeData: [
          // { textValue: 'PGJH', textName: '评估计划' },
          // { textValue: 'ZDFXCJ', textName: '重大风险创建' },
          // { textValue: 'JTPGJH', textName: '集团评估计划' },
          // { textValue: 'FXJCZBCJ', textName: '风险监测指标创建' },
        ],
        showSchemeInfo: false,
      }
    },

    created() {
      this.fetchData()
      this.fetchTypeData()
    },

    methods: {
      /**
       * 获取数据
       */
      async fetchData() {
        this.listLoading = true
        try {
          const {
            data: { tlist, totalRecord },
          } = await getDistributionListPage(this.queryForm)
          this.list = tlist || []
          this.total = totalRecord || 0
        } catch (error) {
          console.error('获取数据失败:', error)
          this.$message.error('获取数据失败，请稍后重试')
        } finally {
          this.listLoading = false
        }
      },

      /**
       * 重置表单
       */
      resetForm() {
        this.queryForm = {
          isread: '1',
          moduleType: 'fxgk',
          pageNumber: 1,
          pageSize: 20,
          distributionType: '',
        }
        this.fetchData()
      },

      /**
       * 查看消息
       * @param {Object} row - 行数据
       */
      handleView(row) {
        this.$message.info(`查看消息：${row.distributionTitle}`)
        // 这里可以添加查看详情的逻辑
      },

      /**
       * 获取类型名称
       * @param {String} type - 类型值
       * @returns {String} 类型名称
       */
      getTypeName(type) {
        const typeItem = this.typeData.find((item) => item.textValue === type)
        return typeItem ? typeItem.textName : '未知类型'
      },

      /**
       * 格式化日期
       * @param {Object} row - 行数据
       * @param {Object} column - 列配置
       * @returns {String} 格式化后的日期字符串
       */
      formatDate(row, column) {
        let data = row[column.property]
        return formatDay(data)
      },

      /**
       * 页码变化
       * @param {Number} val - 页码
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },

      /**
       * 页面大小变化
       * @param {Number} val - 页面大小
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      handleViewDetail(row) {
        if (row.distributionType == 'PGJH') {
          this.$refs['pgjh'].showEdit({ assplanid: row.formId }, true)
        } else if (row.distributionType == 'JTPGJH') {
          this.$refs['groupPlan'].showEdit({ id: row.formId }, 'detail')
        } else if (row.distributionType == 'FXJCZBCJ') {
          this.$refs['riskIndicatorCreation'].showEdit(
            { id: row.formId },
            'detail'
          )
        } else if (row.distributionType == 'ZDFXCJ') {
          this.$refs['zdfxcj'].showEdit({ id: row.formId }, 'detail')
        } else if (row.distributionType == 'JTCSJH') {
          this.$refs['testplan'].showEdit({ id: row.formId }, true)
        } else if (row.distributionType == 'PJLX') {
          this.$refs['project'].showEdit({ assid: row.formId }, true)
        } else if (row.distributionType == 'CSFA') {
          this.$refs['csfa'].showEdit({ testplanid: row.formId }, true)
        } else if (row.distributionType == 'SJLXJYTZ') {
          this.$refs['sjlxjytzEdit'].showEdit({ id: row.formId }, true)
        } else if (row.distributionType == 'LXJYB') {
          this.$refs['lxjybEdit'].showEdit({ id: row.formId }, true)
        } else if (row.distributionType == 'FGLDHZ') {
          this.$refs['fgldhzEdit'].showEdit({ fgldhzid: row.formId }, true)
        } else if (row.distributionType == 'XQJYB') {
          this.$refs['xqjybEdit'].showEdit({ id: row.formId }, true)
        } else if (row.distributionType == 'FWXQB') {
          this.$refs['fwxqbEdit'].showEdit({ id: row.formId }, true)
        } else if (row.distributionType == 'EJDWJCYLRSJ') {
          this.$refs['lrjyjlrsjView'].showEdit({ id: row.formId }, true)
        } else if (row.distributionType == 'WWTJYJLR') {
          this.$refs['wwtjyjlrView'].showEdit({ id: row.formId }, true)
        } else if (row.distributionType == 'SJDWLRSJ') {
          this.$refs['lrjjzrsqView'].showEdit({ jdid: row.formId }, 'detail')
        } else if (row.distributionType == 'GZFA') {
          this.$refs['gzxfView'].showEdit({ gzfaid: row.formId }, 'detail')
        } else if (row.distributionType == 'SJQKB') {
          this.$refs['sjqkbView'].showEdit({ sjxmbid: row.formId }, 'detail')
        } else if (row.distributionType == 'SJXMZD') {
          this.$refs['sjxmzdView'].showEdit({ sjxmzdid: row.formId }, 'detail')
        } else if (row.distributionType == 'IPQD') {
          this.$refs['ipqdView'].showEdit({ id: row.formId }, 'detail')
        } else if (row.distributionType == 'YXXMPX') {
          this.$refs['yxxmpxView'].showEdit('detail', { id: row.formId })
        } else if (row.distributionType == 'GCSJXMAP') {
          this.$refs['gcsjxmapView'].showEdit({ id: row.formId }, 'detail')
        } else if (row.distributionType == 'CWSJXMAP') {
          this.$refs['cwsjxmapView'].showEdit({ id: row.formId }, 'detail')
        } else if (row.distributionType == 'CWDDFG') {
          this.$refs['cwsjxmapView'].showEdit({ id: row.formId }, 'detail')
        } else if (row.distributionType == 'GCDDFG') {
          this.$refs['gcsjxmapView'].showEdit({ id: row.formId }, 'detail')
        } else if (row.distributionType == 'LLYTTZ') {
          this.$refs['llyttzView'].showEdit('detail', { ryid: row.formId })
        } else if (row.distributionType == 'WTDZ') {
          this.$refs['wtdzView'].show('详情', { id: row.formId })
        } else if (row.distributionType == 'HJTZ') {
          this.$refs['lwhjtzEdit'].show('详情', { ryid: row.formId })
        } else if (row.distributionType == 'SJTZS') {
          this.$refs['sjtzsEdit'].showEdit({ adviceid: row.formId }, true)
        } else if (row.distributionType == 'XMPYHJTZ') {
          this.$refs['xmpyhjtzEdit'].show('详情', { ryid: row.formId })
        } else if (row.distributionType == 'ZNSJSJTZS') {
          this.$nextTick(async () => {
            this.$refs['ZNSJSJTZS'].showEdit({ adviceid: row.formId }, true)
          })
        } else if (row.distributionType == 'SJJGWS') {
          this.$nextTick(async () => {
            const data = await reportDetail({ reportid: row.formId })
            await this.$refs['SJJGWS'].showEdit('detail', data.data)
          })
        } else if (row.distributionType == 'ZGFA') {
          this.$nextTick(async () => {
            this.showSchemeInfo = true
            this.$nextTick(async () => {
              const res = await getRectificationPlanDetail({
                planId: row.formId,
              })
              res.data.showModels = { reimpl: true, valua: true }
              await this.$refs['SchemeInfo'].showEdit('detail', res.data)
            })
          })
        } else if (row.distributionType == 'XMZL') {
          this.$nextTick(async () => {
            this.$refs['ProjectDataInfo'].showEdit({ id: row.formId }, true)
          })
        } else if (row.distributionType == 'RWFP') {
          this.$nextTick(async () => {
            this.$refs['RWFP'].showEdit({ projectId: row.formId }, true)
          })
        } else if (row.distributionType == 'ZCGZLS') {
          this.showSchemeInfo = true
          this.$nextTick(async () => {
            const res = await getRectificationPlanDetail({
              planId: row.formId,
            })
            res.data.showModels = { reimpl: true, valua: true }
            await this.$refs['SchemeInfo'].showEdit('detail', res.data)
          })
        } else {
          this.$message.error('未找到对应的详情')
        }
      },
      async fetchTypeData() {
        const res = await getTypeData()
        if (res && res.data) {
          this.typeData = res.data
        }
      },
      closeDialog() {
        this.showSchemeInfo = false
      },
    },
  }
</script>

<style scoped>
  .yysx-container {
    padding: 20px;
  }

  .page-title {
    font-size: 18px;
    margin: 0 0 20px 0;
    color: #333;
    font-weight: bold;
  }

  /* 响应式设计 */
  @media (max-width: 768px) {
    .yysx-container {
      padding: 10px;
    }

    .page-title {
      font-size: 16px;
      margin-bottom: 15px;
    }
  }
</style>
