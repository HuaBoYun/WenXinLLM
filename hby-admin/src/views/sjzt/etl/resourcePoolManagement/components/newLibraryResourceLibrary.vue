<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="600px"
    @close="close"
  >
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="24">
          <el-form-item label="资源库名称" prop="repoName">
            <el-input
              v-model="formData.repoName"
              clearable
              placeholder="请输入资源库名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="资源库类型" prop="type">
            <el-select
              v-model="formData.type"
              placeholder="请选择资源库类型"
              clearable
              :style="{ width: '100%' }"
            >
              <el-option label="文件资源库" value="File" />
              <el-option label="数据库资源库" value="db" />
            </el-select>
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="24">
          <el-form-item label="创建方式" prop="createdType">
            <el-select
              v-model="formData.createdType"
              placeholder="请选创建方式"
              clearable
              :style="{ width: '100%' }"
            >
              <el-option label="创建资源库" value="创建资源库" />
              <el-option label="连接资源库" value="连接资源库" />
            </el-select>
          </el-form-item>
        </el-col> -->
        <div v-if="formData.createdType == '连接资源库'">
          <el-col :span="24">
            <el-form-item label="登录资源库用户名" prop="libraryUserName">
              <el-input
                v-model="formData.libraryUserName"
                clearable
                placeholder="请输入登录资源库用户名"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="登录资源库密码" prop="libraryPsw">
              <el-input
                v-model="formData.libraryPsw"
                clearable
                placeholder="请输入登录资源库密码"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
        </div>
        <div v-if="formData.type == 'db'">
          <el-col :span="24">
            <el-form-item label="数据库类型" prop="dbType">
              <el-select
                v-model="formData.dbType"
                placeholder="数据库类型"
                clearable
                :style="{ width: '100%' }"
              >
                <el-option label="mySQL" value="mySQL" />
                <el-option label="oracle" value="oracle" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="数据库访问模式" prop="dbAccessMode">
              <el-select
                v-model="formData.dbAccessMode"
                placeholder="数据库数据库访问模式"
                clearable
                :style="{ width: '100%' }"
              >
                <el-option label="mySQL" value="mySQL" />
                <el-option label="oracle" value="oracle" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="数据库主机名或者IP地址" prop="dbIpAddress">
              <el-input
                v-model="formData.dbIpAddress"
                clearable
                placeholder="请输入数据库主机名或者IP地址"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="数据库端口号" prop="dbPort">
              <el-input
                v-model="formData.dbPort"
                clearable
                placeholder="请输入数据库端口号"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="数据库名称" prop="dbName">
              <el-input
                v-model="formData.dbName"
                clearable
                placeholder="请输入数据库名称"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="数据库登录账号" prop="dbUserName">
              <el-input
                v-model="formData.dbUserName"
                clearable
                placeholder="请输入数据库登录账号"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="数据库登录密码" prop="dbPsw">
              <el-input
                v-model="formData.dbPsw"
                clearable
                placeholder="请输入数据库登录密码"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
        </div>
        <el-col :span="24">
          <el-form-item label="文件资源库路径" prop="baseDir" v-if="!this.edit">
            <el-input
              v-model="formData.baseDir"
              clearable
              placeholder="请输入文件资源库路径"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <div></div>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取 消</el-button>
      <el-button @click="save" type="primary">保 存</el-button>
      <el-button @click="test" type="primary" v-if="formData.type == 'db'">
        测试连接
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
  import store from '@/store'
  import { baseURL } from '@/config'
  import { addRepository, editRepository } from '@/api/sjzt/etl/etl'

  const token = store.getters['user/token']
  import { formatDate } from '@/utils'
  export default {
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        edit: '',
        loading: false,
        baseURL: baseURL,
        headers: { token: token },
        tableData: [],
        formData: {
          type: 'File',
        },
        footer: true,
        rules: {
          repoName: [
            {
              required: true,
              message: '请输入资源库名称',
              trigger: 'blur',
            },
          ],
          baseDir: [
            {
              required: true,
              message: '请输入资源库路径',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增资源库',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      handlePreview() {},
      handleSuccess(res, b, c) {
        if (res && res.data) {
          this.tableData = this.tableData.concat(res.data.fileIds || [])
        }
      },
      handleDown(row) {},
      handleDelete(row) {
        const { fileId } = row
        const i = this.tableData.findIndex((x) => x.fileId === fileId)
        if (i > -1) {
          this.tableData.splice(i, 1)
        }
      },
      async showEdit(title, row) {
        if (row) {
          this.edit = title
          this.formData = row
        } else {
          this.formData = { type: 'File' }
        }
        this.dialogFormVisible = true
      },
      close() {
        this.formData = { type: 'File' }
        this.tableData = []
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.loading = true
        if (this.edit == 'edit') {
          // this.formData.baseDir = ''
          editRepository(this.formData).then((res) => {
            if (res.msg == '操作成功') {
              this.dialogFormVisible = false
              this.$emit('fetch-data')
            }
          })
        } else {
          addRepository(this.formData).then((res) => {
            if (res.msg == '操作成功') {
              this.dialogFormVisible = false
              this.$emit('fetch-data')
            }
          })
        }

        this.loading = false
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
