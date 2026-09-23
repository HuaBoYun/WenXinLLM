<template>
  <div class="">
    <h3>{{ selectedTableType }}</h3>
    <template v-if="selectedTableType == '实施方案'">
      <Ssfa ref="ssfa" :projectid="projectid" />
    </template>
    <template v-else-if="selectedTableType == '审计任务清单'">
      <Guide :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '我的任务'">
      <Wdrw :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '项目资料'">
      <Xmzl :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '系统配置'">
      <Xtpz :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '审计通知'">
      <Sjtz :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '通知变更'">
      <Tzbg :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '审计承诺书'">
      <Sjcns :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '我的底稿'">
      <Wddg :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '底稿管理'">
      <Dggl :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '我的工程任务'">
      <Wdgcrw :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '审计结果确认单'">
      <Sjjgqrd :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '审计项目追款'">
      <Sjxmzk :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '审计工作记录'">
      <Sjgzjl :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '审计发现'">
      <Sjfx :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '审计督导任务'">
      <Sjddjl :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '现场审查主要内容'">
      <Xcsczynr :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '审计项目运行情况表'">
      <Sjxmqkb :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '质量分析报告'">
      <Zlfxbg :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '交换意见稿'">
      <Jhyjg :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '审理报告'">
      <Slbg :isShow="false" :projectId="projectid" />
    </template>
    <template v-else-if="selectedTableType == '审计报告定稿'">
      <Sjbgdg :isShow="false" :projectId="projectid" />
    </template>
  </div>
</template>
<script>
  import Guide from '@/views/oilAudit/prepare/guide.vue'
  import Wdrw from '@/views/oilAudit/implement/wdrw.vue'
  import Ssfa from '@/views/oilAudit/data/components/IndexEdit.vue'
  import { getCurrSsProject } from '@/oapi/audit/project'

  export default {
    components: {
      Ssfa,
      Guide,
      Wdrw,
      Xmzl: () => import('@/views/oilAudit/prepare/projectData'), //项目资料
      Xtpz: () => import('@/views/oilAudit/prepare/auditData'), //系统配置
      Sjtz: () => import('@/views/oilAudit/prepare/notice'), //审计通知
      Tzbg: () => import('@/views/oilAudit/prepare/notice_biangeng'), //通知变更
      Sjcns: () => import('@/views/oilAudit/implement/guide'), //审计承诺书
      Wddg: () => import('@/views/oilAudit/implement/newMyDraft'), //我的底稿
      Dggl: () => import('@/views/oilAudit/implement/draftManage'), //底稿管理
      Wdgcrw: () => import('@/views/oilAudit/implement/task'), //我的工程任务
      Sjjgqrd: () => import('@/views/oilAudit/implement/sjjgqrd'), //审计结果确认单
      Sjxmzk: () => import('@/views/oilAudit/report/sjxmzk'), //审计项目追款
      Sjgzjl: () => import('@/views/oilAudit/implement/sjgzjl'), //审计工作记录
      Sjfx: () => import('@/views/oilAudit/implement/discover'), //审计发现
      Sjddjl: () => import('@/views/oilAudit/implement/sjddjl'), //审计督导任务
      Xcsczynr: () => import('@/views/oilAudit/implement/xcsczynr'), //现场审查主要内容
      Sjxmqkb: () => import('@/views/oilAudit/implement/sjxmqkb'), //审计项目运行情况表
      Zlfxbg: () => import('@/views/oilAudit/report/zlfxbg'), //质量分析报告
      Jhyjg: () => import('@/views/oilAudit/report/custom'), //交换意见稿
      Slbg: () => import('@/views/oilAudit/report/slbg'), //审理报告
      Sjbgdg: () => import('@/views/oilAudit/report/suggest'), //审计报告定稿
    },
    props: {
      nodeName: {
        type: String,
        default: '',
      },
      targetId: {
        type: [Number, String],
        default: '',
      },
      //档案传入
      projectId: {
        type: [Number, String],
        default: undefined,
      },
    },
    watch: {
      nodeName(val) {
        this.selectedTableType = val
      },
    },
    data() {
      return {
        loading: false,
        selectedTableType: '',
        projectid: null,
      }
    },
    async mounted() {
      if (this.$route.path == '/implement/look') {
        this.getCurrentProject()
      } else {
        this.projectid = this.projectId
      }
      this.selectedTableType = '实施方案'
    },
    methods: {
      // 获取当前实施项目
      async getCurrentProject() {
        const { data } = await getCurrSsProject()
        this.projectid = data.pj.id
      },
    },
  }
</script>
<style lang="scss" scoped></style>
