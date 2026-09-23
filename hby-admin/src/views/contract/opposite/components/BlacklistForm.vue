<!--
 * @Date: 2022-04-20 14:34:13
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-22 16:45:22
 * @FilePath: /hb-admin/src/views/contract/opposite/components/BlacklistForm.vue
-->
<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row>
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="相对方编号">
            <span>{{ node.counterpartno }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="相对方名称">
            <span>{{ node.budgetname }}</span>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="第三方编号">
            <span>{{ node.othermoney }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属行业">
            <span>{{ node.financemoney }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="相对方类型">
            <span>{{ node.counterparttype }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="相对方性质">
            <span>{{ node.oppositenature }}</span>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="证件类型">
            <span>{{ node.projectrisk }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="证件编号">
            <span>{{ node.cretificateno }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="证件有效期">
            {{ node.pstartdate }} 至 {{ node.penddate }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="内部单位" prop="servicetype">
            {{ node.servicetype }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="注册资本(万元)" prop="totaltmoney">
            {{ node.totaltmoney }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="法定代表人" prop="projectstagegoal">
            {{ node.projectstagegoal }}
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="法定代表人身份证" prop="projectcondition">
            {{ node.projectcondition }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮编" prop="counterpartcode">
            {{ node.counterpartcode }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="网址" prop="counterpartnetaddress">
            {{ node.counterpartnetaddress }}
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="负责人" prop="director">
            {{ node.director }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人联系电话" prop="counterpartphone">
            {{ node.counterpartphone }}
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="地址" prop="counterpartaddress">
            {{ node.counterpartaddress }}
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="纳税人识别号" prop="resultdescription">
            {{ node.resultdescription }}
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="相对简介" prop="counterpartdesc">
            {{ node.counterpartdesc }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="staffid">
            {{ node.staffid }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建日期" prop="date">
            {{ node.date }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form ref="form" label-width="120px" :model="form" :rules="rules">
            <el-form-item label="黑名单类型" prop="blackType">
              <el-radio-group v-model="form.blackType" size="medium">
                <el-radio :label="2">长期黑名单</el-radio>
                <el-radio :label="1">短期黑名单</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item
              v-if="form.blackType == 1"
              label="黑名单有效期"
              prop="datetext"
            >
              <el-date-picker
                v-model="form.datetext"
                clearable
                format="yyyy-MM-dd"
                placeholder="黑名单有效期"
                :style="{ width: '100%' }"
                value-format="yyyy-MM-dd"
                :picker-options="pickerOptions"
              />
            </el-form-item>
          </el-form>
        </el-col>
        <!-- <el-col :span="24">
          <el-divider>联系人信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人" prop="contacts">
            {{ node.contacts }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人电话" prop="contactsphone">
            {{ node.contactsphone }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人地址" prop="contactsadress">
            {{ node.contactsadress }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="电子邮箱" prop="contactsemail">
            {{ node.contactsemail }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="岗位" prop="station">
            {{ node.station }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="称呼" prop="callname">
            {{ node.callname }}
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            {{ node.remarks }}
          </el-form-item>
        </el-col> -->

        <el-col :span="24">
          <Bank :opposite="node" :readonly="true" />
        </el-col>
        <el-col :span="24">
          <el-divider>资质信息</el-divider>
          <AttachList :att-list="node.attList" :readonly="true" />
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <ProcessList :modal="false" ref="process" />
  </el-dialog>
</template>
<script>
  import { addToBlackList, getOpposite } from '@/api/contract/opposite'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import Bank from './Bank.vue'
  import AttachList from './AttachList.vue'
  export default {
    components: {
      Bank,
      AttachList,
      ProcessList,
    },
    data() {
      return {
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() < Date.now()
          },
        },
        dialogFormVisible: false,
        form: {
          budgetId: undefined,
          blackType: 2,
          datetext: undefined,
        },
        curretnRow: {},
        rules: {
          blackType: [
            { required: true, trigger: 'blur', message: '请输入黑名单有效期' },
          ],
          datetext: [
            { required: true, trigger: 'blur', message: '请输入黑名单有效期' },
          ],
        },
        title: '',
        node: {},
      }
    },
    methods: {
      async fetchItem(row) {
        const {
          data: { attList, budget, bankInfoList },
        } = await getOpposite({
          flowId: row.flowid,
          budgetId: row.budgetid,
        })
        const {
          createStaff: { username },
          createtime,
        } = budget
        this.node = budget
        this.node.attList = attList
        this.node.bankInfoList = bankInfoList
        this.node.staffid = username
        this.node.date = createtime
      },
      showEdit(row) {
        this.title = '加入黑名单'
        this.fetchItem(row)
        this.curretnRow = row
        this.form.budgetId = row.budgetid
        this.dialogFormVisible = true
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { code, msg } = await addToBlackList(this.form)
            if (code == 1) {
              // tableId, fromId
              this.$refs['process'].save(3, this.curretnRow.budgetid)
              // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              // this.$emit('fetch-data')
              // this.close()
            }
          }
        })
      },
      close() {
        this.curretnRow = {}
        this.form = {}
        this.node = {}
        this.dialogFormVisible = false
      },
    },
  }
</script>
