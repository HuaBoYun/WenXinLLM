<template>
  <!-- 三级单位离任审计 -->
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="新增"
    :visible.sync="dialogJdVisible"
    width="1000px"
    @close="close"
    v-if="dialogJdVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级"
            prop="secrectlevelid"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="formData.secrectlevelid"
              clearable
              placeholder="密级"
              style="width: 100%"
              @change="changeMJ"
              :disabled="disabled || type == 1"
            >
              <el-option
                v-for="item in MJoption"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="showMJ">
          <el-form-item label="知悉范围" prop="staffscopenames">
            <el-input
              v-model="formData.staffscopenames"
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '75%' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="handleBatchIssue('mj')"
              :disabled="!formData.secrectlevelid || disabled || type == 1"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="showMJ">
          <el-divider>基本信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="季度" prop="quartername">
            <el-select
              v-model="formData.quartername"
              placeholder="请选择季度"
              clearable
              :style="{ width: '100%' }"
              :disabled="disabled || type == 1"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="年度" prop="riskyear">
            <el-date-picker
              v-model="formData.riskyear"
              type="year"
              value-format="yyyy"
              placeholder="选择年"
              :style="{ width: '100%' }"
              :disabled="disabled || type == 1"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报单位" prop="linkOrgName">
            <el-input
              v-model="formData.linkOrgName"
              disabled
              placeholder="请输入填报单位"
            />
            <!-- <el-button
              @click="handleObject"
              style="margin-left: 10px"
              type="primary"
              :disabled="disabled"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人">
            <el-input
              v-model="formData.createname"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间">
            <el-input
              v-model="formData.createdTime"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="margin-bottom: 5px; display: flex; justify-content: right"
          >
            <el-button type="primary" @click="handleDownloadTemplate">
              下载模板
            </el-button>
            <el-button type="success" @click="handleUploadClick">
              导入
            </el-button>
            <el-button
              type="success"
              @click="add()"
              v-if="!disabled && type == 0"
            >
              新增
            </el-button>
            <el-button
              type="success"
              @click="batchDelete()"
              v-if="!disabled && type == 0"
            >
              批量删除
            </el-button>

            <el-upload
              style="display: none"
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleImportSuccess"
              :data="{ majorid: formData.id }"
              ref="upload"
            ></el-upload>
            <el-button
              v-show="type"
              type="success"
              @click="handleBatchIssue('xf')"
              :disabled="!selectedRows.length"
              style="margin-left: 10px"
            >
              选择下发人员
            </el-button>

            <el-button
              v-show="type"
              type="success"
              @click="handleSubmit"
              :disabled="allDataIssued"
            >
              确认下发
            </el-button>
          </div>
          <el-table
            v-loading="listLoading"
            :data="tableData"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              type="selection"
              width="55"
              align="center"
              :selectable="(row) => row.toIssued !== 1"
            />
            <el-table-column align="center" label="风险名称" prop="impRiskName">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDetail(row)"
                  style="white-space: pre-line; line-height: 16px"
                >
                  {{ row.impRiskName }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="牵头领导"
              prop="impWayStaffName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="牵头责任部门"
              prop="impWayDeptName"
              show-overflow-tooltip
            ></el-table-column>
            <el-table-column
              align="center"
              label="公司相关责任单位"
              prop="impDutyUnitName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="重点事项"
              prop="priorities"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="风险描述"
              prop="impRiskDetails"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="下发人员"
              prop="lssuedStaffName"
              v-if="type == 1 || title == 'detail'"
            />
            <el-table-column
              align="center"
              label="下发时间"
              prop="impLssuedDate"
              v-if="type == 1 || title == 'detail'"
            />
            <el-table-column align="center" label="是否下发" prop="toIssued">
              <template #default="{ row }">
                <div v-if="row.toIssued == 1">已下发</div>
                <div v-else>未下发</div>
              </template>
            </el-table-column>

            <el-table-column
              align="center"
              label="操作"
              width="180"
              v-if="!disabled && type == 0"
            >
              <template #default="scope">
                <el-button type="text" @click="add(scope.row, scope.$index)">
                  修改
                </el-button>

                <el-button type="text" @click="handleDelete(scope.row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            v-if="title == 'detail'"
            class="pagination"
            background
            :current-page="queryForm.pageNumber"
            :layout="layout"
            :page-size="queryForm.pageSize"
            :total="total"
            @current-change="handleCurrentChange"
            @size-change="handleSizeChange"
            :page-sizes="[5, 10, 20, 50]"
          />
        </el-col>
      </el-form>
    </el-row>
    <createEdit ref="edit" @fetchData="fetchData"></createEdit>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <ProcessList ref="process" @fetchData="close" />
    <SelectPersonModal
      ref="SelectPersonModal"
      @projectManage="selectP"
      :multiple="true"
    />
    <div slot="footer" v-if="!disabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" v-if="type == 0">确定</el-button>
    </div>
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </el-dialog>
</template>

<script>
  import {
    createSaveOrUpdate,
    delMajorRiskDelete,
    updateMajorIssued,
    getMajorDetails,
    majorIssuedValidate,
  } from '@/api/risk/create'
  import createEdit from './createEdit2.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { formatDay } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  import SelectPersonModal from '@/components/duoxuanPerson.vue'

  const token = store.getters['user/token']
  import ZXPerson from '@/components/selectPerson.vue'
  import { hasMJ, couldMJ } from '@/utils'
  import { getMJ } from '@/api/setting/mjsz'
  import { xiafaListNew } from '@/oapi/audit/preparation'
  export default {
    components: {
      createEdit,
      SelectDepartment,
      ProcessList,
      SelectPersonModal,
      ZXPerson,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/riskcontrol/majorRisk/importMjorRisk',
        headers: { token },
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 5,
          id: '',
        },
        total: 0,
        listLoading: false,
        tableData: [],
        formData: {
          quartername: '', //季度
          linkOrgName: JSON.parse(localStorage.getItem('userInfo')).linkOrg
            .orgname, //填报单位
          linkOrgId: JSON.parse(localStorage.getItem('userInfo')).linkOrg.orgid, //填报单位id
          createdTime: formatDay(new Date().toString()),
          createname: JSON.parse(localStorage.getItem('userInfo')).realname,
          id: '',
          riskyear: '', //年度
          secrectlevelid: undefined,
          staffscopenames: undefined,
          staffscopeids: undefined,
        },
        rules: {
          quartername: [
            {
              required: true,
              message: '请选择季度',
              trigger: 'change',
            },
          ],
          linkOrgName: [
            {
              required: true,
              message: '请输入填报单位',
              trigger: 'change',
            },
          ],
          riskyear: [
            {
              required: true,
              message: '请选择年度',
              trigger: 'change',
            },
          ],
        },
        dialogJdVisible: false,
        options: [
          {
            value: '一季度',
            label: '一季度',
          },
          {
            value: '二季度',
            label: '二季度',
          },
          {
            value: '三季度',
            label: '三季度',
          },
          {
            value: '四季度',
            label: '四季度',
          },
        ],
        disabled: false,
        editId: '',
        selectedRows: [], // 新增选中行数据
        type: false, //是否点击下发进入
        showMJ: false,
        MJoption: [],
        fromType: '',
      }
    },
    computed: {
      // 判断是否所有数据都已下发
      allDataIssued() {
        if (!this.tableData || this.tableData.length === 0) {
          return true // 如果没有数据，认为都已下发，禁用按钮
        }
        return this.tableData.every((row) => row.toIssued === 1)
      },
    },
    mounted() {
      // this.getOption()
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        const res = await hasMJ('riskcreate')
        this.menuId = res[0].menuid
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    methods: {
      // 处理导入成功事件
      handleImportSuccess(response) {
        if (!this.formData.id) {
          this.$message.error('请先保存基本信息后再进行导入')
          return
        }
        if (response.code === 1) {
          this.getDetail()
          this.$baseMessage(response.msg, 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },
      changeMJ(selectedValue) {
        console.log('🚀 ~ changeMJ ~ selectedValue:', selectedValue)
        const selectedItem = this.MJoption.find(
          (item) => item.levelId == selectedValue
        )
        console.log('🚀 ~ changeMJ ~  this.MJoption:', this.MJoption)
        console.log('🚀 ~ changeMJ ~ selectedItem:', selectedItem)
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffscopenames = '全部人员'
            this.formData.staffscopeids = ''
          } else {
            this.formData.staffscopeids = ''
            this.formData.staffscopenames = ''
          }
        }
      },
      async showEdit(row, title, type) {
        this.dialogJdVisible = true
        this.title = title
        this.disabled = title == 'detail'
        // 初始化selectedRows
        this.tableData = []
        this.selectedRows = []
        if (row) {
          this.editId = row.id
          const {
            data: {
              data: { data, list },
            },
          } = await getMajorDetails({ id: row.id })
          this.formData.quartername = data.quartername
          this.$set(this.formData, 'riskyear', String(data.riskyear))
          this.$set(this.formData, 'linkOrgName', data.linkOrgName)
          this.$set(this.formData, 'linkOrgId', data.linkOrgId)
          this.$set(this.formData, 'createdTime', data.createdate)
          this.$set(this.formData, 'createname', data.createname)
          this.$set(this.formData, 'createdTime', data.createtime)
          this.$set(this.formData, 'id', data.id)
          this.$set(this.formData, 'status', data.status)
          this.$set(this.formData, 'secrectlevelid', data.secrectlevelid)
          this.$set(this.formData, 'staffscopenames', data.staffscopenames)
          this.$set(this.formData, 'staffscopeids', data.staffscopeids)
          this.tableData = list
        }
        if (type) {
          this.type = true
        } else {
          this.type = false
        }
      },
      close() {
        this.formData = {
          quartername: '', //季度
          linkOrgName: JSON.parse(localStorage.getItem('userInfo')).linkOrg
            .orgname, //填报单位
          linkOrgId: JSON.parse(localStorage.getItem('userInfo')).linkOrg.orgid, //填报单位id
          createdTime: formatDay(new Date().toString()),
          createname: JSON.parse(localStorage.getItem('userInfo')).realname,
          id: '',
          riskyear: '', //年度
          secrectlevelid: '',
          staffscopenames: '',
          staffscopeids: '',
        }
        this.fromType = ''
        this.tableData = []
        this.selectedRows = []
        this.dialogJdVisible = false
        this.editId = ''
        this.$emit('fetchData')
      },
      add(row, i) {
        if (!this.formData.id) {
          return this.$message.error('请先保存')
        }
        if (row) {
          this.$refs['edit'].showEdit({
            id: row.id,
            fatherId: this.formData.id,
          })
        }
        this.$refs['edit'].showEdit({ fatherId: this.formData.id })
      },
      async fetchData({ data: data }) {
        if (data) {
          const existingIndex = this.tableData.findIndex(
            (item) => item.id === data.id
          )
          if (existingIndex > -1) {
            // 如果存在相同id的数据，则替换
            this.$set(this.tableData, existingIndex, data)
          } else {
            // 如果不存在，则添加到数组末尾
            this.tableData.push(data)
          }
        }
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getList()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getList()
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await delMajorRiskDelete({ id: row.id })
          if (res.code == 1) {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            let list = this.tableData
            list = list.filter((item) => item.id != row.id)
            this.tableData = list
            // 删除后不需要调用fetchData，因为我们已经手动更新了tableData
          }
        })
      },
      // 添加批量删除方法
      batchDelete() {
        if (this.selectedRows.length === 0) {
          this.$message.error('请先选择要删除的项')
          return
        }
        this.$baseConfirm('你确定要删除选中的项吗', null, async () => {
          try {
            // 获取所有选中行的ID并转为逗号分隔的字符串
            const ids = this.selectedRows.map((row) => row.id).join(',')
            // 一次请求删除所有选中项
            const res = await delMajorRiskDelete({ id: ids })
            if (res && res.code == 1) {
              this.getDetail()
              this.selectedRows = []
              this.$baseMessage(
                '批量删除成功',
                'success',
                'vab-hey-message-success'
              )
            } else {
              this.$message.error(res?.msg || '批量删除失败')
            }
          } catch (error) {
            console.error('批量删除出错:', error)
            this.$message.error('批量删除过程中出现错误')
          }
        })
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.linkOrgName = val.label
        this.formData.linkOrgId = val.id
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await createSaveOrUpdate({
              ...this.formData,
            })
            if (res && res.code === 1) {
              this.editId = res.data.data.id
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
              this.formData = { ...this.formData, ...res.data.data.data }
              this.$emit('fetchData')
            } else {
              this.$message({
                message: '保存失败',
                type: 'error',
              })
            }
          }
        })
      },
      async handleSubmit() {
        try {
          // 过滤出未下发的数据（toIssued 不等于 1 的数据）
          const unIssuedData = this.tableData.filter((row) => row.toIssued != 1)

          // 如果没有未下发的数据
          if (unIssuedData.length === 0) {
            this.$message.warning('所有数据已经下发，无需再次下发')
            return
          }

          // 检查未下发的数据是否有选择下发人员
          const hasEmptyStaff = unIssuedData.some(
            (row) => !row.lssuedStaffName || row.lssuedStaffName.trim() === ''
          )
          if (hasEmptyStaff) {
            this.$message.error(
              '列表中有未下发的数据没有选择下发人员，请先选择下发人员'
            )
            return
          }

          this.$baseConfirm('是否确认下发,下发后将不能调整', null, async () => {
            const res = await majorIssuedValidate({
              majorid: String(this.formData.id),
            })
            if (res.code == 1) {
              this.$baseMessage('下发成功', 'success')

              // 只遍历未下发的数据
              const promises = []
              for (const row of unIssuedData) {
                // 如果有下发人员ID，拆分为数组
                if (row.impLssuedStaffid) {
                  const staffIds = row.impLssuedStaffid.split(',')
                  const xiafaDataArray = []

                  // 为每个接收人创建一条数据
                  staffIds.forEach((staffId) => {
                    xiafaDataArray.push({
                      formId: this.editId,
                      distributionTitle: row.impRiskName,
                      reciver: staffId,
                      isread: 0,
                      moduleType: 'fxgk',
                    })
                  })

                  // 为每行数据单独发送请求
                  promises.push(
                    xiafaListNew({
                      tableId: '633657419219013',
                      jsondistribution: JSON.stringify(xiafaDataArray),
                    })
                  )
                }
              }

              // 等待所有请求完成
              Promise.all(promises)
                .then((responses) => {
                  // 检查所有响应是否都成功
                  const allSuccess = responses.every(
                    (response) => response.msg === '成功'
                  )
                  if (allSuccess) {
                    this.$baseMessage('下发通知成功', 'success')
                  } else {
                    this.$baseMessage('部分下发通知失败', 'warning')
                  }
                  this.getDetail()
                })
                .catch((error) => {
                  console.error('下发通知失败:', error)
                  this.$baseMessage('下发通知失败', 'error')
                })
            }
          })
        } catch (error) {
          console.log(error)
        }
      },
      async handleExport() {
        const data = await sjdwlrsjlrExportData({
          ...this.queryForm,
          id: this.formData.id,
        })
        let fileName = '三级单位离任审计表'
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit({ id: row.id }, '详情')
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(112, this.editId)
      },
      handleSelectionChange(rows) {
        this.selectedRows = rows
      },

      handleBatchIssue(fromType) {
        this.fromType = fromType
        if (fromType == 'xf') {
          if (this.formData.status != 6) {
            this.$message.error('当前状态不能批量下发')
            return
          }
        }
        this.$refs.ZXPerson.showEdit(this.formData.secrectlevelid)
        // this.$refs['SelectPersonModal'].showEdit()
      },
      async getDetail() {
        const {
          data: {
            data: { data, list },
          },
        } = await getMajorDetails({ id: this.formData.id })
        this.tableData = list
      },
      async selectP(val) {
        // 获取姓名列表并拼接
        const realNames = val.map((item) => item.realname).join(',')
        const ids = val.map((item) => item.staffid).join(',')
        try {
          const res = await updateMajorIssued({
            majorid: String(this.formData.id),
            ids: this.selectedRows.map((item) => item.id).join(','),
            staffids: ids,
            staffnames: realNames,
          })
          if (res.code == 1) {
            this.$baseMessage('选择下发人员成功', 'success')
            // 更新tableData中的数据
            this.getDetail()
          }
        } catch (error) {
          console.log(error)
        }
      },
      async handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        if (this.fromType == 'mj') {
          this.formData.staffscopeids = ids
          this.formData.staffscopenames = names
        } else {
          this.selectP(val)
        }
      },
      handleDownloadTemplate() {
        // 获取当前域名和协议
        const baseUrl = window.location.origin
        // 拼接完整的文件URL
        const fileUrl = `${baseUrl}/files/重大风险创建内层.xlsx`

        // 创建一个隐藏的a标签用于下载
        const link = document.createElement('a')
        link.href = fileUrl
        link.setAttribute('download', '重大风险创建内层.xlsx')
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      },
      // 处理上传按钮点击
      handleUploadClick() {
        if (!this.formData.id) {
          this.$message.error('请先保存基本信息后再进行导入')
          return
        }
        // 手动触发上传组件的点击事件
        this.$refs.upload.$el.querySelector('input').click()
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
  .upload-demo {
    display: inline-block;
  }
</style>
