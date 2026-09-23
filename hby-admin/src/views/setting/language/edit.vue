<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
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
        <el-col :span="24">
          <el-form-item label="简体中文 " prop="zh">
            <el-input
              v-model="formData.zh"
              clearable
              placeholder="请输入简体中文"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="繁体中文 " prop="ZH">
            <el-input
              v-model="formData.ZH"
              clearable
              placeholder="繁体中文"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="English " prop="en">
            <el-input
              v-model="formData.en"
              clearable
              placeholder="Plese type English"
              :style="{ width: '100%' }"
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
  import { saveCjfa, getCjfaDetail } from '@/api/cwsc'
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
          fid: '',
          fname: '',
          financetype: '',
          financeorgname: '',
          fversionid: '',
          fversionname: '',
          dbconfigid: '',
          dbconfigname: '',
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
          const res = await getCjfaDetail({ fid: row.fid })
          this.formData = res.data
          //   this.formData.createType = res.data.createType
          //   this.formData.dataBaseConnectionAddress =
          //     res.data.dataBaseConnectionAddress
          //   this.formData.dataBaseOwnership = res.data.dataBaseOwnership
          //   this.formData.dataBasePassWord = res.data.dataBasePassWord
          //   this.formData.dataBaseType = res.data.dataBaseType
          //   this.formData.dataBaseUsers = res.data.dataBaseUsers
          //   this.formData.id = res.data.id
          //   this.formData.secrectLevelId = res.data.secrectLevelId
          //   this.formData.staffScopeNames = res.data.staffScopeNames
          //   this.formData.staffScopeIds = res.data.staffScopeIds
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
          fintext: '',
          financedbtype: '',
          financeconn: '',
          financeport: '',
          financedbexpm: '',
          financeuser: '',
          financepwd: '',
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
            const res = await saveCjfa(params)
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
        this.formData.dbconfigid = val[0].fid
        this.formData.dbconfigname = val[0].fintext
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
