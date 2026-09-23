<template>
  <div>
    <el-row :gutter="15">
      <el-form
        ref="form"
        :disabled="disabled"
        label-width="150px"
        :model="formData"
        :rules="rules"
      >
        <el-col :span="24">
          <el-divider>合同基本信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractno">
            <el-input
              v-model="node.contractno"
              clearable
              disabled
              placeholder=""
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同名称" prop="contractname">
            <el-input
              v-model="node.contractname"
              clearable
              placeholder=""
              disabled
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="handleDetail(node)"
              :disabled="false"
            >
              详细
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同金额（元）" prop="contractmoney">
            <el-input
              v-model="node.contractmoney"
              clearable
              disabled
              placeholder=""
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同类型" prop="contracttype">
            <el-select
              v-model="node.contracttype"
              filterable
              disabled
              placeholder=""
              style="width: 100%"
            >
              <el-option
                v-for="item in typeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.label"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请用印日期" prop="createtime">
            <el-date-picker
              v-model="formData.createtime"
              clearable
              placeholder=""
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="项目名称" prop="topicname">
            <el-input
              v-model="node.topicname"
              clearable
              placeholder=""
              :style="{ width: '256px' }"
            />
          </el-form-item>
        </el-col> -->

        <el-col :span="12">
          <el-form-item label="印章所属主体" prop="counterparthank">
            <el-input
              v-model="formData.counterparthank"
              clearable
              placeholder="请输入印章所属主体"
              :style="{ width: '256px' }"
              :disabled="true"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.unit.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="印章名称">
            <!-- <el-input
              v-model="formData.counterpartcode"
              clearable
              placeholder="请输入印章名称"
              :style="{ width: '80%' }"
            /> -->
            <el-checkbox
              :indeterminate="isIndeterminate"
              v-model="checkAll"
              @change="handleCheckAllChange"
            >
              全选
            </el-checkbox>
            <div style="margin: 15px 0"></div>
            <el-checkbox-group
              v-model="counterpartcode"
              @change="handleCheckedCitiesChange"
            >
              <el-checkbox
                v-for="city in cities"
                :label="city"
                :key="city"
              ></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="用印信息">
            <el-input
              v-model="form.projectgoal"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入用印信息"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col> -->

        <!-- <el-col :span="24">
          <el-divider>附件</el-divider>
          <AttachList
            :att-list="formData.attList"
            :local-list="localList"
            @delete-att="handleDelAtt"
            @upload-success="handleUploadSuccess"
          />
        </el-col> -->
      </el-form>
    </el-row>
    <div style="text-align: right; margin-top: 10px" v-if="!disabled">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save" :disabled="changeSaveBtn">
        确 定
      </el-button>
      <el-button type="primary" @click="ymsubmit">提交</el-button>
    </div>
    <unit-options ref="unit" @selected="handleUnitSelected" />
    <CreateDetail ref="detail" />

    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import {
    getContractSealDetail,
    getContractSealDetailForAdd,
    getContractTypes,
    saveContractSeal,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import AttachList from './AttachList.vue'
  import CreateDetail from './contractsEdit/CreateDetail.vue'
  import UnitOptions from './options/unit.vue'
  import { formatDate } from '@/utils'
  import CandidateList from '@/components/CandidateList'
  import CandidateListNew from '@/views/contract/contractManage/components/CandidateList'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  const cityOptions = [
    '公司公章',
    '党委章',
    // "纪委章",
    '工会章',
    // "团支部章",
    // "党支部章",
    '法人章',
    // "财务专用章",
    // "发票专用章",
    // "预留印鉴章",
    // "部门章",
    '合同专用章',
    '其它印鉴',
  ]
  export default {
    name: 'ContractSealEdit',
    components: {
      AttachList,
      UnitOptions,
      CreateDetail,
      CandidateListNew,
      CandidateList,
      Resubmit,
    },
    data() {
      return {
        changeSaveBtn: false,
        submiting: false,
        disabled: false,
        node: {},
        // localList: [],
        formData: {
          flowId: 622324,
          contractid: undefined,
          budgetid: undefined,
          recordparent: undefined,

          counterparthank: undefined,
          createtime: undefined,
          projectgoal: undefined,
          sealorgid: undefined,
          // attList: [],
        },
        typeOptions: [],
        //不能放formData中，否则选择其中一个，就会全选，应该是element的bug
        counterpartcode: [],
        rules: {
          counterparthank: [
            {
              required: true,
              message: '请输入印章所属主体',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        disabled: false,
        curRow: {},
        checkAll: false,
        cities: cityOptions,
        isIndeterminate: false,
        fromId: null,
        fromIdcopy: null,
        flowtaskinfoflowid: null,
        ymFromId: null,
        status: 0,
      }
    },
    // watch: {
    //   formData(val) {
    //     this.form = val
    //   },
    // },
    mounted() {
      this.changeSaveBtn = false
    },
    created() {
      this.$bus.$off('changeSaveBtn').$on('changeSaveBtn', () => {
        this.changeSaveBtn = true
      })

      this.fetchTypes()
    },
    methods: {
      handlefzChange(e) {
        this.$refs['fzform'].clearValidate()
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        this.$refs['detail'].showDetail(row, row.contracttype)
      },
      //多选的处理方法
      handleCheckAllChange(val) {
        this.counterpartcode = val ? cityOptions : []
        this.isIndeterminate = false
      },
      handleCheckedCitiesChange(value) {
        let checkedCount = value.length
        this.checkAll = checkedCount === this.cities.length
        this.isIndeterminate =
          checkedCount > 0 && checkedCount < this.cities.length
      },
      //回调函数
      handleUnitSelected(node) {
        this.formData.sealorgid = node.id
        this.formData.counterparthank = node.label
        this.$forceUpdate()
      },

      async fetchTypes() {
        const res = await getContractTypes()
        this.typeOptions = res.typeofList.map((item) => {
          return {
            label: item.typename,
            value: item.typeid,
          }
        })
      },
      async fetchItem(row) {
        if (row.budgetid) {
          const res = await getContractSealDetail({
            budgetId: row.budgetid,
            flowId: this.formData.flowId,
          })
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data.tcpb[key]
          })
          this.counterpartcode = res.data.tcpb.counterpartcode.split(',')
          this.formData.flowId = res.data.flowId
          this.node = res.data.tcu
          // this.formData.attList = res.data.attList
        } else {
          const res = await getContractSealDetailForAdd({
            contractId: row.contractid,
            flowId: this.formData.flowId,
          })
          this.node = res.dataMap.unit
          this.formData.contractid = this.node && this.node.contractid
          this.formData.recordparent = this.node && this.node.recordparent
          this.formData.createtime = formatDate(new Date())
        }
      },
      showDetail(row) {
        this.title = '查看'
        this.disabled = true
        this.fetchItem(row)
      },

      showEdit(row, type, fromId, flowtaskinfoflowid, ymFromId, status) {
        // this.contractid = row.contractid
        this.disabled = false
        this.title = '编辑'
        this.curRow = row
        this.fetchItem(row)

        if (fromId) {
          this.fromId = fromId
          this.fromIdcopy = fromId // fromId为-1时，拷贝一份
        }
        if (flowtaskinfoflowid) {
          this.flowtaskinfoflowid = flowtaskinfoflowid
        }
        if (ymFromId) {
          this.ymFromId = ymFromId
        }
        this.status = status
      },
      close() {
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
        this.disabled = false
        this.$bus.$emit('updateMsg', 0)
      },
      // handleUploadSuccess(val) {
      //   this.localList.push(val.data)
      // },
      //保存
      save() {
        if (this.submiting) return
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let counterpartcodeList = ''
            if (this.counterpartcode.length > 0) {
              counterpartcodeList = this.counterpartcode.join(',')
            }

            // const arrAttid = this.localList.map((item) => item.attid)
            // this.formData.attids = arrAttid.join(',')

            const { msg, code } = await saveContractSeal({
              counterpartcode: counterpartcodeList,
              ...this.formData,
            })
            if (code == 1) {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            }
            // this.disabled = false
            // this.$emit('fetch-data')
            // this.fetchItem(this.curRow)
            // this.close()
          }
        })
      },
      //流程提交
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
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
</style>
