<template>
  <div>
    <!-- <Deal ref="deal" />
    <PaymentBanli ref="banlifk" />
    <ReceivingBanli ref="banlisk" />
    <Track ref="track" />
    <PapersDetails ref="paper" />
    <ConfirmDetail ref="confirm" />
    <ShenjiModal ref="shenji" :UEditorCloudEdit="this.UEditorShenPiCloudEdit" />
    <Reference ref="reference" />
    <reportModal ref="report" :UEditorCloudEdit="this.UEditorCloudEdit" />
    <EvaluationModal ref="evaluation" />
    <PersonModal ref="person" /> -->
    <WddbDeal ref="Wddbdeal" />
  </div>
</template>

<script>
  // import PaymentBanli from '@//views/msg/components/operation/PaymentDetail'
  // import { getToDoList, project_plan_list } from '@/api/setting/msg'
  // import { handleHanlde, handleReport } from '@/api/workbench/auditTools'
  // import { formatDate } from '@/utils/index'
  // import PapersDetails from '@/views/msg/components/operation/papersDetails'
  // import ReceivingBanli from '@/views/msg/components/operation/ReceivingDetail'
  // import Reference from '@/views/msg/components/operation/ReferenceDetail.vue'
  // import Deal from '@/views/msg/components/options/Deal'
  // import Track from '@/views/msg/components/options/Track'
  // import * as Dayjs from 'dayjs'
  // import ConfirmDetail from '@/views/msg/components/operation/confirmDetail.vue'
  // import EvaluationModal from '@/views/msg/components/operation/evaluationsModal.vue'
  // import PersonModal from '@/views/msg/components/operation/personModal.vue'
  // import reportModal from '@/views/msg/components/operation/reportModal.vue'
  // import ShenjiModal from '@/views/msg/components/operation/shenpiModal.vue'
  import WddbDeal from '@/views/msg/components/options/WddbDeal.vue'

  export default {
    name: 'Deal',
    components: {
      // PaymentBanli,
      // ReceivingBanli,
      // Track,
      // Deal,
      // ShenjiModal,
      // PapersDetails,
      // Reference,
      // ConfirmDetail,
      // reportModal,
      // EvaluationModal,
      // PersonModal,
      WddbDeal,
    },
    data() {
      return {
        type: '',
        showModal: false,
        UEditorCloudEdit: false,
        UEditorShenPiCloudEdit: false,
        timer: null,
        activeName: 'second',
      }
    },
    watch: {},
    mounted() {
      // const info = {
      //   flowId: '354159975280410821',
      //   id: '412702938339737797',
      //   processId: '412702938339737797',
      //   thisStepId: 'RrD5fS1',
      //   flowName: 'szfk测试',
      // }
      // this.handleDetail1(info)

      if (this.$store.state.work.wddbState) {
        this.handleDetail1(this.$store.state.work.wddbDetails)
      }
    },
    methods: {
      handleDetail1(row) {
        let dataRow = JSON.parse(JSON.stringify(row))
        this.$refs.Wddbdeal.show(dataRow, false)
      },
      async handleDetail(row) {
        //判断用户信息，处理富文本框的状态
        let userInfo = JSON.parse(localStorage.getItem('userInfo'))
        const ids = row.cyurl.split('=')[1]
        if (row.cytype == '收款管理' || row.cytype == '付款管理') {
          if (row.cytype == '收款管理') {
            let collectionid = row.cyurl.split('=')
            row.collectionId = collectionid[1]
            this.type = 'receiving'
            this.$refs['banlisk'].showEdit(row, 'wddb')
          }
          if (row.cytype == '付款管理') {
            let paymentid = row.cyurl.split('=')
            row.paymentId = paymentid[1]
            this.type = 'payment'
            this.$refs['banlifk'].showEdit(row, 'wddb')
          }
        } else {
          // if (row.cytype == '合同管理' && row.recordtype == 'HTGL007') {
          //   //合同范本
          //   this.type = 'sample'
          // }
          // if (row.cytype == '合同管理' && row.recordtype == 'HTGL002') {
          //   //合同起草
          //   this.type = 'create'
          // }
          // if (row.cytype == '合同管理' && row.recordtype == 'HTGL005') {
          //   //合同变更
          //   this.type = 'change'
          // }
          if (row.cytype == '合同管理') {
            //合同变更
            this.type = 'create'
          }

          if (row.cytype == '合同用印') {
            this.type = 'seal'
          }
          if (row.cytype == '合同借阅') {
            this.type = 'borrow'
          }
          if (row.cytype == '相对方维护') {
            this.type = 'opposite'
          }
          // 审计计划
          if (row.cytype == '计划审批') {
            this.type = 'IndexEdit'
          }
          if (row.cytype == '项目审批') {
            this.type = 'project'
          }
          if (row.cytype == '档案借阅') {
            this.$refs['reference'].showEdit('借鉴', row)
            return
          }
          if (row.cytype == '事实确认书') {
            this.type = 'confirm'
            this.$refs['confirm'].showEdit('事实确认书', row)

            return
          }
          if (row.cytype == '审计人员审核') {
            this.$refs['person'].showEdit(row, '查看')
            return
          }
          if (row.cytype == '评价审核') {
            this.$refs['evaluation'].showEdit('评价审核', row)
            return
          }
          if (row.cytype === '底稿复核') {
            this.type = 'paper'
            this.$refs['paper'].showEdit('复核', row)
            return
          }
          if (row.cytype === '审计通知书') {
            let res = await handleHanlde({
              adviceid: ids,
              cyId: row.cyid,
              taskId: row.taskid,
            })
            if (
              row.cystaffid == userInfo.staffid &&
              res.data.cy.cystate == '需调整'
            ) {
              this.UEditorShenPiCloudEdit = true
            } else {
              this.UEditorShenPiCloudEdit = false
            }
            this.$refs['shenji'].showEdit(row, '办理')
            return
          }
          if (
            row.cytype === '审计报告复核' ||
            row.cytype === '审计报告' ||
            row.cytype === '审计报告征求意见'
          ) {
            let res = await handleReport({
              cyId: row.cyid,
              reportid: ids,
              taskId: row.taskid,
            })
            if (
              row.cystaffid == userInfo.staffid &&
              res.data.cy.cystate == '需调整'
            ) {
              this.UEditorCloudEdit = true
            } else {
              this.UEditorCloudEdit = false
            }
            this.$refs['report'].showEdit(row, '办理')
            return
          }
          this.$refs['deal'].show(row, this.type, true)
        }
      },
    },
  }
</script>

<style>
  .deal_dialog .el-loading-spinner {
    top: 200px !important;
    margin-top: -21px;
    width: 100%;
    text-align: center;
    position: absolute;
  }
</style>
