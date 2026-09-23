<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12" style="height: 29px">
          <el-form-item label="表名称" prop="fname">
            <el-input
              v-model="formData.fname"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入表名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="目标端表名" prop="oursTableName">
            <el-input
              v-model="formData.oursTableName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入目标端表名"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="源端数名" prop="outsTableName">
            <el-input
              v-model="formData.outsTableName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入源端数名"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据源" prop="dataConfig">
            <el-input
              v-model="formData.datatext"
              disabled
              clearable
              placeholder="请输入数据源"
              :style="{ width: '75%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="handleSelectDataSource()">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="采集方式" prop="financeType">
            <el-radio-group v-model="formData.financeType">
              <el-radio :label="1">仅采集</el-radio>
              <el-radio :label="2">仅导入</el-radio>
              <el-radio :label="3">采集和导入</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="sql" prop="sqlText">
            <el-input
              v-model="formData.sqlText"
              :style="{ width: '100%' }"
              type="textarea"
              placeholder="请输入sql"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <el-button type="primary" @click="execute">sql测试</el-button>
          </div>
        </el-col>
        <el-col :span="24">
          <el-divider>列信息</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-button
              type="success"
              style="margin-bottom: 5px"
              @click="handleAddDataSource()"
            >
              新增列
            </el-button>
          </div>
          <el-table :data="tableData">
            <el-table-column
              align="center"
              label="目标端列名"
              prop="oursColname"
              width="300"
            >
              <template #default="{ row }">
                <el-input v-model="row.oursColname"></el-input>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="源端列名"
              prop="outsColname"
              width="300"
            >
              <template #default="{ row }">
                <el-input v-model="row.outsColname"></el-input>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="列类型"
              prop="colType"
              width="300"
            >
              <template #default="{ row }">
                <el-input v-model="row.colType"></el-input>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="长度"
              prop="colLength"
              width="100"
            >
              <template #default="{ row }">
                <el-input v-model="row.colLength"></el-input>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="是否列表展示"
              prop="isShowList"
              width="100"
            >
              <template #default="{ row }">
                <el-select v-model="row.isShowList" placeholder="请选择">
                  <el-option label="是" :value="0"></el-option>
                  <el-option label="否" :value="1"></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="是否条件查询"
              prop="isFilter"
              width="100"
            >
              <template #default="{ row }">
                <el-select v-model="row.isFilter" placeholder="请选择">
                  <el-option label="是" :value="0"></el-option>
                  <el-option label="否" :value="1"></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="是否主键"
              prop="isPrimaryKey"
              width="100"
            >
              <template #default="{ row }">
                <el-select v-model="row.isPrimaryKey" placeholder="请选择">
                  <el-option label="是" :value="0"></el-option>
                  <el-option label="否" :value="1"></el-option>
                </el-select>
              </template>
            </el-table-column>

            <el-table-column
              align="center"
              label="注释"
              prop="fname"
              width="200"
            >
              <template #default="{ row }">
                <el-input v-model="row.fname" type="textarea"></el-input>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row, $index }">
                <el-button type="text" @click="handleDeletByid(row, $index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
    <departmentSelect
      ref="departmentSelect"
      @submit="handleDepartmentSelected"
    />
    <bbModal ref="bbModal" @versionManage="handleVersionManage" />
    <sjyModal
      ref="sjyModal"
      @dataSourceManage="handleDataSourceManage"
      :fid="formData.planId"
    />
    <result-modal ref="resultModal" />
  </el-dialog>
</template>

<script>
  import { getYWSJAdd, getYWSJDetail, getYWSJEdit, sqlTest } from '@/api/cwsc'
  import departmentSelect from '@/components/department.vue'
  import bbModal from '@/views/cwsc/jcpz/components/bbModal.vue'
  import sjyModal from '@/views/cwsc/jcpz/components/shujuyuanModal.vue'
  import ResultModal from './resultModal.vue'
  export default {
    components: { departmentSelect, bbModal, sjyModal, ResultModal },
    inheritAttrs: false,
    props: ['planId'],
    data() {
      return {
        loading: false,

        tableData: [],
        formData: {
          fid: '',
          fname: '',
          planId: '',
          colList: [],
          oursTableName: '',
          dataConfig: '',
          datatext: '',
          outsTableName: '',
          sqlText: '',
          financeType: '',
        },
        footer: true,
        rules: {
          dataBaseType: [
            {
              required: true,
              message: '请选择数据库类型',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    created() {
      this.formData.planId = this.planId
    },
    methods: {
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          const res = await getYWSJDetail({ fid: row.fid })
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data.tableInfo[key]
          })
          this.formData.planId = this.planId
          this.tableData = res.data.colList
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.formData = {
          fid: '',
          fname: '',
          planId: '',
          colList: [],
          oursTableName: '',
          dataConfig: '',
          datatext: '',
          outsTableName: '',
          sqlText: '',
          financeType: '',
        }
        this.formData.planId = this.planId
        this.tableData = []
        this.dialogFormVisible = false
        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = JSON.parse(JSON.stringify(this.formData))
            params.colList = this.tableData
            const fuc = this.formData.fid ? getYWSJEdit : getYWSJAdd
            const res = await fuc(params)
            if (res && res.code == 1) {
              this.close()
              this.$emit('fetchData')
              this.$message({
                message: '提交成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
            }
          }
        })
      },

      /**
       * @description: 选择公司
       * @param {*} val
       * @return {*}
       */

      handleSelectCompany() {
        this.$refs.departmentSelect.showEdit()
      },
      /**
       * @description: 选择版本
       * @param {*} val
       * @return {*}
       */
      handleSelectVersion() {
        this.$refs.bbModal.showEdit()
      },
      /**
       * @description: 选择数据源
       * @param {*} val
       * @return {*}
       */
      handleSelectDataSource(index) {
        this.$refs.sjyModal.showEdit()
      },
      handleDepartmentSelected(val) {
        this.formData.financeorgid = val.id
        this.formData.financeorgname = val.name
      },
      handleDataSourceManage(val) {
        console.log(val, 'val')
        // 更新表单数据
        this.formData.dataConfig = val[0].fid
        this.formData.datatext = val[0].fintext
      },
      handleVersionManage(val) {
        this.formData.fversionid = val[0].fid
        this.formData.fversionname = val[0].handtext
      },
      handleAddDataSource(command) {
        const obj = {
          fname: '',
          colLength: '',
          colType: '',
          isFilter: 0,
          isPrimaryKey: 0,
          isShowList: 0,
          oursColname: '',
          outsColname: '',
        }
        this.tableData.push(obj)
      },
      async handleDeletByid(row, index) {
        this.tableData.splice(index, 1)
      },
      async execute() {
        if (this.formData.sqlText == '') {
          this.$message({
            message: '请输入sql',
            type: 'error',
          })
          return
        }
        if (this.formData.dataConfig == '') {
          this.$message({
            message: '请选择数据源',
            type: 'error',
          })
          return
        }
        const res = await sqlTest({
          fid: this.formData.fid,
          sqlText: this.formData.sqlText,
          dataConfig: this.formData.dataConfig,
        })
        if (res.msg == '成功') {
          this.$baseMessage('执行成功', 'success', 'vab-hey-message-success')
          this.$refs['resultModal'].show(res.data)
        } else {
          this.$baseMessage('执行失败', 'error', 'vab-hey-message-error')
        }
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
