<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="外币兑换"
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
          <el-form-item label="财务组织" prop="organization">
            <el-input v-model="formData.organization" placeholder="财务组织" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单据日期" prop="receiptsDate">
            <el-date-picker
              v-model="formData.receiptsDate"
              type="date"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="业务类型" prop="businessType">
            <el-select
              v-model="formData.businessType"
              placeholder="请选择"
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
          <el-form-item label="审批状态" prop="approvalStatus">
            <el-select
              v-model="formData.approvalStatus"
              placeholder="请选择"
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
          <el-form-item label="结算状态" prop="settlementStatus">
            <el-select
              v-model="formData.settlementStatus"
              placeholder="请选择"
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
          <el-form-item label="单据编号" prop="receiptsCode">
            <el-input v-model="formData.receiptsCode" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结算方式" prop="receiptsCode">
            <el-input v-model="formData.receiptsCode" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单据状态" prop="approvalStatus">
            <el-select
              v-model="formData.approvalStatus"
              placeholder="请选择"
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
          <el-form-item label="利润中心" prop="profitCenter">
            <el-input v-model="formData.profitCenter" />
          </el-form-item>
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
              买入信息
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow1">
            <el-col :span="12">
              <el-form-item label="买入币种" prop="currencyPurchased">
                <el-input v-model="formData.currencyPurchased" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="买入金额" prop="purchaseAmount">
                <el-input v-model="formData.purchaseAmount" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="买入银行账户" prop="purchaseBankAccount">
                <el-input v-model="formData.purchaseBankAccount" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="本币汇率" prop="currencyExchange1">
                <el-input v-model="formData.currencyExchange1" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="本币金额" prop="exchangeRate1">
                <el-input v-model="formData.exchangeRate1" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="交易价" prop="dealPrice">
                <el-input v-model="formData.dealPrice" />
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
              卖入信息
            </div>
            <div class="line"></div>
          </div>
          <template v-if="isUnfoldAuditShow2">
            <el-col :span="12">
              <el-form-item label="卖出币种" prop="soldCurrency">
                <el-input v-model="formData.soldCurrency" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="卖出金额" prop="soldAccount">
                <el-input v-model="formData.soldAccount" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="卖出银行账" prop="soldBankAccount">
                <el-input v-model="formData.soldBankAccount" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="本币汇率" prop="currencyExchange2">
                <el-input v-model="formData.currencyExchange2" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="本币金额" prop="exchangeRate2">
                <el-input v-model="formData.exchangeRate2" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="交易价" prop="dealPrice2">
                <el-input v-model="formData.dealPrice2" />
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
              <el-form-item label="付费币种" prop="currency">
                <el-input v-model="formData.currency" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="付费金额" prop="currentAccount">
                <el-input v-model="formData.currentAccount" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="付手续费账户" prop="bankAccount">
                <el-input v-model="formData.bankAccount" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="本币汇率" prop="originalAmount">
                <el-input v-model="formData.originalAmount" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="本币金额" prop="exchangeRate">
                <el-input v-model="formData.exchangeRate" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="汇兑损益" prop="money">
                <el-input v-model="formData.money" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="备注" prop="money">
                <el-input
                  type="textarea"
                  :rows="2"
                  placeholder="请输入内容"
                  v-model="formData.textarea"
                ></el-input>
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
              <el-form-item label="制单人" prop="prepared">
                <el-input v-model="formData.prepared" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="制单日期" prop="preparedDate">
                <el-date-picker
                  v-model="formData.preparedDate"
                  type="date"
                  placeholder="选择日期"
                  :style="{ width: '100%' }"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="制单时间" prop="preparedDate">
                <el-time-select
                  v-model="formData.value"
                  :picker-options="{
                    start: '08:30',
                    step: '00:15',
                    end: '18:30',
                  }"
                  placeholder="选择时间"
                  :style="{ width: '100%' }"
                ></el-time-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="审批人" prop="approver">
                <el-input v-model="formData.approver" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="审批日期" prop="approverDate">
                <el-date-picker
                  v-model="formData.receiptsDate"
                  type="date"
                  placeholder="选择日期"
                  :style="{ width: '100%' }"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="审批时间" prop="preparedDate">
                <el-time-select
                  v-model="formData.value"
                  :picker-options="{
                    start: '08:30',
                    step: '00:15',
                    end: '18:30',
                  }"
                  placeholder="选择时间"
                  :style="{ width: '100%' }"
                ></el-time-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结算人" prop="closeAccount">
                <el-input v-model="formData.closeAccount" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结算日期" prop="closeAccountDate">
                <el-date-picker
                  v-model="formData.receiptsDate"
                  type="date"
                  placeholder="选择日期"
                  :style="{ width: '100%' }"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="提交人" prop="submitBy">
                <el-input v-model="formData.submitBy" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="提交日期" prop="submitByDate">
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
          businessType: '',
          receiptsCode: '',
          receiptsStatus: '',
          approvalStatus: '',
          profitCenter: '',
          settlementStatus: '',

          currencyPurchased: '',
          purchaseAmount: '',
          purchaseBankAccount: '',
          currencyExchange1: '',
          exchangeRate1: '',
          dealPrice: '',

          soldCurrency: '',
          soldAccount: '',
          soldBankAccount: '',
          originalAmount: '',
          submitBy: '',
          submitByDate: '',
          approver: '',
          approverDate: '',
          closeAccount: '',
          closeAccountDate: '',
        },
        rules: {
          organization: [
            {
              required: true,
              message: '请输入所属组织',
              trigger: 'blur',
            },
          ],
          receiptsDate: [
            {
              required: true,
              message: '请输入单据日期',
              trigger: 'blur',
            },
          ],
          currentAccount: [
            {
              required: true,
              message: '请输入现金账户',
              trigger: 'blur',
            },
          ],
          bankAccount: [
            {
              required: true,
              message: '请输入银行账户',
              trigger: 'blur',
            },
          ],
          originalAmount: [
            {
              required: true,
              message: '请输入原币金额',
              trigger: 'blur',
            },
          ],
          exchangeRate: [
            {
              required: true,
              message: '请输入组织本币汇率',
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
      handleIsUnfold1() {
        this.isUnfoldAuditShow1 = !this.isUnfoldAuditShow1
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
    width: 100px;
    text-align: center;
    cursor: pointer;
  }
  .line {
    flex: 1;
    width: 100%;
    height: 1px;
    border: 1px solid #cccccc6e;
  }
</style>
