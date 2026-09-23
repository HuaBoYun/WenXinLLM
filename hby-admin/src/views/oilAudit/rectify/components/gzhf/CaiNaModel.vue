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
              disabled
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
              disabled
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
              disabled
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
        <el-col :span="12">
          <el-form-item label="建议标题" prop="title">
            <el-input
              v-model="formData.title"
              clearable
              placeholder="请输入建议标题"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="关联底稿" prop="draft">
            <el-input
              v-model="formData.draft"
              clearable
              disabled
              placeholder="请选择关联底稿"
              :style="{ width: '70%' }"
            />
            <el-button
              style="margin-left: 10px"
              type="primary"
              @click="handleDraft"
              disabled
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="建议描述" prop="details">
            <el-input
              v-model="formData.details"
              disabled
              :style="{ width: '100%' }"
              clearable
              :rows="4"
              type="textarea"
              placeholder="请输入建议描述"
            />
          </el-form-item>
        </el-col>

        <!-- <el-col :span="12">
          <el-form-item label="是否采纳" prop=" isAdopt">
            <el-select v-model="formData.isAdopt" placeholder="请选择建议涉及业务类型" :style="{ width: '100%' }" >
              <el-option v-for="item in  isAdoptoptions1" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col> -->
        <template v-if="formData.isAdopt == 1">
          <el-col :span="12">
            <el-form-item label="直接经济成果类型" prop="zjjjcglx">
              <el-select
                v-model="formData.zjjjcglx"
                placeholder="请选择直接经济成果类型"
                :style="{ width: '100%' }"
                @change="$forceUpdate()"
              >
                <el-option
                  v-for="(item, index) in typeOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="直接经济成果金额（元）" prop="zjjjcgje">
              <el-input
                v-model="formData.zjjjcgje"
                clearable
                placeholder="请输入"
                type="number"
                :style="{ width: '100%' }"
                @input="inputMoney($event, 'zjjjcgje')"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="其他经济成果类型" prop="qtjjcglx">
              <el-select
                v-model="formData.qtjjcglx"
                placeholder="请选择当期其他经济成果类型"
                :style="{ width: '100%' }"
                @change="$forceUpdate()"
              >
                <el-option
                  v-for="(item, index) in dqzjjjcgtypeTypeOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="其他经济成果金额（元）" prop="qtjjcgje">
              <el-input
                v-model="formData.qtjjcgje"
                clearable
                placeholder="请输入"
                type="number"
                :style="{ width: '100%' }"
                @input="inputMoney($event, 'qtjjcgje')"
              />
            </el-form-item>
          </el-col>
        </template>
        <template v-else>
          <el-col :span="24">
            <el-form-item label="不采纳原因" prop="notReason">
              <el-input
                v-model="formData.notReason"
                clearable
                placeholder="请输入"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
        </template>
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
  import { saveAuditProposeAdopt, getDetailInfo } from '@/oapi/yqns_sjzg/sjjy'

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
          id: '',
          isAdopt: '',
          zjjjcglx: '',
          zjjjcgje: '',
          qtjjcglx: '',
          qtjjcgje: '',
          notReason: '',
          adoptId: '',
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
        isAdoptoptions1: [
          {
            value: '1',
            label: '是',
          },
          {
            value: '0',
            label: '否',
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
        typeOptions: [
          {
            label: '节约各类开支',
            value: '节约各类开支',
          },
          {
            label: '建设工程项目审减',
            value: '建设工程项目审减',
          },
          {
            label: '经济合同审减',
            value: '经济合同审减',
          },
          {
            label: '物资采购项目审减',
            value: '物资采购项目审减',
          },
          {
            label: '招投标审减',
            value: '招投标审减',
          },
          {
            label: '剔除联合账簿不合理费用',
            value: '剔除联合账簿不合理费用',
          },
          {
            label: '挽回各类损失',
            value: '挽回各类损失',
          },
          {
            label: '挽回债券性和股权性投资损失',
            value: '挽回债券性和股权性投资损失',
          },
          {
            label: '挽回货币性资产损失',
            value: '挽回货币性资产损失',
          },
          {
            label: '挽回非货币性资产损失',
            value: '挽回非货币性资产损失',
          },
          {
            label: '内部收缴',
            value: '内部收缴',
          },
          {
            label: '罚款金额',
            value: '罚款金额',
          },
          {
            label: '收缴小金库',
            value: '收缴小金库',
          },
          {
            label: '账外资金',
            value: '账外资金',
          },
          {
            label: '收缴/罚没其他违规违纪资金',
            value: '收缴/罚没其他违规违纪资金',
          },
        ],
        dqzjjjcgtypeTypeOptions: [
          {
            label: '促进清理债权债务',
            value: '促进清理债权债务',
          },
          {
            label: '促进清理不良资产',
            value: '促进清理不良资产',
          },
          {
            label: '促进解除担保责任',
            value: '促进解除担保责任',
          },
          {
            label: '促进增效',
            value: '促进增效',
          },
          {
            label: '收回上级投入资金结余',
            value: '收回上级投入资金结余',
          },
          {
            label: '调整账目',
            value: '调整账目',
          },
          {
            label: '其他',
            value: '其他',
          },
        ],
        wtzgid: '',
        proposeId: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      //金额输入
      inputMoney(value, key) {
        // 移除非数字字符和小数点
        let sanitizedValue = value.replace(/[^0-9.]/g, '')
        // 如果输入的是小数点，确保只有一个小数点
        if (sanitizedValue.indexOf('.') !== sanitizedValue.lastIndexOf('.')) {
          sanitizedValue = sanitizedValue.slice(
            0,
            sanitizedValue.lastIndexOf('.')
          )
        }
        // 如果输入的是0开头且后面有其他数字，去掉开头的0
        if (
          sanitizedValue.startsWith('0') &&
          sanitizedValue.length > 1 &&
          sanitizedValue[1] !== '.'
        ) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是小数点开头，前面加0
        if (sanitizedValue.startsWith('.')) {
          sanitizedValue = '0' + sanitizedValue
        }
        // 如果输入的是负数，去掉负号
        if (sanitizedValue.startsWith('-')) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是空字符串或0，设置为空字符串
        if (sanitizedValue === '' || sanitizedValue === '0') {
          sanitizedValue = ''
        }
        // 更新输入框的值
        const keys = key.split('.')
        let formDataRef = this.formData
        for (let i = 0; i < keys.length - 1; i++) {
          formDataRef = formDataRef[keys[i]]
        }
        formDataRef[keys[keys.length - 1]] = sanitizedValue
      },
      handlePlan() {
        this.$refs['suggestModal'].showEdit()
      },
      handleDraft() {
        this.$refs['myDraftModal'].showEdit()
      },
      async handlePlanSelected(val) {
        this.formData.sjbgdgTitle = val[0].title
        this.formData.sjbgdgid = val[0].sjbgdgid
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
      async showEdit(row, title, wtzgid) {
        this.wtzgid = wtzgid
        this.proposeId = row.id
        this.dialogFormVisible = true
        if (row) {
          const res = await getDetailInfo({ proposeId: row.id, wtzgid })
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
          // this.formData = JSON.parse(JSON.stringify(row))

          this.formData.zjjjcglx = res.data.adopt && res.data.adopt.zjjjcglx
          this.formData.zjjjcgje = res.data.adopt && res.data.adopt.zjjjcgje
          this.formData.qtjjcglx = res.data.adopt && res.data.adopt.qtjjcglx
          this.formData.qtjjcgje = res.data.adopt && res.data.adopt.qtjjcgje
          this.formData.notReason = res.data.adopt && res.data.adopt.notReason
          this.formData.adoptId = res.data.adopt && res.data.adopt.adoptId
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
        } else if (title == '1') {
          this.title = '采纳'
          this.formData.isAdopt = '1'
        } else if (title == '2') {
          this.title = '不采纳'
          this.formData.isAdopt = '0'
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
          id: '',
          isAdopt: '',
          zjjjcglx: '',
          zjjjcgje: '',
          qtjjcglx: '',
          qtjjcgje: '',
          notReason: '',
          adoptId: '',
        }
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            // return
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await saveAuditProposeAdopt({
              ...params,
              wtzgid: this.wtzgid,
              proposeId: this.proposeId,
            })
            if (res && (res.code == 200 || res.code == 1)) {
              this.close()
              this.$emit('fetchData', this.wtzgid)
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
