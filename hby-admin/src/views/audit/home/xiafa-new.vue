<template>
  <div>
    <message-notification
      :moduleType="moduleType"
      :typeMapping="type"
      :jumpUrlMapping="jumpUrl"
      :jumpInfo="typeToModuleMapping"
      :showHytz="isHytz"
      :showXmpy="isXmpy"
      :hyTotal="hyTotal"
      :xmpyTotal="xmpyTotal"
      :customTypeData="typeData"
      @view-detail="handleViewDetail"
      @confirm-received="handleConfirmReceived"
      @batch-confirm="handleBatchConfirm"
    >
      <!-- 会议通知内容插槽 -->
      <template #hytz-content v-if="isHytz">
        <HYTZ @HYtotal="handleHYtotal"></HYTZ>
      </template>

      <!-- 项目评优内容插槽 -->
      <template #xmpy-content v-if="isXmpy">
        <XMPYTable @XMPYtotal="handleXMPYtotal"></XMPYTable>
      </template>
    </message-notification>

    <!-- 引入所需的弹窗组件 -->
    <NoticeInfo ref="ZNSJSJTZS" />
    <IndexView ref="SJJGWS" />
    <SchemeInfo
      v-if="showSchemeInfo"
      ref="SchemeInfo"
      @closeDialog="closeDialog"
    />
    <ProjectDataInfo ref="ProjectDataInfo" />
    <IndexEdit ref="edit" />
  </div>
</template>

<script>
  import MessageNotification from '@/components/Common/MessageNotification_new.vue'
  import { formatDay } from '@/utils/index'
  import { imPlementOrderDetail } from '@/api/audit/implement'
  import { reportDetail } from '@/api/audit/report'
  import { getRectificationPlanDetail } from '@/api/zgzz/index.js'
  import NoticeInfo from '@/views/audit/prepare/components/NoticeInfo.vue'
  import IndexView from '@/views/audit/report/components/IndexView'
  import SchemeInfo from '@/views/audit/rectify/components/SchemeInfo'
  import ProjectDataInfo from '@/views/audit/prepare/components/ProjectDataInfo.vue'
  import IndexEdit from '@/views/audit/project/components/IndexEditNew.vue'

  // 如果需要会议通知和项目评优组件，请取消下面的注释并导入相应组件
  // import HYTZ from '../home/components/message.vue'
  // import XMPYTable from '../home/components/xmpy/index.vue'

  export default {
    name: 'AuditMessageNotification',
    props: {
      //个人操作台消息通知项目评优是否展示
      isXmpy: {
        type: Boolean,
        default: false,
      },
      //个人操作台消息通知会议通知是否展示
      isHytz: {
        type: Boolean,
        default: false,
      },
      // 智能审计和敏捷审计的动态查询参数
      moduleType: {
        type: String,
        default: 'yqns',
      },
    },
    components: {
      MessageNotification,
      NoticeInfo,
      IndexView,
      SchemeInfo,
      ProjectDataInfo,
      IndexEdit,
      // 如果需要会议通知和项目评优组件，请取消下面的注释
      // HYTZ,
      // XMPYTable,
    },
    data() {
      return {
        hyTotal: 0,
        xmpyTotal: 0,
        type: {
          ZNSJSJTZS: '审计通知',
          SJJGWS: '审计结果文书',
          ZGFA: '整改方案',
          XMZL: '项目资料',
          RWFP: '任务分配',
        },
        jumpUrl: {
          XMZL: '/implement/projectData',
          ZNSJSJTZS: '/implement/notice',
          SJJGWS: '/implement/index',
          ZGFA: '/rectify/scheme',
          RWFP: '/project/index',
        },
        typeData: [
          { textValue: 'ZNSJSJTZS', textName: '审计通知' },
          { textValue: 'SJJGWS', textName: '审计结果文书' },
          { textValue: 'ZGFA', textName: '整改方案' },
          { textValue: 'XMZL', textName: '项目资料' },
          { textValue: 'RWFP', textName: '任务分配' },
        ],
        showSchemeInfo: false,
        typeToModuleMapping: [
          // 整改追踪
          {
            name: '整改追踪',
            moduleType: 'zhjd',
            types: ['ZGFA'],
          },
        ],
      }
    },
    methods: {
      /**
       * 处理查看详情事件
       * @param {Object} row - 行数据
       */
      async handleViewDetail(row) {
        if (row.distributionType == 'ZNSJSJTZS') {
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
            this.$refs['edit'].showEdit({ projectId: row.formId }, true)
          })
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
       * 处理会议通知总数
       * @param {Number} val - 会议通知总数
       */
      handleHYtotal(val) {
        this.hyTotal = val
      },

      /**
       * 处理项目评优总数
       * @param {Number} val - 项目评优总数
       */
      handleXMPYtotal(val) {
        this.xmpyTotal = val
      },

      /**
       * 关闭整改方案弹窗
       */
      closeDialog() {
        this.showSchemeInfo = false
      },
    },
  }
</script>
