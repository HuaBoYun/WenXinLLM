<!--
 * @Author: 康某 dev@example.com
 * @Date: 2022-10-23 16:23:51
 * @LastEditors: raincoat dev@example.com
 * @LastEditTime: 2024-01-20 22:16:03
 * @FilePath: \hb-admin\src\views\contract\legal\components\zcbqAdd.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    append-to-body
  >
    <el-form :model="form" ref="form" :rules="rules" label-width="140px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="纠纷名称" prop="disputename">
            <el-input
              v-model="form.disputename"
              placeholder="请输入纠纷名称"
              disabled
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行案号" prop="execuno">
            <el-input
              v-model="form.execuno"
              placeholder="请输入执行案号"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行法院" prop="execucourt">
            <el-input
              v-model="form.execucourt"
              clearable
              placeholder="请选择执行法院"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行方式" prop="executype">
            <el-select v-model="form.executype" style="width: 100%">
              <el-option value="账户冻结">账户冻结</el-option>
              <el-option value="查封">查封</el-option>
              <el-option value="扣押">扣押</el-option>
              <el-option value="拍卖">拍卖</el-option>
              <el-option value="变卖">变卖</el-option>
              <el-option value="协助执行">协助执行</el-option>
              <el-option value="其它">其它</el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行总金额（万元）" prop="execuamount">
            <el-input v-model="form.execuamount" type="number"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="已执行总金额（万元）" prop="execuedamount">
            <el-input v-model="form.execuedamount" type="number"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="未执行总金额（万元）" prop="noexecuamount">
            <el-input v-model="form.noexecuamount" type="number"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
      <el-button type="primary" @click="save">保 存</el-button>
      <el-button
        v-if="(form.status == 2 || form.status == 3) && jurisdictionCode == 1"
        @click="ymsubmit"
        type="primary"
      >
        提交
      </el-button>
    </template>
    <!-- 纠纷登记 -->
    <xzjf-options ref="xzjf" @selected="handleSsjd" />

    <!-- 提交 -->
    <el-dialog
      @close="currentClose"
      title="选择分支"
      :visible="visible"
      :append-to-body="true"
      :close-on-click-modal="false"
    >
      <el-form
        label-width="100px"
        ref="fzforms"
        :modal="fzforms"
        :rules="fzRules"
      >
        <el-form-item label="分支选择" prop="branchStrs">
          <el-select
            style="width: 100%"
            v-model="fzforms.branchStrs"
            @change="handlefzChange"
            multiple
          >
            <el-option
              v-for="item in fzoptions"
              :label="item.nodeName"
              :value="item.nodeId"
              :key="item.nodeId"
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
        <el-button @click="currentClose">取消</el-button>
        <el-button type="primary" @click="save4">提交</el-button>
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
        :modal="fzform1"
        label-width="100px"
        ref="fzform1"
        :rules="fzRules1"
      >
        <el-form-item label="分支选择" prop="branchStrs">
          <el-select
            style="width: 100%"
            v-model="fzform1.branchStrs"
            @change="selectValue"
            multiple
          >
            <el-option
              v-for="item in fzoptions"
              :key="item.nodeId"
              :label="item.nodeName"
              :value="item.nodeId"
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
  </el-dialog>
</template>

