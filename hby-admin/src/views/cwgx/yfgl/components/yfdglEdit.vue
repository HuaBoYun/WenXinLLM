<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="应付单管理"
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
          <el-form-item label="应付财务组织" prop="organization">
            <el-input
              v-model="formData.organization"
              placeholder="应付财务组织"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单据号" prop="name">
            <el-input v-model="formData.name" placeholder="单据号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单据日期" prop="province">
            <el-date-picker
              v-model="formData.receiptsDate"
              type="date"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="起算日期" prop="province">
            <el-date-picker
              v-model="formData.receiptsDate"
              type="date"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="往来对象" prop="city">
            <el-select
              v-model="formData.value"
              placeholder="往来对象"
              :style="{ width: '100%' }"
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
          <el-form-item label="应付类型" prop="status">
            <el-input v-model="formData.status" placeholder="付款类型" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="业务流程" prop="status">
            <el-input v-model="formData.status" placeholder="业务流程" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="红冲类型" prop="status">
            <el-input v-model="formData.status" placeholder="红冲类型" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="红冲标志" prop="status">
            <el-input v-model="formData.status" placeholder="红冲标志" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单据状态" prop="status">
            <el-input v-model="formData.status" placeholder="单据状态" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审批状态" prop="status">
            <el-input v-model="formData.status" placeholder="审批状态" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生效状态" prop="status">
            <el-input v-model="formData.status" placeholder="生效状态" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="制单人" prop="status">
            <el-input v-model="formData.status" placeholder="制单人" />
          </el-form-item>
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
              客商信息
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow">
            <el-col :span="12">
              <el-form-item label="供应商" prop="creator">
                <el-input v-model="formData.creator" placeholder="供应商" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="客户" prop="createDate">
                <el-input v-model="formData.createDate" placeholder="客户" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="部门" prop="updateDate">
                <el-input v-model="formData.updateDate" placeholder="部门" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="业务员" prop="updateDate">
                <el-input v-model="formData.updateDate" placeholder="业务员" />
              </el-form-item>
            </el-col>
          </template>
        </el-col>
        <el-col :span="24">
          <div class="show_line_box">
            <div class="title_l">
              <i
                @click="handleIsUnfold1"
                v-if="isUnfoldAuditShow1"
                class="el-icon-minus"
              ></i>
              <i @click="handleIsUnfold1" v-else class="el-icon-plus"></i>
              金额信息
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow1">
            <el-col :span="12">
              <el-form-item label="币种" prop="creator">
                <el-input v-model="formData.creator" placeholder="币种" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="组织本币汇率" prop="createDate">
                <el-input
                  v-model="formData.createDate"
                  placeholder="组织本币汇率"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="原币金额" prop="updateDate">
                <el-input
                  v-model="formData.updateDate"
                  placeholder="原币金额"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="组织本币金额" prop="updateDate">
                <el-input
                  v-model="formData.updateDate"
                  placeholder="组织本币金额"
                />
              </el-form-item>
            </el-col>
          </template>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否退款" prop="status">
            <el-input v-model="formData.status" placeholder="是否退款" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发票号" prop="status">
            <el-input v-model="formData.status" placeholder="发票号" />
          </el-form-item>
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
          organization: [
            {
              required: true,
              message: '请输入所属组织',
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
        isUnfoldAuditShow: false,
        isUnfoldAuditShow1: false,
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
