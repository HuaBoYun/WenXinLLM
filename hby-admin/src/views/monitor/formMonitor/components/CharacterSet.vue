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
        label="替换符"
        prop="replacekey"
        width="260"
      />
      <el-table-column align="center" label="替换值" prop="replacekvalue" />
      <el-table-column align="center" label="替换类型" prop="replacetype" />
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
      :close-on-click-modal="false"
      append-to-body
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <el-form ref="form" label-width="120px" :model="formData" :rules="rules">
        <el-col :span="24">
          <el-form-item label="替换符" prop="replacekey">
            <el-input
              v-model="formData.replacekey"
              clearable
              placeholder="请输入替换符"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="替换值" prop="replacekvalue">
            <el-input
              v-model="formData.replacekvalue"
              clearable
              placeholder="请输入替换值"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="替换类型" prop="replacetype">
            <el-input
              v-model="formData.replacetype"
              clearable
              placeholder="请输入替换类型"
              :style="{ width: '100%' }"
            />
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
    getBankList,
    saveBank,
    updateBankStatus,
    deleteBank,
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
          replacekey: null,
          replacekvalue: null,
          replacetype: null,
        },
        rules: {
          replacekey: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          replacekvalue: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
          replacetype: [
            {
              required: true,
              message: '请输入',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        tableData: [],
        addArr: {},
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
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = row[key]
          })
        }
        // this.formData.flowId = 622322
        // this.formData.budgetId = this.opposite.budgetid
        this.dialogFormVisible = true
      },
      async fetchData() {
        // this.tableData = []
        this.tableData.push(this.addArr)
        // if (!this.opposite) {
        //   this.queryForm.budgetId = undefined
        // }

        // if (!this.queryForm.budgetId) return
        // this.listLoading = true
        // const {
        //   data: { tlist, totalRecord },
        // } = await getBankList(this.queryForm)
        // this.tableData = tlist
        // this.total = totalRecord
        // this.listLoading = false
      },
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
            this.addArr = {}
            this.addArr = {
              replacekey: this.formData.replacekey,
              replacekvalue: this.formData.replacekvalue,
              replacetype: this.formData.replacetype,
            }
            // const { msg } = await saveBank(this.formData)

            // this.$emit('addCharacter', arr)
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
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
