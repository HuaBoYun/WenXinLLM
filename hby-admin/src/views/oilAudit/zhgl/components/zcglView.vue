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
          <el-form-item label="类别" prop="assetType">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.assetType"
              placeholder="请选择类别"
            >
              <el-option label="上移资产" :value="0" />
              <el-option label="审计部上市" :value="1" />
              <el-option label="审计部未上市" :value="2" />
              <el-option label="审计中心上市" :value="3" />
              <el-option label="审计中心未上市" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产编码" prop="assetCode">
            <el-input
              v-model="formData.assetCode"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入资产编码"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产名称" prop="assetName">
            <el-input
              v-model="formData.assetName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入资产名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="规格型号" prop="specificationType">
            <el-input
              v-model="formData.specificationType"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入规格型号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="制造厂家" prop="manufacturer">
            <el-input
              v-model="formData.manufacturer"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入制造厂家"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属单位编码" prop="unitCode">
            <el-input
              v-model="formData.unitCode"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入所属单位编码"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属单位名称" prop="unitName">
            <el-input
              v-model="formData.unitName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入所属单位名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="车牌井号" prop="licensePlate">
            <el-input
              v-model="formData.licensePlate"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入车牌井号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="自编号" prop="selfNum">
            <el-input
              v-model="formData.selfNum"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入自编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出厂编号" prop="factoryNumber">
            <el-input
              v-model="formData.factoryNumber"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入出厂编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出厂、建筑或完井日期" prop="factoryTime">
            <el-date-picker
              v-model="formData.factoryTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择出厂、建筑或完井日期"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="投产日期" prop="productionTime">
            <el-date-picker
              v-model="formData.productionTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择投产日期"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="区块" prop="block">
            <el-input
              v-model="formData.block"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入区块"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="区块名称" prop="blockName">
            <el-input
              v-model="formData.blockName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入区块名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="存放（安装）地点" prop="depositPlace">
            <el-input
              v-model="formData.depositPlace"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入存放（安装）地点"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="计量单位" prop="measurement">
            <el-input
              v-model="formData.measurement"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入计量单位"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="复合数量" prop="compositeQuantity">
            <el-input
              v-model="formData.compositeQuantity"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入复合数量"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="功率能力" prop="powerCapacity">
            <el-input
              v-model="formData.powerCapacity"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入功率能力"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="技术状况名称" prop="technicalConditionName">
            <el-input
              v-model="formData.technicalConditionName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入技术状况名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="使用状态名称" prop="usageStatusName">
            <el-input
              v-model="formData.usageStatusName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入使用状态名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="增加原因名称" prop="addCauseName">
            <el-input
              v-model="formData.addCauseName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入增加原因名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资金渠道名称" prop="fundSchannelName">
            <el-input
              v-model="formData.fundSchannelName"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入资金渠道名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="增加日期" prop="fiaddTimeeld23">
            <el-date-picker
              v-model="formData.addTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择增加日期"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="停产日期" prop="discontinuedTime">
            <el-date-picker
              v-model="formData.discontinuedTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择停产日期"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="保管人" prop="custodianName">
            <el-input
              v-model="formData.custodianName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择保管人"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="showPeople('save')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="期末原值" prop="finalOriginalValue">
            <el-input
              v-model="formData.finalOriginalValue"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入期末原值"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="期末累计折旧" prop="finalCumulativeDepreciation">
            <el-input
              v-model="formData.finalCumulativeDepreciation"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入期末累计折旧"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="期末净值" prop="finalNetWorth">
            <el-input
              v-model="formData.finalNetWorth"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入期末净值"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="期末减值准备" prop="finalImpairmentPrepare">
            <el-input
              v-model="formData.finalImpairmentPrepare"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入期末减值准备"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="在用人员" prop="usePeopleName">
            <el-input
              v-model="formData.usePeopleName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择在用人员"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="showPeople('onUse')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="在用部门" prop="useDepartmentName">
            <el-input
              v-model="formData.useDepartmentName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择在用部门"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="$refs.department.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="特殊事项" prop="specialMatter">
            <el-input
              v-model="formData.specialMatter"
              :style="{ width: '100%' }"
              type="textarea"
              placeholder="请输入特殊事项"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
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
                <el-button type="text" @click="handleDowns(row)">
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
    </div>
    <!-- 选择人员弹窗 -->
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <!-- 选择部门弹窗 -->
    <department-options ref="department" @submit="handleDepartmentSelected" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import {
    deleteFileInfo,
    getUserDetailInfo,
  } from '@/oapi/ypns_zhgl/filePublic'
  import { download } from '@/oapi/audit/report'
  import DepartmentOptions from '@/views/oilAudit/jhlx/components/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'

  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/zygl'
  const token = store.getters['user/token']
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    components: { DepartmentOptions, ExecutorOptions },
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
        formData: {
          addCauseName: '',
          addTime: '',
          assetCode: '',
          assetName: '',
          assetType: '',
          block: '',
          blockName: '',
          compositeQuantity: '',
          custodian: '',
          custodianName: '',
          depositPlace: '',
          discontinuedTime: '',
          factoryNumber: '',
          factoryTime: '',
          fileIds: '',
          finalCumulativeDepreciation: '',
          finalImpairmentPrepare: '',
          finalNetWorth: '',
          finalOriginalValue: '',
          fundSchannelName: '',
          id: '',
          licensePlate: '',
          manufacturer: '',
          measurement: '',
          powerCapacity: '',
          productionTime: '',
          remark: '',
          selfNum: '',
          specialMatter: '',
          specificationType: '',
          technicalConditionName: '',
          unitCode: '',
          unitName: '',
          usageStatusName: '',
          useDepartment: '',
          useDepartmentName: '',
          usePeople: '',
          usePeopleName: '',
        },
        peopleType: '',
        footer: true,
        rules: {
          field2: [
            { required: true, message: '请输入资产编码', trigger: 'blur' },
          ],
          field3: [
            { required: true, message: '请输入资产名称', trigger: 'blur' },
          ],
          field4: [
            { required: true, message: '请输入规格型号', trigger: 'blur' },
          ],
          field5: [
            { required: true, message: '请输入制造厂家', trigger: 'blur' },
          ],
          field6: [
            { required: true, message: '请输入所属单位编码', trigger: 'blur' },
          ],
          field11: [
            {
              required: true,
              message: '请输入出厂、建筑或完井日期',
              trigger: 'blur',
            },
          ],
          field12: [
            { required: true, message: '请输入投产日期', trigger: 'blur' },
          ],
          field23: [
            { required: true, message: '请输入增加日期', trigger: 'blur' },
          ],
          field26: [
            { required: true, message: '请输入期末原值', trigger: 'blur' },
          ],
          field27: [
            { required: true, message: '请输入期末累计折旧', trigger: 'blur' },
          ],
          field28: [
            { required: true, message: '请输入期末净值', trigger: 'blur' },
          ],
          field29: [
            { required: true, message: '请输入期末减值准备', trigger: 'blur' },
          ],
          field30: [
            { required: true, message: '请输入在用人员', trigger: 'blur' },
          ],
          field31: [
            { required: true, message: '请输入在用部门', trigger: 'blur' },
          ],
          field32: [
            { required: true, message: '请输入特殊事项', trigger: 'blur' },
          ],
          field33: [{ required: true, message: '请输入备注', trigger: 'blur' }],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      showPeople(type) {
        this.peopleType = type
        this.$refs.executor.showEdit()
      },
      // 选择经办人
      async handleExecutorSelected(node) {
        if (this.peopleType === 'save') {
          this.$set(this.formData, 'custodianName', node[0].realname)
          this.$set(this.formData, 'custodian', node[0].staffid)
        } else if (this.peopleType === 'onUse') {
          this.$set(this.formData, 'usePeopleName', node[0].realname)
          this.$set(this.formData, 'usePeople', node[0].staffid)
        }
        this.$forceUpdate()
      },
      // 选择部门
      handleDepartmentSelected(node) {
        this.$set(this.formData, `useDepartmentName`, node.label)
        this.$set(this.formData, `useDepartment`, node.id)
        this.$forceUpdate()
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          const res = await getInfoDetail({ id: row.id })

          this.formData.addCauseName = res.data.data.addCauseName
          this.formData.addTime = res.data.data.addTime
          this.formData.assetCode = res.data.data.assetCode
          this.formData.assetName = res.data.data.assetName
          this.formData.assetType = +res.data.data.assetType
          this.formData.block = res.data.data.block
          this.formData.blockName = res.data.data.blockName
          this.formData.compositeQuantity = res.data.data.compositeQuantity
          this.formData.custodian = res.data.data.custodian
          this.formData.custodianName = res.data.data.custodianName
          this.formData.depositPlace = res.data.data.depositPlace
          this.formData.discontinuedTime = res.data.data.discontinuedTime
          this.formData.factoryNumber = res.data.data.factoryNumber
          this.formData.factoryTime = res.data.data.factoryTime
          this.formData.finalCumulativeDepreciation =
            res.data.data.finalCumulativeDepreciation
          this.formData.finalImpairmentPrepare =
            res.data.data.finalImpairmentPrepare
          this.formData.finalNetWorth = res.data.data.finalNetWorth
          this.formData.finalOriginalValue = res.data.data.finalOriginalValue
          this.formData.fundSchannelName = res.data.data.fundSchannelName
          this.formData.id = res.data.data.id
          this.formData.licensePlate = res.data.data.licensePlate
          this.formData.manufacturer = res.data.data.manufacturer
          this.formData.measurement = res.data.data.measurement
          this.formData.powerCapacity = res.data.data.powerCapacity
          this.formData.productionTime = res.data.data.productionTime
          this.formData.remark = res.data.data.remark
          this.formData.selfNum = res.data.data.selfNum
          this.formData.specialMatter = res.data.data.specialMatter
          this.formData.specificationType = res.data.data.specificationType
          this.formData.technicalConditionName =
            res.data.data.technicalConditionName
          this.formData.unitCode = res.data.data.unitCode
          this.formData.unitName = res.data.data.unitName
          this.formData.usageStatusName = res.data.data.usageStatusName
          this.formData.useDepartment = res.data.data.useDepartment
          this.formData.useDepartmentName = res.data.data.useDepartmentName
          this.formData.usePeople = res.data.data.usePeople
          this.formData.usePeopleName = res.data.data.usePeopleName
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            createdUser: resL,
            createdTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData.addCauseName = ''
        this.formData.addTime = ''
        this.formData.assetCode = ''
        this.formData.assetName = ''
        this.formData.assetType = ''
        this.formData.block = ''
        this.formData.blockName = ''
        this.formData.compositeQuantity = ''
        this.formData.custodian = ''
        this.formData.custodianName = ''
        this.formData.depositPlace = ''
        this.formData.discontinuedTime = ''
        this.formData.factoryNumber = ''
        this.formData.factoryTime = ''
        this.formData.fileIds = ''
        this.formData.finalCumulativeDepreciation = ''
        this.formData.finalImpairmentPrepare = ''
        this.formData.finalNetWorth = ''
        this.formData.finalOriginalValue = ''
        this.formData.fundSchannelName = ''
        this.formData.id = ''
        this.formData.licensePlate = ''
        this.formData.manufacturer = ''
        this.formData.measurement = ''
        this.formData.powerCapacity = ''
        this.formData.productionTime = ''
        this.formData.remark = ''
        this.formData.selfNum = ''
        this.formData.specialMatter = ''
        this.formData.specificationType = ''
        this.formData.technicalConditionName = ''
        this.formData.unitCode = ''
        this.formData.unitName = ''
        this.formData.usageStatusName = ''
        this.formData.useDepartment = ''
        this.formData.useDepartmentName = ''
        this.formData.usePeople = ''
        this.formData.usePeopleName = ''
        this.tableData = []
        this.dialogFormVisible = false
        this.loading = false
        this.footer = true
        this.$emit('fetchData')
      },
      // 附件列表 下载附件
      // async handleDown(row) {
      //   const data = await download({ attId: row.attid })
      //   console.log(data)
      //   let filename = row.attname
      //   let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
      //   console.log(blob)
      //   let url = window.URL.createObjectURL(blob, {
      //     type: 'application/vnd.ms-excel',
      //   })
      //   const link = document.createElement('a')
      //   link.style.display = 'none'
      //   link.href = url
      //   console.log(url)
      //   link.setAttribute('download', filename)
      //   document.documentElement.appendChild(link)
      //   link.click()
      //   document.documentElement.removeChild(link)
      // },
      // 附件列表 删除附件
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        await deleteFileInfo({ id: row.attid })
        this.tableData = list
      },
      // handlePreview(file) {},
      // // 附件上传成功
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
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            const params = JSON.parse(JSON.stringify(this.formData))
            let fileIds = ''
            this.tableData.map((item) => {
              fileIds += item.attid
              fileIds += ','
            })
            fileIds = fileIds.substring(0, fileIds.length - 1)
            const res = await editInfor({
              ...params,
              fileIds,
            })
            if (res && res.code == 200) {
              this.formData.id = res.data.id
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '保存失败',
                type: 'error',
              })
            }
            this.loading = false
          }
        })
      },
      async checkLink() {
        return
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await LinkTest({
              dataBaseConnectionAddress:
                this.formData.dataBaseConnectionAddress,
              dataBasePassWord: this.formData.dataBasePassWord,
              dataBaseType: this.formData.dataBaseType,
              dataBaseUsers: this.formData.dataBaseUsers,
            })
            if (res.code == 1) {
              this.$message({
                message: res.msg,
                type: 'success',
              })
            } else {
              this.$message({
                message: res.msg,
                type: 'error',
              })
            }
          }
        })
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
</style>
