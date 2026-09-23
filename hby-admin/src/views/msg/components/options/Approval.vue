<template>
  <div>
    <div>
      <el-row :gutter="15">
        <el-col :span="24">
          <el-divider>审批意见</el-divider>
        </el-col>
        <el-form ref="form" label-width="100px" :model="form" :rules="rules">
          <el-col :span="24">
            <el-form-item label="审批意见" prop="examination">
              <el-input
                v-model="form.examination"
                rows="3"
                :style="{ width: '100%' }"
                type="textarea"
              />
            </el-form-item>
            <el-form-item v-if="showSign" label="电子签名">
              <vue-esign
                ref="esign"
                :height="200"
                style="border: 1px #d5d5d5 solid; width: 100%"
                :width="860"
              />
            </el-form-item>
            <el-form-item v-if="showRepeat" label="添加会签人">
              <el-input
                style="width: 600px"
                placeholder="选择会签人"
                v-model="form.zfstaffname"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.userlistdialog.show()"
              >
                选择
              </el-button>
              <!-- <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="doRepeat('审查提交')"
                :disabled="form.zfstaffname === ''"
                需调整能修改
              >
                转发
              </el-button> -->
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="clearZfstaff"
              >
                取消
              </el-button>
            </el-form-item>
            <el-form-item v-if="showFgrepeat" label="添加会签人">
              <el-input
                style="width: 600px"
                placeholder="选择会签人"
                v-model="form.zfstaffname"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.fgldlistdialog.show()"
              >
                选择
              </el-button>
              <!-- <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="doRepeat('审查提交')"
                :disabled="form.zfstaffname === ''"
                需调整能修改
              >
                转发
              </el-button> -->
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="clearZfstaff"
              >
                取消
              </el-button>
            </el-form-item>
          </el-col>
        </el-form>
        <el-col :span="24" style="display: flex">
          <div style="height: 20px; width: 100px"></div>
          <el-button
            v-for="(item, index) in results"
            :key="index"
            type="primary"
            @click="doSave(item)"
          >
            {{ item }}
          </el-button>
          <el-button
            v-if="results === undefined"
            type="primary"
            @click="doSave('提交')"
          >
            提 交
          </el-button>
          <el-button v-if="showSign" type="primary" @click="handleReset">
            清 除
          </el-button>
        </el-col>
      </el-row>
    </div>
    <bmfzr-list
      ref="userlistdialog"
      @selected="handleRepeatUserSelected"
    ></bmfzr-list>
    <fgld-list
      ref="fgldlistdialog"
      @selected="handleRepeatUserSelected"
    ></fgld-list>
  </div>
</template>

