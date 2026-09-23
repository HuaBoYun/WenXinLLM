<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row v-if="show == 0" :gutter="15">
      <el-form ref="form" label-width="140px" :model="formData" :rules="rules">
        <el-col :span="12">
          <el-form-item label="隶属纠纷" prop="disputeitem">
            <el-input
              v-model.trim="formData.disputeitem"
              clearable
              placeholder="请选择隶属纠纷"
              readonly
              :style="{ width: '256px' }"
              @input="change()"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              @click="$refs.xzjf.show(1)"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="起诉类型">
            <el-input
              v-model.trim="formData.whethersuedName"
              clearable
              placeholder="请输入起诉类型"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否申请保全" prop="applypreservation">
            <el-radio-group v-model.trim="formData.applypreservation">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="2">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="保全资产数额（万元）">
            <el-input
              v-model.trim="formData.preservedamount"
              clearable
              placeholder="请输入保全资产数额（万元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="保全资产性质">
            <el-input
              v-model.trim="formData.preservednature"
              clearable
              placeholder="请输入保全资产性质"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行金额（万元）">
            <el-input
              v-model.trim="formData.exceteamount"
              clearable
              placeholder="请输入执行金额（万元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否执行扣划">
            <el-radio-group v-model.trim="formData.isperformed">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="2">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否解除保全">
            <el-radio-group v-model.trim="formData.iscancel">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="2">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <el-row v-if="show == 1" :gutter="15">
      <el-form ref="form" label-width="140px" :model="formData" :rules="rules">
        <el-col :span="12">
          <el-form-item label="隶属纠纷" prop="disputeitem">
            <span>{{ formData.disputeitem }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="起诉类型">
            <span>{{ formData.whethersuedName === 1 ? '起诉' : '被诉' }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否申请保全" prop="applypreservation">
            <span>{{ formData.applypreservation === 1 ? '是' : '否' }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="保全资产数额（万元）">
            <span>{{ formData.preservedamount }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="保全资产性质">
            <span>{{ formData.preservednature }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行金额（万元）">
            <span>{{ formData.exceteamount }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否执行扣划">
            <span>{{ formData.isperformed === 1 ? '是' : '否' }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否解除保全">
            <span>{{ formData.iscancel === 1 ? '是' : '否' }}</span>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button v-if="show == 0" type="primary" @click="save">确 定</el-button>
    </template>
    <xzjf-options ref="xzjf" @selected="handleXzjf" />
  </el-dialog>
</template>

<script>
  import {
    getQualificationSave,
    getQualificationModify,
  } from '@/api/fwgl/legal'
  import xzjfOptions from './options/xzjf.vue'

  export default {
    name: 'DraftEdit',
    components: { xzjfOptions },
    data() {
      return {
        form: {
          code: '',
          name: '',
          org: '',
          intro: '',
          remark: '',
        },
        formData: {
          // 隶属纠纷
          disputeId: null,
          disputeitem: null,
          // 起诉类型
          whethersued: null,
          whethersuedName: null,
          // 是否保全 1.是 2.
          applypreservation: 1,
          // 保全资产数额
          preservedamount: null,
          // 保全资产性质
          preservednature: null,
          // 执行金额
          exceteamount: null,
          // 是否执行扣划 1.是 2
          isperformed: 1,
          // 是否解除保全 1.是 2
          iscancel: 1,
        },
        rules: {
          disputeitem: [
            {
              required: true,
              message: '请选择隶属纠纷',
              trigger: 'blur',
            },
          ],
          applypreservation: [
            {
              required: true,
              message: '请选择申请保全',
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
      change() {
        this.$forceUpdate()
      },
      showEdit(row, disabled) {
        this.show = 0
        if (!row) {
          this.title = '添加'
        } else {
          console.warn('row', row)
          if (disabled) {
            this.title = '查看'
            this.show = 1
          } else {
            this.title = '编辑'
          }
          // this.formData = Object.assign({}, row)
          Object.keys(this.formData).forEach(
            (key) => (this.formData[key] = row[key])
          )
          this.formData.qualid = row.qualid
          this.formData.disputeid = row.disputeid
          this.formData.whethersuedName =
            row.whethersued === 1 ? '起诉' : '被诉'
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
        this.formData = this.$options.data().formData
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
              const { msg } = await getQualificationSave(this.formData)
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            } else if (this.title === '编辑') {
              const { msg } = await getQualificationModify(this.formData)
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            }
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      handleXzjf(val) {
        this.formData.disputeId = val.disputeid
        this.formData.disputeitem = val.disputeitem
        this.formData.whethersued = val.whethersued
        this.formData.whethersuedName = val.whethersued === 1 ? '起诉' : '被诉'
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
