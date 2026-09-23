<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
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
            :key="item.COLUMN_NAME"
            @click="selectData(item)"
            :class="activeKey.includes(item.COLUMN_NAME) ? 'active' : ''"
            :id="item.COLUMN_NAME"
          >
            {{ item.COLUMN_NAME }}
            <img src="@/assets/dataBase.png" alt="" />
          </li>
        </ul>
      </div>

      <div class="right_box">
        <el-input
          type="textarea"
          :rows="25"
          :placeholder="SQL"
          v-model="text"
        ></el-input>
      </div>
    </div>
    <div style="text-align: right; margin-top: 20px">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="handle">确定</el-button>
    </div>
  </el-dialog>
</template>
<script>
  import { getSQLData } from '@/oapi/setting/org'
  export default {
    name: '',
    props: ['SQL', 'bookid', 'dataBase'],
    data() {
      return {
        text: '',
        activeKey: [],
        select: [],
        list: [],
        title: '选择库表',
        dialogFormVisible: false,
        selectName: '',
        lastItem: '',
      }
    },
    created() {},
    methods: {
      getSQL() {
        getSQLData({
          bookid: this.bookid,
          table: this.dataBase,
        }).then((res) => {
          this.list = res.data
        })
      },
      show() {
        this.dialogFormVisible = true
        this.getSQL()
      },

      close() {
        this.dialogFormVisible = false
        this.text = ''
        this.list = []
      },
      selectData(item) {
        if (this.activeKey.includes(item.COLUMN_NAME)) {
          this.activeKey.splice(this.activeKey.indexOf(item.COLUMN_NAME), 1)
          this.select.splice(this.select.indexOf(item), 1)
        } else {
          this.activeKey.push(item.COLUMN_NAME)
          this.select.push(item)
        }
        const info = this.select.map((item) => item.COLUMN_NAME).toString()
        this.text = this.SQL.replace('*', info)
      },
      handle() {
        const info = this.select.map((item) => item.COLUMN_NAME).toString()
        this.$emit('handle', this.text, info)
        this.close()
      },
      changeSelect(item) {
        let toElement = document.getElementById(item)
        let toElementLast = document.getElementById(this.lastItem)
        const info = this.list.map((res) => res.COLUMN_NAME)
        if (info.includes(item)) {
          toElement.setAttribute('class', 'aa')
          this.lastItem = item
        } else {
          toElementLast.removeAttribute('class', 'aa')
        }

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
    width: 30%;
    height: 500px;
    border: 1px solid #ccc;
    line-height: 22px;
    display: block;
    overflow-y: scroll;
    box-shadow: 0 2px 5px 0 rgb(0 12 98 / 10%);
    border-radius: 5px;
    position: relative;
  }
  .right_box {
    flex: 1;
    margin-left: 10px;
    line-height: 22px;
    box-shadow: 0 2px 5px 0 rgb(0 12 98 / 10%);
    border-radius: 5px;
  }
  .search {
    position: sticky;
    width: 100%;
    top: 0;
  }
  ul {
    margin-top: 20px;
  }
  li {
    max-width: 150px;
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
s
