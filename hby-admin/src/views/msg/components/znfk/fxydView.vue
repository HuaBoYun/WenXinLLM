<template>
  <div>
    <el-row :gutter="15" v-loading="loading">
      <el-form
        ref="elForm"
        label-width="150px"
        :model="formData"
        :rules="rules"
        size="medium"
      >
        <el-col :span="12">
          <el-form-item label="风险编号" prop="risknumber">
            <el-input
              v-model="formData.risknumber"
              clearable
              placeholder="请输入风险编号"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险名称" prop="riskname">
            <el-input
              v-model="formData.riskname"
              clearable
              placeholder="请输入风险名称"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="风险描述" prop="riskdes">
            <el-input
              v-model="formData.riskdes"
              clearable
              placeholder="请输入风险描述"
              :style="{ width: '100%' }"
              type="textarea"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="归属单位" prop="ssjgName">
            <el-input
              v-model="formData.ssjgName"
              clearable
              placeholder="请输入归属单位"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险等级" prop="level">
            <el-select
              v-model="formData.level"
              clearable
              placeholder="请选择风险等级"
              :style="{ width: '100%' }"
              ref="levels"
              @change="chageTextColor($event, 'levels')"
              disabled
            >
              <el-option
                v-for="(item, index) in field105Options"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险应对策略类型" prop="copingPlot">
            <el-select
              v-model="formData.copingPlot"
              clearable
              placeholder="请选择风险应对策略类型"
              :style="{ width: '100%' }"
              :disabled="disabled"
            >
              <el-option
                v-for="(item, index) in field106Options"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险期望值" prop="riskHopeValue">
            <el-input
              v-model="formData.riskHopeValue"
              clearable
              placeholder="请输入风险期望值"
              :style="{ width: '100%' }"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险应对负责人" prop="ydusername">
            <el-input
              placeholder="请选择风险应对负责人"
              v-model="formData.ydusername"
              clearable=""
              disabled
            >
              <template slot="append">
                <el-button
                  type="primary"
                  @click="handleShowUser"
                  :disabled="disabled"
                >
                  选 择
                </el-button>
              </template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="应对方案" prop="yddes">
            <el-input
              v-model="formData.yddes"
              :autosize="{ minRows: 4, maxRows: 8 }"
              placeholder="请输入应对方案"
              :style="{ width: '100%' }"
              type="textarea"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>一体化管控措施</el-divider>
        </el-col>
        <el-col :span="24" style="margin-bottom: 20px">
          <div
            style="text-align: right; margin-bottom: 5px; margin-right: 10px"
            v-if="!disabled"
          >
            <el-button type="success" @click="hanAddControl">新增</el-button>
          </div>
          <el-table :data="controlList">
            <el-table-column
              align="center"
              label="一体化管控措施编号"
              prop="controlnumber"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                <el-button type="text" @click="showControlDetail(row)">
                  {{ row.controlnumber }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="一体化控制目标"
              prop="controldes"
              show-overflow-tooltip
            />
            <el-table-column align="center" label="控制措施" prop="conkzcs">
              <template #default="{ row }">
                <div
                  v-html="row.conkzcs ? row.conkzcs.replace(/\n/g, '<br>') : ''"
                ></div>
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEditControl(row)"
                  v-if="!disabled"
                >
                  修改
                </el-button>
                <el-button
                  type="text"
                  @click="handleDeleteControl(row)"
                  v-if="!disabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>

        <el-col :span="24">
          <UEditor
            ref="ueditor"
            v-model="formData.content"
            :height="300"
            :templates="templates"
            template="fxyd"
            :disabled="disabled"
          />
        </el-col>

        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="!disabled">
            <el-upload
              :action="baseApi + api"
              :show-file-list="false"
              :data="{
                moduleType: 'FXYD',
                moduleId: this.riskcopingid,
              }"
              :on-success="handleSuccess"
              :on-error="handleErr"
              :headers="{
                'X-Requested-With': 'XMLHttpRequest',
                token: token,
              }"
            >
              <el-button type="success" :loading="uploadLoading">
                上传
              </el-button>
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
              <template #default="{ row }">
                <el-button type="text" @click="handleDownload(row)">
                  下载
                </el-button>
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="!disabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" style="text-align: right" v-if="!disabled">
      <el-button @click="save" type="primary">确定</el-button>
      <el-button @click="ymsubmit" type="primary" :disable="btnLoading">
        提交
      </el-button>
    </div>
    <CompanySelectUserByTree
      ref="userTreeRef"
      @selected="handleExecutorSelected"
    />
    <CompanySelectUserByTree
      ref="userTreeRef2"
      @selected="handleExecutorSelected2"
    />
    <controlMeasures ref="controlMeasures" @fetch-data="fetchData" />

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
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import { riPlanSInfo } from '@/api/systemLog'
  import {
    fxydSave,
    controlDelete,
    updateControlResponseplan,
  } from '@/api/risk'
  import { deleteFieldById, downFieldById } from '@/api/risk/riskEvents'
  const { baseURL } = require('@/config')
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  // import UEditor from '@/components/UEditor'
  import controlMeasures from '@/views/risk/treatment/controlMeasures.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  export default {
    name: 'TreatmentEdit',
    components: {
      CompanySelectUserByTree,
      CandidateUserSelect,
      UEditor: () => import('@/components/UEditor'),
      controlMeasures,
      Resubmit,
    },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        controlList: [],
        tableDataFile: [],
        fileIdList: [],
        templates: [],
        removeIds: [],
        baseApi: baseURL,
        riskcopingid: '',
        loading: false,
        uploadLoading: false,
        disabled: false,
        api: '/riskcontrol/attachment/uploadFileAttInfo',
        token: this.$store.getters['user/token'],
        title: '',
        formData: {
          risknumber: '',
          riskname: '',
          riskdes: '',
          ssjgName: '',
          level: '0',
          copingPlot: '',
          riskHopeValue: '',
          ydusername: '',
          yddes: '',
          controlnumber: '',
          controlmanager: '',
          controlfrequency: '',
          controltype: '',
          controlmethod: '',
          keycontrol: '',
          effective: '',
          controltest: '',
          financialreportidentify: '',
          controldes: '',
          conkzcs: '',
        },
        color: ['', '#52FFB7', '#33D73B', '#FFB500', '#FF7F00', '#E92129'],
        rules: {
          copingPlot: [
            {
              required: true,
              message: '请选择风险应对策略类型',
              trigger: 'change',
            },
          ],
          riskHopeValue: [
            {
              required: true,
              message: '请输入风险期望值',
              trigger: 'blur',
            },
          ],
          ydusername: [
            {
              required: true,
              message: '请选择风险应对负责人',
              trigger: 'blur',
            },
          ],
        },
        field105Options: [
          {
            label: '未评估',
            value: '0',
          },
          {
            label: '很低',
            value: '1',
          },
          {
            label: '较低',
            value: '2',
          },
          {
            label: '中等',
            value: '3',
          },
          {
            label: '较高',
            value: '4',
          },
          {
            label: '很高',
            value: '5',
          },
        ],
        field106Options: [
          {
            label: '承担',
            value: '1',
          },
          {
            label: '转移',
            value: '2',
          },
          {
            label: '规避',
            value: '3',
          },
          {
            label: '降低（风险控制）',
            value: '4',
          },
        ],
        controltypeOptions: [
          {
            label: '预防性控制',
            value: '1',
          },
          {
            label: '发现性控制',
            value: '2',
          },
          {
            label: '纠正性控制',
            value: '3',
          },
        ],
        controlmethodOptions: [
          {
            label: '手工',
            value: '1',
          },
          {
            label: '自动',
            value: '2',
          },
          {
            label: '依赖手工的自动化',
            value: '3',
          },
        ],
        financialreportidentifyOptions: [
          {
            label: '存在与发生',
            value: '1',
          },
          {
            label: '完整性',
            value: '2',
          },
          {
            label: '权利与义务',
            value: '3',
          },
          {
            label: '估计与平摊',
            value: '4',
          },
          {
            label: '表达与披露',
            value: '5',
          },
        ],
        //提交
        riskid: '',
        status: 0,
        jurisdictionCode: 0,
        btnLoading: false,
        disabled: false,
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
        footer: true,
        formDisabled: false,
      }
    },
    computed: {},
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
    updated() {
      this.$nextTick(() => {
        this.chageTextColor(this.formData.level, 'levels')
      })
    },
    methods: {
      /**
       * @description: 预览
       * @return {*}
       */
      async handlePreviewFile(row) {
        console.log('row', row)
        const { data } = await getPrivewAttInfo({
          //此接口通用
          attId: row.attid,
          attType: 2,
        })

        window.open(
          data.previewurl +
            '?url=' +
            encodeURIComponent(Base64.encode(data.ftpUrl))
        )
      },
      /**
       * @description: table下拉框 颜色初始化
       * @return {*}
       */
      chageTextColor($event, selectedRef) {
        const color = this.color[$event]
        // 改变下拉框颜色值
        this.$refs[selectedRef].$el.children[0].children[0].style.color =
          '' + color + ''
      },
      /**
       * @description: 打开选人
       * @return {*}
       */
      handleShowUser() {
        this.$refs['userTreeRef'].show()
      },
      /**
       * @description: 打开选人
       * @return {*}
       */
      handleShowUser2() {
        this.$refs['userTreeRef2'].show()
      },
      // 风险应对责任人
      handleExecutorSelected(e) {
        console.log('ss', e)
        this.$set(this.formData, 'ydusername', e.realname)
        this.$set(this.formData, 'userId', e.staffid)
      },
      /**
       * @description: 选人回调
       * @return {*}
       */
      handleExecutorSelected2(e) {
        console.log('ss', e)
        this.$set(this.formData, 'controlmanager', e.realname)
        this.$set(this.formData, 'reporterid', e.staffid)
      },
      /**
       * @description: 保存
       * @return {*}
       */
      async save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const { attachments, ...other } = this.formData
            if (!this.formData.content) {
              this.$message.error('请填写正文内容!')
              return
            }
            const info = {
              ...other,
              reporteds: this.fileIdList.toString(),
              removeReporteds: this.removeIds.toString(),
              riskcopingid: this.riskcopingid,
            }
            console.log(info)
            const res = await fxydSave(info)
            if (res.code == 200) {
              await this.fetchData()
              this.$baseMessage('成功', 'success')
              this.$emit('fetch-data')
            }
          }
        })
      },

      /**
       * @description: 初始化
       * @return {*}
       */
      // async showEdit(row, type, disabled) {
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        nextNodeName
      ) {
        console.log('🚀 ~ //showEdit ~ formId:', formId)
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
          this.disabled = true
        } else if (title == 'add') {
          this.title = '新增'
        }
        if (formId) {
          this.riskcopingid = formId
          await this.fetchData()
        }
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
      },
      /**
       * @description: 获取数据
       * @return {*}
       */
      async fetchData() {
        this.loading = true
        await riPlanSInfo({
          riskcopingid: this.riskcopingid,
        })
          .then(async (res) => {
            this.riskcopingid =
              res.data.copings && res.data.copings.riskcopingid
            Object.assign(this.formData, {
              ...(res.data.controls ? res.data.controls[0] : {}),
              content: res.data.copings && res.data.copings.content,
              status: res.data.risk.status,
              risknumber: res.data.risk.risknumber,
              riskname: res.data.risk.riskname,
              riskdes: res.data.risk.riskdes,
              ssjgName: res.data.risk.ssjgName,
              level: res.data.risk.level ? res.data.risk.level : '0',
              copingPlot:
                res.data.copings && res.data.copings.copingplot
                  ? res.data.copings.copingplot
                  : '',
              riskHopeValue: res.data.copings && res.data.copings.riskhopevalue,
              ydusername: res.data.userName,
              userId: res.data.copings && res.data.copings.copinghead,
              yddes: res.data.copings && res.data.copings.yddes,
              riskid: res.data.risk.riskid,
              copingId: res.data.copings && res.data.copings.riskcopingid,
              riskcopingid: res.data.copings && res.data.copings.riskcopingid,
              attachments: res.data.attachments || [],
            })
            this.tableDataFile = res.data.attachments || []
            this.fileIdList = []
            if (res.data.attachments && res.data.attachments.length != 0) {
              res.data.attachments.forEach((item) => {
                this.fileIdList.push(item.attid)
              })
            }

            this.removeIds = []
            this.controlList = res.data.controls || []
          })
          .finally(() => {
            this.loading = false
          })
      },
      /**
       * @description: 关闭页面，初始化值
       * @return {*}
       */
      close() {
        this.formData = {
          risknumber: '',
          riskname: '',
          riskdes: '',
          ssjgName: '',
          level: '0',
          copingPlot: '',
          riskHopeValue: '',
          ydusername: '',
          yddes: '',
          controlnumber: '',
          controlmanager: '',
          controlfrequency: '',
          controltype: '',
          controlmethod: '',
          keycontrol: '',
          effective: '',
          controltest: '',
          financialreportidentify: '',
          controldes: '',
          conkzcs: '',
        }
        this.tableDataFile = []
        this.riskcopingid = ''
        this.$bus.$emit('updateMsg', 0)
      },
      /**
       * @description:下载附件
       * @return {*}
       */
      async handleDownload(row) {
        console.log('downlaod', row)
        const res = await downFieldById({ id: row.attid })
        console.log(res)
        if (!res) return
        let filename = row.attname
        let blob = new Blob([res]) //res即为blob数据，请注意自己的数据形式
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
       * @description: 删除附件
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
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
      /**
       * @description: 附件上传前，校验
       * @return {*}
       */
      beforUpload() {
        if (!this.riskcopingid) {
          this.$baseMessage(
            '请先保存基本信息',
            'error',
            'vab-hey-message-error'
          )
          return false
        } else {
          this.uploadLoading = true
        }
      },
      /**
       * @description: 上传失败
       * @return {*}
       */
      handleErr() {
        this.uploadLoading = false
        this.$baseMessage('上传失败', 'error', 'vab-hey-message-error')
      },
      /**
       * @description: 上传成功
       * @return {*}
       */
      handleSuccess(e) {
        console.log(e)
        if (e.code === 200) {
          let attInfo = {}
          attInfo = e.data
          this.fileIdList.push(attInfo.attid)
          this.tableDataFile.push(attInfo)
          console.log(this.tableDataFile)
          this.$baseMessage('上传成功', 'success', 'vab-hey-message-success')
        } else {
          this.$baseMessage('上传失败', 'error', 'vab-hey-message-error')
        }
        this.uploadLoading = false
      },
      // 控制措施
      hanAddControl() {
        if (!this.riskcopingid) {
          this.$baseMessage(
            '请先保存基本信息',
            'error',
            'vab-hey-message-error'
          )
          return false
        }
        this.$refs.controlMeasures.showEdit(null, {
          riskcopingid: this.riskcopingid,
          riskHopeValue: this.formData.riskHopeValue,
          risknumber: this.formData.risknumber,
          type: 'fxyd',
          riskid: this.formData.riskid,
        })
      },
      /**
       * @description: 一体化 打开详情
       * @return {*}
       */
      showControlDetail(row) {
        this.$refs.controlMeasures.showEdit(
          row,
          {
            riskcopingid: this.riskcopingid,
            riskHopeValue: this.formData.riskHopeValue,
            risknumber: this.formData.risknumber,
            type: 'fxyd',
            riskid: this.formData.riskid,
          },
          true
        )
      },
      /**
       * @description: 一体化 打开编辑
       * @return {*}
       */
      handleEditControl(row) {
        this.$refs.controlMeasures.showEdit(row, {
          riskcopingid: this.riskcopingid,
          riskHopeValue: this.formData.riskHopeValue,
          type: 'fxyd',
          riskid: this.formData.riskid,
        })
      },
      /**
       * @description: 删除一体化管控措施
       * @return {*}
       */
      handleDeleteControl(row) {
        this.loading = true
        controlDelete(row.conmatid)
          .then((res) => {
            console.log('???', res)
            if (res.code == 200) {
              this.$baseMessage('成功', 'success')
              this.upControlData()
              this.fetchData()
            }
          })
          .finally(() => {
            this.loading = false
          })
      },
      //操作一体化管控以后的更新
      async upControlData() {
        const { data, code, msg } = await updateControlResponseplan({
          riskid: this.formData.riskid,
        })
      },
      async ymsubmit() {
        try {
          this.btnLoading = true
          this.$refs['elForm'].validate(async (valid) => {
            if (valid) {
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
    },
  }
</script>
<style></style>
