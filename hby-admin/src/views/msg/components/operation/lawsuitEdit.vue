<template>
  <div>
    <el-row :gutter="15">
      <el-form ref="form" label-width="150px" :model="form" :rules="rules">
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="案号">
            <el-input
              v-model="form.proceedno"
              clearable
              :disabled="isReadonly"
              placeholder="请输入案号"
              :style="{ width: '100%', height: '29px' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="诉讼阶段">
            <el-input
              v-model="form.porceedstage"
              clearable
              :disabled="isReadonly"
              placeholder="请输入诉讼阶段"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col> -->
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="纠纷阶段" prop="porceedstage">
            <span v-if="show">
              {{
                form.porceedstage == '一审审理中'
                  ? '一审审理中'
                  : form.porceedstage == '一审已判决'
                  ? '一审已判决'
                  : form.porceedstage == '二审审理中'
                  ? '二审审理中'
                  : form.porceedstage == '二审已判决'
                  ? '二审已判决'
                  : form.porceedstage == '再审审理中'
                  ? '再审审理中'
                  : form.porceedstage == '再审已判决'
                  ? '再审已判决'
                  : form.porceedstage == '强制执行'
                  ? '强制执行'
                  : form.porceedstage == '破产重整'
                  ? '破产重整'
                  : form.porceedstage == '仲裁审理中'
                  ? '仲裁审理中'
                  : form.porceedstage == '已裁决'
                  ? '已裁决'
                  : form.porceedstage == '已结案'
                  ? '已结案'
                  : '诉前调解'
              }}
            </span>
            <span v-else>
              <el-select v-model="form.porceedstage" :style="{ width: '100%' }">
                <el-option value="诉前调解" label="诉前调解"></el-option>
                <el-option value="一审审理中" label="一审审理中"></el-option>
                <el-option value="一审已判决" label="一审已判决"></el-option>
                <el-option value="二审审理中" label="二审审理中"></el-option>
                <el-option value="二审已判决" label="二审已判决"></el-option>
                <el-option value="再审审理中" label="再审审理中"></el-option>
                <el-option value="再审已判决" label="再审已判决"></el-option>
                <el-option value="强制执行" label="强制执行"></el-option>
                <el-option value="破产重整" label="破产重整"></el-option>
                <el-option value="仲裁审理中" label="仲裁审理中"></el-option>
                <el-option value="已裁决" label="已裁决"></el-option>
                <el-option value="已结案" label="已结案"></el-option>
              </el-select>
            </span>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="审理法院">
            <el-input
              v-model="form.court"
              clearable
              :disabled="isReadonly"
              placeholder="请输入审理法院"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="承办法官">
            <el-input
              v-model="form.presedingjudge"
              clearable
              :disabled="isReadonly"
              placeholder="请输入承办法官"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="法院联系人">
            <el-input
              v-model="form.courtlink"
              clearable
              :disabled="isReadonly"
              placeholder="请输入法院联系人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="法院联系方式">
            <el-input
              v-model="form.courtcontact"
              clearable
              :disabled="isReadonly"
              placeholder="请输入法院联系方式"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="立案时间">
            <el-date-picker
              v-model="form.filingtime"
              clearable
              :disabled="isReadonly"
              placeholder="请输入立案时间"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="开庭时间">
            <el-date-picker
              v-model="form.openingtime"
              clearable
              :disabled="isReadonly"
              placeholder="请输入开庭时间"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="缴费提醒时间">
            <el-date-picker
              v-model="form.paymentremindtime"
              clearable
              :disabled="isReadonly"
              placeholder="请输入缴费提醒时间"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="收到判决时间">
            <el-date-picker
              v-model="form.judgetiem"
              clearable
              :disabled="isReadonly"
              placeholder="请输入收到判决时间"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>

        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="录入人">
            <el-input
              v-model="form.username"
              clearable
              placeholder="请输入录入人"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="录入时间">
            <el-input
              v-model="form.createtime"
              clearable
              disabled
              placeholder="请输入录入时间"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="发现的问题">
            <el-input
              v-model="form.casepromotion"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入发现的问题"
              :style="{ width: '100%' }"
              :disabled="isReadonly"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="存在的困难">
            <el-input
              v-model="form.existingdifficulties"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入存在的困难"
              :style="{ width: '100%' }"
              :disabled="isReadonly"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="推进的措施">
            <el-input
              v-model="form.measurespromote"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入推进的措施"
              :style="{ width: '100%' }"
              :disabled="isReadonly"
              type="textarea"
            />
          </el-form-item>
          <div style="text-align: right" v-if="!isReadonly">
            <el-button type="primary" @click="save">保 存</el-button>
          </div>
        </el-col>

        <el-col :span="24">
          <el-divider>我方代理人</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="!isReadonly">
            <el-button @click="addLine" type="success">新增</el-button>
          </div>
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
            <el-table-column align="center" prop="lawFirm" label="律所" />
            <el-table-column
              align="center"
              prop="attorneyphont"
              label="操作"
              v-if="!isReadonly"
            >
              <template #default="{ row }">
                <el-button @click="deleteRow(row.id)" type="text" size="small">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <el-col :span="24">
          <el-divider>上传裁决文书</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!isReadonly && !$store.state.work.processMobile"
          >
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleSuccess"
              :file-list="fileList"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableDataFile">
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
                  @click="handleDownload(scope.row)"
                >
                  下载
                </el-button>
                <el-button type="text" @click="handlePreviewFile(scope.row)">
                  预览
                </el-button>
                <el-button
                  v-if="show"
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
    <div style="text-align: right; margin-top: 10px" v-if="!isReadonly">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
    </div>
    <executor-options ref="executor" @selected="handleSelected" />
    <!-- 我方代理人选择modal -->
    <lower-edit ref="lowerEdit" @fetch-lawer-list="fetchLawerList" />

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
  const { baseURL } = require('@/config')
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import store from '@/store'
  import {
    legalAttorney,
    legalAttorneyDelete,
    proceedingsRecordDetail,
    proceedingsRecordModify,
    proceedingsRecordSave,
    deleteCJSFile,
    getListatt,
  } from '@/api/fwgl/legal'
  import { downloads } from '@/api/fwgl/zzxx'
  import LowerEdit from '@/views/fwgl/legal/components/lowerEdit'
  import ExecutorOptions from '@/views/fwgl/legal/components/options/executor'
  import * as dayjs from 'dayjs'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { changeFormSizeStyleFunc } from '@/utils/processMobile'

  export default {
    name: 'TemplateEdit',
    components: {
      ExecutorOptions,
      LowerEdit,
      Resubmit,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/contract/uploadFileAttInfo',
        headers: {
          token: store.getters['user/token'],
        },
        show: false,
        form: {
          zxstaffid: undefined,
          proceedid: undefined,
          proceedno: undefined,
          porceedstage: undefined,
          court: undefined,
          presedingjudge: undefined,
          courtlink: undefined,
          courtcontact: undefined,
          filingtime: undefined,
          openingtime: undefined,
          paymentremindtime: undefined,
          judgetiem: undefined,
          isexternallawyer: 1,
          lawyearword: undefined,
          lawyearname: undefined,
          lawyearlink: undefined,
          negotiator: undefined,
          negotiatorlink: undefined,
          casepromotion: undefined,
          existingdifficulties: undefined,
          measurespromote: undefined,
          createtime: undefined,
          username: undefined,
          realname: undefined,
          ourcontractperson: undefined,
        },
        rules: {},
        title: '',
        dialogFormVisible: false,
        uploadlist: [],
        dllsData: [],
        isReadonly: true, // 是否详情禁用
        tableDataFile: [],
        fileList: [],
        fileIds: [],
        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
      }
    },
    created() {
      const yy = new Date().getFullYear()
      const mm =
        new Date().getMonth() + 1 < 10
          ? '0' + new Date().getMonth()
          : new Date().getMonth() + 1
      const dd =
        new Date().getDate() < 10
          ? '0' + new Date().getDate()
          : new Date().getDate()
      this.form.createtime = yy + '-' + mm + '-' + dd
    },
    methods: {
      async getDetail(id) {
        const res = await proceedingsRecordDetail({ id })
        if (res) {
          Object.keys(this.form).forEach((key) => {
            if (
              key === 'openingtime' ||
              key === 'paymentremindtime' ||
              key === 'judgetiem' ||
              key === 'filingtime'
            ) {
              this.form[key] = res[key]
                ? dayjs(new Date(res[key])).format('YYYY-MM-DD')
                : undefined
            } else if (key === 'createtime') {
              this.form[key] = res[key]
                ? dayjs(new Date(res[key])).format('YYYY-MM-DD HH:mm:ss')
                : undefined
            } else {
              this.form[key] = res[key]
            }
          })
        }
      },
      showEdit(row) {
        //
        // this.$set(this.form, 'litigationId', row.litigationId)
        this.form.litigationId = row.proceedid
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.form.username = userInfo.realname
        if (!row.proceedid) {
          this.title = '添加'
          this.form.createtime = dayjs(new Date()).format('YYYY-MM-DD HH:mm:ss')
          this.isReadonly = false
          this.show = true
        } else if (row.type === 'edit') {
          this.title = '编辑'
          this.getDetail(row.proceedid)
          this.form.proceedid = row.proceedid
          this.form.username = row.realname
          this.isReadonly = false
          this.show = true
          this.formId = row.formId
          this.flowtaskinfoflowid = row.flowtaskinfoflowid
          this.ymFromId = row.ymFromId
          this.status = row.status
          this.fetchLawerList()
        } else if (row.type === 'detail') {
          this.show = false
          this.getDetail(row.proceedid)
          this.form.proceedid = row.proceedid
          this.title = '详情'
          this.isReadonly = true
          this.fetchLawerList()
        }

        this.fromId = row.formId
        this.fromIdcopy = row.formId
        this.flowtaskinfoflowid = row.flowtaskinfoflowid
        this.ymFromId = row.ymFromId

        this.dialogFormVisible = true

        if (this.$store.state.work.processMobile) {
          this.$nextTick(() => {
            changeFormSizeStyleFunc()
          })
        }
      },
      close() {
        this.$refs['form'].resetFields()

        this.$bus.$emit('updateMsg', 0)
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      nallChange() {
        this.$set(this.form, 'negotiatorname', undefined)
        this.$set(this.form, 'negotiator', undefined)
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.form.attids = this.fileIds.toString() || ''
            if (!this.form.proceedid) {
              const { data, msg } = await proceedingsRecordSave(this.form)
              if (data) this.form.proceedid = data
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            } else if (this.form.proceedid) {
              const { msg } = await proceedingsRecordModify(this.form)
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            }
            // this.$emit('fetch-data')
            // this.$emit('editadd')
            // this.close()
          }
        })
      },
      handleSelected(val) {
        // this.form.zxstaffid = val.staffid;
        // this.form.ourcontractperson = val.realname;
        this.$set(this.form, 'negotiatorname', val.realname)
        this.$set(this.form, 'negotiator', val.staffid)
      },
      // 增加一个空行, 用于录入或显示第一行
      addLine() {
        if (!this.form.proceedid) {
          return this.$message({
            type: 'error',
            message: '请先保存表单',
          })
        }
        this.$refs['lowerEdit'].showModal({
          lawsuitid: this.form.proceedid,
        })
      },
      // 删除指定行
      async deleteRow(id) {
        const res = await legalAttorneyDelete({ id })
        this.fetchLawerList()
      },
      async fetchLawerList() {
        if (!this.form.proceedid) return
        const res = await legalAttorney({ lawsuitid: this.form.proceedid })
        const fileRes = await getListatt({
          litigationId: this.form.proceedid,
        })
        this.dllsData = res.data || []
        this.tableDataFile = fileRes.data || []
      },
      handleSuccess(response, file, fileList) {
        if (file.response.code == '1') {
          file.createPerson = JSON.parse(
            localStorage.getItem('userInfo')
          ).realname
          let arr = file.response.data
          const arr1 = {
            name: arr.attname,
            size: arr.attsize,
            createPerson: arr.uploader,
            attid: arr.attid,
          }
          this.tableDataFile.push(arr1)

          this.fileList = fileList
          let fileArr = []
          this.fileList.forEach((item) => {
            fileArr.push(item.response.data.attid)
          })
          this.fileIds = [...this.fileIds, ...fileArr]
          this.$baseMessage(file.response.msg, 'success')
        } else {
          this.$baseMessage(file.response.msg, 'error')
        }
      },
      async handleDownload(row) {
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
      handleDeleteFile(index, row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deleteCJSFile({ attid: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            this.fileIds.splice(index, 1)
            this.tableDataFile.splice(index, 1)
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
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
