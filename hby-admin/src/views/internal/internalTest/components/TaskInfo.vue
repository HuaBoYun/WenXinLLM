<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-form ref="form" label-width="120px" :model="form">
      <el-col :span="12">
        <el-form-item label="编号" prop="elementcode">
          <span>{{ form.elementcode }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制方法" prop="controlmethod">
          <span>{{ form.controlmethod }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制类型" prop="controltype">
          <span>{{ form.controltype }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制频率" prop="controlreq">
          <span>{{ form.controlreq }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="业务描述" prop="businessdesc">
          <span>{{ form.businessdesc }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="风险描述" prop="risktype">
          <span>{{ form.risktype }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="控制目标" prop="controltarget">
          <span>{{ form.controltarget }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="控制措施" prop="controlmeasures">
          <span>{{ form.controlmeasures }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="检查方法" prop="checkmethod">
          <span>{{ form.checkmethod }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="所需资料" prop="material">
          <span>{{ form.material }}</span>
        </el-form-item>
      </el-col>
    </el-form>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { elemendatail } from '@/api/internal/tack'
  export default {
    name: 'TaskInfo',
    data() {
      return {
        title: '详细',
        dialogFormVisible: false,
        form: {
          businessdesc: '',
          checkmethod: '',
          controlmeasures: '',
          controlmethod: '',
          controlreq: '',
          controltarget: '',
          controltype: '',
          createtime: '',
          elementcode: '',
          elementid: '',
          material: '',
          risktype: '',
          templid: '',
          typeid: '',
        },
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        // console.log('data,row', row)
        this.getInfo(row.ELEMENTID)
        this.dialogFormVisible = true
      },
      async getInfo(id) {
        const { code, data, msg } = await elemendatail({ elementId: id })
        if (code == 200) {
          this.form = data.element
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style lang="scss" scoped>
  .box_row {
    margin-bottom: 20px;
  }
  .flex {
    display: flex;
    justify-content: flex-end;
  }
</style>
