<!--
 * @Author: 康某 dev@example.com
 * @Date: 2022-09-01 22:00:16
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-09-01 22:00:22
 * @FilePath: \hb-admin\src\views\contract\contractManage\components\print.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div class="">
    <el-button v-print="printContent" id="printBtn" ref="print">打印</el-button>
    <div style="display: none">
      <div id="printMe" class="printMes">
        <el-form label-width="150px" :model="formData">
          <el-col :span="24" v-if="formData.recordtype == 'HTGL005'">
            <el-divider>合同变更内容</el-divider>
          </el-col>
          <div class="mycol" v-if="formData.recordtype == 'HTGL005'">
            <el-col :span="12">
              <el-form-item label="合同变更类型" prop="changetype">
                {{ formData.changetype }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="合同变更时间" prop="changedate">
                {{ formData.changedate }}
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="合同变更内容" prop="changedesc">
                {{ formData.changedesc }}
              </el-form-item>
            </el-col>
          </div>
          <el-col :span="24">
            <el-divider>合同基本信息</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同编号" prop="contractno">
              {{ formData.contractno }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              ref="contractname"
              label="合同名称"
              prop="contractname"
            >
              {{ formData.contractname }}
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
        <el-form-item label="项目编号" prop="contractitem">
          {{ formData.contractitem }}
        </el-form-item>
      </el-col> -->
          <el-col :span="12">
            <el-form-item label="项目名称" prop="topicid">
              {{ formData.topicname }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同类型" prop="contracttype">
              {{ formData.contracttype }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同分类" prop="typefl">
              {{ formData.typefl }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['counterparttype']" :span="12">
            <el-form-item
              :label="formFields['counterparttype'].label"
              prop="counterparttype"
            >
              {{ formData.counterparttype }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              :label="formFields['startdate'].label"
              prop="startdate"
            >
              {{ formData.startdate }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="formFields['enddate'].label" prop="enddate">
              {{ formData.enddate }}
            </el-form-item>
          </el-col>
          <el-form-item label="事项审议机构" prop="matterorg">
            {{ formData.matterorg }}
          </el-form-item>
          <el-col :span="12" v-if="formData.matterorg == '董事会'">
            <el-form-item label="是否三重一大事项" prop="isbigmatter">
              {{ formData.isbigmatter }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['director']" :span="12">
            <el-form-item :label="formFields['director'].label" prop="director">
              {{ formData.director }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['counterpartaddress']" :span="12">
            <el-form-item
              :label="formFields['counterpartaddress'].label"
              prop="counterpartaddress"
            >
              {{ formData.counterpartaddress }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['contacts']" :span="12">
            <el-form-item :label="formFields['contacts'].label" prop="contacts">
              {{ formData.contacts }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['contactsphone']" :span="12">
            <el-form-item
              :label="formFields['contactsphone'].label"
              prop="contactsphone"
            >
              {{ formData.contactsphone }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['contractbd']" :span="12">
            <el-form-item
              :label="formFields['contractbd'].label"
              prop="contractbd"
            >
              {{ formData.contractbd }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['contractdatetype']" :span="12">
            <el-form-item
              :label="formFields['contractdatetype'].label"
              prop="contractdatetype"
            >
              {{ formData.contractdatetype }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['contractplan']" :span="12">
            <el-form-item
              :label="formFields['contractplan'].label"
              prop="contractplan"
            >
              {{ formData.contractplan }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['contractxz']" :span="12">
            <el-form-item
              :label="formFields['contractxz'].label"
              prop="contractxz"
            >
              {{ formData.contractxz }}
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['contractlink']" :span="12">
            <el-form-item
              :label="formFields['contractlink'].label"
              prop="contractlink"
            >
              {{ formData.contractlink }}
            </el-form-item>
          </el-col>
          <!-- jijiatype字段在金额信息部分也有用，在非默认模板情况下才用在合同基本信息 -->
          <el-col :span="12">
            <el-form-item
              :label="formFields['jijiatype'].label"
              prop="jijiatype"
            >
              {{ formData.jijiatype }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="我方签署主体" prop="orgname">
              {{ formData.orgname }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="承办部门" prop="orgmeno">
              {{ formData.orgmeno }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="承办人" prop="realname">
              {{ formData.realname }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="授权委托人" prop="entrustStaffName">
              {{ formData.entrustStaffName }}
            </el-form-item>
          </el-col>
          <!-- <el-col v-if="currentEdit !== 'moren'" :span="12"> -->
          <!-- <el-col :span="12">
        <el-form-item label="项目名称" prop="topicid">
          {{ formData.topicname }}
        </el-form-item>
        </el-col> -->
          <template v-if="formFields['contractchildren']">
            <el-col :span="12">
              <el-form-item label="是否关联合同" prop="contractchildren">
                {{ formData.contractchildren }}
              </el-form-item>
            </el-col>
            <!-- 详情关联合同的展示在底部tab的合同版本信息 -->
            <el-col v-show="formData['contractchildren'] == '是'" :span="12">
              <el-form-item label="关联合同" prop="parentname">
                {{ formData.parentname }}
              </el-form-item>
            </el-col>
          </template>
          <el-col :span="12">
            <el-form-item label="是否为多个合同" prop="ismany">
              {{ formData.ismany }}
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.ismany == '是'">
            <el-form-item label="多个合同数量" prop="agreementcount">
              {{ formData.agreementcount }}
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="formFields['momoconcat'].label">
              <el-input
                v-model="formData.momoconcat"
                :autosize="{ minRows: 4, maxRows: 4 }"
                :disabled="true"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
          </el-col>
          <el-col v-if="formFields['riskcontrol']" :span="24">
            <el-form-item :label="formFields['riskcontrol'].label">
              {{ formData.riskcontrol }}
              <!-- <el-input
                v-model="formData.riskcontrol"
                :autosize="{ minRows: 4, maxRows: 4 }"
                :placeholder="`请输入${formFields['riskcontrol'].label}`"
                :style="{ width: '100%' }"
                type="textarea"
              /> -->
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>合同金额信息</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收付方向" prop="dctype">
              {{ formData.dctype }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              :label="formFields['contractmoney'].label"
              prop="contractmoney"
            >
              {{ formData.contractmoney }}
            </el-form-item>
          </el-col>
          <el-col v-if="currentEdit == 'moren'" :span="12">
            <el-form-item label="计价方式" prop="jijiatype">
              {{ formData.jijiatype }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="moneytype">
              {{ formData.moneytype }}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="人民币大写" prop="hzsumowing">
              {{ formData.hzsumowing }}
            </el-form-item>
          </el-col>
        </el-form>
        <el-col>
          <el-divider>相对方信息</el-divider>
        </el-col>
        <el-table :data="tableDataProject" style="width: 100%">
          <el-table-column
            align="center"
            label="相对方类型"
            prop="bugetType"
          ></el-table-column>
          <el-table-column
            align="center"
            label="相对方名称"
            prop="budgetname"
          ></el-table-column>
        </el-table>
        <el-col v-if="formData.contractplan === '是'" :span="24">
          <el-divider>合同阶段信息</el-divider>
          <!-- <JieduanList
            :contract="formData"
            :readonly="true"
            ref="jieduanListRef"
            @data-change="formData.nodeList = $event"
            :XDFList="JDDetailXDFInfo"
          /> -->
        </el-col>
        <el-table :data="list" v-if="formData.contractplan === '是'">
          <el-table-column
            align="center"
            label="付款方向"
            prop="performanceCategory"
          >
            <template slot-scope="{ row }">
              {{ renderPerformance(row) }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="履行内容"
            prop="nodecontent"
          ></el-table-column>
          <el-table-column
            align="center"
            label="预计开始时间"
            prop="planstartdate"
          />
          <el-table-column
            align="center"
            label="预计结束时间"
            prop="planenddate"
          />
          <el-table-column
            v-if="formData.dctype != '无'"
            align="center"
            :label="'预计' + formData.dctype + '时间'"
            prop="nodeplanpaydate"
          />
          <el-table-column
            v-if="formData.dctype != '无'"
            align="center"
            :label="formData.dctype + '比例'"
            prop="nodepost"
          />
          <el-table-column
            v-if="formData.dctype != '无'"
            align="center"
            :label="formData.dctype + '金额(元)'"
            prop="nodemoney"
          />
          <el-table-column align="center" label="经办部门" prop="orgname" />
          <el-table-column align="center" label="经办人" prop="realname" />
        </el-table>
        <div>
          <el-col :span="24">
            <el-divider>合同审批记录</el-divider>
          </el-col>
        </div>
        <el-table :data="tableData" style="width: 100%">
          <el-table-column
            align="center"
            prop="nodeName"
            label="办理节点"
            width="150px"
          ></el-table-column>
          <el-table-column
            align="center"
            prop="userName"
            label="办理人"
            width="150px"
          ></el-table-column>

          <el-table-column
            prop="handleStatus"
            align="center"
            label="办理结果"
            width="100px"
          >
            <template slot-scope="scope">
              {{
                scope.row.handleStatus == 0
                  ? '审核拒绝'
                  : scope.row.handleStatus == 1
                  ? '审核通过'
                  : scope.row.handleStatus == 2
                  ? '审核发起'
                  : scope.row.handleStatus == 3
                  ? '审核撤回'
                  : scope.row.handleStatus == 4
                  ? '流程终止'
                  : scope.row.handleStatus == 5
                  ? '流程指派'
                  : scope.row.handleStatus == 6
                  ? '流程加签'
                  : scope.row.handleStatus == 7
                  ? '流程转审'
                  : '流程结束'
              }}
            </template>
          </el-table-column>
          <el-table-column prop="handleOpinion" align="center" label="审批时间">
            <template #default="{ row }">
              {{ thirteenBitTimestamp(row.handleTime) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="handleOpinion"
            align="center"
            label="办理意见"
            width="200px"
          ></el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import { getContractItem, viewDealProcess } from '@/api/contract/manage'
// import { getCYInfo } from '@/api/setting/msg'
import { thirteenBitTimestamp } from '@/utils/index'
import { comboFields } from './contractsEdit/methods'
import JieduanList from '@/views/contract/contractManage/components/block/JieduanList.vue'
export default {
  // directives: {
  //   print,
  // },
  components: {
    JieduanList,
  },
  data() {
    return {
      formData: {},
      printContent: {
        id: 'printMe',
        popTitle: '',
        preview: false,
        previewTitle: '',
        etraCss: ' ',
        extraHead: ' ',
      },
      list: [],
      JDDetailXDFInfo: {},
      tableDataProject: [],
      currentEdit: '',
      formFields: {
        changetype: undefined,
      },
      tableData: [],
      thirteenBitTimestamp: thirteenBitTimestamp,
    }
  },
  watch: {},
  methods: {
    renderPerformance(e) {
      switch (e.performanceCategory) {
        case '1':
          return '付款'
        case '2':
          return '收款'
        // case '3':
        //   return '工期'
        // case '4':
        //   return '服务期'
        // case '5':
        //   return '交付成果'
        // case '6':
        //   return '其它'
        default:
          return ''
      }
    },
    //打印的回调
    async printClick(row, flowTaskOperatorRecordList) {
      const { formFields } = comboFields(row.contracttype, false)
      this.formFields = formFields
      const res = await getContractItem({
        contractId: row.contractid,
        flowId: row.flowid,
        flowname: row.recordtype,
      })
      // const res2 = await getCYInfo({
      //   id: row.id,
      //   flowId: row.flowid,
      //   thisStepId: row.thisStepId,
      //   processId: row.processId,
      // })
      this.list = res.data.nodeList
      this.JDDetailXDFInfo = res
      this.tableData = flowTaskOperatorRecordList || []
      this.formData = res.data && res.data.tcu
      const info =
        res.data.tcu.budgetList &&
        res.data.tcu.budgetList.map((res) => {
          return {
            budgetname: res.budgetname,
            bugetId: res.budgetid,
            bugetType: res.budgettype,
          }
        })
      this.tableDataProject = info || []
      // const tableData = await viewDealProcess({
      //   contractId: row.contractid,
      //   taskId: '',
      // })

      const btn = document.getElementById('printBtn')
      btn.click()
    },
  },
}
</script>

<style lang="scss" scoped>
#printBtn {
  display: none;
}

.printMes {
  padding-top: 30px;
  padding-left: 30px;
  padding-right: 30px;
  padding-bottom: 30px;
}
</style>
<style media="print">
@media print {
  @page {
    size: auto;
  }

  body,
  html {
    height: auto !important;
  }

  .el-table__header {
    width: 100% !important;
  }
  .el-table__body {
    width: 100% !important;
  }
}
</style>
