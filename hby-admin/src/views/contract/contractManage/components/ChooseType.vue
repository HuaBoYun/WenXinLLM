<!--
 * @Date: 2022-04-11 17:35:02
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-20 16:11:54
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/ChooseType.vue
-->
<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="close"
  >
    <div>
      <ul class="level1">
        <li v-for="item in typeOptions" :key="item.value">
          <p class="title">{{ item.typename }}</p>
          <ul class="level2">
            <li
              v-for="child in item.childrenList"
              :key="child.value"
              @click="handleClick(child, item.typename)"
            >
              {{ child.typename }}
            </li>
          </ul>
        </li>
      </ul>
    </div>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
      <!-- <el-button type="primary" @click="confirm">确 定</el-button> -->
    </template>
  </el-dialog>
</template>

<script>
  import { getContractTypes } from '@/api/contract/manage'

  export default {
    name: 'ChooseType',
    data() {
      return {
        dialogVisible: false,
        typeOptions: [],
        title: '选择合同类型',
      }
    },
    created() {
      this.fetchTypes()
    },
    methods: {
      show() {
        this.dialogVisible = true
      },
      //请求类型数据
      async fetchTypes() {
        const res = await getContractTypes()
        this.typeOptions = res.typeofList
      },
      //点击方法
      handleClick(item, typename) {
        this.$emit('close')
        this.$emit('selected', item, typename)

        this.dialogVisible = false
      },

      close() {
        this.dialogVisible = false
      },
      confirm() {},
    },
  }
</script>
<style scoped>
  ul {
    padding-left: 0;
  }
  li {
    list-style: none;
    padding: 10px;
    cursor: pointer;
  }
  .title {
    border-left: 2px solid;
    padding-left: 5px;
    font-weight: bold;
    text-align: left;
    font-size: 16px;
    padding-left: 20px;
  }
  .level2 {
    display: flex;
    flex: space-between;
    flex-wrap: wrap;
    justify-content: between;
    margin: 10px;
  }
</style>
