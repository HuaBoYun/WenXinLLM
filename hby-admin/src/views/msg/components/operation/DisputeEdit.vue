<template>
  <div>
    <el-form
      v-if="show == 0"
      ref="form"
      label-width="140px"
      :model="form"
      :rules="rules"
    >
      <el-row :gutter="15">
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="登记编号" prop="disputeno">
            <el-input
              v-model.trim="form.disputeno"
              clearable
              placeholder="请输入登记编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="纠纷名称" prop="disputeitem">
            <el-input
              v-model.trim="form.disputeitem"
              clearable
              placeholder="请输入纠纷名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="纠纷类型" prop="disputetype">
            <el-select v-model="form.disputetype" :style="{ width: '100%' }">
              <el-option value="一般纠纷">一般纠纷</el-option>
              <el-option value="重大纠纷">重大纠纷</el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="是否关联合同" prop="glht">
            <el-radio-group
              v-model.trim="form.glht"
              :style="{ height: '45px', lineHeight: '45px' }"
            >
              <el-radio :label="1">是</el-radio>
              <el-radio :label="2">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="涉诉金额（万元）" prop="litigationamount">
            <el-input
              type="number"
              v-model.trim="form.litigationamount"
              clearable
              :disabled="isDetail"
              placeholder="请输入涉诉金额（万元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24" v-if="form.glht === 1">
          <el-form-item label="合同名称" prop="contractname">
            <el-input
              v-model.trim="form.contractname"
              clearable
              placeholder="请选择合同名称"
              readonly
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              @click="$refs.xzht.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24" v-if="form.glht === 1">
          <el-form-item label="合同编号">
            <el-input
              v-model.trim="form.contractno"
              clearable
              placeholder="请输入合同编号"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24" v-if="form.glht === 1">
          <el-form-item label="合同执行人">
            <el-input
              v-model.trim="form.realname"
              clearable
              placeholder="请输入合同执行人"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="原告" prop="plaintiff">
            <el-input
              v-model.trim="form.plaintiff"
              clearable
              placeholder="请输入原告"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="被告" prop="defendant">
            <el-input
              v-model.trim="form.defendant"
              clearable
              placeholder="请输入被告"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="涉诉标的" prop="disputecours">
            <el-input
              v-model.trim="form.disputecours"
              clearable
              placeholder="请输入涉诉标的"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="公司经办人" prop="zxstaffname">
            <el-input
              v-model.trim="form.zxstaffname"
              clearable
              placeholder="请选择公司经办人"
              readonly
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              @click="$refs.executor.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="是否紧急事项" prop="isuegent">
            <el-radio-group
              v-model.trim="form.isuegent"
              :style="{ height: '45px', lineHeight: '45px' }"
            >
              <el-radio :label="1">是</el-radio>
              <el-radio :label="2">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="诉讼地位" prop="whethersued">
            <el-select v-model="form.whethersued" :style="{ width: '100%' }">
              <el-option label="原告" :value="1">原告</el-option>
              <el-option label="被告" :value="2">被告</el-option>
              <!-- <el-option label="上诉人" :value="3">上诉人</el-option>
              <el-option label="被上诉人" :value="4">被上诉人</el-option> -->
              <el-option label="第三人" :value="5">第三人</el-option>
              <!-- <el-option label="再审申请认" :value="6">再审申请人</el-option>
              <el-option label="被申请人" :value="7">被申请人</el-option> -->
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="预计办结时间" prop="enddate1">
            <el-date-picker
              v-model.trim="form.enddate1"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入预计办结时间"
              style="width: 100%"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="业务发生时间" prop="businessdate">
            <el-date-picker
              v-model.trim="form.businessdate"
              clearable
              format="yyyy-MM-dd"
              placeholder="请选择业务发生时间"
              style="width: 100%"
              value-format="yyyy-MM-dd"
              :disabled="isDetail"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="是否外聘律师" prop="isattorney">
            <el-radio-group v-model.trim="form.isattorney">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="2">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="15">
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="代理律师" prop="attorney">
            <el-input
              v-model.trim="form.attorney"
              clearable
              placeholder="请输入代理律师"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="代理律师联系电话" prop="attorneyphont">
            <el-input
              v-model.trim="form.attorneyphont"
              clearable
              placeholder="请输入代理律师联系电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="初步解决建议" prop="solutionsuggestions">
            <el-input
              v-model="form.solutionsuggestions"
              placeholder="请输入初步解决建议"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="form.isuegent === 1">
          <el-form-item label="紧急事项情况说明" prop="jjsxsm">
            <el-input
              v-model="form.jjsxsm"
              placeholder="请输入紧急事项情况说明"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="法务审核意见" prop="fwsh">
            <el-input
              v-model="form.fwsh"
              disabled
              placeholder="请输入法务审核意见"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="总法律顾问审核意见" prop="flgw">
            <el-input
              v-model="form.flgw"
              disabled
              placeholder="请输入总法律顾问审核意见"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="总经理审核意见" prop="zjl">
            <el-input
              v-model="form.zjl"
              disabled
              placeholder="请输入总经理审核意见"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="董事长审核意见" prop="dsz">
            <el-input
              disabled
              v-model="form.dsz"
              placeholder="请输入董事长审核意见"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!$store.state.work.processMobile"
          >
            <el-button v-if="!form.disputeid" type="success" @click="hold()">
              上传
            </el-button>
            <el-upload
              v-else
              ref="upload"
              :accept="accept"
              :action="baseApi + api"
              :before-upload="handleBeforeUpload"
              :data="uploadData"
              :file-list="fileList"
              :headers="headers"
              :on-error="onError"
              :on-remove="handleRemove"
              :on-success="onSuccess"
              :on-exceed="onExceed"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="uploadlist">
            <el-table-column align="center" label="附件名称" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="downloadData(row)">
                  下载
                </el-button>
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="handleDelete(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-form>
    <el-row v-if="show == 1" :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="登记编号" prop="disputeno">
            <span>{{ form.disputeno }}</span>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="纠纷名称" prop="disputeitem">
            <span>{{ form.disputeitem }}</span>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="纠纷类型" prop="disputetype">
            <span>{{ form.disputetype }}</span>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="涉诉金额（万元）" prop="litigationamount">
            <span>{{ form.litigationamount }}</span>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item
            label="合同名称"
            prop="contractname"
            v-if="form.glht == 1"
          >
            <span>{{ form.contractname }}</span>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="合同编号" v-if="form.glht == 1">
            <span>{{ form.contractno }}</span>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="合同执行人" v-if="form.glht == 1">
            <span>{{ form.realname }}</span>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="原告" prop="plaintiff">
            <span>{{ form.plaintiff }}</span>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="被告" prop="defendant">
            <span>{{ form.defendant }}</span>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="涉诉标的" prop="disputecours">
            <span>{{ form.disputecours }}</span>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="公司经办人" prop="zxstaffname">
            <span>{{ form.zxstaffname }}</span>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="是否紧急事项" prop="isuegent">
            <span>{{ form.isuegent == 1 ? '是' : '否' }}</span>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="诉讼地位" prop="whethersued">
            <span>
              {{
                form.whethersued == 1
                  ? '原告'
                  : form.whethersued == 2
                  ? '被告'
                  : form.whethersued == 5
                  ? '第三人'
                  : ''
              }}
            </span>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="预计办结时间" prop="enddate1">
            <span>{{ form.enddate1 }}</span>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="业务发生时间" prop="businessdate">
            <span>{{ form.businessdate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="初步解决建议" prop="solutionsuggestions">
            <span>{{ form.solutionsuggestions }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="form.isuegent === 1">
          <el-form-item label="紧急事项情况说明" prop="jjsxsm">
            <span>{{ form.jjsxsm }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="法务部审核意见" prop="fwsh">
            <span>{{ form.fwsh }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="总法律顾问审核意见" prop="flgw">
            <span>{{ form.flgw }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="董事长审核意见" prop="dsz">
            <span>{{ form.dsz }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="总经理审核意见" prop="zjl">
            <span>{{ form.zjl }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>我方代理人</el-divider>
        </el-col>
        <el-col :span="24">
          <el-table
            :data="dllsData"
            style="width: 100%; margin: 10px 0 50px 0%"
          >
            <el-table-column align="center" prop="isattorney" label="是否外聘">
              <template #default="{ row }">
                {{ row.isattorney === 1 ? '是' : '否' }}
              </template>
            </el-table-column>
            <el-table-column align="center" prop="attorney" label="代理人" />
            <el-table-column
              align="center"
              prop="attorneyphont"
              label="联系方式"
            />
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!$store.state.work.processMobile"
          >
            <el-upload
              v-if="isOnwer"
              ref="upload"
              :accept="accept"
              :action="baseApi + api"
              :show-file-list="false"
              :before-upload="handleBeforeUpload"
              :data="uploadData"
              :file-list="fileList"
              :headers="headers"
              :on-error="onError"
              :on-remove="handleRemove"
              :on-success="onSuccess"
              :on-exceed="onExceed"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="uploadlist">
            <el-table-column align="center" label="附件名称" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="downloadData(row)">
                  下载
                </el-button>
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="isOnwer"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>

    <xzht-options ref="xzht" @selecteded="handleSsjd" />
    <executor-options ref="executor" @selected="handleSelected" />
    <div
      style="text-align: right; margin-top: 10px"
      v-if="show == 0 || isOnwer"
    >
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="add" v-if="show == 0 || isOnwer">
        确 定
      </el-button>
      <el-button type="primary" @click="ymsubmit" v-if="show == 0">
        提 交
      </el-button>
    </div>

    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import { ymWorkCandidates, ymWorkSubmit } from '@/api/contract/manage'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import {
    caseInformationModify,
    caseInformationSave,
    deleAttacheMent,
    disputeRegisterDetail,
    findAttacheMent,
  } from '@/api/contract/legal'
  import { legalAttorney } from '@/api/fwgl/legal'
  import { baseURL } from '@/config/net.config'
  import store from '@/store'
  import ExecutorOptions from '@/views/contract/legal/components/options/executor'
  import xzhtOptions from '@/views/contract/legal/components/options/xzht.vue'
  import { downloads } from '@/api/fwgl/zzxx'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { changeFormSizeStyleFunc } from '@/utils/processMobile'

  export default {
    name: 'DraftEdit',
    components: { xzhtOptions, ExecutorOptions, CandidateUserSelect, Resubmit },
    props: {},
    data() {
      return {
        isOnwer: false,
        accept: '.pdf, .doc, .docx, .xls, .xlsx',
        api: '/contract/uploadFileAttInfo',
        // data: {
        //   attpath: '1649657363411.xlsx',
        //   attname: '1649657363411.xlsx',
        //   token: store.getters['user/token'],
        // },
        headers: {
          token: store.getters['user/token'],
          // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        is_initiator: false,
        uploadlist: [],
        value: {},
        dllsData: [],
        beforeUpload: null,
        baseApi: baseURL,
        fileList: [],
        currentValue: this.value || null,
        previewUrl: undefined,
        initialed: false,
        form: {
          zxstaffid: undefined,
          contractId: undefined,
          contractid: undefined,
          disputeno: undefined,
          disputeitem: undefined,
          disputetype: undefined,
          contractname: undefined,
          contractno: undefined,
          realname: undefined,
          plaintiff: undefined,
          defendant: undefined,
          disputecours: undefined,
          zxstaffname: undefined,
          litigationamount: undefined,
          isuegent: 1,
          whethersued: 1,
          isattorney: 1,
          attorneyphont: undefined,
          solutionsuggestions: undefined,
          enddate1: undefined,
          businessdate: undefined,
          attorney: undefined,
          disputeid: undefined,
        },
        rules: {
          disputeno: [
            {
              required: true,
              message: '请输入登记编号',
              trigger: 'blur',
            },
          ],
          disputeitem: [
            {
              required: true,
              message: '请输入纠纷名称',
              trigger: 'blur',
            },
          ],
          disputetype: [
            {
              required: true,
              message: '请输入纠纷类型',
              trigger: 'blur',
            },
          ],
          contractname: [
            {
              required: true,
              message: '请选择合同',
              trigger: 'blur',
            },
          ],
          plaintiff: [
            {
              required: true,
              message: '请输入原告',
              trigger: 'blur',
            },
          ],
          defendant: [
            {
              required: true,
              message: '请输入被告',
              trigger: 'blur',
            },
          ],
          disputecours: [
            {
              required: true,
              message: '请输入涉诉标的',
              trigger: 'blur',
            },
          ],
          zxstaffname: [
            {
              required: true,
              message: '请选择公司经办人',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        radio: '',
        show: 0,
        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
      }
    },
    computed: {
      uploadData() {
        return {
          type: 1,
          bid: this.form.disputeid,
        }
      },
    },
    created() {},
    methods: {
      hold() {
        this.$message.error('请先保存基本信息!')
      },
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 200
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handleRemove(file, fileList) {},
      onExceed(file, fileList) {},
      onSuccess(response, file, fileList) {
        this.uploadList()
      },
      onError(err) {
        this.$message.error(JSON.parse(err.message).message)
      },
      showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        userId,
        isWfqdedit,
        status
      ) {
        this.show = 0
        if (title == 'add') {
          this.title = '添加'
        } else if (title == 'edit') {
          this.title = '编辑'
        } else {
          this.title = '查看'
          this.show = 1
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          if (userId == userInfo.staffid && isWfqdedit) {
            this.isOnwer = true
          }
        }

        this.form.disputeid = formId
        this.uploadList()
        this.getDetail(formId)
        this.fetchLawerList()

        this.fromId = formId
        this.fromIdcopy = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status

        if (this.$store.state.work.processMobile) {
          this.$nextTick(() => {
            changeFormSizeStyleFunc()
          })
        }
      },
      showDetail(row) {
        this.showEdit(row, true)
      },
      async getDetail(formId) {
        const { dispute } = await disputeRegisterDetail({
          disputeId: formId,
        })
        Object.keys(this.form).forEach((key) => {
          this.form[key] = dispute[key]
        })
        this.form.enddate1 = dispute.lastdealdate
        // this.form.businessdate = dispute.businessdate
        this.form.contractId = dispute.contractid
        this.form.contractid = dispute.contractid
        this.form.realname = dispute.realname
        this.form.isuegent = dispute.isuegent
        this.form.zxstaffid = dispute.disputeundertaker
        this.form.litigationamount = dispute.litigationamount
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form

        this.$bus.$emit('updateMsg', 0)
        this.uploadlist = []
        this.dllsData = []
      },
      add() {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            const bCreate = !this.form.disputeid
            const func = bCreate ? caseInformationSave : caseInformationModify
            const msg = bCreate ? '新增成功' : '修改成功'
            func(this.form).then((res) => {
              if (res.code == 1) {
                if (bCreate) {
                  this.form.disputeid = res.data
                }
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              }
            })
            this.$emit('fetch-data')
            // this.close()
          }
        })
      },
      //附件列表
      async uploadList() {
        // this.listLoading = true
        const { data } = await findAttacheMent({
          type: 1,
          bid: this.form.disputeid,
        })
        this.uploadlist = data
        // this.$refs.upload.clearFiles()
        // this.$refs.upload.uploadFiles.length = 0
      },
      //附件删除
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleAttacheMent({ aid: row.attid, type: 1 })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.uploadList()
        })
      },
      // 下载数据
      // downloadData(row) {
      //   const fileName = row.attname
      //   const link = document.createElement('a')
      //   link.download = fileName
      //   link.href = this.baseApi + '/download?id=' + row.attid
      //   link.style.display = 'none'
      //   document.body.appendChild(link)
      //   link.click()
      //   document.body.removeChild(link)
      // },
      async downloadData(row) {
        const data = await downloads({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      handleSelected(val) {
        this.form.zxstaffid = val.staffid
        this.form.zxstaffname = val.realname
      },
      handleSsjd(item) {
        console.warn('handleSsjd', item)
        this.form.contractname = item.contractname
        this.form.contractno = item.contractno
        this.form.realname = item.realname
        this.form.contractId = item.contractid
        this.form.contractid = item.contractid
      },
      async fetchLawerList() {
        this.dllsData = []
        //
        if (!this.form.disputeid) return
        const res = await legalAttorney({ disputeid: this.form.disputeid })
        //
        this.dllsData = res.data
      },

      async handlePreviewFile(row) {
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

      //提交
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
