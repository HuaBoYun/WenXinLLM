<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
    @close="close"
    class="copy_info_dialog"
    v-if="dialogFormVisible"
  >
    <el-tabs
      v-if="dialogFormVisible"
      v-model="activeName"
      type="card"
      @tab-click="handleTabClick"
      v-loading="loading"
    >
      <el-tab-pane label="基本信息" name="first">
        <create
          ref="createInfo"
          :flowType="flowType"
          :formId="formId"
          :is-dialog="false"
          :is-wdcy="true"
          :is-wfqdedit="false"
        ></create>
      </el-tab-pane>

      <el-tab-pane label="流程信息" name="second" v-loading="loading">
        <div class="flow-info-container">
          <!-- 5.x 版本：使用 bpmn-js 渲染，完全还原引迈中设置的横/纵向布局 -->
          <div
            v-if="flowVersion == '5' && flowTaskInfo && flowTaskInfo.id"
            class="flow-chart-inline"
          >
            <BpmnViewer
              ref="inlineBpmnViewer"
              :flowXml="inlineFlowInfo.flowXml"
              :progressList="flowTaskNodeList"
              :key="'bpmn-' + updateKey"
            />
          </div>
          <el-empty
            v-else-if="flowVersion == '5'"
            description="暂无流程信息"
          />
          <!-- 3.x 版本：原有流程图 -->
          <div
            v-if="
              flowVersion != '5' && flowTemplateJson && flowTemplateJson.nodeId
            "
          >
            <div class="flow-chart-wrapper">
              <Process :conf="flowTemplateJson" :key="'process-' + updateKey" />
            </div>
          </div>
          <el-empty
            v-else-if="flowVersion != '5'"
            description="暂无流程信息"
          />
        </div>
      </el-tab-pane>

      <el-tab-pane label="流转记录" name="third" v-loading="loading">
        <RecordTimeList
          v-if="flowVersion == '5'"
          :list="flowTaskOperatorList"
          :taskId="currentRow ? currentRow.id : ''"
        />
        <recordList
          v-else
          :list="flowTaskOperatorRecordList"
          :endTime="endTime"
        />
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script>
  import { getContractItem } from '@/api/contract/manage'
  import create from '@/views/msg/components/operation/CreateDetail.vue'
  import { getCYInfo } from '@/api/setting/msg'
  import Process from '@/components/Process/Preview'
  import { formatDay } from '@/utils/index'
  import { flowVersion } from '@/config/net.config'
  import recordList from './RecordList'
  import RecordTimeList from './RecordTimeList.vue'
  import BpmnViewer from '@/components/bpmnjs/BpmnViewer.vue'

  export default {
    name: 'CopyInfoDetail',
    components: {
      create,
      Process,
      recordList,
      RecordTimeList,
      BpmnViewer,
    },
    data() {
      return {
        flowVersion,
        updateKey: 0,
        flowType: '',
        formId: '',
        activeName: 'first',
        title: '',
        dialogFormVisible: false,
        loading: false,
        formatDay,
        flowTemplateJson: {},
        flowTaskOperatorRecordList: [],
        flowTaskNodeList: [],
        flowTaskInfo: {},
        flowNodesDict: {},
        flowTaskOperatorList: [],
        currentRow: null,
        endTime: 0,
      }
    },
    computed: {
      inlineFlowInfo() {
        return {
          id: this.flowTaskInfo ? this.flowTaskInfo.id : '',
          flowXml: this.flowTaskInfo ? this.flowTaskInfo.flowXml || '' : '',
          flowNodes:
            this.flowNodesDict ||
            (this.flowTaskInfo ? this.flowTaskInfo.flowNodes : {}) ||
            {},
          flowTemplateJson: this.flowTemplateJson,
        }
      },
    },
    methods: {
      async show(row) {
        console.log('抄送详情:', row)
        this.loading = true
        this.title = row.flowName || '抄送详情'
        this.currentRow = row

        // 构建请求参数
        const requestParams = {
          id: row.id,
          flowId: row.flowId,
          thisStepId: row.thisStepId,
          processId: row.processId,
        }

        // 5.x 版本需要传递 opType
        if (this.flowVersion == '5') {
          requestParams.opType = 6 // 抄送查看
        }

        let res = await getCYInfo(requestParams)

        console.log('抄送数据:', res.data)

        this.flowType = res.data.flowType
        this.formId = res.data.formId

        // 版本兼容：处理数据
        if (this.flowVersion == '5') {
          this.flowTaskInfo = res.data.flowTaskInfo || {}
          this.flowTaskNodeList = res.data.flowTaskNodeList || []
          this.flowTaskOperatorRecordList =
            res.data.flowTaskOperatorRecordList || []
          this.flowTaskOperatorList = res.data.flowTaskOperatorList || []

          // 保存 flowNodes 字典供流程图使用
          let flowNodes =
            res.data.flowNodes ||
            (this.flowTaskInfo ? this.flowTaskInfo.flowNodes : {}) ||
            {}
          this.flowNodesDict = flowNodes
        } else {
          const flowData = res.data.dataJson || {}
          this.flowTaskInfo = flowData.flowTaskInfo || {}
          this.flowTaskNodeList = flowData.flowTaskNodeList || []
          this.flowTaskOperatorRecordList =
            flowData.flowTaskOperatorRecordList || []
        }

        // 解析流程模板JSON
        if (this.flowVersion != '5') {
          this.flowTemplateJson = this.flowTaskInfo.flowTemplateJson
            ? JSON.parse(this.flowTaskInfo.flowTemplateJson)
            : {}
        }

        // 更新流程节点状态
        if (this.flowTaskNodeList.length && this.flowTemplateJson.nodeId) {
          for (let i = 0; i < this.flowTaskNodeList.length; i++) {
            const nodeItem = this.flowTaskNodeList[i]
            const loop = (data) => {
              if (Array.isArray(data)) data.forEach((d) => loop(d))
              if (data.nodeId == nodeItem.nodeCode) {
                if (nodeItem.type == 0) data.state = 'state-past'
                if (nodeItem.type == 1) data.state = 'state-curr'
                if (
                  nodeItem.nodeType == 'approver' ||
                  nodeItem.nodeType == 'start' ||
                  nodeItem.nodeType == 'subFlow'
                )
                  data.content = nodeItem.userName
                return
              }
              if (data.conditionNodes && Array.isArray(data.conditionNodes))
                loop(data.conditionNodes)
              if (data.childNode) loop(data.childNode)
            }
            loop(this.flowTemplateJson)
          }
        }

        // 计算结束时间
        this.endTime =
          this.flowTaskInfo.completion == 100 ? this.flowTaskInfo.endTime : 0

        this.updateKey += 1
        this.dialogFormVisible = true
        this.loading = false

        this.$nextTick(() => {
          this.fetchInfo(res.data)
        })
      },

      async fetchInfo(row) {
        const res = await getContractItem({
          flowid: '622316',
          flowname: '',
          contractId: row.formId,
        })
        this.loading = false

        this.$nextTick(() => {
          this.$refs['createInfo'].showDetail(
            this.currentRow,
            this.currentRow.contracttype,
            false,
            row.formId
          )
        })
      },

      close() {
        this.activeName = 'first'
        this.dialogFormVisible = false
        this.currentRow = null
      },

      handleTabClick() {
        if (this.activeName == 'second') {
          this.$nextTick(() => {
            if (this.$refs.inlineJsPlumb && this.$refs.inlineJsPlumb.rebuild) {
              this.$refs.inlineJsPlumb.rebuild()
            }
          })
        }
      },
    },
  }
</script>

<style lang="scss" scoped>
  .flow-info-container {
    .flow-chart-inline {
      min-height: 500px;
      border: 1px solid #ebeef5;
      border-radius: 4px;
      overflow: hidden;
    }

    .flow-chart-wrapper {
      padding: 20px;
      background: #fff;
      border-radius: 4px;
      border: 1px solid #ebeef5;
    }
  }

  .copy_info_dialog {
    ::v-deep .el-dialog {
      margin-top: 5vh !important;
    }

    ::v-deep .el-dialog__body {
      max-height: 75vh;
      overflow-y: auto;
    }
  }
</style>
