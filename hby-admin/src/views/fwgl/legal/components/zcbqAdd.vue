<!--
 * @Author: 康某 dev@example.com
 * @Date: 2022-10-23 16:23:51
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-10-23 16:29:23
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
    <el-form :model="form" :rules="rules" label-width="140px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="申请人" prop="apper">
            <el-input
              v-model="form.apper"
              placeholder="请输入申请人"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被申请人" prop="responder">
            <el-input
              v-model="form.responder"
              placeholder="请输入申请人"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属纠纷" prop="disputeItem">
            <el-input
              v-model="form.disputeItem"
              disabled
              placeholder="请选择纠纷登记"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="保全类型" prop="actionstage">
            <el-select v-model="form.actionstage" style="width: 100%">
              <el-option value="诉前保全">诉前保全</el-option>
              <el-option value="诉中保全">诉中保全</el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否申请保全" prop="isapppreserva">
            <el-select v-model="form.isapppreserva" style="width: 100%">
              <el-option :value="1" label="是">是</el-option>
              <el-option :value="0" label="否">否</el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="保全资产数额（万元）" prop="preservaamount">
            <el-input v-model="form.preservaamount" type="number"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="保全资产性质" prop="preservanature">
            <el-input v-model="form.preservanature"></el-input>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="是否执行扣划" prop="isexecutdeduction">
            <el-select v-model="form.isexecutdeduction" style="width: 100%">
              <el-option :value="1" label="是">是</el-option>
              <el-option :value="0" label="否">否</el-option>
            </el-select>
          </el-form-item>
        </el-col> -->
        <!-- <el-col :span="12">
          <el-form-item label="执行金额（万元）" prop="executamount">
            <el-input v-model="form.executamount" type="number"></el-input>
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="是否解除保全" prop="isreleasepreserva">
            <el-select v-model="form.isreleasepreserva" style="width: 100%">
              <el-option :value="1" label="是"></el-option>
              <el-option :value="0" label="否"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
      <el-button type="primary" @click="save">保 存</el-button>
    </template>
    <!-- 纠纷登记 -->
    <xzjf-options ref="xzjf" @selected="handleSsjd" />
  </el-dialog>
</template>

<script>
  import xzjfOptions from './options/xzjf.vue'
  import { assetporotectSave, getBQDetailInfo } from '@/api/fwgl/legal'
  export default {
    components: { xzjfOptions },
    props: ['info'],
    data() {
      return {
        form: {
          apper: '',
          responder: '',
          disputeItem: '',
          actionstage: '',
          isapppreserva: '',
          preservaamount: '',
          preservanature: '',
          isreleasepreserva: '',
        },
        rules: {},
        dialogFormVisible: false,
        title: '',
      }
    },
    methods: {
      async show(data, title, row) {
        this.title = title
        this.form.disputeItem = this.info.disputename
        this.form.disputeid = this.info.disputeid
        if (row) {
          let res = await getBQDetailInfo({ id: row.id })
          this.form.apper = res.data.apper
          this.form.responder = res.data.responder
          this.form.disputeItem = res.data.disputeitem
          this.form.actionstage = res.data.actionstage
          this.form.isapppreserva = res.data.isreleasepreserva
          this.form.preservaamount = res.data.preservaamount
          this.form.preservanature = res.data.preservanature
          this.form.isreleasepreserva = res.data.isreleasepreserva
          this.form.id = res.data.id
        }
        this.dialogFormVisible = true
        this.form.litigationid = data.litigationid
        this.form.arbitraid = data.arbitraid
      },
      handleSsjd(val) {
        this.form.disputeid = val.disputeid
        this.$set(this.form, 'disputeItem', val.disputeitem)
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.form = {
          apper: '',
          responder: '',
          disputeItem: '',
          actionstage: '',
          isapppreserva: '',
          preservaamount: '',
          preservanature: '',
          isreleasepreserva: '',
        }
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        const res = await assetporotectSave(this.form)
        // 返回date...
        if (res && res.date) {
          this.$message({
            type: 'success',
            message: '保存成功！',
          })
          this.$emit('selected', res.date)
          this.$emit('fetch')
          this.close()
        } else {
          this.$message({
            type: 'error',
            message: '保存失败！',
          })
        }
      },
    },
  }
</script>

<style lang="less" scoped></style>
