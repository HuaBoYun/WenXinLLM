<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="借款控制规则-组织"
    :visible.sync="dialogJdVisible"
    width="1000px"
    @close="close"
    v-if="dialogJdVisible"
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
          <el-form-item label="编码" prop="code">
            <el-input v-model="formData.code" placeholder="编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="名称" prop="name">
            <el-input v-model="formData.name" placeholder="名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="币种" prop="name">
            <el-input v-model="formData.name" placeholder="币种" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="按组织本币控制" prop="name">
            <el-switch
              v-model="formData.name"
              active-color="#13ce66"
              inactive-color="#ff4949"
            ></el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制对象" prop="name">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.name"
              placeholder="控制对象"
              clearable
            >
              <el-option
                v-for="item in options_1"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="控制类型" prop="name">
            <el-select
              :style="{ width: '100%' }"
              v-model="formData.name"
              placeholder="控制对象"
              clearable
            >
              <el-option
                v-for="item in options_2"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
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
              高级
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow">
            <el-col :span="12">
              <el-form-item label="交易类型" prop="creator">
                <el-input v-model="formData.creator" placeholder="交易类型" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结算方式" prop="createDate">
                <el-input
                  v-model="formData.createDate"
                  placeholder="结算方式"
                />
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
              控制方式
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow1">
            <el-col :span="12">
              <el-form-item prop="creator">
                <el-checkbox v-model="formData.creator"></el-checkbox>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="前款不清后款不借" prop="createDate">
                <el-input
                  v-model="formData.createDate"
                  placeholder="前款不清后款不借"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="creator">
                <el-checkbox v-model="formData.creator"></el-checkbox>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="允许借款总额" prop="updateDate">
                <el-input
                  v-model="formData.updateDate"
                  placeholder="允许借款总额"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="creator">
                <el-checkbox v-model="formData.creator"></el-checkbox>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="允许借款单张数" prop="updateDate">
                <el-input
                  v-model="formData.updateDate"
                  placeholder="允许借款单张数"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="creator">
                <el-checkbox v-model="formData.creator"></el-checkbox>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="允许最长借款天数" prop="updateDate">
                <el-input
                  v-model="formData.updateDate"
                  placeholder="允许最长借款天数"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="creator">
                <el-checkbox v-model="formData.creator"></el-checkbox>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="控制到期未报销" prop="updateDate">
                <el-input
                  v-model="formData.updateDate"
                  placeholder="控制到期未报销"
                />
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
          radioName: 1,
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
          code: [
            {
              required: true,
              message: '请输入编码',
              trigger: 'blur',
            },
          ],
          name: [
            {
              required: true,
              message: '请输入名称',
              trigger: 'blur',
            },
          ],
          creator: [
            {
              required: true,
              message: '请输入交易类型',
              trigger: 'blur',
            },
          ],
        },
        dialogJdVisible: false,
        options_1: [
          {
            value: '1',
            label: '借款人',
          },
          {
            value: '2',
            label: '借款部门',
          },
          {
            value: '3',
            label: '承担部门',
          },
        ],
        options_2: [
          {
            value: '1',
            label: '提示',
          },
          {
            value: '2',
            label: '控制',
          },
        ],
        disabled: false,
        editId: '',
        multipleSelection: [],
        isUnfoldAuditShow: false,
        isUnfoldAuditShow1: false,
        isUnfoldAuditShow2: false,
        activeName: 'first',
      }
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event)
      },
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

      handleChange(value) {
        console.log('选择的值:', value)
        this.formData.radioName = value
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
