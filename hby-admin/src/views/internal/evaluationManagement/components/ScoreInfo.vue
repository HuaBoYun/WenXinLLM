<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="15">
      <el-form ref="elForm" label-width="125px" :model="formData" size="medium">
        <el-col :span="24">
          <el-form-item label="要素编号">
            <span>{{ formData.elementnumber }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="要素名称">
            <span>{{ formData.elementname }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="业务类别">
            <span>{{ formData.businesstype }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审查要点">
            <span>
              {{ formData.auditpoint }}
            </span>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="评分规则">
            <span>{{ formData.assessrules }}</span>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>

    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getDefBasicModifyt } from '@/api/internal/score'
  export default {
    name: 'ScoreInfo',
    data() {
      return {
        formData: {
          auditpoint: undefined,
          businesstype: undefined,
          elementname: undefined,
          elementnumber: undefined,
          assessrules: undefined,
        },
        title: '要素详情',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.getInfo(row.asseleid)
        this.dialogFormVisible = true
      },
      async getInfo(basicId) {
        const { code, msg, data } = await getDefBasicModifyt({ basicId })
        if (code === 200) {
          this.formData = data.assesselement
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
      },
      close() {
        this.formData = {
          auditpoint: undefined,
          businesstype: undefined,
          elementname: undefined,
          elementnumber: undefined,
          assessrules: undefined,
        }
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
