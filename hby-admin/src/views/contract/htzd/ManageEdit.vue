<!--
 * @Date: 2022-01-21 09:06:33
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-28 10:48:58
 * @FilePath: /hb-admin/src/views/setting/system/components/LcdyEdit.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      label-width="80px"
      :model="form"
      :rules="rules"
      :disabled="disabled"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="制度编号" prop="rulecode">
            <el-input v-model.trim="form.rulecode" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="制度名称" prop="rulename">
            <el-input v-model.trim="form.rulename" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="类别" prop="innruletype">
            <el-select
              style="width: 100%"
              :clearable="true"
              v-model="form.innruletype"
              placeholder="请选择状态"
            >
              <el-option label="法人治理" value="法人治理"></el-option>
              <el-option label="党建工作" value="党建工作"></el-option>
              <el-option label="人力资源" value="人力资源"></el-option>
              <el-option label="纪检监察" value="纪检监察"></el-option>
              <el-option label="行政管理" value="行政管理"></el-option>
              <el-option label="财务管理" value="财务管理"></el-option>
              <el-option label="安全环保" value="安全环保"></el-option>
              <el-option label="法务合规风控" value="法务合规风控"></el-option>
              <el-option label="投资管理" value="投资管理"></el-option>
              <el-option label="资产运营" value="资产运营"></el-option>
              <el-option label="巡察监督" value="巡察监督"></el-option>
              <el-option label="审计追责" value="审计追责"></el-option>
            </el-select>
            <!-- <el-input v-model.trim="form.innruletype" /> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发文文号" prop="rulenumber">
            <el-input v-model.trim="form.rulenumber" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select
              style="width: 100%"
              :clearable="true"
              v-model="form.status"
              placeholder="请选择状态"
            >
              <el-option label="草稿" value="草稿"></el-option>
              <el-option label="发布待审核" value="发布待审核"></el-option>
              <el-option label="已发布" value="已发布"></el-option>
              <el-option label="发布审核拒绝" value="发布审核拒绝"></el-option>
              <el-option label="已修订" value="已修订"></el-option>
              <el-option label="已废止" value="已废止"></el-option>
              <el-option label="废止待审核" value="废止待审核"></el-option>
              <el-option label="废止审核拒绝" value="废止审核拒绝"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发文机构" prop="orgname">
            <el-input
              v-model.trim="form.orgname"
              :style="{ width: '80%' }"
              :disabled="true"
            />
            <el-button
              type="primary"
              size="small"
              style="float: right"
              @click="$refs.manageTreeDialog.showDialog()"
              class="fl_r"
            >
              选 择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生效日期" prop="pulishdate">
            <el-date-picker
              v-model.trim="form.pulishdate"
              placeholder="生效时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="" prop="bodyinfo">
            <UEditor
              ref="ueditor"
              v-model="form.bodyinfo"
              :height="300"
              :templates="templates"
              style="margin-left: -80px"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <el-upload
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
            <div v-if="!disabled" style="margin-right: 10px">
              <el-button type="success">点击上传</el-button>
            </div>
            <!-- <el-button type="success" slot="tip" @click="handleDelFile">
              删除
            </el-button> -->
          </el-upload>
          <el-table
            :data="fileTableList"
            @selection-change="handleSelectionChangeFile"
          >
            <!-- <el-table-column type="selection" width="55"></el-table-column> -->
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
                  :disabled="false"
                  type="text"
                  @click="handleDown(row)"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  :disabled="false"
                  @click="handlePreviewFilePDf(row)"
                  size="mini"
                >
                  预览
                </el-button>
                <el-button
                  v-if="!disabled"
                  type="text"
                  @click="handleEdit2(row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button v-if="!disabled" @click="close">取 消</el-button>
      <el-button type="primary" v-if="!disabled" @click="save">确 定</el-button>
    </template>
    <Department ref="department" @selected="handleDepartmentSelected" />
    <ManageTreeDialog
      @selectNode="selectNode"
      ref="manageTreeDialog"
    ></ManageTreeDialog>
  </el-dialog>
</template>

