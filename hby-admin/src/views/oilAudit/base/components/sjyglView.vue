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
          <el-form-item label="数据库名称" prop="dataBaseOwnership">
            <el-input
              v-model="formData.dataBaseOwnership"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入数据库名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据库类型" prop="dataBaseType">
            <el-radio-group v-model="formData.dataBaseType">
              <el-radio label="Oracle">Oracle</el-radio>
              <el-radio label="Mysql">Mysql</el-radio>
              <el-radio label="SqlServer">SqlServer</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据库连接 " prop="dataBaseConnectionAddress">
            <el-input
              v-model="formData.dataBaseConnectionAddress"
              clearable
              placeholder="请输入数据库连接"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据库用户" prop="dataBaseUsers">
            <el-input
              v-model="formData.dataBaseUsers"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入数据库用户"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据库密码" prop="dataBasePassWord">
            <el-input
              v-model="formData.dataBasePassWord"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入数据库密码"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="checkLink">链接测试</el-button>
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'

  import {
    editDataSource,
    getDataSourceDefaultInfo,
    LinkTest,
  } from '@/oapi/setting/org'
  import { getPrivewAttInfo } from '@/oapi/contract/manage'

  const token = store.getters['user/token']

  export default {
    components: {},
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: '/audit/fileManage/upload',
        headers: { token: token },
        tableData: [],
        formData: {
          createType: 1,
          dataBaseConnectionAddress: '',
          dataBasePassWord: '',
          dataBaseOwnership: '',
          dataBaseType: 'Oracle',
          dataBaseUsers: '',
          id: '',
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
    watch: {},
    created() {},
    mounted() {},
    methods: {
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          const res = await getDataSourceDefaultInfo({ id: row.id })
          this.formData.createType = res.data.createType
          this.formData.dataBaseConnectionAddress =
            res.data.dataBaseConnectionAddress
          this.formData.dataBaseOwnership = res.data.dataBaseOwnership
          this.formData.dataBasePassWord = res.data.dataBasePassWord
          this.formData.dataBaseType = res.data.dataBaseType
          this.formData.dataBaseUsers = res.data.dataBaseUsers
          this.formData.id = res.data.id
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
      close() {
        ;(this.formData = {
          createType: 1,
          dataBaseConnectionAddress: '',
          dataBasePassWord: '',
          dataBaseOwnership: '',
          dataBaseType: 'Oracle',
          dataBaseUsers: '',
          id: '',
        }),
          (this.dialogFormVisible = false)
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await editDataSource(params)
            if (res && res.code == 200) {
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
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
      async checkLink() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await LinkTest({
              dataBaseConnectionAddress:
                this.formData.dataBaseConnectionAddress,
              dataBasePassWord: this.formData.dataBasePassWord,
              dataBaseType: this.formData.dataBaseType,
              dataBaseUsers: this.formData.dataBaseUsers,
            })
            if (res.code == 1) {
              this.$message({
                message: res.msg,
                type: 'success',
              })
            } else {
              this.$message({
                message: res.msg,
                type: 'error',
              })
            }
          }
        })
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
