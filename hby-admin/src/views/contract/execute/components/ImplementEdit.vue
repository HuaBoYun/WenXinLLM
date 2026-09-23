<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <vab-query-form v-if="!disabled">
      <vab-query-form-right-panel :span="24">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save" :loading="buttonLoading">
          保 存
        </el-button>
        <!-- <el-button type="success" @click="submit(1)">提 交</el-button> -->
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-row :gutter="15">
      <el-form
        ref="form"
        label-width="150px"
        :model="formData"
        :rules="rules"
        :disabled="disabled"
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
              format="yyyy-MM-dd"
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
              format="yyyy-MM-dd"
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
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="'履行类别'" prop="performanceCategory">
            <el-select style="width: 100%" v-model="node.goodsName" disabled>
              <!-- <el-option label="收付款" value="1"></el-option>
              <el-option label="货物" value="2"></el-option>
              <el-option label="工期" value="3"></el-option>
              <el-option label="服务期" value="4"></el-option>
              <el-option label="交付成果" value="5"></el-option>
              <el-option label="其它" value="6"></el-option> -->
              <el-option label="货物" value="货物"></el-option>
              <el-option label="工期" value="工期"></el-option>
              <el-option label="服务期" value="服务期"></el-option>
              <el-option label="交付成果" value="交付成果"></el-option>
              <el-option label="其它" value="其它"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <!-- <el-col v-if="node.performanceCategory === '2'" :span="12">
          <el-form-item label="货物名称" prop="goodsName">
            <el-input
              v-model.number="node.goodsName"
              clearable
              readonly
              :placeholder="'请输入货物名称'"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col> -->
        <!-- <el-col v-if="node.performanceCategory === '2'" :span="12">
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
        </el-col> -->
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
              readonly
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
              readonly
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>填写履行信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开始时间" prop="startdate">
            <el-date-picker
              v-model="formData.startdate"
              clearable
              placeholder=""
              :style="{ width: '100%' }"
              type="date"
              value-format="yyyy-MM-dd"
            />
            <!--
                :picker-options="pickerOptionsStart"
              @change="handleEndTime(false)"
              @blur="handleEndTime(false)" -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束时间" prop="enddate">
            <el-date-picker
              v-model="formData.enddate"
              clearable
              placeholder=""
              :style="{ width: '100%' }"
              type="date"
              value-format="yyyy-MM-dd"
            />
            <!-- :disabled="
                formData.startdate == null && formData.enddate == null
                  ? true
                  : false
              "
              :picker-options="pickerOptionsEnd"
              @change="handleEndTime(true)"
              @blur="handleEndTime(true)" -->
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="履行情况" prop="nodememo">
            <el-input
              v-model="formData.nodememo"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder=""
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="验收情况" prop="nodecontent">
            <el-input
              v-model="formData.nodecontent"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder=""
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="是否违约" prop="iswy">
            <el-select v-model="formData.iswy">
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col v-if="formData.iswy == '是'" :span="24">
          <el-form-item label="违约内容及违约责任" prop="nodepost" required>
            <el-input
              v-model="formData.nodepost"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder=""
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-divider>附件</el-divider>
          <AttachList
            :att-list="formData.attList"
            :local-list="localList"
            @delete-att="handleDeleteAtt"
            @upload-success="handleUploadSuccess"
            :node-id="formData.nodeId"
          />
        </el-col>
      </el-form>
    </el-row>
  </el-dialog>
</template>

