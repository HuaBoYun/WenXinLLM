<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="close">取 消</el-button>
        <!-- <el-button type="primary" @click="save(2)">通 过</el-button> -->
        <!-- <el-button type="danger" @click="save(3)">驳 回</el-button> -->
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-row :gutter="15">
      <el-form ref="form" label-width="150px" :model="formData" :rules="rules">
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
          <el-form-item
            label="收款比例"
            v-if="node.performanceCategory !== '2'"
            prop="nodepost"
          >
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
        <el-col :span="12">
          <el-form-item :label="'履行类别'" prop="performanceCategory">
            <el-select
              readonly
              style="width: 100%"
              v-model="node.performanceCategory"
            >
              <el-option label="收付款" value="1"></el-option>
              <el-option label="货物" value="2"></el-option>
              <el-option label="工期" value="3"></el-option>
              <el-option label="服务期" value="4"></el-option>
              <el-option label="交付成果" value="5"></el-option>
              <el-option label="其它" value="6"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="node.performanceCategory === '2'" :span="12">
          <el-form-item label="货物名称" prop="goodsName">
            <el-input
              readonly
              v-model.number="node.goodsName"
              clearable
              :placeholder="'请输入货物名称'"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="node.performanceCategory === '2'" :span="12">
          <el-form-item label="货物数量" prop="goodsCount ">
            <el-input
              v-model.number="node.goodsCount"
              clearable
              readonly
              :placeholder="'请输入货物数量'"
              :style="{ width: '100%' }"
              oninput="value=value.replace(/^(0+)|[^\d]+/g,'')"
            />
          </el-form-item>
        </el-col>
        <el-col v-if="node.performanceCategory == '1'" :span="12">
          <el-form-item
            :label="node.tcudctype == '付款' ? '付款金额(元)' : '收款金额(元)'"
            prop="nodemoney"
          >
            <el-input
              v-model.number="node.nodemoney"
              clearable
              :placeholder="'请输入收付款金额(元)'"
              :style="{ width: '100%' }"
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
          <el-form-item label="交付情况" prop="tcsnodememo">
            <el-input
              v-model="node.tcsnodememo"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder=""
              readonly
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
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
        </el-col>
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
              v-model="formData.feedback"
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
  import { getNodeDetail, trackingApproval } from '@/api/contract/fulfil'
  import AttachList from './AttachList.vue'

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
        this.fetchNodeDetail({ planId: row.nodeid })
        this.formData.nodeId = row.nodeid
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
      },
      //详情
      async fetchNodeDetail(data) {
        const res = await getNodeDetail(data)
        this.formData.attList = res.data.attList
        this.node = res.data.node
      },
      save(status) {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.formData.planStatus = status

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
