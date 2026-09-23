<template>
  <div class="myTableStyle">
    <el-form label-width="150px" :model="formData">
      <!-- <img src="@/assets/zhezi.png" style="width: 185px; height: 20px" alt="" /> -->
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
      <div class="mycol">
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractno">
            {{ formData.contractno }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item ref="contractname" label="合同名称" prop="contractname">
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
          <el-form-item :label="formFields['startdate'].label" prop="startdate">
            {{ formData.startdate }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="formFields['enddate'].label" prop="enddate">
            {{ formData.enddate }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="事项审议机构" prop="matterorg">
            {{ formData.matterorg }}
          </el-form-item>
        </el-col>
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
          <el-form-item :label="formFields['jijiatype'].label" prop="jijiatype">
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
      </div>
      <el-col :span="24">
        <el-divider>合同金额信息</el-divider>
      </el-col>
      <div class="mycol">
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
      </div>
    </el-form>
    <el-col>
      <el-divider>相对方信息</el-divider>
    </el-col>
    <el-col :span="24">
      <el-table :data="tableDataProject">
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
    </el-col>

    <el-col v-if="formData.contractplan === '是'" :span="24">
      <el-divider>合同阶段信息</el-divider>
      <JieduanList
        v-if="Jie"
        :contract="formData"
        :readonly="true"
        ref="jieduanListRef"
        @data-change="formData.nodeList = $event"
        :XDFList="JDDetailXDFInfo"
      />
    </el-col>
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
      ></el-table-column>
      <el-table-column
        align="center"
        prop="userName"
        label="办理人"
      ></el-table-column>

      <el-table-column prop="handleStatus" align="center" label="办理结果">
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
      ></el-table-column>
    </el-table>
  </div>
</template>

<script>
  import { getContractItem, viewDealProcess } from '@/api/contract/manage'
  import { thirteenBitTimestamp } from '@/utils/index'
  import { comboFields } from '@/views/contract/contractManage/components/contractsEdit/methods'
  import JieduanList from '@/views/contract/contractManage/components/block/JieduanList.vue'
  export default {
    // directives: {
    //   print,
    // },
    components: {
      JieduanList,
    },
    name: 'Authorization',
    data() {
      return {
        formData: {},
        Jie: true,
        currentEdit: '',
        tableDataProject: [],
        formFields: {
          changetype: undefined,
        },
        JDDetailXDFInfo: {},
        tableData: [],
        thirteenBitTimestamp: thirteenBitTimestamp,
      }
    },
    watch: {
      formData(val, val2) {
        if (val != val2) {
          // 移除组件
          this.Jie = false
          // 在组件移除后，重新渲染组件
          // this.$nextTick可实现在DOM 状态更新后，执行传入的方法。
          this.$nextTick(() => {
            this.Jie = true
            console.log(this.Jie, 'this.Jie')
            this.$refs['jieduanListRef'].fetchData()
          })
        }
      },
      'formData.contractname'(val) {
        if (val) {
          this.$nextTick(() => {
            setTimeout(() => {
              this.syncInputHeight()
            }, 100)
          })
        }
      },
    },
    methods: {
      async show(row, flowTaskOperatorRecordList) {
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
        this.JDDetailXDFInfo = res
        let tableData = flowTaskOperatorRecordList || []
        console.log(tableData, 'tableData')
        const first = tableData[0]
        this.tableData = tableData.filter((item) => {
          return !item.nodeName.includes('无需')
        })
        //过滤审核拒绝的数据
        this.tableData = this.tableData.filter((item) => {
          return item.handleStatus != 0
        })
        this.tableData = this.tableData.filter((item) => {
          return item.handleOpinion != '自动审批通过'
        })
        this.tableData = this.tableData.filter((item) => {
          return item.nodeName != '开始'
        })
        this.tableData.unshift(first)
        this.formData = res.data && res.data.tcu
        this.formData.nodeList =
          res.data && res.data.nodeList && res.data.nodeList.length > 0
            ? res.data.nodeList
            : []

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

        this.$nextTick(() => {
          setTimeout(() => {
            this.syncInputHeight()
          }, 100)
        })
        // const tableData = await viewDealProcess({
        //   contractId: row.contractid,
        //   taskId: '',
        // })
      },
      // 同步每一行最高，为了省事，懒得写css
      syncInputHeight() {
        const _mycol = document.getElementsByClassName('mycol')
        const _elFormItems = _mycol[0].getElementsByClassName(
          'el-form-item__content'
        )
        let first = true,
          cacheFirst = 0
        _elFormItems.forEach((x, i) => {
          if (i === _elFormItems.length - 1) return
          const curH = _elFormItems[i].clientHeight
          if (first === true) {
            cacheFirst = curH
            first = false
          } else {
            const m = curH - cacheFirst
            if (m > 0) {
              _elFormItems[i - 1].style.height = curH + 'px'
            } else {
              _elFormItems[i].style.height = cacheFirst + 'px'
            }
            first = true
            cacheFirst = 0
          }
        })
      },
    },
  }
</script>

<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
  .el-table {
    margin-top: 10px;
    margin-bottom: 18px;
  }
  :deep .myTableStyle .el-col {
    padding: 0 !important;
  }
  :deep .myTableStyle .el-divider {
    margin-top: 30px !important;
  }
  :deep .mycol {
    display: flow-root;
    border: 1px solid rgba(0, 0, 0, 0.4);
    margin-bottom: 30px;
  }
  :deep .mycol .el-col .el-form-item {
    margin-bottom: 0 !important;
    margin-left: -1px;
    margin-top: -1px;
    margin-right: -1px;
    border-top: 1px solid rgba(0, 0, 0, 0.4);
    border-bottom: 1px solid rgba(0, 0, 0, 0.4);
    border-right: 1px solid rgba(0, 0, 0, 0.4);
  }

  :deep .mycol .el-col:last-child .el-form-item {
    border-bottom: 0;
  }

  :deep .mycol .el-col .el-form-item__label {
    padding: 8px;
  }
  :deep .mycol .el-col .el-form-item__content {
    margin-bottom: 0 !important;
    padding: 8px 4px 4px 10px;
    border-left: 1px solid rgba(0, 0, 0, 0.4);
  }
</style>
