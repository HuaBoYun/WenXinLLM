<!--
 * @Date: 2022-04-20 13:04:17
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-20 15:52:41
 * @FilePath: /hb-admin/src/views/contract/opposite/components/MaintainDetail.vue
-->
<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="dialogFormVisible = false"
  >
    <el-row :gutter="15">
      <el-form ref="form" label-width="125px" :model="formData" size="medium">
        <el-col :span="12">
          <el-form-item label="相对方编号" prop="counterpartno">
            {{ formData.counterpartno }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="相对方名称" prop="budgetname">
            {{ formData.budgetname }}
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="第三方编号" prop="othermoney">
            {{ formData.othermoney }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属行业" prop="financemoney">
            {{ formData.financemoney }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="相对方类型" prop="counterparttypeparttype">
            {{ formData.counterparttype }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="相对方性质" prop="oppositenature">
            {{ formData.oppositenature }}
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="证件类型" prop="projectrisk">
            {{ formData.projectrisk }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="证件编号" prop="cretificateno">
            {{ formData.cretificateno }}
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="证件有效期">
            {{ formData.pdate[0] }} 至 {{ formData.pdate[1] }}
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="内部单位" prop="servicetype">
            {{ formData.servicetype }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="注册资本(万元)" prop="totaltmoney">
            {{ formData.totaltmoney }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="法定代表人" prop="projectstagegoal">
            {{ formData.projectstagegoal }}
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="法定代表人身份证" prop="projectcondition">
            {{ formData.projectcondition }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="邮编" prop="counterpartcode">
            {{ formData.counterpartcode }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="网址" prop="counterpartnetaddress">
            {{ formData.counterpartnetaddress }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人" prop="director">
            {{ formData.director }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人联系电话" prop="counterpartphone">
            {{ formData.counterpartphone }}
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="地址" prop="counterpartaddress">
            {{ formData.counterpartaddress }}
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="企业状态" prop="resultdescription">
            {{ formData.resultdescription }}
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="相对简介" prop="counterpartdesc">
            {{ formData.counterpartdesc }}
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
        <el-form-item label="证件有效期类型" prop="cerType">
          {{ formData.cerType==0?'短期':'长期' }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="有效期开始时间">
          {{ formData.pstartdateStr }}   
        </el-form-item>
      </el-col> 
      <el-col :span="12" v-if="formData.cerType == 0">
        <el-form-item label="有效期结束时间">
        {{ formData.penddateStr }} 
        </el-form-item>
      </el-col> 
        <el-col :span="12">
          <el-form-item label="创建人" prop="staffid">
            {{ formData.staffid }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建日期" prop="date">
            {{ formData.date }}
          </el-form-item>
        </el-col>
     
        <el-col :span="24">
          <el-divider>联系人信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人" prop="contacts">
            {{ formData.contacts }}
          </el-form-item>
        </el-col>
       <!--  <el-col :span="12">
          <el-form-item label="联系人电话" prop="contactsphone">
            {{ formData.contactsphone }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人地址" prop="contactsadress">
            {{ formData.contactsadress }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="电子邮箱" prop="contactsemail">
            {{ formData.contactsemail }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="岗位" prop="station">
            {{ formData.station }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="称呼" prop="callname">
            {{ formData.callname }}
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            {{ formData.remarks }}
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-divider>银行账户</el-divider>
          <Bank :opposite="current" :readonly="true" />
        </el-col>
        <el-col :span="24">
          <el-divider>资质信息</el-divider>
          <AttachList
            :att-list="formData.attList"
            :local-list="localList"
            :readonly="true"
          />
        </el-col>
      </el-form>
    </el-row>

    <template #footer>
      <el-button @click="dialogFormVisible = false">取 消</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getPersonalData, getOpposite } from '@/api/contract/opposite'
  import Bank from './Bank.vue'
  import AttachList from './AttachList.vue'

  export default {
    name: 'MaintainDetail',
    components: { Bank, AttachList },
    data() {
      return {
        localList: [], // 本地缓存新增的附件列表
        formData: {
          flowId: 771398,
          counterpartno: undefined,
          budgetid: undefined,
          budgetname: undefined,
          othermoney: undefined,
          financemoney: undefined,
          counterparttype: undefined,
          oppositenature: undefined,
          projectrisk: undefined,
          cretificateno: undefined,
          pdate: undefined,
          servicetype: 1,
          totaltmoney: undefined,
          projectstagegoal: undefined,
          projectcondition: undefined,
          counterpartcode: undefined,
          counterpartnetaddress: undefined,
          director: undefined,
          counterpartphone: undefined,
          counterpartaddress: undefined,
          resultdescription: undefined,
          counterpartdesc: undefined,
          staffid: undefined,
          date: undefined,
          contacts: undefined,
          contactsphone: undefined,
          contactsadress: undefined,
          contactsemail: undefined,
          station: undefined,
          callname: undefined,
          remarks: undefined,
          pstartdateStr: undefined,
          penddateStr: undefined,
          
          cerType:0,
          attList: [],
        },
        title: '',
        dialogFormVisible: false,
        current: null,
      }
    },
    created() {},
    methods: {
      async fetchItem(row) {
        const {
          data: { attList },
        } = await getOpposite({
          flowId: row.flowid,
          budgetId: row.budgetid,
        })
        this.formData.attList = attList
        // 重置localList
        this.localList = []
      },
      showDetail(row) {
        this.fetchItem(row)
        this.fetchPersonalData()

        this.title = '查看'
        Object.keys(this.formData).forEach((key) => {
          this.formData[key] = row[key]
        })
        const { pstartdate, penddate } = row
        this.formData.pdate = [pstartdate, penddate]
        this.formData.penddateStr = pstartdate
        this.formData.pstartdateStr = penddate
        this.current = row
        this.formData.flowId = 771398
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
      },
      //获取数据
      async fetchPersonalData() {
        const res = await getPersonalData({
          flowId: this.formData.flowId,
        })
        const {
          counterpartno,
          createDate,
          staffInfo: { realname },
        } = res.data
        this.formData.staffid = realname
        this.formData.date = createDate
        // return {
        //   counterpartno,
        //   realname,
        //   createDate,
        // }
      },
    },
  }
</script>
<style scoped></style>
