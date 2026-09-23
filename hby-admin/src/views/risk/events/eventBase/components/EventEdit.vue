<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      append-to-body
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          label-width="140px"
          :model="formData"
          :rules="rules"
          size="medium"
          :disabled="alldisabled"
        >
          <el-col :span="24">
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
                  readonly
                  placeholder="请选择知悉范围"
                  :style="{ width: '75%' }"
                  disabled
                />
                <el-button
                  :style="{ marginLeft: '10px' }"
                  type="primary"
                  @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
                  :disabled="!formData.secrectLevelId || alldisabled"
                >
                  选择
                </el-button>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-divider>基本信息</el-divider>
            </el-col>
            <el-col :span="12">
              <el-form-item label="事件编号" prop="riskeventcode">
                <el-input
                  readonly
                  v-model="formData.riskeventcode"
                  placeholder="请输入事件编号"
                  :style="{ width: '100%' }"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="事件名称" prop="riskeventname">
                <el-input
                  v-model="formData.riskeventname"
                  clearable
                  placeholder="请输入事件名称"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发生部门" prop="occureddepartment">
                <el-input
                  v-model="formData.occureddepartment"
                  disabled
                  style="width: 75%; margin-right: 8px"
                  placeholder="请选择发生部门"
                ></el-input>
                <el-button type="primary" @click="$refs.audiTree.showEdit()">
                  选择
                </el-button>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发生日期" prop="occureddate">
                <el-date-picker
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                  v-model="formData.occureddate"
                  clearable
                  placeholder="请选择发生日期"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发现日期" prop="discovereddate">
                <el-date-picker
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                  v-model="formData.discovereddate"
                  clearable
                  placeholder="请输入发现日期"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="损失事件定性类别" prop="losseventcategory">
                <el-select
                  v-model="formData.losseventcategory"
                  clearable
                  placeholder="请选择损失事件定性类别"
                  :style="{ width: '100%' }"
                >
                  <el-option
                    v-for="(item, index) in field106Options"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="经办人" prop="riskfactor2">
                <el-input v-model="formData.riskfactor2" disabled></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="经办部门" prop="recorddepart">
                <el-input v-model="formData.recorddepart" disabled></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="版本号" prop="version">
                <el-input
                  v-model="formData.version"
                  placeholder="请输入版本号"
                  disabled
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="涉及企业名称"
                prop="involvingenterprisenames"
              >
                <el-input
                  v-model="formData.involvingenterprisenames"
                  placeholder="请输入涉及企业名称"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="涉及企业层级"
                prop="involvingenterprisehierarchy"
              >
                <el-input
                  v-model="formData.involvingenterprisehierarchy"
                  placeholder="请输入涉及企业层级"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="填报状态" prop="fillingStatus">
                <el-select
                  v-model="formData.fillingStatus"
                  clearable
                  placeholder="请选择填报状态"
                  :style="{ width: '100%' }"
                >
                  <el-option
                    v-for="(item, index) in field107Options"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="损失(风险)金额(万元)" prop="amountofdamages">
                <el-input
                  v-model="formData.amountofdamages"
                  placeholder="请输入损失(风险)金额"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="事件说明" prop="riskeventdescription">
                <el-input
                  v-model="formData.riskeventdescription"
                  clearable
                  placeholder="请输入事件说明"
                  :style="{ width: '100%' }"
                  type="textarea"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="当期情况描述" prop="description">
                <el-input
                  v-model="formData.description"
                  clearable
                  placeholder="请输入当期情况描述"
                  :style="{ width: '100%' }"
                  type="textarea"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="处置进展情况" prop="progressofdisposal">
                <el-input
                  v-model="formData.progressofdisposal"
                  clearable
                  placeholder="请输入处置进展情况"
                  :style="{ width: '100%' }"
                  type="textarea"
                />
              </el-form-item>
            </el-col>

            <!-- <el-col :span="24">
              <el-divider>财务信息</el-divider>
            </el-col>
            <el-col :span="24">
              <el-form-item label="最大预估损失汇总金额" prop="maxestimateloss">
                <el-input
                  v-model="formData.maxestimateloss"
                  clearable
                  type="number"
                  placeholder="请输入最大预估损失汇总金额"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item
                label="已确认的直接损失总金额"
                prop="confirmeddirectloss"
              >
                <el-input
                  v-model="formData.confirmeddirectloss"
                  clearable
                  type="number"
                  placeholder="请输入已确认的直接损失总金额"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item
                label="已确认的直接损失净额"
                prop="confirmeddirectlossa"
              >
                <el-input
                  v-model="formData.confirmeddirectlossa"
                  clearable
                  type="number"
                  placeholder="请输入已确认的直接损失净额"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-divider>索赔信息</el-divider>
            </el-col>
            <el-col :span="12">
              <el-form-item label="索赔类型" prop="claimtype">
                <el-input
                  v-model="formData.claimtype"
                  clearable
                  placeholder="请输入索赔类型"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="索赔名称" prop="claimname">
                <el-input
                  v-model="formData.claimname"
                  clearable
                  placeholder="请输入索赔名称"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="贷方会计科目代码" prop="accidd">
                <el-input
                  v-model="formData.accidd"
                  clearable
                  placeholder="请输入贷方会计科目代码"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="贷方会计科目名称" prop="accnamed">
                <el-input
                  v-model="formData.accnamed"
                  clearable
                  placeholder="请输入贷方会计科目名称"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="索赔日" prop="claimdate">
                <el-date-picker
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                  v-model="formData.claimdate"
                  clearable
                  placeholder="请选择索赔日"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="入账日" prop="indate">
                <el-date-picker
                  v-model="formData.indate"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                  clearable
                  placeholder="请选择入账日"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="索赔金额" prop="pricenumber">
                <el-input
                  v-model="formData.pricenumber"
                  clearable
                  type="number"
                  placeholder="输入"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="索赔说明" prop="claimdes">
                <el-input
                  v-model="formData.claimdes"
                  clearable
                  placeholder="请输入索赔说明"
                  :style="{ width: '100%' }"
                  type="textarea"
                />
              </el-form-item>
            </el-col> -->
            <el-col :span="24">
              <UEditor
                ref="ueditor"
                v-model="formData.content"
                :height="300"
                :templates="templates"
                template="FXSJBG"
              />
            </el-col>
            <el-col :span="24">
              <el-divider>附件</el-divider>
              <!-- <el-divider>附件 {{ this.formData.riseveid }}</el-divider> -->
            </el-col>
            <el-col :span="24">
              <div
                style="text-align: right; margin-bottom: 5px"
                v-if="!alldisabled"
              >
                <el-upload
                  style="text-align: right; margin-bottom: 5px"
                  class="upload-demo"
                  :show-file-list="false"
                  action=""
                  :headers="headers"
                  :on-preview="handlePreview"
                  :on-success="handleSuccess"
                  :file-list="fileList"
                  :before-upload="handleBeforeUpload"
                  :multiple="true"
                >
                  <div style="margin-right: 10px">
                    <el-button type="success">点击上传</el-button>
                  </div>
                </el-upload>
              </div>
              <el-table :data="tableDataFile">
                <el-table-column
                  align="center"
                  label="附件名称"
                  prop="attname"
                />
                <el-table-column
                  align="center"
                  label="文件大小(KB)"
                  prop="attsize"
                />
                <el-table-column
                  align="center"
                  label="创建人"
                  prop="uploader"
                />
                <el-table-column
                  align="center"
                  label="操作"
                  show-overflow-tooltip
                  width="120"
                >
                  <template #default="{ row }">
                    <el-button
                      type="text"
                      @click="handleDowns(row)"
                      :disabled="false"
                    >
                      下载
                    </el-button>

                    <el-button
                      type="text"
                      @click="handlePreviewFile(row)"
                      :disabled="false"
                    >
                      预览
                    </el-button>
                    <el-button
                      type="text"
                      @click="handleDelete(row)"
                      v-if="!alldisabled"
                    >
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-col>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" :loading="saveLoading" @click="save">
          确 定
        </el-button>
        <!-- <el-button
        v-if="(formData.status == 2 || formData.status == 3)&&jurisdictionCode==1"
        @click="ymsubmit"
        type="primary"
      >
        提交
      </el-button> -->
      </template>
    </el-dialog>
    <CompanyTreeModel ref="comTreeRef" @selected="handleSelectCompany" />
    <company-select-modal ref="audiTree" @submit="handleCompanyTreeSelected" />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>
