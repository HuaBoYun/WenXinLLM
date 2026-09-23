<template>
  <el-row :gutter="15">
    <el-form ref="form" label-width="100px" :model="formData">
      <el-col :span="12">
        <el-form-item label="合同名称" prop="contractname">
          <el-input
            v-model="formData.contractname"
            :style="{ width: '80%' }"
            disabled
          />
          <el-button
            :style="{ marginLeft: '10px' }"
            type="primary"
            @click="$refs.table.show()"
            size="mini"
          >
            选择
          </el-button>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="合同编号" prop="contractno">
          <el-input v-model="formData.contractno" disabled />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="借阅日期" prop="lenddate">
          <el-date-picker
            v-model="formData.lenddate"
            placeholder=""
            disabled
            :style="{ width: '100%' }"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="归还日期" prop="returndate">
          <el-date-picker
            v-model="formData.returndate"
            clearable
            placeholder="请输入归还日期"
            :style="{ width: '100%' }"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="借阅事由" prop="memo">
          <el-input
            v-model="formData.memo"
            :autosize="{ minRows: 4, maxRows: 4 }"
            placeholder="请输入借阅事由"
            :style="{ width: '100%' }"
            type="textarea"
          />
        </el-form-item>
      </el-col>
      <div style="text-align: right; margin-top: 10px; margin-right: 15px">
        <!-- <el-button @click="close">取 消</el-button> -->
        <el-button type="primary" @click="save" :disabled="changeSaveBtn">
          确 定
        </el-button>
        <el-button type="primary" @click="ymsubmit">提交</el-button>
      </div>
    </el-form>

    <el-dialog
      @close="currentClose"
      title="选择分支"
      :visible="visible"
      :append-to-body="true"
      :close-on-click-modal="false"
    >
      <el-form
        :modal="fzform"
        label-width="100px"
        ref="fzform"
        :rules="fzRules"
      >
        <el-form-item label="分支选择">
          <el-select
            style="width: 100%"
            v-model="fzform.branchStrs"
            @change="handlefzChange"
            multiple
          >
            <el-option
              v-for="item in fzoptions"
              :label="item.nodeName"
              :value="item.nodeId"
              :key="item.key"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="currentClose">取消</el-button>
        <el-button type="primary" @click="ymWorkBeforSubmit">提交</el-button>
      </div>
    </el-dialog>
    <el-dialog
      @close="close1"
      title="选择分支"
      :visible="visible1"
      :append-to-body="true"
      :close-on-click-modal="false"
      v-if="visible1"
    >
      <el-form
        :modal="fzform"
        label-width="100px"
        ref="fzform1"
        :rules="fzRules"
      >
        <el-form-item label="分支选择">
          <el-select
            style="width: 100%"
            v-model="fzform.branchStrs"
            @change="selectValue"
            multiple
          >
            <el-option
              v-for="item in fzoptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <div v-for="(item, index) in this.runderList" :key="item.value">
          <el-form-item
            :label="item.label"
            prop="transferStaffName"
            v-if="item.hasCandidates"
          >
            <!-- <el-input
              disabled
              placeholder="请选择候选人"
              v-model="formData3[index].transferStaffName"
              style="width: 79%; margin-right: 8px"
            ></el-input>
            <el-button type="primary" @click="handleSelectNew(item, index)">
              请选择
            </el-button> -->
            <CandidateUserSelect
              :clearType="clearType"
              @selected="handleCandSelect"
              :index="index"
              :nodeId="item.nodeId"
              :candidateData="candidateData"
              multiple
              placeholder="请选择候选人"
            />
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer">
        <el-button @click="close1">取消</el-button>
        <el-button type="primary" @click="save1">提交</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="选择候选人"
      :visible.sync="visible2"
      :close-on-click-modal="false"
      width="40%"
      :modal="false"
      @close="close2"
      v-if="visible2"
    >
      <el-form
        :model="formData2"
        :rules="rules2"
        ref="ruleForm2"
        label-width="80px"
      >
        <el-form-item label="候选人" prop="transferStaffName">
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
        <el-button @click="close2">取 消</el-button>
        <el-button type="primary" @click="save2">确 定</el-button>
      </span>
    </el-dialog>
    <ContractTable ref="table" @selected="handTable" />
    <CandidateListNew ref="candidateListNew" @selected="handSelectedNew1" />
    <CandidateList ref="candidateList" @selected="handSelected" />
  </el-row>
