<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="到账通知自动生单规则"
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
            <el-input v-model="formData.organization" placeholder="财务组织" />
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
              生成条件
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow">
            <el-col :span="12">
              <el-form-item label="本方账号" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="交易类型对象" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="对方单位名称" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="对方账号" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="摘要条件" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="摘要条件内容" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
          </template>
        </el-col>

        <el-col :span="24">
          <div class="show_line_box">
            <div class="title_l">
              <i
                @click="handleIsUnfold2"
                v-if="isUnfoldAuditShow2"
                class="el-icon-minus"
              ></i>
              <i @click="handleIsUnfold2" v-else class="el-icon-plus"></i>
              目的单据信息
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow2">
            <el-col :span="12">
              <el-form-item label="收付性质" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="单据类型" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="单据交易类型" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="计划项目" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="托收协议" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="部门" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="人员" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="生成单据日期" prop="updateDate">
                <el-date-picker
                  v-model="formData.receiptsDate"
                  type="date"
                  placeholder="选择日期"
                  :style="{ width: '100%' }"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结算日期" prop="updateDate">
                <el-date-picker
                  v-model="formData.receiptsDate"
                  type="date"
                  placeholder="选择日期"
                  :style="{ width: '100%' }"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="上收下拨类型" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
          </template>
        </el-col>

        <el-col :span="24">
          <div class="show_line_box">
            <div class="title_l">
              <i
                @click="handleIsUnfold3"
                v-if="isUnfoldAuditShow3"
                class="el-icon-minus"
              ></i>
              <i @click="handleIsUnfold3" v-else class="el-icon-plus"></i>
              其他信息
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow3">
            <el-col :span="12">
              <el-form-item label="生效日期" prop="updateDate">
                <el-date-picker
                  v-model="formData.receiptsDate"
                  type="date"
                  placeholder="选择日期"
                  :style="{ width: '100%' }"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="状态" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
          </template>
        </el-col>

        <el-col :span="24">
          <div class="show_line_box">
            <div class="title_l">
              <i
                @click="handleIsUnfold4"
                v-if="isUnfoldAuditShow4"
                class="el-icon-minus"
              ></i>
              <i @click="handleIsUnfold4" v-else class="el-icon-plus"></i>
              操作信息
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow4">
            <el-col :span="12">
              <el-form-item label="创建人" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="创建时间" prop="updateDate">
                <el-date-picker
                  v-model="formData.receiptsDate"
                  type="date"
                  placeholder="选择日期"
                  :style="{ width: '100%' }"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最后修改人" prop="creator">
                <el-input v-model="formData.creator" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="最后修改时间" prop="updateDate">
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
        isUnfoldAuditShow: true,
        isUnfoldAuditShow2: false,
        isUnfoldAuditShow3: false,
        isUnfoldAuditShow4: false,
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
      handleIsUnfold2() {
        this.isUnfoldAuditShow2 = !this.isUnfoldAuditShow2
      },
      handleIsUnfold3() {
        this.isUnfoldAuditShow3 = !this.isUnfoldAuditShow3
      },
      handleIsUnfold4() {
        this.isUnfoldAuditShow4 = !this.isUnfoldAuditShow4
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
    width: 102px;
    text-align: left;
    cursor: pointer;
  }
  .line {
    flex: 1;
    width: 100%;
    height: 1px;
    border: 1px dashed #cccccc6e;
  }
</style>
