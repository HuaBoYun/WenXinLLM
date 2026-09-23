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
          <el-form-item label="结项日期">
            <el-date-picker
              v-model="form.closedate"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入结项日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="判决金额">
            <el-input
              v-model="form.judgementamount"
              clearable
              placeholder="请输入判决金额"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷主题" prop="disputeitem">
            <el-input
              v-model="form.disputeitem"
              clearable
              placeholder="请选择纠纷主题"
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.xzjf.show(1)"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="处理结果">
            <el-input
              v-model="form.closeresult"
              clearable
              placeholder="请输入处理结果"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="管理建议">
            <el-input
              v-model="form.managerecommond"
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
          <el-form-item label="结项日期">
            <span>{{ form.closedate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="判决金额">
            <span>{{ form.judgementamount }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷主题" prop="disputeitem">
            <span>{{ form.disputeitem }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="处理结果">
            <span>{{ form.closeresult }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="管理建议">
            <span>{{ form.managerecommond }}</span>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button v-if="show == 0" type="primary" @click="save">确 定</el-button>
    </template>
    <xzjf-options ref="xzjf" @selected="handleSsjd" />
  </el-dialog>
</template>

<script>
  import {
    disputeSettlementSave,
    disputeSettlementModify,
  } from '@/api/contract/legal'
  import xzjfOptions from './options/xzjf.vue'

  export default {
    name: 'DraftEdit',
    components: { xzjfOptions },
    data() {
      return {
        form: {
          disputeId: undefined,
          disputeid: undefined,
          closedate: undefined,
          judgementamount: undefined,
          disputeitem: undefined,
          closeresult: undefined,
          managerecommond: undefined,
          flowid: '698879',
        },
        rules: {
          disputeitem: [
            {
              required: true,
              message: '请输入纠纷主题',
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
          this.form.closeid = row.closeid
          this.form.disputeId = row.disputeid
          this.form.disputeid = row.disputeid
          this.form.budgetid = row.budgetid
        }
        this.dialogFormVisible = true
      },
      showDetail(row) {
        this.showEdit(row, true)
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            if (this.title === '添加') {
              const { msg } = await disputeSettlementSave(this.form)
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            } else if (this.title === '编辑') {
              const { msg } = await disputeSettlementModify(this.form)
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            }
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      //回调
      handleSsjd(val) {
        this.form.disputeId = val.disputeid
        this.form.disputeid = val.disputeid
        this.form.disputeitem = val.disputeitem
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