</template>

<script>
  import {
    saveBorrowContract,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import CandidateList from '@/components/CandidateList'
  import CandidateListNew from '@/views/contract/contractManage/components/CandidateList'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import ContractTable from '@/views/contract/contractManage/components/ContractTable2.vue'

  export default {
    components: {
      CandidateListNew,
      CandidateList,
      CandidateUserSelect,
      ContractTable,
    },
    name: 'BorrowEdit',
    data() {
      return {
        fromIdcopy: 0,
        visible: false,
        fzform: {
          branchStrs: [],
        },
        fzRules: {
          branchStrs: [
            {
              required: true,
              message: '请选择分支',
              trigger: 'change',
            },
          ],
        },
        formData: {
          contractId: undefined,
          contractno: undefined,
          contractname: undefined,
          lenddate: undefined,
          returndate: undefined,
          memo: undefined,
        },
        formData2: {
          transferStaffName: '',
          transferStaffId: '',
        },
        visible1: false,
        visible2: false,
        formData3: [],
        runderList: [],
        candidateType: '',
        title: '',
        fzoptions: [],
        candidateData: {},
        disabled: false,
        dialogFormVisible: false,
        changeSaveBtn: false,
        fromId: null,
        fromIdcopy: null,
        flowtaskinfoflowid: null,
        ymFromId: null,
        clearType: false,
      }
    },
    mounted() {
      this.changeSaveBtn = false
    },
    created() {
      this.$bus.$off('changeSaveBtn').$on('changeSaveBtn', () => {
        this.changeSaveBtn = true
      })
    },
    methods: {
      handTable(data) {
        this.formData.contractId = data.contractid
        this.formData.contractno = data.contractno
        this.formData.contractname = data.contractname
      },
      //保存弹框的回调
      handleCandSelect1(index, value) {
        this.formData2.transferStaffName = value
      },
      //回调方法
      handleCandSelect(index, value) {
        // this.$set(this.formData3[index], 'transferStaffId', value)
        this.formData3[index].transferStaffId = value
      },
      showDetail(row, type, fromId, flowtaskinfoflowid, ymFromId) {
        this.title = '借阅信息'
        Object.keys(this.formData).forEach((key) => {
          this.formData[key] = row[key]
        })
        this.formData.contractId = row.contractid

        if (fromId) {
          this.fromId = fromId
          this.fromIdcopy = fromId // fromId为-1时，拷贝一份
        }
        if (flowtaskinfoflowid) {
          this.flowtaskinfoflowid = flowtaskinfoflowid
        }
        if (ymFromId) {
          this.ymFromId = ymFromId
        }
      },
      close() {
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData

        this.$bus.$emit('updateMsg', 0)
      },
      handlefzChange(e) {
        this.$refs['fzform'].clearValidate()
      },
      handleSelectNew(row, index) {
        this.candidateData.nodeId = row.value
        this.$refs['candidateListNew'].show(this.candidateData, index)
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
        this.$set(this.formData3[index], 'transferStaffName', name)
        this.$set(this.formData3[index], 'transferStaffId', id)
      },
      //唤起弹框
      handleSelect() {
        this.$refs['candidateList'].show(this.candidateData)
      },
      //回调方法
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
      selectValue(e) {
        let arr = []
        this.fzoptions.forEach((res) => {
          e.forEach((res1) => {
            if (res.value == res1) {
              arr.push(res)
            }
          })
        })
        this.runderList = arr
        this.formData3 = arr.map(() => {
          return { transferStaffName: '', transferStaffId: '' }
        })
      },
      //保存
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg, code, lendId } = await saveBorrowContract({
              ...this.formData,
              lendid: this.fromId,
            })
            if (code == '1') {
              //
              // this.$refs['process'].show({ lendId }, 13)
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            } else {
              this.$baseMessage(msg, 'error', 'vab-hey-message-error')
            }
            // this.$emit('fetch-data')
            // this.close()
          }
        })
      },
      async save1() {
        let branchStrs = ''
        this.fzform.branchStrs.map((item) => {
          branchStrs = branchStrs + item + ','
        })
        branchStrs = branchStrs.substring(0, branchStrs.length - 1)
        let arr = []
        if (this.formData3.length > 0) {
          this.formData3 &&
            this.formData3.map((res) => {
              let str = []
              res.transferStaffId &&
                res.transferStaffId.map((item) => {
                  str.push(item.id)
                })
              arr.push(str)
            })
        }

        let list = []
        arr.map((item) => {
          let str = item.join(',')
          if (str) {
            list.push(str)
          }
        })
        let candidateList = list.join('~')
        const { data, code } = await ymWorkSubmit({
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          branchStrs,
          candidateType: this.candidateType,
          candidateList: candidateList || '',
          ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.visible1 = false
        }
      },
      close1() {
        this.visible1 = false
        this.resetINfo()
      },
      close2() {
        this.visible2 = false
        this.resetINfo()
      },
      //重置
      resetINfo() {
        this.flowId = ''
        this.fromId = ''
        this.runderList = []
        this.formData = {
          value: [],
        }
        this.formData2 = {
          transferStaffName: '',
          transferStaffId: '',
        }
      },
      currentClose() {
        this.visible = false
        this.form = {}
      },
      //流程的提交
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const res = await ymWorkCandidates({
              flowId: this.flowtaskinfoflowid,
              fromId: this.fromId,
              flowTaskOperatorId: '',
              id: '',
            })
            // res.data.candidateType = 1
            // res.data.candidateType = 2;
            // res.data.candidateType = 3;
            this.candidateType = res.data.candidateType
            //混合弹框
            if (res.data && res.data.candidateType == 1) {
              // 选择分支
              let list = []
              this.visible1 = true
              res.data.list.map((item) => {
                list.push({
                  value: item.nodeId,
                  label: item.nodeName,
                  hasCandidates: item.hasCandidates,
                })
              })
              this.fzoptions = list
              //保存请求人员列表的信息
              let candidateData = {
                flowId: this.flowtaskinfoflowid,
                fromId: this.fromId,
              }
              this.candidateData = candidateData
              //候选人
            } else if (res.data.candidateType == 2) {
              this.visible2 = true
              let candidateData = {
                flowId: this.flowtaskinfoflowid,
                fromId: this.fromId,
                nodeId: res.data.list[0].nodeId,
              }
              this.candidateData = candidateData
              //分支
            } else {
              const wordres = await ymWorkSubmit({
                flowId: this.flowtaskinfoflowid,
                fromId: this.fromId,
                branchStrs: this.fzform.branchStrs.join(','),
                candidateType: res.data.candidateType,
                ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
              })
              this.$baseMessage(
                wordres.msg,
                'success',
                'vab-hey-message-success'
              )
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
      //流程提交的前置
      async ymWorkBeforSubmit() {
        if (!this.fzform.branchStrs || !this.fzform.branchStrs.length)
          return this.$message.warning('请选择分支')
        // 先掉编辑接口
        // const arrAttid = this.localList.map((item) => item.attid);
        // if (arrAttid && arrAttid.length) {
        //   this.formData.attids = arrAttid.join(",");
        // }
        this.$delete(this.formData, 'flowid')
        const { msg, code, data } = await saveContract(this.formData)
        if (code == 1) {
          const { code } = await ymWorkSubmit({
            flowId: this.flowtaskinfoflowid,
            fromId: this.fromId,
            branchStrs: '',
            candidateType: this.candidateType,
            ymFromId: this.ymFromId,
          })
          if (code == 1) {
            this.$message.success('成功')
            this.visible = false
            this.close()
          }
        }
      },
      async save2() {
        let list = []
        this.formData2.transferStaffName.map((item) => {
          list.push(item.id)
        })
        const { data, code } = await ymWorkSubmit({
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          candidateList: list.join(','),
          nodeCode: this.candidateData.nodeId,
          candidateType: this.candidateType,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close2()
        }
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
