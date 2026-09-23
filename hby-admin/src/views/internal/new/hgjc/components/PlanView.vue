<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="form"
        label-width="120px"
        :model="form"
        :rules="rules"
        :validate-on-rule-change="false"
      >
        <!-- <el-col
          v-for="item in renderData"
          :key="item.field"
          :span="item.componentWidth == '50' ? 12 : 24"
        >
          <CustormForm
            :item="item"
            :form="form"
            :ref="item.field"
          ></CustormForm>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="方案编号" prop="planCode">
            <el-input v-model.trim="form.planCode" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="方案名称" prop="planName">
            <el-input
              v-model.trim="form.planName"
              placeholder="请输入方案名称"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="方案年度" prop="planYear">
            <el-date-picker
              v-model="form.planYear"
              format="yyyy"
              placeholder="请选择方案年度"
              :style="{ width: '100%' }"
              type="year"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查类型" prop="inspectType">
            <el-select
              v-model="form.inspectType"
              clearable
              placeholder="请选择检查类型"
              :style="{ width: '100%' }"
              :disabled="footer"
            >
              <el-option
                v-for="item in field103Options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="方案时间" prop="time">
            <el-date-picker
              v-model="form.time"
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="开始日期"
              :style="{ width: '100%' }"
              type="daterange"
              value-format="yyyy-MM-dd"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="配合部门" prop="cooperateDepartmentName">
            <!-- <el-input v-model.trim="form.org2" disabled /> -->
            <el-input
              v-model.trim="form.cooperateDepartmentName"
              :style="{ width: '256px' }"
              placeholder="请选择配合部门"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.departmentTree.show(0, null, '配合部门')"
              :disabled="footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="开展费用" prop="cost">
            <el-input v-model.trim="form.cost" placeholder="请输入开展费用" />
          </el-form-item>
        </el-col> -->
        <el-col :span="12" style="height: 50px">
          <el-form-item label="负责人" prop="responsiblePersonName">
            <el-input
              v-model.trim="form.responsiblePersonName"
              :style="{ width: '256px' }"
              placeholder="请选择负责人"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.executor.show()"
              :disabled="footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="投入人力" prop="investManpower">
            <el-input
              v-model.trim="form.investManpower"
              placeholder="请输入投入人力"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查公司" prop="inspectCompanyName">
            <el-input
              v-model.trim="form.inspectCompanyName"
              placeholder="请选择检查公司"
              :style="{ width: '256px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.companyTree.show(0, null, '检查公司')"
              :disabled="footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检查部门" prop="inspectDepartmentName">
            <!-- <el-input v-model.trim="form.org2" disabled /> -->
            <el-input
              v-model.trim="form.inspectDepartmentName"
              :style="{ width: '256px' }"
              placeholder="请选择检查部门"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.departmentTree.show(0, null, '检查部门')"
              :disabled="footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="模板" prop="testtemid">
            <el-input
              v-model.trim="form.templename"
              :style="{ width: '256px' }"
              placeholder="请选择模板"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.template.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="创建人" prop="creatorName">
            <el-input
              v-model.trim="form.creatorName"
              placeholder="请输入创建人"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createdTime">
            <el-date-picker
              v-model="form.createdTime"
              :style="{ width: '100%' }"
              type="date"
              disabled
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="" prop="content">
            <!-- <tinymce
              v-model="formData.content"
              :height="300"
              placeholder="请输入编辑器"
            /> -->
            <UEditor
              ref="ueditor"
              v-model="form.content"
              :height="300"
              :templates="templates"
              template="lcjy"
              style="margin-left: -100px"
              v-if="dialogFormVisible"
              :disabled="footer"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="!footer">
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
          <el-table :data="tableDataFile">
            <el-table-column align="center" label="附件名称" prop="name" />
            <el-table-column align="center" label="文件大小(KB)" prop="size" />
            <el-table-column
              align="center"
              label="创建人"
              prop="createPerson"
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row }">
                <!-- <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button> -->
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button
                  type="text"
                  @click="handleDelete(row)"
                  v-if="!footer"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <template #footer v-if="!footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>

    <test-template ref="template" @selected="handleTemplateSelected" />
    <CompanyTreeModal
      ref="companyTree"
      :checkStrictly="true"
      @selected="handleCompanyTreeSelected"
    />
    <DepartmentTreeModal
      ref="departmentTree"
      @selected="handleDepartmentTreeSelected"
    />
    <CompanySelectUserByTree
      ref="executor"
      @selected="handleExecutorSelected"
    />
  </el-dialog>
</template>

