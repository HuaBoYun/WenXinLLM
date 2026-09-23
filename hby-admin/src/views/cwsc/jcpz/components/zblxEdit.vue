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
        <el-col :span="12">
          <el-form-item label="名称" prop="name">
            <el-input
              v-model="formData.name"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入账簿名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编码" prop="code">
            <el-input
              v-model="formData.code"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入编码"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="简称" prop="shortname">
            <el-input
              v-model="formData.shortname"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入简称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核算目的" prop="checkaim">
            <el-input
              v-model="formData.checkaim"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入核算目的"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="助记码" prop="mnecode">
            <el-input
              v-model="formData.mnecode"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入助记码"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分布式" prop="dataoriginflag">
            <el-select
              v-model="formData.dataoriginflag"
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option label="本级产生" :value="0"></el-option>
              <el-option label="上级下发" :value="1"></el-option>
              <el-option label="下级上报" :value="2"></el-option>
              <el-option label="本级产生已上报下发" :value="3"></el-option>
              <el-option label="系统预置" :value="-1"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="财务核算账簿" prop="isaccountbook">
            <el-input
              v-model="formData.isaccountbook"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入财务核算账簿"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任核算账簿" prop="isliabilitybook">
            <el-input
              v-model="formData.isliabilitybook"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入责任核算账簿"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="会计期间方案" prop="pkAccperiodscheme">
            <el-input
              v-model="formData.pkAccperiodscheme"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入会计期间方案"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="科目体系" prop="pkAccsystem">
            <el-input
              v-model="formData.pkAccsystem"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入科目体系"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核算要素体系" prop="pkCheckelemsystem">
            <el-input
              v-model="formData.pkCheckelemsystem"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入核算要素体系"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="外币汇率方案" prop="pkExratescheme">
            <el-input
              v-model="formData.pkExratescheme"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入外币汇率方案"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="本位币" prop="pkStandardcurr">
            <el-input
              v-model="formData.pkStandardcurr"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入本位币"
            />
          </el-form-item>
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
    <sjyModal ref="sjyModal" @dataSourceManage="handleDataSourceManage" />
  </el-dialog>
</template>

<script>
  import { saveZblx, getZblxDetail } from '@/api/cwsc'
  import departmentSelect from '@/components/department.vue'
  import bbModal from '@/views/cwsc/jcpz/components/bbModal.vue'
  import sjyModal from '@/views/cwsc/jcpz/components/sjyModal.vue'
  export default {
    components: { departmentSelect, bbModal, sjyModal },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,

        tableData: [],
        formData: {
          name: '',
          code: '',
          shortname: '',
          checkaim: '',
          mnecode: '',
          dataoriginflag: '',
          isaccountbook: '',
          isliabilitybook: '',
          pkAccperiodscheme: '',
          pkAccsystem: '',
          pkCheckelemsystem: '',
          pkExratescheme: '',
          pkSetofbook: '',
          pkStandardcurr: '',
          pkGroup: '',
          pkOrg: '',
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
          dataBaseConnectionAddress: [
            {
              required: true,
              message: '请输入数据库连接',
              trigger: 'blur',
            },
          ],
          dataBaseUsers: [
            {
              required: true,
              message: '请输入数据库用户',
              trigger: 'blur',
            },
          ],
          dataBasePassWord: [
            {
              required: true,
              message: '请输入数据库密码',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},

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
          const res = await getZblxDetail({ pkSetofbook: row.pkSetofbook })
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data[key]
            // this.formData.pkFinanplanname = res.data.planName
          })
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
          name: '',
          code: '',
          shortname: '',
          checkaim: '',
          mnecode: '',
          dataoriginflag: '',
          isaccountbook: '',
          isliabilitybook: '',
          pkAccperiodscheme: '',
          pkAccsystem: '',
          pkCheckelemsystem: '',
          pkExratescheme: '',
          pkSetofbook: '',
          pkStandardcurr: '',
          pkGroup: '',
          pkOrg: '',
        }
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
            const res = await saveZblx(params)
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
      handleSelectDataSource() {
        this.$refs.sjyModal.showEdit()
      },
      handleDepartmentSelected(val) {
        this.formData.financeorgid = val.id
        this.formData.financeorgname = val.name
      },
      handleDataSourceManage(val) {
        this.formData.pkFinanplanid = val[0].fid
        this.formData.pkFinanplanname = val[0].fintext
      },
      handleVersionManage(val) {
        this.formData.fversionid = val[0].fid
        this.formData.fversionname = val[0].handtext
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
