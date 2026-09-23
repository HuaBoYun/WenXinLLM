<template>
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
        <el-col :span="12">
          <el-form-item label="所属组织" prop="organization">
            <el-input
              v-model="formData.organization"
              placeholder="请输入所属组织"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="利率编码" prop="name">
            <el-input v-model="formData.name" placeholder="请输入利率编码" />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="利率名称" prop="code">
            <el-input v-model="formData.code" :style="{ width: '100%' }" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="利率类型" prop="province">
            <el-input v-model="formData.province" :style="{ width: '100%' }" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="日利率天数" prop="city">
            <el-input v-model="formData.city" :style="{ width: '100%' }" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="融资利率类型" prop="phone">
            <el-input v-model="formData.phone" :style="{ width: '100%' }" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="利率录入日期" prop="address">
            <el-input v-model="formData.address" :style="{ width: '100%' }" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="起效日期" prop="status">
            <el-input v-model="formData.status" :style="{ width: '100%' }" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="启用定额利率" prop="status">
            <el-checkbox v-model="formData.checked" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="启用提前利率" prop="status">
            <el-checkbox v-model="formData.checked" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="启用状态" prop="status">
            <el-input v-model="formData.status" :style="{ width: '100%' }" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备注" prop="status">
            <el-input
              v-model="formData.status"
              type="textarea"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <div class="show_line_box">
            <div class="title_l">
              <i
                @click="handleIsUnfold"
                v-if="isUnfoldAuditShow1"
                class="el-icon-minus"
              ></i>
              <i @click="handleIsUnfold1" v-else class="el-icon-plus"></i>
              利率信息
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow1">
            <el-col :span="12">
              <el-form-item label="年利率%" prop="creator">
                <el-input
                  v-model="formData.creator"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="月利率‰" prop="createDate">
                <el-input
                  v-model="formData.createDate"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="日利率‰" prop="updateCreator">
                <el-input
                  v-model="formData.updateCreator"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="透支起点天数" prop="updateDate">
                <el-input
                  v-model="formData.updateDate"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="透支起点金额" prop="updateDate">
                <el-input
                  v-model="formData.updateDate"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="透支年利率%" prop="updateDate">
                <el-input
                  v-model="formData.updateDate"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="透支月利率‰" prop="updateDate">
                <el-input
                  v-model="formData.updateDate"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="透支日利率‰" prop="updateDate">
                <el-input
                  v-model="formData.updateDate"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
          </template>
        </el-col>
        <el-col :span="24">
          <div class="show_line_box">
            <div class="title_l">
              <i
                @click="handleIsUnfold"
                v-if="isUnfoldAuditShow"
                class="el-icon-minus"
              ></i>
              <i @click="handleIsUnfold" v-else class="el-icon-plus"></i>
              审计信息
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow">
            <el-col :span="12">
              <el-form-item label="创建人" prop="creator">
                <el-input
                  v-model="formData.creator"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="创建时间" prop="createDate">
                <el-input
                  v-model="formData.createDate"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最后修改人" prop="updateCreator">
                <el-input
                  v-model="formData.updateCreator"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最后修改时间" prop="updateDate">
                <el-input
                  v-model="formData.updateDate"
                  :style="{ width: '100%' }"
                />
              </el-form-item>
            </el-col>
          </template>
        </el-col>
      </el-form>
    </el-row>
    <lxjyzypgEdit ref="edit" @selected="selected"></lxjyzypgEdit>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <ProcessList ref="process" @fetchData="close" />
    <div slot="footer" v-if="!disabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    lxjyzypgBaseSave,
    lxjyzypgBaseDetail,
    getLxjyzypgBaseRelateList,
    deleteLxjyzypgBaseRelateList,
  } from '@/oapi/audit/plan'
  import { downloadFile } from '@/utils/otherUtils'
  import { exportList } from '@/api/oilAudit/jhgl/lxjyzypg'
  import lxjyzypgEdit from '@/views/oilAudit/jhlx/components/lxjyzypgEdit'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import typeView from '@/views/oilAudit/lrjjzr/components/type'
  import { formatDay } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'

  export default {
    components: { lxjyzypgEdit, SelectDepartment, typeView, ProcessList },
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/plan/project/evaluation/import',
        headers: { token },
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 9999,
        },
        total: 0,
        listLoading: false,
        tableData: [],
        formData: {
          organization: '',
          name: '',
          code: '',
          province: '',
          city: '',
          phone: '',
          address: '',
          status: '',
          creator: '',
          createDate: '',
          updateCreator: '',
          updateDate: '',
        },
        rules: {
          organization: [
            {
              required: true,
              message: '请输入所属组织',
              trigger: 'blur',
            },
          ],
          name: [
            {
              required: true,
              message: '请输入金融机构名称',
              trigger: 'blur',
            },
          ],
          code: [
            {
              required: true,
              message: '请输入金融机构编码',
              trigger: 'blur',
            },
          ],
          status: [
            {
              required: true,
              message: '请选择启用状态',
              trigger: 'blur',
            },
          ],
        },
        dialogJdVisible: false,
        options: [
          {
            value: '工程',
            label: '工程',
          },
          {
            value: '财务',
            label: '财务',
          },
        ],
        disabled: false,
        editId: '',
        multipleSelection: [],
        isUnfoldAuditShow1: true,
        isUnfoldAuditShow: false,
      }
    },
    methods: {
      async showEdit(row, title) {
        this.dialogJdVisible = true
        this.title = title
        this.disabled = title == 'detail'

        if (row) {
          this.editId = row.tbid
          const {
            data: { data },
          } = await lxjyzypgBaseDetail({ tbid: row.tbid })
          this.formData.tbname = data.tbname
          this.formData.tbrgname = data.tbrgname
          this.formData.tbrgid = data.tbrgid
          this.formData.createdate = data.createdate
          this.formData.createname = data.createname
          this.formData.tbid = data.tbid
          this.getTableList()
          // this.fetchData()
        }
      },

      close() {
        this.formData = {
          tbname: '', //季度
          tbrgname: '', //填报单位
          tbrgid: '', //填报单位id
          createdate: formatDay(new Date().toString()),
          createname: JSON.parse(localStorage.getItem('userInfo')).realname,
          tbid: '',
          itemType: '',
        }
        this.tableData = []
        this.dialogJdVisible = false
        this.editId = ''
        this.$emit('fetchData')
      },
      add(row, type) {
        this.$refs['fyhjrjgBaseEdit'].showEdit('add', row, type)
      },
      edit(row) {
        this.$refs['fyhjrjgBaseEdit'].showEdit('edit', row)
      },
      async fetchData(data) {
        const arr = JSON.parse(JSON.stringify(this.tableData))
        if (!data) {
          this.tableData = [...arr]
          return false
        }
        if (data?.index) {
          arr[data.index - 1] = data.data[0]
          this.tableData = [...arr]
        } else {
          this.tableData = [...arr, ...data.data]
        }
      },
      async handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getTableList()
        // this.fetchData()
      },
      async handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getTableList()
        // this.fetchData()
      },
      async getTableList() {
        const arr = await getLxjyzypgBaseRelateList({
          tbid: this.formData.tbid,
          ...this.queryForm,
        })
        this.tableData = arr.data.tlist
        this.total = arr.data.totalRecord
      },
      handleDelete(row) {
        console.log('handleDelete', row)
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await deleteLxjyzypgBaseRelateList({ id: row.id })
          if (res.code == 1) {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            let list = this.tableData
            list = list.filter((item) => item.id != row.id)
            this.tableData = list
            // await this.fetchData()
            await this.getTableList()
          }
        })
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.tbrgname = val.label
        this.formData.tbrgid = val.id
      },
      save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const ids = this.tableData.map((res) => res.id)
            const res = await lxjyzypgBaseSave({
              ...this.formData,
              glids: ids.toString(),
            })
            if (res && res.code === 1) {
              this.editId = res.data.data.tbid
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '提交失败',
                type: 'error',
              })
            }
          }
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
        this.listLoading = true
        const res = await exportList({
          ...this.queryForm,
          tbid: this.formData.tbid,
        })
        downloadFile(res, '立项建议专业评估.xlsx')
        this.listLoading = false
      },
      selected(val) {
        const arr = this.tableData.filter((res) => res.id != val.id)
        this.tableData = [...arr, val]
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      handleIsUnfold() {
        this.isUnfoldAuditShow = !this.isUnfoldAuditShow
      },
      handleIsUnfold1() {
        this.isUnfoldAuditShow1 = !this.isUnfoldAuditShow1
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
  .show_line_box {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
  }
  .title_l {
    width: 100px;
    text-align: center;
  }
  .line {
    flex: 1;
    width: 100%;
    height: 1px;
    border: 1px solid #cccccc6e;
  }
</style>
