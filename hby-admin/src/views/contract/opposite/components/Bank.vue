<!--
 * @Date: 2022-04-07 09:48:37
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-24 11:03:02
 * @FilePath: /hb-admin/src/views/contract/opposite/components/Bank.vue
-->
<template>
  <div>
    <div style="text-align: right; margin-bottom: 5px">
      <el-button v-if="!readonly" type="success" @click="showEdit()">
        新增
      </el-button>
    </div>
    <el-table :data="tableData">
      <el-table-column
        align="center"
        label="银行账号"
        prop="bankaccount"
        width="260"
      />
      <el-table-column align="center" label="账户" prop="bankaccname" />
      <el-table-column align="center" label="开户银行" prop="bankkhyh" />
      <el-table-column align="center" label="账户性质" prop="banknature">
        <template #default="{ row }">
          {{ row.banknature == 0 ? '公司' : '个人' }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" prop="bankstatus">
        <template #default="{ row }">
          {{ row.bankstatus == 0 ? '弃用' : '启用' }}
        </template>
      </el-table-column>
      <el-table-column
        v-if="!readonly"
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="showEdit(row)">修改</el-button>
          <el-button type="text" @click="handleStatus(row)">
            {{ row.bankstatus == 0 ? '启用' : '弃用' }}
          </el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      append-to-body
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      :close-on-click-modal="false"
    >
      <el-form ref="form" label-width="120px" :model="formData" :rules="rules">
        <el-col :span="12">
          <el-form-item label="银行账号" prop="bankaccount">
            <el-input
              v-model="formData.bankaccount"
              clearable
              placeholder="请输入银行账号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账户" prop="bankaccname">
            <el-input
              v-model="formData.bankaccname"
              clearable
              placeholder="请输入账户"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开户银行" prop="bankkhyh">
            <el-input
              v-model="formData.bankkhyh"
              clearable
              placeholder="请输入开户银行"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="银行类别" prop="banktype">
            <el-input
              v-model="formData.banktype"
              clearable
              placeholder="请输入银行类别"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账户性质" prop="banknature">
            <el-radio-group v-model="formData.banknature" size="medium">
              <el-radio :label="0">公司</el-radio>
              <el-radio :label="1">个人</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账户类型" prop="bankstatus">
            <el-radio-group v-model="formData.bankstatus" size="medium">
              <el-radio :label="0">弃用</el-radio>
              <el-radio :label="1">启用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-form>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import {
    deleteBank,
    getBankList,
    saveBank,
    updateBankStatus,
  } from '@/api/contract/opposite'
  export default {
    name: 'Bank',
    props: {
      opposite: {
        type: Object,
        default: null,
      },
      code: {
        type: String,
        default: '0',
      },
      bankList: {
        type: Array,
        default: () => [],
      },
      readonly: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        getcode: this.code,
        queryForm: {
          budgetId: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        dialogFormVisible: false,
        formData: {
          flowId: 622322,
          budgetId: undefined,
          bankid: undefined,
          bankaccount: undefined,
          bankaccname: undefined,
          bankkhyh: undefined,
          banktype: undefined,
          banknature: 0, // 0公司，1个人
          bankstatus: 1, // 1启用  0弃用
        },
        rules: {
          bankaccount: [
            {
              required: true,
              message: '请输入相对方编号',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        tableData: [],
      }
    },
    watch: {
      opposite: {
        handler(val) {
          if (val) {
            this.queryForm.budgetId = val.budgetid
            this.fetchData(this.queryForm)
          }
        },
        immediate: true,
      },
    },
    created() {},

    methods: {
      showEdit(row) {
        // if (!this.opposite || !this.opposite.budgetid) {
        //   this.$baseMessage(
        //     '请先到页面底部保存相对方信息后，再添加银行账户信息',
        //     'error',
        //     'vab-hey-message-error'
        //   )
        //   return
        // }
        if (!this.opposite || !this.opposite.budgetid) {
          this.$baseMessage(
            '请先到页面底部保存相对方信息后，再添加银行账户信息',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = row[key]
          })
        }
        this.formData.flowId = 622322
        this.formData.budgetId = this.opposite.budgetid
        this.dialogFormVisible = true
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.tableData = []

        if (!this.opposite) {
          this.queryForm.budgetId = undefined
        }

        if (!this.queryForm.budgetId) return
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getBankList(this.queryForm)
        this.tableData = tlist
        this.total = totalRecord
        this.listLoading = false
      },
         //处理状态
      async handleStatus(row) {
        const { code, msg } = await updateBankStatus({
          bankId: row.bankid,
          bankstatus: row.bankstatus == 0 ? 1 : 0,
        })
        if (code == 1) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.fetchData()
        }
      },
      close() {
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
        this.getcode = '0'
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await saveBank(this.formData)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
            this.fetchData()
          }
        })
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { code, msg } = await deleteBank({
            bankId: row.bankid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            await this.fetchData()
          }
        })
      },
    },
  }
</script>
