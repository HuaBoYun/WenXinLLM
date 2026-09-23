<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    append-to-body
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="15" v-loading="formLoad">
      <el-form
        ref="form"
        label-width="125px"
        :model="formData"
        :rules="rules"
        size="medium"
      >
        <el-col :span="12">
          <el-form-item label="相对方编号">
            <el-input
              v-model="formData.counterpartno"
              clearable
              placeholder="请输入相对方编号"
              :style="{ width: '100%' }"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="相对方名称" prop="budgetname">
            <el-input
              v-model="formData.budgetname"
              placeholder="请输入相对方名称"
              :style="{ width: '80%' }"
            />
            <el-button
              @click="handleXDF"
              style="margin-left: 10px"
              type="primary"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="证件类型" prop="projectrisk">
            <el-select
              v-model="formData.projectrisk"
              placeholder="请选择证件类型"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in projectriskOptions"
                :key="index"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="证件编号" prop="cretificateno">
            <el-input
              v-model="formData.cretificateno"
              placeholder="请输入证件编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="证件有效期" prop="pdate">
            <el-date-picker
              v-model="formData.pdate"
              clearable
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              :style="{ width: '100%' }"
              type="daterange"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="内部单位" prop="servicetype">
            <el-radio-group v-model="formData.servicetype" size="medium">
              <el-radio
                v-for="(item, index) in servicetypeOptions"
                :key="index"
                :disabled="item.disabled"
                :label="item.value"
              >
                {{ item.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="注册资本" prop="totaltmoney">
            <el-input
              v-model="formData.totaltmoney"
              placeholder="请输入注册资本(万元)"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="法定代表人" prop="projectstagegoal">
            <el-input
              v-model="formData.projectstagegoal"
              placeholder="请输入法定代表人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="企业状态" prop="resultdescription">
            <el-input
              v-model="formData.resultdescription"
              placeholder="请输入企业状态"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-show="false">
          <el-form-item label="成立日期" prop="establishDate">
            <el-input
              v-model="formData.establishDate"
              placeholder="成立日期"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-show="false">
          <el-form-item label="营业期限" prop="businessTerm">
            <el-input
              v-model="formData.businessTerm"
              placeholder="营业期限"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-show="false">
          <el-form-item label="所在地区" prop="counterpartaddress">
            <el-input
              v-model="formData.counterpartaddress"
              placeholder="所在地区"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="证件有效期类型" prop="cerType">
            <el-radio-group v-model="formData.cerType" size="medium">
              <el-radio
                v-for="(item, index) in cerTypeOptions"
                :key="index"
                :disabled="item.disabled"
                :label="item.value"
              >
                {{ item.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="有效期开始时间" prop="pstartdateStr">
            <el-date-picker
              v-model="formData.pstartdateStr"
              clearable
              placeholder="请选择"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.cerType == 0">
          <el-form-item label="有效期结束时间" prop="penddateStr">
            <el-date-picker
              v-model="formData.penddateStr"
              clearable
              placeholder="请选择"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="counterpartdesc">
            <el-input
              v-model="formData.counterpartdesc"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="staffid">
            <el-input
              v-model="formData.staffid"
              clearable
              :disabled="true"
              placeholder="张三"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建日期" prop="date">
            <el-input
              v-model="formData.date"
              clearable
              :disabled="true"
              placeholder="2021-08-15"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>联系人信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人" prop="contacts">
            <el-input
              v-model="formData.contacts"
              clearable
              placeholder="请输入联系人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>银行账户</el-divider>
        </el-col>
        <el-col :span="24">
          <Bank
            :opposite="current"
            :code="code"
            @fetch-data="fetchData"
            ref="bank"
          />
        </el-col>
        <el-col :span="24">
          <el-divider>资质信息</el-divider>
          <AttachList
            :att-list="formData.attList"
            :local-list="localList"
            @delete-att="handleDeleteAtt"
            @upload-success="handleUploadSuccess"
          />
        </el-col>
      </el-form>
    </el-row>
    <OppositeSelectModal ref="oppositeSelect" @selectCompany="selectCompany"></OppositeSelectModal>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save" :disabled="formLoad">
        确 定
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import {
  getOpposite,
  getPersonalData,
  removeOppsiteFile,
  saveOpposite,
  updateOpposite,
} from '@/api/contract/opposite'
import OppositeSelectModal from '@/views/contract/opposite/components/OppositeSelectModal.vue'
// import { deleteAttach } from '@/api/contract/manage'
import AttachList from './AttachList.vue'
import Bank from './Bank.vue'

export default {
  name: 'MaintainEdit',
  components: { Bank, AttachList, OppositeSelectModal },
  data() {
    return {
      formLoad: false,
      localList: [], // 本地缓存新增的附件列表
      code: '',
      formData: {
        flowId: 622322,
        counterpartno: undefined,
        budgetid: undefined,
        budgetname: undefined,
        cerType: 0,
        // othermoney: undefined,
        // financemoney: undefined,
        // counterparttype: undefined,
        // oppositenature: undefined,
        projectrisk: '统一社会信用代码',
        cretificateno: undefined,
        pdate: undefined,
        servicetype: '否',
        totaltmoney: undefined,
        projectstagegoal: undefined,
        // projectcondition: undefined,
        // counterpartcode: undefined,
        // counterpartnetaddress: undefined,
        // director: undefined,
        // counterpartphone: undefined,
        // counterpartaddress: undefined,
        resultdescription: undefined,
        counterpartdesc: undefined,
        staffid: undefined,
        date: undefined,
        pstartdateStr: undefined,
        penddateStr: undefined,
        contacts: undefined,
        // 工商信息补充字段
        establishDate: undefined,    // 成立日期
        businessTerm: undefined,     // 营业期限
        counterpartaddress: undefined, // 所在地区
        attList: [],
      },
      rules: {
        counterpartno: [
          {
            required: true,
            message: '请输入相对方编号',
            trigger: 'blur',
          },
        ],
        budgetname: [
          {
            required: true,
            message: '请输入相对方名称',
            trigger: 'blur',
          },
        ],
        // othermoney: [
        //   {
        //     required: true,
        //     message: '请输入第三方编号',
        //     trigger: 'blur',
        //   },
        // ],
        // financemoney: [],
        // counterparttype: [
        //   {
        //     required: true,
        //     message: '请输入相对方类型',
        //     trigger: 'blur',
        //   },
        // ],
        // oppositenature: [
        //   {
        //     required: true,
        //     message: '请输入相对方性质',
        //     trigger: 'blur',
        //   },
        // ],
        projectrisk: [
          {
            required: false,
            message: '请选择证件类型',
            trigger: 'change',
          },
        ],
        cretificateno: [
          {
            required: false,
            message: '请输入证件编号',
            trigger: 'blur',
          },
        ],
        pdate: [
          {
            required: false,
            message: '证件有效期不能为空',
            trigger: 'change',
          },
        ],
        servicetype: [],
        totaltmoney: [
          {
            required: false,
            message: '请输入注册资本(万元)',
            trigger: 'blur',
          },
        ],
        projectstagegoal: [],
        // projectcondition: [],
        // counterpartcode: [],
        // counterpartnetaddress: [],
        // director: [
        //   {
        //     required: true,
        //     message: '请输入负责人',
        //     trigger: 'blur',
        //   },
        // ],
        // counterpartphone: [
        //   {
        //     required: true,
        //     message: '请输入负责人联系电话',
        //     trigger: 'blur',
        //   },
        //   {
        //     pattern: /^\d*$/,
        //     message: '请输入合法手机号/电话号',
        //     trigger: 'blur',
        //   },
        // ],

        // counterpartaddress: [
        //   {
        //     required: true,
        //     message: '请输入地址',
        //     trigger: 'blur',
        //   },
        // ],
        resultdescription: [
          {
            required: false,
            message: '请输入纳税人识别号',
            trigger: 'blur',
          },
        ],
        counterpartdesc: [],
        staffid: [],
        date: [],
        contacts: [],
        // contactsphone: [
        //   {
        //     pattern: /^\d*$/,
        //     message: '请输入合法手机号/电话号',
        //     trigger: 'blur',
        //   },
        // ],
        // contactsadress: [],
        // contactsemail: [],
        // station: [],
        // callname: [],
        // remarks: [],
      },
      projectriskOptions: [
        {
          label: '统一社会信用代码',
          value: '统一社会信用代码',
        },
        {
          label: '证件自然人',
          value: '证件自然人',
        },
      ],
      servicetypeOptions: [
        {
          label: '是',
          value: '是',
        },
        {
          label: '否',
          value: '否',
        },
      ],
      cerTypeOptions: [
        {
          label: '短期',
          value: 0,
        },
        {
          label: '长期',
          value: 1,
        },
      ],
      title: '',
      dialogFormVisible: false,
      current: null,
    }
  },
  created() {},
  methods: {
    fetchData() {
      // 刷新列表以获取银行信息
      this.$emit('fetch-data')
    },
    async fetchItem(row) {
      const {
        data: { attList, bankInfoList, budget },
      } = await getOpposite({
        flowId: row.flowid,
        budgetId: row.budgetid,
      })
      Object.keys(this.formData).forEach((key) => {
        this.formData[key] = budget[key]
      })
      const {
        pstartdate,
        penddate,
        createStaff: { username },
        createtime,
      } = budget

      this.formData.pdate = [pstartdate, penddate]
      this.formData.staffid = username
      this.formData.date = createtime
      this.formData.attList = attList
      this.formData.bankInfoList = bankInfoList
      this.formData.penddateStr = penddate
      this.formData.pstartdateStr = pstartdate
      // 重置localList
      this.localList = []
      this.current = budget
    },
    async showEdit(row) {
      if (!row) {
        this.title = '添加'
        const res = await getPersonalData({
          flowId: this.formData.flowId,
        })
        const {
          counterpartno,
          createDate,
          staffInfo: { realname },
        } = res.data
        this.formData.counterpartno = counterpartno
        this.formData.staffid = realname
        this.formData.date = createDate
        // 重置localList
        this.localList = []
      } else {
        this.title = '编辑'
        this.code = '1'
        this.fetchItem(row)
      }
      this.formData.flowId = 622322
      this.dialogFormVisible = true

      this.$nextTick(() => {
        this.$refs['bank'].fetchData()
      })
    },
    close() {
      this.$emit('fetch-data')
      this.$refs['form'].resetFields()
      this.formData = this.$options.data().formData
      this.current = null
      this.dialogFormVisible = false
    },
    //附件成功回调
    handleUploadSuccess(val) {
      this.localList.push(val.data)
    },
    //删除附件
    async handleDeleteAtt(row, index) {
      const { code } = await removeOppsiteFile({
        attid: row.attid,
      })
      if (code == 1) {
        if (row.type == 'local') {
          this.localList.splice(index - this.formData.attList.length, 1)
        } else {
          this.formData.attList.splice(index, 1)
          this.$set(this.formData, 'attList', this.formData.attList)
        }
      }
    },
    save() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          if (this.formData.cerType == 0) {
            if (!this.formData.penddateStr || !this.formData.pstartdateStr) {
              return this.$baseMessage(
                '请选择有效日期',
                'error',
                'vab-hey-message-error'
              )
            }
          }
          this.formLoad = true
          const fn = this.formData.budgetid ? updateOpposite : saveOpposite
          const [pstartDate1, pendDate1] = this.formData.pdate || []
          const arrAttid = this.localList.map((item) => item.attid)
          if (arrAttid && arrAttid.length) {
            this.formData.attids = arrAttid.join(',')
          }
          const params = this.formData.pdate
            ? Object.assign({ pstartDate1, pendDate1 }, this.formData)
            : Object.assign(this.formData)
          delete params.pdate
          delete params.bankInfoList
          delete params.attList

          const { msg, data, code } = await fn(params)
          if (code == 1) {
            if (!this.formData.budgetid && data) {
              this.formData.budgetid = data.budgetid
            }
            this.code = '1'
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.current = data
            // this.dialogFormVisible = false
          }
          this.formLoad = false
        }
      })
    },
    //相对方
    handleXDF() {
      this.$refs['oppositeSelect'].showEdit()
    },
    //回调：将工商信息字段带入表单
    selectCompany(val) {
      this.$set(this.formData, 'budgetname', val.entName || '')
      this.$set(this.formData, 'cretificateno', val.creditCode || '')
      this.$set(this.formData, 'projectstagegoal', val.legalPerson || '')
      this.$set(this.formData, 'totaltmoney', val.regCapital || '')
      this.$set(this.formData, 'resultdescription', val.entStatus || '')
      this.$set(this.formData, 'establishDate', val.establishDate || '')
      // 营业期限：拼接 businessFrom 至 businessTo（隐藏字段，仅保留数据）
      const bFrom = val.businessFrom || ''
      const bTo = val.businessTo || ''
      this.$set(this.formData, 'businessTerm', bFrom && bTo ? bFrom + ' 至 ' + bTo : (bFrom || bTo || ''))
      // 所在地区：拼接省市区（隐藏字段，仅保留数据）
      const area = (val.province || '') + (val.city || '') + (val.district || '')
      this.$set(this.formData, 'counterpartaddress', area)
      // 将经营期限的开始/结束时间填入有效期开始/结束时间
      this.$set(this.formData, 'pstartdateStr', bFrom || '')
      this.$set(this.formData, 'penddateStr', bTo || '')
    },
  },
}
</script>
<style scoped></style>
