<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row v-if="show == 0" :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="对方谈判人" prop="counterpart">
            <el-input
              v-model="form.counterpart"
              clearable
              placeholder="请输入对方谈判人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对方谈判人联系电话">
            <el-input
              v-model="form.counterpartphone"
              clearable
              placeholder="请输入对方谈判人联系电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否预设调解方案">
            <el-radio-group v-model="form.ispresetcase">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="modalType === 'consult'">
          <el-form-item label="是否协商一致">
            <el-radio-group v-model="form.isaggree">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="2">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col v-if="form.isaggree == 1 && modalType === 'consult'" :span="12">
          <el-form-item label="解决方式">
            <el-radio-group v-model="form.solutionmode">
              <el-radio :label="1">私下调解</el-radio>
              <el-radio :label="2">司法调解</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col v-if="form.isaggree == 2 && modalType === 'consult'" :span="12">
          <el-form-item label="司法解决">
            <el-radio-group v-model="form.judicialsettlement">
              <el-radio :label="1">诉讼</el-radio>
              <el-radio :label="2">仲裁</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="modalType === 'consult'">
          <el-form-item label="隶属纠纷" prop="disputeitem">
            <el-input
              v-model.trim="form.disputeitem"
              clearable
              placeholder="请选择隶属纠纷"
              readonly
              :style="{ width: '256px' }"
              @input="change()"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.xzjf.show(0)"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="纠纷合同">
            <el-input
              v-model="form.contractname"
              clearable
              placeholder="请输入纠纷合同"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="24" v-if="modalType === 'consult'">
          <el-form-item label="谈判结果">
            <el-input
              v-model="form.negetiaresult"
              clearable
              placeholder="请输入谈判结果"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="调解方案">
            <el-input
              v-model="form.mediationScheme"
              clearable
              :disabled="modalType == 'consult'"
              placeholder="请输入调解方案"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-form>

      <el-col :span="24">
        <el-divider>协商过程信息</el-divider>
      </el-col>
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px">
          <el-button type="success" @click="showEditAdd({}, 'add')">
            新增
          </el-button>
        </div>
        <el-table
          v-loading="listLoading"
          :data="list"
          style="margin-bottom: 20px"
        >
          <el-table-column
            align="center"
            label="谈判时间"
            prop="negotiationtime"
          >
            <template #default="{ row }">
              <el-button @click="showEditAdd(row, 'detail')" type="text">
                {{ row.negotiationtime }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="对方谈判人"
            prop="recordcounterpart"
          />
          <el-table-column align="center" label="我方谈判人" prop="username" />
          <el-table-column
            align="center"
            label="谈判方式"
            prop="negotiationmode"
          />
          <el-table-column align="center" label="录入人" prop="realname" />
          <el-table-column align="center" label="录入时间" prop="createtime" />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button type="text" @click="showEditAdd(row, 'edit')">
                修改
              </el-button>
              <el-button type="text" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col :span="24">
        <el-divider>附件</el-divider>
      </el-col>
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px">
          <el-button v-if="!form.negotiaid" type="success" @click="hold()">
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
            :limit="1"
            :on-error="onError"
            :on-remove="handleRemove"
            :on-success="onSuccess"
          >
            <el-button type="success">上传</el-button>
          </el-upload>
        </div>
        <el-table :data="uploadlist">
          <el-table-column align="center" label="附件名称" prop="attname" />
          <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
          <el-table-column align="center" label="创建人" prop="uploader" />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handlePreviewFile(row)">
                预览
              </el-button>
              <el-button type="text" @click="downloadData(row)">下载</el-button>
              <el-button type="text" @click="handleDelete1(row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <el-row v-if="show == 1" :gutter="15">
      <el-form ref="form" label-width="140px" :model="form" :rules="rules">
        <el-col :span="12">
          <el-form-item label="对方谈判人" prop="counterpart">
            <span>{{ form.counterpart }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="对方谈判人联系电话">
            <span>{{ form.counterpartphone }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否预设调解方案">
            <span>{{ form.ispresetcase == 1 ? '是' : '否' }}</span>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="是否协商一致">
            <span>{{ form.isaggree == 1 ? '是' : '否' }}</span>
          </el-form-item>
        </el-col>
        <el-col v-if="form.isaggree == 1" :span="12">
          <el-form-item label="解决方式">
            <span>
              {{ form.solutionmode === 1 ? '私下调解' : '司法调解' }}
            </span>
          </el-form-item>
        </el-col>
        <el-col v-if="form.isaggree == 2" :span="12">
          <el-form-item label="司法解决">
            <span>
              {{ form.judicialsettlement == 1 ? '诉讼' : '仲裁' }}
            </span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="隶属纠纷" prop="disputeitem">
            <span>{{ form.disputeitem }}</span>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="纠纷合同">
            <span>{{ form.contractname }}</span>
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-form-item label="谈判结果">
            <span>{{ form.negetiaresult }}</span>
          </el-form-item>
        </el-col>
      </el-form>
      <el-col :span="24">
        <el-divider>协商过程信息</el-divider>
      </el-col>
      <el-col :span="24">
        <el-table
          v-loading="listLoading"
          :data="list"
          style="margin-bottom: 20px"
        >
          <el-table-column
            align="center"
            label="谈判时间"
            prop="negotiationtime"
          >
            <template #default="{ row }">
              <el-button @click="showEditAdd(row, 'detail')" type="text">
                {{ row.negotiationtime }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="对方谈判人"
            prop="recordcounterpart"
          />
          <el-table-column align="center" label="我方谈判人" prop="username" />
          <el-table-column
            align="center"
            label="谈判方式"
            prop="negotiationmode"
          />
          <el-table-column align="center" label="录入人" prop="realname" />
          <el-table-column align="center" label="录入时间" prop="createtime" />
        </el-table>
      </el-col>
      <el-col :span="24">
        <el-divider>附件</el-divider>
      </el-col>
      <el-col :span="24">
        <el-table :data="uploadlist">
          <el-table-column align="center" label="附件名称" prop="attname" />
          <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
          <el-table-column align="center" label="创建人" prop="uploader" />
          <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handlePreviewFile(row)">
                预览
              </el-button>
              <el-button type="text" @click="downloadData(row)">下载</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button v-if="show == 0" type="primary" @click="save">确 定</el-button>
      <el-button
        v-if="modalType === 'consult' && schemeStatus == 0"
        type="primary"
        @click="handleApproval"
      >
        提交审批
      </el-button>
      <el-button
        v-if="
          (form.negotiastatus == 2 || form.negotiastatus == 3) &&
          jurisdictionCode == 1 &&
          modalType != 'consult'
        "
        @click="ymsubmit"
        type="primary"
      >
        提交
      </el-button>
      <el-button
        v-if="
          (form.schemeStatus == 2 || form.schemeStatus == 3) &&
          jurisdictionCode == 1
        "
        @click="ymsubmit"
        type="primary"
      >
        提交
      </el-button>
    </template>
    <xzjf-options ref="xzjf" @selected="handleSsjd" />
    <consultEdit ref="editadd" @editadd="handleeditadd" />
    <!-- 代理律师新增编辑 -->
    <lower-edit ref="lowerEdit" @fetch-lawer-list="fetchLawerList" />
    <ProcessList ref="process" @fetchData="fetchData" />

    <!-- 提交 -->
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </el-dialog>
</template>

<script>
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'

  import {
    deleAttacheMent,
    findAttacheMent,
    legalAttorney,
    legalAttorneyDelete,
    negotiatedRecordDetail,
    negotiatedSettlemenModify,
    negotiatedSettlemenSave,
    negotiateRecord,
    removeNegotiatedRecord,
  } from '@/api/fwgl/legal'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { baseURL } from '@/config/net.config'
  import store from '@/store'
  import LowerEdit from '@/views/fwgl/legal/components/lowerEdit'
  import xzjfOptions from './options/xzjf.vue'
  import consultEdit from './smEdit/consultEdit2.vue'
  import { downloads } from '@/api/fwgl/zzxx'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  export default {
    name: 'DraftEdit',
    components: {
      xzjfOptions,
      consultEdit,
      LowerEdit,
      ProcessList,
      Resubmit,
      CandidateUserSelect,
    },
    data() {
      return {
        form: {
          negotiaid: undefined,
          counterpart: undefined,
          counterpartphone: undefined,
          isaggree: 1,
          ispresetcase: 1,
          solutionmode: 1,
          disputeitem: undefined,
          negetiaresult: undefined,
          mediationScheme: undefined,
          disputeId: undefined,
          attids: undefined,
          contractname: undefined,
          judicialsettlement: 1,
          negotiastatus: 0,
          schemeStatus: 0,
        },
        schemeStatus: 0,
        rules: {
          counterpart: [
            {
              required: true,
              message: '请输入对方谈判人',
              trigger: 'blur',
            },
          ],
          disputeitem: [
            {
              required: true,
              message: '请输入隶属纠纷',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        radio: '',
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          flowid: '698869',
        },
        list: [],
        dllsData: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        show: 0,
        accept: '.pdf, .doc, .docx, .xls, .xlsx,.png,.jpg,.jpeg',
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
        uploadlist: [],
        value: {},
        beforeUpload: null,
        baseApi: baseURL,
        fileList: [],
        modalType: 'add',
        //提交
        jurisdictionCode: 0,
        fromId: 0,
        fromIdcopy: 0,
        ymFromId: 0,
        flowtaskinfoflowid: '',
        status: '',
      }
    },
    computed: {
      uploadData() {
        return {
          type: 2,
          bid: this.form.negotiaid,
        }
      },
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.close()
        }
      })
    },
    created() {},
    methods: {
      /**
       * @description: 看起来没啥用的提示信息
       * @return {*}
       */
      hold() {
        this.$message.error('请先保存基本信息!')
      },
      change() {
        this.$forceUpdate()
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getEditList()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getEditList()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.getEditList()
      },
      /**
       * @description: 打开详情弹窗
       * @param {*} row
       * @return {*}
       */
      showDetail(row) {
        this.showEdit(row, 'detail', true)
      },
      async getDetail(negotiaid) {
        const res = await negotiatedRecordDetail({ negotiaId: negotiaid })

        if (res && res.negotiated) {
          this.form = res.negotiated
          this.netotiaId = res.negotiated.negotiaid

          if (
            res.negotiated.negotiastatus == 2 ||
            res.negotiated.negotiastatus == 3
          ) {
            const res2 = await getFlowTaskInfo({
              tableId: 21,
              formId: negotiaid,
            })
            this.jurisdictionCode = res2.data.isFlowInfo
            if (res2.data.isFlowInfo) {
              this.flowtaskinfoflowid = res2.data.flowId + ''
              this.fromId = negotiaid + ''
              this.fromIdcopy = negotiaid + ''
              this.ymFromId = res2.data.id + ''

              const res3 = await getFaqiInfo({
                id: res2.data.id,
                flowId: res2.data.flowId,
              })
              if (res3.code == 1) {
                this.status = res3.data.dataJson.flowTaskInfo.status
              }
            }
          }

          if (
            res.negotiated.schemeStatus == 2 ||
            res.negotiated.schemeStatus == 3
          ) {
            const res2 = await getFlowTaskInfo({
              tableId: 35,
              formId: negotiaid,
            })
            this.jurisdictionCode = res2.data.isFlowInfo
            if (res2.data.isFlowInfo) {
              this.flowtaskinfoflowid = res2.data.flowId + ''
              this.fromId = negotiaid + ''
              this.fromIdcopy = negotiaid + ''
              this.ymFromId = res2.data.id + ''

              const res3 = await getFaqiInfo({
                id: res2.data.id,
                flowId: res2.data.flowId,
              })
              if (res3.code == 1) {
                this.status = res3.data.dataJson.flowTaskInfo.status
              }
            }
          }
          this.uploadList()
          this.getEditList()
          this.fetchLawerList()
        }
      },
      /**
       * @description: 弹窗入口获取信息打开弹窗
       * @param {*} row
       * @param {*} type
       * @param {*} disabled
       * @return {*}
       */
      showEdit(row, type, disabled) {
        this.show = 0
        this.modalType = type
        if (!row) {
          this.title = '添加'
          this.listLoading = false
        } else {
          this.listLoading = true
          if (disabled) {
            this.title = '查看'
            this.show = 1
          } else {
            this.title = type && type === 'consult' ? '协商结果' : '编辑'
            this.schemeStatus = row.schemeStatus
            if (type == 'consult' && row.schemeStatus != 0) {
              this.show = 1
            }
          }

          this.getDetail(row.negotiaid)
        }
        this.dialogFormVisible = true
      },
      /**
       * @description: 提交审批
       * @return {*}
       */
      handleApproval() {
        const ps = [
          'negotiaid',
          'negotiastatus',
          'counterpart',
          'counterpartphone',
          'isaggree',
          'ispresetcase',
          'negetiaresult',
          'mediationScheme',
          'judicialsettlement',
          'solutionmode',
          'courtname',
          'disputeitem',
          'disputeId',
        ]
        const params = JSON.parse(JSON.stringify(this.form))
        Object.keys(params).forEach((key) => {
          if (!ps.some((x) => x === key)) delete params[key]
        })
        //提交审批
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const bCreate = !this.form.negotiaid
            const func = bCreate
              ? negotiatedSettlemenSave
              : negotiatedSettlemenModify
            func(params).then((res) => {
              if (res.code == 1) {
                if (bCreate) {
                  this.form.negotiaid = res.data
                }
                this.$refs['process'].save(35, this.form.negotiaid)
              }
            })
          } else {
            this.$message.error('请先保存基本信息!')
          }
        })
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.$refs['form'].resetFields()
        this.clearType = true
        this.$emit('fetch-data')
        this.form = this.$options.data().form
        this.list = []
        this.uploadlist = []
        this.dialogFormVisible = false
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        const ps = [
          'negotiaid',
          'negotiastatus',
          'counterpart',
          'counterpartphone',
          'isaggree',
          'ispresetcase',
          'negetiaresult',
          'mediationScheme',
          'judicialsettlement',
          'solutionmode',
          'courtname',
          'disputeitem',
          'disputeId',
        ]
        const params = JSON.parse(JSON.stringify(this.form))
        Object.keys(params).forEach((key) => {
          if (!ps.some((x) => x === key)) delete params[key]
        })

        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const bCreate = !this.form.negotiaid
            const func = bCreate
              ? negotiatedSettlemenSave
              : negotiatedSettlemenModify
            const msg = bCreate ? '新增成功' : '修改成功'
            func(params).then((res) => {
              if (res.code == 1) {
                this.$emit('fetch-data')
                if (bCreate) {
                  this.form.negotiaid = res.data
                }
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              }
            })

            // this.close()
          }
        })
      },
      /**
       * @description: 新增编辑协商过程
       * @param {*} row
       * @param {*} title
       * @return {*}
       */
      showEditAdd(row, title) {
        // this.$refs['editadd'].showEdit(row)
        if (!this.form.negotiaid) {
          this.$baseMessage(
            '请先保存基本信息',
            'success',
            'vab-hey-message-success'
          )
          return
        }
        this.$refs['editadd'].showEdit(
          {
            netotiaId: this.form.negotiaid,
            ...row,
          },
          title
        )
      },
      //附件列表
      async uploadList() {
        // this.listLoading = true
        const { data } = await findAttacheMent({
          type: 2,
          bid: this.form.negotiaid,
        })
        this.uploadlist = data
        this.$refs.upload.clearFiles()
        this.$refs.upload.uploadFiles.length = 0
        // this.listLoading = false
      },
      //附件删除
      handleDelete1(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleAttacheMent({ aid: row.attid, type: 2 })
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
      /**
       * @description: 文件上传限制
       * @param {*} file
       * @return {*}
       */
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 200
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      handleRemove(file, fileList) {},
      onSuccess(response, file, fileList) {
        this.uploadList()
      },
      onError(err) {
        this.$message.error(JSON.parse(err.message).message)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await removeNegotiatedRecord({
            recordId: row.recordid,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.getEditList()
        })
      },
      handleSsjd(val) {
        this.$set(this.form, 'disputeId', val.disputeid)
        this.$set(this.form, 'disputeitem', val.disputeitem)
        this.$set(this.form, 'contractname', val.contractname)
        this.form.disputeId = val.disputeid
        this.form.disputeitem = val.disputeitem
        this.form.contractname = val.contractname
      },
      handleeditadd() {
        this.getEditList()
      },
      async getEditList() {
        this.listLoading = true
        this.queryForm.negotiaId = this.form.negotiaid
        const {
          date: { tlist, totalRecord },
        } = await negotiateRecord(this.queryForm)
        this.list = tlist.map((i) => {
          return {
            ...i,
            username: i.wFTPR,
            realname: i.lRR,
          }
        })
        console.warn('this.list ', this.list)
        this.total = totalRecord
        this.listLoading = false
      },
      // 增加一个空行, 用于录入或显示第一行
      addLine() {
        if (!this.form.negotiaid) {
          return this.$message({
            type: 'error',
            message: '请先保存表单',
          })
        }
        this.$refs['lowerEdit'].showModal({
          negotiaid: this.form.negotiaid,
        })
      },
      // 删除指定行
      async deleteRow(id) {
        const res = await legalAttorneyDelete({ id })
        this.fetchLawerList()
      },
      /**
       * @description: 获取律师列表信息
       * @return {*}
       */
      async fetchLawerList() {
        if (!this.form.negotiaid) return
        const res = await legalAttorney({ negotiaid: this.form.negotiaid })
      },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      },
      /**
       * @description: 流程提交
       * @return {*}
       */
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