<script>
import {
  getNodeDetail,
  implement,
  trackingApproval,
} from '@/api/contract/fulfil'
import { deleteAttach } from '@/api/contract/manage'
import AttachList from './AttachList.vue'
export default {
  name: 'ImplementEdit',
  components: { AttachList },
  data() {
    return {
      // // 设置开始日期
      // pickerOptionsStart: {
      //   disabledDate: (time) => {
      //     let endDateVal = this.formData.enddate
      //     if (endDateVal) {
      //       return (
      //         time.getTime() > new Date(endDateVal).getTime() ||
      //         time.getTime() < new Date(endDateVal).getTime() - 86400000
      //       )
      //     } else {
      //       return time.getTime() < Date.now() - 8.64e7
      //     }
      //   },
      //   // 限制时间
      //   selectableRange: [
      //     new Date().getHours() +
      //       ':' +
      //       new Date().getMinutes() +
      //       ':' +
      //       new Date().getSeconds() +
      //       ' - 23:59:59',
      //   ],
      // },
      // // 设置结束日期
      // pickerOptionsEnd: {
      //   disabledDate: (time) => {
      //     let beginDateVal = this.formData.startdate
      //     if (beginDateVal) {
      //       return (
      //         time.getTime() < new Date(beginDateVal).getTime() - 86400000
      //         // ||
      //         // time.getTime() > new Date(beginDateVal).getTime()
      //       )
      //     } else {
      //       return time.getTime() < Date.now() - 8.64e7
      //     }
      //   },
      //   // 限制时间
      //   selectableRange: [
      //     new Date().getHours() +
      //       ':' +
      //       new Date().getMinutes() +
      //       ':' +
      //       new Date().getSeconds() +
      //       ' - 23:59:59',
      //   ],
      // },
      buttonLoading: false,
      disabled: false,
      formData: {
        contractId: undefined,
        nodeId: undefined,
        spnodeid: undefined,
        startdate: undefined,
        enddate: undefined,
        nodememo: undefined,
        nodecontent: undefined,
        iswy: undefined,
        nodepost: undefined,
        attList: [],
      },
      localList: [],
      rules: {
        startdate: [
          {
            required: true,
            message: '请选择开始时间',
            trigger: 'blur',
          },
        ],
        enddate: [
          {
            required: true,
            message: '请选择结束时间',
            trigger: 'blur',
          },
        ],
        nodememo: [
          {
            required: true,
            message: '请输入交付情况',
            trigger: 'blur',
          },
        ],
        // nodecontent: [
        //   {
        //     required: true,
        //     message: '请输入验收情况',
        //     trigger: 'blur',
        //   },
        // ],
        iswy: [
          {
            required: true,
            message: '请选择是否违约',
            trigger: 'blur',
          },
        ],
      },
      title: '履行落实',
      dialogFormVisible: false,
      options: [],
      node: {},
    }
  },
  computed: {
    // pickerOptions() {
    //   return {
    //     disabledDate(time) {
    //       return time.getTime() < Date.now() && false
    //     },
    //   }
    // },
  },
  created() {},
  watch: {
    // 'formData.startdate': {
    //   handler(newVal, oldVal) {
    //     if (newVal != null && newVal != undefined) {
    //       // 原来的数据一直为今天，和新时间比对
    //       oldVal = Date.now()
    //       let start = new Date(oldVal)
    //       let end = new Date(newVal)
    //       // 判断是否超过一天
    //       var dateDiff = end.getDate() - start.getDate()
    //       if (dateDiff >= 1) {
    //         this.pickerOptionsStart.selectableRange = '00:00:00 - 23:59:59'
    //       } else {
    //         this.pickerOptionsStart.selectableRange =
    //           new Date().getHours() +
    //           ':' +
    //           new Date().getMinutes() +
    //           ':' +
    //           new Date().getSeconds() +
    //           ' - 23:59:59'
    //       }
    //     }
    //   },
    // },
    // 'formData.enddate': {
    //   handler(newVal, oldVal) {
    //     if (newVal != null && newVal != undefined) {
    //       // 原来的数据一直为今天，和新时间比对
    //       oldVal = Date.now()
    //       let start = new Date(oldVal)
    //       let end = new Date(newVal)
    //       // 判断是否超过一天
    //       var dateDiff = end.getDate() - start.getDate()
    //       if (dateDiff >= 1) {
    //         if (
    //           this.formData.startdate != null &&
    //           this.formData.startdate != undefined
    //         ) {
    //           let startdate = new Date(this.formData.startdate)
    //           this.pickerOptionsEnd.selectableRange =
    //             startdate.getHours() +
    //             ':' +
    //             startdate.getMinutes() +
    //             ':' +
    //             startdate.getSeconds() +
    //             ' - 23:59:59'
    //         }
    //       } else {
    //         this.pickerOptionsEnd.selectableRange =
    //           new Date().getHours() +
    //           ':' +
    //           new Date().getMinutes() +
    //           ':' +
    //           new Date().getSeconds() +
    //           ' - 23:59:59'
    //       }
    //     }
    //   },
    // },
  },
  methods: {
    //回调
    handleUploadSuccess(val) {
      this.localList.push(val.data)
    },
    //删除附件
    async handleDeleteAtt(row, index) {
      const { code } = await deleteAttach({
        attid: row.attid,
      })
      if (code == 1) {
        if (row.type == 'local') {
          this.localList.splice(index - this.formData.attList.length, 1)
        } else {
          this.formData.attList.splice(index, 1)
        }
      }
    },
    // 判断结束时间
    handleEndTime(obj) {
      if (this.formData.startdate != null && this.formData.enddate != null) {
        if (this.formData.startdate >= this.formData.enddate) {
          this.msgError('结束时间必须大于开始时间！')
          if (obj) {
            this.formData.enddate = null
          } else {
            this.formData.startdate = null
          }
        } else {
          let start = new Date(this.formData.startdate)
          let end = new Date(this.formData.enddate)
        }
      }
    },

    showEdit(row) {
      this.disabled = false
      this.fetchNodeDetail({ planId: row.nodeid })
      this.formData.nodeId = row.nodeid
      this.formData.contractId = row.projectid
      this.dialogFormVisible = true
    },
    showDetail(row) {
      this.disabled = true
      this.fetchNodeDetail({ planId: row.nodeid })
      this.formData.nodeId = row.nodeid
      this.formData.contractId = row.projectid
      this.dialogFormVisible = true
    },
    close() {
      this.$refs['form'].resetFields()
      this.formData = this.$options.data().formData
      this.dialogFormVisible = false
    },
    async fetchNodeDetail(data) {
      const res = await getNodeDetail(data)

      const node = res.data.node
      const {
        tcpspnodeid,
        tcsstartdate,
        tcsenddate,
        tcsnodememo,
        tcsnodecontent,
        iswy,
        tcsnodepost,
      } = node
      this.formData.spnodeid = tcpspnodeid
      this.formData.startdate = tcsstartdate || node.planstartdate
      this.formData.enddate = tcsenddate || node.planenddate
      this.formData.nodememo = tcsnodememo
      this.formData.nodecontent = tcsnodecontent
      this.formData.iswy = iswy
      this.formData.nodepost = tcsnodepost
      this.formData.attList = res.data.attList

      this.node = node

      // 重置localList
      this.localList = []
    },
    //提交
    submit(status) {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          this.formData.planStatus = status

          const { msg } = await trackingApproval({
            nodeId: this.formData.nodeId,
            planStatus: 1,
            feedback: '',
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.$emit('fetch-data')
          this.close()
        }
      })
    },
    save() {
      this.$refs['form'].validate(async (valid) => {
        if (valid) {
          const arrAttid = this.localList.map((item) => item.attid)
          if (arrAttid && arrAttid.length) {
            this.formData.attids = arrAttid.join(',')
          }
          if (this.formData.iswy == '否') {
            this.formData.nodepost = ''
          }
          this.buttonLoading = true

          const { msg } = await implement(this.formData)
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.$emit('fetch-data')

          this.close()
          this.buttonLoading = false
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
