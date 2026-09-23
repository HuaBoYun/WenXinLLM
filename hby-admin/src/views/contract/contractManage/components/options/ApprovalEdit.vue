<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <!-- <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save(2)">通 过</el-button>
        <el-button type="danger" @click="save(3)">驳 回</el-button>
      </vab-query-form-right-panel>
    </vab-query-form> -->
    <el-row :gutter="15">
      <el-form
        ref="form"
        label-width="150px"
        :model="formData"
        :rules="rules"
        disabled
      >
        <el-col :span="12">
          <el-form-item label="预计开始时间" prop="planstartdate">
            <el-date-picker
              v-model="node.planstartdate"
              clearable
              :picker-options="pickerOptions"
              placeholder=""
              readonly
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预计结束时间" prop="planenddate">
            <el-date-picker
              v-model="node.planenddate"
              clearable
              :picker-options="pickerOptions"
              placeholder=""
              readonly
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收款比例" prop="nodepost">
            <el-input
              v-model="node.nodepost"
              clearable
              placeholder=""
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="收款金额" prop="nodemoney">
            <el-input
              v-model="node.nodemoney"
              clearable
              placeholder=""
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预计收款时间" prop="nodeplanpaydate">
            <el-date-picker
              v-model="node.nodeplanpaydate"
              clearable
              :picker-options="pickerOptions"
              placeholder=""
              readonly
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="履行内容" prop="nodecontent">
            <el-input
              v-model="node.nodecontent"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder=""
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>填写履行信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开始时间" prop="tcsstartdate">
            <el-date-picker
              v-model="node.tcsstartdate"
              clearable
              :picker-options="pickerOptions"
              placeholder=""
              readonly
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束时间" prop="tcsenddate">
            <el-date-picker
              v-model="node.tcsenddate"
              clearable
              :picker-options="pickerOptions"
              placeholder=""
              readonly
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="履行情况" prop="dispatchdept">
            <el-input
              v-model="node.dispatchdept"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder=""
              readonly
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="验收情况" prop="tcsnodecontent">
            <el-input
              v-model="node.tcsnodecontent"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder=""
              readonly
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="是否违约" prop="iswy">
            <el-input
              v-model="node.iswy"
              clearable
              placeholder=""
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="node.iswy == '是'">
          <el-form-item label="违约内容及违约责任" prop="tcsnodepost">
            <el-input
              v-model="node.tcsnodepost"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder=""
              readonly
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-divider>填写反馈意见</el-divider>
        </el-col>
        <el-col :span="24">
          <el-form-item label="反馈意见" prop="feedback">
            <el-input
              v-model="node.feedback"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder=""
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-divider>附件</el-divider>
          <AttachList :att-list="formData.attList" :readonly="true" />
        </el-col>
      </el-form>
    </el-row>
    <!-- <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save(2)">通 过</el-button>
      <el-button type="danger" @click="save(3)">驳 回</el-button>
    </template> -->
  </el-dialog>
</template>

<script>
import { trackingApproval, getNodeDetail } from '@/api/contract/fulfil'
import AttachList from '@/views/contract/execute/components/AttachList.vue'

export default {
  name: 'ApprovalEdit',
  components: { AttachList },
  data() {
    return {
      formData: {
        nodeId: undefined,
        planStatus: undefined,
        feedback: undefined,
        attList: [],
      },
      rules: {
        date: [
          {
            required: true,
            message: '请选择提醒时间',
            trigger: 'blur',
          },
        ],
        feedback: [
          {
            required: true,
            message: '请输入反馈意见',
            trigger: 'blur',
          },
        ],
        content: [
          {
            required: true,
            message: '请输入提醒内容',
            trigger: 'blur',
          },
        ],
      },
      title: '履行审批',
      dialogFormVisible: false,
      options: [],
      node: { attList: [] },
    }
  },
  computed: {
    pickerOptions() {
      return {
        disabledDate(time) {
          return time.getTime() < Date.now()
        },
      }
    },
  },
  created() {},
  methods: {
    showEdit(row) {
      console.log('row', row)
      this.fetchNodeDetail({ planId: row.nodeid })
      this.formData.nodeId = row.nodeid
      this.dialogFormVisible = true
    },
    close() {
      this.$refs['form'].resetFields()
      this.formData = this.$options.data().formData
      this.dialogFormVisible = false
    },
    async fetchNodeDetail(data) {
      const res = await getNodeDetail(data)
      this.formData.attList = res.data.attList
      this.node = res.data.node
    },
    save(status) {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          console.log('status1', status)
          this.formData.planStatus = status
          console.log('status2', status)
          const { msg } = await trackingApproval({
            ...this.formData,
            tcu: this.row,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.$emit('fetch-data')
          this.close()
        }
      })
    },
  },
}
</script>
<style scoped>
.formula .el-form-item--small.el-form-item {
  margin-bottom: 5px;
}
</style>
