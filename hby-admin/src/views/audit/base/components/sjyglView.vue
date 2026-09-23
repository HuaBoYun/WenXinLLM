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
        <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="formData.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              :disabled="disabled"
              @change="changeMJ"
            >
              <el-option
                v-for="item in MJoption"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="showMJ">
          <el-form-item label="知悉范围" prop="staffScopeNames">
            <el-input
              v-model="formData.staffScopeNames"
              disabled
              placeholder="请选择知悉范围"
              :style="{ width: '76%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>
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
              <el-radio label="DM8">DM8</el-radio>
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
              type="password"
              show-password
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
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'

  import {
    editDataSource,
    getDataSourceDefaultInfo,
    LinkTest,
  } from '@/api/setting/org'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  const token = store.getters['user/token']

  export default {
    components: { ZXPerson },
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
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
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
        showMJ: false,
        menuId: '',
        MJoption: [],
      }
    },
    computed: {},
    watch: {},
    async created() {
      // this.showMJ = couldMJ()
      if (this.showMJ) {
        // 获取密级,菜单id
        const res = await hasMJ('BaseSjygl')
        this.menuId = res[0].menuid
        // 请求密级下拉数据
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    methods: {
      changeMJ(selectedValue) {
        // selectedValue 就是选中的 value（即 levelId）
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeNames = ''
            this.formData.staffScopeIds = ''
          }
        }
      },
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
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
          this.formData.secrectLevelId = res.data.secrectLevelId
          this.formData.staffScopeNames = res.data.staffScopeNames
          this.formData.staffScopeIds = res.data.staffScopeIds
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
      /**
       * @description: 保存表单
       * @return {*}
       */
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
