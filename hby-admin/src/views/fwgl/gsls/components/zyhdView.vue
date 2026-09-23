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
      <el-form ref="ruleForm" label-width="100px" :model="formData" size="mini">
        <el-col :span="12">
          <el-form-item
            :label="formData.activityCategory == '5' ? '项目名称' : '活动名称'"
            prop="activityName"
            v-if="formData.activityCategory !== '2'"
          >
            <el-input
              v-model="formData.activityName"
              clearable
              placeholder="请输入名称"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="活动类别" prop="activityCategory">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.activityCategory"
              placeholder="活动类别"
              disabled
            >
              <!-- <el-option label="合同审查" value="0" />
              <el-option label="诉讼代理" value="1" /> -->
              <el-option label="文章发表" value="2" />
              <el-option label="法律培训" value="3" />
              <el-option label="法律审核" value="4" />
              <el-option label="法律尽调" value="5" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="活动时间"
            prop="activityTime"
            v-if="
              formData.activityCategory !== '2' &&
              formData.activityCategory !== '5'
            "
          >
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.activityTime"
              placeholder="活动时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>

        <!--  序号、合同编号、金额、审核时间、审核人。 -->
        <div v-if="formData.activityCategory == '0'">
          <el-col :span="12">
            <el-form-item label="合同编号" prop="contractCode">
              <el-input
                v-model="formData.contractCode"
                clearable
                placeholder="请输入合同编号"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="金额" prop="money">
              <el-input
                v-model="formData.money"
                clearable
                placeholder="请输入金额"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核时间" prop="audittime">
              <el-date-picker
                :style="{ width: '100%' }"
                v-model="formData.audittime"
                placeholder="审核时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核人" prop="auditPerson">
              <el-input
                v-model="formData.auditPerson"
                clearable
                placeholder="请输入审核人"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
        </div>
        <!-- 名称、案号、原告、被告、标的金额、立案日期、审理阶段、代理人 -->
        <div v-if="formData.activityCategory == '1'">
          <el-col :span="12">
            <el-form-item label="名称" prop="lawsuitAgentName">
              <el-input
                v-model="formData.lawsuitAgentName"
                clearable
                placeholder="请输入名称"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="案号" prop="lawsuitAgentCaseNumber">
              <el-input
                v-model="formData.lawsuitAgentCaseNumber"
                clearable
                placeholder="请输入案号"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="原告" prop="lawsuitAgentPlaintiff">
              <el-input
                v-model="formData.lawsuitAgentPlaintiff"
                clearable
                placeholder="请输入原告"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="被告" prop="lawsuitAgentDefendant">
              <el-input
                v-model="formData.lawsuitAgentDefendant"
                clearable
                placeholder="请输入被告"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标的金额" prop="lawsuitAgentMoney">
              <el-input
                v-model="formData.lawsuitAgentMoney"
                clearable
                placeholder="请输入标的金额"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="立案日期" prop="lawsuitAgentTime">
              <el-date-picker
                :style="{ width: '100%' }"
                v-model="formData.lawsuitAgentTime"
                placeholder="立案日期"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审理阶段" prop="lawsuitAgentTrial">
              <el-input
                v-model="formData.lawsuitAgentTrial"
                clearable
                placeholder="请输入审理阶段"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="代理人" prop="lawsuitAgentPersonnel">
              <el-input
                v-model="formData.lawsuitAgentPersonnel"
                clearable
                placeholder="请输入代理人"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
        </div>
        <!-- 题目、发表载体、发表时间、作者 -->
        <div v-if="formData.activityCategory == '2'">
          <el-col :span="12">
            <el-form-item label="题目" prop="activityName">
              <el-input
                v-model="formData.activityName"
                clearable
                placeholder="请输入题目"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="发表载体"
              prop="articlePublishPublishingCarrier"
            >
              <el-input
                v-model="formData.articlePublishPublishingCarrier"
                clearable
                placeholder="请输入发表载体"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发表时间" prop="activityTime">
              <el-date-picker
                :style="{ width: '100%' }"
                v-model="formData.activityTime"
                placeholder="发表时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="作者" prop="articlePublishAuthor">
              <el-input
                v-model="formData.articlePublishAuthor"
                clearable
                placeholder="请输入作者"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
        </div>
        <!-- 培训主题、培训内容、培训时间、参训人员 -->
        <div v-if="formData.activityCategory == '3'">
          <el-col :span="12">
            <el-form-item label="培训主题" prop="legalTrainingTopic">
              <el-input
                v-model="formData.legalTrainingTopic"
                clearable
                placeholder="请输入培训主题"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="培训内容" prop="legalTrainingContent">
              <el-input
                v-model="formData.legalTrainingContent"
                clearable
                placeholder="请输入培训内容"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="培训时间" prop="legalAuditTime">
              <el-date-picker
                :style="{ width: '100%' }"
                v-model="formData.legalAuditTime"
                placeholder="培训时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参训人员" prop="creatorName">
              <el-input
                v-model="formData.creatorName"
                clearable
                placeholder="请输入参训人员"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
        </div>
        <!-- 项目名称、项目概述、审核时间、审核人 -->
        <div v-if="formData.activityCategory == '4'">
          <el-col :span="12">
            <el-form-item label="项目名称" prop="legalAuditName">
              <el-input
                v-model="formData.legalAuditName"
                clearable
                placeholder="请输入项目名称"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目概述" prop="legalAuditSummary">
              <el-input
                v-model="formData.legalAuditSummary"
                clearable
                placeholder="请输入项目概述"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核时间" prop="legalAuditTime">
              <el-date-picker
                :style="{ width: '100%' }"
                v-model="formData.legalAuditTime"
                placeholder="审核时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核人" prop="legalAuditPersonnel">
              <el-input
                v-model="formData.legalAuditPersonnel"
                clearable
                placeholder="请输入审核人"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
        </div>
        <!-- 尽调单位、尽调内容、尽调时间、尽调人 -->
        <div v-if="formData.activityCategory == '5'">
          <el-col :span="12">
            <el-form-item label="尽调单位" prop="legalAdjustmentWorkUnit">
              <el-input
                v-model="formData.legalAdjustmentWorkUnit"
                clearable
                placeholder="请输入尽调单位"
                :style="{ width: '100%' }"
                disabled
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="尽调内容" prop="legalAdjustmentContent">
              <el-input
                v-model="formData.legalAdjustmentContent"
                clearable
                placeholder="请输入尽调内容"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="尽调时间" prop="activityTime">
              <el-date-picker
                :style="{ width: '100%' }"
                v-model="formData.activityTime"
                placeholder="尽调时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd HH:mm:ss"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="尽调人" prop="legalAdjustmentPersonnel">
              <el-input
                v-model="formData.legalAdjustmentPersonnel"
                clearable
                placeholder="请输入尽调人"
                :style="{ width: '100%' }"
                :disabled="!footer"
              />
            </el-form-item>
          </el-col> -->
        </div>
        <el-col :span="12">
          <el-form-item
            :label="formData.activityCategory == '5' ? '尽调人' : '创建人'"
            prop="creatorName"
            v-if="formData.activityCategory !== '3'"
          >
            <el-input
              v-model="formData.creatorName"
              clearable
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="创建时间" prop="createdTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.createdTime"
              placeholder="创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleAvatarSuccess"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="fileList">
            <el-table-column align="center" label="附件名称" prop="fileName" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="fileSize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
    <template #footer v-if="footer">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { addZXHD, getZXHDDefaultInfo } from '@/api/fwgl/gsls'
  import { deleteFile, uploadApi, download } from '@/api/fwgl/zzxx'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import { baseURL } from '@/config'
  import store from '@/store'
  import { formatDate } from '@/utils'
  const token = store.getters['user/token']
  export default {
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          activityCategory: '0',
          activityName: '',
          activityTime: '',
          auditPerson: '',
          audittime: '',
          contractCode: '',
          fileIds: '',
          money: 0,
          createdTime: '',
          creatorName: '',
          fileIds: '',
        },
        footer: true,
        dialogFormVisible: false,
        title: '新增',
        tableData: [],
        fileList: [],
        baseApi: baseURL,
        api: uploadApi,
        headers: { token: token },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      // 注释掉原本的复杂表单逻辑，改为通过emit事件调用zyhdDetail组件
      showEdit(title, row, type) {
        // 对于新增操作，仍然使用原有逻辑
        if (title === 'add') {
          this.dialogFormVisible = true
          this.formData = {
            activityCategory: '0',
            activityName: '',
            activityTime: '',
            auditPerson: '',
            audittime: '',
            contractCode: '',
            fileIds: '',
            money: 0,
            createdTime: '',
            creatorName: '',
            fileIds: '',
          }
          this.fileList = []
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.creatorName = userInfo.realname
          this.formData.createdTime = formatDate(new Date())

          if (type == '文章发表') {
            this.formData.activityCategory = '2'
          } else if (type == '法律培训') {
            this.formData.activityCategory = '3'
          } else if (type == '法律尽调') {
            this.formData.activityCategory = '5'
            const info = JSON.parse(localStorage.getItem('userInfo'))
            this.formData.legalAdjustmentWorkUnit = info.linkOrg.orgname
            this.$set(
              this.formData,
              'legalAdjustmentWorkUnit',
              info.linkOrg.orgname
            )
          }
        } else {
          // 对于详情和编辑操作，emit事件让父组件调用zyhdDetail
          this.$emit('showDetail', title, row)
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {
          type: '合同审查',
        }
        this.dialogFormVisible = false

        this.footer = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      add() {
        const aa = []
        this.fileList.forEach((e) => {
          aa.push(e.fileId)
        })
        this.formData.fileIds = aa.toString()
        this.$emit('add', this.formData)
        addZXHD(this.formData).then((res) => {
          if (res.msg == '成功') {
            this.dialogFormVisible = false
            this.$emit('fetchData')
          }
        })
      },
      handleAvatarSuccess(res) {
        if (res.code == 200) {
          this.fileList.push(res.data.fileIds[0])
        }
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            this.fileList.splice(
              this.fileList.findIndex((x) => x.fileId == row.fileId),
              1
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */      
      async handleDown(row) {
        const data = await download({ fileId: row.fileId })
        let filename = row.fileName
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
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */      
      async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          fileId: row.fileId,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
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
