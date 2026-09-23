<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row v-if="show == 0" :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="诉讼阶段" prop="porceedstage">
            <el-input
              v-model.trim="form.porceedstage"
              clearable
              placeholder="请选择诉讼阶段"
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              @click="$refs.ssjd.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷主题">
            <el-input
              v-model.trim="form.disputeitem"
              clearable
              placeholder="请输入纠纷主题"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="案号">
            <el-input
              v-model.trim="form.proceedno"
              clearable
              placeholder="请输入案号"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审理法院">
            <el-input
              v-model.trim="form.court"
              clearable
              placeholder="请输入审理法院"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="原告">
            <el-input
              v-model.trim="form.plaintiff"
              clearable
              placeholder="请输入原告"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被告">
            <el-input
              v-model.trim="form.defendant"
              clearable
              placeholder="请输入被告"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被冻结账户开户行" prop="frozenblank">
            <el-input
              v-model.trim="form.frozenblank"
              clearable
              placeholder="请输入被冻结账户开户行"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被冻结账户账号">
            <el-input
              v-model.trim="form.frozenaccount"
              clearable
              placeholder="请输入被冻结账户账号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请冻结金额（元）">
            <el-input
              v-model.trim="form.applyamount"
              clearable
              placeholder="请输入申请冻结金额（元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实际被冻结金额（元）">
            <el-input
              v-model.trim="form.frozenamount"
              clearable
              placeholder="请输入实际被冻结金额（元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被扣划金额（元）">
            <el-input
              v-model.trim="form.kouhuaamount"
              clearable
              placeholder="请输入被扣划金额（元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账户性质	">
            <el-input
              v-model.trim="form.accountnature"
              clearable
              placeholder="请输入账户性质	"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="冻结起始日">
            <el-date-picker
              v-model.trim="form.startdate"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入冻结起始日"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="冻结期届满日">
            <el-date-picker
              v-model.trim="form.enddate"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入冻结期届满日"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注">
            <el-input
              v-model.trim="form.frozenmemo"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <el-row v-if="show == 1" :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="诉讼阶段" prop="porceedstage">
            <span>{{ form.porceedstage }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷主题">
            <span>{{ form.disputeitem }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="案号">
            <span>{{ form.proceedno }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审理法院">
            <span>{{ form.court }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="原告">
            <span>{{ form.plaintiff }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被告">
            <span>{{ form.defendant }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被冻结账户开户行" prop="frozenblank">
            <span>{{ form.frozenblank }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被冻结账户账号">
            <span>{{ form.frozenaccount }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请冻结金额（元）">
            <span>{{ form.applyamount }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实际被冻结金额（元）">
            <span>{{ form.frozenamount }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被扣划金额（元）">
            <span>{{ form.kouhuaamount }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账户性质	">
            <span>{{ form.accountnature }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="冻结起始日">
            <span>{{ form.startdate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="冻结期届满日">
            <span>{{ form.enddate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注">
            <span>{{ form.frozenmemo }}</span>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button v-if="show == 0" type="primary" @click="save">确 定</el-button>
    </template>
    <ssjd-options ref="ssjd" @selected="handleSsjd" />
  </el-dialog>
</template>

<script>
  import { frozenAccountSave, frozenAccountModify } from '@/api/fwgl/legal'
  import ssjdOptions from './options/ssjd.vue'

  export default {
    name: 'DraftEdit',
    components: { ssjdOptions },
    data() {
      return {
        form: {
          inforid: undefined,
          flowid: undefined,
          flowname: undefined,
          proceedId: undefined,
          proceedid: undefined,
          porceedstage: undefined,
          disputeitem: undefined,
          proceedno: undefined,
          court: undefined,
          plaintiff: undefined,
          defendant: undefined,
          frozenblank: undefined,
          frozenaccount: undefined,
          applyamount: undefined,
          frozenamount: undefined,
          kouhuaamount: undefined,
          accountnature: undefined,
          startdate: undefined,
          enddate: undefined,
          frozenmemo: undefined,
        },
        rules: {
          porceedstage: [
            {
              required: true,
              message: '请输入诉讼阶段',
              trigger: 'blur',
            },
          ],
          frozenblank: [
            {
              required: true,
              message: '请输入被冻结账户开户行',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        radio: '',
        show: 0,
      }
    },
    created() {},
    methods: {
      showEdit(row, disabled) {
        this.show = 0
        if (!row) {
          this.title = '添加'
        } else {
          if (disabled) {
            this.title = '查看'
            this.show = 1
          } else {
            this.title = '编辑'
          }
          Object.keys(this.form).forEach((key) => (this.form[key] = row[key]))
          this.form.proceedId = row.proceedid
        }
        this.dialogFormVisible = true
      },
      showDetail(row) {
        this.showEdit(row, true)
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            if (this.title === '添加') {
              const { msg } = await frozenAccountSave(this.form)
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            } else if (this.title === '编辑') {
              const { msg } = await frozenAccountModify(this.form)
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            }
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      handleSsjd(val) {
        this.form.proceedId = val.proceedid
        this.form.porceedstage = val.porceedstage
        this.form.disputeitem = val.disputeitem
        this.form.proceedno = val.proceedno
        this.form.court = val.court
        this.form.plaintiff = val.plaintiff
        this.form.defendant = val.defendant
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
