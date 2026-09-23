<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12" style="height: 29px">
          <el-form-item label="编号" prop="resultcode">
            <el-input
              v-model="formData.resultcode"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="projectname">
            <el-input
              v-model="formData.projectname"
              clearable
              placeholder="请输入审计项目名称"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 29px">
          <el-form-item label="被审计单位名称" prop="orgidnames">
            <el-input
              v-model="formData.orgidnames"
              clearable
              placeholder="请输入被审计单位名称"
              :style="{ width: '266px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.company.showEdit()"
              size="small"
              disabled
            >
              选择

              <!-- @click="openDep('orgidnames')" -->
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractcode">
            <el-input
              v-model="formData.contractcode"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入合同编号"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="合同名称" prop="contractname">
            <el-input
              v-model="formData.contractname"
              clearable
              placeholder="请输入合同名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="
              formData.served == '经责'
                ? '供应商'
                : formData.served == '专项'
                ? '合同相对方/人'
                : '施工单位'
            "
            prop="sgorgname"
            v-if="!isJZK"
          >
            <el-input
              v-model="formData.sgorgname"
              clearable
              :placeholder="
                formData.served == '经责'
                  ? '请输入供应商'
                  : formData.served == '专项'
                  ? '请输入合同相对方/人'
                  : '请输入施工单位'
              "
              :style="{ width: '100%' }"
            />
          </el-form-item>
          <el-form-item label="供应商" prop="sgorgname" v-if="isJZK">
            <el-input
              v-model="formData.sgorgname"
              clearable
              placeholder="请输入供应商"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报审金额(元)" prop="contractmoney">
            <el-input
              v-model.number="formData.contractmoney"
              :style="{ width: '100%' }"
              placeholder="请输入报审金额(元)"
              @input="inputMoney($event, 'contractmoney')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="formData.served == '经责' ? '结算金额(元)' : '核增金额(元)'"
            prop="hzmoney"
          >
            <el-input
              v-model.number="formData.hzmoney"
              :style="{ width: '100%' }"
              :placeholder="
                formData.served == '经责'
                  ? '请输入结算金额(元)'
                  : '请输入核增金额(元)'
              "
              @input="inputMoney($event, 'hzmoney')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="formData.served == '经责' ? '扣减金额(元)' : '核减金额(元)'"
            prop="hjmoney"
          >
            <el-input
              v-model.number="formData.hjmoney"
              :style="{ width: '100%' }"
              :placeholder="
                formData.served == '经责'
                  ? '请输入扣减金额(元)'
                  : '请输入核减金额(元)'
              "
              @input="inputMoney($event, 'hjmoney')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计认定金额(元)" prop="sdmoney">
            <el-input
              v-model.number="formData.sdmoney"
              :style="{ width: '100%' }"
              placeholder="请输入审计认定金额(元)"
              @input="inputMoney($event, 'sdmoney')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="问题描述" label-width="140px" prop="overview">
            <el-input
              type="textarea"
              placeholder="请输入问题描述"
              :rows="5"
              v-model="formData.overview"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="审计意见"
            label-width="140px"
            prop="auditopinion"
          >
            <el-input
              type="textarea"
              placeholder="请输入审计意见"
              :rows="5"
              v-model="formData.auditopinion"
            ></el-input>
          </el-form-item>
        </el-col>

        <el-col :span="24" v-if="formData.served == '基建'">
          <el-divider>子项表</el-divider>
        </el-col>
        <el-col
          :span="24"
          style="margin-bottom: 20px"
          v-if="formData.served == '基建'"
        >
          <div style="text-align: right; margin-bottom: 5px">
            <el-button
              type="success"
              @click="$refs.addchild.showEdit(null, 'add')"
            >
              新增
            </el-button>
          </div>
          <el-table :data="tableData1">
            <el-table-column align="center" label="编号" prop="resultcode">
              <template #default="{ row }">
                <el-button
                  type="text"
                  :disabled="false"
                  @click="$refs.addchild.showEdit(row, 'detail')"
                >
                  {{ row.resultcode }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="审计项目名称"
              prop="projectname"
            />
            <el-table-column
              align="center"
              label="被审计单位名称"
              prop="orgidnames"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="合同编号"
              prop="contractcode"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="合同名称"
              prop="contractname"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="120"
            >
              <template #default="{ row, $index }">
                <el-button
                  type="text"
                  @click="$refs.addchild.showEdit(row, 'edit')"
                >
                  修改
                </el-button>
                <el-button type="text" @click="handleTableDelete(row, $index)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <!-- <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
              :before-upload="handleBeforeUpload"
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
              <div v-if="!disabled" style="margin-right: 10px">
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
                  :disabled="false"
                  @click="handleDowns(row)"
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
      <el-button @click="save" type="primary" :loading="loading">
        确定
      </el-button>
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>

    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
    <AddChild ref="addchild" @AddChild="onAddChild" />
    <SelectDepartment ref="company" @submit="selectedCompany" />
    <ProcessList ref="process" @fetchData="close" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import {
    resultGetone,
    resultSaveOrUpdate,
    getattList,
  } from '@/oapi/audit/implement'
  import { deleteFile, download } from '@/oapi/audit/report'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import AddChild from './addChild.vue'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import { getCurrSsProject } from '@/oapi/audit/project'
  const token = store.getters['user/token']
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { DepartmentOptions, AddChild, SelectDepartment, ProcessList },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        // baseApi: baseURL,
        // api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        fileList: [],
        baseApi: baseURL,
        importApi: '/audit/auditPlan/mergePlanProjectManageInfoImport',
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        tableData: [],
        tableData1: [],
        formData: {
          scoreItems: [],
          resultcode: '',
          projectname: '',
          orgidnames: '',
          contractcode: '',
          contractname: '',
          sgorgname: '',
          contractmoney: '',
          hzmoney: '',
          hjmoney: '',
          sdmoney: '',
          auditopinion: '',
          overview: '',
          served: '',
        },
        footer: true,
        rules: {
          resultcode: [
            {
              required: true,
              message: '请输入编号',
              trigger: 'blur',
            },
          ],
          orgidnames: [
            {
              required: true,
              message: '请输入被审计单位名称',
              trigger: 'blur',
            },
          ],
          contractcode: [
            {
              required: true,
              message: '请输入合同编号',
              trigger: 'blur',
            },
          ],
          projectname: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'blur',
            },
          ],
          contractname: [
            {
              required: true,
              message: '请输入合同名称',
              trigger: 'blur',
            },
          ],
          sgorgname: [
            {
              required: true,
              message: this.isJZK ? '请输入供应商' : '请输入施工单位',
              trigger: 'blur',
            },
          ],
          contractmoney: [
            {
              required: true,
              message: '请输入报审金额(元)',
              trigger: 'blur',
            },
          ],
          hzmoney: [
            {
              required: true,
              message: '请输入核增金额(元)',
              trigger: 'blur',
            },
          ],
          hjmoney: [
            {
              required: true,
              message: '请输入核减金额(元)',
              trigger: 'blur',
            },
          ],
          sdmoney: [
            {
              required: true,
              message: '请输入审计认定金额(元)',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        depType: '',
        typeRow: null,
        isJZK: false,
        Zrow: {},
        editId: '',
      }
    },
    created() {
      //判断权限是否有经责科.展示不同title
      let userInfo = JSON.parse(localStorage.getItem('userInfo'))
      if (!userInfo.roleNames.includes('经责科审计人员')) {
        this.isJZK = false
      } else {
        this.isJZK = true
      }
    },
    methods: {
      //金额输入
      inputMoney(value, key) {
        // 移除非数字字符和小数点
        let sanitizedValue = value.replace(/[^0-9.]/g, '')
        // 如果输入的是小数点，确保只有一个小数点
        if (sanitizedValue.indexOf('.') !== sanitizedValue.lastIndexOf('.')) {
          sanitizedValue = sanitizedValue.slice(
            0,
            sanitizedValue.lastIndexOf('.')
          )
        }
        // 如果输入的是0开头且后面有其他数字，去掉开头的0
        if (
          sanitizedValue.startsWith('0') &&
          sanitizedValue.length > 1 &&
          sanitizedValue[1] !== '.'
        ) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是小数点开头，前面加0
        if (sanitizedValue.startsWith('.')) {
          sanitizedValue = '0' + sanitizedValue
        }
        // 如果输入的是负数，去掉负号
        if (sanitizedValue.startsWith('-')) {
          sanitizedValue = sanitizedValue.slice(1)
        }
        // 如果输入的是空字符串或0，设置为空字符串
        // if (sanitizedValue === '' || sanitizedValue === '0') {
        if (sanitizedValue === '') {
          sanitizedValue = ''
        }
        // 更新输入框的值
        const keys = key.split('.')
        let formDataRef = this.formData
        for (let i = 0; i < keys.length - 1; i++) {
          formDataRef = formDataRef[keys[i]]
        }
        formDataRef[keys[keys.length - 1]] = sanitizedValue
      },
      // 上下移动
      handleMove(index, dir) {
        const curOptionData = this.formData.scoreItems.splice(index, 1)[0]
        const listData = JSON.parse(JSON.stringify(this.formData.scoreItems))
        const len = listData.length
        let _index = 0
        if (dir === 'up') {
          _index = index <= 0 ? 0 : index - 1
        } else if (dir === 'down') {
          _index = index >= len ? len : index + 1
        }

        listData.splice(_index, 0, curOptionData)

        this.formData.scoreItems = listData
      },

      // 删除归属重点项
      removeItem(index) {
        this.postForm.scoreItems.splice(index, 1)
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      openDep(type) {
        this.depType = type
        this.$refs.department.show()
      },
      handleDepartmentSelected(node) {
        console.log('node', node)
        this.$set(this.formData, this.depType, node.label)
        if (this.depType == 'orgidnames') {
          this.$set(this.formData, `orgids`, node.id)
        } else if (this.depType == 'sgorgname') {
          this.$set(this.formData, `sgorgid`, node.id)
        }
      },

      // handleBeforeUpload(file) {
      //   const isLt2M = file.size / 1024 / 1024 < 100
      //   if (!isLt2M) {
      //     this.$message.error('文件大小不能超过 200MB!')
      //   }
      //   return isLt2M
      // },
      // 获取当前实施项目
      async getCurrentProject() {
        let obj = {}
        const { data } = await getCurrSsProject()
        obj = data.pj
        return obj
      },
      async showEdit(row, title, type) {
        this.typeRow = type
        this.dialogFormVisible = true
        if (row) {
          this.editId = row.resultid
          const res = await resultGetone({ resultid: row.resultid })
          if (res && res.data && res.data.data) {
            Object.assign(this.formData, res.data.data)
            this.tableData1 = res.data.data.zixbs
            // this.tableData = res.data.data.zixbs
          }
          const data = await getattList({ resultid: row.resultid })
          this.tableData = data.data
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          this.formData.served = type
          if (type == '专项') {
            this.rules.sgorgname = [
              {
                required: true,
                message: '请输入合同相对方/人',
                trigger: 'blur',
              },
            ]
          } else {
            this.rules.sgorgname = [
              {
                required: true,
                message: this.isJZK ? '请输入供应商' : '请输入施工单位',
                trigger: 'blur',
              },
            ]
          }
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          const currentProject = await this.getCurrentProject()
          this.formData.projectname = currentProject.projectName
          this.formData.projectid = currentProject.id
          this.formData.orgidnames = currentProject.auditOrgName
          this.formData.orgids = currentProject.auditOrgId
        }
      },
      async showEditWithRow(row, title, type, Zrow) {
        this.typeRow = type
        this.dialogFormVisible = true
        if (row) {
          this.editId = row.resultid
          const res = await resultGetone({ resultid: row.resultid })
          if (res && res.data && res.data.data) {
            Object.assign(this.formData, res.data.data)
            this.tableData1 = res.data.data.zixbs
            // this.tableData = res.data.data.zixbs
          }
          const data = await getattList({ resultid: row.resultid })
          this.tableData = data.data
        }
        this.Zrow = Zrow
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          this.formData.served = type
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          const currentProject = await this.getCurrentProject()
          this.formData.projectname = currentProject.projectName
          this.formData.projectid = currentProject.id
          this.formData.orgidnames = currentProject.auditOrgName
          this.formData.orgids = currentProject.auditOrgId
        }
      },
      close() {
        this.formData.scoreItems = []
        this.formData.resultcode = ''
        this.formData.projectname = ''
        this.formData.orgidnames = ''
        this.formData.contractcode = ''
        this.formData.contractname = ''
        this.formData.sgorgname = ''
        this.formData.contractmoney = ''
        this.formData.hzmoney = ''
        this.formData.hjmoney = ''
        this.formData.sdmoney = ''
        this.formData.auditopinion = ''
        this.formData.overview = ''
        this.formData.served = ''
        this.dialogFormVisible = false
        this.footer = true
        this.tableData = []
        this.tableData1 = []
        this.editId = ''
        this.$emit('fetchData')
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            let zixbids = ''
            this.tableData1.map((item) => {
              zixbids += item.resultid
              zixbids += ','
            })
            zixbids = zixbids.substring(0, zixbids.length - 1)
            let params = { ...this.formData, ...this.Zrow }

            delete params.zixbs
            delete params.realname
            if (this.editId) {
              params.resultid = this.editId
            }
            const data = await resultSaveOrUpdate({
              ...params,
              attids,
              zixbids,
            })
            if (data.code == 1) {
              this.$baseMessage(data.msg, 'success')
              this.$emit('fetchData')
              // this.close()
              this.editId = data.data.data.resultid
            } else {
              this.$baseMessage(data.msg, 'error')
            }
            this.loading = false
          }
        })
      },
      // async handleDown(row) {
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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        await deleteFile({ attId: row.attid })
      },
      async handleTableDelete(index) {
        this.tableData1.splice(index, 1)
        this.calculateTotalAmount(this.tableData1)
      },
      // handlePreview(file) {},
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
      onAddChild(rowData) {
        let index = this.tableData1.findIndex(
          (item) => item.resultid == rowData.resultid
        )
        if (index !== -1) {
          this.$set(this.tableData1, index, rowData)
        } else {
          this.tableData1.push(rowData) // 如果ID不同，或者不存在，添加进数组
        }
        this.calculateTotalAmount(this.tableData1)
      },
      calculateTotalAmount(items) {
        let contractmoney = 0
        let hzmoney = 0
        let hjmoney = 0
        let sdmoney = 0
        items.forEach((item) => {
          contractmoney += Number(item.contractmoney)
          hzmoney += Number(item.hzmoney)
          hjmoney += Number(item.hjmoney)
          sdmoney += Number(item.sdmoney)
        })
        this.formData.contractmoney = contractmoney
        this.formData.hzmoney = hzmoney
        this.formData.hjmoney = hjmoney
        this.formData.sdmoney = sdmoney
      },
      selectedCompany(node) {
        this.$set(this.formData, `orgids`, node.id)
        this.$set(this.formData, `orgidnames`, node.name)
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(151, this.editId)
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
  .el-form-item__contractname span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
