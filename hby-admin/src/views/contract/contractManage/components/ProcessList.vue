<!--
 * @Author: 康某 dev@example.com
 * @Date: 2022-09-19 20:52:58
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-09-19 20:53:31
 * @FilePath: \hb-admin\src\views\contract\contractManage\components\ProcessList.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div>
    <!-- <el-table
      :data="tableData"
      highlight-current-row
      @current-change="handleCurrentChange"
    >
      <el-table-column type="index" width="50px" label="序号">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="流程名称"
        prop="ymWorkName"
      ></el-table-column>
    </el-table>
    <span slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </span> -->
    <el-dialog
      title="选择候选人"
      :visible.sync="dialogVisible3"
      :close-on-click-modal="false"
      width="40%"
      :modal="false"
      @close="close3"
      v-if="dialogVisible3"
    >
      <el-form
        :model="formData2"
        :rules="rules2"
        ref="ruleForm2"
        label-width="80px"
      >
        <el-form-item
          :label="candidateData && candidateData.nodeName"
          prop="transferStaffName"
        >
          <!-- <el-input
            disabled
            placeholder="请选择候选人"
            v-model="formData2.transferStaffName"
            style="width: 79%; margin-right: 8px"
          ></el-input>
          <el-button type="primary" @click="handleSelect">请选择</el-button> -->
          <CandidateUserSelect
            :clearType="clearType"
            @selected="handleCandSelect1"
            :index="0"
            :nodeId="candidateData.nodeId"
            :candidateData="candidateData"
            multiple
            placeholder="请选择候选人"
          />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close3">取 消</el-button>
        <el-button type="primary" @click="save3" :loading="buttonLoading">
          确 定
        </el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="提交审核"
      :modal="false"
      :visible.sync="dialogVisible2"
      :close-on-click-modal="false"
      width="40%"
      @close="close2"
      v-if="dialogVisible2"
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
            style="width: 100%"
            @change="selectValue"
            multiple
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
        <el-button type="primary" @click="save2" :loading="buttonLoading">
          确 定
        </el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="提交审核"
      :modal="false"
      :visible.sync="dialogVisible4"
      :close-on-click-modal="false"
      width="40%"
      @close="close4"
      v-if="dialogVisible4"
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
            style="width: 100%"
            @change="selectValue"
            multiple
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <div v-for="(item, index) in runderList" :key="item.value">
          <el-form-item
            :label="item.label"
            :prop="'formData3.' + index + '.transferStaffId'"
            :rules="{
              required: item.hasCandidates,
              message: '请选择候选人',
              trigger: 'change',
              validator: (rule, value, callback) => {
                if (item.hasCandidates && (!value || value.length === 0)) {
                  callback('请选择候选人')
                } else {
                  callback()
                }
              },
            }"
            v-if="item.hasCandidates"
          >
            <CandidateUserSelect
              :clearType="clearType"
              @selected="handleCandSelect"
              :index="index"
              :nodeId="item.nodeId"
              :candidateData="candidateData"
              multiple
              placeholder="请选择候选人"
              v-model="formData.formData3[index].transferStaffId"
            />
          </el-form-item>
        </div>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="close4">取 消</el-button>
        <el-button type="primary" @click="save4" :loading="buttonLoading">
          确 定
        </el-button>
      </span>
    </el-dialog>
    <CandidateList ref="candidateList" @selected="handSelected" />
    <CandidateListNew ref="candidateListNew" @selected="handSelectedNew1" />
  </div>
</template>

