<!--
 * @Author: 康某 dev@example.com
 * @Date: 2022-09-19 20:52:58
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-09-19 20:53:31
 * @FilePath: \hb-admin\src\views\contract\contractManage\components\ProcessList.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <el-dialog
    title="选择流程"
    :close-on-click-modal="false"
    :visible.sync="dialogVisible"
    append-to-body
    width="60%"
    :modal="modal"
    @close="close"
    v-if="dialogVisible"
  >
    <el-form ref="form" :model="queryForm" label-width="0">
      <vab-query-form>
        <vab-query-form-left-panel :span="18">
          <el-form-item>
            <el-input
              v-model="queryForm.workName"
              placeholder="请输入流程名称"
              clearable
            />
          </el-form-item>
        </vab-query-form-left-panel>
        <vab-query-form-right-panel :span="6">
          <el-form-item>
            <el-button type="primary" @click="fetchData">查询</el-button>
            <!-- 重置 -->
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </vab-query-form-right-panel>
      </vab-query-form>
    </el-form>
    <el-table
      :data="list"
      highlight-current-row
      @current-change="handleTableCurrentChange"
    >
      <el-table-column
        label="序号"
        align="center"
        prop="rowNo"
      ></el-table-column>
      <el-table-column
        align="center"
        label="流程名称"
        prop="ymWorkName"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        align="center"
        label="版本"
        prop="version"
      ></el-table-column>
      <el-table-column align="center" label="状态" prop="qyStats">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.qyStats == '1'" type="success">启用</el-tag>
          <el-tag v-else type="danger">未启用</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            @click="handleChangeStatus(scope.row)"
          >
            {{ scope.row.qyStats == 1 ? '弃用' : '启用' }}
          </el-button>
          <el-button type="text" size="small" @click="handleCopy(scope.row)">
            复制
          </el-button>
          <el-button type="text" size="small" @click="handleEdit(scope.row)">
            编辑
          </el-button>
          <!-- 删除 -->
          <el-button type="text" size="small" @click="handleDelete(scope.row)">
            删除
          </el-button>
          <!-- <el-button
            type="text"
            size="small"
            @click="handleSelectCompany(scope.row, true)"
          >
            流程分配
          </el-button>
          <el-button
            type="text"
            size="small"
            @click="handleSelectCompany(scope.row, false)"
          >
            取消分配
          </el-button> -->
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </span>
    <el-dialog
      title="选择候选人"
      :visible.sync="dialogVisible3"
      :close-on-click-modal="false"
      width="40%"
      :modal="false"
      @close="close3"
    >
      <el-form
        :model="formData2"
        :rules="rules2"
        ref="ruleForm2"
        label-width="80px"
      >
        <el-form-item label="候选人" prop="transferStaffName">
          <el-input
            disabled
            placeholder="请选择候选人"
            v-model="formData2.transferStaffName"
            style="width: 79%; margin-right: 8px"
          ></el-input>
          <el-button type="primary" @click="handleSelect">请选择</el-button>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close3">取 消</el-button>
        <el-button type="primary" @click="save3">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="提交审核"
      :modal="false"
      :visible.sync="dialogVisible2"
      :close-on-click-modal="false"
      width="40%"
      @close="close2"
    >
      <el-form
        :model="formData"
        :rules="rules"
        ref="ruleForm"
        label-width="80px"
      >
        <el-form-item label="分支选择" prop="value">
          <el-select
            v-model="formData.value"
            placeholder="请选择分支"
            style="width: 500px"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close2">取 消</el-button>
        <el-button type="primary" @click="save2">确 定</el-button>
      </span>
    </el-dialog>
    <CandidateList ref="candidateList" @selected="handSelected" />
    <CompanyList ref="companyList" @selected="selectedCompany" />
    <CompanyTreeModal
      ref="companyTreeModal"
      @selected="handleCompanyTreeSelected"
    />
    <ProcessList ref="process" @fetchData="fetchData" />
  </el-dialog>
</template>

