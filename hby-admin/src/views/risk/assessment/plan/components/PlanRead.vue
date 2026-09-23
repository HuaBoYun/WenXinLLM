<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="80%"
      append-to-body
      @close="close"
    >
      <el-tabs
        v-model="activeName"
        type="card"
        @tab-click="handleTabClick"
        v-loading="loading"
      >
        <el-tab-pane label="基本信息" name="dealInfo" v-if="showDealPage">
          <create
            ref="createInfo"
            :flowType="flowType"
            :formId="formId"
            :is-dialog="false"
            :is-wdcy="true"
            :is-wfqdedit="false"
            :isPlan="true"
          ></create>
        </el-tab-pane>
        <el-tab-pane label="流程信息" name="second">
          <div style="display: flex">
            <Process
              :conf="flowTemplateJson"
              v-if="flowTemplateJson.nodeId"
              :key="updateKey"
            />
            <div style="width: 900px">
              <ProcessTable :data="flowTaskNodeList" />
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="流转记录" name="third">
          <recordList :list="flowTaskOperatorRecordList" :endTime="endTime" />
        </el-tab-pane>
        <el-tab-pane label="知会信息" name="five">
          <InformInfoList :flowTaskInfo="flowTaskInfo" />
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>
<script>
  import { riTaskInfo } from '@/api/systemLog'
  import Process from '@/components/Process/Preview'
  import ProcessTable from '@/views/msg/components/options/ProcessTable.vue'
  import recordList from '@/views/msg/components/options/RecordList'
  import InformInfoList from '@/views/msg/components/options/InformInfoList.vue'
  import { getFaqiInfo } from '@/api/setting/msg'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import create from '@/views/msg/components/operation/CreateDetail.vue'

  export default {
    name: 'PlanRead',
    components: {
      Process,
      ProcessTable,
      recordList,
      InformInfoList,
      create,
    },
    inheritAttrs: false,
    data() {
      return {
        updateKey: 0,
        title: '',
        dialogFormVisible: false,
        activeName: 'dealInfo',
        currentRow: null,
        loading: false,
        showDealPage: false,
        flowType: '',
        formId: '',
        formData: {
          plancode: undefined,
          planName: undefined,
          startDate: null,
          endDate: null,
          assname: undefined,
          planType: undefined,
          orgname: undefined,
          recorder: undefined,
          planDes: undefined,
        },
        dealFormData: {},
        dealCurrent: {},
        assessmentstd: {},
        list: [],
        tableDataFile: [],
        flowTemplateJson: {},
        flowTaskNodeList: [],
        flowTaskOperatorRecordList: [],
        endTime: 0,
        flowTaskInfo: {},
      }
    },
    watch: {
      activeName(val) {
        if (val === 'dealInfo' && this.flowType) {
          this.$nextTick(() => {
            this.initDealInfoComponents()
          })
        }
      },
    },
    methods: {
      initDealInfoComponents() {
        if (this.$refs.createInfo) {
          this.$refs.createInfo.showDetail(
            this.dealFormData,
            this.dealFormData.contracttype,
            false,
            this.formId,
            this.flowTaskInfo.flowId,
            this.flowTaskInfo.id,
            this.flowTaskInfo.status
          )
        }
      },
      showRead(row) {
        this.title = '查看'
        this.currentRow = row
        this.loading = true

        riTaskInfo({ planId: row.assplanid }).then((res) => {
          this.$set(this, 'formData', {
            plancode: res.data.RiskAssplan.plancode,
            planName: res.data.RiskAssplan.planName,
            startDate: res.data.RiskAssplan.startDate,
            endDate: res.data.RiskAssplan.endDate,
            assname: res.data.RiskAssplan.assessmentstd.assname,
            planType: res.data.RiskAssplan.planType,
            orgname: res.data.RiskAssplan.organization.memo,
            planDes: res.data.RiskAssplan.plandes,
            recorder: res.data.RiskAssplan.recorder,
            content: res.data.RiskAssplan.content || undefined,
          })
          this.assessmentstd = res.data.RiskAssplan.assessmentstd
          this.tableDataFile = res.data.RiskAssplan.tblAttachments || []
          let list = res.data.RiskAssplan.riskAssplanRiskList || []
          this.list = list.filter((item) => item.risk)

          this.fetchFlowInfo(row)
        })

        this.dialogFormVisible = true
      },

      fetchFlowInfo(row) {
        getFlowPkInfo({
          formId: row.assplanid,
          tableId: 91,
        })
          .then((res) => {
            if (res.data) {
              this.flowTaskInfo = res.data
              this.showDealPage = true

              getFaqiInfo({
                id: res.data.id,
                flowId: res.data.flowId,
              })
                .then((faqiRes) => {
                  if (faqiRes.data && faqiRes.data.dataJson) {
                    this.flowTaskInfo = faqiRes.data.dataJson.flowTaskInfo || {}
                    this.flowType = faqiRes.data.flowType
                    this.formId = faqiRes.data.formId
                    this.dealFormData = faqiRes.data.dataJson.formData || {}
                    this.dealCurrent = faqiRes.data.dataJson.current || {}

                    this.flowTemplateJson = faqiRes.data.dataJson.flowTaskInfo
                      .flowTemplateJson
                      ? JSON.parse(
                          faqiRes.data.dataJson.flowTaskInfo.flowTemplateJson
                        )
                      : {}
                    this.updateKey += 1

                    this.flowTaskNodeList =
                      faqiRes.data.dataJson.flowTaskNodeList || []

                    if (
                      this.flowTaskNodeList.length &&
                      this.flowTemplateJson.nodeId
                    ) {
                      for (let i = 0; i < this.flowTaskNodeList.length; i++) {
                        const nodeItem = this.flowTaskNodeList[i]
                        const loop = (data) => {
                          if (Array.isArray(data)) data.forEach((d) => loop(d))
                          if (data.nodeId === nodeItem.nodeCode) {
                            if (nodeItem.type == 0) data.state = 'state-past'
                            if (nodeItem.type == 1) data.state = 'state-curr'
                            if (
                              nodeItem.nodeType === 'approver' ||
                              nodeItem.nodeType === 'start' ||
                              nodeItem.nodeType === 'subFlow'
                            )
                              data.content = nodeItem.userName
                            return
                          }
                          if (
                            data.conditionNodes &&
                            Array.isArray(data.conditionNodes)
                          )
                            loop(data.conditionNodes)
                          if (data.childNode) loop(data.childNode)
                        }
                        loop(this.flowTemplateJson)
                      }
                    }

                    this.flowTaskOperatorRecordList =
                      faqiRes.data.dataJson.flowTaskOperatorRecordList || []

                    this.endTime =
                      faqiRes.data.dataJson.flowTaskInfo.completion == 100
                        ? faqiRes.data.dataJson.flowTaskInfo.endTime
                        : 0

                    if (this.activeName === 'dealInfo') {
                      this.$nextTick(() => {
                        this.initDealInfoComponents()
                      })
                    }
                  }

                  this.loading = false
                })
                .catch(() => {
                  this.loading = false
                })
            } else {
              this.loading = false
            }
          })
          .catch(() => {
            this.loading = false
          })
      },

      close() {
        this.dialogFormVisible = false
        this.activeName = 'dealInfo'
        this.flowTemplateJson = {}
        this.flowTaskNodeList = []
        this.flowTaskOperatorRecordList = []
        this.flowTaskInfo = {}
        this.showDealPage = false
        this.flowType = ''
        this.formId = ''
        this.dealFormData = {}
        this.dealCurrent = {}
      },

      handleTabClick() {
        // 可以在这里处理标签页切换逻辑
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