<script>
  import {
    dealAuditPlanApporvalInfo,
    dealProjectApporvalInfo,
  } from '@/api/audit/implement'
  import {
    blprocessfk,
    blprocesshtjy,
    blprocessjc,
    blprocesssk,
    blprocessyszc,
    scStatus,
  } from '@/api/setting/msg'
  import vueEsign from 'vue-esign'
  import bmfzrList from './BmfzrList.vue'
  import fgldList from './FgldList.vue'
  export default {
    name: 'DraftEdit',
    components: { vueEsign, bmfzrList, fgldList },
    props: {
      spinfo: {
        type: Object,
        // default: {},
      },
      showRepeat: {
        type: Boolean,
        default: false,
      },
      showFgrepeat: {
        type: Boolean,
        default: false,
      },
      showSign: {
        type: Boolean,
        default: false,
      },
      processType: {
        type: String,
        default: 'blprocessjc',
      },
      processData: {
        type: Object,
        default: undefined,
      },
      results: {
        type: Array,
        default: undefined,
      },
    },
    data() {
      return {
        form: {
          examination: undefined,
          imgBaseStr: undefined,
          taskId: undefined,
          processDefinitionId: undefined, //cyhw_jjhtjc:10:175751,
          processInstanceId: undefined,
          transitionName: undefined,
          flowid: undefined,
          cyId: undefined,
          // businessKey: undefined,
          contractId: undefined,
          contractNo: undefined,
          zfstaffname: '',
          zfstaffid: '',
        },
        rules: {
          examination: [
            {
              required: true,
              message: '请输入意见',
              trigger: 'blur',
            },
          ],
          imgBaseStr: [
            {
              required: true,
              message: '请签名',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
      }
    },
    mounted() {
      //
    },
    methods: {
      handleRepeatUserSelected(node) {
        this.form.zfstaffname = node.realname
        this.form.zfstaffid = node.staffid
      },
      clearZfstaff() {
        this.form.zfstaffname = ''
        this.form.zfstaffid = ''
      },
      //清空
      handleReset() {
        this.$refs.esign.reset()
      },
      //转换图片
      handleGenerate(transitionName) {
        this.$refs.esign
          .generate()
          .then((res) => {
            this.form.imgBaseStr = res
            this.save(transitionName)
          })
          .catch((err) => {
            console.error(err)
            this.save(transitionName)
          })
      },
      doSave(transitionName) {
        if (this.showSign) {
          this.handleGenerate(transitionName)
        } else {
          this.save(transitionName)
        }
      },
      async doRepeat(transitionName) {
        this.form.transitionName = transitionName
        this.form.contractId = this.spinfo && this.spinfo.tcu.contractid
        this.form.describe = this.spinfo && this.spinfo.tcu.describe
        this.form.processInstanceId =
          this.spinfo && this.spinfo.task.processInstanceId
        this.form.cyId = this.spinfo && this.spinfo.cy.cyid
        const { msg } = await scStatus(this.form)
        this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        this.$emit('close')
      },
      showDetail() {
        this.form.examination = ''
        this.dialogFormVisible = true
        this.dotClick = false
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      getQueryVariable(url, variable) {
        var query = url.substring(1)
        var vars = query.split('?')
        for (var i = 0; i < vars.length; i++) {
          var pair = vars[i].split('=')
          if (pair[0] == variable) {
            return pair[1]
          }
        }
        return false
      },
      save(transitionName) {
        if (this.processType == 'dealAuditPlanApporvalInfo') {
          this.auditSave(transitionName)
          this.close()
        } else if (this.processType == 'project') {
          this.auditProject(transitionName)
          this.close()
        } else {
          this.contractSave(transitionName)
        }
      },
      // 项目相关
      auditProject(transitionName) {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let func = () => {}
            const tempData = Object.assign({}, this.form)
            var planid = this.getQueryVariable(this.processData.cyurl, 'spid')
            tempData.cyId = this.processData.cyid ? this.processData.cyid : ''
            tempData.optDesc = this.form.examination
            tempData.projectId = planid
            tempData.taskId = this.spinfo && this.spinfo.task.taskId
            // tempData.taskId = this.processData.taskid
            tempData.transition = transitionName
            tempData.processDefinitionId =
              this.spinfo && this.spinfo.task.processDefinitionId
            tempData.processInstanceId =
              this.spinfo && this.spinfo.task.processInstanceId
            func = dealProjectApporvalInfo
            func(tempData)
              .then((res) => {
                if (res.code === 1) {
                  this.$message.success(res.msg)
                  this.$bus.$emit('updateMsg', 0)
                  this.dotClick = true
                } else {
                  this.dotClick = false
                }
              })
              .finally(() => {})
          }
        })
      },
      // 审计相关
      auditSave(transitionName) {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let func = () => {}
            const tempData = Object.assign({}, this.form)

            var planid = this.getQueryVariable(this.processData.cyurl, 'planid')
            tempData.cyId = this.processData.cyid ? this.processData.cyid : ''
            tempData.optDesc = this.form.examination
            tempData.planId = planid
            tempData.taskId = this.spinfo && this.spinfo.task.taskId
            // tempData.taskId = this.processData.taskid
            tempData.transition = transitionName
            tempData.processDefinitionId =
              this.spinfo && this.spinfo.task.processDefinitionId
            tempData.processInstanceId =
              this.spinfo && this.spinfo.task.processInstanceId
            func = dealAuditPlanApporvalInfo
            func(tempData)
              .then((res) => {
                if (res.code === 1) {
                  this.$message.success(res.msg)
                  this.$bus.$emit('updateMsg', 0)
                  this.dotClick = true
                } else {
                  this.dotClick = false
                }
              })
              .finally(() => {})
            // const { msg } = await frozenAccountModify(this.form).then((res) => {
            //
            // })
            // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
        })
      },
      // 合同相关
      contractSave(transitionName) {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let userInfo = JSON.parse(localStorage.getItem('userInfo'))

            if (
              transitionName == '通过' &&
              this.processData.cystaffid == userInfo.staffid &&
              this.processData.cystate == '需调整'
            ) {
              this.$confirm('上方主体信息是否确认保存？', '提示', {
                confirmButtonText: '是',
                cancelButtonText: '否',
                type: 'warning',
              })
                .then(() => {
                  let func = () => {}

                  const tempData = Object.assign({}, this.form)

                  //下面三项从task对象取
                  if (this.processData.task) {
                    tempData.taskId = this.processData.task.taskId
                    tempData.processDefinitionId =
                      this.processData.task.processDefinitionId
                    tempData.processInstanceId =
                      this.processData.task.processInstanceId
                  } else {
                    // 不能用外边的taskid
                    // tempData.taskId = this.processData.taskid
                  }

                  if (this.processData.collection) {
                    tempData.collectionid =
                      this.processData.collection.collectionid
                  } else {
                  }
                  if (this.processData.payment) {
                    tempData.paymentid =
                      this.processData.payment.paymentid || ''
                  } else {
                  }

                  tempData.transitionName = transitionName
                  tempData.flowid = this.processData.flowid
                  tempData.cyId = this.processData.cyid
                    ? this.processData.cyid
                    : this.processData.cy
                    ? this.processData.cy.cyid
                    : ''
                  tempData.contractId = this.processData.contractid
                  tempData.contractNo = this.processData.contractno
                  tempData.budgetId = this.processData.taskid
                  tempData.lendid = this.processData.taskid
                  if (this.processType === 'blprocessjc') {
                    func = blprocessjc
                  } else if (this.processType === 'blprocessyszc') {
                    func = blprocessyszc
                  } else if (this.processType === 'blprocesssk') {
                    func = blprocesssk
                  } else if (this.processType === 'blprocessfk') {
                    func = blprocessfk
                  } else if (this.processType === 'blprocesshtjy') {
                    func = blprocesshtjy
                  }
                  func(tempData)
                    .then((res) => {
                      if (res.code === '1') {
                        this.$message.success(res.msg)
                        this.$bus.$emit('updateMsg', 0)
                        if (this.showRepeat || this.showFgrepeat) {
                          this.doRepeat('审查提交')
                        }
                        this.$emit('close')
                        this.$bus.$emit('changeSaveBtn')
                      }
                    })
                    .finally(() => {})
                  // const { msg } = await frozenAccountModify(this.form).then((res) => {
                  //
                  // })
                  // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
                })
                .catch(() => {
                  this.$message({
                    type: 'info',
                    message: '已取消',
                  })
                })
            } else {
              let func = () => {}

              const tempData = Object.assign({}, this.form)

              //下面三项从task对象取
              if (this.processData.task) {
                tempData.taskId = this.processData.task.taskId
                tempData.processDefinitionId =
                  this.processData.task.processDefinitionId
                tempData.processInstanceId =
                  this.processData.task.processInstanceId
              } else {
                // 不能用外边的taskid
                // tempData.taskId = this.processData.taskid
              }

              if (this.processData.collection) {
                tempData.collectionid = this.processData.collection.collectionid
              } else {
              }
              if (this.processData.payment) {
                tempData.paymentid = this.processData.payment.paymentid || ''
              } else {
              }

              tempData.transitionName = transitionName
              tempData.flowid = this.processData.flowid
              tempData.cyId = this.processData.cyid
                ? this.processData.cyid
                : this.processData.cy
                ? this.processData.cy.cyid
                : ''
              tempData.contractId = this.processData.contractid
              tempData.contractNo = this.processData.contractno
              tempData.budgetId = this.processData.taskid

              if (this.processType === 'blprocessjc') {
                func = blprocessjc
              } else if (this.processType === 'blprocessyszc') {
                func = blprocessyszc
              } else if (this.processType === 'blprocesssk') {
                func = blprocesssk
              } else if (this.processType === 'blprocessfk') {
                func = blprocessfk
              } else if (this.processType === 'blprocesshtjy') {
                tempData.lendid = this.processData.taskid
                // tempData.lenddate = this.processData.lenddate
                tempData.returnDate = this.processData.returndate
                tempData.memo = this.processData.memo
                func = blprocesshtjy
              }
              func(tempData)
                .then((res) => {
                  if (res.code == '1') {
                    this.$message.success(res.msg)
                    this.$bus.$emit('updateMsg', 0)
                    if (this.showRepeat || this.showFgrepeat) {
                      this.doRepeat('审查提交')
                    }
                    this.$emit('close')
                  }
                })
                .finally(() => {})
              // const { msg } = await frozenAccountModify(this.form).then((res) => {
              //
              // })
              // this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            }
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
  .el-input.is-disabled .el-input__inner {
    background: #fff;
    color: #333;
  }
</style>
