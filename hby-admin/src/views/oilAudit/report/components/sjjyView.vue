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
        label-width="210px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="审计报告定稿" prop="sjbgdgTitle">
            <el-input
              v-model="formData.sjbgdgTitle"
              :style="{ width: '70%' }"
              disabled
              placeholder="审计报告定稿"
            />
            <el-button
              @click="handlePlan"
              style="margin-left: 15px"
              type="primary"
              size="mini"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="建议层级类型" prop="hierarchyType">
            <el-select
              v-model="formData.hierarchyType"
              placeholder="请选择建议层级类型"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="建议涉及业务类型" prop="businessType">
            <el-select
              v-model="formData.businessType"
              placeholder="请选择建议涉及业务类型"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in options1"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="建议标题" prop="title">
            <el-input
              v-model="formData.title"
              clearable
              placeholder="请输入建议标题"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="建议描述" prop="details">
            <el-input
              v-model="formData.details"
              :style="{ width: '100%' }"
              clearable
              :rows="4"
              type="textarea"
              placeholder="请输入建议描述"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="关联底稿" prop="draft">
            <el-input
              v-model="formData.draft"
              clearable
              disabled
              placeholder="请选择关联底稿"
              :style="{ width: '90%' }"
            />
            <el-button
              style="margin-left: 10px"
              type="primary"
              @click="handleDraft"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
    <suggestModal ref="suggestModal" @selected="handlePlanSelected" />
    <myDraftModal ref="myDraftModal" @selected="handlePlanSelecteds" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import suggestModal from '@/views/oilAudit/rectify/components/suggestModal.vue'
  import myDraftModal from '@/views/oilAudit/rectify/components/myDraftModal.vue'
  import store from '@/store'
  import { editInfo, getDetailInfo } from '@/oapi/yqns_sjzg/sjjy'
  import { sjbgdgDetail } from '@/oapi/audit/report'

  const token = store.getters['user/token']

  export default {
    components: { suggestModal, myDraftModal },
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
          sjbgdgTitle: '',
          sjbgdgid: '',
          hierarchyType: '',
          businessType: '',
          title: '',
          draft: '',
          draftIdStrs: '',
          details: '',
          projectId: '',
          id: '',
        },
        footer: true,
        rules: {
          hierarchyType: [
            {
              required: true,
              message: '请选择建议层级类型',
              trigger: ['blur', 'change'],
            },
          ],
          businessType: [
            {
              required: true,
              message: '请选择建议涉及业务类型',
              trigger: ['blur', 'change'],
            },
          ],
          title: [
            {
              required: true,
              message: '请输入建议标题',
              trigger: 'blur',
            },
          ],
          // draft: [
          //   {
          //     required: true,
          //     message: '请选择关联底稿',
          //     trigger: ['blur', 'change'],
          //   },
          // ],
          details: [
            {
              required: true,
              message: '请输入建议描述',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        options: [
          {
            value: '集团公司级建议',
            label: '集团公司级建议',
          },
          {
            value: '地区公司级建议',
            label: '地区公司级建议',
          },
          {
            value: '基层单位级建议',
            label: '基层单位级建议',
          },
        ],
        options1: [
          {
            value: '公司战略',
            label: '公司战略',
          },
          {
            value: '投资计划与管理',
            label: '投资计划与管理',
          },
          {
            value: '基本建设工程',
            label: '基本建设工程',
          },
          {
            value: '生产管理',
            label: '生产管理',
          },
          {
            value: '会计核算',
            label: '会计核算',
          },
          {
            value: '财务管理',
            label: '财务管理',
          },
          {
            value: '资产管理',
            label: '资产管理',
          },
          {
            value: '人力资源',
            label: '人力资源',
          },
          {
            value: '预算管理',
            label: '预算管理',
          },
          {
            value: '资本运作',
            label: '资本运作',
          },
          {
            value: '股权管理',
            label: '股权管理',
          },
          {
            value: '销售与市场',
            label: '销售与市场',
          },
          {
            value: '合同管理',
            label: '合同管理',
          },
          {
            value: '法律事务',
            label: '法律事务',
          },
          {
            value: '安全环保',
            label: '安全环保',
          },
          {
            value: '质量管理',
            label: '质量管理',
          },
          {
            value: '节能',
            label: '节能',
          },
          {
            value: '科技管理',
            label: '科技管理',
          },
          {
            value: '信息管理',
            label: '信息管理',
          },
          {
            value: '物资采购',
            label: '物资采购',
          },
          {
            value: '海外业务',
            label: '海外业务',
          },
          {
            value: '境内对外合作',
            label: '境内对外合作',
          },
          {
            value: '内控与风险',
            label: '内控与风险',
          },
          {
            value: '矿区服务',
            label: '矿区服务',
          },
          {
            value: '工会',
            label: '工会',
          },
        ],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      handlePlan() {
        this.$refs['suggestModal'].showEdit()
      },
      handleDraft() {
        if (!this.formData.projectId) {
          this.$message({
            message: '请先选择审计报告定稿！',
            type: 'error',
          })
          return
        }
        this.$refs['myDraftModal'].showEdit(this.formData.projectId)
      },
      async handlePlanSelected(val) {
        this.formData.sjbgdgTitle = val[0].title
        this.formData.sjbgdgid = val[0].sjbgdgid
        this.formData.projectId = val[0].projectId
      },
      handlePlanSelecteds(e) {
        this.$set(
          this.formData,
          'draft',
          e.map((item) => item.draftName).join(',')
        )
        this.$set(
          this.formData,
          'draftIdStrs',
          e.map((item) => item.id).join(',')
        )
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          const res = await getDetailInfo({ proposeId: row.id, wtzgid: -1 })
          this.formData.sjbgdgTitle = res.data.tblYqnsProposeEntity.sjbgdgTitle
          this.formData.sjbgdgid = res.data.tblYqnsProposeEntity.sjbgdgid
          this.formData.businessType =
            res.data.tblYqnsProposeEntity.businessType
          this.formData.draftIdStrs = res.data.tblYqnsProposeEntity.draftIdStrs
          this.formData.title = res.data.tblYqnsProposeEntity.title
          this.formData.hierarchyType =
            res.data.tblYqnsProposeEntity.hierarchyType
          this.formData.details = res.data.tblYqnsProposeEntity.details
          this.formData.draft = res.data.tblYqnsProposeEntity.draft
          this.formData.id = res.data.tblYqnsProposeEntity.id
          const zz = await sjbgdgDetail({
            sjbgdgid: res.data.tblYqnsProposeEntity.sjbgdgid,
          })

          this.formData.projectId = zz.data.projectId
          // this.formData = JSON.parse(JSON.stringify(row))
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'details') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            createdUser: resL,
            createdTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData = {
          sjbgdgTitle: '',
          sjbgdgid: '',
          hierarchyType: '',
          businessType: '',
          title: '',
          draft: '',
          draftIdStrs: '',
          details: '',
          projectId: '',
          id: '',
        }
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            // return
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await editInfo({ ...params, wtzgid: -1 })
            if (res && (res.code == 200 || res.code == 1)) {
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
        return
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
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      async getChildlistPro(val) {
        this.staffid = val[0].staffid
        this.$set(this.formData, 'orgName', val[0].realname)
      },
    },
  }
</script>
<style scoped>
  .el-form-item__contractname span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
