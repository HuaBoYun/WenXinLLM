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
          <el-form-item label="表" label-width="140px">
            <dataBase
              @handle="handleSQL"
              :bookid="formData.bookid"
              ref="data"
            ></dataBase>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="字段" label-width="140px">
            <sqlField
              ref="field"
              @handle="handleSQLData"
              :bookid="formData.bookid"
              :dataBase="formData.sqlName"
            ></sqlField>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>

      <el-button @click="add" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import dataBase from './components/newDataBase.vue'
  import sqlField from './components/newSqlField1.vue'
  import { exportExcel } from '@/oapi/setting/org'

  export default {
    components: { dataBase, sqlField },
    data() {
      return {
        dialogFormVisible: false,
        title: '导出',
        formData: {
          name: '',
          dataBase: '',
          bookid: '',
          sqlstr: '', // sql完整的语句
          sqlName: '', //选择的sql表名
        },
        rules: {},
        footer: true,
        editIndex: 0,
        submitName: '',
        id: '',
      }
    },
    methods: {
      close() {
        this.dialogFormVisible = false
        this.formData = {
          name: '',
          dataBase: '',
          bookid: '',
          sqlName: '', //选择的sql表名
        }
      },
      showEdit(row) {
        this.dialogFormVisible = true
        this.selectInfo(row)
        this.id = row.id
      },
      select() {
        this.$refs['select'].show()
      },
      //选择数据源，触发字表更新
      async selectInfo(info) {
        await this.$set(this.formData, 'name', info.dataBaseUsers)
        await this.$set(this.formData, 'bookid', info.id)
        await this.$refs['data'].getDataBaseData(this.formData.bookid)
      },
      handleSelectModel() {
        this.$refs['model'].show()
      },
      selectModel(info) {
        this.$set(this.formData, 'dataBase', info.steptitle)
        this.$set(this.formData, 'sqlstr', info.sqlstr)
        // this.$set(this.formData, 'bookid', info.bookid)
      },
      //选择表，触发字段列表更新
      async handleSQL(sql, name) {
        await this.$set(this.formData, 'sqlName', name)
        await this.$refs['field'].getSQL(
          this.formData.bookid,
          this.formData.sqlName
        )
      },
      //选择字段，触发sql更新
      handleSQLData(name) {
        this.submitName = name
      },
      async add() {
        const data = await exportExcel({
          dataBaseId: this.id,
          sql: this.submitName,
          tableName: this.formData.sqlName,
        })
        let fileName = '数据源查看'
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
        this.close()
      },
    },
  }
</script>

<style></style>
