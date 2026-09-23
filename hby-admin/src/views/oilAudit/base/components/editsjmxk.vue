<template>
  <el-dialog
    v-if="dialogFormVisible"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="模型编号" label-width="140px" prop="stepno">
            <el-input
              v-model="formData.stepno"
              clearable
              placeholder="请输入模型编号"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型名称" label-width="140px" prop="steptitle">
            <el-input
              v-model="formData.steptitle"
              clearable
              placeholder="请输入模型名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" label-width="140px" prop="createname">
            <el-input
              v-model="formData.createname"
              clearable
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="关联数据源"
            label-width="140px"
            prop="stepcontent"
          >
            <el-input
              v-model="formData.stepcontent"
              clearable
              placeholder="请选择关联数据源"
              :style="{ width: '78%' }"
              disabled
            />
            <el-button @click="select" style="margin-left: 10px" type="primary">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="库表" label-width="140px" prop="dataBase">
            <el-input
              v-model="formData.dataBase"
              clearable
              placeholder="选择库表"
              :style="{ width: '78%' }"
              disabled
            />
            <el-button
              @click="selectDataBase"
              style="margin-left: 10px"
              type="primary"
              :disabled="!formData.name"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="表字段" label-width="140px" prop="sqlField">
            <el-input
              v-model="formData.sqlField"
              clearable
              placeholder="选择表字段"
              :style="{ width: '78%' }"
              disabled
            />
            <el-button
              @click="selectSqlField"
              style="margin-left: 10px"
              type="primary"
              :disabled="!formData.dataBase"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="24">
          <el-form-item label="说明" label-width="140px" prop="stepcontent">
            <el-input
              v-model="formData.stepcontent"
              clearable
              :rows="4"
              type="textarea"
              placeholder="请输入说明"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="Sql" label-width="140px" prop="sqlstr">
            <el-input
              v-model="formData.sqlstr"
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
      </el-form>
    </el-row>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>

      <el-button @click="check" type="primary">结果</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
    <SelectModal ref="select" @selected="selectInfo"></SelectModal>
    <SqlModal ref="check"></SqlModal>
    <dataBase
      ref="dataBase"
      @handle="handleSQL"
      :bookid="formData.bookid"
    ></dataBase>
    <sqlField
      ref="field"
      @handle="handleSQLData"
      :SQL="formData.sqlstr"
      :bookid="formData.bookid"
      :dataBase="formData.dataBase"
    ></sqlField>
    <SelectSQL ref="sql" @add="handleADD"></SelectSQL>
  </el-dialog>
</template>

<script>
  import { createSJMXKData, getModelNo } from '@/oapi/setting/org'
  import SelectModal from './select.vue'
  import SqlModal from './sqlCheck.vue'
  import dataBase from './database'
  import sqlField from './sqlField'
  import SelectSQL from './selectSQL'
  export default {
    components: { SelectModal, SqlModal, dataBase, sqlField, SelectSQL },
    data() {
      return {
        dialogFormVisible: false,
        formData: {
          stepno: '',
          steptitle: '',
          createstaffid: '',
          createname: '',
          stepcontent: '',
          sqlstr: '',
        },
        footer: true,
        rules: {
          stepno: [
            {
              required: true,
              message: '请输入步骤编号',
              trigger: 'blur',
            },
          ],
          steptitle: [
            {
              required: true,
              message: '请输入步骤标题',
              trigger: 'blur',
            },
          ],
          stepcontent: [
            {
              required: true,
              message: '请选择关联账套',
              trigger: 'blur',
            },
          ],
          sqlstr: [
            {
              required: true,
              message: '请输入sql',
              trigger: 'blur',
            },
          ],
        },
        title: '',
      }
    },
    created() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.$set(this.formData, 'createname', userInfo.realname)
      this.$set(this.formData, 'createstaffid', userInfo.staffid)
    },
    methods: {
      show(row, title) {
        this.title = title
        this.dialogFormVisible = true
        if (title == '新增') {
          // 自动编号
          getModelNo().then((res) => {
            if (res && res.code === 1) {
              this.formData.stepno = res.data.data
            }
          })
        }

        if (title == '编辑') {
          this.formData = {
            stepno: row.stepno,
            steptitle: row.steptitle,
            createstaffid: row.createstaffid,
            createname: row.createname,
            stepcontent: row.stepcontent,
            sqlstr: row.sqlstr,
            stepid: row.stepid,
            // name: row.acctid || '',
            bookid: row.bookid || '',
          }
        } else {
          this.nodeId = row.nodeId
        }
      },
      close() {
        this.formData = {
          stepno: '',
          steptitle: '',
          createstaffid: '',
          createname: '',
          stepcontent: '',
          sqlstr: '',
        }
        this.dialogFormVisible = false
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            createSJMXKData({
              ...this.formData,
              typeid: this.nodeId,
            }).then((res) => {
              if (res.code == 1) {
                this.$baseMessage('保存成功', 'success')
                this.$emit('fetchData')
                this.dialogFormVisible = false
              }
            })
          }
        })
      },
      select() {
        this.$refs['select'].show()
      },
      selectInfo(info) {
        this.$set(this.formData, 'stepcontent', info.dataBaseUsers)
        this.$set(this.formData, 'bookid', info.id)
      },
      check() {
        this.$refs['check'].show(this.formData.sqlstr, this.formData.bookid)
      },
      selectDataBase() {
        this.$refs['dataBase'].show()
      },
      handleSQL(sql, name) {
        this.$set(this.formData, 'sqlstr', sql)
        this.$set(this.formData, 'dataBase', name)
      },
      handleSQLData(sql, name) {
        this.$set(this.formData, 'sqlstr', sql)
        this.$set(this.formData, 'sqlField', name)
      },
      selectSqlField() {
        this.$refs['field'].show()
      },
      handleSelectSQL() {
        this.$refs['sql'].showEdit(this.formData.sqlstr)
      },
      handleADD(sql) {
        this.$set(this.formData, 'sqlstr', sql.sqlstr)
      },
    },
  }
</script>
