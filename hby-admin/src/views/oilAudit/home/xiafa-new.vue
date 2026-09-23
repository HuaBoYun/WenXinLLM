<template>
  <div>
    <message-notification
      :moduleType="moduleType"
      :typeMapping="type"
      :jumpUrlMapping="jumpUrl"
      :showHytz="true"
      :showXmpy="true"
      :hyTotal="hyTotal"
      :xmpyTotal="xmpyTotal"
      :customTypeData="typeData"
      @view-detail="handleViewDetail"
      @confirm-received="handleConfirmReceived"
      @batch-confirm="handleBatchConfirm"
    >
      <!-- 会议通知内容插槽 -->
      <template #hytz-content>
        <HYTZ @HYtotal="handleHYtotal"></HYTZ>
      </template>

      <!-- 项目评优内容插槽 -->
      <template #xmpy-content>
        <XMPYTable @XMPYtotal="handleXMPYtotal"></XMPYTable>
      </template>
    </message-notification>

    <!-- 引入所需的弹窗组件 -->
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
  </div>
</template>

<script>
  import MessageNotification from '@/components/Common/MessageNotification.vue'
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
  import HYTZ from '../home/components/message.vue'
  import gcsjxmapView from '@/views/oilAudit/jhlx/components/gcsjxmapbEdit.vue'
  import cwsjxmapView from '@/views/oilAudit/jhlx/components/cwsjxmapbEdit.vue'
  import llyttzView from '@/views/oilAudit/lwpy/components/llyttzEdit.vue'
  import XMPYTable from '@/views/oilAudit/home/components/xmpy/index.vue'
  import lwhjtzEdit from '@/views/oilAudit/lwpy/components/lwhjtzEdit.vue'
  import sjtzsEdit from '@/views/oilAudit/prepare/components/NoticeInfo.vue'
  import xmpyhjtzEdit from '@/views/oilAudit/xmpy/hjtz/edit.vue'

  export default {
    name: 'OilAuditMessageNotification',
    components: {
      MessageNotification,
      XMPYTable,
      HYTZ,
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
    },
    data() {
      return {
        moduleType: 'yqns',
        hyTotal: 0,
        xmpyTotal: 0,
        type: {
          SJLXJYTZ: '审计立项建议通知',
          LXJYB: '立项建议表',
          XQJYB: '需求建议表',
          FGLDHZ: '分管领导汇总',
          FWXQB: '服务需求表',
          EJDWJCYLRSJ: '二级单位及成员单位离任审计',
          WWTJYJLR: '未委托及预计离任',
          SJDWLRSJ: '三级单位离任审计',
          GZFA: '工作方案',
          SJQKB: '审计项目表',
          SJXMZD: '审计项目制度',
          IPQD: 'IP清单',
          YXXMPX: '优秀项目评选',
          GCSJXMAP: '工程审计项目安排',
          CWSJXMAP: '财务审计项目安排',
          CWDDFG: '财务督导分工',
          GCDDFG: '工程督导分工',
          LLYTTZ: '理论研讨通知',
          WTDZ: '问题定责',
          HJTZ: '获奖通知',
          SJTZS: '审计通知书',
          XMPYHJTZ: '项目评优-获奖通知',
        },
        jumpUrl: {
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
        },
        typeData: [
          { textValue: 'SJLXJYTZ', textName: '审计立项建议通知' },
          { textValue: 'LXJYB', textName: '立项建议表' },
          { textValue: 'XQJYB', textName: '需求建议表' },
          { textValue: 'FGLDHZ', textName: '分管领导汇总' },
          { textValue: 'FWXQB', textName: '服务需求表' },
          { textValue: 'EJDWJCYLRSJ', textName: '二级单位及成员单位离任审计' },
          { textValue: 'WWTJYJLR', textName: '未委托及预计离任' },
          { textValue: 'SJDWLRSJ', textName: '三级单位离任审计' },
          { textValue: 'GZFA', textName: '工作方案' },
          { textValue: 'SJQKB', textName: '审计项目表' },
          { textValue: 'SJXMZD', textName: '审计项目制度' },
          { textValue: 'IPQD', textName: 'IP清单' },
          { textValue: 'YXXMPX', textName: '优秀项目评选' },
          { textValue: 'GCSJXMAP', textName: '工程审计项目安排' },
          { textValue: 'CWSJXMAP', textName: '财务审计项目安排' },
          { textValue: 'CWDDFG', textName: '财务督导分工' },
          { textValue: 'GCDDFG', textName: '工程督导分工' },
          { textValue: 'LLYTTZ', textName: '理论研讨通知' },
          { textValue: 'WTDZ', textName: '问题定责' },
          { textValue: 'HJTZ', textName: '获奖通知' },
          { textValue: 'SJTZS', textName: '审计通知书' },
          { textValue: 'XMPYHJTZ', textName: '项目评优-获奖通知' },
        ],
      }
    },
    methods: {
      /**
       * 处理查看详情事件
       * @param {Object} row - 行数据
       */
      handleViewDetail(row) {
        if (row.distributionType == 'SJLXJYTZ') {
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
    },
  }
</script>
