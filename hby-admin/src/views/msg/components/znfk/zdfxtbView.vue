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
        <el-col :span="12" style="height: 47px">
          <el-form-item label="季度" prop="quartername">
            <el-select
              v-model="formData.quartername"
              placeholder="请选择季度"
              clearable
              :style="{ width: '100%' }"
              disabled
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
        <el-col :span="12" style="height: 47px">
          <el-form-item label="年度" prop="riskyear">
            <el-date-picker
              v-model="formData.riskyear"
              type="year"
              value-format="yyyy"
              placeholder="选择年"
              :style="{ width: '100%' }"
              disabled
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报单位" prop="linkOrgName">
            <el-input
              v-model="formData.linkOrgName"
              disabled
              placeholder="请输入填报单位"
              :style="{ width: '80%' }"
            />
            <el-button
              @click="handleObject"
              style="margin-left: 10px"
              type="primary"
              disabled
            >
              选择
            </el-button>
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
        <el-table :data="newlist" ref="multipleTable">
          <el-table-column
            align="center"
            label="风险名称"
            prop="impRiskName"
            #default="{ row }"
            width="200"
          >
            <el-button type="text" @click="handleEdit(row, 'detail')">
              {{ row.impRiskName }}
            </el-button>
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
          />
          <el-table-column
            align="center"
            label="公司相关责任单位"
            prop="impDutyUnitName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="重点事项"
            prop="impKeyIssues"
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
            :label="getThisControlLabel"
            prop="impThisControl"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="已发生的风险事件及应对处置情况"
            prop="impSolutions"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="需要提示的问题和风险"
            prop="impTips"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="!isFourthQuarter"
            align="center"
            :label="getNextControlLabel"
            prop="impTextControl"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="其他需要说明的情况"
            prop="impOther"
            show-overflow-tooltip
          />

          <el-table-column
            label="操作"
            #default="{ row }"
            fixed="right"
            align="center"
            width="120"
            v-if="!disabled"
          >
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
          </el-table-column>
        </el-table>
      </el-form>
    </el-row>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <ProcessList ref="process" @fetchData="close" />
    <SelectPersonModal
      ref="SelectPersonModal"
      @projectManage="selectP"
      :multiple="true"
    />
    <div
      slot="footer"
      v-if="!disabled"
      style="text-align: right; margin-top: 10px"
    >
      <el-button @click="save" type="primary">确定</el-button>
      <el-button @click="ymsubmit" type="primary" :disable="btnLoading">
        提交
      </el-button>
    </div>
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="dialogTitle"
      :visible.sync="riskfillEdit2Visable"
      width="1000px"
      @close="riskfillEdit2Visable = false"
    >
      <riskfillEdit2
        v-if="riskfillEdit2Visable"
        :curRow="curRow"
        @fetchData="fetchData"
        @close="riskfillEdit2Visable = false"
        :isEdit="isEdit"
      />
    </el-dialog>
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
    getRiskDetail,
  } from '@/api/risk/create'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { formatDay } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  import SelectPersonModal from '@/components/duoxuanPerson.vue'
  import { getInsideList } from '@/api/risk/create'
  const token = store.getters['user/token']
  import riskfillEdit2 from '@/views/risk/riskfill/riskfillEdit2'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import ZXPerson from '@/components/selectPerson.vue'
  import { hasMJ, couldMJ } from '@/utils'
  import { getSPMJ } from '@/api/setting/mjsz'
  export default {
    components: {
      SelectDepartment,
      ProcessList,
      SelectPersonModal,
      riskfillEdit2,
      Resubmit,
      ZXPerson,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/plan/leave/audit3L/importData',
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
        type: 0, //是否点击下发进入
        newlist: [],
        curRow: null,
        riskfillEdit2Visable: false,
        fromId: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
        fromIdcopy: '',
        dialogTitle: '',
        isEdit: false,
        btnLoading: false,
        MJoption: [],
        showMJ: false,
      }
    },
    computed: {
      // 当前季度风险防控情况标签
      getThisControlLabel() {
        if (!this.formData.quartername) return '本季度风险防控情况'

        if (this.formData.quartername.includes('一季度')) {
          return '本季度风险防控情况'
        } else if (this.formData.quartername.includes('二季度')) {
          return '上半年风险防控情况'
        } else if (this.formData.quartername.includes('三季度')) {
          return '第三季度风险防控情况'
        } else if (this.formData.quartername.includes('四季度')) {
          return '第四季度风险防控情况'
        }
        return '本季度风险防控情况'
      },

      // 下季度主要风险研判标签
      getNextControlLabel() {
        if (!this.formData.quartername)
          return '下季度主要风险研判及相应防护措施'

        if (this.formData.quartername.includes('一季度')) {
          return '下季度主要风险研判及相应防护措施'
        } else if (this.formData.quartername.includes('二季度')) {
          return '下半年主要风险研判及相应防护措施'
        } else if (this.formData.quartername.includes('三季度')) {
          return '下季度主要风险研判及相应防护措施'
        }
        return '下季度主要风险研判及相应防护措施'
      },

      // 是否是第四季度
      isFourthQuarter() {
        return (
          this.formData.quartername &&
          this.formData.quartername.includes('四季度')
        )
      },
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
        console.log(formId, 'formId')
        // this.fetchData11({ id: formId })
        this.disabled = title == 'detail'
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        if (formId) {
          const {
            data: {
              data: { data, list },
            },
          } = await getRiskDetail({ id: formId })
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
          this.newlist = list
          if (data.secrectlevelid) {
            localStorage.setItem('SPsecrectLevelId', data.secrectlevelid)
          }
        }
      },
      close() {
        this.$emit('fetchData')
        this.$bus.$emit('updateMsg', 0)
      },
      async fetchData({ pageInfo }) {
        console.log('🚀 ~ fetchData ~ data:', pageInfo)
        if (pageInfo) {
          const existingIndex = this.newlist.findIndex(
            (item) => item.id === pageInfo.id
          )
          if (existingIndex > -1) {
            // 如果存在相同id的数据，则替换
            this.$set(this.newlist, existingIndex, pageInfo)
          } else {
            // 如果不存在，则添加到数组末尾
            this.newlist.push(pageInfo)
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
        this.$message({
          message: '保存成功！',
          type: 'success',
        })
      },
      handleSuccess(response) {
        if (response.code == 1) {
          this.tableData = [...this.tableData, ...response.data.data]
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
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

      handleBatchIssue() {
        if (this.formData.status != 6) {
          this.$message.error('当前状态不能批量下发')
          return
        }
        this.$refs['SelectPersonModal'].showEdit()
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
            this.$baseMessage('批量下发成功', 'success')
          }
        } catch (error) {
          console.log(error)
        }
      },
      // async fetchData11(row) {
      //   getInsideList({ majorid: row.id }).then((res) => {
      //     if (
      //       res &&
      //       res.data &&
      //       res.data.data &&
      //       res.data.data.pageInfo &&
      //       res.data.data.pageInfo.tlist
      //     ) {
      //       this.newlist = res.data.data.pageInfo.tlist
      //       this.total = res.data.data.pageInfo.totalRecord
      //     }
      //   })
      // },
      async handleEdit(row, disabled) {
        this.curRow = JSON.parse(JSON.stringify(row))
        // 将父组件的季度信息传递给子组件
        this.curRow.quartername = this.formData.quartername
        this.isEdit = false
        if (row && disabled) {
          this.dialogTitle = '详情'
          this.curRow.disabled = true
        } else if (row && !disabled) {
          this.dialogTitle = '修改'
          this.isEdit = true
        } else {
          this.dialogTitle = '新增'
        }
        this.riskfillEdit2Visable = true
      },
      async ymsubmit() {
        try {
          this.btnLoading = true
          this.$refs.resubmit.ymsubmit()
        } catch (error) {
          this.btnLoading = false
          console.error('Error fetching data:', error)
          // 你可以在这里处理错误，比如显示错误提示
        }
      },
      fetchClose() {
        this.close()
      },
      async handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffscopeids = ids
        this.formData.staffscopenames = names
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
