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
          <el-form-item label="账号" prop="bankaccnum">
            <el-input
              v-model.trim="form.bankaccnum"
              clearable
              placeholder="请输入账号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="户名">
            <el-input
              v-model="form.bankaccname"
              clearable
              placeholder="请输入户名"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账户编码">
            <el-input
              v-model="form.bankcode"
              clearable
              placeholder="请输入账户编码"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账户名称">
            <el-input
              v-model="form.bankname"
              clearable
              placeholder="请输入账户名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开户银行">
            <el-input
              v-model="form.bankkhyh"
              clearable
              placeholder="请输入开户银行"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="银行类别">
            <el-input
              v-model="form.bankyhlb"
              clearable
              placeholder="请输入银行类别"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账户状态">
            <el-select
              v-model="form.bankstate"
              filterable
              placeholder="请选择账户状态"
              style="width: 100%"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="启用状态">
            <el-radio-group v-model="form.bankstatus">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="0">非启用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>

    <el-row v-if="show == 1" :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="账号" prop="bankaccnum">
            <span>{{ form.bankaccnum }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="户名">
            <span>{{ form.bankaccname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账户编码">
            <span>{{ form.bankcode }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账户名称">
            <span>{{ form.bankname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开户银行">
            <span>{{ form.bankkhyh }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="银行类别">
            <span>{{ form.bankyhlb }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账户状态">
            {{
              2 >= form.bankstate && form.bankstate >= 0
                ? ['正常', '冻结', '部分冻结'][form.bankstate]
                : '销户'
            }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="启用状态">
            <span>{{ form.bankstatus === 1 ? '启用' : '非启用' }}</span>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button v-if="show == 0" type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { orgBankInfoSave } from '@/api/contract/financing'

  export default {
    name: 'DraftEdit',
    data() {
      return {
        form: {
          bankaccname: undefined,
          bankaccnum: undefined,
          bankcode: undefined,
          bankid: undefined,
          bankkhyh: undefined,
          bankname: undefined,
          bankstate: undefined,
          bankstatus: 1,
          bankyhlb: undefined,
        },
        rules: {
          bankaccnum: [
            {
              required: true,
              message: '请输入账号',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        options: [
          {
            value: 0,
            label: '正常',
          },
          {
            value: 1,
            label: '冻结',
          },
          {
            value: 2,
            label: '部分冻结',
          },
          {
            value: 3,
            label: '销户',
          },
        ],
        show: 0,
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.show = 0
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.form.bankid = row.bankid
          Object.keys(this.form).forEach((key) => {
            this.form[key] = row[key]
          })
          if (this.form.bankstate === undefined) {
            this.form.bankstate = 3
          }
        }
        this.dialogFormVisible = true
      },
      showDetail(row) {
        this.form = row
        this.title = '查看'
        this.show = 1
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await orgBankInfoSave(this.form)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
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
</style>
