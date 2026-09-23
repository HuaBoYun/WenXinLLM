<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    :append-to-body="true"
    width="800px"
    @close="closeCurrent"
    :close-on-click-modal="false"
  >
    <el-form :model="form" label-width="100px" :rules="rules">
      <el-form-item label="分支选择" v-if="hasFzStatus" prop="branchStrs">
        <el-select
          style="width: 100%"
          v-model="form.branchStrs"
          @change="selectValue"
        >
          <el-option
            v-for="item in fzoptions"
            :label="item.nodeName"
            :value="item.nodeId + '~' + item.hasCandidates"
            :key="item.key"
          ></el-option>
        </el-select>
      </el-form-item>
      <div v-for="(item, index) in runderList" :key="item.value">
        <el-form-item :label="item.nodeName" v-if="item.hasCandidates">
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
      <el-form-item
        label="加签人"
        v-if="type !== 'reject' && hasFreeApprover"
        prop="freeApproverUserName"
      >
        <el-input
          v-model="form.freeApproverUserName"
          placeholder="请选择加签人"
          style="width: 79%; margin-right: 8px"
          @clear="clearFreeApprover()"
          clearable
        ></el-input>
        <el-button type="primary" @click="handleSelectUser1">请选择</el-button>
      </el-form-item>
      <el-form-item
        :label="candidateData && candidateData.nodeName"
        prop="transferStaffName"
        v-if="type !== 'reject' && candidateType == 2"
      >
        <!-- <el-input
          placeholder="请选择候选人"
          v-model="form.transferStaffName"
          style="width: 79%; margin-right: 8px"
          clearable
        ></el-input>
        <el-button type="primary" @click="handleSelectPeo">请选择</el-button> -->
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
      <el-form-item label="退回节点" prop="rejectStep" v-if="type == 'reject'">
        <el-select
          v-model="form.rejectStep"
          @change="$forceUpdate()"
          placeholder="请选择退回节点"
          style="width: 79%; margin-right: 8px"
        >
          <el-option
            v-for="item in rejectList"
            :label="item.nodeName"
            :value="item.nodeCode"
            :key="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="rejectType" v-if="type == 'reject'" label="">
        <el-radio-group v-model="form.rejectType" @change="$forceUpdate()">
          <el-radio :label="1">
            重走流程
            <el-tooltip
              content="若流程为A->B->C,C退回至A，则C->A->B->C"
              placement="top"
            >
              <i class="el-icon-warning-outline"></i>
            </el-tooltip>
          </el-radio>

          <el-radio :label="2">
            直接提交给我
            <el-tooltip
              content="若流程为A->B->C,C退回至A，则C->A->C"
              placement="top"
            >
              <i class="el-icon-warning-outline"></i>
            </el-tooltip>
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="审批意见" prop="handleOpinion">
        <el-input
          type="textarea"
          v-model="form.handleOpinion"
          @input="onInput()"
          placeholder="请输入审批意见"
        ></el-input>
      </el-form-item>
      <el-form-item label="电子签名" v-if="type !== 'reject' && hasSign">
        <div style="text-align: right">
          <el-button type="text" @click="YRQM">引用签名</el-button>
          <el-button type="text" @click="handleGenerate">确定</el-button>
          <el-button type="text" @click="handleReset">清除</el-button>
        </div>
        <div>
          <img
            style="width: 100%; height: 100px"
            :src="form.signImg"
            v-if="form.signImg"
            alt=""
          />
          <vue-esign
            v-else
            ref="esign"
            :height="200"
            style="border: 1px #d5d5d5 solid; width: 100%"
            :width="860"
          />
        </div>
      </el-form-item>
      <el-form-item
        label="抄送人"
        v-if="type !== 'reject' && isCustomCopy"
        prop="copyIdsName"
      >
        <el-input
          clearable
          placeholder="请选择抄送人"
          @clear="clearCopyIds()"
          v-model="form.copyIdsName"
          style="width: 79%; margin-right: 8px"
        ></el-input>
        <el-button type="primary" @click="handleSelectUser2">请选择</el-button>
      </el-form-item>
      <el-form-item
        label="抄送审批人员"
        v-if="type !== 'reject' && isCustomCopy"
        prop="copyApproverName"
      >
        <el-input
          clearable
          placeholder="请选择抄送审批人员"
          @clear="clearCopyIds()"
          v-model="form.copyApproverName"
          style="width: 79%; margin-right: 8px"
        ></el-input>
        <el-button type="primary" @click="handleSelectUser3">请选择</el-button>
      </el-form-item>
    </el-form>
    <template v-if="!$store.state.work.processMobile">
      <div style="text-align: right; margin-bottom: 5px">
        <el-upload
          class="upload-demo"
          :show-file-list="false"
          :action="baseApi + api"
          :headers="headers"
          :data="{
            flowTaskOperatorId: this.form.operatorId,
            flowTaskId: this.form.id,
          }"
          :on-success="handleSuccess"
        >
          <el-button type="success">上传</el-button>
        </el-upload>
        <!-- <el-upload
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
        </el-upload> -->
      </div>
      <el-table :data="tableData">
        <el-table-column align="center" label="附件名称" prop="attname" />
        <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="150"
        >
          <template #default="{ row, $index }">
            <el-button type="text" @click="handleDownLCfile(row)">
              下载
            </el-button>
            <el-button type="text" @click="handlePreviewOpen(row)">
              预览
            </el-button>
            <el-button
              v-if="!readonly"
              type="text"
              @click="handleDelete(row, $index)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </template>
    <div slot="footer">
      <el-row>
        <!-- <el-col :span="12" style="text-align: left !important">
          <el-button type="primary" @click="saveOpinion">保存模板</el-button>
          <el-button type="primary" @click="openOpinionList">
            引用模板
          </el-button>
        </el-col> -->
        <el-col :span="24" style="text-align: right !important">
          <el-button type="primary" @click="save" :loading="buttonLoading">
            {{ type == 'audit' ? '同意' : '不同意' }}
          </el-button>
          <el-button @click="closeCurrent">取 消</el-button>
        </el-col>
      </el-row>
    </div>

    <executor-options
      ref="userref1"
      @selected="handleSelected1"
      :batch="!!form.batch"
    />
    <executor-options2
      ref="userref2"
      @projectManage="handleSelected2"
      :isUserName="true"
    />

    <!-- <CompanySelectUserByTree
      ref="userref2"
      @selected="handleSelected4"
      :isUserName="true"
    /> -->
    <CopyApproverNameList
      ref="userref3"
      @selected="handleSelected3"
      :isUserName="true"
    />
    <CandidateList ref="candidateList" @selected="handSelected" />
    <CandidateListNew ref="candidateListNew" @selected="handSelectedNew1" />

    <OpinionList ref="opinionList" @selected="handSelectedOpinion" />
    <OpinionEdit ref="opinionEdit" />
  </el-dialog>
