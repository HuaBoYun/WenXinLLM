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
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="小组名称" prop="xzname">
            <el-input
              v-model="formData.xzname"
              clearable
              placeholder="请输入小组名称"
              :disabled="!disabled"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组长" prop="zznames">
            <el-input
              v-model="formData.zznames"
              :style="{ width: '80%' }"
              disabled
              placeholder="请选择组长"
            />
            <el-button
              type="primary"
              style="margin-left: 10px"
              @click="showPeople('zznames')"
              :disabled="!disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="副组长" prop="fznames">
            <el-input
              v-model="formData.fznames"
              :style="{ width: '80%' }"
              disabled
              placeholder="请选择副组长"
            />
            <el-button
              type="primary"
              style="margin-left: 10px"
              @click="showPeople('fznames')"
              :disabled="!disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评委组" prop="pwenames">
            <el-input
              v-model="formData.pwenames"
              :style="{ width: '80%' }"
              disabled
              placeholder="请选择评委组"
            />
            <el-button
              type="primary"
              style="margin-left: 10px"
              @click="showPeople('pwenames')"
              :disabled="!disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报单位" prop="tbrgname">
            <el-input
              v-model="formData.tbrgname"
              :style="{ width: '80%' }"
              disabled
              placeholder="请输入填报单位"
            />
            <el-button
              @click="handleObject"
              style="margin-left: 10px"
              type="primary"
              :disabled="!disabled"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报人" prop="createname">
            <el-input
              v-model="formData.createname"
              clearable
              placeholder="请输入填报人"
              disabled
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="填报时间" prop="createdate">
            <el-input
              v-model="formData.createdate"
              clearable
              placeholder="请输入填报时间"
              disabled
              style="width: 100%"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>评选论文</el-divider>
        </el-col>
        <el-col :span="24">
          <div
            style="
              display: flex;
              flex-direction: row-reverse;
              margin-bottom: 15px;
            "
          >
            <el-button type="success" @click="handleExport">导出</el-button>
            <el-button
              style="margin-right: 15px"
              v-if="disabled && formData.pwenames"
              type="success"
              @click="openRelate"
            >
              选择论文
            </el-button>
          </div>
          <el-table :data="tableData">
            <el-table-column
              align="center"
              label="论文名称"
              prop="papername"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDetail(row)">
                  {{ row.papername }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="填报单位"
              prop="tbrgname"
              show-overflow-tooltip
            />
            <el-table-column align="center" prop="total" label="排序" />
            <el-table-column align="center" label="操作">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  @click="PresonSort(scope.row, scope.$index)"
                  v-if="pfBtn"
                >
                  评选组排序
                </el-button>
                <el-button
                  type="text"
                  @click="deleteTable(scope.row)"
                  v-if="disabled"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <!-- 选择人员弹窗 -->
    <selectPerson
      ref="executor"
      @projectManage="handleExecutorSelected"
      :multiple="selectPersonType"
    />
    <RelateSB ref="sb" @selected="handleRelate" />
    <PresonSort ref="PresonSort" @selected="handlePresonSortRelate" />
    <Edit ref="edit" />
    <ProcessList ref="process" @fetchData="close" />

    <div slot="footer" v-if="disabled || pfBtn">
      <el-button @click="close">关 闭</el-button>
      <el-button @click="add" type="primary" :loading="loading">确定</el-button>
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    saveLwpxData,
    getLwpxData,
    getLwpxzbList,
    lwpxExportList,
    deletezbLwpx,
  } from '@/oapi/audit/lwpy'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import selectPerson from '@/components/selectPerson'
  import RelateSB from '@/views/oilAudit/lwpy/components/relateSB.vue'
  import PresonSort from '@/views/oilAudit/lwpy/components/PresonSort.vue'
  import store from '@/store'
  import Edit from '@/views/oilAudit/lwpy/components/lwsbEdit.vue'
  import { formatDate, formatDay } from '@/utils/index'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'

  const resL = JSON.parse(localStorage.getItem('userInfo'))?.realname
  const createdate = formatDay(new Date())
  export default {
    name: 'lwpxEdit',
    inheritAttrs: false,
    components: {
      SelectDepartment,
      selectPerson,
      RelateSB,
      PresonSort,
      Edit,
      ProcessList,
    },
    props: [],
    data() {
      return {
        loading: false,
        formData: {
          xzname: undefined,
          zznames: undefined,
          zsstaffids: undefined,
          fznames: undefined,
          fsstaffids: undefined,
          pwenames: undefined,
          pwstaffids: undefined,
          tbrgname: undefined,
          tbrgid: undefined,
          createname: resL,
          createdate: createdate,
          tbid: undefined,
        },
        tbid: '',
        pfBtn: false,
        auditProjectSelectList: [],
        tableData: [],
        disabled: true,
        rules: {
          xzname: [
            {
              required: true,
              message: '请输入小组名称',
              trigger: 'blur',
            },
          ],
          zznames: [
            {
              required: true,
              message: '请选择组长',
              trigger: 'change',
            },
          ],
          fznames: [
            {
              required: true,
              message: '请选择副组长',
              trigger: 'change',
            },
          ],
          pwenames: [
            {
              required: true,
              message: '请选择评委组',
              trigger: 'change',
            },
          ],
          tbrgname: [
            {
              required: true,
              message: '请输入填报单位',
              trigger: 'change',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        renderPersonList: [],
        editIndex: 0, //当前编辑的索引
        personType: '',
        selectPersonType: false,
        editId: '',
      }
    },
    methods: {
      async showEdit(title, row, type) {
        this.dialogFormVisible = true
        if (type == 1) {
          this.disabled = false
          this.pfBtn = true
        } else {
          if (title == 'edit') {
            this.title = '编辑'
          } else if (title == 'detail') {
            this.title = '详细'
            this.disabled = false
          } else {
            this.title = '新增'
            this.disabled = true
            let userInfo = JSON.parse(localStorage.getItem('userInfo'))
            this.formData = {
              ...this.formData,
              cjr: userInfo.realname,
              cjsj: new Date().toJSON().split('T')[0],
            }
          }
        }
        if (row) {
          this.editId = row.tbid
          this.tbid = row.tbid
          const res = await getLwpxData({ tbid: row.tbid })
          this.formData = res.data.data

          let names = res.data.data.pwenames.split(',')
          let ids = res.data.data.pwstaffids.split(',')

          let sortList = []
          for (let i = 0; i < names.length; i++) {
            let obj = {
              realname: names[i],
              staffid: ids[i],
              fs: '',
            }
            sortList.push(obj)
          }
          this.renderPersonList = sortList

          const listRes = await getLwpxzbList({ tbid: row.tbid })
          this.tableData = listRes.data.data
        }
      },
      close() {
        this.formData = {
          xzname: undefined,
          zznames: undefined,
          zsstaffids: undefined,
          fznames: undefined,
          fsstaffids: undefined,
          pwenames: undefined,
          pwstaffids: undefined,
          tbrgname: undefined,
          tbrgid: undefined,
          createname: resL,
          createdate: createdate,
          tbid: undefined,
        }
        this.dialogFormVisible = false
        this.loading = false
        this.tableData = []
        this.disabled = true
        this.pfBtn = false
        this.$refs['ruleForm'].resetFields()
        this.editId = ''
        this.$emit('fetchData')
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            const ids = this.tableData.map((res) => res.perid).join(',')
            let params = { ...this.formData }
            delete params.createdate
            delete params.createname
            const data = await saveLwpxData({
              ...params,
              glids: ids,
            })
            if (data.code == 1) {
              this.$baseMessage('保存成功', 'success')
              this.editId = data.data.data.tbid
            }
            this.loading = false
          } else {
            return false
          }
        })
      },
      // 填报单位
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.tbrgname = val.label
        this.formData.tbrgid = val.id
      },
      // 选择人员
      showPeople(type) {
        this.personType = type
        if (type == 'pwenames') {
          this.selectPersonType = true
        } else {
          this.selectPersonType = false
        }
        this.$refs.executor.showEdit()
      },
      handleExecutorSelected(val) {
        if (this.personType == 'zznames') {
          this.formData.zsstaffids = val[0].staffid
          this.formData.zznames = val[0].realname
        } else if (this.personType == 'pwenames') {
          this.formData.pwstaffids = val.map((item) => item.staffid).join(',')
          this.formData.pwenames = val.map((item) => item.realname).join(',')
          const sortList = val.map((x) => {
            return { ...x, fs: '' }
          })
          this.renderPersonList = sortList
        } else {
          this.formData.fsstaffids = val[0].staffid
          this.formData.fznames = val[0].realname
        }
      },
      openRelate() {
        this.$refs['sb'].show()
      },
      handleRelate(val) {
        const info = val.map((res, index) => {
          return { ...res }
        })
        this.tableData = this.tableData.concat(info)
      },
      async deleteTable(row) {
        const data = await deletezbLwpx({
          perid: Number(row.perid),
        })
        if (data.code == 1) {
          this.$baseMessage('删除成功', 'success')
          let list = this.tableData
          list = list.filter((item) => item.perid != row.perid)
          this.tableData = list
        } else {
          this.$baseMessage('删除失败', 'error')
        }
      },
      async PresonSort(row, index) {
        const arr = JSON.parse(JSON.stringify(this.renderPersonList))
        this.$refs['PresonSort'].showEdit(arr, row.perid, index)
      },
      handlePresonSortRelate(val) {
        this.$set(this.tableData[val.index], 'total', val.fs)
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit('detail', row)
      },
      async handleExport() {
        const data = await lwpxExportList({
          tbid: Number(this.tbid),
        })
        let fileName = '评选论文'
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
      handleApproval() {
        //提交审批
        this.$refs['process'].save(191, this.editId)
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
