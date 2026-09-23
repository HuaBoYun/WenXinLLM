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
          <el-form-item label="账簿名称" prop="bookName">
            <el-input
              v-model="formData.bookName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入账簿名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账簿类型" prop="accbooktypename">
            <el-input
              v-model="formData.accbooktypename"
              :style="{ width: '75%', marginRight: '10px' }"
              disabled
              placeholder="请输入账簿类型"
            />
            <el-button type="primary" @click="handleZbTypeManage">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账簿类别编码" prop="accbooktypecode">
            <el-input
              v-model="formData.accbooktypecode"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入账簿类别编码"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账簿类别名称" prop="accbooktypename">
            <el-input
              v-model="formData.accbooktypename"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入账簿类别名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="外币折算日期" prop="convertDate">
            <el-radio-group v-model="formData.convertDate">
              <el-radio :label="0">卡片建卡日期</el-radio>
              <el-radio :label="1">业务发生日期</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="本币原值来源" prop="localoriginvalue">
            <el-input
              v-model="formData.localoriginvalue"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入本币原值来源"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="数据采集方案" prop="pkFinanplanname">
            <el-input
              v-model="formData.pkFinanplanname"
              disabled
              clearable
              placeholder="请输入数据采集方案"
              :style="{ width: '75%', marginRight: '10px' }"
            />
            <el-button type="primary" @click="handleSelectDataSource">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="明细" prop="bodyvos">
            <el-input
              v-model="formData.bodyvos"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入明细"
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
    <zbType ref="zbType" @zbTypeManage="handleSelectZbTypeManage" />
  </el-dialog>
</template>

<script>
  import { saveZbgl, getZbglDetail } from '@/api/cwsc'
  import departmentSelect from '@/components/department.vue'
  import bbModal from '@/views/cwsc/jcpz/components/bbModal.vue'
  import sjyModal from '@/views/cwsc/jcpz/components/sjyModal.vue'
  import zbType from '@/views/cwsc/jcpz/components/zbType.vue'
  export default {
    components: { departmentSelect, bbModal, sjyModal, zbType },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,

        tableData: [],
        formData: {
          bookName: '',
          pkSetofbook: '',
          accbooktypecode: '',
          accbooktypename: '',
          convertDate: '',
          localoriginvalue: '',
          pkGroup: '',
          pkOrg: '',
          pkFinanplanid: '',
          pkFinanplanname: '',
          bodyvos: '',
          pkAccbookinfo: '',
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
          const res = await getZbglDetail({ pkAccbookinfo: row.pkAccbookinfo })
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data[key]
            this.formData.pkFinanplanname = res.data.planName
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
          bookName: '',
          pkSetofbook: '',
          accbooktypecode: '',
          accbooktypename: '',
          convertDate: '',
          localoriginvalue: '',
          pkGroup: '',
          pkOrg: '',
          pkFinanplanid: '',
          pkFinanplanname: '',
          bodyvos: '',
          pkAccbookinfo: '',
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
            const res = await saveZbgl(params)
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
        this.formData.pkFinanplanname = val[0].fname
      },
      handleVersionManage(val) {
        this.formData.fversionid = val[0].fid
        this.formData.fversionname = val[0].handtext
      },
      handleZbTypeManage() {
        this.$refs.zbType.showEdit()
      },
      handleSelectZbTypeManage(val) {
        console.log(val)
        this.formData.pkSetofbook = val[0].pkSetofbook
        this.formData.accbooktypename = val[0].name
        this.formData.accbooktypecode = val[0].code
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
