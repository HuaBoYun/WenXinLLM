<template>
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
</template>

<script>
  import { getContractTypes } from '@/api/contract/manage'
  import { mapActions } from 'vuex'

  export default {
    name: 'ChooseType',
    data() {
      return {
        typeOptions: [],
        title: '选择合同类型',
      }
    },
    created() {
      this.fetchTypes()
    },
    methods: {
      ...mapActions({
        delVisitedRoute: 'tabs/delVisitedRoute',
      }),
      //请求数据
      async fetchTypes() {
        const res = await getContractTypes()
        this.typeOptions = res.typeofList
      },
      //点击
      async handleClick(item, typename) {
        this.$router.push({
          path: '/contractManage/createEdit',
          query: { item, typename },
        })
        await this.delVisitedRoute('/contractManage/chooseType')
      },
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
