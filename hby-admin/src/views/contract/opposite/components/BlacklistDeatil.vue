<template>
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
          {{ formData.cerType == 0 ? '短期' : '长期' }}
        </el-form-item>
      </el-col>
      <el-col :span="12" v-if="formData.cerType == 0">
        <el-form-item label="证件有效期">
          {{ formData.pstartdateStr }} 至 {{ formData.penddateStr }}
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
      <el-col :span="12">
        <el-form ref="form" label-width="120px" :model="formData" inline>
          <el-form-item label="黑名单类型" prop="blacktype">
            <el-radio-group v-model="formData.blacktype" size="medium">
              <el-radio :label="2">长期黑名单</el-radio>
              <el-radio :label="1">短期黑名单</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
            v-if="formData.blacktype == 1"
            label="黑名单有效期"
            prop="effectdate"
          >
            <el-date-picker
              v-model="formData.effectdate"
              clearable
              format="yyyy-MM-dd"
              placeholder="黑名单有效期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptions"
              :disabled="true"
            />
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="24">
        <el-divider>联系人信息</el-divider>
      </el-col>
      <el-col :span="12">
        <el-form-item label="联系人" prop="contacts">
          {{ formData.contacts }}
        </el-form-item>
      </el-col>
      <!--     <el-col :span="12">
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
</template>

<script>
import Bank from './Bank.vue'
import AttachList from './AttachList.vue'
export default {
  name: 'CounterpartDetail',
  components: { Bank, AttachList },
  props: {
    current: {
      type: Object,
      default: null,
    },
    formData: {
      type: Object,
      default: () => {},
    },
    localList: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {}
  },
}
</script>

<style></style>
