<!--
 * @Author: 康某 dev@example.com
 * @Date: 2022-10-23 16:23:51
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-10-23 16:29:23
 * @FilePath: \hb-admin\src\views\contract\legal\components\zcbqAdd.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div>
    <el-form :model="form" :rules="rules" label-width="140px">
      <el-row>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="纠纷名称" prop="disputename">
            <el-input
              v-model="form.disputename"
              placeholder="请选择纠纷名称"
              :style="{ width: '75%' }"
              disabled
            ></el-input>
            <el-button
              :style="{ marginLeft: '10px', height: '30px' }"
              type="primary"
              :disabled="disabled"
              @click="$refs.xzjf.show(2)"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="所属过程" prop="sslx">
            <el-select
              v-model="form.sslx"
              style="width: 100%"
              :disabled="disabled"
              @change="changeSSlx"
            >
              <el-option value="诉讼">诉讼</el-option>
              <el-option value="仲裁">仲裁</el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :lg="12" :md="12" :sm="24" v-if="form.sslx == '诉讼'">
          <el-form-item label="关联诉讼" prop="litigationname">
            <el-input
              v-model="form.litigationname"
              clearable
              placeholder="请选择关联诉讼"
              disabled
              :style="{ width: '75%' }"
            />
            <el-button
              :style="{ marginLeft: '10px', height: '30px' }"
              type="primary"
              :disabled="disabled"
              @click="$refs.ss.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24" v-if="form.sslx == '仲裁'">
          <el-form-item label="关联仲裁" prop="arbitraname">
            <el-input
              v-model="form.arbitraname"
              clearable
              placeholder="请选择关联仲裁"
              disabled
              :style="{ width: '75%' }"
            />
            <el-button
              :style="{ marginLeft: '10px', height: '30px' }"
              type="primary"
              :disabled="disabled"
              @click="$refs.glzc.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="执行案号" prop="execuno">
            <el-input
              v-model="form.execuno"
              placeholder="请输入执行案号"
              :disabled="disabled"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="执行法院" prop="execucourt">
            <el-input
              v-model="form.execucourt"
              clearable
              placeholder="请选择执行法院"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="执行方式" prop="executype">
            <el-select
              v-model="form.executype"
              style="width: 100%"
              :disabled="disabled"
            >
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
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="执行总金额（万元）" prop="execuamount">
            <el-input
              v-model="form.execuamount"
              type="number"
              :disabled="disabled"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="已执行总金额（万元）" prop="execuedamount">
            <el-input
              v-model="form.execuedamount"
              type="number"
              :disabled="disabled"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="未执行总金额（万元）" prop="noexecuamount">
            <el-input
              v-model="form.noexecuamount"
              type="number"
              :disabled="disabled"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行时间" prop="executiontime">
            <el-date-picker
              v-model.trim="form.executiontime"
              clearable
              format="yyyy-MM-dd"
              placeholder="请选择执行时间"
              style="width: 100%"
              value-format="yyyy-MM-dd"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div style="text-align: right; margin-top: 10px" v-if="!disabled">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="add">确 定</el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
    </div>
    <!-- 纠纷登记 -->
    <xzjf-options ref="xzjf" @selected="handleSsjd" />
    <!-- 关联仲裁 -->
    <glzc-options ref="glzc" @selected="handleSelectGlzc" />
    <!-- 关联诉讼 -->
    <ss-options ref="ss" @selected="handleSelectSs" />
    <!-- <template #footer>
      <el-button @click="close">关 闭</el-button>
      <el-button type="primary" @click="save">保 存</el-button>
    </template> -->
    <!-- 提交 -->
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </div>
</template>

