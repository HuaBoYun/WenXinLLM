<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="应付汇兑损益记录"
    :visible.sync="dialogJdVisible"
    width="1000px"
    @close="close"
    v-if="dialogJdVisible"
  >
    <el-row :gutter="14">
      <el-form ref="ruleForm" :model="formData" :rules="rules" size="mini">
        <el-col :span="4">
          <el-form-item prop="province">
            <el-input v-model="formData.province" placeholder="财务组织" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="name">
            <el-input v-model="formData.name" placeholder="供应商" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="code">
            <el-input v-model="formData.code" placeholder="损益类型" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="code">
            <el-input v-model="formData.code" placeholder="汇兑查询" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="province1">
            <el-select
              v-model="queryForm.supplier"
              placeholder="交易类型"
              clearable
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in optionsStatus"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="province">
            <el-input v-model="formData.province" placeholder="币种" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="province2">
            <el-input v-model="formData.province" placeholder="单户编号" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="province">
            <el-input v-model="formData.province" placeholder="本币差额" />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item prop="province4">
            <el-input v-model="formData.province" placeholder="组织本币" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item prop="province">
            <el-date-picker
              v-model="queryForm.day"
              type="daterange"
              align="right"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              stye="{ width: '260px' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-button type="primary">查询</el-button>
        </el-col>
        <el-col :span="24">
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="date" label="选择标志"></el-table-column>
            <el-table-column prop="name"></el-table-column>
            <el-table-column prop="address" label="供应商"></el-table-column>
            <el-table-column prop="address" label="币种"></el-table-column>
            <el-table-column prop="address" label="原币金额"></el-table-column>
            <el-table-column
              prop="address"
              label="组织本币金额"
            ></el-table-column>
            <el-table-column
              prop="address"
              label="集团本币金额"
            ></el-table-column>
            <el-table-column
              prop="address"
              label="全局本币金额"
            ></el-table-column>
            <el-table-column
              prop="address"
              label="调整后原币金额"
            ></el-table-column>
            <el-table-column
              prop="address"
              label="调整后组织本币金额"
            ></el-table-column>
            <el-table-column
              prop="address"
              label="调整后集团本币金额"
            ></el-table-column>
            <el-table-column
              prop="address"
              label="调整后全局本币金额"
            ></el-table-column>
            <el-table-column
              prop="address"
              label="组织本币金额"
            ></el-table-column>
            <el-table-column
              prop="address"
              label="集团本币差额"
            ></el-table-column>
            <el-table-column
              prop="address"
              label="全局本币差额"
            ></el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>
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
  import { formatDay } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']

  export default {
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
          province1: [
            {
              required: true,
              message: '请输入来源系统',
              trigger: 'blur',
            },
          ],
          province2: [
            {
              required: true,
              message: '请输入交易类型',
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
          province3: [
            {
              required: true,
              message: '请选择目的系统',
              trigger: 'blur',
            },
          ],
          province4: [
            {
              required: true,
              message: '请输入交易类型',
              trigger: 'blur',
            },
          ],
        },
        dialogJdVisible: false,
        optionsStatus: [
          {
            value: '1',
            label: '应收',
          },
          {
            value: '1',
            label: '应付',
          },
        ],
        disabled: false,
        editId: '',
        multipleSelection: [],
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
        this.listLoading = false
        const res = await exportList({
          ...this.queryForm,
          tbid: this.formData.tbid,
        })
        downloadFile(res, '立项建议专业评估.xlsx')
        this.listLoading = false
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      handleIsUnfold() {
        this.isUnfoldAuditShow = !this.isUnfoldAuditShow
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
  .title_box {
    margin: 15px 0;
  }
</style>