<script>
  // import { doEdit } from '@/api/table'
  import {
    findAutoNumber,
    savePlan,
    getPlanNumber,
    getPlanInfo,
    planSaveOrUpdate,
  } from '@/api/internal/new/plan'
  import { deleteFile, download } from '@/api/hggl/hgjhgl'
  import { getDefaultRenderData } from '@/api/internal/project'
  import TestTemplate from './options/TestTemplate.vue'
  import CompanyTreeModal from '@/components/CompanyTreeModal'
  import DepartmentTreeModal from './DepartmentTreeModal'
  import CompanySelectUserByTree from '@/components/CompanySelectUserByTree'
  import CustormForm from '@/components/customForm/index.vue'
  import Tinymce from '@/components/Tinymce'
  import UEditor from '@/components/UEditor'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']

  export default {
    name: 'PlanView',
    components: {
      TestTemplate,
      CompanyTreeModal,
      DepartmentTreeModal,
      CompanySelectUserByTree,
      CustormForm,
      UEditor,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/fileManage/upload',
        headers: { token: token },
        form: {
          planCode: '',
          planName: '',
          planYear: '',
          time: [],
          cooperateDepartmentName: '',
          responsiblePersonName: '',
          investManpower: '',
          inspectCompanyName: '',
          inspectPersonName: '',
          content: '',
        },
        rules: {
          planCode: [
            { required: true, trigger: 'blur', message: '请输入项目编号' },
          ],
          planName: [
            { required: true, trigger: 'blur', message: '请输入项目名称' },
          ],
          planYear: [
            { required: true, trigger: 'blur', message: '请选择方案年度' },
          ],
          time: [
            { required: true, trigger: 'blur', message: '请选择方案时间' },
          ],
          cooperateDepartmentName: [
            { required: true, trigger: 'blur', message: '请选择配合部门' },
          ],
          responsiblePersonName: [
            { required: true, trigger: 'blur', message: '请选择负责人' },
          ],
          investManpower: [
            { required: true, trigger: 'blur', message: '请输入投入人力' },
          ],
          inspectCompanyName: [
            { required: true, trigger: 'blur', message: '请选择检查单位' },
          ],
          inspectDepartmentName: [
            { required: true, trigger: 'blur', message: '请选择检查部门' },
          ],
        },
        field103Options: [
          {
            label: '专项检查',
            value: 1,
          },
          {
            label: '综合检查',
            value: 2,
          },
          {
            label: '定期检查',
            value: 3,
          },
        ],
        title: '',
        dialogFormVisible: false,
        renderData: [],
        tableData: [],
        templates: [],
        footer: false,
        fileList: [],
        tableDataFile: [],
        fileIds: [],
      }
    },
    created() {},
    watch: {
      'form.content'(val) {
        // console.log('formData.describe', val)
        // console.log('ueditor', this.$refs['ueditor'].editor.openTemplate)
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
            if (this.form[i[1]]) {
              s = s.replace(i[0], this.form[i[1]])
            }
          })
          this.form.content = s
        }
      },
    },
    methods: {
      showEdit(row, title) {
        if (title == 'add') {
          this.title = '添加'
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.form.creatorName = userInfo.realname
          this.form.createdTime = new Date()
          this.getNumber()
        } else {
          this.getInfo(row)

          if (title == 'edit') {
            this.title = '编辑'
          } else {
            this.title = '查看'
            this.footer = true
          }
        }
        this.dialogFormVisible = true
      },
      async getInfo(row) {
        const { data, code, msg } = await getPlanInfo({
          id: row.id,
        })
        if (code === 1) {
          const { planTimeStart, planTimeEnd, ...other } = data.data

          this.form = {
            time: [planTimeStart, planTimeEnd],
            ...other,
          }
          this.tableDataFile = data.file || []
        }
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.footer = false
        this.tableDataFile = []
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { time, createdTime, planYear, ...other } = this.form
            const aa = []
            this.tableDataFile.forEach((e) => {
              aa.push(e.fileId)
            })
            const { msg, code, data } = await planSaveOrUpdate({
              ...other,
              planTimeStart: time ? time[0] : '',
              planTimeEnd: time ? time[1] : '',
              fileIds: aa.toString(),
              planYear: new Date(planYear).getFullYear(),
            })
            if (code == 1) {
              this.$baseMessage('成功', 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
              this.close()
            } else {
              // this.$baseMessage(data, 'error', 'vab-hey-message-error')
            }
          }
        })
      },
      async getNumber() {
        const { msg, code, data } = await getPlanNumber({
          year: new Date().getFullYear(),
        })
        if (code == 1) {
          this.form.planCode = data
        } else {
          this.$baseMessage(msg, 'error')
        }
      },
      handleExecutorSelected(node) {
        console.log('node', node)
        const { responsiblePerson, responsiblePersonName, ...other } = this.form
        let form = {
          responsiblePerson: node.staffid,
          responsiblePersonName: node.realname,
          ...other,
        }
        this.form = form
      },
      handleTemplateSelected(node) {
        const { testtemid, templename, ...other } = this.form
        let form = {
          templename: node.templename,
          testtemid: node.testtemid,
          ...other,
        }
        this.form = form
      },
      handleCompanyTreeSelected(node) {
        if (node instanceof Array) {
          const { org3, ...other } = this.form
          let form = {
            org3: node.map((item) => item.label).join(','),
            ...other,
          }
          this.form = form
        } else {
          const { inspectCompany, inspectCompanyName, ...other } = this.form
          let form = {
            inspectCompany: node.id,
            inspectCompanyName: node.name,
            ...other,
          }
          this.form = form
        }
      },
      handleDepartmentTreeSelected(val) {
        console.log(val, 'val')
        if (val.title == '配合部门') {
          // this.$set(this.form, 'cooperateDepartment', val.checkeds.map(obj => obj.id).toString())
          this.$set(
            this.form,
            'cooperateDepartmentName',
            val.checkeds.map((obj) => obj.name).toString()
          )
        } else if (val.title == '检查部门') {
          // this.$set(this.form, 'inspectDepartment', val.checkeds.map(obj => obj.id).toString())
          this.$set(
            this.form,
            'inspectDepartmentName',
            val.checkeds.map((obj) => obj.name).toString()
          )
        }
      },
      handleAvatarSuccess(response, file, fileList) {
        if (file.response.result == '200') {
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
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteFile({ id: row.fileId })
          if (code == 200) {
            this.tableDataFile.splice(
              this.tableDataFile.findIndex((x) => x.fileId == row.fileId),
              1
            )
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
    },
  }
</script>
