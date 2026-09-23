<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="应收核销方案设置"
    :visible.sync="dialogJdVisible"
    width="1000px"
    @close="close"
    v-if="dialogJdVisible"
  >
    <el-row :gutter="14">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="常用条件" name="first" />
        <el-tab-pane label="核销规则" name="second" />
        <el-tab-pane label="核销方式" name="third" />
      </el-tabs>
      <el-form
        ref="ruleForm"
        label-width="140px"
        :model="formData"
        :rules="rules"
        size="mini"
      >
        <template v-if="activeName === 'first'">
          <el-col :span="12">
            <el-form-item label="方案组织" prop="name">
              <el-input v-model="formData.name" placeholder="方案组织" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="方案名称" prop="name">
              <el-input v-model="formData.name" placeholder="方案名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="方案描述" prop="name">
              <el-input v-model="formData.name" placeholder="方案描述" />
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
                本方
              </div>
              <div class="line"></div>
            </div>
            <template v-if="isUnfoldAuditShow">
              <el-col :span="12">
                <el-form-item label="本方核销对象" prop="creator">
                  <el-input
                    v-model="formData.creator"
                    placeholder="本方核销对象"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="本方对象名称" prop="createDate">
                  <el-input
                    v-model="formData.createDate"
                    placeholder="本方对象名称"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="本方单据类型" prop="updateDate">
                  <el-input
                    v-model="formData.updateDate"
                    placeholder="本方单据类型"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="本方交易类型" prop="updateDate">
                  <el-input
                    v-model="formData.updateDate"
                    placeholder="本方交易类型"
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
                对方
              </div>
              <div class="line"></div>
            </div>
            <template v-if="isUnfoldAuditShow1">
              <el-col :span="12">
                <el-form-item label="对方核销对象" prop="creator">
                  <el-input
                    v-model="formData.creator"
                    placeholder="本方核销对象"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="对方对象名称" prop="createDate">
                  <el-input
                    v-model="formData.createDate"
                    placeholder="对方对象名称"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="对方单据类型" prop="updateDate">
                  <el-input
                    v-model="formData.updateDate"
                    placeholder="对方单据类型"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="对方交易类型" prop="updateDate">
                  <el-input
                    v-model="formData.updateDate"
                    placeholder="对方交易类型"
                  />
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
                公共条件
              </div>
              <div class="line"></div>
            </div>
            <template v-if="isUnfoldAuditShow2">
              <el-col :span="12">
                <el-form-item label="部门" prop="creator">
                  <el-input v-model="formData.creator" placeholder="部门" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="业务员" prop="createDate">
                  <el-input
                    v-model="formData.createDate"
                    placeholder="业务员"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="地区分类" prop="updateDate">
                  <el-input
                    v-model="formData.updateDate"
                    placeholder="地区分类"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="业务流程" prop="updateDate">
                  <el-input
                    v-model="formData.updateDate"
                    placeholder="业务流程"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="表头科目" prop="updateDate">
                  <el-input
                    v-model="formData.updateDate"
                    placeholder="表头科目"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="表体科目" prop="updateDate">
                  <el-input
                    v-model="formData.updateDate"
                    placeholder="表体科目"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="收支项目" prop="updateDate">
                  <el-input
                    v-model="formData.updateDate"
                    placeholder="收支项目"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="结算财务组织" prop="updateDate">
                  <el-input
                    v-model="formData.updateDate"
                    placeholder="结算财务组织"
                  />
                </el-form-item>
              </el-col>
            </template>
          </el-col>
        </template>
        <template v-if="activeName === 'second'">
          <el-col :span="12">
            <el-form-item label="红蓝对冲" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="金额方向" prop="name">
              <el-input v-model="formData.name" placeholder="金额方向" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="同币种核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="核销币种" prop="name">
              <el-input v-model="formData.name" placeholder="核销币种" />
            </el-form-item>
          </el-col>
        </template>
        <template v-if="activeName === 'third'">
          <el-col :span="8">
            <el-form-item label="按客户核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按供应商核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按业务员核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按部门核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按收支项目核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按合同号核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按成本中心核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按表头科目核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按表体科目核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按发票号核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按物料核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按原币余额核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按业务类型核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按结算方式核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按利润中心" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按产品线核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按项目核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按订单号核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按结算财务组织核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按销售部门核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按销售订单类型核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按销售组织核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按销售业务员核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按销售渠道类型核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按原币金额核销" prop="name">
              <el-checkbox v-model="formData.checked"></el-checkbox>
            </el-form-item>
          </el-col>
        </template>
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
      handleIsUnfold2() {
        this.isUnfoldAuditShow2 = !this.isUnfoldAuditShow2
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
