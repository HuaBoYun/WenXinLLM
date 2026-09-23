<!--
 * @Date: 2022-04-29 11:05:09
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-06 17:45:25
 * @FilePath: /hb-admin/src/views/setting/system/components/YwcjEdit/BasicForm.vue
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
          <el-input
            v-model="formData.flownumber"
            :disabled="action == '编辑'"
            placeholder="请输入业务编号"
            :style="{ width: '100%' }"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="流程名称" prop="flowname">
          <el-input
            v-model="formData.flowname"
            placeholder="请输入流程名称"
            :style="{ width: '100%' }"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="责任部门" prop="deparChargeName">
          <el-input
            v-model="formData.deparChargeName"
            clearable
            placeholder="请选择责任部门"
            style="width: 187px"
          />
          <el-button
            :style="{ marginLeft: '10px' }"
            type="primary"
            @click="
              $refs.department.show({
                field: 'deparChargeName',
                multiple: false,
              })
            "
          >
            选择
          </el-button>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="相关部门" prop="departissName">
          <el-input
            v-model="formData.departissName"
            clearable
            placeholder="请选择相关部门"
            style="width: 187px"
          />
          <el-button
            :style="{ marginLeft: '10px' }"
            type="primary"
            @click="
              $refs.department.show({ field: 'departissName', multiple: true })
            "
          >
            选择
          </el-button>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="业务名称" prop="bussinessname">
          <el-input
            v-model="formData.bussinessname"
            clearable
            placeholder="请输入业务名称"
            :style="{ width: '100%' }"
          />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="业务描述" prop="bussinessdes">
          <el-input
            v-model="formData.bussinessdes"
            :autosize="{ minRows: 4, maxRows: 4 }"
            placeholder="请输入业务描述"
            :style="{ width: '100%' }"
            type="textarea"
          />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-divider>风险描述</el-divider>
      </el-col>
      <el-col :span="12">
        <el-form-item label="风险编号" prop="risknumber">
          <el-input
            v-model="formData.risknumber"
            :disabled="action == '编辑'"
            placeholder="请输入风险编号"
            :style="{ width: '100%' }"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="风险名称" prop="riskname">
          <el-input
            v-model="formData.riskname"
            clearable
            placeholder="请输入风险名称"
            :style="{ width: '100%' }"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="版本" prop="version">
          <el-input v-model="versionF" readonly :style="{ width: '100%' }" />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="风险描述" prop="riskdes">
          <el-input
            v-model="formData.riskdes"
            :autosize="{ minRows: 4, maxRows: 4 }"
            placeholder="请输入风险描述"
            :style="{ width: '100%' }"
            type="textarea"
          />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-divider>控制措施</el-divider>
      </el-col>
      <el-col :span="12">
        <el-form-item label="风险控制点编号" prop="controlnumber">
          <el-input
            v-model="formData.controlnumber"
            :disabled="action == '编辑'"
            placeholder="请输入风险控制点编号"
            :style="{ width: '100%' }"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制责任人" prop="controlmanager">
          <el-input
            v-model="formData.controlmanager"
            clearable
            placeholder="请选择控制责任人"
            style="width: 187px"
          />
          <el-button
            :style="{ marginLeft: '10px' }"
            type="primary"
            @click="$refs.executor.show()"
          >
            选择
          </el-button>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="流程分类" prop="toplevelflowcat">
          <el-select
            v-model="formData.toplevelflowcat"
            clearable
            filterable
            placeholder="请选择流程分类"
            :style="{ width: '100%' }"
          >
            <el-option
              v-for="(item, index) in toplevelflowcatOptions"
              :key="index"
              :label="item.flowname"
              :value="item.flowid"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制频率" prop="controlfrequency">
          <el-input
            v-model="formData.controlfrequency"
            clearable
            placeholder="请输入控制频率"
            :style="{ width: '100%' }"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制类型" prop="controltype">
          <el-select
            v-model="formData.controltype"
            clearable
            filterable
            placeholder="请选择控制类型"
            :style="{ width: '100%' }"
          >
            <el-option
              v-for="(item, index) in controltypeOptions"
              :key="index"
              :disabled="item.disabled"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制手段" prop="controlmethod">
          <el-select
            v-model="formData.controlmethod"
            clearable
            filterable
            placeholder="请选择控制手段"
            :style="{ width: '100%' }"
          >
            <el-option
              v-for="(item, index) in controlmethodOptions"
              :key="index"
              :disabled="item.disabled"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="是否关键控制" prop="keycontrol">
          <el-select
            v-model="formData.keycontrol"
            clearable
            filterable
            placeholder="请选择是否关键控制"
            :style="{ width: '100%' }"
          >
            <el-option
              v-for="(item, index) in booleanOptions"
              :key="index"
              :disabled="item.disabled"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制是否有效" prop="effective">
          <el-select
            v-model="formData.effective"
            clearable
            filterable
            placeholder="请选择控制是否有效"
            :style="{ width: '100%' }"
          >
            <el-option
              v-for="(item, index) in booleanOptions"
              :key="index"
              :disabled="item.disabled"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="是否进行控制测试" prop="controltest">
          <el-select
            v-model="formData.controltest"
            clearable
            filterable
            placeholder="请选择是否进行控制测试"
            :style="{ width: '100%' }"
          >
            <el-option
              v-for="(item, index) in booleanOptions"
              :key="index"
              :disabled="item.disabled"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="财务报表认定" prop="financialreportidentify">
          <el-select
            v-model="formData.financialreportidentify"
            clearable
            filterable
            placeholder="请选择财务报表认定"
            :style="{ width: '100%' }"
          >
            <el-option
              v-for="(item, index) in financialreportidentifyOptions"
              :key="index"
              :disabled="item.disabled"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="风险控制点描述" prop="controldes">
          <el-input
            v-model="formData.controldes"
            :autosize="{ minRows: 4, maxRows: 4 }"
            placeholder="请输入风险控制点描述"
            :style="{ width: '100%' }"
            type="textarea"
          />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="控制措施" prop="conkzcs">
          <el-input
            v-model="formData.conkzcs"
            :autosize="{ minRows: 4, maxRows: 4 }"
            placeholder="请输入控制措施"
            :style="{ width: '100%' }"
            type="textarea"
          />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-divider>审计程序</el-divider>
      </el-col>
      <el-col :span="24">
        <el-form-item label="审计程序" prop="riskprogram">
          <el-input
            v-model="formData.riskprogram"
            :autosize="{ minRows: 4, maxRows: 4 }"
            placeholder="请输入审计程序"
            :style="{ width: '100%' }"
            type="textarea"
          />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item>
          <el-button type="primary" @click="save">保 存</el-button>
        </el-form-item>
      </el-col>
    </el-form>
    <Department ref="department" @selected="handleDepartmentSelected" />
    <Executor ref="executor" @selected="handleExecutorSelected" />
  </el-row>
