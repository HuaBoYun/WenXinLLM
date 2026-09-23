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
          <el-form-item label="案件名称" prop="courtfirst">
            <el-input
              v-model="form.courtfirst"
              clearable
              placeholder="请输入案件名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁金额">
            <el-input
              v-model="form.arbitrationamount"
              clearable
              placeholder="请输入仲裁金额"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁受理日期">
            <el-date-picker
              v-model="form.asdealdate"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入仲裁受理日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁首次开庭日期">
            <el-date-picker
              v-model="form.asfirsthearingdate"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入仲裁首次开庭日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="纠纷登记">
            <el-input
              v-model="form.disputeitem"
              clearable
              placeholder="请选择纠纷登记"
              :style="{ width: '266px' }"
              readonly
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.xzjf.show(2)"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁结案日期">
            <el-date-picker
              v-model="form.arbitrationenddate"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入仲裁结案日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁机构">
            <el-input
              v-model="form.arbitraorg"
              clearable
              placeholder="请输入仲裁机构"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁机构联系人">
            <el-input
              v-model="form.arbitralinkman"
              clearable
              placeholder="请输入仲裁机构"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="我方代理人">
            <el-input
              v-model="form.zxstaffname"
              clearable
              placeholder="请选择我方代理人"
              readonly
              :style="{ width: '266px' }"
            />
            <el-button
              type="primary"
              @click="$refs.executor.show()"
              :style="{ marginLeft: '10px' }"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="录入人">
            <el-input
              v-model="form.enteringperson"
              clearable
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="时间">
            <el-date-picker
              v-model="form.arbitratime"
              clearable
              format="yyyy-MM-dd"
              placeholder="请输入仲裁结案日期"
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="仲裁结果">
            <el-input
              v-model="form.arbitrationresult"
              clearable
              placeholder="请输入仲裁结果"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-form>

      <el-col :span="24">
        <el-divider>我方代理人</el-divider>
      </el-col>
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px">
          <el-button @click="addLine" type="success">新增</el-button>
        </div>
        <el-table :data="dllsData" style="width: 100%; margin: 10px 0 50px 0%">
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
          <el-table-column align="center" prop="attorneyphont" label="操作">
            <template #default="{ row }">
              <el-button @click="deleteRow(row.id)" type="text" size="small">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>

      <el-col :span="24">
        <el-divider>附件</el-divider>
      </el-col>
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px">
          <el-button v-if="!form.arbitraid" type="success" @click="hold()">
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
          <el-form-item label="案件名称" prop="courtfirst">
            <span>{{ form.courtfirst }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁金额">
            <span>{{ form.arbitrationamount }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁受理日期">
            <span>{{ form.asdealdate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁首次开庭日期">
            <span>{{ form.asfirsthearingdate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="协商信息11111">
            <span>{{ form.disputeitem }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仲裁结案日期">
            <span>{{ form.arbitrationenddate }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="仲裁结果">
            <span>{{ form.arbitrationresult }}</span>
          </el-form-item>
        </el-col>
      </el-form>
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
      <div class="dialog_btn">
        <div>
          <el-button type="primary" @click="ccbqAdd">财产保全</el-button>
          <!-- <el-button type="primary" @click="zxglAdd">执行管理</el-button> -->
          <el-button type="primary" @click="jazjAdd">结案总结</el-button>
        </div>
        <div>
          <el-button @click="close">取 消</el-button>
          <el-button v-if="show == 0" type="primary" @click="save">
            确 定
          </el-button>
        </div>
      </div>
    </template>
    <xzss-options ref="xzss" @selected="handleSsjd" />
    <arbitrationEdit ref="editadd" @editadd="handleeditadd" />
    <!-- 财产保全 -->
    <zcbqModule ref="zcbqModuleref" :info="this.baseInfo" />
    <!-- 执行管理 -->
    <zxglModule ref="zxglModuleref" :info="this.baseInfo" />
    <!-- 结案 -->
    <jazjAdd ref="jazjAddref" />
    <!-- 纠纷登记 -->
    <xzjf-options ref="xzjf" @selected="handleSsjd" />
    <!-- 代理律师新增编辑 -->
    <lower-edit ref="lowerEdit" @fetch-lawer-list="fetchLawerList" />
  </el-dialog>
</template>

<script>
  import {
    ArbitrationRecord,
    ArbitratSettlementModify,
    ArbitratSettlementSave,
    deleAttacheMent,
    findAttacheMent,
    getArbitratSettlementDetail,
    legalAttorney,
    legalAttorneyDelete,
    removeArbitrationRecord,
  } from '@/api/fwgl/legal'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { baseURL } from '@/config/net.config'
  import store from '@/store'
  import LowerEdit from '@/views/fwgl/legal/components/lowerEdit'
  import jazjAdd from './jazjAdd.vue'
  import xzjfOptions from './options/xzjf.vue'
  import xzssOptions from './options/xzss.vue'
  import arbitrationEdit from './smEdit/arbitrationEdit2.vue'
  import zcbqModule from './zcbqModule.vue'
  import zxglModule from './zxglModule.vue'
  import { downloads } from '@/api/fwgl/zzxx'

  const nowDate =
    new Date().getFullYear() +
    '-' +
    (new Date().getMonth() + 1) +
    '-' +
    new Date().getDate()

  export default {
    name: 'DraftEdit',
    components: {
      xzjfOptions,
      xzssOptions,
      arbitrationEdit,
      zcbqModule,
      jazjAdd,
      zxglModule,
      LowerEdit,
    },
    data() {
      return {
        form: {
          arbitraid: undefined,
          courtfirst: undefined,
          arbitrationamount: undefined,
          asdealdate: undefined,
          asfirsthearingdate: undefined,
          disputeitem: undefined,
          arbitrationenddate: undefined,
          arbitrationresult: undefined,
          negotiaId: undefined,
          arbitraorg: undefined,
          arbitralinkman: undefined,
          enteringperson: '',
          arbitratime: nowDate,
          zxstaffname: undefined,
          zxstaffid: undefined,
        },
        dllsData: [],
        rules: {
          courtfirst: [
            {
              required: true,
              message: '请输入案件名称',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          flowid: '698874',
        },
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
        baseInfo: {}, //传给孙子组件信息
      }
    },
    computed: {
      uploadData() {
        return {
          type: 4,
          bid: this.form.arbitraid,
        }
      },
    },
    created() {},
    mounted() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))

      this.$set(this.form, 'enteringperson', userInfo.realname || '')
    },
    methods: {
      /**
       * @description: 新增财产保全
       * @return {*}
       */
      ccbqAdd() {
        if (!this.form.arbitraid) {
          this.$message({
            type: 'error',
            message: '请先保存表单',
          })
          return
        }
        this.$refs['zcbqModuleref'].show({
          arbitraid: this.form.arbitraid,
        })
      },
      /**
       * @description: 新增执行管理
       * @return {*}
       */
      zxglAdd() {
        if (!this.form.arbitraid) {
          this.$message({
            type: 'error',
            message: '请先保存表单',
          })
          return
        }
        this.$refs['zxglModuleref'].show({
          arbitraid: this.form.arbitraid,
        })
      },
      /**
       * @description: 新增结案总结
       * @return {*}
       */
      jazjAdd() {
        if (!this.form.arbitraid) {
          this.$message({
            type: 'error',
            message: '请先保存表单',
          })
          return
        }
        this.$refs['jazjAddref'].show({
          arbitraid: this.form.arbitraid,
        })
      },
      /**
       * @description: 看起来没啥用的提示按钮
       * @return {*}
       */
      hold() {
        this.$message.error('请先保存基本信息!')
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
      showDetail(row) {
        this.showEdit(row, true)
      },
      async showEdit(row, disabled) {
        this.show = 0
        if (!row) {
          this.listLoading = false
          this.title = '添加'
        } else {
          this.listLoading = true
          if (disabled) {
            this.title = '查看'
            this.show = 1
          } else {
            this.title = '编辑'
          }
          // Object.keys(this.form).forEach((key) => {
          //   this.form[key] = row[key]
          // })
          const res = await getArbitratSettlementDetail({
            arbitraId: row.arbitraid,
          })

          // this.$set(this, 'form', res.arbitrat)
          this.$set(this.form, 'courtfirst', res.arbitrat.courtfirst)
          this.$set(
            this.form,
            'arbitrationamount',
            res.arbitrat.arbitrationamount
          )
          this.$set(this.form, 'asdealdate', res.arbitrat.asdealdate)
          this.$set(
            this.form,
            'asfirsthearingdate',
            res.arbitrat.asfirsthearingdate
          )
          this.$set(this.form, 'disputeitem', res.arbitrat.disputeitem)
          this.baseInfo = {
            disputeid: res.arbitrat.disputeid,
            disputename: res.arbitrat.disputeitem,
          }
          this.$set(
            this.form,
            'arbitrationenddate',
            res.arbitrat.arbitrationenddate
          )
          this.$set(this.form, 'arbitraorg', res.arbitrat.arbitraorg)
          this.$set(this.form, 'arbitralinkman', res.arbitrat.arbitralinkman)
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))

          this.$set(
            this.form,
            'enteringperson',
            res.arbitrat.enteringperson || userInfo.realname
          )
          this.$set(this.form, 'arbitratime', res.arbitrat.arbitratime)
          this.$set(
            this.form,
            'arbitrationresult',
            res.arbitrat.arbitrationresult
          )
          this.$set(this.form, 'negotiaid', res.arbitrat.negotiaid)
          this.$set(this.form, 'arbitraid', res.arbitrat.arbitraid)

          this.uploadList()
          this.getEditList()
          this.fetchLawerList()
        }
        this.dialogFormVisible = true
      },
      //附件列表
      async uploadList() {
        // this.listLoading = true
        const { data } = await findAttacheMent({
          type: 4,
          bid: this.form.arbitraid,
        })
        this.uploadlist = data
        this.$refs.upload.clearFiles()
        this.$refs.upload.uploadFiles.length = 0
        // this.listLoading = false
      },
      //附件删除
      handleDelete1(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleAttacheMent({ aid: row.attid, type: 4 })
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
      /**
       * @description: 原生下载
       * @param {*} row
       * @return {*}
       */
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
      // 增加一个空行, 用于录入或显示第一行
      /**
       * @description: 新增一行我方代理人
       * @return {*}
       */
      addLine() {
        if (!this.form.arbitraid) {
          return this.$message({
            type: 'error',
            message: '请先保存表单',
          })
        }
        this.$refs['lowerEdit'].showModal({
          arbitrationid: this.form.arbitraid,
        })
      },
      // 删除指定行
      async deleteRow(id) {
        const res = await legalAttorneyDelete({ id })
        this.fetchLawerList()
      },
      async fetchLawerList() {
        if (!this.form.arbitraid) return
        const res = await legalAttorney({ arbitrationid: this.form.arbitraid })
        this.dllsData = res.data
      },
      onSuccess(response, file, fileList) {
        this.uploadList()
      },
      onError(err) {
        this.$message.error(JSON.parse(err.message).message)
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */
      close() {
        this.$refs['form'].resetFields()
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
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const bCreate = !this.form.arbitraid
            const func = bCreate
              ? ArbitratSettlementSave
              : ArbitratSettlementModify
            const msg = bCreate ? '新增成功' : '修改成功'

            const tempData = Object.assign({}, this.form)
            if (!bCreate) {
              delete tempData.d
            }
            func(this.form).then((res) => {
              if (res.code == 1) {
                if (bCreate) {
                  this.form.arbitraid = res.data
                }
                this.$emit('fetch-data')
                this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              }
            })
            this.$emit('fetch-data')
          }
        })
      },
      handleLawerSelect(row) {
        this.lawerIndex = row.id
        this.$refs.executor.show()
      },
      /**
       * @description: 选择组件回调
       * @param {*} val
       * @return {*}
       */
      handleSsjd(val) {
        this.$set(this.form, 'disputeid', val.disputeid)
        this.$set(this.form, 'disputeitem', val.disputeitem)
        this.$set(this.form, 'negotiaId', val.negotiaid)
        this.baseInfo = {
          disputeid: val.disputeid,
          disputename: val.disputeitem,
        }
        // this.form.negotiaId = val.negotiaid
        // this.form.disputeitem = val.disputeitem
        // this.form.disputeid = val.disputeid
      },
      /**
       * @description: 编辑财产保全
       * @return {*}
       */
      handleeditadd() {
        this.getEditList()
      },
      async getEditList() {
        this.listLoading = true
        this.queryForm.arrecordid = this.form.arbitraid
        const {
          data: { tlist, totalRecord },
        } = await ArbitrationRecord(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await removeArbitrationRecord({
            arrecordId: row.arrecordid,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.getEditList()
        })
      },
      showEditAdd(row) {
        // this.$refs['editadd'].showEdit(row)
        if (!this.form.arbitraid) {
          // this.$baseMessage(
          //   '请先保存基本信息',
          //   'success',
          //   'vab-hey-message-success'
          // )
          this.$message.error('请先保存基本信息!')
          return
        }
        this.$refs['editadd'].showEdit({
          arbitraId: this.form.arbitraid,
          ...row,
        })
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
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }

  .dialog_btn {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .dlls {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .attorneyClass {
    display: flex;
  }
</style>
