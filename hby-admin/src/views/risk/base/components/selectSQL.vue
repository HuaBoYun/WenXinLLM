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
          <el-form-item label="关联数据源" label-width="140px" prop="name">
            <el-input
              v-model="formData.name"
              clearable
              placeholder="请选择关联账套"
              :style="{ width: '78%' }"
              disabled
            />
            <el-button @click="select" style="margin-left: 10px" type="primary">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="选择历史模型"
            label-width="140px"
            prop="dataBase"
          >
            <el-input
              v-model="formData.dataBase"
              clearable
              placeholder="请选择模型"
              :style="{ width: '78%' }"
              disabled
            />
            <el-button
              @click="handleSelectModel"
              style="margin-left: 10px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.bookid">
          <el-form-item label="表" label-width="140px">
            <dataBase
              @handle="handleSQL"
              :bookid="formData.bookid"
              ref="data"
            ></dataBase>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formData.bookid">
          <el-form-item label="字段" label-width="140px">
            <sqlField
              ref="field"
              @handle="handleSQLData"
              :bookid="formData.bookid"
              :dataBase="formData.sqlName"
            ></sqlField>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="Sql" label-width="140px" prop="sqlstr">
            <el-input
              v-model="formData.sqlstr"
              clearable
              :rows="8"
              type="textarea"
              placeholder="请输入sql"
              :style="{ width: '100%' }"
              :disabled="!footer"
              ref="vta"
              @blur="hanldeSSSSSSS"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="check" type="primary">验证</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
    <SelectModal ref="select" @selected="selectInfo"></SelectModal>
    <SelectNewModel ref="model" @selected="selectModel"></SelectNewModel>
    <SqlModal ref="check"></SqlModal>
  </el-dialog>
</template>

<script>
  import SelectModal from './select.vue'
  import SelectNewModel from './selectNewModel.vue'
  import dataBase from './newDataBase.vue'
  import sqlField from './newSqlField.vue'
  import SqlModal from './sqlCheck.vue'
  export default {
    components: { SelectModal, SelectNewModel, dataBase, sqlField, SqlModal },
    data() {
      return {
        dialogFormVisible: false,
        title: '配置',
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
      }
    },
    methods: {
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.formData = {
          name: '',
          dataBase: '',
          bookid: '',
          sqlstr: '', // sql完整的语句
          sqlName: '', //选择的sql表名
        }
      },
      /**
       * @description: 打开表单
       * @param {*} row 传入数据
       * @return {*}
       */      
      showEdit(row) {
        this.dialogFormVisible = true
        this.formData.sqlstr = row
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
        this.formData.sqlstr =
          this.formData.sqlstr.substring(0, this.editIndex) +
          sql +
          this.formData.sqlstr.substring(
            this.editIndex,
            this.formData.sqlstr.length
          )
        await this.$set(this.formData, 'sqlName', name)
        await this.$refs['field'].getSQL(
          this.formData.bookid,
          this.formData.sqlName
        )
      },
      //选择字段，触发sql更新
      handleSQLData(sql, name) {
        this.formData.sqlstr =
          this.formData.sqlstr.substring(0, this.editIndex) +
          sql +
          this.formData.sqlstr.substring(
            this.editIndex,
            this.formData.sqlstr.length
          )
        this.$set(this.formData, 'sqlField', name)
      },
      add() {
        this.$emit('add', this.formData)
        this.close()
      },
      check() {
        this.$refs['check'].show(this.formData.sqlstr, this.formData.bookid)
      },
      hanldeSSSSSSS() {
        const start = this.$refs.vta.$refs.textarea.selectionStart
        const end = this.$refs.vta.$refs.textarea.selectionEnd
        this.editIndex = start
      },
    },
  }
</script>

<style></style>
