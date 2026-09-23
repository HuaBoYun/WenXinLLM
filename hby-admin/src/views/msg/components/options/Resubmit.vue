<template>
  <div>
    <el-dialog
      @close="close3"
      :title="title"
      :visible="visible"
      :append-to-body="true"
      :close-on-click-modal="false"
    >
      <el-form
        label-width="100px"
        ref="fzforms"
        :model="fzforms"
        :rules="fzRules"
      >
        <el-form-item label="分支选择" prop="branchStrs">
          <el-select
            style="width: 100%"
            v-model="fzforms.branchStrs"
            @change="handlefzChange"
          >
            <el-option
              v-for="item in fzoptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <div v-for="(item, index) in runderList" :key="item.value">
          <el-form-item
            :label="item.label"
            prop="transferStaffName"
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
            />
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer">
        <el-button @click="close3">取消</el-button>
        <el-button type="primary" @click="save3">提交</el-button>
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
        :model="fzform1"
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
                if (item.hasCandidates && (!value || value.length == 0)) {
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
              v-model="fzform1.formData3[index].transferStaffId"
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
  </div>
</template>

<script>
  import { ymWorkCandidates, ymWorkSubmit } from '@/api/contract/manage'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import { debounce } from '@/utils'
  export default {
    name: 'Resubmit',
    components: {
      CandidateUserSelect,
    },
    props: {
      flowId: {
        type: String | Number,
        default: '',
      },
      fromId: {
        type: String | Number,
        default: '',
      },
      fromIdcopy: {
        type: String | Number,
        default: '',
      },
      flowtaskinfoflowid: {
        type: String | Number,
        default: '',
      },
      ymFromId: {
        type: String | Number,
        default: '',
      },
      status: {
        type: String | Number,
        default: '',
      },
      candidateType: {
        type: String | Number,
        default: '',
      },
    },
    data() {
      return {
        title: '分支选择',
        //提交
        visible: false,
        fzform: {
          branchStrs: '',
        },
        fzforms: {
          branchStrs: '',
          formData3: [],
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
          branchStrs: '',
          formData3: [],
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
        localCandidateType: '',

        visible1: false,
        visible2: false,
        formData2: {
          transferStaffName: '',
          transferStaffId: '',
        },
        formData3: [],
        clearType: false,
        typeName: '',

        rules2: {
          transferStaffName: [
            { required: true, message: '请选择候选人', trigger: 'change' },
          ],
        },
      }
    },
    watch: {
      candidateType: {
        immediate: true,
        handler(newVal) {
          this.localCandidateType = newVal
        },
      },
    },
    methods: {
      ymsubmit: debounce(async function (contracttype = '') {
        const res = await ymWorkCandidates({
          flowId: this.flowtaskinfoflowid,
          fromId: this.fromId,
          flowTaskOperatorId: '',
          id: '',
          typeName: contracttype,
        })
        this.localCandidateType = res.data.candidateType
        this.typeName = contracttype
        // res.data.candidateType = 1
        // res.data.candidateType == 2
        // res.data.candidateType == 3
        //混合弹框
        if (res.data && res.data.candidateType == 1) {
          // 选择分支
          let list = []
          this.visible1 = true
          res.data.list.map((item) => {
            list.push({
              ...item,
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
          this.$forceUpdate()
          //候选人
        } else if (res.data.candidateType == 2) {
          this.visible2 = true
          let candidateData = {
            flowId: this.flowtaskinfoflowid,
            fromId: this.fromId,
            nodeId: res.data.list[0].nodeId,
          }
          this.candidateData = candidateData
          this.$forceUpdate()
          //分支
        } else {
          const el = this.$parent.$el
          let loadingInstance = null
          if (el) {
            loadingInstance = this.$loading({
              target: el,
            })
          }
          let params = {
            flowId: this.flowtaskinfoflowid,
            fromId: this.fromId,
            branchStrs: this.fzform.branchStrs.toString(),
            candidateType: res.data.candidateType,
            ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
            status: this.status,
          }
          if (this.typeName != '') {
            params.typeName = this.typeName
          }
          const wordres = await ymWorkSubmit(params)
          if (loadingInstance) loadingInstance.close()
          if (wordres.code == 1) {
            this.$baseMessage('提交成功', 'success', 'vab-hey-message-success')
            // this.$baseMessage(wordres.msg, 'success', 'vab-hey-message-success')
            this.$emit('fetchClose')
          }
        }
      }, 1000),
      handleCandSelect1(index, value) {
        // this.formData3[index].transferStaffId = value
        this.formData2.transferStaffName = value
      },
      handleCandSelect(index, value) {
        // 根据当前激活的弹窗决定更新哪个数据
        if (this.visible1) {
          // 第二个弹窗的逻辑
          this.$set(this.fzform1.formData3[index], 'transferStaffId', value)
          // 选择后主动触发该字段的验证
          this.$nextTick(() => {
            if (this.$refs.fzform1) {
              this.$refs.fzform1.validateField(
                'formData3.' + index + '.transferStaffId'
              )
            }
          })
        } else {
          // 第一个弹窗的原始逻辑
          if (this.formData3 && this.formData3[index]) {
            this.formData3[index].transferStaffId = value
          }
        }
      },
      handlefzChange(e) {
        this.$refs['fzform'].clearValidate()

        let arr = []
        if (Array.isArray(e)) {
          this.fzoptions.forEach((res) => {
            e.forEach((res1) => {
              if (res.value == res1) {
                arr.push(res)
              }
            })
          })
        } else if (e) {
          const selectedOption = this.fzoptions.find(
            (option) => option.value == e
          )
          if (selectedOption) {
            arr.push(selectedOption)
          }
        }

        this.runderList = arr
        // 初始化formData3数组
        this.$set(
          this.fzforms,
          'formData3',
          arr.map(() => {
            return { transferStaffName: '', transferStaffId: [] }
          })
        )
      },

      async save3() {
        if (!this.fzforms.branchStrs) {
          this.$message.warning('请选择分支')
          return
        }
        let arr = []

        if (this.formData3 && this.formData3.length > 0) {
          this.formData3.map((res) => {
            let str = []
            if (res.transferStaffId && res.transferStaffId.length) {
              res.transferStaffId.map((item) => {
                str.push(item.id)
              })
            }
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
          branchStrs: this.fzforms.branchStrs.toString(),
          candidateList: candidateList || '',
          ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
          candidateType: this.localCandidateType,
          status: this.status,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close3()
          this.$emit('fetchClose')
        }
        // } else {
        //
        //   return false
        // }
        // })
      },
      close3() {
        this.visible = false
        this.resetINfo()
      },
      resetINfo() {
        this.runderList = []
        this.formData = {
          value: [],
        }
        this.formData2 = {
          transferStaffName: '',
          transferStaffId: '',
        }
        this.fzform1 = {
          branchStrs: '',
          formData3: [],
        }
        this.clearType = true
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
        // 初始化formData3数组
        this.$set(
          this.fzform1,
          'formData3',
          arr.map(() => {
            return { transferStaffName: '', transferStaffId: [] }
          })
        )

        // 重置表单验证
        this.$nextTick(() => {
          if (this.$refs.fzform1) {
            this.$refs.fzform1.clearValidate()
          }
        })
      },
      async save1() {
        this.$refs['fzform1'].validate(async (valid) => {
          if (valid) {
            let branchStrs = this.fzform1.branchStrs.toString()

            let arr = []
            if (this.fzform1.formData3 && this.fzform1.formData3.length > 0) {
              this.fzform1.formData3.forEach((res) => {
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

            const { data, code } = await ymWorkSubmit({
              flowId: this.flowtaskinfoflowid,
              fromId: this.fromId,
              branchStrs: branchStrs,
              candidateType: this.localCandidateType,
              candidateList: candidateList || '',
              ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
              status: this.status,
            })
            if (code == 1) {
              this.$message.success('提交成功')
              this.close1()
              this.$emit('fetchClose')
            }
          }
        })
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
          candidateType: this.localCandidateType,
          ymFromId: this.fromIdcopy == -1 ? '' : this.ymFromId,
          status: this.status,
        })
        if (code == 1) {
          this.$message.success('提交成功')
          this.close2()
          this.$emit('fetchClose')
        }
      },
    },
  }
</script>

<style></style>
