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
          <el-form-item label="姓名" prop="practiceExamineName">
            <el-input
              v-model="formData.practiceExamineName"
              clearable
              placeholder="请输入姓名"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button
              @click="projectManager"
              style="margin-left: 10px"
              type="primary"
              :disabled="!footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="性别 " prop="sex">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.sex"
              placeholder="性别"
              :disabled="!footer"
            >
              <el-option label="男" value="1" />
              <el-option label="女" value="0" />
            </el-select>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="已执业年限" prop="practiceAgeLimit">
            <el-input
              v-model="formData.practiceAgeLimit"
              clearable
              placeholder="请输入已执业年限"
              :style="{ width: '100%' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属单位" prop="practiceExamineBelongGroupName">
            <el-input
              v-model="formData.practiceExamineBelongGroupName"
              disabled
              placeholder="请输入所属单位"
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="
                $refs.companySelect.show({
                  labelKey: 'unit',
                  idKey: 'unitId',
                  title: '单位',
                })
              "
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="creatorName">
            <el-input
              v-model="formData.creatorName"
              clearable
              placeholder="请选择创建人"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="创建时间" prop="practiceExamineCreatedTime">
            <el-date-picker
              :style="{ width: '100%' }"
              v-model="formData.practiceExamineCreatedTime"
              placeholder="创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd HH:mm:ss"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="考核结果 " prop="examineGrade">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.examineGrade"
              placeholder="考核结果"
              :disabled="!footer"
            >
              <el-option label="称职" :value="0" />
              <el-option label="不称职" :value="1" />
              <el-option label="基本称职" :value="2" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col
          :span="24"
          v-if="formData.type == '不称职' || formData.type == '基本称职'"
        >
          <el-form-item label="理由" prop="entername">
            <el-input
              v-model="formData.entername"
              clearable
              type="textarea"
              rows="2"
              placeholder="请输入理由"
              :style="{ width: '100%' }"
              :disabled="!footer"
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
              :file-list="tableData"
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
    <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    />
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
  </el-dialog>
</template>

<script>
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import { formatDate } from '@/utils'
  import store from '@/store'
  import { uploadApi, deleteFile, download } from '@/api/fwgl/zzxx'
  import { addZXKH, getZXKHDefaultInfo } from '@/api/fwgl/gsls'
  import { baseURL } from '@/config'
  import CompanySelectModal from '@/components/CampanySelectModal'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  const token = store.getters['user/token']
  export default {
    components: { CompanySelectModal, projectManage },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        formData: {
          examineGrade: '',
          practiceAgeLimit: '',
          practiceExamineBelongGroup: '',
          practiceExamineCreatedTime: '',
          practiceExamineName: '',
          sex: '',
          fileIds: [],
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
      /**
       * @description: 选择公司部门回调
       * @param {*} val 已选数据
       * @return {*}
       */      
      handleCompanyTreeSelected(val) {
        this.$set(this.formData, 'practiceExamineBelongGroup', val.id)
        this.$set(this.formData, 'practiceExamineBelongGroupName', val.label)
      },
      /**
       * @description: 外部打开dialog
       * @param {*} title 表单类型
       * @param {*} row 行数据
       * @return {*}
       */      
      showEdit(title, row) {
        this.dialogFormVisible = true
        this.fileList = []
        if (row) {
          getZXKHDefaultInfo({
            id: row.practiceExamineId,
          }).then((res) => {
            this.formData = Object.assign({}, res.data.practiceExamine)
            // this.formData.sex = res.data.practiceExamine.sex.toString()
            if (res.data.files) {
              this.fileList = res.data.files
            }
          })
        }
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else {
          this.title = '新增'
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.creatorName = userInfo.realname
          this.formData.practiceExamineCreatedTime = formatDate(new Date())
        }
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.formData = {}
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
        addZXKH(this.formData).then((res) => {
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
       * @description: 打开选择人员组件
       * @return {*}
       */      
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      getChildlistPro(val) {
        this.$set(this.formData, 'practiceExamineName', val[0].realname)
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
        let url = window.URL.createObjectURL(blob)
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
