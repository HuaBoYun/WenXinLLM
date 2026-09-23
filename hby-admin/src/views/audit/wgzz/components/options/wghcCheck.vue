<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="elForm"
        label-width="150px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="线索编号" prop="cluenaber">
            <el-input
              v-model="formData.cluenaber"
              clearable
              placeholder="请输入线索编号"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核实内容" prop="hscontent">
            <el-input
              v-model="formData.hscontent"
              clearable
              placeholder="请输入核实内容"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="核实范围" prop="hsfw">
            <el-input
              v-model="formData.hsfw"
              clearable
              placeholder="请输入核实范围"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工作组织" prop="orgname">
            <el-input
              v-model="formData.orgname"
              clearable
              placeholder="请输入工作组织"
              :style="{ width: '240px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.companyTreeModel.show(true)"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="责任定性" prop="zrdx">
            <el-input
              v-model="formData.zrdx"
              clearable
              placeholder="请输入责任定性"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产损失认定" prop="zcssrd">
            <el-input
              v-model="formData.zcssrd"
              clearable
              placeholder="请输入资产损失认定"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经办人" prop="jbrid">
            <el-input
              v-model="formData.jbrname"
              clearable
              placeholder="请输入经办人"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="!footer"
              @click="$refs.manage.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="责任追究处理建议" prop="zrzjcljy">
            <el-input
              v-model="formData.zrzjcljy"
              clearable
              type="textarea"
              row="4"
              placeholder="请输入责任追究处理建议"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="" prop="content">
            <UEditor
              ref="ueditor"
              v-model="formData.content"
              :height="300"
              :templates="templates"
              :disabled="!footer"
              template="nbsj"
              style="margin-left: -100px"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="check" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="check" type="primary">确定</el-button>
    </template>
    <CompanyTreeModel
      ref="companyTreeModel"
      @selected="selectCompany"
      :lable="'工作组织'"
    />
    <projectManage
      :modal="false"
      ref="manage"
      @reviewTypeSelect="reviewTypeSelect"
    />
  </el-dialog>
</template>

<script>
  import { wghcbgSave, wghcbgList } from '@/api/audit/wgzz'
  import store from '@/store'
  import CompanyTreeModel from '@/components/CompanyTreeModel/index.vue'
  const { baseURL } = require('@/config')
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'

  export default {
    name: 'FlawInfo',
    components: {
      UEditor: () => import('@/components/UEditor'),
      CompanyTreeModel,
      projectManage,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          cluenaber: undefined,
          verifycontent: undefined,
          content: undefined,
          jbrname: undefined,
          jbrid: undefined,
        },
        templates: [],
        footer: true,
        rules: {
          // cluenaber: [
          //   {
          //     required: true,
          //     message: '请输入线索编号',
          //     trigger: 'blur',
          //   },
          // ],
          // verifycontent: [
          //   {
          //     required: true,
          //     message: '请输入核实内容',
          //     trigger: 'blur',
          //   },
          // ],
        },
        dialogFormVisible: false,
        title: '核查',
        staffid: '',
        arr: [],
      }
    },
    computed: {},
    created() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.$set(this.formData, 'jbrid', userInfo.realname)
      this.$set(this.formData, 'jbrname', userInfo.staffid)
    },
    mounted() {},
    methods: {
      /**
       * @description: 打开表单
       * @param {*} row 传入数据
       * @return {*}
       */
      showEdit(row) {
        this.dialogFormVisible = true
        this.formData = { ...row, wghcid: row.id }
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.$set(this.formData, 'jbrname', userInfo.realname)
        this.$set(this.formData, 'jbrid', userInfo.staffid)
        wghcbgList({ pageNumber: 1, pageSize: 20 }).then((res) => {
          console.log(res)
        })
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.formData = {}
        this.dialogFormVisible = false
        this.footer = true
      },
      check() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            delete this.formData.id
            const data = await wghcbgSave({
              ...this.formData,
            })

            if (data.code == 1) {
              this.formData = {}
            }
            this.$emit('fetch-data')
            this.close()
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
      selectCompany(val) {
        console.log(val)
        let orgidnames = val.map((item) => item.name).join(',')
        let orgids = val.map((item) => item.id).join(',')
        this.$set(this.formData, 'orgname', orgidnames)
        this.$set(this.formData, 'orgid', orgids)
        this.$refs['ruleForm'].clearValidate()
      },
      reviewTypeSelect(e) {
        this.$set(this.formData, 'jbrname', e.id[0].realname)
        this.$set(this.formData, 'jbrid', e.id[0].staffid)
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
