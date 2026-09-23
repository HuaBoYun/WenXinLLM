<template>
  <!-- 基本情况 edit -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="200px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
        style="display: flex; flex-wrap: wrap"
      >
        <el-col :span="12">
          <el-form-item label="合同编号" prop="htbh">
            <el-input
              v-model="formData.htbh"
              clearable
              placeholder="请输入合同编号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目类别" prop="fl">
            <el-input
              v-model="formData.fl"
              placeholder="请输入项目类别"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="验收项目名称" prop="ysxmmc">
            <el-input
              v-model="formData.ysxmmc"
              placeholder="请输入验收项目名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报单位" prop="tborgname">
            <el-input
              v-model="formData.tborgname"
              placeholder="请选择填报单位"
              :style="{ width: '75%' }"
              readonly
            />
            <el-button
              type="primary"
              size="mini"
              style="margin-left: 10px"
              @click="$refs.audiTree.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划文号" prop="jhwh">
            <el-input
              v-model="formData.jhwh"
              placeholder="请输入计划文号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划文号上传" prop="attname">
            <el-button
              type="text"
              style="
                width: 200px;
                text-align: left;
                overflow: hidden;
                text-overflow: ellipsis;
              "
              @click="handlePreviewFile({ attid: formData.attid })"
              :disabled="false"
            >
              {{ formData.attname || '请选择文件' }}
            </el-button>
            <el-button
              type="text"
              @click="
                handleDown({ attid: formData.attid, attname: formData.attname })
              "
              :disabled="false"
            >
              下载
            </el-button>
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="api"
              :headers="headers"
              :on-success="onSelectJhwhFile"
              style="display: inline-block; margin-left: 10px"
            >
              <el-button type="text" size="mini">上传</el-button>
            </el-upload>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="竣工计划" prop="jgjh">
            <el-input
              v-model="formData.jgjh"
              clearable
              placeholder="请输入竣工计划"
              readonly
              :style="{ width: '75%' }"
            />
            <el-button
              @click="selectPlan"
              style="margin-left: 12px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计划投资金额（万元）" prop="jhtzje">
            <el-input
              v-model="formData.jhtzje"
              placeholder="请输入计划投资"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="承包方式（EPC或施工承包）" prop="cbfs">
            <el-input
              v-model="formData.cbfs"
              placeholder="请输入承包方式（EPC或施工承包）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开工时间" prop="kjgsj">
            <el-date-picker
              v-model="formData.kjgsj"
              placeholder="请选择开工时间"
              type="month"
              format="yyyy-MM"
              value-format="yyyy-MM"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="竣工时间" prop="jgsj">
            <el-date-picker
              v-model="formData.jgsj"
              placeholder="请选择竣工时间"
              type="month"
              format="yyyy-MM"
              value-format="yyyy-MM"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目投产时间" prop="xmtcsj">
            <el-date-picker
              v-model="formData.xmtcsj"
              placeholder="请输入项目投产时间"
              type="month"
              format="yyyy-MM"
              value-format="yyyy-MM"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="试运行合格时间" prop="syxhgsj">
            <el-date-picker
              v-model="formData.syxhgsj"
              placeholder="请输入试运行合格时间"
              type="month"
              format="yyyy-MM"
              value-format="yyyy-MM"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其他专项验收情况" prop="qtzxysqk">
            <el-input
              v-model="formData.qtzxysqk"
              clearable
              placeholder="请输入其他专项验收情况"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资料归档时间" prop="zlgdsj">
            <el-date-picker
              v-model="formData.zlgdsj"
              placeholder="请输入资料归档时间"
              type="month"
              format="yyyy-MM"
              value-format="yyyy-MM"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="完成工程结算时间（二审）" prop="wcgcjssj">
            <el-date-picker
              v-model="formData.wcgcjssj"
              placeholder="请输入完成工程结算时间（二审）"
              type="month"
              format="yyyy-MM"
              value-format="yyyy-MM"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工程费用结算金额" prop="gcfyjsje">
            <el-input
              v-model="formData.gcfyjsje"
              placeholder="请输入工程费用结算金额"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否完成财务决算" prop="sfwccwjs">
            <el-radio-group v-model="formData.sfwccwjs">
              <el-radio label="1">是</el-radio>
              <el-radio label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="拟竣工验收时间" prop="njgyssj">
            <el-date-picker
              v-model="formData.njgyssj"
              placeholder="请输入拟竣工验收时间"
              type="month"
              format="yyyy-MM"
              value-format="yyyy-MM"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目负责人" prop="xmfzr">
            <el-input
              v-model="formData.xmfzr"
              clearable
              placeholder="请输入项目负责人"
              :style="{ width: '100%' }"
            />
            <!-- <el-button
              @click="projectManager"
              style="margin-left: 12px"
              type="primary"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="lxdh">
            <el-input
              v-model="formData.lxdh"
              placeholder="请输入联系电话"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目类型" prop="xmzttype">
            {{ formData.xmzttype }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目状态" prop="xmstatus">
            {{ formData.xmstatus == 1 ? '已做审计项目' : '未做审计项目' }}
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="创建人" prop="cjr">
            <el-input
              v-model="formData.cjr"
              disabled
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="cjsj">
            <el-date-picker
              v-model="formData.cjsj"
              placeholder="请输入创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>
            文件上传
            <span class="color-red">
              (需上传主管领导签字加盖二级机构公章扫描件及电子版文件)
            </span>
          </el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!formDisabled"
          >
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleSuccess"
            >
              <el-button type="success">上传</el-button>
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
              <div style="margin-right: 10px">
                <el-button type="success">点击上传</el-button>
              </div>
            </el-upload>
          </div>
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
                  v-if="!formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary" v-loading="loading">
        确定
      </el-button>
    </div>
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
      :multiple="false"
    ></project-manage>
    <!-- 单位选择 -->
    <SelectDepartment ref="audiTree" @submit="handleUnitSelected" />
    <SelectPlan ref="plan" @selected="handleSelectPlan" />
  </el-dialog>
</template>

<script>
  import projectManage from '@/components/selectPerson.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import {
    engineeringBasicInformationDetail,
    engineeringBasicInformationUpdate,
  } from '@/oapi/audit/plan'
  import { download, deleteReportFile } from '@/oapi/audit/report'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import store from '@/store'
  import SelectPlan from './selectGcjgysjhModal.vue'
  const { baseURL } = require('@/config')
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'jsxmjbqkEdit',
    inheritAttrs: false,
    components: {
      projectManage,
      SelectDepartment,
      SelectPlan,
    },
    data() {
      const validator = (_rule, value, callback) => {
        if (value !== '' && isNaN(value)) {
          callback(new Error('请输入数字值'))
        } else {
          callback()
        }
      }
      return {
        loading: false,
        baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          // 'Content-Type': 'application/json;charset=UTF-8',
          token: store.getters['user/token'],
        },
        fileList: [],
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        formData: {
          htbh: '', // 合同编号
          fl: '', // 项目类别
          ysxmmc: '', // 验收项目名称
          jhwh: '', // 计划文号
          jhtzje: '', // 计划投资金额（万元）
          cbfs: '', // 承包方式（EPC或施工承包）
          kjgsj: '', // 开竣工时间
          jgsj: '', // 开竣工时间
          syxhgsj: '', // 试运行合格时间
          qtzxysqk: '', // 其他专项验收情况
          zlgdsj: '', // 资料归档时间
          xmtcsj: '', // 项目投产时间
          wcgcjssj: '', // 完成工程结算时间（二审）
          gcfyjsje: '', // 工程费用结算金额
          sfwccwjs: '', // 是否完成财务决算 0 1
          xmfzr: '', // 项目负责人
          xmfzrId: '',
          attid: '', // 文号附件
          attname: '', // 文号附件
          tborgname: '', // 填报单位
          tborgid: '', // 填报单位
          lxdh: '', // 联系电话
          cjr: '', // 创建人
          cjsj: '', // 创建时间
          attIds: [], // 附件主键集合
          jgplanid: '', //竣工计划
          jgjh: '',
        },
        formDisabled: true,
        tableData: [],
        rules: {
          jhtzje: [
            {
              validator,
              trigger: 'blur',
            },
          ],
          gcfyjsje: [
            {
              validator,
              trigger: 'blur',
            },
          ],
          attname: [
            {
              required: true,
              message: '请上传文号',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        btnType: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      async showEdit(row, disabled, fl) {
        this.dialogFormVisible = true
        this.formDisabled = disabled
        if (!row) {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.cjr = userInfo.realname
          this.formData.cjsj = new Date().toJSON().split('T')[0]
          this.formData.fl = fl
          this.btnType = 'add'
        } else if (!row.jsxmjbqkid) {
          Object.assign(this.formData, row)
          this.tableData = row.attachments
        } else {
          const res = await engineeringBasicInformationDetail({
            jsxmjbqkid: row.jsxmjbqkid,
          }) // 获取到详情回显
          if (res.code === 1) {
            Object.assign(this.formData, res.data)
            if (res.data.attachments.length > 0) {
              this.tableData = res.data.attachments
            }
          }
        }
        if (row && !disabled) {
          this.title = '编辑'
        } else if (row && disabled) {
          this.title = '详细'
        }

        // this.fetchData(row)
      },
      // async fetchData(row) {
      //   this.loading = true
      //   const res = await detail({
      //     jsxmjbqkid: row.jsxmjbqkid,
      //   })
      //   this.loading = false
      //   if (res && res.code == 1) {
      //     Object.assign(this.formData, res.data)
      //   } else {
      //     this.$message.error(res.msg || '操作失败！')
      //   }
      // },
      close() {
        this.formData = {
          htbh: '', // 合同编号
          fl: '', // 项目类别
          ysxmmc: '', // 验收项目名称
          jhwh: '', // 计划文号
          jhtzje: '', // 计划投资金额（万元）
          cbfs: '', // 承包方式（EPC或施工承包）
          kjgsj: '', // 开竣工时间
          jgsj: '', // 开竣工时间
          syxhgsj: '', // 试运行合格时间
          qtzxysqk: '', // 其他专项验收情况
          zlgdsj: '', // 资料归档时间
          xmtcsj: '', // 项目投产时间
          wcgcjssj: '', // 完成工程结算时间（二审）
          gcfyjsje: '', // 工程费用结算金额
          sfwccwjs: '', // 是否完成财务决算 0 1
          xmfzr: '', // 项目负责人
          xmfzrId: '',
          tborgname: '', // 填报单位
          attid: '', // 文号附件
          attname: '', // 文号附件
          tborgid: '', // 填报单位
          lxdh: '', // 联系电话
          cjr: '', // 创建人
          cjsj: '', // 创建时间
          attIds: [], // 附件主键集合
          jgplanid: '',
          jgjh: '',
        }

        this.btnType = ''
        this.dialogFormVisible = false
        this.tableData = []
        this.formDisabled = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            if (this.formData.sfwccwjs == 0) {
              return this.$message.error('必须完成财务决算才能上报')
            }
            this.loading = true
            // 处理上传动作之后的数据收集一起发送
            let attIds = ''
            this.tableData.map((item) => {
              attIds += item.attid
              attIds += ','
            })
            attIds = attIds.substring(0, attIds.length - 1)
            const params = {
              ...this.formData,
              attIds,
            }
            delete params.attachments
            const res = await engineeringBasicInformationUpdate(params)
            this.loading = false
            if (res && res.code === 1) {
              this.$message.success('保存成功！')
              this.$emit('fetch-data', res.data, this.btnType)
              this.close()
            } else {
              this.$message.error(res.msg || '保存失败！')
            }
          } else {
            return false
          }
        })
      },
      handleDelete(row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deleteReportFile({ attId: row.attid })
          if (res.msg === '成功') {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            let list = this.tableData
            list = list.filter((item) => item.attid != row.attid)
            this.tableData = list
          } else {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          }
        })
      },
      /**
       * @description: 下载文件
       * @param {*} row
       * @return {*}
       */
      // async handleDown(row) {
      //   if (!row.attid) return this.$message.error('请先上传文件')
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
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */
      // async handlePreviewFile(row) {
      //   if (!row.attid) return this.$message.error('请先上传文件')
      //   const { data } = await getPrivewAttInfo({
      //     attId: row.attid,
      //     attType: 2,
      //   })

      //   const url =
      //     data.previewurl +
      //     '?url=' +
      //     encodeURIComponent(Base64.encode(data.ftpUrl))
      //   this.$iFrameDialog({ iframeUrl: url })
      // },
      // handleSuccess(file) {
      //   if (file.result == '200') {
      //     let list = this.tableData
      //     list.push(file.data)
      //     this.tableData = list
      //     this.$baseMessage(file.msg, 'success')
      //   } else {
      //     this.$baseMessage(file.msg, 'error')
      //   }
      // },
      async getChildlistPro(val) {
        const ids = val.map((res) => res.staffid)
        const names = val.map((res) => res.realname)
        this.$set(this.formData, 'xmfzrId', ids.toString())
        this.$set(this.formData, 'xmfzr', names.toString())
      },
      /**
       * @description: 选择单位
       * @param {*}
       * @return {*}
       */
      handleUnitSelected(node) {
        console.log('node', node)
        this.formData.tborgname = node.label
        this.formData.tborgid = node.id
      },
      /**
       * @description: 上传计划文号附件
       * @param {*}
       * @return {*}
       */
      onSelectJhwhFile(file) {
        if (file.result == '200') {
          this.formData.attid = file.data.attid
          this.formData.attname = file.data.attname
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      selectPlan() {
        this.$refs.plan.showEdit()
      },
      handleSelectPlan(data) {
        if (data) {
          this.formData.jgplanid = data.gcxmjgysjhid
          this.formData.jgjh = data.xmmc
          this.formData.xmtcsj = data.xmtcsj
          this.formData.jhtzje = data.xmztzje
        }
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
        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
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
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }

  .color-red {
    color: red;
    font-size: 12px;
  }
</style>
