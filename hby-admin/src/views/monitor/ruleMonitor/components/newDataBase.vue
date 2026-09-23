<template>
  <div class="content">
    <div class="left_box">
      <el-input
        v-model="selectName"
        prefix-icon="el-icon-search"
        @input="changeSelect"
        class="search"
      ></el-input>
      <ul>
        <li
          v-for="item in list"
          :key="item.TABLE_NAME"
          @click="selectData(item)"
          :id="item.TABLE_NAME"
          :class="activeKey == item.TABLE_NAME ? 'active' : ''"
        >
          {{ item.TABLE_NAME }}
          <img src="@/assets/dataBase.png" alt="" />
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
  import { getDataBase } from '@/api/monitor/rule/index'
  export default {
    props: ['bookid'],
    name: '',
    data() {
      return {
        text: '',
        activeKey: '',
        list: [],
        title: '选择库表',
        dialogFormVisible: false,
        selectName: '',
      }
    },
    created() {},

    methods: {
      getDataBaseData(id) {
        getDataBase({ bookid: this.bookid || id }).then((res) => {
          this.list = res.data
        })
      },
      show() {
        this.dialogFormVisible = true
        this.getDataBaseData()
      },

      close() {
        this.dialogFormVisible = false
        this.text = ''
        this.list = []
      },
      selectData(item) {
        this.activeKey = item.TABLE_NAME
        this.text = item.TABLE_NAME
        this.$emit('handle', this.text, this.activeKey)
      },
      // handle() {
      //   this.close()
      // },
      changeSelect(item) {
        let toElement = document.getElementById(item)
        let toElementLast = document.getElementById(this.lastItem)
        const info = this.list.map((res) => res.TABLE_NAME)
        if (info.includes(item)) {
          toElement.setAttribute('class', 'aa')
          this.lastItem = item
        } else {
          toElementLast.removeAttribute('class', 'aa')
        }
        // this.activeKey = item
        toElement.scrollIntoView(false)
        toElement.scrollIntoView({ block: 'center' })
      },
    },
  }
</script>
<style scoped lang="scss">
  .content {
    display: flex;
  }
  .left_box {
    width: 80%;
    height: 300px;
    border: 1px solid #ccc;
    line-height: 22px;
    display: inline-block;
    overflow-y: scroll;
    box-shadow: 0 2px 5px 0 rgb(0 12 98 / 10%);
    border-radius: 5px;
    position: relative;
  }
  .search {
    position: sticky;
    width: 100%;
    top: 0;
  }
  .right_box {
    flex: 1;
    margin-left: 10px;
    line-height: 22px;
    box-shadow: 0 2px 5px 0 rgb(0 12 98 / 10%);
    border-radius: 5px;
  }
  ul {
    margin-top: 20px;
  }
  li {
    max-width: 200px;
    display: flex;
    justify-content: space-between;
    padding: 5px 15px;
    border-radius: 5px;
    &:hover {
      cursor: pointer;
      background-color: #eaf4fe;
    }
  }
  img {
    width: 15px;
    height: 15px;
  }
  .active {
    background-color: #eaf4fe;
    color: red;
  }
  .aa {
    background-color: #eaf4fe;
  }
</style>
