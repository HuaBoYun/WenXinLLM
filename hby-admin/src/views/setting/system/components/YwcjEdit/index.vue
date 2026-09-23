<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-col :span="24">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="基本信息" name="first">
            <BasicDetail
              v-if="title == '查看'"
              ref="basic-detail"
              :action="title"
              :fatherflowid="fatherflowid"
            />
            <BasicForm
              v-else
              ref="basic-form"
              :action="title"
              :fatherflowid="fatherflowid"
            />
          </el-tab-pane>
          <el-tab-pane label="流程图" name="second">
            <workflow />
          </el-tab-pane>
          <el-tab-pane label="流程描述" name="third">
            <FlowDescription />
          </el-tab-pane>
          <el-tab-pane label="内外规" name="fourth">
            <el-col :span="24">
              <el-divider>内规</el-divider>
              <InternalControl ref="internal" :cur-row="curRow" />
            </el-col>
            <el-col :span="24">
              <el-divider>外规</el-divider>
              <ExternalControl ref="external" :cur-row="curRow" />
            </el-col>
          </el-tab-pane>
          <el-tab-pane label="工作流编辑" name="fifth">
            <BpmnModeler />
          </el-tab-pane>
          <el-tab-pane label="自定义表单" name="sixth">
            <CustomList />
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
  import BpmnModeler from '@/components/bpmnjs/BpmnModeler'
  import BasicDetail from './BasicDetail.vue'
  import BasicForm from './BasicForm.vue'
  import CustomList from './CustomList.vue'
  import ExternalControl from './ExternalControl'
  import FlowDescription from './FlowDescription.vue'
  import InternalControl from './InternalControl.vue'
  import workflow from './workflow'

  export default {
    name: 'YwcjEdit',
    components: {
      BpmnModeler,
      workflow,
      CustomList,
      BasicForm,
      BasicDetail,
      FlowDescription,
      InternalControl,
      ExternalControl,
    },
    props: {
      curRow: {
        type: Object,
        default: () => {},
      },
      fatherflowid: {
        type: [Number, String],
        default: undefined,
      },
    },
    data() {
      return {
        activeName: 'first',
        title: '',
        dialogFormVisible: false,
        internalList: [],
        externalList: [],
      }
    },
    created() {
      // this.fetchData()
    },
    methods: {
      async handleClick(tab) {
        const { name } = tab
        if (name == 'fourth') {
          this.$refs['internal'].fetchData()
          this.$refs['external'].fetchData()
        }
      },
      showDetail(row) {
        this.title = '查看'
        this.$nextTick(() => {
          this.$refs['basic-detail'].fetchDetail(row)
        })
        this.dialogFormVisible = true
      },
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          this.$nextTick(() => {
            this.$refs['basic-form'].fetchDetail(row)
          })
        }
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped>
  .mt-20 {
    margin-bottom: 20px;
  }
</style>