</template>
<script>
  import {
    saveBusinessProcess,
    getFlowTypeOptions,
    getBusinessProcessDetail,
  } from '@/api/setting/system'
  import Department from '@/views/setting/system/components/Department.vue'
  import Executor from '@/views/setting/system/components/Executor'

  export default {
    components: { Department, Executor },
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
          controltype: '0',
          controlmethod: '0',
          keycontrol: '0',
          effective: '0',
          controltest: '0',
          financialreportidentify: '0',
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
      async save() {
        this.formData.fatherflowid = this.fatherflowid
        const { code, msg, data } = await saveBusinessProcess(this.formData)

        if (data && !this.formData.flowid) {
          this.formData.flowid = data.flowid
          this.formData.flownumber = data.flownumber
          this.formData.flowname = data.flowname
          const { data: mdata } = await getBusinessProcessDetail({
            flowid: data.flowid,
          })
          if (mdata && mdata.controlMatrix) {
            this.formData.conmatid = mdata.controlMatrix.conmatid
            this.formData.riskid = mdata.risk.riskid
            this.formData.bussinessid = mdata.riskBussiness.bussinessid
          }

          this.$emit('input', '编辑')
        }
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.fatherFetchData()
        }
      },
      handleDepartmentSelected(data) {
        const { field, checked } = data
        if (field == 'departissName') {
          this.formData['departassist'] = checked
            .map((item) => item.id)
            .join(',')
          this.formData['departissName'] = checked
            .map((item) => item.text)
            .join(',')
        } else if (field == 'deparChargeName') {
          this.formData['departincharge'] = checked.id
          this.formData['deparChargeName'] = checked.text
        }
      },
      handleExecutorSelected(data) {
        this.formData['controlmanager'] = data.realname
      },
    },
  }
</script>