<script>
  import { download } from '@/api/audit/implement'
  import {
    createHTZDCode,
    deleteManageFile,
    editInnerRule,
    selectInnerRuleInfo,
  } from '@/api/contract/htzd'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  // import { selectInnerRuleInfo } from '@/api/workbench/auditTools'
  import UEditor from '@/components/UEditor'
  import { baseURL } from '@/config'
  import store from '@/store'
  import { parseTime } from '@/utils/index'
  import Department from '@/views/contract/contractManage/components/options/department.vue'
  import ManageTreeDialog from './components/ManageTreeDialog.vue'

  export default {
    name: 'LcdyEdit',
    components: { Department, UEditor, ManageTreeDialog },
    data() {
      return {
        headers: {
          token: store.getters['user/token'],
        },
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        form: {
          rulecode: '',
          rulename: '',
          innruletype: '',
          rulenumber: '',
          status: '',
          publishorg: '',
          publishdate: undefined,
          bodyinfo: '',
          attIds: [],
        },
        disabled: false,
        templates: [],
        fileTableList: [],
        fileList: [],
        // tableData: [],
        rules: {
          rulecode: [{ required: true, trigger: 'blur', message: '请输入' }],
          rulename: [{ required: true, trigger: 'blur', message: '请输入' }],
          rulenumber: [{ required: true, trigger: 'blur', message: '请选择' }],
          innruletype: [{ required: true, trigger: 'blur', message: '请输入' }],
          status: [{ required: true, trigger: 'blur', message: '请输入' }],
          orgname: [{ required: true, trigger: 'blur', message: '请输入' }],
          pulishdate: [{ required: true, trigger: 'blur', message: '请输入' }],
          remark: [
            { required: true, trigger: 'blur', message: '请输入模板描述' },
          ],
        },
        title: '',
        dialogFormVisible: false,
        options: [],
      }
    },
    created() {},
    methods: {
      //下载
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
      //上传前置校验
      handleBeforeUpload(file) {
        const isLt2M = file.size / 1024 / 1024 < 100
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 200MB!')
        }
        return isLt2M
      },
      //回调
      selectNode(node) {
        this.form.publishorg = node.id
        this.form.orgname = node.name
        console.dir(this.form)
        this.$forceUpdate()
      },
      async showEdit(row, flag) {
        if (!row) {
          this.disabled = false
          this.title = '添加'
          createHTZDCode().then((res) => {
            this.$set(this.form, 'rulecode', res.data.autoCode.toString())
          })
        } else if (row && flag) {
          let res = await selectInnerRuleInfo(row.innrulid)
          this.fileTableList = res.data.attList || []
          this.form = {
            innrulid: res.data.tblNbsjInnerrule.innrulid,
            orgname: res.data.tblNbsjInnerrule.orgname,
            rulecode: res.data.tblNbsjInnerrule.rulecode,
            rulename: res.data.tblNbsjInnerrule.rulename,
            innruletype: res.data.tblNbsjInnerrule.innruletype,
            rulenumber: res.data.tblNbsjInnerrule.rulenumber,
            status: res.data.tblNbsjInnerrule.status,
            publishorg: res.data.tblNbsjInnerrule.publishorg,
            pulishdate: parseTime(
              res.data.tblNbsjInnerrule.publishdate,
              '{y}-{m}-{d}'
            ),
            bodyinfo: res.data.tblNbsjInnerrule.bodyinfo,
            attIds: [],
          }

          this.disabled = true
        } else {
          let res = await selectInnerRuleInfo(row.innrulid)
          this.fileTableList = res.data.attList || []
          this.title = '编辑'
          this.form = {
            innrulid: res.data.tblNbsjInnerrule.innrulid,
            orgname: res.data.tblNbsjInnerrule.orgname,
            rulecode: res.data.tblNbsjInnerrule.rulecode,
            rulename: res.data.tblNbsjInnerrule.rulename,
            innruletype: res.data.tblNbsjInnerrule.innruletype,
            rulenumber: res.data.tblNbsjInnerrule.rulenumber,
            status: res.data.tblNbsjInnerrule.status,
            publishorg: res.data.tblNbsjInnerrule.publishorg,
            pulishdate: parseTime(
              res.data.tblNbsjInnerrule.publishdate,
              '{y}-{m}-{d}'
            ),
            bodyinfo: res.data.tblNbsjInnerrule.bodyinfo,
            attIds: [],
          }
          this.disabled = false
        }
        this.dialogFormVisible = true
      },
      close() {
        this.fileTableList = []
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      //回调
      handleDepartmentSelected(node) {
        const data = node.id ? node : node.checked
        this.form.orgid = data.id
        this.form.orgname = data.text
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          // this.form.publishdate = this.form.publishdate.substring(0, 11)
          if (valid) {
            let attids = ''
            this.fileTableList.forEach((item) => {
              attids += item.attid + ','
            })
            attids = attids.substring(0, attids.length - 1)
            this.form.attIds = attids
            const { msg } = await editInnerRule(this.form)
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
      handleSuccess(response, file, fileList) {
        // file.createPerson = this.createPerson
        // file.id = Math.random()
        this.fileTableList.push(response.data)
        console.dir(this.fileTableList)
      },
      //删除选中的已上传的文件
      // handleDelFile() {
      //   let newArr = this.multipleSelectionFile.map((item) => {
      //     return item.id
      //   })
      //   this.form.attIds = this.form.attIds.filter((item) => {
      //     return !newArr.includes(item.id)
      //   })
      // },
      async handleEdit2(row) {
        // this.$confirm('此操作将永久删除, 是否继续?', '提示', {
        //   confirmButtonText: '确定',
        //   cancelButtonText: '取消',
        //   type: 'warning',
        // })
        // deleteManageFile({ attId: row.attid })
        //   .then(() => {
        //     let list = this.fileTableList
        //     list = list.filter((item) => item.attid != row.attid)
        //     this.fileTableList = list
        //     this.$message({
        //       type: 'success',
        //       message: '删除成功!',
        //     })
        //   })
        //   .catch(() => {
        //     this.$message({
        //       type: 'info',
        //       message: '已取消删除',
        //     })
        //   })
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          deleteManageFile({ attId: row.attid }).then(() => {
            let list = this.fileTableList
            list = list.filter((item) => item.attid != row.attid)
            this.fileTableList = list
            this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
          })
        })
      },
      //回调
      handleSelectionChangeFile(val) {
        this.multipleSelectionFile = val
      },
      async handlePreviewFilePDf(list) {
        const { data } = await getPrivewAttInfo({
          attId: list.attid,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
    },
  }
</script>
