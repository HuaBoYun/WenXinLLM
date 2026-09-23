<!--
 * @Date: 2022-05-07 09:37:21
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-07 10:55:33
 * @FilePath: /hb-admin/src/views/setting/system/components/YwcjEdit/BasicDetail.vue
-->
<template>
  <el-row :gutter="15">
    <el-form
      ref="elForm"
      label-width="125px"
      :model="formData"
      :rules="rules"
      size="small"
    >
      <el-col :span="24">
        <el-divider>业务单元</el-divider>
      </el-col>
      <el-col :span="12">
        <el-form-item label="业务编号" prop="flownumber">
          {{ formData.flownumber }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="流程名称" prop="flowname">
          {{ formData.flowname }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="责任部门" prop="deparChargeName">
          {{ formData.deparChargeName }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="相关部门" prop="departissName">
          {{ formData.departissName }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="业务名称" prop="bussinessname">
          {{ formData.bussinessname }}
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="业务描述" prop="bussinessdes">
          {{ formData.bussinessdes }}
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-divider>风险描述</el-divider>
      </el-col>
      <el-col :span="12">
        <el-form-item label="风险编号" prop="risknumber">
          {{ formData.risknumber }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="风险名称" prop="riskname">
          {{ formData.riskname }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="版本" prop="version">
          {{ versionF }}
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="风险描述" prop="riskdes">
          {{ formData.riskdes }}
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-divider>控制措施</el-divider>
      </el-col>
      <el-col :span="12">
        <el-form-item label="风险控制点编号" prop="controlnumber">
          {{ formData.controlnumber }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制责任人" prop="controlmanager">
          {{ formData.controlmanager }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="流程分类" prop="toplevelflowcat">
          {{ mapOptions(formData.toplevelflowcat, toplevelflowcatOptions) }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制频率" prop="controlfrequency">
          {{ formData.controlfrequency }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制类型" prop="controltype">
          {{ mapOptions(formData.controltype, toplevelflowcatOptions) }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制手段" prop="controlmethod">
          {{ formData.controlmethod }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="是否关键控制" prop="keycontrol">
          {{ formData.keycontrol }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制是否有效" prop="effective">
          {{ formData.effective }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="是否进行控制测试" prop="controltest">
          {{ formData.controltest }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="财务报表认定" prop="financialreportidentify">
          {{ formData.financialreportidentify }}
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="风险控制点描述" prop="controldes">
          {{ formData.controldes }}
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="控制措施" prop="conkzcs">
          {{ formData.conkzcs }}
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-divider>审计程序</el-divider>
      </el-col>
      <el-col :span="24">
        <el-form-item label="审计程序" prop="riskprogram">
          {{ formData.riskprogram }}
        </el-form-item>
      </el-col>
    </el-form>
  </el-row>
</template>
<script>
  import {
    getFlowTypeOptions,
    getBusinessProcessDetail,
  } from '@/api/setting/system'

  export default {
    inject: ['fatherFetchData'],
    props: {
      action: {
        type: [String],
        default: '添加',
      },
      fatherflowid: {
        type: [Number, String],
        default: undefined,
      },
    },
    data() {
      return {
        formData: {
          flowid: undefined,
          flownumber: undefined,
          flowname: undefined,
          departincharge: undefined,
          deparChargeName: undefined, //text
          departassist: undefined,
          departissName: undefined, // text
          bussinessdes: undefined,
          bussinessname: undefined,
          risknumber: undefined,
          riskname: undefined,
          version: 1,
          riskdes: undefined,
          controlnumber: undefined,
          controlmanager: undefined,
          toplevelflowcat: undefined,
          controlfrequency: undefined,
          riskprogram: undefined,
          controltype: undefined,
          controlmethod: undefined,
          keycontrol: undefined,
          effective: undefined,
          controltest: undefined,
          financialreportidentify: undefined,
          controldes: undefined,
          conkzcs: undefined,
          // 修改时，需要原路返回到后端的几个id
          riskid: undefined,
          bussinessid: undefined,
          conmatid: undefined,
        },
        controlmanagerOptions: [],
        toplevelflowcatOptions: [],
        controltypeOptions: [
          { label: '请选择', value: '0' },
          { label: '预防性控制', value: '1' },
          { label: '发现性控制', value: '2' },
          { label: '纠正性控制', value: '3' },
        ],
        controlmethodOptions: [
          { label: '请选择', value: '0' },
          { label: '手工', value: '1' },
          { label: '自动', value: '2' },
          { label: '依赖手工的自动化', value: '3' },
        ],
        booleanOptions: [
          { label: '请选择', value: '0' },
          { label: '是', value: '1' },
          { label: '否', value: '2' },
        ],
        financialreportidentifyOptions: [
          { label: '请选择', value: '0' },
          { label: '存在与发生', value: '1' },
          { label: '完整性', value: '2' },
          { label: '权利与义务', value: '3' },
          { label: '估价与分摊', value: '4' },
          { label: '表达与披露', value: '5' },
        ],
        rules: {
          flownumber: [
            {
              required: true,
              message: '请输入业务编号',
              trigger: 'blur',
            },
          ],
          flowname: [
            {
              required: true,
              message: '请输入流程名称',
              trigger: 'blur',
            },
          ],
          departincharge: [
            {
              required: true,
              message: '请选择责任部门',
              trigger: 'blur',
            },
          ],
          risknumber: [
            {
              required: true,
              message: '请输入风险编号',
              trigger: 'blur',
            },
          ],
          riskname: [
            {
              required: true,
              message: '请输入风险名称',
              trigger: 'blur',
            },
          ],
          version: [
            {
              required: true,
              message: '请输入版本',
              trigger: 'blur',
            },
          ],
          controlnumber: [
            {
              required: true,
              message: '请输入风险控制点编号',
              trigger: 'blur',
            },
          ],
          toplevelflowcat: [
            {
              required: true,
              message: '请选择流程分类',
              trigger: 'blur',
            },
          ],
          controldes: [
            {
              required: true,
              message: '请输入风险控制点描述',
              trigger: 'blur',
            },
          ],
          conkzcs: [
            {
              required: true,
              message: '请输入控制措施',
              trigger: 'blur',
            },
          ],
        },
      }
    },
    computed: {
      versionF() {
        return this.formData.version + '.0'
      },
    },
    created() {
      this.fetchFlowTypes()
    },
    methods: {
      async fetchDetail(row) {
        const { code, data } = await getBusinessProcessDetail({
          flowid: row.flowid,
        })
        if (code == 1) {
          const { controlMatrix, dp, risk, riskBussiness } = data
          const source = { ...controlMatrix, ...dp, ...risk, ...riskBussiness }
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = source[key]
          })
          // version使用dp里的
          this.formData.version = dp.version
        }
      },
      async fetchFlowTypes() {
        const { code, data } = await getFlowTypeOptions()
        if (code == 1) {
          this.toplevelflowcatOptions = data
        }
      },
      mapOptions(value, Options) {
        return Options.filter((item) => item.value == value)[0]
      },
    },
  }
</script>
