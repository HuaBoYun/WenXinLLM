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
    <PlanEdit ref="pgjh" />
    <RiskCreateEdit ref="zdfxcj" />
    <GroupPlanView ref="groupPlan" />
    <RiskIndicatorCreation ref="riskIndicatorCreation" />
  </div>
</template>

<script>
  import MessageNotification from '@/components/Common/MessageNotification.vue'
  import PlanEdit from '@/views/risk/assessment/plan/components/PlanEdit.vue'
  import RiskCreateEdit from '@/views/risk/riskfill/cetateEdit.vue'
  import GroupPlanView from '@/views/workbench/contractTools/components/GroupPlanView.vue'
  import RiskIndicatorCreation from '@/views/risk/riskfill/components/IndicatorEdit.vue'

  export default {
    name: 'RiskMessageNotification',
    components: {
      MessageNotification,
      PlanEdit,
      RiskCreateEdit,
      GroupPlanView,
      RiskIndicatorCreation,
    },
    data() {
      return {
        moduleType: 'fxgk',
        type: {
          PGJH: '评估计划',
          ZDFXCJ: '重大风险创建',
          JTPGJH: '集团评估计划',
          FXJCZBCJ: '风险监测指标创建',
        },
        jumpUrl: {
          PGJH: '/assessment/task',
          ZDFXCJ: '/riskfill/riskfill',
          JTPGJH: '/assessment/plan',
          FXJCZBCJ: '/riskfill/riskIndicatorReporting',
        },
        typeData: [
          { textValue: 'PGJH', textName: '评估计划' },
          { textValue: 'ZDFXCJ', textName: '重大风险创建' },
          { textValue: 'JTPGJH', textName: '集团评估计划' },
          { textValue: 'FXJCZBCJ', textName: '风险监测指标创建' },
        ],
      }
    },
    methods: {
      /**
       * 处理查看详情事件
       * @param {Object} row - 行数据
       */
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
        } else {
          this.$refs['zdfxcj'].showEdit({ id: row.formId }, 'detail')
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