<script>
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import UEditor from '@/components/UEditor'
  import {
    createProjectCode,
    createProjectCodeNew,
  } from '@/api/internal/project'
  import { deleteFieldById } from '@/api/risk/riskEvents'
  import {
    getFxsjkDetails,
    getFxsjkspDetails,
    addFxsjData,
    addFxsjspData,
    downFieldById,
    getMaxVersion,
  } from '@/api/risk/riskEvents'
  import CompanyTreeModel from '@/components/CompanyTreeModel'
  import dayjs from 'dayjs'
  const { baseURL } = require('@/config')
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import CompanySelectModal from '@/views/oilAudit/jhlx/components/department.vue'
  import ZXPerson from '@/components/selectPerson'
  import { couldMJ, hasMJ } from '@/utils'
  import { getMJ } from '@/api/setting/mjsz'
  import store from '@/store'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'EventEdit',
    components: {
      CompanyTreeModel,
      CandidateUserSelect,
      UEditor,
      CompanySelectModal,
      ZXPerson,
    },
    inheritAttrs: false,
    props: ['riskcatid'],
    data() {
      return {
        tableDataFile: [],
        fileIdList: [],
        templates: [],
        removeIds: [],
        baseApi: baseURL,
        uploadLoading: false,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        fileList: [],
        headers: {
          token: store.getters['user/token'],
        },
        dayjs: dayjs,
        activeName: 'first',
        title: '',
        dialogFormVisible: false,
        formData: {
          version: '1',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
          fillingStatus: '',
        },
        list: [{ name: 'XXXXX' }],
        tableData: [{ name: 'XXXXX' }],
        rules: {
          riskeventcode: [
            {
              required: true,
              message: '请输入事件编号',
              trigger: 'blur',
            },
          ],
          riskeventname: [
            {
              required: true,
              message: '请输入事件名称',
              trigger: 'blur',
            },
          ],
          occureddepartment: [
            {
              required: true,
              message: '请输入发生部门',
              trigger: 'blur',
            },
          ],
          occureddate: [
            {
              required: true,
              message: '请选择发生日期',
              trigger: 'change',
            },
          ],
          discovereddate: [
            {
              required: true,
              message: '请选择发现日期',
              trigger: 'change',
            },
          ],
          losseventcategory: [
            {
              required: true,
              message: '请选择损失事件定义类别',
              trigger: 'change',
            },
          ],
          amountofdamages: [
            {
              required: false,
              message: '请输入损失(风险)金额',
              trigger: 'blur',
            },
            {
              pattern: /^\d+(\.\d+)?$/,
              message: '请输入有效的数字或小数',
              trigger: 'blur',
            },
          ],
          // maxestimateloss: [
          //   {
          //     required: true,
          //     message: '请输入最大预估损失汇总金额',
          //     trigger: 'blur',
          //   },
          // ],
          // confirmeddirectloss: [
          //   {
          //     required: true,
          //     message: '请输入已确认的直接损失总金额',
          //     trigger: 'blur',
          //   },
          // ],
          // confirmeddirectlossa: [
          //   {
          //     required: true,
          //     message: '请输入已确认的直接损失净额',
          //     trigger: 'change',
          //   },
          // ],
          // claimtype: [
          //   {
          //     required: true,
          //     message: '请输入索赔类型',
          //     trigger: 'blur',
          //   },
          // ],
          // claimname: [
          //   {
          //     required: true,
          //     message: '请输入索赔名称',
          //     trigger: 'blur',
          //   },
          // ],
          // accidd: [
          //   {
          //     required: true,
          //     message: '请输入贷方会计科目代码',
          //     trigger: 'blur',
          //   },
          // ],
          // accnamed: [
          //   {
          //     required: true,
          //     message: '请输入贷方会计科目名称',
          //     trigger: 'blur',
          //   },
          // ],
          // claimdate: [
          //   {
          //     required: true,
          //     message: '请选择索赔日',
          //     trigger: 'change',
          //   },
          // ],
          // indate: [
          //   {
          //     required: true,
          //     message: '请选择入账日',
          //     trigger: 'change',
          //   },
          // ],
          // pricenumber: [
          //   {
          //     required: true,
          //     message: '请输入索赔金额',
          //     trigger: 'blur',
          //   },
          // ],
          // claimdes: [
          //   {
          //     required: true,
          //     message: '请输入索赔金额',
          //     trigger: 'blur',
          //   },
          // ],
        },

        field106Options: [
          {
            label: '一般事件',
            value: '1',
          },
          {
            label: '重大事件',
            value: '2',
          },
        ],
        field107Options: [
          {
            label: '首报',
            value: '0',
          },
          {
            label: '续报',
            value: '1',
          },
          {
            label: '终报',
            value: '2',
          },
        ],
        bmType: 'zr',
        saveLoading: false,
        showRow: {},
        //提交

        status: 0,
        jurisdictionCode: 0,
        update: false,
        showMJ: false,
        MJoption: [],
        alldisabled: false,
      }
    },
    computed: {
      getFormLevel() {
        if (!this.formData.secrectLevelId) return ''
        const level = this.MJoption.find(
          (item) => item.levelId == this.formData.secrectLevelId
        )
        return level ? level.levelName : ''
      },
    },
    watch: {
      'formData.content'(val) {
        if (this.$refs['ueditor'].editor.openTemplate) {
          this.$refs['ueditor'].editor.openTemplate = false
          let s = val
          const arr = [
            ['$[contract.contractno]', 'contractno'],
            ['$[contract.contractname]', 'contractname'],
            ['$[contract.contractamount]', 'contractmoney'],
            ['$[contract.contractItem]', 'contractitem'],
            ['$[contract.executor]', 'realname'],
            ['$[contract.rmbinwords]', 'hzsumowing'],

            ['$[counterpart.coupersion]', 'counterpartcode'],
            ['$[counterpart.personincharge]', 'contractbd'],
            ['$[counterpart.counterpartHank]', 'bankkhyh'],
            ['$[counterpart.counumber]', 'counterpartno'],
            ['$[counterpart.couname]', 'budgetname'],
            ['$[counterpart.couaddress]', 'counterpartaddress'],
            ['$[counterpart.coupersion]', 'contacts'],
            ['$[counterpart.contactsPhone]', 'contactsphone'],
            ['$[counterpart.counterpartHankAccount]', 'bankaccount'],
            ['$[counterpart.legarepresentative]', 'contacts'],
            ['$[counterpart.pctelephonenumber]', 'contractzd'],
            // ['$[counterpart.taxpayeridentification]', 'hzsumowing'], //纳税人识别号
          ]
          arr.forEach((i) => {
            if (this.formData[i[1]]) {
              s = s.replace(i[0], this.formData[i[1]])
            }
          })
          this.formData.content = s
        }
      },
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        const res = await hasMJ('EventsEventBase')
        this.menuId = res[0].menuid
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    methods: {
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeIds = ''
            this.formData.staffScopeNames = ''
          }
        }
      },
      async handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },

      handleSelectCompany(e) {
        if (this.bmType === 'zr') {
          this.$set(this.formData, 'occureddepartment', e.name)
          // this.$set(this.formData, 'occureddepartment', e.id)
        }
        this.$refs['elForm'].clearValidate()
      },

      /**
       * @description: 无意义
       * @return {*}
       */
      handleClick(tab, event) {
        if (!this.formData.riseveid) {
          this.activeName = 'frist'
          return this.$baseMessage(
            '请先保存基本信息',
            'error',
            'vab-hey-message-error'
          )
        } else {
          this.activeName = tab.name
        }
      },
      /**
       * @description: 保存
       * @return {*}
       */
      async save() {
        // if()
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            let obj = {
              ...this.formData,
              riseveid: this.update ? '' : this.formData.riseveid,
              status: this.update ? '' : this.formData.status,
              createdate: this.update ? '' : this.formData.createdate,
              riskcatid: this.update ? this.formData.riskcatid : this.riskcatid,
              reporteds: this.fileIdList.toString(),
              removeReporteds: this.removeIds.toString(),
              reportStatus: '',
            }

            // 如果有发现日期
            if (obj.discovereddate) {
              obj.start1date = obj.discovereddate
              obj.discovereddate = dayjs(obj.discovereddate).format(
                'YYYY-MM-DD'
              )
              obj.start1date = dayjs(obj.start1date).format('YYYY-MM-DD')
            }
            // 如果有发生日期
            if (obj.occureddate) {
              obj.startdate = obj.occureddate
              obj.occureddate = dayjs(obj.occureddate).format('YYYY-MM-DD')
              obj.startdate = dayjs(obj.startdate).format('YYYY-MM-DD')
            }

            const res = await addFxsjData(obj)
            if (res.code == 1) {
              this.$set(this.formData, 'riseveid', res.data.riskid)
              this.showRow = this.formData
              // this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
              // this.$emit('fetch-data')
              // this.close()
            }
            let obj2 = {
              ...this.formData,
              riskcatid: this.riskcatid,
              reporteds: this.fileIdList.toString(),
              removeReporteds: this.removeIds.toString(),
            }

            obj2.claimnumber = this.formData.riseveid
            if (obj2.indate) {
              obj2.date2 = obj2.indate
              obj2.indate = dayjs(obj2.indate).format('YYYY-MM-DD')
              obj2.date2 = dayjs(obj2.date2).format('YYYY-MM-DD')
            }
            if (obj2.claimdate) {
              obj2.date1 = obj2.claimdate
              obj2.claimdate = dayjs(obj2.claimdate).format('YYYY-MM-DD')
              obj2.date1 = dayjs(obj2.date1).format('YYYY-MM-DD')
            }

            const res2 = await addFxsjspData(obj2)
            if (res2.code == 1) {
              this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
              this.close()
            }

            this.saveLoading = false
          }
        })
      },
      /**
       * @description: 初始化，获取数据
       * @return {*}
       */
      async showEdit(row, id, update, type) {
        this.fileIdList = []
        this.removeIds = []
        this.tableDataFile = []
        this.update = false
        if (!row) {
          this.title = '添加'
          const res = await createProjectCodeNew()
          this.$set(this.formData, 'riskeventcode', res.data)
          let userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.riskfactor1 = userInfo.staffid
          this.formData.riskfactor2 = userInfo.realname
          this.formData.recorddepart = userInfo.linkDetp.orgname
          this.formData.recordorg = userInfo.linkDetp.orgid
          this.formData.version = '1'
        } else {
          this.showRow = row
          this.title = '编辑'
          const res = await getFxsjkDetails({ eventid: row.riseveid })
          this.formData = JSON.parse(JSON.stringify(res.data.riskevent))
          const detailData = JSON.parse(JSON.stringify(res.data.riskevent))
          this.formData.content = res.data.riskevent.content || undefined
          this.formData.fillingStatus =
            row.fillingStatus == null ? '' : String(row.fillingStatus)
          this.tableDataFile = res.data.attachments || []
          this.fileIdList = []
          if (type == 'detail') {
            this.title = '详情'
            this.alldisabled = true
          }
          if (res.data.attachments.length != 0) {
            res.data.attachments.forEach((item) => {
              this.fileIdList.push(item.attid)
            })
          }
          if (update) {
            this.update = true
            await this.upDateData(detailData, row)
          }
          this.removeIds = []
          const res1 = await getFxsjkspDetails({
            riseveid: row.riseveid,
          })
          if (res1.data && res1.data.length) {
            this.formData = { ...this.formData, ...res1.data[0] }
          }
        }

        this.dialogFormVisible = true
        this.uploadLoading = false
      },
      async upDateData(detailData, row) {
        const { data, code, msg } = await getMaxVersion({
          riseveid: row.riseveid,
        })
        this.formData.version = data

        if (
          !detailData.initialfatherriseveid &&
          !detailData.initialfatherriskeventcode &&
          !detailData.fatherriseveid &&
          !detailData.fatherriskeventcode
        ) {
          this.formData.fillingStatus = '0'
          this.formData.initialfatherriseveid = detailData.riseveid
          this.formData.initialfatherriskeventcode = detailData.riskeventcode
          this.formData.fatherriseveid = detailData.riseveid
          this.formData.fatherriskeventcode = detailData.riskeventcode
        } else if (
          detailData.initialfatherriseveid == detailData.fatherriseveid
        ) {
          this.formData.fillingStatus = '0'
          this.formData.initialfatherriseveid = detailData.initialfatherriseveid
          this.formData.initialfatherriskeventcode =
            detailData.initialfatherriskeventcode
          this.formData.fatherriseveid = detailData.riseveid
          this.formData.fatherriskeventcode = detailData.riskeventcode
        }
      },
      /**
       * @description: 关闭页面
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
        this.activeName = 'first'
        this.formData = {}
        this.alldisabled = false
        this.$nextTick(() => {
          this.$refs['elForm'].clearValidate()
        })
      },

      /**
       * @description: 附件删除
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const deleteId = row.attid
          this.removeIds.push(deleteId)
          //删除formData要返回给后端的id
          this.fileIdList = this.fileIdList.filter((item) => {
            return item != deleteId
          })
          //删除tableDataFile，假删除
          this.tableDataFile = this.tableDataFile.filter((item) => {
            return item != row
          })
          this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
      //选择发生部门
      handleCompanyTreeSelected(val) {
        this.$set(this.formData, 'occureddepartment', val.label)
        this.$set(this.formData, 'occureddepartmentid', val.id)
      },

      handleBeforeUpload(file, fileList) {
        const isLt2M = file.size / 1024 / 1024 < 100 // 检查文件大小是否小于100MB
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false // 返回false停止上传
        }
        // 如果文件大小合适，则调用自定义上传逻辑
        this.customUploadWrapper({ file })
        return false // 停止默认上传行为
      },
      customUploadWrapper(options) {
        if (
          !this.baseApi ||
          !this.api ||
          !this.headers ||
          !window.key ||
          !window.iv
        ) {
          return
        }
        // 确保 fileList 是一个数组
        const fileList = Array.isArray(options.file)
          ? options.file
          : [options.file]

        // 获取 el-upload 的 data 参数
        const formData = {
          formlevel: this.getFormLevel,
        }

        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            formData: formData, // 传递额外的表单数据
            onProgress: this.handleProgress,
            onSuccess: (response) => {
              this.handleSuccess(response)
              resolve(response) // 成功时调用 resolve
            },
            onError: (error) => {
              // this.handleError(error)
              reject(error) // 失败时调用 reject
            },
          })
        })
      },
      handleSuccess(file) {
        if (file.code == 200) {
          // 确保 file.data 是数组格式
          const uploadedFiles = Array.isArray(file.data)
            ? file.data
            : [file.data]

          // 更新文件列表
          this.fileList = [...this.fileList, ...uploadedFiles]
          this.tableDataFile = [...this.tableDataFile, ...uploadedFiles]

          // 保存上传文件的attid到fileIdList
          uploadedFiles.forEach((fileItem) => {
            if (fileItem.attid) {
              this.fileIdList.push(fileItem.attid)
            }
          })

          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      //下载公共方法调用
      async handleDowns(row) {
        try {
          // 调用 handleDown 并传递自定义的下载接口
          await handleDown(row, this.headers, this.lodeapi)
        } catch (error) {
          console.error('自定义下载失败:', error)
        }
      },
      handlePreviewFile(row) {
        if (row.isEncrypted === '1') {
          // 当文件是加密状态时，使用指定的在线预览链接
          const previewUrl = row.previewUrl
          window.open(previewUrl, '_blank')
        } else {
          this.$iFrameDialog({ attid: row.attid })
        }
      },
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 0,
        })
        let url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        if (
          data.ftpUrl.includes('.pdf') ||
          data.ftpUrl.includes('.doc') ||
          data.ftpUrl.includes('.docx')
        ) {
          url = url + '&officePreviewType=pdf'
        }
        window.open(url)
      },
    },
  }
</script>
<style></style>