<script>
  import xzjfOptions from './options/xzjf.vue'
  import {
    legalExecumgrDetail,
    legalExecumgrSave,
    getDetailInfo,
  } from '@/api/fwgl/legal'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  export default {
    components: { xzjfOptions, CandidateUserSelect },
    props: ['info'],
    data() {
      return {
        form: {
          id: '',
          disputename: '',
          execuno: '',
          execucourt: '',
          executype: '',
          execuamount: '',
          execuedamount: '',
          noexecuamount: '',
          status: '',
        },
        rules: {},
        dialogFormVisible: false,
        title: '',
        //提交
        visible: false,
        fzforms: {
          branchStrs: [],
        },
        fzRules: {
          branchStrs: [
            {
              required: true,
              message: '请选择分支',
              trigger: 'blur',
            },
          ],
        },
        fzform1: {
          branchStrs: [],
        },
        fzRules1: {
          branchStrs: [
            {
              required: true,
              message: '请选择分支',
              trigger: 'blur',
            },
          ],
        },
        fzoptions: [],
        runderList: [],
        candidateData: {},
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        visible1: false,
        visible2: false,
        formData2: {
          transferStaffName: '',
          transferStaffId: '',
        },
        status: 0,
        jurisdictionCode: 0,
        clearType: false,
      }
    },
    methods: {
      async show(data, title, row) {
        this.form.disputename = this.info.disputename
        this.form.disputeid = this.info.disputeid
        if (row) {
          let res = await getDetailInfo({ id: row.id })
          this.form.disputename = res.data.disputename
          this.form.execuno = res.data.execuno
          this.form.execucourt = res.data.execucourt
          this.form.executype = res.data.executype
          this.form.execuamount = res.data.execuamount
          this.form.execuedamount = res.data.execuedamount
          this.form.noexecuamount = res.data.noexecuamount
          this.form.id = res.data.id
          this.form.status = res.data.status

          if (res.data.status == 2 || res.data.status == 3) {
            const res2 = await getFlowTaskInfo({
              tableId: 29,
              formId: row.id,
            })
            this.jurisdictionCode = res2.data.isFlowInfo
            if (res2.data.isFlowInfo) {
              this.flowtaskinfoflowid = res2.data.flowId
              this.fromId = row.id
              this.ymFromId = res2.data.id

              const res3 = await getFaqiInfo({
                id: res2.data.id,
                flowId: res2.data.flowId,
              })
              if (res3.code == 1) {
                this.status = res3.data.dataJson.flowTaskInfo.status
              }
            }
          }
        }

        this.title = title
        this.dialogFormVisible = true
        this.form.litigationid = data.litigationid
        this.form.arbitraid = data.arbitraid
      },
      handleSsjd(val) {
        this.form.disputeid = val.disputeid
        this.$set(this.form, 'disputeItem', val.disputeitem)
      },
      close() {
        this.dialogFormVisible = false
        this.clearType = true
        this.$emit('fetch')
        this.form = {
          id: '',
          disputename: '',
          execuno: '',
          execucourt: '',
          executype: '',
          execuamount: '',
          execuedamount: '',
          noexecuamount: '',
        }
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        const res = await legalExecumgrSave(this.form)
        // 返回date...
        if (res && res.date) {
          this.$message({
            type: 'success',
            message: '保存成功！',
          })
          this.$emit('selected', res.date)
          this.close()
        } else {
          this.$message({
            type: 'error',
            message: '保存失败！',
          })
        }
      },
      /**
       * @description: 引迈流程提交
       * @return {*}
       */      
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const res = await ymWorkCandidates({
              flowId: this.flowtaskinfoflowid,
              fromId: this.fromId,
              flowTaskOperatorId: '',
              id: '',
            })
            this.candidateType = res.data.candidateType
            if (res.data.candidateType == 1) {
              this.fzoptions = res.data.list
              this.visible = true
              let list = []
              res.data.list.map((item) => {
                list.push({
                  value: item.nodeId,
                  label: item.nodeName,
                  hasCandidates: item.hasCandidates,
                })
              })
              this.options = list
              //保存请求人员列表的信息
              let candidateData = {
                // tableId: tableId,
                fromId: this.fromId,
              }
              this.candidateData = candidateData
            } else if (res.data.candidateType == 2) {
              let candidateData = {
                // tableId: tableId,
                fromId: this.fromId,
                nodeId: res.data.list[0].nodeId,
              }
              this.candidateData = candidateData
              this.visible2 = true
            } else {
              const wordres = await ymWorkSubmit({
                flowId: this.flowtaskinfoflowid,
                fromId: this.fromId,
                branchStrs: this.fzforms.branchStrs
                  ? this.fzforms.branchStrs.join(',')
                  : '',
                candidateType: res.data.candidateType,
                ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
                status: this.status,
              })
              if (wordres.code === 1) {
                this.$message.success(wordres.msg)
                this.close()
                this.visible = false
              }
            }
          }
        })
      },
      currentClose() {
        this.visible = false
        this.form = {}
      },
      handlefzChange(e) {
        this.$refs['fzform'].clearValidate()
      },
      handleCandSelect1(index, value) {
        this.formData2.transferStaffName = value
      },
      handleCandSelect(index, value) {
        // this.$set(this.formData3[index], 'transferStaffId', value)
        this.formData3[index].transferStaffId = value
      },
      async save4() {
        if (this.fzforms.branchStrs.length == 0) {
          this.$message.warning('请选择分支')
          return
        }
        let arr = []
        if (this.formData3.length > 0) {
          this.formData3.map((res) => {
            let str = []
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
          // tableId: this.tableId,
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          branchStrs: this.fzforms.branchStrs
            ? this.fzforms.branchStrs.join(',')
            : '',
          candidateList: candidateList || '',
          ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
          candidateType: this.candidateType,
          status: this.status,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close4()
          this.close()
        }
        // } else {
        //
        //   return false
        // }
        // })
      },
      selectValue(e) {
        let arr = []
        this.fzoptions.forEach((res) => {
          if (res.nodeId == e.split('~')[0]) {
            arr.push(res)
          }
        })
        this.runderList = arr
        this.formData3 = arr.map(() => {
          return { transferStaffName: '', transferStaffId: '' }
        })
      },

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
      async save1() {
        let branchStrs = ''
        this.fzform1.branchStrs.map((item) => {
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
          status: this.status,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close()
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
      async save2() {
        if (!this.formData2.transferStaffName) {
          this.$message.error('请选择候选人')
          return
        }

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
          status: this.status,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close2()
          this.close()
        }
      },
    },
  }
</script>

<style lang="less" scoped></style>
