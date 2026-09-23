<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="100px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="条款名称" label-width="140px" prop="title">
            <el-input
              v-model="formData.title"
              clearable
              placeholder="请输入条款名称"
              :style="{ width: '256px' }"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="条款类型" label-width="140px" prop="termsTypeCode">
            <el-select v-model="formData.termsTypeCode" placeholder="请选择条款类型" :disabled="!footer" style="width:256px" @change="returnName($event,'termsTypeName',2)">
              <el-option v-for="group in configList[2].data" :key="group.label" :label="group.label" :value="group.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="利益倾向" label-width="140px" prop="interestPartyCode">
            <el-select v-model="formData.interestPartyCode" placeholder="请选择利益倾向" :disabled="!footer" style="width:256px" @change="returnName($event,'interestPartyName',4)">
              <el-option v-for="group in configList[4].data" :key="group.label" :label="group.label" :value="group.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同类型" label-width="140px" prop="contractTypeCode">
            <el-select v-model="formData.contractTypeCode" placeholder="请选择合同类型" :disabled="!footer" style="width:256px" @change="returnName($event,'contractTypeName',0)">
              <el-option v-for="group in configList[0].data" :key="group.label" :label="group.label" :value="group.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="适用行业" label-width="140px" prop="industryTypeCode">
            <el-select v-model="formData.industryTypeCode" placeholder="请选择适用行业" :disabled="!footer" style="width:256px" @change="returnName($event,'industryTypeName',1)">
              <el-option v-for="group in configList[1].data" :key="group.label" :label="group.label" :value="group.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险等级" label-width="140px" prop="riskLevelCode">
            <el-select v-model="formData.riskLevelCode" placeholder="请选择风险等级" :disabled="!footer" style="width:256px" @change="returnName($event,'riskLevelName',3)">
              <el-option v-for="group in configList[3].data" :key="group.label" :label="group.label" :value="group.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发布时间" label-width="140px" prop="issueDate">
            <el-date-picker
              :disabled="!footer"
              style="width:256px"
              v-model="formData.issueDate"
              type="date"
              placeholder="请选择发布时间"
              value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>条款内容</el-divider>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="content">
            <UEditor
              ref="ueditor"
              v-model="formData.content"
              :height="300"
              :templates="templates"
              style="margin-left: -100px"
              v-if="dialogFormVisible"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>风险提示</el-divider>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="riskTips">
            <UEditor
              ref="ueditors"
              v-model="formData.riskTips"
              :height="300"
              :templates="templates"
              style="margin-left: -100px"
              v-if="dialogFormVisible"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { getSidebar, createTerms, getTermsInfo } from '@/api/sjzt/ht/index'
  import { download } from '@/api/audit/implement'
  import { addHtfbk, deleteWKFile } from '@/api/setting/auth'
  import SelectIcon from '../selectIcon.vue'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  const { baseURL } = require('@/config')
  export default {
    name: 'htyskDetail',
    components: { SelectIcon, UEditor },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/setting/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          title: '',
          termsTypeCode: '',
          termsTypeName: '',
          interestPartyCode: '',
          interestPartyName: '',
          contractTypeCode: '',
          contractTypeName: '',
          industryTypeCode: '',
          industryTypeName: '',
          riskLevelCode: '',
          riskLevelName: '',
          issueDate: '',
          content: '',
          riskTips: '',
        },
        templates: [],
        footer: true,
        rules: {
          title: [
            {
              required: true,
              message: '请输入条款名称',
              trigger: 'blur',
            },
          ],
          termsTypeCode: [
            {
              required: true,
              message: '请选择条款类型',
              trigger: 'change',
            },
          ],
          interestPartyCode: [
            {
              required: true,
              message: '请选择利益倾向',
              trigger: 'change',
            },
          ],
          contractTypeCode: [
            {
              required: true,
              message: '请选择合同类型',
              trigger: 'change',
            },
          ],
          industryTypeCode: [
            {
              required: true,
              message: '请选择适用行业',
              trigger: 'change',
            },
          ],
          riskLevelCode: [
            {
              required: true,
              message: '请选择风险等级',
              trigger: 'change',
            },
          ],
          issueDate: [
            {
              required: true,
              message: '请选择发布时间',
              trigger: 'change',
            },
          ],
          content: [
            {
              required: true,
              message: '请输入条款内容',
              trigger: 'change',
            },
          ],
          riskTips: [
            {
              required: true,
              message: '请输入风险提示',
              trigger: 'change',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        disabled: false,
        fileList: [],
        tableDataFile: [],
        fileIds: [],
        configList: []
      }
    },
    watch: {
      // 'formData.content'(val) {
      //   if (this.$refs['ueditor'].editor.openTemplate) {
      //     this.$refs['ueditor'].editor.openTemplate = false
      //     let s = val
      //     const arr = [
      //       ['$[contract.contractno]', 'contractno'],
      //       ['$[contract.contractname]', 'contractname'],
      //       ['$[contract.contractamount]', 'contractmoney'],
      //       ['$[contract.contractItem]', 'contractitem'],
      //       ['$[contract.executor]', 'realname'],
      //       ['$[contract.rmbinwords]', 'hzsumowing'],

      //       ['$[counterpart.coupersion]', 'counterpartcode'],
      //       ['$[counterpart.personincharge]', 'contractbd'],
      //       ['$[counterpart.counterpartHank]', 'bankkhyh'],
      //       ['$[counterpart.counumber]', 'counterpartno'],
      //       ['$[counterpart.couname]', 'budgetname'],
      //       ['$[counterpart.couaddress]', 'counterpartaddress'],
      //       ['$[counterpart.coupersion]', 'contacts'],
      //       ['$[counterpart.contactsPhone]', 'contactsphone'],
      //       ['$[counterpart.counterpartHankAccount]', 'bankaccount'],
      //       ['$[counterpart.legarepresentative]', 'contacts'],
      //       ['$[counterpart.pctelephonenumber]', 'contractzd'],
      //     ]
      //     arr.forEach((i) => {
      //       if (this.formData[i[1]]) {
      //         s = s.replace(i[0], this.formData[i[1]])
      //       }
      //     })
      //     this.formData.content = s
      //   }
      // },
      // 'formData.riskTips'(val) {
      //   if (this.$refs['ueditors'].editor.openTemplate) {
      //     this.$refs['ueditors'].editor.openTemplate = false
      //     let s = val
      //     const arr = [
      //       ['$[contract.contractno]', 'contractno'],
      //       ['$[contract.contractname]', 'contractname'],
      //       ['$[contract.contractamount]', 'contractmoney'],
      //       ['$[contract.contractItem]', 'contractitem'],
      //       ['$[contract.executor]', 'realname'],
      //       ['$[contract.rmbinwords]', 'hzsumowing'],

      //       ['$[counterpart.coupersion]', 'counterpartcode'],
      //       ['$[counterpart.personincharge]', 'contractbd'],
      //       ['$[counterpart.counterpartHank]', 'bankkhyh'],
      //       ['$[counterpart.counumber]', 'counterpartno'],
      //       ['$[counterpart.couname]', 'budgetname'],
      //       ['$[counterpart.couaddress]', 'counterpartaddress'],
      //       ['$[counterpart.coupersion]', 'contacts'],
      //       ['$[counterpart.contactsPhone]', 'contactsphone'],
      //       ['$[counterpart.counterpartHankAccount]', 'bankaccount'],
      //       ['$[counterpart.legarepresentative]', 'contacts'],
      //       ['$[counterpart.pctelephonenumber]', 'contractzd'],
      //     ]
      //     arr.forEach((i) => {
      //       if (this.formData[i[1]]) {
      //         s = s.replace(i[0], this.formData[i[1]])
      //       }
      //     })
      //     this.formData.content = s
      //   }
      // },
    },
    created() {
      this.getConfig()
    },
    methods: {
      async getConfig(value) {
        let res = await getSidebar({'types': 'termsType, interestParty, contractType, industryType, riskLevel, issueYear'})
        this.configList = res.data.data
      },
      async getDetails(id) {
        let res = await getTermsInfo(id)
        this.formData = res.data.data
      },
      returnName(e,name,index) {
        let res = this.configList[index].data.find(function(item){
          return item.value === e;
        })
        this.formData[name] = res.label
      },
      showEdit(data) {
        if(data){
          this.title = '详情'
          this.getDetails(data)
          this.footer = false
        }else{
          this.title = '新增'
          this.footer = true
        }
        this.dialogFormVisible = true
        
      },
      async getFileList(reportid) {
        const res = await reportFileList({ reportid })
        //回填上传文件表格
        const arr = res.data.data
        const arr1 = arr.map((item) => {
          return {
            ...item,
            name: item.attname,
            size: item.attsize,
            createPerson: item.uploader,
          }
        })
        const arr2 = arr.map((res) => {
          return res.attid
        })
        //收集id
        this.fileIds = arr2
        this.tableDataFile = arr1
      },

      close() {
        this.formData = {}
        this.$refs['ruleForm'].resetFields()
        this.dialogFormVisible = false
        this.tableData = []
        this.footer = true
      },
      add() {
        console.log(this.$refs['ruleForm'])
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            // const { ...other } = 

            const data = await createTerms({
              ...this.formData,
              issueYear: new Date(this.formData.issueDate).getFullYear(),
            })
            if (data.code == 1) {
              this.$baseMessage('创建成功', 'success')
              this.close()
              this.$emit('fetchData')
            } else {
              this.$baseMessage(data.msg, 'error')
            }
          } else {
            return false
          }
        })
      },

      async handleDownload(row) {
        const res = await download({ attId: row.attid })
        this.downloadFileByBlob(res, row.name)
      },
      downloadFileByBlob(blob, fileName = 'file') {
        let blobUrl = window.URL.createObjectURL(blob)
        let link = document.createElement('a')
        link.download = fileName || 'defaultName'
        link.style.display = 'none'
        link.href = blobUrl
        // 触发点击
        document.body.appendChild(link)
        link.click()
        // 移除
        document.body.removeChild(link)
      },

      handleSuccess(response, file, fileList) {
        console.log(file, 'file')
        console.log(fileList, 'fileList')
        if (file.response.code == '200') {
          file.createPerson = JSON.parse(
            localStorage.getItem('userInfo')
          ).realname
          let arr = file.response.data.fileIds[0]
          const arr1 = {
            name: arr.fileName,
            size: arr.fileSize,
            createPerson: arr.uploader,
            attid: arr.fileId,
          }
          this.tableDataFile.push(arr1)

          this.fileList = fileList
          let fileArr = []
          this.fileList.forEach((item) => {
            fileArr.push(item.response.data.fileIds[0].fileId)
          })
          this.fileIds = [...this.fileIds, ...fileArr]
          this.$baseMessage(file.response.msg, 'success')
        } else {
          this.$baseMessage(file.response.msg, 'error')
        }
      },
      handleDeleteFile(index, row) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          //删除对应的id
          let res = await deleteWKFile({ attId: row.attid })
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
      getSelectIcon(data) {
        this.formData.fgimage = data.url
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
  textarea {
    display: none !important;
  }
</style>
