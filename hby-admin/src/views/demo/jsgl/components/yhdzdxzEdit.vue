<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
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
        <template v-if="this.title === '在线下载'">
          <el-col :span="12">
            <el-form-item label="财务组织" prop="organization">
              <el-input
                v-model="formData.organization"
                placeholder="财务组织"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行类别" prop="code">
              <el-input v-model="formData.code" placeholder="银行类别" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行账号" prop="brankAccount">
              <el-input
                v-model="formData.brankAccount"
                placeholder="银行账号"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="formData.startDate"
                type="date"
                placeholder="选择日期"
                :style="{ width: '100%' }"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker
                v-model="formData.endDate"
                type="date"
                placeholder="选择日期"
                :style="{ width: '100%' }"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </template>

        <template v-if="this.title === '导入文件'">
          <el-col :span="12">
            <el-form-item label="方式" prop="receiptsStatus">
              <el-select
                v-model="formData.receiptsStatus"
                placeholder="请选择"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="item in wayList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行类型" prop="receiptsStatus">
              <el-select
                v-model="formData.receiptsStatus"
                placeholder="请选择"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="item in brankList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行对账单管理" prop="brankAccount">
              <el-input
                v-model="formData.brankAccount"
                placeholder="银行对账单管理"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行账号" prop="brankAccount">
              <el-input
                v-model="formData.brankAccount"
                placeholder="银行账号"
              />
            </el-form-item>
          </el-col>
        </template>
      </el-form>
    </el-row>
    <div slot="footer" v-if="!disabled">
      <template v-if="this.title === '导入文件'">
        <el-button>文件</el-button>
        <el-button>模板XML</el-button>
        <el-button>模板XLS</el-button>
        <el-button>预览</el-button>
      </template>
      <el-button @click="save" type="primary">确定</el-button>
      <el-button @click="close">取消</el-button>
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
  import { formatDay } from '@/utils'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']

  export default {
    components: {},
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
          receiptsDate: '',
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
          brankAccount: [
            {
              required: true,
              message: '请输入银行账号',
              trigger: 'blur',
            },
          ],
          startDate: [
            {
              required: true,
              message: '请选择开始日期',
              trigger: 'blur',
            },
          ],
          endDate: [
            {
              required: true,
              message: '请选择结束日期',
              trigger: 'blur',
            },
          ],
          adjustmentDate: [
            {
              required: true,
              message: '请选择调整日期',
              trigger: 'blur',
            },
          ],
          endingBalance: [
            {
              required: true,
              message: '请选择期末余额',
              trigger: 'blur',
            },
          ],
        },
        dialogJdVisible: false,
        wayList: [
          {
            value: '1',
            label: '按日覆盖',
          },
        ],
        brankList: [
          {
            value: '1',
            label: '工商银行',
          },
        ],
        disabled: false,
        editId: '',
        multipleSelection: [],
        isUnfoldAuditShow: false,
      }
    },
    methods: {
      async showEdit(type) {
        this.dialogJdVisible = true
        this.title = type === 'on_line' ? '在线下载' : '导入文件'

        // if (row) {
        //   this.editId = row.tbid
        //   const {
        //     data: { data },
        //   } = await lxjyzypgBaseDetail({ tbid: row.tbid })
        //   this.formData.tbname = data.tbname
        //   this.formData.tbrgname = data.tbrgname
        //   this.formData.tbrgid = data.tbrgid
        //   this.formData.createdate = data.createdate
        //   this.formData.createname = data.createname
        //   this.formData.tbid = data.tbid
        //   this.getTableList()
        // }
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
</style>