</template>

<script>
  import {
    ymWorkAudit,
    ymWorkCandidates,
    ymWorkReject,
    ymWorkRejectList,
    fildDownload,
    fileList,
    fileRemove,
    getPrivewAttInfo,
  } from '@/api/contract/manage.js'
  import {
    getBatchList,
    batchCandidate,
    batchOperation,
  } from '@/oapi/contract/manage'
  import CandidateList from '@/components/CandidateList'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree/index.vue'
  import CandidateListNew from '@/views/contract/contractManage/components/CandidateList'
  import vueEsign from 'vue-esign'
  import ExecutorOptions from './executor.vue'
  import ExecutorOptions2 from '@/components/selectPerson.vue'
  import CopyApproverNameList from './CopyApproverNameList.vue'
  import UserList from './UserList.vue'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import store from '@/store'
  import OpinionList from './OpinionList.vue'
  import OpinionEdit from './OpinionEdit.vue'
  const { baseURL } = require('@/config')
  import { deWeight2 } from '@/utils'
  import { getSPQMInfo } from '@/api/setting/qmgl'
  import { getLCfile } from '@/api/setting/system'
  // import { customUpload } from '@/utils/Uploader'
  // import { handleDown } from '@/utils/fileHandler'
  const token = store.getters['user/token']
  export default {
    components: {
      vueEsign,
      UserList,
      ExecutorOptions,
      ExecutorOptions2,
      CandidateList,
      CandidateListNew,
      CandidateUserSelect,
      CompanySelectUserByTree,
      CopyApproverNameList,
      OpinionList,
      OpinionEdit,
    },
    props: ['hasSign', 'hasFreeApprover', 'isCustomCopy'],
    data() {
      return {
        baseApi: baseURL,
        api: '/setting/ymWrok/fileuploadZH',
        headers: {
          token: store.getters['user/token'],
        },
        // headers: { token: token },
        // fileList: [],
        // api: 'https://www.wenxin.example.com/api/file/file/upload',
        // lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        tableData: [],
        visible: false,
        buttonLoading: false,
        form: {
          candidateList: '',
          rejectStep: '',
          rejectType: 1,
        },
        fzoptions: [],
        rules: {
          branchStrs: [
            { required: true, message: '请选择分支', trigger: 'change' },
          ],
          handleOpinion: [
            { required: true, message: '请输入审批意见', trigger: 'blur' },
          ],
          // transferStaffName: [
          //   { required: true, message: '请选择候选人', trigger: 'change' },
          // ],
        },
        hasFzStatus: false, // 是否有选择分支的功能
        candidateType: 0,
        type: '',
        candidateData: {},
        formData3: [],
        runderList: [
          // {
          //   hasCandidates: true,
          //   isCandidates: true,
          //   nodeId: 'AHGcOE1',
          //   nodeName: '部门负责人加签(或签)',
          // },
          // {
          //   hasCandidates: true,
          //   isCandidates: true,
          //   nodeId: 'AHGcOE1',
          //   nodeName: '部门负责人加签(或签)2222',
          // },
        ],
        clearType: false,
        rejectList: [],
      }
    },
    computed: {
      title() {
        if (this.type === 'reject') {
          return '审批拒绝'
        } else if (this.type === 'audit') {
          return '审批通过'
        } else {
          return ''
        }
      },
    },
    methods: {
      handSelectedOpinion(data) {
        console.log(data)
      },
      handleCandSelect1(index, value) {
        // this.formData3[index].transferStaffId = value

        // let transferStaffId = value
        // this.formData3.push({ transferStaffId })
        // this.form.transferStaffName = value
        let list = []
        list.push({
          transferStaffId: value,
          index,
        })
        this.formData3 = list
        console.log('z', value)
        this.form.transferStaffName = value
      },

      handleCandSelect(index, value) {
        // this.$set(this.formData3[index], 'transferStaffId', value)
        let list = []
        list.push({
          transferStaffId: value,
          index,
        })
        this.formData3 = list
      },
      removeDuplicateObj(arr) {
        let obj = {}
        arr = arr.reduce((newArr, next) => {
          obj[next.index] ? '' : (obj[next.index] = true && newArr.push(next))
          return newArr
        }, [])
        return arr
      },
      handleSelectPeo() {
        this.$refs.candidateList.show(this.candidateData)
      },
      handSelected(data) {
        let name = '',
          id = ''
        data.map((item) => {
          name += item.fullName + ','
          id += item.id + ','
        })
        name = name.substring(0, name.length - 1)
        id = id.substring(0, id.length - 1)
        let form = this.form
        form.transferStaffName = name
        form.candidateList = id
        this.form = null
        this.form = form
      },
      closeCurrent() {
        this.$bus.$emit('candidate', 1)
        this.hasFzStatus = false
        this.visible = false
        this.form = {}
        this.formData3 = []
        this.runderList = []
      },
      async getFileList(flowTaskOperatorId, flowTaskId) {
        const { data, code } = await fileList({
          flowTaskOperatorId,
          flowTaskId,
        })
        if (code == 1) {
          this.tableData = data
        }
      },
      async show(e, type) {
        this.form = { ...e }
        this.type = type
        if (type == 'audit') {
          this.form.handleOpinion = '同意'
        } else {
          this.form.handleOpinion = '不同意'
        }

        // 批量
        if (type === 'audit' && e.batch) {
          const { data, code } = await batchCandidate({
            flowId: e.flowId,
            id: e.id,
          })
          if (code == 1) {
            this.candidateType = data.type
            if (this.candidateType === 1) {
              this.fzoptions = data.list
              this.hasFzStatus = true
              //保存请求人员列表的信息
              let candidateData = {
                flowId: e.flowId,
                fromId: e.formId,
                batch: true,
              }
              this.candidateData = candidateData
            } else if (this.candidateType === 2) {
              let candidateData = {
                flowId: e.flowId,
                // fromId: e.formId,
                // flowTaskOperatorId: e.operatorId,
                id: e.id,
                nodeId: data.list[0].nodeId,
                nodeName: data.list[0].nodeName,
                batch: true,
              }
              this.candidateData = candidateData
            } else {
              this.hasFzStatus = false
            }
          }
          this.visible = true
          this.$forceUpdate()
          return
        }

        if (type === 'audit') {
          const { data, code } = await ymWorkCandidates({
            flowId: e.flowId,
            fromId: e.formId,
            flowTaskOperatorId: e.operatorId,
            id: e.id,
          })
          if (code == 1) {
            this.getFileList(e.operatorId, e.id)
            this.candidateType = data.candidateType

            // this.candidateData
            if (data.candidateType === 1) {
              //

              this.fzoptions = data.list
              this.hasFzStatus = true
              //保存请求人员列表的信息
              let candidateData = {
                flowId: e.flowId,
                fromId: e.formId,
              }
              this.candidateData = candidateData
            } else if (data.candidateType == 2) {
              let candidateData = {
                flowId: e.flowId,
                fromId: e.formId,
                flowTaskOperatorId: e.operatorId,
                id: e.id,
                nodeId: data.list[0].nodeId,
                nodeName: data.list[0].nodeName,
              }
              this.candidateData = candidateData
            } else {
              this.hasFzStatus = false
            }
          }
          this.visible = true
        } else {
          this.getRejectList(e.operatorId)
          this.visible = true
        }

        this.$forceUpdate()
        // if (type === 'reject') {

        // }
      },
      async getRejectList(operatorId) {
        this.buttonLoading = true
        const { data, code } = await ymWorkRejectList({
          operatorId: operatorId,
        })
        if (code == 1) {
          this.buttonLoading = false
          this.form.rejectStep = data.rejectList[0].nodeCode
          this.form.rejectType = 1
          this.rejectList = data.rejectList
          this.$forceUpdate()
        }
      },
      close() {
        this.closeCurrent()

        this.$emit('close')
      },
      async save() {
        // 删除SP密级id
        localStorage.removeItem('SPsecrectLevelId')
        const {
          transferStaffName,
          copyApproverName,
          copyApproverNameIds,
          copyIdsName,
          copyIds,
          ...other
        } = this.form
        //
        if (this.hasFzStatus) {
          if (!this.form.branchStrs) {
            this.$message.error('请选择分支')
            this.buttonLoading = false
            return
          }
        }

        for (var i = 0; i < this.runderList.length; i++) {
          if (this.runderList[i].hasCandidates && !this.formData3[i]) {
            this.$message.error('请选择候选人')
            this.buttonLoading = false
            return
          }
        }

        let arr = []
        if (this.formData3 && this.formData3.length > 0) {
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
        let list2 = []
        let copyId = copyIds ? copyIds.split(',') : []

        let copyIdsNames = copyIdsName ? copyIdsName.split(',') : []

        copyIdsNames.length > 0 &&
          copyIdsNames.map((item, index) => {
            list2.push({
              realname: item,
              staffid: copyId[index],
            })
          })
        let copyApproverNames = copyApproverName
          ? copyApproverName.split(',')
          : []

        console.log('copyApproverName', copyApproverName)
        console.log('copyId', copyIds)
        let copyApproverNameId = copyApproverNameIds
          ? copyApproverNameIds.split(',')
          : []
        copyApproverNames.length > 0 &&
          copyApproverNames.map((item, index) => {
            list2.push({
              realname: item,
              staffid: copyApproverNameId[index],
            })
          })
        list2 = deWeight2(list2)
        // console.log('list2', list2)
        // return
        let obj = {}
        if (this.candidateType == 2) {
          if (this.formData3.length == 0) {
            this.$message.error('请选择候选人')
            this.buttonLoading = false
            return
          }
          obj = {
            ...other,
            candidateType: this.candidateType,
            candidateList: candidateList || '',
            nodeCode: this.candidateData.nodeId,
            copyIds: list2.map((item) => item.staffid).join(','),
          }
        } else {
          obj = {
            candidateType: this.candidateType,
            candidateList: candidateList || '',
            ...other,
            copyIds: list2.map((item) => item.staffid).join(','),
          }
        }

        // if (obj.branchStrs) {
        //   obj.branchStrs = obj.branchStrs.join(',')
        // }

        // 批量审批
        if (this.type === 'audit' && this.form.batch) {
          this.buttonLoading = true
          const params = {
            candidatelist: {},
            candidateType: this.candidateType,
            // copyIds: '',
            encode: this.form.flowCode,
            pkYmstaffId: this.form.pkYmStaffId,
            freeApproverstaffId: this.form.freeApproverUserId,
            handleOpinion: this.form.handleOpinion,
            ids: [],
            flowId: this.form.flowId,
            taskList: [],
          }
          // 是否有候选人
          if (this.candidateData && this.candidateData.nodeId) {
            params.candidatelist = {
              [this.candidateData.nodeId]:
                this.formData3[0].transferStaffId.map((x) => x.id),
            }
          }
          this.form.taskList.map((x) => {
            params.ids.push(x.id)
            params.taskList.push({ fid: x.id, taskId: x.processId })
          })

          const { code } = await batchOperation(params)
          if (code == 1) {
            this.$message.success('审批成功')
            this.$bus.$emit('updateMsg', 0)
          }
          this.buttonLoading = false
        }

        if (this.type === 'audit' && !this.form.batch) {
          this.buttonLoading = true
          const { code } = await ymWorkAudit(obj)
          if (code == 1) {
            this.$message.success('审批成功')

            this.$bus.$emit('updateMsg', 0)
          }
          this.buttonLoading = false
        }

        if (this.type === 'reject') {
          this.buttonLoading = true
          const { data, code } = await ymWorkReject({
            ...other,
            flowId: this.form.flowId,
            id: this.form.id,
            operatorId: this.form.operatorId,
            handleOpinion: this.form.handleOpinion,
            thisStepId: this.form.thisStepId,
            nextNodeName: this.form.nextNodeName,
          })
          if (code == 1) {
            this.$message.success('成功')
            this.$bus.$emit('updateMsg', 0)
          }
          this.buttonLoading = false
        }

        this.close()
      },
      //清空
      handleReset() {
        this.$set(this.form, 'signImg', undefined)
        this.$refs.esign.reset()
      },
      //转换图片
      handleGenerate(transitionName) {
        this.$refs.esign
          .generate()
          .then((res) => {
            this.$set(this.form, 'signImg', res)
          })
          .catch((err) => {
            console.error(err)
          })
      },
      handleSelectUser1() {
        this.$refs['userref1'].show()
      },
      handleSelectUser2() {
        const mjId = JSON.parse(localStorage.getItem('SPsecrectLevelId'))
        this.$refs['userref2'].showEdit(mjId)
      },
      // handleSelectUser2() {
      //   let list = []
      //   let copyApproverNames = this.form.copyApproverName
      //     ? this.form.copyApproverName.split(',')
      //     : []
      //   let copyIds = this.form.copyIds ? this.form.copyIds.split(',') : []
      //   copyApproverNames.length > 0 &&
      //     copyApproverNames.map((item, index) => {
      //       list.push({
      //         realname: item,
      //         staffid: copyIds[index],
      //       })
      //     })
      //   this.$refs['userref2'].show(list)
      // },
      handleSelectUser3() {
        let list = []
        let copyIdsName = this.form.copyIdsName
          ? this.form.copyIdsName.split(',')
          : []
        let copyApproverNameId = this.form.copyApproverNameIds
          ? this.form.copyApproverNameIds.split(',')
          : []
        copyIdsName.length > 0 &&
          copyIdsName.map((item, index) => {
            list.push({
              realname: item,
              staffid: copyApproverNameId[index],
            })
          })
        let processId = this.form.id
        this.$refs['userref3'].show(list, processId)
      },
      handleSelected1(e) {
        this.$set(
          this.form,
          'pkYmStaffId',
          e.map((item) => item.pkYmStaffId).join(',')
        )
        this.$set(
          this.form,
          'freeApproverUserName',
          e.map((item) => item.realname).join(',')
        )
        this.$set(
          this.form,
          'freeApproverUserId',
          e.map((item) => item.staffid).join(',')
        )
      },
      clearFreeApprover() {
        this.form.freeApproverUserName = ''
        this.form.freeApproverUserId = ''
      },
      handleSelected2(e) {
        console.log(e)
        let realname = e.map((item) => item.realname).join(',')
        let staffid = e.map((item) => item.staffid).join(',')
        this.$set(this.form, 'copyIdsName', realname)
        this.$set(this.form, 'copyIds', staffid)
      },
      handleSelected4(e) {
        let realnames = e.map((item) => {
          return item.realname
        })
        let staffids = e.map((item) => {
          return item.staffid
        })
        this.$set(this.form, 'copyIdsName', realnames.join(','))
        this.$set(this.form, 'copyIds', staffids.join(','))
      },
      handleSelected3(e) {
        let realnames = e.map((item) => {
          if (item) return item.realname
        })
        let staffids = e.map((item) => {
          if (item) return item.staffid
        })
        this.$set(this.form, 'copyApproverName', realnames.join(','))
        this.$set(this.form, 'copyApproverNameIds', staffids.join(','))
      },
      clearCopyIds() {
        this.form.copyIdsName = ''
        this.form.copyIds = ''
      },
      selectValue(e) {
        let arr = []
        this.fzoptions.forEach((res) => {
          if (res.nodeId == e.split('~')[0]) {
            arr.push(res)
          }
        })

        this.runderList = arr

        // this.formData3 = arr.map(() => {
        //   return { transferStaffName: '', transferStaffId: '' }
        // })
      },
      handleSelectNew(row, index) {
        this.candidateData.nodeId = row.nodeId

        this.$refs['candidateListNew'].show(this.candidateData, index)
      },
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
      onInput() {
        this.$forceUpdate()
      },
      async handleDownLCfile(row) {
        const data = await getLCfile({ fileId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {})
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      // async handleDown(row) {
      //   const data = await fildDownload({ attId: row.attid })
      //   let filename = row.attname
      //   let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
      //   let url = window.URL.createObjectURL(blob, {})
      //   const link = document.createElement('a')
      //   link.style.display = 'none'
      //   link.href = url
      //   link.setAttribute('download', filename)
      //   document.documentElement.appendChild(link)
      //   link.click()
      //   document.documentElement.removeChild(link)
      // },
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        const res = await fileRemove({ attId: row.attid })
        if (res.code == 1 || res.code == '1') {
          this.$baseMessage('删除成功', 'success')
        } else {
          this.$baseMessage(res.msg || '删除失败', 'error')
        }
      },
      handlePreview(file) {},
      handleSuccess(response) {
        const res = response || {}
        console.log(res, 'upload success response')
        if (res.code == 1 || res.code == '1') {
          const current = Array.isArray(this.tableData) ? this.tableData : []
          const incoming = Array.isArray(res.data) ? res.data : [res.data]
          this.tableData = [...current, ...incoming]
          this.$baseMessage(res.msg || '上传成功', 'success')
        } else {
          this.$baseMessage(res.msg || '上传失败', 'error')
        }
      },
      async handlePreviewOpen(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 3,
        })
        if (data.zxtoken) {
          const url = data.previewurl + '?url=' + data.zxtoken
          this.$iFrameDialog({ iframeUrl: url })
        } else {
          const url =
            data.previewurl +
            '?url=' +
            encodeURIComponent(Base64.encode(data.ftpUrl))
          this.$iFrameDialog({ iframeUrl: url })
        }
      },
      saveOpinion() {
        if (!this.form.handleOpinion) {
          this.$message.error('请输入审批意见')
          return
        }
        this.$refs.opinionEdit.show(
          {
            tempMemo: this.form.handleOpinion,
          },
          'edit',
          {
            flowId: this.form.flowId,
            taskNodeId: this.form.id,
          }
        )
      },
      openOpinionList() {
        this.$refs['opinionList'].show({
          flowId: this.form.flowId,
          taskNodeId: this.form.id,
        })
      },
      YRQM() {
        getSPQMInfo().then((res) => {
          if (res.code == 1) {
            this.$set(this.form, 'signImg', res.data[0])
          }
        })
      },
      // customUploadWrapper(options) {
      //   if (
      //     !this.baseApi ||
      //     !this.api ||
      //     !this.headers ||
      //     !window.key ||
      //     !window.iv
      //   ) {
      //     return
      //   }

      //   // 确保 fileList 是一个数组
      //   const fileList = Array.isArray(options.file)
      //     ? options.file
      //     : [options.file]

      //   // 获取 el-upload 的 data 参数
      //   // const formData = {
      //   //   formlevel: this.getFormLevel,
      //   // }

      //   // 调用自定义上传函数
      //   new Promise((resolve, reject) => {
      //     customUpload({
      //       baseApi: this.baseApi,
      //       api: this.api,
      //       key: window.key,
      //       iv: window.iv,
      //       headers: this.headers,
      //       fileList: fileList, // 使用 fileList 而不是 file
      //       // formData: formData, // 传递额外的表单数据
      //       onProgress: this.handleProgress,
      //       onSuccess: (response) => {
      //         this.handleSuccess(response)
      //         resolve(response) // 成功时调用 resolve
      //       },
      //       onError: (error) => {
      //         // this.handleError(error)
      //         reject(error) // 失败时调用 reject
      //       },
      //     })
      //   })
      // },
      // handleSuccess(file) {
      //   if (file.code == 200) {
      //     // this.fileList = [...this.fileList, ...file.data]
      //     this.tableData = [...this.tableData, ...file.data]
      //     // this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      // handleBeforeUpload(file, fileList) {
      //   const isLt2M = file.size / 1024 / 1024 < 100 // 检查文件大小是否小于100MB
      //   if (!isLt2M) {
      //     this.$message.error('文件大小不能超过 100MB!')
      //     return false // 返回false停止上传
      //   }

      //   // 如果文件大小合适，则调用自定义上传逻辑
      //   this.customUploadWrapper({ file })
      //   return false // 停止默认上传行为
      // },
      // //下载公共方法调用
      // async handleDowns(row) {
      //   try {
      //     // 调用 handleDown 并传递自定义的下载接口
      //     await handleDown(row, this.headers, this.lodeapi)
      //   } catch (error) {
      //     console.error('自定义下载失败:', error)
      //   }
      // },
      // handlePreviewFile(row) {
      //   if (row.isEncrypted === '1') {
      //     // 当文件是加密状态时，使用指定的在线预览链接
      //     const previewUrl = row.previewUrl
      //     window.open(previewUrl, '_blank')
      //   } else {
      //     this.$iFrameDialog({ attid: row.attid })
      //   }
      // },
      // async handlePreview(row) {
      //   const { data } = await getPrivewAttInfo({
      //     attId: row.attid,
      //     attType: 0,
      //   })
      //   let url =
      //     data.previewurl +
      //     '?url=' +
      //     encodeURIComponent(Base64.encode(data.ftpUrl))
      //   if (
      //     data.ftpUrl.includes('.pdf') ||
      //     data.ftpUrl.includes('.doc') ||
      //     data.ftpUrl.includes('.docx')
      //   ) {
      //     url = url + '&officePreviewType=pdf'
      //   }
      //   window.open(url)
      // },
    },
    beforeDestroy() {
      this.$bus.$off('candidate')
    },
  }
</script>

<style lang="scss" scoped></style>
