<template>
  <div>
    <message-notification
      :moduleType="moduleType"
      :typeMapping="type"
      :jumpUrlMapping="jumpUrl"
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
    <PlanView ref="edit" />
    <TestPlanView ref="testplan" menuKey="TestPlan" />
    <ProjectView ref="project" />
    <issueView ref="wttz" />
  </div>
</template>

<script>
  import MessageNotification from '@/components/Common/MessageNotification.vue'
  import PlanView from '@/views/internal/internalTest/components/PlanView'
  import TestPlanView from '@/views/internal/internalTest/components/TestPlanView'
  import ProjectView from '@/views/internal/evaluationManagement/components/ProjectView'
  import issueView from '@/views/internal/internalTest/components/issueView.vue'

  export default {
    name: 'InternalMessageNotification',
    components: {
      MessageNotification,
      PlanView,
      TestPlanView,
      ProjectView,
      issueView,
    },
    data() {
      return {
        moduleType: 'nkhg',
        type: {
          CSFA: '处室任务分派',
          CSRW: '测试方案',
          JTCSJH: '集团测试计划',
          PJLX: '评价立项',
          WTTZ: '问题台账',
        },
        jumpUrl: {
          CSRW: '/internalTest/task',
          CSFA: '/internalTest/plan',
          JTCSJH: '/internalTest/plan',
          PJLX: '/evaluationManagement/score',
          WTTZ: '/internalTest/wttz',
        },
        typeData: [
          { textValue: 'CSRW', textName: '测试方案' },
          { textValue: 'CSFA', textName: '处室任务分派' },
          { textValue: 'JTCSJH', textName: '集团测试计划' },
          { textValue: 'PJLX', textName: '评价立项' },
          { textValue: 'WTTZ', textName: '问题台账' },
        ],
      }
    },
    methods: {
      /**
       * 处理查看详情事件
       * @param {Object} row - 行数据
       */
      handleViewDetail(row) {
        if (row.distributionType == 'JTCSJH') {
          this.$refs['testplan'].showEdit({ id: row.formId }, true)
        } else if (row.distributionType == 'PJLX') {
          this.$refs['project'].showEdit({ assid: row.formId }, true)
        } else if (row.distributionType == 'WTTZ') {
          this.$refs['wttz'].show({ findid: row.formId }, 'detail')
        } else {
          this.$refs['edit'].showEdit({ testplanid: row.formId }, true)
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
    },
  }
</script>
