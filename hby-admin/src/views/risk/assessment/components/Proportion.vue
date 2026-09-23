<template>
  <div>
    <el-dialog
      title="评价项目跟踪 - 任务管理"
      :visible.sync="dialogFormVisible"
      width="1000px"
      :modal="false"
      @close="close"
      :close-on-press-escape="false"
      :close-on-click-modal="false"
      v-if="dialogFormVisible"
    >
      <el-table :data="list">
        <el-table-column align="center" label="参评人ID" prop="staff.staffid" />
        <el-table-column
          align="center"
          label="参评人名称"
          prop="staff.realname"
        />
        <el-table-column align="center" label="权重" prop="assweight">
          <template #default="{ $index }">
            <el-input v-model="list[$index].assweight">
              <template slot="append">%</template>
            </el-input>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import { estimateUser, saveProportion } from '@/api/systemLog'
  export default {
    name: 'HbAdminProportion',

    data() {
      return {
        dialogFormVisible: false,
        list: [],
        socreList: [],
      }
    },

    mounted() {},

    methods: {
       /**
       * @description: 关闭页面
       * @return {*}
       */ 
      close() {
        this.dialogFormVisible = false
      },
       /**
       * @description: 保存权重
       * @return {*}
       */ 
      save() {
        const totalScore = this.list.reduce((cur, pre, index) => {
          return +cur + +pre.assweight
        }, 0)
        if (totalScore !== 100) {
          this.$baseMessage('权重比例不对', 'error', 'vab-hey-message-error')
          return
        }
        const ids = this.list.map((res) => res.markingid).toString()
        const values = this.list.map((res) => res.assweight).toString()
        saveProportion({
          ids,
          values,
        }).then((res) => {
          if (res.code == 1) {
            this.$baseMessage('设置成功', 'success', 'vab-hey-message-success')
            this.$emit('setProportion')
          }
          this.dialogFormVisible = false
        })
      },
       /**
       * @description: 初始化，获取数据
       * @return {*}
       */ 
      async showEdit(row) {
        this.dialogFormVisible = true
        const {
          data: { list },
        } = await estimateUser({ assriskid: row.assriskid })
        this.list = list
        this.socreList = list
      },
    },
  }
</script>

<style lang="scss" scoped></style>
