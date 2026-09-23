<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      width="80%"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      @close="handleClose"
    >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      :disabled="isDetail"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目选择" prop="projectId">
            <el-input v-model="selectedProjectName" placeholder="请选择项目" readonly>
              <el-button slot="append" @click="showProjectSelector">选择项目</el-button>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="债权类型" prop="debtType">
            <el-select v-model="form.debtType" placeholder="请选择债权类型" style="width: 100%">
              <el-option label="应收账款" :value="1" />
              <el-option label="预付款项" :value="2" />
              <el-option label="其他应收款" :value="3" />
              <el-option label="保证金" :value="4" />
              <el-option label="违约金" :value="5" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="债权ID" prop="debtId">
            <el-input v-model="form.debtId" placeholder="请输入债权ID" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="债务人" prop="debtorName">
            <el-input v-model="form.debtorName" placeholder="请输入债务人名称" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="债务人联系方式" prop="debtorContact">
            <el-input v-model="form.debtorContact" placeholder="请输入债务人联系方式" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="债务人地址" prop="debtorAddress">
            <el-input v-model="form.debtorAddress" placeholder="请输入债务人地址" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="债权金额" prop="debtAmount">
            <el-input-number
              v-model="form.debtAmount"
              :min="0"
              :precision="2"
              placeholder="请输入债权金额"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="已回收金额" prop="collectedAmount">
            <el-input-number
              v-model="form.collectedAmount"
              :min="0"
              :precision="2"
              placeholder="请输入已回收金额"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="形成日期" prop="debtDate">
            <el-date-picker
              v-model="form.debtDate"
              type="date"
              placeholder="选择形成日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="到期日期" prop="dueDate">
            <el-date-picker
              v-model="form.dueDate"
              type="date"
              placeholder="选择到期日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="利率(%)" prop="interestRate">
            <el-input-number
              v-model="form.interestRate"
              :min="0"
              :max="100"
              :precision="2"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>

      </el-row>



      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="收款难度" prop="collectionDifficulty">
            <el-select v-model="form.collectionDifficulty" placeholder="请选择收款难度" style="width: 100%">
              <el-option label="容易" :value="1" />
              <el-option label="一般" :value="2" />
              <el-option label="困难" :value="3" />
              <el-option label="极难" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="债权状态" prop="debtStatus" v-if="isDetail">
            <el-select v-model="form.debtStatus" disabled style="width: 100%">
              <el-option label="正常" :value="1" />
              <el-option label="逾期" :value="2" />
              <el-option label="催收中" :value="3" />
              <el-option label="已回收" :value="4" />
              <el-option label="坏账" :value="5" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="债权描述" prop="debtDescription">
        <el-input
          v-model="form.debtDescription"
          type="textarea"
          :rows="3"
          placeholder="请输入债权描述"
        />
      </el-form-item>

      <el-form-item label="合同条款" prop="contractTerms">
        <el-input
          v-model="form.contractTerms"
          type="textarea"
          :rows="3"
          placeholder="请输入相关合同条款"
        />
      </el-form-item>

      <el-form-item label="法律依据" prop="legalBasis">
        <el-input
          v-model="form.legalBasis"
          type="textarea"
          :rows="2"
          placeholder="请输入法律依据"
        />
      </el-form-item>

      <el-form-item label="催收策略" prop="collectionStrategy">
        <el-input
          v-model="form.collectionStrategy"
          type="textarea"
          :rows="2"
          placeholder="请输入催收策略"
        />
      </el-form-item>

      <el-form-item label="风险评估" prop="riskAssessment">
        <el-input
          v-model="form.riskAssessment"
          type="textarea"
          :rows="3"
          placeholder="请输入风险评估"
        />
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="form.remarks"
          type="textarea"
          :rows="2"
          placeholder="请输入备注"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSave" v-if="!isDetail">保存</el-button>
    </div>
  </el-dialog>

  <!-- 项目选择对话框 -->
  <el-dialog
    title="选择项目"
    :visible.sync="projectSelectorVisible"
    width="80%"
    :close-on-click-modal="false"
  >
    <el-form :inline="true" :model="projectQuery" class="demo-form-inline">
      <el-form-item label="项目名称">
        <el-input v-model="projectQuery.projectName" placeholder="请输入项目名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchProjects">查询</el-button>
        <el-button @click="resetProjectQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="projectListLoading"
      :data="projectList"
      @selection-change="handleProjectSelectionChange"
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="registerNo" label="登记编号" width="150" />
      <el-table-column prop="projectName" label="项目名称" min-width="200" />
      <el-table-column prop="projectType" label="项目类型" width="120" />
      <el-table-column prop="projectStatus" label="项目状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.projectStatus === 1 ? 'success' : 'info'">
            {{ scope.row.projectStatus === 1 ? '进行中' : '已完成' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="150" />
    </el-table>

    <el-pagination
      @size-change="handleProjectSizeChange"
      @current-change="handleProjectCurrentChange"
      :current-page="projectQuery.pageNumber"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="projectQuery.pageSize"
      :total="projectTotal"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 20px"
    />

    <div slot="footer" class="dialog-footer">
      <el-button @click="projectSelectorVisible = false">取消</el-button>
      <el-button type="primary" @click="confirmProjectSelection">确定</el-button>
    </div>
  </el-dialog>
  </div>
</template>

<script>
  import {
    createDebtRecord,
    updateDebtRecord,
    getProjectList
  } from '@/api/contract/debt'

  export default {
    name: 'DebtEdit',
    data() {
      return {
        dialogVisible: false,
        isDetail: false,
        title: '',
        form: {
          id: null,
          projectId: null,
          projectName: '',
          debtType: null,
          debtId: '',
          debtorName: '',
          debtorContact: '',
          debtorAddress: '',
          debtAmount: null,
          collectedAmount: 0,
          debtDate: '',
          dueDate: '',
          interestRate: 0,
          collectionDifficulty: null,
          debtDescription: '',
          contractTerms: '',
          legalBasis: '',
          collectionStrategy: '',
          riskAssessment: '',
          remarks: '',
          debtStatus: 1
        },
        rules: {
          projectName: [
            { required: true, message: '请输入项目名称', trigger: 'blur' }
          ],
          debtType: [
            { required: true, message: '请选择债权类型', trigger: 'change' }
          ],
          debtId: [
            { required: true, message: '请输入债权ID', trigger: 'blur' }
          ],
          debtorName: [
            { required: true, message: '请输入债务人名称', trigger: 'blur' }
          ],
          debtAmount: [
            { required: true, message: '请输入债权金额', trigger: 'blur' }
          ],
          debtDate: [
            { required: true, message: '请选择形成日期', trigger: 'change' }
          ],
          dueDate: [
            { required: true, message: '请选择到期日期', trigger: 'change' }
          ]
        },
        // 项目选择相关数据
        projectSelectorVisible: false,
        projectListLoading: false,
        projectList: [],
        projectTotal: 0,
        selectedProjects: [],
        selectedProjectName: '', // 用于显示选中的项目名称
        projectQuery: {
          projectName: '',
          pageNumber: 1,
          pageSize: 10
        }
      }
    },
    methods: {
      showEdit(type, data) {
        console.log('DebtEdit showEdit called with:', type, data)
        this.dialogVisible = true
        this.isDetail = type === 'detail'

        if (type === 'add') {
          this.title = '新建债权记录'
          this.resetForm()
        } else if (type === 'edit') {
          this.title = '编辑债权记录'
          this.form = { ...data }
        } else if (type === 'detail') {
          this.title = '债权记录详情'
          this.form = { ...data }
        }

        console.log('Dialog visible set to:', this.dialogVisible)
      },
      
      resetForm() {
        this.form = {
          id: null,
          projectId: null,
          projectName: '',
          debtType: null,
          debtId: '',
          debtorName: '',
          debtorContact: '',
          debtorAddress: '',
          debtAmount: null,
          collectedAmount: 0,
          debtDate: '',
          dueDate: '',
          interestRate: 0,
          collectionDifficulty: null,
          debtDescription: '',
          contractTerms: '',
          legalBasis: '',
          collectionStrategy: '',
          riskAssessment: '',
          remarks: '',
          debtStatus: 1
        }
        // 重置项目选择显示
        this.selectedProjectName = ''
        this.$nextTick(() => {
          this.$refs.form && this.$refs.form.clearValidate()
        })
      },
      
      handleClose() {
        this.dialogVisible = false
        this.resetForm()
      },
      
      async handleSave() {
        try {
          await this.$refs.form.validate()
          
          let response
          if (this.form.id) {
            response = await updateDebtRecord(this.form)
          } else {
            response = await createDebtRecord(this.form)
          }
          
          if (response.code === 1) {
            this.$message.success(this.form.id ? '更新成功' : '创建成功')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          if (error.message) {
            this.$message.error('操作失败：' + error.message)
          }
        }
      },

      // 项目选择相关方法
      showProjectSelector() {
        this.projectSelectorVisible = true
        this.fetchProjectList()
      },

      async fetchProjectList() {
        this.projectListLoading = true
        try {
          const response = await getProjectList(this.projectQuery)
          console.log('项目列表API响应:', response)
          if (response.code === 1) {
            this.projectList = response.data?.list || []
            this.projectTotal = response.data?.total || 0
            console.log('项目列表数据:', this.projectList)
            console.log('项目总数:', this.projectTotal)
          } else {
            this.$message.error('获取项目列表失败: ' + (response.msg || '未知错误'))
          }
        } catch (error) {
          console.error('获取项目列表失败:', error)
          this.$message.error('获取项目列表失败: ' + error.message)
        } finally {
          this.projectListLoading = false
        }
      },

      searchProjects() {
        this.projectQuery.pageNumber = 1
        this.fetchProjectList()
      },

      resetProjectQuery() {
        this.projectQuery = {
          projectName: '',
          pageNumber: 1,
          pageSize: 10
        }
        this.fetchProjectList()
      },

      handleProjectSelectionChange(selection) {
        this.selectedProjects = selection
      },

      handleProjectSizeChange(val) {
        this.projectQuery.pageSize = val
        this.fetchProjectList()
      },

      handleProjectCurrentChange(val) {
        this.projectQuery.pageNumber = val
        this.fetchProjectList()
      },

      confirmProjectSelection() {
        if (this.selectedProjects.length === 0) {
          this.$message.warning('请选择一个项目')
          return
        }
        if (this.selectedProjects.length > 1) {
          this.$message.warning('只能选择一个项目')
          return
        }

        const selectedProject = this.selectedProjects[0]
        this.form.projectId = selectedProject.projectId
        this.selectedProjectName = selectedProject.projectName
        this.projectSelectorVisible = false
        this.selectedProjects = []
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
</style>
