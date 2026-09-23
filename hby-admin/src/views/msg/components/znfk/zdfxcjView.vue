<template>
  <!-- 三级单位离任审计 -->
  <div>
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
              :disabled="disabled"
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
              @click="$refs.ZXPerson.showEdit(formData.secrectlevelid)"
              :disabled="!formData.secrectlevelid || disabled"
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
              :disabled="disabled"
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
              :disabled="disabled"
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
            <el-button type="success" @click="add()" v-if="!disabled">
              新增
            </el-button>
            <el-button type="primary" @click="handleDownloadTemplate">
              下载模板
            </el-button>
            <el-upload
              style="margin: 0 10px; display: inline-block"
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleImportSuccess"
              :data="{ majorid: formData.id }"
            >
              <el-button type="success">导入</el-button>
            </el-upload>
            <!-- <el-button
              type="success"
              @click="handleBatchIssue"
              v-if="!disabled"
              :disabled="!selectedRows.length"
            >
              批量下发
            </el-button> -->
          </div>
          <el-table
            v-loading="listLoading"
            :data="tableData"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" align="center" />
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
              label="操作"
              width="180"
              v-if="!disabled"
            >
              <template #default="scope">
                <el-button type="text" @click="add(scope.row, scope.$index)">
                  修改
                </el-button>
                <!-- <el-button type="text" @click="handleIssue(scope.row)">
                  下发
                </el-button> -->
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

    <div slot="footer" style="text-align: right" v-if="!disabled">
      <el-button @click="save" type="primary">确定</el-button>
      <el-button @click="ymsubmit" type="primary" :disabled="btnLoading">
        提交
      </el-button>
    </div>
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="fetchClose"
      :status="status"
    />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>

<script>
  import {
    createSaveOrUpdate,
    delMajorRiskDelete,
    updateMajorIssued,
    getMajorDetails,
  } from '@/api/risk/create'
  import createEdit from '@/views/risk/riskfill/createEdit2.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { formatDay } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  const token = store.getters['user/token']
  import ZXPerson from '@/components/selectPerson.vue'
  import { hasMJ, couldMJ } from '@/utils'
  import { getSPMJ } from '@/api/setting/mjsz'
  export default {
    components: {
      createEdit,
      SelectDepartment,
      ProcessList,
      Resubmit,
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
          secrectlevelid: '',
          staffscopenames: '',
          staffscopeids: '',
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

        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
        footer: true,
        showMJ: false,
        btnLoading: false,
        MJoption: [],
      }
    },
    mounted() {
      // this.getOption()
    },
    methods: {
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
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
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        nextNodeName,
        flowType
      ) {
        // 拿到类型传给getMJData获取审批的密级的下拉数据
        if (flowType) {
          this.getMJData(flowType)
        }
        this.dialogJdVisible = true
        this.title = title
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
          this.disabled = true
        } else if (title == 'add') {
          this.title = '新增'
        }
        if (formId) {
          this.editId = formId
          const {
            data: {
              data: { data, list },
            },
          } = await getMajorDetails({ id: formId })
          this.formData.quartername = data.quartername
          this.$set(this.formData, 'riskyear', String(data.riskyear))
          this.$set(this.formData, 'linkOrgName', data.linkOrgName)
          this.$set(this.formData, 'linkOrgId', data.linkOrgId)
          this.$set(this.formData, 'createdTime', data.createdate)
          this.$set(this.formData, 'createname', data.createname)
          this.$set(this.formData, 'createdTime', data.createtime)
          this.$set(this.formData, 'id', data.id)
          this.$set(this.formData, 'secrectlevelid', data.secrectlevelid)
          this.$set(this.formData, 'staffscopenames', data.staffscopenames)
          this.$set(this.formData, 'staffscopeids', data.staffscopeids)
          this.tableData = list
          if (data.secrectlevelid) {
            localStorage.setItem('SPsecrectLevelId', data.secrectlevelid)
          }
        }
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
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
        }
        this.tableData = []
        this.selectedRows = []
        this.dialogJdVisible = false
        this.editId = ''
        this.$emit('fetchData')
      },
      fetchClose() {
        this.formData = {
          quartername: '', //季度
          linkOrgName: JSON.parse(localStorage.getItem('userInfo')).linkOrg
            .orgname, //填报单位
          linkOrgId: JSON.parse(localStorage.getItem('userInfo')).linkOrg.orgid, //填报单位id
          createdTime: formatDay(new Date().toString()),
          createname: JSON.parse(localStorage.getItem('userInfo')).realname,
          id: '',
          riskyear: '', //年度
        }
        this.tableData = []
        this.selectedRows = []
        this.dialogJdVisible = false
        this.editId = ''
        this.$bus.$emit('updateMsg', 0)
      },
      add(row, i) {
        if (row) {
          this.$refs['edit'].showEdit({
            id: row.id,
            fatherId: this.formData.id,
          })
        }
        this.$refs['edit'].showEdit({ fatherId: this.formData.id })
      },
      async fetchData({ data: data }) {
        console.log('🚀 ~ fetchData ~ data:', data)
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
            await this.fetchData()
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
      handleImportSuccess(response) {
        if (response.code === 1) {
          if (response.data && response.data.data) {
            this.$baseMessage(response.data.data, 'error')
          } else {
            this.$baseMessage('导入成功', 'success', 'vab-hey-message-success')
            // 重新获取数据
            this.getDetail()
          }
        } else {
          this.$baseMessage(response.msg || '导入失败', 'error')
        }
      },
      async getDetail() {
        const {
          data: {
            data: { data, list },
          },
        } = await getMajorDetails({ id: this.editId })
        this.tableData = list
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
      handleSelectionChange(rows) {
        this.selectedRows = rows
      },

      // handleIssue(row) {
      //   // 单个下发处理
      //   this.$baseConfirm('确定要下发该项吗？', null, async () => {
      //     try {
      //       const res = await updateMajorIssued({
      //         majorid: String(this.formData.id),
      //         ids: row.id,
      //       })
      //       if (res.code == 1) {
      //         this.$baseMessage('下发成功', 'success')
      //       }
      //     } catch (error) {
      //       console.log(error)
      //     }
      //   })
      // },

      handleBatchIssue() {
        // 批量下发处理
        this.$baseConfirm(
          `确定要下发选中的 ${this.selectedRows.length} 项吗？`,
          null,
          async () => {
            try {
              const res = await updateMajorIssued({
                majorid: String(this.formData.id),
                ids: this.selectedRows.map((item) => item.id).join(','),
              })
              if (res.code == 1) {
                this.$baseMessage('批量下发成功', 'success')
              }
            } catch (error) {
              console.log(error)
            }
          }
        )
      },
      async ymsubmit() {
        try {
          this.btnLoading = true
          this.$refs['ruleForm'].validate(async (valid) => {
            if (valid) {
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
          console.error('Error fetching data:', error)
          // 你可以在这里处理错误，比如显示错误提示
        }
      },
      async handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffscopeids = ids
        this.formData.staffscopenames = names
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