<script>
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import {
    candidates,
    getAllFlowList,
    submitByYmWork,
  } from '@/api/setting/system'
  import { submitYYCreateFile } from '@/api/contract/manage'
  import CandidateList from '@/components/CandidateList'
  import CandidateListNew from './CandidateList'

  export default {
    props: {
      modal: {
        type: Boolean,
        default: true,
      },
    },
    components: {
      CandidateList,
      CandidateListNew,
      CandidateUserSelect,
    },
    data() {
      return {
        buttonLoading: false,
        dialogVisible: false,
        dialogVisible2: false,
        dialogVisible3: false,
        dialogVisible4: false,
        current: null,
        tableData: [],
        curretnRow: null,
        options: [],
        type: '',
        formData: {
          value: [],
          formData3: [],
        },
        rules: {
          value: [{ required: true, message: '请选择分支', trigger: 'change' }],
        },
        formData2: {
          transferStaffName: '',
          transferStaffId: '',
        },
        formData3: [],
        rules2: {
          transferStaffName: [
            { required: true, message: '请选择候选人', trigger: 'change' },
          ],
        },
        candidateData: {},
        candidateType: '',
        runderList: [],
        fromId: '',
        tableId: '',
        clearType: false,
        nodeId: '',
      }
    },
    methods: {
      handleCandSelect1(index, value) {
        this.formData2.transferStaffName = value
        this.formData3[index].transferStaffId = value
      },
      handleCandSelect(index, value) {
        this.$set(this.formData.formData3[index], 'transferStaffId', value)
        // 选择后主动触发该字段的验证
        this.$nextTick(() => {
          this.$refs.ruleForm.validateField(
            'formData3.' + index + '.transferStaffId'
          )
        })
      },
      handleSelect() {
        // console.log('handleSelect', this.candidateData)
        this.$refs['candidateList'].show(this.candidateData)
      },
      handleSelectNew(row, index) {
        console.log('index', index)
        this.candidateData.nodeId = row.value
        // console.log('handleSelect', this.candidateData)
        this.$refs['candidateListNew'].show(this.candidateData, index)
      },
      handSelected(data) {
        console.log('handSelected', data)
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
      handSelectedNew1(data, index) {
        let name = '',
          id = ''
        data.map((item) => {
          name += item.fullName + ','
          id += item.id + ','
        })
        name = name.substring(0, name.length - 1)
        id = id.substring(0, id.length - 1)
        this.$set(this.formData.formData3[index], 'transferStaffName', name)
        this.$set(this.formData.formData3[index], 'transferStaffId', id)

        // this.formData2.transferStaffName = name
        // this.formData2.transferStaffId = id
      },

      async save(tableId, fromId, htType = '') {
        try {
          this.tableId = tableId
          this.fromId = fromId
          this.htType = htType

          this.dialogVisible = true
          let param = {
            tableId: tableId,
            fromId: fromId,
          }

          const { data, code } = await candidates(param)
          this.candidateType = data.candidateType

          if (data.candidateType == 2) {
            let candidateData = {
              tableId: tableId,
              fromId: fromId,
              nodeId: data.list[0].nodeId,
              nodeName: data.list[0].nodeName,
            }
            this.candidateData = candidateData
            this.dialogVisible3 = true
            // return Promise.resolve()
          } else if (data.candidateType == 1) {
            this.dialogVisible4 = true
            let list = []
            data.list.map((item) => {
              list.push({
                ...item,
                value: item.nodeId,
                label: item.nodeName,
                hasCandidates: item.hasCandidates,
              })
            })
            this.options = list
            //保存请求人员列表的信息
            let candidateData = {
              tableId: tableId,
              fromId: fromId,
            }
            this.candidateData = candidateData
            // return Promise.resolve()
          } else {
            let params = {
              tableId: tableId,
              fromId: fromId,
              candidateType: data.candidateType,
            }
            if (this.htType != '') {
              params.typeName = this.htType
            }
            await submitByYmWork(params)
            this.$message.success('提交成功')
            this.$emit('fetchData')
            this.close()
            return Promise.resolve()
          }
        } catch (error) {
          this.loading = false
          return Promise.reject(error)
        }
        // if (code == 1) {
        //   this.$message.success('提交成功')
        // }
      },
      async save2() {
        this.buttonLoading = true
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let branchStrs = this.formData.value
            if (branchStrs) {
              branchStrs = branchStrs.join(',')
            }
            const { data, code } = await submitByYmWork({
              tableId: this.tableId,
              fromId: this.fromId,
              branchStrs,
              candidateType: this.candidateType,
            })
            if (code == 1) {
              this.buttonLoading = false
              this.$message.success('提交成功')
              this.close2()
              this.close()
            }
          } else {
            this.buttonLoading = false
            console.log('error submit!!')
            return false
          }
        })
      },
      async save4() {
        this.buttonLoading = true
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            let branchStrs = this.formData.value
            if (branchStrs) {
              branchStrs = branchStrs.join(',')
            }
            let arr = []
            if (this.formData.formData3.length > 0) {
              this.formData.formData3.forEach((res) => {
                let str = []
                if (res.transferStaffId && res.transferStaffId.length) {
                  res.transferStaffId.forEach((item) => {
                    str.push(item.id)
                  })
                }
                arr.push(str)
              })
            }

            let list = []
            arr.forEach((item) => {
              let str = item.join(',')
              if (str) {
                list.push(str)
              }
            })
            let candidateList = list.join('~')

            const { data, code } = await submitByYmWork({
              tableId: this.tableId,
              fromId: this.fromId,
              branchStrs,
              candidateList: candidateList || '',
              candidateType: this.candidateType,
            })
            if (code == 1) {
              this.buttonLoading = false
              this.$message.success('提交成功')
              this.close4()
              this.close()
            }
          } else {
            this.buttonLoading = false
            console.log('error submit!!')
            return false
          }
        })
      },

      save3() {
        this.buttonLoading = true
        this.$refs['ruleForm2'].validate(async (valid) => {
          if (valid) {
            console.log(this.formData2.transferStaffName)
            let list = []
            this.formData2.transferStaffName.map((item) => {
              list.push(item.id)
            })
            // console.log(list.join(','))
            // return
            const { data, code } = await submitByYmWork({
              tableId: this.tableId,
              fromId: this.fromId,
              candidateList: list.join(','),
              nodeCode: this.candidateData.nodeId,
              candidateType: this.candidateType,
            })
            if (code == 1) {
              this.buttonLoading = false
              this.$message.success('提交成功')
              this.close3()
              this.close()
            }
          } else {
            this.buttonLoading = false
            console.log('error submit!!')
            return false
          }
        })
      },
      async show(e, type) {
        //type为类型  2相对方维护 3黑名单 4合同范本  5合同订立  6合同用印 7合同变更  13合同借阅
        // this.dialogVisible = true
        this.curretnRow = e
        this.type = type
        const { data } = await getAllFlowList({
          tablId: type,
        })
        this.tableData = data.list
      },
      handleCurrentChange(e) {
        this.current = e
      },
      handleSelectUser3() {},

      close() {
        this.current = null
        this.curretnRow = null
        this.tableData = []
        this.dialogVisible = false
        this.clearType = true
        this.$bus.$emit('approvalType', 1)
        this.$bus.$emit('updateMsg', 0)
        this.loading = false
      },
      close2() {
        this.dialogVisible2 = false
        this.resetINfo()
        this.loading = false
      },
      close3() {
        this.dialogVisible3 = false
        this.resetINfo()
        this.loading = false
      },
      close4() {
        this.dialogVisible4 = false
        this.resetINfo()
        this.loading = false
      },
      resetINfo() {
        this.tableId = ''
        this.fromId = ''
        this.runderList = []
        this.formData = {
          value: [],
          formData3: [],
        }
        this.formData2 = {
          transferStaffName: '',
          transferStaffId: '',
        }
      },

      selectValue(e) {
        let arr = []
        this.options.forEach((res) => {
          e.forEach((res1) => {
            if (res.value == res1) {
              arr.push(res)
            }
          })
        })
        this.runderList = arr
        // 初始化formData3数组
        this.$set(
          this.formData,
          'formData3',
          arr.map(() => {
            return { transferStaffName: '', transferStaffId: [] }
          })
        )

        // 重置表单验证
        this.$nextTick(() => {
          if (this.$refs.ruleForm) {
            this.$refs.ruleForm.clearValidate()
          }
        })
      },
    },
  }
</script>

<style lang="scss" scoped></style>
