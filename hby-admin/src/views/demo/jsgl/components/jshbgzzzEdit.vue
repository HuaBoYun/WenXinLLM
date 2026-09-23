<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="结算合并规则-集团"
    :visible.sync="dialogJdVisible"
    width="1000px"
    @close="close"
    v-if="dialogJdVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="160px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <el-col :span="12">
          <el-form-item label="财务组织" prop="organization">
            <el-input v-model="formData.organization" placeholder="所属集团" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="规则名称" prop="organization">
            <el-input v-model="formData.organization" placeholder="规则名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="规则编号" prop="organization">
            <el-input v-model="formData.organization" placeholder="规则编号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单据类型" prop="organization">
            <el-input v-model="formData.organization" placeholder="单据类型" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="交易类型" prop="organization">
            <el-input v-model="formData.organization" placeholder="交易类型" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="来源单据类型" prop="organization">
            <el-input
              v-model="formData.organization"
              placeholder="来源单据类型"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="来源交易类型" prop="organization">
            <el-input
              v-model="formData.organization"
              placeholder="来源交易类型"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="启用状态" prop="organization">
            <el-select
              v-model="formData.organization"
              clearable
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in jxTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="可合并" prop="name">
            <el-checkbox v-model="formData.membershipGroup"></el-checkbox>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合单时自动合行" prop="name">
            <el-checkbox v-model="formData.membershipGroup"></el-checkbox>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="新增单据自动合行" prop="code">
            <el-checkbox v-model="formData.membershipGroup"></el-checkbox>
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
              操作信息
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow">
            <el-col :span="12">
              <el-form-item label="启用人" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="启用日期" prop="updateDate">
                <el-date-picker
                  v-model="formData.receiptsDate"
                  type="date"
                  placeholder="选择日期"
                  :style="{ width: '100%' }"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="停用人" prop="updateDate">
                <el-input v-model="formData.updateDate" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="停用日期" prop="updateDate">
                <el-date-picker
                  v-model="formData.receiptsDate"
                  type="date"
                  placeholder="选择日期"
                  :style="{ width: '100%' }"
                ></el-date-picker>
              </el-form-item>
            </el-col>
          </template>
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
        jxTypeList: [
          {
            value: '1',
            label: '开启',
          },
          {
            value: '2',
            label: '关闭',
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
    cursor: pointer;
  }
  .line {
    flex: 1;
    width: 100%;
    height: 1px;
    border: 1px dashed #cccccc6e;
  }
</style>
