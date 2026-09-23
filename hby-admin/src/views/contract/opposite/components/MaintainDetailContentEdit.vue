<!--
 * @Date: 2022-04-24 17:56:43
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-24 18:20:57
 * @FilePath: /hb-admin/src/views/contract/opposite/components/MaintainDetailContent.vue
-->
<template>
  <div>
    <el-row :gutter="15">
      <el-form
        ref="form"
        label-width="125px"
        :model="form"
        :rules="rules"
        size="medium"
      >
        <el-col :span="12">
          <el-form-item label="相对方编号">
            <el-input
              v-model="form.counterpartno"
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
              v-model="form.budgetname"
              clearable
              placeholder="请输入相对方名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="证件类型" prop="projectrisk">
            <el-select
              v-model="form.projectrisk"
              clearable
              placeholder="请选择证件类型"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in projectriskOptions"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="证件编号" prop="cretificateno">
            <el-input
              v-model="form.cretificateno"
              clearable
              placeholder="请输入证件编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="证件有效期" prop="pdate">
            <el-date-picker
              v-model="form.pdate"
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
            <el-radio-group v-model="form.servicetype" size="medium">
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
          <el-form-item label="注册资本(万元)" prop="totaltmoney">
            <el-input
              v-model="form.totaltmoney"
              clearable
              placeholder="请输入注册资本(万元)"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="法定代表人" prop="projectstagegoal">
            <el-input
              v-model="form.projectstagegoal"
              clearable
              placeholder="请输入法定代表人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="企业状态" prop="resultdescription">
            <el-input
              v-model="form.resultdescription"
              clearable
              placeholder="请输入企业状态"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="证件有效期类型" prop="cerType">
            <el-radio-group v-model="form.cerType" size="medium">
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
              v-model="form.pstartdateStr"
              clearable
              placeholder="请选择"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="form.cerType == 0">
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
              v-model="form.counterpartdesc"
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
              v-model="form.staffid"
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
              v-model="form.date"
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
              v-model="form.contacts"
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
            ref="bankref"
          />
        </el-col>
        <el-col :span="24">
          <el-divider>资质信息</el-divider>
          <AttachList
            :att-list="form.attList"
            :local-list="localList"
            @delete-att="handleDeleteAtt"
            @upload-success="handleUploadSuccess"
          />
        </el-col>
      </el-form>
    </el-row>
    <div style="text-align: right; margin-top: 10px">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save" :disabled="changeSaveBtn">
        确 定
      </el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
    </div>

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
    saveOpposite,
    updateOpposite,
    getPersonalData,
    getOpposite,
    removeOppsiteFile,
  } from '@/api/contract/opposite'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  // import { deleteAttach } from '@/api/contract/manage'
  import Bank from './Bank.vue'
  import AttachList from './AttachList.vue'
  export default {
    components: { AttachList, Bank, Resubmit },
    props: {
      formData: {
        type: Object,
        default: () => {},
      },
      // current: {
      //   type: Object,
      //   default: () => {},
      // },
    },
    data() {
      return {
        changeSaveBtn: false,
        localList: [], // 本地缓存新增的附件列表
        code: '',
        form: {
          // flowid: 622322,
          counterpartno: undefined,
          budgetid: undefined,
          budgetname: undefined,
          // othermoney: undefined,
          // financemoney: undefined,
          // counterparttype: undefined,
          // oppositenature: undefined,
          projectrisk: '统一社会信用代码',
          cretificateno: undefined,
          pdate: undefined,
          servicetype: '是',
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
          cerType: 0,
          date: undefined,
          penddateStr: undefined,
          pstartdateStr: undefined,
          contacts: undefined,
          // contactsphone: undefined,
          // contactsadress: undefined,
          // contactsemail: undefined,
          // station: undefined,
          // callname: undefined,
          // remarks: undefined,
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
            label: '个人身份证',
            value: '个人身份证',
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

        fromId: null,
        fromIdcopy: null,
        flowtaskinfoflowid: null,
        ymFromId: null,
        status: 0,
        uploadUrl: '',
      }
    },
    mounted() {
      // this.fetchItem(this.formData)
      this.form = this.formData
      this.current = this.formData
      this.changeSaveBtn = false
    },
    created() {
      this.$bus.$off('changeSaveBtn').$on('changeSaveBtn', () => {
        this.changeSaveBtn = true
      })
    },
    watch: {
      'formData.budgetid': {
        handler() {
          this.fetchItem(this.formData)
        },
      },
    },
    methods: {
      showEdit(row, fromId, flowtaskinfoflowid, ymFromId, status) {
        this.fetchItem(this.formData)

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
      fetchData() {
        // 刷新列表以获取银行信息
        // this.$emit('fetch-data')
        // this.fetchItem(this.formData)
      },
      async fetchItem(row) {
        if (row.budgetid) {
          const {
            data: { attList, bankInfoList, budget },
          } = await getOpposite({
            flowId: row.flowId,
            budgetId: row.budgetid,
          })
          Object.keys(this.form).forEach((key) => {
            this.form[key] = budget[key]
          })
          const {
            pstartdate,
            penddate,
            createStaff: { username },
            createtime,
          } = budget
          this.form.pdate = [pstartdate, penddate]
          this.form.staffid = username
          this.form.date = createtime
          this.form.attList = attList
          this.form.bankInfoList = bankInfoList
          this.formData.penddateStr = penddate
          this.formData.pstartdateStr = pstartdate
          // 重置localList
          console.log('zzz', this.form)
          this.localList = []

          this.current = budget
          this.$nextTick(() => {
            this.$refs['bankref'].fetchData()
          })
        }
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.current = null
        this.$bus.$emit('updateMsg', 0)
        this.dialogFormVisible = false
      },
      handleUploadSuccess(val) {
        this.localList.push(val.data)
      },
      async handleDeleteAtt(row, index) {
        const { code } = await removeOppsiteFile({
          attid: row.attid,
        })
        if (code == 1) {
          if (row.type == 'local') {
            this.localList.splice(index - this.formData.attList.length, 1)
          } else {
            this.form.attList.splice(index, 1)
            this.$set(this.form, 'attList', this.form.attList)
          }
        }
      },
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            if (this.form.cerType == 0) {
              if (!this.form.penddateStr || !this.form.pstartdateStr) {
                return this.$baseMessage(
                  '请选择有效日期',
                  'error',
                  'vab-hey-message-error'
                )
              }
            }
            this.form.flowid = 622322
            const fn = this.form.budgetid ? updateOpposite : saveOpposite
            const [pstartDate1, pendDate1] = this.form.pdate || []
            const arrAttid = this.localList.map((item) => item.attid)
            if (arrAttid && arrAttid.length) {
              this.form.attids = arrAttid.join(',')
            }
            const params = this.form.pdate
              ? Object.assign({ pstartDate1, pendDate1 }, this.form)
              : Object.assign(this.form)
            delete params.pdate
            delete params.bankInfoList
            delete params.attList
            delete params.pstartdate
            delete params.penddate
            const { msg, data, code } = await fn(params)
            if (code == 1) {
              if (!this.form.budgetid && data) {
                this.form.budgetid = data.budgetid
              }
              this.code = '1'
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
              this.current = data
            }
          }
        })
      },
    },
  }
</script>
