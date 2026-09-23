<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :destroy-on-close="true"
  >
    <el-row :gutter="15">
      <el-form
        ref="form"
        label-width="125px"
        size="medium"
        :model="formData"
        :disabled="disable"
        :rules="rules"
      >
        <el-col :span="12">
          <el-form-item label="业务规则编码" prop="rulecode">
            <el-input
              v-model="formData.rulecode"
              placeholder="请输入规则编码"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="业务规则名称" prop="rulename">
            <el-input
              v-model="formData.rulename"
              placeholder="请输入规则编码"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="优先级">
            <el-select
              v-model="formData.rulepriority"
              placeholder="请选择"
              style="width: 348px"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险等级">
            <el-select
              v-model="formData.risklevel"
              placeholder="请选择"
              style="width: 348px"
            >
              <el-option
                v-for="item in options1"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="规则状态">
            <el-select
              v-model="formData.satus"
              placeholder="请选择"
              style="width: 348px"
            >
              <el-option
                v-for="item in options2"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="规则所属部门" prop="orgname">
            <el-input v-model="formData.orgname" disabled />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="数据连接字符串" prop="connectionstrings">
            <el-select
              v-model="formData.connectionstrings"
              placeholder="请选择"
              style="width: 348px"
            >
              <el-option
                v-for="item in connectionarr"
                :key="item.bookid"
                :label="item.bookname"
                :value="item.bookid"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="关联数据源" prop="connectionstrings">
            <el-input
              v-model="formData.dataname"
              clearable
              placeholder="请选择关联数据源"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button @click="select" style="margin-left: 10px" type="primary">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="规则对应SQL" prop="rulesql">
            <el-input
              v-model="formData.rulesql"
              clearable
              :rows="8"
              type="textarea"
              placeholder="请输入sql"
              :style="{ width: '90%' }"
              :disabled="!footer"
            />
            <el-button
              @click="handleSelectSQL"
              style="margin-left: 10px"
              type="primary"
            >
              编辑
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="报表SQL">
            <el-input
              placeholder="请输入报表SQL"
              type="textarea"
              v-model="formData.reportsql"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="规则描述" prop="ruledescription">
            <el-input
              placeholder="请输入规则描述"
              type="textarea"
              v-model="formData.ruledescription"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="规则备注" prop="memo">
            <el-input
              placeholder="请输入规则备注"
              type="textarea"
              v-model="formData.memo"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer v-if="!disable">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <sqlField
      ref="field"
      @handle="handleSQLData"
      :SQL="formData.rulesql"
      :bookid="formData.bookid"
      :dataBase="formData.dataBase"
    ></sqlField>
    <SelectSQL ref="sql" @add="handleADD"></SelectSQL>
    <SelectModal ref="select" @selected="selectInfo"></SelectModal>
  </el-dialog>
</template>

<script>
  import {
    RuleDisp,
    CheckUpdate,
    ToRuleAdd,
    ruleAdd,
    ToRuleModify,
    ruleEdit,
  } from '@/api/monitor/rule/index'
  import { getOrgTreeByDepartment } from '@/api/common'
  import sqlField from './sqlField'
  import SelectSQL from './selectSQL'
  import SelectModal from './select.vue'

  export default {
    name: 'RuleInfo',
    components: { sqlField, SelectSQL, SelectModal },
    data() {
      return {
        title: '详细信息',
        dialogFormVisible: false,
        tableData: [],
        formData: {
          rulecode: null,
          rulename: null,
          rulepriority: '高',
          risklevel: '高',
          satus: '启用',
          orgid: null,
          orgname: null,
          connectionstrings: null,
          dataname: null,
          rulesql: null,
          reportsql: null,
          ruledescription: null,
          memo: null,
        },
        disable: false,
        rules: {
          rulecode: [
            { required: true, trigger: 'blur', message: '请输入规则编号' },
          ],
          rulename: [
            { required: true, trigger: 'blur', message: '请输入规则名称' },
          ],
          orgname: [
            { required: true, trigger: 'blur', message: '请输入规则所属部门' },
          ],
          connectionstrings: [
            {
              required: true,
              trigger: 'blur',
              message: '请输入数据连接字符串',
            },
          ],
          rulesql: [
            { required: true, trigger: 'blur', message: '请输入规则对应SQL' },
          ],
        },
        options: [
          {
            value: '高',
            label: '高',
          },
          {
            value: '中',
            label: '中',
          },
          {
            value: '低',
            label: '低',
          },
        ],
        options1: [
          {
            value: '高',
            label: '高',
          },
          {
            value: '中',
            label: '中',
          },
          {
            value: '低',
            label: '低',
          },
        ],
        options2: [
          {
            value: '启用',
            label: '启用',
          },
          {
            value: '禁用',
            label: '禁用',
          },
        ],
        connectionarr: [],
      }
    },
    created() {},
    methods: {
      async showEdit(row, flag) {
        this.dialogFormVisible = true
        const { data, msg } = await ToRuleAdd()
        this.connectionarr = data.books

        if (flag == 'look') {
          this.title = '详细'
          this.disable = true
          RuleDisp({ selectedruleid: row.ruleid }).then((res) => {
            //
            this.formData = res.data.rule
          })
        } else if (flag == 'add') {
          this.title = '新增'
          this.formData = { ...this.formData }
          this.formData.orgid = row.orgid
          this.formData.orgname = row.orgname
          this.disable = false
        } else if (flag == 'edit') {
          this.title = '编辑'
          this.disable = false
          RuleDisp({ selectedruleid: row.ruleid }).then((res) => {
            //
            this.formData = res.data.rule
          })
        }
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const func = this.title == '编辑' ? ruleEdit : ruleAdd
            const { data, msg } = await func(this.formData)
            if (msg == '错误') {
              this.$baseMessage(data.result, 'error', 'vab-hey-message-error')
            } else {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            }
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      close() {
        this.formData = {
          rulecode: null,
          rulename: null,
          rulepriority: '高',
          risklevel: '高',
          satus: '启用',
          orgid: null,
          orgname: null,
          connectionstrings: null,
          dataname: null,
          rulesql: null,
          reportsql: null,
          ruledescription: null,
          memo: null,
        }
        this.dialogFormVisible = false
      },
      handleSelectSQL() {
        this.$refs['sql'].showEdit(this.formData.rulesql)
      },
      handleSQLData(sql, name) {
        this.$set(this.formData, 'rulesql', sql)
        this.$set(this.formData, 'sqlField', name)
      },
      handleADD(sql) {
        this.$set(this.formData, 'rulesql', sql.sqlstr)
      },
      select() {
        this.$refs['select'].show()
      },
      selectInfo(info) {
        this.$set(this.formData, 'dataname', info.dataBaseUsers)
        this.$set(this.formData, 'connectionstrings', info.id)
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
