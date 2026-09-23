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
          <el-form-item label="数据库名称" prop="fintext">
            <el-input
              v-model="formData.fintext"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入数据库名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据库类型" prop="financedbtype">
            <el-radio-group v-model="formData.financedbtype">
              <el-radio label="Oracle">Oracle</el-radio>
              <el-radio label="Mysql">Mysql</el-radio>
              <el-radio label="SqlServer">SqlServer</el-radio>
              <el-radio label="DM">达梦</el-radio>
              <el-radio label="inceptor">inceptor</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据库连接 " prop="financeconn">
            <el-input
              v-model="formData.financeconn"
              clearable
              placeholder="请输入数据库连接"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据库端口 " prop="financeport">
            <el-input
              v-model="formData.financeport"
              clearable
              placeholder="请输入数据库端口"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据库实例 " prop="financedbexpm">
            <el-input
              v-model="formData.financedbexpm"
              clearable
              placeholder="请输入数据库实例"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据库用户" prop="financeuser">
            <el-input
              v-model="formData.financeuser"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入数据库用户"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据库密码" prop="financepwd">
            <el-input
              v-model="formData.financepwd"
              :style="{ width: '100%' }"
              v-secure-input
              clearable
              placeholder="请输入数据库密码"
              type="password"
              autocomplete="new-password"
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
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>

<script>
  import {
    saveDataSource,
    getDataSourceDetail,
    testDataSource,
  } from '@/api/cwsc'
  import ZXPerson from '@/components/selectPerson.vue'
  import CryptoJS from 'crypto-js'

  export default {
    components: { ZXPerson },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,

        tableData: [],
        formData: {
          fid: '',
          fintext: '',
          financedbtype: '',
          financeconn: '',
          financeport: '',
          financedbexpm: '',
          financeuser: '',
          financepwd: '',
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
        encryptionKey: 'xingguangdamoxing', // 加密密钥，建议存储在环境变量或配置文件中
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
        // if (row) {
        const res = await getDataSourceDetail({ fid: row.fid })
        Object.keys(this.formData).forEach((key) => {
          this.formData[key] = res.data[key]
        })
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
        // }

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
       * @description: 加密密码
       * @param {string} password 原始密码
       * @return {string} 加密后的密码
       */
      encryptPassword(password) {
        return CryptoJS.AES.encrypt(password, this.encryptionKey).toString()
      },

      /**
       * @description: 保存表单
       * @return {*}
       */
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = JSON.parse(JSON.stringify(this.formData))

            // 加密密码
            // if (params.financepwd) {
            //   params.financepwd = encodeURIComponent(
            //     this.encryptPassword(params.financepwd)
            //   )
            // }

            const res = await saveDataSource(params)
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

      async checkLink() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            // 创建一个新对象用于传输
            const params = {
              financeconn: this.formData.financeconn,
              financeport: this.formData.financeport,
              financedbexpm: this.formData.financedbexpm,
              financeuser: this.formData.financeuser,
              // 加密密码
              financepwd: this.formData.financepwd,
              // financepwd: encodeURIComponent(
              //   this.encryptPassword(this.formData.financepwd)
              // ),
              financedbtype: this.formData.financedbtype,
            }

            const res = await testDataSource(params)
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
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.$set(this.formData, 'staffScopeIds', ids)
        this.$set(this.formData, 'staffScopeNames', names)
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