<script>
  import xzjfOptions from '@/views/fwgl/legal/components/options/xzjf.vue'
  import glzcOptions from '@/views/fwgl/legal/components/options/glzc.vue'
  import ssOptions from '@/views/fwgl/legal/components/options/ss.vue'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import {
    legalExecumgrDetail,
    legalExecumgrSave,
    getDetailInfo,
  } from '@/api/fwgl/legal'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { changeFormSizeStyleFunc } from '@/utils/processMobile'

  export default {
    components: {
      CandidateUserSelect,
      Resubmit,
      xzjfOptions,
      glzcOptions,
      ssOptions,
    },
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
          executiontime: '',
          status: '',
          sslx: '',
          litigationname: '',
          arbitraname: '',
        },
        rules: {},
        dialogFormVisible: false,
        title: '',
        disabled: true,
        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
      }
    },
    methods: {
      async show(
        data,
        title,
        row,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        status
      ) {
        if (formId) {
          this.fromId = formId
          this.fromIdcopy = formId // fromId为-1时，拷贝一份
        }
        if (flowtaskinfoflowid) {
          this.flowtaskinfoflowid = flowtaskinfoflowid
        }
        if (ymFromId) {
          this.ymFromId = ymFromId
        }
        this.status = status
        // this.form.disputename = this.info.disputename
        // this.form.disputeid = this.info.disputeid
        if (row) {
          let res = await getDetailInfo({ id: row.id })
          this.form.disputename = res.data.disputename
          this.form.execuno = res.data.execuno
          this.form.execucourt = res.data.execucourt
          this.form.executype = res.data.executype
          this.form.execuamount = res.data.execuamount
          this.form.execuedamount = res.data.execuedamount
          this.form.noexecuamount = res.data.noexecuamount
          this.form.executiontime = res.data.executiontime
          this.form.id = res.data.id
          this.form.status = res.data.status
          this.form.litigationid = res.data.litigationid || ''
          this.form.litigationname = res.data.litigationname || ''
          this.form.arbitraid = res.data.arbitraid || ''
          this.form.arbitraname = res.data.arbitraname || ''
          this.form.sslx = res.data.sslx
        }

        this.title = title
        if (title == '编辑') {
          this.disabled = false
        }
        this.dialogFormVisible = true
        // this.form.litigationid = data.litigationid
        // this.form.arbitraid = data.arbitraid

        if (this.$store.state.work.processMobile) {
          this.$nextTick(() => {
            changeFormSizeStyleFunc()
          })
        }
      },
      handleSsjd(val) {
        this.form.disputeid = val.disputeid
        this.$set(this.form, 'disputeItem', val.disputeitem)
      },
      close() {
        // this.dialogFormVisible = false

        this.$bus.$emit('updateMsg', 0)
        this.form = {
          id: '',
          disputename: '',
          execuno: '',
          execucourt: '',
          executype: '',
          execuamount: '',
          execuedamount: '',
          noexecuamount: '',
          executiontime: '',
          sslx: '',
          litigationname: '',
          arbitraname: '',
        }
      },
      async add() {
        const res = await legalExecumgrSave(this.form)
        // 返回date...
        if (res && res.date) {
          this.$message({
            type: 'success',
            message: '保存成功！',
          })
          // this.$emit('selected', res.date)
          // this.$emit('fetch')
          // this.close()
        } else {
          this.$message({
            type: 'error',
            message: '保存失败！',
          })
        }
      },
      handleSelectGlzc(val) {
        this.$set(this.form, 'arbitraname', val.courtfirst)
        this.$set(this.form, 'arbitraid', val.arbitraid)
      },
      handleSelectSs(val) {
        this.$set(this.form, 'litigationname', val.disputeitem)
        this.$set(this.form, 'litigationid', val.litigationid)
      },
      changeSSlx(val) {
        if (val == '仲裁') {
          this.$set(this.form, 'litigationname', '')
          this.$set(this.form, 'litigationid', '')
        } else {
          this.$set(this.form, 'arbitraname', '')
          this.$set(this.form, 'arbitraid', '')
        }
      },
      //提交
      async ymsubmit() {
        this.$refs.resubmit.ymsubmit()
      },
    },
  }
</script>

<style lang="less" scoped></style>
