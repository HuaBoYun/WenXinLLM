<template>
  <el-dialog
    title="模板"
    :visible.sync="dialogVisible"
    width="600px"
    :append-to-body="true"
    :close-on-click-modal="false"
  >
    <div class="system-log-container" v-loading="loading">
      <div class="temp" v-for="(item, index) in list" :key="index">
        <div>{{ item.title }}</div>
        <div>
          <el-popconfirm title="确认删除模板？" @confirm="deleteTemp(item)">
            <el-button
              slot="reference"
              type="danger"
              icon="el-icon-delete"
              circle
            ></el-button>
          </el-popconfirm>
        </div>
        <div>
          <el-button type="primary" @click="selectTemp(item)">
            选择模板
          </el-button>
        </div>
      </div>
    </div>
  </el-dialog>
</template>
<script>
  import { getTempList, delTemp } from '@/api/setting/ueditor'
  export default {
    name: 'UEditorTemplate',
    data() {
      return {
        dialogVisible: false,
        tempType: '',
        list: [],
        loading: false,
      }
    },
    methods: {
      showDialog(type) {
        this.tempType = type
        this.getList()
        this.dialogVisible = true
      },
      async getList() {
        this.loading = true
        const { data } = await getTempList({ tempType: this.tempType })
        if (data.tempList) {
          this.list = data.tempList
        } else {
          this.list = []
        }
        this.loading = false
      },
      async deleteTemp(item) {
        const { msg } = await delTemp({ tempId: item.tempId })
        this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        await this.getList()
      },
      async selectTemp(item) {
        this.$emit('selectTemp', item.html)

        this.dialogVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 70px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
  .temp {
    display: flex;
    justify-content: space-between;
    padding: 5px;
    margin-bottom: 10px;
    background: gainsboro;
    align-items: center;
    div:first-child {
      width: 300px;
      font-size: 16px;
      font-weight: 600;
    }
  }
</style>