<script>
  import {
    getAllFlowList,
    saveAllNewFlowList,
    submitByYmWork,
    SSOToJNFD,
    removeWorkFlowFormInfo,
    copyWorkFlowFormInfo,
  } from '@/api/setting/system'
  import CandidateList from '@/components/CandidateList'
  import store from '@/store'
  import CompanyList from './ChooseCompanyList.vue'
  import CompanyTreeModal from '@/components/CompanyTreeModal'
  import { getFlowList } from '@/api/setting/auth'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  export default {
    props: {
      modal: {
        type: Boolean,
        default: true,
      },
    },
    components: {
      CandidateList,
      CompanyList,
      CompanyTreeModal,
      ProcessList,
    },
    data() {
      return {
        dialogVisible: false,
        dialogVisible2: false,
        dialogVisible3: false,
        current: null,

        curretnRow: null,
        options: [],
        type: '',
        formData: {
          value: [],
        },
        rules: {
          value: [{ required: true, message: '请选择分支', trigger: 'change' }],
        },
        formData2: {
          transferStaffName: '',
          transferStaffId: '',
        },
        rules2: {
          transferStaffName: [
            { required: true, message: '请选择候选人', trigger: 'change' },
          ],
        },
        candidateData: {},
        candidateType: '',
        tablId: '',
        list: [],
        total: 0,
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          workName: '',
          tablId: '',
          pageNumber: 1,
          pageSize: 20,
        },
        row: {},
        requireValuedata: false, // 是否需要流程校验
      }
    },
    created() {
      const info = JSON.parse(localStorage.getItem('userInfo'))
      // 判断是否需要流程校验
      if (info.requireValuedata) {
        this.requireValuedata = info.requireValuedata
      }
    },
    methods: {
      handleEdit(row) {
        const token = store.getters['user/token']

        SSOToJNFD()
          .then((result) => {
            window.open(
              // 'http://localhost:3000/workFlow/flowEngine?token=' +
              // 'https://192.0.2.14:9100/workFlow/flowEngine?token=' +
              'https://www.example.com/workFlow/flowEngine?token=' +
                result.data.ymToken +
                '&showzdy=edit' +
                '&id=' +
                row.ymWorkFrom +
                '&tableId=' +
                this.queryForm.tablId +
                '&xtoken=' +
                token
            )
          })
          .catch((err) => {})
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { code, data } = await removeWorkFlowFormInfo({
            ymWorkId: row.ymWorkFrom,
            tableId: this.queryForm.tablId,
          })
          // 流程校验
          if (this.requireValuedata) {
            const userInfo = JSON.parse(localStorage.getItem('userInfo'))
            //  查询当前是否有流程
            getFlowList({
              targetId: userInfo.linkOrg.orgid + '-' + row.ymWorkFrom,
              targetType: 'commonflow',
              operationType: 3,
            }).then((res) => {
              if (res.data == 0) {
                // 可以提交流程
                this.$refs['process'].save(220, data.recordId)
                this.$baseMessage(
                  '审批流程提交成功,请等待审批',
                  'success',
                  'vab-hey-message-success'
                )
                this.close()
              } else {
                // 不可以提交流程
                this.$baseMessage(
                  '当前用户流程已存在,请先走审批流程',
                  'error',
                  'vab-hey-message-error'
                )
                return
              }
            })
          } else {
            if (code == 1) {
              this.$message.success('删除成功')
              this.queryData()
            }
          }
        })
      },
      handleSelect() {
        //
        this.$refs['candidateList'].show(this.candidateData)
      },
      handSelected(data) {
        let name = '',
          id = ''
        data.map((item) => {
          name += item.fullName + ','
          id += item.id + ','
        })
        name = name.substring(0, name.length - 1)
        id = id.substring(0, id.length - 1)
        this.formData2.transferStaffName = name
        this.formData2.transferStaffId = id
      },

      async save() {
        if (!this.current) return this.$message.warning('请选择流程')

        let param = {
          ymWorkForm: this.current.ymWorkFrom,
          tablId: this.tablId,
        }
        const { data, code } = await saveAllNewFlowList(param)

        if (code == 1) {
          this.$message.success('提交成功')
          this.dialogVisible = false
        }
      },
      async save2() {
        //
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let branchStrs = this.formData.value
            // this.formData.value.map((item) => {
            //   branchStrs = branchStrs + item + ','
            // })
            // branchStrs = branchStrs.substring(0, branchStrs.length - 1)

            const { data, code } = await submitByYmWork({
              flowId: this.current.ymWorkFrom,
              fromId: this.curretnRow.contractid,
              branchStrs,
              candidateType: this.candidateType,
            })
            if (code == 1) {
              this.$message.success('提交成功')
              this.close2()
              this.close()
            }
          } else {
            return false
          }
        })
      },

      save3() {
        this.$refs['ruleForm2'].validate(async (valid) => {
          if (valid) {
            const { data, code } = await submitByYmWork({
              flowId: this.current.ymWorkFrom,
              fromId: this.curretnRow.contractid,
              candidateList: this.formData2.transferStaffId,
              nodeCode: this.candidateData.nodeId,
              candidateType: this.candidateType,
            })
            if (code == 1) {
              this.$message.success('提交成功')
              this.close3()
              this.close()
            }
          } else {
            return false
          }
        })
      },
      resetQueryForm() {
        this.queryForm.pageNumber = 1
        this.queryForm.workName = ''
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getAllFlowList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async show(e, type) {
        //type为类型  2相对方维护 3黑名单 4合同范本  5合同订立  6合同用印 7合同变更  13合同借阅
        this.dialogVisible = true
        this.tablId = type
        // this.curretnRow = e
        this.type = type
        this.queryForm.tablId = type
        this.fetchData()
      },
      handleTableCurrentChange(e) {
        this.current = e
      },
      handleSelectUser3() {},

      close() {
        this.current = null
        this.curretnRow = null
        this.tableData = []
        this.dialogVisible = false
        this.queryForm = this.$options.data().queryForm
        this.$emit('fetchData')
        this.$bus.$emit('updateMsg', 0)
      },
      close2() {
        this.dialogVisible2 = false
      },
      close3() {
        this.dialogVisible3 = false
      },
      handleCopy(row) {
        this.row = row
        this.$refs['companyTreeModal'].show(1, null, '复制公司')

        // this.$baseConfirm('你确定要复制当前项吗', null, async () => {
        //   const { code, data } = await copyWorkFlowFormInfo({
        //     ymWorkFrom: row.ymWorkFrom,
        //     tableId: this.queryForm.tablId,
        //   })
        //   if (code == 1) {
        //     this.$message.success('复制成功')
        //     this.queryData()
        //   }
        // })
      },
      async handleCompanyTreeSelected(node) {
        if (node instanceof Array) {
          console.log('node', node)
          let orgIds = node.map((item) => item.id).join(',')
          const { code, data } = await copyWorkFlowFormInfo({
            ymWorkFrom: this.row.ymWorkFrom,
            tableId: this.queryForm.tablId,
            orgIds,
          })
          if (code == 1) {
            this.$message.success('复制成功')
            this.queryData()
          }
        } else {
          // const { oid2, org2, ...other } = this.form
          // let form = { oid2: node.id, org2: node.name, ...other }
          // this.form = form
        }
      },
      async handleChangeStatus(row) {
        let param = {
          ymWorkForm: row.ymWorkFrom,
          tablId: this.queryForm.tablId,
          qystatus: row.qyStats == 1 ? 0 : 1,
        }
        const { data, code } = await saveAllNewFlowList(param)
        // 流程校验
        if (this.requireValuedata) {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))

          //  查询当前是否有流程
          getFlowList({
            targetId: userInfo.linkOrg.orgid + '-' + this.queryForm.tablId,
            targetType: 'commonflow',
            operationType: row.qyStats == 1 ? 5 : 4,
          }).then((res) => {
            if (res.data == 0) {
              // 可以提交流程
              this.$refs['process'].save(220, data.recordId)
              this.$baseMessage(
                '审批流程提交成功,请等待审批',
                'success',
                'vab-hey-message-success'
              )
              this.close()
            } else {
              // 不可以提交流程
              this.$baseMessage(
                '当前用户流程已存在,请先走审批流程',
                'error',
                'vab-hey-message-error'
              )
              return
            }
          })
        } else {
          if (code == 1) {
            this.$message.success('更改成功')
            this.queryData()
          }
        }
      },
      selectedCompany() {},
      handleSelectCompany(row, isAll) {
        this.$refs['companyList'].show({ tableId: this.tablId, ...row }, isAll)
      },
    },
  }
</script>

<style lang="scss" scoped></style>
