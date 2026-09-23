<template>
  <div>
    <message-notification
      :moduleType="moduleType"
      :typeMapping="type"
      :jumpUrlMapping="jumpUrl"
      :jumpInfo="typeToModuleMapping"
      :showHytz="false"
      :showXmpy="false"
      :customTypeData="typeData"
      @view-detail="handleViewDetail"
      @confirm-received="handleConfirmReceived"
      @batch-confirm="handleBatchConfirm"
    >
      <!-- 这里可以添加额外的插槽内容 -->
    </message-notification>

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
  import MessageNotification from '@/components/Common/MessageNotification_new.vue'
  import PlanEdit from '@/views/risk/assessment/plan/components/PlanEdit.vue'
  import RiskCreateEdit from '@/views/risk/riskfill/cetateEdit.vue'
  import GroupPlanView from '@/views/workbench/contractTools/components/GroupPlanView.vue'
  import RiskIndicatorCreation from '@/views/risk/riskfill/components/IndicatorEdit.vue'
  import PlanView from '@/views/internal/internalTest/components/PlanView'
  import TestPlanView from '@/views/internal/internalTest/components/TestPlanView'
  import ProjectView from '@/views/internal/evaluationManagement/components/ProjectView'
  import { getTypeData } from '@/oapi/setting/system'
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
    name: 'RiskMessageNotification',
    components: {
      MessageNotification,
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
      wtdzView,
      lwhjtzEdit,
      sjtzsEdit,
      xmpyhjtzEdit,
      NoticeInfo,
      IndexView,
      SchemeInfo,
      ProjectDataInfo,
      IndexEdit,
    },
    data() {
      return {
        moduleType: '',
        type: {
          // PGJH: '评估计划',
          // ZDFXCJ: '重大风险创建',
          // JTPGJH: '集团评估计划',
          // FXJCZBCJ: '风险监测指标创建',
        },
        jumpUrl: {
          //风险穿透
          PGJH: '/assessment/task',
          ZDFXCJ: '/riskfill/riskfill',
          JTPGJH: '/assessment/plan',
          FXJCZBCJ: '/riskfill/riskIndicatorReporting',
          //内控管理
          CSRW: '/internalTest/task',
          CSFA: '/internalTest/plan',
          JTCSJH: '/internalTest/plan',
          PJLX: '/evaluationManagement/score',
          // 大庆审计
          SJLXJYTZ: '/jhlx/sjlxjytz',
          LXJYB: '/jhlx/lxjyb',
          XQJYB: '/jhlx/xqjyb',
          FGLDHZ: '/jhlx/fgldhz',
          FWXQB: '/jhlx/fwxqb',
          EJDWJCYLRSJ: '/jhlx/lrjyjlrsj',
          WWTJYJLR: '/jhlx/wwtjyjlr',
          SJDWLRSJ: '/jhlx/lrjjzrsq',
          GZFA: '/project/gzfa',
          SJQKB: '审计项目表',
          SJXMZD: '/project/sjxmzd',
          IPQD: '/zhgl/ipqd',
          YXXMPX: '/XMPY/xmpyhz',
          GCSJXMAP: '/project/gcxmrysb',
          CWSJXMAP: '/project/cwxmrysb',
          CWDDFG: '/project/cwddfg',
          GCDDFG: '/project/gcddfg',
          LLYTTZ: '/Pygl/llyttz',
          WTDZ: '/wgzrzj/wtdz',
          HJTZ: '/Pygl/lwhjtz',
          SJTZS: '/project/notice',
          XMPYHJTZ: '/XMPY/hjtz',
          // 智能审计
          XMZL: '/implement/projectData',
          ZNSJSJTZS: '/implement/notice',
          SJJGWS: '/implement/index',
          RWFP: '/project/index',
          // 整改追踪
          ZGFA: '/rectify/scheme',
          ZGLS: '/rectify/scheme',
          ZCGZLS: '/rectify/track',
        },

        // 类型到大模块的映射表
        typeToModuleMapping: [
          {
            name: '风险穿透',
            moduleType: 'fxgk',
            types: ['PGJH', 'ZDFXCJ', 'JTPGJH', 'FXJCZBCJ'],
          },
          // 内控管理模块
          {
            name: '内控管理',
            moduleType: 'nkhg',
            types: ['CSRW', 'CSFA', 'JTCSJH', 'PJLX'],
          },
          // 大庆审计模块
          {
            name: '大庆审计',
            moduleType: 'yqns',
            types: [
              'SJLXJYTZ',
              'LXJYB',
              'XQJYB',
              'FGLDHZ',
              'FWXQB',
              'EJDWJCYLRSJ',
              'WWTJYJLR',
              'SJDWLRSJ',
              'GZFA',
              'SJQKB',
              'SJXMZD',
              'IPQD',
              'YXXMPX',
              'GCSJXMAP',
              'CWSJXMAP',
              'CWDDFG',
              'GCDDFG',
              'LLYTTZ',
              'WTDZ',
              'HJTZ',
              'SJTZS',
              'XMPYHJTZ',
            ],
          },
          // 智能审计模块
          {
            name: '智能审计',
            moduleType: 'znsj',
            types: ['XMZL', 'ZNSJSJTZS', 'SJJGWS', 'RWFP'],
          },
          // 整改追踪
          {
            name: '整改追踪',
            moduleType: 'zhjd',
            types: ['ZGFA', 'ZCGZLS', 'ZGLS'],
          },
        ],
        typeData: [
          // { textValue: 'PGJH', textName: '评估计划' },
          // { textValue: 'ZDFXCJ', textName: '重大风险创建' },
          // { textValue: 'JTPGJH', textName: '集团评估计划' },
          // { textValue: 'FXJCZBCJ', textName: '风险监测指标创建' },
        ],
        showSchemeInfo: false,
        testType: '', // 新增用于测试的类型标识
        currentModuleInfo: null, // 新增用于存储当前模块信息
      }
    },
    created() {
      this.fetchTypeData()
    },
    methods: {
      /**
       * 处理查看详情事件
       * @param {Object} row - 行数据
       */
      handleViewDetail(row) {
        row.formId = (row.formId || '').toString().trim()
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
        } else if (row.distributionType == 'ZGLS') {
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

      /**
       * 处理确认接收事件
       * @param {Object} row - 行数据
       */
      handleConfirmReceived(row) {
        console.log('确认接收成功:', row)
        // 可以在这里添加额外的处理逻辑
      },

      /**
       * 处理批量确认事件
       * @param {Array} rows - 选中的行数据
       */
      handleBatchConfirm(rows) {
        console.log('批量确认成功:', rows)
        // 可以在这里添加额外的处理逻辑
      },
      /**
       * 获取类型数据
       */
      async fetchTypeData() {
        const res = await getTypeData()
        if (res && res.data) {
          this.typeData = res.data
          // 将获取到的数据转换成 PGJH: '评估计划' 这样的结构
          const typeMapping = {}
          this.typeData.forEach((item) => {
            typeMapping[item.textValue] = item.textName
          })
          // 更新 type 对象
          this.type = { ...this.type, ...typeMapping }
        }
      },

      /**
       * 关闭弹窗
       */
      closeDialog() {
        this.showSchemeInfo = false
      },
    },
  }
</script>
