<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-form
      ref="ruleForm"
      label-width="140px"
      :model="formData"
      :rules="rules"
      size="mini"
      :disabled="formDisabled"
    >
      <el-row :gutter="14">
        <el-col :span="12">
          <el-form-item label="制订规章制度数" prop="zdgzzds">
            <el-input
              type="number"
              v-model="formData.zdgzzds"
              placeholder="请输入制订规章制度数"
              :style="{ width: '100%' }"
              @input="$forceUpdate()"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="修订规章制度数" prop="xggzzds">
            <el-input
              type="number"
              v-model="formData.xggzzds"
              placeholder="请输入修订规章制度数"
              :style="{ width: '100%' }"
              @input="$forceUpdate()"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="移送处理事项" prop="ysclsx">
            <el-input
              type="number"
              v-model="formData.ysclsx"
              clearable
              disabled
              placeholder="请输入移送处理事项"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item
            label="其中涉及向司法机关移送或报告事项"
            prop="sjsfjgysbg"
          >
            <el-input
              v-model="formData.sjsfjgysbg"
              clearable
              disabled
              type="number"
              placeholder="请输入其中涉及向司法机关移送或报告事项"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="移送处理涉及金额（元）" prop="ysclje">
            <el-input
              v-model="formData.ysclje"
              clearable
              disabled
              type="number"
              placeholder="请输入移送处理涉及金额（元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="移送处理人员（人）" prop="ysclr">
            <el-input
              v-model="formData.ysclr"
              clearable
              disabled
              type="number"
              placeholder="请输入移送处理人员（人） "
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="其中涉及向司法机关移送或报告事项涉及人员（人）"
            prop="sjsfjgysbgr"
          >
            <el-input
              v-model="formData.sjsfjgysbgr"
              clearable
              disabled
              type="number"
              placeholder="请输入其中涉及向司法机关移送或报告事项涉及人员（人） "
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="落实移送处理事项" prop="lsysclsx">
            <el-input
              v-model="formData.lsysclsx"
              clearable
              type="number"
              disabled
              placeholder="请输入落实移送处理事项"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="移送处理落实情况（人）" prop="yscllsqk">
            <el-input
              v-model="formData.yscllsqk"
              clearable
              type="number"
              disabled
              placeholder="请输入移送处理落实情况（人）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其中涉及党纪处分（人）" prop="qzsjdjcf">
            <el-input
              v-model="formData.qzsjdjcf"
              clearable
              disabled
              type="number"
              placeholder="请输入其中涉及党纪处分（人）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其中涉及政务处分（人）" prop="qzsjzwcf">
            <el-input
              v-model="formData.qzsjzwcf"
              clearable
              disabled
              type="number"
              placeholder="请输入其中涉及政务处分（人）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其中涉及内部纪律处分（人）" prop="qzsjnbjlcf">
            <el-input
              v-model="formData.qzsjnbjlcf"
              clearable
              disabled
              type="number"
              placeholder="请输入其中涉及内部纪律处分（人）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="其他经济处分（人）" prop="qtjjcf">
            <el-input
              v-model="formData.qtjjcf"
              clearable
              disabled
              type="number"
              placeholder="请输入其他经济处分（人）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="累计直接经济成果（元）" prop="ljzjjjcg">
            <el-input
              v-model="formData.ljzjjjcg"
              clearable
              type="number"
              disabled
              placeholder="请输入累计直接经济成果（元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="累计其他经济成果（元）" prop="ljqtjjcg">
            <el-input
              v-model="formData.ljqtjjcg"
              clearable
              type="number"
              disabled
              placeholder="累计其他经济成果（元）"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
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
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="提出审计建议数" prop="jynum">
            <el-input
              v-model="formData.jynum"
              clearable
              disabled
              placeholder="请输入"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计建议采纳数" prop="cnnum">
            <el-input
              v-model="formData.cnnum"
              clearable
              disabled
              placeholder="请输入"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出具审计要情(份)" prop="qynum">
            <el-input
              v-model="formData.qynum"
              clearable
              disabled
              placeholder="请输入"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col> -->

        <!-- <el-col :span="24" class="title">
          <el-divider>制/修订规章制度情况说明及文件</el-divider>
        </el-col>
        <el-col :span="24">
          <el-col :span="24">
            <el-form-item label="制/修订规章制度情况说明" prop="gzzdqksm">
              <el-input
                v-model="formData.tblYqnsSjzgGzzdqksm.gzzdqksm"
                placeholder="请输入制/修订规章制度情况说明"
                :style="{ width: '100%' }"
                type="textarea"
                rows="4"
              />
            </el-form-item>
          </el-col>
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!formDisabled"
          >
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableData">
            <el-table-column align="center" label="文件名" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="文件类型" prop="attType" />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row, '1')"
                  v-if="!formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col> -->

        <!-- <el-col :span="24" class="title">
          <el-divider>上传移送处理文书</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="text-align: right; margin-bottom: 5px"
            v-if="!formDisabled"
          >
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview2"
              :on-success="handleSuccess2"
              :file-list="tableData2"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableData2">
            <el-table-column align="center" label="附件名称" prop="attname" />
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
                  :disabled="false"
                  @click="handleDown(row)"
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
                  @click="handleDelete(row, '2')"
                  v-if="!formDisabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col> -->
      </el-row>
    </el-form>

    <div slot="footer" v-if="!formDisabled">
      <!-- <el-button @click="send" type="success">送审</el-button> -->
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    editInfo,
    getDetailInfo,
    deleteFileEditInfo,
    deleteFileInfo,
    getZgtjhzData,
    getStatisticsInfo,
  } from '@/oapi/yqns_sjzg/sjzgtjxx'
  import { download } from '@/oapi/audit/report'
  import store from '@/store'
  const { baseURL } = require('@/config')
  import { formatDate } from '@/utils'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  export default {
    name: 'sjzgtjxxEdit',
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          zdgzzds: '',
          xggzzds: '',
          ysclsx: '',
          sjsfjgysbg: '',
          ysclje: '',
          ysclr: '',
          sjsfjgysbgr: '',
          lsysclsx: '',
          yscllsqk: '',
          qzsjdjcf: '',
          qzsjzwcf: '',
          qzsjnbjlcf: '',
          qtjjcf: '',
          ljqtjjcg: '',
          ljzjjjcg: '',
          cjr: '',
          cjsj: '',
          sjzgtjid: '',
          jynum: '',
          cnnum: '',
          qynum: '',
          // tblYqnsSjzgGzzdqksm: {
          //   gzzdqksm: '',
          //   attIds: [],
          // },
        },
        formDisabled: true,
        tableData: [],
        tableData2: [],
        rules: {
          // code: [
          //   {
          //     required: true,
          //     message: '请输入文号',
          //     trigger: 'blur',
          //   },
          // ],
        },
        dialogFormVisible: false,
        title: '新增',
        typeOptions: [
          {
            label: '节约各类开支',
            value: '节约各类开支',
          },
          {
            label: '建设工程项目审减',
            value: '建设工程项目审减',
          },
          {
            label: '经济合同审减',
            value: '经济合同审减',
          },
          {
            label: '物资采购项目审减',
            value: '物资采购项目审减',
          },
          {
            label: '招投标审减',
            value: '招投标审减',
          },
          {
            label: '剔除联合账簿不合理费用',
            value: '剔除联合账簿不合理费用',
          },
          {
            label: '挽回各类损失',
            value: '挽回各类损失',
          },
          {
            label: '挽回债券性和股权性投资损失',
            value: '挽回债券性和股权性投资损失',
          },
          {
            label: '挽回货币性资产损失',
            value: '挽回货币性资产损失',
          },
          {
            label: '挽回非货币性资产损失',
            value: '挽回非货币性资产损失',
          },
          {
            label: '内部收缴',
            value: '内部收缴',
          },
          {
            label: '罚款金额',
            value: '罚款金额',
          },
          {
            label: '收缴小金库',
            value: '收缴小金库',
          },
          {
            label: '账外资金',
            value: '账外资金',
          },
          {
            label: '收缴/罚没其他违规违纪资金',
            value: '收缴/罚没其他违规违纪资金',
          },
        ],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
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
      // 送审
      send() {},
      async showEdit(row, disabled, id, projectId, dqzjjjcgtype, dqqtjjcgtype) {
        console.log('🚀 ~ showEdit ~ row:', row)
        this.dialogFormVisible = true
        this.formDisabled = disabled
        if (row && !disabled) {
          this.title = '编辑'
          if (projectId) {
            this.getInfoZZZ(projectId, dqzjjjcgtype, dqqtjjcgtype)
          }
          if (row?.sjzgtjid) {
            this.getInfo(row)
          } else {
            this.formData = row
            // this.tableData = row.tableData
          }
        } else if (row && disabled) {
          this.title = '详细'
          if (projectId) {
            this.getInfoZZZ(projectId, dqzjjjcgtype, dqqtjjcgtype)
          }
          if (row?.sjzgtjid) {
            this.getInfo(row)
          } else {
            this.formData = row
            // this.tableData = row.tableData
          }
        } else {
          this.title = '新建'
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.cjr = userInfo.realname
          this.formData.cjsj = formatDate(new Date())
          id ? this.getNum(id) : ''
          console.log('ss', projectId)
          if (projectId) {
            this.getInfoZZZ(projectId, dqzjjjcgtype, dqqtjjcgtype)
          }
        }
      },
      async getInfoZZZ(projectId, dqzjjjcgtype, dqqtjjcgtype) {
        const res = await getStatisticsInfo({
          projectId,
          dqzjjjcgtype,
          dqqtjjcgtype,
        })
        // this.formData = res.data
        console.log(res.data)
        this.formData.ysclsx = res.data.ysclsx
        this.formData.sjsfjgysbg = res.data.sjsfjgysbg
        this.formData.ysclje = res.data.ysclje
        this.formData.ysclr = res.data.ysclr
        this.formData.sjsfjgysbgr = res.data.sjsfjgysbgr
        this.formData.lsysclsx = res.data.lsysclsx
        this.formData.yscllsqk = res.data.yscllsqk
        this.formData.qzsjdjcf = res.data.qzsjdjcf
        this.formData.qzsjzwcf = res.data.qzsjzwcf
        this.formData.qzsjnbjlcf = res.data.qzsjnbjlcf
        this.formData.qtjjcf = res.data.qtjjcf
        this.formData.ljzjjjcg = res.data.ljzjjjcg
        this.formData.ljqtjjcg = res.data.ljqtjjcg
        this.$forceUpdate()
      },
      async getNum(id) {
        const res = await getZgtjhzData({ wtzgid: id })
        this.formData.jynum = res.data.jynum
        this.formData.cnnum = res.data.cnnum
        this.formData.qynum = res.data.qynum
        this.$forceUpdate()
      },
      async getInfo(row) {
        const res = await getDetailInfo({ sjzgtjid: row.sjzgtjid })
        // this.formData = res.data
        this.formData.zdgzzds = res.data.zdgzzds
        this.formData.xggzzds = res.data.xggzzds
        this.tableData2 = res.data.attachments
        this.formData.sjzgtjid = res.data.sjzgtjid
        this.$forceUpdate()
      },
      close() {
        this.formData = {
          // zdgzzds: '',
          // xggzzds: '',
          ysclsx: '',
          sjsfjgysbg: '',
          ysclje: '',
          ysclr: '',
          sjsfjgysbgr: '',
          lsysclsx: '',
          yscllsqk: '',
          qzsjdjcf: '',
          ljqtjjcg: '',
          ljzjjjcg: '',
          qzsjzwcf: '',
          qzsjnbjlcf: '',
          qtjjcf: '',
          cjr: '',
          cjsj: '',
          sjzgtjid: '',
          jynum: '',
          cnnum: '',
          qynum: '',
          // tblYqnsSjzgGzzdqksm: {
          //   gzzdqksm: '',
          //   attIds: '',
          // },
        }
        this.dialogFormVisible = false
        this.tableData = []
        this.tableData2 = []
        this.formDisabled = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let attIds = []
            this.formData.tableData = this.tableData2
            this.tableData2.map((item) => {
              attIds.push(item.attid)
            })
            this.formData.attIds = attIds
            this.$emit('setTable', this.formData, this.title)
            this.dialogFormVisible = false
            this.close()
          } else {
            return false
          }
        })
      },
      async handleDown(row) {
        const data = await download({ attId: row.attid })
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
      async handleDelete(row, type) {
        if (type == '1') {
          let list = this.tableData
          list = list.filter((item) => item.attid != row.attid)
          this.tableData = list
          await deleteFileEditInfo({ attid: row.attid })
        } else if (type == '2') {
          let list = this.tableData2
          list = list.filter((item) => item.attid != row.attid)
          this.tableData2 = list
          await deleteFileInfo({ attid: row.attid })
        }
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.tableData
          list.push(file.data)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handlePreview2(file) {},
      handleSuccess2(file) {
        if (file.result == '200') {
          let list = this.tableData2
          list.push(file.data)
          this.tableData2 = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
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

  .title {
    margin-top: 50px;
  }
</style>
