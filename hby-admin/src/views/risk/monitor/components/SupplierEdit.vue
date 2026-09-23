<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      v-if="dialogFormVisible"
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          label-width="120px"
          :model="formData"
          :rules="rules"
          size="medium"
        >
          <el-col :span="12">
            <el-form-item label="公司名称" prop="companyName">
              <el-input
                v-model="formData.companyName"
                placeholder="请输入公司名称"
                :style="{ width: 'calc(100% - 70px)' }"
              />
              <el-button
                type="primary"
                size="small"
                style="margin-left: 8px"
                @click="openCompanySearch"
              >选择</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险状况" prop="fxtype">
              <el-select
                v-model="formData.fxtype"
                clearable
                placeholder="风险状况"
              >
                <el-option
                  v-for="item in typeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="监控外部数据" prop="priceid">
              <el-checkbox-group v-model="formData.priceid">
                <el-checkbox
                  v-for="item in field103Options"
                  :key="item.interfacename"
                  :label="item.priceid"
                >
                  {{ item.interfacename }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="监控内部数据" prop="pageid">
              <el-checkbox-group v-model="formData.pageid">
                <el-checkbox
                  v-for="item in field104Options"
                  :key="String(item.stepid)"
                  :label="String(item.stepid)"
                >
                  {{ item.steptitle }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>

    <!-- 企业搜索选择弹窗 -->
    <el-dialog
      title="搜索企业"
      :visible.sync="companySearchVisible"
      width="860px"
      append-to-body
      :close-on-click-modal="false"
      @close="closeCompanySearch"
    >
      <div style="margin-bottom: 12px; display: flex; gap: 8px;">
        <el-input
          v-model="companyKeyword"
          placeholder="请输入企业名称关键词"
          style="width: 320px"
          clearable
          @keyup.enter.native="doCompanySearch"
        />
        <el-button type="primary" icon="el-icon-search" :loading="companySearchLoading" @click="doCompanySearch">查询</el-button>
        <el-button @click="companyKeyword = ''; companyList = []">重置</el-button>
      </div>
      <el-table
        ref="companyTable"
        :data="companyList"
        v-loading="companySearchLoading"
        tooltip-effect="dark"
        highlight-current-row
        @current-change="handleCompanySelect"
        @row-dblclick="handleCompanyDblClick"
        style="width: 100%; cursor: pointer;"
      >
        <el-table-column label="企业名称" prop="entName" show-overflow-tooltip />
        <el-table-column label="统一社会信用代码" prop="creditCode" width="190" align="center" />
        <el-table-column label="法定代表人" prop="legalPerson" width="120" align="center" />
      </el-table>
      <div v-if="!companySearchLoading && companyList.length === 0 && companySearched" style="text-align:center; color:#999; padding: 20px 0;">
        未查询到相关企业，请尝试其他关键词
      </div>
      <template #footer>
        <el-button @click="closeCompanySearch">取 消</el-button>
        <el-button type="primary" :disabled="!selectedCompany" @click="confirmCompanySelect">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import {
    saveCompanyInfo,
    getGyDetail,
    updateCompanyInfo,
  } from '@/api/risk/monitor'
  import { querySendRiskModelListByStaff } from '@/api/risk'
  import { CJBDI_MONITOR_ITEMS } from '@/config/cjbdi-monitor-items'
  import { searchCjbdiEnterprise } from '@/api/risk/cjbdi'

  export default {
    name: 'SupplierEdit',
    components: {},
    inheritAttrs: false,
    props: ['type'],
    data() {
      return {
        addtype: 'add',
        title: '',
        dialogFormVisible: false,
        formData: {
          companyName: undefined,
          fxtype: undefined,
          priceid: [],
          pageid: [],
        },
        list: [],
        tableData: [],
        rules: {
          companyName: [
            {
              required: true,
              message: '请输入公司名称',
              trigger: 'blur',
            },
          ],
          fxtype: [
            {
              required: true,
              message: '请选择风险状况',
              trigger: 'blur',
            },
          ],
          priceid: [
            {
              required: true,
              message: '请选择监控外部数据',
              trigger: 'change',
            },
          ],
          pageid: [
            {
              required: true,
              message: '请选择监控内部数据',
              trigger: 'change',
            },
          ],
        },
        typeOptions: [
          { label: '特别预警', value: '特别预警' },
          { label: '一般预警', value: '一般预警' },
          { label: '关注', value: '关注' },
          { label: '正常', value: '正常' },
        ],
        // 监控外部数据选项：使用 CJBDI 配置，排除企业名录通用版（cjbdi_02）
        field103Options: CJBDI_MONITOR_ITEMS.filter(
          (item) => item.priceid !== 'cjbdi_02'
        ),
        field104Options: [],
        teamid: undefined,
        // 企业搜索弹窗
        companySearchVisible: false,
        companyKeyword: '',
        companyList: [],
        companySearchLoading: false,
        companySearched: false,
        selectedCompany: null,
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      // ---- 企业搜索弹窗相关方法 ----
      openCompanySearch() {
        this.companyKeyword = ''
        this.companyList = []
        this.companySearched = false
        this.selectedCompany = null
        this.companySearchVisible = true
      },
      async doCompanySearch() {
        if (!this.companyKeyword.trim()) {
          this.$message.warning('请输入企业名称关键词')
          return
        }
        this.companySearchLoading = true
        this.companySearched = true
        this.selectedCompany = null
        try {
          const res = await searchCjbdiEnterprise({ keyword: this.companyKeyword.trim() })
          if (res.code === 200 && res.data) {
            this.companyList = Array.isArray(res.data) ? res.data : []
          } else {
            this.companyList = []
            if (res.msg) this.$message.warning(res.msg)
          }
        } catch (e) {
          console.error('企业搜索失败', e)
          this.$message.error('查询失败，请稍后重试')
          this.companyList = []
        } finally {
          this.companySearchLoading = false
        }
      },
      handleCompanySelect(row) {
        this.selectedCompany = row
      },
      confirmCompanySelect() {
        if (!this.selectedCompany) {
          this.$message.warning('请先选择一家企业')
          return
        }
        // 将选中企业名称回填到公司名称输入框
        this.formData.companyName = this.selectedCompany.entName || this.selectedCompany.name || ''
        this.companySearchVisible = false
      },
      // 双击行直接回填，无需点确定
      handleCompanyDblClick(row) {
        this.formData.companyName = row.entName || row.name || ''
        this.companySearchVisible = false
      },
      closeCompanySearch() {
        this.companySearchVisible = false
        this.selectedCompany = null
      },
      async save() {
        this.$refs['elForm'].validate(async (valid) => {
          if (valid) {
            const info = {
              ...this.formData,
              pageid: this.formData.pageid.toString(),
              priceid: this.formData.priceid.toString(),
              teamid: this.teamid,
              fxjktype: this.type,
            }
            if (this.addtype === 'add') {
              await saveCompanyInfo({ ...info })
              this.$baseMessage('添加成功', 'success', 'vab-hey-message-success')
            }
            if (this.addtype === 'edit') {
              await updateCompanyInfo({ ...info })
              this.$baseMessage('修改成功', 'success', 'vab-hey-message-success')
            }
            this.dialogFormVisible = false
            this.$emit('resetSearch')
            this.$bus.$emit('reloadTopbar')
          }
        })
      },
      async showEdit(row, type) {
        this.formData = {
          companyName: undefined,
          fxtype: undefined,
          priceid: [],
          pageid: [],
        }
        const {
          data: { tlist },
        } = await querySendRiskModelListByStaff()
        this.field104Options = tlist
        this.addtype = type
        if (type === 'add') {
          this.title = '添加'
          this.teamid = row
          this.dialogFormVisible = true
        } else {
          this.title = '修改'
          this.teamid = row.teamid
          getGyDetail({ companyid: row.companyid }).then((res) => {
            const { data, code } = res
            if (code == 200) {
              this.formData.companyName = data.yyCompany.companyname
              this.formData.fxtype = data.yyCompany.fxtype
              this.formData.companyid = data.yyCompany.companyid
              let arr = []
              if (data.checkNb && data.checkNb.length) {
                // 统一转为 String，与 :label="String(item.stepid)" 保持类型一致
                data.checkNb.map((item) => {
                  arr.push(String(item))
                })
              }
              this.formData.pageid = arr
              if (data.yyCompany.priceList && data.yyCompany.priceList.length) {
                // 回显时过滤掉 cjbdi_02，保持与选项列表一致
                this.formData.priceid = data.yyCompany.priceList
                  .map((item) => item.priceid)
                  .filter((id) => id !== 'cjbdi_02')
              }
            }
            // 数据加载完成后再打开弹窗，确保监控内部数据能正确回显
            this.dialogFormVisible = true
          })
        }
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style></style>
