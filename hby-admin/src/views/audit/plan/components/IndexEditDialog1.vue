<template>
  <div>
    <!-- <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      :append-to-body="true"
      @close="close"
    > -->
    <el-row :gutter="15">
      <el-form
        ref="form"
        :class="{ disabled: disabled }"
        label-width="125px"
        :model="formData"
        :rules="rules"
        size="medium"
        v-loading="showLoading"
      >
        <el-col :span="12">
          <el-form-item label="计划编号">
            <el-input
              v-model="formData.plancode"
              clearable
              placeholder="请输入计划编号"
              :style="{ width: '100%' }"
              :disabled="true"
              readonly
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划名称" prop="planname">
            <el-input
              v-model.trim="formData.planname"
              clearable
              placeholder="请输入计划名称"
              :style="{ width: '100%' }"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划年度" prop="palnyear">
            <el-date-picker
              v-model="formData.palnyear"
              type="year"
              :style="{ width: '100%' }"
              placeholder="请选择计划年度"
              style="width: 266px"
              value-format="yyyy"
              :disabled="disabled"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划类别" prop="plantype">
            <el-input
              v-model="formData.plantype"
              clearable
              placeholder="请输入计划类别"
              :style="{ width: '100%' }"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="计划费用估算(元)">
            <el-input
              v-model="formData.palncost"
              clearable
              placeholder="请输入计划费用估算"
              :style="{ width: '100%' }"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="计划时间" prop="spanDate" style="height: '30px'">
            <el-date-picker
              :key="itemKey"
              @input="changeTime"
              v-model="formData.spanDate"
              clearable
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              :style="{ width: '100%' }"
              type="daterange"
              value-format="yyyy-MM-dd"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="计划负责人" prop="realname">
            <el-input
              v-model="formData.realname"
              clearable
              placeholder="请选择计划负责人"
              style="width: 266px"
              disabled
            />
            <el-button
              @click="showGroupLeader"
              style="margin-left: 10px"
              type="primary"
               :disabled="disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="是否外审" prop="isauditor">
            <el-radio-group v-model="formData.isauditor" size="medium"  :disabled="disabled">
              <el-radio
                v-for="(item, index) in fieldOptions"
                :key="index"
                :disabled="item.disabled"
                :label="item.value"
              >
                {{ item.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="编制人">
            <el-input
              v-model="formData.organizationName"
              clearable
              placeholder="请输入编制人"
              :style="{ width: '100%' }"
              disabled
              readonly
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="编制日期">
            <el-input
              v-model="formData.createtime"
              clearable
              placeholder="请输入编制日期"
              :style="{ width: '100%' }"
              disabled
              readonly
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计单位">
            <el-input
              v-model="formData.orgname"
              clearable
              placeholder="请输入审计单位"
              :style="{ width: '100%' }"
              disabled
              readonly
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="备注">
            <el-input
              v-model="formData.remarks"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col> -->

        <el-col :span="24">
          <el-divider>{{ this.formData.plantype }}项目</el-divider>
        </el-col>
        <el-col :span="24">
          <div v-if="!disabled" style="text-align: right; margin-bottom: 5px">
            <el-button type="success" @click="handleAdd">增加一行</el-button>
            <!-- <el-button type="success" @click="handleDelCheck">删除</el-button> -->
          </div>
          <!-- 新增可编辑表格 -->
          <el-table
            border
            :data="tableDataProject"
            style="width: 100%; margin-bottom: 25px"
            :rules="rulesProject"
            :disabled="disabled"
          >
            <el-table-column
              align="center"
              label="项目编号"
              prop="projectCode"
              :render-header="addRedStar"
            ></el-table-column>
            <el-table-column
              align="center"
              label="项目名称"
              prop="prjoectName"
              :render-header="addRedStar"
            >
              <template slot-scope="scope">
                <el-input
                  v-show="scope.row.show"
                  v-model="scope.row.prjoectName"
                  size="mini"
                  style="width: 90%"
                  :disabled="disabled"
                />
                <span v-show="!scope.row.show">
                  {{ scope.row.prjoectName }}
                </span>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="被审计对象"
              prop="orgIdNames"
              min-width="180px"
            ></el-table-column>
            <!-- <el-table-column
              align="center"
              label="计划开始时间"
              prop="platformName"
              min-width="180px"
            >
              <template slot-scope="scope">
                {{ UTCformat(scope.row.startDate) }}
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="计划结束时间"
              prop="platformName"
              min-width="180px"
            >
              <template slot-scope="scope">
                {{ UTCformat(scope.row.endDate) }}
              </template>
            </el-table-column> -->
            <el-table-column
              align="center"
              label="时间安排"
              prop="sjap"
              min-width="180px"
            ></el-table-column>
            <el-table-column
              align="center"
              label="项目概述"
              prop="xmgs"
              min-width="180px"
            ></el-table-column>

            <!-- <el-table-column align="center" label="是否外委" min-width="130">
              <template slot-scope="scope">
                {{ scope.row.externAlassig === 1 ? '是' : '否' }}
              </template>
            </el-table-column> -->
            <!-- <el-table-column align="center" label="操作" min-width="130">
              <template>
                <el-button type="primary" disabled>终止</el-button>
              </template>
            </el-table-column> -->

            <el-table-column
              v-if="!disabled"
              align="center"
              label="操作"
              min-width="110"
              prop=""
            >
              <template slot-scope="scope">
                <el-button
                  type="text"
                  @click="handleDelete(scope.$index, scope.row)"
                >
                  删除
                </el-button>

                <!-- <el-button type="text" @click="scope.row.show = true">
                  编辑
                </el-button>

                <el-button type="text" @click="saveProject(scope.row)">
                  保存
                </el-button> -->
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <!-- <el-upload
            style="text-align: right; margin-bottom: 5px"
            class="upload-demo"
            :action="baseApi + api"
            :on-success="handleSuccess"
            :show-file-list="false"
            :headers="headers"
            multiple
            :file-list="fileList"
            :before-upload="handleBeforeUpload"
          >
            <div v-if="!disabled || isOnwer" style="margin-right: 10px">
              <el-button type="success">点击上传</el-button>
            </div>
          </el-upload> -->
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
            <div v-if="!disabled || isOnwer" style="margin-right: 10px">
              <el-button type="success">点击上传</el-button>
            </div>
          </el-upload>
          <el-table :data="tableData">
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
              <template slot-scope="scope">
                <el-button
                  :disabled="false"
                  type="text"
                  @click="handleDowns(scope.row)"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  :disabled="false"
                  @click="handlePreviewFile(scope.row)"
                >
                  预览
                </el-button>
                <el-button
                  v-if="!disabled || isOnwer"
                  type="text"
                  @click="handleDeleteFile(scope.$index, scope.row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>

    <!-- <div style="text-align: right; margin-top: 10px" v-if="!disabled">
      <el-button @click="close" v-if="!isedit">取 消</el-button>
      <el-button @click="close" v-if="isedit">关 闭</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div> -->
    <!-- </el-dialog> -->
    <!-- <plan-leader ref="plan" @planList="planList"></plan-leader> -->
    <Company ref="auditee" @submit="auditee"></Company>
    <!-- 选择组长组员子组件 -->
    <!-- <selectTeam ref="select" @selectTeamList="selectTeamList" /> -->
    <select-team ref="select" @selectTeamList="selectTeamList"></select-team>

    <div
      style="text-align: right; margin-top: 10px; margin-right: 10px"
      v-if="isSubmit || isOnwer"
    >
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="add" v-if="isSubmit || isOnwer">
        确 定
      </el-button>

      <el-button type="primary" @click="ymsubmit" v-if="isSubmit">
        提 交
      </el-button>
    </div>

    <!-- 提交 -->
    <el-dialog
      @close="currentClose"
      title="选择分支"
      :visible="visible"
      :append-to-body="true"
      :close-on-click-modal="false"
    >
      <el-form
        label-width="100px"
        ref="fzforms"
        :modal="fzforms"
        :rules="fzRules"
      >
        <el-form-item label="分支选择" prop="branchStrs">
          <el-select
            style="width: 100%"
            v-model="fzforms.branchStrs"
            @change="handlefzChange"
            multiple
          >
            <el-option
              v-for="item in fzoptions"
              :label="item.nodeName"
              :value="item.nodeId"
              :key="item.nodeId"
            ></el-option>
          </el-select>
        </el-form-item>
        <div v-for="(item, index) in this.runderList" :key="item.value">
          <el-form-item
            :label="item.label"
            prop="transferStaffName"
            v-if="item.hasCandidates"
          >
            <!-- <el-input
              disabled
              placeholder="请选择候选人"
              v-model="formData3[index].transferStaffName"
              style="width: 79%; margin-right: 8px"
            ></el-input>
            <el-button type="primary" @click="handleSelectNew(item, index)">
              请选择
            </el-button> -->
            <CandidateUserSelect
              :clearType="clearType"
              @selected="handleCandSelect"
              :index="index"
              :nodeId="item.nodeId"
              :candidateData="candidateData"
              multiple
              placeholder="请选择候选人"
            />
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer">
        <el-button @click="currentClose">取消</el-button>
        <el-button type="primary" @click="save4">提交</el-button>
      </div>
    </el-dialog>
    <el-dialog
      @close="close1"
      title="选择分支"
      :visible="visible1"
      :append-to-body="true"
      :close-on-click-modal="false"
      v-if="visible1"
    >
      <el-form
        :modal="fzform1"
        label-width="100px"
        ref="fzform1"
        :rules="fzRules1"
      >
        <el-form-item label="分支选择" prop="branchStrs">
          <el-select
            style="width: 100%"
            v-model="fzform1.branchStrs"
            @change="selectValue"
            multiple
          >
            <el-option
              v-for="item in fzoptions"
              :key="item.nodeId"
              :label="item.nodeName"
              :value="item.nodeId"
            ></el-option>
          </el-select>
        </el-form-item>
        <div v-for="(item, index) in this.runderList" :key="item.value">
          <el-form-item
            :label="item.label"
            prop="transferStaffName"
            v-if="item.hasCandidates"
          >
            <!-- <el-input
              disabled
              placeholder="请选择候选人"
              v-model="formData3[index].transferStaffName"
              style="width: 79%; margin-right: 8px"
            ></el-input>
            <el-button type="primary" @click="handleSelectNew(item, index)">
              请选择
            </el-button> -->
            <CandidateUserSelect
              :clearType="clearType"
              @selected="handleCandSelect"
              :index="index"
              :nodeId="item.nodeId"
              :candidateData="candidateData"
              multiple
              placeholder="请选择候选人"
            />
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer">
        <el-button @click="close1">取消</el-button>
        <el-button type="primary" @click="save1">提交</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="选择候选人"
      :visible.sync="visible2"
      :close-on-click-modal="false"
      width="40%"
      :modal="false"
      @close="close2"
      v-if="visible2"
    >
      <el-form
        :model="formData2"
        :rules="rules2"
        ref="ruleForm2"
        label-width="80px"
      >
        <el-form-item label="候选人" prop="transferStaffName">
          <!-- <el-input
            disabled
            placeholder="请选择候选人"
            v-model="formData2.transferStaffName"
            style="width: 79%; margin-right: 8px"
          ></el-input>
          <el-button type="primary" @click="handleSelect">请选择</el-button> -->
          <CandidateUserSelect
            :clearType="clearType"
            @selected="handleCandSelect1"
            :index="0"
            :nodeId="candidateData.nodeId"
            :candidateData="candidateData"
            multiple
            placeholder="请选择候选人"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close2">取 消</el-button>
        <el-button type="primary" @click="save2">确 定</el-button>
      </span>
    </el-dialog>
    <CandidateListNew ref="candidateListNew" @selected="handSelectedNew1" />
    <CandidateList ref="candidateList" @selected="handSelected" />
  </div>
</template>

<script>
  import {
    saveContract,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import CandidateList from '@/components/CandidateList'

  import {
    deleteFileById,
    getAuditPlanAttInfo,
    getAuditPlanViewDetail,
    getPlanProjectListByPlanId,
    mergeAuditPlanInfo,
    mergePlanProjectManageInfoList,
    removePlanProjectInfo,
  } from '@/api/audit/plan'
  import { UTCformat } from '@/utils'
  // import PlanLeader from './childCom/PlanLeader.vue'
  import { download } from '@/api/audit/implement'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'IndexEdit',
    components: {
      // PlanLeader,
      Company: () => import('@/components/Company.vue'),

      selectTeam: () =>
        import('../../project/components/formComponents/selectTeam.vue'),
      CandidateUserSelect,
      CandidateList,
    },
    props: {
      isedit: {
        type: Boolean,
        default: true,
      },
    },
    data() {
      return {
        baseApi: baseURL,
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        headers: {
          token: store.getters['user/token'],
        },
        disabled: false,
        value: '',
        planRow: {},
        formData: {
          plancode: '',
          planname: undefined,
          palnyear: undefined,
          plantype: '',
          palncost: undefined,
          spanDate: [],
          realname: '',
          isauditor: '',
          organizationName: '',
          createtime: '',
          orgname: '',
          remarks: '',
        },
        UTCformat,
        fileList: [],
        planid: '',
        createPerson: '',
        itemKey: '',
        rules: {
          plancode: [
            { required: true, message: '请输入计划编号', trigger: 'blur' },
          ],
          palnyear: [
            { required: true, message: '请输入计划年度', trigger: 'change' },
          ],
          plantype: [
            { required: true, message: '请输入计划类别', trigger: 'change' },
          ],
          // spanDate: [
          //   {
          //     required: true,
          //     message: '请选择计划时间',
          //     trigger: 'change',
          //   },
          // ],
          realname: [
            { required: true, message: '请选择计划负责人', trigger: 'change' },
          ],
          isauditor: [
            { required: true, message: '请选择是否外审', trigger: 'change' },
          ],
          orgname: [
            { required: true, message: '请输入审计单位', trigger: 'change' },
          ],
          remarks: [
            { required: true, message: '请输入活动名称', trigger: 'change' },
          ],
        },
        rulesProject: {
          projectname: [
            { required: true, message: '请输入项目名称', trigger: 'change' },
          ],
          targetname: [
            { required: true, message: '请输入工作目标', trigger: 'change' },
          ],
        },
        fieldOptions: [
          {
            label: '是',
            value: 1,
          },
          {
            label: '否',
            value: 0,
          },
        ],
        title: '',
        isOnwer: false,
        plancode: '',
        tableData: [],
        tableDataProject: [],
        sIndex: 0,
        isSendBack: false,
        showLoading: false,
        isSubmit: false,
        //提交
        visible: false,
        fzforms: {
          branchStrs: [],
        },
        fzform1: {
          branchStrs: [],
        },
        fzRules: {
          branchStrs: [
            {
              required: true,
              message: '请选择分支',
              trigger: 'blur',
            },
          ],
        },
        fzRules1: {
          branchStrs: [
            {
              required: true,
              message: '请选择分支',
              trigger: 'blur',
            },
          ],
        },
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        chooseStatus: true,
        executorType: '',
        contractid: '',
        fzoptions: [],
        formData2: {
          transferStaffName: '',
          transferStaffId: '',
        },
        visible1: false,
        visible2: false,
        formData3: [],
        candidateData: {},
        runderList: [],
        candidateType: '',
        clearType: false,
      }
    },
    created() {},
    methods: {
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
          this.fileList = [...this.fileList, ...file.data]
          this.tableData = [...this.tableData, ...file.data]
          // this.tableData = list
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
      /**
       * @description:初始化，前两参数通用，1.页面数据,2.控制页面编辑、新建、详情，后面几个 流程相关，给流程重新提交接口，提供参数
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit(
        row,
        disabled,
        planNum,
        fromId,
        flowtaskinfoflowid,
        ymFromId,
        isSubmit,
        userId,
        isWfqdedit
      ) {
        if (fromId) {
          this.fromId = fromId
          this.fromIdcopy = fromId // fromId为-1时，拷贝一份
        }
        if (flowtaskinfoflowid) {
          this.flowtaskinfoflowid = flowtaskinfoflowid
        }
        if (ymFromId) {
          this.ymFromId = ymFromId
        }
        if (isSubmit) {
          this.isSubmit = isSubmit
        }

        // 退回之后的情况，根据url获取planid
        if (row.planid) {
          this.planid = row.planid
          this.planRow = row
        } else if (row.cyurl) {
          this.planid = this.getQueryVariable(row.cyurl, 'planid')
        } else {
          this.planid = ''
        }
        this.plancode = row.plancode

        //查看按钮入口
        this.title = '查看'
        this.disabled = disabled
        let resD = await getAuditPlanViewDetail({
          planid: this.planid,
        })

        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        if (resD.data.auditPlan.createStaff.staffid == userInfo.staffid) {
          this.isOnwer = true
        }

        //获取审计计划附件
        let resF = await getAuditPlanAttInfo({
          planId: this.planid,
        })
        this.tableData = resF.data

        this.formData = {
          plancode: resD.data.auditPlan.plancode,
          planname: resD.data.auditPlan.planname,
          palnyear: resD.data.auditPlan.palnyear,
          plantype: resD.data.auditPlan.plantype,
          palncost: resD.data.auditPlan.palncost,
          // realname: resD.data.auditPlan.principalStaff.realname,
          // isauditor: resD.data.auditPlan.isauditor,
          organizationName: resD.data.auditPlan.createStaff.realname,
          createtime: resD.data.auditPlan.createtime,
          orgname: resD.data.auditPlan.auditOrgInfo.orgname,
          remarks: resD.data.auditPlan.remarks,
          // principalid: resD.data.auditPlan.principalStaff.staffid,
          createstaffid: resD.data.auditPlan.createStaff.staffid,
          planStartTime: resD.data.auditPlan.starttime,
          planEndTime: resD.data.auditPlan.endtime,
          spanDate: [
            UTCformat(resD.data.auditPlan.starttime),
            UTCformat(resD.data.auditPlan.endtime),
          ],
          createtime: UTCformat(resD.data.auditPlan.createtime),
        }
        this.uploader = resD.data.auditPlan.createStaff.realname

        //计划项目接口
        // let res = await getPlanProjectListByPlanId({ planId: this.planid })  //旧逻辑
        let res = await getPlanProjectListByPlanId({ planId: this.planid }) //旧逻辑
        this.tableDataProject = res.data.planList
      },
      /**
       * @description: 处理地址
       * @param {*}
       * @return {*}
       */
      getQueryVariable(url, variable) {
        var query = url && url.substring(1)
        var vars = query && query.split('?')
        if (vars && vars.length > 0) {
          for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split('=')
            if (pair[0] == variable) {
              return pair[1]
            }
          }
        }
        return false
      },

      changeTime() {
        //设置key，使组件重新渲染生成
        this.itemKey = Math.random()
      },
      // 无意义
      showObj(sIndex) {
        this.sIndex = sIndex
        this.$refs['auditee'].showEdit()
      },
      /**
       * @description: 选择组长， 组员 子组件 确认后，回调函数，把返回的数据存入tableDataProject
       * @param {*}
       * @return {*}
       */
      auditee(val, flag) {
        if (flag == 'right') {
          this.$set(this.tableDataProject[this.sIndex], 'bsjtype', 'ry')
          this.$set(
            this.tableDataProject[this.sIndex],
            'orgidnames',
            val[0].realname
          )
          this.$set(
            this.tableDataProject[this.sIndex],
            'orgids',
            val[0].staffid
          )
        } else {
          this.$set(this.tableDataProject[this.sIndex], 'orgidnames', val.label)
          this.$set(this.tableDataProject[this.sIndex], 'orgids', val.id)
          this.$set(this.tableDataProject[this.sIndex], 'bsjtype', 'bm')
        }
      },
      /**
       * @description:  组件取消按钮，清空form内容，回调父组件函数，关闭组件
       * @param {*}
       * @return {*}
       */
      closed() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.$emit('close')
      },
      /**
       * @description: 确定按钮，form表单校验，请求保存接口（最开始的接口，后续废弃）
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 保存表单
       * @return {*}
       */
      async save() {
        this.$refs['form'].validate(async (valid) => {
          const arrAttid = this.tableData.map((item) => item.attid)
          if (arrAttid && arrAttid.length) {
            this.formData.attids = arrAttid.join(',')
          } else {
            this.formData.attids = ''
          }

          if (valid) {
            //上层表单参数
            let params = {
              planid: this.formData.planid,
              plancode: this.formData.plancode,
              planname: this.formData.planname,
              palnyear: this.formData.palnyear,
              plantype: this.formData.plantype,
              palncost: this.formData.palncost,
              planStartTime: this.formData.spanDate[0],
              planEndTime: this.formData.spanDate[1],
              isauditor: this.formData.isauditor,
              createstaffid: this.formData.staffid,
              principalid: this.formData.principalid,
              attIds: this.formData.attids,
              // organizationName: this.formData.realname,
              // createtime: this.formData.createtime,
              // orgname: this.formData.orgname,
              remarks: this.formData.remarks,
            }

            // if (this.disabled) {
            //   params.opinionstatus = 0
            // }
            //审计计划上层表单提交
            let resForm = await mergeAuditPlanInfo(params)
            if (resForm.code === 1) {
              this.$baseMessage(
                resForm.msg,
                'success',
                'vab-hey-message-success'
              )
              this.closed()
              this.planid = resForm.data.auditPlan.planid
              this.formData.planid = resForm.data.auditPlan.planid
              this.tableDataProject.map((v) => {
                this.saveProject(v)
              })
              this.$emit('fetch-data')
              this.closed()
            }
          }
        })
      },
      /**
       * @description: 确定按钮，form表单校验，请求保存接口
       * @param {*}
       * @return {*}
       */
      async add() {
        this.$refs['form'].validate(async (valid) => {
          const arrAttid = this.tableData.map((item) => item.attid)
          if (arrAttid && arrAttid.length) {
            this.formData.attids = arrAttid.join(',')
          } else {
            this.formData.attids = ''
          }

          if (valid) {
            //上层表单参数
            let params = {
              planid: this.formData.planid,
              plancode: this.formData.plancode,
              planname: this.formData.planname,
              palnyear: this.formData.palnyear,
              plantype: this.formData.plantype,
              palncost: this.formData.palncost,
              planStartTime: this.formData.spanDate[0],
              planEndTime: this.formData.spanDate[1],
              isauditor: this.formData.isauditor,
              createstaffid: this.formData.staffid,
              principalid: this.formData.principalid,
              attIds: this.formData.attids,
              // organizationName: this.formData.realname,
              // createtime: this.formData.createtime,
              // orgname: this.formData.orgname,
              remarks: this.formData.remarks,
            }

            // if (this.disabled) {
            //   params.opinionstatus = 0
            // }
            //审计计划上层表单提交
            let resForm = await mergeAuditPlanInfo(params)
            if (resForm.code === 1) {
              this.$baseMessage(
                resForm.msg,
                'success',
                'vab-hey-message-success'
              )
            }
          }
        })
      },
      /**
       * @description: 给表头加必填符号*
       * @param {*}
       * @return {*}
       */
      addRedStar(h, { column }) {
        return [
          h('span', { style: 'color: red' }, '*'),
          h('span', ' ' + column.label),
        ]
      },
      // 无意义
      submitForm() {
        this.$refs['elForm'].validate((valid) => {
          if (!valid) return
          // TODO 提交表单
        })
      },
      /**
       * @description: 表单清空
       * @param {*}
       * @return {*}
       */
      resetForm() {
        this.$refs['elForm'].resetFields()
      },
      /**
       * @description: 保存
       * @param {*}
       * @return {*}
       */
      async saveProject(row) {
        if (!row.projectname || !row.targetname) {
          this.$baseMessage('请填写项目名称和工作目标', 'error')
          return
        }
        if (!this.planid) {
          this.$baseMessage('请保存表单信息后再进行当前操作', 'error')
          return
        }
        row.show = false

        const data = {
          planid: this.planid,
          // planprojectid: this.planid,
          plancode: this.formData.plancode,
          finishtime: UTCformat(row.finishtime),
          projectname: row.projectname,
          planprojectid: row.planprojectid,
          targetname: row.targetname,
          orgids: row.orgids,
          orgidnames: row.orgidnames,
          bsjtype: row.bsjtype,
          externalassig: row.externalassig,
        }
        let res = await mergePlanProjectManageInfoList(data)
      },
      /**
       * @description: 添加点击按钮
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        // if (!this.planid) {
        //   this.$baseMessage('请保存表单信息后再进行当前操作', 'error')
        //   return
        // }
        // 000000
        this.tableDataProject.push({
          projectname: '',
          targetname: '',
          finishtime: '',
          externalassig: '',
          orgidnames: '',
          show: true,
        })
      },
      /**
       * @description: 删除附件
       * @param {*}
       * @return {*}
       */
      handleDeleteFile(index, row) {
        deleteFileById({ attId: row.attid }).then((res) => {
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
          this.tableData.splice(index, 1)
        })
      },
      /**
       * @description: 删除增加的项目
       * @param {*}
       * @return {*}
       */
      handleDelete(index, row) {
        if (row.planprojectid) {
          removePlanProjectInfo({
            planprojectid: row.planprojectid,
          }).then((res) => {
            if (res.code == 1) {
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
              this.tableDataProject.splice(index, 1)
            }
          })
        } else {
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
          this.tableDataProject.splice(index, 1)
        }
      },
      /**
       * @description: 唤起 选择组长的组件
       * @param {*}
       * @return {*}
       */
      showGroupLeader() {
        this.$refs['select'].showEdit('leader')
      },
      /**
       * @description: 选择 组员组件回调，把返回的值，保存到formData
       * @param {*}
       * @return {*}
       */
      selectTeamList(val) {
        // 0000000

        if (val) {
          this.$set(this.formData, 'realname', val[0].realname)
          this.$set(this.formData, 'principalid', val[0].staffid)
        }
      },
      /**
       * @description: 附件下载
       * @param {*}
       * @return {*}
       */
      // async handleDown(row) {
      //   const data = await download({ attId: row.attid })
      //   let filename = row.attname
      //   let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
      //   let url = window.URL.createObjectURL(blob, {
      //     type: 'application/vnd.ms-excel',
      //   })
      //   const link = document.createElement('a')
      //   link.style.display = 'none'
      //   link.href = url
      //   link.setAttribute('download', filename)
      //   document.documentElement.appendChild(link)
      //   link.click()
      //   document.documentElement.removeChild(link)
      // },
      /**
       * @description: 附件上传成功，把数据保存到tableData
       * @param {*}
       * @return {*}
       */
      // handleSuccess(response, file, fileList) {
      //   let fileObj = {}
      //   if (file.response.result == '200') {
      //     fileObj.uploader = this.uploader
      //     fileObj.attname = file.name
      //     fileObj.attsize = file.size
      //     fileObj.attid = response.data.attid

      //     this.tableData.push(fileObj)
      //     this.$baseMessage(file.response.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.response.msg, 'error')
      //   }
      // },
      // planLeader() {
      //   this.$refs['plan'].showEdit()
      // },
      // planList(val) {
      //   this.$set(this.formData, 'realname', val[0].realname)
      // },
      /**
       * @description: 提交
       * @param {*}
       * @return {*}
       */
      handleCandSelect1(index, value) {
        this.formData2.transferStaffName = value
      },
      /**
       * @description: 择候选人 回调
       * @param {*}
       * @return {*}
       */
      handleCandSelect(index, value) {
        // this.$set(this.formData3[index], 'transferStaffId', value)
        this.formData3[index].transferStaffId = value
      },
      /**
       * @description: 关闭组件
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.$refs['form'].resetFields()
        this.clearType = true
        this.$bus.$emit('updateMsg', 0)
      },
      /**
       * @description: 关闭 流程提交页面
       * @param {*}
       * @return {*}
       */
      currentClose() {
        this.visible = false
        this.form = {}
      },
      /**
       * @description: 唤起 候选人组件
       * @param {*}
       * @return {*}
       */
      handlefzChange(e) {
        this.$refs['fzform'].clearValidate()
      },
      /**
       * @description: 重新提交，区分是否有分支，是否有候选人
       * @param {*}
       * @return {*}
       */
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const res = await ymWorkCandidates({
              flowId: this.flowtaskinfoflowid,
              fromId: this.fromId,
              flowTaskOperatorId: '',
              id: '',
            })
            this.candidateType = res.data.candidateType
            if (res.data.candidateType == 1) {
              this.fzoptions = res.data.list
              this.visible = true
              let list = []
              res.data.list.map((item) => {
                list.push({
                  value: item.nodeId,
                  label: item.nodeName,
                  hasCandidates: item.hasCandidates,
                })
              })
              this.options = list
              //保存请求人员列表的信息
              let candidateData = {
                // tableId: tableId,
                fromId: this.fromId,
              }
              this.candidateData = candidateData
            } else if (res.data.candidateType == 2) {
              let candidateData = {
                // tableId: tableId,
                fromId: this.fromId,
                nodeId: res.data.list[0].nodeId,
              }
              this.candidateData = candidateData
              this.visible2 = true
            } else {
              const wordres = await ymWorkSubmit({
                flowId: this.flowtaskinfoflowid,
                fromId: this.fromId,
                branchStrs: this.fzforms.branchStrs
                  ? this.fzforms.branchStrs.join(',')
                  : '',
                candidateType: res.data.candidateType,
                ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
              })
              if (wordres.code === 1) {
                this.$message.success(wordres.msg)
                this.close()
                this.visible = false
              }
            }
          }
        })
      },
      /**
       * @description:  流程提交函数，有分支的情况
       * @param {*}
       * @return {*}
       */
      async save4() {
        if (this.fzforms.branchStrs.length == 0) {
          this.$message.warning('请选择分支')
          return
        }
        let arr = []
        if (this.formData3.length > 0) {
          this.formData3.map((res) => {
            let str = []
            res.transferStaffId.map((item) => {
              str.push(item.id)
            })
            arr.push(str)
          })
        }

        let list = []
        arr.map((item) => {
          let str = item.join(',')
          if (str) {
            list.push(str)
          }
        })
        let candidateList = list.join('~')

        const { data, code } = await ymWorkSubmit({
          // tableId: this.tableId,
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          branchStrs: this.fzforms.branchStrs
            ? this.fzforms.branchStrs.join(',')
            : '',
          candidateList: candidateList || '',
          ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
          candidateType: this.candidateType,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close4()
          this.close()
        }
      },
      /**
       * @description:  打开 选择候选人组件
       * @param {*}
       * @return {*}
       */
      handleSelect() {
        this.$refs['candidateList'].show(this.candidateData)
      },
      /**
       * @description: 数据处理
       * @param {*}
       * @return {*}
       */
      selectValue(e) {
        let arr = []
        this.fzoptions.forEach((res) => {
          e.forEach((res1) => {
            if (res.value == res1) {
              arr.push(res)
            }
          })
        })
        this.runderList = arr
        this.formData3 = arr.map(() => {
          return { transferStaffName: '', transferStaffId: '' }
        })
      },
      /**
       * @description: 唤起 另一个候选人组件
       * @param {*}
       * @return {*}
       */
      handleSelectNew(row, index) {
        this.candidateData.nodeId = row.value
        this.$refs['candidateListNew'].show(this.candidateData, index)
      },
      /**
       * @description: 数据处理，把候选人数据数组 转化为字符串，在页面上显示
       * @param {*}
       * @return {*}
       */
      handSelectedNew1(data, index) {
        let name = '',
          id = ''
        data.map((item) => {
          name += item.fullName + ','
          id += item.id + ','
        })
        name = name.substring(0, name.length - 1)
        id = id.substring(0, id.length - 1)
        this.$set(this.formData3[index], 'transferStaffName', name)
        this.$set(this.formData3[index], 'transferStaffId', id)
      },
      /**
       * @description: 流程提交，有分支，有候选人的情况
       * @param {*}
       * @return {*}
       */
      async save1() {
        let branchStrs = ''
        this.fzform1.branchStrs.map((item) => {
          branchStrs = branchStrs + item + ','
        })
        branchStrs = branchStrs.substring(0, branchStrs.length - 1)
        let arr = []
        if (this.formData3.length > 0) {
          this.formData3 &&
            this.formData3.map((res) => {
              let str = []
              res.transferStaffId &&
                res.transferStaffId.map((item) => {
                  str.push(item.id)
                })
              arr.push(str)
            })
        }

        let list = []
        arr.map((item) => {
          let str = item.join(',')
          if (str) {
            list.push(str)
          }
        })
        let candidateList = list.join('~')

        const { data, code } = await ymWorkSubmit({
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          branchStrs,
          candidateType: this.candidateType,
          candidateList: candidateList || '',
          ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.visible1 = false
        }
      },
      /**
       * @description: 流程提交页面
       * @param {*}
       * @return {*}
       */
      close1() {
        this.visible1 = false
        this.resetINfo()
      },
      /**
       * @description: 关闭流程提交页面
       * @param {*}
       * @return {*}
       */
      close2() {
        this.visible2 = false
        this.resetINfo()
      },
      /**
       * @description: 关闭流程提交页面
       * @param {*}
       * @return {*}
       */
      close4() {
        this.visible = false
        this.resetINfo()
      },
      /**
       * @description: 流程提交页面的数据
       * @param {*}
       * @return {*}
       */
      resetINfo() {
        this.flowId = ''
        this.fromId = ''
        this.runderList = []
        this.formData = {
          value: [],
        }
        this.formData2 = {
          transferStaffName: '',
          transferStaffId: '',
        }
      },
      // 无意义
      handSelected(data) {
        let name = '',
          id = ''
        data.map((item) => {
          name += item.fullName + ','
          id += item.id + ','
        })
        name = name.substring(0, name.length - 1)
        id = id.substring(0, id.length - 1)
        this.formData2.transferStaffName = name
        this.formData2.transferStaffId = id
      },
      /**
       * @description: 程提交，有候选人的情况
       * @param {*}
       * @return {*}
       */
      async save2() {
        if (!this.formData2.transferStaffName) {
          this.$message.error('请选择候选人')
          return
        }

        let list = []
        this.formData2.transferStaffName.map((item) => {
          list.push(item.id)
        })
        const { data, code } = await ymWorkSubmit({
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          candidateList: list.join(','),
          nodeCode: this.candidateData.nodeId,
          candidateType: this.candidateType,
          ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close2()
          this.close()
        }
      },
    },
  }
</script>
<style scoped lang="scss">
  ::v-deep .el-form.disabled {
    input {
      border: 0;
      background-color: #ffffff;
    }
  }
  ::v-deep .el-date-editor.el-input,
  .el-date-editor.el-input__inner {
    width: 140px;
  }
  ::v-deep .el-input--small .el-input__inner {
    width: 140px;
  }

  ::v-deep .el-form-item__content {
    line-height: 0 !important;
  }
</style>
