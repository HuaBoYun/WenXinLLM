<template>
  <el-dialog
    v-if="isDialog"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    :append-to-body="true"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form ref="form" label-width="120px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="黑名单类型" prop="blacktype">
            <el-radio-group
              v-model="form.blacktype"
              size="medium"
              @input="$forceUpdate()"
            >
              <el-radio :label="2">长期黑名单</el-radio>
              <el-radio :label="1">短期黑名单</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            v-if="form.blacktype == 1"
            label="黑名单有效期"
            prop="datetext"
          >
            <el-date-picker
              v-model="form.pageEffectDate"
              clearable
              format="yyyy-MM-dd"
              placeholder="黑名单有效期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptions"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>

    <MaintainDetailContent :current="current" :form-data="formData" />

    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <ProcessList :modal="false" ref="process" />
  </el-dialog>
  <div v-else>
    <el-row :gutter="15">
      <el-form ref="form" label-width="120px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="黑名单类型" prop="blacktype">
            <el-radio-group
              v-model="form.blacktype"
              size="medium"
              @input="$forceUpdate()"
            >
              <el-radio :label="2">长期黑名单</el-radio>
              <el-radio :label="1">短期黑名单</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            v-if="form.blacktype == 1"
            label="黑名单有效期"
            prop="datetext"
          >
            <el-date-picker
              v-model="form.pageEffectDate"
              clearable
              format="yyyy-MM-dd"
              placeholder="黑名单有效期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptions"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <MaintainDetailContent :current="current" :form-data="formData" />

    <div style="text-align: right; margin-top: 10px">
      <el-button type="primary" @click="saveZZ">确 定</el-button>
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
  import MaintainDetailContent from './MaintainDetailContent.vue'
  import { addToBlackList, getOpposite } from '@/api/contract/opposite'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  export default {
    name: 'MaintainDetail',
    components: { MaintainDetailContent, ProcessList, Resubmit },
    props: {
      isDialog: {
        type: Boolean,
        default: true,
      },
    },
    data() {
      return {
        localList: [], // 本地缓存新增的附件列表
        formData: {
          flowId: 622322,
          counterpartno: undefined,
          budgetid: undefined,
          budgetname: undefined,
          // othermoney: undefined,
          // financemoney: undefined,
          // counterparttype: undefined,
          // oppositenature: undefined,
          projectrisk: undefined,
          cretificateno: undefined,
          pdate: undefined,
          pstartdate: undefined,
          penddate: undefined,
          servicetype: 1,
          totaltmoney: undefined,
          projectstagegoal: undefined,
          // projectcondition: undefined,
          // counterpartcode: undefined,
          // counterpartnetaddress: undefined,
          // director: undefined,
          // counterpartphone: undefined,
          counterpartaddress: undefined,
          resultdescription: undefined,
          counterpartdesc: undefined,
          staffid: undefined,
          date: undefined,
          pstartdateStr: undefined,
          penddateStr: undefined,
          cerType: 0,
          contacts: undefined,
          // contactsphone: undefined,
          // contactsadress: undefined,
          // contactsemail: undefined,
          // station: undefined,
          // callname: undefined,
          // remarks: undefined,
          effectdate: undefined,
          blacktype: undefined,
          attList: [],
          bankInfoList: [],
        },
        title: '',
        dialogFormVisible: false,
        current: null,
        form: {
          pageEffectDate: '',
          blacktype: 2,
        },
        rules: {
          blacktype: [
            { required: true, message: '请选择黑名单类型', trigger: 'blur' },
          ],
        },

        fromId: null,
        fromIdcopy: null,
        flowtaskinfoflowid: null,
        ymFromId: null,
        status: 0,

        pickerOptions: {
          disabledDate(time) {
            return time.getTime() < Date.now()
          },
        },
      }
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.close()
        }
      })
    },
    created() {},
    methods: {
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

        this.form.pageEffectDate = budget.effectdate
        this.form.blacktype = budget.blacktype
        this.$forceUpdate()
        // 重置localList
        this.localList = []
        this.current = budget
      },
      async showDetail(row) {
        this.title = '查看'
        this.fetchItem(row)
        this.formData.flowId = 622322
        this.dialogFormVisible = true
      },
      async showEdit(row, fromId, flowtaskinfoflowid, ymFromId, status) {
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
        console.log('zz', status)
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { code, msg, brid } = await addToBlackList({
              ...this.form,
              budgetid: this.formData.budgetid,
            })
            // 拿brid当做flowid传入
            if (code == 1) {
              // tableId, fromId
              this.$refs['process'].save(3, brid)
              // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              // this.$emit('fetch-data')
              // this.close()
            }
          }
        })
      },
      saveZZ() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { code, msg } = await addToBlackList({
              ...this.form,
              budgetid: this.formData.budgetid,
            })
            if (code == 1) {
              // tableId, fromId
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              // this.$emit('fetch-data')
              // this.close()
            }
          }
        })
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
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
<style scoped></style>
